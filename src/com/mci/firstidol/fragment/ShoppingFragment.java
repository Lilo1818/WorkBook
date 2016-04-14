package com.mci.firstidol.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mci.firstidol.activity.SquareWelfareDetailActivity;
import com.mci.firstidol.activity.WebActivity;
import com.mci.firstidol.adapter.ShoppingAdapter;
import com.mci.firstidol.base.BaseListFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.ShoppingModel;
import com.mci.firstidol.model.SquareLiveModel;
import com.mci.firstidol.utils.Utily;

import android.os.Bundle;
import android.widget.BaseAdapter;

public class ShoppingFragment extends BaseListFragment {

	@Override
	public void dataItemClick(int position) {
		Bundle mbuBundle = new Bundle();
		mbuBundle.putSerializable(Constant.IntentKey.squareLiveModel,
				(Serializable) modelList.get(position));
		mbuBundle.putString(Constant.IntentKey.typeCode,
				Constant.IntentValue.ACTIVITY_SHOPPING);
		Utily.go2Activity(activity, SquareWelfareDetailActivity.class,
				mbuBundle);
	}

	@Override
	public void dataLongItemClick(int position) {

	}

	@Override
	public BaseAdapter setAdapter() {
		BaseAdapter adapter = new ShoppingAdapter(activity, modelList, listView);
		return adapter;
	}

	@Override
	public String setProcessURL() {
		return Constant.RequestContstants.Request_shopping_List;
	}

	@Override
	public List<String> setParams() {
		List<String> params = new ArrayList<String>();
		if (index == 0) {
			params.add("00");
		} else {
			if (modelList != null && modelList.size() > 0) {
				int length = modelList.size();
				SquareLiveModel squareLiveModel = (SquareLiveModel) modelList
						.get(length - 1);
				params.add(squareLiveModel.getPublishDate());
			}
		}
		return params;
	}

	@Override
	public Class<?> setModelClass() {
		return SquareLiveModel.class;
	}

	@Override
	public String setChannelId() {
		return Constant.ChannelIDMap.Shopping_channelId;
	}

	@Override
	protected void findViewById() {

	}

}
