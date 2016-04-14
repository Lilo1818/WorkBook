package com.mci.firstidol.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.mci.firstidol.activity.SquareFoundDetailActivity;
import com.mci.firstidol.activity.SquareWelfareDetailActivity;
import com.mci.firstidol.adapter.MyCollectionAdapter;
import com.mci.firstidol.base.BaseListFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.CollectionModel;
import com.mci.firstidol.model.NoticeModel;
import com.mci.firstidol.utils.AsyncTaskUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.AlertDialog;
import com.mci.firstidol.view.ToastUtils;

public class CollectionListFragment extends BaseListFragment{
	private static int id;
	@Override
	public void dataItemClick(int position) {
		/*Bundle bundle = new Bundle();
		bundle.putLong(Constant.IntentKey.articleID, ((CollectionModel)modelList.get(position)).ArticleId);
		Utily.go2Activity(getActivity(), SquareFoundDetailActivity.class,
				bundle);*/
		CollectionModel collectionModel = (CollectionModel) modelList.get(position);
		//ToastUtils.showCustomToast(activity, "值为"+collectionModel.Type);
		switch (collectionModel.Type) {
		case 0:
			/*Bundle bundle = new Bundle();
			bundle.putLong(Constant.IntentKey.articleID, ((CollectionModel)modelList.get(position)).ArticleId);
			Utily.go2Activity(getActivity(), SquareFoundDetailActivity.class,
					bundle);*/
			//ToastUtils.showCustomToast(activity, "此条为系统通知");
			switch (collectionModel.ChannelId) {
			case 44:
				ToastUtils.showCustomToast(activity, "此条为商城");
				break;
			default:
				Bundle bundle = new Bundle();
				bundle.putLong(Constant.IntentKey.articleID, ((CollectionModel)modelList.get(position)).ArticleId);
				Utily.go2Activity(getActivity(), SquareFoundDetailActivity.class,
						bundle);
				break;
			}
			break;
		default:
			ToastUtils.showCustomToast(activity, "此条为福利");
			break;
		}
	}

	@Override
	public void dataLongItemClick(int position) {
		// TODO Auto-generated method stub
		//Toast.makeText(activity, "是否删除", Toast.LENGTH_SHORT).show();
		id = position;
		showrEport();
	}
	private void showrEport(){
		AlertDialog.show(activity, "", "确定要取消收藏这条内容?", "确定", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "你点击了确定", Toast.LENGTH_SHORT).show();
				obtainFoundDetailCommentFavRequest();
			}
		}, "取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "你点击了取消", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	/**
	 * 广场-发现详情-收藏
	 * 
	 * @param isCancelFav
	 */
	public void obtainFoundDetailCommentFavRequest() {
		BaseRequest request = new BaseRequest();
		// 请求地址
		String requestID = String
				.format(Constant.RequestContstants.Request_Article_CancelStore
						,((CollectionModel)modelList.get(id)).ArticleId);
		doAsync(false, requestID, request,
				new AsyncTaskUtils.Callback<Exception>() {

					@Override
					public void onCallback(Exception e) {
						// TODO Auto-generated method stub
						//ToastUtils.showCustomToast(e.getLocalizedMessage());
						ToastUtils.showCustomToast(e.getLocalizedMessage());
					}
				}, new AsyncTaskUtils.Callback<String>() {

					@Override
					public void onCallback(String result) {
						/*ToastUtils.showCustomToast("点赞成功");
						starVideoModel.HasStore = true;*/
						modelList.remove(id);
						adapter.notifyDataSetChanged();  
						listView.invalidate();
					}
				});
	}
	
	
	
	
	
	@Override
	public BaseAdapter setAdapter() {
		BaseAdapter adapter = new MyCollectionAdapter(activity, modelList, listView);
		return adapter;
	}

	@Override
	public String setProcessURL() {
		return Constant.RequestContstants.Request_myCoolection_list;
	}

	@Override
	public List<String> setParams() {
		onlyParams = true;
		List<String> params = new ArrayList<String>();
		String timeStr = "";
		if(index==0){
			timeStr = "00";
		}else{
			if(modelList!=null&&modelList.size()>0){
				int length = modelList.size();
				CollectionModel collectionModel = (CollectionModel) modelList.get(length-1);
				timeStr = collectionModel.PublishDate;
			}
		}
		params.add(timeStr);
		return params;
	}

	@Override
	public Class<?> setModelClass() {
		return CollectionModel.class;
	}

	@Override
	public String setChannelId() {
		return null;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

}
