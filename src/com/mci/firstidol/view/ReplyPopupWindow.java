package com.mci.firstidol.view;

import com.mci.firstidol.R;
import com.mci.firstidol.utils.LogUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class ReplyPopupWindow extends PopupWindow implements OnClickListener {

	private Context context;
	private Button btn_reply;

	private EditText et_reply;

	private OnReplyClickListener onReplyClickListener;

	public ReplyPopupWindow() {
		super();
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	public ReplyPopupWindow(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public ReplyPopupWindow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public ReplyPopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public ReplyPopupWindow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public ReplyPopupWindow(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	public ReplyPopupWindow(View contentView, int width, int height,
			boolean focusable) {
		super(contentView, width, height, focusable);
		// TODO Auto-generated constructor stub
	}

	public ReplyPopupWindow(View contentView, int width, int height) {
		super(contentView, width, height);
		// TODO Auto-generated constructor stub
	}

	public ReplyPopupWindow(View contentView) {
		super(contentView);
		// TODO Auto-generated constructor stub
	}

	public void init() {
		// 获取屏幕的高度和宽度

		setAnimationStyle(R.style.AnimationPreview);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View contentView = inflater.inflate(R.layout.layout_reply, null);

		btn_reply = (Button) contentView.findViewById(R.id.btn_send);
		et_reply = (EditText) contentView.findViewById(R.id.et_reply);
		btn_reply.setOnClickListener(this);

		btn_reply.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				LogUtils.i("sdfssssssssssss:");

				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (et_reply.getText() == null
							|| et_reply.getText().toString().length() <= 0) {
						ToastUtils.showCustomToast(context, "回复内容不能为空");
					} else {
						onReplyClickListener.onReplyClick(v);
					}

				}

				return true;
			}
		});

		et_reply.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				LogUtils.i("sdfssssssssssss:" + et_reply.hasFocus());
				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					et_reply.requestFocus();
					InputMethodManager m = (InputMethodManager) context
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

				}
				return true;
			}
		});

		setContentView(contentView);

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		// contentView.measure(w, h);
		int height = contentView.getMeasuredHeight();
		int width = contentView.getMeasuredWidth();

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x80000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);

		setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		// window.setHeight(height/2);
		// setHeight(height);
		setHeight(LinearLayout.LayoutParams.MATCH_PARENT);

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
		contentView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = contentView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					} else {

					}
				}
				return true;
			}
		});

		// 设置PopupWindow外部区域是否可触摸
		setFocusable(true); // 设置PopupWindow可获得焦点
		setTouchable(true); // 设置PopupWindow可触摸
		setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
	}

	public CharSequence getMessage() {
		return et_reply.getText();
	}

	public void setTextHint(String replyHint) {
		et_reply.setHint(replyHint);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btn_reply:

			if (et_reply.getText() == null
					|| et_reply.getText().toString().length() <= 0) {
				ToastUtils.showCustomToast(context, "回复内容不能为空");
				return;
			}

			onReplyClickListener.onReplyClick(v);

			break;

		default:
			break;
		}

	}

	public void setOnReplyClickListener(
			OnReplyClickListener onReplyClickListener) {
		this.onReplyClickListener = onReplyClickListener;
	}

	public interface OnReplyClickListener {
		public void onReplyClick(View v);
	}

	public void reset() {
		// TODO Auto-generated method stub
		et_reply.setText("");
	}

	// 隐藏软键盘
	public void hideInputManager(Context ct) {
		try {
			((InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(((Activity) ct).getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			LogUtils.i("hideInputManager Catch error,skip it!" + e);
		}
	}

	/**
	 * 关闭键盘
	 */
	public void closeKeyBoard() {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et_reply.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 打开键盘
	 */
	public void openKeyBoard() {

	}

}
