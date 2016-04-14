
package com.mci.firstidol.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.mci.firstidol.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommonUtils {
    // 服务器端返回的是北京时间，需要先转换为格林威治时间
    public static final long TIME_DIFFERENCE = 8 * 60 * 60 * 1000;

    public static int getResourceValueByName(Context context, String resStr, String resType) {
        Resources localResources = context.getResources();
        String pkgName = context.getPackageName();
        return localResources.getIdentifier(resStr, resType, pkgName);
    }

    /**
     * 通过对象返回ContentValues
     * 
     * @param clazz
     * @return
     */
    public static ContentValues getContentValues(Object o) {
        ContentValues cv = new ContentValues();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field f : fields) {
            String name = f.getName();
            Class<?> type = f.getType();
            try {
                if (name.equals("serialVersionUID")) {

                } else if (type == String.class) {
                    String value = (String) f.get(o);
                    cv.put(name, value);
                } else if (type == Integer.TYPE) {
                    int value = f.getInt(o);
                    cv.put(name, value);
                } else if (type == Long.TYPE) {
                    long value = f.getLong(o);
                    cv.put(name, value);
                } else if (type == Float.TYPE) {
                    float value = f.getFloat(o);
                    cv.put(name, value);
                } else if (type == Double.TYPE) {
                    double value = f.getDouble(o);
                    cv.put(name, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cv;
    }

    /**
     * 通过类对象返回该类实例
     * 
     * @param cursor
     * @param clasz
     * @return
     */
    public static <T> Object getClassObject(Cursor cursor, Class<T> clasz) {
        if (clasz == null)
            return null;
        Object tempObject = null;
        try {
            tempObject = clasz.newInstance();
        } catch (InstantiationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (tempObject == null)
            return null;
        Field[] fields = clasz.getDeclaredFields();
        for (Field f : fields) {
            String name = f.getName();
            Class type = f.getType();
            try {
                if (type == String.class) {
                    if (cursor.getColumnIndex(name) >= 0) {
                        String value = cursor.getString(cursor.getColumnIndex(name));
                        f.set(tempObject, value);
                    }
                } else if (type == Integer.TYPE) {
                    if (cursor.getColumnIndex(name) >= 0) {
                        int value = cursor.getInt(cursor.getColumnIndex(name));
                        f.setInt(tempObject, value);
                    }
                } else if (type == Long.TYPE) {
                    if (cursor.getColumnIndex(name) >= 0) {
                        long value = cursor.getLong(cursor.getColumnIndex(name));
                        f.setLong(tempObject, value);
                    }
                } else if (type == Float.TYPE) {
                    if (cursor.getColumnIndex(name) >= 0) {
                        float value = cursor.getFloat(cursor.getColumnIndex(name));
                        f.setFloat(tempObject, value);
                    }
                } else if (type == Double.TYPE) {
                    if (cursor.getColumnIndex(name) >= 0) {
                        double value = cursor.getDouble(cursor.getColumnIndex(name));
                        f.setDouble(tempObject, value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tempObject;
    }

    public static long changeServerStringToLong(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }

        boolean isContainPlus = str.contains("+");
        String tempStr = null;
        if (!isContainPlus) {
            tempStr = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
        } else {
            tempStr = str.substring(str.indexOf("(") + 1, str.indexOf("+"));
        }

        // 后台返回北京时间，减去时差转换为GMT标准时间
        return Long.parseLong(tempStr);
    }

    public static long changeServerStringToGMTLong(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }

        boolean isContainPlus = str.contains("+");
        String tempStr = null;
        if (!isContainPlus) {
            tempStr = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
        } else {
            tempStr = str.substring(str.indexOf("(") + 1, str.indexOf("+"));
        }

        // 后台返回北京时间，减去时差转换为GMT标准时间
        return Long.parseLong(tempStr) - TIME_DIFFERENCE;
    }

    public static String changeServerStringToDate(String str, String formatType) {
        if (str == null || str.isEmpty()) {
            return null;
        }

        boolean isContainPlus = str.contains("+");
        String tempStr = null;
        if (!isContainPlus) {
            tempStr = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
        } else {
            tempStr = str.substring(str.indexOf("(") + 1, str.indexOf("+"));
        }
        if ("".equals(tempStr)) {
            return null;
        }

        return new SimpleDateFormat(formatType).format(new Date(Long.valueOf(tempStr))).toString();
    }

    public static String changeServerStringToGMTDate(String str, String formatType) {
        if (str == null || str.isEmpty()) {
            return null;
        }

        boolean isContainPlus = str.contains("+");
        String tempStr = null;
        if (!isContainPlus) {
            tempStr = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
        } else {
            tempStr = str.substring(str.indexOf("(") + 1, str.indexOf("+"));
        }
        if ("".equals(tempStr)) {
            return null;
        }

        return new SimpleDateFormat(formatType).format(new Date(Long.valueOf(tempStr) - TIME_DIFFERENCE)).toString();
    }

    public static int compareTime(String timeA, String timeB) {
        long a = changeServerStringToLong(timeA);
        long b = changeServerStringToLong(timeB);

        return (a > b) ? 1 : ((a < b) ? -1 : 0);
    }

    public static long getRestTime(String str) {
        if (str == null || str.isEmpty()) {
            return 0;
        }

        boolean isContainPlus = str.contains("+");
        String tempStr = null;
        if (!isContainPlus) {
            tempStr = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
        } else {
            tempStr = str.substring(str.indexOf("(") + 1, str.indexOf("+"));
        }

        if ("".equals(tempStr)) {
            return 0;
        }

        long restTime = (Long.parseLong(tempStr) - System.currentTimeMillis() + (1000 * 60 * 60 * 24 - 1))
                / (1000 * 60 * 60 * 24);
        restTime = restTime > 0 ? restTime : 0;
        return restTime;
    }

    public static String getRelativeDate(Context context, Date date) {
        if (context == null) {
            return "";
        }
        String relativeDate = "";

        if (date != null) {
            long timeSpan = System.currentTimeMillis() - date.getTime();
            if (timeSpan < 0 || timeSpan >= DateUtils.WEEK_IN_MILLIS) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
                relativeDate = dateFormat.format(date);
            } else if (timeSpan < DateUtils.HOUR_IN_MILLIS) {
                timeSpan = timeSpan / DateUtils.MINUTE_IN_MILLIS;
                relativeDate = timeSpan + context.getString(R.string.minutes_ago);
            } else if (timeSpan < DateUtils.DAY_IN_MILLIS) {
                timeSpan = timeSpan / DateUtils.HOUR_IN_MILLIS;
                relativeDate = timeSpan + context.getString(R.string.hours_ago);
            } else if (timeSpan < DateUtils.WEEK_IN_MILLIS) {
                timeSpan = timeSpan / DateUtils.DAY_IN_MILLIS;
                relativeDate = timeSpan + context.getString(R.string.days_ago);
            }
        }

        return relativeDate;
    }

    public static String getRelativeDate(Context context, long date) {
        Date sartDate = new Date(date);
        return getRelativeDate(context, sartDate);
    }

    /**
     * 是否离开半个小时
     * 
     * @param context
     * @return
     */
    public static boolean isLeaveStoreHalfAnHour(Context context) {
        long current = System.currentTimeMillis();
//        if ((current - Configs.getLeaveStoreTime(context)) > 1800 * 1000) {// 超过半个小时
//            return true;
//        }
        return false;
    }

    public static String sign(String params) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            return convert(messageDigest.digest(params.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private static String convert(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String temp = Integer.toHexString(bytes[i] & 0xff);
            sb.append(temp.length() == 1 ? "0" + temp : temp);
        }

        return sb.toString();
    }

    /**
     * @Title: convertDownloadPageStr
     * @Description: 转化后 17/25
     * @param @param currrentPage
     * @param @param countPage
     * @param @return
     * @return String
     * @throws
     */
    public static String convertDownloadPageStr(int currrentPage, int countPage) {
        StringBuilder processStr = new StringBuilder();
        processStr.append(currrentPage).append("/").append(countPage);
        return processStr.toString();
    }

    /**
     * @Title: getPageProcess
     * @Description: 获取百分比进度
     * @param @param currrentPage
     * @param @param countPage
     * @param @return
     * @return int
     * @throws
     */
    public static int getProcess(int currrent, int count) {
        return (int) ((double) currrent / (double) count * 100);
    }

    /**
     * 转化 后如 101.23M/103.20M
     */
    public static String convertDownloadStr(long currrent, long count) {
        double currentrD = (double) currrent / 1024 / 1024;
        double countD = (double) count / 1024 / 1024;
        String s = String.format("%.2f", currentrD) + "M/" + String.format("%.2f", countD) + "M";
        return s;
    }

    public static String convertLongToMBStr(long size) {
        double currentrD = (double) size / 1024 / 1024;
        String s = String.format("%.2f", currentrD);
        return s;
    }

    public static String formatUrl(String url) {
        if (url == null || url.length() <= 0)
            return "";
        String urlString = url.replace("http:\\", "http://");
        return urlString.replace("\\", "/");
    }

    /**
     * 获取apk版本号
     * 
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String version = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            version = packInfo.versionName;
        } catch (Exception e) {
            version = "";
        }

        return version;
    }

    /**
     * 获取设备编号
     */
    public static String getUDID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice = tm.getDeviceId();

        if (tmDevice == null || tmDevice.isEmpty()) {
            tmDevice = "-";
        }

        return tmDevice;
    }

    /**
     * 是否在前台运行
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
            return true;
        }

        return false;
    }

    /**
     * 是否在运行
     */
    public static boolean isRunningTask(Context context) {
        // String MY_PKG_NAME ="com.mci.smagazine";

        String MY_PKG_NAME = context.getPackageName();

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<RunningTaskInfo> list = am.getRunningTasks(100);

        for (RunningTaskInfo info : list) {

            if (info.topActivity.getPackageName().equals(MY_PKG_NAME) && info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isValidTablet(String model) {
        boolean isValidTablet = false;

        // for (int i = 0; i < Common.TABLET_MODEL.length; i++) {
        // if (Common.TABLET_MODEL[i] != null
        // && model.contains(Common.TABLET_MODEL[i])) {
        // return true;
        // }
        // }

        return isValidTablet;
    }

    public static boolean isValidPhone(String model) {
        boolean isValidTablet = false;

        // for (int i = 0; i < Common.PHONE_MODEL.length; i++) {
        // if (Common.PHONE_MODEL[i] != null
        // && model.contains(Common.PHONE_MODEL[i])) {
        // return true;
        // }
        // }

        return isValidTablet;
    }

    /**
     * 获取设备类型
     */
//    public static int getDeviceType() {
//        if (isValidTablet(Build.MODEL)) {
//            return Common.DEVICE_TYPE_TABLET;
//        } else {
//            return Common.DEVICE_TYPE_PHONE;
//        }
//    }

    public static String getValidImageUrl(Context context, String path) {
        String url = path;

        if (context == null || path == null || path.length() <= 0) {
            return url;
        }

        if (url.indexOf("drawable://") != -1) {
            return url;
        }

//        if (!url.contains(Common.HTTP_SCHEMA)) {
//            StringBuilder stringBuilder = new StringBuilder(Common.getImageServer(context));
//
//            if (!path.substring(0, 1).equals("/")) {
//                stringBuilder.append("/");
//            }
//
//            url = stringBuilder.append(url).toString();
//        }

        return url;
    }

    public static String getValidItemUrl(Context context, String path) {
        String url = path;

        if (context == null || path == null || path.length() <= 0) {
            return url;
        }

//        if (!url.contains(Common.HTTP_SCHEMA)) {
//            StringBuilder stringBuilder = new StringBuilder(Common.getItemsServer(context));
//
//            if (!path.substring(0, 1).equals("/")) {
//                stringBuilder.append("/");
//            }
//
//            url = stringBuilder.append(path).toString();
//        }

        return url;
    }


    public static String spliceResourceUrl(Context context, String url) {
        url = spliceResourceUrl("src=\"", context, url);
        url = spliceResourceUrl("src=\\\"", context, url);
        return url;
    }

    public static String spliceResourceUrl(String srcFlag, Context context, String url) {
        StringBuilder strBuilder = new StringBuilder(url);

        int startIndex = strBuilder.indexOf(srcFlag) + srcFlag.length();
        while (startIndex != (-1 + srcFlag.length())) {
            String tempString = strBuilder.substring(startIndex);
            int endPos = tempString.indexOf("\"");
            String resourceUrl = strBuilder.substring(startIndex, startIndex + endPos);
            if (!resourceUrl.contains("http")) {
                strBuilder.replace(startIndex, startIndex + endPos, CommonUtils.getValidImageUrl(context, resourceUrl));
            }
            startIndex = strBuilder.indexOf(srcFlag, startIndex) + srcFlag.length();
        }

        return strBuilder.toString();
    }

    public static String getAuthorityFromPermission(Context context, String permission) {
        if (permission == null) {
            return null;
        }

        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);

        if (packages != null) {
            for (PackageInfo info : packages) {
                ProviderInfo[] providers = info.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (permission.equals(provider.readPermission)) {
                            return provider.authority;
                        }

                        if (permission.equals(provider.writePermission)) {
                            return provider.authority;
                        }
                    }
                }
            }
        }

        return null;
    }

    private static final String PERMISSION_READ_SETTINGS = "com.android.launcher.permission.READ_SETTINGS";

    public static boolean hasShortcut(Context context, String sharCutName) {
        boolean isInstallShortcut = false;
        final ContentResolver cr = context.getContentResolver();
        final String AUTHORITY = CommonUtils.getAuthorityFromPermission(context, PERMISSION_READ_SETTINGS);
        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites");
        Cursor c = cr.query(CONTENT_URI, new String[] { "title", "icon" }, "title=?", new String[] { sharCutName }, null);
        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }

        if (c != null) {
            c.close();
        }

        return isInstallShortcut;
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return statusBarHeight;
    }

    public static float getActionBarHeight(Context context) {
        TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });

        float height = actionbarSizeTypedArray.getDimension(0, 0);
        actionbarSizeTypedArray.recycle();
        return height;
    }

    public static String getFileNameByUrl(String url) {
        String decodeStr = null;
        if (url == null)
            return decodeStr;
        decodeStr = url.substring(url.lastIndexOf('/') + 1);
        return decodeStr;
    }


    public static boolean copyCoverFile(String src, String des) {
        boolean isok = true;
        try {
            int byteread = 0;
            File srcFile = new File(src);
            if (srcFile.exists()) { // 文件存在时
                InputStream is = new FileInputStream(src); // 读入原文件
                FileOutputStream fs = new FileOutputStream(des);
                byte[] buffer = new byte[1024];
                while ((byteread = is.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                fs.flush();
                fs.close();
                is.close();
            } else {
                isok = false;
            }
        } catch (Exception e) {
            // System.out.println("复制单个文件操作出错");
            // e.printStackTrace();
            isok = false;
        }
        return isok;

    }

    /**
     * 获取位置信息，为经度和纬度 的二位数组
     * 
     * @param context
     * @return
     */
    public static double[] getLocationInfo(Context context) {
        double[] locations = new double[2];
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);// 高精度
        criteria.setAltitudeRequired(false);// 不要求海拔
        criteria.setBearingRequired(false);// 不要求方位
        criteria.setCostAllowed(true);// 允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW);// 低功耗
        // 从可用的位置提供器中，匹配以上标准的最佳提供器
        String provider = locationManager.getBestProvider(criteria, true);
        if (!TextUtils.isEmpty(provider)) {
            // 获得最后一次变化的位置
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                locations[0] = location.getLatitude();
                locations[1] = location.getLongitude();
            }
        }
        return locations;
    }

    public static int getVersionOfSamsungAccount(Context context) {
        int version = 0;

        try {
            final PackageManager pm = context.getPackageManager();
            final PackageInfo pi = pm.getPackageInfo("com.osp.app.signin", PackageManager.GET_META_DATA);
            version = pi.versionCode;
        } catch (final Exception e) {
            // Samsung Account Package not installed
        }

        return version;
    }


    public static boolean isServiceRunning(Context context, String serviceClassName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().contains(serviceClassName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取设备尺寸
     * http://blog.csdn.net/moruite/article/details/7281428
     */
    public static double deviceSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        double screenInches = Math.sqrt(Math.pow(dm.widthPixels / dm.xdpi, 2) + Math.pow(dm.heightPixels / dm.ydpi, 2));
        return screenInches;
    }

    public static boolean fileCopy(String src, String des) {
        File srcFile = new File(src);
        File desFile = new File(des);
        byte[] b = new byte[1024];
        try {
            FileInputStream fis = new FileInputStream(srcFile);
            FileOutputStream fos = new FileOutputStream(desFile, false);
            while (true) {
                int i = fis.read(b);
                if (i == -1)
                    break;
                fos.write(b, 0, i);
            }
            fos.close();
            fis.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * 文件夹copy param src,des return ture 成功。false 失败
     */

    public static boolean folderCopy(String src, String des) {
        File srcFile = new File(src);
        File desFile = new File(des);
        File[] files = srcFile.listFiles();
        boolean flag = false;
        if (!desFile.exists())
            desFile.mkdir();
        for (int i = 0; i < files.length; i++) {
            String path = files[i].getAbsolutePath();
            if (files[i].isDirectory()) {
                File newFile = new File(path.replace(src, des));
                if (!newFile.exists())
                    newFile.mkdir();// 不存在新建文件夹
                folderCopy(path, path.replace(src, des));
            } else
                flag = fileCopy(path, path.replace(src, des));// 文件复制函数
        }
        return flag;
    }

    public static void useDefaultSystemSetting(Context context) {
        Resources res = context.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public static boolean isMainProcess(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> apps = activityManager.getRunningAppProcesses();
        if (apps != null && apps.size() > 0) {
            int pid = android.os.Process.myPid();
            for (RunningAppProcessInfo app : apps) {
                if (app.pid == pid && context.getPackageName().equalsIgnoreCase(app.processName)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hasNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }

        NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if ((mobile != null && mobile.getState() == State.CONNECTED) || (wifi != null && wifi.getState() == State.CONNECTED)) {
            return true;
        }

        return false;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 输入法显示和隐藏
     */
    public static void toggleKeyboard(final android.app.Activity activity, final View v, final boolean visible) {
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                if (visible) {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(v, 0);
                } else {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (activity.getCurrentFocus() == null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    } else {
                        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        });
    }

    /**
     * 验证邮箱格式
     * 
     * @param email
     * @return
     */
    public static boolean validateEmail(String email) {
        String format = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern pattern = Pattern.compile(format); // "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher mc = pattern.matcher(email);
        return mc.matches();
    }

    /**
     * 验证是否是手机号
     * 
     * @param num
     * @return
     */
    public static boolean isMobileNum(String num) {
        String str = "^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(num);
        return m.matches();
    }


    /**
     * 复制内容到剪切板
     * 
     * @param context
     * @param text
     */
    @SuppressLint("NewApi")
	public static void copyToClipboard(Context context, String text) {
        if (TextUtils.isEmpty(text)) {
            showToast(context, "不能复制空的内容");
        } else {
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(ClipData.newPlainText(null, text.trim()));
            showToast(context, "已复制到剪切板");
        }
    }

    public static boolean isToday(String date) {
        String time = changeServerStringToGMTDate(date, "yyyy-MM-dd");
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return today.equals(time);
    }

    public static boolean isThisYear(String date) {
        String time = changeServerStringToGMTDate(date, "yyyy");
        String year = new SimpleDateFormat("yyyy").format(new Date());
        return year.equals(time);
    }

    public static String getTimeDifferenceNow(String date) {
        long time = CommonUtils.changeServerStringToGMTLong(date);
        long now = System.currentTimeMillis();
        long difference = now - time;
        if (difference < 0) {
            difference = -difference;
        }

        long day = difference / (24 * 60 * 60 * 1000);
        long hour = (difference / (60 * 60 * 1000)) % 24;
        long minute = (difference / (60 * 1000)) % 60;

        return day + "天" + hour + "小时" + minute + "分";
    }

    public static String getZhiboHeaderTime(String date) {
        if (CommonUtils.isToday(date)) {
            return CommonUtils.changeServerStringToGMTDate(date, "HH:mm");
        } else {
            if (CommonUtils.isThisYear(date)) {
                return CommonUtils.changeServerStringToGMTDate(date, "MM/dd");
            } else {
                return CommonUtils.changeServerStringToGMTDate(date, "yyyy/MM/dd");
            }
        }
    }

    public static String formatCurrentTime(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date());
    }

}