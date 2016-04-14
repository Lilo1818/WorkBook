package com.mci.firstidol.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StarVideoModel implements Serializable{
	
	public long ArticleId;//标题ID
	
	public String Title;//标题
	
	private long ModelType;// 0,
	
	public String VideoIco;//视频背景图
	
	public String Ico;//视频地址
	
	public long getModelType() {
		return ModelType;
	}

	public void setModelType(long modelType) {
		ModelType = modelType;
	}

	public String ViewCount;
	
	public String CommentCount;
	
	public String UpCoun;//赞的数量
	
	public String UpCount2;
	
	public String UpCount3;
	
	public String UpCount4;
	
	public String UserNickName;
	
	public String UserAvatar;//用户图片
	
	public String PublishDate;//日期时间
	
	public boolean HasStore;
	
	public ArrayList<CommentModel> HotComments;

	public long getArticleId() {
		return ArticleId;
	}

	public void setArticleId(long articleId) {
		ArticleId = articleId;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getVideoIco() {
		return VideoIco;
	}

	public void setVideoIco(String videoIco) {
		VideoIco = videoIco;
	}

	public String getIco() {
		return Ico;
	}

	public void setIco(String ico) {
		Ico = ico;
	}

	public String getViewCount() {
		return ViewCount;
	}

	public void setViewCount(String viewCount) {
		ViewCount = viewCount;
	}

	public String getCommentCount() {
		return CommentCount;
	}

	public void setCommentCount(String commentCount) {
		CommentCount = commentCount;
	}

	public String getUpCoun() {
		return UpCoun;
	}

	public void setUpCoun(String upCoun) {
		UpCoun = upCoun;
	}

	public String getUpCount2() {
		return UpCount2;
	}

	public void setUpCount2(String upCount2) {
		UpCount2 = upCount2;
	}

	public String getUpCount3() {
		return UpCount3;
	}

	public void setUpCount3(String upCount3) {
		UpCount3 = upCount3;
	}

	public String getUpCount4() {
		return UpCount4;
	}

	public void setUpCount4(String upCount4) {
		UpCount4 = upCount4;
	}

	public String getUserNickName() {
		return UserNickName;
	}

	public void setUserNickName(String userNickName) {
		UserNickName = userNickName;
	}

	public String getUserAvatar() {
		return UserAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		UserAvatar = userAvatar;
	}

	public String getPublishDate() {
		return PublishDate;
	}

	public void setPublishDate(String publishDate) {
		PublishDate = publishDate;
	}

	public boolean isHasStore() {
		return HasStore;
	}

	public void setHasStore(boolean hasStore) {
		HasStore = hasStore;
	}

	public ArrayList<CommentModel> getHotComments() {
		return HotComments;
	}

	public void setHotComments(ArrayList<CommentModel> hotComments) {
		HotComments = hotComments;
	}

	

}
