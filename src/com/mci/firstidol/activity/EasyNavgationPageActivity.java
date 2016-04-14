package com.mci.firstidol.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.mci.firstidol.R;
import com.mci.firstidol.adapter.NavgationPageFragmentAdapter;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class EasyNavgationPageActivity extends FragmentActivity {
	
	NavgationPageFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //去除title   
	    requestWindowFeature(Window.FEATURE_NO_TITLE);  
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
//        	ActionBar actionBar = getActionBar();  
//        	actionBar.hide();  
        }
	    
        setContentView(R.layout.layout_navgation_page);

        mAdapter = new NavgationPageFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(3);

        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        //We set this on the indicator, NOT the pager
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
               // Toast.makeText(EasyNavgationPageActivity.this, "Changed to page " + position, Toast.LENGTH_SHORT).show();
                
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}