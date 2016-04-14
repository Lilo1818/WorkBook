package com.mci.firstidol.activity;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mci.firstidol.R;
import com.mci.firstidol.adapter.PhotoPageAdapter;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.PhotoModel;
import com.mci.firstidol.model.StarPhotoModel;
import com.mci.firstidol.model.StarVideoModel;
import com.mci.firstidol.utils.AsyncTaskUtils;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.ToastUtils;

public class PhotoSelectedActivity extends BaseActivity implements
		OnPageChangeListener, OnClickListener {

	private ViewPager viewPager;// 图片轮播
	private TextView text_comment;// 评论数目
	private TextView text_zan;// 赞的数目
	private RelativeLayout layout_zan;// 赞的布局
	private RelativeLayout layout_comment;// 评论布局

	private LinearLayout layout_loading;// 菊花圈圈
	private RelativeLayout layout_main;// 主页面
	private LinearLayout layout_error;// 错误页 
	
	private ImageView image_error;//错误图片

	private List<PhotoModel> pictures;// 图册地址
	private StarPhotoModel starPhotoModel;// 学生相册对象
	private PagerAdapter pagerAdapter;// 图片适配器

	private String article_id;

	@Override
	protected int getViewId() {
		Intent intent = getIntent();
		if(intent!=null){
			article_id = intent.getStringExtra(Constant.IntentKey.articleID);
			
		}
		return R.layout.activity_photoselected;
	}

	@Override
	protected void initNavBar() {
		
		if (pictures != null) {
			setTitle("1/" + pictures.size());
		} else{
			setTitle("1");
		}
	}

	@Override
	protected void initView() {

		layout_loading = (LinearLayout) findViewById(R.id.loading);
		layout_main = (RelativeLayout) findViewById(R.id.layout_main);
		layout_error = (LinearLayout) findViewById(R.id.layout_error);

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		text_comment = (TextView) findViewById(R.id.text_comment);
		text_zan = (TextView) findViewById(R.id.text_zan);
		layout_zan = (RelativeLayout) findViewById(R.id.layout_zan);
		layout_comment = (RelativeLayout) findViewById(R.id.layout_comment);
		image_error = (ImageView) findViewById(R.id.image_error);
		
		layout_zan.setOnClickListener(this);
		layout_comment.setOnClickListener(this);
		
		getPhotoInfoRequest();
		showLoadingView();
		
		image_error.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getPhotoInfoRequest();
				showLoadingView();
			}
		});
	}

	@Override
	public void rightNavClick() {

	}

	@Override
	protected void findViewById() {

	}

	/**
	 * 显示圈圈页
	 */
	public void showLoadingView() {
		layout_loading.setVisibility(View.VISIBLE);
		layout_main.setVisibility(View.GONE);
		layout_error.setVisibility(View.GONE);
	}

	/**
	 * 显示内容页
	 */
	public void showContentView() {
		layout_loading.setVisibility(View.GONE);
		layout_main.setVisibility(View.VISIBLE);
		layout_error.setVisibility(View.GONE);
	}

	/**
	 * 显示错误页
	 */
	public void showErrorView() {
		layout_loading.setVisibility(View.GONE);
		layout_main.setVisibility(View.GONE);
		layout_error.setVisibility(View.VISIBLE);
	}

	@Override
	protected void initData() {

		if (starPhotoModel != null) {
			pictures = starPhotoModel.ArticlePictures;
			text_zan.setText(String.valueOf(starPhotoModel.UpCount));
			text_comment.setText(String.valueOf(starPhotoModel.CommentCount));
			
			pagerAdapter = new PhotoPageAdapter(context, pictures,starPhotoModel);
			viewPager.setAdapter(pagerAdapter);
			viewPager.setOnPageChangeListener(this);
		}
	}

	/**
	 * 得到照片对象
	 */
	public void getPhotoInfoRequest() {
		String processURL = Constant.RequestContstants.Request_get_article
				+ "/" + article_id;

		ConnectionService.getInstance().serviceConnUseGet(context, processURL,
				null, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						Log.e("yyw", Result);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if (check) {
								starPhotoModel = (StarPhotoModel) parseJson
										.getModelObject(jsonObject,
												StarPhotoModel.class);
								initData();
								showContentView();
							} else {
								showErrorView();
							}
						} catch (Exception e) {
							showErrorView();
						}
					}

					@Override
					public void getError() {
						showErrorView();
					}
				});
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		setTitle((arg0 + 1) + "/" + pictures.size());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_zan:
			if (starPhotoModel != null && !starPhotoModel.HasUp) {
				obtainZanRequest(starPhotoModel.ArticleId, 0);
			} else {
				ToastUtils.showCustomToast(context, "已经赞过");
			}
			break;
		case R.id.layout_comment:
			HashMap<String, String> params = new HashMap<String,String>();
			params.put(Constant.IntentKey.articleID, article_id);
			Utily.go2Activity(context, PhotoCommentActivity.class, params, null);
			break;

		default:
			break;
		}

	}

	// 文章点赞
	private void obtainZanRequest(long articleId, final int index) {
		// TODO Auto-generated method stub
		BaseRequest request = new BaseRequest();
		// 请求地址
		String requestID = String.format(
				Constant.RequestContstants.Request_Square_Zan, articleId,
				index + 1);
		doAsync(false, requestID, request,
				new AsyncTaskUtils.Callback<Exception>() {

					@Override
					public void onCallback(Exception e) {
						// TODO Auto-generated method stub
						ToastUtils.showCustomToast(e.getLocalizedMessage());
					}
				}, new AsyncTaskUtils.Callback<String>() {

					@Override
					public void onCallback(String result) {
						ToastUtils.showCustomToast("点赞成功");
						starPhotoModel.HasUp = true;
						starPhotoModel.UpCount = starPhotoModel.UpCount+1;
						initZanView();
					}
				});
	}
	
	/**
	 * 初始化赞数量
	 */
	public void initZanView(){
		text_zan.setText(String.valueOf(starPhotoModel.UpCount));
	}

}
