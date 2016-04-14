package com.mci.firstidol.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;

import org.json.JSONObject;

import com.mci.firstidol.R;
import com.mci.firstidol.constant.Constant;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wang on 2015/6/8.
 */
@SuppressLint("NewApi")
public class Utily {

	/**
	 * 通用Activity调整
	 *
	 * @param context
	 * @param targetActivity
	 * @param param
	 *            intent参数
	 */
	
	public final static int INVALID_ID = -1;
	public final static int INVALID_REQUEST_ID = 0;
	
	
	
	public static void go2Activity(Context context, Class<?> targetActivity) {
		go2Activity(context, targetActivity, null, null);
	}

	public static void go2Activity(Context context, Class<?> targetActivity,
			Bundle bundle) {
		Intent intent = new Intent(context, targetActivity);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtras(bundle);
		Activity activity = (Activity) context;
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.activity_right_in,
				R.anim.activity_left_harf_out);
	}
	
	public static void go2Activity(Context context, Class<?> targetActivity,
			Bundle bundle,boolean isNew) {
		Intent intent = new Intent();
		if (isNew) {
//			intent.setAction(Intent.ACTION_MAIN);
//			intent.addCategory(Intent.CATEGORY_LAUNCHER); 
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		
		intent.putExtras(bundle);
		intent.setClass(context.getApplicationContext(), targetActivity);
//		Activity activity = (Activity) context;
		context.getApplicationContext().startActivity(intent);
//		activity.overridePendingTransition(R.anim.activity_right_in,R.anim.activity_left_harf_out);
	}
	
	/**
	 * 得到时间
	 * @param times
	 * @return
	 */
	public static String getTimeText(int times) {
		int hour = times / 3600;
		int minute = times % 3600 / 60;
		int second = times % 3600 % 60;
		String timeText = "";
		if(hour >= 10) {
			timeText = timeText + hour + ":";
		}else if(hour > 0) {
			timeText = timeText + "0" + hour + ":";
		}
		
		if(minute >= 10) {
			timeText = timeText + minute + ":";
		}else if(minute >= 0) {
			timeText = timeText + "0" + minute + ":";
		}else {
			timeText = timeText + "00:";
		}
		
		if(second >= 10) {
			timeText = timeText + second;
		}else if(second >= 0) {
			timeText = timeText + "0" + second;
		}else {
			timeText = timeText + "00";
		}
		return timeText;
	}

	/**
	 * 通用Activity调整
	 *
	 * @param context
	 * @param targetActivity
	 * @param param
	 *            intent参数
	 */
	public static void go2Activity(Context context, Class<?> targetActivity,
			HashMap<String, String> param,
			HashMap<String, Serializable> seriPram) {
		Intent intent = new Intent(context, targetActivity);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (param != null) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				intent.putExtra(entry.getKey(), entry.getValue());
			}
		}
		if (seriPram != null) {
			for (Map.Entry<String, Serializable> entry : seriPram.entrySet()) {
				intent.putExtra(entry.getKey(), entry.getValue());
			}
		}
		Activity activity = (Activity) context;
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.activity_right_in,
				R.anim.activity_left_harf_out);
	}

	public static void go2Activity(Context context, Intent intent) {
		Activity activity = (Activity) context;
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.activity_right_in,
				R.anim.activity_left_harf_out);
	}

	/**
	 * 通用Activity调整
	 *
	 * @param context
	 * @param targetActivity
	 * @param param
	 *            intent参数
	 */
	public static void go2Activity(Context context, Class<?> targetActivity,
			HashMap<String, String> param,
			HashMap<String, Serializable> seriPram, int requestCode) {
		Intent intent = new Intent(context, targetActivity);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (param != null) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				intent.putExtra(entry.getKey(), entry.getValue());
			}
		}
		if (seriPram != null) {
			for (Map.Entry<String, Serializable> entry : seriPram.entrySet()) {
				intent.putExtra(entry.getKey(), entry.getValue());
			}
		}
		Activity activity = (Activity) context;
		activity.startActivityForResult(intent, requestCode);
		activity.overridePendingTransition(R.anim.activity_right_in,
				R.anim.activity_left_harf_out);
	}

	/**
	 * 通用Activity调整
	 *
	 * @param context
	 * @param targetActivity
	 * @param param
	 *            intent参数
	 */
	public static void go2Activity(Context context, Class<?> targetActivity,
			HashMap<String, String> param,
			HashMap<String, Serializable> seriPram,
			HashMap<String, ArrayList<Parcelable>> listParams) {
		Intent intent = new Intent(context, targetActivity);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (param != null) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				intent.putExtra(entry.getKey(), entry.getValue());
			}
		}
		if (seriPram != null) {
			for (Map.Entry<String, Serializable> entry : seriPram.entrySet()) {
				intent.putExtra(entry.getKey(), entry.getValue());
			}
		}
		if (listParams != null) {
			for (Map.Entry<String, ArrayList<Parcelable>> entry : listParams
					.entrySet()) {
				intent.putParcelableArrayListExtra(entry.getKey(),
						entry.getValue());
			}
		}
		Activity activity = (Activity) context;
		activity.overridePendingTransition(R.anim.activity_right_in,
				R.anim.activity_left_harf_out);
	}

	public static String getWeekOfDate(Date date) {
		String[] weekOfDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekOfDays[w];
	}
	
	/**
     * Map集合转换为Json
     * @param map
     * @return
     */
    public static String map2json(HashMap<?, ?> map) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        if (map != null && map.size() > 0) {
            for (Object key : map.keySet()) {
                json.append(object2json(key));
                json.append(":");
                json.append(object2json(map.get(key)));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }
        return json.toString();
    }
    
    /**
     * 对象转换为Json
     * @param obj 
     * @return
     */
    public static String object2json(Object obj) {
        StringBuilder json = new StringBuilder();
        if (obj == null) {
            json.append("\"\"");
        } else if (obj instanceof String ) {
            json.append("\"").append(string2json(obj.toString())).append("\"");
        } 
        return json.toString();
    }
    
    /**
     * 字符串转换为Json
     * @param s
     * @return
     */
    public static String string2json(String s) {
        if (s == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
            case '"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '/':
                sb.append("\\/");
                break;
            default:
                if (ch >= '\u0000' && ch <= '\u001F') {
                    String ss = Integer.toHexString(ch);
                    sb.append("\\u");
                    for (int k = 0; k < 4 - ss.length(); k++) {
                        sb.append('0');
                    }
                    sb.append(ss.toUpperCase());
                } else {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
    }

	/**
	 * 验证邮箱
	 *
	 * @param email
	 *            ：邮箱
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "(?=^[\\w.@]{6,50}$)\\w+@\\w+(?:\\.[\\w]{2,3}){1,2}";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 得到屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.heightPixels;// 高度
	}

	/**
	 * 得到屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;// 高度
	}

	/**
	 * dp转成px
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转成dp
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}
	
	/**
	 * 从相机获取照片
	 * 
	 * @param context
	 * @param data
	 * @return
	 */
	public static String getPhotoPath(Context context, Intent data) {
		String path = null;

		Uri uri = data.getData();
		Cursor cur = context.getContentResolver().query(uri, null, null, null,
				null);
		if (cur.moveToFirst()) {
			path = cur.getString(cur.getColumnIndex("_data"));// 获取绝对路径
		}
		return path;

	}
	
	/**
	 * 从4.4上得到路径
	 * 
	 * @param context
	 * @param data
	 * @return
	 */
	public static String getImagePathFromKitKat(Context context, Intent data) {
		Uri selectedImage = data.getData();

		String picturePath = getPathFromKitKat(context, selectedImage);
		return picturePath;
	}

	/**
	 * 从4.4的相机获取图片
	 * 
	 * @param context
	 * @param data
	 * @return
	 */
	public static String getPhotoPathFromKitKat(Context context, Intent data) {
		String path = null;

		new DateFormat();
		String name = DateFormat.format("yyyyMMdd_hhmmss",
				Calendar.getInstance(Locale.CHINA))
				+ ".jpg";
		Bundle bundle = data.getExtras();
		Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

		FileOutputStream b = null;
		String pathStr = Environment.getExternalStorageDirectory().getPath();
		File file = new File(pathStr + "/DCIM/Camera/");
		if (file.exists()) {
			file.mkdirs();// 创建文件夹
		}
		String fileName = pathStr + "/DCIM/Camera/" + name;
		path = fileName;
		try {
			b = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				bitmap.recycle();
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return path;
	}
	/**
	 * 在android4.4上获取资源的路径
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static String getPathFromKitKat(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

}
