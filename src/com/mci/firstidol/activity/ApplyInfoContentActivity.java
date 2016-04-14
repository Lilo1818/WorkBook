package com.mci.firstidol.activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mci.firstidol.R;
import com.mci.firstidol.adapter.UploadPhotoAdapter;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.fragment.welfare.CommonUtils;
import com.mci.firstidol.model.ArticleModel;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.PictureModel;
import com.mci.firstidol.model.SquareModel;
import com.mci.firstidol.model.UploadArticleModel;
import com.mci.firstidol.model.UserModel;
import com.mci.firstidol.model.SquareModel.LiveAnnex;
import com.mci.firstidol.model.SquareModel.QiNiuTokenModel;
import com.mci.firstidol.utils.AsyncTaskUtils;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.ActionSheet;
import com.mci.firstidol.view.ToastUtils;
import com.mci.firstidol.view.ActionSheet.ActionSheetListener;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

public class ApplyInfoContentActivity extends BaseActivity implements
		OnClickListener, ActionSheetListener {

	private static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/DCIM/Camera");

	private static final int REQUEST_TAKE_PHOTO = 0;
	private static final int REQUEST_SELECT_PHOTO = 2;

	private File mCurrentPhotoFile;

	private UploadHolder[] mUploadHolders;
	private List<UploadHolder> uploList;

	private QiNiuTokenModel qiNiuTokenModel;
	private UploadManager mUploadManager;

	private ArrayList<LiveAnnex> mAttachments;

	private EditText text_content;// 内容
	private GridView grid_photo;// 照片库

	private RelativeLayout layout_starTrip;// 添加明星行程布局
	private UserModel userModel;// 用户对象
	
	private boolean uploadQiniuResult = true;

	private ArrayList<String> mSelectPath;// 选择图片的路径
	private BaseAdapter photoAdapter;// 图片加载器
	
	private int done_count = 0;//完成上传的次数

	@Override
	protected int getViewId() {
		return R.layout.activity_apply_info;
	}

	@Override
	protected void initNavBar() {
		setTitle(R.string.apply_info);
		setRightBtnBackgroundResource(R.drawable.star_upload_info);
	}

	@Override
	protected void initView() {

		mAttachments = new ArrayList<LiveAnnex>();
		mUploadManager = new UploadManager();

		text_content = (EditText) findViewById(R.id.text_content);
		layout_starTrip = (RelativeLayout) findViewById(R.id.layout_addStarTrip);
		grid_photo = (GridView) findViewById(R.id.hot_grid);
		grid_photo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if(uploList!=null&&uploList.size()>0){
					if(position==uploList.size()-1){
						showSelectActionSheet();
					}
				}else{
					showSelectActionSheet();
				}
			}
		});

		layout_starTrip.setOnClickListener(this);
		//isMasterRequest();
		initPhoto();
	}

	@Override
	public void rightNavClick() {
		hideKeyboard();
		if (isValidContent()) {
			if(isValidPhoto()){
				showDialog();
				obtainQiNiuTokenRequest();
			}else{
				ToastUtils.showCustomToast(context, "请选择照片，至少一张");
			}
		}
	}
	
	/**
	 * 是否含有照片
	 * @return
	 */
	public boolean isValidPhoto(){
		boolean result = false;
		if(mUploadHolders!=null&&mUploadHolders.length>0){
			int length = mUploadHolders.length;
			for (int i = 0; i < length; i++) {
				UploadHolder uploadHolder = mUploadHolders[i];
				if(uploadHolder!=null){
					String path = uploadHolder.path;
					if(path!=null&&!path.equals("")){
						result = true;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 上传
	 */
	private void upload() {
		if (isHasAttachment()) {
			uploadToQiniuServer();
		} else {
			obtainPhotoSaveRequest();
		}
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
	 * 选择照片
	 */
	private void goToSelectPhoto() {
		if (mUploadHolders == null) {
			mUploadHolders = new UploadHolder[9];
		}
		
		if (mSelectPath == null) {
			mSelectPath = new ArrayList<String>();
		}
		showImagePicker();
	}

	/**
	 * 拍照片
	 */
	private void goToTakePhoto() {
		if (mUploadHolders == null) {
			mUploadHolders = new UploadHolder[9];
		}
		takePhoto();
	}

	private void takePhoto() {
		try {
			if (!PHOTO_DIR.exists()) {
				PHOTO_DIR.mkdirs();
			}

			String photoFileName = "img_" + System.currentTimeMillis() + ".jpg";
			mCurrentPhotoFile = new File(PHOTO_DIR, photoFileName);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(mCurrentPhotoFile));
			startActivityForResult(intent, REQUEST_TAKE_PHOTO);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 上传债片到七牛
	 * 
	 * @param isVideo
	 */
	private void uploadToQiniuServer() {
		showDialog();
		uploadQiniuResult = true;
		done_count = 0;
		for (int i = 0; i < mUploadHolders.length; i++) {
			final UploadHolder holder = mUploadHolders[i];
			if (holder != null && !TextUtils.isEmpty(holder.path)) {
				String key = holder.attachment.getAnnexUrl().replace(
						AsyncTaskUtils.File_URL, "");
				mUploadHolders[i].web_url = key;
				mUploadManager.put(holder.path, key,
						qiNiuTokenModel.getUpToken(),
						new UpCompletionHandler() {
							@Override
							public void complete(String key, ResponseInfo info,
									JSONObject response) {
								try {
									if (info.isOK()) {
										mAttachments.add(holder.attachment);
									}else{
										uploadQiniuResult = false;
										hideDialog();
										ToastUtils.showCustomToast(context, "上传失败");
									}
									holder.isUploadFinish = true;
									handler.sendEmptyMessage(0);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}, null);
			}
		}
	}
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				done_count++;
				if(done_count==uploList.size()-1){
					uploadToEasyServer();
				}
				break;

			default:
				break;
			}
		};
	};

	// 相册多选
	public void showImagePicker() {

		mSelectPath.clear();

		int selectedMode = MultiImageSelectorActivity.MODE_MULTI;// 选择模式：单选、多选

		selectedMode = MultiImageSelectorActivity.MODE_MULTI;// 多选

		boolean showCamera = false;// 是否显示拍摄按钮

		int maxNum = 9;// 选择图片数量
		if (mUploadHolders != null) {
			maxNum = 9 - mUploadHolders.length;
		}
		boolean isCameraBack = false;// 拍照时是否立即返回

		Intent intent = new Intent(context, MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA,
				showCamera);

		// 选择模式
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
				selectedMode);
		// 拍完照是否返回
		intent.putExtra(MultiImageSelectorActivity.EXTRA_CAMERA_BACK,
				isCameraBack);
		// 默认选择
		if (mSelectPath != null && mSelectPath.size() > 0) {
			intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,
					checkUploadHoldersNum());

			intent.putExtra(
					MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
					mSelectPath);
		} else {
			// 最大可选择图片数量
			intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,
					checkUploadHoldersNum());
		}
		startActivityForResult(intent, REQUEST_SELECT_PHOTO);
	}

	public int checkUploadHoldersNum() {
		int num = 0;
		for (int i = 0; i < mUploadHolders.length; i++) {
			if (mUploadHolders[i] == null) {
				num++;
			}
		}
		return num;
	}

	/**
	 * 上传到服务端
	 */
	private void uploadToEasyServer() {
		boolean done = true;
		for (int i = 0; i < mUploadHolders.length; i++) {
			UploadHolder holder = mUploadHolders[i];
			if (holder != null && !holder.isUploadFinish) {
				done = false;
			}
		}
		if (done) {
			hideDialog();
			obtainPhotoSaveRequest();
		}
	}
	
	//获取bitmap
	public static Bitmap GetLocalOrNetBitmap(String url)  
    {  
        Bitmap bitmap = null;  
        InputStream in = null;  
        BufferedOutputStream out = null;  
        try  
        {  
            in = new BufferedInputStream(new URL(url).openStream());  
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();  
            out = new BufferedOutputStream(dataStream);  
            out.flush();         
            byte[] data = dataStream.toByteArray();  
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);  
            data = null;  
            return bitmap;  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
            return null;  
        }  
    }  
	
	/**
	 * 保存聊天信息
	 * 
	 * @param attachments
	 */
	protected void obtainPhotoSaveRequest() {
		pd.setMessage("正在提交信息");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		UploadArticleModel uploadArticleModel = new UploadArticleModel();
		uploadArticleModel.requestMethod = Constant.Request_POST;
		ArticleModel articleModel = new ArticleModel();
		List<PictureModel> pics = new ArrayList<PictureModel>();
		BitmapFactory.Options options = new BitmapFactory.Options();
		Bitmap bitmap = BitmapFactory.decodeFile(mSelectPath.get(0), options); 
		String width = String.valueOf(options.outWidth);
		String height = String.valueOf(options.outHeight);
		articleModel.ChannelId = "49";
		articleModel.Content = text_content.getText().toString().trim();
		articleModel.ModelType = "1";
		articleModel.IcoHeight = height;
		articleModel.IcoWidth = width;
		if (Constant.starModel != null) {
			articleModel.StarId = String.valueOf(Constant.starModel.starId);
			articleModel.StarName = Constant.starModel.StarName;
		}
		if (DataManager.userModel != null) {
			articleModel.UserId = String.valueOf(DataManager.userModel.UserId);
			articleModel.UserAvatar = DataManager.userModel.Avatar;
			articleModel.UserNickName = DataManager.userModel.NickName;
			if (DataManager.userModel.IsStar == 1) {
				articleModel.IsStarPublish = "1";
			} else {
				articleModel.IsStarPublish = "0";
			}
		}
		 if(mUploadHolders!=null&&mUploadHolders.length>0){
			 int length = mUploadHolders.length;
			 PictureModel pictureModel = null;
			 for (int i = 0; i < length; i++) {
				 UploadHolder uploadHolder = mUploadHolders[i];
				 String path = null;
				 if(uploadHolder!=null){
					path = uploadHolder.web_url;
				 }
				 if(path!=null&&!path.equals("")){
					 path =  AsyncTaskUtils.File_URL + path;
					 if(articleModel.Ico==null||articleModel.Ico.equals("")){
						 articleModel.Ico = path;
					 }
					 BitmapFactory.Options options1 = new BitmapFactory.Options();
					 options1.inJustDecodeBounds = true;
					 Bitmap bitmap2 = BitmapFactory.decodeFile(mSelectPath.get(i), options1);
					 String width1 = String.valueOf(options1.outWidth);
					 String height1 = String.valueOf(options1.outHeight);
					 Log.v("Tsarrsad", "地址为+"+mSelectPath); 
					 pictureModel = new PictureModel();
					 pictureModel.PicPath = path;
					 pictureModel.PicHeight = height1;
					 pictureModel.PicWidth = width1;
					 pics.add(pictureModel);
				 }
			}
		 }
		uploadArticleModel.pics = pics;
		uploadArticleModel.article = articleModel;
		String processURL = Constant.RequestContstants.Request_article_add;
		String params = parseJson.ObjectToJsonStr(uploadArticleModel);
		ConnectionService.getInstance().serviceConn(context, processURL,
				params, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if (check) {
								ToastUtils.show(context, "上传成功");
								exitThis();
							} else {
								ToastUtils.show(context, "上传失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.show(context, "上传失败");
						}
					}
					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						ToastUtils.show(context, "上传失败");
					}
				});
	}

	/**
	 * 
	 * @return
	 */
	private boolean isValidContent() {
		if (!CommonUtils.hasNetwork(this)) {
			ToastUtils.showCustomToast(getString(R.string.check_network));
			return false;
		}
		String content = text_content.getText().toString();
		if (TextUtils.isEmpty(content)) {
			Toast.makeText(this, "请输入发布内容", Toast.LENGTH_LONG).show();
			return false;
		}
		if (content.length() < 5) {
			Toast.makeText(this, "发布内容不能少于5个字", Toast.LENGTH_LONG).show();
			return false;
		}
		if (content.length() > 140) {
			Toast.makeText(this, "发布内容不能多于140个字", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	/**
	 * 获取七牛 token
	 */
	protected void obtainQiNiuTokenRequest() {
		BaseRequest baseRequest = new BaseRequest();// 其它参数需要继承此类
		// 请求地址 annexType 1：图片，2：音频 3：视频
		String requestID = String.format(
				Constant.RequestContstants.Request_QiNiu_Token, 1);
		doAsync(true, requestID, baseRequest, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(String result) {
				qiNiuTokenModel = (QiNiuTokenModel) GsonUtils.jsonToBean(
						result, QiNiuTokenModel.class);
				// 获取到 token 时，调用七牛接口把数据上传到七牛服务器
				upload();
			}
		});
	}

	/**
	 * 隐藏软键盘
	 */
	public void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getCurrentFocus() == null) {
			imm.hideSoftInputFromWindow(text_content.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		} else {
			imm.hideSoftInputFromWindow(
					this.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 判断是否有上传
	 * 
	 * @return
	 */
	private boolean isHasAttachment() {
		boolean hasAttachment = false;
		for (int i = 0; i < mUploadHolders.length; i++) {
			if (mUploadHolders[i] != null) {
				hasAttachment = true;
				break;
			}
		}
		return hasAttachment;
	}
	
	@Override
	protected void findViewById() {
	}

	@Override
	protected void initData() {
	}
	/**
	 * 图片显示
	 */
	public void initPhoto() {
		
		uploList = new ArrayList<>();
		if(mUploadHolders!=null&&mUploadHolders.length>0){
			int length = mUploadHolders.length;
			for (int i = 0; i < length; i++) {
				if(mUploadHolders[i]!=null){
					String path = mUploadHolders[i].path;
					if(path!=null&&!path.equals("")){
						uploList.add(mUploadHolders[i]);
					}
				}
			}
		}
		if(uploList.size()<9){
			UploadHolder uploadHolder = new UploadHolder();
			uploList.add(uploadHolder);
		}
		photoAdapter = new UploadPhotoAdapter(context, null, null,
				uploList);
		grid_photo.setAdapter(photoAdapter);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_addStarTrip:
			Utily.go2Activity(context, UploadStarTripActivity.class);
			break;
		default:
			break;
		}
	}

	public class UploadHolder {
		public String path;
		public LiveAnnex attachment;
		public boolean isUploadFinish;
		public String web_url;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case REQUEST_SELECT_PHOTO:
			mSelectPath = data
					.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
			setImage(mSelectPath);
			break;
		case REQUEST_TAKE_PHOTO:
			setImage(mCurrentPhotoFile.getPath());
			break;
		}
	}

	private void setImage(List<String> paths) {
		int j = 0;
		for (int i = 0; i < mUploadHolders.length; i++) {

			if (j >= paths.size()) {
				initPhoto();
				return;
			}

			if (mUploadHolders[i] == null) {
				mUploadHolders[i] = new UploadHolder();
				mUploadHolders[i].path = paths.get(j);
				mUploadHolders[i].isUploadFinish = false;

				LiveAnnex attachment1 = new SquareModel().new LiveAnnex();
				attachment1.setAnnexType(1);
				attachment1.setAnnexUrl(AsyncTaskUtils.File_URL
						+ CommonUtils.createUploadKey(LiveAnnex.TYPE_IMAGE));
				File file = new File(paths.get(j));
				if (file != null && file.exists()) {
					attachment1.setFileName(file.getName());
					attachment1.setFileSize(file.length());
					attachment1.setFileType("image/jpg");
				}
				mUploadHolders[i].attachment = attachment1;

				j++;
			}

		}
		initPhoto();
	}

	private void setImage(String path) {
		for (int i = 0; i < mUploadHolders.length; i++) {
			if (mUploadHolders[i] == null) {
				mUploadHolders[i] = new UploadHolder();
				mUploadHolders[i].path = path;
				mUploadHolders[i].isUploadFinish = false;

				if (mSelectPath == null) {
					mSelectPath = new ArrayList<String>();
				}

				LiveAnnex attachment1 = new SquareModel().new LiveAnnex();
				attachment1.setAnnexType(1);
				attachment1.setAnnexUrl(AsyncTaskUtils.File_URL
						+ CommonUtils.createUploadKey(LiveAnnex.TYPE_IMAGE));
				File file = new File(path);
				if (file != null && file.exists()) {
					attachment1.setFileName(file.getName());
					attachment1.setFileSize(file.length());
					attachment1.setFileType("image/jpg");
				}
				mUploadHolders[i].attachment = attachment1;

				break;
			}
		}
		initPhoto();
	}

}
