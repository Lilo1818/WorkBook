package com.mci.firstidol.activity;

import android.content.Intent;
import android.database.CursorJoiner.Result;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.SquareRequest.ArticleComment;
import com.mci.firstidol.model.SquareRequest.Events;
import com.mci.firstidol.model.SquareRequest.SquareFoundDetaiCommentAddRequest;
import com.mci.firstidol.model.SquareRequest.SquareWelfareDetailJoinRequest;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.ToastUtils;

public class SquareWelfareDetailJoinActivity extends BaseActivity implements OnClickListener{

	private EditText et_name,et_phone,et_address;
	private ImageButton ib_join;
	public SquareWelfareDetailJoinActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.layout_square_welfare_detail_join;
	}

	@Override
	protected void initNavBar() {
		// TODO Auto-generated method stub
		setTitle(R.string.welfare_detail_join);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		ib_join.setOnClickListener(this);
	}

	@Override
	public void rightNavClick() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		et_name = (EditText) findViewById(R.id.et_name);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_address = (EditText) findViewById(R.id.et_address);
		ib_join = (ImageButton) findViewById(R.id.ib_join);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (!dataManager.isLogin) {
			Utily.go2Activity(context, LoginActivity.class);
			return;
		}
		
		//校验
		if (TextUtils.isEmpty(et_name.getText().toString())) {
			ToastUtils.showCustomToast("姓名不能为空!");
			return;
		} else if (TextUtils.isEmpty(et_phone.getText().toString())) {
			ToastUtils.showCustomToast("手机号码不能为空!");
			return;
		} else if (!isMobileNO(et_phone.getText().toString())) {
			ToastUtils.showCustomToast("请输入正确的手机号!");
			return;
		} else if (TextUtils.isEmpty(et_address.getText().toString())) {
			ToastUtils.showCustomToast("地址不能为空!");
			return;
		}
		obtainFoundDetailCommentAddRequest();
		
	}
	
	/** 
	 * 验证手机格式 
	 */  
	public static boolean isMobileNO(String mobiles) {  
	    /* 
	    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 
	    联通：130、131、132、152、155、156、185、186 
	    电信：133、153、180、189、（1349卫通） 
	    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9 
	    */  
	    String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
	    if (TextUtils.isEmpty(mobiles)) return false;  
	    else return mobiles.matches(telRegex);  
	   }  
	
	
	
	/**
	 * 广场-福利详情-参加活动
	 * @param isCancelFav
	 */
	public void obtainFoundDetailCommentAddRequest(){
		SquareWelfareDetailJoinRequest request = SquareRequest.squareWelfareDetailJoinRequest();
		request.requestMethod = Constant.Request_POST;
		
		Events events = SquareRequest.events();
		events.RealName = et_name.getText().toString();
		events.Tel = et_phone.getText().toString();
		events.ArticleId = getIntent().getExtras().getLong(Constant.IntentKey.articleID);
		events.DeviceType = 1;
		events.Bd_ChannelId = dataManager.bd_channelId;
		events.Bd_UserId = dataManager.bd_userId;
		events.Address = et_address.getText().toString();
				
		request.events = events;
		//请求地址
		String requestID = Constant.RequestContstants.Request_Square_Events_Join;		
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
				//成功时
				ToastUtils.showCustomToast("参与成功");
				Intent intent = new Intent();
                intent.putExtra("join_success", true);
                setResult(RESULT_OK, intent);
				finishActivity(SquareWelfareDetailJoinActivity.this);
			}
		});
	}

}
