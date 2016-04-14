package com.mci.firstidol.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.fragment.welfare.CommonUtils;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.SquareModel;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.SquareModel.LiveAnnex;
import com.mci.firstidol.model.SquareModel.QiNiuTokenModel;
import com.mci.firstidol.model.SquareModel.SquareLiveChatModel;
import com.mci.firstidol.model.SquareRequest.SquareLiveChatSaveRequest;
import com.mci.firstidol.utils.AsyncTaskUtils;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.utils.LogUtils;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.ActionSheet;
import com.mci.firstidol.view.ToastUtils;
import com.mci.firstidol.view.ActionSheet.ActionSheetListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

public class UploadActivity extends BaseActivity implements OnClickListener,
		ActionSheetListener {
	private static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/DCIM/Camera");

	private static final int TYPE_TEXT = 0;
	private static final int TYPE_PAI_SHIPIN = 1;
	private static final int TYPE_XUAN_SHIPIN = 2;
	private static final int TYPE_PAI_ZHAOPIAN = 3;
	private static final int TYPE_XUAN_ZHAOPIAN = 4;

	private static final int REQUEST_TAKE_PHOTO = 0;
	private static final int REQUEST_SELECT_PHOTO = 2;
	private static final int REQUEST_TAKE_VIDEO = 1;
	private static final int REQUEST_SELECT_VIDEO = 3;

	private View mImageArea;
	private ImageView mImage1;
	private ImageView mImage2;
	private ImageView mImage3;
	private EditText mContent;

	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;

	// private DataEngineContext mDataEngineContext;
	private File mCurrentPhotoFile;
	private UploadHolder[] mUploadHolders;
	private ArrayList<LiveAnnex> mAttachments;
	private long mArticleId;
	private boolean mIsRequestRoleSuc;
	private int mUserRole;
	private long mUserId;
	private int mParentId;
	private int mType;
	private int mRequestUploadId;
	private int mRequestUploadTokenId;
	private String mToken;
	private String mVideoCover;
	private String mWaterMarkVideo;
	private String mNickName;
	private String mAvatar;

	private ProgressDialog mDialog;
	private UploadManager mUploadManager;

	private int selectType;

	private QiNiuTokenModel qiNiuTokenModel;

	private static final int REQUEST_IMAGE = 2;
	private ArrayList<String> mSelectPath;// 选择图片的路径

	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.activity_upload);
	// initView();
	// initData();
	// }

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.activity_upload;
	}

	@Override
	protected void initNavBar() {
		// TODO Auto-generated method stub
		setRightBtnBackgroundDrawable(getResources().getDrawable(
				R.drawable.square_found_send));
	}

	@Override
	public void rightNavClick() {
		// TODO Auto-generated method stub
		hideKeyboard();
		if (isValidContent()) {
			showDialog();
			if (mType == TYPE_TEXT) {
				upload();
				
			} else {
				// mRequestUploadTokenId =
				// mDataEngineContext.requestUploadToken();
				obtainQiNiuTokenRequest((mType == TYPE_PAI_SHIPIN || mType == TYPE_XUAN_SHIPIN) ? true
						: false);
			}
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

	}

	public void initView() {
		mImageArea = findViewById(R.id.image_area);
		mImage1 = (ImageView) findViewById(R.id.image_1);
		mImage2 = (ImageView) findViewById(R.id.image_2);
		mImage3 = (ImageView) findViewById(R.id.image_3);
		mContent = (EditText) findViewById(R.id.content);

		mImage1.setOnClickListener(this);
		mImage2.setOnClickListener(this);
		mImage3.setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.send).setOnClickListener(this);
	}

	public void initData() {
		mAttachments = new ArrayList<LiveAnnex>();
		mUploadManager = new UploadManager();
		mImageLoader = ImageLoader.getInstance();
		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true).build();

		// mDataEngineContext = DataEngineContext.getInstance(this);
		// mDataEngineContext.registerReceiver(this, this);
		Intent intent = getIntent();
		mArticleId = intent.getLongExtra("articleId", 0);
		mUserId = intent.getLongExtra("userId", 0);
		mIsRequestRoleSuc = intent.getBooleanExtra("isRequestRoleSuc", false);
		mUserRole = intent.getIntExtra("userRole", 0);
		mParentId = intent.getIntExtra("parentId", 0);
		mNickName = intent.getStringExtra("nickName");
		mAvatar = intent.getStringExtra("avatar");
		mType = intent.getIntExtra("type", TYPE_TEXT);

		mUploadHolders = new UploadHolder[3];
		mImageArea.setVisibility(View.VISIBLE);
		mImage2.setVisibility(View.GONE);
		mImage3.setVisibility(View.GONE);
		/*
		 * switch (mType) { case TYPE_TEXT: mImageArea.setVisibility(View.GONE);
		 * break; case TYPE_PAI_SHIPIN: mUploadHolders = new UploadHolder[1];
		 * mImageArea.setVisibility(View.VISIBLE);
		 * mImage2.setVisibility(View.GONE); mImage3.setVisibility(View.GONE);
		 * takeVideo(); break; case TYPE_XUAN_SHIPIN: mUploadHolders = new
		 * UploadHolder[1]; mImageArea.setVisibility(View.VISIBLE);
		 * mImage2.setVisibility(View.GONE); mImage3.setVisibility(View.GONE);
		 * pickVideoFromGallery(); break; case TYPE_PAI_ZHAOPIAN: mUploadHolders
		 * = new UploadHolder[3]; mImageArea.setVisibility(View.VISIBLE);
		 * takePhoto(); break; case TYPE_XUAN_ZHAOPIAN: mUploadHolders = new
		 * UploadHolder[3]; mImageArea.setVisibility(View.VISIBLE);
		 * pickPhotoFromGallery(); break; }
		 */
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_1:
			if (mUploadHolders[0] == null) {
				// showUploadDialog();
				showSelectActionSheet();
			}
			break;
		case R.id.image_2:
			if (mUploadHolders[1] == null) {

				// showUploadDialog();
				showSelectActionSheet();
			}
			break;
		case R.id.image_3:
			if (mUploadHolders[2] == null) {
				// showUploadDialog();
				showSelectActionSheet();
			}
			break;
		case R.id.send:
			hideKeyboard();
			if (isValidContent()) {
				showDialog();
				if (mType == TYPE_TEXT) {
					upload();
				} else {
					// mRequestUploadTokenId =
					// mDataEngineContext.requestUploadToken();
				}
			}
			break;
		case R.id.back:
			finish();
			break;
		}
	}

	public void showSelectActionSheet() {

		String[] showType;
		// if (mType == TYPE_XUAN_SHIPIN || mType == TYPE_PAI_SHIPIN) {
		if (selectType == 1) {
			showType = new String[] { "小视频", "本地视频" };
			// } else if (mType == TYPE_XUAN_ZHAOPIAN || mType ==
			// TYPE_PAI_ZHAOPIAN) {
		} else if (selectType == 2) {
			showType = new String[] { "拍照", "相册" };
		} else {
			showType = new String[] { "小视频", "本地视频", "拍照", "相册" };
		}

		ActionSheet
				.createBuilder(UploadActivity.this, getSupportFragmentManager())
				.setTitle("发布内容").setCancelButtonTitle("取消")
				.setOtherButtonTitles(showType)
				.setCancelableOnTouchOutside(true).setListener(this).show();
	}

	// @Override
	// public void onMessage(Message msg) {
	// switch (msg.what) {
	// case MessageCode.POST_SAVE_CHAT_INFO:
	// if (msg.getData().getInt(Utils.KEY_REQUEST_ID) == mRequestUploadId) {
	// mRequestUploadId = Utils.INVALID_ID;
	// hideDialog();
	// if (msg.obj != null) {
	// CommonRes res = (CommonRes) msg.obj;
	// if (res.isSuc) {
	// CommonUtils.showToast(this, "发布内容成功");
	// finish();
	// } else {
	// CommonUtils.showToast(this, res.message);
	// }
	// }
	// }
	// break;
	// case MessageCode.GET_UPLOAD_TOKEN:
	// if (msg.getData().getInt(Utils.KEY_REQUEST_ID) == mRequestUploadTokenId)
	// {
	// mRequestUploadTokenId = Utils.INVALID_REQUEST_ID;
	// if (msg.arg1 == ErrorCode.OK) {
	// try {
	// CommonRes res = (CommonRes) msg.obj;
	// if (res.isSuc) {
	// JSONObject json = new JSONObject(res.json).getJSONObject("result");
	// mToken = json.getString("UpToken");
	// mVideoCover = json.getString("VideoCover");
	// mWaterMarkVideo = json.getString("WaterMarkVideo");
	// upload();
	// } else {
	// hideDialog();
	// CommonUtils.showToast(this, "发布内容失败");
	// }
	// } catch (JSONException e) {
	// hideDialog();
	// CommonUtils.showToast(this, "发布内容失败");
	// }
	// } else {
	// hideDialog();
	// }
	// }
	// break;
	// }
	// }

	private boolean isValidContent() {
		if (!CommonUtils.hasNetwork(this)) {
			ToastUtils.showCustomToast(getString(R.string.check_network));
			return false;
		}

		String content = mContent.getText().toString();
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

	private void upload() {
		if (mType == TYPE_TEXT) {
			// mRequestUploadId =
			// mDataEngineContext.postSaveChatInfo(mArticleId,
			// mContent.getText().toString(), mUserId, mUserRole,
			// mParentId, mNickName, mAvatar, null);
			obtainChatSaveRequest();
		} else {
			if (isHasAttachment()) {
				uploadToQiniuServer((mType == TYPE_PAI_SHIPIN || mType == TYPE_XUAN_SHIPIN) ? true
						: false);
			} else {
				// mRequestUploadId =
				// mDataEngineContext.postSaveChatInfo(mArticleId,
				// mContent.getText().toString(), mUserId,
				// mUserRole, mParentId, mNickName, mAvatar, null);
				obtainChatSaveRequest();
			}
		}
	}

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
			// mRequestUploadId =
			// mDataEngineContext.postSaveChatInfo(mArticleId,
			// mContent.getText().toString(), mUserId, mUserRole,
			// mParentId, mNickName, mAvatar, mAttachments);
			obtainChatSaveRequest();
		}
	}

	private void showUploadDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("发布内容");
		String[] items = null;
		if (mType == TYPE_XUAN_SHIPIN || mType == TYPE_PAI_SHIPIN) {
			items = new String[] { "小视频", "选择本地视频" };
		} else {
			items = new String[] { "拍照", "从手机相册选择" };
		}
		builder.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (mType == TYPE_XUAN_SHIPIN
								|| mType == TYPE_PAI_SHIPIN) {
							if (which == 0) {
								takeVideo();
							} else {
								pickVideoFromGallery();
							}
						} else {
							if (which == 0) {
								takePhoto();
							} else {
								pickPhotoFromGallery();
							}
						}
					}
				});
		AlertDialog dialog = builder.create();
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
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

	private void takeVideo() {
		try {
			if (!PHOTO_DIR.exists()) {
				PHOTO_DIR.mkdirs();
			}

			String photoFileName = "img_" + System.currentTimeMillis() + ".mp4";
			mCurrentPhotoFile = new File(PHOTO_DIR, photoFileName);

			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(mCurrentPhotoFile));
			intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
			intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 20 * 1024 * 1024);
			startActivityForResult(intent, REQUEST_TAKE_VIDEO);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void pickPhotoFromGallery() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, REQUEST_SELECT_PHOTO);
	}

	private void pickVideoFromGallery() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				"video/*");
		startActivityForResult(intent, REQUEST_SELECT_VIDEO);
	}

	@SuppressWarnings("deprecation")
	private String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		// if(requestCode == REQUEST_IMAGE){
		// if(resultCode == RESULT_OK){
		// // Get the result list of select image paths
		// List<String> path =
		// data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
		// // do your logic ....
		// return;
		// }
		// }

		switch (requestCode) {
		case REQUEST_SELECT_PHOTO:
			mSelectPath = data
					.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
			setImage(mSelectPath);
			break;
		case REQUEST_TAKE_PHOTO:
			setImage(mCurrentPhotoFile.getPath());
			break;
		case REQUEST_SELECT_VIDEO:
			setVideoImage(getRealPathFromURI(data.getData()));
			break;
		case REQUEST_TAKE_VIDEO:
			setVideoImage(mCurrentPhotoFile.getPath());
			break;
		}
	}

	private void setImage(List<String> paths) {
		int j = 0;
		for (int i = 0; i < mUploadHolders.length; i++) {

			if (j >= paths.size()) {
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

				if (i == 0) {
					mImageLoader.displayImage("file://" + paths.get(j),
							mImage1, mOptions);
					mImage2.setVisibility(View.VISIBLE);
				} else if (i == 1) {
					mImageLoader.displayImage("file://" + paths.get(j),
							mImage2, mOptions);
					mImage3.setVisibility(View.VISIBLE);
				} else {
					mImageLoader.displayImage("file://" + paths.get(j),
							mImage3, mOptions);
				}
				// break;
				j++;
			}

		}
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

	private void setImage(String path) {
		for (int i = 0; i < mUploadHolders.length; i++) {
			if (mUploadHolders[i] == null) {
				mUploadHolders[i] = new UploadHolder();
				mUploadHolders[i].path = path;
				mUploadHolders[i].isUploadFinish = false;

				if (mSelectPath == null) {
					mSelectPath = new ArrayList<String>();
				}
				// mSelectPath.add(path);

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

				if (i == 0) {
					mImageLoader.displayImage("file://" + path, mImage1,
							mOptions);
					mImage2.setVisibility(View.VISIBLE);
				} else if (i == 1) {
					mImageLoader.displayImage("file://" + path, mImage2,
							mOptions);
					mImage3.setVisibility(View.VISIBLE);
				} else {
					mImageLoader.displayImage("file://" + path, mImage3,
							mOptions);
				}
				break;
			}
		}
	}

	private void setVideoImage(String path) {
		mUploadHolders[0] = new UploadHolder();
		mUploadHolders[0].path = path;
		mUploadHolders[0].isUploadFinish = false;

		LiveAnnex attachment = new SquareModel().new LiveAnnex();
		attachment.setAnnexType(3);
		attachment.setAnnexUrl(AsyncTaskUtils.File_URL
				+ CommonUtils.createUploadKey(LiveAnnex.TYPE_VIDEO));
		File file = new File(path);
		if (file != null && file.exists()) {
			attachment.setFileName(file.getName());
			attachment.setFileSize(file.length());
			attachment.setFileType("video/mp4");
			int index = attachment.getFileName().indexOf(".");
			if (index != -1) {
				attachment.setOrigVideoUrl(CommonUtils.createFuckKey(attachment
						.getFileName().substring(index)));
			}
		}
		mUploadHolders[0].attachment = attachment;

		mImage1.setImageBitmap(ThumbnailUtils.createVideoThumbnail(path,
				Thumbnails.MICRO_KIND));
	}

	public void showDialog() {
		if (mDialog == null) {
			mDialog = new ProgressDialog(this);
			mDialog.setCancelable(true);
		}

		if (!mDialog.isShowing()) {
			mDialog.setMessage("上传中...");
			mDialog.show();
		}
	}

	public void hideDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	public void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getCurrentFocus() == null) {
			imm.hideSoftInputFromWindow(mContent.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		} else {
			imm.hideSoftInputFromWindow(
					this.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// if (mDataEngineContext != null) {
		// mDataEngineContext.unregisterReceiver(this);
		// }
	}

	private class UploadHolder {
		public String path;
		public LiveAnnex attachment;
		public boolean isUploadFinish;
	}

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		// TODO Auto-generated method stub

		if (selectType == 1) {
			mType = index + 1;
			pickerEvent1();
		} else if (selectType == 2) {
			mType = index + 3;
			pickerEvent2();
		} else {
			mType = index + 1;
			pickerEvent();
		}

	}

	private void pickerEvent() {
		// TODO Auto-generated method stub
		switch (mType) {
		case TYPE_TEXT:
			mImageArea.setVisibility(View.GONE);
			break;
		case TYPE_PAI_SHIPIN:
			goToTakeVideo();
			break;
		case TYPE_XUAN_SHIPIN:
			goToSelectVideo();
			break;
		case TYPE_PAI_ZHAOPIAN:
			goToTakePhoto();
			break;
		case TYPE_XUAN_ZHAOPIAN:
			goToSelectPhoto();
			break;
		}
	}

	private void pickerEvent1() {
		// TODO Auto-generated method stub
		selectType = 1;
		switch (mType) {
		case TYPE_TEXT:
			mImageArea.setVisibility(View.GONE);
			break;
		case TYPE_PAI_SHIPIN:
			goToTakeVideo();
			break;
		case TYPE_XUAN_SHIPIN:
			goToSelectVideo();
			break;
		}
	}

	private void pickerEvent2() {
		// TODO Auto-generated method stub

		switch (mType) {
		case TYPE_TEXT:
			mImageArea.setVisibility(View.GONE);
			break;
		case TYPE_PAI_ZHAOPIAN:
			goToTakePhoto();
			break;
		case TYPE_XUAN_ZHAOPIAN:
			goToSelectPhoto();
			break;
		}
	}

	private void goToSelectPhoto() {
		// TODO Auto-generated method stub
		selectType = 2;
		if (mUploadHolders == null) {
			mUploadHolders = new UploadHolder[3];
			mImageArea.setVisibility(View.VISIBLE);
		}

		// pickPhotoFromGallery();
		showImagePicker();
	}

	private void goToTakePhoto() {
		// TODO Auto-generated method stub
		selectType = 2;
		if (mUploadHolders == null) {
			mUploadHolders = new UploadHolder[3];
			mImageArea.setVisibility(View.VISIBLE);
		}
		takePhoto();
	}

	private void goToSelectVideo() {
		// TODO Auto-generated method stub
		selectType = 1;
		if (mUploadHolders == null) {
			mUploadHolders = new UploadHolder[1];
			mImageArea.setVisibility(View.VISIBLE);
			mImage2.setVisibility(View.GONE);
			mImage3.setVisibility(View.GONE);
		}
		pickVideoFromGallery();
	}

	private void goToTakeVideo() {
		// TODO Auto-generated method stub
		selectType = 1;
		if (mUploadHolders == null) {
			mUploadHolders = new UploadHolder[1];
			mImageArea.setVisibility(View.VISIBLE);
			mImage2.setVisibility(View.GONE);
			mImage3.setVisibility(View.GONE);
		}
		takeVideo();
	}

	/**
	 * 获取七牛 token
	 */
	protected void obtainQiNiuTokenRequest(final boolean isVideo) {
		// TODO Auto-generated method stub
		BaseRequest baseRequest = new BaseRequest();// 其它参数需要继承此类
		// 请求地址 annexType 1：图片，2：音频 3：视频
		String requestID = String
				.format(Constant.RequestContstants.Request_QiNiu_Token,
						isVideo ? 3 : 1);
		doAsync(true, requestID, baseRequest, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				LogUtils.i(result);
				qiNiuTokenModel = (QiNiuTokenModel) GsonUtils.jsonToBean(
						result, QiNiuTokenModel.class);

				// 获取到 token 时，调用七牛接口把数据上传到七牛服务器
				// uploadAudio(qiNiuTokenModel);
				upload();
			}
		});
	}
	
	
	

	private void uploadToQiniuServer(final boolean isVideo) {
		showDialog();
		for (int i = 0; i < mUploadHolders.length; i++) {
			final UploadHolder holder = mUploadHolders[i];
			if (holder != null && !TextUtils.isEmpty(holder.path)) {
				String key = isVideo ? qiNiuTokenModel.getWaterMarkVideo()
						: holder.attachment.getAnnexUrl().replace(
								AsyncTaskUtils.File_URL, "");
				// String key = isVideo ?
				// CommonUtils.createUploadKey(LiveAnnex.TYPE_VIDEO) :
				// CommonUtils.createUploadKey(LiveAnnex.TYPE_AUDIO);
				mUploadManager.put(holder.path, key,
						qiNiuTokenModel.getUpToken(),
						new UpCompletionHandler() {
							@Override
							public void complete(String key, ResponseInfo info,
									JSONObject response) {
								// TODO Auto-generated method stub
								try {
									if (info.isOK()) {
										if (isVideo) {
											holder.attachment.setVideoCover(AsyncTaskUtils.File_URL
													+ qiNiuTokenModel
															.getVideoCover());
											holder.attachment.setAnnexUrl(AsyncTaskUtils.File_URL
													+ qiNiuTokenModel
															.getWaterMarkVideo());
											holder.attachment.setPersistentId(response
													.getString("persistentId"));
										}
										mAttachments.add(holder.attachment);
									}
									holder.isUploadFinish = true;
									uploadToEasyServer();
								} catch (Exception e) {
									e.printStackTrace();
									hideDialog();
								}
							}
						}, null);
			}
		}
	}

	/**
	 * 保存聊天信息
	 * 
	 * @param attachments
	 */
	protected void obtainChatSaveRequest() {
		// TODO Auto-generated method stub
		if (mIsRequestRoleSuc) {
			obtainSaveRequest();
		} else {
			obtainUserRoleRequest();
		}
		
	}
	
	
	/**
     * 获取用户角色
     */
    protected void obtainUserRoleRequest() {
		// TODO Auto-generated method stub
    	BaseRequest baseRequest = new BaseRequest();// 其它参数需要继承此类
		// 请求地址 annexType      1：图片，2：音频 3：视频
		String requestID = String.format(Constant.RequestContstants.Request_LiveChat_UserRole, mArticleId,dataManager.userModel.UserId);
		doAsync(false, requestID, baseRequest, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				 ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				//{"ArticleId":15279.0,"CreateDate":"/Date(1453098142538+0800)/","CreateUser":0.0,"IsDeleted":0.0,"LiveHostId":0.0,"UserId":56521.0,"UserNickName":"","UserRole":0.0}
				mUserRole = (int)Float.parseFloat(GsonUtils.getJsonValue(result, "UserRole").toString());
				
				obtainSaveRequest();
			}
		});
	}
	
	public void obtainSaveRequest(){
		SquareLiveChatSaveRequest request = SquareRequest
				.squareLiveChatSaveRequest();// 其它参数需要继承此类
		request.requestMethod = Constant.Request_POST;
		request.liveChat = initLiveChatModel();
		request.liveAnnexList = mAttachments;
		String requestID = Constant.RequestContstants.Request_LiveChat_Save;
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
//				LogUtils.i(result);
//				ToastUtils.showCustomToast("发布成功");
				ToastUtils.showPointToast("5");//回复加5分
				finishActivity(UploadActivity.this);
			}
		});
	}

	private SquareLiveChatModel initLiveChatModel() {
		// TODO Auto-generated method stub
		SquareLiveChatModel squareLiveChatModel = new SquareModel().new SquareLiveChatModel();
		squareLiveChatModel.setArticleId(mArticleId);
		squareLiveChatModel.setChatContent(mContent.getText().toString());
		squareLiveChatModel.setUserId(dataManager.userModel.UserId);
		squareLiveChatModel.setUserRole(mUserRole);
		squareLiveChatModel.setParentId(0);
		squareLiveChatModel.setUserAvatar(dataManager.userModel.Avatar);
		squareLiveChatModel.setUserNickName(dataManager.userModel.NickName);

		return squareLiveChatModel;
	}

	// 相册多选
	public void showImagePicker() {

		if (mSelectPath!=null) {
			mSelectPath.clear();
		}
		

		int selectedMode = MultiImageSelectorActivity.MODE_MULTI;// 选择模式：单选、多选

		// selectedMode = MultiImageSelectorActivity.MODE_SINGLE;//单选
		selectedMode = MultiImageSelectorActivity.MODE_MULTI;// 多选

		boolean showCamera = false;// 是否显示拍摄按钮

		int maxNum = 3;// 选择图片数量
		boolean isCameraBack = false;// 拍照时是否立即返回

		Intent intent = new Intent(UploadActivity.this,
				MultiImageSelectorActivity.class);
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

}