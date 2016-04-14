/*
 * Copyright 2012 flyrise. All rights reserved.
 * Create at 2013-3-18 下午4:50:00
 */
package com.mci.firstidol.utils;

import java.util.List;

/**
 * 类功能描述：</br>
 *
 * @author zms
 * @version 1.0 </p> 修改时间：</br> 修改备注：</br>
 */
public class FileRequestContent {
	private List<String> files;
	private String attachmentGUID;
	private List<String> deleteFileIds;

	public String getAttachmentGUID() {
		return attachmentGUID;
	}

	public void setAttachmentGUID(String attachmentGUID) {
		this.attachmentGUID = attachmentGUID;
	}

	public List<String> getDeleteFileIds() {
		return deleteFileIds;
	}

	public void setDeleteFileIds(List<String> attachmentItemIds) {
		this.deleteFileIds = attachmentItemIds;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

}
