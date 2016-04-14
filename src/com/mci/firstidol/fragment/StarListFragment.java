package com.mci.firstidol.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.shinsoft.Model;
import cn.shinsoft.query.Delete;
import cn.shinsoft.query.Select;

import com.mci.firstidol.R;
import com.mci.firstidol.activity.AllStarActivity;
import com.mci.firstidol.activity.OnlyFragemntActivity.EditInterface;
import com.mci.firstidol.adapter.MyStarAdapter;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.StarModel;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.ToastUtils;

import de.greenrobot.event.EventBus;

public class StarListFragment extends BaseFragment implements EditInterface {

	protected final int INIT_DATA = 1;
	private final int STAR_DELETE = 10;// 删除明星
	public List<Object> modelList;// 数据模型列表

	public GridView gridView;// 数据视图

	public BaseAdapter adapter;// 数据适配器

	public LinearLayout loading_layout;// 圈圈页
	public LinearLayout error_layout;// 错误页

	public ImageView image_error;// 错误页icon
	public TextView text_error;// 错误页info

	public int isShowError = 0;// 是否显示错误页 1：错误页 2：空数据

	public boolean isDelete = false;// 是否处于删除状态

	@Override
	protected int getViewId() {
		return R.layout.layout_mystar;
	}

	@Override
	protected void initView() {
		initViewNormal();
		getDataView();

	}

	/**
	 * 初始化得到数据
	 */
	public void getDataView() {
		showLoadingView();
		List<Model> list = new Select().from(StarModel.class).execute();
		if (list != null && list.size() > 0) {
			modelList = new ArrayList<Object>();
			int length = list.size();
			for (int i = 0; i < length; i++) {
				modelList.add(list.get(i));
			}
			handler.sendEmptyMessage(INIT_DATA);
		} else {
			getDataFromServer();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (Constant.is_selectedChanged) {
			getDataView();
			Constant.is_selectedChanged = false;
		}
	}

	/**
	 * 得到数据页面
	 */
	public void initViewNormal() {

		// 得到listview视图
		gridView = (GridView) findViewById(R.id.myGridView);

		// 得到圈圈页 错误页 内容页
		loading_layout = (LinearLayout) findViewById(R.id.loading_layout);
		error_layout = (LinearLayout) findViewById(R.id.error_layout);

		image_error = (ImageView) findViewById(R.id.image_errorView);
		text_error = (TextView) findViewById(R.id.textview_error);

		// 初始化空页事件和错误页事件
		image_error.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isShowError == 1) {
					retryClick();
				} else if (isShowError == 2) {
					nodataClick();
				}
			}
		});

		// 增加触发事件
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (isDelete && modelList.size() == position) {
					Utily.go2Activity(activity, AllStarActivity.class);
				}else if(!isDelete){
					Constant.starModel = (StarModel) modelList.get(position);
					changeStar();
					Constant.is_selected_star = true;
					exitThis();
				}
			}
		});

	}

	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case INIT_DATA:
				if (isDelete) {
					initEditData();
				} else {
					initData();
				}
				showContentView();
				break;
			case STAR_DELETE:
				StarModel starModel = (StarModel) msg.obj;
				deleteStarRequest(starModel);
				break;
			}
		}
	};
	
	/**
	 *切换明星
	 */
	public void changeStar(){
		AnyEventType type = new AnyEventType(Constant.Config.SELECTED_STAR, AnyEventType.Type_Default);
        EventBus.getDefault().post(type);
	}

	/**
	 * 删除  
	 * 
	 * @param starModel
	 */
	public void deleteStarRequest(final StarModel starModel) {
		pd.setMessage("正在取消关注");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		String processURL = Constant.RequestContstants.Request_cancle_followStar
				+ "/" + starModel.starId;
		ConnectionService.getInstance().serviceConnUseGet(activity, processURL,
				null, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if (check) {
								deleteStar(starModel);
							} else {
								String errorMsg = parseJson
										.getErrorMessage(jsonObject);
								ToastUtils.show(activity, errorMsg);
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.show(activity, "删除失败");
						}

					}

					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						ToastUtils.show(activity, "删除失败");
					}

				});
	}

	/**
	 * 删除明星操作
	 * 
	 * @param starModel
	 */
	public void deleteStar(StarModel starModel) {
		int starId = starModel.starId;
		// 删除本地缓存
		try {
			String whereStr = String.format("starId=%s", starId);
			new Delete().from(StarModel.class).where(whereStr).executeSingle();
			int length = modelList.size();
			for (int i = 0; i < length; i++) {
				if (starId == ((StarModel) modelList.get(i)).starId) {
					modelList.remove(i);
					break;
				}
			}
			initEditData();
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtils.show(activity, "删除失败");
		}

	}

	/**
	 * 显示圈圈页
	 */
	public void showLoadingView() {
		loading_layout.setVisibility(View.VISIBLE);
		gridView.setVisibility(View.GONE);
		error_layout.setVisibility(View.GONE);
	}

	/**
	 * 显示数据页
	 */
	public void showContentView() {
		loading_layout.setVisibility(View.GONE);
		gridView.setVisibility(View.VISIBLE);
		error_layout.setVisibility(View.GONE);
	}

	/**
	 * 显示错误页 和空白页
	 */
	public void showErrorView() {
		isShowError = 1;
		image_error.setBackgroundResource(R.drawable.no_network);
		loading_layout.setVisibility(View.GONE);
		gridView.setVisibility(View.GONE);
		error_layout.setVisibility(View.VISIBLE);
	}

	/**
	 * 显示空白页
	 */
	public void showNodataView() {
		isShowError = 2;
		image_error.setBackgroundResource(R.drawable.add_star);
		loading_layout.setVisibility(View.GONE);
		gridView.setVisibility(View.GONE);
		error_layout.setVisibility(View.VISIBLE);
	}

	/**
	 * 重新获取数据
	 */
	public void retryClick() {
		showLoadingView();
		getDataFromServer();
	}

	/**
	 * 空数据触发
	 */
	public void nodataClick() {
		Utily.go2Activity(activity, AllStarActivity.class);
	}

	/**
	 * 从服务器获取数据
	 */
	public void getDataFromServer() {

		String processURL = Constant.RequestContstants.URL
				+ Constant.RequestContstants.Request_myStar_list;

		ConnectionService.getInstance().serviceConnUseGet(activity, processURL,
				null, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if (check) {
								modelList = parseJson.getModelList(jsonObject,
										StarModel.class);
								if (modelList != null && modelList.size() > 0) {
									saveStar(modelList);
									handler.sendEmptyMessage(INIT_DATA);
								} else {
									showNodataView();
								}
							} else {
								showErrorView();
							}
						} catch (Exception e) {
							e.printStackTrace();
							showErrorView();
						}
					}

					@Override
					public void getError() {
						showErrorView();
					}
				});

	}

	/**
	 * 的导向现在的时间
	 * 
	 * @return long类型字符串
	 */
	public String getNowTime() {
		long time = new Date().getTime();
		return String.valueOf(time);
	}

	public void doShowLoadingView() {

		if (loading_layout != null) {
			loading_layout.setVisibility(View.VISIBLE);
		}
	}

	public void doHideLoadingView() {
		if (loading_layout != null) {
			loading_layout.setVisibility(View.GONE);
		}

	}

	@Override
	protected void findViewById() {

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

	@Override
	protected void initData() {
		adapter = new MyStarAdapter(activity, modelList, null, handler,
				isDelete);
		gridView.setAdapter(adapter);

	}

	public void initEditData() {
		List<Object> list = new ArrayList<Object>();
		if (modelList != null && modelList.size() > 0) {
			int length = modelList.size();
			for (int i = 0; i < length; i++) {
				list.add(modelList.get(i));
			}
		}
		StarModel starModel = new StarModel();
		starModel.starId = -100;
		list.add(starModel);
		adapter = new MyStarAdapter(activity, list, null, handler, isDelete);
		gridView.setAdapter(adapter);
	}

	@Override
	public boolean isEdit(boolean isEdit) {
		boolean result = false;
		if (View.VISIBLE == gridView.getVisibility()) {
			result = true;
			isDelete = isEdit;
			if (isEdit) {
				initEditData();
			} else {
				initData();
			}
		}
		return result;

	}

}
