package com.mci.firstidol.fragment.square;

import java.util.ArrayList;
import java.util.HashMap;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.mci.firstidol.activity.SquareLiveActivity;
import com.mci.firstidol.activity.WebActivity;
import com.mci.firstidol.adapter.SquareLiveListViewAdapter;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.base.BaseTempFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.listener.SquareFragmentListener;
import com.mci.firstidol.model.ShoppingModel;
import com.mci.firstidol.model.SquareLiveModel;
import com.mci.firstidol.model.SquareModel.SquareFoundModel;
import com.mci.firstidol.utils.DateHelper;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.ProgressActivity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;

public class SquareLiveFragment extends BaseFragment implements
		OnItemClickListener {

	private PullToRefreshListView mPullRefreshListView;
	private ListView listView;
	private SquareLiveListViewAdapter adapter;

	private ArrayList<SquareLiveModel> contents;

	private SquareFragmentListener squareFragmentListener;
	private static int livePageIndex;
	
	private ProgressActivity progressActivity;
	
	private boolean currentIsRefresh;
	private int currentPageIndex;

	public static SquareLiveFragment newInstance(String content) {
		SquareLiveFragment fragment = new SquareLiveFragment();

		return fragment;
	}
	
	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.layout_fragment_square_live;
	}

	protected void findViewById() {
		// TODO Auto-generated method stub
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
		listView = mPullRefreshListView.getRefreshableView();
		
		progressActivity = (ProgressActivity) findViewById(R.id.progressActivity);
		progressActivity.showLoading();
	}

	protected void initView() {
		
		findViewById();
		
		if (livePageIndex==0) {
			livePageIndex = 1;
		}
		currentIsRefresh = true;
		currentPageIndex = 1;
		// TODO Auto-generated method stub
		mPullRefreshListView.setMode(Mode.BOTH);// 设置刷新方式，上拉、下拉等

		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

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

						livePageIndex = 1;
						didLoadDataRequest(true, livePageIndex);

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

						livePageIndex++;
						didLoadDataRequest(false, livePageIndex);
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
		mPullRefreshListView.setOnItemClickListener(this);

		final LinearLayout ll_flow = (LinearLayout)findViewById(R.id.ll_flow);
		listView.addHeaderView(View.inflate(getActivity(),
				R.layout.item_square_live_header, null));// ListView条目中的悬浮部分
															// 添加到头部
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if (contents!=null && contents.size()>0) {
					if (firstVisibleItem >= 1) {
						ll_flow.setVisibility(View.VISIBLE);
					} else {

						ll_flow.setVisibility(View.GONE);
					}
				}
				
			}
		});

		adapter = new SquareLiveListViewAdapter(getActivity(), null, false);
		listView.setAdapter(adapter);
		
		initData();
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
		// (BaseActivity)getActivity().startActivity(SquareLiveActivity.class);
		//
		// ShoppingModel shoppingModel = (ShoppingModel)
		// modelList.get(position);
		// String h5_content = shoppingModel.Content;
		//
		// HashMap<String, String> params = new HashMap<String,String>();
		// params.put(Constant.IntentKey.H5_CONTENT, h5_content);

		Bundle bundle = new Bundle();
		bundle.putSerializable(Constant.IntentKey.squareLiveModel, contents.get(position-2));
		Utily.go2Activity(getActivity(), SquareLiveActivity.class, bundle);
	}

	// @Override
	// protected int getViewId() {
	// // TODO Auto-generated method stub
	// return 0;
	// }

	/**
	 * 
	 * @param isRefresh
	 * @param pageIndex
	 */
	private void didLoadDataRequest(final boolean isRefresh, final int pageIndex) {
		// /Date(1445623661000)/
		squareFragmentListener.pullRefresh(
				isRefresh,
				1,
				pageIndex,
				isRefresh ? "00" : DateHelper
						.getTimeFromDateStr(((SquareLiveModel) contents
								.get(contents.size() - 1)).getPublishDate()),0);
	}

	/**
	 * 
	 * @param isError 是否请求错误
	 * @param isNoNetwork 是否网络请求错误
	 */
	public void refreshComplete(boolean isError,boolean isNoNetwork) {
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
			ArrayList<SquareLiveModel> squareLiveModels, boolean isRefresh,boolean isComplete) {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
		contents = squareLiveModels;
		adapter.refershData(contents);
		if (isRefresh) {// 下拉刷新
			if (contents != null && contents.size() < Constant.pageNum10) {
				mPullRefreshListView.setMode(Mode.PULL_FROM_START);
			} else {
				mPullRefreshListView.setMode(Mode.BOTH);
			}
		} else {// 上拉加载更多
//			if (squareLiveModels != null) {
//				contents.addAll(squareLiveModels);
//			} else {
//				contents = squareLiveModels;
//			}
//			adapter.refershData(contents);
			if (isComplete) {
				mPullRefreshListView.setMode(Mode.PULL_FROM_START);
			}
		}

	}

	public void setSquareFragmentListener(
			SquareFragmentListener squareFragmentListener) {
		this.squareFragmentListener = squareFragmentListener;
	}
	

}
