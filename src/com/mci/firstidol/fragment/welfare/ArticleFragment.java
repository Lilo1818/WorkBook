package com.mci.firstidol.fragment.welfare;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;
import com.mci.firstidol.model.SquareLiveModel;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.utils.LogUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.activity.GiftResultActivity;
import com.mci.firstidol.activity.LoginActivity;
import com.mci.firstidol.activity.SquareWelfareDetailJoinActivity;
import com.mci.firstidol.activity.WebActivity;
import com.mci.firstidol.activity.ZhiboPicturesActivity;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ArticleFragment extends BaseFragment implements OnClickListener {
	private static final String TAG = "ArticleFragment";
	private static final String LOCAL_URL = "file:///android_asset/player/article.html";

	private static final int STATE_LOADING = 0;// 正在获取活动状态
	private static final int STATE_NOT_BEGIN = 1;// 即将开始
	private static final int STATE_JOIN = 2;// 立即参与
	private static final int STATE_JOIN_RESULT = 3;// 查看结果
	private static final int STATE_JOINED = 4;// 已参与

	private static final int REQUEST_CODE_JOIN = 0;

	private Context mContext;
	private int mRequestArticleId = -1;
	private int mRequestVoteId;
	private long mArticleId;
	private SquareLiveModel mArticleDbWarpper;
	private RelativeLayout mLoadingLayout;
	private RelativeLayout mContentLayout;
	private RelativeLayout mReloadingLayout;
	private LinearLayout mArticleShareRL;
	private WebView mWebView;
	private TextView mBack;
	private TextView mLike;
	private TextView mShare;
	private TextView mComments;

	private int mState = STATE_LOADING;
	private int mType = BaseFragment.TYPE_ARTICLE;
	private int mCategoryId;

	private int mRequestPostId = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getArguments();
		if (bundle != null) {
			mArticleDbWarpper = (SquareLiveModel) bundle
					.getSerializable(Constant.IntentKey.squareLiveModel);
			mArticleId = mArticleDbWarpper.getArticleId();
			mCategoryId = bundle.getInt("data_categoryId");

			switch (mArticleDbWarpper.getEventsProgress()) {
			case 0:
				mState = STATE_NOT_BEGIN;
				break;
			case 1:
				mState = STATE_JOIN;
				break;
			case 2:
				mState = STATE_JOIN_RESULT;
				break;
			}

			ArrayList<String> images = getContentPics(mArticleDbWarpper.getContent());
		}
		mContext = getActivity();
	}

	@SuppressWarnings("deprecation")
	@Override
	@SuppressLint("SetJavaScriptEnabled")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View content = inflater.inflate(
				R.layout.layout_fragment_square_welfare_detail, container,
				false);
		mLoadingLayout = (RelativeLayout) content.findViewById(R.id.rl_loading);
		mContentLayout = (RelativeLayout) content.findViewById(R.id.rl_content);
		mReloadingLayout = (RelativeLayout) content
				.findViewById(R.id.rl_reloading);
		// mArticleShareRL = (LinearLayout)
		// content.findViewById(R.id.article_share_area);
		// mBack = (TextView) content.findViewById(R.id.back);
		// mLike = (TextView) content.findViewById(R.id.fav_area_tv);
		// mShare = (TextView) content.findViewById(R.id.share);
		// mComments = (TextView) content.findViewById(R.id.comment_num);
		mWebView = (WebView) content.findViewById(R.id.webview);
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
		mWebView.setFocusable(false);

		// mBack.setOnClickListener(this);
		// mShare.setOnClickListener(this);
		// mComments.setOnClickListener(this);
		// mLike.setOnClickListener(this);
		// content.findViewById(R.id.hide_area).setOnClickListener(this);
		// content.findViewById(R.id.share_weibo).setOnClickListener(this);
		// content.findViewById(R.id.share_wx).setOnClickListener(this);
		// content.findViewById(R.id.share_wx_moments).setOnClickListener(this);
		// content.findViewById(R.id.share_copylink).setOnClickListener(this);

		WebSettings webSetting = mWebView.getSettings();
		webSetting.setJavaScriptEnabled(true);
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportMultipleWindows(false);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		webSetting.setAppCachePath(mContext.getDir("appcache", 0).getPath());
		webSetting.setDatabasePath(mContext.getDir("databases", 0).getPath());
		webSetting.setGeolocationDatabasePath(mContext.getDir("geolocation", 0)
				.getPath());
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);

		return content;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData1();
		CookieSyncManager.createInstance(mContext);
		CookieSyncManager.getInstance().sync();
	}

	@Override
	public void onResume() {
		super.onResume();
		mWebView.onResume();
		// hideProgressDialog();
	}

	@Override
	public void onPause() {
		super.onPause();
		mWebView.onPause();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (mWebView != null) {
			if (isVisibleToUser) {
				mWebView.onResume();
			} else {
				mWebView.onPause();
			}
		}
	}

	public class TEST {
		public int code;
		public boolean isSuc;
		public String message;
		public String name;
		public SquareLiveModel result;
	}

	private void initData1() {
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("mci://docready")) {
					TEST test = new TEST();
					test.code = 1;
					test.isSuc = true;
					test.message = "OK";
					test.name = "";
					test.result = mArticleDbWarpper;

					url = "javascript:article.render("
							+ GsonUtils.objectToJson(test)
							+ ","
							+ Configs.isSavedVoteId(mContext,
									mArticleDbWarpper.getVoteId()) + ");";
					mWebView.loadUrl(url);
					showContent();
				} else if (url.startsWith("mci://contentready")) {
					mWebView.loadUrl("javascript:article.setFontSize("
							+ CommonUtils
									.returnRealContentTextSize(
											mContext,
											Configs.getSettingContentTextSize(mContext))
							+ ");");
					showGiftState();
					replaceImage();
				} else if (url.startsWith("mci://related")) {// 打开相关文章
					openRelatedArticle(url);
				} else if (url.startsWith("mci://vote")) {// 投票
					vote(url);
				} else if (url.startsWith("mci://comment")) {// 热门评论
					openComment();
				} else if (url.startsWith("mci://ad")) {// 广告
					openAd(url);
				} else if (url.startsWith("mci://image")) {
					openImage(url);
				} else if (url.startsWith("mci://giftJoin")) {// 参加好礼
					giftJoin();
				} else if (url.startsWith("mci://giftResult")) {// 查看好礼结果
					giftResult();
				} else if (url.startsWith("mci://video")) {
					openVideo(url);
				} else {
//					url = "http://www.baidu.com";
					LogUtils.i("xxxxxxxxxx:"+url);
					Bundle bundle = new Bundle();
					bundle.putString("URL", url);
					Utily.go2Activity(mContext, WebActivity.class,bundle);
					
//					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
//							.parse(url));
//					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					startActivity(intent);
					
//					view.loadUrl(url);
				}

				return true;
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient());
		// mArticleDbWarpper = mDataEngineContext.getArticleById(mArticleId);
		if (mArticleDbWarpper == null
				|| TextUtils.isEmpty(mArticleDbWarpper.getContent())) {
			// mRequestArticleId =
			// mDataEngineContext.requestArticle(mArticleId);
		} else {
			mWebView.loadUrl(LOCAL_URL);
			// mRequestArticleId =
			// mDataEngineContext.requestArticle(mArticleId);
		}
		showLoading();
		// EventLog.getInstance().saveEvent(EventLog.EVENT_NAME_ARTICLEVIEW,
		// String.valueOf(mArticleId));
	}

	private void showLikedIfNeed() {
		switch (mType) {
		case BaseFragment.TYPE_ARTICLE:
			if (Configs.isSavedLikeArticle(mContext, mArticleId)) {
				mLike.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.article_liked, 0, 0, 0);
				mLike.setClickable(false);
			}
			break;
		case BaseFragment.TYPE_GIFT:
			if (Configs.isSavedLikeGift(mContext, mArticleId)) {
				mLike.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.article_liked, 0, 0, 0);
				mLike.setClickable(false);
			}
			break;
		case BaseFragment.TYPE_SHOPPING:
			if (Configs.isSavedLikeShopping(mContext, mArticleId)) {
				mLike.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.article_liked, 0, 0, 0);
				mLike.setClickable(false);
			}
			break;
		}
	}

	// updateWebContent 为false时，不更新WebView，只更新评论数、赞、收藏等状态
	private void bindData(final boolean updateWebContent) {
		if (mArticleDbWarpper == null) {
			return;
		}
		((Activity) mContext).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (updateWebContent) {
					mWebView.loadUrl(LOCAL_URL);
				} else {
					showContent();
				}
			}
		});
	}

	private void openRelatedArticle(String url) {
		try {
			Uri uri = Uri.parse(url);
			int articleId = Integer
					.parseInt(uri.getQueryParameter("articleId"));
			int modelType = Integer
					.parseInt(uri.getQueryParameter("ModelType"));
			Intent intent = new Intent();
			intent.putExtra("data_articleId", articleId);
			switch (modelType) {
			case ArticlesContent.MODELTYPE.VIDEO:// 视频
			case ArticlesContent.MODELTYPE.SIMPLE:// 普通
				intent.setClass(mContext, ArticleFragment.class);
				startActivity(intent);
				break;
			case ArticlesContent.MODELTYPE.PIC_SET:// 图集
//				 intent.setClass(mContext, PictureSetActivity.class);
//				 startActivity(intent);
//				 ((Activity) mContext).finish();
				break;
			}
		} catch (Exception e) {
			Log.e(TAG, "openRelatedArticle exception:" + e);
		}
	}

	private void vote(String url) {
		// try {
		// Uri uri = Uri.parse(url);
		// int voteId = Integer.parseInt(uri.getQueryParameter("voteId"));
		// String detailId = uri.getQueryParameter("voteDetail");
		// mRequestVoteId = mDataEngineContext.requestVote(voteId, detailId);
		// } catch (Exception e) {
		// Log.e(TAG, "vote exception:" + e);
		// }
	}

	private void openComment() {
		// try {
		// Intent intent = new Intent(mContext, CommentActivity.class);
		// intent.putExtra("data_articleDbWarpper", mArticleDbWarpper);
		// startActivity(intent);
		// } catch (Exception e) {
		// Log.e(TAG, "openComment exception:" + e);
		// }
	}

	private void openAd(String url) {
		try {
			Uri uri = Uri.parse(url);
			String articleId = uri.getQueryParameter("articleId");
			String adUrl = uri.getQueryParameter("url");
			if (!TextUtils.isEmpty(articleId)) {
				Intent intent = new Intent();
				intent.putExtra("data_articleId", Integer.parseInt(articleId));
				intent.setClass(mContext, ArticleFragment.class);
				startActivity(intent);
			}

			if (!TextUtils.isEmpty(adUrl)) {
				Uri adUri = Uri.parse(adUrl);
				Intent intent = new Intent(Intent.ACTION_VIEW, adUri);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		} catch (Exception e) {
			Log.e(TAG, "openAd exception:" + e);
		}
	}

	private void openImage(String url) {
//		 Uri uri = Uri.parse(url);
//		 String index = uri.getQueryParameter("index");
//		 Intent intent = new Intent(mContext, ContentPicturesActivity.class);
//		 intent.putExtra("index", index);
//		 intent.putExtra("pics", mArticleDbWarpper.ContentPics);
//		 startActivity(intent);
		
		ArrayList<String> images = getContentPics(mArticleDbWarpper.getContent());
		if (images!=null && images.size()>0) {
			Bundle bundle = new Bundle();
			bundle.putString(Constant.IntentKey.from,
					"WebArticleDetail");
//			bundle.putInt("position",getSelectPosition(url,images));
			bundle.putInt("position",Integer.parseInt(url.split("index=")[1]));
			bundle.putSerializable("attachments",
					images);
			Utily.go2Activity(getActivity(),
					ZhiboPicturesActivity.class, bundle);
		}
	}
	
	public int getSelectPosition(String url,ArrayList<String> images){
		int position = 0;
		for (int i = 0; i < images.size(); i++) {
			String imagePath = images.get(i);
			
			LogUtils.i("xxx:"+url + "----yyyy:"+imagePath);
			
			if (imagePath.equals(url)) {
				position = i;
				break;
			}
		}
		return position;
	}

	private void giftJoin() {
		// UserInfoDbWarpper user = mDataEngineContext.getUserInfo();
		// if (user == null) {
		// Intent intent = new Intent(mContext, LoginActivity.class);
		// startActivity(intent);
		// return;
		// }

		if (mState == STATE_JOINED) {
			CommonUtils.showToast(mContext, "您已参与过此项活动");
			return;
		}

		// if (mArticleDbWarpper != null && mArticleDbWarpper.IsRaffle == 1) {
		// // 调动接口 events/join
		// requestEventJoin();
		// } else {
		
		
		if (!DataManager.getInstance().isLogin) {
			Utily.go2Activity(getActivity(), LoginActivity.class);
		} else {
		 Intent intent = new Intent(mContext, SquareWelfareDetailJoinActivity.class);
		 intent.putExtra(Constant.IntentKey.articleID, mArticleId);
		 startActivityForResult(intent, REQUEST_CODE_JOIN);
		 activity.overridePendingTransition(R.anim.activity_right_in,
					R.anim.activity_left_harf_out);
		 }
		
//		Bundle bundle = new Bundle();
//		bundle.putLong(Constant.IntentKey.articleID, mArticleId);
//		Utily.go2Activity(activity, SquareWelfareDetailJoinActivity.class, bundle);
	}

	private void requestEventJoin() {
		// showProgressDialog("请求中...");
		// if (mRequestPostId != Utils.INVALID_REQUEST_ID) {
		// mDataEngineContext.cancelRequest(mRequestPostId);
		// }
		// mRequestPostId = mDataEngineContext.requestEventJoin(null, null,
		// null, mArticleId,
		// PushUtils.getPushUserId(getActivity()),
		// PushUtils.getPushChannelId(getActivity()));
	}

	private void giftResult() {
//		 Intent intent = new Intent(getActivity(), GiftResultActivity.class);
//		 intent.putExtra("data_articleId", mArticleId);
//		 startActivity(intent);
		 
		 Bundle bundle = new Bundle();
		 bundle.putLong(Constant.IntentKey.articleID, mArticleId);
		 Utily.go2Activity(getActivity(), GiftResultActivity.class,bundle);
	}

	private void openVideo(String url) {
		try {
			Uri uri = Uri.parse(URLDecoder.decode(url, "UTF-8"));
			String videoUrl = uri.getQueryParameter("videoUrl");
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse(videoUrl), "video/*");
			mContext.startActivity(intent);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void showGiftState() {
		switch (mState) {
		case STATE_NOT_BEGIN:
			mWebView.loadUrl("javascript:article.setActComing()");
			break;
		case STATE_JOIN:
			mWebView.loadUrl("javascript:article.setActJoin()");
			break;
		case STATE_JOIN_RESULT:
			mWebView.loadUrl("javascript:article.setActResult()");
			break;
		case STATE_JOINED:
			mWebView.loadUrl("javascript:article.setActJoined()");
			break;
		}
	}

	private void showContent() {
		setType();
		showLikedIfNeed();
		mLoadingLayout.setVisibility(View.GONE);
		mContentLayout.setVisibility(View.VISIBLE);
		mReloadingLayout.setVisibility(View.GONE);
		// mLike.setVisibility(View.VISIBLE);
		// mShare.setVisibility(View.VISIBLE);
		// mComments.setVisibility(View.VISIBLE);

		HashMap<String, String> article = new HashMap<String, String>();
		// ///**************
		// article.put("param", Common.CATEGORY_MAP.get(mCategoryId) + "-" +
		// mArticleDbWarpper.getChannelId());
		// MobclickAgent.onEvent(mContext, "read_article_times", article);
//		 TCAgent.onEvent(mContext, "read_article_times", "文章阅读数", article);
		// if (mDataEngineContext != null) {
		// mDataEngineContext.saveArticleReadTime(mArticleDbWarpper.ArticleId);
		// }
	}

	private void showReloadingContent() {
		mLoadingLayout.setVisibility(View.GONE);
		mContentLayout.setVisibility(View.GONE);
		mReloadingLayout.setVisibility(View.VISIBLE);
		mReloadingLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initData1();
			}
		});
		mLike.setVisibility(View.GONE);
		mShare.setVisibility(View.GONE);
		mComments.setVisibility(View.GONE);
	}

	private void showLoading() {
		mContentLayout.setVisibility(View.GONE);
		mLoadingLayout.setVisibility(View.VISIBLE);
		mReloadingLayout.setVisibility(View.GONE);
		// mLike.setVisibility(View.GONE);
		// mShare.setVisibility(View.GONE);
		// mComments.setVisibility(View.GONE);
	}

	private void setType() {
		// ChannelDbWarpper channel =
		// mDataEngineContext.getChannelById(mArticleDbWarpper.ChannelId);
		// if (channel != null) {
		// int categoryId = channel.CategoryId;
		// switch (categoryId) {
		// case Common.GIFT_CATEGORY_ID:
		mType = BaseFragment.TYPE_GIFT;
		// break;
		// case Common.SHOPPING_CATEGORY_ID:
		// mType = BaseFragment.TYPE_SHOPPING;
		// break;
		// case Common.VOTE_CATEGORY_ID:
		// mType = BaseFragment.TYPE_VOTE;
		// break;
		// }
		// }
		// setShareType(mType);
	}

	private void replaceImage() {
		if (mArticleDbWarpper != null) {
			// ArrayList<String> images = mArticleDbWarpper.getContentPics();
			ArrayList<String> images = getContentPics(mArticleDbWarpper.getContent());
			
			if (images != null && !images.isEmpty()) {
				for (String imageUrl : images) {
					String validUrl = CommonUtils.getValidImageUrl(mContext,
							imageUrl);
					File file = ImageLoader.getInstance().getDiscCache()
							.get(validUrl);
					if (file.exists() && file.isFile()) {
						mWebView.loadUrl("javascript:article.setImgUrl('"
								+ imageUrl + "','" + file.getPath() + "')");
					} else {
						ImageLoader.getInstance().loadImage(validUrl,
								BaseApp.options, new ImageLoadingListener() {
									@Override
									public void onLoadingStarted(String url,
											View view) {
									}

									@Override
									public void onLoadingFailed(String url,
											View view, FailReason reason) {
									}

									@Override
									public void onLoadingComplete(
											final String url, View view,
											final Bitmap bitmap) {
										File file = ImageLoader.getInstance()
												.getDiscCache().get(url);
										if (file.exists() && file.isFile()) {
											mWebView.loadUrl("javascript:article.setImgUrl('"
													+ url
													+ "','"
													+ file.getPath() + "')");
										}
									}

									@Override
									public void onLoadingCancelled(String url,
											View view) {
									}
								});
					}
				}
			} else {
				showContent();
			}
		}
	}

	private ArrayList<String> getContentPics(String htmlStr) {
		//(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic)
		ArrayList<String> imageSrcList = new ArrayList<String>();
		Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(htmlStr);
		String quote = null;
		String src = null;
		while (m.find()) {
		quote = m.group(1);
		
		// src=https://sms.reyo.cn:443/temp/screenshot/zY9Ur-KcyY6-2fVB1-1FSH4.png
		src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("\\s+")[0] : m.group(2);
		imageSrcList.add(src);
		
		}
		return imageSrcList;

	}

	@Override
	public void onClick(View v) {
		// switch (v.getId()) {
		// case R.id.back:
		// ((Activity) mContext).finish();
		// break;
		// case R.id.share:
		// mArticleShareRL.setVisibility(View.VISIBLE);
		// Animation fadeIn = AnimationUtils.loadAnimation(mContext,
		// R.anim.fade_in);
		// mArticleShareRL.startAnimation(fadeIn);
		// break;
		// case R.id.hide_area:
		// mArticleShareRL.setVisibility(View.GONE);
		// break;
		// case R.id.share_weibo:
		// shareToSinaWeibo(mArticleDbWarpper);
		// mArticleShareRL.setVisibility(View.GONE);
		// break;
		// case R.id.share_wx:
		// shareToWeixin(mArticleDbWarpper);
		// mArticleShareRL.setVisibility(View.GONE);
		// break;
		// case R.id.share_wx_moments:
		// shareToWechatMoments(mArticleDbWarpper);
		// mArticleShareRL.setVisibility(View.GONE);
		// break;
		// case R.id.share_copylink:
		// copyLink(mArticleDbWarpper);
		// mArticleShareRL.setVisibility(View.GONE);
		// break;
		// case R.id.comment_num:
		// Intent intent = new Intent(mContext, CommentActivity.class);
		// intent.putExtra("data_articleDbWarpper", mArticleDbWarpper);
		// startActivity(intent);
		// break;
		// case R.id.fav_area_tv:
		// mLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.article_liked,
		// 0, 0, 0);
		// mLike.setClickable(false);
		// saveLikeArticle();
		// mDataEngineContext.requestLikeArticle(mArticleId);
		// break;
		// }
	}

	private void saveLikeArticle() {
		switch (mType) {
		case BaseFragment.TYPE_ARTICLE:
			Configs.saveLikeArticle(mContext, mArticleId);
			break;
		case BaseFragment.TYPE_GIFT:
			Configs.saveLikeGift(mContext, mArticleId);
			break;
		case BaseFragment.TYPE_SHOPPING:
			Configs.saveLikeShopping(mContext, mArticleId);
			break;
		}
	}

	/*
	 * @Override public void onMessage(Message msg) { switch (msg.what) { case
	 * MessageCode.GET_ARTICLE: if (msg.getData().getInt(Utils.KEY_REQUEST_ID)
	 * == mRequestArticleId) { mRequestArticleId = Utils.INVALID_ID; if (msg.obj
	 * != null) { mArticleDbWarpper = (ArticleDbWarpper) msg.obj;
	 * mArticleDbWarpper.save(mContext); switch
	 * (mArticleDbWarpper.EventsProgress) { case 0: mState = STATE_NOT_BEGIN;
	 * break; case 1: mState = STATE_JOIN; break; case 2: mState =
	 * STATE_JOIN_RESULT; break; } if (mArticleDbWarpper.HasJoinEvents && mState
	 * != STATE_JOIN_RESULT) { mState = STATE_JOINED; } bindData(true); } else {
	 * if (mArticleDbWarpper == null ||
	 * TextUtils.isEmpty(mArticleDbWarpper.Content)) { showReloadingContent(); }
	 * else { showContent(); } } } break; case MessageCode.POST_VOTE: if
	 * (msg.getData().getInt(Utils.KEY_REQUEST_ID) == mRequestVoteId) {
	 * mRequestVoteId = Utils.INVALID_ID; if (msg.obj != null) { try { String
	 * json = msg.obj.toString(); JSONObject obj = new JSONObject(json); if
	 * (obj.getBoolean("isSuc")) { Configs.saveVoteId(mContext,
	 * obj.getJSONObject("result").getInt("Id"));
	 * mWebView.loadUrl("javascript:article.setVotedDom(" + json + ");"); } else
	 * { mWebView.loadUrl("javascript:article.setVoteFailed();"); } } catch
	 * (JSONException e) { Log.e(TAG, "onMessage post vote exception:" + e); } }
	 * else { mWebView.loadUrl("javascript:article.setVoteFailed();"); } }
	 * break; case MessageCode.POST_EVENT_JOIN: hideProgressDialog(); if
	 * (msg.arg1 == ErrorCode.OK && msg.obj != null) { if
	 * (msg.getData().getInt(Utils.KEY_REQUEST_ID) == mRequestPostId) { try {
	 * JSONObject result = new JSONObject(msg.obj.toString()); if
	 * (result.getBoolean("isSuc")) { UserInfoDbWarpper user =
	 * mDataEngineContext.getUserInfo(); if (user == null) { return; } String
	 * url = Common.getHost(getActivity()) + "/" +
	 * mArticleDbWarpper.RaffleGameUrl + "?UserId=" + user.UserId +
	 * "&ArticleId=" + mArticleId + "&token=" + Configs.getToken(getActivity());
	 * Intent intent = new Intent(getActivity(), GiftJoinWebActivity.class);
	 * intent.putExtra("url", url); startActivity(intent); } else {
	 * CommonUtils.showToast(getActivity(), result.getString("message")); } }
	 * catch (Exception e) { Log.e(TAG, "onMessage exception:" + e); } } } else
	 * { CommonUtils.showToast(getActivity(), "请求失败"); } break; } }
	 */

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case REQUEST_CODE_JOIN:
			if (data.getBooleanExtra("join_success", false)) {// 已参与
				mState = STATE_JOINED;
			}
			showGiftState();
			break;
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mRequestArticleId != -1) {
			// if (mDataEngineContext != null) {
			// mDataEngineContext.cancelRequest(mRequestArticleId);
			// }
		}
	}

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

}