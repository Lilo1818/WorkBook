package com.mci.firstidol.activity;

import android.annotation.SuppressLint;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;

@SuppressLint("NewApi")
public class AboutActivity extends BaseActivity {

	private WebView webView;//网页
	private String url;
	
	@Override
	protected int getViewId() {
		return R.layout.activity_about;
	}

	@Override
	protected void initNavBar() {
		setTitle(R.string.setting_about);
	}

	@Override
	protected void initView() {
		
		url = "http://item.ieasy.tv/aboutus/index.html";
		webView = (WebView) findViewById(R.id.webView);
		
		WebSettings webSettings = webView.getSettings();
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setSupportZoom(false);
		webSettings.setJavaScriptEnabled(true);// 可用JS
		webSettings.setAllowFileAccessFromFileURLs(true);
		webSettings.setAllowUniversalAccessFromFileURLs(true);
		webView.setScrollBarStyle(0);// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				loadurl(url);// 载入网页
				return true;
			}// 重写点击动作,用webview载入

			@Override
			public void onPageFinished(WebView view, String url) {
				android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
						android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
						android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
				webView.setLayoutParams(params);
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				if (failingUrl.contains("#")) {
					String[] temp;
					temp = failingUrl.split("#");
					webView.loadUrl(temp[0]);
					try {
						Thread.sleep(400);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					webView.loadUrl(failingUrl);
				}
			}
			

		});
		
		loadurl(url);
	}
	
	public void loadurl(final String url){
		webView.post(new Runnable() {
			
			@Override
			public void run() {
				webView.loadUrl(url);
				
			}
		});
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
