package com.mci.firstidol.adapter;

import java.util.List;

import com.mci.firstidol.R;
import com.mci.firstidol.model.PhotoModel;
import com.mci.firstidol.model.StarPhotoModel;
import com.mci.firstidol.utils.SyncImageLoader;
import com.mci.firstidol.view.photoview.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PhotoPageAdapter extends PagerAdapter{

	private List<PhotoModel> list;
	private Context context;
	private StarPhotoModel starPhotoModel;//照片对象
	public LayoutInflater mLayoutInflater;// 反射器
	public SyncImageLoader syncImageLoader;//异步图片加载器
	public View[] views;
	
	public PhotoPageAdapter(Context context,List<PhotoModel> list,StarPhotoModel starPhotoModel){
		this.context = context;
		this.list = list;
		mLayoutInflater = LayoutInflater.from(context);
		syncImageLoader = new SyncImageLoader(context);
		views = new View[list.size()];
		this.starPhotoModel = starPhotoModel;
	}
	
	@Override
	public int getCount() {
		if(list!=null&&list.size()!=0){
			return list.size();
		}else{
			return 0;
		}
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View view = mLayoutInflater.inflate(R.layout.item_photo,
				container, false);
		PhotoView imageView =  (PhotoView) view.findViewById(R.id.image_photo);
		String url = list.get(position).PicPath;
		ImageLoader.getInstance().displayImage(url,
				imageView);
		container.addView(view);
		views[position] = view;
		return view;
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(views[position
		                           					% views.length]);
	}
	
	/**
	 * 异步加载图片的回调函数
	 */
	public SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {
		@Override
		public void onImageLoad(Integer t, Bitmap bm, View parent) {
			if (parent != null) {
				if (bm == null || bm.isRecycled()) {
					((ImageView) parent)
							.setImageResource(R.drawable.ic_launcher);
				} else {
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
	
	/**
	 * 加载图片
	 * @param path
	 * @param iv
	 */
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

}
