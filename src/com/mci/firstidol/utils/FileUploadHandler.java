/*
 * Copyright 2012 flyrise. All rights reserved.
 * Create at 2012-12-22 上午10:54:29
 */
package com.mci.firstidol.utils;

import org.apache.http.protocol.RequestContent;
import org.json.JSONObject;

import android.os.Message;

/**
 * 类功能描述：</br>
 *
 * @author zms
 * @version 1.0 </p> 修改时间：</br> 修改备注：</br>
 */
public class FileUploadHandler {
	private static final int PROGRESS_MESSAGE = 4;

	public void onProgress(int progress) {
	}

	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case PROGRESS_MESSAGE:
			onProgress((Integer) msg.obj);
			break;
		}
	}

	public void onUploadSuccess(String rsp, RequestContent rquestContent) {

	}

	public void sendProgressMessage(int progress) {
		// sendMessage(obtainMessage(PROGRESS_MESSAGE, progress));
	}

	public void sendSuccessMessage(String responseBody) {
		// super.sendSuccessMessage(responseBody);
	}

	public void sendFailureMessage(Throwable e, String responseBody) {
		// super.sendFailureMessage(e, responseBody);
	}
}
