package com.mci.firstidol.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;
import com.mci.firstidol.activity.FlashActivity;
import com.mci.firstidol.activity.LoginActivity;
import com.mci.firstidol.activity.MainActivity;
import com.mci.firstidol.activity.WelcomeActivity;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.ParseJson;
import com.mci.firstidol.utils.SyncImageLoader;
import com.mci.firstidol.view.ToastUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by wang on 2015/8/25.
 */
@SuppressLint("NewApi")
public class BaseBackActivity extends FragmentActivity {

	public Context context;

	// 退出的时间定义
	long lastPressedTime = 0;
	long exitTime = 1000;// 两次下按的时间间隔

	public static final int SHOWDIALOG = 1;
	public static final int HIDEDIALOG = 2;

	public ParseJson parseJson;// json解析工具

	public ProgressDialog pd;
	public SyncImageLoader syncImageLoader;// 图片加载器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if ((this instanceof WelcomeActivity) || (this instanceof FlashActivity)) {
			if (Build.VERSION.SDK_INT < 16) {  
	            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
	                    WindowManager.LayoutParams.FLAG_FULLSCREEN);  
	        }  else {
	        	View decorView = getWindow().getDecorView();  
	        	// Hide the status bar.  
	        	int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;  
	        	decorView.setSystemUiVisibility(uiOptions);  
	        	// Remember that you should never show the action bar if the  
	        	// status bar is hidden, so hide that too if necessary.  
//	        	ActionBar actionBar = getActionBar();  
//	        	actionBar.hide();  
	        }
		}
        
        
		EventBus.getDefault().register(this);

		context = this;

		parseJson = new ParseJson();
		syncImageLoader = new SyncImageLoader(context);

		/**
		 * 初始对话框
		 */
		pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCancelable(false);
		pd.setOnKeyListener(onKeyListener);

	}

	protected void fillStatusBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// Translucent navigation bar
			window.setFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

	protected void setTransLucentStatus() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

	public Handler baseHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SHOWDIALOG:
				if (pd != null) {
					if (!isFinishing()) {
						pd.show();
					}
				}
				break;
			case HIDEDIALOG:
				if (pd != null) {
					pd.hide();
				}
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 显示对话框
	 */
	public void showDialog() {
		baseHandler.sendEmptyMessage(SHOWDIALOG);
	}

	/**
	 * 隐藏对话框
	 */
	public void hideDialog() {
		baseHandler.sendEmptyMessage(HIDEDIALOG);
	}

	// 滚动条事件
	private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				dismissDialog();
			}
			return false;
		}
	};

	/**
	 * 关闭对话框
	 * 
	 * @author 王海啸
	 */
	public void dismissDialog() {
		if (isFinishing()) {
			return;
		}
		if (null != pd && pd.isShowing()) {
			pd.dismiss();
		}
	}

	/**
	 * 重写返回键监听处理
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (this instanceof LoginActivity) {
				finish();
				return super.onKeyDown(keyCode, event);
			} else if (this instanceof MainActivity) {
				if (mainExit()) {
					long current = System.currentTimeMillis();
					if (current - lastPressedTime < exitTime) {
						exitThisOnly();
						AppExit(context);
						ToastUtils.cancel();
						return super.onKeyDown(keyCode, event);
					} else {
						lastPressedTime = current;
						ToastUtils.showCustomToast(context, "再按一次退出程序");
						return false;
					}
				} else {
					return false;
				}

			} else {
				exitThis();
				return true;
			}
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}
	
	/** 
     * 退出应用程序 
     */  
    public void AppExit(Context context) {  
        try {  
            ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);  
            activityMgr.restartPackage(context.getPackageName());  
            System.exit(0);  
        } catch (Exception e) { }  
    }  

	/**
	 * 主页退出方法判断
	 * 
	 * @return
	 */
	public boolean mainExit() {
		return true;
	};

	public void loadImage(String path, ImageView iv) {
		Bitmap bm = syncImageLoader.getBitmapFromMemory(path);
		if (bm == null || bm.isRecycled()) {
			syncImageLoader.loadImage(-1, path, imageLoadListener, iv, 1);
		} else {
			iv.setImageBitmap(bm);
		}
	}

	public SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {
		@Override
		public void onImageLoad(Integer t, Bitmap bm, View parent) {
			if (parent != null) {
				if (bm == null || bm.isRecycled()) {
					((ImageView) parent)
							.setImageResource(R.drawable.ic_launcher);
				} else {
					((ImageView) parent).setImageBitmap(bm);
				}
			}
		}

		@Override
		public void onError(Integer t, View parent) {
			if (parent != null) {
				((ImageView) parent).setImageResource(R.drawable.ic_launcher);
			}
		}
	};

	/**
	 * 关闭指定的Activity
	 * 
	 * @param activity
	 */
	public void finishActivity(Activity activity) {
		BaseApp.getInstance().finishActivity(activity);
		overridePendingTransition(R.anim.activity_left_harf_in,
				R.anim.activity_right_out);
	}

	/**
	 * 仅关闭无动画
	 * 
	 * @param activity
	 */
	public void onlyExit(Activity activity) {
		BaseApp.getInstance().finishActivity(activity);
	}

	/**
	 * 关闭当前activity
	 */
	public void exitThisOnly() {
		finishActivity(this);
	}

	// 退出
	public void exitThis() {
		leftNavClick();
	}

	public void leftNavClick() {
		finishActivity(this);
	}

	private boolean isResume = false;

	//
	@Override
	protected void onResume() {
		super.onResume();
		isResume = true;
	}
	
	public void onEventMainThread(AnyEventType event) {
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);// 反注册EventBus
	}

	@Override
	protected void onPause() {
		super.onPause();
		isResume = false;
	}

}
