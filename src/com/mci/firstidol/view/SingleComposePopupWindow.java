package com.mci.firstidol.view;

import java.util.ArrayList;

import com.mci.firstidol.R;
import com.mci.firstidol.view.wheel.OnWheelScrollListener;
import com.mci.firstidol.view.wheel.WheelView;
import com.mci.firstidol.view.wheel.adapters.AbstractWheelTextAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class SingleComposePopupWindow extends PopupWindow {

	private Context context;
	// Scrolling flag
	private boolean scrolling = false;
	private TextView tv;
	private Button button_ok;

	private OnSingleComposeChangeListener onSingleComposeChangeListener;

	// private String countries[] =
	// {"不限","北京","天津","河北","山西","内蒙古","辽宁","吉林","黑龙江","上海","江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","广东","广西","海南","重庆","四川","贵州","云南","西藏","陕西","甘肃","青海","宁夏","新疆","台湾","香港","澳门"};
	private ArrayList<String> hospitals;

	public SingleComposePopupWindow() {
		super();
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	public SingleComposePopupWindow(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public SingleComposePopupWindow(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public SingleComposePopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public SingleComposePopupWindow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public SingleComposePopupWindow(Context context, ArrayList<String> hospitals) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.hospitals = hospitals;
		init();
	}

	public SingleComposePopupWindow(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	public SingleComposePopupWindow(View contentView, int width, int height,
			boolean focusable) {
		super(contentView, width, height, focusable);
		// TODO Auto-generated constructor stub
	}

	public SingleComposePopupWindow(View contentView, int width, int height) {
		super(contentView, width, height);
		// TODO Auto-generated constructor stub
	}

	public SingleComposePopupWindow(View contentView) {
		super(contentView);
		// TODO Auto-generated constructor stub
	}

	public void init() {
		// 加上下面两行可以用back键关闭popupwindow，否则必须调用dismiss();
		ColorDrawable dw = new ColorDrawable(-00000);
		setBackgroundDrawable(dw);

		// 获取屏幕的高度和宽度

		setAnimationStyle(R.style.AnimationPreview);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.layout_compose, null);
		setContentView(contentView);

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		contentView.measure(w, h);
		int height = contentView.getMeasuredHeight();
		int width = contentView.getMeasuredWidth();

		Log.i("xxxxxxxxxxxxxxxxx", "width:" + width + "-- height:" + height);

		// tv = (TextView)contentView.findViewById(R.id.tv_cityName);

		final WheelView country = (WheelView) contentView
				.findViewById(R.id.country);
		country.setVisibleItems(6);
		country.setViewAdapter(new CountryAdapter(context));

		country.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}

			public void onScrollingFinished(WheelView wheel) {
				scrolling = false;

				// tv.setText( AddressData.PROVINCES[country.getCurrentItem()]
				// );
				onSingleComposeChangeListener.valueComposeChange(hospitals
						.get(country.getCurrentItem()));
			}
		});

		setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		// window.setHeight(height/2);
		setHeight(height);

		// 设置PopupWindow外部区域是否可触摸
		setFocusable(true); // 设置PopupWindow可获得焦点
		setTouchable(true); // 设置PopupWindow可触摸
		setOutsideTouchable(false); // 设置非PopupWindow区域可触摸
	}

	/**
	 * Adapter for countries
	 */
	private class CountryAdapter extends AbstractWheelTextAdapter {
		// Countries names

		/**
		 * Constructor
		 */
		protected CountryAdapter(Context context) {
			super(context, R.layout.country_layout, NO_RESOURCE);

			setItemTextResource(R.id.country_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return hospitals.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return hospitals.get(index);
		}
	}

	public void setOnSingleComposeChangeListener(
			OnSingleComposeChangeListener onSingleComposeChangeListener) {
		this.onSingleComposeChangeListener = onSingleComposeChangeListener;
	}

	public interface OnSingleComposeChangeListener {
		public void valueComposeChange(String value);
	}

}
