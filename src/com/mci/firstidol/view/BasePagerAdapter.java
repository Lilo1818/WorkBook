
package com.mci.firstidol.view;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.List;

/**
 * Class wraps URLs to adapter, then it instantiates <b>UrlTouchImageView</b>
 * objects to paging up through them.
 */
public class BasePagerAdapter extends PagerAdapter {

    protected List<String> mResources;
    protected Context mContext;
    protected int mCurrentPosition = -1;
    protected OnItemChangeListener mOnItemChangeListener;
    protected OnItemClickListener mOnItemClickListener;

    public BasePagerAdapter() {
        mResources = null;
        mContext = null;
    }

    public BasePagerAdapter(Context context, List<String> resources) {
        this.mResources = resources;
        this.mContext = context;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, final int position, Object object) {
        super.setPrimaryItem(container, position, object);
        GalleryViewPager galleryContainer = ((GalleryViewPager) container);
        if (galleryContainer.mCurrentView != null) {
            galleryContainer.mCurrentView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });
        }
        if (mCurrentPosition == position) {
            return;
        }
        if (galleryContainer.mCurrentView != null)
            galleryContainer.mCurrentView.resetScale();

        mCurrentPosition = position;
        if (mOnItemChangeListener != null)
            mOnItemChangeListener.onItemChange(mCurrentPosition);
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mResources == null ? 0 : mResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void finishUpdate(ViewGroup arg0) {
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(ViewGroup arg0) {
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setOnItemChangeListener(OnItemChangeListener listener) {
        mOnItemChangeListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public static interface OnItemChangeListener {
        public void onItemChange(int currentPosition);
    }

    public static interface OnItemClickListener {
        public void onItemClick(int position);
    }
}