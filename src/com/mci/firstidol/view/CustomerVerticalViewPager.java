package com.mci.firstidol.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by wang on 2015/8/27.
 */
public class CustomerVerticalViewPager extends ViewPager {
    /**
     * 禁止滑动
     */
    private boolean Froze_Scroll = false;

    public CustomerVerticalViewPager (Context context) {
        super(context);
    }

    public CustomerVerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (Froze_Scroll) {
            return false;
        }
        else {
            return super.onTouchEvent(ev);
        }
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (Froze_Scroll) {
            return false;
        }
        else {
            //return true;
            return super.onInterceptTouchEvent(ev);
        }
    }
    /**
     * 设置ViewPager不能滑动
     */
    public void setIsSrollFroze(boolean isFroze){
        this.Froze_Scroll = isFroze;
    }
}
