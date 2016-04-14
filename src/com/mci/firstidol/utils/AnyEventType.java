package com.mci.firstidol.utils;

/**
 * 事件总线
 */
public class AnyEventType {
    public static  final int Type_Default = 0;

//    public static  final int Type_ReFreshProvice = 1;
//    public static  final int Type_GetCity = 2;
//    public static  final int Type_WeixinResult = 3;
//    public static  final int Type_GetUserInfo_AndBind = 4;
//    public static final int Type_Fragment_GetInfo = 5;
//    public static  final int Type_RefreshGrid = 6;
//    public static  final int Type_ChooseTeacher_Change = 7;
//    public static  final int Type_ShowAlert = 7;
    public String mid;
    public String message;
    public String info;
    public int Type;
    public AnyEventType(String message){
        this.message = message;
    }
    public AnyEventType(String message,int Type){
        this.message = message;
        this.Type = Type;
    }

    public AnyEventType(String message,String info){
        this.message = message;
        this.info = info;
    }
 }