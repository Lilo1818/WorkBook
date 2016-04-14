package com.mci.firstidol.activity;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.cardemulation.OffHostApduService;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mci.firstidol.R;
import com.mci.firstidol.adapter.SquareFragmentAdapter;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.fragment.personal.DynamicFragment;
import com.mci.firstidol.fragment.personal.FansFragment;
import com.mci.firstidol.fragment.personal.FollowFragment;
import com.mci.firstidol.model.FollowModel;
import com.mci.firstidol.model.UserModel;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.FileUtil;
import com.mci.firstidol.utils.PreferencesUtils;
import com.mci.firstidol.view.ActionSheet;
import com.mci.firstidol.view.ActionSheet.ActionSheetListener;
import com.mci.firstidol.view.RoundedImageView;
import com.mci.firstidol.view.ToastUtils;
import com.viewpagerindicator.TabPageIndicator;

import de.greenrobot.event.EventBus;

@SuppressLint("HandlerLeak")
public class MyInfoActivity extends BaseActivity implements ActionSheetListener{

	private UserModel userModel;// 用户对象
	private Context mContext;
	private String urlpath;			// 图片背景本地路径
	
	private RoundedImageView userIcon;// 用户头像
	private TextView text_name;// 用户名称
	private TextView text_info;// 用户信息
	private TextView text_integration;// 用户积分
	private UserModel uploadUserModel;//上传的对象
	
	private ImageView myinfo_Focus_on;//是否关注图标
	
	
	private ImageView myinfo_back;//返回图标
	
	private boolean has_change = false;// 是否进行了修改
	
    private static final int REQUESTCODE_PICK = 0;		// 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;		// 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;	// 图片裁切标记
    
    private static Boolean isfollow ;
	
    private static final String IMAGE_FILE_NAME = "imagebg.jpg";// 头像文件名称
	
	private RelativeLayout personal_cover;
	
	private static int currentIndex;

	private static final String[] titles = new String[3];
	private static final int[] icons = new int[3];

	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private ArrayList<String> types = new ArrayList<String>();
	private TabPageIndicator indicator;
	private SquareFragmentAdapter adapter;
	private FragmentManager fm;
	private ViewPager pager;

	private DynamicFragment dynamicFragment;// 动态
	private FansFragment fansFragment;// 粉丝
	private FollowFragment followFragment;// 关注

	private String typeCode;// 类型
	private long userId;// 用户的ID

	@Override
	protected int getViewId() {
		userModel = DataManager.userModel;
		Intent intent = getIntent();
		if (intent != null) {
			typeCode = intent.getStringExtra(Constant.IntentKey.typeCode);
			userId = intent.getLongExtra(Constant.IntentKey.userID, 0);
			if (userId == 0) {
				String userIdStr = intent.getStringExtra(Constant.IntentKey.userID);
				if(userIdStr!=null&&!userIdStr.equals("")){
					userId = Long.parseLong(userIdStr);
				}
			}
		}
		if (Constant.IntentValue.ACTIITY_FOLLOWER.equals(typeCode)) {
			currentIndex = 2;
		} else if (Constant.IntentValue.ACTIVITY_FOLLOW.equals(typeCode)) {
			currentIndex = 1;
		}
		
		return R.layout.activity_myinfo;
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch (msg.what) {
			
			case 0:
				if (userModel != null) {
					titles[0] = userModel.DynamicCount + ""
							+ context.getResources().getString(R.string.dynamic);
					titles[1] = userModel.UserFollowCount + ""
							+ context.getResources().getString(R.string.follow);
					titles[2] = userModel.FansCount + ""
							+ context.getResources().getString(R.string.fans);
				}

				fm = ((FragmentActivity) context).getSupportFragmentManager();

				adapter = new SquareFragmentAdapter(fm, fragments, titles, icons,
						types);

				pager.setOffscreenPageLimit(fragments.size());

				pager.setAdapter(adapter);
				indicator.setViewPager(pager);
				getIsFocus();
				indicator.setCurrentItem(currentIndex);
				indicator.setVisibility(View.VISIBLE);
				break;
			case 1:
				userModel.Isfollow = true;
				if (userId == 0) {
					myinfo_Focus_on.setVisibility(View.GONE);
				}else{
					//Toast.makeText(context, "jinlaile,值为+"+userModel.Isfollow, Toast.LENGTH_SHORT).show();
					if(userModel.Isfollow==true){
						myinfo_Focus_on.setImageResource(R.drawable.cancle_follow);
						//Toast.makeText(context, "jinlaile1", Toast.LENGTH_SHORT).show();
					}else{
						myinfo_Focus_on.setImageResource(R.drawable.follow);
						//Toast.makeText(context, "jinlaile2", Toast.LENGTH_SHORT).show();
					}
				}
				myinfo_Focus_on.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
							// TODO Auto-generated method stub
						if(userModel.Isfollow == true){
							String userid = String.valueOf(userId);
							//cancleOrFollow(userid,false);
							cancleFollow(userid);
							myinfo_Focus_on.setImageResource(R.drawable.follow);
						}else{
							String userid = String.valueOf(userId);
							cancleOrFollow(userid,true);
							myinfo_Focus_on.setImageResource(R.drawable.cancle_follow);
						}
					}
				});
				break;
			case 2:
				userModel.Isfollow = false;
				if (userId == 0) {
					myinfo_Focus_on.setVisibility(View.GONE);
				}else{
					if(userModel.Isfollow==true){
						myinfo_Focus_on.setImageResource(R.drawable.cancle_follow);
					}else{
						myinfo_Focus_on.setImageResource(R.drawable.follow);
					}
				}
				myinfo_Focus_on.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
							// TODO Auto-generated method stub
						if(userModel.Isfollow == true){
							String userid = String.valueOf(userId);
							cancleFollow(userid);
							myinfo_Focus_on.setImageResource(R.drawable.follow);
						}else{
							String userid = String.valueOf(userId);
							cancleOrFollow(userid,true);
							myinfo_Focus_on.setImageResource(R.drawable.cancle_follow);
						}
					}
				});
			    break;
			default:
				break;
			}
			
			
			
		}

	};

	/**
	 * 获取用户信息
	 */
	public void getUserInfoRequest() {
		String processURL = Constant.RequestContstants.Request_userinfo + "/"
				+ userId;
		ConnectionService.getInstance().serviceConnUseGet(context, processURL,
				null, new StringCallBack() {

					@SuppressWarnings("unchecked")
					@Override
					public void getSuccessString(String Result) {
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if (check) {

								userModel = (UserModel) parseJson
										.getModelObject(jsonObject,
												UserModel.class);
								initUserInfo(userModel);

								handler.sendEmptyMessage(0);
							} else {
								ToastUtils.showCustomToast(context, "获取用户信息失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCustomToast(context, "获取用户信息失败");
						}
					}

					@Override
					public void getError() {
						ToastUtils.showCustomToast(context, "获取用户信息失败");
					}
				});
	}
	
	/**
	 * 判断是否已经关注
	 */
	public void getIsFocus(){
		String processURL = Constant.RequestContstants.Request_user_isfollow + "/"
				+ userId;
		ConnectionService.getInstance().serviceConnUseGet(context, processURL,
				null, new StringCallBack() {

					@SuppressWarnings("unchecked")
					@Override
					public void getSuccessString(String Result) {
						try {
							JSONObject jsonObject = new JSONObject(Result);
							
							String isFollow = jsonObject.getString("result");
							//Toast.makeText(context, "值为+"+isFollow, Toast.LENGTH_SHORT).show();
							if(isFollow.equals("true")){
								handler.sendEmptyMessage(1);
								userModel.Isfollow = true;
							}else{
								handler.sendEmptyMessage(2);
								userModel.Isfollow = false;
							}
							
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCustomToast(context, "获取用户信息失败");
						}
					}
					
					@Override
					public void getError() {
						ToastUtils.showCustomToast(context, "获取用户信息失败");
					}
				});
	}
	
	@Override
	protected void initNavBar() {
		hideNavBar();
	}

	@Override
	protected void initView() {
		
		userIcon = (RoundedImageView) findViewById(R.id.icon_mine);

		text_name = (TextView) findViewById(R.id.text_name);
		text_info = (TextView) findViewById(R.id.text_content);
		text_integration = (TextView) findViewById(R.id.text_integration);
		myinfo_Focus_on = (ImageView) findViewById(R.id.myinfo_Focus_on);
		personal_cover = (RelativeLayout) findViewById(R.id.personal_cover);
		myinfo_back = (ImageView)findViewById(R.id.myinfo_back);
		if(text_integration!=null){
			if(userId==0){
				text_integration.setVisibility(View.GONE);
				
				personal_cover.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showSelectActionSheet();
					}
				});
				
			}else{
				text_integration.setVisibility(View.GONE);
			}
		}
		
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		pager = (ViewPager) findViewById(R.id.pager);

		if (userModel != null) {
			titles[0] = userModel.DynamicCount + ""
					+ context.getResources().getString(R.string.dynamic);
			titles[1] = userModel.UserFollowCount + ""
					+ context.getResources().getString(R.string.follow);
			titles[2] = userModel.FansCount + ""
					+ context.getResources().getString(R.string.fans);
		}

		initFragment();
		getIsFocus();
	}

	@Override
	public void rightNavClick() {

	}

	@Override
	protected void findViewById() {

	}

	/**
	 * 初始化fragment
	 */
	public void initFragment() {

		dynamicFragment = new DynamicFragment();
		dynamicFragment.setIsChild(true);
		Bundle bundle = new Bundle();
		if (userId != 0) {
			bundle.putString(Constant.IntentKey.userID, userId + "");
		}
		dynamicFragment.setArguments(bundle);

		followFragment = new FollowFragment();
		followFragment.setIsChild(true);
		Bundle followBundle = new Bundle();
		if (userId != 0) {
			followBundle.putString(Constant.IntentKey.userID, userId + "");
		}
		followFragment.setArguments(followBundle);

		fansFragment = new FansFragment();
		fansFragment.setIsChild(true);
		Bundle fansBundle = new Bundle();
		if (userId != 0) {
			fansBundle.putString(Constant.IntentKey.userID, userId + "");
		}
		fansFragment.setArguments(fansBundle);
		fragments.add(dynamicFragment);
		fragments.add(followFragment);
		fragments.add(fansFragment);
	}

	@Override
	protected void initData() {
		// 用户ID

		userModel = DataManager.userModel;
		if (userId > 0) {
			getUserInfoRequest();
		} else if (userModel != null) {
			initUserInfo(userModel);

			fm = ((FragmentActivity) context).getSupportFragmentManager();

			adapter = new SquareFragmentAdapter(fm, fragments, titles, icons,
					types);
			pager.setOffscreenPageLimit(fragments.size());

			pager.setAdapter(adapter);
			indicator.setViewPager(pager);

			indicator.setCurrentItem(currentIndex);
			indicator.setVisibility(View.VISIBLE);
			getIsFocus();
		}

	}

	/**
	 * 初始化用户信息
	 * 
	 * @param userModel
	 */
	public void initUserInfo(UserModel userModel) {
		// 头像
		loadImage(userModel.Avatar, userIcon);

		// 名称
		text_name.setText(userModel.NickName);
		if(userId!=0){
			setTitle(userModel.NickName+"的资料");
		}

		// 信息
		String info = userModel.Signature;
		if (info != null && !info.equals("")) {
			text_info.setText(info);
		} else {
			text_info.setText(context.getResources().getString(
					R.string.info_content));
		}

		// 积分
		text_integration.setText(context.getResources().getString(
				R.string.integration)
				+ "\r\n" + userModel.Points);
		String textbgimage = userModel.BgImage;
		if(textbgimage != null && !textbgimage.equals("")){
			loadImage(userModel.BgImage, myinfo_back);
		}else{
			myinfo_back.setImageResource(R.drawable.tab_bg);
		}
		//Toast.makeText(getApplication(), "值为"+textbgimage, Toast.LENGTH_SHORT).show();
		//Log.w("MyInfoActivity", "背景图为"+userModel.BgImgage);
		
	}
	
	/**
	 * 显示图片的添加选择
	 */
	public void showSelectActionSheet() {

		String[] showType = new String[] { "拍照", "相册" };
		ActionSheet.createBuilder(context, getSupportFragmentManager())
				.setTitle("选择背景").setCancelButtonTitle("取消")
				.setOtherButtonTitles(showType)
				.setCancelableOnTouchOutside(true).setListener(this).show();
	}

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		// TODO Auto-generated method stub
		switch (index) {
		case 0:
			goToTakePhoto();
			break;
		case 1:
			goToSelectPhoto();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 打开相机
	 */
	public void goToTakePhoto() {
		Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//下面这句指定调用相机拍照后的照片存储的路径
		takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, 
				Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
		startActivityForResult(takeIntent, REQUESTCODE_TAKE);
	}

	/**
	 * 打开相册
	 */
	public void goToSelectPhoto() {
		Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
		// 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
		pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(pickIntent, REQUESTCODE_PICK);
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (requestCode) {
		case REQUESTCODE_PICK:// 直接从相册获取
			try {
				startPhotoZoom(data.getData());
			} catch (NullPointerException e) {
				e.printStackTrace();// 用户点击取消操作
			}
			break;
		case REQUESTCODE_TAKE:// 调用相机拍照
			File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
			startPhotoZoom(Uri.fromFile(temp));
			break;
		case REQUESTCODE_CUTTING:// 取得裁剪后的图片
			if (data != null) {
				setPicToView(data);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 裁剪图片方法实现
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm=new DisplayMetrics();
	    manager.getDefaultDisplay().getMetrics(dm);
	    int width2=dm.widthPixels;                    
	    int height2=dm.heightPixels;
	    Log.d("width2", String.valueOf(width2));
	    Log.d("height2", String.valueOf(height2));
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 2);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 800);
		intent.putExtra("outputY", 400);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, REQUESTCODE_CUTTING);
	}
	
	
	/**
	 * 保存裁剪之后的图片数据
	 * @param picdata
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			// 取得SDCard图片路径做显示
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(null, photo);
			urlpath = FileUtil.saveFile(mContext, "temphead.jpg", photo);
			myinfo_back.setImageDrawable(drawable);
			//personal_cover.setBackground(drawable);
			uploadBitmapToServer(urlpath);
		}
	}
	
	/**
	 * 上传图片到服务器
	 * 
	 * @param path
	 */
	public void uploadBitmapToServer(String path) {
		pd.setMessage("图片上传中");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		File uploadFile = new File(path);
		String processURL = Constant.RequestContstants.Request_uploadFile;
		ConnectionService.getInstance().serviceConnWithSingleFile(context,
				processURL, null, "imgFile", uploadFile, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = false;
							if (jsonObject != null && jsonObject.has("error")) {
								String result = jsonObject.get("error")
										.toString().trim();
								if ("0".equals(result)) {
									has_change = true;//已经更新
									check = true;
								}
							}
							if (check) {
								if (jsonObject.has("url")) {
									urlpath = jsonObject.get("url")
											.toString().trim();
								}
							} else {
								ToastUtils.showCustomToast(context, "图片上传失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCustomToast(context, "图片上传失败");
						}
					}

					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						ToastUtils.showCustomToast(context, "图片上传失败");
					}
				});
	}
	
	@Override
	public void leftNavClick() {
		if(has_change){
			uploadUserModel = userModel;
			uploadUserModel.BgImage = urlpath;
			UploadInfoRequest();
		}else{
			super.leftNavClick();
		}
		
	}
	/**
	 * 上传用户信息
	 */
	public void UploadInfoRequest(){
		String processURL = Constant.RequestContstants.Request_upload_info;
		String paramsStr = "{\"userInfo\":"+parseJson.ObjectToJsonStr(uploadUserModel)+"}";
		pd.setMessage("资料提交中");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		
		ConnectionService.getInstance().serviceConn(context, processURL, paramsStr, new StringCallBack() {
			
			@Override
			public void getSuccessString(String Result) {
				baseHandler.sendEmptyMessage(HIDEDIALOG);
				try {
					JSONObject jsonObject = new JSONObject(Result);
					boolean check = parseJson.isCommon(jsonObject);
					if(check){
						DataManager.userModel = (UserModel) parseJson.getModelObject(jsonObject, UserModel.class);
						PreferencesUtils.putComplexObject(getApplicationContext(), "UserModel", DataManager.userModel);
						ToastUtils.showCustomToast(context, "上传成功");
						Constant.is_userinfo_change = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				exit();
			}
			
			@Override
			public void getError() {
				ToastUtils.showCustomToast(context, "提交失败");
				baseHandler.sendEmptyMessage(HIDEDIALOG);
				exit();
			}
		});
	}
	
	/**
	 *退出
	 */
	public void exit(){
		finishActivity(this);
	}
	
	/**
	 * 关注或者取消关注
	 * 
	 * @param position
	 * @param userId
	 * @param isFollow
	 */
	public void cancleFollow(String userId) {
		String processURL = null;
		pd.setMessage("正在取消关注");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		processURL = Constant.RequestContstants.Request_user_cancleFollow+ "/" + userId;

		ConnectionService.getInstance().serviceConnUseGet(getApplicationContext(), processURL,
				null, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if (check) {
								sendFollowNumber(false);
							} else {
								ToastUtils.showCustomToast(getApplicationContext(), "取消失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCustomToast(getApplicationContext(), "取消失败");
						}

					}

					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						ToastUtils.showCustomToast(getApplicationContext(), "取消失败");
					}
				});
	}
	
	
	
	
	
	/**
	 * 关注或者取消关注
	 * 
	 * @param position
	 * @param userId
	 * @param isFollow
	 */
	public void cancleOrFollow(String userId,
			final boolean isFollow) {
		String processURL = null;
		pd.setMessage("正在发送请求");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		if (isFollow) {
			processURL = Constant.RequestContstants.Request_user_follow;
		} else {
			processURL = Constant.RequestContstants.Request_user_cancleFollow;
		}
		processURL = processURL + "/" + userId;

		ConnectionService.getInstance().serviceConnUseGet(getApplicationContext(), processURL,
				null, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if(check){
								//userModel.IsFollow = true;
								if(isFollow){
									userModel.Isfollow = true;
								}else{
									userModel.Isfollow = false;
								}
								sendFollowNumber(isFollow);
								initData();
							}else{
								userModel.Isfollow = false;
								ToastUtils.showCustomToast(getApplicationContext(), "请求失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCustomToast(getApplicationContext(), "请求失败");
						}

					}

					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						ToastUtils.showCustomToast(getApplicationContext(), "请求失败");
					}
				});
	}
	
	/**
	 * 
	 * @param isFollow
	 */
	public void sendFollowNumber(boolean isFollow){
		if(isFollow){
			DataManager.userModel.UserFollowCount++;
		}else{
			DataManager.userModel.UserFollowCount--;
		}
		AnyEventType type = new AnyEventType(Constant.Config.UPDATE_FOLLOW,
				AnyEventType.Type_Default);
		EventBus.getDefault().post(type);
	}
	
}
