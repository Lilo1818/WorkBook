package com.mci.firstidol.fragment.star;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.BaseAdapter;

import com.mci.firstidol.activity.PhotoSelectedActivity;
import com.mci.firstidol.adapter.StarPhotoAdapter;
import com.mci.firstidol.base.BaseGridFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.PhotoModel;
import com.mci.firstidol.model.StarPhotoModel;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.ToastUtils;

public class StarPhotoFragment extends BaseGridFragment{
	
	@Override
	public void dataItemClick(int position) {
		StarPhotoModel starPhotoModel = (StarPhotoModel) modelList.get(position);
		List<PhotoModel> pictures = starPhotoModel.ArticlePictures;
		if(pictures!=null&&pictures.size()>0){
			HashMap<String, String> params = new HashMap<String,String>();
			params.put(Constant.IntentKey.articleID, String.valueOf(starPhotoModel.ArticleId));
			Utily.go2Activity(activity, PhotoSelectedActivity.class,params,null);
		}else{
			ToastUtils.showCustomToast(activity, "暂无相册");
		}
	    
		
	}

	@Override
	public void dataLongItemClick(int position) {
		
	}

	@Override
	public BaseAdapter setAdapter() {
		BaseAdapter adapter = new StarPhotoAdapter(activity, modelList, null);
		return adapter;
	}

	@Override
	public String setProcessURL() {
		return Constant.RequestContstants.Request_staring_List;
	}

	@Override
	public List<String> setParams() {
		List<String> params = new ArrayList<String>();
		if(index==0){
			params.add("00");
		}else{
			if(modelList!=null&&modelList.size()>0){
				int length = modelList.size();
				StarPhotoModel starPhotoModel = (StarPhotoModel) modelList.get(length-1);
				params.add(starPhotoModel.PublishDate);
			}
		}
		if(Constant.starModel!=null){
			params.add(String.valueOf(Constant.starModel.starId));
			params.add("1");
		}
		return params;
	}

	@Override
	public Class<?> setModelClass() {
		return StarPhotoModel.class;
	}

	@Override
	public String setChannelId() {
		return Constant.ChannelIDMap.Staring_channelId;
	}

	@Override
	public int setNumColumns() {
		return 2;
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
