package com.mci.firstidol.fragment.star;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.widget.BaseAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.mci.firstidol.adapter.StarTripAdapter;
import com.mci.firstidol.base.BaseListFragment;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.StarTripModel;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.view.ToastUtils;

public class StarAddressFragment extends BaseListFragment {

	private String timeStr;// 时间字符串
	private String star_id;//明星ID

	@Override
	public void dataItemClick(int position) {
		StarTripModel starTripModel = (StarTripModel) modelList.get(position);
		if (!starTripModel.HasRemind) {
			noticeTripRequest(position, starTripModel.TripId);
		}else{
			deleteTripRequest(position,starTripModel.TripId);
		}
	}

	@Override
	public void dataLongItemClick(int position) {

	}

	@Override
	public BaseAdapter setAdapter() {
		BaseAdapter adapter = new StarTripAdapter(activity, modelList, listView);
		return adapter;
	}

	@Override
	public String setProcessURL() {
		onlyParams = true;
		return Constant.RequestContstants.Request_triping_list;
	}

	@Override
	protected void childinitView() {
		refreshListView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
	}

	@Override
	public List<String> setParams() {
		List<String> params = new ArrayList<String>();
		if (Constant.starModel != null) {
			Bundle bundle = getArguments();
			if (bundle != null) {
				timeStr = bundle.getString(Constant.IntentKey.time_str);
			}

			params.add(String.valueOf(Constant.starModel.starId));
			params.add(timeStr);
		}else{
			Bundle bundle = getArguments();
			if (bundle != null) {
				timeStr = bundle.getString(Constant.IntentKey.time_str);
				star_id = bundle.getString(Constant.IntentKey.star_id);
			}
			params.add(star_id);
			params.add(timeStr);
		}

		return params;
	}
	
	/**
	 * 删除提醒
	 * @param position
	 * @param id
	 */
	public void deleteTripRequest(final int position, String id){
		pd.setMessage("删除提醒中");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		String processURL = Constant.RequestContstants.Request_trip_notice_delete;
		String paramStr = "{\"tripId\":\"" + id + "\"}";

		ConnectionService.getInstance().serviceConn(activity, processURL,
				paramStr, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if (check) {
								ToastUtils.showCustomToast(activity, "删除提醒成功");
								((StarTripModel) modelList.get(position)).HasRemind = false;
								adapter.notifyDataSetChanged();
							} else {
								ToastUtils.showCustomToast(activity, "删除提醒失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCustomToast(activity, "删除提醒失败");
						}

					}

					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						ToastUtils.showCustomToast(activity, "删除提醒失败");
					}
				});
	}

	/**
	 * 添加提醒
	 * @param position
	 * @param id
	 */
	public void noticeTripRequest(final int position, String id) {
		pd.setMessage("添加提醒中");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		String processURL = Constant.RequestContstants.Request_trip_notice;
		String paramStr = "{\"tripId\":\"" + id + "\"}";

		ConnectionService.getInstance().serviceConn(activity, processURL,
				paramStr, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if (check) {
								ToastUtils.showCustomToast(activity, "添加提醒成功");
								((StarTripModel) modelList.get(position)).HasRemind = true;
								adapter.notifyDataSetChanged();
							} else {
								ToastUtils.showCustomToast(activity, "添加提醒失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCustomToast(activity, "添加提醒失败");
						}

					}

					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						ToastUtils.showCustomToast(activity, "添加提醒失败");
					}
				});
	}

	@Override
	public Class<?> setModelClass() {
		return StarTripModel.class;
	}

	@Override
	public String setChannelId() {
		return null;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEventMainThread(AnyEventType event) {
		if (event != null && Constant.Config.UPDATE_STAR.equals(event.message)) {
			retryClick();
		}
	};

}
