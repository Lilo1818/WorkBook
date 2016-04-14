
package com.mci.firstidol.fragment.welfare;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class Configs {

    public static final String CONFIGS = "settings";

    public static final String SIGN_IN = "sign_in";
    public static final String USER_ID = "user_id";
    public static final String TOKEN = "token";
    public static final String USER_LOGIN_ID = "user_login_id";
    public static final String USER_BIRTHDAY = "user_borthday";
    public static final String ACCESS_TOKEN = "access_token";

    public static final String IMAGE_DOWNLOAD_MODE = "photo_mode";
    public static final String DOWNLOAD_TO_SDCARD = "download_to_sdcard";

    public static final String PUSH_MESSAGE_LAST_DATE = "push_message_last_date";
    public static final String VP_PUSH_MESSAGE_LAST_DATE = "vp_push_message_last_date";
    public static final String STORE_RECOMMENDS_REFRESH_TIME = "store_recommends_refresh_time";

    public static final String CONFIG_SERVER = "configserver";
    public static final String API_SERVER = "apiserver";
    public static final String IMAGE_SERVER = "imageserver";
    public static final String ITEMS_SERVER = "itemserver";
    public static final String LOG_SERVER = "logserver";
    public static final String PAYMENT_SERVER = "paymentserver";
    public static final String ORDER_STATE_SERVER = "orderstateserver";
    public static final String BAIDU_PUSH_SERVER = "baidupushserver";
    public static final String UPLOAD_ITEM_SERVER = "uploaditemserver";
    public static final String DOWNRANK_DAYS = "downrankdays";
    public static final String LOG_LEVEL = "loglevel";
    public static final String ENABLE_INVITE_VIP = "joinvip";

    public static final String DEVICE_UNSUPPORTED_NOTIFIED_FLAG = "device_unsupported_notified_flag";

    public static final String SHORTCUT_HAS_INSTALLED = "shortcut_has_installed";
    // 网络变化提示
    public static final String SPLASH_NETWORK_TIPS_STATUS = "splash_network_tips_status";
    // 是否设置wifi下载
    public static final String WIFI_DOWNLOAD_STATUS = "wifi_download_status";

    // 杂志详情界面提示
    public static final String STORE_DETAILS_TIPS_COLLECT = "store_details_tips_collect";
    public static final String STORE_DETAILS_TIPS_SHORTCUT = "store_details_tips_shortcut";

    // 主界面提示
    public static final String MAIN_ACTIVITY_TIPS_FEATURE = "main_activity_tips_feature";
    public static final String MAIN_ACTIVITY_TIPS_BOOKSHELF = "main_activity_tips_bookshelf";

    // 下载的app版本号
    public static final String APP_VERSION = "app_version";

    // 网络变化提示
    public static final String NETWORK_TIPS_STATUS_WIFI = "network_tips_status_wifi";
    public static final String NETWORK_TIPS_STATUS_3G = "network_tips_status_3g";
    // 是否记录了位置信息
    public static final String IS_LOCATED = "is_located";

    // device type: 1 Tablet; 2 Phone
    public static final String DEVICE_TYPE = "key_device_type";
    public static final String LAST_CONFIG_DEVICE_TIME = "last_config_device_time";
    // 正文字号
    public static final String CONTENT_TEXT_SIZE = "content_text_size";

    private static final String KEY_GUIDE = "key_guide";

    private static final String KEYWORD_VOTE = "vote";
    private static final String KEYWORD_ARTICLE = "article";
    private static final String KEYWORD_GIFT = "gift";
    private static final String KEYWORD_SHOPPING = "shopping";
    private static final String KEYWORD_ZAN = "zan";
    private static final String KEY_UPLOAD_TOKEN = "upload_token";

    public static String getUserLoginId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return pref.getString(USER_LOGIN_ID, null);
    }

    public static void setUserLoginId(Context context, String loginId) {
        SharedPreferences pref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(USER_LOGIN_ID, loginId);
        editor.commit();
    }

    public static String getUserBirthday(Context context) {
        SharedPreferences pref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return pref.getString(USER_BIRTHDAY, null);
    }

    public static void setUserBirthday(Context context, String birthday) {
        SharedPreferences pref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(USER_BIRTHDAY, birthday);
        editor.commit();
    }

    public static String getUserAccessToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return pref.getString(ACCESS_TOKEN, null);
    }

    public static void setUserAccessToken(Context context, String accessToken) {
        SharedPreferences pref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    // true只在Wi-Fi可用时下载， false没有Wi-Fi时也可下载
    public static boolean getNetworkMode(Context context) {
        int photoMode = 1;
        SharedPreferences pref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        photoMode = pref.getInt(IMAGE_DOWNLOAD_MODE, 1);
        return photoMode > 0 ? true : false;
    }

    // true只在Wi-Fi可用时下载， false没有Wi-Fi时也可下载
    public static void setNetworkMode(Context context, boolean bPhotoMode) {
        int photoMode = bPhotoMode ? 1 : 0;
        SharedPreferences pref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putInt(IMAGE_DOWNLOAD_MODE, photoMode);
        editor.commit();
    }

    /****************** 动态配置相关 start *******************/
//    public static void setDynamicConfigs(Context context, List<ConfigInfo> configList) {
//        if (context != null && configList != null && !configList.isEmpty()) {
//            SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sp.edit();
//
//            for (ConfigInfo info : configList) {
//                if (info != null && info.getKey() != null && !info.getKey().isEmpty()) {
//                    editor.putString(info.getKey(), info.getValue());
//                }
//            }
//
//            editor.commit();
//        }
//    }

    public static String getConfigsServer(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getString(CONFIG_SERVER, null);
    }

    public static String getAPIHost(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getString(API_SERVER, null);
    }

    public static String getImageServer(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getString(IMAGE_SERVER, null);
    }

    public static String getItemsServer(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getString(ITEMS_SERVER, null);
    }

    public static String getLogServer(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getString(LOG_SERVER, null);
    }

    public static String getPaymentServer(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getString(PAYMENT_SERVER, null);
    }

    public static String getPaymentStateQueryServer(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getString(ORDER_STATE_SERVER, null);
    }

    public static String getBaiduPushHost(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getString(BAIDU_PUSH_SERVER, null);
    }

    public static String getUploadItemServer(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getString(UPLOAD_ITEM_SERVER, null);
    }

    public static String getDownrankDays(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getString(DOWNRANK_DAYS, null);
    }

    public static String getLogLevel(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getString(LOG_LEVEL, null);
    }

    public static boolean getEnableInviteVip(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String result = sp.getString(ENABLE_INVITE_VIP, "0");
        if ("1".equals(result)) {
            return true;
        }
        return false;
    }

    /****************** 动态配置相关 end *******************/

    public static boolean getDeviceUnsupportedNotifiedFlag(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getBoolean(DEVICE_UNSUPPORTED_NOTIFIED_FLAG, false);
    }

    public static void setDeviceUnsupportedNotifiedFlag(Context context, boolean flag) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(DEVICE_UNSUPPORTED_NOTIFIED_FLAG, flag);
        editor.commit();
    }

    /** 处理消息推送时间 **/
    public static String getPushInfosLastDate(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getString(PUSH_MESSAGE_LAST_DATE, 0 + "");
    }

    public static void setPushInfosLastDate(Context context, String date) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PUSH_MESSAGE_LAST_DATE, date);
        editor.commit();
    }

    /** 处理VP消息推送时间 **/
    public static String getVPPushInfosLastDate(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getString(VP_PUSH_MESSAGE_LAST_DATE, 0 + "");
    }

    public static void setVPPushInfosLastDate(Context context, String date) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(VP_PUSH_MESSAGE_LAST_DATE, date);
        editor.commit();
    }

    /**
     * 获取离开商店时间
     * 
     * @param context
     * @return
     */
    public static long getLeaveStoreTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getLong(STORE_RECOMMENDS_REFRESH_TIME, 0);
    }

    /**
     * 保存离开商店时间
     * 
     * @param context
     */
    public static void saveLeaveStoreTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(STORE_RECOMMENDS_REFRESH_TIME, System.currentTimeMillis());
        edit.commit();
    }

    /**
     * 存储杂志详情界面是否显示了Tips
     */
    public static boolean getCollectTipsStatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getBoolean(STORE_DETAILS_TIPS_COLLECT, false);
    }

    public static void setCollectTipsStatus(Context context, boolean result) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(STORE_DETAILS_TIPS_COLLECT, result);
        editor.commit();
    }

    public static boolean getShortcutTipsStatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getBoolean(STORE_DETAILS_TIPS_SHORTCUT, false);
    }

    public static void setShortcutTipsStatus(Context context, boolean result) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(STORE_DETAILS_TIPS_SHORTCUT, result);
        editor.commit();
    }

    public static boolean getShortcutInstalledFlag(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getBoolean(SHORTCUT_HAS_INSTALLED, false);
    }

    public static void setShortcutInstalledFlag(Context context, boolean flag) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SHORTCUT_HAS_INSTALLED, flag);
        editor.commit();
    }

    // 是否记录了位置信息
    public static boolean isLocated(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getBoolean(IS_LOCATED, false);
    }

    public static void setIsLocated(Context context, boolean flag) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IS_LOCATED, flag);
        editor.commit();
    }

    /**
     * 存储 主界面 是否显示了Tips
     */
    public static boolean getFeatureTipStatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getBoolean(MAIN_ACTIVITY_TIPS_FEATURE, false);
    }

    public static void setFeatureTipStatus(Context context, boolean result) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(MAIN_ACTIVITY_TIPS_FEATURE, result);
        editor.commit();
    }

    public static boolean getBookShelfTipStatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getBoolean(MAIN_ACTIVITY_TIPS_BOOKSHELF, false);
    }

    public static void setBookShelfTipStatus(Context context, boolean result) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(MAIN_ACTIVITY_TIPS_BOOKSHELF, result);
        editor.commit();
    }

    /**
     * 存储 更新的版本
     */
    public static String getDownloadAppVersion(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getString(APP_VERSION, "");
    }

    public static void setDownloadAppVersion(Context context, String strVersion) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(APP_VERSION, strVersion);
        editor.commit();
    }

    /**
     * 设置splash 网络状况的提示
     */
    public static boolean getNetworkTipsStatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getBoolean(SPLASH_NETWORK_TIPS_STATUS, false);
    }

    public static void setNetworkTipsStatus(Context context, boolean b) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SPLASH_NETWORK_TIPS_STATUS, b);
        editor.commit();
    }

    public static boolean isDownloadToSDCard(Context context) {
        return false;
    }

    public static void setDownloadToSDCardFlag(Context context, boolean flag) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(DOWNLOAD_TO_SDCARD, flag);
        editor.commit();
    }

    /**
     * 设置WIFI连接状态的提示
     */
    public static boolean getNetworkTipsStatusWiFi(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getBoolean(NETWORK_TIPS_STATUS_WIFI, false);
    }

    public static void setNetworkTipsStatusWiFi(Context context, boolean flag) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(NETWORK_TIPS_STATUS_WIFI, flag);
        editor.commit();
    }

    /**
     * 设置GPS网络连接状态的提示
     */
    public static boolean getNetworkTipsStatus3G(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getBoolean(NETWORK_TIPS_STATUS_3G, false);
    }

    public static void setNetworkTipsStatus3G(Context context, boolean flag) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(NETWORK_TIPS_STATUS_3G, flag);
        editor.commit();
    }

//    public static int getDeviceType(Context context) {
//        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
//        return sp.getInt(DEVICE_TYPE, Common.DEVICE_TYPE_NONE);
//    }
//
//    public static void setDeviceType(Context context, int deviceType) {
//        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
//
//        if (Common.DEVICE_TYPE_TABLET != deviceType) {
//            deviceType = Common.DEVICE_TYPE_PHONE;
//        }
//
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putInt(DEVICE_TYPE, deviceType);
//        editor.commit();
//    }
//
//    public static void setDeviceType(Context context) {
//        if (getDeviceType(context) == Common.DEVICE_TYPE_NONE) {
//            if (CommonUtils.deviceSize(context) > Common.DEVICE_SIZE) {
//                setDeviceType(context, Common.DEVICE_TYPE_TABLET);
//            } else {
//                setDeviceType(context, Common.DEVICE_TYPE_PHONE);
//            }
//        }
//    }

    /**
     * 获取上次请求配置的时间
     * 
     * @param context
     * @return
     */
    public static long getLastRequestConfigTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getLong(LAST_CONFIG_DEVICE_TIME, 0);
    }

    /**
     * 保存上次请求配置的时间
     * 
     * @param context
     */
    public static void saveLastRequestConfigTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(LAST_CONFIG_DEVICE_TIME, System.currentTimeMillis());
        edit.commit();
    }

//    public static boolean needRequestDynamicConfig(Context context) {
//        return System.currentTimeMillis() - getLastRequestConfigTime(context) >= Common.DYNAMIC_CONFIG_INTERVAL ? true : false;
//    }

    /******************** 设置中用到的SharedPreference start ************************/
    /**
     * 正文字号
     */
    public static void saveSettingContentTextSize(Context context, int textSize) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(CONTENT_TEXT_SIZE, textSize);
        edit.commit();
    }

    public static int getSettingContentTextSize(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        int result = sp.getInt(CONTENT_TEXT_SIZE, Common.CONTENT_TEXT_SIZE_NORMAL);
        return result;
    }

    /******************** 设置中用到的SharedPreference end ************************/

    public static String getToken(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(TOKEN, "");
    }

    public static void saveToken(Context context, String token) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public static String getUserId(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(USER_ID, "");
    }

    public static void saveUserId(Context context, String userId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    public static void saveVoteId(Context context, int voteId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(createKey(Configs.getUserId(context), KEYWORD_VOTE, voteId), true);
        editor.commit();
    }

    public static boolean isSavedVoteId(Context context, long voteId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(createKey(Configs.getUserId(context), KEYWORD_VOTE, voteId), false);
    }

    public static void saveLikeArticle(Context context, long articleId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(createKey(Configs.getUserId(context), KEYWORD_ARTICLE, articleId), true);
        editor.commit();
    }

    public static boolean isSavedLikeArticle(Context context, long articleId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(createKey(Configs.getUserId(context), KEYWORD_ARTICLE, articleId), false);
    }

    public static void saveLikeGift(Context context, long articleId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(createKey(Configs.getUserId(context), KEYWORD_GIFT, articleId), true);
        editor.commit();
    }

    public static boolean isSavedLikeGift(Context context, long articleId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(createKey(Configs.getUserId(context), KEYWORD_GIFT, articleId), false);
    }

    public static void saveLikeShopping(Context context, long articleId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(createKey(Configs.getUserId(context), KEYWORD_SHOPPING, articleId), true);
        editor.commit();
    }

    public static boolean isSavedLikeShopping(Context context, long articleId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(createKey(Configs.getUserId(context), KEYWORD_SHOPPING, articleId), false);
    }

    public static void doneGuide(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY_GUIDE, true);
        editor.commit();
    }

    public static boolean needGuide(Context context) {
        // SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        // return !sp.getBoolean(KEY_GUIDE, false);
        return false;
    }

    private static String createKey(String userId, String keyword, long id) {
        String key = userId + "_" + keyword + "_" + id;
        if (TextUtils.isEmpty(userId)) {
            key = "anonymity_" + keyword + "_" + id;
        }
        return key;
    }

    public static boolean getWifiDownloadStatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getBoolean(WIFI_DOWNLOAD_STATUS, true);
    }

    public static void setWifiDownloadStatus(Context context, boolean b) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(WIFI_DOWNLOAD_STATUS, b);
        editor.commit();
    }

    public static String getUploadToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getString(KEY_UPLOAD_TOKEN, "");
    }

    public static void saveUploadToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_UPLOAD_TOKEN, token);
        editor.commit();
    }

    public static void saveChatZan(Context context, long articleId, long chatId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEYWORD_ZAN + articleId + chatId, true);
        editor.commit();
    }

    public static boolean isSavedChatZan(Context context, long articleId, long chatId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(KEYWORD_ZAN + articleId + chatId, false);
    }
}