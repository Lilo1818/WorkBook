package com.mci.firstidol.activity;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.AlertDialog;
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
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.fragment.welfare.CommonUtils;
import com.mci.firstidol.model.ArticleModel;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.SquareModel;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.SquareModel.LiveAnnex;
import com.mci.firstidol.model.SquareModel.QiNiuTokenModel;
import com.mci.firstidol.model.SquareRequest.SquareLiveChatSaveRequest;
import com.mci.firstidol.model.UploadArticleModel;
import com.mci.firstidol.utils.AsyncTaskUtils;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.utils.LogUtils;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.ToastUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

public class ApplyVideoActivity extends BaseActivity implements OnClickListener {

	private EditText edit_content;// 文本内容
	private ImageView image_video;// 视频图片

	private QiNiuTokenModel qiNiuTokenModel;

	private UploadManager mUploadManager;

	private static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/DCIM/Camera");

	private UploadHolder mUploadHolders;
	private File mCurrentPhotoFile;

	private static final int REQUEST_TAKE_VIDEO = 2;
	private static final int REQUEST_SELECT_VIDEO = 3;

	@Override
	protected int getViewId() {
		return R.layout.activity_apply_video;
	}

	@Override
	protected void initNavBar() {
		setTitle(R.string.apply_info);
		setRightBtnBackgroundResource(R.drawable.star_upload_info);
	}

	@Override
	protected void initView() {

		mUploadManager = new UploadManager();

		edit_content = (EditText) findViewById(R.id.text_content);
		image_video = (ImageView) findViewById(R.id.image_video);

		image_video.setOnClickListener(this);
	}

	@Override
	public void rightNavClick() {
		hideKeyboard();
		if (isValidContent()) {
			if (isValidVideo()) {
				showDialog();
				obtainQiNiuTokenRequest();
			} else {
				ToastUtils.showCustomToast(context, "请选择一个视频");
			}
		}
	}

	/**
	 * 是否有视频
	 * 
	 * @return
	 */
	public boolean isValidVideo() {
		boolean result = false;
		if (mUploadHolders != null) {
			String path = mUploadHolders.path;
			if (path != null && !path.equals("")) {
				result = true;
			}
		}
		return result;
	}

	@Override
	protected void findViewById() {

	}

	@Override
	protected void initData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_video:
			showUploadDialog();
			break;
		default:
			break;
		}
	}

	/**
	 * 弹窗dialog
	 */
	private void showUploadDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("发布内容");
		String[] items = null;
		items = new String[] { "小视频", "选择本地视频" };
		builder.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (which == 0) {
							takeVideo();
						} else {
							pickVideoFromGallery();
						}
					}
				});
		AlertDialog dialog = builder.create();
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	/**
	 * 拍视频
	 */
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

	/**
	 * 是否输入文字
	 * 
	 * @return
	 */
	private boolean isValidContent() {
		if (!CommonUtils.hasNetwork(this)) {
			ToastUtils.showCustomToast(getString(R.string.check_network));
			return false;
		}

		String content = edit_content.getText().toString();
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
	 * 视频列表
	 */
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

		switch (requestCode) {
		case REQUEST_SELECT_VIDEO:
			setVideoImage(getRealPathFromURI(data.getData()));
			break;
		case REQUEST_TAKE_VIDEO:
			setVideoImage(mCurrentPhotoFile.getPath());
			break;
		}
	}

	/**
	 * 设置图片
	 * 
	 * @param path
	 */
	private void setVideoImage(String path) {
		mUploadHolders = new UploadHolder();
		mUploadHolders.path = path;
		mUploadHolders.isUploadFinish = false;

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
		mUploadHolders.attachment = attachment;

		image_video.setImageBitmap(ThumbnailUtils.createVideoThumbnail(path,
				Thumbnails.MICRO_KIND));
	}

	/**
	 * 隐藏输入框
	 */
	public void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getCurrentFocus() == null) {
			imm.hideSoftInputFromWindow(edit_content.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		} else {
			imm.hideSoftInputFromWindow(
					this.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 获取七牛 token
	 */
	protected void obtainQiNiuTokenRequest() {
		BaseRequest baseRequest = new BaseRequest();// 其它参数需要继承此类
		// 请求地址 annexType 1：图片，2：音频 3：视频
		String requestID = String.format(
				Constant.RequestContstants.Request_QiNiu_Token, 3);
		doAsync(true, requestID, baseRequest, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(String result) {
				LogUtils.i(result);
				qiNiuTokenModel = (QiNiuTokenModel) GsonUtils.jsonToBean(
						result, QiNiuTokenModel.class);

				// 获取到 token 时，调用七牛接口把数据上传到七牛服务器
				upload();
			}
		});
	}

	/**
	 * 最后上传
	 */
	private void upload() {
		if (isHasAttachment()) {
			uploadToQiniuServer();
		} else {
			obtainArticleSaveRequest();
		}
	}

	/**
	 * 视频是否上传过
	 * 
	 * @return
	 */
	private boolean isHasAttachment() {
		boolean hasAttachment = false;
		if (mUploadHolders != null) {
			hasAttachment = true;
		}

		return hasAttachment;
	}

	/**
	 * 上传到7牛服务器
	 * 
	 * @param isVideo
	 */
	private void uploadToQiniuServer() {
		showDialog();
		final UploadHolder holder = mUploadHolders;
		if (holder != null && !TextUtils.isEmpty(holder.path)) {
			String key = qiNiuTokenModel.getWaterMarkVideo();
			mUploadManager.put(holder.path, key, qiNiuTokenModel.getUpToken(),
					new UpCompletionHandler() {
						@Override
						public void complete(String key, ResponseInfo info,
								JSONObject response) {
							try {
								if (info.isOK()) {
									holder.attachment
											.setVideoCover(AsyncTaskUtils.File_URL
													+ qiNiuTokenModel
															.getVideoCover());
									holder.attachment.setAnnexUrl(AsyncTaskUtils.File_URL
											+ qiNiuTokenModel
													.getWaterMarkVideo());
									holder.attachment.setPersistentId(response
											.getString("persistentId"));

									holder.isUploadFinish = true;
									uploadToEasyServer();
								} else {
									hideDialog();
									ToastUtils.showCustomToast(context, "上传失败");
								}

							} catch (Exception e) {
								e.printStackTrace();
								hideDialog();
							}
						}
					}, null);
		}
	}

	private void uploadToEasyServer() {
		boolean done = true;
		UploadHolder holder = mUploadHolders;
		if (holder != null && !holder.isUploadFinish) {
			done = false;
		}
		hideDialog();
		if (done) {
			obtainArticleSaveRequest();
		}
	}

	/**
	 * 保存发布信息
	 * 
	 * @param attachments
	 */
	protected void obtainArticleSaveRequest() {
		pd.setMessage("正在上传");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		UploadArticleModel uploadArticleModel = new UploadArticleModel();
		uploadArticleModel.requestMethod = Constant.Request_POST;
		ArticleModel articleModel = new ArticleModel();
		articleModel.ChannelId = "49";
		articleModel.Content = edit_content.getText().toString().trim();
		articleModel.ModelType = "2";
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
		if (mUploadHolders != null) {
			articleModel.Ico = AsyncTaskUtils.File_URL
					+ qiNiuTokenModel.getWaterMarkVideo();
			articleModel.VideoIco = mUploadHolders.attachment.getVideoCover();
		}
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

	private class UploadHolder {
		public String path;
		public LiveAnnex attachment;
		public boolean isUploadFinish;
	}

}
