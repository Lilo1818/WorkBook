package com.mci.firstidol.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import java.io.*;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.uucun.android.utils.io.IOUtils;
import com.uucun.android.utils.newstring.StringUtils;

@SuppressLint("NewApi")
public class SyncImageLoader {

    private Object lock = new Object();
    
    private ExecutorService executorService = null;

    private boolean mAllowLoad = true;

    private boolean firstLoad = true;

    private int mStartLoadLimit = 0;

    private int mStopLoadLimit = 0;

    final Handler handler = new Handler();
    /**
     * Avoid OutOfMemoryError
     */
    private Options opts;

    /**
     * 默认缓存目录名称
     */
    private String cacheDirName = "mk";

    /**
     * 图片类型,菜品列表ICON
     */
//	public static int MENU_ICON = 1;
//
//	private int imageFlag = 1;

    /**
     * 缓存目录
     */
    private static final String IMAGE_CACHE_DIRECTORY = "image";

    public ConcurrentHashMap<String, SoftReference<Bitmap>> imageCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>();

    public SyncImageLoader(Context context) {
        super();
        opts = new Options();
        opts.inJustDecodeBounds = true;
        opts.inJustDecodeBounds = false;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        opts.inTempStorage = new byte[16 * 1024];
        opts.inSampleSize = computeSampleSize(opts, -1,Utily.getHeight(context) * Utily.getWidth(context));
    }

    public static int computeSampleSize(Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    // 压缩图片
    private static int computeInitialSampleSize(Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public interface OnImageLoadListener {
        public void onImageLoad(Integer t, Bitmap bitmap, View imageView);

        public void onError(Integer t, View imageView);
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
                          OnImageLoadListener listener, final View parent, final int flag) {
        final OnImageLoadListener mListener = listener;
        final String mImageUrl = imageUrl;
        final Integer mt = t;
        //imageFlag = flag;
        final Bitmap d = getBitmapFromMemory(mImageUrl);
        if (d != null) {
            handler.post(new Runnable() {
                public void run() {
                    mListener.onImageLoad(mt, d, parent);
                }
            });
        }
        getExecutorService().execute(new Runnable() {
        	
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
                    loadImage(mImageUrl, mt, mListener, parent);
                }
                if (mAllowLoad && mt <= mStopLoadLimit + 1
                        && mt >= mStartLoadLimit - 1) {
                    loadImage(mImageUrl, mt, mListener, parent);
                }
            }

        });
    }

    private void loadImage(final String mImageUrl, final Integer mt,
                           final OnImageLoadListener mListener, final View parent) {
        final Bitmap d = getBitmapFromMemory(mImageUrl);
        if (d != null) {
            handler.post(new Runnable() {
                public void run() {
                    if (mAllowLoad) {
                        mListener.onImageLoad(mt, d, parent);
                    }
                }
            });
            return;
        }
        try {
            final Bitmap mBp = getBitmapFromCacheOrUrl(mImageUrl);
            handler.post(new Runnable() {
                public void run() {
                    if (mAllowLoad) {
                        mListener.onImageLoad(mt, mBp, parent);
                    }
                }
            });
        } catch (Exception e) {
            handler.post(new Runnable() {
                public void run() {
                    mListener.onError(mt, parent);
                }
            });
            e.printStackTrace();
        }
    }

    /**
     * 从本地、或者网络上取得图片
     *
     * @param url
     * @return
     * @Title: getBitmapFromCacheOrUrl
     */
    private Bitmap getBitmapFromCacheOrUrl(String url) {
        String name = StringUtils.generateFileName(url);
        Bitmap bb = null;
        /** 从缓存文件中取 **/
        bb = loadImageFromLocal(getCacheDirectory(), name);
        if (bb != null) {
            imageCache.put(name, new SoftReference<Bitmap>(bb));
            return bb;
        }
        /** 从网络中加载 ***/
        bb = forceDownload(url);
        if (bb != null) {

            return bb;
        }
        return null;

    }

    /**
     * 强制下载
     *
     * @param url
     * @return
     * @Title: forceDownload2
     */
    private Bitmap forceDownload(String url) {
        if (url == null || TextUtils.isEmpty(url)) {
            return null;
        }
        return downloadImageFromUri(url);
    }

    /**
     * 从URL中下载图片
     *
     * @param mUrl
     * @return
     * @Title: downloadImageFromUri
     */
    private Bitmap downloadImageFromUri(String mUrl) {
        Bitmap bitmap = null;
        String fileName = StringUtils.generateFileName(mUrl);
        try {
            byte[] bb = getImageFromServer(mUrl);
            if (bb == null) {
                return null;
            }
            bitmap = BitmapFactory.decodeByteArray(bb, 0, bb.length, opts);
        } catch (OutOfMemoryError e) {
            System.gc();
            return null;
        }

//		if (imageFlag == SyncImageLoader.MENU_ICON) {
//			bitmap = restoreImageToCache(bitmap, 60, 60, fileName);
//		} else {
        bitmap = restoreImageToCache(bitmap, (int) bitmap.getWidth(),
                (int) bitmap.getHeight(), fileName);
        //}

        return bitmap;
    }

    /**
     * 存储图片
     *
     * @param bitmap
     * @param width
     * @param height
     * @param imageName
     * @return
     * @Title: restoreImageToCache
     */
    private Bitmap restoreImageToCache(Bitmap bitmap, int width, int height,
                                       String imageName) {
        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            addImageToLocalCache(getCacheDirectory(), imageName, bitmap);
        }
        return bitmap;
    }

    /**
     * 取得缓存目录
     *
     * @return
     * @Title: getCacheDirectory
     */
    public File getCacheDirectory() {
        StringBuilder imageCache = new StringBuilder(cacheDirName);
        imageCache.append(File.separator);
        imageCache.append(IMAGE_CACHE_DIRECTORY);
        File cacheDirectory = IOUtils.getExternalFile(imageCache.toString());
        if (!cacheDirectory.exists()) {
            cacheDirectory.mkdirs();
        }
        return cacheDirectory;
    }

    /**
     * 将文件写入到缓存中
     *
     * @param cacheDirectory
     * @param fileName
     * @param bitmap
     * @return
     * @Title: addImageToLocalCache
     */
    private boolean addImageToLocalCache(File cacheDirectory, String fileName,
                                         Bitmap bitmap) {

        File coverFile = new File(cacheDirectory, fileName);

        FileOutputStream out = null;
        try {
            if (!coverFile.exists())
                coverFile.createNewFile();
            out = new FileOutputStream(coverFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            String s = IOUtils.exception2String(e);
            return false;
        } finally {
            IOUtils.closeStream(out);
        }
        return true;
    }

    /**
     * @param urlPath
     * @return byte[]
     * @Description 获取图片流
     */
    public static byte[] getImageFromServer(String urlPath) {
        if (urlPath == null)
            return null;

        URL url = null;
        HttpURLConnection con = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        try {
            url = new URL(urlPath);
            con = (HttpURLConnection) url.openConnection();
            is = con.getInputStream();
            byte[] bs = new byte[1024];
            int len;
            outStream = new ByteArrayOutputStream();
            while ((len = is.read(bs)) != -1) {
                outStream.write(bs, 0, len);
            }
            return outStream.toByteArray();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(is);
            IOUtils.closeStream(outStream);
        }

        return null;
    }

    /**
     * 从本地缓存中加载图片
     *
     * @param cacheDirectory
     * @param fileName
     * @return
     * @Title: loadImageFromLocal
     */
    private Bitmap loadImageFromLocal(File cacheDirectory, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        final File file = new File(cacheDirectory, fileName);
        if (file.exists()) {
            InputStream stream = null;
            try {
                file.setLastModified(System.currentTimeMillis());
                stream = new FileInputStream(file);
                //stream = new BufferedInputStream(file.openFileInputStream(), 16*1024);
                return BitmapFactory.decodeStream(stream, null, opts);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeStream(stream);
            }
        }
        return null;
    }

    /**
     * 取得內存中的圖像
     *
     * @param url
     * @return
     * @Title: getBitmapFromMemory
     */
    public Bitmap getBitmapFromMemory(String url) {
        String name = StringUtils.generateFileName(url);
        Bitmap bb = null;
        /*** 从内存中取 **/
        SoftReference<Bitmap> reference = imageCache.get(name);
        if (reference != null) {
            bb = reference.get();
        }
        if (bb != null) {
            imageCache.put(name, new SoftReference<Bitmap>(bb));
            return bb;
        }
        return null;
    }
    
    
    /**
     * 得到线程池
     * @return
     */
    private ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(10);
        }
        return executorService;
    }
}
