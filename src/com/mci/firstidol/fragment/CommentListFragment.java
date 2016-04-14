package com.mci.firstidol.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.mci.firstidol.activity.SquareFoundDetailActivity;
import com.mci.firstidol.adapter.MyCommentAdapter;
import com.mci.firstidol.base.BaseListFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.CommentModel;
import com.mci.firstidol.model.MyCommentModel;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.StarIndexModel;
import com.mci.firstidol.model.SquareRequest.SquareLiveDetailDeleteRequest;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.AlertDialog;
import com.mci.firstidol.view.ToastUtils;

public class CommentListFragment extends BaseListFragment {
	
	private static int id;
	@Override
	public void dataItemClick(int position) {
		Bundle bundle = new Bundle();
		bundle.putLong(Constant.IntentKey.articleID, ((MyCommentModel)modelList.get(position)).ArticleId);
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
				obtainLiveChatDeleteEvent(id);
			}
		}, "取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "你点击了取消", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	protected void obtainLiveChatDeleteEvent(final int position) {
		// TODO Auto-generated method stub
		//CommentModel  commentModel = listData.get(position);
		SquareLiveDetailDeleteRequest request = SquareRequest.squareLiveDetailDeleteRequest();
		request.requestMethod = Constant.Request_POST;
		request.commentId =((MyCommentModel)modelList.get(position)).CommentId;
		//请求地址
		String requestID = Constant.RequestContstants.Request_ArticleComment_Delete;		
		
		
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
			}
		});
	}
	
	
	
	
	
	
	@Override
	public BaseAdapter setAdapter() {
		BaseAdapter adapter = new MyCommentAdapter(activity, modelList, listView);
		return adapter;
	}

	@Override
	public String setProcessURL() {
		return Constant.RequestContstants.Request_myComment_list;
	}

	@Override
	public List<String> setParams() {
		onlyParams = true;
		List<String> params = new ArrayList<String>();
		params.add("0");
		if(index==0){
			params.add("0");
		}else{
			String patitentId = String.valueOf(((MyCommentModel)modelList.get(modelList.size()-1)).CommentId);
			params.add(patitentId);
		}
		params.add("15");
		return params;
	}

	@Override
	public Class<?> setModelClass() {
		return MyCommentModel.class;
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
