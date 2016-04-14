package com.mci.firstidol.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
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
import com.mci.firstidol.model.MyCommentModel;
import com.mci.firstidol.model.NoticeModel;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("SimpleDateFormat")
public class MyCommentAdapter extends MsBaseAdapter {

	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
	
	public MyCommentAdapter(Context context, List<Object> modelList,
			ListView listView) {
		super(context, modelList, listView);
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView==null){
			convertView = mLayoutInflater.inflate(R.layout.item_mycomment,
					parent, false);
			viewHolder = new ViewHolder();
			viewHolder.icon = (RoundedImageView) convertView.findViewById(R.id.icon);
			viewHolder.image_back = (ImageView) convertView.findViewById(R.id.image_back);
			viewHolder.text_name = (TextView) convertView.findViewById(R.id.text_name);
			viewHolder.text_time = (TextView) convertView.findViewById(R.id.text_time);
			viewHolder.text_title = (TextView) convertView.findViewById(R.id.text_title);
            convertView.setTag(viewHolder);			
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		MyCommentModel commentModel = (MyCommentModel) modelList.get(position);
		
		String icon_url = commentModel.UserAvatar;
		String back_url = commentModel.Ico;
		String back_urll = commentModel.VideoIco;
		
		//Toast.makeText(mContext, "值为+"+back_urll, Toast.LENGTH_SHORT).show();
		ImageLoader.getInstance().displayImage(icon_url,
				viewHolder.icon);
		
		if(back_urll==null||back_urll.length()<=0){
			ImageLoader.getInstance().displayImage(back_url,
					viewHolder.image_back);
		}else{
			ImageLoader.getInstance().displayImage(back_urll,
					viewHolder.image_back);
		}
		
		
		if(commentModel.Title.length()<=0){
			viewHolder.text_name.setText("(无标题)");
		}else{
			viewHolder.text_name.setText(commentModel.Title);
		}
		
		viewHolder.text_title.setText(commentModel.Content);

		String publishData = commentModel.CreateDate;
		try {
			if(publishData!=null&&publishData.length()>10){
				String dateStr = publishData.substring(6, publishData.length()-2);
				viewHolder.text_time.setText(sdf.format(new Date(Long.parseLong(dateStr))));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void bindEvent(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		MyCommentModel noticeModel = (MyCommentModel) modelList.get(position);
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
		public ImageView image_back;
		public TextView text_name;
		public TextView text_time;
		public TextView text_title;
	}

}
