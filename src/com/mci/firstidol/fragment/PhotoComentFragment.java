package com.mci.firstidol.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.BaseAdapter;

import com.mci.firstidol.adapter.SquareFoundDetailListViewAdapter;
import com.mci.firstidol.base.BaseListFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.CommentModel;

public class PhotoComentFragment extends BaseListFragment {

	private String article_id;
	
	@Override
	public void dataItemClick(int position) {

	}

	@Override
	public void dataLongItemClick(int position) {

	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(Constant.isComment){
			downMore(refreshListView);
			Constant.isComment = false;
		}
	}

	@Override
	public BaseAdapter setAdapter() {
		ArrayList<CommentModel> list = new ArrayList<CommentModel>();
		if(modelList!=null&&modelList.size()>0){
			int length = modelList.size();
			for (int i = 0; i < length; i++) {
				list.add((CommentModel)modelList.get(i));
			}
		}
		BaseAdapter adapter = new SquareFoundDetailListViewAdapter(activity, list, true);
		return adapter;
	}

	@Override
	public String setProcessURL() {
		return Constant.RequestContstants.Request_photo_comment;
	}

	@Override
	public List<String> setParams() {
		onlyParams = true;
		
		Bundle bundle = getArguments();
		if(bundle!=null){
			article_id = bundle.getString(Constant.IntentKey.articleID);
		}
		
		List<String> params = new ArrayList<String>();
		params.add(article_id);
		params.add("0");
		if(index==0){
			params.add("0");
		}else{
			if(modelList!=null&&modelList.size()>0){
				int length = modelList.size();
				CommentModel commentModel = (CommentModel) modelList.get(length-1);
				params.add(String.valueOf(commentModel.getCommentId()));
			}
		}
		params.add("10");
		params.add("0");
		return params;
	}

	@Override
	public Class<?> setModelClass() {
		return CommentModel.class;
	}

	@Override
	public String setChannelId() {
		return null;
	}

	@Override
	protected void findViewById() {

	}

}
