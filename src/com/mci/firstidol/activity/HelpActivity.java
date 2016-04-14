package com.mci.firstidol.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;

public class HelpActivity extends BaseActivity {

	private ViewPager viewPager;
	private ImageView[] mImageViews;
	private int[] imgIdArray;

	@Override
	protected int getViewId() {
		return R.layout.activity_help;
	}

	@Override
	protected void initNavBar() {
		//setTitle(R.string.setting_help);
		hideNavBar();
	}

	@Override
	protected void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewPager);

		// 载入图片资源ID
		imgIdArray = new int[] { R.drawable.help_featuer1,
				R.drawable.help_featuer2, R.drawable.help_featuer3,
				R.drawable.help_featuer4, R.drawable.help_featuer5 };

		// 将图片装载到数组中

		mImageViews = new ImageView[imgIdArray.length];

		for (int i = 0; i < mImageViews.length; i++) {

			ImageView imageView = new ImageView(this);

			mImageViews[i] = imageView;

			imageView.setBackgroundResource(imgIdArray[i]);
			imageView.setScaleType(ScaleType.FIT_XY);

		}

		// 设置Adapter

		viewPager.setAdapter(new MyAdapter());
		// 设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动

		viewPager.setCurrentItem(0);
	}

	@Override
	public void rightNavClick() {

	}

	@Override
	protected void findViewById() {

	}

	@Override
	protected void initData() {

	}

	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {

			return Integer.MAX_VALUE;

		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;

		}

		@Override
		public void destroyItem(View container, int position, Object object) {

			((ViewPager) container).removeView(mImageViews[position
					% mImageViews.length]);

		}

		/**
		 * 
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */

		@Override
		public Object instantiateItem(View container, int position) {

			((ViewPager) container).addView(mImageViews[position
					% mImageViews.length], 0);

			return mImageViews[position % mImageViews.length];

		}

	}

}
