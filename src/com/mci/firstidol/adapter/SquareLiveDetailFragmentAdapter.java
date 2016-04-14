package com.mci.firstidol.adapter;

import java.util.ArrayList;

import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.base.BaseTempFragment;
import com.mci.firstidol.fragment.square.SquareFoundFragment;
import com.viewpagerindicator.IconPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

public class SquareLiveDetailFragmentAdapter extends FragmentPagerAdapter{

	private ArrayList<String> squares;
	private ArrayList<BaseFragment> fragments;
	public SquareLiveDetailFragmentAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}
	
	public SquareLiveDetailFragmentAdapter(FragmentManager fm,ArrayList<BaseFragment> fragments, ArrayList<String> squares) {
		// TODO Auto-generated constructor stub
		super(fm);
		this.fragments = fragments;
		this.squares = squares;
	}

	 @Override
     public Fragment getItem(int position) {
         return fragments.get(position);
     }


   @Override
     public int getCount() {
       return fragments.size();
     }
   
   
   @Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
	   Object f =  super.instantiateItem(container, position);
	   return f;
	}

}
