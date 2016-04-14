package com.mci.firstidol.view;

import com.mci.firstidol.R;

import android.animation.AnimatorSet.Builder;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


public class AlertDialog {
	private static Context context;
	private static Dialog dialog;
	private static LinearLayout lLayout_bg;
	private static TextView txt_title;
	private static TextView txt_msg;
	private static Button btn_neg;
	private static Button btn_pos;
	private static ImageView img_line;
	private static Display display;
	private static boolean showTitle = false;
	private static boolean showMsg = false;
	private static boolean showPosBtn = false;
	private static boolean showNegBtn = false;
	
	public static void show(Context mContext,String title,String message,OnClickListener confirmListener,OnClickListener cancelClickListener){
		show(mContext, title, message, null, confirmListener, null, cancelClickListener);
	}
	public static void show(Context mContext,String title,String message,String confirmTitle, OnClickListener confirmListener,String cancelTitle,OnClickListener cancelClickListener){
		
		if (dialog!=null && dialog.isShowing()) {
			dialog.dismiss();
		}
		context = mContext;
		
		showTitle = false;
		showMsg = false;
		showPosBtn = false;
		showNegBtn = false;
		
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
		builder();
		if (title!=null && title.length()>0) {
			setTitle(title);
		}
		if (message!=null && message.length()>0) {
			setMsg(message);
		}
		
		if (confirmTitle!=null && !"".equals(confirmTitle) && confirmListener!=null && !"".equals(confirmListener)){
			setPositiveButton(confirmTitle, confirmListener);
		} else if (confirmListener!=null && !"".equals(confirmListener)) {
			setPositiveButton(null, confirmListener);
		}
		
		if (cancelTitle!=null && !"".equals(cancelTitle) && cancelClickListener!=null && !"".equals(cancelClickListener)){
			setNegativeButton(cancelTitle, cancelClickListener);
		} else if (cancelClickListener!=null && !"".equals(cancelClickListener)) {
			setNegativeButton(null, cancelClickListener);
		}  
		show();
	}

	public AlertDialog(Context mContext) {
		context = mContext;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public static void builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_alertdialog, null);

		// 获取自定义Dialog布局中的控件
		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_title.setVisibility(View.GONE);
		txt_msg = (TextView) view.findViewById(R.id.txt_msg);
		txt_msg.setVisibility(View.GONE);
		btn_neg = (Button) view.findViewById(R.id.btn_neg);
		btn_neg.setVisibility(View.GONE);
		btn_pos = (Button) view.findViewById(R.id.btn_pos);
		btn_pos.setVisibility(View.GONE);
		img_line = (ImageView) view.findViewById(R.id.img_line);
		img_line.setVisibility(View.GONE);

		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);

		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

	}

	public static void setTitle(String title) {
		showTitle = true;
		if ("".equals(title)) {
			txt_title.setText(context.getResources().getString(R.string.alert_title));
		} else {
			txt_title.setText(title);
		}
	}

	public static void setMsg(String msg) {
		showMsg = true;
		if ("".equals(msg)) {
			txt_msg.setText(context.getResources().getString(R.string.alert_content));
		} else {
			txt_msg.setText(msg);
		}
	}

	public static void setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
	}

	public static void setPositiveButton(String text,
			final OnClickListener listener) {
		showPosBtn = true;
		if ("".equals(text) || text==null) {
			btn_pos.setText(context.getResources().getString(R.string.alert_confirm));
		} else {
			btn_pos.setText(text);
		}
		btn_pos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				listener.onClick(v);
			}
		});
	}

	public static void setNegativeButton(String text,
			final OnClickListener listener) {
		showNegBtn = true;
		if ("".equals(text) || text==null) {
			btn_neg.setText(context.getResources().getString(R.string.alert_cancle));
		} else {
			btn_neg.setText(text);
		}
		btn_neg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				listener.onClick(v);
			}
		});
	}

	private static void setLayout() {
		if (!showTitle && !showMsg) {
			txt_title.setText(context.getResources().getString(R.string.alert_hint));
			txt_title.setVisibility(View.VISIBLE);
		} else {
			txt_title.setVisibility(View.GONE);
		}

		if (showTitle) {
			txt_title.setVisibility(View.VISIBLE);
		} else {
			txt_title.setVisibility(View.GONE);
		}

		if (showMsg) {
			txt_msg.setVisibility(View.VISIBLE);
		} else {
			txt_msg.setVisibility(View.GONE);
		}

		if (!showPosBtn && !showNegBtn) {
			btn_pos.setText(context.getResources().getString(R.string.alert_confirm));
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
			btn_pos.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		} else if (showPosBtn && showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
			img_line.setVisibility(View.VISIBLE);
		} else if (showPosBtn && !showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
			btn_neg.setVisibility(View.GONE);
		} else if (!showPosBtn && showNegBtn) {
			btn_pos.setVisibility(View.GONE);
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
		} 
	}

	public static void show() {
		setLayout();
		dialog.show();
	}
}
