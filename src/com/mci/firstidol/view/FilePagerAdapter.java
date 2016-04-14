
package com.mci.firstidol.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import net.frakbot.imageviewex.ImageViewNext;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.mci.firstidol.R;
import com.mci.firstidol.view.TouchImageView.TouchImageViewListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FilePagerAdapter extends BasePagerAdapter {
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private FilePagerAdapterListener filePagerAdapterListener;

    public FilePagerAdapter(Context context, List<String> resources) {
        super(context, resources);
        mImageLoader = ImageLoader.getInstance();
        mOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).resetViewBeforeLoading(true).build();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        
        if (object instanceof TouchImageView) {
        	((GalleryViewPager) container).mCurrentView = ((TouchImageView) object);
		} else {
			((GalleryViewPager) container).otherView = (View) object;
		}
        
    }

    @SuppressWarnings("static-access")
	@Override
    public Object instantiateItem(final ViewGroup collection, final int position) {
    	Log.v("TextGif", "值为+"+mResources.get(position).toString());
    	
    	if (mResources.get(position).indexOf(".gif")==-1) {
    		ImageViewNext iv = new ImageViewNext(mContext);
            iv.setBackgroundResource(R.drawable.bg_pic_set_image_loading);
            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            mImageLoader.displayImage(mResources.get(position), iv, mOptions);
            iv.setUrl(mResources.get(position));
            collection.addView(iv, 0);
            Log.v("TextGif", "1");
            iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					filePagerAdapterListener.dismiss();
				}
			});
            return iv;
		} else {
			TouchImageView iv = new TouchImageView(mContext);
	        iv.setBackgroundResource(R.drawable.bg_pic_set_image_loading);
	        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	        mImageLoader.displayImage(mResources.get(position), iv, mOptions);
	        collection.addView(iv, 0);
	        Log.v("TextGif", "2");
	        iv.setTouchImageViewListener(new TouchImageViewListener() {
				
				@Override
				public void touchImageViewClick(View v) {
					// TODO Auto-generated method stub
					filePagerAdapterListener.dismiss();
				}
			});
	        
//	        iv.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					filePagerAdapterListener.dismiss();
//				}
//			});


	        return iv;
		}
        
    }

    public void setDataList(List<String> resources) {
        mResources = resources;
        notifyDataSetChanged();
    }
    
    public void releaseResource() {
        if (mImageLoader != null) {
            mImageLoader.clearMemoryCache();
            mImageLoader = null;
        }
        mOptions = null;
        mResources = null;
        mCurrentPosition = -1;
        mOnItemChangeListener = null;
        mOnItemClickListener = null;
        mContext = null;
    }
    
    public void setFilePagerAdapterListener(
			FilePagerAdapterListener filePagerAdapterListener) {
		this.filePagerAdapterListener = filePagerAdapterListener;
	}
    public interface FilePagerAdapterListener{
    	public void dismiss();
    }
}