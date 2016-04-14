package com.mci.firstidol.activity;

import java.util.HashMap;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.ToastUtils;

public class ApplyMasterActivity extends BaseActivity implements
		OnClickListener {

	private EditText edit_mail;// 邮箱
	private EditText edit_qq;// qq
	private EditText edit_address;// 所在地
	private EditText edit_reason;// 申请理由

	private Button btn_apply;// 确认申请

	@Override
	protected int getViewId() {
		return R.layout.activity_apply_master;
	}

	@Override
	protected void initNavBar() {
		setTitle(R.string.apply_master);
	}

	@Override
	protected void initView() {
		edit_address = (EditText) findViewById(R.id.edit_address);
		edit_mail = (EditText) findViewById(R.id.edit_email);
		edit_qq = (EditText) findViewById(R.id.edit_qq);
		edit_reason = (EditText) findViewById(R.id.edit_reason);
		btn_apply = (Button) findViewById(R.id.btn_apply);

		btn_apply.setOnClickListener(this);
	}

	@Override
	public void rightNavClick() {

	}

	@Override
	protected void findViewById() {

	}

	@Override
	protected void initData() {

	}
	
	/**
	 * 审亲版主事件
	 */
	public void applyMasterClick(){
		String emailStr = edit_mail.getText().toString().trim();
		String qqStr = edit_qq.getText().toString().trim();
		String addressStr = edit_address.getText().toString().trim();
		String reasonStr = edit_reason.getText().toString().trim();
		
		boolean check = true;
		if(emailStr==null||emailStr.equals("")){
			check = false;
			ToastUtils.showCustomToast(context, "请输入邮箱");
			return;
		}
		
		if(qqStr==null||qqStr.equals("")){
			check = false;
			ToastUtils.showCustomToast(context, "请输入qq");
			return;
		}
		
		if(addressStr==null||addressStr.equals("")){
			check = false;
			ToastUtils.showCustomToast(context, "请输入所在地");
			return;
		}
		
		if(reasonStr ==null||reasonStr.equals("")){
			check = false;
			ToastUtils.showCustomToast(context, "请输入理由");
			return;
		}
		
		if(check){
			applyMaster(qqStr, emailStr, addressStr, reasonStr);
		}
		
	}

	/**
	 * 申请版主请求
	 * @param qqStr
	 * @param emailStr
	 * @param addressStr
	 * @param reasonStr
	 */
	public void applyMaster(String qqStr,String emailStr,String addressStr,String reasonStr) {
		pd.setMessage("申请版主");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		String processURL = Constant.RequestContstants.Request_apply_master;
		
		HashMap<String, String> params = new HashMap<String, String>();
		if (Constant.starModel != null) {
			params.put("StarId", String.valueOf(Constant.starModel.starId));
		}
		params.put("QQ", qqStr);
		params.put("Email", emailStr);
		params.put("Address", addressStr);
		params.put("Remark", reasonStr);

		String paramStr = Utily.map2json(params);
		paramStr = "{\"moderator\":" + paramStr + "}";

		ConnectionService.getInstance().serviceConn(context, processURL,
				paramStr, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean isCommon = parseJson.isCommon(jsonObject);
							if (isCommon) {
								ToastUtils.showCustomToast(context, "提交申请成功");
								exitThis();
							} else {
								String errorMsg = parseJson.getErrorMessage(jsonObject);
								ToastUtils.showCustomToast(context, errorMsg);
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCustomToast(context, "提交申请失败");
						}
					}

					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						ToastUtils.showCustomToast(context, "提交申请失败");
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_apply:
			applyMasterClick();
			break;
		default:
			break;
		}

	}

}
