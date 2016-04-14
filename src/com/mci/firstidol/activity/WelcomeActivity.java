package com.mci.firstidol.activity;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import cn.sharesdk.framework.ShareSDK;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.UserModel;
import com.mci.firstidol.model.UserRequest;
import com.mci.firstidol.model.UserRequest.LoginRequest;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.DateHelper;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.utils.LogUtils;
import com.mci.firstidol.utils.PreferencesUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.ToastUtils;

public class WelcomeActivity extends BaseActivity {

	
	
	
	@Override
	protected int getViewId() {
		return R.layout.layout_welcome;
	}

	@Override
	protected void initNavBar() {
		hideNavBar();

	}

	@Override
	protected void initView() {

	}

	@Override
	public void rightNavClick() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {

		// ====== ShareSDK START ======
		ShareSDK.initSDK(this);
		ShareSDK.setConnTimeout(20000);
		ShareSDK.setReadTimeout(20000);
		// ====== ShareSDK END ========

//		new Handler().postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
////				 toLogin();
//				
//				
//				
//				goToMain();
//			}
//		}, 3000);

		
		
		//在此请求广告接口
		obtainSplashEvent();
		
	}

	private void obtainSplashEvent() {
		// TODO Auto-generated method stub
		
		final long startTime = System.currentTimeMillis();//开始毫秒
		
		BaseRequest request = new BaseRequest();//其它参数需要继承此类
		//请求地址
		String requestID = Constant.RequestContstants.Request_Start_Splash;		
		doAsync(false, requestID, request, new Callback<Exception>() {
			
			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
//				ToastUtils.showCustomToast(e.getLocalizedMessage());
				//请求失败，则判断是否有历史数据保存，有的话加载历史的广告，否则直接跳过广告页
				
				long endTime = System.currentTimeMillis();//结束秒数
				long time = endTime - startTime;
				if (time>=3000) {//查过3秒
					flashErrorEvent();
				} else {//未超过3秒
					//睡眠3-(endTime - startTime)
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							flashErrorEvent();
						}
					}, 3000-time);
				}
				
				
				
				
			}
		}, new Callback<String>() {
			
			@SuppressWarnings("static-access")
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				//获取停留时间和图片地址
				double stayTime = (double) GsonUtils.getJsonValue(result, "StayTime");
				String path = (String) GsonUtils.getJsonValue(result, "Path");
				
				//保存
				PreferencesUtils.putFloat(context, "StayTime", (float)stayTime);
				PreferencesUtils.putString(context, "Path", path);
				
				long endTime = System.currentTimeMillis();//结束毫秒数
				long time = endTime - startTime;
				if (time>=3000) {//查过3秒，直接跳转到广告页
					
					boolean isFirstStart = PreferencesUtils.getBoolean(context, "ISFirstStartApp");//首次启动 APP ，跳转到导航页
					if (!isFirstStart) {//
						PreferencesUtils.putBoolean(context, "ISFirstStartApp", true);
						goToNavgationPage();
					} else {
						goToFlash();
					}
				} else {
					//睡眠3-(endTime - startTime)
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							boolean isFirstStart = PreferencesUtils.getBoolean(context, "ISFirstStartApp");//首次启动 APP ，跳转到导航页
							if (!isFirstStart) {//
								PreferencesUtils.putBoolean(context, "ISFirstStartApp", true);
								
								goToNavgationPage();
							} else {
								goToFlash();
							}
							
						}

						
					}, 3000-time);
				}
				
			}
		});
	}
	
	
	public void flashErrorEvent(){
		String path = PreferencesUtils.getString(context, "Path");
		boolean isFirstStart = PreferencesUtils.getBoolean(context, "ISFirstStartApp");//首次启动 APP ，跳转到导航页
		if (TextUtils.isEmpty(path)) {//无值
			if (!isFirstStart) {//首次加载
				PreferencesUtils.putBoolean(context, "ISFirstStartApp", true);
				goToNavgationPage();
			} else {
				goToMain();
			}
		} else {
			if (!isFirstStart) {//首次加载
				PreferencesUtils.putBoolean(context, "ISFirstStartApp", true);
				goToNavgationPage();
			} else {
				goToFlash();
			}
		}
	}
	
	/**
	 * 跳转到广告页
	 */
	private void goToFlash() {
		// TODO Auto-generated method stub
		exitThisOnly();
		Utily.go2Activity(context, FlashActivity.class);
		
//		Intent intent = new Intent(context, FlashActivity.class);
//		startActivity(intent);
//		exitThisOnly();
	}
	
	/**
	 * 跳转到导航页
	 */
	private void goToNavgationPage() {
		// TODO Auto-generated method stub
		exitThisOnly();
		//Utily.go2Activity(context, EasyNavgationPageActivity.class);
		Utily.go2Activity(context, NavgationPageTActivity.class);
		
//		exitThisOnly();
	}

	/**
	 * 跳往主页面
	 */
	public void goToMain() {
		exitThisOnly();
		Utily.go2Activity(context, MainActivity.class, null, null);
//		exitThisOnly();
	}

	/**
	 * 登录方法
	 */
	public void toLogin() {
		if (dataManager.userModel==null) {
			return;
		}
		
		LoginRequest request = UserRequest.loginRequest();//其它参数需要继承此类
		request.requestMethod = "post";//默认为get
		request.setUserName(dataManager.userModel.UserName);
		request.setPassword(dataManager.userModel.Password);
//		18625081989   密: 19891117
		//请求地址
		String requestID = Constant.RequestContstants.Request_User_Login;		
		doAsync(true, requestID, request, new Callback<Exception>() {
			
			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
				
			}
		}, new Callback<String>() {
			
			@SuppressWarnings("static-access")
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast("登录成功");
				
				UserModel userModel = (UserModel) GsonUtils.jsonToBean(result, UserModel.class);
				dataManager.userModel = userModel;
				dataManager.isLogin = true;
				
				PreferencesUtils.putComplexObject(getApplicationContext(), "UserModel", userModel);
				PreferencesUtils.putBoolean(getApplicationContext(), "IsLogin", true);
				LogUtils.i(result);
				
			}
		});
		
		
		
//		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("userName", "18625081989");
//		params.put("password", "12345678");
//
//		String paramsStr = Utily.map2json(params);
//
//		ConnectionService.getInstance().serviceConn(context,
//				Constant.RequestContstants.Request_User_Login, paramsStr,
//				new StringCallBack() {
//
//					@Override
//					public void getSuccessString(String Result) {
//						try {
//							JSONObject jsonObject = new JSONObject(Result);
//							boolean check = parseJson.isCommon(jsonObject);
//							if (check) {
//								DataManager.getInstance().userModel = (UserModel) parseJson
//										.getModelObject(jsonObject,
//												UserModel.class);
//								DataManager.getInstance().isLogin = true;
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//						goToMain();
//					}
//
//					@Override
//					public void getError() {
//						goToMain();
//					}
//				});

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

	}

}
