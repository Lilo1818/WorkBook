package com.mci.firstidol.model;

import java.util.ArrayList;

import com.mci.firstidol.model.SquareModel.LiveAnnex;
import com.mci.firstidol.model.SquareModel.SquareLiveChatModel;


public class SquareRequest extends BaseRequest {
	
	/**
	 * 明星排行榜-明星置顶
	 * @return
	 */
	public static SquareOrderStarTopRequest squareOrderStarTopRequest() {
		return new SquareOrderStarTopRequest();
	}
	public static class SquareOrderStarTopRequest extends SquareRequest{
		public long starid;
	}
	
	
	/**
	 * 直播-聊天信息删除
	 * @return
	 */
	public static SquareLiveDetailDeleteRequest squareLiveDetailDeleteRequest() {
		return new SquareLiveDetailDeleteRequest();
	}
	public static class SquareLiveDetailDeleteRequest extends SquareRequest{
		public long chatId;
		public long articleId;
		public long commentId;
	}
	
	/**
	 * 直播-聊天评论点赞
	 * @return
	 */
	public static SquareLiveDetailUpRequest squareLiveDetailUpRequest() {
		return new SquareLiveDetailUpRequest();
	}
	public static class SquareLiveDetailUpRequest extends SquareRequest{
		public long chatId;
	}
	/**
	 * 广场-发现详情评论点赞
	 * @return
	 */
	public static SquareFoundDetaiCommentLikelRequest squareFoundDetaiCommentLikelRequest() {
		return new SquareFoundDetaiCommentLikelRequest();
	}
	public static class SquareFoundDetaiCommentLikelRequest extends SquareRequest{
		public CommentAction action;
	}
	
	public static CommentAction commentAction() {
		return new CommentAction();
	}
	public static class CommentAction{
		public String ActType;
		public String ActName;
		public long CommentId;
		public String ActUserId;
		public int CommentType;
	}
	
	
	/**
	 * 广场-发现详情添加评论
	 * @return
	 */
	public static SquareFoundDetaiCommentAddRequest squareFoundDetaiCommentAddRequest() {
		return new SquareFoundDetaiCommentAddRequest();
	}
	public static class SquareFoundDetaiCommentAddRequest extends SquareRequest{
		public ArticleComment articleComment;
	}
	public static ArticleComment articleComment(){
		return new ArticleComment();
	}
	
	public static class ArticleComment{
		public long ArticleId;//2222
		public long ParentId;//0
		public String Content;//ssss
        public long CommentType;//0
        public String UserNickName;//sss
        public String UserAvatar;//sssss"
        public long AtUserId;
	}
	
	
	
	/**
	 * 广场-发现详情——置顶、删除
	 * @return
	 */
	public static SquareFoundDetaiSetArticleAttrRequest squareFoundDetaiSetArticleAttrRequest() {
		return new SquareFoundDetaiSetArticleAttrRequest();
	}
	public static class SquareFoundDetaiSetArticleAttrRequest extends SquareRequest{
		public long articleId;
		public String attr;// IsHot //置顶，State //删除 
		public int value;//值为1
	}
	
	
	//{events:{RealName:"hello",Tel:"13334324234",ArticleId:30,Bd_UserId:"asdfasdf",Bd_ChannelId:"asdfasdf324",DeviceType :1/2}}
	/**
	 * 广场-福利——参加活动
	 * @return
	 */
	public static SquareWelfareDetailJoinRequest squareWelfareDetailJoinRequest() {
		return new SquareWelfareDetailJoinRequest();
	}
	public static class SquareWelfareDetailJoinRequest extends SquareRequest{
		public Events events;
	}
	public static Events events(){
		return new Events();
	}
	
	public static class Events{
		public String RealName;//hello",
		public String Tel;//13334324234",
		public long ArticleId;//30,
		public String Bd_UserId;//asdfasdf",
		public String Bd_ChannelId;//asdfasdf324",
		public long DeviceType;//1/android    2/IOS
		public String Address;
	}
	
	
	/**
	 * 广场-直播--发布消息
	 */
	public static SquareLiveChatSaveRequest squareLiveChatSaveRequest() {
		return new SquareLiveChatSaveRequest();
	}
	public static class SquareLiveChatSaveRequest extends SquareRequest{
//		public LiveChatSave liveChatSave;
		public ArrayList<LiveAnnex> liveAnnexList;
		public SquareLiveChatModel liveChat;
		
	}
	
	public static LiveChatSave liveChatSave(){
		return new LiveChatSave();
	}
	public static class LiveChatSave{
		public SquareLiveChatModel liveChat;
		public ArrayList<LiveAnnex> liveAnnexList;
	}
	
}
