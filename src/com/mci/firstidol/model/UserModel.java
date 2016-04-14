package com.mci.firstidol.model;

import java.io.Serializable;

public class UserModel implements Serializable {

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	
	public long UserId;//用户编号	
	public String UserName;//用户名	
	public String NickName;//昵称	
	public String Avatar;//头像地址	
	public int Sex;//0:为止  , 1 男，2  女	
	public String UDID;//设备 ID--------	
	public String Email;//Email	
	public String CreateDate;//创建时间	
	public int IsVip;//是否为 VIP 	0 不是/1 是	
	public String VipEndDate;//Vip 过期时间	
	public String RealName;//用户姓名	--------
	public String Tel;//用户电话	        -------
	public String Address;//用户地址	
	public int ValidDays;//VIP 有效天数	
	public String Token;//登录成功后服务器返回的 token	
	
	public int Points;//积分
	public int Experience;
	public int Level;
	public String BornDate;//生日
	
	public int UserFollowCount;//关注的用户数
	public int FansCount;//粉丝数
	public int DynamicCount;//动态数
	
	public String BgImage;//背景图
	
	public String Bd_ChannelId;
	public String Bd_UserId;
	public int IsStar;
	public long MagazineId;
	public String Password;
	public long ProgramId;
	public String QQ;
	public String Signature;
	public String Tags;
	public String LoginDate;
	
	
	public boolean Isfollow;//是否关注

}
