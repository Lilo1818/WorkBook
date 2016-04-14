package com.mci.firstidol.activity;

import java.util.HashMap;

import org.json.JSONObject;

import android.widget.EditText;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.ToastUtils;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;

public class UploadSuggestionActivity extends BaseActivity{

	private EditText text_content;//内容文本
	
	@Override
	protected int getViewId() {
		return R.layout.activity_suggestion;
	}

	@Override
	protected void initNavBar() {
		setTitle(R.string.setting_tucao);
		setRightBtnBackgroundResource(R.drawable.done);
	}

	@Override
	protected void initView() {
		text_content = (EditText) findViewById(R.id.edit_content);
	}

	@Override
	public void rightNavClick() {
		String content = text_content.getText().toString().trim();
		if(content!=null&&!content.equals("")){
			uploadSuggestion(content);
		}else{
			ToastUtils.showCustomToast(context, "请输入吐槽内容");
		}
	}

	@Override
	protected void findViewById() {
		
	}

	@Override
	protected void initData() {
		
	}
	
	/**
	 * 上传吐槽内容
	 * @param content
	 */
	public void uploadSuggestion(String content){
		String processURL = Constant.RequestContstants.Request_upload_tucao;
		pd.setMessage("正在吐槽");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		
		HashMap<String, String> params = new HashMap<String,String>();
		if(DataManager.userModel!=null){
			params.put("UserId",String.valueOf(DataManager.userModel.UserId));
			params.put("Email", DataManager.userModel.Email);
		}
		params.put("Content", content);
		
		String paramStr = "{\"feedback\":"+Utily.map2json(params)+"}";
		ConnectionService.getInstance().serviceConn(context, processURL, paramStr, new StringCallBack() {
			
			@Override
			public void getSuccessString(String Result) {
				baseHandler.sendEmptyMessage(HIDEDIALOG);
				try {
					JSONObject jsonObject = new JSONObject(Result);
					boolean isCommon = parseJson.isCommon(jsonObject);
					if(isCommon){
						ToastUtils.showCustomToast(context, "吐槽成功");
						exitThis();
					}else{
						ToastUtils.showCustomToast(context, "吐槽失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastUtils.showCustomToast(context, "吐槽失败");
				}
				
			}
			
			@Override
			public void getError() {
				baseHandler.sendEmptyMessage(HIDEDIALOG);
				ToastUtils.showCustomToast(context, "吐槽失败");
			}
		});
		
	}

}
