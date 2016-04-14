package com.mci.firstidol.activity;

import android.os.Bundle;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.fragment.ShoppingFragment;

/**
 * demo 使用activity显示商店列表
 * @author wanghaixiao
 *
 */
public class ShoppingActivity extends BaseActivity{

	private ShoppingFragment shoppingFragment;//商店的fragment
	
	@Override
	protected int getViewId() {
		return R.layout.activity_frame_list;
	}

	@Override
	protected void initNavBar() {
		setTitle(R.string.shopping);
		
	}

	@Override
	protected void initView() {
		//封装fragment
		shoppingFragment = new ShoppingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("","");
        shoppingFragment.setArguments(bundle);
        
        //view的替换
        getSupportFragmentManager().beginTransaction().replace(R.id.my_data, shoppingFragment).commit();
		
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
		
	}

}
