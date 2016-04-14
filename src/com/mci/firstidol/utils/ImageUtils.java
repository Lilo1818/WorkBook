package com.mci.firstidol.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;

public class ImageUtils {

	// 拍照保存路径
	public static String FILE_DIR = Environment.getExternalStorageDirectory()
			+ "/MommySecure/images/";

	/**
	 * @param 将图片内容解析成字节数组
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}

	/**
	 * @param 将字节数组转换为ImageView可调用的Bitmap对象
	 * @param bytes
	 * @param opts
	 * @return Bitmap
	 */
	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	/**
	 * 把Bitmap转Byte
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	// byte转bitmap
	public static Bitmap Bytes2Bimap(byte[] bytes) {
		if (bytes.length != 0)
			return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	// drawable转bitmap
	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable.getOpacity() != -1)
			;
		for (Bitmap.Config config = Bitmap.Config.ARGB_8888;; config = Bitmap.Config.RGB_565)
			return Bitmap.createBitmap(800, 1280, config);
	}

	// 获取某文件的bitmap对象
	public static Bitmap getBitmap(String fileName) {
		File file = new File(FILE_DIR, fileName);
		boolean bool = file.exists();
		Bitmap bitmap = null;
		if (bool)
			;
		try {
			bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String storeInSD(Bitmap bitmap) {
		LogUtils.i(FILE_DIR);
		File file = new File(FILE_DIR);
		if (!file.exists()) {
			file.mkdirs();
		}
		String fileName = UUID.randomUUID().toString() + ".png";
		File localFile2 = new File(file + "/" + fileName);

		try {
			// localFile2.createNewFile();
			FileOutputStream fileStream = new FileOutputStream(localFile2);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileStream);
			fileStream.flush();
			fileStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return localFile2.getAbsolutePath();
	}

	public static void storeInSD(Bitmap bitmap, String fileName) {
		LogUtils.i(FILE_DIR);
		File file = new File(FILE_DIR);
		if (!file.exists()) {
			file.mkdirs();
		}

		// File localFile2 = new File(FILE_DIR+fileName+".png");
		try {
			// FileOutputStream fileStream = new FileOutputStream(localFile2);
			// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileStream);
			// fileStream.flush();
			// fileStream.close();

			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(FILE_DIR + fileName + ".png"));
			bitmap.compress(CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();

			return;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void storeInSD1(Bitmap bitmap, String fileName) {
		LogUtils.i(FILE_DIR);
		File file = new File(FILE_DIR);
		if (!file.exists()) {
			file.mkdirs();
		}
		File localFile2 = new File(FILE_DIR + fileName + ".png");
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;
			while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
				baos.reset();// 重置baos即清空baos
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
				options -= 10;// 每次都减少10
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(
					baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
			Bitmap bmp = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片

			FileOutputStream fileStream = new FileOutputStream(localFile2);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileStream);

			fileStream.flush();
			fileStream.close();
			isBm.close();
			baos.close();
			return;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 压缩图片大小
	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 把字节数组保存为一个文件
	 * 
	 * @Author HEH
	 * @EditTime 2010-07-19 上午11:45:56
	 */
	public static File getFileFromBytes(byte[] b, String outputFile) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * 处理图片
	 * 
	 * @param bm
	 *            所要转换的bitmap
	 * @param newWidth新的宽
	 * @param newHeight新的高
	 * @return 指定宽高的bitmap
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片 www.2cto.com
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}
}
