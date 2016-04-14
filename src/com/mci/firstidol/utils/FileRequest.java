/*
 * Copyright 2012 flyrise. All rights reserved.
 * Create at 2013-3-18 下午4:50:00
 */
package com.mci.firstidol.utils;

import org.apache.http.protocol.RequestContent;

/**
 * 类功能描述：</br>
 *
 * @author zms
 * @version 1.0 </p> 修改时间：</br> 修改备注：</br>
 */
public class FileRequest {
	private FileRequestContent fileContent;
	private RequestContent requestContent;

	public FileRequestContent getFileContent() {
		return fileContent;
	}

	public void setFileContent(FileRequestContent fileContent) {
		this.fileContent = fileContent;
	}

	public RequestContent getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(RequestContent requestContent) {
		this.requestContent = requestContent;
	}

}
