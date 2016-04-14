package com.mci.firstidol.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Toast;
import cn.shinsoft.Model;
import cn.shinsoft.query.Select;

import com.mci.firstidol.activity.MyInfoActivity;
import com.mci.firstidol.activity.PhotoSelectedActivity;
import com.mci.firstidol.activity.SquareFoundDetailActivity;
import com.mci.firstidol.activity.SquareWelfareDetailActivity;
import com.mci.firstidol.activity.StarAddressActivity;
import com.mci.firstidol.activity.VideoPlayActivity;
import com.mci.firstidol.adapter.MyNoticeAdapter;
import com.mci.firstidol.base.BaseListFragment;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.CollectionModel;
import com.mci.firstidol.model.NoticeModel;
import com.mci.firstidol.model.SquareLiveModel;
import com.mci.firstidol.model.StarModel;
import com.mci.firstidol.model.StarTripModel;
import com.mci.firstidol.opensource.afinal.FinalBitmap;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.AlertDialog;
import com.mci.firstidol.view.ToastUtils;

import de.greenrobot.event.EventBus;

public class NoticeListFragment extends BaseListFragment {
	
	
	
	private static int id;
	
	
	
	
	@Override
	public void dataItemClick(int position) {

		NoticeModel noticeModel = (NoticeModel) modelList.get(position);
		ToastUtils.showCustomToast(activity, "系统通知");
		/*switch (noticeModel.Type) {
		case 0:
			ToastUtils.showCustomToast(activity, "此条为系统通知");
			break;
		case 1:
			switch (noticeModel.ChannelId) {
			case 43:

				break;
			case 44:
				gotoShopping(noticeModel);
				break;
			case 49:
				switch (noticeModel.ModelType) {
				case 0:
					Bundle bundle = new Bundle();
					bundle.putLong(Constant.IntentKey.articleID, noticeModel.RefId);
					Utily.go2Activity(getActivity(), SquareFoundDetailActivity.class,
							bundle);
					break;
				case 1:
					HashMap<String, String> photoParams = new HashMap<String,String>();
					photoParams.put(Constant.IntentKey.articleID,String.valueOf(noticeModel.RefId));
					Utily.go2Activity(activity, PhotoSelectedActivity.class, photoParams, null);
					break;
				case 2:
					HashMap<String, String> param = new HashMap<String,String>();
					param.put(Constant.IntentKey.articleID, String.valueOf(noticeModel.RefId));
					Utily.go2Activity(activity, VideoPlayActivity.class, param, null);
					break;

				default:
					break;
				}
			
				break;

			default:
				break;
			}
		
			break;
		case 4:
			HashMap<String, String> tripParam = new HashMap<String,String>();
			tripParam.put(Constant.IntentKey.star_id, noticeModel.StarId);
			Utily.go2Activity(activity, StarAddressActivity.class, tripParam, null);
			break;
		case 10:
			gotoStar(noticeModel);
			break;
		case 11:
			HashMap<String, String> params = new HashMap<String,String>();
			params.put(Constant.IntentKey.userID, noticeModel.UserId);
			Utily.go2Activity(activity, MyInfoActivity.class, params, null);
			break;
		default:
			break;
		}*/

	}
	
	/**
	 * 跳转商品
	 * @param noticeModel
	 */
	public void gotoShopping(NoticeModel noticeModel){
		
		SquareLiveModel squareLiveModel = new SquareLiveModel();
		//squareLiveModel.setContent(noticeModel.);
		
		Bundle mbuBundle = new Bundle();
		mbuBundle.putSerializable(Constant.IntentKey.squareLiveModel,squareLiveModel);
		mbuBundle.putString(Constant.IntentKey.typeCode,
				Constant.IntentValue.ACTIVITY_SHOPPING);
		Utily.go2Activity(activity, SquareWelfareDetailActivity.class,
				mbuBundle);
	}
	
	/**
	 * 跳转明星
	 * @param noticeModel
	 */
	public void gotoStar(NoticeModel noticeModel){
		String starId = String.valueOf(noticeModel.RefId);
		List<StarModel> list = new Select().from(StarModel.class).execute();
		boolean check = false;
		int position  = -1;
		
		if(list!=null&&list.size()>0){
			int length = list.size();  
			for (int i = 0; i < length; i++) {
				if(starId!=null&&starId.equals(String.valueOf(list.get(i).starId))){
					  check = true;
					  position = i;
				}
			}
		}
		
		if(check&&position>0){
			Constant.starModel = list.get(position);
			changeStar();
			Constant.is_selected_star = true;
			exitThis();
		}else{
			ToastUtils.showCustomToast(activity, "没有此明星");
		}
		
	}
	
	/**
	 *切换明星
	 */
	public void changeStar(){
		AnyEventType type = new AnyEventType(Constant.Config.SELECTED_STAR, AnyEventType.Type_Default);
        EventBus.getDefault().post(type);
	}

	@Override
	public void dataLongItemClick(int position) {
		id = position;
		NoticeModel noticeModel = (NoticeModel) modelList.get(position);
		showrEport(noticeModel);
	}
	
	private void showrEport(final NoticeModel noticeModel){
		AlertDialog.show(activity, "", "确定要删除这条内容?", "确定", new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "你点击了删除", Toast.LENGTH_SHORT).show();
				
				/*modelList.remove(id);
				adapter.notifyDataSetChanged();  
				listView.invalidate();*/
				
				getRegist(noticeModel);
				
				
				
				
			}
		}, "取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "你点击了取消", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	
	//请求删除的方法
	private void  getRegist(NoticeModel noticeModel){
		
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		String processURL = Constant.RequestContstants.Request_delete;
		String paramStr = "{\"id\":\"" + String.valueOf(noticeModel.Id) + "\"}";

		ConnectionService.getInstance().serviceConn(activity, processURL,
				paramStr, new StringCallBack() {
					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean check = parseJson.isCommon(jsonObject);
							if (check) {
								modelList.remove(id);
								adapter.notifyDataSetChanged();  
								listView.invalidate();
							} else {
								ToastUtils.showCustomToast(activity, "删除失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCustomToast(activity, "删除失败（异常）");
						}

					}

					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						ToastUtils.showCustomToast(activity, "请求失败失败");
					}
				});

		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	@Override
	public BaseAdapter setAdapter() {
		BaseAdapter adapter = new MyNoticeAdapter(activity, modelList, listView);
		return adapter;
	}

	@Override
	public String setProcessURL() {
		return Constant.RequestContstants.Request_myNotice_list;
	}

	@Override
	public List<String> setParams() {
		onlyParams = true;
		List<String> params = new ArrayList<String>();
		String timeStr = "";
		if (index == 0) {
			timeStr = "00";
		} else {
			if (modelList != null && modelList.size() > 0) {
				int length = modelList.size();
				NoticeModel noticeModel = (NoticeModel) modelList
						.get(length - 1);
				timeStr = noticeModel.PublishDate;
			}
		}
		params.add(timeStr);
		return params;
	}

	@Override
	public Class<?> setModelClass() {
		return NoticeModel.class;
	}

	@Override
	public String setChannelId() {
		return null;
	}

	@Override
	protected void findViewById() {

	}

}
