package com.mci.firstidol.fragment.star;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;
import com.mci.firstidol.activity.ApplyMasterActivity;
import com.mci.firstidol.adapter.MasterAdapter;
import com.mci.firstidol.adapter.SquareFragmentAdapter;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.fragment.star.StarIndexTextFragment.TextLoad;
import com.mci.firstidol.model.MasterModel;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.CustomViewPager;
import com.mci.firstidol.view.CustomerVerticalViewPager;
import com.mci.firstidol.view.HorizontalListView;
import com.mci.firstidol.view.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.TabPageIndicator;

public class StarIndexFragment extends BaseFragment implements OnClickListener,
		TextLoad, OnPageChangeListener {

	private final int INITDATA = 1;// 耗时操作完成回调

	private int currentIndex = 0;// 当前选中
	private String[] titles;// 标题
	private int[] icons;// 图片

	private List<Fragment> fragments = new ArrayList<Fragment>();
	private List<String> stars = new ArrayList<String>();
	private TabPageIndicator indicator;
	private SquareFragmentAdapter adapter;
	private FragmentManager fm;
	private CustomViewPager pager;

	private PullToRefreshScrollView scrollView;// 上下滑动滚动框
	private HorizontalListView master_listView;// 版主的横向列表
	private ImageView image_top_back;// 头部广告背景图
	private ImageView image_applyMaster;// 申请版主
	private CustomerVerticalViewPager viewPager;

	private BaseAdapter masterAdapter;// 版主列表适配器
	private List<Object> masterList;// 版主数据列表

	private StarIndexTextFragment soundFragment;// 明星说
	private StarIndexTextFragment observationFragment;// 观察团
	private StarIndexTextFragment hotFragment;// 热议

	@Override
	protected int getViewId() {
		return R.layout.fragment_star_index;
	}

	@Override
	protected void initView() {

		scrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollView);
		master_listView = (HorizontalListView) findViewById(R.id.master_listView);
		master_listView.setViewPager(viewPager);
		image_top_back = (ImageView) findViewById(R.id.image_top);
		image_applyMaster = (ImageView) findViewById(R.id.image_applyMaster);

		image_applyMaster.setOnClickListener(this);

		scrollView.setMode(PullToRefreshBase.Mode.BOTH);
		scrollView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						pullToReflush(refreshView);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						downMore(refreshView);
					}

				});

		if(Constant.starModel!=null){
			if(Constant.starModel.IsCheckIn){
				initTabData(true);
				initFragment(true);
			}else{
				initTabData(false);
				initFragment(false);
			}
		}else{
			initTabData(false);
			initFragment(false);
		}
		
		initPageData();
		if(Constant.starModel!=null){
			getMasterRequest(String.valueOf(Constant.starModel.starId));
		}

		// 初始化头部图片
		if (Constant.starModel != null) {
			ImageLoader.getInstance().displayImage(Constant.starModel.BgImage,
					image_top_back);
		}

	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(image_top_back!=null){
			image_top_back.setFocusable(true);
			image_top_back.setFocusableInTouchMode(true);
			image_top_back.requestFocus();
		}
	}

	/**
	 * 刷新
	 */
	public void pullToReflush(PullToRefreshBase<ScrollView> refreshView) {
		String label = DateUtils.formatDateTime(activity,
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		// 更新最后一次刷新时间
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

		// 设置初始化
		if(soundFragment!=null){
			((IndexDataFlush) soundFragment).pullToReflushData();
		}
		if(hotFragment!=null){
			((IndexDataFlush) hotFragment).pullToReflushData();
		}
		if(observationFragment!=null){
			((IndexDataFlush) observationFragment).pullToReflushData();
		}
	}
	
	/**
	 * 设置viewpager
	 * @param viewPager
	 */
	public void setViewpager(CustomerVerticalViewPager viewPager){
		this.viewPager = viewPager;
		if(master_listView!=null){
			master_listView.setViewPager(viewPager);
		}
	}

	/**
	 * 加载更多
	 * 
	 * @param refreshView
	 */
	public void downMore(PullToRefreshBase<ScrollView> refreshView) {
		String label = DateUtils.formatDateTime(activity,
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		// 更新最后一次刷新时间
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

		if(soundFragment!=null){
			((IndexDataFlush) soundFragment).loadMore();
		}
		if(hotFragment!=null){
			((IndexDataFlush) hotFragment).loadMore();
		}
		if(observationFragment!=null){
			((IndexDataFlush) observationFragment).loadMore();
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case INITDATA:

				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_applyMaster:
			Utily.go2Activity(activity, ApplyMasterActivity.class);
			break;

		default:
			break;
		}

	}

	/**
	 * 初始化tab选项卡
	 * 
	 * @param hasInfo
	 *            ：是否含有观察团 true：含有 false：不含有
	 */
	public void initTabData(boolean hasInfo) {
		if (hasInfo) {
			titles = new String[] { "", "", "" };
			icons = new int[] { R.drawable.star_sound_drawable,
					R.drawable.star_bot_drawable,
					R.drawable.star_observation_drawable };
		} else {
			titles = new String[] { "", "" };
			icons = new int[] { R.drawable.star_sound_drawable,
					R.drawable.star_bot_drawable };
		}
	}

	/**
	 * 初始化fragment
	 * 
	 * @param hasInfo
	 *            ：是否包含观察团
	 */
	public void initFragment(boolean hasInfo) {

		soundFragment = new StarIndexTextFragment();
		soundFragment.setIsChild(true);
		soundFragment.setListener(this);
		Bundle bundle = new Bundle();
		bundle.putString(Constant.IntentKey.typeCode, Constant.IntentValue.ACTIVITY_FANS);
		soundFragment.setArguments(bundle);
		
		hotFragment = new StarIndexTextFragment();
		hotFragment.setIsChild(true);
		hotFragment.setListener(this);
		Bundle hotBundle = new Bundle();
		hotBundle.putString(Constant.IntentKey.typeCode, Constant.IntentValue.ACTIVITY_HOT);
		hotFragment.setArguments(hotBundle);

		fragments.add(soundFragment);
		fragments.add(hotFragment);

		if (hasInfo) {
			observationFragment = new StarIndexTextFragment();
			observationFragment.setListener(this);
			observationFragment.setIsChild(true);
            Bundle obBundle = new Bundle();	
            obBundle.putString(Constant.IntentKey.typeCode, Constant.IntentValue.ACTIVITY_SEE);
            observationFragment.setArguments(obBundle);
			
			fragments.add(observationFragment);
		}

	}

	/**
	 * 得到版主请求
	 */
	public void getMasterRequest(String starId) {
		pd.setMessage("正在获取版主列表");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		String processURL = Constant.RequestContstants.URL
				+ Constant.RequestContstants.Request_master_list + "/" + starId;

		ConnectionService.getInstance().serviceConnUseGet(activity, processURL,
				null, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean isComon = parseJson.isCommon(jsonObject);
							if (isComon) {
								masterList = parseJson.getModelList(jsonObject,
										MasterModel.class);
								initMasterData();
							} else {
								ToastUtils.showCustomToast(activity, "获取版主失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCustomToast(activity, "获取版主失败");
						}

					}

					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						ToastUtils.showCustomToast(activity, "获取版主失败");
					}
				});
	}

	/**
	 * 初始化版主数据
	 */
	public void initMasterData() {
		masterAdapter = new MasterAdapter(activity, masterList, null);
		master_listView.setAdapter(masterAdapter);
	}

	/**
	 * 加载数据
	 */
	public void initPageData() {
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.clearDisappearingChildren();
		pager = (CustomViewPager) findViewById(R.id.pager);
		fm = getChildFragmentManager();
		adapter = new SquareFragmentAdapter(fm, fragments, titles, icons, stars);
		pager.setOffscreenPageLimit(fragments.size());
		pager.setAdapter(adapter);

		indicator.setViewPager(pager);
		indicator.setCurrentItem(0);
	}

	/**
	 * 页面数据
	 * 
	 * @author wanghaixiao
	 * 
	 */
	public interface IndexDataFlush {
		public void pullToReflushData();

		public void loadMore();
	}

	@Override
	public void hasLoading() {
		scrollView.onRefreshComplete();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		currentIndex = arg0;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onEventMainThread(AnyEventType event) {
		if(event!=null&&Constant.Config.UPDATE_STAR.equals(event.message)){
			// 初始化头部图片
			if (Constant.starModel != null) {
				ImageLoader.getInstance().displayImage(Constant.starModel.BgImage,
						image_top_back);
				getMasterRequest(String.valueOf(Constant.starModel.starId));
				if(soundFragment==null){
					if(Constant.starModel!=null){
						if(Constant.starModel.IsCheckIn){
							initTabData(true);
							initFragment(true);
						}else{
							initTabData(false);
							initFragment(false);
						}
					}
					initPageData();
				}
			}
			
		}
	};

}
