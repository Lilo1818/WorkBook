package  com.mci.firstidol.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import java.util.List;

import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.model.PhotoModel;
import com.mci.firstidol.view.photoview.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by wang on 2015/7/28.
 */
public class ImageBroaseAdapter extends PagerAdapter {

    private List<PhotoModel> pictures;
    private Context context;

    public ImageBroaseAdapter(Context context,List<PhotoModel> pictures){
    	this.context = context;
        this.pictures = pictures;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public void finishUpdate(View container) {}

    @Override
    public int getCount() {
        if(pictures!=null){
            return pictures.size();
        }else{
            return  0;
        }

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        String url = pictures.get(position).PicPath;
        ImageLoader.getInstance().displayImage(url,photoView);
        container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {}

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View container) {}
}
