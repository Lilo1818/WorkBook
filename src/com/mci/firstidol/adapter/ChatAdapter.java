
package com.mci.firstidol.adapter;

import java.util.ArrayList;

import net.frakbot.imageviewex.ImageViewNext;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;
import com.mci.firstidol.activity.LoginActivity;
import com.mci.firstidol.activity.MyInfoActivity;
import com.mci.firstidol.activity.ReportActivity;
import com.mci.firstidol.activity.SquareLiveActivity;
import com.mci.firstidol.activity.VideoPlayActivity;
import com.mci.firstidol.activity.ZhiboPicturesActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.fragment.welfare.Configs;
import com.mci.firstidol.model.SquareModel.LiveAnnex;
import com.mci.firstidol.model.SquareModel.SquareLiveChatModel;
import com.mci.firstidol.model.SquareRequest.SquareLiveChatSaveRequest;
import com.mci.firstidol.model.SquareModel;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.SquareRequest.SquareLiveDetailDeleteRequest;
import com.mci.firstidol.model.SquareRequest.SquareLiveDetailUpRequest;
import com.mci.firstidol.model.UserModel;
import com.mci.firstidol.utils.AsyncTaskUtils;
import com.mci.firstidol.utils.CommonUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.AlertDialog;
import com.mci.firstidol.view.ReplyPopupWindow;
import com.mci.firstidol.view.ToastUtils;
import com.mci.firstidol.view.ReplyPopupWindow.OnReplyClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ChatAdapter extends BaseAdapter  implements OnReplyClickListener{
    private static final int TYPE_TEXT = 0;
    private static final int TYPE_PIC = 1;
    private static final int TYPE_AUDIO = 2;
    private static final int TYPE_VIDEO = 3;
    private static final int TYPE_MYSELF_TEXT = 4;
    private static final int TYPE_MYSELF_PIC = 5;
    private static final int TYPE_MYSELF_AUDIO = 6;
    private static final int TYPE_MYSELF_VIDEO = 7;
    private static final int VIEW_TYPE_COUNT = TYPE_MYSELF_VIDEO + 1;

    private static final float VIDEO_COVER_SCALE = 240f / 436f;

    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private DisplayImageOptions mMessageOptions;
    private Context mContext;
    private ArrayList<SquareLiveChatModel> mChats;
    private UserModel mUserInfo;
//    private DataEngineContext mDataEngineContext;

    private MediaPlayer mMediaPlayer;

    private long mUserRole;
    private long mRequestDisableId;
    private long mRequestTopId;
    private float mCoverWidth;
    private float mCoverHeight;
    private float mReferenceCoverWidth;
    private float mReferenceCoverHeight;
    private int mImageSpace;
    private int mImageSize;
    private int mReferenceImageSize;
    private long mType;
    
    private ReplyPopupWindow replyWindow;
	private String replyHint;
	
	private static int clickPosition;

    public ChatAdapter(Context context, ArrayList<SquareLiveChatModel> list) {
        mContext = context;
        mChats = list;
        mImageLoader = ImageLoader.getInstance();
        mOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).resetViewBeforeLoading(true).build();
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
        mReferenceCoverWidth = mCoverWidth - 2 * context.getResources().getDimension(R.dimen.dp10);
        mReferenceCoverHeight = mReferenceCoverWidth * VIDEO_COVER_SCALE;
        mImageSpace = (int) context.getResources().getDimension(R.dimen.dp5);
        mImageSize = (int) ((mCoverWidth - 2 * mImageSpace) / 3);
        mReferenceImageSize = (int) ((mReferenceCoverWidth - 2 * mImageSpace) / 3);
    }
    
    
    public void refershData(ArrayList<SquareLiveChatModel> contents) {
		// TODO Auto-generated method stub
    	mChats = contents;
		notifyDataSetChanged();
	}

    public void setUserInfo(UserModel userInfo) {
        mUserInfo = userInfo;
    }

    public void setUserRole(int userRole) {
        mUserRole = userRole;
    }

    public void setType(long type) {
        mType = type;
    }

    @Override
    public int getCount() {
        return mChats == null ? 10 : mChats.size();
//    	return mChats == null ? 10 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        SquareLiveChatModel chat = mChats.get(position);
//        chat.loadAnnexList();
//        chat.loadReferenceChat();
        if (mUserInfo != null && mUserInfo.UserId == chat.getUserId()) {
            
            if (chat.getLiveAnnexList() != null && !chat.getLiveAnnexList().isEmpty()) {
                switch ((int)chat.getLiveAnnexList().get(0).getAnnexType()) {
                    case 1:
                        return TYPE_MYSELF_PIC;
                    case 2:
                        return TYPE_MYSELF_AUDIO;
                    case 3:
                        return TYPE_MYSELF_VIDEO;
                    default:
                        return TYPE_MYSELF_TEXT;
                }
            } else {
            	return TYPE_MYSELF_TEXT;
            }
        } else {
            if (chat.getLiveAnnexList() != null && !chat.getLiveAnnexList().isEmpty()) {
                switch ((int)chat.getLiveAnnexList().get(0).getAnnexType()) {
                    case 1:
                        return TYPE_PIC;
                    case 2:
                        return TYPE_AUDIO;
                    case 3:
                        return TYPE_VIDEO;
                    default:
                        return TYPE_TEXT;
                }
            } else {
                return TYPE_TEXT;
            }
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (convertView == null) {
            ViewHolder holder = new ViewHolder();

            switch (viewType) {
                case TYPE_PIC:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.zhibo_message_pic_list_item, null);
                    holder.mOnePic = (ImageViewNext) convertView.findViewById(R.id.one_pic);
                    holder.mTwoPic = convertView.findViewById(R.id.two_pic);
                    holder.mThreePic = convertView.findViewById(R.id.three_pic);
                    break;
                case TYPE_AUDIO:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.zhibo_message_audio_list_item, null);
                    holder.mAudioView = convertView.findViewById(R.id.audio_view);
                    break;
                case TYPE_VIDEO:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.zhibo_message_video_list_item, null);
                    holder.mCover = (ImageView) convertView.findViewById(R.id.cover);
                    holder.mVideoTime = (TextView) convertView.findViewById(R.id.video_time);
                    break;
                case TYPE_MYSELF_TEXT:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.zhibo_myself_message_text_list_item, null);
                    break;
                case TYPE_MYSELF_PIC:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.zhibo_message_myself_pic_list_item, null);
                    holder.mOnePic = (ImageViewNext) convertView.findViewById(R.id.one_pic);
                    holder.mTwoPic = convertView.findViewById(R.id.two_pic);
                    holder.mThreePic = convertView.findViewById(R.id.three_pic);
                    break;
                case TYPE_MYSELF_AUDIO:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.zhibo_message_myself_audio_list_item, null);
                    holder.mAudioView = convertView.findViewById(R.id.audio_view);
                    break;
                case TYPE_MYSELF_VIDEO:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.zhibo_message_myself_video_list_item, null);
                    holder.mCover = (ImageView) convertView.findViewById(R.id.cover);
                    holder.mVideoTime = (TextView) convertView.findViewById(R.id.video_time);
                    break;
                default:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.zhibo_message_text_list_item, null);
                    break;
            }

            holder.left_part = (RelativeLayout) convertView.findViewById(R.id.left_part);
            
            holder.mContent = (TextView) convertView.findViewById(R.id.content);
            holder.mIcon = (ImageView) convertView.findViewById(R.id.icon);
            holder.mRoleIcon = (ImageView) convertView.findViewById(R.id.role_icon);
            holder.mTopIcon = (ImageView) convertView.findViewById(R.id.top_icon);
            holder.mDingIcon = (ImageView) convertView.findViewById(R.id.ding_icon);
            holder.mRole = (TextView) convertView.findViewById(R.id.role);
            holder.mTime = (TextView) convertView.findViewById(R.id.time);
            holder.mName = (TextView) convertView.findViewById(R.id.name);
            holder.mDing = (TextView) convertView.findViewById(R.id.ding);
            holder.mCai = (TextView) convertView.findViewById(R.id.cai);
            holder.mDingArea = convertView.findViewById(R.id.ding_area);
            holder.mCaiArea = convertView.findViewById(R.id.cai_area);

            holder.mTop = (ImageView) convertView.findViewById(R.id.top);
            holder.mDisable = (ImageView) convertView.findViewById(R.id.disable);
            holder.mDelete = (ImageView) convertView.findViewById(R.id.delete);
            holder.mReport = (ImageView) convertView.findViewById(R.id.report);

            holder.mReferenceView = convertView.findViewById(R.id.reference_item);
            holder.mReferencePicView = holder.mReferenceView.findViewById(R.id.reference_pic);
            holder.mReferenceAudioView = holder.mReferenceView.findViewById(R.id.reference_audio);
            holder.mReferenceVideoView = holder.mReferenceView.findViewById(R.id.reference_video);
            holder.mReferenceName = (TextView) convertView.findViewById(R.id.reference_name);
            holder.mReferenceContent = (TextView) holder.mReferenceView.findViewById(R.id.content);
            holder.mReferenceVideoTime = (TextView) holder.mReferenceView.findViewById(R.id.video_time);
            holder.mReferenceCover = (ImageView) holder.mReferenceView.findViewById(R.id.cover);
            holder.mReferenceOnePic = (ImageViewNext) holder.mReferenceView.findViewById(R.id.one_pic);
            holder.mReferenceTwoPic = holder.mReferenceView.findViewById(R.id.two_pic);
            holder.mReferenceThreePic = holder.mReferenceView.findViewById(R.id.three_pic);

            convertView.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) convertView.getTag();
        final SquareLiveChatModel chat = mChats.get(position);

        mImageLoader.displayImage(CommonUtils.getValidImageUrl(mContext, chat.getUserAvatar()), holder.mIcon, BaseApp.circleOptions,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (TextUtils.isEmpty(imageUri)) {
                            ((ImageView) view).setImageResource(R.drawable.default_circle_img);
                        }
                    }
                });

        //置顶按钮
        holder.mTop.setVisibility(mType == 0 ? (mUserRole == 1 ? View.VISIBLE : View.INVISIBLE) : View.GONE);
        //置顶标志
        holder.mTopIcon.setVisibility(mType == 0 ? (chat.getIsTop() == 1 ? View.VISIBLE : View.GONE) : View.GONE);
        //禁言按钮
//        holder.mDisable.setVisibility(mUserRole == 1 ? View.VISIBLE : View.INVISIBLE);
        holder.mDisable.setVisibility(View.INVISIBLE);
        //删除按钮
        holder.mDelete.setVisibility(mUserRole == 0 ? View.INVISIBLE : View.VISIBLE);
        //时间
        holder.mTime.setText(CommonUtils.getZhiboHeaderTime(chat.getCreateDate()));
        //昵称
        holder.mName.setText(chat.getUserNickName());
        //点赞数量
        holder.mDing.setText(String.valueOf(chat.getUpCount()));
        //回复数量
        holder.mCai.setText(String.valueOf(chat.getReplyCount()));
        
        //根据本条数据的用户角色来展示不同的头像信息
        switch ((int)chat.getUserRole()) {
            case 0://普通用户
                holder.mRole.setText("");
                holder.mRole.setVisibility(View.GONE);
                holder.mRoleIcon.setVisibility(View.GONE);
                break;
            case 1://主持人
                holder.mRole.setText("主持人");
                holder.mRole.setVisibility(View.VISIBLE);
                holder.mRoleIcon.setVisibility(View.VISIBLE);
                holder.mRoleIcon.setImageResource(R.drawable.zhuchiren_icon);
                break;
            case 2://嘉宾
                holder.mRole.setText("嘉宾");
                holder.mRole.setVisibility(View.VISIBLE);
                holder.mRoleIcon.setVisibility(View.VISIBLE);
                holder.mRoleIcon.setImageResource(R.drawable.jiabin_icon);
                break;
        }
        
        //根据当前登录用户的用户角色来显示不同的权限按钮
        initOperateViewShow(holder,chat);

        switch (viewType) {
            case TYPE_PIC:
            case TYPE_MYSELF_PIC:
                renderPicView(chat, holder.mContent, holder.mOnePic, holder.mTwoPic, holder.mThreePic, false);
                break;
            case TYPE_AUDIO:
            case TYPE_MYSELF_AUDIO:
                renderAudioView(chat, holder.mContent, holder.mAudioView, false);
                break;
            case TYPE_VIDEO:
            case TYPE_MYSELF_VIDEO:
                renderVideoView(chat, holder.mContent, holder.mVideoTime, holder.mCover, false);
                break;
            default:
                renderTextView(chat, holder.mContent);
                break;
        }

        renderReferenceView(holder, chat.getParentLiveChat());

        if (Configs.isSavedChatZan(mContext, chat.getArticleId(), chat.getChatId())) {
            holder.mDingArea.setEnabled(false);
            holder.mDingIcon.setImageResource(R.drawable.zhibo_zan_press);
        } else {
            holder.mDingArea.setEnabled(true);
            holder.mDingIcon.setImageResource(R.drawable.zhibo_zan_default);
        }
        
        holder.left_part.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putLong(Constant.IntentKey.userID, chat.getUserId());
				Utily.go2Activity(mContext, MyInfoActivity.class,bundle);
			}
		});
        
        holder.mDingArea.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//点赞
//                mDataEngineContext.postUpdateDingCount(chat.ChatId);
//                Configs.saveChatZan(mContext, chat.ArticleId, chat.ChatId);
//                chat.UpCount++;
//                v.setEnabled(false);
//                notifyDataSetChanged();
            	clickPosition = position;
            	squareLiveDetailUpRequest();
            }
        });

        holder.mCaiArea.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//回复
//                if (mUserInfo == null) {
//                    ((ZhiBoActivity) mContext).login();
//                } else {
//                    ((SquareLiveActivity) mContext).startUploadActivity(0, chat.ChatId);
//                }
            	
            	if (DataManager.getInstance().isLogin) {
            		clickPosition = position;
                	replyHint = "@"+ chat.getUserNickName();
                	popupReplyWindow(v);
				} else {
					Utily.go2Activity(mContext, LoginActivity.class);
				}
            	
            }
        });
        
        //举报
        holder.mReport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
	        	bundle.putString("ModType", "1");
	        	bundle.putLong("RefId", chat.getChatId());
	    		Utily.go2Activity(mContext, ReportActivity.class,
	    				bundle);
			}
		});
        
        
        holder.mDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	AlertDialog.show(mContext, "", "确定要删除这条内容?", "确定", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						clickPosition = position;
						obtainLiveChatDeleteEvent();
					}
				}, "取消", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});
            }
        });

        holder.mDisable.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	AlertDialog.show(mContext, "温馨提示", "确定要禁言用户\"" + chat.getUserNickName() + "\"?", "确定", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				}, "取消", null);
            }
        });
        
        holder.mTop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	final long isTop = chat.getIsTop();
            	
            	AlertDialog.show(mContext, "", isTop == 1 ? "确定要取消置顶?":"确定要置顶这条内容?", "确定", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						clickPosition = position;
						if (isTop == 1) {//取消置顶
							obtainLiveChatTopEvent(false);
						} else {//置顶
							obtainLiveChatTopEvent(true);
						}
					}
				}, "取消", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});
          
            }
        });

        return convertView;
    }


	private void initOperateViewShow(ViewHolder holder, SquareLiveChatModel chat) {
		// TODO Auto-generated method stub
        
        switch (SquareLiveActivity.userRole) {
        case 0://普通用户
            //普通用户只显示回复按钮、回复数量、赞数量，当只有自己发布的数据显示删除按钮
        	holder.mTop.setVisibility(View.INVISIBLE);
        	if (mUserInfo != null && mUserInfo.UserId == chat.getUserId()) {//登录用户发布的
        		holder.mDelete.setVisibility(View.VISIBLE);
			} else {
				holder.mDelete.setVisibility(View.INVISIBLE);
			}
        	
            break;
        case 1://主持人
        case 2://嘉宾
        	//主持人 隐藏置顶， 显示删除按钮、回复按钮、回复数量、赞数量
        	
        	if (SquareLiveActivity.currentPosition == 0) {//LIVE
				holder.mTop.setVisibility(View.VISIBLE);
				holder.mDelete.setVisibility(View.VISIBLE);
			} else {//CHAT
				holder.mTop.setVisibility(View.INVISIBLE);
				holder.mDelete.setVisibility(View.VISIBLE);
			}
        	
//        	if (SquareLiveActivity.currentPosition == 1 && mUserInfo != null && mUserInfo.UserId == chat.getUserId()) {
//        		//自己发布的显示删除按钮
//        		holder.mTop.setVisibility(View.INVISIBLE);
//        		holder.mDelete.setVisibility(View.VISIBLE);
//			} else if (SquareLiveActivity.currentPosition == 0) {//LIVE显示，
//				holder.mTop.setVisibility(View.VISIBLE);
//				holder.mDelete.setVisibility(View.VISIBLE);
//			} else {//CHAT 隐藏
//				holder.mTop.setVisibility(View.INVISIBLE);
//				holder.mDelete.setVisibility(View.INVISIBLE);
//			}
            break;
        
//        	//嘉宾同主持人
//        	if (SquareLiveActivity.currentPosition == 0) {
//        		holder.mTop.setVisibility(View.VISIBLE);
//            	holder.mDelete.setVisibility(View.VISIBLE);
//			} else {
//				holder.mTop.setVisibility(View.INVISIBLE);
//	        	holder.mDelete.setVisibility(View.VISIBLE);
//			}
//        	
//            break;
    }
	}


	private void renderPicView(final SquareLiveChatModel chat, TextView content, ImageViewNext onePic, View twoPic, View threePic,
            boolean isReference) {
        content.setText(chat.getChatContent());
        if (chat.getLiveAnnexList().size() == 1) {
            onePic.setVisibility(View.VISIBLE);
            twoPic.setVisibility(View.GONE);
            threePic.setVisibility(View.GONE);

            LiveAnnex attachment = chat.getLiveAnnexList().get(0);
            RelativeLayout.LayoutParams lp = (LayoutParams) onePic.getLayoutParams();
            if (lp != null) {
                if (isReference) {
                    lp.width = mReferenceImageSize;
                    lp.height = mReferenceImageSize;
                } else {
                    lp.width = mImageSize;
                    lp.height = mImageSize;
                }

                onePic.setLayoutParams(lp);
            }
            
//            if (attachment.getAnnexUrl().indexOf(".gif")!=-1) {
//            	onePic.setUrl(attachment.getAnnexUrl());
//			} else {
				mImageLoader.displayImage(CommonUtils.getValidImageUrl(mContext, attachment.getAnnexUrl()), onePic, mMessageOptions);
//			}
            
            onePic.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startImageActivity(0, chat.getLiveAnnexList());
                }
            });
        } else if (chat.getLiveAnnexList().size() == 2) {
            onePic.setVisibility(View.GONE);
            twoPic.setVisibility(View.VISIBLE);
            threePic.setVisibility(View.GONE);

            ImageViewNext image1 = (ImageViewNext) twoPic.findViewById(R.id.two_pic_1);
            ImageViewNext image2 = (ImageViewNext) twoPic.findViewById(R.id.two_pic_2);
            LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) image1.getLayoutParams();
            if (lp != null) {
                if (isReference) {
                    lp.width = mReferenceImageSize;
                    lp.height = mReferenceImageSize;
                } else {
                    lp.width = mImageSize;
                    lp.height = mImageSize;
                }

                lp.rightMargin = mImageSpace;
                image1.setLayoutParams(lp);
                image2.setLayoutParams(lp);
            }

            final String url1 = CommonUtils.getValidImageUrl(mContext, chat.getLiveAnnexList().get(0).getAnnexUrl());
            final String url2 = CommonUtils.getValidImageUrl(mContext, chat.getLiveAnnexList().get(1).getAnnexUrl());
            
//            if (url1.indexOf(".gif")!=-1) {
//            	image1.setUrl(url1);
//			} else {
				mImageLoader.displayImage(url1, image1, mMessageOptions);
//			}
            
//            if (url2.indexOf(".gif")!=-1) {
//            	image2.setUrl(url2);
//			} else {
				mImageLoader.displayImage(url2, image2, mMessageOptions);
//			}
//            mImageLoader.displayImage(url1, image1, mMessageOptions);
//            mImageLoader.displayImage(url2, image2, mMessageOptions);
            image1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startImageActivity(0, chat.getLiveAnnexList());
                }
            });
            image2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startImageActivity(1, chat.getLiveAnnexList());
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
                if (isReference) {
                    lp.width = mReferenceImageSize;
                    lp.height = mReferenceImageSize;
                } else {
                    lp.width = mImageSize;
                    lp.height = mImageSize;
                }

                lp.rightMargin = mImageSpace;
                image1.setLayoutParams(lp);
                image2.setLayoutParams(lp);
            }
            ImageViewNext image3 = (ImageViewNext) threePic.findViewById(R.id.three_pic_3);
            LinearLayout.LayoutParams lp2 = (android.widget.LinearLayout.LayoutParams) image3.getLayoutParams();
            if (lp2 != null) {
                if (isReference) {
                    lp2.width = mReferenceImageSize;
                    lp2.height = mReferenceImageSize;
                } else {
                    lp2.width = mImageSize;
                    lp2.height = mImageSize;
                }

                image3.setLayoutParams(lp);
            }

            final String url1 = CommonUtils.getValidImageUrl(mContext, chat.getLiveAnnexList().get(0).getAnnexUrl());
            final String url2 = CommonUtils.getValidImageUrl(mContext, chat.getLiveAnnexList().get(1).getAnnexUrl());
            final String url3 = CommonUtils.getValidImageUrl(mContext, chat.getLiveAnnexList().get(2).getAnnexUrl());
//            mImageLoader.displayImage(url1, image1, mMessageOptions);
//            mImageLoader.displayImage(url2, image2, mMessageOptions);
//            mImageLoader.displayImage(url3, image3, mMessageOptions);
            
//            if (url1.indexOf(".gif")!=-1) {
//            	image1.setUrl(url1);
//			} else {
				mImageLoader.displayImage(url1, image1, mMessageOptions);
//			}
            
//            if (url2.indexOf(".gif")!=-1) {
//            	image2.setUrl(url2);
//			} else {
				mImageLoader.displayImage(url2, image2, mMessageOptions);
//			}
            
//            if (url3.indexOf(".gif")!=-1) {
//            	image3.setUrl(url3);
//			} else {
				mImageLoader.displayImage(url3, image3, mMessageOptions);
//			}
            
            image1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startImageActivity(0, chat.getLiveAnnexList());
                }
            });
            image2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startImageActivity(1, chat.getLiveAnnexList());
                }
            });
            image3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startImageActivity(2, chat.getLiveAnnexList());
                }
            });
        }
    }

    private void renderAudioView(SquareLiveChatModel chat, TextView content, View view, boolean isReference) {
        if (TextUtils.isEmpty(chat.getChatContent())) {
            content.setVisibility(View.GONE);
        } else {
            content.setVisibility(View.VISIBLE);
            content.setText(chat.getChatContent());
        }

        final LiveAnnex attachment = chat.getLiveAnnexList().get(0);
        int time = 1;
        if (!TextUtils.isEmpty(attachment.getDuration()) && attachment.getDuration().contains("\"")) {
            try {
                time = Integer.parseInt(attachment.getDuration().replace("\"", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LinearLayout background = (LinearLayout) view.findViewById(R.id.audio_bg);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) background.getLayoutParams();
        if (lp != null) {
            if (isReference) {
                lp.width = (int) (mReferenceCoverWidth / 3 + (float) time / (float) 60 * (mReferenceCoverWidth / 3));
            } else {
                lp.width = (int) (mCoverWidth / 3 + (float) time / (float) 60 * (mCoverWidth / 3));
            }
        }
        background.setLayoutParams(lp);

        final ImageView audioIcon = (ImageView) view.findViewById(R.id.audio_icon);
        TextView audioTime = (TextView) view.findViewById(R.id.audio_time);
        audioTime.setText((TextUtils.isEmpty(attachment.getDuration())?"":attachment.getDuration())+"");
        background.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio(attachment.getAnnexUrl(), audioIcon);
            }
        });
    }

    private void renderVideoView(SquareLiveChatModel chat, TextView content, TextView videoTime, ImageView cover, boolean isReference) {
        final LiveAnnex attachment = chat.getLiveAnnexList().get(0);
        content.setText(chat.getChatContent());
        videoTime.setText(TextUtils.isEmpty(attachment.getDuration())?"" : attachment.getDuration() +"");

        float scale = 1;
        if (attachment.getOrigWidth() > 0 && attachment.getOrigHeight() > 0) {
            scale = (float) attachment.getOrigWidth() / (float) attachment.getOrigHeight();
        }
        RelativeLayout.LayoutParams lp = (LayoutParams) cover.getLayoutParams();
        if (lp != null) {
            if (isReference) {
                lp.width = (int) (mReferenceCoverHeight * scale);
                lp.height = (int) mReferenceCoverHeight;
            } else {
                lp.width = (int) (mReferenceCoverHeight * scale);
                lp.height = (int) mCoverHeight;
            }
            cover.setLayoutParams(lp);
        }

        mImageLoader.displayImage(CommonUtils.getValidImageUrl(mContext, attachment.getVideoCover()), cover, mMessageOptions);
        cover.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(attachment.getAnnexUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "video/mp4");
                mContext.startActivity(intent);
            }
        });
    }

    private void renderTextView(SquareLiveChatModel chat, TextView content) {
        content.setText(chat.getChatContent());
    }

    private void renderReferenceView(ViewHolder holder, SquareLiveChatModel chat) {
        if (chat == null) {
            holder.mReferenceView.setVisibility(View.GONE);
        } else {
            holder.mReferenceView.setVisibility(View.VISIBLE);
            int viewType = 0;
            if (chat.getLiveAnnexList() != null && !chat.getLiveAnnexList().isEmpty()) {
                viewType = (int)chat.getLiveAnnexList().get(0).getAnnexType();
            }

            holder.mReferenceName.setText(chat.getUserNickName());
            switch (viewType) {
                case TYPE_PIC:
                    holder.mReferenceContent.setVisibility(View.VISIBLE);
                    holder.mReferencePicView.setVisibility(View.VISIBLE);
                    holder.mReferenceAudioView.setVisibility(View.GONE);
                    holder.mReferenceVideoView.setVisibility(View.GONE);
                    renderPicView(chat, holder.mReferenceContent, holder.mReferenceOnePic, holder.mReferenceTwoPic,
                            holder.mReferenceThreePic, true);
                    break;
                case TYPE_AUDIO:
                    holder.mReferenceContent.setVisibility(View.GONE);
                    holder.mReferencePicView.setVisibility(View.GONE);
                    holder.mReferenceAudioView.setVisibility(View.VISIBLE);
                    holder.mReferenceVideoView.setVisibility(View.GONE);
                    renderAudioView(chat, holder.mReferenceContent, holder.mReferenceAudioView, true);
                    break;
                case TYPE_VIDEO:
                    holder.mReferenceContent.setVisibility(View.VISIBLE);
                    holder.mReferencePicView.setVisibility(View.GONE);
                    holder.mReferenceAudioView.setVisibility(View.GONE);
                    holder.mReferenceVideoView.setVisibility(View.VISIBLE);
                    renderVideoView(chat, holder.mReferenceContent, holder.mReferenceVideoTime, holder.mReferenceCover, true);
                    break;
                default:
                    holder.mReferenceContent.setVisibility(View.VISIBLE);
                    holder.mReferencePicView.setVisibility(View.GONE);
                    holder.mReferenceAudioView.setVisibility(View.GONE);
                    holder.mReferenceVideoView.setVisibility(View.GONE);
                    renderTextView(chat, holder.mReferenceContent);
                    break;
            }
        }
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
                        	mMediaPlayer.start();
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
                    mMediaPlayer.prepareAsync();
//                    mMediaPlayer.start();
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
                CommonUtils.showToast(mContext, "错误的音频地址");
            }
        } catch (Exception e) {
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            view.setImageResource(R.drawable.audio_state_1);
        }
    }

    private void startImageActivity(int position, ArrayList<LiveAnnex> attachments) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putSerializable("attachments", attachments);
        Utily.go2Activity(mContext, ZhiboPicturesActivity.class, bundle);
        
    }
    
    
    
    
    
    
    private void popupReplyWindow(View v) {
		// TODO Auto-generated method stub
		// 实例化PopupWindow
		if (replyWindow == null) {
			replyWindow = new ReplyPopupWindow(mContext);
			replyWindow.setOnReplyClickListener(this);
		}
		replyWindow.setTextHint(replyHint);
		// 显示窗口
		replyWindow.showAtLocation(v, Gravity.TOP
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		
		
		((SquareLiveActivity)mContext).openKeyboard();
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
    	
    	
    	
		obtainChatSaveRequest();
	}
    
    
    /**
     * 保存回复信息
     * @param attachments
     */
	protected void obtainChatSaveRequest() {
		// TODO Auto-generated method stub
		
		
		SquareLiveChatSaveRequest request = SquareRequest.squareLiveChatSaveRequest();// 其它参数需要继承此类
		request.requestMethod = Constant.Request_POST;
		request.liveChat = initLiveChatModel();
		request.liveAnnexList = null;
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
//				ToastUtils.showCustomToast("评论成功");
				ToastUtils.showPointToast("5");//回复加5分
				
				mChats.get(clickPosition).setReplyCount(mChats.get(clickPosition).getReplyCount()+1);
				notifyDataSetChanged();
			}
		});
	}
	
	private SquareLiveChatModel initLiveChatModel() {
		// TODO Auto-generated method stub
		SquareLiveChatModel chat = mChats.get(clickPosition);
		
		SquareLiveChatModel squareLiveChatModel = new SquareModel().new SquareLiveChatModel();
		squareLiveChatModel.setArticleId(chat.getArticleId());
		squareLiveChatModel.setChatContent(replyHint +" " + replyWindow.getMessage().toString());
		squareLiveChatModel.setUserId(DataManager.getInstance().userModel.UserId);
		squareLiveChatModel.setUserRole(SquareLiveActivity.userRole);
		squareLiveChatModel.setParentId(chat.getChatId());
		squareLiveChatModel.setUserAvatar(DataManager.getInstance().userModel.Avatar);
		squareLiveChatModel.setUserNickName(DataManager.getInstance().userModel.NickName);
		
		return squareLiveChatModel;
	}
	
	
	
	/**
	 * 评论点赞
	 * 
	 */
	public void squareLiveDetailUpRequest(){
		final SquareLiveChatModel chat = mChats.get(clickPosition);
		
		SquareLiveDetailUpRequest request = SquareRequest.squareLiveDetailUpRequest();
		request.requestMethod = Constant.Request_POST;
		request.chatId = chat.getChatId();
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
				Configs.saveChatZan(mContext, chat.getArticleId(), chat.getChatId());
				chat.setUpCount((long)Float.parseFloat(result));
				notifyDataSetChanged();
			}
		});
	}
	
	
	/**
	 * 置顶、取消置顶
	 * @param position
	 */
	private void obtainLiveChatTopEvent(final boolean isTop) {
		// TODO Auto-generated method stub
		final SquareLiveChatModel chat = mChats.get(clickPosition);
		
		SquareLiveDetailDeleteRequest request = SquareRequest.squareLiveDetailDeleteRequest();
		request.requestMethod = Constant.Request_POST;
		request.chatId = chat.getChatId();
		//请求地址
		String requestID = isTop ? Constant.RequestContstants.Request_LiveChat_Top : Constant.RequestContstants.Request_LiveChat_CancelTop;		
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
				ToastUtils.showCustomToast(isTop?"置顶成功":"取消置顶成功");
				if (isTop) {
					mChats.get(clickPosition).setIsTop(1);
				} else {
					mChats.get(clickPosition).setIsTop(0);
				}
				
				notifyDataSetChanged();
				
			}
		});
	}
	
	/**
	 * 删除发布信息
	 * @param position
	 */
	private void obtainLiveChatDeleteEvent() {
		// TODO Auto-generated method stub
		final SquareLiveChatModel chat = mChats.get(clickPosition);
		
		SquareLiveDetailDeleteRequest request = SquareRequest.squareLiveDetailDeleteRequest();
		request.requestMethod = Constant.Request_POST;
		request.chatId = chat.getChatId();
		request.articleId = chat.getArticleId();
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
				//成功时，更改图标状态
				mChats.remove(clickPosition);
				notifyDataSetChanged();
			}
		});
	}
    
//
//    @Override
//    public void onMessage(Message msg) {
//        switch (msg.what) {
//            case MessageCode.POST_DISABLE_SEND_MESSAGE:
//                if (msg.getData().getInt(Utils.KEY_REQUEST_ID) == mRequestDisableId) {
//                    mRequestDisableId = Utils.INVALID_ID;
//                    if (msg.obj != null) {
//                        CommonRes res = (CommonRes) msg.obj;
//                        if (res.isSuc) {
//                            CommonUtils.showToast(mContext, "禁言成功");
//                        } else {
//                            CommonUtils.showToast(mContext, "禁言失败");
//                        }
//                    } else {
//                        CommonUtils.showToast(mContext, "禁言失败");
//                    }
//                }
//                break;
//            case MessageCode.POST_TOP_CHAT:
//                if (msg.getData().getInt(Utils.KEY_REQUEST_ID) == mRequestTopId) {
//                    mRequestTopId = Utils.INVALID_ID;
//                    if (msg.obj != null) {
//                        CommonRes res = (CommonRes) msg.obj;
//                        if (res.isSuc) {
//                            CommonUtils.showToast(mContext, "置顶成功");
//                        } else {
//                            CommonUtils.showToast(mContext, "置顶失败");
//                        }
//                    } else {
//                        CommonUtils.showToast(mContext, "置顶失败");
//                    }
//                }
//                break;
//        }
//    }

    public class ViewHolder {
        public ImageView mIcon;
        public ImageView mCover;
        public ImageView mRoleIcon;
        public ImageView mTopIcon;
        public TextView mRole;
        public TextView mTime;
        public TextView mName;
        public TextView mContent;
        public TextView mDing;
        public ImageView mDingIcon;
        public View mDingArea;
        public TextView mCai;
        public View mCaiArea;
        public TextView mVideoTime;
        public ImageViewNext mOnePic;
        public View mTwoPic;
        public View mThreePic;
        public View mAudioView;

        public ImageView mTop;
        public ImageView mDisable;
        public ImageView mDelete;
        public ImageView mReport;

        public View mReferenceView;
        public View mReferencePicView;
        public View mReferenceAudioView;
        public View mReferenceVideoView;
        public TextView mReferenceName;
        public TextView mReferenceContent;
        public TextView mReferenceVideoTime;
        public ImageView mReferenceCover;
        public ImageViewNext mReferenceOnePic;
        public View mReferenceTwoPic;
        public View mReferenceThreePic;
        
        public RelativeLayout left_part;
    }
    
    protected <T> AsyncTask doAsync(final boolean isShowPorgress,
			final CharSequence requestID, final Object requestObj,
			final Callback<Exception> pExceptionCallback,
			final Callback<T> pCallback) {
		return AsyncTaskUtils.doAsync(mContext, null,
				mContext.getResources().getString(R.string.request_loading), requestID,
				requestObj, pCallback, pExceptionCallback, true,
				isShowPorgress);
	}

	
}
