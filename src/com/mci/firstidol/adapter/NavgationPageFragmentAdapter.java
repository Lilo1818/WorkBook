package com.mci.firstidol.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.mci.firstidol.R;
import com.mci.firstidol.fragment.NavgationPageFragment;
import com.viewpagerindicator.IconPagerAdapter;

public class NavgationPageFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    protected static final String[] CONTENT = new String[] { "This", "Is", "A" };
    protected static final int[] imgs = new int[] {
        R.drawable.new_feature_1,
        R.drawable.new_feature_2,
        R.drawable.new_feature_3
};

    private int mCount = CONTENT.length;

    public NavgationPageFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return NavgationPageFragment.newInstance(imgs[position % imgs.length]);
    }

    @Override
    public int getCount() {
    	 if (imgs != null)  
         {  
             return imgs.length;  
         }   
         return 0;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//      return NavgationPageFragmentAdapter.CONTENT[position % CONTENT.length];
//    }
//	  
//    @Override
//    public int getIconResId(int index) {
//      return 0;
//    }
    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }

	@Override
	public int getIconResId(int index) {
		// TODO Auto-generated method stub
		return imgs[index];
	}
}