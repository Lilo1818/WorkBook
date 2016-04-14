package com.mci.firstidol.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;

import com.mci.firstidol.R;
import com.mci.firstidol.activity.ApplyInfoContentActivity.UploadHolder;
import com.mci.firstidol.base.MsBaseAdapter;
import com.mci.firstidol.utils.Utily;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UploadPhotoAdapter extends MsBaseAdapter{

	private List<UploadHolder> uploadHolders;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private int width;//宽度
	
	public UploadPhotoAdapter(Context context, List<Object> modelList,
			ListView listView,List<UploadHolder> uploadHolders) {
		super(context, modelList, listView);
		this.uploadHolders = uploadHolders;
		
		mImageLoader = ImageLoader.getInstance();
		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true).build();
		width = (Utily.getWidth(mContext)-20)/4;
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.item_upload_photo,
					parent, false);
			viewHolder.image_delete = (ImageView) convertView.findViewById(R.id.image_delete);
			viewHolder.photo = (ImageView) convertView.findViewById(R.id.photo);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent) {
		UploadHolder uploadHolder = uploadHolders.get(position);
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		String path = uploadHolder.path;
		LayoutParams layoutParams = viewHolder.photo.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = width;
		viewHolder.photo.setLayoutParams(layoutParams);
		
		if(path!=null&&!path.equals("")){
			mImageLoader.displayImage("file://" + path, viewHolder.photo,
					mOptions);
		}
		
	}

	@Override
	public void bindEvent(int position, View convertView, ViewGroup parent) {
		
	}
	
	@Override
	public int getCount() {
		if (uploadHolders != null) {
			return uploadHolders.size();
		} else {
			return 0;
		}

	}

	@Override
	public Object getItem(int position) {
		if (uploadHolders != null) {
			return uploadHolders.get(position);
		} else {
			return null;
		}

	}
	
	class ViewHolder{
		public ImageView photo;
		public ImageView image_delete;
	}

}
