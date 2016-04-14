package com.mci.firstidol.activity;

import android.content.Intent;
import android.os.Bundle;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.fragment.star.StarTripFragment;

public class StarAddressActivity extends BaseActivity {

	private String starId;// 明星ID

	private StarTripFragment starTripFragment;// 明星行程

	@Override
	protected int getViewId() {
		Intent intent = getIntent();
		if (intent != null) {
			starId = intent.getStringExtra(Constant.IntentKey.star_id);
		}
		return R.layout.activity_frame_list;
	}

	@Override
	protected void initNavBar() {
		setTitle(R.string.star_address);
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
		starTripFragment = new StarTripFragment();
		Bundle bundle = new Bundle();
		bundle.putString(Constant.IntentKey.star_id, starId);
		
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.my_data, starTripFragment).commit();
	}

}
