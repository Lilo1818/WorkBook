package com.mci.firstidol.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mci.firstidol.R;
import com.mci.firstidol.activity.MyInfoActivity;
import com.mci.firstidol.adapter.FollowAdapter.ViewHolder;
import com.mci.firstidol.base.MsBaseAdapter;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.FansModel;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FansAdapter extends MsBaseAdapter {

	private final int CANCLE_FOLLOW = 1;//取消关注
	private Handler handler;
	
	public FansAdapter(Context context, List<Object> modelList,
			ListView listView,Handler handler) {
		super(context, modelList, listView);
		this.handler = handler;
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.item_fans,
					parent, false);
			viewHolder.icon = (RoundedImageView) convertView
					.findViewById(R.id.icon);
			viewHolder.image_follow = (ImageView) convertView
					.findViewById(R.id.image_follow);
			viewHolder.text_dynamic = (TextView) convertView
					.findViewById(R.id.text_dynamic);
			viewHolder.text_name = (TextView) convertView
					.findViewById(R.id.text_name);
			viewHolder.text_fans = (TextView) convertView
					.findViewById(R.id.text_fans);
			viewHolder.text_follow = (TextView) convertView
					.findViewById(R.id.text_follow);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		FansModel fansModel = (FansModel) modelList.get(position);

		String url = fansModel.Avatar;
		ImageLoader.getInstance().displayImage(url,
				viewHolder.icon);

		if (fansModel.IsFollow) {
			viewHolder.image_follow
			.setBackgroundResource(R.drawable.cancle_follow);
		} else {
			viewHolder.image_follow
			.setBackgroundResource(R.drawable.follow);
		}

		viewHolder.text_name.setText(fansModel.NickName);
		viewHolder.text_dynamic.setText(mContext.getResources().getString(
				R.string.dynamic)
				+ fansModel.DynamicCount);
		viewHolder.text_follow.setText(mContext.getResources().getString(
				R.string.follow)
				+ fansModel.UserFollowCount);
		viewHolder.text_fans.setText(mContext.getResources().getString(
				R.string.fans)
				+ fansModel.FansCount);

	}

	@Override
	public void bindEvent(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		final FansModel fansModel = (FansModel) modelList.get(position);
		viewHolder.image_follow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(handler!=null){
					Message message = new Message();
					message.what = CANCLE_FOLLOW;
					message.obj = position;
					handler.sendMessage(message);
				}
				
			}
		});
		viewHolder.icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HashMap<String, String> params = new HashMap<String,String>();
				params.put(Constant.IntentKey.userID, fansModel.UserId);
				Utily.go2Activity(mContext, MyInfoActivity.class, params, null);
			}
		});
		
	}

	class ViewHolder {
		public RoundedImageView icon;
		public TextView text_name;
		public ImageView image_follow;
		public TextView text_follow;
		public TextView text_fans;
		public TextView text_dynamic;
	}

}
