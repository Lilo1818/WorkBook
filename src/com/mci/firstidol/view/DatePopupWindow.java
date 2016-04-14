package com.mci.firstidol.view;

import java.util.Calendar;

import com.mci.firstidol.R;
import com.mci.firstidol.view.wheel.OnWheelChangedListener;
import com.mci.firstidol.view.wheel.WheelView;
import com.mci.firstidol.view.wheel.adapters.ArrayWheelAdapter;
import com.mci.firstidol.view.wheel.adapters.NumericWheelAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class DatePopupWindow extends PopupWindow {

	private Context context;

	private OnComposeChangeListener onComposeChangeListener;

	public DatePopupWindow() {
		super();
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	public DatePopupWindow(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public DatePopupWindow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public DatePopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public DatePopupWindow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public DatePopupWindow(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	public DatePopupWindow(View contentView, int width, int height,
			boolean focusable) {
		super(contentView, width, height, focusable);
		// TODO Auto-generated constructor stub
	}

	public DatePopupWindow(View contentView, int width, int height) {
		super(contentView, width, height);
		// TODO Auto-generated constructor stub
	}

	public DatePopupWindow(View contentView) {
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
		View contentView = inflater.inflate(R.layout.layout_date, null);
		setContentView(contentView);

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		contentView.measure(w, h);
		int height = contentView.getMeasuredHeight();
		int width = contentView.getMeasuredWidth();

		Calendar calendar = Calendar.getInstance();

		final WheelView month = (WheelView) contentView
				.findViewById(R.id.month);
		final WheelView year = (WheelView) contentView.findViewById(R.id.year);
		final WheelView day = (WheelView) contentView.findViewById(R.id.day);

		final String months[] = new String[] { "1月", "2月", "3月", "4月", "5月",
				"6月", "7月", "8月", "9月", "10月", "11月", "12月" };

		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(year, month, day);
			}
		};

		// month
		int curMonth = calendar.get(Calendar.MONTH);

		month.setViewAdapter(new DateArrayAdapter(context, months, curMonth));
		month.setCurrentItem(curMonth);
		month.addChangingListener(listener);

		// year
		int curYear = calendar.get(Calendar.YEAR);
		// int curYear = 1900;
		String[] years = new String[200];
		for (int i = 0; i < years.length; i++) {
			years[i] = (1900 + i) + "年";
		}
		year.setViewAdapter(new DateArrayAdapter(context, years, curYear - 1900));
		// year.setViewAdapter(new DateNumericAdapter(context, 1900, curYear +
		// 100, curYear-1900));
		year.setCurrentItem(curYear - 1900);
		year.addChangingListener(listener);

		// day
		updateDays(year, month, day);
		day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
		day.addChangingListener(listener);

		setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		// window.setHeight(height/2);
		setHeight(height);

		// 设置PopupWindow外部区域是否可触摸
		setFocusable(true); // 设置PopupWindow可获得焦点
		setTouchable(true); // 设置PopupWindow可触摸
		setOutsideTouchable(false); // 设置非PopupWindow区域可触摸
	}

	/**
	 * Updates day wheel. Sets max days according to selected month and year
	 */
	void updateDays(WheelView year, WheelView month, WheelView day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR,
				calendar.get(Calendar.YEAR) + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		String[] days = new String[maxDays];
		for (int i = 0; i < maxDays; i++) {
			days[i] = (i + 1) + "日";
		}
		day.setViewAdapter(new DateArrayAdapter(context, days, calendar
				.get(Calendar.DAY_OF_MONTH) - 1));
		// day.setViewAdapter(new DateNumericAdapter(context, 1, maxDays,
		// calendar.get(Calendar.DAY_OF_MONTH) - 1));
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);

		String age = 1900
				+ year.getCurrentItem()
				+ "-"
				+ ((month.getCurrentItem() + 1) < 10 ? "0"
						+ (month.getCurrentItem() + 1) : (month
						.getCurrentItem() + 1)) + "-"
				+ (curDay < 10 ? ("0" + curDay) : curDay);

		if (onComposeChangeListener != null) {
			Log.i("xxxxxxxxxxxxx", "++++++++++++++:" + age);
			onComposeChangeListener.valueChange(age);
		}

	}

	/**
	 * Adapter for numeric wheels. Highlights the current value.
	 */
	private class DateNumericAdapter extends NumericWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateNumericAdapter(Context context, int minValue, int maxValue,
				int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			// setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFF000000);
			} else {
				view.setTextColor(0xFF808080);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class DateArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			// setTextSize(20);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFF000000);
			} else {
				view.setTextColor(0xFF808080);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}

	public void setOnComposeChangeListener(
			OnComposeChangeListener onComposeChangeListener) {
		this.onComposeChangeListener = onComposeChangeListener;
	}

	public interface OnComposeChangeListener {
		public void valueChange(String value);
	}

}
