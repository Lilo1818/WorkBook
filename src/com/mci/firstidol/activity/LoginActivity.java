package com.mci.firstidol.activity;

import java.util.HashMap;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.UserModel;
import com.mci.firstidol.model.UserRequest;
import com.mci.firstidol.model.SquareRequest.ArticleComment;
import com.mci.firstidol.model.SquareRequest.SquareFoundDetaiCommentAddRequest;
import com.mci.firstidol.model.UserRequest.LoginRequest;
import com.mci.firstidol.model.UserRequest.LoginThirdRequest;
import com.mci.firstidol.model.UserRequest.Socialuser;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.utils.LogUtils;
import com.mci.firstidol.utils.PreferencesUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.ToastUtils;
import com.mob.tools.utils.UIHandler;

import de.greenrobot.event.EventBus;

public class LoginActivity extends BaseActivity implements OnClickListener,PlatformActionListener, android.os.Handler.Callback {

	EditText et_username, et_password;
	Button btn_login, btn_register, btn_resetpwd;
	ImageButton ib_wx, ib_wb, ib_qq;
	RelativeLayout rl_login_main;
	
	private Platform currentPlatform;
	private int platform;
	private String appId;
	@Override
	protected void onResume() {
		// TODO Auto-generated meth
		super.onResume();
		
		if (DataManager.isFinish) {
			finishActivity(this);
		}
	}

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.layout_login;
	}

	@Override
	protected void initNavBar() {
		// TODO Auto-generated method stub
		hideNavBar();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		btn_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		btn_resetpwd.setOnClickListener(this);
		ib_wx.setOnClickListener(this);
		ib_wb.setOnClickListener(this);
		ib_qq.setOnClickListener(this);
		rl_login_main.setOnClickListener(this);
	}

	@Override
	public void rightNavClick() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);

		btn_login = (Button) findViewById(R.id.btn_login);
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_resetpwd = (Button) findViewById(R.id.btn_resetpwd);

		ib_wx = (ImageButton) findViewById(R.id.ib_wx);
		ib_wb = (ImageButton) findViewById(R.id.ib_wb);
		ib_qq = (ImageButton) findViewById(R.id.ib_qq);
		
		rl_login_main = (RelativeLayout) findViewById(R.id.rl_login_main);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Bundle bundle;
		switch (v.getId()) {
		case R.id.btn_login:
			obtainLoginRequest();
			break;
		case R.id.btn_register:
			bundle = new Bundle();
//			bundle.putBoolean(Constant.IntentKey.isForgotPwd, false);
			Utily.go2Activity(context, RegisterActivity.class,bundle);
			break;
		case R.id.btn_resetpwd:
			bundle = new Bundle();
			bundle.putBoolean(Constant.IntentKey.isForgotPwd, true);
			Utily.go2Activity(context, RegisterActivity.class, bundle);
			break;
		case R.id.ib_wx:
			intentLoginWeixin();
			break;
		case R.id.ib_wb:
			intentLoginSina();
			break;
		case R.id.ib_qq:
			intentLoginQQ();
			break;
			
		case R.id.rl_login_main:
			hideKeyboard();
			break;

		default:
			break;
		}
	}
	
	private void intentLoginSina() {
		platform = 1;
		appId = "";
		//新浪微博
		Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
		authorize(sina);
	}
	
	private void intentLoginQQ() {
		platform = 3;
		appId = "1101498611";
		//QQ空间
		Platform qzone = ShareSDK.getPlatform(QZone.NAME);
		authorize(qzone);
	}
	
	private void intentLoginWeixin() {
		platform = 4;
		appId = "wxcb4bf8a9c8dbcd1a";
		//微信登录
		//测试时，需要打包签名；sample测试时，用项目里面的demokey.keystore
		//打包签名apk,然后才能产生微信的登录
		Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
		authorize(wechat);
	}
	
	
	public void obtainLoginRequest(){
		LoginRequest request = UserRequest.loginRequest();//其它参数需要继承此类
		request.requestMethod = "post";//默认为get
		request.setUserName(et_username.getText().toString());
		request.setPassword(et_password.getText().toString());
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
				sendLoginNotice();
				Constant.is_selectedChanged = true;
				Constant.is_userinfo_change = true;
				finishActivity(LoginActivity.this);
			}
		});
	}

	/**
	 * 登录成功消息
	 */
	public void sendLoginNotice(){
		AnyEventType type = new AnyEventType(Constant.Config.LOGIN, AnyEventType.Type_Default);
        EventBus.getDefault().post(type);
	}
	
	
	
	// ============== ShareSDK START ==============
	
	
		private static final int MSG_USERID_FOUND = 1;
		private static final int MSG_LOGIN = 2;
		private static final int MSG_AUTH_CANCEL = 3;
		private static final int MSG_AUTH_ERROR= 4;
		private static final int MSG_AUTH_COMPLETE = 5;
		
		private void authorize(Platform plat) {
			plat.removeAccount(); // 移除账号
			
			//判断指定平台是否已经完成授权
			if(plat.isValid()) {
				String userId = plat.getDb().getUserId();
				if (!TextUtils.isEmpty(userId)) {
					UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
					login();
					return;
				}
			}
			plat.setPlatformActionListener(this);
			// true不使用SSO授权，false使用SSO授权
			plat.SSOSetting(false);
			// 获取用户资料
			plat.showUser(null);
		}
		
		private void login() {
			Message msg = new Message();
			msg.what = MSG_LOGIN;
			UIHandler.sendMessage(msg, this);
		}

		@Override
		public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
			//{is_yellow_vip=0, msg=, vip=0, nickname=天上星, figureurl_qq_1=http://q.qlogo.cn/qqapp/1101498611/CF1B0A8BCBC9B000FD5F8E962A526A07/40, city=浦东新区, figureurl_1=http://qzapp.qlogo.cn/qzapp/1101498611/CF1B0A8BCBC9B000FD5F8E962A526A07/50, gender=男, province=上海, is_yellow_year_vip=0, yellow_vip_level=0, figureurl=http://qzapp.qlogo.cn/qzapp/1101498611/CF1B0A8BCBC9B000FD5F8E962A526A07/30, figureurl_2=http://qzapp.qlogo.cn/qzapp/1101498611/CF1B0A8BCBC9B000FD5F8E962A526A07/100, is_lost=0, figureurl_qq_2=http://q.qlogo.cn/qqapp/1101498611/CF1B0A8BCBC9B000FD5F8E962A526A07/100, level=0, ret=0}
			if (action == Platform.ACTION_USER_INFOR) {
				currentPlatform = platform;
				UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
				login();
			}
			
			
//			String loginType = platform.getName();
//			userReferType = platform.getDb().getPlatformNname();
//			userReferId = platform.getDb().getUserId();
//			
//			if(userReferType.equals("QZone")) {
//				userReferType = "1";
//			}
//			
//			new ThirdLoginUserTask().execute();
			
			
			
			// {ret=0, is_yellow_year_vip=0, 
			// figureurl_qq_1=http://q.qlogo.cn/qqapp/100371282/5CEE93FDB809FCF306B041F49D837F0C/40, 
			// nickname=难啊, 
			// figureurl_qq_2=http://q.qlogo.cn/qqapp/100371282/5CEE93FDB809FCF306B041F49D837F0C/100, 
			// yellow_vip_level=0, is_lost=0, msg=, city=苏州, 
			// figureurl_1=http://qzapp.qlogo.cn/qzapp/100371282/5CEE93FDB809FCF306B041F49D837F0C/50, 
			// vip=0, 
			// figureurl_2=http://qzapp.qlogo.cn/qzapp/100371282/5CEE93FDB809FCF306B041F49D837F0C/100, 
			// level=0, province=江苏, gender=男, is_yellow_vip=0, 
			// figureurl=http://qzapp.qlogo.cn/qzapp/100371282/5CEE93FDB809FCF306B041F49D837F0C/30}
//			Log.i(TAG, ""+res);
//			Log.i(TAG, "------User Name ---------" + platform.getDb().getUserName());
//			Log.i(TAG, "------User ID ---------" + platform.getDb().getUserId());
		}
		

		private void obtainThirdLoginRequest(String openId, String token,
				String avatar, String nickName) {
			// TODO Auto-generated method stub
			LoginThirdRequest request = UserRequest.loginThirdRequest();
			request.requestMethod = Constant.Request_POST;
			
			
			Socialuser socialuser = UserRequest.socialuser();
			socialuser.Platform = platform;
			socialuser.OpenId = openId;
			socialuser.AppId = appId;
			socialuser.Token = token;
			socialuser.Avatar = avatar;
			socialuser.NickName = nickName;
					
			request.socialuser = socialuser;
			//请求地址
			String requestID = Constant.RequestContstants.Request_User_Social_Login;		
			doAsync(true, requestID, request, new Callback<Exception>() {
				
				@Override
				public void onCallback(Exception e) {
					// TODO Auto-generated method stub
					ToastUtils.showCustomToast(e.getLocalizedMessage());
				}
			}, new Callback<String>() {
				
				@Override
				public void onCallback(String result) {
					// TODO Auto-generated method stub
					ToastUtils.showCustomToast("登录成功");
					//成功时
					
					UserModel userModel = (UserModel) GsonUtils.jsonToBean(result, UserModel.class);
					dataManager.userModel = userModel;
					dataManager.isLogin = true;
					
					PreferencesUtils.putComplexObject(getApplicationContext(), "UserModel", userModel);
					PreferencesUtils.putBoolean(getApplicationContext(), "IsLogin", true);
					sendLoginNotice();
					Constant.is_selectedChanged = true;
					Constant.is_userinfo_change = true;  
					finishActivity(LoginActivity.this);
				}
			});
		}

		@Override
		public void onCancel(Platform platform, int action) {
			if (action == Platform.ACTION_USER_INFOR) {
				UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
			}
		}

		@Override
		public void onError(Platform platform, int action, Throwable t) {
			if (action == Platform.ACTION_USER_INFOR) {
				UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
			}
			t.printStackTrace();
		}

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_USERID_FOUND: {
				Toast.makeText(this, R.string.userid_found, Toast.LENGTH_SHORT)
						.show();
			}
				break;
			case MSG_LOGIN: {
//				System.out.println("---------------");
//				dialog.setMessage("登录中...");
//				dialog.show();
				String openId = currentPlatform.getDb().getUserId();
				String token = currentPlatform.getDb().getToken();
				String avatar = currentPlatform.getDb().getUserIcon();
				String nickName = currentPlatform.getDb().getUserName();
				obtainThirdLoginRequest(openId,token,avatar,nickName);
			}
				break;
			case MSG_AUTH_CANCEL: {
				Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
				System.out.println("-------MSG_AUTH_CANCEL--------");
			}
				break;
			case MSG_AUTH_ERROR: {
				Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT)
						.show();
				System.out.println("-------MSG_AUTH_ERROR--------");
			}
				break;
			case MSG_AUTH_COMPLETE: {
				Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
//				System.out.println("--------MSG_AUTH_COMPLETE-------");
			}
				break;
			}

			return false;
		}
		
		// ============== ShareSDK END ==============
}
