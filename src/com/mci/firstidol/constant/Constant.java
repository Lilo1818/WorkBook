package com.mci.firstidol.constant;

import java.security.PublicKey;
import java.util.List;

import com.mci.firstidol.model.PhotoModel;
import com.mci.firstidol.model.StarModel;
import com.mci.firstidol.model.StarPhotoModel;

public class Constant {

	// 初始化http 请求方式
	public static final String Request_POST = "post";
	public static final String Request_GET = "get";
	// 参数初始化
	public static final int pageNum = 20;
	public static final int pageNum10 = 10;

	public static boolean hasClickStar = false;// 是否加载过

	public static StarModel starModel;// 明星

	public static StarPhotoModel starPhotoModel;// 照片集

	public static boolean is_selectedChanged = false;// 追星数据是否更新
	
	public static boolean is_userinfo_change = false;//用户信息改变
	
	public static boolean is_selected_star = false;//是否选择用户了
	
	public static boolean isComment = false;//是否更新了

	public class RequestContstants {

		
		public static final String URL_IP = "http://sns.ieasy.tv";
		//public static final String URL_IP = "http://app.ieasy.tv:8158";
		// --------公共服务--------------
		public static final String URL = URL_IP + "/api/";

		// ****************用户登录、注册**************************
		public static final String Request_User_Login = "user/login";// 登陆
																		// 验证用户信息
		public static final String Request_User_Social_Login = "user/social/login";// 第三方社交账号登录
																					// 信息接口
		public static final String Request_User_Register = "user/Reg";// 注册用户信息接口
		public static final String Request_User_Update = "user/Update";// 修改用户信息
		public static final String Request_User_ValidateCode = "user/validatecode/%1$s";// 注册获取验证码,user/validatecode/{mobile}
		public static final String Request_User_RetrievePasswordCode = "user/retrievepassword/%1$s";// 用户找回密码验证码获取,user/retrievepassword/{userName}
		public static final String Request_User_UpdatePassword = "user/updatepassword";// 设置新密码
		
		
		public static final String Request_Start_Splash = "splash/splash";

		// ****************广场***********************
		// 广场--发现
		public static final String Request_Square_List = "article/squarelist/%1$d/%2$d/%3$d/%4$s";// {CHANNELID}/{PAGEINDEX}/{PAGESIZE}/{MAXDATE}
		// 广场--发现--首页推荐列表
		public static final String Request_Square_recommendUrl = "article/recommendlist/%1$s";
		// 广场--发现详情
		public static final String Request_Square_FoundDetail = "article/article/%1$s";// article/article/{ID}
		/***
		 * 文章评价 articleId: 文章 ID parentId:父级编号，如果添加二级评论，parentId 则为父级评论编号；一级评论
		 * parentid 为0 MaxId : 最大 ID, 获取最新时 maxid=0,获取更多数据时请填写最大的
		 * maxid=commentId， Count: 获取条数 commentType:0 文章评论，1：文章图集评论
		 */
		public static final String Request_Article_Comment = "articlecomment/list/%1$s/%2$s/%3$s/%4$s/%5$s";// {ARTICLEID}/{PARENTID}/{MAXID}/{COUNT}/{

		// 收藏
		public static final String Request_Article_Store = "action/article/store/%1$s";// 收藏，{articleId}
		// 取消收藏
		public static final String Request_Article_CancelStore = "action/article/cancelstore/%1$s";// 取消收藏，{articleId}
		// 文章点赞 articleId 文章Id actName 1, 2, 3, 4 代表四种赞
		public static final String Request_Square_Zan = "action/article/like/%1$s/%2$s";// {articleId}/{
																						// actName
																						// }
		
		//文章取消点赞
		public static final String Request_Square_DeleZan = "action/article/cancellike/%1$s/%2$s";//{articleId}/{
																								  // actName
																								  // }
		//检查版本更新
		public static final String Request_Version = URL_IP +"/api/appversion/version";
		
		// 文章评论添加接口
		public static final String Request_Square_ArticleComment_Add = "articlecomment/add";//
		// 评论点赞
		public static final String Request_Square_CommentActions_Add = "commentactions/add";//
		// 2.8 文章设置为热议（置顶）和删除
		public static final String Request_Square_SetArticleAttr = "article/setarticleattr";//

		// 30.6 直播评论接口（翻页）
		public static final String Request_LiveChat_LiveChildrenList = "livechat/livechildrenlist/%1$s/%2$s/%3$s";// {articleId}/{parentId}/{minChatId}
		// 30.7 直播评论获取最新数据（下拉刷新）
		public static final String Request_LiveChat_UnreadLiveChildren = "livechat/unreadlivechildren/%1$s/%2$s/%3$s";// {articleId}/{parentId}/{maxChatId}

		// 查看结果
		public static final String Request_Square_Events_ResultInfo = "events/resultinfo/%1$s";// {articleid}
		// 参加活动
		public static final String Request_Square_Events_Join = "events/join";//

		// 广场--直播
		public static final String Request_Live_List = "article/livelist/%1$s/%2$s/%3$s/%4$s";// 47/1/20/00

		// 广场--直播--直播详情：直播列表(下拉刷新——未读)
		public static final String Request_Live_detail_live_down_List = "livechat/unreadlive/%1$s/%2$s";// ArticleId/maxChatId
		// 广场--直播--直播详情：直播列表(翻页)
		public static final String Request_Live_detail_live_up_List = "livechat/livelist/%1$s/%2$s/%3$s";// {pageSize}/{ArticleId}/minChatId
		// 广场--直播--直播详情：聊天列表(下拉刷新——未读)
		public static final String Request_Live_detail_chat_down_List = "livechat/unread/%1$s/%2$s";// {articleId}/{chatId}
		// 广场--直播--直播详情：直播列表(翻页)
		public static final String Request_Live_detail_chat_up_List = "livechat/list/%1$s/%2$s/%3$s";// {pageSize}/{articleId}/{minChatId}

		// 广场--福利
		public static final String Request_Welfare_List = "article/list/%1$s/%2$s/%3$s/%4$s";// 43/1/20/00

		// 1、中国 2、韩国 3、日本 4、其他
		public static final String Request_Order_Hotlist = "star/hotlist/%1$s/%2$s";// 1/20
		// 广场--明星排行榜
		public static final String Request_Star_Order_List = "star/ranking/%1$s/%2$s";// 1/20
		// 广场--粉丝排行榜
		public static final String Request_Fan_Order_List = "user/ranking/%1$s/%2$s";// 1/20

		// 获取用户在聊天室的角色
		public static final String Request_LiveChat_UserRole = "livechat/liveuserrole/%1$s/%2$s";// {articleId}/{userId}
		// 获取七牛 token
		public static final String Request_QiNiu_Token = "qiniu/token/%1$s";// {annexType
																			// }
		// 提交聊天消息
		public static final String Request_LiveChat_Save = "livechat/save";//
		//30.6	更新聊天记录的支持数（点赞）
		public static final String Request_LiveChat_Up = "livechat/uplivechat";//
		//31.5	删除聊天信息
		public static final String Request_LiveChat_Delete = "livechat/delete";//
		//消息置顶
		public static final String Request_LiveChat_Top = "livechat/toplivechat";
		//取消置顶
		public static final String Request_LiveChat_CancelTop = "livechat/canceltoplivechat";
		
		//删除评论
		public static final String Request_ArticleComment_Delete = "articlecomment/delete";
		
		//分享
		public static final String Request_Article_Share = "easyarticledetail.html?id=%1$s";
		
		//28.7	判断是否已经关注明星
		public static final String Request_Order_StarIsFollow = "star/isfollow/%1$s";//{starid}
		//28.5	关注明星
		public static final String Request_Order_StarFollow = "star/follow/%1$s";//{starid}
		//28.8	 置顶明星
		public static final String Request_Order_StarFollowTop = "star/follow/top";
		
		// **********商城********************
		public static final String Request_shopping_List = "article/list/";

		// 追星粉丝说
		public static final String Request_staring_List = "article/starchaserlist/";
		
		//追星热议
		public static final String Request_starhot_list = "article/hotlist/";
		
		//观察团
		public static final String Request_starsee_list = "article/starpublishlist/";

		// 明星行程
		public static final String Request_triping_list = "star/triplist";

		// 获取版主列表
		public static final String Request_master_list = "userrole/moderatorlist";

		// 申请版主列表
		public static final String Request_apply_master = "userrole/moderator/add";
		
		//删除通知
		public static final String Request_delete = "pushmessage/delete";
		
		// 添加明星行程
		public static final String Request_add_trip = "star/trip/add";

		// 得到我关注的明星
		public static final String Request_myStar_list = "star/followedStar";

		// 关注明星
		public static final String Request_follow_star = URL + "star/follow";

		// 取消关注的明星
		public static final String Request_cancle_followStar = URL
				+ "star/cancelfollow";

		// 得到热门的明星
		public static final String Request_hotStar_list = "star/hotlist";

		// 得到所有的明星
		public static final String Request_allStar_list = "star/alllist";

		// 得到我的收藏
		public static final String Request_myCoolection_list = "article/storelist";

		// 得到我的通知
		public static final String Request_myNotice_list = "pushmessage/list";

		// 得到我的评论
		public static final String Request_myComment_list = "articlecomment/mylist";

		// 得到我的动态
		public static final String Request_dynamic_list = "article/userdynamic";

		// 我的关注
		public static final String Request_follow_list = "userfollow/userfollow";

		// 我的粉丝
		public static final String Request_fans_list = "userfollow/userfans";
		
		//上传吐槽
		public static final String Request_upload_tucao ="feedback/add";
		
		//获取用户信息
		public static final String Request_userinfo = URL + "user/userinfo";

		// 关注普通用户
		public static final String Request_user_follow = URL
				+ "userfollow/follow";

		// 取消关注普通用户
		public static final String Request_user_cancleFollow = URL
				+ "userfollow/cancelfollow";
		
		//判断是非已经关注用户
		public static final String Request_user_isfollow = URL+"userfollow/isfollow";
		
		//添加文章
		public static final String Request_article_add ="article/add";
		
		//是否是版主
		public static final String Request_isMaster = URL + "userrole/ismoderator/";
		
		//上传头像
		public static final String Request_uploadFile ="http://newipsitem.createapp.cn/pic/uploadimage.ashx";
		
		//上传资料信息
		public static final String Request_upload_info = "user/update";
		
		//得到文章详情
		public static final String Request_get_article = URL + "article/article";
		
		//添加行程提醒
		public static final String Request_trip_notice = "star/tripremind/add";
		
		//删除行程提醒
		public static final String Request_trip_notice_delete = "star/tripremind/delete";
		
		//照片评论
		public static final String Request_photo_comment ="articlecomment/list";
		
		
		//举报接口
		public static final String Report = "reportactions/add";
		
		
		//删除我的通知接口
		public static final String Delete = "http://newips.createapp.cn/api/pushmessage/delete";
		

	}

	public class ChannelIDMap {
		public static final String Shopping_channelId = "44";// 商店的channelID
		public static final String Staring_channelId = "49";// 追星的channelId
	}

	public class IntentKey {
		public static final String H5_CONTENT = "H5_CONTENT";
		public static final String typeCode = "typeCode";

		public static final String isForgotPwd = "isForgotPwd";
		public static final String upCount = "upCount";
		public static final String viewCount = "viewCount";
		public static final String hasUp = "hasUp";

		public static final String star_video = "star_video";

		public static final String isSearch = "IsSearch";
		public static final String articleID = "ArticleID";
		public static final String time_str = "time_str";
		public static final String squareLiveModel = "SquareLiveModel";
		public static final String squareLiveChatModel = "SquareLiveChatModel";
		public static final String eventsResult = "EventsResult";
		public static final String county_type = "county_type";
		public static final String userRole = "UserRole";
		public static final String from = "From";
		public static final String chatID = "ChatID";
		public static final String userID = "UserID";
		public static final String tip = "tip";
		public static final String starModel = "starModel";
		public static final String star_id = "star_id";
		public static final String starphotoModel = "starPhotoModel";
		public static final String IMAGE_POSITION = "image_position";
		public static final String customContent = "CustomContentString";
	}

	public class IntentValue {
		public static final String ACTIVITY_STAR = "activity_star";// 追星
		public static final String ACTIVITY_COLLECTION = "activity_collection";// 收藏
		public static final String ACTIVITY_COMMENT = "activity_comment";// 评论
		public static final String ACTIVITY_NOTICE = "activity_notice";// 通知
		public static final String ACTIVITY_PERSONAL = "activity_personal";// 我的
		
		public static final String ACTIVITY_DYNAMIC = "activity_dynamic";//动态
		public static final String ACTIVITY_FOLLOW = "activity_follow";//关注
		public static final String ACTIITY_FOLLOWER = "activity_follower";//粉丝
		
		public static final String ACTIVITY_FANS = "fans";//粉丝说
		public static final String ACTIVITY_HOT = "hot";//热议
		public static final String ACTIVITY_SEE = "see";//观察团
		public static final String ACTIVITY_SHOPPING = "activity_shopping";//商品详情
	}

	public class Config {
		public static final String LOGIN = "LOGIN";// 登录
		public static final String LOGINOUT = "LOGIN_OUT";// 登出
		public static final String UPDATE_STAR = "UPDATE_STAR";// 更新明星
		public static final String UPDATE_FOLLOW = "UPDATE_FOLLOW";//更新关注
		public static final String UPDATE_STAR_INFO = "UPDATE_STAR_INFO";// 更新明星
		public static final String UPDATE_STAR_ERROR = "UPDATE_STAR_ERROR";// 更新明星出错
		
		public static final String UPDATE_INFO = "update_info";//个人信息
		
		public static final String UPDATE_TIP = "update_tip";//标签信息
		public static final String UPDATE_COMMENT_INFO = "update_comment_info";//更新评论数据
		
		public static final String SELECTED_STAR = "selected_star";//选中明星
	}

	public class ConfigValue {
		public static final int NOSTARDATA = 2;// 明星数据为空
		public static final int STARDATAERROR = 1;// 获取明星数据出错
		
		public static final int FOLLOW_ADD = 1;//关注增加
		public static final int FOLLOW_SUB = 2;//关注减少
	}

}
