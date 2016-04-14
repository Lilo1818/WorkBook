package com.mci.firstidol.fragment.square;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;
import com.mci.firstidol.activity.LoginActivity;
import com.mci.firstidol.activity.MyInfoActivity;
import com.mci.firstidol.adapter.SquareOrderListViewAdapter;
import com.mci.firstidol.adapter.SquareWelfareListViewAdapter;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.listener.SquareFragmentListener;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.StarModel;
import com.mci.firstidol.model.SquareModel.SquareFoundModel;
import com.mci.firstidol.model.SquareModel.SquareListFanModel;
import com.mci.firstidol.model.SquareModel.SquareListStarModel;
import com.mci.firstidol.model.SquareModel.SquareLiveChatModel;
import com.mci.firstidol.model.SquareRequest.SquareLiveChatSaveRequest;
import com.mci.firstidol.model.SquareRequest.SquareOrderStarTopRequest;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.DateHelper;
import com.mci.firstidol.utils.DisplayUtil;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.utils.LogUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.AlertDialog;
import com.mci.firstidol.view.ProgressActivity;
import com.mci.firstidol.view.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public final class OrderTypeFragment extends BaseFragment implements
		OnItemClickListener, OnClickListener {
	private static final String KEY_CONTENT = "TestFragment:Content";

	private String content;
	private TextView text;
	private View headerView;
	private PullToRefreshListView mPullRefreshListView;
	private ListView listView;

	private LinearLayout ll_2th, ll_1th, ll_3th;

	private ArrayList<String> listData;
	private SquareOrderListViewAdapter adapter;
	private String mContent = "???";

	private SquareFragmentListener squareFragmentListener;
	private int orderPageIndex;

	public int position;//所在 Fragment 的位置
	private int currentIndex;//点击的位置

	private ArrayList<Object> contents;
	
	private ProgressActivity progressActivity;
	private boolean currentIsRefresh;
	private int currentPageIndex;

	public static OrderTypeFragment newInstance(String content) {
		OrderTypeFragment fragment = new OrderTypeFragment();

		return fragment;
	}

	// private static OrderTypeFragment instance = null;
	// public static OrderTypeFragment newInstance(String content) {
	// if (null == instance) {
	// instance = new OrderTypeFragment();
	// }
	// return instance;
	// }

	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	//
	// if ((savedInstanceState != null) &&
	// savedInstanceState.containsKey(KEY_CONTENT)) {
	// mContent = savedInstanceState.getString(KEY_CONTENT);
	// }
	// }
	//
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	//
	// view = inflater.inflate(R.layout.layout_fragment_square_list_type, null);
	//
	//
	//
	// initView();
	//
	// return view;
	// }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mContent);
	}

	public void updateContent(ArrayList<Object> squareListModels) {

		Log.i("xxxxxxxxxxxxx", "updateContent1" + "----" + squareListModels);
		if (squareListModels != null) {
			adapter.setListData(squareListModels);
		}

	}

	protected void findViewById() {
		// TODO Auto-generated method stub
		headerView = View.inflate(activity, R.layout.item_square_order_header,
				null);
		ll_2th = (LinearLayout) headerView.findViewById(R.id.ll_2th);
		ll_1th = (LinearLayout) headerView.findViewById(R.id.ll_1th);
		ll_3th = (LinearLayout) headerView.findViewById(R.id.ll_3th);

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		listView = mPullRefreshListView.getRefreshableView();

		listView.addHeaderView(headerView);

		ll_1th.setOnClickListener(this);
		ll_2th.setOnClickListener(this);
		ll_3th.setOnClickListener(this);
		
		progressActivity = (ProgressActivity) findViewById(R.id.progressActivity);
		progressActivity.showLoading();
	}

	protected void initView() {
		// headerView = View.inflate(activity,R.layout.item_square_order_header,
		// null);
		// findViewById();
		if (orderPageIndex == 0) {
			orderPageIndex = 1;
		}
		
		currentIsRefresh = true;
		currentPageIndex = 1;

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

						orderPageIndex = 1;
						didLoadDataRequest(true, orderPageIndex);
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
						orderPageIndex++;
						didLoadDataRequest(false, orderPageIndex);
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
		// 选择事件
		mPullRefreshListView.setOnItemClickListener(this);

		adapter = new SquareOrderListViewAdapter(getActivity(), null, false);
		mPullRefreshListView.setAdapter(adapter);
		// initData();
		
		
		initheaderImageHight();
	}

	private void initheaderImageHight() {
		// TODO Auto-generated method stub
		if (position == 1) {//粉丝排行榜的前三名头像为正方形
			final ImageView firstImageView = (ImageView) headerView.findViewWithTag(400+"");
			final ImageView secondImageView = (ImageView) headerView.findViewWithTag(401+"");
			final ImageView thirdImageView = (ImageView) headerView.findViewWithTag(402+"");
			final LinearLayout ll_image = (LinearLayout) headerView.findViewById(R.id.ll_image);
			
			WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
			int width = wm.getDefaultDisplay().getWidth();
			int height = wm.getDefaultDisplay().getHeight();
	        
	        RelativeLayout.LayoutParams para = (android.widget.RelativeLayout.LayoutParams) firstImageView.getLayoutParams();
	        int width1 = (width - DisplayUtil.dip2px(activity, 80))*10/24;
	        para.height=width1;//修改高度
	        firstImageView.setLayoutParams(para);
	        
	        RelativeLayout.LayoutParams para2 = (android.widget.RelativeLayout.LayoutParams) secondImageView.getLayoutParams();
	        int width2 = (width - DisplayUtil.dip2px(activity, 80))*8/24;
	        para2.height=width2;//修改高度
	        secondImageView.setLayoutParams(para2);
	        
	        RelativeLayout.LayoutParams para3 = (android.widget.RelativeLayout.LayoutParams) thirdImageView.getLayoutParams();
	        int width3 = (width - DisplayUtil.dip2px(activity, 80))*6/24;
	        para3.height=width3;//修改高度
	        thirdImageView.setLayoutParams(para3);
	        
	        
	        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
	        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
	        ll_1th.measure(w, h);  
	        int height4 =ll_1th.getMeasuredHeight();  
	        int width4 =ll_1th.getMeasuredWidth();  
	        
	        LinearLayout.LayoutParams para4 = (android.widget.LinearLayout.LayoutParams) ll_image.getLayoutParams();
//	        width4 = DisplayUtil.dip2px(activity, 80);
	        para4.height=height4;//修改高度
	        ll_image.setLayoutParams(para4);
			
		}else if(position == 0){
			final ImageView firstImageView = (ImageView) headerView.findViewWithTag(400+"");
			final ImageView secondImageView = (ImageView) headerView.findViewWithTag(401+"");
			final ImageView thirdImageView = (ImageView) headerView.findViewWithTag(402+"");
			final LinearLayout ll_image = (LinearLayout) headerView.findViewById(R.id.ll_image);
			
			WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
			int width = wm.getDefaultDisplay().getWidth();
			int height = wm.getDefaultDisplay().getHeight();
	        
	        RelativeLayout.LayoutParams para = (android.widget.RelativeLayout.LayoutParams) firstImageView.getLayoutParams();
	        int width1 = (width - DisplayUtil.dip2px(activity, 80))*10/24;
	        para.height=width1;//修改高度
	        firstImageView.setLayoutParams(para);
	        
	        RelativeLayout.LayoutParams para2 = (android.widget.RelativeLayout.LayoutParams) secondImageView.getLayoutParams();
	        int width2 = (width - DisplayUtil.dip2px(activity, 80))*8/24;
	        para2.height=width2;//修改高度
	        secondImageView.setLayoutParams(para2);
	        
	        RelativeLayout.LayoutParams para3 = (android.widget.RelativeLayout.LayoutParams) thirdImageView.getLayoutParams();
	        int width3 = (width - DisplayUtil.dip2px(activity, 80))*6/24;
	        para3.height=width3;//修改高度
	        thirdImageView.setLayoutParams(para3);
	        
	        
	        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
	        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
	        ll_1th.measure(w, h);  
	        int height4 =ll_1th.getMeasuredHeight();  
	        int width4 =ll_1th.getMeasuredWidth();  
	        
	        LinearLayout.LayoutParams para4 = (android.widget.LinearLayout.LayoutParams) ll_image.getLayoutParams();
//	        width4 = DisplayUtil.dip2px(activity, 80);
	        para4.height=height4;//修改高度
	        ll_image.setLayoutParams(para4);
		}
		
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
	public void onItemClick(AdapterView<?> parent, View view, int position1,
			long id) {
		// TODO Auto-generated method stub
//		ToastUtils.showCustomToast("Page-position:" + position + "--"+ position1);

		
		// 先判断是否关注改
		int index = position1 + 1;
		if (position == 0) {//明星排行榜
			if (DataManager.getInstance().isLogin) {//登录
				validateIsFollowStar(index);
			} else {
				Utily.go2Activity(activity, LoginActivity.class);
			}
			
		} else {//粉丝排行榜
			goToFanPersonInfo(index);
		}
	}

	private void didLoadDataRequest(final boolean isRefresh, final int pageIndex) {
		squareFragmentListener.pullRefresh(isRefresh, 3, pageIndex, "",
				position);
	}

	public void setSquareFragmentListener(
			SquareFragmentListener squareFragmentListener) {
		this.squareFragmentListener = squareFragmentListener;
	}

	public void refreshComplete() {
		mPullRefreshListView.onRefreshComplete();
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

	public void updateContentData(ArrayList<Object> squareListModels,
			boolean isRefresh, boolean isComplete) {
		Log.i("xxxxxxxxxxxxx", "进了" + "----" + "进来了");
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
		contents = squareListModels;
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

		// 更新前三名的信息
		updateHeaderData();

	}

	private void updateHeaderData() {
		// TODO Auto-generated method stub
		if (contents != null && contents.size() > 0) {
			for (int i = 0; i < 3; i++) {

				Object object = contents.get(i);//

				if (object instanceof SquareListStarModel) {
					SquareListStarModel squareListStarModel = (SquareListStarModel) object;

					ImageLoader.getInstance().displayImage(
							squareListStarModel.getAvatar(),
							(ImageView) headerView.findViewWithTag((400 + i)
									+ ""), BaseApp.cornerCircleOptions);
					((TextView) headerView.findViewWithTag((100 + i) + ""))
							.setText(squareListStarModel.getStarName());
					((TextView) headerView.findViewWithTag((200 + i) + ""))
							.setText("活跃度:");
					((TextView) headerView.findViewWithTag((300 + i) + ""))
							.setText(squareListStarModel.getLate7DayPoints() + "");

				} else if (object instanceof SquareListFanModel) {
					SquareListFanModel squareListFanModel = (SquareListFanModel) object;
					ImageLoader.getInstance().displayImage(
							squareListFanModel.getAvatar(),
							(ImageView) headerView.findViewWithTag((400 + i)
									+ ""), BaseApp.cornerCircleOptions);

					((TextView) headerView.findViewWithTag((100 + i) + ""))
							.setText(squareListFanModel.getNickName());
					((TextView) headerView.findViewWithTag((200 + i) + ""))
							.setText("积分:");
					((TextView) headerView.findViewWithTag((300 + i) + ""))
							.setText(squareListFanModel.getExperience() + "");
				}
			}
		}
	}

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.layout_fragment_square_list_type;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_1th:
			obtainClickEvent(0);
			break;
		case R.id.ll_2th:
			obtainClickEvent(1);
			break;
		case R.id.ll_3th:
			obtainClickEvent(2);
			break;

		default:
			break;
		}
	}

	private void obtainClickEvent(int index) {
		// TODO Auto-generated method stub
		
		
		if (position == 0) {//明星排行榜
			if (DataManager.isLogin) {
				validateIsFollowStar(index);
			} else {
				Utily.go2Activity(activity, LoginActivity.class);
			}
			
		} else {//粉丝排行榜
			goToFanPersonInfo(index);
		}
	}

	private void goToFanPersonInfo(int index) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle.putLong(Constant.IntentKey.userID, ((SquareListFanModel) contents.get(index)).getUserId());
		Utily.go2Activity(activity, MyInfoActivity.class,bundle);
	}
	
	private void goToStarPageInfo() {
		// TODO Auto-generated method stub
//		Bundle bundle = new Bundle();
//		bundle.putLong(Constant.IntentKey.userID, ((SquareListStarModel) contents.get(currentIndex)).getStarId());
//		Utily.go2Activity(activity, MyInfoActivity.class,bundle);
		
		SquareListStarModel squareListStarModel = (SquareListStarModel) contents.get(currentIndex);
		Constant.starModel = new StarModel();
		
		Constant.starModel.Avatar = squareListStarModel.getAvatar();
		Constant.starModel.BgImage = squareListStarModel.getBgImage();
		Constant.starModel.CreateDate = squareListStarModel.getCreateDate();
		Constant.starModel.Description = squareListStarModel.getDescription();
		Constant.starModel.FollowCount = (int)squareListStarModel.getFollowCount();
		Constant.starModel.MagazineId = (int)squareListStarModel.getMagazineId();
		Constant.starModel.starId = (int)squareListStarModel.getStarId();
		Constant.starModel.StarName = squareListStarModel.getStarName();
		
		changeStar();
		Constant.is_selected_star = true;
	}
	
	/**
	 *切换明星
	 */
	public void changeStar(){
		AnyEventType type = new AnyEventType(Constant.Config.SELECTED_STAR, AnyEventType.Type_Default);
        EventBus.getDefault().post(type);
	}

	public void validateIsFollowStar(int index) {
		currentIndex = index;
		BaseRequest baseRequest = new BaseRequest();// 其它参数需要继承此类
		// baseRequest.requestMethod = "get";//默认为get
		// 请求地址
		String requestID = String.format(
				Constant.RequestContstants.Request_Order_StarIsFollow,
				((SquareListStarModel) contents.get(index)).getStarId());
		doAsync(true, requestID, baseRequest, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				LogUtils.i(result);
				if ("false".equals(result)) {//未关注
					showHintDialog();
				} else {//关注
					//跳转到明星主页
					goToStarPageInfo();
				}
			}
		});
	}

	protected void showHintDialog() {
		// TODO Auto-generated method stub
		AlertDialog.show(activity, "提示", "是否将他/她添加到我的追星", "添加", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addMyFollowStarList();
			}
		}, "取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	protected void addMyFollowStarList() {
		// TODO Auto-generated method stub
		BaseRequest baseRequest = new BaseRequest();// 其它参数需要继承此类
		// baseRequest.requestMethod = "get";//默认为get
		// 请求地址
		String requestID = String.format(
				Constant.RequestContstants.Request_Order_StarFollow,
				((SquareListStarModel) contents.get(currentIndex)).getStarId());
		doAsync(true, requestID, baseRequest, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				LogUtils.i(result);
				ToastUtils.showCustomToast("添加成功");
				//添加成功，把该明星置顶
				obtainStarTopEvent();
				
			}
		});
	}

	protected void obtainStarTopEvent() {
		// TODO Auto-generated method stub
		SquareOrderStarTopRequest request = SquareRequest.squareOrderStarTopRequest();// 其它参数需要继承此类
		request.requestMethod = Constant.Request_POST;
		request.starid = ((SquareListStarModel) contents.get(currentIndex)).getStarId();
		String requestID = Constant.RequestContstants.Request_Order_StarFollowTop;
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
				LogUtils.i(result);
				ToastUtils.showCustomToast("置顶成功");
				//置顶成功后，跳转到追星界面
				goToStarPageInfo();
			}
		});
	}
	
	
}
