package com.mci.firstidol;

import java.io.File;
import java.util.Stack;

import cn.sharesdk.framework.ShareSDK;
import cn.shinsoft.ActiveAndroid;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tendcloud.tenddata.TCAgent;
import com.tendcloud.tenddata.TalkingDataSMS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import android.graphics.Bitmap;

public class BaseApp extends Application {

	private static BaseApp baseApp;
	private static Context context;
	private static Stack<Activity> activityList;
	public static DisplayImageOptions options, circleOptions,
			cornerCircleOptions;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		baseApp = this;
		context = this;

		initImageLoader(getApplicationContext());

		// ====== 百度推送 START ======
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY, "xapeIifhiYAMj0LIjntRe2iD");

		// // ====== ShareSDK START ======
		// ShareSDK.initSDK(this);
		// ShareSDK.setConnTimeout(20000);
		// ShareSDK.setReadTimeout(20000);
		// // ====== ShareSDK END ========

		ActiveAndroid.initialize(this);

		// JPushInterface.setDebugMode(false); // 设置开启日志,发布时请关闭日志
		// JPushInterface.init(this);
		// JPushInterface.setAliasAndTags(getApplicationContext(),
		// Utility.getDeviceId(this), tags);

		// 异常处理，不需要处理时注释掉这两句即可！
		// CrashHandler crashHandler = CrashHandler.getInstance();
		// 注册crashHandler
		// crashHandler.init(getApplicationContext());

		// 启动应用时，检查有异常产生，并发送
		// startCrashService();

		// PListThreadManager.init();
		TCAgent.LOG_ON=true;
		TCAgent.init(this, "3057AFA7329ECE2D6B3DD663965A61C6", "EasyProject");
	    TCAgent.setReportUncaughtExceptions(true);
	}

	public static BaseApp getInstance() {
		return baseApp;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		ActiveAndroid.dispose();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by

		// method.
		int memory = (int) Runtime.getRuntime().maxMemory();

		String CACHE_DIR = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/.temp_tmp";
		new File(CACHE_DIR).mkdirs();

		File cacheDir = StorageUtils.getOwnCacheDirectory(context, CACHE_DIR);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_img)
				.showImageForEmptyUri(R.drawable.default_img)
				.showImageOnFail(R.drawable.default_img)
				 .resetViewBeforeLoading(true)
				.cacheOnDisk(true)
				.cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new SimpleBitmapDisplayer()).build();

		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				context).defaultDisplayImageOptions(options).threadPoolSize(5)
				.diskCache(new UnlimitedDiskCache(cacheDir))
				.memoryCache(new UsingFreqLimitedMemoryCache(1024 * 1024 * 20));

		ImageLoaderConfiguration config = builder.build();
		ImageLoader.getInstance().init(config);

		circleOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_circle_img)
				.showImageForEmptyUri(R.drawable.default_circle_img)
				.showImageOnFail(R.drawable.default_circle_img)
				 .resetViewBeforeLoading(true)
				.cacheOnDisk(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				// EXACTLY:恰好\精确地 EXACTLY_STRETCHED:全部拉伸 default
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(720)) // default:SimpleBitmapDisplayer()
				.build();

		cornerCircleOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_corncircle_img)
				.showImageForEmptyUri(R.drawable.default_corncircle_img)
				.showImageOnFail(R.drawable.default_corncircle_img)
				 .resetViewBeforeLoading(true)
				.cacheOnDisk(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				// EXACTLY:恰好\精确地 EXACTLY_STRETCHED:全部拉伸 default
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(30)) // default:SimpleBitmapDisplayer()
				.build();

	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityList.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			if (activityList != null) {
				activityList.remove(activity);
			}
			activity.finish();
			activity = null;
		}
	}

	// 添加Activity到容器中
	public static void addActivity(Activity activity) {

		if (activityList == null) {
			activityList = new Stack<Activity>();
		}

		activityList.add(activity);
	}

	// 遍历所有Activity并finish
	public static void exit() {
		try {
			if (activityList != null && activityList.size() > 0) {
				for (Activity activity : activityList) {
					activity.finish();
				}
			}
			System.exit(0);
		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	private void startCrashService() {

	}

}
