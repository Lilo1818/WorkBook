package com.mci.firstidol.fragment.star;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import android.os.Bundle;
import android.widget.BaseAdapter;

import com.mci.firstidol.activity.SquareFoundDetailActivity;
import com.mci.firstidol.activity.VideoPlayActivity;
import com.mci.firstidol.adapter.StarVideoAdapter;
import com.mci.firstidol.base.BaseListFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.MyCommentModel;
import com.mci.firstidol.model.StarVideoModel;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.Utily;

public class StarVideoFragment extends BaseListFragment{

	@Override
	public void dataItemClick(int position) {
		StarVideoModel model = (StarVideoModel) modelList.get(position);
		
		HashMap<String, String> params = new HashMap<String,String>();
		params.put(Constant.IntentKey.articleID, String.valueOf(model.ArticleId));
		
		/*Bundle bundle = new Bundle();
		int articeid = new Integer(String.valueOf(model.ArticleId));
		bundle.putLong(Constant.IntentKey.articleID, articeid);
		Utily.go2Activity(activity, SquareFoundDetailActivity.class,
				bundle);*/
		
		Utily.go2Activity(activity, VideoPlayActivity.class,params,null);
		
	}

	@Override
	public void dataLongItemClick(int position) {
		
	}

	@Override
	public BaseAdapter setAdapter() {
		BaseAdapter adapter = new StarVideoAdapter(activity, modelList, listView);
		return adapter;
	}

	@Override
	public String setProcessURL() {
		return Constant.RequestContstants.Request_staring_List;
	}

	@Override
	public List<String> setParams() {
		List<String> list = new ArrayList<>();
		if(index==0){
			list.add("00");
		}else{
			if(modelList!=null&&modelList.size()>0){
				int length = modelList.size();
				StarVideoModel starPhotoModel = (StarVideoModel) modelList.get(length-1);
				list.add(starPhotoModel.PublishDate);
			}
		}
		if(Constant.starModel!=null){
			list.add(String.valueOf(Constant.starModel.starId));
			list.add("2");
		}
		return list;
	}

	@Override
	public Class<?> setModelClass() {
		return StarVideoModel.class;
	}

	@Override
	public String setChannelId() {
		return Constant.ChannelIDMap.Staring_channelId;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onEventMainThread(AnyEventType event) {
		if(event!=null&&Constant.Config.UPDATE_STAR.equals(event.message)){
			retryClick();
		}
	};



}
