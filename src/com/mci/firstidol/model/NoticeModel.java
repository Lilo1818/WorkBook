package com.mci.firstidol.model;

public class NoticeModel {

	public String ActId; //0:系统通知,1:点赞 2:评论 3:删除
	
	//public long ArticleId;
	
	public String ActName;
	
	public String CreateDate;//创建时间
	
	public String FromUserId;//发送消息的用户Id
	
	public String Ico;//图片
	
	public String IcoHeight;//图片高
	
	public String IcoWidth;//图片宽
	
	public String Id;//设备id
	
	public String Message;//消息内容
	
	public String ModifyDate;//显示的时间 
	
	public long RefId;//文章Id 
	
	public String ToUserId;//当前用户Id
	
	public String UserId;
	
	public String StarId;
	
	public int Type;//0为普通无关联，1为关联文章
	
	public int ChannelId;//频道
	
	public int ModelType;//类型
	
	public String FromUserAvatar;//用户头像
	
	public String FromUserNickName;//昵称
	
	public String PublishDate;
	
}
