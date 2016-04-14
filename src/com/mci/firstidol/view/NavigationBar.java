package com.mci.firstidol.view;


import com.mci.firstidol.R;
import com.mci.firstidol.callbacks_and_listeners.ButtonListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class NavigationBar extends LinearLayout implements OnClickListener {

    private Context context;
    //按钮
    public ImageButton rightButton;
    private ImageButton leftButton;
    private TextView titleTextView;

    private RelativeLayout navBarLayout;//导航栏布局
    private LinearLayout navLeftLayout,//导航栏左边布局
            navCenterLayout,//导航栏中间布局
            navTitleLayout,//导航栏title布局
            navRightLayout,//导航栏右边布局
            mainLinearLayout;//主布局
    private RelativeLayout allLinearLayout;//父布局

    private ButtonListener buttonListener;

    public View view_topline;

    public NavigationBar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    private void init() {
        // 导入布局并实例化
        LayoutInflater.from(context).inflate(R.layout.nav_bar, this, true);
        leftButton = (ImageButton) findViewById(R.id.btn_left);
        rightButton = (ImageButton) findViewById(R.id.btn_right);
        titleTextView = (TextView) findViewById(R.id.tv_title);
        allLinearLayout = (RelativeLayout) findViewById(R.id.ll_all);
        navBarLayout = (RelativeLayout) findViewById(R.id.nav_bar);
        mainLinearLayout = (LinearLayout) findViewById(R.id.content_view);
        navLeftLayout = (LinearLayout) findViewById(R.id.nav_left);
        navCenterLayout = (LinearLayout) findViewById(R.id.nav_center);
        navTitleLayout = (LinearLayout) findViewById(R.id.nav_title);
        navRightLayout = (LinearLayout) findViewById(R.id.nav_right);
        view_topline = findViewById(R.id.view_topline);
        if (leftButton != null) {
            leftButton.setOnClickListener(this);
        }
        if (rightButton != null) {
            rightButton.setOnClickListener(this);
        }
    }

    public ImageButton getLeftButton() {
        return leftButton;
    }

    public void setLeftButton(ImageButton leftButton) {
        this.leftButton = leftButton;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
    }


    public RelativeLayout getNavBarLayout() {
        return navBarLayout;
    }

    public void setNavBarLayout(RelativeLayout navBarLayout) {
        this.navBarLayout = navBarLayout;
    }

    public LinearLayout getNavLeftLayout() {
        return navLeftLayout;
    }

    public void setNavLeftLayout(LinearLayout navLeftLayout) {
        this.navLeftLayout = navLeftLayout;
    }

    public LinearLayout getNavCenterLayout() {
        return navCenterLayout;
    }

    public void setNavCenterLayout(LinearLayout navCenterLayout) {
        this.navCenterLayout = navCenterLayout;
    }

    public LinearLayout getNavTitleLayout() {
        return navTitleLayout;
    }

    public void setNavTitleLayout(LinearLayout navTitleLayout) {
        this.navTitleLayout = navTitleLayout;
    }

    public LinearLayout getNavRightLayout() {
        return navRightLayout;
    }


    public void setNavRightLayout(LinearLayout navRightLayout) {
        this.navRightLayout = navRightLayout;
    }

    public LinearLayout getMainLinearLayout() {
        return mainLinearLayout;
    }

    public RelativeLayout getAllLinearLayout() {
        return allLinearLayout;
    }

    public void setMainLinearLayout(LinearLayout mainLinearLayout) {
        this.mainLinearLayout = mainLinearLayout;
    }

    public void setButtonListener(ButtonListener buttonListener) {
        this.buttonListener = buttonListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left://左按钮
                buttonListener.setOnLeftBtnClickListener(v);
                break;
            case R.id.btn_right://右按钮牛
                buttonListener.setOnRightBtnClickListener(v);
                break;
            default:
                break;
        }
    }

}
