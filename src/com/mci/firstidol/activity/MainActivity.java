package com.mci.firstidol.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.shinsoft.Model;
import cn.shinsoft.query.Select;

import com.imobile.mixobserver.util.AppUpgradeService;
import com.mci.firstidol.R;
import com.mci.firstidol.adapter.MyStarAdapter;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.fragment.PersonalFragment;
import com.mci.firstidol.fragment.ShoppingFragment;
import com.mci.firstidol.fragment.SquareFragment;
import com.mci.firstidol.fragment.StarFragment;
import com.mci.firstidol.fragment.StarFragment.StarSelected;
import com.mci.firstidol.fragment.welfare.Configs;
import com.mci.firstidol.model.StarModel;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.CommonUtils;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.DateHelper;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.utils.PreferencesUtils;
import com.mci.firstidol.utils.SystemBarTintManager;
import com.mci.firstidol.utils.TabManager;
import com.mci.firstidol.utils.VersionManager;
import com.mci.firstidol.utils.TabManager.ChangeTable;
import com.mci.firstidol.utils.UpdateManager;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.VersionManager.AppVersion;
import com.mci.firstidol.utils.VersionManager.OnUpdateListener;
import com.mci.firstidol.view.MyGridView;
import com.mci.firstidol.view.ToastUtils;

import de.greenrobot.event.EventBus;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.networkbench.agent.impl.NBSAppAgent;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements ChangeTable,
		StarSelected {

	private SquareFragment squareFragment;// 广场fragment
	private StarFragment starFragment;// 追星fragment
	private ShoppingFragment shoppingFragment;// 商场fragment
	private PersonalFragment personalFragment;// 我的fragment

	public String square = "guangchang";// 广场
	public String star = "star";// 追星
	public String shopping = "shopping";// 商店
	public String personal = "personal";// 我的

	private RelativeLayout square_index;// 广场table
	private RelativeLayout star_index;// 追星tab
	private RelativeLayout shopping_index;// 商店tab
	private RelativeLayout personal_index;// 我的tab

	private PopupWindow starPopWindows;// 明星选项框

	private TabHost tabHost;// 分类的tabhost
	private TabManager mTabManager;// tab管理

	private int star_selected_item = 0;// 明星选中的item

	private View view_myStar;
	private MyGridView star_gridView;// 我的明星列表
	private BaseAdapter starAdapter;// 明星适配器

	private boolean onlyGetStar = false;// 只是获取明星

	private FrameLayout fl_main_hint;
	private ImageView iv_main_show;
	private int index = 0;

	private boolean is_master = false;// 用户是否是版主
	private boolean has_check_master = false;// 是否发送过请求
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// if (Build.VERSION.SDK_INT < 16) {
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// }
		super.onCreate(savedInstanceState);
		/*UpdateManager manager = new UpdateManager(MainActivity.this);
		// 检查软件更新
		manager.checkUpdate();*/
		NBSAppAgent.setLicenseKey("23049b827ac8435b931c4cab1a84b8fc").withLocationServiceEnabled(true).start(getApplicationContext());
		RegistVersionCode();
		
	}

	@Override
	protected int getViewId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initNavBar() {
		hideLeftBtn();

	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void initView() {
		initMainHint();
		

		tabHost = (TabHost) findViewById(android.R.id.tabhost);

		view_myStar = LayoutInflater.from(context).inflate(
				R.layout.my_gridview, null);
		star_gridView = (MyGridView) view_myStar.findViewById(R.id.myGridView);

		// 得到fragment
		squareFragment = new SquareFragment();
		starFragment = new StarFragment();
		starFragment.setSatrSelected(this);
		shoppingFragment = new ShoppingFragment();
		personalFragment = new PersonalFragment();
		//isMasterRequest();
		initTab();
		getStar();
	}

	private void initMainHint() {
		// TODO Auto-generated method stub
		boolean isShowMainHint = PreferencesUtils.getBoolean(context,
				"IsShowMainHint");
		// isShowMainHint = false;
		if (!isShowMainHint) {// 显示提示界面
			hideNavBar();
			PreferencesUtils.putBoolean(context, "IsShowMainHint", true);

			fl_main_hint = (FrameLayout) findViewById(R.id.fl_main_hint);
			iv_main_show = (ImageView) findViewById(R.id.iv_main_show);

			fl_main_hint.setVisibility(View.VISIBLE);

			final int[] resIds = new int[] { R.drawable.new_feature_4,
					R.drawable.new_feature_5, R.drawable.new_feature_6,
					R.drawable.new_feature_7, R.drawable.new_feature_8 };

			iv_main_show.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					index++;
					if (index == resIds.length) {// 隐藏提示界面
						showNavBar();
						fl_main_hint.setVisibility(View.GONE);

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
							setTranslucentStatus(false);
							SystemBarTintManager mTintManager = new SystemBarTintManager(
									MainActivity.this);
							mTintManager.setStatusBarTintEnabled(true);
							mTintManager.setNavigationBarTintEnabled(true);
							// mTintManager.setTintColor(0xF00099CC);
							mTintManager.setTintDrawable(getResources()
									.getDrawable(R.drawable.white));
						}

						return;
					}

					iv_main_show.setImageResource(resIds[index]);

				}
			});
		}
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	/**
	 * 得到明星
	 */
	public void getStar() {
		List<Model> list = new Select().from(StarModel.class).execute();
		if (list != null && list.size() > 0) {
			int star_id = PreferencesUtils.getStarId(context);
			int length = list.size();
			for (int i = 0; i < length; i++) {
				StarModel starModel = (StarModel) list.get(i);
				if (star_id == starModel.starId) {
					Constant.starModel = starModel;
				}
			}
			if (Constant.starModel == null) {
				Constant.starModel = (StarModel) list.get(0);
			}
		}
	}

	@Override
	protected void initData() {
		// 在此处理百度推送信息
		Bundle pBundle = getIntent().getExtras();
		if (pBundle != null) {
			String customContent = pBundle
					.getString(Constant.IntentKey.customContent);
			if (!TextUtils.isEmpty(customContent)) {// 非空
				obtainBaiduPushClickEvent(customContent);
			}
		}

		// 在此初始化每天登录提示积分信息
		if (!dataManager.isLogin) {
			return;
		}

		ArrayList<HashMap<String, String>> loginDays = (ArrayList<HashMap<String, String>>) PreferencesUtils
				.getComplexObject(context, "LoginDays");
		// if (loginDays!=null) {
		// loginDays.clear();
		// }
		//
		// PreferencesUtils.putComplexObject(context, "LoginDays", loginDays);

		if (loginDays != null && loginDays.size() > 0) {// 已经有值
			HashMap<String, String> lastDayDict = loginDays.get(loginDays
					.size() - 1);// 获取最后一条数据

			// 判断是否是今天
			String result = lastDayDict.get(DateHelper
					.getCurrentDate("yyyy-MM-dd"));
			if ("Y".equals(result)) {// 今天已经签到
				// 不做处理
			} else {// 今天未签到
				// 先判断是否连续签到
				// 获取当天的前一条的签到结果
				String value = lastDayDict.get(DateHelper.addDay(
						DateHelper.getCurrentDate("yyyy-MM-dd"), -1));
				if ("Y".equals(value)) {// 说明连续签到
					// 判断是否满足连续七天签到
					if (loginDays.size() == 6) {// 已经连签6天，今天是第七天
						ToastUtils.showPointToast("10");
						HashMap<String, String> pointDict = new HashMap<String, String>();
						pointDict.put(DateHelper.getCurrentDate("yyyy-MM-dd"),
								"Y");
						loginDays.add(pointDict);
					} else if (loginDays.size() == 7) {// 连续低8天时，清空之前七天的，今天重新开始计算
						loginDays.clear();
						HashMap<String, String> pointDict = new HashMap<String, String>();
						pointDict.put(DateHelper.getCurrentDate("yyyy-MM-dd"),
								"Y");
						loginDays.add(pointDict);
					} else {// 不满足7天签到的
						HashMap<String, String> pointDict = new HashMap<String, String>();
						pointDict.put(DateHelper.getCurrentDate("yyyy-MM-dd"),
								"Y");
						loginDays.add(pointDict);
					}
					// 保存
					PreferencesUtils.putComplexObject(context, "LoginDays",
							loginDays);
				} else {// 没有连续签到
					ToastUtils.showPointToast("5");
					loginDays.clear();
					PreferencesUtils.putComplexObject(context, "LoginDays",
							loginDays);
				}
			}
		} else {// 首次记录
			ToastUtils.showPointToast("5");

			loginDays = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> pointDict = new HashMap<String, String>();
			pointDict.put(DateHelper.getCurrentDate("yyyy-MM-dd"), "Y");
			loginDays.add(pointDict);

			// 保存
			PreferencesUtils.putComplexObject(context, "LoginDays", loginDays);
		}
	}

	private void obtainBaiduPushClickEvent(String customContentString) {
		// TODO Auto-generated method stub
		long articleId = 0, articleType = 0, channelId = 0, modelType = 0;
		// 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
		if (!TextUtils.isEmpty(customContentString)) {
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(customContentString);
				articleId = customJson.getLong("rel_id");
				articleType = customJson.getLong("rel_type");
				channelId = customJson.getLong("rel_channel");
				modelType = customJson.getLong("rel_modeltype");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		// updateContent(context, notifyString);
		//
		//
		// Intent intent = new Intent();
		// intent.setClass(context.getApplicationContext(),
		// LoginActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// context.getApplicationContext().startActivity(intent);

		// 根据ChannelId 和 ModelType来跳转不同页面
		// SquareLiveModel squareLiveModel = headers.get(position);
		if (channelId == 49) {// 发现
			if (modelType == 2) {// 跳转到视频页面
				Bundle bundle = new Bundle();
				bundle.putBoolean("IsVideo", true);
				bundle.putLong(Constant.IntentKey.articleID, articleId);
				Utily.go2Activity(context, SquareFoundDetailActivity.class,
						bundle, true);
			} else {// 普通页面
				Bundle bundle = new Bundle();
				bundle.putLong(Constant.IntentKey.articleID, articleId);
				Utily.go2Activity(context, SquareFoundDetailActivity.class,
						bundle, true);
			}
		} else if (channelId == 44) {// 商城
			Bundle mbuBundle = new Bundle();
			mbuBundle.putBoolean(Constant.IntentKey.isSearch, true);
			mbuBundle.putSerializable(Constant.IntentKey.articleID, articleId);
			mbuBundle.putString(Constant.IntentKey.typeCode,
					Constant.IntentValue.ACTIVITY_SHOPPING);
			Utily.go2Activity(context, SquareWelfareDetailActivity.class,
					mbuBundle, true);
		} else if (channelId == 43) {// 福利
			Bundle mbuBundle = new Bundle();
			mbuBundle.putBoolean(Constant.IntentKey.isSearch, true);
			mbuBundle.putSerializable(Constant.IntentKey.articleID, articleId);
			Utily.go2Activity(context, SquareWelfareDetailActivity.class,
					mbuBundle, true);
		} else if (channelId == 47) {// 直播
			Bundle mbuBundle = new Bundle();
			mbuBundle.putBoolean(Constant.IntentKey.isSearch, true);
			mbuBundle.putSerializable(Constant.IntentKey.articleID, articleId);
			Utily.go2Activity(context, SquareLiveActivity.class, mbuBundle,
					true);
		} else {// 文章详情,web
			Bundle mbuBundle = new Bundle();
			mbuBundle.putBoolean("IsFromFound", true);
			mbuBundle.putBoolean(Constant.IntentKey.isSearch, true);
			mbuBundle.putSerializable(Constant.IntentKey.articleID, articleId);
			Utily.go2Activity(context, SquareWelfareDetailActivity.class,
					mbuBundle, true);
		}
	}

	/**
	 * 初始化tab选项卡
	 */
	public void initTab() {
		tabHost.setup();

		if (mTabManager == null) {
			mTabManager = new TabManager(this, null, tabHost,
					android.R.id.tabcontent, context, this);
		}

		// 广场
		square_index = initTabItem(R.drawable.square_tabstyle, R.string.square);

		// 追星
		star_index = initTabItem(R.drawable.star_tabstyle, R.string.star);

		// 商店
		shopping_index = initTabItem(R.drawable.shopping_tabstyle,
				R.string.shopping);

		// 我的
		personal_index = initTabItem(R.drawable.personal_tabstyle,
				R.string.personal);

		mTabManager.addTab(tabHost.newTabSpec(square)
				.setIndicator(square_index), squareFragment, null);
		mTabManager.addTab(tabHost.newTabSpec(star).setIndicator(star_index),
				starFragment, null);
		mTabManager.addTab(
				tabHost.newTabSpec(shopping).setIndicator(shopping_index),
				shoppingFragment, null);
		mTabManager.addTab(
				tabHost.newTabSpec(personal).setIndicator(personal_index),
				personalFragment, null);

		tabHost.setCurrentTab(0);

	}

	/**
	 * 设置tab的item头部
	 * 
	 * @param drawable_res
	 *            图片资源
	 * @param string_res
	 *            文字资源
	 * @return
	 */
	public RelativeLayout initTabItem(int drawable_res, int string_res) {
		RelativeLayout layout = null;
		try {
			layout = (RelativeLayout) getLayoutInflater().inflate(
					R.layout.tab_item, null);
			ImageView index_icon = (ImageView) layout
					.findViewById(R.id.item_icon);
			TextView index_name = (TextView) layout
					.findViewById(R.id.item_name);
			index_name.setText(context.getResources().getString(string_res));
			index_icon.setBackgroundResource(drawable_res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return layout;
	}

	@Override
	public void getChangeTitle(String title) {
		//isMasterRequest();
		if (personal.equals(personal)) {
			setTitle(R.string.personal);
			hideBarButton();
		}
		if (square.equals(title)) {
			setTitle(R.string.square);
			hideBarButton();
		} else if (star.equals(title)) {
			if (Constant.starModel != null) {
				setTitle(Constant.starModel.StarName);
			} else {
				setTitle(R.string.star);
			}
			Constant.hasClickStar = true;
			// 添加判断是否登录
			initLoginEvent();
			showBarButton();
			
			selectedItem(star_selected_item);
			if (!has_check_master) {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						isMasterRequest();
						
					}
				}, 2000);
			}
			
		} else if (shopping.equals(title)) {
			setTitle(R.string.shopping);
			hideBarButton();
		} else if (personal.equals(personal)) {
			setTitle(R.string.personal);
			hideBarButton();
		}

	}

	@Override
	public void leftNavClick() {
		if (DataManager.isLogin) {
			if (starPopWindows != null && starPopWindows.isShowing()) {
				starPopWindows.dismiss();
			} else {
				showStarData();
			}

		} else {
			ToastUtils.showCustomToast(context, "暂未登录");
		}

	}

	@Override
	public void rightNavClick() {
		if (DataManager.isLogin) {
			if (star_selected_item == 0) {
				Utily.go2Activity(context, ApplyInfoContentActivity.class);
			} else if (star_selected_item == 2) {
				Utily.go2Activity(context, ApplyVideoActivity.class);
			} else if (star_selected_item == 3) {
				if(is_master == true){
					Utily.go2Activity(context, UploadStarTripActivity.class);
				}else{
					ToastUtils.showCustomToast(context, "您还不是版主");
				}
				//Utily.go2Activity(context, UploadStarTripActivity.class);
			}
		} else {
			ToastUtils.showCustomToast(context, "您还未登陆");
		}

	}

	/**
	 * 显示明星的两端button
	 */
	public void showBarButton() {
		showRightBtn();
		showLeftBtn();
		setLeftBtnBackgroundResource(R.drawable.star_exchange);
		setRightBtnBackgroundResource(R.drawable.star_update);
	}

	/**
	 * 关闭明星两端的button
	 */
	public void hideBarButton() {
		hideLeftBtn();
		hideRightBtn();
	}

	private void initLoginEvent() {
		if (!dataManager.isLogin) {// 未登录
			Utily.go2Activity(context, LoginActivity.class);
		}
	}

	/**
	 * 获取我的明星的网络请求
	 */
	public void getMyStarsRequest() {
		pd.setMessage("获取明星");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		String processURL = Constant.RequestContstants.URL
				+ Constant.RequestContstants.Request_myStar_list;

		ConnectionService.getInstance().serviceConnUseGet(context, processURL,
				null, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if (check) {
								List<Object> list = parseJson.getModelList(
										jsonObject, StarModel.class);
								if (list != null && list.size() > 0) {
									if (onlyGetStar) {
										saveStar(list);
										Constant.starModel = (StarModel) list
												.get(0);
										updateStarNotice();
									} else {
										saveStar(list);
										initStarList(list);
										Constant.starModel = (StarModel) list
												.get(0);
									}
								} else {
									ToastUtils.showCustomToast(context,
											"没有关注的明星");
									sendStarError(Constant.ConfigValue.NOSTARDATA);
								}
							} else {
								ToastUtils.showCustomToast(context, "获取我的明星失败");
								sendStarError(Constant.ConfigValue.STARDATAERROR);
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCustomToast(context, "获取我的明星失败");
							sendStarError(Constant.ConfigValue.STARDATAERROR);
						}
					}

					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						ToastUtils.showCustomToast(context, "获取我的明星失败");
					}
				});
	}

	/**
	 * 显示数据
	 */
	public void showStarData() {
		List<Model> list = new Select().from(StarModel.class).execute();
		if (list != null && list.size() > 0) {
			List<Object> modelList = new ArrayList<Object>();
			int length = list.size();
			for (int i = 0; i < length; i++) {
				modelList.add(list.get(i));
			}
			initStarList(modelList);
		} else {
			getMyStarsRequest();
		}
	}

	/**
	 * 保存明星到数据库
	 * 
	 * @param starList
	 */
	public void saveStar(List<Object> starList) {
		int length = starList.size();
		for (int i = 0; i < length; i++) {
			StarModel starModel = (StarModel) starList.get(i);
			starModel.save();
		}
	}

	/**
	 * 初始化明星表格
	 * 
	 * @param starList
	 */
	public void initStarList(final List<Object> starList) {

		starAdapter = new MyStarAdapter(context, starList, null);
		star_gridView.setAdapter(starAdapter);
		star_gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Constant.starModel = (StarModel) starList.get(position);
				PreferencesUtils.setStarId(context, Constant.starModel.starId);
				updateStarNotice();
				if (starPopWindows != null && starPopWindows.isShowing()) {
					starPopWindows.dismiss();
					isMasterRequest();
				}
			}
		});

		starPopWindows = new PopupWindow(view_myStar,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		starPopWindows.setOutsideTouchable(true);
		starPopWindows.setBackgroundDrawable(new BitmapDrawable());
		starPopWindows.setAnimationStyle(R.style.mypopwindow_anim_style);
		starPopWindows.showAsDropDown(titleTextView);
	}

	@Override
	protected void findViewById() {

	}

	@Override
	public void selectedItem(int position) {
		star_selected_item = position;
		//isMasterRequest();
		switch (position) {
		case 0:
			
			showRightBtn();
			break;
		case 1:
			
			hideRightBtn();
			break;
		case 2:
			
			showRightBtn();
			break;
		case 3:
			if (is_master) {
				showRightBtn();
			} else {
				hideRightBtn();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onEventMainThread(AnyEventType event) {
		if (event != null) {
			if (Constant.Config.LOGIN.equals(event.message)) {
				getMyStarsRequest();
				onlyGetStar = true;
			} else if (Constant.Config.SELECTED_STAR.equals(event.message)) {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						if (tabHost != null) {
							tabHost.setCurrentTab(1);
						}

					}
				}, 500);

				Constant.is_selected_star = false;
				updateStarNotice();
			}
		}
	};

	/**
	 * 是否是版主
	 */
	public void isMasterRequest() {
		String userId = null;
		String starId = null;
		if (DataManager.userModel != null) {
			userId = String.valueOf(DataManager.userModel.UserId);
		}
		if (Constant.starModel != null) {
			starId = String.valueOf(Constant.starModel.starId);
		}
		String processURL = Constant.RequestContstants.Request_isMaster
				+ starId + "/" + userId;
		Log.v("TextUserid", "值为+"+userId + starId);
		ConnectionService.getInstance().serviceConnUseGet(context, processURL,
				null, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						has_check_master = true;
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							
							if (check) {
								is_master = parseJson.isResultTrue(jsonObject);
								//Toast.makeText(getApplication(), "结果为+"+is_master, Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void getError() {

					}
				});
	}

	/**
	 * 更新明星通知
	 */
	public void updateStarNotice() {
		if (Constant.hasClickStar) {
			AnyEventType type = new AnyEventType(Constant.Config.UPDATE_STAR,
					AnyEventType.Type_Default);
			EventBus.getDefault().post(type);
			if (Constant.starModel != null) {
				setTitle(Constant.starModel.StarName);
			} else {
				setTitle(R.string.star);
			}
		}
	}

	/**
	 * 更新出错
	 * 
	 * @param errorType
	 */
	public void sendStarError(int errorType) {
		AnyEventType type = new AnyEventType(Constant.Config.UPDATE_STAR_ERROR,
				errorType);
		EventBus.getDefault().post(type);
	}
	
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
							String versionName = getVersionCode(MainActivity.this);
							Log.v("TextVii", "json值"+ Version + "和" + versionName);
							int code = getCodeInt(Version);
							int xCode = getCodeInt(versionName);
							if(code > xCode){
								//handler.sendEmptyMessage(1);
								//Toast.makeText(getApplicationContext(), "有更新", Toast.LENGTH_SHORT).show();
								//setVersion(Url,"Easy Idol",Version);
								doNewVersionUpdate("Easy Idol" ,versionName ,"Easy Idol",Version ,Message,Url);
							}else{
								//handler.sendEmptyMessage(0);
								//Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
								
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
	
	private int getCodeInt(String version){
		String regEx="[^0-9]";   
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(version);
		return Integer.parseInt(m.replaceAll("").trim());
	}
	
	//下载新版本
	private void setVersion(String url,String name,String versionname){
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
					Toast.makeText(MainActivity.this, "下载成功等待安装", Toast.LENGTH_LONG)
							.show();
				}

				@Override
				public void onError(String msg) {
					Toast.makeText(MainActivity.this, "更新失败" + msg,
							Toast.LENGTH_LONG).show();
				}

				@Override
				public void onDownloading() {
					Toast.makeText(MainActivity.this, "正在下载...", Toast.LENGTH_LONG)
							.show();
				}

				@Override
				public void hasNewVersion(boolean has) {
					if (has) {
						Toast.makeText(MainActivity.this, "检测到有新版本",
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
		private void doNewVersionUpdate(final String verName,String verCode,String newVerName ,String newVerCode, String vCont ,final String url ) {
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
			Dialog dialog = new AlertDialog.Builder(MainActivity.this)
					.setIcon(R.drawable.icon)
					.setTitle("软件更新")
					.setMessage(sb.toString())
					// 设置内容
					.setPositiveButton("更新",// 设置确定按钮
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									pBar = new ProgressDialog(MainActivity.this);
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
