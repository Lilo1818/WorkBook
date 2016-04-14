package com.mci.firstidol.model;

import java.util.ArrayList;
import java.util.List;

public class StarIndexModel{
	
	public long ArticleId;
	
	public long UserId;
	
	public String UserAvatar;//发布者头像
	
	public String UserNickName;//昵称
	
	public String PublishDate;//发布时间
	
	public String Ico;//封面图片
	
	public String Content;//内容
	
	public String Title;//标题
	
	public long ViewCount;//查阅的次数
	
	public int CommentCount;//评论的次数
	
	public long UpCount;//赞的次数
	
	public long UpCount1;
	
	public long UpCount2;
	
	public long UpCount3;
	
	public long UpCount4;
	
	public long IcoHeight;// 356,
	
	public long IcoWidth;// 640,
	
	public boolean HasStore;// false,
	
	public List<HotComment> HotComments;
	

}
