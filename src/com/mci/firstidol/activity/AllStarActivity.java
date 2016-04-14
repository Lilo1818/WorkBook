package com.mci.firstidol.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.mci.firstidol.R;
import com.mci.firstidol.adapter.SquareFragmentAdapter;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.fragment.ChoiceStarFragment;
import com.viewpagerindicator.TabPageIndicator;

public class AllStarActivity extends BaseActivity{

	private int currentIndex = 0;//当前选中

	private final String[] titles = new String[] { "中国", "韩国", "日本", "热点" };//标题
	private final int[] icons = new int[4] ;//图片

	private List<Fragment> fragments = new ArrayList<Fragment>();
	private List<String> stars = new ArrayList<String>();
	private TabPageIndicator indicator;
	private SquareFragmentAdapter adapter;
	private FragmentManager fm;
	private ViewPager pager;
	
	private ChoiceStarFragment chinaStarFragment;//中国明星
	private ChoiceStarFragment koreaStarFragment;//韩国明星
	private ChoiceStarFragment japanStarFragment;//日本明星
	private ChoiceStarFragment otherStarFragment;//其他明星
	
	@Override
	protected int getViewId() {
		return R.layout.activity_allstar;
	}

	@Override
	protected void initNavBar() {
		setTitle(R.string.star_list);
		setRightBtnBackgroundResource(R.drawable.done);
	}

	@Override
	protected void initView() {
		initFragment();
		initData();
	}
	
	/**
	 * 初始化fragment
	 */
	public void initFragment(){
		
		chinaStarFragment = new ChoiceStarFragment();
		Bundle chinaBundle = new Bundle();
		chinaBundle.putString(Constant.IntentKey.county_type, "1");
		chinaStarFragment.setArguments(chinaBundle);
		
		koreaStarFragment = new ChoiceStarFragment();
		Bundle koreaBundle = new Bundle();
		koreaBundle.putString(Constant.IntentKey.county_type, "2");
	    koreaStarFragment.setArguments(koreaBundle);
		
		japanStarFragment= new ChoiceStarFragment();
		Bundle japanBundle = new Bundle();
		japanBundle.putString(Constant.IntentKey.county_type, "3");
		japanStarFragment.setArguments(japanBundle);
		
		otherStarFragment = new ChoiceStarFragment();
		Bundle otherBundle = new Bundle();
		otherBundle.putString(Constant.IntentKey.county_type, "4");
		otherStarFragment.setArguments(otherBundle);
		
		fragments.add(chinaStarFragment);
		fragments.add(koreaStarFragment);
		fragments.add(japanStarFragment);
		fragments.add(otherStarFragment);
		
	}
	
	/**
	 * 加载数据
	 */
	public void initData(){
		fm = getSupportFragmentManager();

		adapter = new SquareFragmentAdapter(fm, fragments, titles, icons,
				stars);

		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		//indicator.setOnPageChangeListener(this);

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(fragments.size());

		pager.setAdapter(adapter);
		indicator.setViewPager(pager);

		indicator.setCurrentItem(currentIndex);
	}
	@Override
	public void rightNavClick() {
		exitThis();
	}

	@Override
	protected void findViewById() {
		
	}

}
