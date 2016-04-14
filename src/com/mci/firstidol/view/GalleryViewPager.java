
package com.mci.firstidol.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GalleryViewPager extends ViewPager {
    PointF last;
    public TouchImageView mCurrentView;
    public View otherView;

    public GalleryViewPager(Context context) {
        super(context);
    }

    public GalleryViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float[] handleMotionEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                last = new PointF(event.getX(0), event.getY(0));
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                PointF curr = new PointF(event.getX(0), event.getY(0));
                return new float[] { curr.x - last.x, curr.y - last.y };

        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                super.onTouchEvent(event);
            }

            float[] difference = handleMotionEvent(event);
            
            if (mCurrentView!=null) {
            	if (mCurrentView.pagerCanScroll()) {
                    return super.onTouchEvent(event);
                } else {
                    if (difference != null && mCurrentView.onRightSide && difference[0] < 0) {
                        return super.onTouchEvent(event);
                    }
                    if (difference != null && mCurrentView.onLeftSide && difference[0] > 0) {
                        return super.onTouchEvent(event);
                    }
                    if (difference == null && (mCurrentView.onLeftSide || mCurrentView.onRightSide)) {
                        return super.onTouchEvent(event);
                    }
                }
			} else {
				return super.onTouchEvent(event);
			}
            
        } catch (Exception e) {
            // Catch pointerIndex out of range exception
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try {
            if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                super.onInterceptTouchEvent(event);
            }

            float[] difference = handleMotionEvent(event);

            if (mCurrentView!=null) {
            	if (mCurrentView.pagerCanScroll()) {
                    return super.onInterceptTouchEvent(event);
                } else {
                    if (difference != null && mCurrentView.onRightSide && difference[0] < 0) {
                        return super.onInterceptTouchEvent(event);
                    }
                    if (difference != null && mCurrentView.onLeftSide && difference[0] > 0) {
                        return super.onInterceptTouchEvent(event);
                    }
                    if (difference == null && (mCurrentView.onLeftSide || mCurrentView.onRightSide)) {
                        return super.onInterceptTouchEvent(event);
                    }
                }
			} else {
				return super.onInterceptTouchEvent(event);
			}
        } catch (Exception e) {
            // Catch pointerIndex out of range exception
        }

        return false;
    }
}