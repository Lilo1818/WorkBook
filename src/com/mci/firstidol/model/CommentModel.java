package com.mci.firstidol.model;

public class CommentModel {

	private long ArticleId;// 15036,
	private long CommentId;// 65861,
	private long CommentType;// 0,
	private long CommentUpCount;// 0,
	private String Content;// 个咯我炫舞了",
	private String CreateDate;// /Date(1452273819000)/",
	private String IP;// 192.168.1.1",
	private long ParentId;// 0,
	private long State;// 0,
	private String UserAvatar;// http://qn.ieasy.tv/image/201601/1452244959112_300.png",
	private long UserId;// 0,
	private String UserNickName;// Simple、",
	private long ChildCount;// 0
	private boolean isComment;

	public boolean isComment() {
		return isComment;
	}

	public void setComment(boolean isComment) {
		this.isComment = isComment;
	}

	public long getArticleId() {
		return ArticleId;
	}

	public void setArticleId(long articleId) {
		ArticleId = articleId;
	}

	public long getCommentId() {
		return CommentId;
	}

	public void setCommentId(long commentId) {
		CommentId = commentId;
	}

	public long getCommentType() {
		return CommentType;
	}

	public void setCommentType(long commentType) {
		CommentType = commentType;
	}

	public long getCommentUpCount() {
		return CommentUpCount;
	}

	public void setCommentUpCount(long commentUpCount) {
		CommentUpCount = commentUpCount;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public long getParentId() {
		return ParentId;
	}

	public void setParentId(long parentId) {
		ParentId = parentId;
	}

	public long getState() {
		return State;
	}

	public void setState(long state) {
		State = state;
	}

	public String getUserAvatar() {
		return UserAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		UserAvatar = userAvatar;
	}

	public long getUserId() {
		return UserId;
	}

	public void setUserId(long userId) {
		UserId = userId;
	}

	public String getUserNickName() {
		return UserNickName;
	}

	public void setUserNickName(String userNickName) {
		UserNickName = userNickName;
	}

	public long getChildCount() {
		return ChildCount;
	}

	public void setChildCount(long childCount) {
		ChildCount = childCount;
	}

	
}
