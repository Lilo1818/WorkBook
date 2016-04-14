package com.mci.firstidol.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import java.io.IOException;

public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {
	private Camera.AutoFocusCallback autoFocusCallback;
	private Camera mCamera;
	private SurfaceHolder mHolder;
	private Camera.PreviewCallback previewCallback;
	private Context context;

	public CameraPreview(Context paramContext, Camera paramCamera,
			Camera.PreviewCallback paramPreviewCallback,
			Camera.AutoFocusCallback paramAutoFocusCallback) {
		super(paramContext);
		this.context = paramContext;
		this.mCamera = paramCamera;
		this.previewCallback = paramPreviewCallback;
		this.autoFocusCallback = paramAutoFocusCallback;
		this.mHolder = getHolder();
		this.mHolder.addCallback(this);
		this.mHolder.setFixedSize(300, 300);
		this.mHolder.setType(3);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Canvas canvas1 = mHolder.lockCanvas(null);//获取画布
		// //中间的扫描框，你要修改扫描框的大小，去CameraManager里面修改
		// Rect frame =new Rect(200, 200, 400, 400);
		// if (frame == null) {
		// return;
		// }
		//
		// //初始化中间线滑动的最上边和最下边
		//
		//
		// //获取屏幕的宽和高
		// int width = canvas1.getWidth();
		// int height = canvas1.getHeight();
		// Paint paint = new Paint();
		// paint.setColor(Color.BLUE);
		//
		// //画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
		// //扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
		// // 头部
		// canvas1.drawRect(0, 0, width, frame.top, paint);
		// // 左边
		// canvas1.drawRect(0, frame.top, frame.left, frame.bottom, paint);
		// // 右边
		// canvas1.drawRect(frame.right, frame.top, width, frame.bottom, paint);
		// // 底部
		// canvas1.drawRect(0, frame.bottom, width, height, paint);
		// Bitmap resultBitmap = null;
		// if (resultBitmap != null) {
		// // Draw the opaque result bitmap over the scanning rectangle
		// // paint.setAlpha(OPAQUE);
		// canvas1.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		// } else {
		// // 画出四个角
		// paint.setColor(Color.GREEN);
		// // 左上角
		// canvas1.drawRect(frame.left, frame.top, frame.left + 20,
		// frame.top + 5, paint);
		// canvas1.drawRect(frame.left, frame.top, frame.left + 5,
		// frame.top + 20, paint);
		// // 右上角
		// canvas1.drawRect(frame.right - 20, frame.top, frame.right,
		// frame.top + 5, paint);
		// canvas1.drawRect(frame.right - 5, frame.top, frame.right,
		// frame.top + 20, paint);
		// // 左下角
		// canvas1.drawRect(frame.left, frame.bottom - 5, frame.left + 20,
		// frame.bottom, paint);
		// canvas1.drawRect(frame.left, frame.bottom - 20, frame.left + 5,
		// frame.bottom, paint);
		// // 右下角
		// canvas1.drawRect(frame.right - 20, frame.bottom - 5, frame.right,
		// frame.bottom, paint);
		// canvas1.drawRect(frame.right - 5, frame.bottom - 20, frame.right,
		// frame.bottom, paint);
		// // int linewidht = 3; // 控制线的粗细
		// // // 左上全部和右上角的一半
		// // canvas.drawRect(5 + frame.left, 5 + frame.top,
		// // 5 + (linewidht + frame.left), 5 + (50 + frame.top), paint);
		// // canvas.drawRect(5 + frame.left, 5 + frame.top,
		// // 5 + (50 + frame.left), 5 + (linewidht + frame.top), paint);
		// // canvas.drawRect(-5 + ((0 - linewidht) + frame.right),
		// // 5 + frame.top, -5 + (1 + frame.right),
		// // 5 + (50 + frame.top), paint);
		// //
		// // // 右上角的一半和左下角的一半
		// // canvas.drawRect(-5 + (-50 + frame.right), 5 + frame.top, -5
		// // + frame.right, 5 + (linewidht + frame.top), paint);
		// // canvas.drawRect(5 + frame.left, -5 + (-49 + frame.bottom),
		// // 5 + (linewidht + frame.left), -5 + (1 + frame.bottom),
		// // paint);
		// // // 右下角的全部歌左下角的一半
		// // canvas.drawRect(5 + frame.left, -5
		// // + ((0 - linewidht) + frame.bottom), 5 + (50 + frame.left),
		// // -5 + (1 + frame.bottom), paint);
		// // canvas.drawRect(-5 + ((0 - linewidht) + frame.right), -5
		// // + (-49 + frame.bottom), -5 + (1 + frame.right), -5
		// // + (1 + frame.bottom), paint);
		// // canvas.drawRect(-5 + (-50 + frame.right), -5
		// // + ((0 - linewidht) + frame.bottom), -5 + frame.right, -5
		// // + (linewidht - (linewidht - 1) + frame.bottom), paint);
		//
		// // 在扫描框中画出模拟扫描的线条
		// // 设置扫描线条颜色为绿色
		// paint.setColor(Color.GREEN);
		// // 设置绿色线条的透明值
		// paint.setAlpha((int) 0.5);
		// // 透明度变化
		// // scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
		//
		// // 将扫描线修改为上下走的线
		// // if ((i += 5) < frame.bottom - frame.top) {
		// // /* 以下为图片作为扫描线 */
		// // mRect.set(frame.left - 6, frame.top + i - 6, frame.right + 6,
		// // frame.top + 6 + i);
		// // lineDrawable.setBounds(mRect);
		// // lineDrawable.draw(canvas);
		// //
		// // // 刷新
		// // invalidate();
		// // } else {
		// // i = 0;
		// // }
		//
		// //画扫描框下面的字
		// paint.setColor(Color.WHITE);
		// paint.setTextSize(12);
		// paint.setAlpha(0x40);
		// paint.setTypeface(Typeface.create("System", Typeface.BOLD));
		// canvas1.drawText("4234234", (float)120, (float)120, paint);}

	}

	public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1,
			int paramInt2, int paramInt3) {
		if (this.mHolder.getSurface() == null)
			return;
		try {
			this.mCamera.stopPreview();
			try {
				this.mCamera.setDisplayOrientation(90);
				this.mCamera.setPreviewDisplay(this.mHolder);
				this.mCamera.setPreviewCallback(this.previewCallback);
				this.mCamera.startPreview();
				this.mCamera.autoFocus(this.autoFocusCallback);
				return;
			} catch (Exception localException2) {
				Log.d("DBG", "Error starting camera preview: "
						+ localException2.getMessage());
				return;
			}
		} catch (Exception localException1) {
		}
	}

	public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
		try {
			// new Thread(new MyThread()).start();
			this.mCamera.setPreviewDisplay(paramSurfaceHolder);

		} catch (IOException localIOException) {
			Log.d("DBG",
					"Error setting camera preview: "
							+ localIOException.getMessage());
		}
	}

	public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
	}

	// 内部类的内部类
	class MyThread implements Runnable {

		@Override
		public void run() {
			Canvas canvas = mHolder.lockCanvas(null);// 获取画布
			// 中间的扫描框，你要修改扫描框的大小，去CameraManager里面修改
			Rect frame = new Rect(100, 100, 200, 200);

			// 初始化中间线滑动的最上边和最下边

			// 获取屏幕的宽和高
			int width = 400;
			int height = 880;
			Paint paint = new Paint();
			paint.setColor(Color.BLUE);

			// 画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
			// 扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
			// 头部
			canvas.drawRect(0, 0, width, frame.top, paint);
			// 左边
			canvas.drawRect(0, frame.top, frame.left, frame.bottom, paint);
			// 右边
			canvas.drawRect(frame.right, frame.top, width, frame.bottom, paint);
			// 底部
			canvas.drawRect(0, frame.bottom, width, height, paint);
			Bitmap resultBitmap = null;

			// 画出四个角
			paint.setColor(Color.GREEN);
			// 左上角
			canvas.drawRect(frame.left, frame.top, frame.left + 20,
					frame.top + 5, paint);
			canvas.drawRect(frame.left, frame.top, frame.left + 5,
					frame.top + 20, paint);
			// 右上角
			canvas.drawRect(frame.right - 20, frame.top, frame.right,
					frame.top + 5, paint);
			canvas.drawRect(frame.right - 5, frame.top, frame.right,
					frame.top + 20, paint);
			// 左下角
			canvas.drawRect(frame.left, frame.bottom - 5, frame.left + 20,
					frame.bottom, paint);
			canvas.drawRect(frame.left, frame.bottom - 20, frame.left + 5,
					frame.bottom, paint);
			// 右下角
			canvas.drawRect(frame.right - 20, frame.bottom - 5, frame.right,
					frame.bottom, paint);
			canvas.drawRect(frame.right - 5, frame.bottom - 20, frame.right,
					frame.bottom, paint);
			// int linewidht = 3; // 控制线的粗细
			// // 左上全部和右上角的一半
			// canvas.drawRect(5 + frame.left, 5 + frame.top,
			// 5 + (linewidht + frame.left), 5 + (50 + frame.top), paint);
			// canvas.drawRect(5 + frame.left, 5 + frame.top,
			// 5 + (50 + frame.left), 5 + (linewidht + frame.top), paint);
			// canvas.drawRect(-5 + ((0 - linewidht) + frame.right),
			// 5 + frame.top, -5 + (1 + frame.right),
			// 5 + (50 + frame.top), paint);
			//
			// // 右上角的一半和左下角的一半
			// canvas.drawRect(-5 + (-50 + frame.right), 5 + frame.top, -5
			// + frame.right, 5 + (linewidht + frame.top), paint);
			// canvas.drawRect(5 + frame.left, -5 + (-49 + frame.bottom),
			// 5 + (linewidht + frame.left), -5 + (1 + frame.bottom),
			// paint);
			// // 右下角的全部歌左下角的一半
			// canvas.drawRect(5 + frame.left, -5
			// + ((0 - linewidht) + frame.bottom), 5 + (50 + frame.left),
			// -5 + (1 + frame.bottom), paint);
			// canvas.drawRect(-5 + ((0 - linewidht) + frame.right), -5
			// + (-49 + frame.bottom), -5 + (1 + frame.right), -5
			// + (1 + frame.bottom), paint);
			// canvas.drawRect(-5 + (-50 + frame.right), -5
			// + ((0 - linewidht) + frame.bottom), -5 + frame.right, -5
			// + (linewidht - (linewidht - 1) + frame.bottom), paint);

			// 在扫描框中画出模拟扫描的线条
			// 设置扫描线条颜色为绿色
			paint.setColor(Color.GREEN);
			// 设置绿色线条的透明值
			paint.setAlpha((int) 0.5);
			// 透明度变化
			// scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;

			// 将扫描线修改为上下走的线
			// if ((i += 5) < frame.bottom - frame.top) {
			// /* 以下为图片作为扫描线 */
			// mRect.set(frame.left - 6, frame.top + i - 6, frame.right + 6,
			// frame.top + 6 + i);
			// lineDrawable.setBounds(mRect);
			// lineDrawable.draw(canvas);
			//
			// // 刷新
			// invalidate();
			// } else {
			// i = 0;
			// }

			// 画扫描框下面的字
			paint.setColor(Color.WHITE);
			paint.setTextSize(12);
			paint.setAlpha(0x40);
			paint.setTypeface(Typeface.create("System", Typeface.BOLD));
			canvas.drawText("4234234", (float) 120, (float) 120, paint);

			mHolder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像

		}

	}
}