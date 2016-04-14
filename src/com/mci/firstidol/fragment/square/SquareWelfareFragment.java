package com.mci.firstidol.fragment.square;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.mci.firstidol.activity.SquareWelfareDetailActivity;
import com.mci.firstidol.adapter.SquareWelfareListViewAdapter;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.listener.SquareFragmentListener;
import com.mci.firstidol.model.SquareLiveModel;
import com.mci.firstidol.utils.DateHelper;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.ProgressActivity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;

public class SquareWelfareFragment extends BaseFragment implements OnItemClickListener {

	private PullToRefreshListView mPullRefreshListView;
	private ListView listView;
	private View view;
	private SquareWelfareListViewAdapter adapter;

	private ArrayList<SquareLiveModel> contents;

	private SquareFragmentListener squareFragmentListener;
	private static int welfarePageIndex;
	
	private ProgressActivity progressActivity;
	
	private boolean currentIsRefresh;
	private int currentPageIndex;

	public static SquareWelfareFragment newInstance(String content) {
		SquareWelfareFragment fragment = new SquareWelfareFragment();

		return fragment;
	}

	@Override
	protected int getViewId() {
		return R.layout.layout_fragment_square_welfare;
	}
	@Override
	protected void initView() {
		currentIsRefresh = true;
		currentPageIndex = 1;
		
		findViewById();
		mPullRefreshListView.setMode(Mode.BOTH);//设置刷新方式，上拉、下拉等

		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						String label = DateUtils.formatDateTime(BaseApp
								.getInstance().getApplicationContext(), System
								.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						// 更新最后一次刷新时间
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						welfarePageIndex = 1;
						didLoadDataRequest(true, welfarePageIndex);

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						String label = DateUtils.formatDateTime(BaseApp
								.getInstance().getApplicationContext(), System
								.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						// 更新最后一次刷新时间
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						welfarePageIndex++;
						didLoadDataRequest(false, welfarePageIndex);
					}
				});

		// Add an end-of-list listener
		mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						if (mPullRefreshListView.getMode() == Mode.PULL_FROM_START) {
							// ToastUtils.showC(getApplicationContext(),
							// "数据已全部加载");
						}
					}
				});
		;
		// 选择事件
		listView.setOnItemClickListener(this);

		adapter = new SquareWelfareListViewAdapter(getActivity(), null, false);
		mPullRefreshListView.setAdapter(adapter);
		
		initData();
	}

	protected void findViewById() {
		// TODO Auto-generated method stub
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		listView = mPullRefreshListView.getRefreshableView();
		
		progressActivity = (ProgressActivity) findViewById(R.id.progressActivity);
		progressActivity.showLoading();
	}
	
	
	protected void initData() {
		// TODO Auto-generated method stub

	}

	public void headerRefresh() {
		mPullRefreshListView.onRefreshComplete();
		mPullRefreshListView.setShowViewWhileRefreshing(true);
		mPullRefreshListView.setCurrentModeRefresh();
		mPullRefreshListView.setRefreshing(true);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Bundle mbuBundle = new Bundle();
		mbuBundle.putSerializable(Constant.IntentKey.squareLiveModel, contents.get(position-1));
		Utily.go2Activity(activity, SquareWelfareDetailActivity.class, mbuBundle);
	}

	// @Override
	// protected int getViewId() {
	// // TODO Auto-generated method stub
	// return 0;
	// }

	/**
	 * 
	 * @param isError 是否请求错误
	 * @param isNoNetwork 是否网络请求错误
	 */
	public void refreshComplete(boolean isError,boolean isNoNetwork) {
//		isError = true;
//		isNoNetwork = false;
		try {
			int drawableID = 0;
			String title = null,content = null,btnTitle = null;
			if (isError && isNoNetwork && (contents==null || contents.size() == 0)) {
				drawableID = R.drawable.error_network;
				title = getResources().getString(R.string.hint_error_network_title);
				content = getResources().getString(R.string.hint_error_network_content);
				btnTitle = getResources().getString(R.string.hint_error_button_title);
			} else if (isError  && (contents==null || contents.size() == 0)) {
				drawableID = R.drawable.failed;
				title = getResources().getString(R.string.hint_error_request_title);
				content = getResources().getString(R.string.hint_error_request_content);
				btnTitle = getResources().getString(R.string.hint_error_button_title);
			} else if(contents==null || contents.size() == 0){
				drawableID = R.drawable.default_img;
				title = getResources().getString(R.string.hint_error_nodata_title);
				content = getResources().getString(R.string.hint_error_nodata_content);
				btnTitle = getResources().getString(R.string.hint_error_button_title);
			}
			if (drawableID>0 && progressActivity!=null) {
				progressActivity.showError(getResources().getDrawable(drawableID), title, content, btnTitle, new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (contents==null || contents.size() == 0) {
							progressActivity.showLoading();
						}
						
						didLoadDataRequest(currentIsRefresh, currentPageIndex);
					}
				});
			} else {
				progressActivity.showContent();
			}
			
			
			if (mPullRefreshListView != null) {
				mPullRefreshListView.onRefreshComplete();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	public void updateContentData(
			ArrayList<SquareLiveModel> squareWelfareModels, boolean isRefresh,boolean isComplete) {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
		contents = squareWelfareModels;
		adapter.refershData(contents);
		if (isRefresh) {// 下拉刷新
			if (contents != null && contents.size() < Constant.pageNum) {
				mPullRefreshListView.setMode(Mode.PULL_FROM_START);
			} else {
				mPullRefreshListView.setMode(Mode.BOTH);
			}
		} else {// 上拉加载更多
			if (isComplete) {
				mPullRefreshListView.setMode(Mode.PULL_FROM_START);
			}
		}
		
		
//		if (isRefresh) {// 下拉刷新
//			contents = squareWelfareModels;
//			adapter.refershData(contents);
//
//			if (contents != null && contents.size() < Constant.pageNum) {
//				mPullRefreshListView.setMode(Mode.PULL_FROM_START);
//			} else {
//				mPullRefreshListView.setMode(Mode.BOTH);
//			}
//		} else {// 上拉加载更多
//			if (squareWelfareModels != null) {
//				contents.addAll(squareWelfareModels);
//			} else {
//				contents = squareWelfareModels;
//			}
//			adapter.refershData(contents);
//			if (squareWelfareModels.size() < Constant.pageNum) {
//				mPullRefreshListView.setMode(Mode.PULL_FROM_START);
//			}
//		}

	}

	public void setSquareFragmentListener(
			SquareFragmentListener squareFragmentListener) {
		this.squareFragmentListener = squareFragmentListener;
	}
	
	private void didLoadDataRequest(final boolean isRefresh, final int pageIndex) {
		
		
		squareFragmentListener.pullRefresh(
				isRefresh,
				2,
				pageIndex,
				isRefresh ? "00" : DateHelper
						.getTimeFromDateStr(((SquareLiveModel) contents
								.get(contents.size() - 1)).getPublishDate()),0);
	}

	
}
