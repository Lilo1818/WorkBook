package com.mci.firstidol.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import com.mci.firstidol.base.BaseActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DeviceUtils {
	/**
	 * SD卡是否存在
	 * 
	 * @return
	 */
	public static boolean existSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * SD卡剩余空间
	 * 
	 * @param bytes
	 * @return
	 */
	public static long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/**
	 * SD卡总容量
	 * 
	 * @param bytes
	 * @return
	 */
	public static long getSDAllSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 获取所有数据块数
		long allBlocks = sf.getBlockCount();
		// 返回SD卡大小
		// return allBlocks * blockSize; //单位Byte
		// return (allBlocks * blockSize)/1024; //单位KB
		return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/**
	 * 获得CPU信息
	 * 
	 * @return
	 */
	public static String[] getCpuInfo() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "", "" }; // 1-cpu型号 //2-cpu频率
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
		}
		// Log.i(TAG, "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
		return cpuInfo;
	}

	/**
	 * 获取cpu核心数
	 * 
	 * @return
	 */
	public static int getNumCores() {
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				if (Pattern.matches("cpu[0-9]", pathname.getName())) {
					return true;
				}
				return false;
			}

		}
		try {
			File dir = new File("/sys/devices/system/cpu/");
			File[] files = dir.listFiles(new CpuFilter());
			return files.length;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}

	/**
	 * RAM 总大小
	 */
	public static long getTotalRAMSize() {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long totalSize = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
			arrayOfString = str2.split("\\s+");
			// 获得系统总内存，单位是KB，乘以1024转换为Byte
			totalSize = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return totalSize;
	}

	/**
	 * RAM 可用空间
	 */
	public static long getAvailRAMSize(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(memInfo);
		return memInfo.availMem;
	}

	/**
	 * ROM总大小
	 * 
	 * @return
	 */
	public static long getTotalROMSize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize; // Byte
	}

	/**
	 * ROM可用空间
	 * 
	 * @return
	 */
	public static long getAvailROMSize() {

		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize; // Byte

	}

	/**
	 * MAC地址
	 * 
	 * @return
	 */
	public static String getMacAddress(Context context) {
		String macStr = "";
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (wifiInfo.getMacAddress() != null) {
			macStr = wifiInfo.getMacAddress();// MAC地址
		} else {
			macStr = "null";
		}

		return macStr;
	}

	/**
	 * 设备型号
	 */
	public static String getDeviceModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 系统版本
	 */
	public static String getSysVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 取得开机时长
	 * 
	 * @return
	 */
	public static String getElapsedTime() {
		long ut = SystemClock.elapsedRealtime() / 1000;
		if (ut == 0) {
			ut = 1;
		}
		int m = (int) ((ut / 60) % 60);
		int h = (int) ((ut / 3600));
		return h + " " + "小时 " + m + " " + "分钟";
	}

	/**
	 * 格式化数据
	 * 
	 * @param size
	 *            把Byte数据转换为KB,MB,GB
	 * @return
	 */
	public static String formatSize(long size) {
		String suffix = "B";// Byte
		float fSize = 0;

		if (size >= 1024) {
			suffix = "KB";
			fSize = size / 1024;
			if (fSize >= 1024) {
				suffix = "MB";
				fSize /= 1024;
			}
			if (fSize >= 1024) {
				suffix = "GB";
				fSize /= 1024;
			}
		} else {
			fSize = size;
		}
		java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
		StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}

	/**
	 * 获取手机摄像头的分辨率
	 * 
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String getCamera() {
		String result = "";
		int max = 0;
		if (Build.VERSION.SDK_INT > 9)// 版本号大于2.3才能获取前后摄像头分辨率
		{
			String pic = "无";
			String pic1 = "无";
			System.out.println("进入大于9这里---------》");
			try {
				// 后置摄像头
				Camera camera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
				Parameters parameters = camera.getParameters();
				List<Size> supportPictureSizes = parameters
						.getSupportedPictureSizes();
				for (int i = 0; i < supportPictureSizes.size(); i++) {
					int j = supportPictureSizes.get(i).width
							* supportPictureSizes.get(i).height / 10000;
					if (j > max)
						max = j;
				}
				camera.release();
				camera = null;
				System.out.println("后置摄像头------------------------》" + max);
				pic = max + "";
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				// 前置摄像头
				max = 0;
				Camera camera1 = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
				Parameters parameters1 = camera1.getParameters();
				List<Size> supportPictureSizes1 = parameters1
						.getSupportedPictureSizes();
				for (int i = 0; i < supportPictureSizes1.size(); i++) {
					int j = supportPictureSizes1.get(i).width
							* supportPictureSizes1.get(i).height / 10000;
					if (j > max)
						max = j;
				}
				pic1 = max + "";
				camera1.release();
				camera1 = null;
				System.out.println("前置摄像头------------------------》" + pic1);
			} catch (Exception e) {
				e.printStackTrace();

			}
			result = pic + "*" + pic1;
			System.out.println("result---------------->" + result);
		} else {// 否则只能获取后置摄像头
			String pic = "无";
			try {
				Camera camera = Camera.open();
				Parameters parameters = camera.getParameters();
				List<Size> supportPictureSizes = parameters
						.getSupportedPictureSizes();
				for (int i = 0; i < supportPictureSizes.size(); i++) {
					int j = supportPictureSizes.get(i).width
							* supportPictureSizes.get(i).height / 10000;
					if (j > max)
						max = j;
				}
				pic = max + "";
			} catch (Exception e) {
			}
			result = pic;
		}
		return result;

	}

	/**
	 * 获取屏幕分辨率：宽度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getWidthPixels(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕分辨率：高度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getHeightPixels(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * 获取屏幕分辨率：密度
	 * 
	 * @param activity
	 * @return
	 */
	public static float getDensity(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.density;
	}

	/**
	 * 获取手机尺寸
	 * 
	 * @param activity
	 * @return
	 */
	public static String getScreenSize(Activity activity) {
		String result = "";
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		float density = dm.density;
		int densityDpi = dm.densityDpi;
		double diagonalPixels = Math.sqrt(Math.pow(width, 2)
				+ Math.pow(height, 2));
		double screensize = diagonalPixels / (160 * density);
		result = screensize + "";
		float f = Float.valueOf(result);
		System.out.println("float---------" + f);
		result = f + "";
		return result;
	}

	/**
	 * 获取手机安装的应用信息（排除系统自带）
	 * 
	 * @param activity
	 * @return
	 */
	public static String getAllApp(Activity activity) {
		String result = "";
		List<PackageInfo> packages = activity.getPackageManager()
				.getInstalledPackages(0);
		for (PackageInfo i : packages) {
			if ((i.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				result += i.applicationInfo.loadLabel(
						activity.getPackageManager()).toString()
						+ ",";
			}
		}
		return result.substring(0, result.length() - 1);
	}
}
