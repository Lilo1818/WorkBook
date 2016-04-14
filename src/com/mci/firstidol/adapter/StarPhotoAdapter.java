package com.mci.firstidol.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;

import com.mci.firstidol.R;
import com.mci.firstidol.activity.PhotoSelectedActivity;
import com.mci.firstidol.base.MsBaseAdapter;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.PhotoModel;
import com.mci.firstidol.model.StarPhotoModel;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.RoundedImageView;
import com.mci.firstidol.view.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class StarPhotoAdapter extends MsBaseAdapter {

	private int item_width;
	
	public StarPhotoAdapter(Context context, List<Object> modelList,
			ListView listView ) {
		super(context, modelList, listView);
		item_width = (Utily.getWidth(mContext)-Utily.dip2px(mContext, 20))/2;
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.item_starphoto,
					parent, false);
			viewHolder.image_back = (RoundedImageView) convertView.findViewById(R.id.image_icon);
			viewHolder.image_size = (Button) convertView.findViewById(R.id.image_size);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		return convertView;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		StarPhotoModel starPhotoModel = (StarPhotoModel) modelList.get(position);
		List<PhotoModel> pictures = starPhotoModel.ArticlePictures;
		//设置大小
		LayoutParams layoutParams = viewHolder.image_back.getLayoutParams();
		layoutParams.width = item_width;
		layoutParams.height = item_width;
		viewHolder.image_back.setLayoutParams(layoutParams);
		//Toast.makeText(mContext, "值为+"+pictures.size(), Toast.LENGTH_SHORT).show();
		String size = Integer.toString(pictures.size());
		viewHolder.image_size.setText(size);
		ImageLoader.getInstance().displayImage(starPhotoModel.Ico,
				viewHolder.image_back);
		
		
	}

	@Override
	public void bindEvent(int position, View convertView, ViewGroup parent) {

	}
	
	class ViewHolder{
		public RoundedImageView image_back;
		public Button image_size;
	}

}
