package com.mci.firstidol.fragment.welfare;

import java.io.File;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.SparseIntArray;

import com.mci.firstidol.R;

public class Common {

    public static final String HTTP_SCHEMA = "http://";

    public static final String APP_KEY = "f93f6bed4y21da73cadebe83c623b4s";
    public static final int MAGAZINE_ITEM_ID = 37;
    public static final int PAGE_SIZE = 20;
    public static final int DOWNLOAD_PAGE_SIZE = 10;
    public static final int PAGE_INDEX = 1;
    public static final int COMMENT_SIZE = PAGE_SIZE;
    public static final int SUB_COMMENT_DEFAULT_COUNT = 3;
    public static final int HOT_COMMENT_SIZE = 2;

    public static final int ACTION_TYPE_LIKE = 1;
    public static final int ACTION_TYPE_REPORT = 2;

    public static final String[] CATEGORYS = { "新闻", "独家", "专访", "直播", "写真", "视频", "潮流", "画报", "好礼", "购物",/* "粉丝团", */"投票" };
//    public static final int[] CATEGORY_IDS = { R.id.news_area_msg, R.id.sole_area_msg, R.id.interview_area_msg,
//            R.id.zhibo_area_msg, R.id.photo_area_msg, R.id.video_area_msg, R.id.trend_area_msg, R.id.magazine_area_msg,
//            R.id.gift_area_msg, R.id.shopping_area_msg, R.id.vote_area_msg };
    public static final String[] NEWS_CHANNELS = { "韩国", "中国", "日本", "欧美" };
    public static final String[] VIDEO_CHANNELS = { "MV", "现场" };
    public static final Integer[] NEWS_CHANNEL_IDS = { 18, 17, 46, 20 };
    public static final Integer[] VIDEO_CHANNEL_IDS = { 31, 30 };
    public static final int SOLE_CHANNEL_ID = 28;
    public static final int INTERVIEW_CHANNEL_ID = 37;
    public static final int PHOTO_CHANNEL_ID = 38;
    public static final int TREND_CHANNEL_ID = 41;
    public static final int VOTE_CHANNEL_ID = 42;
    public static final int GIFT_CHANNEL_ID = 43;
    public static final int SHOPPING_CHANNEL_ID = 44;

    // 首页
    public static final int FRAGMENT_TYPE_HOME_PAGE = 100;
    // 新闻
    public static final int FRAGMENT_TYPE_NEWS = 0;
    // 独家
    public static final int FRAGMENT_TYPE_SOLE = FRAGMENT_TYPE_NEWS + 1;
    // 专访
    public static final int FRAGMENT_TYPE_INTERVIEW = FRAGMENT_TYPE_SOLE + 1;
    // 直播
    public static final int FRAGMENT_TYPE_ZHIBO = FRAGMENT_TYPE_INTERVIEW + 1;
    // 写真
    public static final int FRAGMENT_TYPE_PHOTO = FRAGMENT_TYPE_ZHIBO + 1;
    // 视频
    public static final int FRAGMENT_TYPE_VIDEO = FRAGMENT_TYPE_PHOTO + 1;
    // 潮流
    public static final int FRAGMENT_TYPE_TREND = FRAGMENT_TYPE_VIDEO + 1;
    // 画报
    public static final int FRAGMENT_TYPE_MAGAZINE = FRAGMENT_TYPE_TREND + 1;
    // 好礼
    public static final int FRAGMENT_TYPE_GIFT = FRAGMENT_TYPE_MAGAZINE + 1;
    // 购物
    public static final int FRAGMENT_TYPE_SHOPPING = FRAGMENT_TYPE_GIFT + 1;
    // 粉丝团
    public static final int FRAGMENT_TYPE_FANS = FRAGMENT_TYPE_SHOPPING + 1;// 2期
    // 投票
    public static final int FRAGMENT_TYPE_VOTE = FRAGMENT_TYPE_SHOPPING + 1;// 2期修改

    // 新闻
    public static final int NEWS_CATEGORY_ID = 7;
    // 独家
    public static final int SOLE_CATEGORY_ID = 8;
    // 专访
    public static final int INTERVIEW_CATEGORY_ID = 9;
    // 直播
    public static final int ZHIBO_CATEGORY_ID = 20;
    // 写真
    public static final int PHOTO_CATEGORY_ID = 10;
    // 视频
    public static final int VIDEO_CATEGORY_ID = 11;
    // 潮流
    public static final int TREND_CATEGORY_ID = 12;
    // 画报
    public static final int MAGAZINE_CATEGORY_ID = 1000;
    // 好礼
    public static final int GIFT_CATEGORY_ID = 17;
    // 购物
    public static final int SHOPPING_CATEGORY_ID = 18;
    // 投票
    public static final int VOTE_CATEGORY_ID = 14;
    // 粉丝圈
    public static final int FANS_CATEGORY_ID = 19;

    public static final SparseIntArray CATEGORY_ID_TYPE = new SparseIntArray();
    @SuppressLint("UseSparseArrays")
    public static final HashMap<Integer, String> CATEGORY_MAP = new HashMap<Integer, String>();
    public static final SparseIntArray CATEGORY_ID_POSITION = new SparseIntArray();

    static {
        CATEGORY_ID_TYPE.put(FRAGMENT_TYPE_NEWS, NEWS_CATEGORY_ID);
        CATEGORY_ID_TYPE.put(FRAGMENT_TYPE_SOLE, SOLE_CATEGORY_ID);
        CATEGORY_ID_TYPE.put(FRAGMENT_TYPE_INTERVIEW, INTERVIEW_CATEGORY_ID);
        CATEGORY_ID_TYPE.put(FRAGMENT_TYPE_PHOTO, PHOTO_CATEGORY_ID);
        CATEGORY_ID_TYPE.put(FRAGMENT_TYPE_VIDEO, VIDEO_CATEGORY_ID);
        CATEGORY_ID_TYPE.put(FRAGMENT_TYPE_TREND, TREND_CATEGORY_ID);
        CATEGORY_ID_TYPE.put(FRAGMENT_TYPE_MAGAZINE, MAGAZINE_CATEGORY_ID);
        CATEGORY_ID_TYPE.put(FRAGMENT_TYPE_GIFT, GIFT_CATEGORY_ID);
        CATEGORY_ID_TYPE.put(FRAGMENT_TYPE_SHOPPING, SHOPPING_CATEGORY_ID);
        CATEGORY_ID_TYPE.put(FRAGMENT_TYPE_VOTE, VOTE_CATEGORY_ID);
        CATEGORY_ID_TYPE.put(FRAGMENT_TYPE_ZHIBO, ZHIBO_CATEGORY_ID);

        CATEGORY_MAP.put(NEWS_CATEGORY_ID, "新闻");
        CATEGORY_MAP.put(SOLE_CATEGORY_ID, "独家");
        CATEGORY_MAP.put(INTERVIEW_CATEGORY_ID, "专访");
        CATEGORY_MAP.put(ZHIBO_CATEGORY_ID, "直播");
        CATEGORY_MAP.put(PHOTO_CATEGORY_ID, "写真");
        CATEGORY_MAP.put(VIDEO_CATEGORY_ID, "视频");
        CATEGORY_MAP.put(TREND_CATEGORY_ID, "潮流");
        CATEGORY_MAP.put(MAGAZINE_CATEGORY_ID, "画报");
        CATEGORY_MAP.put(GIFT_CATEGORY_ID, "好礼");
        CATEGORY_MAP.put(SHOPPING_CATEGORY_ID, "购物");
        CATEGORY_MAP.put(VOTE_CATEGORY_ID, "投票");

        CATEGORY_ID_POSITION.put(NEWS_CATEGORY_ID, 0);
        CATEGORY_ID_POSITION.put(SOLE_CATEGORY_ID, 1);
        CATEGORY_ID_POSITION.put(INTERVIEW_CATEGORY_ID, 2);
        CATEGORY_ID_POSITION.put(ZHIBO_CATEGORY_ID, 3);
        CATEGORY_ID_POSITION.put(PHOTO_CATEGORY_ID, 4);
        CATEGORY_ID_POSITION.put(VIDEO_CATEGORY_ID, 5);
        CATEGORY_ID_POSITION.put(TREND_CATEGORY_ID, 6);
        CATEGORY_ID_POSITION.put(MAGAZINE_CATEGORY_ID, 7);
        CATEGORY_ID_POSITION.put(GIFT_CATEGORY_ID, 8);
        CATEGORY_ID_POSITION.put(SHOPPING_CATEGORY_ID, 9);
        CATEGORY_ID_POSITION.put(VOTE_CATEGORY_ID, 10);
    }

    // ************************************************************************************** MCI QA
    // **********************************************************************************//
    // 配置服务器地址
    // public static String CONFIGS_HOST = "http://www.ieasy.tv:8889/api";
    public static String CONFIGS_HOST = "http://phone.ieasy.tv/api";
    // API服务器地址
    // public static String HOST = "http://www.ieasy.tv:8889/api";
    public static String HOST = "http://phone.ieasy.tv/api";
    // 图片服务器地址
    public static String IMAGE_HOST = "http://itemcdn.ieasy.tv/pic";
    // 杂志服务器地址
    public static String ITEMS_HOST = "http://itemcdn.ieasy.tv/@";
    // 日志服务器地址
    public static String URL_CHECKIN_LOG = "http://stat.createapp.cn";
    // 支付服务器地址
    public static String URL_PAYMENT = "http://pay.qasvc.mcitech.cn/cpp-payment/sina/order";
    // 订单状态查询地址
    public static String URL_PAYMENT_QUERY = "http://pay.qasvc.mcitech.cn/cpp-payment/sina/query";
    // 消息服务器地址
    public static String BAIDU_PUSH_HOST = "http://push.qasvc.mcitech.cn";
    // 上传资源地址
    public static String UPLOAD_SOURCE_HOST = "http://sitem.mcitech.cn";
    // 下载排行天数
    public static int URL_DOWNLOADRANKING_CONFIG = 4;

    // Baidu Push Parameter:
    // apiKey=7XM68sSwGgoG7OXGPmf7hfxi
    // secretKey=spVjQKajE9LRYc3nuPAkrPBBRj4G6mxp
    public final static String BAIDU_PUSH_API_KEY = "7XM68sSwGgoG7OXGPmf7hfxi";

    // WX Interface
    // WX_APP_ID 为你的应用从官方网站申请到的合法appId
    // public static final String WX_APP_ID = "wxdcdc875652739e96";
    // public static final String WX_APP_KEY = "61c2daf8620a75b2a4dd05828ea6fc5c";

    // 是否需要打印自有log
    public static boolean NEED_PRINT_MCI_LOG = true;

    // ************************************************************************************** Uri definition end
    // **********************************************************************************//

    // S Account version
    public static final int SA_SUPPORT_ACTIVITY_INTERFACE_VERSION = 150200;

    public static final String ARTICLE_URL = "file:///android_asset/player/article_2.html";

    public final static String LOG_LEVEL = "2";

    public static String FEEDBACK = "/feedback/add";
    public static String APP_VERSION = "/appversion/version";
    public static String MERCHANT_URL = "http://www.mcitech.cn/";

    public static final String APP_NAME = ".EasyPhone";
    public static final String IMAGE_CACHE_DIR = "image" + File.separator;// 图片路劲
    public static final String FORMAL_CACHE_DIR = "Formal" + File.separator;// 正式
    public static final String SAMPLE_CACHE_DIR = "Sample" + File.separator;// 试读
    public static final String DOWNLOAD_IMG_DIR = "DownloadImg" + File.separator;// 杂志cp图片
    public static final String VERSION_INFO = "Info" + File.separator;// 存储版本信息
    public static final String AVATAR_DIR = "Avatar" + File.separator;// 头像
    public static final String AUDIO_DIR = "audio" + File.separator;// 录音

    public final static String CLIENT_ID = "15u4c6rp8o";
    public final static String CLIENT_SECRET = "9F042B11DFB7C1EBFD320A2E8D07A0EC";

    public static final String CHANNEL_ID = "0";

    // 文章来源 ID
    public static final int CHANNEL_NUMBER = -1;

    public static final long SPACE_ALARM_SIZE = 100 * 1024 * 1024;

    public static final int WX_ARTICLE_ICON_TYPE_PNG = 1;
    public static final int WX_ARTICLE_ICON_TYPE_JPG = 2;

    public static final long DYNAMIC_CONFIG_INTERVAL = 86400000;// 24*60*60*1000;//milliseconds

    // 自有log相关
    // log输出目录
    public static String MCI_LOG_DIR = "";
    // log文件名
    public static String MCI_LOG_NAME = "mcilog.txt";

    public static final String ACTION_USER_VERIFIED = "com.mci.action.ACTION_USER_VERIFIED";
    public static final String ACTION_SIGN_IN_COMPLETE = "com.mci.action.ACTION_SIGN_IN_COMPLETE";
    public static final String ACTION_SIGN_OUT = "com.mci.action.ACTION_SIGN_OUT";
    // 强制更新广播
    public static final String ACTION_FORCE_UPDATE_ON_OPERATE = "com.mci.action.ACTION_FORCE_UPDATE_ON_OPERATE";

    // 下载杂志任务成功添加到下载Service中
    public static final String ACTION_ADD_DOWNLOAD_TASK_COMPLETE = "com.mci.action.ACTION_ADD_DOWNLOAD_TASK_COMPLETE";
    // 下载杂志任务添加失败
    public static final String ACTION_ADD_DOWNLOAD_TASK_FAILED = "com.mci.action.ACTION_ADD_DOWNLOAD_TASK_FAILED";
    // 取消下载杂志任务
    public static final String ACTION_DOWNLOAD_TASK_PAUSED = "com.mci.action.ACTION_DOWNLOAD_TASK_PAUSED";
    // 取消下载杂志任务
    public static final String ACTION_DOWNLOAD_TASK_CANCELED = "com.mci.action.ACTION_DOWNLOAD_TASK_CANCELED";
    // 杂志资源错误广播
    public static final String ACTION_MAGAZINE_RESOURCE_ERROR = "com.imobile.mixobserver.MAGAZINE_RESOURCE_ERROR";

    // 下载完成，需要post to server
    public static final String ACTION_MAGAZINE_DOWNLOAD_COMPLETE = "com.mci.action.ACTION_MAGAZINE_DOWNLOAD_COMPLETE";
    // 购买完成，需要post to server
    public static final String ACTION_FINISH_BUY_MANAGZINE = "com.mci.action.ACTION_FINISH_BUY_MANAGZINE";

    public static final int RECOMMENDATION_TYPE_MAGAZINE = 0;
    public static final int RECOMMENDATION_TYPE_MAGAZINE_ITEM = RECOMMENDATION_TYPE_MAGAZINE + 1;
    public static final int RECOMMENDATION_TYPE_EXTERNAL = RECOMMENDATION_TYPE_MAGAZINE_ITEM + 1;
    public static final int RECOMMENDATION_TYPE_INVITE = RECOMMENDATION_TYPE_EXTERNAL + 1;

    /*
     * push message type Type为0：普通无关联，只显示通知消息; Type为1：关联文章，需要通过RefId 查看对应的文章;
     * Type为2：关联刊物,需要通过RefId查看对应的杂志详情列表; Type为3：版本升级消息，通过Url链接下载对应的升级安装包
     * Type为4：开通会员; Type为5：关联单本杂志
     */
    public static final int MESSAGE_TYPE_NORMAL = 0;
    public static final int MESSAGE_TYPE_REFERENCE_ARTICLE = MESSAGE_TYPE_NORMAL + 1;
    public static final int MESSAGE_TYPE_REFERENCE_MAGAZINE = MESSAGE_TYPE_REFERENCE_ARTICLE + 1;
    public static final int MESSAGE_TYPE_REFERENCE_EXTERNAL = MESSAGE_TYPE_REFERENCE_MAGAZINE + 1;
    public static final int MESSAGE_TYPE_VIP_INVITE = MESSAGE_TYPE_REFERENCE_EXTERNAL + 1;
    public static final int MESSAGE_TYPE_REFERENCE_ITEM = MESSAGE_TYPE_VIP_INVITE + 1;
    public static final int MESSAGE_TYPE_VALUE_PACKAGE = MESSAGE_TYPE_REFERENCE_ITEM + 1;
    public static final int MESSAGE_TYPE_NEW_MAGAZINE = MESSAGE_TYPE_VALUE_PACKAGE + 1;

    public static final String CATEGORY_TYPE_ADMIN = "adminFeatured";

    /**
     * activity request code
     */
    public static final int REQUEST_LOGIN = 0;
    public static final int REQUEST_USER_CENTER = 1;

    /**
     * 首页 推荐 上图下文 图片比例
     */
    public static final int DEFAULT_NEWS_IMAGE_SCALE_W = 640;
    public static final int DEFAULT_NEWS_IMAGE_SCALE_H = 510;

    /**
     * 投票 左图右文 图片比例
     */
    public static final int DEFAULT_LEFT_PIC_VOTE_IMAGE_SCALE_W = 1;
    public static final int DEFAULT_LEFT_PIC_VOTE_IMAGE_SCALE_H = 1;

    /**
     * 首页快闻 图片比例
     */
    public static final int DEFAULT_NEWSFRAGMENT_AD_IMAGE_SCALE_W = 16;
    public static final int DEFAULT_NEWSFRAGMENT_AD_IMAGE_SCALE_H = 9;

    /**
     * 推荐区域 图片比例
     */
    public static final int NEWSFRAGMENT_AD_IMAGE_SCALE_W = 9;
    public static final int NEWSFRAGMENT_AD_IMAGE_SCALE_H = 5;

    /**
     * 首页快闻 上图下文 图片比例
     */
    public static final int DEFAULT_TOP_PIC_BOTTOM_TEXT_IMAGE_SCALE_W = 4;
    public static final int DEFAULT_TOP_PIC_BOTTOM_TEXT_IMAGE_SCALE_H = 1;

    /**
     * 首页快闻 图集 图片比例
     */
    public static final int DEFAULT_PIC_SET_IMAGE_SCALE_W = 6;
    public static final int DEFAULT_PIC_SET_IMAGE_SCALE_H = 5;

    /**
     * 默认每栏目加载数量
     */
    public static final int DEFAULT_SHOW_COUNT = 6;

    /**
     * 默认精品区栏目固定数量
     */
    public static final int DEFAULT_TREND_COUNT = 3;
    public static final int NORECOM_TREND_COUNT = 2;

    /**
     * 保存图片
     */
    public static final int SAVE_PIC_FAIL = 0;
    public static final int SAVE_PIC_DOWNLOADING = 1;
    public static final int SAVE_PIC_SECUSS = 2;

    /**
     * notification identifier
     */
    public static final int NOTIFICATION_ID_NORMAL = 1;
    public static final int NOTIFICATION_ID_VIP = NOTIFICATION_ID_NORMAL + 1;

    public static final float ASPECT_RATIO = 1.3f;

    public static final int DEVICE_TYPE_NONE = 0;
    public static final int DEVICE_TYPE_TABLET = 1;
    public static final int DEVICE_TYPE_PHONE = 2;
    public static final int DEVICE_SIZE = 8;// 8英寸

    /**
     * 正文字号类型
     */
    public static final int CONTENT_TEXT_SIZE_SMALL = 0;
    public static final int CONTENT_TEXT_SIZE_NORMAL = CONTENT_TEXT_SIZE_SMALL + 1;
    public static final int CONTENT_TEXT_SIZE_LARGE = CONTENT_TEXT_SIZE_NORMAL + 1;

    /**
     * 个人资料中编辑类型
     */
    public final static int TYPE_AVATAR = 0;
    public final static int TYPE_NICKNAME = TYPE_AVATAR + 1;
    public final static int TYPE_SEX = TYPE_NICKNAME + 1;
    public final static int TYPE_TEL = TYPE_SEX + 1;
    public final static int TYPE_ADDRESS = TYPE_TEL + 1;
    public final static int TYPE_CITY = TYPE_ADDRESS + 1;
    public final static int REQUEST_EDIT = TYPE_CITY + 1;
    public static final int REQUEST_PICK_IMAGE = REQUEST_EDIT + 1;
    public static final int REQUEST_CROP_IMAGE = +REQUEST_PICK_IMAGE + 1;
    public static final int REQUEST_TAKE_PHOTO = REQUEST_CROP_IMAGE + 1;

    public static final int AVATAR_SIZE = 200;
    // 首页文章个数
    public static final int HOME_PAGE_RECOMENT_LIST_SIZE = 11;

    public enum LoadMoreState {
        LOAD_MORE_COMPLETE, LOAD_MORE_SERVER_DOWN, LOAD_MORE_FAILED,
    }

    public static String getBasePath() {
    	String basePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/.temp_tmp";

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            basePath = new StringBuilder(Environment.getExternalStorageDirectory().getPath()).append(File.separator)
                    .append(APP_NAME).append(File.separator).toString();
        }

        return basePath;
    }

    public static String getImageCachePath() {
        String cachePath = null;

        if (getBasePath() != null) {
            cachePath = new StringBuilder(getBasePath()).append(".images").toString();
        }

        return cachePath;
    }

    public static int versionToInt(String version) {
        int value;
        version = version.replace(".", "");
        value = Integer.parseInt(version);
        return value;
    }

    public static boolean compareVersion(String oldVersion, String newVersion) {
        if (TextUtils.isEmpty(oldVersion) || TextUtils.isEmpty(newVersion)) {
            return false;
        } else {
            if (oldVersion.compareTo(newVersion) >= 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * @param version
     * @param index
     *            the starting point
     * @return the number between two dots, and the index of the dot
     */
    public static int[] getValue(String version, int index) {
        int[] value_index = new int[2];
        StringBuilder sb = new StringBuilder();
        while (index < version.length() && version.charAt(index) != '.') {
            sb.append(version.charAt(index));
            index++;
        }
        value_index[0] = Integer.parseInt(sb.toString());
        value_index[1] = index;

        return value_index;
    }

    public static String getHost(Context context) {
        String host = Configs.getAPIHost(context);

        if (host == null || host.isEmpty()) {
            host = HOST;
        }

        return host;
    }

    public static String getBaiduPushHost(Context context) {
        String host = Configs.getBaiduPushHost(context);

        if (host == null || host.isEmpty()) {
            host = BAIDU_PUSH_HOST;
        }

        return host;
    }

    public static String getConfigsServer(Context context) {
        String configsServer = Configs.getConfigsServer(context);

        if (configsServer == null || configsServer.isEmpty()) {
            configsServer = CONFIGS_HOST;
        }

        return configsServer;
    }

    public static String getImageServer(Context context) {
        String imageServer = Configs.getImageServer(context);

        if (imageServer == null || imageServer.isEmpty()) {
            imageServer = IMAGE_HOST;
        }

        return imageServer;
    }

    public static String getItemsServer(Context context) {
        String itemsServer = Configs.getItemsServer(context);

        if (itemsServer == null || itemsServer.isEmpty()) {
            itemsServer = ITEMS_HOST;
        }

        return itemsServer;
    }
}
