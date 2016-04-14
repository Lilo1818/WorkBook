package com.mci.firstidol.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CancellationException;

import org.apache.http.ProtocolException;
import org.apache.http.protocol.RequestContent;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.utils.AsyncTaskUtils.IProgressListener;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class HttpUpload implements Runnable {
	public static final String TAG = "HttpMultipartUpload";
	// public static final String UPLOAD_URL =
	// "http://192.168.100.4:8080/Test/UploadFile";
	public static final String UPLOAD_URL = "http://192.168.2.162:8080/Test/UploadFile";
	public static final String HTTP_LINE_END = "\r\n";
	private static String twoHyphens = "--";
	// private static String boundary = "AaB03x87yxdkjnxvi7";
	private static final String boundary = "---------------------------7db1c523809b2";// 数据分割线
	private static int maxBufferSize = 8 * 1024;
	private static String charSet = "UTF-8";

	private List<String> fileList;
	private RequestContent requestContent;
	private FileRequest fileRequest;
	private HashMap<String, String> parameters;
	private FileUploadHandler uploadHandler;

	private Context context;
	private IProgressListener iProgressListener;
	private long filesSize;
	private long uploadedSize;
	private Thread uploadThread;
	private boolean isStop = Boolean.FALSE;

	/**
	 * 上传附件
	 * 
	 * @param fileList
	 *            待上传的附件列表
	 * @param parameters
	 *            所有的纯文本参数
	 * @param uploadHandler
	 *            上传回调接口
	 */
	public HttpUpload upload(FileRequest fileRequest) {
		this.fileRequest = fileRequest;
		List<String> files = new ArrayList<String>();
		files.add(Environment.getExternalStorageDirectory().toString()
				+ "/shinsoft/2.jpg");
		files.add(Environment.getExternalStorageDirectory().toString()
				+ "/shinsoft/3.jpg");
		files.add(Environment.getExternalStorageDirectory().toString()
				+ "/shinsoft/4.jpg");
		files.add(Environment.getExternalStorageDirectory().toString()
				+ "/shinsoft/5.jpg");

		this.fileList = files;
		this.requestContent = null;
		uploadThread = new Thread(this);
		uploadThread.setName("HttpMultipartUpload--Thread");
		uploadThread.start();
		return this;
	}

	/**
	 * 上传附件
	 * 
	 * @param pCallback
	 * @param pCallback
	 * @param fileList
	 *            待上传的附件列表
	 * @param parameters
	 *            所有的纯文本参数
	 * @param uploadHandler
	 *            上传回调接口
	 */
	public HttpUpload upload(Context context, FileRequest fileRequest,
			IProgressListener iProgressListener) {
		this.fileRequest = fileRequest;
		this.context = context;
		// this.fileList = fileRequest.getFileContent().getFiles();
		// this.requestContent = fileRequest.getRequestContent();

		List<String> files = new ArrayList<String>();
		files.add(Environment.getExternalStorageDirectory().toString()
				+ "/shinsoft/2.jpg");
		files.add(Environment.getExternalStorageDirectory().toString()
				+ "/shinsoft/3.jpg");
		files.add(Environment.getExternalStorageDirectory().toString()
				+ "/shinsoft/4.jpg");
		files.add(Environment.getExternalStorageDirectory().toString()
				+ "/shinsoft/5.jpg");
		files.add(Environment.getExternalStorageDirectory().toString()
				+ "/shinsoft/6.jpg");
		// files.add(Environment.getExternalStorageDirectory().toString()+"/shinsoft/7.jpg");

		this.fileList = files;
		this.requestContent = null;
		this.iProgressListener = iProgressListener;
		// uploadThread = new Thread(this);
		// uploadThread.setName("HttpMultipartUpload--Thread");
		// uploadThread.start();
		doRequest();
		return this;
	}

	/**
	 * 上传附件
	 * 
	 * @param fileList
	 *            待上传的附件列表
	 * @param parameters
	 *            所有的纯文本参数
	 * @param uploadHandler
	 *            上传回调接口
	 */
	public HttpUpload upload(FileRequest fileRequest,
			FileUploadHandler uploadHandler) {
		this.fileRequest = fileRequest;
		this.fileList = fileRequest.getFileContent().getFiles();
		this.requestContent = fileRequest.getRequestContent();
		this.uploadHandler = uploadHandler;
		uploadThread = new Thread(this);
		uploadThread.setName("HttpMultipartUpload--Thread");
		uploadThread.start();
		return this;
	}

	/**
	 * 取消上传附件
	 */
	public void cancel() {
		isStop = Boolean.TRUE;
	}

	@Override
	public void run() {
		doRequest();
	}

	public void doRequest() {
		try {
			parameters = new HashMap<String, String>();
			parameters.put("FileName", "file001");
			parameters.put("ObjectName", "Test");
			// parameters = getUploadParams(fileRequest.getFileContent());

			post();
		} catch (FileNotFoundException e) {
			if (!isStop) {
				// uploadHandler.sendFailureMessage(e, "上传文件不存在");
				iProgressListener.onSendFail(new CancellationException(
						"上传文件不存在"));
				LogUtils.i("上传文件不存在");
			}
		} catch (Exception e) {
			if (!isStop) {
				iProgressListener
						.onSendFail(new CancellationException("上传失败-1"));
				LogUtils.i("上传失败1");
				Log.e(TAG, e.getMessage(), e);
				// uploadHandler.sendFailureMessage(e, "上传失败");
			}
		}
	}

	/**
	 * 
	 * 提交HTTP POST请求
	 * 
	 * @param dos
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private void post() throws IOException, FileNotFoundException,
			UnsupportedEncodingException {
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		BufferedReader bin = null;

		try {
			filesSize = FileHelper.getSizeUnitByte(fileList);
			conn = getConnection(new URL(UPLOAD_URL));
			dos = new DataOutputStream(conn.getOutputStream());

			postFileParams(dos);
			postTextplainParams(dos);
			postEnd(dos);

			// bin = new BufferedReader(new
			// InputStreamReader(conn.getInputStream(), charSet));
			// String rsp = getResponse(bin);
			if (!isStop) {
				// uploadHandler.onUploadSuccess(rsp,requestContent);
				// uploadHandler.sendSuccessMessage();
			}

		} catch (ProtocolException e) {
			iProgressListener.onSendFail(new CancellationException("上传失败-2"));
			LogUtils.i("上传失败2");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			iProgressListener.onSendFail(new CancellationException("上传失败-3"));
			LogUtils.i("上传失败3");
		} finally {
			release(conn, dos, bin);
		}
	}

	private HttpURLConnection getConnection(URL url) throws ProtocolException,
			IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(15 * 1000);
		conn.setConnectTimeout(15 * 1000);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);

		// HttpURLConnection默认会缓存所有的输入直到write完毕(造成OOM)，
		// 设置setChunkedStreamingMode可以改变该属性，实行按块发送
		conn.setChunkedStreamingMode(maxBufferSize);

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="
				+ boundary);
		return conn;
	}

	/**
	 * 
	 * 提交附件域
	 * 
	 * @param dos
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private void postFileParams(DataOutputStream dos)
			throws FileNotFoundException, UnsupportedEncodingException,
			IOException {
		for (int i = 0; i < fileList.size(); i++) {
			postFile(new File(fileList.get(i)), "file[" + i + "]", dos);
		}
	}

	private void postFile(File file, String fileFieldName, DataOutputStream dos)
			throws FileNotFoundException, UnsupportedEncodingException,
			IOException {
		if (isStop) {
			return;
		}
		postFileHeader(fileFieldName, file.getName(), dos);
		postFileContent(file, dos);
	}

	/**
	 * 提交附件域的头信息
	 * 
	 * @param fileFieldName
	 * @param fileName
	 * @param dos
	 * @throws IOException
	 */
	private void postFileHeader(String fileFieldName, String fileName,
			DataOutputStream dos) throws IOException {
		dos.writeBytes(twoHyphens + boundary + HTTP_LINE_END);
		dos.write(encode("Content-Disposition: form-data; name=\""
				+ fileFieldName + "\"; filename=\"" + fileName + "\""
				+ HTTP_LINE_END));
		dos.writeBytes("Content-Type: application/octet-stream" + HTTP_LINE_END);
		dos.writeBytes(HTTP_LINE_END);
	}

	/**
	 * 提交附件域内容
	 * 
	 * @param file
	 * @param dos
	 * @throws IOException
	 */
	private void postFileContent(File file, DataOutputStream dos)
			throws IOException {
		FileInputStream fileInputStream = null;
		int length;
		byte[] buffer = null;
		try {
			fileInputStream = new FileInputStream(file);
			buffer = new byte[Math.min((int) file.length(), maxBufferSize)];

			if (file.length() != 0) { // 文件为空直接写结束位就好
				// read file and write it into form...
				while ((length = fileInputStream.read(buffer)) != -1) {
					Log.i("xxxxxxxxxxx", "buffer:" + buffer + "==length:"
							+ length + "==uploadedSize:" + uploadedSize);

					dos.write(buffer, 0, length);
					uploadedSize += length;
					if (isStop) {
						break;
					}
					publishProgress();
				}
			}
			dos.writeBytes(HTTP_LINE_END);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
		}
	}

	/**
	 * 提交文本域
	 * 
	 * @param dos
	 * @throws IOException
	 */
	private void postTextplainParams(DataOutputStream dos) throws IOException {
		for (String name : parameters.keySet()) {
			dos.writeBytes(HTTP_LINE_END);
			dos.writeBytes(twoHyphens + boundary + HTTP_LINE_END);
			dos.writeBytes("Content-Disposition: form-data; name=\"" + name
					+ "\"" + HTTP_LINE_END);
			dos.writeBytes(HTTP_LINE_END);
			dos.writeBytes(parameters.get(name));
		}
	}

	/**
	 * 提交HTTP结束位
	 * 
	 * @param dos
	 * @throws IOException
	 */
	private void postEnd(DataOutputStream dos) throws IOException {
		dos.writeBytes(HTTP_LINE_END);
		dos.writeBytes(twoHyphens + boundary + twoHyphens + HTTP_LINE_END);
		dos.flush();
	}

	/**
	 * 对包含中文的字符串进行转码，此为UTF-8。
	 */
	private byte[] encode(String value) throws UnsupportedEncodingException {
		return value.getBytes(charSet);
	}

	/**
	 * 获取HTTP响应信息
	 * 
	 * @param bin
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private String getResponse(BufferedReader bin) throws IOException {
		if (isStop) {
			return "";
		}

		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = bin.readLine()) != null) {
			buffer.append(line);
		}
		Log.e("Test", "================" + buffer.toString());
		return buffer.toString();
	}

	private void release(HttpURLConnection conn, DataOutputStream dos,
			BufferedReader bin) {
		try {
			if (dos != null) {
				dos.close();
			}
			if (bin != null) {
				bin.close();
			}
		} catch (IOException e) {
		}

		if (conn != null) {
			conn.disconnect();
		}
	}

	/**
	 * 进度变了，发布
	 */
	private void publishProgress() {
		// uploadHandler.sendProgressMessage((int)(uploadedSize * 100 /
		// filesSize));
		iProgressListener
				.onProgressChanged((int) (uploadedSize * 100 / filesSize));
	}

	private HashMap<String, String> getUploadParams(
			FileRequestContent fileContent) throws Exception {
		JSONObject properties = new JSONObject();
		properties.put("iq", new JSONObject());
		JSONObject parent = properties.getJSONObject("iq");

		JSONObject queryContent = new JSONObject();
		if (fileContent.getAttachmentGUID() != null) {
			queryContent.put("attachmentGUID", fileContent.getAttachmentGUID());
		}

		if (fileContent.getDeleteFileIds() != null
				&& fileContent.getDeleteFileIds().size() > 0) {
			JSONArray attachmentsProperty = new JSONArray();
			queryContent.put("attachmentsDelete", attachmentsProperty);
			for (String id : fileContent.getDeleteFileIds()) {
				JSONObject attachmentItemObj = new JSONObject();
				attachmentItemObj.put("attachmentItem", id);
				attachmentsProperty.put(attachmentItemObj);
			}
		}
		parent.put("query", queryContent);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("json", properties.toString());
		return params;
	}

	public ArrayList<byte[]> upload(Context context, String taskID,
			IProgressListener<?> iProgressListener) {
		this.context = context;
		// this.fileList = fileRequest.getFileContent().getFiles();
		// this.requestContent = fileRequest.getRequestContent();

		/*
		 * List<String> files = new ArrayList<String>();
		 * files.add(Environment.getExternalStorageDirectory().toString() +
		 * "/shinsoft/2.jpg");
		 * files.add(Environment.getExternalStorageDirectory().toString() +
		 * "/shinsoft/3.jpg");
		 * files.add(Environment.getExternalStorageDirectory().toString() +
		 * "/shinsoft/4.jpg");
		 * 
		 * files.add(Environment.getExternalStorageDirectory().toString() +
		 * "/shinsoft/5.jpg");
		 * files.add(Environment.getExternalStorageDirectory().toString() +
		 * "/shinsoft/6.jpg");
		 * 
		 * 
		 * // files.add(Environment.getExternalStorageDirectory().toString()+
		 * "/shinsoft/7.jpg");
		 * 
		 * this.fileList = files;
		 */
		this.fileList = FileHelper.readfile(ImageUtils.FILE_DIR + taskID);
		this.requestContent = null;
		this.iProgressListener = iProgressListener;
		filesSize = FileHelper.getSizeUnitByte(fileList);

		ArrayList<byte[]> data = readBytes();

		return data;
	}

	private ArrayList<byte[]> readBytes() {
		// TODO Auto-generated method stub
		ArrayList<byte[]> data = new ArrayList<byte[]>();
		byte[] buffer = null;
		for (int i = 0; i < fileList.size(); i++) {
			buffer = obtainFile(new File(fileList.get(i)));
			data.add(buffer);
		}
		return data;
	}

	private byte[] obtainFile(File file) {

		FileInputStream fileInputStream = null;
		int length;
		byte[] buffer = null;
		try {
			fileInputStream = new FileInputStream(file);
			buffer = new byte[Math.min((int) file.length(), maxBufferSize)];

			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (file.length() != 0) { // 文件为空直接写结束位就好
				// read file and write it into form...
				while ((length = fileInputStream.read(buffer)) != -1) {
					Log.i("xxxxxxxxxxx", "buffer:" + buffer + "==length:"
							+ length + "==uploadedSize:" + uploadedSize);
					// baos.write(buffer, 0, length);
					uploadedSize += length;
					if (isStop) {
						break;
					}
					publishProgress();
				}
			}

			// Base64.encode(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
					fileInputStream = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return buffer;
	}

}