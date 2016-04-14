package com.mci.firstidol.activity;

import android.widget.EditText;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.utils.AnyEventType;

import de.greenrobot.event.EventBus;

public class MyShowActivity extends BaseActivity {

	private EditText text_content;//内容
	
	@Override
	protected int getViewId() {
		return R.layout.activity_myshow;
	}

	@Override
	protected void initNavBar() {
		setTitle(R.string.myShow);
		setRightBtnBackgroundResource(R.drawable.done);
	}

	@Override
	protected void initView() {
		text_content = (EditText) findViewById(R.id.edit_content);
	}

	@Override
	public void rightNavClick() {
		String content = text_content.getText().toString();
		if(content!=null&&!content.equals("")){
			updateShowText(content);
			finishActivity(this);
		}
	}
	
	/**
	 * 更新文本
	 */
	public void updateShowText(String info){
		AnyEventType type = new AnyEventType(Constant.Config.UPDATE_INFO,
				AnyEventType.Type_Default);
		type.info = info;
		EventBus.getDefault().post(type);
	}

	@Override
	protected void findViewById() {

	}

	@Override
	protected void initData() {

	}

}
