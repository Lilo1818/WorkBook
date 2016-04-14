package com.mci.firstidol.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mci.firstidol.R;
import com.mci.firstidol.base.MsBaseAdapter;
import com.mci.firstidol.model.StarModel;
import com.mci.firstidol.view.RoundedImageView;
import com.mci.firstidol.view.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyStarAdapter extends MsBaseAdapter {

	public boolean isShowDelete;//是否显示删除按钮
	public Handler handler;//事件助手
	
	private final int Delete = 10;//删除
	
	public MyStarAdapter(Context context, List<Object> modelList,
			ListView listView) {
		super(context, modelList, listView);
	}
	public MyStarAdapter(Context context, List<Object> modelList,
			ListView listView,Handler handler,boolean isShowDelete) {
		super(context, modelList, listView);
		this.isShowDelete = isShowDelete;
		this.handler = handler;
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.item_star,
					parent, false);
			viewHolder.icon = (RoundedImageView) convertView.findViewById(R.id.icon);
			viewHolder.text_name = (TextView) convertView.findViewById(R.id.text_name);
			viewHolder.image_delete = (ImageView) convertView.findViewById(R.id.image_delete);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		StarModel starModel = (StarModel) modelList.get(position);
		
		if(isShowDelete){
			viewHolder.image_delete.setVisibility(View.VISIBLE);
		}else{
			viewHolder.image_delete.setVisibility(View.GONE);
			
			if(starModel.has_selected){
				viewHolder.image_delete.setBackgroundResource(R.drawable.star_selected);
				viewHolder.image_delete.setVisibility(View.VISIBLE);
			}else{
				viewHolder.image_delete.setVisibility(View.GONE);
			}
			
		}
		
		int starId = starModel.starId;
		if(starId!=-100){
			String url = starModel.Avatar;
			ImageLoader.getInstance().displayImage(url, viewHolder.icon);
			viewHolder.text_name.setText(starModel.StarName);
		}else{
			viewHolder.icon.setImageResource(R.drawable.add);
			if(position==modelList.size()-1){
				viewHolder.image_delete.setVisibility(View.GONE);
			}
		}
		
	}

	@Override
	public void bindEvent(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.image_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(handler!=null){
					if(modelList!=null&&modelList.size()>2){
						Message message = new Message();
						message.what = Delete;
						message.obj = modelList.get(position);
						handler.sendMessage(message);
					}else{
						ToastUtils.showCustomToast("至少关注一个明星");
					}
				}
			}
		});
	}
	
	class ViewHolder{
		public RoundedImageView icon;
		
		public TextView text_name;
		
		public ImageView image_delete;
	}

}
