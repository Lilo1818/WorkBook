package com.mci.firstidol.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import com.mci.firstidol.R;
import com.mci.firstidol.model.SquareLiveModel;
import com.mci.firstidol.utils.BitmapUtil;
import com.mci.firstidol.utils.SyncImageLoader;

/**
 * Created by wang on 2015/6/9.
 */
public abstract class MsBaseAdapter extends BaseAdapter {

	public Context mContext;
	public List<Object> modelList;
	public LayoutInflater mLayoutInflater;// 反射器
	public int selected_position;

	public SyncImageLoader syncImageLoader;
	public ListView listView;
	
	public int backImage_height;//背景高度
	public int backImage_width;//背景宽度

	public MsBaseAdapter(Context context, List<Object> modelList,
			ListView listView) {
		this.mContext = context;
		this.modelList = modelList;
		mLayoutInflater = LayoutInflater.from(context);
		syncImageLoader = new SyncImageLoader(context);
	}

	@Override
	public int getCount() {
		if (modelList != null) {
			return modelList.size();
		} else {
			return 0;
		}

	}

	public SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {
		@Override
		public void onImageLoad(Integer t, Bitmap bm, View parent) {
			if (parent != null) {
				if (bm == null || bm.isRecycled()) {
					((ImageView) parent)
							.setImageResource(R.drawable.ic_launcher);
				} else {
					
					//设置iv的大小
					if(backImage_height!=0){
						int bitmap_height = bm.getHeight();
						int bitmap_width = bm.getWidth();
						backImage_width = (int)((float)backImage_height/(float)bitmap_height)*bitmap_width;
					}else if(backImage_width!=0){
						int bitmap_height = bm.getHeight();
						int bitmap_width = bm.getWidth();
						backImage_height = (int)((float)backImage_width/(float)bitmap_width)*bitmap_height;
					}
					if(backImage_height!=0&&backImage_width!=0){
						LayoutParams params = parent.getLayoutParams();
						params.height = backImage_height;
						//params.width = backImage_width;
						parent.setLayoutParams(params);
					}
					
					((ImageView) parent).setImageBitmap(bm);
				}
			}
		}

		@Override
		public void onError(Integer t, View parent) {
			if (parent != null) {
				((ImageView) parent).setImageResource(R.drawable.ic_launcher);
			}
		}
	};

	@Override
	public Object getItem(int position) {
		if (modelList != null) {
			return modelList.get(position);
		} else {
			return null;
		}

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = bindView(position, convertView, parent);
		bindData(position, convertView, parent);
		bindEvent(position, convertView, parent);
		return convertView;
	}

	/**
	 * release memory -- add By Melvin
	 */
	public void destroyAdapter() {

		if (mLayoutInflater != null) {
			mLayoutInflater = null;
		}
		if (mContext != null) {
			mContext = null;
		}

	}

	public interface CellButtonClickListener {
		void buttonClick(View view, int position);
	}

	/**
	 * 绑定列表的视图
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 */
	public abstract View bindView(int position, View convertView,
			ViewGroup parent);

	/**
	 * 绑定列表的数据
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 */
	public abstract void bindData(int position, View convertView,
			ViewGroup parent);

	/**
	 * 绑定列表中的事件
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 */
	public abstract void bindEvent(int position, View convertView,
			ViewGroup parent);

	public void loadImage(String path, ImageView iv) {
		if (path != null && !path.equals("")) {
			Bitmap bm = syncImageLoader.getBitmapFromMemory(path);
			if (bm == null || bm.isRecycled()) {
				syncImageLoader.loadImage(-1, path, imageLoadListener, iv, 1);
			} else {
				iv.setImageBitmap(bm);
			}
		} else {
			iv.setImageResource(R.drawable.ic_launcher);
		}

	}
	
	
//	public void loadBackgroundImage(String path, ImageView iv){
//		if (path != null && !path.equals("")) {
//			Bitmap bm = syncImageLoader.getBitmapFromMemory(path);
//			if (bm == null || bm.isRecycled()) {
//				syncImageLoader.loadImage(-1, path, imageLoadListener, iv, 1);
//			} else {
//				
//				//设置iv的大小
//				if(height!=0){
//					int bitmap_height = bm.getHeight();
//					int bitmap_width = bm.getWidth();
//					width = (int)((float)height/(float)bitmap_height)*bitmap_width;
//				}
//				if(width!=0){
//					int bitmap_height = bm.getHeight();
//					int bitmap_width = bm.getWidth();
//					height = (int)((float)width/(float)bitmap_width)*bitmap_height;
//				}
//				LayoutParams params = iv.getLayoutParams();
//				params.height = height;
//				params.width = width;
//				iv.setLayoutParams(params);
//				iv.setImageBitmap(bm);
//			}
//		} else {
//			iv.setImageResource(R.drawable.ic_launcher);
//		}
//	}

}
