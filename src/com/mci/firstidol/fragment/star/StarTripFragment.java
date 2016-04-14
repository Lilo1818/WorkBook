package com.mci.firstidol.fragment.star;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;
import com.mci.firstidol.adapter.NetworkImageHolderView;
import com.mci.firstidol.adapter.SquareFragmentAdapter;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.SquareModel.SquareFoundModel;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.view.ToastUtils;
import com.mci.firstidol.view.convenientbanner.ConvenientBanner;
import com.mci.firstidol.view.convenientbanner.holder.CBViewHolderCreator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.TabPageIndicator;

public class StarTripFragment extends BaseFragment {

	private int currentIndex = 1;// 当前选中

	private final String[] titles = new String[4];// 标题
	private final int[] icons = new int[4];// 图片

	private List<Fragment> fragments = new ArrayList<Fragment>();
	private List<String> stars = new ArrayList<String>();
	private TabPageIndicator indicator;
	private SquareFragmentAdapter adapter;
	private FragmentManager fm;
	private ViewPager pager;
	private ImageView image_top;// 头顶图片

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");// 时间格式化字符串

	private StarAddressFragment beforeMonth;// 上个月
	private StarAddressFragment currentMonth;// 当前月
	private StarAddressFragment nextOneMonth;// 下个月
	private StarAddressFragment nextTwoMonth;// 下下个月

	private Date beforeDate;// 上个月时间
	private Date currentDate;// 当前月时间
	private Date nextOneDate;// 下个月时间
	private Date nextTwoDate;// 下下个月时间
	
	private String star_id;//明星id

	@Override
	protected int getViewId() {
		Bundle bundle = getArguments();
		if(bundle!=null){
			star_id = bundle.getString(Constant.IntentKey.star_id);
		}
		return R.layout.fragment_trip;
	}

	@Override
	protected void initView() {

		image_top = (ImageView) findViewById(R.id.image_top);
		beforeDate = getMonth(-1);
		currentDate = new Date();
		nextOneDate = getMonth(1);
		nextTwoDate = getMonth(2);
		
		
		//Toast.makeText(getActivity(), "值为+"+nextTwoDate.getTime()+"第二个+"+nextOneDate.getTime(), Toast.LENGTH_SHORT).show();
		
		titles[0] = sdf.format(beforeDate);
		titles[1] = sdf.format(currentDate);
		titles[2] = sdf.format(nextOneDate);
		titles[3] = sdf.format(nextTwoDate);

		initFragment();
		initData();

		// 初始化头部图片
		if (Constant.starModel != null) {
			ImageLoader.getInstance().displayImage(Constant.starModel.BgImage,
					image_top);
		}
	}

	/**
	 * 初始化fragment
	 */
	public void initFragment() {

		beforeMonth = new StarAddressFragment();
		Bundle beforeBundle = new Bundle();
		beforeBundle.putString(Constant.IntentKey.time_str,
				String.valueOf(beforeDate.getTime()));
		beforeBundle.putString(Constant.IntentKey.star_id, star_id);
		beforeMonth.setArguments(beforeBundle);

		currentMonth = new StarAddressFragment();
		Bundle currentBundle = new Bundle();
		currentBundle.putString(Constant.IntentKey.time_str,
				String.valueOf(currentDate.getTime()));
		currentBundle.putString(Constant.IntentKey.star_id, star_id);
		currentMonth.setArguments(currentBundle);

		nextOneMonth = new StarAddressFragment();
		Bundle nextOneBundle = new Bundle();
		nextOneBundle.putString(Constant.IntentKey.time_str,
				String.valueOf(nextOneDate.getTime()));
		nextOneBundle.putString(Constant.IntentKey.star_id, star_id);
		nextOneMonth.setArguments(nextOneBundle);

		nextTwoMonth = new StarAddressFragment();
		Bundle nextTwoBundle = new Bundle();
		nextTwoBundle.putString(Constant.IntentKey.time_str,
				String.valueOf(nextTwoDate.getTime()));
		nextTwoBundle.putString(Constant.IntentKey.star_id, star_id);
		nextTwoMonth.setArguments(nextTwoBundle);

		fragments.add(beforeMonth);
		fragments.add(currentMonth);
		fragments.add(nextOneMonth);
		fragments.add(nextTwoMonth);

	}

	/**
	 * 得到月份时间
	 * 
	 * @param current
	 * @return
	 */
	public Date getMonth(int current) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.MONTH, current);
		return calendar.getTime();
	}

	/**
	 * 加载数据
	 */
	public void initData() {
		fm = getChildFragmentManager();

		adapter = new SquareFragmentAdapter(fm, fragments, titles, icons, stars);

		indicator = (TabPageIndicator) findViewById(R.id.indicator);

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(fragments.size());

		pager.setAdapter(adapter);
		indicator.setViewPager(pager);

		indicator.setCurrentItem(currentIndex);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onEventMainThread(AnyEventType event) {
		if(event!=null&&Constant.Config.UPDATE_STAR.equals(event.message)){
			// 初始化头部图片
			if (Constant.starModel != null) {
				ImageLoader.getInstance().displayImage(Constant.starModel.BgImage,
						image_top);
			}
		}
	};

}
