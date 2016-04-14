package com.mci.firstidol.activity;

import java.util.List;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.mci.firstidol.R;
import com.mci.firstidol.adapter.ImageBroaseAdapter;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.PhotoModel;
import com.mci.firstidol.model.StarPhotoModel;

public class ClickPhotoActivity extends BaseActivity{

	private ViewPager viewPager;
	
	private StarPhotoModel starPhotoModel;// 学生相册对象
	
	private List<PhotoModel> pictures;// 图册地址

    private int current_position;//当前位置

    private PagerAdapter pagerAdapter;
	
	@Override
	protected int getViewId() {
		Intent intent = getIntent();
        if(intent!=null){
            String position = intent.getStringExtra(Constant.IntentKey.IMAGE_POSITION);
            starPhotoModel = (StarPhotoModel) intent.getSerializableExtra(Constant.IntentKey.starphotoModel);
            current_position = Integer.parseInt(position);
            pictures = starPhotoModel.ArticlePictures;
        }
		return R.layout.activity_image_brower;
	}

	@Override
	protected void initNavBar() {
		hideNavBar();
	}

	@Override
	protected void initView() {
		viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ImageBroaseAdapter(context,pictures);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(current_position);
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
