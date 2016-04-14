package com.mci.firstidol.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mci.firstidol.R;
import com.mci.firstidol.activity.MyInfoActivity;
import com.mci.firstidol.adapter.MyNoticeAdapter.ViewHolder;
import com.mci.firstidol.base.MsBaseAdapter;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.CollectionModel;
import com.mci.firstidol.model.NoticeModel;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyCollectionAdapter extends MsBaseAdapter {

	public MyCollectionAdapter(Context context, List<Object> modelList,
			ListView listView) {
		super(context, modelList, listView);
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.item_collection,
					parent, false);
			viewHolder.icon = (RoundedImageView) convertView.findViewById(R.id.icon);
			viewHolder.image_back = (ImageView) convertView.findViewById(R.id.image_back);
			viewHolder.text_name = (TextView) convertView.findViewById(R.id.text_name);
			viewHolder.text_viewCount = (TextView) convertView.findViewById(R.id.text_seeCount);
			viewHolder.text_zanCount = (TextView) convertView.findViewById(R.id.text_zanCount);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}
	
	
	
	
	
	@Override
	public void bindData(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		CollectionModel collectionModel = (CollectionModel) modelList.get(position);
		String icon_url = collectionModel.UserAvatar;
		String back_url = collectionModel.Ico;
		String back_urll = collectionModel.VideoIco;
		//Toast.makeText(mContext, "值为+"+back_urll, Toast.LENGTH_SHORT).show();
/*		if(icon_url.equals("")||icon_url.equals(null)){
			viewHolder.icon.setImageResource(R.drawable.follower_head);
		}else{
			ImageLoader.getInstance().displayImage(icon_url,
					viewHolder.icon);
		}*/
		if(icon_url == null || icon_url.length() <= 0){
			viewHolder.icon.setImageResource(R.drawable.follower_head);
		}else{
			ImageLoader.getInstance().displayImage(icon_url,
					viewHolder.icon);
		}
/*		ImageLoader.getInstance().displayImage(icon_url,
				viewHolder.icon);*/
		if(back_urll==null||back_urll.length()<=0){
			ImageLoader.getInstance().displayImage(back_url,
					viewHolder.image_back);
		}else{
			ImageLoader.getInstance().displayImage(back_urll,
					viewHolder.image_back);
		}
		/*ImageLoader.getInstance().displayImage(back_url,
				viewHolder.image_back);*/
		
		viewHolder.text_name.setText(collectionModel.Title);
		viewHolder.text_viewCount.setText(collectionModel.ViewCount);
		viewHolder.text_zanCount.setText(collectionModel.UpCount+"个人收藏");
	}

	@Override
	public void bindEvent(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		CollectionModel noticeModel = (CollectionModel) modelList.get(position);
		final String userId = noticeModel.UserId;
	    viewHolder.icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HashMap<String, String> params = new HashMap<String,String>();
				params.put(Constant.IntentKey.userID, userId);
				Utily.go2Activity(mContext, MyInfoActivity.class, params, null);
			}
		});
	}
	
	class ViewHolder{
		public RoundedImageView icon;
		public TextView text_name;
		public TextView text_viewCount;
		public TextView text_zanCount;
		public ImageView image_back;
	}

}
