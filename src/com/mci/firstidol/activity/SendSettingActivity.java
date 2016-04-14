package com.mci.firstidol.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;

public class SendSettingActivity extends BaseActivity implements OnClickListener{

	private ImageView image_accept_notice;//接受新通知
	private ImageView image_notice_info;//显示通知详情
	
	@Override
	protected int getViewId() {
		return R.layout.activity_sendsetting;
	}

	@Override
	protected void initNavBar() {
		setTitle(R.string.setting_push);
	}

	@Override
	protected void initView() {
		image_accept_notice = (ImageView) findViewById(R.id.image_accept_notice);
		image_notice_info = (ImageView) findViewById(R.id.image_notice_info);
		
		image_accept_notice.setOnClickListener(this);
		image_notice_info.setOnClickListener(this);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_accept_notice:
			
			break;
		case R.id.image_notice_info:
			
			break;
		default:
			break;
		}
		
	}

}
