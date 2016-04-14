package com.mci.firstidol.utils;

import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;

import java.io.*;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import com.uucun.android.utils.newstring.MD5Util;

/**
 * Created with IntelliJ IDEA. User: Melvin Date: 13-12-4 Time: 下午5:08
 */
public class MSyncImageLoader {
	private Object lock = new Object();

	private boolean mAllowLoad = true;

	private boolean firstLoad = true;

	private int mStartLoadLimit = 0;

	private int mStopLoadLimit = 0;

	final Handler handler = new Handler();

	private HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();

	private static final String IMAGE_CACHE_DIRECTORY = "/baby/image/";

	public interface OnImageLoadListener {
		public void onImageLoad(Integer t, Drawable drawable);

		public void onError(Integer t);
	}

	public void setLoadLimit(int startLoadLimit, int stopLoadLimit) {
		if (startLoadLimit > stopLoadLimit) {
			return;
		}
		mStartLoadLimit = startLoadLimit;
		mStopLoadLimit = stopLoadLimit;
	}

	public void restore() {
		mAllowLoad = true;
		firstLoad = true;
	}

	public void lock() {
		mAllowLoad = false;
		firstLoad = false;
	}

	public void unlock() {
		mAllowLoad = true;
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public void loadImage(Integer t, String imageUrl,
			OnImageLoadListener listener) {
		final OnImageLoadListener mListener = listener;
		final String mImageUrl = imageUrl;
		final Integer mt = t;

		new Thread(new Runnable() {

			@Override
			public void run() {
				if (!mAllowLoad) {
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				if (mAllowLoad && firstLoad) {
					loadImage(mImageUrl, mt, mListener);
				}

				if (mAllowLoad && mt <= mStopLoadLimit && mt >= mStartLoadLimit) {
					loadImage(mImageUrl, mt, mListener);
				}
			}

		}).start();
	}

	private void loadImage(final String mImageUrl, final Integer mt,
			final OnImageLoadListener mListener) {

		if (imageCache.containsKey(mImageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(mImageUrl);
			final Drawable d = softReference.get();
			if (d != null) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (mAllowLoad) {
							mListener.onImageLoad(mt, d);
						}
					}
				});
				return;
			}
		}
		try {
			final Drawable d = loadImageFromUrl(mImageUrl);
			if (d != null) {
				imageCache.put(mImageUrl, new SoftReference<Drawable>(d));
			}
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (mAllowLoad) {
						mListener.onImageLoad(mt, d);
					}
				}
			});
		} catch (IOException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mListener.onError(mt);
				}
			});
			e.printStackTrace();
		}
	}

	public static Drawable loadImageFromUrl(String url) throws IOException {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File dir = new File(Environment.getExternalStorageDirectory()
					+ IMAGE_CACHE_DIRECTORY);
			if (!dir.exists()) {
				dir.mkdir();
			}
			File f = new File(Environment.getExternalStorageDirectory()
					+ IMAGE_CACHE_DIRECTORY + MD5Util.getMD5String(url));
			if (f.exists()) {
				FileInputStream fis = new FileInputStream(f);
				Drawable d = Drawable.createFromStream(fis, null);
				return d;
			}
			URL m = new URL(url);
			InputStream i = (InputStream) m.getContent();
			DataInputStream in = new DataInputStream(i);
			FileOutputStream out = new FileOutputStream(f);
			byte[] buffer = new byte[1024];
			int byteread = 0;
			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}
			in.close();
			out.close();
			Drawable d = Drawable.createFromStream(i, null);
			return loadImageFromUrl(url);
		} else {
			URL m = new URL(url);
			InputStream i = (InputStream) m.getContent();
			Drawable d = Drawable.createFromStream(i, null);
			return d;
		}

	}
}
