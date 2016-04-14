package com.mci.firstidol.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.content.DialogInterface.OnKeyListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;
import com.mci.firstidol.activity.FlashActivity;
import com.mci.firstidol.activity.WelcomeActivity;
import com.mci.firstidol.callbacks_and_listeners.ButtonListener;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.utils.AsyncTaskUtils;
import com.mci.firstidol.utils.LogUtils;
import com.mci.firstidol.utils.SyncImageLoader;
import com.mci.firstidol.utils.SystemBarTintManager;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.NavigationBar;


/**
 * Created by wang on 2015/6/4.
 */
@SuppressLint("NewApi")
public abstract class BaseActivity extends BaseBackActivity implements ButtonListener {

    // 内容区域的布局
    private View contentView;
    public ImageButton rightButton;
    private ImageButton leftButton;
    public TextView titleTextView;
    public RelativeLayout navBarLayout;// 导航栏布局
    public LinearLayout navLeftLayout,// 导航栏左边布局
            navCenterLayout,// 导航栏中间布局
            navTitleLayout,// 导航栏title布局
            navRightLayout,// 导航栏右边布局
            mainLinearLayout;// 主布局

    public NavigationBar navigationBar;

    public static String TOKEN;
    private SystemBarTintManager mTintManager;
    public View view_topline;//分割线
    public DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // 获取各个分块布局
        navigationBar = new NavigationBar(this);

        view_topline = navigationBar.view_topline;
        navBarLayout = navigationBar.getNavBarLayout();
        navLeftLayout = navigationBar.getNavLeftLayout();
        navCenterLayout = navigationBar.getNavCenterLayout();
        navTitleLayout = navigationBar.getNavTitleLayout();
        navRightLayout = navigationBar.getNavRightLayout();
        mainLinearLayout = navigationBar.getMainLinearLayout();

        leftButton = navigationBar.getLeftButton();
        rightButton = navigationBar.rightButton;
        titleTextView = navigationBar.getTitleTextView();


        leftButton.setOnClickListener(leftNavClickListener);
        rightButton.setOnClickListener(rightNavClickListener);
        titleTextView.setOnClickListener(titleNavClickListener);

        dataManager = DataManager.getInstance();
        init();
        setContentView(navigationBar);
        int resId = getViewId();
        setContentLayout(resId);
        
        initThreeMethod();
    }

    protected void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTransLucentStatus();
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            getContentView().setPadding(0, 0, 0, config.getPixelInsetBottom());
        }
    }

    private void init() {
        navigationBar.setButtonListener(this);

        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        int actionBarColor = Color.parseColor("#FFFFFF");
        mTintManager.setStatusBarTintColor(actionBarColor);
    }

    /**
     * 设置内容区域
     *
     * @param resId 资源文件ID
     */
    public void setContentLayout(int resId) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(resId, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        if (null != mainLinearLayout) {
            mainLinearLayout.addView(contentView, layoutParams);
            BaseApp.getInstance().addActivity(this);
        }
    }

    @Override
    public void setOnLeftBtnClickListener(View v) {
        finishActivity(this);
    }

    /**
     * 设置内容区域
     *
     * @param view View对象
     */
    public void setContentLayout(View view) {
        if (null != mainLinearLayout) {
            mainLinearLayout.addView(view);
        }
    }

    /**
     * 得到内容的View
     *
     * @return
     */
    public View getContentView() {
        return contentView;
    }

    /**
     * 得到左边的按钮
     *
     * @return
     */
    public ImageView getLeftButton() {
        return leftButton;
    }

    /**
     * 得到右边的按钮
     *
     * @return
     */
    public ImageButton getRightButton() {
        return rightButton;
    }

    /**
     * 设置左边按钮的图片资源
     *
     * @param resId
     */
    public void setLeftBtnBackgroundResource(int resId) {
        if (null != leftButton) {
            leftButton.setImageResource(resId);
        }
    }

    /**
     * 设置左边按钮的图片资源
     *
     * @param drawable
     */
    public void setLeftBtnBackgroundDrawable(Drawable drawable) {
        if (null != leftButton) {
            leftButton.setImageDrawable(drawable);
        }
    }

    /**
     * 设置右边按钮的图片资源
     *
     * @param resId
     */
    public void setRightBtnBackgroundResource(int resId) {
        if (null != rightButton) {
            rightButton.setBackgroundResource(resId);
        }
    }

    public void setRightBtnBackgroundDrawable(Drawable drawable) {
        if (null != leftButton) {
        	rightButton.setImageDrawable(drawable);
        }
    }

    /**
     * 隐藏左边的按钮
     */
    public void hideLeftBtn() {
        if (null != leftButton) {
            leftButton.setVisibility(View.GONE);
        }
    }

    /**
     * 显示左边的按钮
     */
    public void showLeftBtn() {
        if (null != leftButton) {
            leftButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏右边的按钮
     */
    public void hideRightBtn() {
        if (null != rightButton) {
            rightButton.setVisibility(View.GONE);
        }
    }

    /**
     * 显示右边的按钮
     */
    public void showRightBtn() {
        if (null != rightButton) {
            rightButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示右边的按钮
     */
    public void unEnableRightBtn() {
        if (null != rightButton) {
            rightButton.setEnabled(false);
        }
    }

    /**
     * 设置导航右边部分
     */
    public void setNavRightView(View view) {
        if (null != navRightLayout) {
            navRightLayout.removeAllViews();
            navRightLayout.addView(view);
        }
    }

    /**
     * 设置导航Title部分
     */
    public void setNavTitleView(View view) {
        if (null != navTitleLayout) {
            navTitleLayout.removeAllViews();
            navTitleLayout.addView(view);
        }
    }

    /**
     * 设置导航左边部分
     */
    public void setNavLeftView(View view) {
        if (null != navLeftLayout) {
            navLeftLayout.removeAllViews();
            navLeftLayout.addView(view);
        }
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (null != titleTextView) {
            titleTextView.setText(title);
        }
    }

    /**
     * 设置标题
     *
     * @param resId
     */
    public void setTitle(int resId) {
        if (null != titleTextView) {
            titleTextView.setText(getString(resId));
        }
    }

    /**
     * 隐藏上方的标题栏
     */
    public void hideTitle() {
        if (null != titleTextView) {
            titleTextView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示标题
     */
    public void showTitle() {
        if (null != titleTextView) {
            titleTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置导航栏背景
     */
    public void setNavBarBg(Drawable drawable) {
        if (null != navBarLayout) {
            navBarLayout.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 设置导航栏背景
     */
    public void setNavBarBg(int resid) {
        if (null != navBarLayout) {
            navBarLayout.setBackgroundResource(resid);
        }
    }

    /**
     * 隐藏导航栏
     */
    public void hideNavBar() {
        if (null != navBarLayout) {
            navBarLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 显示导航栏
     */
    public void showNavBar() {
        if (null != navBarLayout) {
            navBarLayout.setVisibility(View.VISIBLE);
        }
    }
    
	@Override
	public void setOnRightBtnClickListener(View v) {
		rightNavClick();
	}


    // 公共方法

    /**
     * 检验字符串是否为空
     *
     * @param string
     * @return
     */
    public static boolean isNull(String string) {
        boolean t1 = "".equals(string);
        boolean t2 = string == null;
        boolean t3 = "null".equals(string);
        if (t1 || t2 || t3) {
            return true;
        } else {
            return false;
        }
    }


    // 抽象方法：
    
    //得到反射view
    protected abstract int getViewId();

    /**
     * 初始化导航栏
     */
    protected abstract void initNavBar();

    /**
     * 初始化控件
     */
    protected abstract void initView();


    public abstract void rightNavClick();
    
    protected abstract void findViewById();
    
    
    public View.OnClickListener leftNavClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            leftNavClick();
        }
    };

    public View.OnClickListener rightNavClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            rightNavClick();
        }
    };


    public View.OnClickListener titleNavClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            navTitleClick();
        }
    };

    /**
     * 设置标题颜色
     *
     * @param color：颜色值
     */
    public void setTitleTextColor(int color) {
        if (titleTextView != null) {
            titleTextView.setTextColor(color);
        }
    }

    public void navTitleClick() {
    }

    public void navTwoClick() {
    }

    /**
     *
     */
    protected abstract void initData();


    private boolean isResume = false;
    //
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // AppManager.getInstance().printActivity();
        //MobclickAgent.onResume(this);
        isResume= true;
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        isResume = false;
    }

    public void showDialogWithMessage(String msg) {
        /**
         * 显示dialog
         */
        pd.setMessage(msg);
        baseHandler.sendEmptyMessage(SHOWDIALOG);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

   
    protected void initThreeMethod() {
        initNavBar();
        findViewById();
        initView();
        initData();
    }



    public void showAndEnableRightButton() {
        showRightBtn();
        if (rightButton != null) {
            rightButton.setEnabled(true);
            rightButton.setClickable(true);
        }

    }

    public void loadImage(String path, ImageView iv) {
    	if(path!=null&&!path.equals("")){
    		Bitmap bm = syncImageLoader.getBitmapFromMemory(path);
            if (bm == null || bm.isRecycled()) {
                iv.setImageResource(R.drawable.default_img);
                syncImageLoader.loadImage(-1, path, imageLoadListener, iv,
                        1);
            } else {
                iv.setImageBitmap(bm);
            }
    	}else{
    		iv.setImageResource(R.drawable.default_img);
    	}
        
    }

    SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {
        @Override
        public void onImageLoad(Integer t, Bitmap bm, View parent) {

            if (parent != null && parent instanceof ImageView) {
                ((ImageView) parent).setImageBitmap(bm);
            }

            doImageLoadFinish(bm);
        }

        @Override
        public void onError(Integer t, View parent) {
            doImageLoadError();
        }
    };


    public void doImageLoadFinish(Bitmap bm) {

    }

    public void doImageLoadError() {

    }


    /**
     * 页面视图重新。。刷新或者放数据。。。。。。
     */
    public void doResetViewOrViewData() {


    }
    
    @SuppressWarnings("rawtypes")
	protected <T> AsyncTask doAsync(final boolean isShowPorgress,
			final CharSequence requestID, final Object requestObj,
			final Callback<Exception> pExceptionCallback,
			final Callback<T> pCallback) {
		return AsyncTaskUtils.doAsync(this, null,
				getResources().getString(R.string.request_loading), requestID,
				requestObj, pCallback, pExceptionCallback, true,
				isShowPorgress);
	}
    
    
    /**
	 * 打开软键盘
	 */
    public void openKeyboard() {
		(new Handler()).postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 100);
	}
    
    /**
     * 隐藏软键盘
     * @param ct
     */
 	public void hideKeyboard() {
 		try {
 			((InputMethodManager) context
 					.getSystemService(Context.INPUT_METHOD_SERVICE))
 					.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
 							.getWindowToken(),
 							InputMethodManager.HIDE_NOT_ALWAYS);
 		} catch (Exception e) {
 			LogUtils.i("hideInputManager Catch error,skip it!" + e);
 		}
 	}


}
