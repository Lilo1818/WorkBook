package com.mci.firstidol.view;

import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;
import com.mci.firstidol.utils.DisplayUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtils {
	private static Toast toast = null;
	private static Object synObj = new Object();
	private static Toast toast_local,toast_point;

	/**
	 * Toast发送消息，默认Toast.LENGTH_LONG
	 * 
	 * @param context
	 * @param msg
	 */
	public static void show(final Context context, final String msg) {
		// showMessage(context, msg, Toast.LENGTH_SHORT);
		ToastUtils.showCustomToast(context, msg);
	}

	/**
	 * Toast发送消息:可以选择显示时间类型
	 * 
	 * @param context
	 * @param msg
	 * @param duration
	 */
	public static void show(final Context context, final String msg,
			final int duration) {
		showMessage(context, msg, duration);
	}

	/**
	 * Toast发送消息，默认Toast.LENGTH_LONG
	 * 
	 * @param context
	 * @param msg
	 *            资源ID
	 */
	public static void show(final Context context, final int msg) {
		showMessage(context, context.getResources().getString(msg),
				Toast.LENGTH_LONG);
	}

	/**
	 * Toast发送消息：可以选择显示时间长短
	 *
	 * @param context
	 * @param msg
	 *            资源id
	 * @param duration
	 */
	public static void show(final Context context, final int msg,
			final int duration) {
		showMessage(context, context.getResources().getString(msg), duration);
	}

	/**
	 * 取消toast
	 * 
	 * @param context
	 */
	public static void cancleToast(final Context context) {
		if (toast != null) {
			toast.cancel();
		}
	}

	/**
	 * 显示自定义的Toast:
	 *
	 * @param context
	 * @param msg
	 *            资源id
	 */
	public static void showMyToast(final Context context, final int msg) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_toast, null);
		TextView text = (TextView) view.findViewById(R.id.toast_message);
		text.setText(context.getResources().getString(msg));
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 100);
		toast.setView(view);
		toast.show();
	}

	/**
	 * 显示自定义toast
	 * 
	 * @param context
	 * @param msg
	 *            ：提示语
	 */
	public static void showCustomToast(Context context, final String msg) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_toast, null);
		TextView text = (TextView) view.findViewById(R.id.toast_message);
		text.setText(msg);
		if (toast_local == null) {
			toast_local = new Toast(context);
			toast_local.setDuration(Toast.LENGTH_SHORT);
			toast_local.setGravity(Gravity.CENTER, 0, 100);
			toast_local.setView(view);
			toast_local.show();
		} else {
			((TextView) toast_local.getView().findViewById(R.id.toast_message))
					.setText(msg);
		}
		toast_local.show();
	}

	public static void showCustomToast(final String msg) {
		Context context = BaseApp.getInstance();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_toast, null);
		TextView text = (TextView) view.findViewById(R.id.toast_message);
		text.setText(msg);
		if (toast_local == null) {
			toast_local = new Toast(context);
			toast_local.setDuration(Toast.LENGTH_SHORT);
			toast_local.setGravity(Gravity.CENTER, 0, 100);
			toast_local.setView(view);
			toast_local.show();
		} else {
			((TextView) toast_local.getView().findViewById(R.id.toast_message))
					.setText(msg);
		}
		toast_local.show();
	}

	/**
	 * 关闭当前Toast
	 */
	public static void cancel() {
		if (toast != null) {
			toast.cancel();
		}
	}

	/**
	 * Toast发送消息
	 *
	 * @param context
	 * @param msg
	 * @param duration
	 */
	public static void showMessage(final Context context, final String msg,
			final int duration) {
		Toast.makeText(context, msg, duration).show();
	}

	public static void showPointToast(final String msg) {
		Context context = BaseApp.getInstance();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_point, null);
		TextView text = (TextView) view.findViewById(R.id.toast_message);
		text.setText("+ " + msg + "分");
		if (toast_point == null) {
			toast_point = new Toast(context);
			toast_point.setDuration(Toast.LENGTH_SHORT);
			toast_point.setGravity(Gravity.RIGHT | Gravity.TOP, DisplayUtil.dip2px(context, 20),  DisplayUtil.dip2px(context, 60));
			toast_point.setView(view);
			toast_point.show();
		} else {
			((TextView) toast_point.getView().findViewById(R.id.toast_message))
					.setText("+ " + msg + "分");
		}
		toast_point.show();
	}
}
