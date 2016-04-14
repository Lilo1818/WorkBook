package com.mci.firstidol.fragment;

import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.shinsoft.query.Delete;
import cn.shinsoft.query.Select;

import com.mci.firstidol.R;
import com.mci.firstidol.activity.AllStarActivity;
import com.mci.firstidol.adapter.MyStarAdapter;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.StarModel;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.MyGridView;
import com.mci.firstidol.view.ToastUtils;

public class ChoiceStarFragment extends BaseFragment {

	private final int INIT_HOT_DATA = 1;
	private final int INIT_ALL_DATA = 2;
	private final int STAR_DELETE = 10;// 删除明星
	public List<Object> hotList;// 热门明星 列表
	public List<Object> allList;// 所有明星列表

	private MyGridView hot_gridView;// 热门推荐视图
	private MyGridView all_gridView;// 所有明星视图

	public BaseAdapter hotAdapter;// 热门明星适配器
	public BaseAdapter allAdapter;// 所有的明星适配器

	public ScrollView content_layout;// 内容页
	public LinearLayout loading_layout;// 圈圈页
	public LinearLayout error_layout;// 错误页

	public ImageView image_error;// 错误页icon
	public TextView text_error;// 错误页info

	public int isShowError = 0;// 是否显示错误页 1：错误页 2：空数据

	public boolean isDelete = false;// 是否处于删除状态

	public int net_connected = 0;// 链接了几次

	public String type;// 1：中国，2：韩国，3：日本，4其他

	public int item_click_type = 0; // 1：热门 2：全部

	public List<StarModel> selectedList;// 已保存的明星

	@Override
	protected int getViewId() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			type = bundle.getString(Constant.IntentKey.county_type);
		}
		return R.layout.fragment_allstar;
	}

	@Override
	protected void initView() {
		selectedList = new Select().from(StarModel.class).execute();
		initViewNormal();
		getDataFromServer();
	}

	/**
	 * 得到数据页面
	 */
	public void initViewNormal() {

		// 得到listview视图
		hot_gridView = (MyGridView) findViewById(R.id.hot_grid);
		all_gridView = (MyGridView) findViewById(R.id.all_grid);

		// 得到圈圈页 错误页 内容页
		content_layout = (ScrollView) findViewById(R.id.layout_content);
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
		hot_gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				item_click_type = 1;
				Constant.is_selectedChanged = true;
				if (((StarModel) hotList.get(position)).has_selected) {
					cancleFollowStar(position);
				} else {
					followStar(position);
				}
			}
		});

		all_gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				item_click_type = 2;
				Constant.is_selectedChanged = true;
				if (((StarModel) allList.get(position)).has_selected) {
					cancleFollowStar(position);
				} else {
					followStar(position);
				}
			}
		});

	}

	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case INIT_HOT_DATA:
				initHotData();
				break;
			case INIT_ALL_DATA:
				initAllData();
				break;
			case STAR_DELETE:

				break;
			}
		}
	};

	/**
	 * 关注明星
	 * 
	 * @param position
	 * @param type
	 */
	public void followStar(final int position) {
		StarModel starModel = null;
		if (item_click_type == 1) {
			starModel = (StarModel) hotList.get(position);
		} else {
			starModel = (StarModel) allList.get(position);
		}
		pd.setMessage("正在关注");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		String processURL = Constant.RequestContstants.Request_follow_star
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
								saveStar(position);
							} else {
								String errorMsg = parseJson
										.getErrorMessage(jsonObject);
								ToastUtils.show(activity, errorMsg);
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.show(activity, "关注失败");
						}

					}

					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						ToastUtils.show(activity, "关注失败");
					}

				});
	}

	/**
	 * 取消关注
	 * 
	 * @param position
	 * @param type
	 */
	public void cancleFollowStar(final int position) {
		StarModel starModel = null;
		if (item_click_type == 1) {
			starModel = (StarModel) hotList.get(position);
		} else {
			starModel = (StarModel) allList.get(position);
		}
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
								deleteStar(position);
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
	 * 保存明星
	 * 
	 * @param position
	 */
	public void saveStar(int position) {
		StarModel starModel = null;
		if (item_click_type == 1) {
			starModel = (StarModel) hotList.get(position);
		} else {
			starModel = (StarModel) allList.get(position);
		}
		// 删除本地缓存
		try {
			starModel.save();
			if (item_click_type == 1) {
				((StarModel) hotList.get(position)).has_selected = true;
				hotAdapter.notifyDataSetChanged();
				selectedOrDeleteAll(position, true);
				if (allAdapter != null) {
					allAdapter.notifyDataSetChanged();
				}
			} else {
				((StarModel) allList.get(position)).has_selected = true;
				allAdapter.notifyDataSetChanged();
				selectedOrDeleteHot(position, true);
				if (hotAdapter != null) {
					hotAdapter.notifyDataSetChanged();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtils.show(activity, "删除失败");
		}
	}

	/**
	 * 删除明星操作
	 */
	public void deleteStar(int position) {
		int starId = 0;
		if (item_click_type == 1) {
			starId = ((StarModel) hotList.get(position)).starId;
		} else {
			starId = ((StarModel) allList.get(position)).starId;
		}
		// 删除本地缓存
		try {
			String whereStr = String.format("starId=%s", starId);
			new Delete().from(StarModel.class).where(whereStr).executeSingle();
			if (item_click_type == 1) {
				((StarModel) hotList.get(position)).has_selected = false;
				selectedOrDeleteAll(position, false);
				hotAdapter.notifyDataSetChanged();
				if (allAdapter != null) {
					allAdapter.notifyDataSetChanged();
				}
			} else {
				((StarModel) allList.get(position)).has_selected = false;
				allAdapter.notifyDataSetChanged();
				selectedOrDeleteHot(position, false);
				if (hotAdapter != null) {
					hotAdapter.notifyDataSetChanged();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtils.show(activity, "删除失败");
		}

	}

	/**
	 * 选中和删除某个明星 在所有明星
	 * 
	 * @param position
	 * @param selected
	 */
	public void selectedOrDeleteAll(int position, boolean selected) {
		StarModel starModel = (StarModel) hotList.get(position);
		if(allList!=null&&allList.size()>0){
			int length = allList.size();
			int starId = starModel.starId;
			for (int i = 0; i < length; i++) {
				StarModel model = (StarModel) allList.get(i);
				if(model.starId == starId){
					if(selected){
						if(!model.has_selected){
							((StarModel) allList.get(i)).has_selected = selected;
						}
					}else{
						if(model.has_selected){
							((StarModel) allList.get(i)).has_selected = selected;
						}
					}
					break;
				}
			}
		}
	}

	/**
	 * 选中和删除某个明星 在热门明星
	 * 
	 * @param position
	 * @param selected
	 */
	public void selectedOrDeleteHot(int position, boolean selected) {
		StarModel starModel = (StarModel) allList.get(position);
		if(hotList!=null&&hotList.size()>0){
			int length = hotList.size();
			int starId = starModel.starId;
			for (int i = 0; i < length; i++) {
				StarModel model = (StarModel) hotList.get(i);
				if(model.starId == starId){
					if(selected){
						if(!model.has_selected){
							((StarModel) hotList.get(i)).has_selected = selected;
						}
					}else{
						if(model.has_selected){
							((StarModel) hotList.get(i)).has_selected = selected;
						}
					}
					break;
				}
			}
		}
	}

	/**
	 * 从网络获取数据
	 */
	public void getDataFromServer() {
		getHotFromServer();
		getAllFromServer();
	}

	/**
	 * 得到热门明星
	 */
	public void getHotFromServer() {
		String count = "8";
		if ("3".equals(type) || "4".equals(type)) {
			count = "4";
		}
		String processURL = Constant.RequestContstants.URL
				+ Constant.RequestContstants.Request_hotStar_list + "/" + type
				+ "/" + count;

		ConnectionService.getInstance().serviceConnUseGet(activity, processURL,
				null, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if (check) {
								hotList = parseJson.getModelList(jsonObject,
										StarModel.class);
								if (hotList != null && hotList.size() > 0) {
									handler.sendEmptyMessage(INIT_HOT_DATA);
								}
								net_connected++;
								isShowContent();
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
	 * 得到所有的明星
	 */
	public void getAllFromServer() {
		String processURL = Constant.RequestContstants.URL
				+ Constant.RequestContstants.Request_allStar_list + "/" + type;

		ConnectionService.getInstance().serviceConnUseGet(activity, processURL,
				null, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if (check) {
								allList = parseJson.getModelList(jsonObject,
										StarModel.class);
								if (allList != null && allList.size() > 0) {
									handler.sendEmptyMessage(INIT_ALL_DATA);
								}
								net_connected++;
								isShowContent();
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
	 * 初始化热门明星
	 */
	public void initHotData() {
		if (selectedList != null && selectedList.size() > 0) {
			int length = hotList.size();
			int size = selectedList.size();
			for (int i = 0; i < length; i++) {
				StarModel starModel = (StarModel) hotList.get(i);
				int star_id = starModel.starId;
				for (int j = 0; j < size; j++) {
					if (((StarModel) selectedList.get(j)).starId == star_id) {
						((StarModel) hotList.get(i)).has_selected = true;
					}
				}

			}
		}

		hotAdapter = new MyStarAdapter(activity, hotList, null);
		hot_gridView.setAdapter(hotAdapter);
	}

	/**
	 * 初始化所有的明星
	 */
	public void initAllData() {
		if (selectedList != null && selectedList.size() > 0) {
			int length = allList.size();
			int size = selectedList.size();
			for (int i = 0; i < length; i++) {
				StarModel starModel = (StarModel) allList.get(i);
				int star_id = starModel.starId;
				for (int j = 0; j < size; j++) {
					if (((StarModel) selectedList.get(j)).starId == star_id) {
						((StarModel) allList.get(i)).has_selected = true;
					}
				}

			}
		}
		allAdapter = new MyStarAdapter(activity, allList, null);
		all_gridView.setAdapter(allAdapter);
	}

	/**
	 * 是否显示正文
	 */
	public void isShowContent() {
		if (net_connected == 2) {
			showContentView();
		}
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
		showLoadingView();
		getDataFromServer();
	}

	/**
	 * 显示圈圈页
	 */
	public void showLoadingView() {
		loading_layout.setVisibility(View.VISIBLE);
		content_layout.setVisibility(View.GONE);
		error_layout.setVisibility(View.GONE);
	}

	/**
	 * 显示数据页
	 */
	public void showContentView() {
		loading_layout.setVisibility(View.GONE);
		content_layout.setVisibility(View.VISIBLE);
		error_layout.setVisibility(View.GONE);
	}

	/**
	 * 显示错误页 和空白页
	 */
	public void showErrorView() {
		isShowError = 1;
		image_error.setBackgroundResource(R.drawable.no_network);
		loading_layout.setVisibility(View.GONE);
		content_layout.setVisibility(View.GONE);
		error_layout.setVisibility(View.VISIBLE);
	}

	@Override
	protected void findViewById() {

	}

	@Override
	protected void initData() {

	}

}
