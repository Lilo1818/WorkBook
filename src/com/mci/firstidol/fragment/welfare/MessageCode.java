
package com.mci.firstidol.fragment.welfare;

public class MessageCode {

    public final static int GET_APP_VERSION = 0;
    // 获取书架的列表
    public final static int GET_MAGAZINE_ITEMS = GET_APP_VERSION + 1;
    // 快闻推荐
    public final static int GET_NEWS_RECOMMENDATIONS = GET_MAGAZINE_ITEMS + 1;
    // 获取动态配置信息
    public final static int GET_DYNAMIC_CONFIGS = GET_NEWS_RECOMMENDATIONS + 1;
    // 获取文章列表
    public final static int GET_ARTICLES_LIST = GET_DYNAMIC_CONFIGS + 1;
    // 获取详情（包括专题和新闻详情，共用！）
    public final static int GET_ARTICLE = GET_ARTICLES_LIST + 1;
    // 获取文章评论
    public final static int GET_ARTICLE_COMMENT = GET_ARTICLE + 1;
    // 获取文章最热评论
    public final static int GET_HOT_COMMENT = GET_ARTICLE_COMMENT + 1;
    // 获取我的评论
    public final static int GET_MY_COMMENT = GET_HOT_COMMENT + 1;
    // 获取我的评论
    public final static int GET_SPLASH_PIC = GET_MY_COMMENT + 1;
    // 获取二级栏目
    public final static int GET_CHANNEL_LIST = GET_SPLASH_PIC + 1;
    // 获取推荐文章列表
    public final static int GET_RECOMMEND_ARTICLE_LIST = GET_CHANNEL_LIST + 1;
    // 文章点赞
    public final static int GET_LIKE_ARTICLE_LIST = GET_RECOMMEND_ARTICLE_LIST + 1;
    // 我的好礼
    public final static int GET_MY_GIFT = GET_LIKE_ARTICLE_LIST + 1;
    // 查看活动结果
    public final static int GET_EVENT_RESULT = GET_MY_GIFT + 1;
    // 获取分类列表
    public final static int GET_CATEGORY_LIST = GET_EVENT_RESULT + 1;
    // 获取直播列表
    public final static int GET_ZHIBO_LIST = GET_CATEGORY_LIST + 1;
    // 获取聊天内容列表
    public final static int GET_CHAT_LIST = GET_ZHIBO_LIST + 1;
    // 获取直播间主持人和嘉宾信息
    public final static int GET_LIVE_HOST_AND_GUEST = GET_CHAT_LIST + 1;
    // 获取最新的聊天内容
    public final static int GET_NEWEST_CHAT_LIST = GET_LIVE_HOST_AND_GUEST + 1;
    // 获取角色信息
    public final static int GET_ROLE_INFO = GET_NEWEST_CHAT_LIST + 1;
    // 获取聊天室聊天数量
    public final static int GET_CHAT_COUNT = GET_ROLE_INFO + 1;
    // 获取聊天室人数
    public final static int GET_PEOPLE_COUNT = GET_CHAT_COUNT + 1;
    // 获取上传文件的token
    public final static int GET_UPLOAD_TOKEN = GET_PEOPLE_COUNT + 1;
    // 获取直播的聊天列表
    public final static int GET_LIVE_CHAT_LIST = GET_UPLOAD_TOKEN + 1;
    // 获取最新的直播聊天内容
    public final static int GET_NEWEST_LIVE_CHAT_LIST = GET_LIVE_CHAT_LIST + 1;
    // 获取首页数据
    public final static int GET_HOME_PAGE_LIST = GET_NEWEST_LIVE_CHAT_LIST + 1;
	// 获取好礼用户信息
    public final static int GET_GIFT_USERINFO = GET_HOME_PAGE_LIST + 1;

    // 搜索文章
    public final static int POST_SEARCH_ARTICLE = 100;
    // 意见反馈
    public final static int POST_FEEDBACK = POST_SEARCH_ARTICLE + 1;
    // add comment
    public final static int POST_COMMENT = POST_FEEDBACK + 1;
    // post 赞
    public final static int POST_LIKE_OR_REPORT = POST_COMMENT + 1;
    // 请求投票结果
    public final static int POST_VOTE = POST_LIKE_OR_REPORT + 1;
    // post register
    public final static int POST_REGISTER = POST_VOTE + 1;
    // post social login
    public final static int POST_SOCIAL_LOGIN = POST_REGISTER + 1;
    // post login
    public final static int POST_LOGIN = POST_SOCIAL_LOGIN + 1;
    // post user info update
    public final static int POST_USERINFO_UPDATE = POST_LOGIN + 1;
    // post user avatar
    public final static int POST_USER_AVATAR = POST_USERINFO_UPDATE + 1;
    // post event log
    public final static int POST_EVENT_LOG = POST_USER_AVATAR + 1;
    // post points add
    public final static int POST_POINTS_ADD = POST_EVENT_LOG + 1;
    // 参加活动
    public final static int POST_EVENT_JOIN = POST_POINTS_ADD + 1;
    // 保存聊天信息
    public final static int POST_SAVE_CHAT_INFO = POST_EVENT_JOIN + 1;
    // 更新顶的数量
    public final static int POST_UPATE_DING_COUNT = POST_SAVE_CHAT_INFO + 1;
    // 更新踩的数量
    public final static int POST_UPATE_CAI_COUNT = POST_UPATE_DING_COUNT + 1;
    // 禁言某个用户
    public final static int POST_DISABLE_SEND_MESSAGE = POST_UPATE_CAI_COUNT + 1;
    // 置顶聊天内容
    public final static int POST_TOP_CHAT = POST_DISABLE_SEND_MESSAGE + 1;
    // 修改聊天内容
    public final static int POST_EDIT_CHAT = POST_TOP_CHAT + 1;
    // 更新聊天室人数
    public final static int POST_UPDATE_CHAT_COUNT = POST_EDIT_CHAT + 1;
    // 删除聊天内容
    public final static int POST_DELETE_CHAT = POST_UPDATE_CHAT_COUNT + 1;
    // 取消置顶的聊天内容
    public final static int POST_CANCEL_TOP_CHAT = POST_DELETE_CHAT + 1;
}