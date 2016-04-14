package com.mci.firstidol.adapter;


import java.util.ArrayList;
import java.util.List;

import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.fragment.square.OrderTypeFragment;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

public class SquareOrderFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter{

	private String[] titles;
	private int[] icons;
	private ArrayList<String> squares;
	
//	private String[] CONTENT;
	private ArrayList<String> CONTENT;
	private static ArrayList<ArrayList<String>> list;
	
	private ArrayList<OrderTypeFragment> fragments;
	
	public SquareOrderFragmentAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public SquareOrderFragmentAdapter(FragmentManager fm,ArrayList<OrderTypeFragment> fragments,
			String[] titles,int[] icons, ArrayList<String> squares) {
		// TODO Auto-generated constructor stub
		super(fm);
		this.fragments = fragments;
		this.titles = titles;
		this.icons = icons;
		this.squares = squares;
	}
	
	public SquareOrderFragmentAdapter(FragmentManager fm,
			ArrayList<String> CONTENT, ArrayList<ArrayList<String>> lIST) {
		// TODO Auto-generated constructor stub
		super(fm);
		this.CONTENT = CONTENT;
		SquareOrderFragmentAdapter.list = lIST;
	}
	
	public void setList(ArrayList<ArrayList<String>> list) {
		SquareOrderFragmentAdapter.list = list;
//		notifyDataSetChanged();
	}
	
	public void setSNSType(ArrayList<String> titles) {
		// TODO Auto-generated method stub
		CONTENT = titles;
		notifyDataSetChanged();
	}
	
	/**
	 * 当返回为POSITION_NONE，会把布局销毁重新加载，也就是重新刷新
	 */
//	@Override
//	public int getItemPosition(Object object) {
//		// TODO Auto-generated method stub
////		return super.getItemPosition(object);
////		return POSITION_NONE;
//		return PagerAdapter.POSITION_NONE;
//	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Log.i("xxxxxxxxxxxxxxxx", "getItem:"+position+"____");
//		String result = "";
//		if (position<list.size()) {
//			result = list.get(position);
//		}
//		return OrderTypeFragment.newInstance(null);
		return fragments.get(position);
	}
	
	
	
	@Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override public int getIconResId(int index) {
      return 0;
    }

  @Override
    public int getCount() {
      return titles.length;
  }
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
//		Log.i("xxxxxxxxxxxxxxxx", "instantiateItem:"+position+"____"+list.size());
		OrderTypeFragment f = (OrderTypeFragment) super.instantiateItem(container, position);
	     return f;
	}


	public void setSNSType(ArrayList<String> titles,
			TabPageIndicator indicator) {
		// TODO Auto-generated method stub
		CONTENT = titles;
		notifyDataSetChanged();
		indicator.notifyDataSetChanged();
	}

	
	
	

}
