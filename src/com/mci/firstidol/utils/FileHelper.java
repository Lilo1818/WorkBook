//
//  FileHelper.java
//  FeOA
//
//  Created by Administrator on 2011-12-29.
//  Copyright 2011 flyrise. All rights reserved.
//

package com.mci.firstidol.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Environment;

public class FileHelper {
	private static String userID = "download";
	private static String baseFilePath = Environment
			.getExternalStorageDirectory().toString() + "/MommySecure";
	private static String dowloadFilePath = baseFilePath + "/" + userID
			+ "/files";
	/** 下载文件的临时路径 */
	private static String tempDirPath = baseFilePath + "/" + userID + "/temp";
	/** 通讯录头像路径 */
	private static String contactsPath = baseFilePath + "/" + userID
			+ "/portrait";

	private static String keystoreFilePath = baseFilePath + "/" + "KEYSTORE";

	private static String[] wrongChars = { "/", "\\", "*", "?", "<", ">", "\"",
			"|" };

	// 判断某路径的文件是否存储
	public static boolean isExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	// 创建文件
	public void newFile(File f) {
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param f
	 * */
	public static boolean newDirFile(File f) {
		if (!f.exists()) {
			return f.mkdirs();
		}
		return false;
	}

	// 取得指定目录的录音文件
	public static ArrayList<String> checkFile(File f) {
		ArrayList<String> data = new ArrayList<String>();
		File[] files = f.listFiles();
		String strtemp = null;
		for (int i = 0; i < files.length; i++) {
			strtemp = files[i].toString();
			strtemp = strtemp.substring(strtemp.lastIndexOf("/") + 1);

			if (strtemp.endsWith(".amr") || strtemp.endsWith(".mp3")) {
				data.add(strtemp);
				strtemp = null;
			}

		}

		return data;
	}

	public static ArrayList<String> getVoice(File f) {
		ArrayList<String> data = new ArrayList<String>();
		File[] files = f.listFiles();
		String strtemp = null;
		for (int i = 0; i < files.length; i++) {
			strtemp = files[i].toString();
			strtemp = strtemp.substring(strtemp.lastIndexOf("/") + 1);

			if (strtemp.endsWith(".jpg") || strtemp.endsWith(".png")) {
				data.add(strtemp);
				strtemp = null;
			}
		}

		return data;
	}

	public static String getFileSize(String path) {
		File file = new File(path);
		if (file.exists()) {
			long size = file.length();
			return getFileSize(size);
		} else {
			return "未知大小";
		}
	}

	public static String getFileSize(long size) {
		if (size >= 1024 * 1024) {
			double dousize = (double) size / (1024 * 1024);
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			return df.format(dousize) + "  Mb";
		} else if (size >= 1024) {
			double dousize = size / 1024;
			return (int) dousize + "  Kb";
		} else if (size > 0) {
			return size + "  b";
		} else {
			return "未知大小";
		}
	}

	public static String getWebFileSize(String path) {
		try {
			URL url = new URL(path);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			long size = con.getContentLength();
			if (size >= 1024 * 1024) {
				double dousize = (double) size / (1024 * 1024);
				java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
				return df.format(dousize) + "  Mb";
			} else if (size >= 1024) {
				double dousize = size / 1024;
				return (int) dousize + "  Kb";
			} else if (size > 0) {
				return size + "  b";
			} else {
				return "未知大小";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "未知大小";
		}
	}

	// 获取一个文件列表的里的总文件大小
	public static double getSize(List<String> willupload) {
		return (double) getSizeUnitByte(willupload) / (1024 * 1024);
	};

	/**
	 * 计算文件的大小，单位是字节
	 * 
	 * @param willupload
	 * @return
	 */
	public static long getSizeUnitByte(List<String> willupload) {
		long allfilesize = 0;
		for (int i = 0; i < willupload.size(); i++) {
			File newfile = new File(willupload.get(i));
			if (newfile.exists() && newfile.isFile()) {
				allfilesize = allfilesize + newfile.length();
			}
		}
		return allfilesize;
	}

	/**
	 * 根据文件名获取文件类型
	 */
	public static String getFileType(String name) {
		if (name.lastIndexOf(".") != -1) {
			String type = name.substring(name.lastIndexOf("."), name.length());
			return type;
		}
		return "未知类型";
	}

	/**
	 * 获取默认文件存放路径
	 */
	public static String getFileDefaultPath() {
		return dowloadFilePath;
	}

	public static String getBaseFilePath() {
		return baseFilePath;
	}

	/** 获取通讯录路径 */
	public static String getContactsPath() {
		return contactsPath;
	}

	/** 获取下载文件的临时路径 */
	public static String getTempDirPath() {
		return tempDirPath;
	}

	/**
	 * 获取音频文件存放路径
	 */
	public static String getVoiceFilePath() {
		String voicePath = baseFilePath + "/" + "voices/";
		File file = new File(voicePath);
		boolean isSuccess = newDirFile(file);
		return voicePath;
	}

	public static String getKeystoreFilePath() {
		return keystoreFilePath;
	}

	public static void setKeystoreFilePath(String keystoreFilePath) {
		FileHelper.keystoreFilePath = keystoreFilePath;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static boolean copyFile(String oldPath, String newPath) {
		boolean iscopy = false;
		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				inStream = new FileInputStream(oldPath); // 读入原文件
				fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				iscopy = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fs != null) {
					fs.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return iscopy;
	}

	/**
	 * 获取已经存在或已经下载好的文件
	 * 
	 * @return File 已经存在SDcard文件
	 */
	public static File getExistsFile(String id, String fileName) {
		File file = new File(FileHelper.getFileDefaultPath(), "("
				+ filterIDChars(id) + ")" + fileName);
		if (file.exists()) {
			return file;
		}
		return null;
	}

	/**
	 * (设置用户ＩＤ)
	 * 
	 * @param userId
	 *            　 用户ID
	 */
	public static void setUserID(String userId) {
		userID = userId;
		dowloadFilePath = baseFilePath + "/" + userID + "/files";
		tempDirPath = baseFilePath + "/" + userID + "/temp";
		contactsPath = baseFilePath + "/" + userID + "/portrait";
	}

	public static String getUserID() {
		return userID;
	};

	/**
	 * 过滤附件ID中某些不能存在在文件名中的字符
	 */
	public static String filterIDChars(String attID) {
		if (attID != null) {
			for (int i = 0; i < wrongChars.length; i++) {
				String c = wrongChars[i];
				if (attID.contains(c)) {
					attID = attID.replaceAll(c, "");
				}
			}
		}
		return attID;
	}

	public static void putAttachmentIntoUserFolder() {
		File folder = new File(baseFilePath + "/files");
		if (folder.exists() && !userID.equals("download")) {
			File[] files = folder.listFiles();
			int filesize = files.length;
			for (int i = 0; i < filesize; i++) {
				File file = files[i];
				if (file.isFile()) {
					String filename = file.getPath();
					filename = filename.substring(filename.lastIndexOf("/"),
							filename.length());
					File newFile = new File(dowloadFilePath + "/" + filename);
					file.renameTo(newFile);
				}
			}
			folder.delete();
		}
	}

	/**
	 * 获取过滤ID后的文件名
	 */
	public static String getFilterFileName(String flieName) {
		if (flieName == null || "".equals(flieName)) {
			return flieName;
		}
		boolean isNeedFilter = flieName.startsWith("(");
		int index = flieName.indexOf(")");
		if (isNeedFilter && index != -1) {
			int startIndex = index + 1;
			int endIndex = flieName.length();
			if (startIndex < endIndex) {
				return flieName.substring(startIndex, endIndex);
			}
		}
		return flieName;
	}

	/**
	 * 读取某个文件夹下的所有文件夹和文件, 返回所有文件名
	 * 
	 * @param filepath
	 * @return
	 */
	public static List<String> readfile(String filepath) {
		List<String> fileList = new ArrayList<String>();
		File file = new File(filepath);
		// 文件
		if (!file.isDirectory()) {
			// 不作处理
		} else if (file.isDirectory()) { // 如果是目录， 遍历所有子目录取出所有文件名
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				File readfile = new File(filepath + "/" + filelist[i]);
				if (!readfile.isDirectory()) {
					// LogUtils.i(readfile.getPath());
					fileList.add(readfile.getPath());
				} else if (readfile.isDirectory()) { // 子目录的目录
					// readfile(filepath + "/" + filelist[i], fileList);
				}
			}
		}
		return fileList;
	}

	/**
	 * 删除某个文件夹下的所有文件夹和文件
	 * 
	 * @param delpath
	 *            String
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @return boolean
	 */
	public static boolean deleteDirectory(String delpath) {
		File file = new File(delpath);
		if (!file.isDirectory()) {
			file.delete();
		} else if (file.isDirectory()) {
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				File delfile = new File(delpath + "/" + filelist[i]);
				if (!delfile.isDirectory()) {
					delfile.delete();
				} else if (delfile.isDirectory()) {
					deletefile(delpath + "/" + filelist[i]);
				}
			}
			file.delete();
		}
		return true;
	}

	/**
	 * 删除某个文件夹下的指定文件
	 * 
	 * @param delpath
	 * @param fileName
	 * @return
	 */
	public static boolean deletefile(String delpath, String fileName) {
		boolean result = false;
		File file = new File(delpath + fileName);
		if (file.exists() && file.isFile()) {// 如果是文件，则删除
			result = file.delete();
		}
		return result;
	}

	/**
	 * 删除某个文件夹下的指定文件
	 * 
	 * @param delpath
	 * @param fileName
	 * @return
	 */
	public static boolean deletefile(String delpath) {
		boolean result = false;
		File file = new File(delpath);
		if (file.exists() && file.isFile()) {// 如果是文件，则删除
			result = file.delete();
		}
		return result;
	}

}
