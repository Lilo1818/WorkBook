package com.mci.firstidol.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.UserModel;
import com.mci.firstidol.model.UserRequest;
import com.mci.firstidol.model.UserRequest.LoginRequest;
import com.mci.firstidol.model.UserRequest.PWDUpdateRequest;
import com.mci.firstidol.model.UserRequest.RegUser;
import com.mci.firstidol.model.UserRequest.RegisterRequest;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.utils.LogUtils;
import com.mci.firstidol.utils.PreferencesUtils;
import com.mci.firstidol.utils.ValidateHelper;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.ToastUtils;

public class RegisterActivity extends BaseActivity implements OnClickListener{

	EditText et_phone,et_code,et_nickName,et_pwd,et_confirmPWD;
	Button btn_send_code,btn_save;
	LinearLayout ll_register_main;
	
	private boolean isForgotPwd;//来自忘记密码
	private String validateCode;//验证码
	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.layout_register;
	}

	@Override
	protected void initNavBar() {
		// TODO Auto-generated method stub
		hideNavBar();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		btn_send_code.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		ll_register_main.setOnClickListener(this);
		
	}

	@Override
	public void rightNavClick() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_code = (EditText) findViewById(R.id.et_code);
		et_nickName = (EditText) findViewById(R.id.et_nickname);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		et_confirmPWD = (EditText) findViewById(R.id.et_confirm_pwd);
		
		btn_send_code = (Button) findViewById(R.id.btn_send_code);
		btn_save = (Button) findViewById(R.id.btn_save);
		
		ll_register_main = (LinearLayout) findViewById(R.id.ll_register_main);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		Bundle bundle = getIntent().getExtras();
		isForgotPwd = bundle.getBoolean(Constant.IntentKey.isForgotPwd,false);
		
		if (isForgotPwd) {
			et_nickName.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_send_code:
			getVerifyCode();
			break;
		case R.id.btn_save:
			validateInput();
			break;
		case R.id.ll_register_main:
			hideKeyboard();
			break;

		default:
			break;
		}
	}
	
	private void validateInput() {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(et_phone.getText().toString())) {
			ToastUtils.showCustomToast("手机号不能为空");
			return;
		} else if (TextUtils.isEmpty(et_code.getText().toString())) {
			ToastUtils.showCustomToast("验证码不能为空");
			return;
		} else  if (TextUtils.isEmpty(et_pwd.getText().toString())) {
			ToastUtils.showCustomToast("密码不能为空");
			return;
		} else if (TextUtils.isEmpty(et_confirmPWD.getText().toString())) {
			ToastUtils.showCustomToast("确认密码不能为空");
			return;
		} else if (!et_pwd.getText().toString().equals(et_confirmPWD.getText().toString())) {
			ToastUtils.showCustomToast("两次密码输入不一致");
			return;
		} else if(TextUtils.isEmpty(validateCode)){
			ToastUtils.showCustomToast("没有获取验证码");
			return;
		} else if(!validateCode.equals(et_code.getText().toString())){
			ToastUtils.showCustomToast("验证码不正确");
			return;
		} 
		
		if (!isForgotPwd) {
			if (TextUtils.isEmpty(et_nickName.getText().toString())) {
				ToastUtils.showCustomToast("昵称不能为空");
				return;
			}
		}
		
		if (isForgotPwd) {
			obtainPWDUpdateRequest();
		} else {
			obtainRegisterRequest();
			
		}
		
	}

	/**
	 * 获取验证码
	 */
	@SuppressWarnings("deprecation")
	private void getVerifyCode() {
		String phone = et_phone.getText().toString().trim();
		if(phone.equals("")) {
			ToastUtils.showCustomToast("手机号不能为空");
		} else {
			/*if(ValidateHelper.isMobile(phone)) {*/
				btn_send_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_code_disable));;
				startDownTimer();
				
				btn_send_code.setEnabled(false);
				
				obtainCodeRequest();
			/*} else {
				ToastUtils.showCustomToast("手机号格式不正确");
			}*/
		}
	}
	
	private int count = -1;
	private boolean startTimer = false;
	private void startDownTimer() {
		count = 30;
		startTimer = true;
		
		new Thread() {
			public void run() {
				while (startTimer) {
					try {
						Message msg = new Message();
						msg.what = 4;
						if (count == 0) {
							startTimer = false;
							msg.obj = "时间到！";
						} else {
							msg.obj = String.format("%2d", count)+"秒后可重发";
						}
						
						mHandler.sendMessage(msg);
						
						sleep(1000);
						count--;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			int what = msg.what;
			switch (what) {
			case 4: // 发送验证码
				String timer = (String) msg.obj;
				if(timer.equals("时间到！")) {
					btn_send_code.setEnabled(true);
					btn_send_code.setText("重获验证码");
//					btn_send_code.setBackgroundColor(Color.parseColor("#3394e5"));
//					btnResend.setTextColor(Color.WHITE);
					btn_send_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_code_normal));
				} else {
					btn_send_code.setText(timer);
				}
				break;
			
			default:
				break;
			}
		}
		
	};
	
	
	
	public void obtainCodeRequest(){
		BaseRequest request = new BaseRequest();
		//请求地址
		String requestID = String.format(isForgotPwd ? Constant.RequestContstants.Request_User_RetrievePasswordCode :Constant.RequestContstants.Request_User_ValidateCode, et_phone.getText().toString().trim());		
		
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
				validateCode = result.replace("\"", "");
			}
		});
	}
	
	//regUser":{"UserName":"username","Password":"password","NickName":"nickname",” ValidateCode”:” ValidateCode”}
	public void obtainRegisterRequest(){
		RegisterRequest request = UserRequest.registerRequest();//其它参数需要继承此类
		request.requestMethod = Constant.Request_POST;
		RegUser regUser = UserRequest.regUser();
		regUser.UserName = et_phone.getText().toString().trim();
		regUser.Password = et_pwd.getText().toString().trim();
		regUser.NickName = et_nickName.getText().toString().trim();
		regUser.ValidateCode = et_code.getText().toString().trim();
		
		request.regUser = regUser;
		//请求地址
		String requestID = isForgotPwd? Constant.RequestContstants.Request_User_UpdatePassword : Constant.RequestContstants.Request_User_Register;		
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
				
				
				
				ToastUtils.showCustomToast("注册成功");
				DataManager.isFinish = true;
				
				UserModel userModel = (UserModel) GsonUtils.jsonToBean(result, UserModel.class);
				dataManager.userModel = userModel;
				dataManager.isLogin = true;
				
				PreferencesUtils.putComplexObject(getApplicationContext(), "UserModel", userModel);
				PreferencesUtils.putBoolean(getApplicationContext(), "IsLogin", true);
				
				finishActivity(RegisterActivity.this);
			}
		});
	}

	
	public void obtainPWDUpdateRequest(){
		PWDUpdateRequest request = UserRequest.pwdUpdateRequest();//其它参数需要继承此类
		request.requestMethod = Constant.Request_POST;
		request.userName = et_phone.getText().toString().trim();
		request.password = et_pwd.getText().toString().trim();
		request.validateCode = et_code.getText().toString().trim();
		
		//请求地址
		String requestID =  Constant.RequestContstants.Request_User_UpdatePassword ;		
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
				
				ToastUtils.showCustomToast("密码重置成功，请重新登录");
				DataManager.isFinish = false;
				
				dataManager.userModel = null;
				dataManager.isLogin = false;
				
				PreferencesUtils.putComplexObject(getApplicationContext(), "UserModel", null);
				PreferencesUtils.putBoolean(getApplicationContext(), "IsLogin", false);
				
				
				finishActivity(RegisterActivity.this);
			}
		});
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
//		if(keyCode == KeyEvent.KEYCODE_ENTER){  
//			return true;    
//		}
		return super.onKeyDown(keyCode, event);
	}
}
