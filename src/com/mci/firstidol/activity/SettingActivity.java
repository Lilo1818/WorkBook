package com.mci.firstidol.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.VersionManager;
import com.mci.firstidol.utils.VersionManager.AppVersion;
import com.mci.firstidol.utils.VersionManager.OnUpdateListener;

public class SettingActivity extends BaseActivity implements OnClickListener{

	private RelativeLayout layout_clear;//清理占用空间
	private RelativeLayout layout_push;//推送布局
	private RelativeLayout layout_help;//帮助中心
	private RelativeLayout layout_commnet;//评论一下
	private RelativeLayout layout_tucao;//吐槽一下
	private RelativeLayout layout_about;//关于
	private RelativeLayout layout_version;//检查版本更新
	private TextView version;//版本号
	
	@Override
	protected int getViewId() {
		return R.layout.activity_setting;
	}

	@Override
	protected void initNavBar() {
		setTitle(R.string.setting);
		//hideNavBar();
	}

	@Override
	protected void initView() {
		layout_about = (RelativeLayout) findViewById(R.id.layout_about);
		layout_clear = (RelativeLayout) findViewById(R.id.layout_clear);
		layout_commnet = (RelativeLayout) findViewById(R.id.layout_comment);
		layout_help = (RelativeLayout) findViewById(R.id.layout_help);
		layout_push = (RelativeLayout) findViewById(R.id.layout_push);
		layout_tucao = (RelativeLayout) findViewById(R.id.layout_tucao);
		layout_version = (RelativeLayout) findViewById(R.id.layout_version);
		version = (TextView) findViewById(R.id.version);
		String mVerson = getVersionCode(this);
		version.setText("当前版本为："+mVerson);
		layout_about.setOnClickListener(this);
		layout_clear.setOnClickListener(this);
		layout_commnet.setOnClickListener(this);
		layout_help.setOnClickListener(this);
		layout_push.setOnClickListener(this);
		layout_tucao.setOnClickListener(this);
		layout_version.setOnClickListener(this);
		
	}

	@Override
	public void rightNavClick() {
		
	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void findViewById() {
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_clear:
			
			break;
		case R.id.layout_push:
			Utily.go2Activity(context, SendSettingActivity.class);
			break;
		case R.id.layout_help:
			Utily.go2Activity(context, HelpActivity.class);
			break;
		case R.id.layout_comment:
			break;
		case R.id.layout_tucao:
			Utily.go2Activity(context, UploadSuggestionActivity.class);
			break;
		case R.id.layout_about:
			Utily.go2Activity(context, AboutActivity.class);
			break;
		case R.id.layout_version:
			RegistVersionCode();
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	private String getVersionCode(Context context)
	{
		String versionCode = "0";
		try
		{
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo("com.mci.firstidol", 0).versionName;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionCode;
	}
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				Toast.makeText(getApplicationContext(), "无更新", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "有更新", Toast.LENGTH_SHORT).show();
				
				break;
			default:
				break;
			}
		}
	};
	
	
	
	private void RegistVersionCode(){
		String processURL = Constant.RequestContstants.Request_Version;
		ConnectionService.getInstance().serviceConnUseGet(context, processURL,
				null, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						//Log.v("TextVii", "json值"+ Result);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							JSONObject jsonObject1 = jsonObject.getJSONObject("result");
							String Version = jsonObject1.getString("Version");
							String Url = jsonObject1.getString("Url");
							String Message = jsonObject1.getString("Message");
							//Log.v("TextVii", "json值"+ Version);
							String versionName = getVersionCode(SettingActivity.this);
							Log.v("TextVii", "json值"+ Version + "和" + versionName);
							int code = getCodeInt(Version);
							int xCode = getCodeInt(versionName);
							if(code > xCode){
								//handler.sendEmptyMessage(1);
								//Toast.makeText(getApplicationContext(), "有更新", Toast.LENGTH_SHORT).show();
								doNewVersionUpdate("Easy Idol" ,versionName ,"Easy Idol",Version ,Message,Url);
								//setVersion(Url,"Easy Idol",Version);
							}else{
								//handler.sendEmptyMessage(0);
								Toast.makeText(getApplicationContext(), "当前为最新版", Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
							Log.v("TextVii", "异常");
						}
					}
					
					@Override
					public void getError() {

					}
				});
	}
	private int getCodeInt(String version){
		String regEx="[^0-9]";   
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(version);
		return Integer.parseInt(m.replaceAll("").trim());
	}
	
	
	//下载新版本
	private void setVersion(String url,String name,String versionname ){
		AppVersion version = new AppVersion();
		// 设置文件url
		version.setApkUrl(url);
		// 设置文件名
		version.setFileName(name);
		// 设置文件在sd卡的目录
		version.setFilePath("update");
		// 设置app当前版本号
		version.setVersionName(13 + versionname);
		final VersionManager manager = VersionManager
				.getInstance(this, version);
		manager.setOnUpdateListener(new OnUpdateListener() {

			@Override
			public void onSuccess() {
				Toast.makeText(SettingActivity.this, "下载成功等待安装", Toast.LENGTH_LONG)
						.show();
			}

			@Override
			public void onError(String msg) {
				Toast.makeText(SettingActivity.this, "更新失败" + msg,
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onDownloading() {
				Toast.makeText(SettingActivity.this, "正在下载...", Toast.LENGTH_LONG)
						.show();
			}

			@Override
			public void hasNewVersion(boolean has) {
				if (has) {
					Toast.makeText(SettingActivity.this, "检测到有新版本",
							Toast.LENGTH_LONG).show();
					manager.downLoad();
				}
			}
		});
		manager.checkUpdateInfo();
	}
	public ProgressDialog pBar;
	private int newSize = 0;
	private Handler handler1 = new Handler();
	private void doNewVersionUpdate(final String verName,String verCode,String newVerName ,String newVerCode, String vCont,final String url ) {
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(", 发现新版本:");
		sb.append(newVerName);
		sb.append(" Code:");
		sb.append(newVerCode);
		sb.append(", 是否更新?");
		sb.append("更新内容为:");
		sb.append(vCont);
		Dialog dialog = new AlertDialog.Builder(SettingActivity.this)
				.setIcon(R.drawable.icon)
				.setTitle("软件更新")
				.setMessage(sb.toString())
				// 设置内容
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								pBar = new ProgressDialog(SettingActivity.this);
								pBar.setTitle("正在下载");
								pBar.setMessage("请稍候...");
								pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								//downFile(url);
								setVersion(url,"Easy Idol",verName);
							}

						})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 点击"取消"按钮之后退出程序
								//finish();
							}
						}).create();// 创建
		// 显示对话框
		dialog.show();
	}
}
