package com.mci.firstidol.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mci.firstidol.R;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.MyListView;

public abstract class BaseNormalListFragment extends BaseFragment {

	protected final int INIT_DATA = 1;
	protected final int HIDEPROCESS = 2;
	public List<Object> modelList;// 数据模型列表

	public ListView refreshListView;
	// public ListView listView;// 数据视图

	public BaseAdapter adapter;// 数据适配器

	// public LinearLayout header_layout;// listview的顶部
	// public LinearLayout footer_layout;// listview的底部

	public LinearLayout loading_layout;// 圈圈页
	public LinearLayout error_layout;// 错误页

	public ImageView image_error;// 错误页icon
	public TextView text_error;// 错误页info

	private String processURL;// 数据请求地址
	private String channelId;// channel ID

	private HashMap<String, String> requestParams;// 数据请求参数

	public int index = 0;// 当前第几页

	public int pageSize = 10;// 每一页的数据

	private boolean isFirstLoading = true;// 是否是第一次请求

	private Class<?> targetModel;// 目标model类

	public boolean IsDataFromServer = true;// 数据是否是从网络来的

	public boolean IsloadingData = false;// 是否只用于加载数据

	public int isShowError = 0;// 是否显示错误页 1：错误页 2：空数据

	public boolean onlyParams = false;// 是不是网络请求只有参数

	@Override
	protected int getViewId() {
		return R.layout.base_normal_layout;
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
		if (IsDataFromServer) {
			showLoadingView();
			getDataFromServer();
		} else {
			// 初始化本地数据
			showLoadingView();
			initDataFromLocal();
		}
	}

	/**
	 * 得到数据页面
	 */
	public void initViewNormal() {

		// 得到配置数据 抽象方法
		setProfileData();

		// 得到listview视图
		refreshListView = (ListView) findViewById(R.id.pull_refresh_list);

		initListView(refreshListView);

		// 得到圈圈页 错误页 内容页
		loading_layout = (LinearLayout) findViewById(R.id.loading_layout);
		// list_layout = (LinearLayout) findViewById(R.id.layout_list);
		error_layout = (LinearLayout) findViewById(R.id.error_layout);

		image_error = (ImageView) findViewById(R.id.image_errorview);
		text_error = (TextView) findViewById(R.id.textview_error);

		refreshListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						dataItemClick(position);
					}
				});

		refreshListView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						dataLongItemClick(position);
						return true;
					}
				});

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

		// 添加更新，加载
		childinitView();

	}

	/**
	 * 刷新
	 */
	public void pullToReflush() {

		// 设置初始化
		if (IsDataFromServer) {
			index = 0;
			getDataFromServer();
		} else {
			initDataFromLocal();
			handler.sendEmptyMessage(HIDEPROCESS);
		}
		reflushThis();
	}

	/**
	 * 加载更多
	 * 
	 * @param refreshView
	 */
	public void downMore() {
		index++;
		getDataFromServer();
	}

	protected void childinitView() {

	}

	/**
	 * 刷新操作
	 */
	public void reflushThis() {
	}

	protected void initListView(ListView listView) {

	}

	/**
	 * 初始化本地数据列表
	 */
	public void initDataFromLocal() {
		List<Object> list = getDataFromLocal();
		if (list != null && list.size() > 0) {
			addData(list);
			handler.sendEmptyMessage(INIT_DATA);
		} else {
			showNodataView();
		}
	}

	/**
	 * 从本地获取数据
	 * 
	 * @return
	 */
	public List<Object> getDataFromLocal() {
		return null;
	};


	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case INIT_DATA:
				initData();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						showContentView();
					}
				}, 1500);
				
				break;
			}
		}
	};

	/**
	 * 显示圈圈页
	 */
	public void showLoadingView() {
		loading_layout.setVisibility(View.VISIBLE);
		refreshListView.setVisibility(View.GONE);
		error_layout.setVisibility(View.GONE);
	}

	/**
	 * 显示数据页
	 */
	public void showContentView() {
		loading_layout.setVisibility(View.GONE);
		refreshListView.setVisibility(View.VISIBLE);
		error_layout.setVisibility(View.GONE);
	}

	/**
	 * 显示错误页 和空白页
	 */
	public void showErrorView() {
		isShowError = 1;
		image_error.setBackgroundResource(R.drawable.no_network);
		loading_layout.setVisibility(View.GONE);
		refreshListView.setVisibility(View.GONE);
		error_layout.setVisibility(View.VISIBLE);
	}

	/**
	 * 显示空白页
	 */
	public void showNodataView() {
		isShowError = 2;
		image_error.setBackgroundResource(R.drawable.nodata);
		loading_layout.setVisibility(View.GONE);
		refreshListView.setVisibility(View.GONE);
		error_layout.setVisibility(View.VISIBLE);
	}

	/**
	 * 重新获取数据
	 */
	public void retryClick() {
		isFirstLoading = true;
		index = 0;
		showLoadingView();
		getDataFromServer();
	}

	/**
	 * 空数据触发
	 */
	public void nodataClick() {
		isFirstLoading = true;
		index = 0;
		showLoadingView();
		getDataFromServer();
	}

	/**
	 * 从服务器获取数据
	 */
	public void getDataFromServer() {
		// showDialog();
		List<String> params = setParams();
		StringBuffer paramString = new StringBuffer();
		if (params != null && params.size() > 0) {
			int length = params.size();
			for (int i = 0; i < length; i++) {
				paramString.append("/" + params.get(i));
			}
		}

		String url = null;

		if (onlyParams) {
			url = Constant.RequestContstants.URL + processURL + paramString;
		} else {
			url = Constant.RequestContstants.URL + processURL + channelId + "/"
					+ String.valueOf(1) + "/" + String.valueOf(pageSize) + paramString.toString().trim();
		}

		Log.e("easy", url);

		ConnectionService.getInstance().serviceConnUseGet(activity, url, null,
				new StringCallBack() {
					@Override
					public void getSuccessString(String Result) {
						// Log.e("pnl", Result);
						doHideLoadingView();
						hideDialog();
						try {
							if (index == 0 && modelList != null) {
								modelList.clear();
							}
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if (check) {
								List<Object> dataList = parseJson.getModelList(
										jsonObject, targetModel);
								if (dataList != null && dataList.size() > 0) {
									addData(dataList);
									handler.sendEmptyMessage(INIT_DATA);
								} else if (childNotAllowed()) {
									addData(dataList);
									handler.sendEmptyMessage(INIT_DATA);
								} else {
									if (index == 0) {
										showNodataView();
									}
								}
								isFirstLoading = false;
							} else {
								if (index == 0) {
									showErrorView();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							if (index == 0) {
								showErrorView();
							}
						} finally {
							hideDialog();
						}
						handler.sendEmptyMessage(HIDEPROCESS);
					}

					@Override
					public void getError() {
						doHideLoadingView();
						handler.sendEmptyMessage(HIDEPROCESS);
						if (index == 0) {
							showErrorView();
						}
					}
				});
	}

	protected boolean childNotAllowed() {

		return false;
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

	/**
	 * 添加数据
	 * 
	 * @param dataList
	 */
	public void addData(List<Object> dataList) {

		if (modelList == null) {
			modelList = new ArrayList<Object>();
		}
		if (index == 0) {
			modelList.clear();
		}

		int length = dataList.size();
		for (int i = 0; i < length; i++) {
			modelList.add(dataList.get(i));
		}
	}

	/**
	 * 初始化列表数据
	 */
	public void initData() {
		if (index == 0) {
			adapter = setAdapter();
			refreshListView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		
		new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				setListViewHeight(refreshListView);
			}
		});
		
	}

	public void setListViewHeight(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		int item_devide = listView.getDividerHeight();
		int length = listAdapter.getCount();
		for (int i = 0; i < length; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight+(length-1)*item_devide;
		listView.setLayoutParams(params);
	}

	/**
	 * 得到headerView
	 * 
	 * @param resId
	 * @return
	 */
	public void setHeaderView(int resId) {
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View header = inflater.inflate(resId, null);
		if (refreshListView != null) {
			refreshListView.addHeaderView(header);
		}
	}

	/**
	 * 设置属性数据
	 */
	public void setProfileData() {
		processURL = setProcessURL();
		targetModel = setModelClass();
		channelId = setChannelId();
	}

	/**
	 * 设置数据列表的触发
	 */
	public abstract void dataItemClick(int position);

	/**
	 * 长按触发事件
	 * 
	 * @param position
	 */
	public abstract void dataLongItemClick(final int position);

	/**
	 * 设置数据适配器
	 */
	public abstract BaseAdapter setAdapter();

	/**
	 * 设置url地址
	 * 
	 * @return
	 */
	public abstract String setProcessURL();

	/**
	 * 设置请求参数
	 * 
	 * @return
	 */
	public abstract List<String> setParams();

	/**
	 * 设置目标model
	 * 
	 * @return
	 */
	public abstract Class<?> setModelClass();

	/**
	 * 设置channelId
	 * 
	 * @return
	 */
	public abstract String setChannelId();

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

}
