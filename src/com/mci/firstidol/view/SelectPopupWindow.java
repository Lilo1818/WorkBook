package com.mci.firstidol.view;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.utils.ImageUtils;
import com.mci.firstidol.utils.LogUtils;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

public class SelectPopupWindow extends PopupWindow implements OnClickListener,
		OnCheckedChangeListener {

	private LinearLayout ll_popupwidonw;
	private Context context;
	private Button btn_cancel;
	private RadioGroup rg_select;
	private TextView tv_title;
	private String[] data;
	private String confirmTitle, cancelTitle, title;

	private OnSelectClickListener onSelectClickListener;

	public SelectPopupWindow() {
		super();
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	public SelectPopupWindow(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public SelectPopupWindow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public SelectPopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public SelectPopupWindow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public SelectPopupWindow(Context context, String title,
			String confirmTitle, String cancelTitle, String[] data) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.data = data;
		this.title = title;
		this.confirmTitle = confirmTitle;
		this.cancelTitle = cancelTitle;
		init();
	}

	public SelectPopupWindow(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	public SelectPopupWindow(View contentView, int width, int height,
			boolean focusable) {
		super(contentView, width, height, focusable);
		// TODO Auto-generated constructor stub
	}

	public SelectPopupWindow(View contentView, int width, int height) {
		super(contentView, width, height);
		// TODO Auto-generated constructor stub
	}

	public SelectPopupWindow(View contentView) {
		super(contentView);
		// TODO Auto-generated constructor stub
	}

	public void init() {

		// Android获得屏幕信息
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metric);
		// int width = metric.widthPixels; // 屏幕宽度（像素）
		// int height = metric.heightPixels; // 屏幕高度（像素）
		float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
		// int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）

		setAnimationStyle(R.style.AnimationPreview);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View contentView = inflater.inflate(R.layout.layout_select, null);

		ll_popupwidonw = (LinearLayout) contentView
				.findViewById(R.id.ll_popupwidonw);
		btn_cancel = (Button) contentView.findViewById(R.id.btn_cancel);
		tv_title = (TextView) contentView.findViewById(R.id.tv_title);
		rg_select = (RadioGroup) contentView.findViewById(R.id.rg_select);

		RadioButton tempButton;
		int index = 0;
		if (confirmTitle != null && confirmTitle.length() > 0) {// 非空
			tempButton = new RadioButton(context);
			tempButton
					.setBackgroundResource(R.drawable.radio_exchange_select_bg); // 设置RadioButton的背景图片
			tempButton.setButtonDrawable(R.color.transparent); // 设置按钮的样式
			tempButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			tempButton.setGravity(Gravity.CENTER);
			tempButton.setPadding(0, (int) (10 * density), 0,
					(int) (10 * density));
			tempButton.setText(confirmTitle);
			tempButton.setTextColor(context.getResources().getColor(
					R.color.pop_confirm_color));
			index = index + 1;
			tempButton.setId(index);
			rg_select.addView(tempButton,
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
		}
		for (int i = 0; i < data.length; i++) {
			tempButton = new RadioButton(context);
			tempButton
					.setBackgroundResource(R.drawable.radio_exchange_select_bg); // 设置RadioButton的背景图片
			tempButton.setButtonDrawable(R.color.transparent); // 设置按钮的样式
			tempButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			tempButton.setGravity(Gravity.CENTER);
			tempButton.setPadding(0, (int) (10 * density), 0,
					(int) (10 * density));
			tempButton.setText(data[i]);
			tempButton.setTextColor(context.getResources().getColor(
					R.color.pop_other_color));
			tempButton.setId(i + index);
			rg_select.addView(tempButton,
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
		}
		rg_select.setOnCheckedChangeListener(this);
		// rg_select.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		ll_popupwidonw.setOnClickListener(this);

		if (title != null && title.length() > 0) {
			tv_title.setText(title);
		} else {
			tv_title.setVisibility(View.GONE);
		}
		if (cancelTitle != null && cancelTitle.length() > 0) {
			btn_cancel.setText(cancelTitle);
		}

		setContentView(contentView);

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		contentView.measure(w, h);
		int height = contentView.getMeasuredHeight();
		int width = contentView.getMeasuredWidth();

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);

		setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		// window.setHeight(height/2);
		// setHeight(height);
		setHeight(LinearLayout.LayoutParams.MATCH_PARENT);// 控制popupwindow屏幕

		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				AnimationSet animationSet = new AnimationSet(true);
				// 参数1～2：x轴的开始位置
				// 参数3～4：y轴的开始位置
				// 参数5～6：x轴的结束位置
				// 参数7～8：y轴的结束位置
				TranslateAnimation translateAnimation = new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, -0.5f,
						Animation.RELATIVE_TO_SELF, 0.0f);
				translateAnimation.setDuration(500);
				animationSet.addAnimation(translateAnimation);
				animationSet.setFillAfter(true);
				// test_pop_layout.startAnimation(animationSet);
			}
		});

		// contentView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		// contentView.setOnTouchListener(new OnTouchListener() {
		//
		// public boolean onTouch(View v, MotionEvent event) {
		//
		// int height = contentView.findViewById(R.id.pop_layout).getTop();
		// int y=(int) event.getY();
		// if(event.getAction()==MotionEvent.ACTION_UP){
		// if(y<height){
		// dismiss();
		// }
		// }
		// return true;
		// }
		// });

		// 设置PopupWindow外部区域是否可触摸
		setFocusable(true); // 设置PopupWindow可获得焦点
		setTouchable(true); // 设置PopupWindow可触摸
		setOutsideTouchable(false); // 设置非PopupWindow区域可触摸
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btn_cancel:
			dismiss();
			break;
		case R.id.ll_popupwidonw:
			dismiss();
			break;
		default:
			LogUtils.i("xxxxxxxxxxxxx:" + v.getId());
			// onSelectClickListener.onSelectClick(group, checkedId);
			break;
		}

	}

	public void setOnSelectClickListener(
			OnSelectClickListener onSelectClickListener) {
		this.onSelectClickListener = onSelectClickListener;
	}

	public interface OnSelectClickListener {
		public void onSelectClick(RadioGroup group, int checkedId);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		LogUtils.i("xxxxxxxxxxxxx:" + checkedId);
		((RadioButton) group.getChildAt(checkedId)).setChecked(false);
		onSelectClickListener.onSelectClick(group, checkedId);
	}

}
