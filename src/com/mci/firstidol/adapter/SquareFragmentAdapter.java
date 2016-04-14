package com.mci.firstidol.adapter;

import java.util.List;

import com.viewpagerindicator.IconPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class SquareFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter{

	private String[] titles;
	private int[] icons;
	private List<String> squares;
	private List<Fragment> fragments;
	public SquareFragmentAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public SquareFragmentAdapter(FragmentManager fm,List<Fragment> fragments,
			String[] titles,int[] icons, List<String> squares) {
		super(fm);
		this.fragments = fragments;
		this.titles = titles;
		this.icons = icons;
		this.squares = squares;
	}

	 @Override
     public Fragment getItem(int position) {
		 if(fragments!=null){
			 return fragments.get(position);
		 }else{
			 return null;
		 }
         
     }

     @Override
     public CharSequence getPageTitle(int position) {
    	 if(titles!=null&&titles.length>0){
    		 return titles[position];
    	 }else{
    		 return "";
    	 }
         
     }

     @Override public int getIconResId(int index) {
       return icons[index];
     }

   @Override
     public int getCount() {
	   if(icons!=null){
		   return icons.length;
	   }else{
		   return 0;
	   }
       
     }
   
   
   @Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
	   Object f =  super.instantiateItem(container, position);
	   return f;
	}

}
