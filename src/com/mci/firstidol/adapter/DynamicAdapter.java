package com.mci.firstidol.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mci.firstidol.R;
import com.mci.firstidol.activity.MyInfoActivity;
import com.mci.firstidol.adapter.MyNoticeAdapter.ViewHolder;
import com.mci.firstidol.base.MsBaseAdapter;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.DynamicModel;
import com.mci.firstidol.model.NoticeModel;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DynamicAdapter extends MsBaseAdapter {

	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
	
	public DynamicAdapter(Context context, List<Object> modelList,
			ListView listView) {
		super(context, modelList, listView);
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView==null){
			convertView = mLayoutInflater.inflate(R.layout.item_notice,
					parent, false);
			viewHolder = new ViewHolder();
			viewHolder.icon = (RoundedImageView) convertView.findViewById(R.id.icon);
			viewHolder.image_back = (ImageView) convertView.findViewById(R.id.image_back);
			viewHolder.notice_read = (TextView) convertView.findViewById(R.id.notice_read);
			viewHolder.notice_recomend = (TextView) convertView.findViewById(R.id.notice_recomend);
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
		DynamicModel noticeModel = (DynamicModel) modelList.get(position);
		
		String icon_url = noticeModel.UserAvatar;
		String back_url = noticeModel.Ico;
		
		ImageLoader.getInstance().displayImage(icon_url,
				viewHolder.icon);
		ImageLoader.getInstance().displayImage(back_url,
				viewHolder.image_back);
		
		viewHolder.notice_read.setText(noticeModel.ViewCount);
		viewHolder.text_title.setText(noticeModel.Title);
		int upzan = (int) (noticeModel.UpCount+noticeModel.UpCount2+noticeModel.UpCount3+noticeModel.UpCount4);
		//Toast.makeText(mContext, "值为+"+upzan, Toast.LENGTH_SHORT).show();
		
		viewHolder.notice_recomend.setText(String.valueOf(upzan));
		/*String publishData = noticeModel.PublishDate;
		try {
			if(publishData!=null&&publishData.length()>10){
				String dateStr = publishData.substring(6, publishData.length()-2);
				viewHolder.text_time.setText(sdf.format(new Date(Long.parseLong(dateStr))));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	@Override
	public void bindEvent(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		final DynamicModel dynamicModel = (DynamicModel) modelList.get(position);
		
		viewHolder.icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HashMap<String, String> params = new HashMap<String,String>();
				params.put(Constant.IntentKey.userID, dynamicModel.UserId);
				Utily.go2Activity(mContext, MyInfoActivity.class, params, null);
				
			}
		});
		
	}
	
	class ViewHolder{
		public RoundedImageView icon;
		public ImageView image_back;
		public TextView notice_read;
		public TextView notice_recomend;
		public TextView text_title;
		
	}

}
