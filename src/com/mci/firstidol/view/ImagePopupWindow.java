package com.mci.firstidol.view;


import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

import com.mci.firstidol.R;
import com.mci.firstidol.utils.LogUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class ImagePopupWindow extends PopupWindow implements OnViewTapListener {

	private Context context;
	private ImageView iv_show;

	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private PhotoViewAttacher mAttacher;

	public ImagePopupWindow() {
		super();
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	public ImagePopupWindow(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public ImagePopupWindow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public ImagePopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public ImagePopupWindow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		initOption();
		init();
	}

	public ImagePopupWindow(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	public ImagePopupWindow(View contentView, int width, int height,
			boolean focusable) {
		super(contentView, width, height, focusable);
		// TODO Auto-generated constructor stub
	}

	public ImagePopupWindow(View contentView, int width, int height) {
		super(contentView, width, height);
		// TODO Auto-generated constructor stub
	}

	public ImagePopupWindow(View contentView) {
		super(contentView);
		// TODO Auto-generated constructor stub
	}

	private void initOption() {
		// TODO Auto-generated method stub
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_img) // resource or
															// drawable
				.showImageForEmptyUri(R.drawable.default_img) // resource or
																// drawable
				.showImageOnFail(R.drawable.default_img) // resource or drawable
				.resetViewBeforeLoading(false) // default:false
				.delayBeforeLoading(0).cacheInMemory(true) // default:false
				.cacheOnDisc(true) // default:false
				// .preProcessor(null)
				// .postProcessor(null)
				// .extraForDownloader(null)
				.considerExifParams(true) // default
				// .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) //
				// default
				// .bitmapConfig(Config.ARGB_8888) // default
				// .decodingOptions(null)
				// .displayer(new RoundedBitmapDisplayer(720)) //
				// default:SimpleBitmapDisplayer()
				// .handler(new Handler()) // default
				.build();
	}

	public void init() {
		// 获取屏幕的高度和宽度

		setAnimationStyle(R.style.AnimationImagePreview);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View contentView = inflater.inflate(R.layout.layout_image_show,
				null);

		iv_show = (ImageView) contentView.findViewById(R.id.iv_image_show);
		// The MAGIC happens here!
		mAttacher = new PhotoViewAttacher(iv_show);
		mAttacher.setOnViewTapListener(this);
		setContentView(contentView);

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		contentView.measure(w, h);
		int height = contentView.getMeasuredHeight();
		int width = contentView.getMeasuredWidth();

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);

		setWidth(LinearLayout.LayoutParams.FILL_PARENT);
		// window.setHeight(height/2);
		// setHeight(height);
		setHeight(LinearLayout.LayoutParams.FILL_PARENT);

		// setOnDismissListener(new OnDismissListener() {
		//
		// @Override
		// public void onDismiss() {
		// // TODO Auto-generated method stub
		// AnimationSet animationSet = new AnimationSet(true);
		// //参数1～2：x轴的开始位置
		// //参数3～4：y轴的开始位置
		// //参数5～6：x轴的结束位置
		// //参数7～8：y轴的结束位置
		// TranslateAnimation translateAnimation =
		// new TranslateAnimation(
		// Animation.RELATIVE_TO_SELF,0f,
		// Animation.RELATIVE_TO_SELF,0f,
		// Animation.RELATIVE_TO_SELF,0.0f,
		// Animation.RELATIVE_TO_SELF,1.0f);
		// translateAnimation.setDuration(500);
		// animationSet.addAnimation(translateAnimation);
		// animationSet.setFillAfter(true);
		// // test_pop_layout.startAnimation(animationSet);
		// }
		// });

		// contentView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		// contentView.setOnTouchListener(new OnTouchListener() {
		//
		// public boolean onTouch(View v, MotionEvent event) {
		//
		// int height = contentView.findViewById(R.id.pop_layout).getTop();
		// int y=(int) event.getY();
		// if(event.getAction()==MotionEvent.ACTION_UP){
		// if(y<height){
		// dismiss();
		// }
		// }
		// return true;
		// }
		// });

		// 设置PopupWindow外部区域是否可触摸
		setFocusable(true); // 设置PopupWindow可获得焦点
		setTouchable(true); // 设置PopupWindow可触摸
		setOutsideTouchable(false); // 设置非PopupWindow区域可触摸
	}

	public void setImageURL(String imgURL) {
		imageLoader.displayImage(imgURL, iv_show, options,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub
						mAttacher.update();
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub

					}
				});
	}

	@Override
	public void onViewTap(View view, float x, float y) {
		// TODO Auto-generated method stub
		dismiss();
	}

}
