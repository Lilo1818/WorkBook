package com.mci.firstidol.fragment.square;


import java.util.ArrayList;

import com.mci.firstidol.adapter.SquareOrderFragmentAdapter;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.listener.SquareFragmentListener;
import com.mci.firstidol.view.ProgressActivity;
import com.viewpagerindicator.TabPageIndicator;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.mci.firstidol.R;

public class SquareListFragment extends BaseFragment implements OnPageChangeListener,SquareFragmentListener{

	private static final String[] titles = new String[] { "明星排行榜", "粉丝活跃榜"};
	private static final int[] icons = new int[] {0,0};
	
	public static int currentIndex;
	
	private ArrayList<OrderTypeFragment> fragments = new ArrayList<OrderTypeFragment>();
	private  ArrayList<String> squares = new ArrayList<String>();
	private TabPageIndicator indicator;
	private SquareOrderFragmentAdapter adapter;
	private FragmentManager fm;
	private ViewPager pager;
	
	private ProgressActivity progressActivity;
	
	
	private SquareFragmentListener squareFragmentListener;
	private  ArrayList orders;
	
//	public static SquareListFragment newInstance(String content) {
//		SquareListFragment fragment = new SquareListFragment();
//
//        return fragment;
//    }
	
	private static SquareListFragment instance = null;
	public static SquareListFragment newInstance(String content) {
		if (null == instance) {
            instance = new SquareListFragment();
        }
        return instance;
    }
	
	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.layout_fragment_square_list;
	}
	
	@Override
	protected void initView() {
		findViewById();
		initData();
	}

	protected void findViewById() {
		// TODO Auto-generated method stub
		initFragments();
		
		fm = getChildFragmentManager();
		
 		adapter = new SquareOrderFragmentAdapter(fm,fragments,titles,icons,squares);

        indicator = (TabPageIndicator)findViewById(R.id.orderIndicator);
        indicator.setOnPageChangeListener(this);
        
        pager = (ViewPager)findViewById(R.id.orderPager);
        pager.setOffscreenPageLimit(titles.length);
        
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);
		
		indicator.setCurrentItem(currentIndex);
	}
	
	
	private void initFragments() {
		// TODO Auto-generated method stub
//		OrderTypeFragment startOrderTypeFragment = OrderTypeFragment
//				.newInstance(null);
		OrderTypeFragment startOrderTypeFragment = new OrderTypeFragment();
		startOrderTypeFragment.position = 0;
		startOrderTypeFragment.setSquareFragmentListener(this);
		
//		OrderTypeFragment fanOrderTypeFragment = OrderTypeFragment
//				.newInstance(null);
		OrderTypeFragment fanOrderTypeFragment = new OrderTypeFragment();
		fanOrderTypeFragment.position = 1;
		fanOrderTypeFragment.setSquareFragmentListener(this);
		
		fragments.add(startOrderTypeFragment);
		fragments.add(fanOrderTypeFragment);

	}

	protected void initData() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		currentIndex = position;
		//F:首次刷新
		squareFragmentListener.pullRefresh(true, 3, 1, "F",currentIndex);
		
	}

	
	public void refreshComplete(int currentPosition){
		fragments.get(0).refreshComplete();
		fragments.get(1).refreshComplete();
	}
	
	public void refreshComplete(int currentPosition,boolean isError,boolean isNoNetwork){
		fragments.get(currentPosition).refreshComplete(isError,isNoNetwork);
	}
	
	public void updateContentData(int currentPosition, ArrayList squareListModels,boolean isRefresh,boolean isComplete){
		fragments.get(currentPosition).updateContentData(squareListModels, isRefresh, isComplete);
	}
    public void setSquareFragmentListener(
			SquareFragmentListener squareFragmentListener) {
		this.squareFragmentListener = squareFragmentListener;
	}
	@Override
	public void pullRefresh(boolean isRefresh, int fragmentIndex,
			int pageIndex, String maxDate,int currentPosition) {
		// TODO Auto-generated method stub
		squareFragmentListener.pullRefresh(isRefresh, fragmentIndex, pageIndex, maxDate,currentPosition);
	}

}
