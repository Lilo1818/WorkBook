package com.mci.firstidol.fragment.personal;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.BaseAdapter;

import com.mci.firstidol.adapter.FansAdapter;
import com.mci.firstidol.base.BaseListFragment;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.model.FansModel;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.view.ToastUtils;

import de.greenrobot.event.EventBus;

public class FansFragment extends BaseListFragment {

	private final int CANCLE_FOLLOW = 1;//取消关注
	private String userId;//用户id
	
	@Override
	public void dataItemClick(int position) {

	}

	@Override
	public void dataLongItemClick(int position) {

	}

	@Override
	public BaseAdapter setAdapter() {
		BaseAdapter adapter = new FansAdapter(activity, modelList, listView,
				handler);
		return adapter;
	}

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CANCLE_FOLLOW:
				int position = (int) msg.obj;
				FansModel fansModel = (FansModel) modelList.get(position);
				String userId = fansModel.UserId;
				if(fansModel.IsFollow){
					cancleOrFollow(position, userId, false);
				}else{
					cancleOrFollow(position, userId, true);
				}
				break;

			default:
				break;
			}
		}
	};

	@Override
	public String setProcessURL() {
		return Constant.RequestContstants.Request_fans_list;
	}

	@Override
	public List<String> setParams() {
		
		Bundle bundle = getArguments();
		if(bundle!=null){
			userId = bundle.getString(Constant.IntentKey.userID);
		}
		
		onlyParams = true;
		List<String> params = new ArrayList<String>();
		if(userId!=null){
			params.add(userId);
		}else if (DataManager.userModel != null) {
			params.add(String.valueOf(DataManager.userModel.UserId));
		}
		if (index == 0) {
			params.add("0");
		} else {
			String id = ((FansModel) modelList.get(modelList.size() - 1)).Id;
			params.add(id);
		}
		params.add("20");
		return params;
	}

	/**
	 * 关注或者取消关注
	 * 
	 * @param position
	 * @param userId
	 * @param isFollow
	 */
	public void cancleOrFollow(final int position, String userId,
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

		ConnectionService.getInstance().serviceConnUseGet(activity, processURL,
				null, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if(check){
								if(isFollow){
									((FansModel)modelList.get(position)).IsFollow = true;
								}else{
									((FansModel)modelList.get(position)).IsFollow = false;
								}
								sendFollowNumber(isFollow);
								initData();
							}else{
								ToastUtils.showCustomToast(activity, "请求失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCustomToast(activity, "请求失败");
						}

					}

					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						ToastUtils.showCustomToast(activity, "请求失败");
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

	@Override
	public Class<?> setModelClass() {
		return FansModel.class;
	}

	@Override
	public String setChannelId() {
		return null;
	}

	@Override
	protected void findViewById() {

	}

}
