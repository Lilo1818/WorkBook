package com.mci.firstidol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.fragment.PhotoComentFragment;
import com.mci.firstidol.utils.Utily;

public class PhotoCommentActivity extends BaseActivity implements OnClickListener{

	private String article_id;
	
	private ImageView btn_apply;//发布按钮

	@Override
	protected int getViewId() {
		Intent intent = getIntent();
		if (intent != null) {
			article_id = intent.getStringExtra(Constant.IntentKey.articleID);
		}
		return R.layout.activity_photo_comment;
	}

	@Override
	protected void initNavBar() {
		setTitle(R.string.comment);
	}

	@Override
	protected void initView() {
		btn_apply = (ImageView) findViewById(R.id.btn_apply);
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
		BaseFragment baseFragment = new PhotoComentFragment();
		Bundle bundle = new Bundle();
		bundle.putString(Constant.IntentKey.articleID, article_id);
		baseFragment.setArguments(bundle);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.my_data, baseFragment).commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_apply:
			Bundle bundle = new Bundle();
			if(article_id!=null&&!article_id.equals("")){
				bundle.putLong(Constant.IntentKey.articleID,
						Long.parseLong(article_id));
			}
			Utily.go2Activity(context, SquareFoundDetailAddReplyActivity.class,
					bundle);
			break;

		default:
			break;
		}
		
	}

}
