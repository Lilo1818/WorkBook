package com.mci.firstidol.base;

import java.lang.reflect.Field;

import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.AsyncTaskUtils;
import com.mci.firstidol.utils.ParseJson;
import com.mci.firstidol.utils.SyncImageLoader;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by wang on 2015/6/4.
 */
public abstract class BaseFragment extends Fragment {

	public static final int TYPE_ARTICLE = 0;
	public static final int TYPE_GIFT = 1;
	public static final int TYPE_SHOPPING = 2;
	public static final int TYPE_VOTE = 3;
	public static final int TYPE_GIFT_RESULT = 4;

	public final int SHOWDIALOG = 1;
	public final int HIDEDIALOG = 2;

	public ProgressDialog pd;

	public View view;
	protected boolean isInited;
	public Activity activity;

	public ParseJson parseJson;// json解析器
	public boolean isChildFragment;// 是不是嵌套在fragment内

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		syncImageLoader = new SyncImageLoader(activity);
	}

	public void onEventMainThread(AnyEventType event) {
	}

	@Override
	public ViewGroup getView() {
		return (ViewGroup) super.getView();
	}

	protected abstract int getViewId();

	protected View getRootView(Context context) {
		return null;
	}

	protected View findViewById(int id) {
		if (view != null) {
			return view.findViewById(id);
		}
		return null;
	}

	/**
	 * 退出
	 */
	public void exitThis() {
		BaseApp.getInstance().finishActivity(activity);
		activity.overridePendingTransition(R.anim.activity_left_harf_in,
				R.anim.activity_right_out);
	}

	public Handler baseHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SHOWDIALOG:
				if (!activity.isFinishing()) {
					pd.show();
				}
				break;
			case HIDEDIALOG:
				pd.hide();
				break;
			}
		}
	};

	protected abstract void initView();
	protected abstract void findViewById();
	protected abstract void initData();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			if (getViewId() == 0) {
				view = getRootView(activity);
			} else {
				synchronized (BaseFragment.class) {
					view = View.inflate(activity, getViewId(), null);
				}
				findViewById();
				initView();
				initData();
			}
		}
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		if (view == null) {
			if (getViewId() == 0) {
				view = getRootView(activity);
			} else {

				int view_id = getViewId();

				view = View.inflate(activity, view_id, null);
			}
		}
		this.activity = activity;
		parseJson = new ParseJson();

		/**
		 * 初始对话框
		 */
		pd = new ProgressDialog(activity);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCancelable(false);
		pd.setOnKeyListener(onKeyListener);

		super.onAttach(activity);

	}

	/**
	 * 隐藏对话框
	 */
	public void hideDialog() {
		baseHandler.sendEmptyMessage(HIDEDIALOG);
	}

	// 滚动条事件
	private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				activity.dismissDialog(0);
			}
			return false;
		}
	};

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (view == null) {
			return;
		}
		((ViewGroup) view.getParent()).removeView(view);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);// 反注册EventBus
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (!isInited) {
			isInited = true;
			findViewById();
			initView();
			initData();
		}
	}
	
	public void setIsChild(boolean isChild){
		isChildFragment = isChild;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (isChildFragment) {
			try {
				Field childFragmentManager = Fragment.class
						.getDeclaredField("mChildFragmentManager");
				childFragmentManager.setAccessible(true);
				childFragmentManager.set(this, null);
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}

		}
	}

	public SyncImageLoader syncImageLoader;// 图片加载器

	public void loadImage(String path, ImageView iv) {
		Bitmap bm = syncImageLoader.getBitmapFromMemory(path);
		if (bm == null || bm.isRecycled()) {
			syncImageLoader.loadImage(-1, path, imageLoadListener, iv, 1);
		} else {
			iv.setImageBitmap(bm);
		}
	}

	SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {
		@Override
		public void onImageLoad(Integer t, Bitmap bm, View parent) {

			if (bm != null) {
				if (parent != null && parent instanceof ImageView) {
					((ImageView) parent).setImageBitmap(bm);
				}
				doImageLoadFinish(bm);
			}
		}

		@Override
		public void onError(Integer t, View parent) {
			doImageLoadError();
		}
	};

	public void doImageLoadFinish(Bitmap bm) {

	}

	public void doImageLoadError() {

	}

	public void showDialog() {
		((BaseActivity) activity).showDialog();
	}

	public void hidemDialog() {
		((BaseActivity) activity).hideDialog();
	}
	
	@SuppressWarnings("rawtypes")
	protected <T> AsyncTask doAsync(final boolean isShowPorgress,
			final CharSequence requestID, final Object requestObj,
			final Callback<Exception> pExceptionCallback,
			final Callback<T> pCallback) {
		
		return AsyncTaskUtils.doAsync(getActivity(), null,
				getActivity().getResources().getString(R.string.request_loading), requestID,
				requestObj, pCallback, pExceptionCallback, false,
				isShowPorgress);
	}

}
