package com.mci.firstidol.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.mci.firstidol.activity.MyInfoActivity;
import com.mci.firstidol.activity.ReportActivity;
import com.mci.firstidol.activity.SquareLiveChatDetailActivity;
import com.mci.firstidol.adapter.SquareFoundDetailListViewAdapter.SquareFoundDetailListViewAdapterListener;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.fragment.welfare.Configs;
import com.mci.firstidol.model.CommentModel;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.SquareModel.SquareFoundModel;
import com.mci.firstidol.model.SquareModel.SquareLiveChatModel;
import com.mci.firstidol.model.SquareRequest.SquareLiveDetailDeleteRequest;
import com.mci.firstidol.utils.AsyncTaskUtils;
import com.mci.firstidol.utils.DateHelper;
import com.mci.firstidol.utils.DeviceUtils;
import com.mci.firstidol.utils.DisplayUtil;
import com.mci.firstidol.utils.LogUtils;
import com.mci.firstidol.utils.PreferencesUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.AlertDialog;
import com.mci.firstidol.view.PopAnimView;
import com.mci.firstidol.view.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SquareLiveChatCommentListViewAdapter extends BaseAdapter implements OnClickListener{

	private Context context;
	private ArrayList<SquareLiveChatModel> listData;
	private LayoutInflater inflater;
	private CommentModel commentModel;
	private boolean isShowRight;
	
	private SquareLiveChatCommentListViewAdapterListener squareLiveChatCommentListViewAdapterListener;

	public SquareLiveChatCommentListViewAdapter(Context context,
			ArrayList<SquareLiveChatModel> listData, boolean isShowRight) {
		super();
		this.context = context;
		this.listData = listData;
		this.isShowRight = isShowRight;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (listData != null) {
			return listData.size();
		} else {
			return 0;
		}
	}

	@Override
	public SquareLiveChatModel getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setListData(ArrayList<SquareLiveChatModel> listData) {
		this.listData = listData;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView instanceof TextView) {// 这是无数据时，添加的缓存view，有数据时需要清除，否则不为空时，影响下面的判断
			convertView = null;
		}

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_square_found_detail, null);
			holder.iv_user_img = (ImageView) convertView
					.findViewById(R.id.iv_user_img);
			holder.tv_nickname = (android.widget.TextView) convertView
					.findViewById(R.id.tv_nickname);
			holder.tv_time = (android.widget.TextView) convertView
					.findViewById(R.id.tv_time);
			holder.tv_content = (android.widget.TextView) convertView
					.findViewById(R.id.tv_content);
			holder.tv_collection_operate = (TextView) convertView
					.findViewById(R.id.tv_collection_operate);
			holder.report = (ImageButton)convertView
					.findViewById(R.id.item_imaget_report);
			
			holder.rl_operate = (RelativeLayout) convertView.findViewById(R.id.rl_operate);
			holder.btn_operate = (Button) convertView.findViewById(R.id.btn_operate);
			holder.btn_delete = (Button) convertView.findViewById(R.id.btn_delete);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		
		
		if (listData != null && listData.size() > 0) {
			final SquareLiveChatModel commentModel = listData.get(position);

			ImageLoader.getInstance().displayImage(
					commentModel.getUserAvatar(), holder.iv_user_img,
					BaseApp.circleOptions);
			holder.tv_nickname.setText(commentModel.getUserNickName());;
			holder.tv_time.setText(DateHelper.intervalSinceNow(DateHelper.getStringFromDateStr(commentModel.getCreateDate(), "yyyy-MM-dd HH:mm:ss")));
			holder.tv_content.setText(commentModel.getChatContent());
			holder.tv_collection_operate.setText(commentModel.getUpCount()+"");
			
			
			if (commentModel.getUserId() == DataManager.getInstance().userModel.UserId) {
				holder.btn_delete.setVisibility(View.VISIBLE);
			} else {
				holder.btn_delete.setVisibility(View.INVISIBLE);
			}
			
//			if (Configs.isSavedChatZan(mContext, chat.getArticleId(), chat.getChatId())) {
//	            holder.mDingArea.setEnabled(false);
//	            holder.mDingIcon.setImageResource(R.drawable.zhibo_zan_press);
//	        } else {
//	            holder.mDingArea.setEnabled(true);
//	            holder.mDingIcon.setImageResource(R.drawable.zhibo_zan_default);
//	        }
			
			holder.report.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Bundle bundle = new Bundle();
		        	bundle.putString("ModType", "2");
		        	bundle.putLong("RefId", commentModel.getCommentId());
		    		Utily.go2Activity(context, ReportActivity.class,
		    				bundle);
				}
			});
			
			holder.btn_operate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					squareLiveChatCommentListViewAdapterListener.squareLiveChatCommentLikeClick(position);
				}
			});
			
			holder.iv_user_img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					ToastUtils.showCustomToast("111111");
					Bundle bundle = new Bundle();
					bundle.putLong(Constant.IntentKey.userID, listData.get(position).getUserId());
					Utily.go2Activity(context, MyInfoActivity.class,bundle);
				}
			});
			
			holder.btn_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showDeleteHint(position);
				}
			});
			
			if (!Configs.isSavedChatZan(context, commentModel.getArticleId(), commentModel.getChatId())) {
				holder.tv_collection_operate.setEnabled(true);
				holder.btn_operate.setClickable(true);
				holder.tv_collection_operate.setTextColor(context.getResources().getColor(R.color.color_gray_02));
				Drawable drawable= context.getResources().getDrawable(R.drawable.square_found_detail_recommend);
				/// 这一步必须要做,否则不会显示.
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				holder.tv_collection_operate.setCompoundDrawables(drawable,null,null,null);
			} else {
				holder.btn_operate.setClickable(false);
				holder.tv_collection_operate.setTextColor(context.getResources().getColor(R.color.color_purple_01));
				Drawable drawable= context.getResources().getDrawable(R.drawable.square_found_detail_recommend_red);
				/// 这一步必须要做,否则不会显示.
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				holder.tv_collection_operate.setCompoundDrawables(drawable,null,null,null);
			}
		}
		
		

		return convertView;
	}
	
	
	private void showDeleteHint(final int position) {
		// TODO Auto-generated method stub
		AlertDialog.show(context, "", "确定要删除这条内容?", "确定", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				obtainLiveChatDeleteEvent(position);
			}
		}, "取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	protected void obtainLiveChatDeleteEvent(final int position) {
		// TODO Auto-generated method stub
		SquareLiveChatModel  squareLiveChatModel = listData.get(position);
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
				//成功时
				listData.remove(position);
				notifyDataSetChanged();
			}
		});
	}

	public <T> AsyncTask doAsync(final boolean isShowPorgress,
			final CharSequence requestID, final Object requestObj,
			final Callback<Exception> pExceptionCallback,
			final Callback<T> pCallback) {
		return AsyncTaskUtils.doAsync(context, null,
				context.getResources().getString(R.string.request_loading), requestID,
				requestObj, pCallback, pExceptionCallback, true,
				isShowPorgress);
	}
	public class ViewHolder {
		ImageView iv_user_img;// 用户图标
		TextView tv_nickname;// 昵称
		TextView tv_time;// 发表时间
		TextView tv_content;// 发表内容
		TextView tv_collection_operate;//收藏图标 
		RelativeLayout rl_operate;
		Button btn_operate;
		Button btn_delete;
		
		ImageButton report;
		
		PopAnimView view_meng;
		PopAnimView view_tian;
		PopAnimView view_jia;
		PopAnimView view_shuai;
	}

	public void refershData(ArrayList<SquareLiveChatModel> contents) {
		// TODO Auto-generated method stub
		listData = contents;
		notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setSquareLiveChatCommentListViewAdapterListener(
			SquareLiveChatCommentListViewAdapterListener squareLiveChatCommentListViewAdapterListener) {
		this.squareLiveChatCommentListViewAdapterListener = squareLiveChatCommentListViewAdapterListener;
	}
	
	public interface SquareLiveChatCommentListViewAdapterListener{
		public void squareLiveChatCommentLikeClick(int position);
	}

}
