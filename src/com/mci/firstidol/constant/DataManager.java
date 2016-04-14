package com.mci.firstidol.constant;

import com.mci.firstidol.BaseApp;
import com.mci.firstidol.model.UserModel;
import com.mci.firstidol.utils.PreferencesUtils;

public class DataManager {

	public static boolean isLogin;
	public static UserModel userModel;
	
	public static String bd_channelId;
	public static String bd_userId;
	
	public static boolean isFinish;//上一个页面是否需要关闭
	
	static DataManager instance;
	public static synchronized DataManager getInstance(){  
        if(instance == null) {
            instance = new DataManager();  
        }
        return instance;  
    }  
	
	public DataManager() {
		super();
		
		if (userModel == null) {
			userModel = (UserModel) PreferencesUtils.getComplexObject(BaseApp.getInstance().getApplicationContext(), "UserModel");
			isLogin = PreferencesUtils.getBoolean(BaseApp.getInstance().getApplicationContext(), "IsLogin");
		}
	}

}
