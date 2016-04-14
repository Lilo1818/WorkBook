package com.mci.firstidol.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.view.ToastUtils;

@SuppressLint("NewApi")
public class WebActivity extends BaseActivity {

	public WebView webView;// webview的展示页面
	public WebSettings webSettings;// webView的权限获取

	private String content;// 显示的内容

	@Override
	protected int getViewId() {
		return R.layout.activity_webview;
	}

	@Override
	protected void initNavBar() {
		setTitle(R.string.shopping_detail);

	}

	@Override
	protected void initView() {
		webView = (WebView) findViewById(R.id.webView);

		webSettings = webView.getSettings();

		// 增加权限
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(webViewClient);

	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		if (intent != null) {
			content = intent.getExtras().getString("URL");
		}
		webView.loadUrl(content);
//		webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
	}

	@Override
	public void rightNavClick() {
		// TODO Auto-generated method stub

	}

	/**
	 * 初始化webViewClient
	 */
	WebViewClient webViewClient = new WebViewClient() {

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
			if( url.startsWith("http:") || url.startsWith("https:") ) {  
                return false;  
            }  
			
//			view.loadUrl(url);
			return true;
		}// 重写点击动作,用webview载入

	};

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}
	
	
	//改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//返回上一页面
                return true;
            }
            else
            {
            	finishActivity(this);
//                System.exit(0);//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
