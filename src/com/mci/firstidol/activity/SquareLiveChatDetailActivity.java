package com.mci.firstidol.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import net.frakbot.imageviewex.ImageViewNext;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.mci.firstidol.adapter.SquareLiveChatCommentListViewAdapter;
import com.mci.firstidol.adapter.SquareLiveChatCommentListViewAdapter.SquareLiveChatCommentListViewAdapterListener;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.fragment.welfare.Configs;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.SquareModel;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.SquareModel.LiveAnnex;
import com.mci.firstidol.model.SquareModel.SquareLiveChatModel;
import com.mci.firstidol.model.SquareRequest.CommentAction;
import com.mci.firstidol.model.SquareRequest.SquareFoundDetaiCommentLikelRequest;
import com.mci.firstidol.model.SquareRequest.SquareLiveChatSaveRequest;
import com.mci.firstidol.model.SquareRequest.SquareLiveDetailDeleteRequest;
import com.mci.firstidol.model.SquareRequest.SquareLiveDetailUpRequest;
import com.mci.firstidol.utils.CommonUtils;
import com.mci.firstidol.utils.DateHelper;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.AlertDialog;
import com.mci.firstidol.view.ReplyPopupWindow;
import com.mci.firstidol.view.ReplyPopupWindow.OnReplyClickListener;
import com.mci.firstidol.view.ToastUtils;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;

import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SquareLiveChatDetailActivity extends BaseActivity implements OnItemClickListener,OnClickListener,
SquareLiveChatCommentListViewAdapterListener,OnReplyClickListener{

	
	private  PullToRefreshListView mPullRefreshListView;
	private ListView listView;
	private SquareLiveChatCommentListViewAdapter adapter;
	
	private ImageButton btn_reply;
	private View ll_text,ll_pic,ll_video,ll_audio;
	private Button btn_delete;
	private TextView tv_time,tv_read,tv_share,content,videoTime;
	private ImageView cover;
	private ImageViewNext onePic;
	private View twoPic,threePic;
	
	private LinearLayout ll_square_livechat_detail;
	
	private MediaPlayer mMediaPlayer;
	private DisplayImageOptions mOptions;
    private DisplayImageOptions mMessageOptions;
	private static final float VIDEO_COVER_SCALE = 240f / 436f;
	private float mCoverWidth;
    private float mCoverHeight;
    private int mImageSpace;
    private int mImageSize;
    private View headerView;
	
	private ArrayList<SquareLiveChatModel> commentModels;
//	private long articleID;
	private SquareLiveChatModel squareLiveChatModel;
	private long userRole;
	private String from;
	
	
	private ReplyPopupWindow replyWindow;
	private String replyHint;
	private SquareLiveChatModel selectCommentModel;
	
	private boolean isFirst;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if (!isFirst) {
			obtainLiveChatCommentRequest(true);
		} else {
			isFirst = false;
		}
	}

	protected void initNavBar() {
		// TODO Auto-generated method stub
//		setTitle(R.string.comment);
		
	}

	protected void findViewById() {
		// TODO Auto-generated method stub
		ll_square_livechat_detail = (LinearLayout) findViewById(R.id.ll_square_livechat_detail);
		
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		listView = mPullRefreshListView.getRefreshableView();

		btn_reply = (ImageButton) findViewById(R.id.btn_reply);
		
		
		
		headerView = LayoutInflater.from(this).inflate(R.layout.item_live_chat_comment_header, null);
		btn_delete = (Button) headerView.findViewById(R.id.btn_delete);

		ll_text = (LinearLayout) headerView.findViewById(R.id.ll_text);
		content = (TextView) headerView.findViewById(R.id.content);
		
		ll_pic = (RelativeLayout) headerView.findViewById(R.id.ll_pic);
		onePic = (ImageViewNext) headerView.findViewById(R.id.one_pic);
		twoPic = headerView.findViewById(R.id.two_pic);
		threePic =  headerView.findViewById(R.id.three_pic);
		
		ll_video = (RelativeLayout) headerView.findViewById(R.id.ll_video);
		cover = (ImageView) headerView.findViewById(R.id.cover);
        videoTime = (TextView) headerView.findViewById(R.id.video_time);
        		
		ll_audio = (LinearLayout) headerView.findViewById(R.id.ll_audio);
		
		
		tv_time = (TextView) headerView.findViewById(R.id.tv_time);
		tv_read = (TextView) headerView.findViewById(R.id.tv_read);
		tv_share = (TextView) headerView.findViewById(R.id.tv_share);
		
	}

	protected void initView() {
		// TODO Auto-generated method stub
		mPullRefreshListView.setMode(Mode.DISABLED);//设置刷新方式，上拉、下拉等
		
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(
						BaseApp.getInstance().getApplicationContext(),
						System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				// 更新最后一次刷新时间
				refreshView.getLoadingLayoutProxy()
						.setLastUpdatedLabel(label);

				obtainLiveChatCommentRequest(true);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(
						BaseApp.getInstance().getApplicationContext(),
						System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				// 更新最后一次刷新时间
				refreshView.getLoadingLayoutProxy()
						.setLastUpdatedLabel(label);
				
				obtainLiveChatCommentRequest(false);
			}
		});

		// Add an end-of-list listener
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if (mPullRefreshListView.getMode() == Mode.PULL_FROM_START) {
//					ToastUtils.showC(getApplicationContext(), "数据已全部加载");
				}
			}
		});;
		//选择事件
		mPullRefreshListView.setOnItemClickListener(this);
		
		adapter = new SquareLiveChatCommentListViewAdapter(this, null, false);
		adapter.setSquareLiveChatCommentListViewAdapterListener(this);
		mPullRefreshListView.setAdapter(adapter);
		
		listView.addHeaderView(headerView);
		
		btn_reply.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
	}
	
	public void headerRefresh(){
		mPullRefreshListView.onRefreshComplete();
		mPullRefreshListView.setShowViewWhileRefreshing(true);
		mPullRefreshListView.setCurrentModeRefresh();
		mPullRefreshListView.setRefreshing(true);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (position>1) {
			if (dataManager.isLogin) {
				selectCommentModel = commentModels.get(position-2);
				replyHint = "@"+ selectCommentModel.getUserNickName();
				popupReplyWindow();
			} else {
				Utily.go2Activity(context, LoginActivity.class);
			}
			
			
		}
		
	}
	
	private void popupReplyWindow() {
		// TODO Auto-generated method stub
		// 实例化PopupWindow
		if (replyWindow == null) {
			replyWindow = new ReplyPopupWindow(SquareLiveChatDetailActivity.this);
			replyWindow.setOnReplyClickListener(this);
		}
		replyWindow.setTextHint(replyHint);
		// 显示窗口
		replyWindow.showAtLocation(ll_square_livechat_detail, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		openKeyboard();
	}
	
	@Override
	public void onReplyClick(View v) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(replyWindow.getMessage())) {
			ToastUtils.showCustomToast("回复内容不能为空");
			return;
		} else if(replyWindow.getMessage().length()<5){
			ToastUtils.showCustomToast("长度不能少于5个字符");
			return;
		} else if(replyWindow.getMessage().length()>140){
			ToastUtils.showCustomToast("长度不能大于140个字符");
			return;
		}
		//发表评论
		obtainChatSaveRequest(null);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_reply:
//			startActivity(SquareFoundDetailAddReplyActivity.class);
			if (dataManager.isLogin) {
				Bundle bundle = new Bundle();
				bundle.putString(Constant.IntentKey.from, "Live");
				bundle.putLong(Constant.IntentKey.chatID, squareLiveChatModel.getChatId());
				bundle.putLong(Constant.IntentKey.userRole, userRole);
				bundle.putLong(Constant.IntentKey.articleID, squareLiveChatModel.getArticleId());
				Utily.go2Activity(this, SquareFoundDetailAddReplyActivity.class,bundle);
			} else {
				Utily.go2Activity(context, LoginActivity.class);
			}
			
			
			break;
		case R.id.btn_delete:
			showDeleteHint();
			break;

		default:
			break;
		}
	}

	private void showDeleteHint() {
		// TODO Auto-generated method stub
		AlertDialog.show(context, "", "确定要删除这条内容?", "确定", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				obtainLiveChatDeleteEvent();
			}
		}, "取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.layout_fragment_square_found_detail_reply;
	}

	@Override
	public void rightNavClick() {
		// TODO Auto-generated method stub
		
	}


	protected void initData() {
		// TODO Auto-generated method stub
		isFirst = true;
		
//		articleID = getIntent().getExtras().getLong(Constant.IntentKey.articleID);
		squareLiveChatModel = (SquareLiveChatModel) getIntent().getExtras().getSerializable(Constant.IntentKey.squareLiveChatModel);
		userRole = getIntent().getExtras().getLong(Constant.IntentKey.userRole);
		from = getIntent().getExtras().getString(Constant.IntentKey.from);
		
		setTitle(squareLiveChatModel.getUserNickName());
		
		initParam();
		//初始化评论内容显示
		initCommentContentShow();
		//请求评论子列表
		obtainLiveChatCommentRequest(false);
	}

	private void initParam() {
		// TODO Auto-generated method stub
		 mMessageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).resetViewBeforeLoading(true)
	                .showImageForEmptyUri(R.drawable.zhibo_message_default_icon)
	                .showImageOnFail(R.drawable.zhibo_message_default_icon).showImageOnLoading(R.drawable.zhibo_message_default_icon)
	                .build();
		 
		mCoverWidth = CommonUtils.getScreenWidth(context) - 2
                * context.getResources().getDimension(R.dimen.newsfragment_listview_padding)
                - context.getResources().getDimension(R.dimen.zhibo_avatar_size)
                - context.getResources().getDimension(R.dimen.zhibo_message_left_padding)
                - context.getResources().getDimension(R.dimen.zhibo_message_right_padding);
        mCoverHeight = mCoverWidth * VIDEO_COVER_SCALE;
        mImageSpace = (int) context.getResources().getDimension(R.dimen.dp5);
        mImageSize = (int) ((mCoverWidth - 2 * mImageSpace) / 3);
	}

	@SuppressWarnings("static-access")
	private void initCommentContentShow() {
		// TODO Auto-generated method stub
		int type = 0;
		if (squareLiveChatModel.getLiveAnnexList() != null && !squareLiveChatModel.getLiveAnnexList().isEmpty()) {
			type = (int)squareLiveChatModel.getLiveAnnexList().get(0).getAnnexType();
        }
		
		if (type == 0) {//文本
			ll_text.setVisibility(View.VISIBLE);
			content.setText(squareLiveChatModel.getChatContent());
		} else if(type == 1){//图片
			ll_text.setVisibility(View.VISIBLE);
			ll_pic.setVisibility(View.VISIBLE);
			renderPicView();
		} else if(type == 2){//音频
			ll_audio.setVisibility(View.VISIBLE);
			renderAudioView();
		} else if(type == 3){//视频
			ll_text.setVisibility(View.VISIBLE);
			ll_video.setVisibility(View.VISIBLE);
			renderVideoView();
		}
		
		
		//显示数字提示
		tv_time.setText(DateHelper.intervalSinceNow(DateHelper.getStringFromDateStr(squareLiveChatModel.getCreateDate(), "yyyy-MM-dd HH:mm:ss")));
		tv_read.setText(squareLiveChatModel.getReplyCount()+"");
		tv_share.setText(squareLiveChatModel.getUpCount()+"");
		
		if ((dataManager.userModel!=null && dataManager.userModel.UserId == squareLiveChatModel.getUserId()) || userRole!=0) {//登录用户发布
			btn_delete.setVisibility(View.VISIBLE);
		} else {
			btn_delete.setVisibility(View.GONE);
		}
	}

	private void renderVideoView() {
		// TODO Auto-generated method stub
		final LiveAnnex attachment = squareLiveChatModel.getLiveAnnexList().get(0);
        content.setText(squareLiveChatModel.getChatContent());
        videoTime.setText(TextUtils.isEmpty(attachment.getDuration())?"":attachment.getDuration());

        float scale = 1;
        if (attachment.getOrigWidth() > 0 && attachment.getOrigHeight() > 0) {
            scale = (float) attachment.getOrigWidth() / (float) attachment.getOrigHeight();
        }
        RelativeLayout.LayoutParams lp = (LayoutParams) cover.getLayoutParams();
        if (lp != null) {
        	lp.width = (int) ((mCoverWidth - 2 * context.getResources().getDimension(R.dimen.dp10)) * VIDEO_COVER_SCALE * scale);
            lp.height = (int) mCoverHeight;
            
            cover.setLayoutParams(lp);
        }

        ImageLoader.getInstance().displayImage(CommonUtils.getValidImageUrl(context, attachment.getVideoCover()), cover, mMessageOptions);
        cover.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(attachment.getAnnexUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "video/mp4");
                startActivity(intent);
            }
        });
	}

	private void renderAudioView() {
		// TODO Auto-generated method stub

        final LiveAnnex attachment = squareLiveChatModel.getLiveAnnexList().get(0);
        int time = 1;
        if (!TextUtils.isEmpty(attachment.getDuration()) && attachment.getDuration().contains("\"")) {
            try {
                time = Integer.parseInt(attachment.getDuration().replace("\"", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LinearLayout background = (LinearLayout) headerView.findViewById(R.id.audio_bg);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) background.getLayoutParams();
        if (lp != null) {
        	lp.width = (int) (mCoverWidth / 3 + (float) time / (float) 60 * (mCoverWidth / 3));
        }
        background.setLayoutParams(lp);

        final ImageView audioIcon = (ImageView) headerView.findViewById(R.id.audio_icon);
        TextView audioTime = (TextView) headerView.findViewById(R.id.audio_time);
        audioTime.setText((TextUtils.isEmpty(attachment.getDuration())?"":attachment.getDuration())+"");
        background.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio(attachment.getAnnexUrl(), audioIcon);
            }
        });
	}
	
	private void playAudio(String url, final ImageView view) {
        try {
            if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
                view.setImageResource(R.drawable.zhibo_audio_play);
                final AnimationDrawable animation = (AnimationDrawable) view.getDrawable();
                animation.stop();
                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            animation.start();
                        }
                    });
                    mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mMediaPlayer.release();
                            mMediaPlayer = null;
                            animation.stop();
                            view.setImageResource(R.drawable.audio_state_1);
                        }
                    });
                    mMediaPlayer.setDataSource(url);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } else {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                        animation.stop();
                        view.setImageResource(R.drawable.audio_state_1);
                    }
                }
            } else {
                ToastUtils.showCustomToast("错误的音频地址");
            }
        } catch (Exception e) {
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            view.setImageResource(R.drawable.audio_state_1);
        }
    }

	private void renderPicView() {
		// TODO Auto-generated method stub
		content.setText(squareLiveChatModel.getChatContent());
        if (squareLiveChatModel.getLiveAnnexList().size() == 1) {
            onePic.setVisibility(View.VISIBLE);
            twoPic.setVisibility(View.GONE);
            threePic.setVisibility(View.GONE);

            LiveAnnex attachment = squareLiveChatModel.getLiveAnnexList().get(0);
            RelativeLayout.LayoutParams lp = (LayoutParams) onePic.getLayoutParams();
            if (lp != null) {
            	lp.width = mImageSize;
                lp.height = mImageSize;

                onePic.setLayoutParams(lp);
            }

//            ImageLoader.getInstance().displayImage(CommonUtils.getValidImageUrl(context, attachment.getAnnexUrl()), onePic, mMessageOptions);
            if (attachment.getAnnexUrl().indexOf(".gif")!=-1) {
            	onePic.setUrl(attachment.getAnnexUrl());
			} else {
				ImageLoader.getInstance().displayImage(CommonUtils.getValidImageUrl(context, attachment.getAnnexUrl()), onePic, mMessageOptions);
			}
            
            onePic.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startImageActivity(0, squareLiveChatModel.getLiveAnnexList());
                }
            });
        } else if (squareLiveChatModel.getLiveAnnexList().size() == 2) {
            onePic.setVisibility(View.GONE);
            twoPic.setVisibility(View.VISIBLE);
            threePic.setVisibility(View.GONE);

            ImageViewNext image1 = (ImageViewNext) twoPic.findViewById(R.id.two_pic_1);
            ImageViewNext image2 = (ImageViewNext) twoPic.findViewById(R.id.two_pic_2);
            LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) image1.getLayoutParams();
            if (lp != null) {
            	lp.width = mImageSize;
                lp.height = mImageSize;

                lp.rightMargin = mImageSpace;
                image1.setLayoutParams(lp);
                image2.setLayoutParams(lp);
            }

            final String url1 = CommonUtils.getValidImageUrl(context, squareLiveChatModel.getLiveAnnexList().get(0).getAnnexUrl());
            final String url2 = CommonUtils.getValidImageUrl(context, squareLiveChatModel.getLiveAnnexList().get(1).getAnnexUrl());
//            ImageLoader.getInstance().displayImage(url1, image1, mMessageOptions);
//            ImageLoader.getInstance().displayImage(url2, image2, mMessageOptions);
            if (url1.indexOf(".gif")!=-1) {
            	image1.setUrl(url1);
			} else {
				ImageLoader.getInstance().displayImage(url1, image1, mMessageOptions);
			}
            
            if (url2.indexOf(".gif")!=-1) {
            	image2.setUrl(url2);
			} else {
				ImageLoader.getInstance().displayImage(url2, image2, mMessageOptions);
			}
            
            
            image1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startImageActivity(0, squareLiveChatModel.getLiveAnnexList());
                }
            });
            image2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startImageActivity(1, squareLiveChatModel.getLiveAnnexList());
                }
            });
        } else {
            onePic.setVisibility(View.GONE);
            twoPic.setVisibility(View.GONE);
            threePic.setVisibility(View.VISIBLE);

            ImageViewNext image1 = (ImageViewNext) threePic.findViewById(R.id.three_pic_1);
            ImageViewNext image2 = (ImageViewNext) threePic.findViewById(R.id.three_pic_2);
            LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) image1.getLayoutParams();
            if (lp != null) {
            	lp.width = mImageSize;
                lp.height = mImageSize;

                lp.rightMargin = mImageSpace;
                image1.setLayoutParams(lp);
                image2.setLayoutParams(lp);
            }
            ImageViewNext image3 = (ImageViewNext) threePic.findViewById(R.id.three_pic_3);
            LinearLayout.LayoutParams lp2 = (android.widget.LinearLayout.LayoutParams) image3.getLayoutParams();
            if (lp2 != null) {
            	lp2.width = mImageSize;
                lp2.height = mImageSize;

                image3.setLayoutParams(lp);
            }

            final String url1 = CommonUtils.getValidImageUrl(context, squareLiveChatModel.getLiveAnnexList().get(0).getAnnexUrl());
            final String url2 = CommonUtils.getValidImageUrl(context, squareLiveChatModel.getLiveAnnexList().get(1).getAnnexUrl());
            final String url3 = CommonUtils.getValidImageUrl(context, squareLiveChatModel.getLiveAnnexList().get(2).getAnnexUrl());
//            ImageLoader.getInstance().displayImage(url1, image1, mMessageOptions);
//            ImageLoader.getInstance().displayImage(url2, image2, mMessageOptions);
//            ImageLoader.getInstance().displayImage(url3, image3, mMessageOptions);
            
            if (url1.indexOf(".gif")!=-1) {
            	image1.setUrl(url1);
			} else {
				ImageLoader.getInstance().displayImage(url1, image1, mMessageOptions);
			}
            
            if (url2.indexOf(".gif")!=-1) {
            	image2.setUrl(url2);
			} else {
				ImageLoader.getInstance().displayImage(url2, image2, mMessageOptions);
			}
            
            if (url3.indexOf(".gif")!=-1) {
            	image3.setUrl(url3);
			} else {
				ImageLoader.getInstance().displayImage(url3, image3, mMessageOptions);
			}
            
            image1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startImageActivity(0, squareLiveChatModel.getLiveAnnexList());
                }
            });
            image2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startImageActivity(1, squareLiveChatModel.getLiveAnnexList());
                }
            });
            image3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startImageActivity(2, squareLiveChatModel.getLiveAnnexList());
                }
            });
        }
	}
	
	private void startImageActivity(int position, ArrayList<LiveAnnex> attachments) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putSerializable("attachments", attachments);
        Utily.go2Activity(context, ZhiboPicturesActivity.class, bundle);
        
    }

	
	
	public void obtainLiveChatCommentRequest(final boolean isRefresh){
		BaseRequest request = new BaseRequest();
		//请求地址
		long chatId;
		if (isRefresh) {//加载未读信息
			 if (commentModels == null || commentModels.size()==0) {
				 chatId = 0;
			} else {
				chatId = commentModels.get(0).getChatId();
			}
		} else {//加载更多
			 if (commentModels == null || commentModels.size()==0) {
				 chatId = 0;
			} else {
				chatId = commentModels.get(commentModels.size()-1).getChatId();
			}
		}
		
		String requestID = String.format(isRefresh?Constant.RequestContstants.Request_LiveChat_UnreadLiveChildren : Constant.RequestContstants.Request_LiveChat_LiveChildrenList, squareLiveChatModel.getArticleId(),squareLiveChatModel.getChatId(),chatId);		
		doAsync(false, requestID, request, new Callback<Exception>() {
			
			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
				mPullRefreshListView.onRefreshComplete();
			}
		}, new Callback<String>() {
			
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				mPullRefreshListView.onRefreshComplete();
				
				Type type = new TypeToken<ArrayList<SquareLiveChatModel>>() {
				}.getType();
				
				@SuppressWarnings("unchecked")
				ArrayList<SquareLiveChatModel> tempCommentModels = (ArrayList<SquareLiveChatModel>) GsonUtils
						.jsonToList(result, type);
				boolean isComplete = false;
				if (!isRefresh) {//上拉加载更多
					if (tempCommentModels.size()<Constant.pageNum10) {
						isComplete = true;
					}
					if (commentModels==null) {
						commentModels = new ArrayList<SquareLiveChatModel>();
					}
					commentModels.addAll(tempCommentModels);
					
					if (isComplete) {
						mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					} else {
						mPullRefreshListView.setMode(Mode.BOTH);
					}
				} else {
					if (tempCommentModels!= null && tempCommentModels.size()>0) {
						commentModels.addAll(0, tempCommentModels);
					}
					
					
					if (commentModels != null && commentModels.size() < Constant.pageNum10) {
						mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					} else {
						mPullRefreshListView.setMode(Mode.BOTH);
					}
				}
				
				adapter.refershData(commentModels);
			}
		});
	}

	@Override
	public void squareLiveChatCommentLikeClick(int position) {
		// TODO Auto-generated method stub
		SquareLiveChatModel commentModel = commentModels.get(position);
		obtainFoundDetailCommentLikeRequest(commentModel);
	}
	
	/**
	 * 广场-发现详情-评论点赞
	 * @param isCancelFav
	 */
	public void obtainFoundDetailCommentLikeRequest(final SquareLiveChatModel commentModel){
		
		
		
//		final CommentModel chat = listData.get(position);
		
		SquareLiveDetailUpRequest request = SquareRequest.squareLiveDetailUpRequest();
		request.requestMethod = Constant.Request_POST;
		request.chatId = commentModel.getChatId();
		//请求地址
		String requestID = Constant.RequestContstants.Request_LiveChat_Up;		
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
				//成功时，更改图标状态
				Configs.saveChatZan(context, commentModel.getArticleId(), commentModel.getChatId());
				commentModel.setUpCount((long)Float.parseFloat(result));
//				notifyDataSetChanged();
				adapter.refershData(commentModels);
			}
		});
		
		
//		SquareFoundDetaiCommentLikelRequest request = SquareRequest.squareFoundDetaiCommentLikelRequest();
//		request.requestMethod = Constant.Request_POST;
//		
//		CommentAction commentAction = SquareRequest.commentAction();
//		commentAction.ActType = "1";
//		commentAction.ActName = "up";
//		commentAction.CommentId = commentModel.getCommentId();
//		commentAction.ActUserId = dataManager.isLogin?(dataManager.userModel.UserId+""):"-1";
//		commentAction.CommentType = 0;
//				
//		request.action = commentAction;
//		//请求地址
//		String requestID = Constant.RequestContstants.Request_Square_CommentActions_Add;		
//		doAsync(true, requestID, request, new Callback<Exception>() {
//			
//			@Override
//			public void onCallback(Exception e) {
//				// TODO Auto-generated method stub
//				ToastUtils.showCustomToast(e.getLocalizedMessage());
//			}
//		}, new Callback<String>() {
//			
//			@Override
//			public void onCallback(String result) {
//				// TODO Auto-generated method stub
//				//成功时，更改图标状态
//				commentModel.setComment(true);
//				commentModel.setCommentUpCount((long)Float.parseFloat(result));
//				adapter.refershData(commentModels);
//			}
//		});
		
		
		
		
	}
	
	
	
	protected void obtainLiveChatDeleteEvent() {
		// TODO Auto-generated method stub
		SquareLiveDetailDeleteRequest request = SquareRequest.squareLiveDetailDeleteRequest();
		request.requestMethod = Constant.Request_POST;
		request.chatId = squareLiveChatModel.getChatId();
		request.articleId = squareLiveChatModel.getArticleId();
		//请求地址
		String requestID = Constant.RequestContstants.Request_LiveChat_Delete;		
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
				//成功时，返回，并刷新数据
				Intent intent = new Intent();
				
				setResult(RESULT_OK, intent);
				
				finishActivity(SquareLiveChatDetailActivity.this);
			}
		});
	}
	
	
	
	
	/**
     * 保存聊天信息
     * @param attachments
     */
	protected void obtainChatSaveRequest(ArrayList<LiveAnnex> attachments) {
		// TODO Auto-generated method stub
		SquareLiveChatSaveRequest request = SquareRequest.squareLiveChatSaveRequest();// 其它参数需要继承此类
		request.requestMethod = Constant.Request_POST;
		request.liveChat = initLiveChatModel();
		request.liveAnnexList = attachments;
		String requestID = Constant.RequestContstants.Request_LiveChat_Save;
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
				replyWindow.dismiss();
				replyWindow.reset();
//				LogUtils.i(result);
//				ToastUtils.showCustomToast("发布成功");
				ToastUtils.showPointToast("5");//回复加5分
				obtainLiveChatCommentRequest(true);
			}
		});
	}

	private SquareLiveChatModel initLiveChatModel() {
		// TODO Auto-generated method stub
		SquareLiveChatModel tepSquareLiveChatModel = new SquareModel().new SquareLiveChatModel();
		tepSquareLiveChatModel.setArticleId(squareLiveChatModel.getArticleId());
		tepSquareLiveChatModel.setChatContent(replyHint + " " + replyWindow.getMessage().toString());
		tepSquareLiveChatModel.setUserId(dataManager.userModel.UserId);
		tepSquareLiveChatModel.setUserRole(userRole);
		tepSquareLiveChatModel.setParentId(squareLiveChatModel.getChatId());
//		squareLiveChatModel.setParentId(0);
		tepSquareLiveChatModel.setUserAvatar(dataManager.userModel.Avatar);
		tepSquareLiveChatModel.setUserNickName(dataManager.userModel.NickName);
		tepSquareLiveChatModel.setAtUserId(selectCommentModel.getUserId());
		
		return tepSquareLiveChatModel;
	}

}
