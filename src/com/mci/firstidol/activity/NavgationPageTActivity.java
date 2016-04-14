package com.mci.firstidol.activity;

import java.util.ArrayList;
import java.util.List;

import com.mci.firstidol.R;
import com.mci.firstidol.adapter.ViewPagerAdapter;
import com.mci.firstidol.utils.Utily;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class NavgationPageTActivity extends FragmentActivity implements OnClickListener, OnPageChangeListener{
	
	private ViewPager vp;  
    private ViewPagerAdapter vpAdapter;  
    private List<View> views;  
    private Button button;
	
  //引导图片资源  
    private static final int[] pics = { R.drawable.new_feature_1,
        R.drawable.new_feature_2,
        R.drawable.new_feature_3 };  
	
  //底部小店图片  
    private ImageView[] dots ;  
  //记录当前选中位置  
    private int currentIndex;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
	    setContentView(R.layout.activity_navgationpaget);  
		
        views = new ArrayList<View>();  
        
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,  
                LinearLayout.LayoutParams.WRAP_CONTENT);  
          
        //初始化引导图片列表  
        for(int i=0; i<pics.length; i++) {  
            ImageView iv = new ImageView(this);  
            iv.setLayoutParams(mParams);  
            iv.setImageResource(pics[i]);
            iv.setScaleType(ScaleType.CENTER_CROP);
            views.add(iv);  
        }  
        vp = (ViewPager) findViewById(R.id.viewpager);  
        //初始化Adapter  
        vpAdapter = new ViewPagerAdapter(views);  
        vp.setAdapter(vpAdapter);  
        //绑定回调  
        vp.setOnPageChangeListener(this);  
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        //初始化底部小点  
        initDots();  
	}
	
    private void initDots() {  
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);  
  
        dots = new ImageView[pics.length];  
  
        //循环取得小点图片  
        for (int i = 0; i < pics.length; i++) {  
            dots[i] = (ImageView) ll.getChildAt(i);  
            dots[i].setEnabled(true);//都设为灰色  
            dots[i].setOnClickListener(this);  
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应  
        }  
  
        currentIndex = 0;  
        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态  
    } 
	
	
    /** 
     *设置当前的引导页  
     */  
    private void setCurView(int position)  
    {  
        if (position < 0 || position >= pics.length) {  
            return;  
        }  
  
        vp.setCurrentItem(position);  
    }
	
    /** 
     *这只当前引导小点的选中  
     */  
    private void setCurDot(int positon)  
    {  
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {  
            return;  
        }  
  
        dots[positon].setEnabled(false);  
        dots[currentIndex].setEnabled(true);  
  
        currentIndex = positon;  
    } 
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		 setCurDot(arg0);  
	        if(arg0 == 2){
	        	button.setVisibility(View.VISIBLE);
	        }else{
	        	button.setVisibility(View.GONE);
	        }
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
        switch (v.getId()) {
		case R.id.button:
			finish();
			Utily.go2Activity(this, MainActivity.class);
			break;

		default:
			break;
		}
	}

}
