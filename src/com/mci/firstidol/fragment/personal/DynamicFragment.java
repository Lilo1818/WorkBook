package com.mci.firstidol.fragment.personal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.mci.firstidol.activity.SquareFoundDetailActivity;
import com.mci.firstidol.adapter.DynamicAdapter;
import com.mci.firstidol.base.BaseListFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.model.CollectionModel;
import com.mci.firstidol.model.DynamicModel;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.SquareRequest.SquareFoundDetaiSetArticleAttrRequest;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.AlertDialog;
import com.mci.firstidol.view.ToastUtils;

public class DynamicFragment extends BaseListFragment{
	
	private String userId;//用户id
	private static int id;
	@Override
	public void dataItemClick(int position) {
		Bundle bundle = new Bundle();
		bundle.putLong(Constant.IntentKey.articleID, ((DynamicModel)modelList.get(position)).ArticleId);
		Utily.go2Activity(getActivity(), SquareFoundDetailActivity.class,
				bundle);
	}

	@Override
	public void dataLongItemClick(int position) {
		id = position;
		showrEport();
	}
	
	private void showrEport(){
		AlertDialog.show(activity, "", "确定要删除这条内容?", "确定", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "你点击了删除", Toast.LENGTH_SHORT).show();
				//加上判断是否删除的接口
				obtainFoundDetailSetArticleAttrRequest();
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
	 * 广场-发现详情-置顶/删除
	 * @param isCancelFav
	 */
	public void obtainFoundDetailSetArticleAttrRequest(){
		SquareFoundDetaiSetArticleAttrRequest request = SquareRequest.squareFoundDetaiSetArticleAttrRequest();
		request.requestMethod = Constant.Request_POST;
		DynamicModel dynamicModel = (DynamicModel) modelList.get(id);
		request.articleId = dynamicModel.ArticleId;
		request.attr = "State"; //IsHot //置顶，State //删除 
		request.value = 1;
		
		//请求地址
		String requestID = Constant.RequestContstants.Request_Square_SetArticleAttr;		
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
				//成功时
				modelList.remove(id);
				adapter.notifyDataSetChanged();  
				listView.invalidate();
				ToastUtils.showCustomToast("删除成功");
					
			}
		});
	}
	
	
	
	
	@Override
	public BaseAdapter setAdapter() {
		BaseAdapter adapter = new DynamicAdapter(activity, modelList, listView);
		return adapter;
	}

	@Override
	public String setProcessURL() {
		return Constant.RequestContstants.Request_dynamic_list;
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
		}else if(DataManager.userModel!=null){
			params.add(String.valueOf(DataManager.userModel.UserId));
		}
		if(index==0){
			params.add("00");
		}else{
			if(modelList!=null&&modelList.size()>0){
				int length = modelList.size();
				DynamicModel dynamicModel = (DynamicModel) modelList.get(length-1);
				params.add(dynamicModel.PublishDate);
			}
			
		}
		params.add("2");
		
		return params;
	}

	@Override
	public Class<?> setModelClass() {
		return DynamicModel.class;
	}

	@Override
	public String setChannelId() {
		return null;
	}

	@Override
	protected void findViewById() {
		
	}

}
