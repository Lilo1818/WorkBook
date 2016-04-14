package com.mci.firstidol.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.mci.firstidol.adapter.SquareFragmentAdapter;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.fragment.square.SquareFoundFragment;
import com.mci.firstidol.fragment.square.SquareListFragment;
import com.mci.firstidol.fragment.square.SquareLiveFragment;
import com.mci.firstidol.fragment.square.SquareWelfareFragment;
import com.mci.firstidol.listener.SquareFragmentListener;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.SquareLiveModel;
import com.mci.firstidol.model.SquareModel.SquareFoundModel;
import com.mci.firstidol.model.SquareModel.SquareListFanModel;
import com.mci.firstidol.model.SquareModel.SquareListStarModel;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.utils.LogUtils;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.ProgressActivity;
import com.mci.firstidol.view.ToastUtils;
import com.viewpagerindicator.TabPageIndicator;
import com.mci.firstidol.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SquareFragment extends BaseFragment implements OnClickListener,
		OnPageChangeListener, SquareFragmentListener {

	TextView textView;
	EditText editText;
	Button button, button11, btn_go1;
	OnExchangeListener onExchangeListener;

	private static int currentIndex;

	private static final String[] titles = new String[] { "", "", "", "" };
	private static final int[] icons = new int[] {
			R.drawable.square_found_selector, R.drawable.square_live_selector,
			R.drawable.square_welfare_selector,
			R.drawable.square_list_selector, };

	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private ArrayList<String> squares = new ArrayList<String>();
	private TabPageIndicator indicator;
	private SquareFragmentAdapter adapter;
	private FragmentManager fm;
	private ViewPager pager;

	private ProgressActivity progressActivity;

	// 定义四个模块的数据源
	// 1、发现相关
	ArrayList<SquareFoundModel> squareFoundModels;
	ArrayList<SquareLiveModel> squareFoundModelHeaders;

	// 2、直播相关
	ArrayList<SquareLiveModel> squareLiveModels;
	// 3、福利相关
	ArrayList<SquareLiveModel> squareWelfareModels;
	// 4、排行榜相关
	ArrayList<SquareListStarModel> squareListStarModels;
	ArrayList<SquareListFanModel> squareListFanModels;

	public OnExchangeListener getOnExchangeListener() {
		return onExchangeListener;
	}

	public void setOnExchangeListener(OnExchangeListener onExchangeListener) {
		this.onExchangeListener = onExchangeListener;
	}

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.fragment1;
	}

	// Container Activity must implement this interface
	public interface OnExchangeListener {
		public void exchangeFromSource(String from);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public void findViewById() {
		initFragments();

		fm = getChildFragmentManager();

		adapter = new SquareFragmentAdapter(fm, fragments, titles, icons,
				squares);

		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setOnPageChangeListener(this);

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(fragments.size());

		pager.setAdapter(adapter);
		indicator.setViewPager(pager);

		indicator.setCurrentItem(currentIndex);
	}

	private void initFragments() {
		// TODO Auto-generated method stub
		SquareFoundFragment squareFoundFragment = SquareFoundFragment
				.newInstance(null);
		squareFoundFragment.setSquareFragmentListener(this);
		SquareLiveFragment squareLiveFragment = SquareLiveFragment
				.newInstance(null);
		squareLiveFragment.setSquareFragmentListener(this);
		SquareWelfareFragment squareWelfareFragment = SquareWelfareFragment
				.newInstance(null);
		squareWelfareFragment.setSquareFragmentListener(this);
		SquareListFragment squareListFragment = SquareListFragment
				.newInstance(null);
		squareListFragment.setSquareFragmentListener(this);
		fragments.add(squareFoundFragment);
		fragments.add(squareLiveFragment);
		fragments.add(squareWelfareFragment);
		fragments.add(squareListFragment);

	}

	public void initView() {
		findViewById();
		initData();
	}

	public void initData() {
		// SquareFoundFragment fragment = ((SquareFoundFragment) fragments
		// .get(currentIndex));
		// // 查询广场--发现列表
		// squareFoundRequest(fragment,true,1);
		// // 查询广场--轮播效果图片信息
		// squareBannerRequest(fragment);

		obtainFoundData(true, 0, 1,"00");
	}

	/**
	 * 以下三个方法为TabPageIndicator监听
	 */
	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		currentIndex = position;
		if (position == 0) {// 发现
			SquareFoundFragment fragment = ((SquareFoundFragment) fragments
					.get(currentIndex));
			if (squareFoundModels == null) {
				squareFoundRequest(fragment, true, 1,"00");
			}
			if (squareFoundModelHeaders == null) {
				squareBannerRequest(fragment);
			}
		} else if (position == 1) {// 直播
			SquareLiveFragment fragment = ((SquareLiveFragment) fragments
					.get(currentIndex));
			if (squareLiveModels == null) {
				squareLiveRequest(fragment, true, 1,"00");
			}
		} else if (position == 2) {// 福利
			SquareWelfareFragment fragment = ((SquareWelfareFragment) fragments
					.get(currentIndex));
			if (squareWelfareModels == null) {
				squareWelfareRequest(fragment, true, 1,"00");
			}
		} else if (position == 3) {// 排行榜
			SquareListFragment fragment = ((SquareListFragment) fragments
					.get(currentIndex));
			if (squareListStarModels == null || squareListFanModels == null) {
				squareListRequest(fragment, true, 1,"00",0);
			}
		}

	}

	

	// ********发现回调事件************************************

	@Override
	public void pullRefresh(boolean isRefresh, int fragmentIndex, int pageIndex,String maxDate, int currentPosition) {
		// TODO Auto-generated method stub

		switch (fragmentIndex) {
		case 0:
			obtainFoundData(isRefresh, fragmentIndex, pageIndex,maxDate);
			break;
		case 1:
			obtainLiveData(isRefresh, fragmentIndex, pageIndex, maxDate);
			break;
		case 2:
			obtainWelfareData(isRefresh, fragmentIndex, pageIndex, maxDate);
			break;
		case 3:
			//F:首次刷新
			if ("F".equals(maxDate) &&  SquareListFragment.currentIndex == 0 && squareListStarModels == null) {
				obtainListData(isRefresh, fragmentIndex, pageIndex, maxDate,currentPosition);
			} else if ("F".equals(maxDate) && SquareListFragment.currentIndex == 1 && squareListFanModels==null) {
				obtainListData(isRefresh, fragmentIndex, pageIndex, maxDate,currentPosition);
			} else if(!"F".equals(maxDate)){
				obtainListData(isRefresh, fragmentIndex, pageIndex, maxDate,currentPosition);
			}
			
			break;

		default:
			break;
		}
	}
	
	
	private void obtainListData(boolean isRefresh, int fragmentIndex,
			int pageIndex, String maxDate, int currentPosition) {
		// TODO Auto-generated method stub
		SquareListFragment fragment = ((SquareListFragment) fragments
				.get(fragmentIndex));
		// 查询广场--排行榜列表
		squareListRequest(fragment, isRefresh, pageIndex,maxDate, currentPosition);
	}

	private void obtainWelfareData(boolean isRefresh, int fragmentIndex,
			int pageIndex, String maxDate) {
		// TODO Auto-generated method stub
		SquareWelfareFragment fragment = ((SquareWelfareFragment) fragments
				.get(fragmentIndex));
		// 查询广场--福利列表
		squareWelfareRequest(fragment, isRefresh, pageIndex,maxDate);
	}

	

	// 处理刷新事件
	public void obtainFoundData(boolean isRefresh, int fragmentIndex,
			int pageIndex,String maxDate) {
		SquareFoundFragment fragment = ((SquareFoundFragment) fragments
				.get(fragmentIndex));
		// 查询广场--发现列表
		squareFoundRequest(fragment, isRefresh, pageIndex,maxDate);
		// 查询广场--轮播效果图片信息
		squareBannerRequest(fragment);
	}

	// 处理刷新事件
	public void obtainLiveData(boolean isRefresh, int fragmentIndex,
			int pageIndex,String maxDate) {
		SquareLiveFragment fragment = ((SquareLiveFragment) fragments
				.get(fragmentIndex));
		// 查询广场--发现列表
		squareLiveRequest(fragment, isRefresh, pageIndex,maxDate);
	}
	
	
	
	/**
	 * 广场--排行榜
	 * @param fragment
	 * @param isRefresh
	 * @param pageIndex
	 */
	private synchronized void squareListRequest(final SquareListFragment fragment,
			final boolean isRefresh, int pageIndex, String maxDate,final int currentPosition) {
		// TODO Auto-generated method stub
		BaseRequest baseRequest = new BaseRequest();// 其它参数需要继承此类
		// baseRequest.requestMethod = "get";//默认为get
		// 请求地址
		String requestID = String.format(
				(currentPosition==0 ? Constant.RequestContstants.Request_Star_Order_List:Constant.RequestContstants.Request_Fan_Order_List), pageIndex,
				Constant.pageNum);
		doAsync(false, requestID, baseRequest, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				// ToastUtils.showMyToast(e.getLocalizedMessage());
//				fragment.refreshComplete(currentPosition);
				if (fragment!=null) {
					String errorMsg = e.getMessage();
					if ("无网络".equals(errorMsg)) {
						fragment.refreshComplete(currentPosition,true,true);
					} else {
						fragment.refreshComplete(currentPosition,true,false);
					}
				}
			}
		}, new Callback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				LogUtils.i(result);
				
				
				 result = GsonUtils.objectToJson(GsonUtils.getJsonValue(result, "Items"));
				Type type;
				if (currentPosition == 0) {
					type = new TypeToken<ArrayList<SquareListStarModel>>() {
					}.getType();
					
					ArrayList<SquareListStarModel> tempSquareListStarModels = (ArrayList<SquareListStarModel>) GsonUtils
							.jsonToList(result, type);
					boolean isComplete = false;
					if (!isRefresh) {//上拉加载更多
						if (tempSquareListStarModels.size()<Constant.pageNum) {
							isComplete = true;
						}
						squareListStarModels.addAll(tempSquareListStarModels);
					} else {
						squareListStarModels = tempSquareListStarModels;
					}
					
					fragment.updateContentData(currentPosition,squareListStarModels, isRefresh,isComplete);
					
				} else {
					type = new TypeToken<ArrayList<SquareListFanModel>>() {
					}.getType();
					
					ArrayList<SquareListFanModel> tempSquareListFanModels = (ArrayList<SquareListFanModel>) GsonUtils
							.jsonToList(result, type);
					boolean isComplete = false;
					if (!isRefresh) {//上拉加载更多
						if (tempSquareListFanModels.size()<Constant.pageNum) {
							isComplete = true;
						}
						squareListFanModels.addAll(tempSquareListFanModels);
					} else {
						squareListFanModels = tempSquareListFanModels;
					}
					
					fragment.updateContentData(currentPosition,squareListFanModels, isRefresh,isComplete);
				}
				
				fragment.refreshComplete(currentPosition,false,false);
//				fragment.refreshComplete(currentPosition);
				

//				squareWelfareModels = (ArrayList<SquareLiveModel>) GsonUtils
//						.jsonToList(result, type);
//				fragment.updateContentData(squareWelfareModels, isRefresh);

			}
		});
	}
	
	
	/**
	 * 广场--福利
	 * @param fragment
	 * @param isRefresh
	 * @param pageIndex
	 */
	private void squareWelfareRequest(final SquareWelfareFragment fragment,
			final boolean isRefresh, int pageIndex, String maxDate) {
		// TODO Auto-generated method stub
		BaseRequest baseRequest = new BaseRequest();// 其它参数需要继承此类
		// baseRequest.requestMethod = "get";//默认为get
		// 请求地址
		String requestID = String.format(
				Constant.RequestContstants.Request_Welfare_List, 43, pageIndex,
				Constant.pageNum, maxDate);
		doAsync(false, requestID, baseRequest, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				// ToastUtils.showMyToast(e.getLocalizedMessage());
				if (fragment!=null) {
					String errorMsg = e.getMessage();
					if ("无网络".equals(errorMsg)) {
						fragment.refreshComplete(true,true);
					} else {
						fragment.refreshComplete(true,false);
					}
				}
			}
		}, new Callback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				LogUtils.i(result);
				
				Type type = new TypeToken<ArrayList<SquareLiveModel>>() {
				}.getType();

//				squareWelfareModels = (ArrayList<SquareLiveModel>) GsonUtils.jsonToList(result, type);
				ArrayList<SquareLiveModel> tempSquareWelfareModels = (ArrayList<SquareLiveModel>) GsonUtils
						.jsonToList(result, type);
				boolean isComplete = false;
				if (!isRefresh) {//上拉加载更多
					if (tempSquareWelfareModels.size()<Constant.pageNum) {
						isComplete = true;
					}
					squareWelfareModels.addAll(tempSquareWelfareModels);
				} else {
					squareWelfareModels = tempSquareWelfareModels;
				}
				
				fragment.updateContentData(squareWelfareModels, isRefresh,isComplete);
				fragment.refreshComplete(false,false);
			}
		});
	}
	
	/**
	 * 广场--直播
	 * @param fragment
	 * @param isRefresh
	 * @param pageIndex
	 */
	private void squareLiveRequest(final SquareLiveFragment fragment,
			final boolean isRefresh, int pageIndex,String maxDate) {
		// TODO Auto-generated method stub
		BaseRequest baseRequest = new BaseRequest();// 其它参数需要继承此类
		// baseRequest.requestMethod = "get";//默认为get
		// 请求地址
		String requestID = String.format(
				Constant.RequestContstants.Request_Live_List, 47, pageIndex,
				Constant.pageNum, maxDate);
		doAsync(false, requestID, baseRequest, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				// ToastUtils.showMyToast(e.getLocalizedMessage());
				if (fragment!=null) {
					String errorMsg = e.getMessage();
					if ("无网络".equals(errorMsg)) {
						fragment.refreshComplete(true,true);
					} else {
						fragment.refreshComplete(true,false);
					}
				}
			}
		}, new Callback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				LogUtils.i(result);
				
				Type type = new TypeToken<ArrayList<SquareLiveModel>>() {
				}.getType();

//				squareLiveModels = (ArrayList<SquareLiveModel>) GsonUtils
//						.jsonToList(result, type);
				
				ArrayList<SquareLiveModel> tempSquareLiveModels = (ArrayList<SquareLiveModel>) GsonUtils
						.jsonToList(result, type);
				boolean isComplete = false;
				if (!isRefresh) {//上拉加载更多
					if (tempSquareLiveModels.size()<Constant.pageNum10) {
						isComplete = true;
					}
					squareLiveModels.addAll(tempSquareLiveModels);
				} else {
					squareLiveModels = tempSquareLiveModels;
				}
				
				fragment.updateContentData(squareLiveModels, isRefresh,isComplete);
				fragment.refreshComplete(false,false);
			}
		});
	}

	/**
	 * 广场--发现，表头
	 */
	private void squareBannerRequest(final SquareFoundFragment fragment) {
		// TODO Auto-generated method stub
		BaseRequest baseRequest = new BaseRequest();// 其它参数需要继承此类
		// 请求地址
		String requestID = String.format(
				Constant.RequestContstants.Request_Square_recommendUrl, "0");
		doAsync(false, requestID, baseRequest, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
//				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				LogUtils.i(result);
				Type type = new TypeToken<ArrayList<SquareLiveModel>>() {
				}.getType();
				squareFoundModelHeaders = (ArrayList<SquareLiveModel>) GsonUtils
						.jsonToList(result, type);

				fragment.updateHeaderData(squareFoundModelHeaders);
			}
		});
	}

	/**
	 * 广场--发现，表单列表
	 */
	private void squareFoundRequest(final SquareFoundFragment fragment,
			final boolean isRefresh, int pageIndex,String maxDate) {
		// TODO Auto-generated method stub
		BaseRequest baseRequest = new BaseRequest();// 其它参数需要继承此类
		// baseRequest.requestMethod = "get";//默认为get
		// 请求地址
		String requestID = String.format(
				Constant.RequestContstants.Request_Square_List, 49, pageIndex,
				Constant.pageNum, maxDate);
		doAsync(false, requestID, baseRequest, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				// ToastUtils.showMyToast(e.getLocalizedMessage());
				if (fragment!=null) {
					String errorMsg = e.getMessage();
					if ("无网络".equals(errorMsg)) {
						fragment.refreshComplete(true,true);
					} else {
						fragment.refreshComplete(true,false);
					}
				}
				
				
			}
		}, new Callback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				LogUtils.i(result);
				
				Type type = new TypeToken<ArrayList<SquareFoundModel>>() {
				}.getType();

				ArrayList<SquareFoundModel> tempSquareFoundModels = (ArrayList<SquareFoundModel>) GsonUtils
						.jsonToList(result, type);
				boolean isComplete = false;
				if (!isRefresh) {//上拉加载更多
					if (tempSquareFoundModels.size()<Constant.pageNum) {
						isComplete = true;
					}
					squareFoundModels.addAll(tempSquareFoundModels);
				} else {
					squareFoundModels = tempSquareFoundModels;
				}
				
				fragment.updateContentData(squareFoundModels, isRefresh,isComplete);
				fragment.refreshComplete(false,false);

			}
		});
	}
	
	
	
	
	
}
