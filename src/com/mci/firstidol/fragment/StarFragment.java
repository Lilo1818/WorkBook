package com.mci.firstidol.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mci.firstidol.R;
import com.mci.firstidol.activity.AllStarActivity;
import com.mci.firstidol.activity.LoginActivity;
import com.mci.firstidol.activity.MainActivity;
import com.mci.firstidol.activity.OnlyFragemntActivity;
import com.mci.firstidol.adapter.SquareFragmentAdapter;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.fragment.star.StarAddressFragment;
import com.mci.firstidol.fragment.star.StarIndexFragment;
import com.mci.firstidol.fragment.star.StarPhotoFragment;
import com.mci.firstidol.fragment.star.StarTripFragment;
import com.mci.firstidol.fragment.star.StarVideoFragment;
import com.mci.firstidol.model.StarModel;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.CustomerVerticalViewPager;
import com.mci.firstidol.view.ToastUtils;
import com.viewpagerindicator.TabPageIndicator;

import de.greenrobot.event.EventBus;

public class StarFragment extends BaseFragment implements OnPageChangeListener,
		OnClickListener {

	private int currentIndex = 0;// 当前选中

//	private final String[] titles = new String[] { "主页", "美图", "视频", "行程" };// 标题
//	private final int[] icons = new int[] { R.drawable.star_index_drawable,
//			R.drawable.star_photo_drawable, R.drawable.star_video_drawable,
//			R.drawable.star_address_drawable, };// 图片
	
	private final String[] titles = new String[] { "", "", "", "" };// 标题
	private final int[] icons = new int[] { R.drawable.square_star_main_selector,
			R.drawable.square_star_image_selector, R.drawable.square_star_video_selector,
			R.drawable.square_star_way_selector, };// 图片

	private List<Fragment> fragments = new ArrayList<Fragment>();
	private List<String> stars = new ArrayList<String>();
	private TabPageIndicator indicator;
	private SquareFragmentAdapter adapter;
	private FragmentManager fm;
	private CustomerVerticalViewPager pager;
	private LinearLayout layout_content;// 主要内容
	private RelativeLayout layout_nodata;// 空页面
	private ImageView btn_login;// 登录按钮
	private int nodata_type = 0;// 无数据的类型 0：正常有数据 1：未登录 2：无明星

	// 定义fragment
	private StarIndexFragment starIndexFragment;// 首页
	private StarPhotoFragment starPhotoFragment;// 相册
	private StarVideoFragment starVideoFragment;// 视频
	private StarTripFragment starTripFragment;// 行程

	private StarSelected starSelected;// 选中的元素

	private int hasStarData = 0;// 1：获取明星数据错误 2:明星数据为空
	
	private StarModel starModel;//明星对象
	

	@Override
	protected int getViewId() {
		Bundle bundle = getArguments();
		if(bundle!=null){
			starModel = (StarModel) bundle.getSerializable(Constant.IntentKey.starModel);
		}
		return R.layout.fragment_star;
	}

	@Override
	protected void initView() {

		layout_content = (LinearLayout) findViewById(R.id.layout_content);
		layout_nodata = (RelativeLayout) findViewById(R.id.layout_nodata);
		pager = (CustomerVerticalViewPager) findViewById(R.id.pager);
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setOnPageChangeListener(this);
		
		btn_login = (ImageView) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		initFragment();
		initData();
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		if (!DataManager.isLogin) {
			showLoginView();
		} else {
			if (Constant.starModel != null) {
				showContentView();
			} else {
				showNoStarView();
			}
		}
	}

	/**
	 * 初始化fragment
	 */
	public void initFragment() {

		starIndexFragment = new StarIndexFragment();
		starIndexFragment.setIsChild(true);
		starIndexFragment.setViewpager(pager);
		starPhotoFragment = new StarPhotoFragment();
		starPhotoFragment.setIsChild(true);
		starVideoFragment = new StarVideoFragment();
		starVideoFragment.setIsChild(true);
		starTripFragment = new StarTripFragment();
		starTripFragment.setIsChild(true);
		
		if(starModel!=null){
			Bundle indexBundle = new Bundle();
			indexBundle.putSerializable(Constant.IntentKey.starModel, starModel);
		}

		fragments.add(starIndexFragment);
		fragments.add(starPhotoFragment);
		fragments.add(starVideoFragment);
		fragments.add(starTripFragment);

	}

	/**
	 * 加载数据
	 */
	public void initData() {
		fm = getChildFragmentManager();

		adapter = new SquareFragmentAdapter(fm, fragments, titles, icons, stars);

		pager.setOffscreenPageLimit(fragments.size());

		pager.setAdapter(adapter);
		indicator.setViewPager(pager);

		indicator.setCurrentItem(currentIndex);
	}

	/**
	 * 显示登录页面
	 */
	public void showLoginView() {
		layout_content.setVisibility(View.GONE);
		layout_nodata.setVisibility(View.VISIBLE);
		btn_login.setBackgroundResource(R.drawable.no_login);
		nodata_type = 1;
	}

	/**
	 * 显示明星页面
	 */
	public void showNoStarView() {
		layout_content.setVisibility(View.GONE);
		layout_nodata.setVisibility(View.VISIBLE);
		btn_login.setBackgroundResource(R.drawable.add_star);   
		nodata_type = 2;
	}

	/**
	 * 显示内容页面
	 */
	public void showContentView() {
		layout_content.setVisibility(View.VISIBLE);
		layout_nodata.setVisibility(View.GONE);
	}

	/**
	 * 设置选中的元素
	 * 
	 * @param starSelected
	 */
	public void setSatrSelected(StarSelected starSelected) {
		this.starSelected = starSelected;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		if (starSelected!=null) {
			starSelected.selectedItem(arg0);
		}
		
	}

	/**
	 * 追星里面的显示回调
	 * 
	 * @author wanghaixiao
	 * 
	 */
	public interface StarSelected {
		public void selectedItem(int position);
	}

	@Override
	protected void findViewById() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			if (nodata_type == 2) {
				if (hasStarData == Constant.ConfigValue.STARDATAERROR) {
					getMyStarsRequest();
				} else {
//					HashMap<String, String> pa = new HashMap<String, String>();
//					pa.put(Constant.IntentKey.typeCode,
//							Constant.IntentValue.ACTIVITY_STAR);
//
//					Utily.go2Activity(activity, OnlyFragemntActivity.class, pa,
//							null);
					
					Utily.go2Activity(activity, AllStarActivity.class);
				}
			} else if (nodata_type == 1) {
				Utily.go2Activity(activity, LoginActivity.class);
			}
			break;

		default:
			break;
		}

	}

	/**
	 * 获取我的明星的网络请求
	 */
	public void getMyStarsRequest() {
		pd.setMessage("获取明星");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		String processURL = Constant.RequestContstants.URL
				+ Constant.RequestContstants.Request_myStar_list;

		ConnectionService.getInstance().serviceConnUseGet(activity, processURL,
				null, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if (check) {
								List<Object> list = parseJson.getModelList(
										jsonObject, StarModel.class);
								if (list != null && list.size() > 0) {
									saveStar(list);
									Constant.starModel = (StarModel) list
											.get(0);
									updateStarNotice();
									hasStarData = 0;
								} else {
									ToastUtils.showCustomToast(activity,
											"没有关注的明星");
									hasStarData = Constant.ConfigValue.NOSTARDATA;
								}
							} else {
								ToastUtils
										.showCustomToast(activity, "获取我的明星失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCustomToast(activity, "获取我的明星失败");
						}
					}

					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						ToastUtils.showCustomToast(activity, "获取我的明星失败");
					}
				});
	}

	/**
	 * 更新明星通知
	 */
	public void updateStarNotice() {
		if (Constant.hasClickStar) {
			AnyEventType type = new AnyEventType(Constant.Config.UPDATE_STAR,
					AnyEventType.Type_Default);
			EventBus.getDefault().post(type);
		}
	}

	/**
	 * 保存明星到数据库
	 * 
	 * @param starList
	 */
	public void saveStar(List<Object> starList) {
		int length = starList.size();
		for (int i = 0; i < length; i++) {
			StarModel starModel = (StarModel) starList.get(i);
			starModel.save();
		}
	}

	@Override
	public void onEventMainThread(AnyEventType event) {
		if (event != null) {
			if (Constant.Config.UPDATE_STAR.equals(event.message)) {
				showContentView();
			} else if (Constant.Config.LOGINOUT.equals(event.message)) {
				showLoginView();
			} else if (Constant.Config.UPDATE_STAR_ERROR.equals(event.message)) {
				hasStarData = event.Type;
			}
		}
	};
}
