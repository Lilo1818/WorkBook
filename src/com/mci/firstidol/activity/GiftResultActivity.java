
package com.mci.firstidol.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.view.ToastUtils;

public class GiftResultActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "GiftResultActivity";
    private static final String LOCAL_URL = "file:///android_asset/player/result.html";

    private int mRequestResultId;
    private long mArticleId;
    private RelativeLayout mLoadingLayout;
    private RelativeLayout mReloadingLayout;

    private WebView mWebView;
//    private HeaderButton mLeftButton;
//    private HeaderButton mRightButton;
    private String mContent;
    private String mTitle;
    private JSONObject mObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (mDataEngineContext != null) {
//            mDataEngineContext.registerReceiver(this, this);
//        }
//        setShareType(SHARE_GIFT_RESULT);
    }

    @Override
    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    public void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            mArticleId = getIntent().getExtras().getLong(Constant.IntentKey.articleID);
//            if (mArticleId != -1) {// 从文章来的
//                setHeaderBackgroudResource(R.drawable.header_orange_bg);
//                setHeaderCenterViewText("查看结果");
//                mLeftButton = new HeaderButton(this);
//                mLeftButton.setVisibility(View.GONE);
//                mRightButton = new HeaderButton(this, R.drawable.btn_cancel_selector);
//                setHeaderRightView(mRightButton);
//            } else {
//                setHeaderBackgroudResource(R.drawable.header_gray_bg);
//                setHeaderCenterViewText("查看结果");
//                mLeftButton = new HeaderButton(this, R.drawable.header_back_default_selector);
//                mRightButton = new HeaderButton(this, R.drawable.gift_result_share);
//                setHeaderLeftView(mLeftButton);
//                setHeaderRightView(mRightButton);
//            }
        }

//        View content = LayoutInflater.from(this).inflate(R.layout.activity_gift_result, null);
        mLoadingLayout = (RelativeLayout) findViewById(R.id.rl_loading);
        mReloadingLayout = (RelativeLayout) findViewById(R.id.rl_reloading);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setFocusable(false);
        

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setRenderPriority(RenderPriority.HIGH);
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void initData() {

    	obtainSquareDetailResultInfoRequest();
    	
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("mci://docready")) {
                    url = "javascript:article.render(" + mObject + ");";
                    mWebView.loadUrl(url);
                    showContent();
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient());

        Intent intent = getIntent();
        if (intent != null) {
            mArticleId = intent.getIntExtra("data_articleId", -1);
            mTitle = intent.getStringExtra("data_title");
            mContent = intent.getStringExtra("data_content");
//            if (TextUtils.isEmpty(mContent)) {
//                mRequestResultId = mDataEngineContext.requestEventResult(mArticleId);
//            } else {
//                try {
//                    mObject = new JSONObject();
//                    mObject.put("Content", mContent);
//                } catch (JSONException e) {
//                    Log.e(TAG, "initData exception:" + e);
//                }
//                mWebView.loadUrl(LOCAL_URL);
//            }
            
            mWebView.loadUrl(LOCAL_URL);
            showLoading();
        }
    }

    private void obtainSquareDetailResultInfoRequest() {
		// TODO Auto-generated method stub
    	BaseRequest request = new BaseRequest();
		// 请求地址
		String requestID = String.format(
				Constant.RequestContstants.Request_Square_Events_ResultInfo, mArticleId);
		doAsync(false, requestID, request, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
//				ToastUtils.showCustomToast("点赞成功");
				try {
					mObject = new JSONObject(GsonUtils.objectToJson(GsonUtils.getJsonValue(result, Constant.IntentKey.eventsResult)));
					
					mWebView.loadUrl(LOCAL_URL);
		            showLoading();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}

	private void bindData(final boolean updateWebContent) {
        runOnUiThread(new Runnable() {
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

    private void showContent() {
        mLoadingLayout.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
        mReloadingLayout.setVisibility(View.GONE);
    }

    private void showReloadingContent() {
        mLoadingLayout.setVisibility(View.GONE);
        mWebView.setVisibility(View.GONE);
        mReloadingLayout.setVisibility(View.VISIBLE);
        mReloadingLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    private void showLoading() {
        mWebView.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mReloadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
//        ArticleDbWarpper article = new ArticleDbWarpper();
//        article.Title = mTitle;
//        if (mRightButton.getId() == v.getId()) {
//            if (mArticleId != -1){
//                finish();
//            } else {
//                mArticleShareRL.setVisibility(View.VISIBLE);
//                Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
//                mArticleShareRL.startAnimation(fadeIn);
//            }
//        } else if (mLeftButton.getId() == v.getId()) {
//            finish();
//        } else if (R.id.hide_area == v.getId()) {
//            mArticleShareRL.setVisibility(View.GONE);
//        } else if (R.id.share_weibo == v.getId()) {
//            shareToSinaWeibo(article);
//            mArticleShareRL.setVisibility(View.GONE);
//        } else if (R.id.share_wx == v.getId()) {
//            shareToWeixin(article);
//            mArticleShareRL.setVisibility(View.GONE);
//        } else if (R.id.share_wx_moments == v.getId()) {
//            shareToWechatMoments(article);
//            mArticleShareRL.setVisibility(View.GONE);
//        } else if (R.id.share_copylink == v.getId()) {
//            copyLink(article);
//            mArticleShareRL.setVisibility(View.GONE);
//        }
    }

//    @Override
//    public void onMessage(Message msg) {
//        switch (msg.what) {
//            case MessageCode.GET_EVENT_RESULT:
//                if (msg.getData().getInt(Utils.KEY_REQUEST_ID) == mRequestResultId) {
//                    mRequestResultId = Utils.INVALID_ID;
//                    if (msg.obj != null) {
//                        try {
//                            mObject = new JSONObject(msg.obj.toString()).getJSONObject("result").getJSONObject("EventsResult");
//                        } catch (JSONException e) {
//                            Log.e(TAG, "onMessage exception:" + e);
//                        }
//                        bindData(true);
//                    } else {
//                        showReloadingContent();
//                    }
//                }
//                break;
//        }
//    }

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.activity_gift_result;
	}

	@Override
	protected void initNavBar() {
		// TODO Auto-generated method stub
		setTitle(R.string.welfare_detail_result);
	}

	@Override
	public void rightNavClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}
}