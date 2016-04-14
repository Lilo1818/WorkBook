package com.mci.firstidol.view;

import com.mci.firstidol.R;

import android.R.string;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PopAnimView extends LinearLayout {

	private Context context;
	private ImageView iv_static,iv_anim;
	private TextView tv_title;
	private int animId;
	private PopupAnimViewListener popupAnimViewListener;
	private boolean isAnim;
	
	public PopAnimView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public PopAnimView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	@SuppressLint("NewApi")
	public PopAnimView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = LayoutInflater.from(context).inflate(R.layout.layout_pop_anim_view, this); 
		
		iv_static = (ImageView) findViewById(R.id.iv_static);
		iv_anim = (ImageView) findViewById(R.id.iv_anim);
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		
		view.setClickable(true);
		isAnim = true;
		view.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (isAnim) {
						initAnimView();
					}
					
					popupAnimViewListener.popupAnimViewClick(v);
				}
				return false;
			}
		});
	}
	
	public void setAnim(boolean isAnim) {
		this.isAnim = isAnim;
	}
	public void setTitle(String title){
		tv_title.setText(title);
	}
	
	public void setStaticImage(int resId){
		iv_static.setImageResource(resId);
	}
	
	public void setAnimId(int animId) {
		this.animId = animId;
	}
	
	public void setDrawableAnimId(int dAnimId){
		iv_anim.setBackgroundResource(dAnimId);//其中R.anim.animation_list就是上一步准备的动画描述文件的资源名 
		//获得动画对象 
		AnimationDrawable animaition = (AnimationDrawable)iv_anim.getBackground();//最后，就可以启动动画了，代码如下：   
		//是否仅仅启动一次？ 
		animaition.setOneShot(false);  
		animaition.start();//启动
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		super.setOnClickListener(l);
		
		
		
	}

	private void initAnimView() {
		// TODO Auto-generated method stub
		iv_anim.clearAnimation();
		Animation animation = (AnimationSet) AnimationUtils.loadAnimation(context, animId);
		iv_anim.setAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				iv_static.setVisibility(View.INVISIBLE);
				iv_anim.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				iv_static.setVisibility(View.VISIBLE);
				iv_anim.setVisibility(View.INVISIBLE);
				
			}
		});
	}
	
	public void setPopupAnimViewListener(
			PopupAnimViewListener popupAnimViewListener) {
		this.popupAnimViewListener = popupAnimViewListener;
	}
	public interface PopupAnimViewListener{
		public void popupAnimViewClick(View v);
	}
	
}
