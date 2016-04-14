package com.mci.firstidol.activity;

import android.content.Intent;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.StarModel;

public class StarActivty extends BaseActivity {

	private StarModel starModel;//明星对象
	
	@Override
	protected int getViewId() {
		Intent intent = getIntent();
		if(intent!=null){
			starModel = (StarModel) intent.getSerializableExtra(Constant.IntentKey.starModel);
		}
		return R.layout.activity_star;
	}

	@Override
	protected void initNavBar() {

	}

	@Override
	protected void initView() {

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

}
