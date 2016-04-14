package com.mci.firstidol.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.mci.firstidol.utils.GsonUtils;

/**
 * 广场模块
 * 
 * @author Shinsoft
 *
 */
public class SquareModel {

	/**
	 * 七牛 token
	 */
	public class QiNiuTokenModel {
		private String UpToken;//
		private String VideoCover;// ": "movie/201511/1448445856244_586.jpg",
		private String WaterMarkVideo;// video/201511/1448445856244_586.mp4"，
		private String AudioName;
		public String getUpToken() {
			return UpToken;
		}
		public void setUpToken(String upToken) {
			UpToken = upToken;
		}
		public String getVideoCover() {
			return VideoCover;
		}
		public void setVideoCover(String videoCover) {
			VideoCover = videoCover;
		}
		public String getWaterMarkVideo() {
			return WaterMarkVideo;
		}
		public void setWaterMarkVideo(String waterMarkVideo) {
			WaterMarkVideo = waterMarkVideo;
		}
		public String getAudioName() {
			return AudioName;
		}
		public void setAudioName(String audioName) {
			AudioName = audioName;
		}

	}

	/**
	 * 直播详情--直播-聊天
	 * 
	 * @author Shinsoft
	 *
	 */
	public class SquareLiveChatModel implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private long ArticleId;// 15058,
		private String ChatContent;// -",
		private long ChatId;// 164735,
		private String CreateDate;// /Date(1452617968000)/",
		private long DownCount;// 0,
		private String IP;// 192.168.1.1",
		private long IsDeleted;// 0,
		private long IsTop;// 0,
		private long ParentId;// 0,
		private long ReplyCount;// 0,
		private long UpCount;// 1,
		private String UserAvatar;// http://qn.ieasy.tv/image/201601/1452244959112_300.png",
		private long UserId;// 56533,
		private String UserNickName;// Simple、",
		private long UserRole;// 0,
		private ArrayList<LiveAnnex> LiveAnnexList;
		private String ReferenceChat;
		private SquareLiveChatModel ParentLiveChat;
		private long AtUserId;
		private long CommentId;
		
		private boolean isComment;

		
		
		public long getCommentId() {
			return CommentId;
		}

		public void setCommentId(long commentId) {
			CommentId = commentId;
		}

		public long getAtUserId() {
			return AtUserId;
		}

		public void setAtUserId(long atUserId) {
			AtUserId = atUserId;
		}

		public boolean isComment() {
			return isComment;
		}

		public void setComment(boolean isComment) {
			this.isComment = isComment;
		}

		public String getReferenceChat() {
			return ReferenceChat;
		}

		public void setReferenceChat(String referenceChat) {
			ReferenceChat = referenceChat;
		}

		public SquareLiveChatModel getParentLiveChat() {
			if (!TextUtils.isEmpty(ReferenceChat)) {
				if (ParentLiveChat == null) {
					ParentLiveChat = (SquareLiveChatModel) GsonUtils
							.jsonToBean(ReferenceChat,
									SquareLiveChatModel.class);
				}
			}
			return ParentLiveChat;
		}

		
		public long getUserId() {
			return UserId;
		}

		public void setUserId(long userId) {
			UserId = userId;
		}

		public void setParentLiveChat(SquareLiveChatModel parentLiveChat) {
			ParentLiveChat = parentLiveChat;
		}

		public long getArticleId() {
			return ArticleId;
		}

		public void setArticleId(long articleId) {
			ArticleId = articleId;
		}

		public String getChatContent() {
			return ChatContent;
		}

		public void setChatContent(String chatContent) {
			ChatContent = chatContent;
		}

		public long getChatId() {
			return ChatId;
		}

		public void setChatId(long chatId) {
			ChatId = chatId;
		}

		public String getCreateDate() {
			return CreateDate;
		}

		public void setCreateDate(String createDate) {
			CreateDate = createDate;
		}

		public long getDownCount() {
			return DownCount;
		}

		public void setDownCount(long downCount) {
			DownCount = downCount;
		}

		public String getIP() {
			return IP;
		}

		public void setIP(String iP) {
			IP = iP;
		}

		public long getIsDeleted() {
			return IsDeleted;
		}

		public void setIsDeleted(long isDeleted) {
			IsDeleted = isDeleted;
		}

		public long getIsTop() {
			return IsTop;
		}

		public void setIsTop(long isTop) {
			IsTop = isTop;
		}

		public long getParentId() {
			return ParentId;
		}

		public void setParentId(long parentId) {
			ParentId = parentId;
		}

		public long getReplyCount() {
			return ReplyCount;
		}

		public void setReplyCount(long replyCount) {
			ReplyCount = replyCount;
		}

		public long getUpCount() {
			return UpCount;
		}

		public void setUpCount(long upCount) {
			UpCount = upCount;
		}

		public String getUserAvatar() {
			return UserAvatar;
		}

		public void setUserAvatar(String userAvatar) {
			UserAvatar = userAvatar;
		}


		public String getUserNickName() {
			return UserNickName;
		}

		public void setUserNickName(String userNickName) {
			UserNickName = userNickName;
		}

		public long getUserRole() {
			return UserRole;
		}

		public void setUserRole(long userRole) {
			UserRole = userRole;
		}

		public ArrayList<LiveAnnex> getLiveAnnexList() {
			return LiveAnnexList;
		}

		public void setLiveAnnexList(ArrayList<LiveAnnex> liveAnnexList) {
			LiveAnnexList = liveAnnexList;
		}

	}

	public class LiveAnnex implements Serializable {

		public static final String TYPE_AUDIO = "audio";
		public static final String TYPE_VIDEO = "video";
		public static final String TYPE_IMAGE = "image";

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private long AnnexId;// 2224,
		private long AnnexType;// 1, // 1:图片 2:音频 3:视频
		private String AnnexUrl;// http://qn.ieasy.tv/image/201512/1451375079408_420.png?watermark%2f1%2fimage%2faHR0cDovL3FuLmllYXN5LnR2L2ltYWdlLzIwMTUwNC8xNDI3OTQ4MjgxNzQyXzc3MS5wbmc_aW1hZ2VNb2dyMi9hdXRvLW9yaWVudC90aHVtYm5haWwvMzc0%7cimageView2%2f0%2fw%2f1600%2fformat%2fjpg%2finterlace%2f1%2fq%2f50",
		private long ChatId;// 164625,
		private String CreateDate;// /Date(1451403887000)/",
		private long CreateUser;// 56532,
		private String FileName;
		private long FileSize;// 752754,
		private String FileType;// png",
		private long OrigHeight;// 498,
		private long OrigWidth;

		private String Duration;// 13〃",
		private String OrigVideoUrl;// http://qn.ieasy.tv/video/201601/1452145957322_307.mp4",
		private String PersistentId;// z0.568dfd257823de14f7808758",
		private String VideoCover;//
		private String AudioUrlExt;

		
		public String getAudioUrlExt() {
			return AudioUrlExt;
		}

		public void setAudioUrlExt(String audioUrlExt) {
			AudioUrlExt = audioUrlExt;
		}

		public String getFileName() {
			return FileName;
		}

		public void setFileName(String fileName) {
			FileName = fileName;
		}

		public String getDuration() {
			return Duration;
		}

		public void setDuration(String duration) {
			Duration = duration;
		}

		public String getOrigVideoUrl() {
			return OrigVideoUrl;
		}

		public void setOrigVideoUrl(String origVideoUrl) {
			OrigVideoUrl = origVideoUrl;
		}

		public String getPersistentId() {
			return PersistentId;
		}

		public void setPersistentId(String persistentId) {
			PersistentId = persistentId;
		}

		public String getVideoCover() {
			return VideoCover;
		}

		public void setVideoCover(String videoCover) {
			VideoCover = videoCover;
		}

		public long getAnnexId() {
			return AnnexId;
		}

		public void setAnnexId(long annexId) {
			AnnexId = annexId;
		}

		public long getAnnexType() {
			return AnnexType;
		}

		public void setAnnexType(long annexType) {
			AnnexType = annexType;
		}

		public String getAnnexUrl() {
			return AnnexUrl;
		}

		public void setAnnexUrl(String annexUrl) {
			AnnexUrl = annexUrl;
		}

		public long getChatId() {
			return ChatId;
		}

		public void setChatId(long chatId) {
			ChatId = chatId;
		}

		public String getCreateDate() {
			return CreateDate;
		}

		public void setCreateDate(String createDate) {
			CreateDate = createDate;
		}

		public long getCreateUser() {
			return CreateUser;
		}

		public void setCreateUser(long createUser) {
			CreateUser = createUser;
		}

		public long getFileSize() {
			return FileSize;
		}

		public void setFileSize(long fileSize) {
			FileSize = fileSize;
		}

		public String getFileType() {
			return FileType;
		}

		public void setFileType(String fileType) {
			FileType = fileType;
		}

		public long getOrigHeight() {
			return OrigHeight;
		}

		public void setOrigHeight(long origHeight) {
			OrigHeight = origHeight;
		}

		public long getOrigWidth() {
			return OrigWidth;
		}

		public void setOrigWidth(long origWidth) {
			OrigWidth = origWidth;
		}

	}

	/**
	 * 粉丝
	 * 
	 * @author Shinsoft
	 *
	 */
	public class SquareListFanModel {
		private String Address;// "",
		private String Avatar;// null,
		private String Bd_ChannelId;// null,
		private String Bd_UserId;// null,
		private String BornDate;// null,
		private String CreateDate;// "/Date(1409581656000)/",
		private long DynamicCount;// 0,
		private String Email;// null,
		private long Experience;// 8,
		private long FansCount;// 0,
		private String InfoExtent;// null,
		private long IsGag;// 0,
		private long IsVip;// 0,
		private long Level;// 1,
		private long MagazineId;// 37,
		private String NickName;// "6666",
		private String Password;// "e10adc3949ba59abbe56e057f20f883e",
		private long Points;// 0,
		private long ProgramId;// 8,
		private String RealName;// "",
		private String Sex;// null,
		private String Signature;// "",
		private String Tags;// null,
		private String Tel;// null,
		private String UDID;// null,
		private long UserFollowCount;// 0,
		private long UserId;// 269,
		private String UserName;// "654@qq.com",
		private String VipEndDate;// "/Date(1409581656000)/"

		public String getAddress() {
			return Address;
		}

		public void setAddress(String address) {
			Address = address;
		}

		public String getAvatar() {
			return Avatar;
		}

		public void setAvatar(String avatar) {
			Avatar = avatar;
		}

		public String getBd_ChannelId() {
			return Bd_ChannelId;
		}

		public void setBd_ChannelId(String bd_ChannelId) {
			Bd_ChannelId = bd_ChannelId;
		}

		public String getBd_UserId() {
			return Bd_UserId;
		}

		public void setBd_UserId(String bd_UserId) {
			Bd_UserId = bd_UserId;
		}

		public String getBornDate() {
			return BornDate;
		}

		public void setBornDate(String bornDate) {
			BornDate = bornDate;
		}

		public String getCreateDate() {
			return CreateDate;
		}

		public void setCreateDate(String createDate) {
			CreateDate = createDate;
		}

		public long getDynamicCount() {
			return DynamicCount;
		}

		public void setDynamicCount(long dynamicCount) {
			DynamicCount = dynamicCount;
		}

		public String getEmail() {
			return Email;
		}

		public void setEmail(String email) {
			Email = email;
		}

		public long getExperience() {
			return Experience;
		}

		public void setExperience(long experience) {
			Experience = experience;
		}

		public long getFansCount() {
			return FansCount;
		}

		public void setFansCount(long fansCount) {
			FansCount = fansCount;
		}

		public String getInfoExtent() {
			return InfoExtent;
		}

		public void setInfoExtent(String infoExtent) {
			InfoExtent = infoExtent;
		}

		public long getIsGag() {
			return IsGag;
		}

		public void setIsGag(long isGag) {
			IsGag = isGag;
		}

		public long getIsVip() {
			return IsVip;
		}

		public void setIsVip(long isVip) {
			IsVip = isVip;
		}

		public long getLevel() {
			return Level;
		}

		public void setLevel(long level) {
			Level = level;
		}

		public long getMagazineId() {
			return MagazineId;
		}

		public void setMagazineId(long magazineId) {
			MagazineId = magazineId;
		}

		public String getNickName() {
			return NickName;
		}

		public void setNickName(String nickName) {
			NickName = nickName;
		}

		public String getPassword() {
			return Password;
		}

		public void setPassword(String password) {
			Password = password;
		}

		public long getPoints() {
			return Points;
		}

		public void setPoints(long points) {
			Points = points;
		}

		public long getProgramId() {
			return ProgramId;
		}

		public void setProgramId(long programId) {
			ProgramId = programId;
		}

		public String getRealName() {
			return RealName;
		}

		public void setRealName(String realName) {
			RealName = realName;
		}

		public String getSex() {
			return Sex;
		}

		public void setSex(String sex) {
			Sex = sex;
		}

		public String getSignature() {
			return Signature;
		}

		public void setSignature(String signature) {
			Signature = signature;
		}

		public String getTags() {
			return Tags;
		}

		public void setTags(String tags) {
			Tags = tags;
		}

		public String getTel() {
			return Tel;
		}

		public void setTel(String tel) {
			Tel = tel;
		}

		public String getUDID() {
			return UDID;
		}

		public void setUDID(String uDID) {
			UDID = uDID;
		}

		public long getUserFollowCount() {
			return UserFollowCount;
		}

		public void setUserFollowCount(long userFollowCount) {
			UserFollowCount = userFollowCount;
		}

		public long getUserId() {
			return UserId;
		}

		public void setUserId(long userId) {
			UserId = userId;
		}

		public String getUserName() {
			return UserName;
		}

		public void setUserName(String userName) {
			UserName = userName;
		}

		public String getVipEndDate() {
			return VipEndDate;
		}

		public void setVipEndDate(String vipEndDate) {
			VipEndDate = vipEndDate;
		}

	}

	public class SquareListStarModel {
		private String Avatar;// http://itemcdn.ieasy.tv/pic/attached/30/20151115/20151115124826_1467-500_750.jpg",
		private String BgImage;// "http://qn.ieasy.tv/image/201512/1450592458482_45.jpg",
		private String CreateDate;// /Date(1448011523000)/",
		private String Description;// "",
		private long FollowCount;// 1,
		private long IsCheckIn;// 1
		private long MagazineId;// 37,
		private long Points;// 1,
		private long StarId;// 8,
		private String StarName;// "王鸥",
		private long Type;// 1,
		private long Late7DayPoints;// 1

		public String getAvatar() {
			return Avatar;
		}

		public void setAvatar(String avatar) {
			Avatar = avatar;
		}

		public String getCreateDate() {
			return CreateDate;
		}

		public void setCreateDate(String createDate) {
			CreateDate = createDate;
		}

		public String getDescription() {
			return Description;
		}

		public void setDescription(String description) {
			Description = description;
		}

		public long getFollowCount() {
			return FollowCount;
		}

		public void setFollowCount(long followCount) {
			FollowCount = followCount;
		}

		public long getMagazineId() {
			return MagazineId;
		}

		public void setMagazineId(long magazineId) {
			MagazineId = magazineId;
		}

		public long getPoints() {
			return Points;
		}

		public void setPoints(long points) {
			Points = points;
		}

		public long getStarId() {
			return StarId;
		}

		public void setStarId(long starId) {
			StarId = starId;
		}

		public String getStarName() {
			return StarName;
		}

		public void setStarName(String starName) {
			StarName = starName;
		}

		public long getType() {
			return Type;
		}

		public void setType(long type) {
			Type = type;
		}

		public long getLate7DayPoints() {
			return Late7DayPoints;
		}

		public void setLate7DayPoints(long late7DayPoints) {
			Late7DayPoints = late7DayPoints;
		}

		public String getBgImage() {
			return BgImage;
		}

		public void setBgImage(String bgImage) {
			BgImage = bgImage;
		}

		public long getIsCheckIn() {
			return IsCheckIn;
		}

		public void setIsCheckIn(long isCheckIn) {
			IsCheckIn = isCheckIn;
		}

	}

	/**
	 * 广场---直播和商城
	 * 
	 * @author Shinsoft
	 *
	 */
	public class SquareLiveModel1 implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private long AdminId;// 30,
		private long ArticleId;// 15037,
		private long ArticleType;// 0,
		private String Author;// s,
		private long ChannelId;// 49,
		private long ChatPeopleCount;// 0,
		private long CommentCount;// 8,
		private String Content;// <
		private String CreateDate;// /Date(1445854761000)/,
		private String EventsEnd;// /Date(1445854773000)/,
		private String EventsStart;// /Date(1445854773000)/,
		private String Flags;// c,l,
		private long ForwardCount;// 0,
		private String GroupPic;// ,
		private String HTitle;// ,
		private String Ico;// http://itemcdn.ieasy.tv/pic/attached/30/20151026/20151026101846_8500-640_356.jpg,
		private String Ico2;// http://qn.ieasy.tv/image/201510/1445826148367_669.jpg,
		private String Ico3;// http://itemcdn.ieasy.tv/pic/attached/30/20151026/20151026101846_8500-640_356.jpg,
		private String IcoEx2;// ,
		private String IcoEx3;// ,
		private long IcoHeight;// 356,
		private long IcoHeight3;// 356,
		private long IcoWidth;// 640,
		private long IcoWidth3;// 640,
		private long Inspecial;// 0,
		private long IsHot;// 1,
		private long IsSquare;// 1,
		private long IsStarPublish;// 0,
		private long IsTop;// 0,
		private long MagazineId;// 37,
		private long ModelType;// 0,
		private long PressId;// 12,
		private String PublishDate;// /Date(1445854727000)/,
		private long ShowStyle;// 1,
		private long StarId;// 1,
		private long State;// 3,
		private String SubTitle;// 第二位预告照主人公JeA亮相，依然持续霸气造型。,
		private String Summary;// ,
		private String Tags;// ,
		private String Tips;// ,
		private String Title;// Brown Eyed Girls公开第二波预告照,
		private long Type;// 0,
		private long UpCount;// 0,
		private long UpCount2;// 0,
		private long UpCount3;// 0,
		private long UpCount4;// 0,
		private String UserAvatar;// http://newipsitem.createapp.cn/pic/attached/626/20141025/20141025180057_2278-256_256.png,
		private long UserId;// 626,
		private String UserNickName;// 恐龙的微笑,
		private String VideoIco;// http://itemcdn.ieasy.tv/pic/attached/30/20151026/20151026101846_8500-640_356.jpg,
		private long ViewCount;// 99,
		private ArrayList<Object> ArticlePictures;// [ ],
		private ArrayList<Object> ArticlePreviewPictures;// [ ],
		private ArrayList<Object> ContentPics;// [ ],
		private int EventsProgress;// 2,
		private ArrayList<Object> GroupArticles;// [ ],
		private boolean HasJoinEvents;// false,
		private boolean HasStore;// false,
		private boolean HasUp;// false,
		private boolean HasUp2;// false,
		public boolean isHasUp2() {
			return HasUp2;
		}

		public void setHasUp2(boolean hasUp2) {
			HasUp2 = hasUp2;
		}

		public boolean isHasUp3() {
			return HasUp3;
		}

		public void setHasUp3(boolean hasUp3) {
			HasUp3 = hasUp3;
		}

		public boolean isHasUp4() {
			return HasUp4;
		}

		public void setHasUp4(boolean hasUp4) {
			HasUp4 = hasUp4;
		}

		private boolean HasUp3;// false,
		private boolean HasUp4;// false,
		
		private boolean HasVoted;// false,
		private ArrayList<Object> HotComments;// [ ],
		private ArrayList<Object> LiveHostList;// [ ],
		private ArrayList<Object> RelatedArticles;// [ ],
		private ArrayList<Object> SpecialList;// [ ],
		private long TodayPrizeCount;// 0
		private String StarName;
		private long VoteId;

		public long getVoteId() {
			return VoteId;
		}

		public void setVoteId(long voteId) {
			VoteId = voteId;
		}

		public long getAdminId() {
			return AdminId;
		}

		public void setAdminId(long adminId) {
			AdminId = adminId;
		}

		public long getArticleId() {
			return ArticleId;
		}

		public void setArticleId(long articleId) {
			ArticleId = articleId;
		}

		public long getArticleType() {
			return ArticleType;
		}

		public void setArticleType(long articleType) {
			ArticleType = articleType;
		}

		public String getAuthor() {
			return Author;
		}

		public void setAuthor(String author) {
			Author = author;
		}

		public long getChannelId() {
			return ChannelId;
		}

		public void setChannelId(long channelId) {
			ChannelId = channelId;
		}

		public long getChatPeopleCount() {
			return ChatPeopleCount;
		}

		public void setChatPeopleCount(long chatPeopleCount) {
			ChatPeopleCount = chatPeopleCount;
		}

		public long getCommentCount() {
			return CommentCount;
		}

		public void setCommentCount(long commentCount) {
			CommentCount = commentCount;
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

		public String getEventsEnd() {
			return EventsEnd;
		}

		public void setEventsEnd(String eventsEnd) {
			EventsEnd = eventsEnd;
		}

		public String getEventsStart() {
			return EventsStart;
		}

		public void setEventsStart(String eventsStart) {
			EventsStart = eventsStart;
		}

		public String getFlags() {
			return Flags;
		}

		public void setFlags(String flags) {
			Flags = flags;
		}

		public long getForwardCount() {
			return ForwardCount;
		}

		public void setForwardCount(long forwardCount) {
			ForwardCount = forwardCount;
		}

		public String getGroupPic() {
			return GroupPic;
		}

		public void setGroupPic(String groupPic) {
			GroupPic = groupPic;
		}

		public String getHTitle() {
			return HTitle;
		}

		public void setHTitle(String hTitle) {
			HTitle = hTitle;
		}

		public String getIco() {
			return Ico;
		}

		public void setIco(String ico) {
			Ico = ico;
		}

		public String getIco2() {
			return Ico2;
		}

		public void setIco2(String ico2) {
			Ico2 = ico2;
		}

		public String getIco3() {
			return Ico3;
		}

		public void setIco3(String ico3) {
			Ico3 = ico3;
		}

		public String getIcoEx2() {
			return IcoEx2;
		}

		public void setIcoEx2(String icoEx2) {
			IcoEx2 = icoEx2;
		}

		public String getIcoEx3() {
			return IcoEx3;
		}

		public void setIcoEx3(String icoEx3) {
			IcoEx3 = icoEx3;
		}

		public long getIcoHeight() {
			return IcoHeight;
		}

		public void setIcoHeight(long icoHeight) {
			IcoHeight = icoHeight;
		}

		public long getIcoHeight3() {
			return IcoHeight3;
		}

		public void setIcoHeight3(long icoHeight3) {
			IcoHeight3 = icoHeight3;
		}

		public long getIcoWidth() {
			return IcoWidth;
		}

		public void setIcoWidth(long icoWidth) {
			IcoWidth = icoWidth;
		}

		public long getIcoWidth3() {
			return IcoWidth3;
		}

		public void setIcoWidth3(long icoWidth3) {
			IcoWidth3 = icoWidth3;
		}

		public long getInspecial() {
			return Inspecial;
		}

		public void setInspecial(long inspecial) {
			Inspecial = inspecial;
		}

		public long getIsHot() {
			return IsHot;
		}

		public void setIsHot(long isHot) {
			IsHot = isHot;
		}

		public long getIsSquare() {
			return IsSquare;
		}

		public void setIsSquare(long isSquare) {
			IsSquare = isSquare;
		}

		public long getIsStarPublish() {
			return IsStarPublish;
		}

		public void setIsStarPublish(long isStarPublish) {
			IsStarPublish = isStarPublish;
		}

		public long getIsTop() {
			return IsTop;
		}

		public void setIsTop(long isTop) {
			IsTop = isTop;
		}

		public long getMagazineId() {
			return MagazineId;
		}

		public void setMagazineId(long magazineId) {
			MagazineId = magazineId;
		}

		public long getModelType() {
			return ModelType;
		}

		public void setModelType(long modelType) {
			ModelType = modelType;
		}

		public long getPressId() {
			return PressId;
		}

		public void setPressId(long pressId) {
			PressId = pressId;
		}

		public String getPublishDate() {
			return PublishDate;
		}

		public void setPublishDate(String publishDate) {
			PublishDate = publishDate;
		}

		public long getShowStyle() {
			return ShowStyle;
		}

		public void setShowStyle(long showStyle) {
			ShowStyle = showStyle;
		}

		public long getStarId() {
			return StarId;
		}

		public void setStarId(long starId) {
			StarId = starId;
		}

		public long getState() {
			return State;
		}

		public void setState(long state) {
			State = state;
		}

		public String getSubTitle() {
			return SubTitle;
		}

		public void setSubTitle(String subTitle) {
			SubTitle = subTitle;
		}

		public String getSummary() {
			return Summary;
		}

		public void setSummary(String summary) {
			Summary = summary;
		}

		public String getTags() {
			return Tags;
		}

		public void setTags(String tags) {
			Tags = tags;
		}

		public String getTips() {
			return Tips;
		}

		public void setTips(String tips) {
			Tips = tips;
		}

		public String getTitle() {
			return Title;
		}

		public void setTitle(String title) {
			Title = title;
		}

		public long getType() {
			return Type;
		}

		public void setType(long type) {
			Type = type;
		}

		public long getUpCount() {
			return UpCount;
		}

		public void setUpCount(long upCount) {
			UpCount = upCount;
		}

		public long getUpCount2() {
			return UpCount2;
		}

		public void setUpCount2(long upCount2) {
			UpCount2 = upCount2;
		}

		public long getUpCount3() {
			return UpCount3;
		}

		public void setUpCount3(long upCount3) {
			UpCount3 = upCount3;
		}

		public long getUpCount4() {
			return UpCount4;
		}

		public void setUpCount4(long upCount4) {
			UpCount4 = upCount4;
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

		public String getVideoIco() {
			return VideoIco;
		}

		public void setVideoIco(String videoIco) {
			VideoIco = videoIco;
		}

		public long getViewCount() {
			return ViewCount;
		}

		public void setViewCount(long viewCount) {
			ViewCount = viewCount;
		}

		public ArrayList getArticlePictures() {
			return ArticlePictures;
		}

		public void setArticlePictures(ArrayList articlePictures) {
			ArticlePictures = articlePictures;
		}

		public ArrayList getArticlePreviewPictures() {
			return ArticlePreviewPictures;
		}

		public void setArticlePreviewPictures(ArrayList articlePreviewPictures) {
			ArticlePreviewPictures = articlePreviewPictures;
		}

		public ArrayList getContentPics() {
			return ContentPics;
		}

		public void setContentPics(ArrayList contentPics) {
			ContentPics = contentPics;
		}

		public int getEventsProgress() {
			return EventsProgress;
		}

		public void setEventsProgress(int eventsProgress) {
			EventsProgress = eventsProgress;
		}

		public ArrayList getGroupArticles() {
			return GroupArticles;
		}

		public void setGroupArticles(ArrayList groupArticles) {
			GroupArticles = groupArticles;
		}

		public boolean isHasJoinEvents() {
			return HasJoinEvents;
		}

		public void setHasJoinEvents(boolean hasJoinEvents) {
			HasJoinEvents = hasJoinEvents;
		}

		public boolean isHasStore() {
			return HasStore;
		}

		public void setHasStore(boolean hasStore) {
			HasStore = hasStore;
		}

		public boolean isHasUp() {
			return HasUp;
		}

		public void setHasUp(boolean hasUp) {
			HasUp = hasUp;
		}

		public boolean isHasVoted() {
			return HasVoted;
		}

		public void setHasVoted(boolean hasVoted) {
			HasVoted = hasVoted;
		}

		public ArrayList getHotComments() {
			return HotComments;
		}

		public void setHotComments(ArrayList hotComments) {
			HotComments = hotComments;
		}

		public ArrayList getLiveHostList() {
			return LiveHostList;
		}

		public void setLiveHostList(ArrayList liveHostList) {
			LiveHostList = liveHostList;
		}

		public ArrayList getRelatedArticles() {
			return RelatedArticles;
		}

		public void setRelatedArticles(ArrayList relatedArticles) {
			RelatedArticles = relatedArticles;
		}

		public ArrayList getSpecialList() {
			return SpecialList;
		}

		public void setSpecialList(ArrayList specialList) {
			SpecialList = specialList;
		}

		public long getTodayPrizeCount() {
			return TodayPrizeCount;
		}

		public void setTodayPrizeCount(long todayPrizeCount) {
			TodayPrizeCount = todayPrizeCount;
		}

		public String getStarName() {
			return StarName;
		}

		public void setStarName(String starName) {
			StarName = starName;
		}

	}

	public class AD {
		private long Id;

		public long getId() {
			return Id;
		}

		public void setId(long id) {
			Id = id;
		}

	}

	/**
	 * 广场---发现
	 * 
	 * @author Shinsoft
	 *
	 */
	public class SquareFoundModel implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private long AdId;// 0,
		private long AdminId;// 30,
		private long ArticleId;// 15016,
		private long ArticleType;// 0,
		private String Author;// "Johanna",
		private long ChannelId;// 49,
		private long ChatPeopleCount;// 0,
		private long CommentCount;// 10,
		private String Content;//
		private String CreateDate;// "/Date(1445623970000)/",
		private String EventsEnd;// "/Date(1445947994000)/",
		private String EventsStart;// "/Date(1445947994000)/",
		private String Flags;// "c",
		private long ForwardCount;// 0,
		private String GroupPic;// "",
		private String HTitle;// "",
		private String Ico;// "http://itemcdn.ieasy.tv/pic/attached/30/20151023/20151023181100_8349-640_356.jpg",
		private String Ico2;// "http://qn.ieasy.tv/image/201510/1445919510625_812.jpg",
		private String Ico3;// "http://itemcdn.ieasy.tv/pic/attached/30/20151023/20151023181100_8349-640_356.jpg",
		private String IcoEx2;// "http://itemcdn.ieasy.tv/pic/attached/30/20151023/20151023181106_7568-640_356.jpg",
		private String IcoEx3;// "http://itemcdn.ieasy.tv/pic/attached/30/20151023/20151023181113_2880-640_356.jpg",
		private long IcoExHeight2;// 356,
		private long IcoExHeight3;// 356,
		private long IcoExWidth2;// 640,
		private long IcoExWidth3;// 640,
		private long IcoHeight;// 356,
		private long IcoHeight3;// 356,
		private long IcoWidth;// 640,
		private long IcoWidth3;// 640,
		private long Inspecial;// 0,
		private long IsHot;// 0,
		private long IsSquare;// 1,
		private long IsStarPublish;// 0,
		private long IsTop;// 0,
		private long MagazineId;// 37,
		private long ModelType;// 0,
		private long PressId;// 12,
		private String PublishDate;// "/Date(1445944260000)/",
		private long ShowStyle;// 2,
		private long State;// 3,
		private String SubTitle;// "掌握穿衣搭配秘诀，成为人们心中的女神。",
		private String Summary;// "",
		private String Tags;// "",
		private String Tips;// "",
		private String Title;// "娃娃脸才是女神标配 Baby示范减龄穿衣术",
		private long Type;// 0,
		private long UpCount;// 3,
		private long UpCount2;// 2,
		private long UpCount3;// 0,
		private long UpCount4;// 1,
		private String UserAvatar;// "http://newipsitem.createapp.cn/pic/attached/626/20141025/20141025180057_2278-256_256.png",
		private long UserId;// 626,
		private String UserNickName;// "恐龙的微笑",
		private String VideoIco;// "http://itemcdn.ieasy.tv/pic/attached/30/20151023/20151023181100_8349-640_356.jpg",
		private long ViewCount;// 152,
		private long VoteId;// 0,
		private ArrayList<ArticlePicture> ArticlePictures;// [ ],
		private ArrayList ArticlePreviewPictures;// [ ],
		private ArrayList ContentPics;// [ ],
		private long EventsProgress;// 2,
		private ArrayList GroupArticles;// [ ],
		private boolean HasJoinEvents;// false,
		private boolean HasStore;// false,
		private boolean HasUp;// false,
		private boolean HasUp2;// false,
		private boolean HasUp3;// false,
		private boolean HasUp4;// false,
		private boolean HasVoted;// false,
		private ArrayList<HotComment> HotComments;// [￼],
		private ArrayList LiveHostList;// [ ],
		private ArrayList RelatedArticles;// [ ],
		private ArrayList SpecialList;// [ ],
		private long TodayPrizeCount;// 0
		private AD ad;

		public boolean isHasUp2() {
			return HasUp2;
		}

		public void setHasUp2(boolean hasUp2) {
			HasUp2 = hasUp2;
		}

		public boolean isHasUp3() {
			return HasUp3;
		}

		public void setHasUp3(boolean hasUp3) {
			HasUp3 = hasUp3;
		}

		public boolean isHasUp4() {
			return HasUp4;
		}

		public void setHasUp4(boolean hasUp4) {
			HasUp4 = hasUp4;
		}

		public String getTags() {
			return Tags;
		}

		public void setTags(String tags) {
			Tags = tags;
		}

		public String getTips() {
			return Tips;
		}

		public void setTips(String tips) {
			Tips = tips;
		}

		public AD getAd() {
			return ad;
		}

		public void setAd(AD ad) {
			this.ad = ad;
		}

		public long getAdId() {
			return AdId;
		}

		public void setAdId(long adId) {
			AdId = adId;
		}

		public long getAdminId() {
			return AdminId;
		}

		public void setAdminId(long adminId) {
			AdminId = adminId;
		}

		public long getArticleId() {
			return ArticleId;
		}

		public void setArticleId(long articleId) {
			ArticleId = articleId;
		}

		public long getArticleType() {
			return ArticleType;
		}

		public void setArticleType(long articleType) {
			ArticleType = articleType;
		}

		public String getAuthor() {
			return Author;
		}

		public void setAuthor(String author) {
			Author = author;
		}

		public long getChannelId() {
			return ChannelId;
		}

		public void setChannelId(long channelId) {
			ChannelId = channelId;
		}

		public long getChatPeopleCount() {
			return ChatPeopleCount;
		}

		public void setChatPeopleCount(long chatPeopleCount) {
			ChatPeopleCount = chatPeopleCount;
		}

		public long getCommentCount() {
			return CommentCount;
		}

		public void setCommentCount(long commentCount) {
			CommentCount = commentCount;
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

		public String getEventsEnd() {
			return EventsEnd;
		}

		public void setEventsEnd(String eventsEnd) {
			EventsEnd = eventsEnd;
		}

		public String getEventsStart() {
			return EventsStart;
		}

		public void setEventsStart(String eventsStart) {
			EventsStart = eventsStart;
		}

		public String getFlags() {
			return Flags;
		}

		public void setFlags(String flags) {
			Flags = flags;
		}

		public long getForwardCount() {
			return ForwardCount;
		}

		public void setForwardCount(long forwardCount) {
			ForwardCount = forwardCount;
		}

		public String getGroupPic() {
			return GroupPic;
		}

		public void setGroupPic(String groupPic) {
			GroupPic = groupPic;
		}

		public String getHTitle() {
			return HTitle;
		}

		public void setHTitle(String hTitle) {
			HTitle = hTitle;
		}

		public String getIco() {
			return Ico;
		}

		public void setIco(String ico) {
			Ico = ico;
		}

		public String getIco2() {
			return Ico2;
		}

		public void setIco2(String ico2) {
			Ico2 = ico2;
		}

		public String getIco3() {
			return Ico3;
		}

		public void setIco3(String ico3) {
			Ico3 = ico3;
		}

		public String getIcoEx2() {
			return IcoEx2;
		}

		public void setIcoEx2(String icoEx2) {
			IcoEx2 = icoEx2;
		}

		public String getIcoEx3() {
			return IcoEx3;
		}

		public void setIcoEx3(String icoEx3) {
			IcoEx3 = icoEx3;
		}

		public long getIcoExHeight2() {
			return IcoExHeight2;
		}

		public void setIcoExHeight2(long icoExHeight2) {
			IcoExHeight2 = icoExHeight2;
		}

		public long getIcoExHeight3() {
			return IcoExHeight3;
		}

		public void setIcoExHeight3(long icoExHeight3) {
			IcoExHeight3 = icoExHeight3;
		}

		public long getIcoExWidth2() {
			return IcoExWidth2;
		}

		public void setIcoExWidth2(long icoExWidth2) {
			IcoExWidth2 = icoExWidth2;
		}

		public long getIcoExWidth3() {
			return IcoExWidth3;
		}

		public void setIcoExWidth3(long icoExWidth3) {
			IcoExWidth3 = icoExWidth3;
		}

		public long getIcoHeight() {
			return IcoHeight;
		}

		public void setIcoHeight(long icoHeight) {
			IcoHeight = icoHeight;
		}

		public long getIcoHeight3() {
			return IcoHeight3;
		}

		public void setIcoHeight3(long icoHeight3) {
			IcoHeight3 = icoHeight3;
		}

		public long getIcoWidth() {
			return IcoWidth;
		}

		public void setIcoWidth(long icoWidth) {
			IcoWidth = icoWidth;
		}

		public long getIcoWidth3() {
			return IcoWidth3;
		}

		public void setIcoWidth3(long icoWidth3) {
			IcoWidth3 = icoWidth3;
		}

		public long getInspecial() {
			return Inspecial;
		}

		public void setInspecial(long inspecial) {
			Inspecial = inspecial;
		}

		public long getIsHot() {
			return IsHot;
		}

		public void setIsHot(long isHot) {
			IsHot = isHot;
		}

		public long getIsSquare() {
			return IsSquare;
		}

		public void setIsSquare(long isSquare) {
			IsSquare = isSquare;
		}

		public long getIsStarPublish() {
			return IsStarPublish;
		}

		public void setIsStarPublish(long isStarPublish) {
			IsStarPublish = isStarPublish;
		}

		public long getIsTop() {
			return IsTop;
		}

		public void setIsTop(long isTop) {
			IsTop = isTop;
		}

		public long getMagazineId() {
			return MagazineId;
		}

		public void setMagazineId(long magazineId) {
			MagazineId = magazineId;
		}

		public long getModelType() {
			return ModelType;
		}

		public void setModelType(long modelType) {
			ModelType = modelType;
		}

		public long getPressId() {
			return PressId;
		}

		public void setPressId(long pressId) {
			PressId = pressId;
		}

		public String getPublishDate() {
			return PublishDate;
		}

		public void setPublishDate(String publishDate) {
			PublishDate = publishDate;
		}

		public long getShowStyle() {
			return ShowStyle;
		}

		public void setShowStyle(long showStyle) {
			ShowStyle = showStyle;
		}

		public long getState() {
			return State;
		}

		public void setState(long state) {
			State = state;
		}

		public String getSubTitle() {
			return SubTitle;
		}

		public void setSubTitle(String subTitle) {
			SubTitle = subTitle;
		}

		public String getSummary() {
			return Summary;
		}

		public void setSummary(String summary) {
			Summary = summary;
		}

		public String getTitle() {
			return Title;
		}

		public void setTitle(String title) {
			Title = title;
		}

		public long getType() {
			return Type;
		}

		public void setType(long type) {
			Type = type;
		}

		public long getUpCount() {
			return UpCount;
		}

		public void setUpCount(long upCount) {
			UpCount = upCount;
		}

		public long getUpCount2() {
			return UpCount2;
		}

		public void setUpCount2(long upCount2) {
			UpCount2 = upCount2;
		}

		public long getUpCount3() {
			return UpCount3;
		}

		public void setUpCount3(long upCount3) {
			UpCount3 = upCount3;
		}

		public long getUpCount4() {
			return UpCount4;
		}

		public void setUpCount4(long upCount4) {
			UpCount4 = upCount4;
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

		public String getVideoIco() {
			return VideoIco;
		}

		public void setVideoIco(String videoIco) {
			VideoIco = videoIco;
		}

		public long getViewCount() {
			return ViewCount;
		}

		public void setViewCount(long viewCount) {
			ViewCount = viewCount;
		}

		public long getVoteId() {
			return VoteId;
		}

		public void setVoteId(long voteId) {
			VoteId = voteId;
		}

		public ArrayList<ArticlePicture> getArticlePictures() {
			return ArticlePictures;
		}

		public void setArticlePictures(ArrayList<ArticlePicture> articlePictures) {
			ArticlePictures = articlePictures;
		}

		public ArrayList getArticlePreviewPictures() {
			return ArticlePreviewPictures;
		}

		public void setArticlePreviewPictures(ArrayList articlePreviewPictures) {
			ArticlePreviewPictures = articlePreviewPictures;
		}

		public ArrayList getContentPics() {
			return ContentPics;
		}

		public void setContentPics(ArrayList contentPics) {
			ContentPics = contentPics;
		}

		public long getEventsProgress() {
			return EventsProgress;
		}

		public void setEventsProgress(long eventsProgress) {
			EventsProgress = eventsProgress;
		}

		public ArrayList getGroupArticles() {
			return GroupArticles;
		}

		public void setGroupArticles(ArrayList groupArticles) {
			GroupArticles = groupArticles;
		}

		public boolean isHasJoinEvents() {
			return HasJoinEvents;
		}

		public void setHasJoinEvents(boolean hasJoinEvents) {
			HasJoinEvents = hasJoinEvents;
		}

		public boolean isHasStore() {
			return HasStore;
		}

		public void setHasStore(boolean hasStore) {
			HasStore = hasStore;
		}

		public boolean isHasUp() {
			return HasUp;
		}

		public void setHasUp(boolean hasUp) {
			HasUp = hasUp;
		}

		public boolean isHasVoted() {
			return HasVoted;
		}

		public void setHasVoted(boolean hasVoted) {
			HasVoted = hasVoted;
		}

		public ArrayList<HotComment> getHotComments() {
			return HotComments;
		}

		public void setHotComments(ArrayList<HotComment> hotComments) {
			HotComments = hotComments;
		}

		public ArrayList getLiveHostList() {
			return LiveHostList;
		}

		public void setLiveHostList(ArrayList liveHostList) {
			LiveHostList = liveHostList;
		}

		public ArrayList getRelatedArticles() {
			return RelatedArticles;
		}

		public void setRelatedArticles(ArrayList relatedArticles) {
			RelatedArticles = relatedArticles;
		}

		public ArrayList getSpecialList() {
			return SpecialList;
		}

		public void setSpecialList(ArrayList specialList) {
			SpecialList = specialList;
		}

		public long getTodayPrizeCount() {
			return TodayPrizeCount;
		}

		public void setTodayPrizeCount(long todayPrizeCount) {
			TodayPrizeCount = todayPrizeCount;
		}

		@Override
		public String toString() {
			return "SquareFoundModel [AdId=" + AdId + ", AdminId=" + AdminId
					+ ", ArticleId=" + ArticleId + ", ArticleType="
					+ ArticleType + ", Author=" + Author + ", ChannelId="
					+ ChannelId + ", ChatPeopleCount=" + ChatPeopleCount
					+ ", CommentCount=" + CommentCount + ", Content=" + Content
					+ ", CreateDate=" + CreateDate + ", EventsEnd=" + EventsEnd
					+ ", EventsStart=" + EventsStart + ", Flags=" + Flags
					+ ", ForwardCount=" + ForwardCount + ", GroupPic="
					+ GroupPic + ", HTitle=" + HTitle + ", Ico=" + Ico
					+ ", Ico2=" + Ico2 + ", Ico3=" + Ico3 + ", IcoEx2="
					+ IcoEx2 + ", IcoEx3=" + IcoEx3 + ", IcoExHeight2="
					+ IcoExHeight2 + ", IcoExHeight3=" + IcoExHeight3
					+ ", IcoExWidth2=" + IcoExWidth2 + ", IcoExWidth3="
					+ IcoExWidth3 + ", IcoHeight=" + IcoHeight
					+ ", IcoHeight3=" + IcoHeight3 + ", IcoWidth=" + IcoWidth
					+ ", IcoWidth3=" + IcoWidth3 + ", Inspecial=" + Inspecial
					+ ", IsHot=" + IsHot + ", IsSquare=" + IsSquare
					+ ", IsStarPublish=" + IsStarPublish + ", IsTop=" + IsTop
					+ ", MagazineId=" + MagazineId + ", ModelType=" + ModelType
					+ ", PressId=" + PressId + ", PublishDate=" + PublishDate
					+ ", ShowStyle=" + ShowStyle + ", State=" + State
					+ ", SubTitle=" + SubTitle + ", Summary=" + Summary
					+ ", Title=" + Title + ", Type=" + Type + ", UpCount="
					+ UpCount + ", UpCount2=" + UpCount2 + ", UpCount3="
					+ UpCount3 + ", UpCount4=" + UpCount4 + ", UserAvatar="
					+ UserAvatar + ", UserId=" + UserId + ", UserNickName="
					+ UserNickName + ", VideoIco=" + VideoIco + ", ViewCount="
					+ ViewCount + ", VoteId=" + VoteId + ", ArticlePictures="
					+ ArticlePictures + ", ArticlePreviewPictures="
					+ ArticlePreviewPictures + ", ContentPics=" + ContentPics
					+ ", EventsProgress=" + EventsProgress + ", GroupArticles="
					+ GroupArticles + ", HasJoinEvents=" + HasJoinEvents
					+ ", HasStore=" + HasStore + ", HasUp=" + HasUp
					+ ", HasVoted=" + HasVoted + ", HotComments=" + HotComments
					+ ", LiveHostList=" + LiveHostList + ", RelatedArticles="
					+ RelatedArticles + ", SpecialList=" + SpecialList
					+ ", TodayPrizeCount=" + TodayPrizeCount + "]";
		}

	}
	
	public class ArticlePicture implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private long ArticleId;//15771.0, 
		private long Id;//12170.0, 
		private long PicHeight;//352.0, 
		private String PicPath;//http://qn.ieasy.tv/FtuwB_mic7csHqWZM9QboqvJTGYF, 
		private long PicWidth;//650.0, 
		private String Remark;//
		public long getArticleId() {
			return ArticleId;
		}
		public void setArticleId(long articleId) {
			ArticleId = articleId;
		}
		public long getId() {
			return Id;
		}
		public void setId(long id) {
			Id = id;
		}
		public long getPicHeight() {
			return PicHeight;
		}
		public void setPicHeight(long picHeight) {
			PicHeight = picHeight;
		}
		public String getPicPath() {
			return PicPath;
		}
		public void setPicPath(String picPath) {
			PicPath = picPath;
		}
		public long getPicWidth() {
			return PicWidth;
		}
		public void setPicWidth(long picWidth) {
			PicWidth = picWidth;
		}
		public String getRemark() {
			return Remark;
		}
		public void setRemark(String remark) {
			Remark = remark;
		}
		
		
	}
	
}
