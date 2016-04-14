package com.mci.firstidol.activity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.model.CityModel;
import com.mci.firstidol.model.DistrictModel;
import com.mci.firstidol.model.ProvinceModel;
import com.mci.firstidol.model.UserModel;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.FileUtil;
import com.mci.firstidol.utils.NetUtil;
import com.mci.firstidol.utils.PreferencesUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.XmlParserHandler;
import com.mci.firstidol.view.ActionSheet;
import com.mci.firstidol.view.RoundedImageView;
import com.mci.firstidol.view.ActionSheet.ActionSheetListener;
import com.mci.firstidol.view.ToastUtils;
import com.mci.firstidol.view.wheel.OnWheelChangedListener;
import com.mci.firstidol.view.wheel.WheelView;
import com.mci.firstidol.view.wheel.adapters.ArrayWheelAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyInfoEditActivity extends BaseActivity implements
		OnClickListener, OnWheelChangedListener, ActionSheetListener {

	private final int SHOW_DATAPICK = 0;
	private final int DATE_DIALOG_ID = 1;
	
    private static final int REQUESTCODE_PICK = 0;		// 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;		// 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;	// 图片裁切标记
    
    private String urlpath;			// 图片本地路径
    
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    
    private Context mContext;
    
    private String resultStr = "";	// 服务端返回结果集
    
 // 上传服务器的路径【一般不硬编码到程序中】
 	private String imgUrl = "";

	private RoundedImageView icon;// 头像
	private EditText edit_name;// 名称
	private TextView text_sex;// 性别
	private TextView text_date;// 日期
	private TextView text_location;// 所在地
	private TextView text_tip;// 标签
	private TextView text_myShow;// 个人展示

	private RelativeLayout layout_tip;// 标签
	private RelativeLayout layout_myShow;// 个人展示

	private int mYear;// 年
	private int mMonth;// 月
	private int mDay;// 日

	// 所有省
	protected String[] mProvinceDatas;
	// key - 省 value - 市
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	// key - 市 values - 区
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
	// key - 区 values - 邮编
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	// 当前省的名称
	protected String mCurrentProviceName;
	// 当前市的名称
	protected String mCurrentCityName;
	// 当前区的名称
	protected String mCurrentDistrictName = "";

	private LinearLayout layout_wheel;// 地址选择
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;

	private Button btn_ok;
	private Button btn_cancle;

	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;

	private String current_icon_url;// 当前的头像的地址

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode = "";

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 时间格式化格式
	private int current_sex;// 当前的性别
	private UserModel userModel;// 用户对象
	private UserModel uploadUserModel;//上传的对象

	private boolean has_change = false;// 是否进行了修改

	@Override
	protected int getViewId() {
		return R.layout.activity_editinfo;
	}

	@Override
	protected void initNavBar() {
		setTitle(R.string.edit_info);

	}

	@Override
	protected void initView() {

		mImageLoader = ImageLoader.getInstance();
		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true).build();
		userModel = DataManager.userModel;

		icon = (RoundedImageView) findViewById(R.id.icon_mine);
		edit_name = (EditText) findViewById(R.id.edit_nickname);
		text_sex = (TextView) findViewById(R.id.edit_sex);
		text_date = (TextView) findViewById(R.id.edit_birthday);
		text_location = (TextView) findViewById(R.id.edit_location);
		text_tip = (TextView) findViewById(R.id.edit_tip);
		text_myShow = (TextView) findViewById(R.id.edit_myShow);

		layout_tip = (RelativeLayout) findViewById(R.id.layout_tip);
		layout_myShow = (RelativeLayout) findViewById(R.id.layout_myshow);

		layout_wheel = (LinearLayout) findViewById(R.id.layout_wheel);
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);
		btn_cancle = (Button) findViewById(R.id.btn_cancle);
		btn_ok = (Button) findViewById(R.id.btn_ok);

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// 初始化滚轮
		setUpViews();
		setUpListener();
		setUpData();

		text_sex.setOnClickListener(this);
		icon.setOnClickListener(this);
		layout_tip.setOnClickListener(this);
		layout_myShow.setOnClickListener(this);
		text_date.setOnClickListener(this);
		text_location.setOnClickListener(this);
		btn_cancle.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
	}

	@Override
	public void rightNavClick() {

	}

	@Override
	protected void findViewById() {

	}

	@Override
	protected void initData() {
		if (userModel != null) {
			// 头像
			String icon_url = userModel.Avatar;
			ImageLoader.getInstance().displayImage(icon_url, icon);

			// 姓名,性别等
			edit_name.setText(userModel.NickName);
			current_sex = userModel.Sex;
			showSexText();

			// 设置生日。地址
			text_location.setText(userModel.Address);

			String publishData = userModel.BornDate;
			try {
				if (publishData != null && publishData.length() > 10) {
					String dateStr = publishData.substring(6,
							publishData.length() - 2);
					text_date.setText(sdf.format(new Date(Long
							.parseLong(dateStr))));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 设置标签等
			text_tip.setText(userModel.Tags);
			text_myShow.setText(userModel.Signature);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit_birthday:
			handler.sendEmptyMessage(SHOW_DATAPICK);
			break;
		case R.id.edit_location:
			layout_wheel.setVisibility(View.VISIBLE);
			break;
		case R.id.icon_mine:
			showSelectActionSheet();
			break;
		case R.id.edit_sex:
			showUploadDialog();
			break;
		case R.id.layout_tip:
			HashMap<String, String> params = new HashMap<String, String>();
			if (DataManager.userModel != null) {
				params.put(Constant.IntentKey.tip, DataManager.userModel.Tags);
			}
			Utily.go2Activity(context, MyTipActivity.class, params, null);
			break;
		case R.id.layout_myshow:
			Utily.go2Activity(context, MyShowActivity.class);
			break;
		case R.id.btn_cancle:
			layout_wheel.setVisibility(View.GONE);
			break;
		case R.id.btn_ok:
			has_change = true;
			text_location.setText(mCurrentProviceName + "-" + mCurrentCityName
					+ "-" + mCurrentDistrictName);
			layout_wheel.setVisibility(View.GONE);
			break;
		default:
			break;
		}

	}

	/**
	 * 选择性别
	 */
	private void showUploadDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择性别");
		String[] items = null;
		items = new String[] { "男", "女" };
		builder.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						current_sex = which + 1;
						has_change = true;
						showSexText();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	/**
	 * 显示性别
	 */
	public void showSexText() {
		if (current_sex == 1) {
			text_sex.setText("男");
		} else {
			text_sex.setText("女");
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_DATAPICK:
				showDialog(DATE_DIALOG_ID);
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}

		return null;
	}

	/**
	 * 设置日期
	 */
	private void setDateTime() {
		final Calendar c = Calendar.getInstance();

		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		updateDateDisplay();
	}

	/**
	 * 更新日期显示
	 */
	private void updateDateDisplay() {
		has_change = true;
		text_date.setText(new StringBuilder().append(mYear).append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-").append((mDay < 10) ? "0" + mDay : mDay));
	}

	/**
	 * 显示图片的添加选择
	 */
	public void showSelectActionSheet() {

		String[] showType = new String[] { "拍照", "相册" };
		ActionSheet.createBuilder(context, getSupportFragmentManager())
				.setTitle("发布内容").setCancelButtonTitle("取消")
				.setOtherButtonTitles(showType)
				.setCancelableOnTouchOutside(true).setListener(this).show();
	}

	/**
	 * 日期控件的事件
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			updateDateDisplay();
		}
	};

	@Override
	public void onEventMainThread(AnyEventType event) {
		if (event != null) {
			if (Constant.Config.UPDATE_INFO.equals(event.message)) {
				has_change = true;
				text_myShow.setText(event.info);
			}else if(Constant.Config.UPDATE_TIP.equals(event.message)){
				has_change = true;
				text_tip.setText(event.info);
			}
		}
	};

	/**
	 * 解析省市区的XML数据
	 */

	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
		AssetManager asset = getAssets();
		try {
			InputStream input = asset.open("province_data.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			// */ 初始化默认选中的省、市、区
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0)
							.getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			// */
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j)
							.getDistrictList();
					String[] distrinctNameArray = new String[districtList
							.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList
							.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(
								districtList.get(k).getName(), districtList
										.get(k).getZipcode());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(),
								districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}

	private void setUpViews() {
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);
	}

	private void setUpListener() {
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context,
				mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

	}

	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
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
		/*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, 10);*/
		
		
		
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

/*	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 10) {

			// 检测sd是否可用
			if (!Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				return;
			}

			String videoPath = null;
			boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
			if (isKitKat) {
				videoPath = Utily.getPhotoPathFromKitKat(context, data);
			} else {
				videoPath = Utily.getPhotoPath(context, data);
			}
			Log.w("MyInfoEditActivity", "路径为"+videoPath);
			mImageLoader.displayImage("file://" + videoPath, icon, mOptions);
			uploadBitmapToServer(videoPath);
		} else if (requestCode == 0) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String videoPath = cursor.getString(columnIndex);
			mImageLoader.displayImage("file://" + videoPath, icon, mOptions);
			uploadBitmapToServer(videoPath);
		}

	}*/
	
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
	 * 上传图片到服务器
	 * 
	 * @param path
	 */
	public void uploadBitmapToServer(String path) {
		pd.setMessage("头像上传中");
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
									current_icon_url = jsonObject.get("url")
											.toString().trim();
								}
							} else {
								ToastUtils.showCustomToast(context, "头像上传失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCustomToast(context, "头像上传失败");
						}
					}

					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						ToastUtils.showCustomToast(context, "头像上传失败");
					}
				});
	}
	

	@Override
	public void leftNavClick() {
		if(has_change){
			uploadUserModel = userModel;
			uploadUserModel.NickName = edit_name.getText().toString().trim();
			uploadUserModel.Address = text_location.getText().toString().trim();
			uploadUserModel.Avatar = current_icon_url;
			uploadUserModel.Sex = current_sex;
			uploadUserModel.Tags = text_tip.getText().toString().trim();
			uploadUserModel.Signature = text_myShow.getText().toString().trim();
			String timeStr = text_date.getText().toString().trim();
			if(timeStr!=null&&!timeStr.equals("")){
				try {
					Date date = sdf.parse(timeStr);
					uploadUserModel.BornDate = "/Date("+String.valueOf(date.getTime())+")/";
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
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
	 * 裁剪图片方法实现
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, REQUESTCODE_CUTTING);
	}
	
	/**
	 * 保存裁剪之后的图片数据
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			// 取得SDCard图片路径做显示
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(null, photo);
			urlpath = FileUtil.saveFile(mContext, "temphead.jpg", photo);
			icon.setImageDrawable(drawable);
			uploadBitmapToServer(urlpath);
			
			// 新线程后台上传服务端
			/*pd = ProgressDialog.show(mContext, null, "正在上传图片，请稍候...");
			new Thread(uploadImageRunnable).start();*/
		}
	}
	
	/**
	 * 使用HttpUrlConnection模拟post表单进行文件
	 * 上传平时很少使用，比较麻烦
	 * 原理是： 分析文件上传的数据格式，然后根据格式构造相应的发送给服务器的字符串。
	 */
	Runnable uploadImageRunnable = new Runnable() {
		@Override
		public void run() {
			
			if(TextUtils.isEmpty(imgUrl)){
				Toast.makeText(mContext, "还没有设置上传服务器的路径！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Map<String, String> textParams = new HashMap<String, String>();
			Map<String, File> fileparams = new HashMap<String, File>();
			
			try {
				// 创建一个URL对象
				URL url = new URL(imgUrl);
				textParams = new HashMap<String, String>();
				fileparams = new HashMap<String, File>();
				// 要上传的图片文件
				File file = new File(urlpath);
				fileparams.put("image", file);
				// 利用HttpURLConnection对象从网络中获取网页数据
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				// 设置连接超时（记得设置连接超时,如果网络不好,Android系统在超过默认时间会收回资源中断操作）
				conn.setConnectTimeout(5000);
				// 设置允许输出（发送POST请求必须设置允许输出）
				conn.setDoOutput(true);
				// 设置使用POST的方式发送
				conn.setRequestMethod("POST");
				// 设置不使用缓存（容易出现问题）
				conn.setUseCaches(false);
				conn.setRequestProperty("Charset", "UTF-8");//设置编码   
				// 在开始用HttpURLConnection对象的setRequestProperty()设置,就是生成HTML文件头
				conn.setRequestProperty("ser-Agent", "Fiddler");
				// 设置contentType
				conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetUtil.BOUNDARY);
				OutputStream os = conn.getOutputStream();
				DataOutputStream ds = new DataOutputStream(os);
				NetUtil.writeStringParams(textParams, ds);
				NetUtil.writeFileParams(fileparams, ds);
				NetUtil.paramsEnd(ds);
				// 对文件流操作完,要记得及时关闭
				os.close();
				// 服务器返回的响应吗
				int code = conn.getResponseCode(); // 从Internet获取网页,发送请求,将网页以流的形式读回来
				// 对响应码进行判断
				if (code == 200) {// 返回的响应码200,是成功
					// 得到网络返回的输入流
					InputStream is = conn.getInputStream();
					resultStr = NetUtil.readString(is);
				} else {
					Toast.makeText(mContext, "请求URL失败！", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
		}
	};
}
