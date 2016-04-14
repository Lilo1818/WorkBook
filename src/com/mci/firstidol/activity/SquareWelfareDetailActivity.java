package com.mci.firstidol.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.fragment.welfare.ArticleFragment;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.SquareLiveModel;
import com.mci.firstidol.model.SquareModel.SquareFoundModel;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.ProgressActivity;
import com.mci.firstidol.view.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SquareWelfareDetailActivity extends BaseActivity implements OnClickListener{

	private FrameLayout fl_fragment_container;
	private ImageButton btn_back,btn_share,btn_reply,btn_collection;
	private static SquareLiveModel squareLiveModel;
	
	private Object typeCode;
	private boolean isFromFound,isSearch;
	private long articleID;
	
	private ProgressActivity progressActivity;
	
	public SquareWelfareDetailActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.layout_square_welfare_detail;
	}

	@Override
	protected void initNavBar() {
		Intent intent = getIntent();
		if(intent!=null){
			typeCode = intent.getExtras().get(Constant.IntentKey.typeCode);
		}
		if(typeCode!=null){
			setTitle(R.string.shopping_detail);
		}else{
			setTitle(R.string.welfare_detail);
		}
		
	}
	
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		progressActivity = (ProgressActivity) findViewById(R.id.progressActivity);
		
		
		fl_fragment_container = (FrameLayout) findViewById(R.id.fl_fragment_container);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_share = (ImageButton) findViewById(R.id.btn_share);
		btn_reply = (ImageButton) findViewById(R.id.btn_reply);
		btn_collection = (ImageButton) findViewById(R.id.btn_collection);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
		
		btn_back.setOnClickListener(this);
		btn_share.setOnClickListener(this);
		btn_reply.setOnClickListener(this);
		btn_collection.setOnClickListener(this);
		
	}

	@Override
	public void rightNavClick() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		isFromFound = getIntent().getExtras().getBoolean("IsFromFound");
		isSearch = (boolean) getIntent().getExtras().getBoolean(Constant.IntentKey.isSearch,false);
		
		
		if (isFromFound) {
			btn_back.setVisibility(View.VISIBLE);
		} else {
			btn_back.setVisibility(View.GONE);
		}
		if (isSearch) {
			articleID = getIntent().getExtras().getLong(Constant.IntentKey.articleID);
			progressActivity.showLoading();
			
			// 查询发现详情
			obtainDetailRequest();
		} else {
			squareLiveModel = (SquareLiveModel) getIntent().getExtras().get(Constant.IntentKey.squareLiveModel);
			
			ArticleFragment articleFragment = new ArticleFragment();
			articleFragment.setArguments(getIntent().getExtras());
			// 添加Fragment到容器总，同时标注第一个菜单项处于选中状态
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.add(fl_fragment_container.getId(), articleFragment);
			// transaction.addToBackStack(null);
			// transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			transaction.commit();
		}
	}
	
	
	public void obtainDetailRequest() {
		BaseRequest request = new BaseRequest();
		// 请求地址
		String requestID = String.format(
				Constant.RequestContstants.Request_Square_FoundDetail,
				articleID);
		doAsync(false, requestID, request, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
//				ToastUtils.showCustomToast(e.getLocalizedMessage());
//				mPullRefreshListView.onRefreshComplete();
				String errorMsg = e.getMessage();
				if ("无网络".equals(errorMsg)) {
					refreshComplete(true,true);
				} else {
					refreshComplete(true,false);
				}
			}
		}, new Callback<String>() {

			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub

				squareLiveModel = (SquareLiveModel) GsonUtils.jsonToBean(
						result, SquareLiveModel.class);
				
				
				Bundle bundle = new Bundle();
				bundle.putSerializable(Constant.IntentKey.squareLiveModel, squareLiveModel);
				ArticleFragment articleFragment = new ArticleFragment();
				articleFragment.setArguments(bundle);
				// 添加Fragment到容器总，同时标注第一个菜单项处于选中状态
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction transaction = fragmentManager.beginTransaction();
				transaction.add(fl_fragment_container.getId(), articleFragment);
				// transaction.addToBackStack(null);
				// transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
				transaction.commit();
				
				
				refreshComplete(false,false);
			}
		});
	}
	
	
	/**
	 * 
	 * @param isError 是否请求错误
	 * @param isNoNetwork 是否网络请求错误
	 */
	public void refreshComplete(boolean isError,boolean isNoNetwork) {
//		isError = true;
//		isNoNetwork = false;
		try {
			int drawableID = 0;
			String title = null,content = null,btnTitle = null;
			if (isError && isNoNetwork) {
				drawableID = R.drawable.error_network;
				title = getResources().getString(R.string.hint_error_network_title);
				content = getResources().getString(R.string.hint_error_network_content);
				btnTitle = getResources().getString(R.string.hint_error_button_title);
			} else if (isError) {
				drawableID = R.drawable.failed;
				title = getResources().getString(R.string.hint_error_request_title);
				content = getResources().getString(R.string.hint_error_request_content);
				btnTitle = getResources().getString(R.string.hint_error_button_title);
			} else {
				drawableID = R.drawable.default_img;
				title = getResources().getString(R.string.hint_error_nodata_title);
				content = getResources().getString(R.string.hint_error_nodata_content);
				btnTitle = getResources().getString(R.string.hint_error_button_title);
			}
			
			if (drawableID>0 && progressActivity!=null) {
				progressActivity.showError(getResources().getDrawable(drawableID), title, content, btnTitle, new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						progressActivity.showLoading();
						// 查询发现详情
						obtainDetailRequest();
					}
				});
			} else {
				progressActivity.showContent();
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (!dataManager.isLogin) {
			Utily.go2Activity(context, LoginActivity.class);
		} else {
			switch (v.getId()) {
			case R.id.btn_share:
				showShare(this, null, true);
				break;
			case R.id.btn_reply:
				Bundle bundle = new Bundle();
				bundle.putLong(Constant.IntentKey.articleID, squareLiveModel.getArticleId());
				Utily.go2Activity(this, SquareFoundDetailReplyActivity.class,bundle);
				break;
			case R.id.btn_collection:
				obtainFoundDetailCommentFavRequest(((ImageButton)v).isSelected());
				break;
			case R.id.btn_back:
				finishActivity(SquareWelfareDetailActivity.this);
				break;

			default:
				break;
			}
		}
		
		
	}
	
	/**
	 * 调用ShareSDK执行分享
	 *
	 * @param context
	 * @param platformToShare  指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
	 * @param showContentEdit  是否显示编辑页
	 */
	public static void showShare(Context context, String platformToShare, boolean showContentEdit) {
		// TODO Auto-generated method stub
		OnekeyShare oks = new OnekeyShare();
		oks.setSilent(!showContentEdit);
		if (platformToShare != null) {
			oks.setPlatform(platformToShare);
		}
		//ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC  第二个是SKYBLUE
		oks.setTheme(OnekeyShareTheme.CLASSIC);
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		// 在自动授权时可以禁用SSO方式
		oks.disableSSOWhenAuthorize();
		//oks.setAddress("12345678901"); //分享短信的号码和邮件的地址
		oks.setTitle(squareLiveModel.getTitle());
		String titleUrl = Constant.RequestContstants.URL+ String.format(Constant.RequestContstants.Request_Article_Share, squareLiveModel.getArticleId());
		oks.setTitleUrl(titleUrl);
		oks.setText(squareLiveModel.getTitle());
		oks.setImagePath(ImageLoader.getInstance().getDiscCache().get(squareLiveModel.getIco()).getPath());  //分享sdcard目录下的图片
		String imageUrl = squareLiveModel.getModelType() == 2? squareLiveModel.getVideoIco():squareLiveModel.getIco();
		oks.setImageUrl(imageUrl);
		oks.setUrl(titleUrl); //微信不绕过审核分享链接
//		oks.setFilePath(ImageLoader.getInstance().getDiscCache().get(squareFoundModel.getIco()).getPath());  //filePath是待分享应用程序的本地路劲，仅在微信（易信）好友和Dropbox中使用，否则可以不提供
		oks.setComment("easy"); //我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
		oks.setSite("Easy");  //QZone分享完之后返回应用时提示框上显示的名称
		oks.setSiteUrl(titleUrl);//QZone分享参数
		oks.setVenueName("Easy");
		oks.setVenueDescription("Easy is a beautiful place!");
		// 启动分享
		oks.show(context);
	}
	
	
	/**
	 * 广场-发现详情-收藏
	 * @param isCancelFav
	 */
	public void obtainFoundDetailCommentFavRequest(final boolean isSelected){
		BaseRequest request = new BaseRequest();
		//请求地址
		String requestID = String.format((isSelected ? Constant.RequestContstants.Request_Article_CancelStore : Constant.RequestContstants.Request_Article_Store), squareLiveModel.getArticleId());		
		doAsync(true, requestID, request, new Callback<Exception>() {
			
			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {
			
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
//				ToastUtils.showCustomToast("收藏成功");
				btn_collection.setSelected(!btn_collection.isSelected());
			}
		});
	}

}
