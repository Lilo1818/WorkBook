
package com.mci.firstidol.fragment.welfare;

import java.io.File;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 文章
 */
public class ArticlesContent {
    protected static interface ArticleColumns extends BaseColumns {
        static final String AD_ID = "ad_id";
        static final String ADMIN_ID = "admin_id";
        static final String ARTICLE_ID = "article_id";
        static final String ARTICLE_TYPE = "article_type";
        static final String AUTHOR = "author";
        static final String CHANNEL_ID = "channel_id";
        static final String CONTENT = "content";
        static final String CREATE_DATE = "create_date";
        static final String GROUP_PIC = "group_pic";
        static final String ICO = "ico";
        static final String ICO2 = "ico2";
        static final String ICO_HEIGHT = "ico_height";
        static final String ICO_WIDTH = "ico_width";
        static final String INSPECIAL = "inspecial";
        static final String IS_TOP = "is_top";
        static final String ITEM_ID = "item_id";
        static final String MAGAZINE_ID = "magazine_id";
        static final String MODEL_TYPE = "model_type";
        static final String PRESS_ID = "press_id";
        static final String PRICE = "price";
        static final String PRODUCT_LINK = "product_link";
        static final String PUBLISH_DATE = "publish_date";
        static final String SHOW_STYLE = "show_style";
        static final String SOURCE = "source";
        static final String STATE = "state";
        static final String SUMMARY = "summary";
        static final String TAGS = "tags";
        static final String TIPS = "tips";
        static final String TITLE = "title";
        static final String TYPE = "type";
        static final String VIEW_COUNT = "view_count";
        static final String VOTE_ID = "vote_id";
        static final String COMMENT_COUNT = "comment_count";
        static final String CONTENT_PICS = "content_pics";
        static final String ISSUE = "issue";
        static final String ITEM_COVER = "item_cover";
        static final String ITEM_DESCRIPTION = "item_description";
        static final String ITEM_TITLE = "item_title";
        static final String ITEM_YEAR = "item_year";
        static final String JSON_STRING = "json_string";
        static final String CHANNEL_NAME = "channel_name";
        static final String UPCOUNT = "upcount";
        static final String FLAGS = "flags";
        static final String READ_TIME = "read_time";
        static final String SUB_TITLE = "sub_title";
        static final String H_TITLE = "h_title";// 推荐首页标题
        static final String HAS_JOIN_EVENTS = "has_join_events";
        static final String HAS_VOTED = "has_voted";
        static final String EVENTS_STATE = "events_state";
        static final String CHAT_PEOPLE_COUNT = "chat_people_count";
        static final String HOME_CHANNEL_ID = "home_channel_id";
        static final String ICO_EX2 = "ico_ex2";
        static final String ICO_EX3 = "ico_ex3";
        static final String IS_RAFFLE = "is_raffle";
        static final String RAFFLE_GAME_URL = "raffle_game_url";
        static final String EVENTS_START = "events_start";
    }

    public static final class ArticlesInfo implements ArticleColumns {
        public static final String TABLE_NAME = "articles";
//        public static final Uri CONTENT_URI = Uri.parse(EasyProvider.CONTENT_URI + File.separator + TABLE_NAME);

        public static final int ID_COLUMN = 0;
        public static final int AD_ID_COLUMN = 1;
        public static final int ADMIN_ID_COLUMN = 2;
        public static final int ARTICLE_ID_COLUMN = 3;
        public static final int ARTICLE_TYPE_COLUMN = 4;
        public static final int AUTHOR_COLUMN = 5;
        public static final int CHANNEL_ID_COLUMN = 6;
        public static final int CONTENT_COLUMN = 7;
        public static final int CREATE_DATE_COLUMN = 8;
        public static final int GROUP_PIC_COLUMN = 9;
        public static final int ICO_COLUMN = 10;
        public static final int ICO_HEIGHT_COLUMN = 11;
        public static final int ICO_WIDTH_COLUMN = 12;
        public static final int INSPECIAL_COLUMN = 13;
        public static final int IS_TOP_COLUMN = 14;
        public static final int ITEM_ID_COLUMN = 15;
        public static final int MAGAZINE_ID_COLUMN = 16;
        public static final int MODEL_TYPE_COLUMN = 17;
        public static final int PRESS_ID_COLUMN = 18;
        public static final int PRICE_COLUMN = 19;
        public static final int PRODUCT_LINK_COLUMN = 20;
        public static final int PUBLISH_DATE_COLUMN = 21;
        public static final int SHOW_STYLE_COLUMN = 22;
        public static final int SOURCE_COLUMN = 23;
        public static final int STATE_COLUMN = 24;
        public static final int SUMMARY_COLUMN = 25;
        public static final int TAGS_COLUMN = 26;
        public static final int TIPS_COLUMN = 27;
        public static final int TITLE_COLUMN = 28;
        public static final int TYPE_COLUMN = 29;
        public static final int VIEW_COUNT_COLUMN = 30;
        public static final int VOTE_ID_COLUMN = 31;
        public static final int COMMENT_COUNT_COLUMN = 32;
        public static final int CONTENT_PICS_COLUMN = 33;
        public static final int ISSUE_COLUMN = 34;
        public static final int ITEM_COVER_COLUMN = 35;
        public static final int ITEM_DESCRIPTION_COLUMN = 36;
        public static final int ITEM_TITLE_COLUMN = 37;
        public static final int ITEM_YEAR_COLUMN = 38;
        public static final int JSON_STRING_COLUMN = 39;
        public static final int CHANNEL_NAME_COLUMN = 40;
        public static final int UPCOUNT_COLUMN = 41;
        public static final int FLAGS_COLUMN = 42;
        public static final int READ_TIME_COLUMN = 43;
        public static final int SUB_TITLE_COLUMN = 44;
        public static final int H_TITLE_COLUMN = 45;
        public static final int HAS_JOIN_EVENTS_COLUMN = 46;
        public static final int HAS_VOTED_COLUMN = 47;
        public static final int ICO2_COLUMN = 48;
        public static final int EVENTS_STATE_COLUMN = 49;
        public static final int CHAT_PEOPLE_COUNT_COLUMN = 50;
        public static final int HOME_CHANNEL_ID_COLUMN = 51;
        public static final int ICO_EX2_COLUMN = 52;
        public static final int ICO_EX3_COLUMN = 53;
        public static final int IS_RAFFLE_COLUMN = 54;
        public static final int RAFFLE_GAME_URL_COLUMN = 55;
        public static final int EVENTS_START_COLUMN = 56;

        public static final String FLAG_RECOMMEND = "c";

        public static final int FLAG_NOT_JOIN_EVENTS = 0;
        public static final int FLAG_HAS_JOIN_EVENTS = 1;

        public static final int FLAG_NOT_VOTED = 0;
        public static final int FLAG_HAS_VOTED = 1;

        public static final String[] CONTENT_PROJECTION = new String[] {
            ArticlesInfo._ID,
            ArticlesInfo.AD_ID,
            ArticlesInfo.ADMIN_ID,
            ArticlesInfo.ARTICLE_ID,
            ArticlesInfo.ARTICLE_TYPE,
            ArticlesInfo.AUTHOR,
            ArticlesInfo.CHANNEL_ID,
            ArticlesInfo.CONTENT,
            ArticlesInfo.CREATE_DATE,
            ArticlesInfo.GROUP_PIC,
            ArticlesInfo.ICO,
            ArticlesInfo.ICO_HEIGHT,
            ArticlesInfo.ICO_WIDTH,
            ArticlesInfo.INSPECIAL,
            ArticlesInfo.IS_TOP,
            ArticlesInfo.ITEM_ID,
            ArticlesInfo.MAGAZINE_ID,
            ArticlesInfo.MODEL_TYPE,
            ArticlesInfo.PRESS_ID,
            ArticlesInfo.PRICE,
            ArticlesInfo.PRODUCT_LINK,
            ArticlesInfo.PUBLISH_DATE,
            ArticlesInfo.SHOW_STYLE,
            ArticlesInfo.SOURCE,
            ArticlesInfo.STATE,
            ArticlesInfo.SUMMARY,
            ArticlesInfo.TAGS,
            ArticlesInfo.TIPS,
            ArticlesInfo.TITLE,
            ArticlesInfo.TYPE,
            ArticlesInfo.VIEW_COUNT,
            ArticlesInfo.VOTE_ID,
            ArticlesInfo.COMMENT_COUNT,
            ArticlesInfo.CONTENT_PICS,
            ArticlesInfo.ISSUE,
            ArticlesInfo.ITEM_COVER,
            ArticlesInfo.ITEM_DESCRIPTION,
            ArticlesInfo.ITEM_TITLE,
            ArticlesInfo.ITEM_YEAR,
            ArticlesInfo.JSON_STRING,
            ArticlesInfo.CHANNEL_NAME,
            ArticlesInfo.UPCOUNT,
            ArticlesInfo.FLAGS,
            ArticlesInfo.READ_TIME,
            ArticlesInfo.SUB_TITLE,
            ArticlesInfo.H_TITLE,
            ArticlesInfo.HAS_JOIN_EVENTS,
            ArticlesInfo.HAS_VOTED,
            ArticlesInfo.ICO2,
            ArticlesInfo.EVENTS_STATE,
            ArticlesInfo.CHAT_PEOPLE_COUNT,
            ArticlesInfo.HOME_CHANNEL_ID,
            ArticlesInfo.ICO_EX2,
            ArticlesInfo.ICO_EX3,
            ArticlesInfo.IS_RAFFLE,
            ArticlesInfo.RAFFLE_GAME_URL,
            ArticlesInfo.EVENTS_START
        };

        static void createTable(SQLiteDatabase db) {
            String sql = "CREATE TABLE " + TABLE_NAME + "("
                            + ArticlesInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + ArticlesInfo.AD_ID + " TEXT,"
                            + ArticlesInfo.ADMIN_ID + " TEXT,"
                            + ArticlesInfo.ARTICLE_ID + " TEXT UNIQUE,"
                            + ArticlesInfo.ARTICLE_TYPE + " TEXT,"
                            + ArticlesInfo.AUTHOR + " TEXT,"
                            + ArticlesInfo.CHANNEL_ID + " TEXT,"
                            + ArticlesInfo.CONTENT + " TEXT,"
                            + ArticlesInfo.CREATE_DATE + " TEXT,"
                            + ArticlesInfo.GROUP_PIC + " TEXT,"
                            + ArticlesInfo.ICO + " TEXT,"
                            + ArticlesInfo.ICO_HEIGHT + " TEXT,"
                            + ArticlesInfo.ICO_WIDTH + " TEXT,"
                            + ArticlesInfo.INSPECIAL + " TEXT,"
                            + ArticlesInfo.IS_TOP + " TEXT,"
                            + ArticlesInfo.ITEM_ID + " TEXT,"
                            + ArticlesInfo.MAGAZINE_ID + " TEXT,"
                            + ArticlesInfo.MODEL_TYPE + " TEXT,"
                            + ArticlesInfo.PRESS_ID + " TEXT,"
                            + ArticlesInfo.PRICE + " TEXT,"
                            + ArticlesInfo.PRODUCT_LINK + " TEXT,"
                            + ArticlesInfo.PUBLISH_DATE + " FLOAT,"
                            + ArticlesInfo.SHOW_STYLE + " TEXT,"
                            + ArticlesInfo.SOURCE + " TEXT,"
                            + ArticlesInfo.STATE + " TEXT,"
                            + ArticlesInfo.SUMMARY + " TEXT,"
                            + ArticlesInfo.TAGS + " TEXT,"
                            + ArticlesInfo.TIPS + " TEXT,"
                            + ArticlesInfo.TITLE + " TEXT,"
                            + ArticlesInfo.TYPE + " TEXT,"
                            + ArticlesInfo.VIEW_COUNT + " TEXT,"
                            + ArticlesInfo.VOTE_ID + " TEXT,"
                            + ArticlesInfo.COMMENT_COUNT + " TEXT,"
                            + ArticlesInfo.CONTENT_PICS + " TEXT,"
                            + ArticlesInfo.ISSUE + " TEXT,"
                            + ArticlesInfo.ITEM_COVER + " TEXT,"
                            + ArticlesInfo.ITEM_DESCRIPTION + " TEXT,"
                            + ArticlesInfo.ITEM_TITLE + " TEXT,"
                            + ArticlesInfo.ITEM_YEAR + " TEXT,"
                            + ArticlesInfo.JSON_STRING + " TEXT,"
                            + ArticlesInfo.CHANNEL_NAME + " TEXT,"
                            + ArticlesInfo.UPCOUNT + " TEXT,"
                            + ArticlesInfo.FLAGS + " TEXT,"
                            + ArticlesInfo.READ_TIME + " FLOAT,"
                            + ArticlesInfo.SUB_TITLE + " TEXT,"
                            + ArticlesInfo.H_TITLE + " TEXT,"
                            + ArticlesInfo.HAS_JOIN_EVENTS + " INTEGER,"
                            + ArticlesInfo.HAS_VOTED + " INTEGER,"
                            + ArticlesInfo.ICO2 + " TEXT,"
                            + ArticlesInfo.EVENTS_STATE + " INTEGER,"
                            + ArticlesInfo.CHAT_PEOPLE_COUNT + " INTEGER,"
                            + ArticlesInfo.HOME_CHANNEL_ID + " INTEGER,"
                            + ArticlesInfo.ICO_EX2 + " TEXT,"
                            + ArticlesInfo.ICO_EX3 + " TEXT,"
                            + ArticlesInfo.IS_RAFFLE + " INTEGER,"
                            + ArticlesInfo.RAFFLE_GAME_URL + " TEXT,"
                            + ArticlesInfo.EVENTS_START + " TEXT" + ");";
            db.execSQL(sql);
        }

        static void dropTable(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        }

        static void addNewColumn(SQLiteDatabase db) {
            db.execSQL("ALTER TABLE articles ADD COLUMN events_state");
            db.execSQL("ALTER TABLE articles ADD COLUMN chat_people_count");
        }

        static void addNewColumn2(SQLiteDatabase db) {
            db.execSQL("ALTER TABLE articles ADD COLUMN home_channel_id");
            db.execSQL("ALTER TABLE articles ADD COLUMN ico_ex2");
            db.execSQL("ALTER TABLE articles ADD COLUMN ico_ex3");
            db.execSQL("ALTER TABLE articles ADD COLUMN is_raffle");
            db.execSQL("ALTER TABLE articles ADD COLUMN raffle_game_url");
        }
        static void addNewColumn3(SQLiteDatabase db) {
            db.execSQL("ALTER TABLE articles ADD COLUMN events_start");
        }

        public static String limit(int limit) {
            return ArticlesInfo.PUBLISH_DATE + " DESC LIMIT " + limit;
        }
    }

    public class TYPE {
        // 普通类型
        public final static int SIMPLE = 0;
        // 专题
        public final static int SPECIAL_TOPIC = SIMPLE + 1;
        // 活动
        public final static int EVENTS = SPECIAL_TOPIC + 1;
    }

    public class MODELTYPE {
        // 普通
        public final static int SIMPLE = 0;
        // 图集
        public final static int PIC_SET = SIMPLE + 1;
        // 视频
        public final static int VIDEO = PIC_SET + 1;
        // 直播
        public final static int CHAT = VIDEO + 1;
    }

    public class SHOW_STYLE {
        // 左图右文
        public final static int TOP_PIC_BOTTOM_TEXT = 1;
        // 上图下文
        public final static int LEFT_PIC_RIGHT_TEXT = TOP_PIC_BOTTOM_TEXT + 1;
    }
}