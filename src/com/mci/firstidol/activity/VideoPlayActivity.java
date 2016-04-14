package com.mci.firstidol.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnTimedTextListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.media.TimedText;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.android.tedcoder.wkvideoplayer.dlna.engine.DLNAContainer;
import com.android.tedcoder.wkvideoplayer.dlna.service.DLNAService;
import com.android.tedcoder.wkvideoplayer.model.Video;
import com.android.tedcoder.wkvideoplayer.model.VideoUrl;
import com.android.tedcoder.wkvideoplayer.util.DensityUtil;
import com.android.tedcoder.wkvideoplayer.view.MediaController;
import com.android.tedcoder.wkvideoplayer.view.SuperVideoPlayer;
import com.mci.firstidol.R;
import com.mci.firstidol.adapter.SquareFoundDetailListViewAdapter;
import com.mci.firstidol.adapter.SquareFoundDetailListViewAdapter.SquareFoundDetailListViewAdapterListener;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.CommentModel;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.SquareRequest.CommentAction;
import com.mci.firstidol.model.SquareRequest.SquareFoundDetaiCommentLikelRequest;
import com.mci.firstidol.model.StarVideoModel;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.AsyncTaskUtils;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.AlertDialog;
import com.mci.firstidol.view.PopAnimView;
import com.mci.firstidol.view.PopAnimView.PopupAnimViewListener;
import com.mci.firstidol.view.RoundedImageView;
import com.mci.firstidol.view.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class VideoPlayActivity extends BaseActivity implements OnClickListener,
		OnSeekBarChangeListener, Callback, OnBufferingUpdateListener,
		OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener,
		OnTimedTextListener, SquareFoundDetailListViewAdapterListener{

	private final int SHOWCONTENT = 0;// 显示内容
	private final int UPDATE_NOWTIME = 1;// 更新时间

	// 视频播放相关
	private SurfaceView surfaceView;// 视频播放的surfaceView
	private SurfaceHolder mHolder;

	private MediaPlayer mMediaPlayer;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	private boolean mShouldAutoStart = true;
	private int mSurfaceViewWidth;
	private int mSurfaceViewHeight;

	private int videoHeight;
	private int videoWidth;

	private MediaPlayer mePlayer;// 正在播放的视频播放器
	//private LinearLayout footer;//底部标题

	// 视频进度条相关
	private RelativeLayout layout_control;// 控制布局
	private TextView text_time;// 时间
	private ImageView image_play;// 播放按钮
	private ImageView image_full;// 全屏按钮
	private SeekBar seek_play;// 播放进度条
	private int allTime;// 总共的时间
	private int nowTime;// 现在的时间

	public Timer playTimer;
	public TimerTask playTimeTask;

	private LinearLayout layout_loading;// 圈圈页面
	private LinearLayout layout_error;// 错误页面
	private ImageView image_error;// 错误icon
	private ImageButton image_report;//举报图标
	private long articleID;

	private RelativeLayout layout_video;// 视频布局
	private RelativeLayout layout_main;// 主页面UI
	private LinearLayout layout_content;// 主内容页面
	private LinearLayout layout_bottom;//底部页面
	private ImageView video_image, play_bg;// 视频替代图片

	private PopAnimView layout_mengmeng;// 萌萌哒布局
	private PopAnimView layout_kiss;// 舔一下布局
	private PopAnimView layout_shuai;// 帅炸了布局
	private PopAnimView layout_marry;// 嫁给他布局
	private Button btn_comment;// 评论布局

	
	
	
	private ImageButton btn_back_video;//底部返回
	private ImageButton btn_collection_video;//底部收藏
	private ImageButton btn_share_video;//底部分享
	private ImageButton btn_reply_video;//底部评论
	
	
	private RoundedImageView star_icon;// 明星头像
	private TextView text_name;// 明星名字
	private TextView text_info;// 明星内容

	// 评论相关
	private ListView list_comment;
	private SquareFoundDetailListViewAdapter commentAdapter;
	private ArrayList<CommentModel> commentList;
	private boolean onlyComment = false;// 是否只是更新评论

	private static StarVideoModel starVideoModel;// 追星视频对象
	private String video_url;// 视频播放地址
	private boolean isFullScreen = false;// 是否是全屏
	private boolean isVideoPlay = false;// 视频是否在播放
	private boolean isVideoPause = false;// 视频是否暂停
	private String article_id;// 文章ID

	private SuperVideoPlayer mSuperVideoPlayer;
	private View mPlayBtnView;

	private int videoPlayPosition;
	private boolean isPlaying;

	private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {
		@Override
		public void onCloseVideo() {
			mSuperVideoPlayer.close();
			mPlayBtnView.setVisibility(View.VISIBLE);
			mSuperVideoPlayer.setVisibility(View.GONE);
			resetPageToPortrait();
		}

		@Override
		public void onSwitchPageType() {
			if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {// 横屏
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);

				layout_content.setVisibility(View.VISIBLE);
				//footer.setVisibility(View.VISIBLE);
				//layout_bottom.setVisibility(View.VISIBLE);
				
				//showNavBar();

			} else {// 竖屏
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				mSuperVideoPlayer.setPageType(MediaController.PageType.EXPAND);

				layout_content.setVisibility(View.GONE);
				//layout_bottom.setVisibility(View.GONE);
				
				//footer.setVisibility(View.GONE);
				//hideNavBar();
			}
		}

		@Override
		public void onPlayFinish() {

		}
	};

	
	
	
	@Override
	protected int getViewId() {
		Intent intent = getIntent();
		if (intent != null) {
			article_id = intent.getStringExtra(Constant.IntentKey.articleID);
		}
		return R.layout.activity_video;
	}

	@Override
	protected void initNavBar() {
		hideNavBar();
		setTitle(R.string.video_play);
	}

	@Override
	protected void initView() {

		// 注册广播
		registerReceiver(mHomeKeyEventReceiver, new IntentFilter(
				Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

		layout_video = (RelativeLayout) findViewById(R.id.layout_video);
		video_image = (ImageView) findViewById(R.id.image_video);
		play_bg = (ImageView) findViewById(R.id.play_bg);
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		image_report = (ImageButton) findViewById(R.id.image_report);

		star_icon = (RoundedImageView) findViewById(R.id.star_icon);
		text_name = (TextView) findViewById(R.id.text_name);
		text_info = (TextView) findViewById(R.id.text_info);

		list_comment = (ListView) findViewById(R.id.comment_list);
		
		
		//footer = (LinearLayout) findViewById(R.id.footer);
		//底部关联
		btn_back_video = (ImageButton) findViewById(R.id.btn_back_video);
		btn_collection_video = (ImageButton) findViewById(R.id.btn_collection_video);
		btn_share_video = (ImageButton) findViewById(R.id.btn_share_video);
		btn_reply_video = (ImageButton) findViewById(R.id.btn_reply_video);
		
		
		
		layout_error = (LinearLayout) findViewById(R.id.layout_error);
		layout_loading = (LinearLayout) findViewById(R.id.loading);
		image_error = (ImageView) findViewById(R.id.image_error);
		layout_main = (RelativeLayout) findViewById(R.id.layout_main);
		layout_content = (LinearLayout) findViewById(R.id.layout_content);
		//layout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);

		layout_mengmeng = (PopAnimView) findViewById(R.id.view_meng);
		layout_shuai = (PopAnimView) findViewById(R.id.view_shuai);
		layout_kiss = (PopAnimView) findViewById(R.id.view_tian);
		layout_marry = (PopAnimView) findViewById(R.id.view_jia);
		btn_comment = (Button) findViewById(R.id.btn_comment);
		initPopAnimView();

		layout_control = (RelativeLayout) findViewById(R.id.layout_control);
		text_time = (TextView) findViewById(R.id.text_time);
		image_play = (ImageView) findViewById(R.id.image_play);
		image_full = (ImageView) findViewById(R.id.image_full);
		seek_play = (SeekBar) findViewById(R.id.play_sb);

		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mHolder = surfaceView.getHolder();
		mHolder.addCallback(this);
		mHolder.setKeepScreenOn(true);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		btn_back_video.setOnClickListener(this);
		btn_collection_video.setOnClickListener(this);
		btn_share_video.setOnClickListener(this);
		btn_reply_video.setOnClickListener(this);
		
		
		image_error.setOnClickListener(this);
		btn_comment.setOnClickListener(this);
		image_report.setOnClickListener(this);
		getVideoInfo();

		image_play.setOnClickListener(this);
		image_full.setOnClickListener(this);
		surfaceView.setOnClickListener(this);

		layout_mengmeng.setPopupAnimViewListener(new PopupAnimViewListener() {

			@Override
			public void popupAnimViewClick(View v) {
				starPopupAnimViewClick(0);
			}
		});

		layout_kiss.setPopupAnimViewListener(new PopupAnimViewListener() {

			@Override
			public void popupAnimViewClick(View v) {
				starPopupAnimViewClick(1);
			}
		});

		layout_shuai.setPopupAnimViewListener(new PopupAnimViewListener() {

			@Override
			public void popupAnimViewClick(View v) {
				starPopupAnimViewClick(2);
			}
		});

		layout_marry.setPopupAnimViewListener(new PopupAnimViewListener() {

			@Override
			public void popupAnimViewClick(View v) {
				starPopupAnimViewClick(3);
			}
		});

		mSuperVideoPlayer = (SuperVideoPlayer) findViewById(R.id.video_player_item_1);
		mPlayBtnView = findViewById(R.id.play_btn);
		mPlayBtnView.setOnClickListener(this);
		mSuperVideoPlayer.setVideoPlayCallback(mVideoPlayCallback);
		startDLNAService();

	}

	/**
	 * 初始化动画
	 */
	private void initPopAnimView() {
		layout_mengmeng.setStaticImage(R.drawable.a0);
		layout_mengmeng.setAnimId(R.anim.anim_meng);
		layout_mengmeng.setDrawableAnimId(R.anim.anim_meng_drawable);
		layout_mengmeng.setTitle(context.getResources().getString(
				R.string.anim_meng));

		layout_kiss.setStaticImage(R.drawable.b0);
		layout_kiss.setAnimId(R.anim.anim_meng);
		layout_kiss.setDrawableAnimId(R.anim.anim_tian_drawable);
		layout_kiss.setTitle(context.getResources().getString(
				R.string.anim_tian));

		layout_shuai.setStaticImage(R.drawable.c0);
		layout_shuai.setAnimId(R.anim.anim_meng);
		layout_shuai.setDrawableAnimId(R.anim.anim_shuai_drawable);
		layout_shuai.setTitle(context.getResources().getString(
				R.string.anim_shuai));

		layout_marry.setStaticImage(R.drawable.d0);
		layout_marry.setAnimId(R.anim.anim_meng);
		layout_marry.setDrawableAnimId(R.anim.anim_jia_drawable);
		layout_marry.setTitle(context.getResources().getString(
				R.string.anim_jia));
	}

	/**
	 * 得到视频信息
	 */
	public void getVideoInfo() {
		getVideoDetailRequest();
		showLoadingView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_error:
			getVideoInfo();
			break;
		case R.id.btn_comment:
			starPopupAnimViewClick(4);
			break;
		case R.id.image_play:
			if (isVideoPlay) {
				if (isVideoPause) {
					isVideoPause = false;
					if (mMediaPlayer != null) {
						mMediaPlayer.pause();
					}
					if (playTimer != null) {
						playTimer.cancel();
					}
				} else {
					isVideoPause = true;
					mMediaPlayer.start();
					timerNowTime();
				}
			} else {
				playVideo(video_url);
			}

			break;
		case R.id.image_full:
			fullScreenClick();
			break;
		case R.id.surfaceView:
			if (View.GONE == layout_control.getVisibility()) {
				layout_control.setVisibility(View.VISIBLE);
				layout_control.postDelayed(new Runnable() {

					@Override
					public void run() {
						layout_control.setVisibility(View.GONE);

					}
				}, 6000);
			}
			break;
		case R.id.play_btn:
			//startVideoPlay();
			startVideo();
			break;
		case R.id.image_report:
			//showrEport();
			Bundle bundle = new Bundle();
        	bundle.putString("ModType", "1");
        	bundle.putLong("RefId", starVideoModel.ArticleId);
    		Utily.go2Activity(VideoPlayActivity.this, ReportActivity.class,
    				bundle);
			break;
			
		case R.id.btn_back_video:
			finish();
			
			break;
			
		case R.id.btn_collection_video:
			//Toast.makeText(getApplicationContext(), "你点击了收藏", Toast.LENGTH_SHORT).show();
//			ToastUtils.showPointToast("5");
			obtainFoundDetailCommentFavRequest(((ImageButton) v).isSelected());

			break;
			
			
		case R.id.btn_share_video:
			//Toast.makeText(getApplicationContext(), "你点击了分享", Toast.LENGTH_SHORT).show();
			oneKeyShareDetail();
			break;
			
			
		case R.id.btn_reply_video:
			//Toast.makeText(getApplicationContext(), "你点击了评论", Toast.LENGTH_SHORT).show();
			starPopupAnimViewClick(4);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 广场-发现详情-收藏
	 * 
	 * @param isCancelFav
	 */
	public void obtainFoundDetailCommentFavRequest(final boolean isSelected) {
		BaseRequest request = new BaseRequest();
		// 请求地址
		String requestID = String
				.format((isSelected ? Constant.RequestContstants.Request_Article_CancelStore
						: Constant.RequestContstants.Request_Article_Store),
						articleID);
		doAsync(false, requestID, request,
				new AsyncTaskUtils.Callback<Exception>() {

					@Override
					public void onCallback(Exception e) {
						// TODO Auto-generated method stub
						//ToastUtils.showCustomToast(e.getLocalizedMessage());
						ToastUtils.showCustomToast(e.getLocalizedMessage());
					}
				}, new AsyncTaskUtils.Callback<String>() {

					@Override
					public void onCallback(String result) {
						/*ToastUtils.showCustomToast("点赞成功");
						starVideoModel.HasStore = true;*/
						btn_collection_video.setSelected(!btn_collection_video.isSelected());
					}
				});
	}
	
	
	private void oneKeyShareDetail() {
		// TODO Auto-generated method stub
		if (!dataManager.isLogin) {
			Utily.go2Activity(context, LoginActivity.class);
			return;
		}

		if (starVideoModel != null) {
			showShare(VideoPlayActivity.this, null, true , video_url);
		}

	}
	
	/**
	 * 调用ShareSDK执行分享
	 * 
	 * @param context
	 * @param platformToShare
	 *            指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
	 * @param showContentEdit
	 *            是否显示编辑页
	 */
	public static void showShare(Context context, String platformToShare,
			boolean showContentEdit ,String video_url) {
		OnekeyShare oks = new OnekeyShare();
		oks.setSilent(!showContentEdit);
		if (platformToShare != null) {
			oks.setPlatform(platformToShare);
		}
		// ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC 第二个是SKYBLUE
		oks.setTheme(OnekeyShareTheme.CLASSIC);
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		// 在自动授权时可以禁用SSO方式
		oks.disableSSOWhenAuthorize();
		// oks.setAddress("12345678901"); //分享短信的号码和邮件的地址
		oks.setTitle(starVideoModel.getTitle());
		Log.v("TextVii", "值为1+"+starVideoModel.getTitle());
		String titleUrl = Constant.RequestContstants.URL
				+ String.format(
						Constant.RequestContstants.Request_Article_Share,
						starVideoModel.getArticleId());
		//String titleUrl = String.format(video_url);
		oks.setTitleUrl(titleUrl);
		//Toast.makeText(context, "值为+"+titleUrl, Toast.LENGTH_SHORT).show();
		oks.setText(starVideoModel.getTitle());
		oks.setImagePath(ImageLoader.getInstance().getDiscCache()
				.get(starVideoModel.getIco()).getPath()); // 分享sdcard目录下的图片
		/*String imageUrl = starVideoModel.getModelType() == 2 ? starVideoModel
				.getVideoIco() : starVideoModel.getVideoIco();*/
		String imageUrl = starVideoModel.getVideoIco();
		//Toast.makeText(context, "值为1+"+imageUrl, Toast.LENGTH_SHORT).show();
		Log.v("TextVii", "值为1+"+imageUrl+"值为+"+titleUrl);
		oks.setImageUrl(imageUrl);
		oks.setUrl(titleUrl); // 微信不绕过审核分享链接
		// oks.setFilePath(ImageLoader.getInstance().getDiscCache().get(squareFoundModel.getIco()).getPath());
		// //filePath是待分享应用程序的本地路劲，仅在微信（易信）好友和Dropbox中使用，否则可以不提供
		oks.setComment("easy"); // 我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
		oks.setSite("Easy"); // QZone分享完之后返回应用时提示框上显示的名称
		oks.setSiteUrl(titleUrl);// QZone分享参数
		oks.setVenueName("Easy");
		oks.setVenueDescription("Easy is a beautiful place!");
/*		Bitmap enableLogo = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.ic_launcher);
		Bitmap disableLogo = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.ic_launcher);
		String label = "Easy";
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {

			}
		};*/
		oks.show(context);
	}
	
	/*
	视频播放
   */
	public void startVideo(){
		Uri uri = Uri.parse(starVideoModel.Ico);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "video/mp4");
		startActivity(intent);
	}
	
	
	private void showrEport(){
		AlertDialog.show(context, "", "确定要举报这条内容?", "确定", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "你举报了", Toast.LENGTH_SHORT).show();
			}
		}, "取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	
	/**
	 * 是否全屏按钮
	 * 
	 * @param view
	 */
	public void fullScreenClick() {
		if (isFullScreen) {
			((Activity) context)
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			isFullScreen = false;
			showNavBar();

		} else {
			((Activity) context)
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			isFullScreen = true;
			hideNavBar();
		}

	}

	/**
	 * 点赞事件
	 * 
	 * @param index
	 */
	public void starPopupAnimViewClick(int index) {
		if (index < 4) {
			if (!starVideoModel.HasStore) {
				obtainZanRequest(starVideoModel.ArticleId, index);
			} else {
				ToastUtils.showCustomToast(context, "已赞过");
			}
		} else {
			Bundle bundle = new Bundle();
			bundle.putLong(Constant.IntentKey.articleID,
					starVideoModel.ArticleId);
			Utily.go2Activity(context, SquareFoundDetailAddReplyActivity.class,
					bundle);
		}

	}

	// 文章点赞
	private void obtainZanRequest(long articleId, final int index) {
		// TODO Auto-generated method stub
		BaseRequest request = new BaseRequest();
		// 请求地址
		String requestID = String.format(
				Constant.RequestContstants.Request_Square_Zan, articleId,
				index + 1);
		doAsync(false, requestID, request,
				new AsyncTaskUtils.Callback<Exception>() {

					@Override
					public void onCallback(Exception e) {
						// TODO Auto-generated method stub
						ToastUtils.showCustomToast(e.getLocalizedMessage());
					}
				}, new AsyncTaskUtils.Callback<String>() {

					@Override
					public void onCallback(String result) {
						ToastUtils.showCustomToast("点赞成功");
						starVideoModel.HasStore = true;
					}
				});
	}

	/**
	 * 初始化评论数据
	 */
	public void initCommentData() {
		commentAdapter = new SquareFoundDetailListViewAdapter(context,
				commentList, true);
		list_comment.setAdapter(commentAdapter);
	}

	@Override
	public void squareFoundDetailCommentLikeClick(int position) {
		CommentModel commentModel = commentList.get(position);
		obtainFoundDetailCommentLikeRequest(commentModel);
	}

	/**
	 * 广场-发现详情-评论点赞
	 * 
	 * @param isCancelFav
	 */
	public void obtainFoundDetailCommentLikeRequest(
			final CommentModel commentModel) {
		SquareFoundDetaiCommentLikelRequest request = SquareRequest
				.squareFoundDetaiCommentLikelRequest();
		request.requestMethod = Constant.Request_POST;

		CommentAction commentAction = SquareRequest.commentAction();
		commentAction.ActType = "1";
		commentAction.ActName = "up";
		commentAction.CommentId = commentModel.getCommentId();
		commentAction.ActUserId = dataManager.isLogin ? (dataManager.userModel.UserId + "")
				: "-1";
		commentAction.CommentType = 0;

		request.action = commentAction;
		// 请求地址
		String requestID = Constant.RequestContstants.Request_Square_CommentActions_Add;
		doAsync(true, requestID, request,
				new AsyncTaskUtils.Callback<Exception>() {

					@Override
					public void onCallback(Exception e) {
						ToastUtils.showCustomToast(e.getLocalizedMessage());
					}
				}, new AsyncTaskUtils.Callback<String>() {

					@Override
					public void onCallback(String result) {
						// 成功时，更改图标状态
						commentModel.setComment(true);
						commentModel.setCommentUpCount((long) Float
								.parseFloat(result));
						commentAdapter.refershData(commentList);
					}
				});
	}

	@Override
	public void onEventMainThread(AnyEventType event) {
		if (event != null
				&& event.message.equals(Constant.Config.UPDATE_COMMENT_INFO)) {
			onlyComment = true;
			getVideoDetailRequest();
		}
	}

	private void playVideo(String path) {
		pd.setMessage("正在加载");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		if (path != null && !path.equals("")) {
			try {
				Uri uri = Uri.parse(path);
				mMediaPlayer.setDataSource(context, uri);
				mMediaPlayer.prepareAsync();
				mMediaPlayer.setOnBufferingUpdateListener(this);
				mMediaPlayer.setOnCompletionListener(this);
				mMediaPlayer.setOnPreparedListener(this);
				mMediaPlayer.setOnVideoSizeChangedListener(this);
				mMediaPlayer.setOnTimedTextListener(this);
			} catch (Exception e) {
				Log.e("yyw", e.toString());
				ToastUtils.showCustomToast(context, "视频无法播放");
				baseHandler.sendEmptyMessage(HIDEDIALOG);
				exitThis();
			}
		} else {
			ToastUtils.showCustomToast(context, "视频无法播放");
			baseHandler.sendEmptyMessage(HIDEDIALOG);
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SHOWCONTENT:
				initDataView();
				break;
			case UPDATE_NOWTIME:
				text_time.setText(Utily.getTimeText(nowTime) + "/"
						+ Utily.getTimeText(allTime));
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 初始化页面信息
	 */
	public void initDataView() {
		initData();
		showContentView();
	}

	@Override
	public void rightNavClick() {

	}

	@Override
	protected void findViewById() {

	}

	@Override
	protected void initData() {

		if (starVideoModel != null) {
			if (onlyComment) {
				commentList = starVideoModel.HotComments;
				initCommentData();
				onlyComment = false;
			} else {
				text_name.setText(starVideoModel.UserNickName);
				text_info.setText(starVideoModel.Title);
				articleID = starVideoModel.ArticleId;
				String url = starVideoModel.UserAvatar;
				String video_image_url = starVideoModel.VideoIco;
				loadImage(url, star_icon);

				ImageLoader.getInstance().displayImage(video_image_url,
						video_image);
				ImageLoader.getInstance()
						.displayImage(video_image_url, play_bg);

				video_url = starVideoModel.Ico;
				commentList = starVideoModel.HotComments;

				initCommentData();
				// playVideo(video_url);
			}
		}
	}

	/**
	 * 显示圈圈页
	 */
	public void showLoadingView() {
		layout_loading.setVisibility(View.VISIBLE);
		layout_main.setVisibility(View.GONE);
		layout_error.setVisibility(View.GONE);
	}

	/**
	 * 显示内容页
	 */
	public void showContentView() {
		layout_loading.setVisibility(View.GONE);
		layout_main.setVisibility(View.VISIBLE);
		layout_error.setVisibility(View.GONE);
	}

	/**
	 * 显示错误页
	 */
	public void showErrorView() {
		layout_loading.setVisibility(View.GONE);
		layout_main.setVisibility(View.GONE);
		layout_error.setVisibility(View.VISIBLE);
	}

	/**
	 * 得到视频详情请求
	 */
	public void getVideoDetailRequest() {
		String processURL = Constant.RequestContstants.Request_get_article
				+ "/" + article_id;
		if (onlyComment) {
			pd.setMessage("正在刷新");
			baseHandler.sendEmptyMessage(SHOWDIALOG);
		}

		ConnectionService.getInstance().serviceConnUseGet(context, processURL,
				null, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if (check) {
								starVideoModel = (StarVideoModel) parseJson
										.getModelObject(jsonObject,
												StarVideoModel.class);
								handler.sendEmptyMessage(SHOWCONTENT);
							} else {
								baseHandler.sendEmptyMessage(HIDEDIALOG);
								if (!onlyComment) {
									showErrorView();
								}
							}
						} catch (Exception e) {
							baseHandler.sendEmptyMessage(HIDEDIALOG);
							e.printStackTrace();
							if (!onlyComment) {
								showErrorView();
							}
						}
					}

					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						if (!onlyComment) {
							showErrorView();
						}
					}
				});
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mePlayer = mp;
		video_image.setVisibility(View.GONE);

		videoHeight = mp.getVideoHeight();
		videoWidth = mp.getVideoWidth();

		if (isFullScreen) {
			showLandscape(videoHeight, videoWidth);
		} else {
			showPortiart(videoHeight, videoWidth);
		}

		mp.start();

		baseHandler.sendEmptyMessage(HIDEDIALOG);

		allTime = mp.getDuration() / 1000;
		seek_play.setMax(mp.getDuration());
		seek_play.setProgress(0);
		String timeText = Utily.getTimeText(allTime);

		isVideoPause = true;
		text_time.setText("00:00/" + timeText);
		timerNowTime();
		image_play.setBackgroundResource(R.drawable.pause);

		baseHandler.sendEmptyMessage(HIDEDIALOG);
	}

	/**
	 * 显示竖屏
	 */
	public void showPortiart(int height, int width) {
		int windows_height = Utily.getHeight(context);
		int windows_width = Utily.getWidth(context);
		//footer.setVisibility(View.VISIBLE);
		mSurfaceViewHeight = Utily.dip2px(context, 200);

		float devide = (float) height / (float) mSurfaceViewHeight;
		mSurfaceViewWidth = (int) ((float) windows_width / devide);

		LayoutParams layoutParams = surfaceView.getLayoutParams();
		layoutParams.height = mSurfaceViewHeight;
		layoutParams.width = mSurfaceViewWidth;
		surfaceView.setLayoutParams(layoutParams);
	}

	/**
	 * 显示横屏
	 */
	public void showLandscape(int height, int width) {
		int windows_height = Utily.getHeight(context);
		int windows_width = Utily.getWidth(context);

		float devide = (float) windows_height / width;
		mSurfaceViewWidth = (int) ((float) windows_width / devide);

		LayoutParams layoutParams = surfaceView.getLayoutParams();
		layoutParams.height = windows_height;
		layoutParams.width = mSurfaceViewWidth;
		surfaceView.setLayoutParams(layoutParams);
	}

	@Override
	public void onTimedText(MediaPlayer mp, TimedText text) {
		text.getText();
		Log.e("yyw", text.getText());
	}

	/**
	 * 开始计时
	 */
	public void timerNowTime() {
		playTimer = new Timer();
		playTimeTask = new TimerTask() {

			@Override
			public void run() {
				if (isVideoPause) {
					if (seek_play != null) {
						seek_play.setProgress(nowTime * 1000);
					}
					nowTime++;
					handler.sendEmptyMessage(UPDATE_NOWTIME);
				}
			}
		};
		playTimer.schedule(playTimeTask, 1000, 1000);
	}

	// @Override
	// public void onConfigurationChanged(Configuration newConfig) {
	// super.onConfigurationChanged(newConfig);
	// Window window = getWindow();
	// if (this.getResources().getConfiguration().orientation ==
	// Configuration.ORIENTATION_LANDSCAPE) {
	//
	// window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	// window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	// WindowManager.LayoutParams.FLAG_FULLSCREEN);
	// RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
	// LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	// layout_video.setLayoutParams(params);
	//
	// } else if (this.getResources().getConfiguration().orientation ==
	// Configuration.ORIENTATION_PORTRAIT) {
	//
	// window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	// window.setFlags(
	// WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
	// WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	// RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
	// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	// param.width = Utily.getWidth(context);
	// param.height = Utily.dip2px(context, 200);
	// layout_video.setLayoutParams(param);
	// }
	// if (mePlayer != null) {
	// if (isFullScreen) {
	// showLandscape(videoHeight, videoWidth);
	// } else {
	// showPortiart(videoHeight, videoWidth);
	// }
	// }
	// super.onConfigurationChanged(newConfig);
	// }

	@Override
	public void onCompletion(MediaPlayer mp) {
		try {
			mp.stop();
			mp.release();
			isVideoPause = false;
			isVideoPlay = false;
			nowTime = 0;
			seek_play.setProgress(0);
			text_time.setText("00:00/" + Utily.getTimeText(allTime));
			image_play.setBackgroundResource(R.drawable.play);
			playTimer.cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		if (percent < 35) {
			String title = "正在加载" + String.valueOf(percent) + "%";
			pd.setMessage(title);
			if (!pd.isShowing()) {
				baseHandler.sendEmptyMessage(SHOWDIALOG);
			}
		} else {
			if (pd.isShowing()) {
				baseHandler.sendEmptyMessage(HIDEDIALOG);
			}
		}
	}

	@Override
	public void leftNavClick() {
		if (isFullScreen) {
			fullScreenClick();
		} else {
			if (isVideoPlay) {
				if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
					mMediaPlayer.stop();
					mMediaPlayer.release();
					mMediaPlayer = null;
				}
				if (playTimer != null) {
					playTimer.cancel();
				}
			}
			super.leftNavClick();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mHolder != null) {
			mMediaPlayer.setDisplay(mHolder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	// *********************************

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopDLNAService();
		// ToastUtils.showCustomToast("OnDestory");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// ToastUtils.showCustomToast("onResume");
	}

	/***
	 * 旋转屏幕之后回调
	 * 
	 * @param newConfig
	 *            newConfig
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// ToastUtils.showCustomToast("oriente");

		if (null == mSuperVideoPlayer)
			return;
		/***
		 * 根据屏幕方向重新设置播放器的大小
		 */
		//footer.setVisibility(View.GONE);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().getDecorView().invalidate();
			float height = DensityUtil.getWidthInPx(this);
			float width = DensityUtil.getHeightInPx(this);
			mSuperVideoPlayer.getLayoutParams().height = (int) width;
			mSuperVideoPlayer.getLayoutParams().width = (int) height;
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			final WindowManager.LayoutParams attrs = getWindow()
					.getAttributes();
			attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().setAttributes(attrs);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
			float width = DensityUtil.getWidthInPx(this);
			float height = DensityUtil.dip2px(this, 200.f);
			mSuperVideoPlayer.getLayoutParams().height = (int) height;
			mSuperVideoPlayer.getLayoutParams().width = (int) width;
		}
	}

	/***
	 * 恢复屏幕至竖屏
	 */
	private void resetPageToPortrait() {
		if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
		}

		layout_content.setVisibility(View.VISIBLE);
		//layout_bottom.setVisibility(View.VISIBLE);

		//footer.setVisibility(View.VISIBLE);
		showNavBar();
		//footer.setVisibility(View.VISIBLE);
		// if (getRequestedOrientation() ==
		// ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {//横屏
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
		//
		// layout_content.setVisibility(View.VISIBLE);
		// showNavBar();
		//
		// } else {//竖屏
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// mSuperVideoPlayer.setPageType(MediaController.PageType.EXPAND);
		//
		// layout_content.setVisibility(View.GONE);
		// hideNavBar();
		// }
	}

	private void startDLNAService() {
		// Clear the device container.
		DLNAContainer.getInstance().clear();
		Intent intent = new Intent(getApplicationContext(), DLNAService.class);
		startService(intent);
	}

	private void stopDLNAService() {
		Intent intent = new Intent(getApplicationContext(), DLNAService.class);
		stopService(intent);
	}

	private void startVideoPlay() {
		// TODO Auto-generated method stub

		isPlaying = true;
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

		mPlayBtnView.setVisibility(View.GONE);
		mSuperVideoPlayer.setVisibility(View.VISIBLE);
		mSuperVideoPlayer.setAutoHideController(true);

		Video video = new Video();
		VideoUrl videoUrl1 = new VideoUrl();
		videoUrl1.setFormatName("720P");
		videoUrl1.setFormatUrl(starVideoModel.Ico);
		ArrayList<VideoUrl> arrayList1 = new ArrayList<>();
		arrayList1.add(videoUrl1);
		video.setVideoName(starVideoModel.Title);
		video.setVideoUrl(arrayList1);
		ArrayList<Video> videoArrayList = new ArrayList<>();
		videoArrayList.add(video);

		mSuperVideoPlayer.loadMultipleVideo(videoArrayList, 0, 0, 0);
	}

	/**
	 * 监听是否点击了home键将客户端推到后台
	 */
	private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
		String SYSTEM_REASON = "reason";
		String SYSTEM_HOME_KEY = "homekey";
		String SYSTEM_HOME_KEY_LONG = "recentapps";

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				String reason = intent.getStringExtra(SYSTEM_REASON);
				if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
					// 表示按了home键,程序到了后台
					if (isPlaying) {
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
						videoPlayPosition = mSuperVideoPlayer
								.getSuperVideoView().getCurrentPosition();
						// Toast.makeText(getApplicationContext(),
						// "home:"+videoPlayPosition, 1).show();
						mPlayBtnView.setVisibility(View.VISIBLE);
						mSuperVideoPlayer.setVisibility(View.GONE);
						play_bg.setVisibility(View.VISIBLE);
					}

				} else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
					// 表示长按home键,显示最近使用的程序列表
				}
			}
		}
	};

}
