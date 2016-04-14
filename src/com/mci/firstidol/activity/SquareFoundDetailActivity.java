package com.mci.firstidol.activity;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.android.tedcoder.wkvideoplayer.dlna.engine.DLNAContainer;
import com.android.tedcoder.wkvideoplayer.dlna.service.DLNAService;
import com.android.tedcoder.wkvideoplayer.model.Video;
import com.android.tedcoder.wkvideoplayer.model.VideoUrl;
import com.android.tedcoder.wkvideoplayer.util.DensityUtil;
import com.android.tedcoder.wkvideoplayer.view.MediaController;
import com.android.tedcoder.wkvideoplayer.view.SuperVideoPlayer;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.mci.firstidol.adapter.SquareFoundDetailListViewAdapter;
import com.mci.firstidol.adapter.SquareFoundDetailListViewAdapter.SquareFoundDetailListViewAdapterListener;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.CommentModel;
import com.mci.firstidol.model.SquareModel.ArticlePicture;
import com.mci.firstidol.model.SquareModel.SquareFoundModel;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.SquareRequest.ArticleComment;
import com.mci.firstidol.model.SquareRequest.CommentAction;
import com.mci.firstidol.model.SquareRequest.SquareFoundDetaiCommentAddRequest;
import com.mci.firstidol.model.SquareRequest.SquareFoundDetaiCommentLikelRequest;
import com.mci.firstidol.utils.DateHelper;
import com.mci.firstidol.utils.DeviceUtils;
import com.mci.firstidol.utils.DisplayUtil;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.utils.LogUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.PopAnimView;
import com.mci.firstidol.view.ProgressActivity;
import com.mci.firstidol.view.ReplyPopupWindow;
import com.mci.firstidol.view.ToastUtils;
import com.mci.firstidol.view.PopAnimView.PopupAnimViewListener;
import com.mci.firstidol.view.ReplyPopupWindow.OnReplyClickListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SquareFoundDetailActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener,
		SquareFoundDetailListViewAdapterListener, PopupAnimViewListener,
		OnReplyClickListener {

	private PullToRefreshListView mPullRefreshListView;
	private ListView listView;
	private SquareFoundDetailListViewAdapter adapter;

	private ImageButton btn_back, btn_share, btn_reply, btn_collection;

	private View headerView;
	private ImageView iv_user_logo;
	private TextView tv_nickname, tv_time, tv_read, tv_share, tv_content;

	private long articleID;
	private static SquareFoundModel squareFoundModel;
	private ArrayList<CommentModel> commentModels;

	PopAnimView view_meng, view_tian, view_jia, view_shuai;
	private ReplyPopupWindow replyWindow;
	private String replyHint;
	private LinearLayout ll_quare_detail, ll_user_info, ll_picker_layout;
	private CommentModel selectCommentModel;
	private boolean isFirst;
	
	private boolean isVideo;

	private SuperVideoPlayer mSuperVideoPlayer;
    private View mPlayBtnView;
    private LinearLayout layout_content;
    private FrameLayout layout_video;
    private ImageView play_bg;
    
    private ProgressActivity progressActivity;
    
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
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {//横屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
                
                layout_content.setVisibility(View.VISIBLE);
//                showNavBar();
            } else {//竖屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mSuperVideoPlayer.setPageType(MediaController.PageType.EXPAND);
                
                layout_content.setVisibility(View.GONE);
//                hideNavBar();
            }
        }

        @Override
        public void onPlayFinish() {

        }
    };
    
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		ToastUtils.showCustomToast("onResume:"+mSuperVideoPlayer.getSuperVideoView().getCurrentPosition());
		if (!isFirst) {
			obtainFoundDetailCommentRequest(true);
		} else {
			isFirst = false;
		}
	}
	

	@Override
	protected void initNavBar() {
		// TODO Auto-generated method stub
		hideNavBar();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		progressActivity = (ProgressActivity) findViewById(R.id.progressActivity);
		progressActivity.showLoading();
		
		ll_quare_detail = (LinearLayout) findViewById(R.id.ll_quare_detail);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		listView = mPullRefreshListView.getRefreshableView();

		layout_content = (LinearLayout) findViewById(R.id.layout_content);
		layout_video = (FrameLayout) findViewById(R.id.layout_video);
		// listView.setOverScrollMode(View.OVER_SCROLL_NEVER);

		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_share = (ImageButton) findViewById(R.id.btn_share);
		btn_reply = (ImageButton) findViewById(R.id.btn_reply);
		btn_collection = (ImageButton) findViewById(R.id.btn_collection);

		headerView = LayoutInflater.from(this).inflate(
				R.layout.item_square_found_detail_header, null);
		ll_picker_layout = (LinearLayout) headerView
				.findViewById(R.id.ll_picker_layout);
		ll_user_info = (LinearLayout) headerView
				.findViewById(R.id.ll_user_info);
		iv_user_logo = (ImageView) headerView.findViewById(R.id.iv_user_logo);
		tv_nickname = (TextView) headerView.findViewById(R.id.tv_nickname);
		tv_time = (TextView) headerView.findViewById(R.id.tv_time);
		tv_read = (TextView) headerView.findViewById(R.id.tv_read);
		tv_share = (TextView) headerView.findViewById(R.id.tv_share);
		tv_content = (TextView) headerView.findViewById(R.id.tv_content);

		view_meng = (PopAnimView) headerView.findViewById(R.id.view_meng);
		view_tian = (PopAnimView) headerView.findViewById(R.id.view_tian);
		view_shuai = (PopAnimView) headerView.findViewById(R.id.view_shuai);
		view_jia = (PopAnimView) headerView.findViewById(R.id.view_jia);

		view_meng.setStaticImage(R.drawable.a0);
		view_meng.setAnimId(R.anim.anim_meng);                                                                                                                    
		view_meng.setDrawableAnimId(R.anim.anim_meng_drawable);
		view_meng
				.setTitle(context.getResources().getString(R.string.anim_meng));

		view_tian.setStaticImage(R.drawable.b0);
		view_tian.setAnimId(R.anim.anim_meng);
		view_tian.setDrawableAnimId(R.anim.anim_tian_drawable);
		view_tian
				.setTitle(context.getResources().getString(R.string.anim_tian));

		view_shuai.setStaticImage(R.drawable.c0);
		view_shuai.setAnimId(R.anim.anim_meng);
		view_shuai.setDrawableAnimId(R.anim.anim_shuai_drawable);
		view_shuai.setTitle(context.getResources().getString(
				R.string.anim_shuai));

		view_jia.setStaticImage(R.drawable.d0);
		view_jia.setAnimId(R.anim.anim_meng);
		view_jia.setDrawableAnimId(R.anim.anim_jia_drawable);
		view_jia.setTitle(context.getResources().getString(R.string.anim_jia));

		view_meng.setPopupAnimViewListener(this);
		view_tian.setPopupAnimViewListener(this);
		view_shuai.setPopupAnimViewListener(this);
		view_jia.setPopupAnimViewListener(this);
	}

	@Override
	public void popupAnimViewClick(View v) {
		// TODO Auto-generated method stub
		// 文章点赞
		obtainFoundDetailZanRequest(v);
	}

	private void obtainFoundDetailZanRequest(View v) {
		// TODO Auto-generated method stub
		BaseRequest request = new BaseRequest();
		// 请求地址
		String requestID = String.format(
				Constant.RequestContstants.Request_Square_Zan,
				squareFoundModel.getArticleId(), v.getTag().toString());
		doAsync(false, requestID, request, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast("点赞成功");
				squareFoundModel.setHasUp(true);
				initDetailZan();
				tv_share.setText((int) Float.parseFloat(result) + "个朋友推荐!");
			}
		});
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mPullRefreshListView.setMode(Mode.PULL_FROM_END);// 设置刷新方式，上拉、下拉等

		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						String label = DateUtils.formatDateTime(BaseApp
								.getInstance().getApplicationContext(), System
								.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						// 更新最后一次刷新时间
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						obtainFoundDetailCommentRequest(true);

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						String label = DateUtils.formatDateTime(BaseApp
								.getInstance().getApplicationContext(), System
								.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						// 更新最后一次刷新时间
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						// didLoadDataRequest(false,1);
						obtainFoundDetailCommentRequest(false);
					}
				});

		// Add an end-of-list listener
		mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						if (mPullRefreshListView.getMode() == Mode.PULL_FROM_START) {
							// ToastUtils.showC(getApplicationContext(),
							// "数据已全部加载");
						}
					}
				});

		listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				LogUtils.i("firstVisibleItem:" + firstVisibleItem
						+ ",visibleItemCount:" + visibleItemCount
						+ ",totalItemCount:" + totalItemCount);
				if (firstVisibleItem == 0) {// 滑到顶部
					mPullRefreshListView.setMode(Mode.PULL_FROM_START);
				} else if (visibleItemCount + firstVisibleItem == totalItemCount) {
					// mPullRefreshListView.setMode(Mode.BOTH);
					if (commentModels != null
							&& commentModels.size() < Constant.pageNum) {
						mPullRefreshListView.setMode(Mode.DISABLED);
					} else {
						mPullRefreshListView.setMode(Mode.PULL_FROM_END);
					}
				} else {
					mPullRefreshListView.setMode(Mode.DISABLED);
					// if (commentModels != null && commentModels.size() <
					// Constant.pageNum) {
					// mPullRefreshListView.setMode(Mode.DISABLED);
					// } else {
					// mPullRefreshListView.setMode(Mode.PULL_FROM_END);
					// }
				}
			}
		});

		// 选择事件
		listView.setOnItemClickListener(this);

		adapter = new SquareFoundDetailListViewAdapter(this, null, false);
		adapter.setSquareFoundDetailListViewAdapterListener(this);
		mPullRefreshListView.setAdapter(adapter);

		listView.addHeaderView(headerView);

		btn_back.setOnClickListener(this);
		btn_share.setOnClickListener(this);
		btn_reply.setOnClickListener(this);
		btn_collection.setOnClickListener(this);
		ll_user_info.setOnClickListener(this);
		
		
		play_bg = (ImageView) findViewById(R.id.play_bg);
		mSuperVideoPlayer = (SuperVideoPlayer) findViewById(R.id.video_player_item_1);
        mPlayBtnView = findViewById(R.id.play_btn);
        mPlayBtnView.setOnClickListener(this);
        mSuperVideoPlayer.setVideoPlayCallback(mVideoPlayCallback);
        startDLNAService();
	}

	public void headerRefresh() {
		mPullRefreshListView.onRefreshComplete();
		mPullRefreshListView.setShowViewWhileRefreshing(true);
		mPullRefreshListView.setCurrentModeRefresh();
		mPullRefreshListView.setRefreshing(true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			finishActivity(this);
			break;
		case R.id.btn_share:

			oneKeyShareDetail();
			break;
		case R.id.btn_reply:

			if (!dataManager.isLogin) {
				Utily.go2Activity(context, LoginActivity.class);
				return;
			}

			if (squareFoundModel != null) {
				Bundle bundle = new Bundle();
				bundle.putLong(Constant.IntentKey.articleID,
						squareFoundModel.getArticleId());
				Utily.go2Activity(this,
						SquareFoundDetailAddReplyActivity.class, bundle);
			}

			break;
		case R.id.btn_collection:
			if (!dataManager.isLogin) {
				Utily.go2Activity(context, LoginActivity.class);
				return;
			}
//			ToastUtils.showPointToast("5");
			obtainFoundDetailCommentFavRequest(((ImageButton) v).isSelected());

			break;
		case R.id.ll_user_info:
			goToUserView();
			break;

		case R.id.play_btn:
			
			//startVideoPlay();
			startVideo();
			break;
		default:
			break;
		}
	}

	private void goToUserView() {
		// TODO Auto-generated method stub

		if (squareFoundModel != null) {
			Bundle bundle = new Bundle();
			bundle.putLong(Constant.IntentKey.userID,
					squareFoundModel.getUserId());
			Utily.go2Activity(context, MyInfoActivity.class, bundle);
		} else {
			ToastUtils.showCustomToast("未知数据");
		}

	}

	private void oneKeyShareDetail() {
		// TODO Auto-generated method stub
		if (!dataManager.isLogin) {
			Utily.go2Activity(context, LoginActivity.class);
			return;
		}

		if (squareFoundModel != null) {
			showShare(this, null, true);
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
			boolean showContentEdit) {
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
		oks.setTitle(squareFoundModel.getTitle());
		String titleUrl = Constant.RequestContstants.URL
				+ String.format(
						Constant.RequestContstants.Request_Article_Share,
						squareFoundModel.getArticleId());
		oks.setTitleUrl(titleUrl);
		//Toast.makeText(context, "值为+"+titleUrl, Toast.LENGTH_SHORT).show();
		oks.setText(squareFoundModel.getTitle());
		oks.setImagePath(ImageLoader.getInstance().getDiscCache()
				.get(squareFoundModel.getIco()).getPath()); // 分享sdcard目录下的图片
		String imageUrl = squareFoundModel.getModelType() == 2 ? squareFoundModel
				.getVideoIco() : squareFoundModel.getIco();
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
		// oks.setShareFromQQAuthSupport(false);
		// 将快捷分享的操作结果将通过OneKeyShareCallback回调
		// oks.setCallback(new OneKeyShareCallback());
		// 去自定义不同平台的字段内容
		// oks.setShareContentCustomizeCallback(new
		// ShareContentCustomizeDemo());
		// 在九宫格设置自定义的图标
		Bitmap enableLogo = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.ic_launcher);
		Bitmap disableLogo = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.ic_launcher);
		String label = "Easy";
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {

			}
		};
		// oks.setCustomerLogo(enableLogo, disableLogo, label, listener);

		// 为EditPage设置一个背景的View
		// oks.setEditPageBackground(getPage());
		// 隐藏九宫格中的新浪微博
		// oks.addHiddenPlatform(SinaWeibo.NAME);

		// String[] AVATARS = {
		// "http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
		// "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
		// "http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
		// "http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
		// "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
		// "http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg"
		// };
		// oks.setImageArray(AVATARS); //腾讯微博和twitter用此方法分享多张图片，其他平台不可以

		// 启动分享
		oks.show(context);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// Utily.go2Activity(this, SquareFoundDetailReplyActivity.class);

		if (position > 1) {
			if (!dataManager.isLogin) {
				Utily.go2Activity(context, LoginActivity.class);
				return;
			}
			selectCommentModel = commentModels.get(position - 2);
			replyHint = "@" + selectCommentModel.getUserNickName();
			popupReplyWindow();
		}

	}
	
	
	public void startVideo(){
		Uri uri = Uri.parse(squareFoundModel.getIco());
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "video/mp4");
		startActivity(intent);
	}
	private void popupReplyWindow() {
		// TODO Auto-generated method stub
		// 实例化PopupWindow
		if (replyWindow == null) {
			replyWindow = new ReplyPopupWindow(SquareFoundDetailActivity.this);
			replyWindow.setOnReplyClickListener(this);
		}
		replyWindow.setTextHint(replyHint);
		// 显示窗口
		replyWindow.showAtLocation(ll_quare_detail, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		openKeyboard();
	}

	@Override
	public void onReplyClick(View v) {
		// TODO Auto-generated method stub
		// 发表评论
		obtainFoundDetailCommentAddRequest();
	}

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		
		return R.layout.layout_fragment_square_found_detail;
	}

	@Override
	public void rightNavClick() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		isFirst = true;
		// 获取文章 ID
		articleID = getIntent().getExtras().getLong(
				Constant.IntentKey.articleID);
		isVideo = getIntent().getExtras().getBoolean("IsVideo");
		
		//注册广播  
        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(  
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS));  
		
		//重置视频页面
		resetVideoShow();
		// 查询发现详情
		obtainFoundDetailRequest();
		// 查询详情评价列表
	}

	private void resetVideoShow() {
		// TODO Auto-generated method stub
		if (isVideo) {
			layout_video.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 广场-发现详情
	 * 
	 * @param isCancelFav
	 */
	public void obtainFoundDetailRequest() {
		BaseRequest request = new BaseRequest();
		// 请求地址
		String requestID = String.format(
				Constant.RequestContstants.Request_Square_FoundDetail,
				articleID);
		doAsync(false, requestID, request, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
//				ToastUtils.showCustomToast(e.getLocalizedMessage());
//				mPullRefreshListView.onRefreshComplete();
				String errorMsg = e.getMessage();
				if ("无网络".equals(errorMsg)) {
					refreshComplete(true,true);
				} else {
					refreshComplete(true,false);
				}
			}
		}, new Callback<String>() {

			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub

				squareFoundModel = (SquareFoundModel) GsonUtils.jsonToBean(
						result, SquareFoundModel.class);
				
				long channelId = squareFoundModel.getChannelId();
				long modelType = squareFoundModel.getModelType();
				
				if (modelType == 2) {//视频
					layout_video.setVisibility(View.VISIBLE);
					
					String video_image_url = squareFoundModel.getVideoIco();
					ImageLoader.getInstance().displayImage(video_image_url,
							play_bg);
				}
				// 更新文章信息
				refreshFoundDetailInfo();
				// 请求评论列表
				obtainFoundDetailCommentRequest(true);
			}
		});
	}

	protected void refreshFoundDetailInfo() {
		// TODO Auto-generated method stub
		ImageLoader.getInstance().displayImage(
				squareFoundModel.getUserAvatar(), iv_user_logo,
				BaseApp.circleOptions);
		tv_nickname.setText(squareFoundModel.getUserNickName());
		;
		tv_time.setText(DateHelper.getStringFromDateStr(
				squareFoundModel.getPublishDate(), "yyyy-MM-dd HH:mm:ss"));
		;

		int readCount = (int) (squareFoundModel.getUpCount()
				+ squareFoundModel.getUpCount2()
				+ squareFoundModel.getUpCount3() + squareFoundModel
				.getUpCount4());
		tv_read.setText((int) squareFoundModel.getViewCount() + "");
		;
		tv_share.setText(readCount + "个朋友推荐!");
		tv_content.setText(squareFoundModel.getContent());

		// final URLImageGetter imageGetter = new
		// URLImageGetter(context,tv_content);
		// tv_content.setText(Html.fromHtml(squareFoundModel.getContent(),imageGetter,
		// null));

		if (squareFoundModel.isHasStore()) {
			btn_collection.setSelected(true);
		}

		initDetailZan();
		initImageShow();
	}

	@SuppressLint("NewApi")
	private void initImageShow() {
		// TODO Auto-generated method stub

		if (squareFoundModel.getModelType() == 2) {// 视频
//			initVideoLayout();
		} else {// 图片
			initImageLayout();
		}

	}

	@SuppressLint("InflateParams")
	private void initVideoLayout() {
		// TODO Auto-generated method stub
		if (squareFoundModel.getIco() != null) {
			@SuppressWarnings("static-access")
			View videoView = getLayoutInflater().from(context).inflate(
					R.layout.layout_video_play, null);
			ll_picker_layout.addView(videoView);
			ImageView iv_play = (ImageView) videoView
					.findViewById(R.id.iv_play);

			// 设置图片的位置
			MarginLayoutParams margin9 = new MarginLayoutParams(
					iv_play.getLayoutParams());
			RelativeLayout.LayoutParams layoutParams9 = new RelativeLayout.LayoutParams(
					margin9);
			int margin = DisplayUtil.dip2px(this, 10);
			layoutParams9.setMargins(margin, margin, margin, 0);
			layoutParams9.height = squareFoundModel.getIcoWidth() == 0 ? DisplayUtil
					.dip2px(context, 200) : (int) (DeviceUtils
					.getWidthPixels((Activity) context)
					* squareFoundModel.getIcoHeight() / squareFoundModel
					.getIcoWidth());// 设置图片的高度

			layoutParams9.width = DeviceUtils
					.getWidthPixels((Activity) context); // 设置图片的宽度
			iv_play.setLayoutParams(layoutParams9);

			iv_play.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Uri uri = Uri.parse(squareFoundModel.getIco());
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri, "video/mp4");
					startActivity(intent);
				}
			});

			ImageLoader.getInstance().displayImage(
					squareFoundModel.getVideoIco(), iv_play);
		}
	}

	private void initImageLayout() {
		// TODO Auto-generated method stub
		if (squareFoundModel.getArticlePictures() != null) {
			for (int i = 0; i < squareFoundModel.getArticlePictures().size(); i++) {
				ArticlePicture articlePicture = squareFoundModel
						.getArticlePictures().get(i);

				ImageView imageView = new ImageView(this);
				imageView.setScaleType(ScaleType.FIT_XY);
				// 设置图片的位置
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				int margin = DisplayUtil.dip2px(this, 10);
				layoutParams.setMargins(margin, margin, margin, 0);
				layoutParams.height = articlePicture.getPicWidth() == 0 ? DisplayUtil
						.dip2px(context, 200) : (int) (DeviceUtils
						.getWidthPixels((Activity) context)
						* articlePicture.getPicHeight() / articlePicture
						.getPicWidth());// 设置图片的高度

				if (articlePicture.getPicWidth() == 0) {
					imageView.setScaleType(ScaleType.CENTER);
				}

				layoutParams.width = DeviceUtils
						.getWidthPixels((Activity) context) - margin * 2; // 设置图片的宽度
				imageView.setLayoutParams(layoutParams);
				imageView.setTag(i);

				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// ToastUtils.showCustomToast(v.getTag().toString());

						if (squareFoundModel != null) {
							Bundle bundle = new Bundle();
							bundle.putString(Constant.IntentKey.from,
									"ArticleDetail");
							bundle.putInt("position",
									Integer.parseInt(v.getTag().toString()));
							bundle.putSerializable("attachments",
									squareFoundModel.getArticlePictures());
							Utily.go2Activity(context,
									ZhiboPicturesActivity.class, bundle);
						}

					}
				});
				ll_picker_layout.addView(imageView);

				ImageLoader.getInstance().displayImage(
						articlePicture.getPicPath(), imageView);
			}

		}
	}

	public void initDetailZan() {
		if (squareFoundModel.isHasUp()) {
			view_meng.setAnim(false);
			view_tian.setAnim(false);
			view_shuai.setAnim(false);
			view_jia.setAnim(false);
		} else {
			view_meng.setAnim(true);
			view_tian.setAnim(true);
			view_shuai.setAnim(true);
			view_jia.setAnim(true);
		}
	}

	/***
	 * 文章评价列表 articleId: 文章 ID parentId:父级编号，如果添加二级评论，parentId 则为父级评论编号；一级评论
	 * parentid 为0 MaxId : 最大 ID, 获取最新时 maxid=0,获取更多数据时请填写最大的 maxid=commentId，
	 * Count: 获取条数 commentType:0 文章评论，1：文章图集评论
	 */
	public void obtainFoundDetailCommentRequest(final boolean isRefresh) {
		BaseRequest request = new BaseRequest();
		// 请求地址
		long maxId = isRefresh ? 0 : (commentModels == null ? 0 : commentModels
				.get(commentModels.size() - 1).getCommentId());
		String requestID = String.format(
				Constant.RequestContstants.Request_Article_Comment, articleID,
				0, maxId, Constant.pageNum, 0);
		doAsync(false, requestID, request, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
//				ToastUtils.showCustomToast(e.getLocalizedMessage());
//				mPullRefreshListView.onRefreshComplete();
				String errorMsg = e.getMessage();
				if ("无网络".equals(errorMsg)) {
					refreshComplete(true,true);
				} else {
					refreshComplete(true,false);
				}
			}
		}, new Callback<String>() {

			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
//				mPullRefreshListView.onRefreshComplete();
				

				Type type = new TypeToken<ArrayList<CommentModel>>() {
				}.getType();

				@SuppressWarnings("unchecked")
				ArrayList<CommentModel> tempCommentModels = (ArrayList<CommentModel>) GsonUtils
						.jsonToList(result, type);
				boolean isComplete = false;
				if (!isRefresh) {// 上拉加载更多
					if (tempCommentModels.size() < Constant.pageNum) {
						isComplete = true;
					}
					commentModels.addAll(tempCommentModels);

					if (isComplete) {
						// mPullRefreshListView.setMode(Mode.PULL_FROM_START);
						mPullRefreshListView.setMode(Mode.DISABLED);
					}
				} else {
					commentModels = tempCommentModels;

					if (commentModels != null
							&& commentModels.size() < Constant.pageNum) {
						// mPullRefreshListView.setMode(Mode.PULL_FROM_START);
						mPullRefreshListView.setMode(Mode.DISABLED);
					} else {
						// mPullRefreshListView.setMode(Mode.BOTH);
						mPullRefreshListView.setMode(Mode.PULL_FROM_END);
					}
				}

				adapter.refershData(commentModels);
				refreshComplete(false,false);
			}
		});
	}

	@Override
	public void squareFoundDetailCommentLikeClick(int position) {
		// TODO Auto-generated method stub
		CommentModel commentModel = commentModels.get(position);
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
		doAsync(true, requestID, request, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				// 成功时，更改图标状态
				commentModel.setComment(true);
				commentModel.setCommentUpCount((long) Float.parseFloat(result));
				adapter.refershData(commentModels);
			}
		});
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
		doAsync(false, requestID, request, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				// ToastUtils.showCustomToast(result);
				btn_collection.setSelected(!btn_collection.isSelected());
			}
		});
	}

	/**
	 * 广场--详情-评论回复
	 */
	private void obtainFoundDetailCommentAddRequest() {
		// TODO Auto-generated method stub
		SquareFoundDetaiCommentAddRequest request = SquareRequest
				.squareFoundDetaiCommentAddRequest();
		request.requestMethod = Constant.Request_POST;

		ArticleComment articleComment = SquareRequest.articleComment();
		articleComment.ArticleId = articleID;
		// articleComment.ParentId = selectCommentModel.getCommentId();
		articleComment.ParentId = 0;
		articleComment.Content = replyHint + "  "
				+ replyWindow.getMessage().toString();
		articleComment.CommentType = 0;
		articleComment.UserNickName = dataManager.userModel.NickName;
		articleComment.UserAvatar = dataManager.userModel.Avatar;
		articleComment.AtUserId = selectCommentModel.getUserId();

		request.articleComment = articleComment;
		// 请求地址
		String requestID = Constant.RequestContstants.Request_Square_ArticleComment_Add;
		doAsync(true, requestID, request, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				// {"ArticleId":15036.0,"CommentId":65866.0,"CommentType":0.0,"CommentUpCount":0.0,"Content":"@测试小编  666666666666666666","CreateDate":"/Date(1452450041000)/","IP":"114.111.166.34","ParentId":65863.0,"State":0.0,"UserAvatar":"http://qn.ieasy.tv/image/201601/1452158360385_440.png","UserId":0.0,"UserNickName":"测试小编","ChildCount":0.0}
				// 成功时
				ToastUtils.showPointToast("5");// 回复加5分
				// ToastUtils.showCustomToast("评论成功");
				replyWindow.dismiss();
				replyWindow.reset();
				obtainFoundDetailCommentRequest(true);
			}
		});
	}

	public class URLImageGetter implements ImageGetter {
		Context context;
		TextView textView;

		public URLImageGetter(Context context, TextView textView) {
			this.context = context;
			this.textView = textView;
		}

		@Override
		public Drawable getDrawable(String paramString) {
			final URLDrawable urlDrawable = new URLDrawable(context);

			ImageGetterAsyncTask getterTask = new ImageGetterAsyncTask(
					urlDrawable);
			getterTask.execute(paramString);
			return urlDrawable;
		}

		public class ImageGetterAsyncTask extends
				AsyncTask<String, Void, Drawable> {
			URLDrawable urlDrawable;

			public ImageGetterAsyncTask(URLDrawable drawable) {
				this.urlDrawable = drawable;
			}

			@Override
			protected void onPostExecute(Drawable result) {
				if (result != null) {
					urlDrawable.drawable = result;

					URLImageGetter.this.textView.requestLayout();
				}
			}

			@Override
			protected Drawable doInBackground(String... params) {
				String source = params[0];
				return fetchDrawable(source);
			}

			public Drawable fetchDrawable(String url) {
				try {
					InputStream is = fetch(url);

					Rect bounds = getDefaultImageBounds(context);
					Bitmap bitmapOrg = BitmapFactory.decodeStream(is);
					Bitmap bitmap = Bitmap.createScaledBitmap(bitmapOrg,
							bounds.right, bounds.bottom, true);

					BitmapDrawable drawable = new BitmapDrawable(bitmap);
					drawable.setBounds(bounds);

					return drawable;
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				return null;
			}

			private InputStream fetch(String url)
					throws ClientProtocolException, IOException {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(url);

				HttpResponse response = client.execute(request);
				return response.getEntity().getContent();
			}

			public Rect getDefaultImageBounds(Context context) {
				Display display = getWindowManager().getDefaultDisplay();
				int width = display.getWidth();
				int height = (int) (width * 16 / 9);

				Rect bounds = new Rect(0, 0, width, height);
				return bounds;
			}

		}

	}

	public class URLDrawable extends BitmapDrawable {
		protected Drawable drawable;

		public URLDrawable(Context context) {
			this.setBounds(getDefaultImageBounds(context));

			drawable = getResources().getDrawable(R.drawable.default_img);
			drawable.setBounds(getDefaultImageBounds(context));
		}

		@Override
		public void draw(Canvas canvas) {
			Log.d("test", "this=" + this.getBounds());
			if (drawable != null) {
				Log.d("test", "draw=" + drawable.getBounds());
				drawable.draw(canvas);
			}
		}

		public Rect getDefaultImageBounds(Context context) {
			Display display = getWindowManager().getDefaultDisplay();
			int width = display.getWidth();
			int height = (int) (width * 16 / 9);

			Rect bounds = new Rect(0, 0, width, height);
			return bounds;
		}

	}
	
	
	
	/**
	 * 
	 * @param isError 是否请求错误
	 * @param isNoNetwork 是否网络请求错误
	 */
	public void refreshComplete(boolean isError,boolean isNoNetwork) {
//		isError = true;
//		isNoNetwork = false;
		try {
			int drawableID = 0;
			String title = null,content = null,btnTitle = null;
			if (isError && isNoNetwork && (squareFoundModel==null || (commentModels==null || commentModels.size() == 0))) {
				drawableID = R.drawable.error_network;
				title = getResources().getString(R.string.hint_error_network_title);
				content = getResources().getString(R.string.hint_error_network_content);
				btnTitle = getResources().getString(R.string.hint_error_button_title);
			} else if (isError  && (squareFoundModel==null || (commentModels==null || commentModels.size() == 0))) {
				drawableID = R.drawable.failed;
				title = getResources().getString(R.string.hint_error_request_title);
				content = getResources().getString(R.string.hint_error_request_content);
				btnTitle = getResources().getString(R.string.hint_error_button_title);
			} else if(squareFoundModel==null){
				drawableID = R.drawable.default_img;
				title = getResources().getString(R.string.hint_error_nodata_title);
				content = getResources().getString(R.string.hint_error_nodata_content);
				btnTitle = getResources().getString(R.string.hint_error_button_title);
			}
			
			if (drawableID>0 && progressActivity!=null) {
				progressActivity.showError(getResources().getDrawable(drawableID), title, content, btnTitle, new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (squareFoundModel==null || (commentModels==null || commentModels.size() == 0)) {
							progressActivity.showLoading();
						}
						// 查询发现详情
						obtainFoundDetailRequest();
					}
				});
			} else {
				progressActivity.showContent();
			}
			
			
			if (mPullRefreshListView != null) {
				mPullRefreshListView.onRefreshComplete();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	
	
	
	//*********************************
	
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			stopDLNAService();
		}
		
		/***
	     * 旋转屏幕之后回调
	     *
	     * @param newConfig newConfig
	     */
	    @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        if (null == mSuperVideoPlayer) return;
	        
	        /***
	         * 根据屏幕方向重新设置播放器的大小
	         */
	        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
	            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
	            getWindow().getDecorView().invalidate();
	            float height = DensityUtil.getWidthInPx(this);
	            float width = DensityUtil.getHeightInPx(this);
	            mSuperVideoPlayer.getLayoutParams().height = (int) width;
	            mSuperVideoPlayer.getLayoutParams().width = (int) height;
	        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
	            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
	            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
	            getWindow().setAttributes(attrs);
	            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	            float width = DensityUtil.getWidthInPx(this);
	            float height = DensityUtil.dip2px(this, 200.f);
	            mSuperVideoPlayer.getLayoutParams().height = (int) height;
	            mSuperVideoPlayer.getLayoutParams().width = (int) width;
	        }
	        
//	        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
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
	        showNavBar();
	    	
//	    	if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {//横屏
//	            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//	            mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
//	            
//	            layout_content.setVisibility(View.VISIBLE);
//	            showNavBar();
//	            
//	        } else {//竖屏
//	            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//	            mSuperVideoPlayer.setPageType(MediaController.PageType.EXPAND);
//	            
//	            layout_content.setVisibility(View.GONE);
//	            hideNavBar();
//	        }
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
	    	
	    	String video_image_url = squareFoundModel.getVideoIco();
			ImageLoader.getInstance().displayImage(video_image_url,
					play_bg);
			
			
	    	mPlayBtnView.setVisibility(View.GONE);
	    	play_bg.setVisibility(View.VISIBLE);
	        mSuperVideoPlayer.setVisibility(View.VISIBLE);
	        mSuperVideoPlayer.setAutoHideController(true);

	        Video video = new Video();
	        VideoUrl videoUrl1 = new VideoUrl();
	        videoUrl1.setFormatName("720P");
	        videoUrl1.setFormatUrl(squareFoundModel.getIco());
//	        VideoUrl videoUrl2 = new VideoUrl();
//	        videoUrl2.setFormatName("480P");
//	        videoUrl2.setFormatUrl("http://7xkbzx.com1.z0.glb.clouddn.com/SampleVideo_720x480_20mb.mp4");
	        ArrayList<VideoUrl> arrayList1 = new ArrayList<>();
	        arrayList1.add(videoUrl1);
//	        arrayList1.add(videoUrl2);
	        video.setVideoName(squareFoundModel.getTitle());
	        video.setVideoUrl(arrayList1);

//	        Video video2 = new Video();
//	        VideoUrl videoUrl3 = new VideoUrl();
//	        videoUrl3.setFormatName("720P");
//	        videoUrl3.setFormatUrl("http://7xkbzx.com1.z0.glb.clouddn.com/SampleVideo_1080x720_10mb.mp4");
//	        VideoUrl videoUrl4 = new VideoUrl();
//	        videoUrl4.setFormatName("480P");
//	        videoUrl4.setFormatUrl("http://7xkbzx.com1.z0.glb.clouddn.com/SampleVideo_720x480_10mb.mp4");
//	        ArrayList<VideoUrl> arrayList2 = new ArrayList<>();
//	        arrayList2.add(videoUrl3);
//	        arrayList2.add(videoUrl4);
//	        video2.setVideoName("测试视频二");
//	        video2.setVideoUrl(arrayList2);

	        ArrayList<Video> videoArrayList = new ArrayList<>();
	        videoArrayList.add(video);
//	        videoArrayList.add(video2);

	        mSuperVideoPlayer.loadMultipleVideo(videoArrayList,0,0,0);
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
	                     //表示按了home键,程序到了后台  
	                	if (isPlaying) {
	                		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	                		videoPlayPosition = mSuperVideoPlayer.getSuperVideoView().getCurrentPosition();
//		                    Toast.makeText(getApplicationContext(), "home:"+videoPlayPosition, 1).show();  
	                		mPlayBtnView.setVisibility(View.VISIBLE);
		                	mSuperVideoPlayer.setVisibility(View.GONE);
		                	play_bg.setVisibility(View.VISIBLE);
						}
	                	
	                	
	                }else if(TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)){  
	                    //表示长按home键,显示最近使用的程序列表  
	                }  
	            }   
	        }  
	    };

}
