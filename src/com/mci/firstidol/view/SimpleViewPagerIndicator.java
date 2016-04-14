package com.mci.firstidol.view;

import com.mci.firstidol.R;
import com.mci.firstidol.utils.DisplayUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SimpleViewPagerIndicator extends LinearLayout
{

	private static final int COLOR_TEXT_NORMAL = 0xFF000000;
	private static final int COLOR_INDICATOR_COLOR = Color.parseColor("#AAD54A77");

	private String[] mTitles;
	private int mTabCount;
	private int mIndicatorColor = COLOR_INDICATOR_COLOR;
	private float mTranslationX;
	private Paint mPaint = new Paint();
	private int mTabWidth;
	
	private int margin = 20;
	private SimpleViewPagerListener simpleViewPagerListener;

	public SimpleViewPagerIndicator(Context context)
	{
		this(context, null);
	}

	public SimpleViewPagerIndicator(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mPaint.setColor(mIndicatorColor);
		mPaint.setStrokeWidth(9.0F);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		mTabWidth = (int) ((w-DisplayUtil.dip2px(getContext(), margin*2)) / (mTabCount==0?1:mTabCount));
	}

	public void setTitles(String[] titles)
	{
		mTitles = titles;
		mTabCount = titles.length;
		generateTitleView();

	}

	public void setIndicatorColor(int indicatorColor)
	{
		this.mIndicatorColor = indicatorColor;
	}

	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		super.dispatchDraw(canvas);
		canvas.save();
		canvas.translate(mTranslationX, getHeight() -DisplayUtil.dip2px(getContext(), 7));
		canvas.drawLine(DisplayUtil.dip2px(getContext(), margin*2 + 10), -DisplayUtil.dip2px(getContext(), 4.8f), mTabWidth + DisplayUtil.dip2px(getContext(), 17), -DisplayUtil.dip2px(getContext(), 4.8f), mPaint);
		canvas.restore();
	}

	public void scroll(int position, float offset)
	{
		/**
		 * <pre>
		 *  0-1:position=0 ;1-0:postion=0;
		 * </pre>
		 */
		mTranslationX = (getWidth() - DisplayUtil.dip2px(getContext(), margin*2 + 56)) / mTabCount * (position + offset);
		invalidate();
	}


	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		return super.dispatchTouchEvent(ev);
	}

	private void generateTitleView()
	{
		if (getChildCount() > 0)
			this.removeAllViews();
		int count = mTitles.length;

//		setWeightSum(count);
		int position = 0;
		for (int i = 0; i < count; i++)
		{
			
		    ImageView btnButton =  new ImageView(getContext());
		    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT);
			lp.weight = 1;
			btnButton.setLayoutParams(lp);
			btnButton.setTag(i);
//			btnButton.setBackgroundDrawable(null);
			if (i== 0) {
				btnButton.setImageResource(R.drawable.square_live_detail_live);
			} else {
				btnButton.setImageResource(R.drawable.square_live_detail_chat);
			}
			btnButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
//					simpleViewPagerListener.onItemClick(v);
				}
			});
			
			
			
		}
		View view =  LayoutInflater.from(getContext()).inflate(R.layout.layout_square_live_detail_segment, null);
		ImageButton ib_live = (ImageButton) view.findViewById(R.id.ib_live);
		ImageButton ib_chat = (ImageButton) view.findViewById(R.id.ib_chat);
		
		ib_live.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simpleViewPagerListener.onItemClick(0);
			}
		});
		
		ib_chat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simpleViewPagerListener.onItemClick(1);
			}
		});
		
		addView(view);
	}
	
	public void setSimpleViewPagerListener(
			SimpleViewPagerListener simpleViewPagerListener) {
		this.simpleViewPagerListener = simpleViewPagerListener;
	}
	public interface SimpleViewPagerListener{
		public void onItemClick(int position);
	}

}
