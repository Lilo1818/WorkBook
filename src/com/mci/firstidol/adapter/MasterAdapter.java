package com.mci.firstidol.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mci.firstidol.R;
import com.mci.firstidol.activity.MyInfoActivity;
import com.mci.firstidol.adapter.StarPhotoAdapter.ViewHolder;
import com.mci.firstidol.base.MsBaseAdapter;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.MasterModel;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MasterAdapter extends MsBaseAdapter {

	public MasterAdapter(Context context, List<Object> modelList,
			ListView listView) {
		super(context, modelList, listView);
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.item_master,
					parent, false);
			viewHolder.icon = (RoundedImageView) convertView.findViewById(R.id.icon);
			viewHolder.text_name = (TextView) convertView.findViewById(R.id.text_name);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		MasterModel masterModel = (MasterModel) modelList.get(position);
		String url = masterModel.Avatar;
		ImageLoader.getInstance().displayImage(url, viewHolder.icon);
		viewHolder.text_name.setText(masterModel.NickName);
	}

	@Override
	public void bindEvent(int position, View convertView, ViewGroup parent) {
		ViewHolder  viewHolder = (ViewHolder) convertView.getTag();
		final String userId = ((MasterModel)modelList.get(position)).UserId;
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
	}
	
}
