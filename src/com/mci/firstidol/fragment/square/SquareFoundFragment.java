package com.mci.firstidol.fragment.square;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;
import com.mci.firstidol.activity.ReportActivity;
import com.mci.firstidol.activity.SquareFoundDetailActivity;
import com.mci.firstidol.activity.SquareFoundDetailAddReplyActivity;
import com.mci.firstidol.activity.SquareLiveActivity;
import com.mci.firstidol.activity.SquareWelfareDetailActivity;
import com.mci.firstidol.adapter.NetworkImageHolderView;
import com.mci.firstidol.adapter.SquareFoundListViewAdapter;
import com.mci.firstidol.adapter.SquareFoundListViewAdapter.SquareFoundListViewAdapterListener;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.listener.SquareFragmentListener;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.SquareLiveModel;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.SquareModel.SquareFoundModel;
import com.mci.firstidol.model.SquareRequest.ArticleComment;
import com.mci.firstidol.model.SquareRequest.SquareFoundDetaiCommentAddRequest;
import com.mci.firstidol.model.SquareRequest.SquareFoundDetaiSetArticleAttrRequest;
import com.mci.firstidol.utils.DateHelper;
import com.mci.firstidol.utils.DeviceUtils;
import com.mci.firstidol.utils.DisplayUtil;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.ProgressActivity;
import com.mci.firstidol.view.ToastUtils;
import com.mci.firstidol.view.convenientbanner.ConvenientBanner;
import com.mci.firstidol.view.convenientbanner.holder.CBViewHolderCreator;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SquareFoundFragment extends BaseFragment implements
		OnItemClickListener,
		com.mci.firstidol.view.convenientbanner.listener.OnItemClickListener,
		SquareFoundListViewAdapterListener {

	private PullToRefreshListView mPullRefreshListView;
	private ListView listView;
	private View view;
	private SquareFoundListViewAdapter adapter;

	private ConvenientBanner banner;

	private ArrayList<SquareLiveModel> headers;
	private ArrayList<SquareFoundModel> contents;

	private SquareFragmentListener squareFragmentListener;
	private static int foundPageIndex;
	private SquareFoundModel selectSquareFoundModel;

	private static SquareFoundFragment instance = null;
	private ProgressActivity progressActivity;
	
	private boolean currentIsRefresh;
	private int currentPageIndex;

	public static SquareFoundFragment newInstance(String content) {
		if (null == instance) {
			instance = new SquareFoundFragment();
		}
		return instance;
	}

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.layout_fragment_square_found;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	protected void initNavBar() {
		// TODO Auto-generated method stub

	}

	protected void findViewById() {
		// TODO Auto-generated method stub
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		listView = mPullRefreshListView.getRefreshableView();
		
		progressActivity = (ProgressActivity) findViewById(R.id.progressActivity);
		progressActivity.showLoading();
	}

	@SuppressWarnings("rawtypes")
	protected void initView() {
		if (foundPageIndex == 0) {
			foundPageIndex = 1;
		}
		currentIsRefresh = true;
		currentPageIndex = 1;
		
		findViewById();
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

						foundPageIndex = 1;
						didLoadDataRequest(true, foundPageIndex);

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
						foundPageIndex++;
						didLoadDataRequest(false, foundPageIndex);
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

		adapter = new SquareFoundListViewAdapter(getActivity(), null, false);
		adapter.setSquareFoundListViewAdapterListener(this);
		mPullRefreshListView.setAdapter(adapter);

		// 添加header
		View headerView = getActivity().getLayoutInflater().inflate(
				R.layout.item_square_found_header, null);
		listView.addHeaderView(headerView);
		
		banner = (ConvenientBanner) headerView
				.findViewById(R.id.convenientBanner);
		
		MarginLayoutParams margin9 = new MarginLayoutParams(banner.getLayoutParams());
		LinearLayout.LayoutParams layoutParams9 = new LinearLayout.LayoutParams(
				margin9);
		
		layoutParams9.height = (int) (DeviceUtils
				.getWidthPixels(getActivity())
				* DisplayUtil.dip2px(getActivity(), 356) / DisplayUtil.dip2px(getActivity(), 640));// 设置图片的高度

		layoutParams9.width = DeviceUtils
				.getWidthPixels(getActivity()); // 设置图片的宽度
		banner.setLayoutParams(layoutParams9);

		
		// 开始自动翻页
		banner.startTurning(4000);

	}

	@SuppressWarnings("unchecked")
	public void updateHeaderData(ArrayList<SquareLiveModel> squareFoundModels) {
		headers = squareFoundModels;
		List<HashMap<String, String>> datas = new ArrayList<>();
		// 初始化表头数据
		for (int i = 0; i < headers.size(); i++) {
			SquareLiveModel squareFoundModel = headers.get(i);
			String icon;
			if (squareFoundModel.getModelType() == 2) {//视频
				icon = squareFoundModel.getVideoIco();
			} else {
				icon = squareFoundModel.getIco();
			}
			String title = squareFoundModel.getTitle();

			HashMap<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("icon", icon);
			dataMap.put("title", title);
			datas.add(dataMap);
		}

		banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
			@Override
			public NetworkImageHolderView createHolder() {
				return new NetworkImageHolderView();
			}
		}, datas)
				// 设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
				.setPageIndicator(
						new int[] { R.drawable.ic_page_indicator,
								R.drawable.ic_page_indicator_focused })
				// 设置指示器的方向
				// .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
				// .setOnPageChangeListener(this)//监听翻页事件
				.setOnItemClickListener(this);
	}

	public void updateContentData(ArrayList<SquareFoundModel> squareFoundModels) {
		contents = squareFoundModels;
		adapter.refershData(contents);
	}

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
			ArrayList<SquareFoundModel> squareFoundModels, boolean isRefresh,
			boolean isComplete) {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
		contents = squareFoundModels;
		if (adapter == null) {
			adapter = new SquareFoundListViewAdapter(getActivity(), null, false);
		}
		adapter.refershData(contents);
		
		if (mPullRefreshListView == null) {
			return;
		}
		if (isRefresh) {// 下拉刷新
		// contents = squareFoundModels;
		// adapter.refershData(contents);

			if (contents != null && contents.size() < Constant.pageNum) {
				mPullRefreshListView.setMode(Mode.PULL_FROM_START);
			} else {
				mPullRefreshListView.setMode(Mode.BOTH);
			}
		} else {// 上拉加载更多
		// contents.addAll(squareFoundModels);
		// if (squareFoundModels!=null) {
		// contents.addAll(squareFoundModels);
		// } else {
		// contents = squareFoundModels;
		// }
		// adapter.refershData(contents);
			if (isComplete) {
				mPullRefreshListView.setMode(Mode.PULL_FROM_START);
			}
		}

	}

	public void headerRefresh() {
		mPullRefreshListView.onRefreshComplete();
		mPullRefreshListView.setShowViewWhileRefreshing(true);
		mPullRefreshListView.setCurrentModeRefresh();
		mPullRefreshListView.setRefreshing(true);
	}

	// 0:下拉刷新 1：上拉加载更多
	private void didLoadDataRequest(final boolean isRefresh, final int pageIndex) {
		// if (isRefresh) {
		// squareFragmentListener.pullRefresh(0,pageIndex);
		// } else {
		// squareFragmentListener.pullLoadMore(0, pageIndex);
		// }
		// /Date(1445623661000)/
		currentIsRefresh = isRefresh;
		currentPageIndex = pageIndex;
		
		if (contents==null || contents.size() == 0) {
			progressActivity.showLoading();
		}
		
		squareFragmentListener.pullRefresh(
				isRefresh,
				0,
				pageIndex,
				isRefresh ? "00" : DateHelper
						.getTimeFromDateStr(((SquareFoundModel) contents
								.get(contents.size() - 1)).getPublishDate()),0);
	}

	/**
	 * 点击头视图元
	 */
	@Override
	public void onItemClick(int position) {
		// TODO Auto-generated method stub
		
		
		//根据ChannelId 和 ModelType来跳转不同页面
		SquareLiveModel squareLiveModel = headers.get(position);
		long channelId = squareLiveModel.getChannelId();
		long modelType = squareLiveModel.getModelType();
		if (channelId == 49) {//发现
	        if (modelType == 2) {//跳转到视频页面
	        	Bundle bundle = new Bundle();
	        	bundle.putBoolean("IsVideo", true);
	    		bundle.putLong(Constant.IntentKey.articleID, headers.get(position)
	    				.getArticleId());
	    		Utily.go2Activity(getActivity(), SquareFoundDetailActivity.class,
	    				bundle);
	        } else {//普通页面
	        	Bundle bundle = new Bundle();
	    		bundle.putLong(Constant.IntentKey.articleID, headers.get(position)
	    				.getArticleId());
	    		Utily.go2Activity(getActivity(), SquareFoundDetailActivity.class,
	    				bundle);
	        }
	    } else if (channelId == 44) {//商城
	    	Bundle mbuBundle = new Bundle();
			mbuBundle.putSerializable(Constant.IntentKey.squareLiveModel,
					(Serializable) headers.get(position));
			mbuBundle.putString(Constant.IntentKey.typeCode,
					Constant.IntentValue.ACTIVITY_SHOPPING);
			Utily.go2Activity(activity, SquareWelfareDetailActivity.class,
					mbuBundle);
	    }else if (channelId == 43) {//福利
	    	Bundle mbuBundle = new Bundle();
			mbuBundle.putSerializable(Constant.IntentKey.squareLiveModel, headers.get(position));
			Utily.go2Activity(activity, SquareWelfareDetailActivity.class, mbuBundle);
	    } else if (channelId == 47) {//直播
	    	Bundle bundle = new Bundle();
			bundle.putSerializable(Constant.IntentKey.squareLiveModel, headers.get(position));
			Utily.go2Activity(activity, SquareLiveActivity.class, bundle);
	    }else {//文章详情,web
	    	Bundle mbuBundle = new Bundle();
	    	mbuBundle.putBoolean("IsFromFound", true);
			mbuBundle.putSerializable(Constant.IntentKey.squareLiveModel, headers.get(position));
			Utily.go2Activity(activity, SquareWelfareDetailActivity.class, mbuBundle);
	    }
	}

	/**
	 * 表单item 点击
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
		
		//根据ChannelId 和 ModelType来跳转不同页面
		SquareFoundModel squareFoundModel = contents.get(position - 2);
		long channelId = squareFoundModel.getChannelId();
		long modelType = squareFoundModel.getModelType();
		long state = squareFoundModel.getState();
		
		
		Bundle bundle = new Bundle();
		bundle.putLong(Constant.IntentKey.articleID, contents.get(position - 2)
				.getArticleId());
		Utily.go2Activity(getActivity(), SquareFoundDetailActivity.class,
				bundle);
		
		
	}

	public void setSquareFragmentListener(
			SquareFragmentListener squareFragmentListener) {
		this.squareFragmentListener = squareFragmentListener;
	}

	/**
	 * 点击右上角弹出菜单事件
	 */
	@Override
	public void suareFoundPopMenuClickIndex(int index, int position) {
		// TODO Auto-generated method stub
		selectSquareFoundModel = contents.get(position);
		switch (index) {
		case 0:
			obtainFoundDetailSetArticleAttrRequest(true);
			break;
		case 1:
			obtainFoundDetailSetArticleAttrRequest(false);
			break;
		case 2:
			obtainFoundDetailCommentFavRequest(true);
			
			break;
		case 3:
			//ToastUtils.show(getActivity(), "举报");
			Bundle bundle = new Bundle();
        	bundle.putString("ModType", "4");
        	bundle.putLong("RefId", selectSquareFoundModel.getArticleId());
    		Utily.go2Activity(getActivity(), ReportActivity.class,
    				bundle);
			break;
		default:
			break;
		}
	}

	/**
	 * 点击底部栏菜单
	 */
	@Override
	public void squareFoundPopupAnimViewClick(int index, int position) {
		// TODO Auto-generated method stub
		selectSquareFoundModel = contents.get(position);
		if (index < 4) {
			obtainFoundDetailZanRequest(selectSquareFoundModel.getArticleId(),
					index);
			//obtainFoundDetaiZanRequest(selectSquareFoundModel.getArticleId(), index, true);
		} else {
			Bundle bundle = new Bundle();
			bundle.putLong(Constant.IntentKey.articleID,
					selectSquareFoundModel.getArticleId());
			Utily.go2Activity(getActivity(),
					SquareFoundDetailAddReplyActivity.class, bundle);
		}
	}

	// 文章点赞
	private void obtainFoundDetailZanRequest(long articleId, final int index) {
		// TODO Auto-generated method stub
		BaseRequest request = new BaseRequest();
		// 请求地址
		String requestID = String.format(
				Constant.RequestContstants.Request_Square_Zan, articleId,
				index + 1);
		doAsync(false, requestID, request, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast("点赞成功");
//				selectSquareFoundModel.setHasStore(true);
				//selectSquareFoundModel.setHasUp(true);
				if (index == 0) {
					selectSquareFoundModel.setUpCount(selectSquareFoundModel
							.getUpCount() + 1);
					selectSquareFoundModel.setHasUp(true);
				} else if (index == 1) {
					selectSquareFoundModel.setUpCount(selectSquareFoundModel
							.getUpCount2() + 1);
					selectSquareFoundModel.setHasUp2(true);
				} else if (index == 2) {
					selectSquareFoundModel.setUpCount(selectSquareFoundModel
							.getUpCount3() + 1);
					selectSquareFoundModel.setHasUp3(true);
				} else if (index == 3) {
					selectSquareFoundModel.setUpCount(selectSquareFoundModel
							.getUpCount4() + 1);
					selectSquareFoundModel.setHasUp4(true);
				}
				adapter.refershData(contents);
			}
		});
	}
	
	//文章点赞与取消点赞
	public void obtainFoundDetaiZanRequest(long articleId, final int index,final boolean isFav){
		BaseRequest request = new BaseRequest();
		// 请求地址
		String requestID = String.format(isFav ? Constant.RequestContstants.Request_Square_Zan : Constant.RequestContstants.Request_Square_DeleZan, articleId,
				index + 1);
		doAsync(false, requestID, request, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast("点赞成功");
//				selectSquareFoundModel.setHasStore(true);
				selectSquareFoundModel.setHasUp(true);
				if (index == 0) {
					selectSquareFoundModel.setUpCount(selectSquareFoundModel
							.getUpCount() + 1);
				} else if (index == 1) {
					selectSquareFoundModel.setUpCount(selectSquareFoundModel
							.getUpCount2() + 1);
				} else if (index == 2) {
					selectSquareFoundModel.setUpCount(selectSquareFoundModel
							.getUpCount3() + 1);
				} else if (index == 3) {
					selectSquareFoundModel.setUpCount(selectSquareFoundModel
							.getUpCount4() + 1);
				}
				adapter.refershData(contents);
			}
		});
	}
	
	
	/**
	 * 广场-发现详情-收藏
	 * @param isCancelFav
	 */
	public void obtainFoundDetailCommentFavRequest(final boolean isFav){
		BaseRequest request = new BaseRequest();
		//请求地址
		String requestID = String.format((isFav ? Constant.RequestContstants.Request_Article_Store : Constant.RequestContstants.Request_Article_CancelStore), selectSquareFoundModel.getArticleId());		
		doAsync(false, requestID, request, new Callback<Exception>() {
			
			@Override
			public void onCallback(Exception e) {
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {
			
			@Override
			public void onCallback(String result) {
				ToastUtils.showCustomToast("收藏成功");
			}
		});
	}
	
	
	
	/**
	 * 广场-发现详情-置顶/删除
	 * @param isCancelFav
	 */
	public void obtainFoundDetailSetArticleAttrRequest(final boolean isTop){
		SquareFoundDetaiSetArticleAttrRequest request = SquareRequest.squareFoundDetaiSetArticleAttrRequest();
		request.requestMethod = Constant.Request_POST;
		request.articleId = selectSquareFoundModel.getArticleId();
		request.attr = isTop?"IsHot":"State"; //IsHot //置顶，State //删除 
		request.value = 1;
		
		//请求地址
		String requestID = Constant.RequestContstants.Request_Square_SetArticleAttr;		
		doAsync(true, requestID, request, new Callback<Exception>() {
			
			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {
			
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				//成功时
				if (isTop) {
					ToastUtils.showCustomToast("置顶成功");
					contents.remove(selectSquareFoundModel);
					contents.add(0, selectSquareFoundModel);
					adapter.refershData(contents);
				} else {
					ToastUtils.showCustomToast("删除成功");
					contents.remove(selectSquareFoundModel);
					adapter.refershData(contents);
				}
			}
		});
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}
}
