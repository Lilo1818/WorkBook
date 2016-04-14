package com.mci.firstidol.fragment.star;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mci.firstidol.activity.ReportActivity;
import com.mci.firstidol.activity.SquareFoundDetailActivity;
import com.mci.firstidol.activity.SquareFoundDetailAddReplyActivity;
import com.mci.firstidol.adapter.StarIndexAdapter;
import com.mci.firstidol.adapter.StarIndexAdapter.StarListAdapterListener;
import com.mci.firstidol.base.BaseNormalListFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.fragment.star.StarIndexFragment.IndexDataFlush;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.StarIndexModel;
import com.mci.firstidol.model.SquareRequest.SquareFoundDetaiSetArticleAttrRequest;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.ToastUtils;

public class StarIndexTextFragment extends BaseNormalListFragment implements
		IndexDataFlush, StarListAdapterListener {

	public TextLoad textLoadListener;// 监听
	private String typeCode;//类型

	@Override
	public void dataItemClick(int position) {
		Bundle bundle = new Bundle();
		bundle.putLong(Constant.IntentKey.articleID,
				((StarIndexModel) modelList.get(position)).ArticleId);
		Utily.go2Activity(getActivity(), SquareFoundDetailActivity.class,
				bundle);
	}

	@Override
	public void dataLongItemClick(int position) {                     

	}  

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

		};
	};

	@Override
	public BaseAdapter setAdapter() {
		BaseAdapter adapter = new StarIndexAdapter(activity, modelList,
				refreshListView, handler, this);
		return adapter;
	}

	@Override
	public String setProcessURL() {
		Bundle bundle = getArguments();
		if(bundle!=null){
			typeCode = bundle.getString(Constant.IntentKey.typeCode);
		}
		if(Constant.IntentValue.ACTIVITY_SEE.equals(typeCode)){
			return Constant.RequestContstants.Request_starsee_list;
		}else if(Constant.IntentValue.ACTIVITY_HOT.equals(typeCode)){
			return Constant.RequestContstants.Request_starhot_list;
		}else{
			return Constant.RequestContstants.Request_staring_List;
		}
		
	}

	@Override
	public List<String> setParams() {
		List<String> params = new ArrayList<String>();
		if (index == 0) {
			params.add("00");
		} else {
			if (modelList != null && modelList.size() > 0) {
				int length = modelList.size();
				StarIndexModel squareLiveModel =  (StarIndexModel) modelList
						.get(length - 1);
				params.add(squareLiveModel.PublishDate);
			}
		}
		if(Constant.starModel!=null){
			params.add(String.valueOf(Constant.starModel.starId));
			Bundle bundle = getArguments();
			if(bundle!=null){
				typeCode = bundle.getString(Constant.IntentKey.typeCode);
			}
			if(Constant.IntentValue.ACTIVITY_FANS.equals(typeCode)){
				params.add("0");
			}
		}
		return params;
	}
	
	public void setListViewHeight(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		int item_devide = listView.getDividerHeight();
		int length = listAdapter.getCount();
		for (int i = 0; i < length; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
			
			StarIndexModel starIndexModel = (StarIndexModel) modelList.get(i);
			String content = starIndexModel.Content;
			if(content!=null&&!content.equals("")){
				int size = content.length();
				int num = size/25;
				totalHeight += num*49;
			}
			
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight+(length-1)*item_devide;
		listView.setLayoutParams(params);
	}

	@Override
	public Class<?> setModelClass() {
		return StarIndexModel.class;
	}

	@Override
	public String setChannelId() {
		return Constant.ChannelIDMap.Staring_channelId;
	}

	@Override
	public void pullToReflushData() {
		pullToReflush();
	}

	@Override
	public void loadMore() {
		downMore();
	}

	public void setListener(TextLoad textLoadListener) {
		this.textLoadListener = textLoadListener;
	}

	@Override
	public void doHideLoadingView() {
		super.doHideLoadingView();
		if (textLoadListener!=null) {
			textLoadListener.hasLoading();
		}
		
	}

	public interface TextLoad {
		void hasLoading();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

	}

	@Override
	public void starPopMenuClickIndex(int index, int position) {
		StarIndexModel starIndexModel = (StarIndexModel) modelList.get(position);
		switch (index) {
		case 0:
			obtainArticleAttrRequest(true,starIndexModel);
			break;
		case 1:
			obtainArticleAttrRequest(false,starIndexModel);
			break;
		case 2:
			obtainCommentFavRequest(true,starIndexModel.ArticleId);
			break;
		case 3:
			//ToastUtils.show(getActivity(), "举报");
			Bundle bundle = new Bundle();
        	bundle.putString("ModType", "4");
        	bundle.putLong("RefId", starIndexModel.ArticleId);
    		Utily.go2Activity(getActivity(), ReportActivity.class,
    				bundle);
			break;
		default:
			break;
		}
	}

	@Override
	public void starPopupAnimViewClick(int index, int position) {
		StarIndexModel starIndexModel = (StarIndexModel) modelList
				.get(position);
		if (index < 4) {
			obtainZanRequest(starIndexModel, starIndexModel.ArticleId, index);
		} else {
			Bundle bundle = new Bundle();
			bundle.putLong(Constant.IntentKey.articleID,
					starIndexModel.ArticleId);
			Utily.go2Activity(getActivity(),
					SquareFoundDetailAddReplyActivity.class, bundle);
		}

	}
	
	/**
	 * 广场-发现详情-置顶/删除
	 * @param isCancelFav
	 */
	public void obtainArticleAttrRequest(final boolean isTop,final StarIndexModel starIndexModel){
		SquareFoundDetaiSetArticleAttrRequest request = SquareRequest.squareFoundDetaiSetArticleAttrRequest();
		request.requestMethod = Constant.Request_POST;
		request.articleId = starIndexModel.ArticleId;
		request.attr = isTop?"IsHot":"State"; //IsHot //置顶，State //删除 
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
				if (isTop) {
					ToastUtils.showCustomToast("置顶成功");
					modelList.remove(starIndexModel);
					modelList.add(0, starIndexModel);
					adapter.notifyDataSetChanged();
				} else {
					ToastUtils.showCustomToast("删除成功");
					modelList.remove(starIndexModel);
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	// 文章点赞
	private void obtainZanRequest(final StarIndexModel selectedStarModel,
			long articleId, final int index) {
		// TODO Auto-generated method stub
		BaseRequest request = new BaseRequest();
		// 请求地址
		String requestID = String.format(
				Constant.RequestContstants.Request_Square_Zan, articleId,
				index + 1);
		doAsync(false, requestID, request, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast("点赞成功");
				selectedStarModel.HasStore = true;
				if (index == 0) {
					selectedStarModel.UpCount = selectedStarModel.UpCount + 1;
				} else if (index == 1) {
					selectedStarModel.UpCount2 = selectedStarModel.UpCount2 + 1;
				} else if (index == 2) {
					selectedStarModel.UpCount3 = selectedStarModel.UpCount3 + 1;
				} else if (index == 3) {
					selectedStarModel.UpCount4 = selectedStarModel.UpCount4 + 1;
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	/**
	 * 详情-收藏
	 * @param isCancelFav
	 */
	public void obtainCommentFavRequest(final boolean isFav,long ArticleId){
		BaseRequest request = new BaseRequest();
		//请求地址
		String requestID = String.format((isFav ? Constant.RequestContstants.Request_Article_Store : Constant.RequestContstants.Request_Article_CancelStore), ArticleId);		
		doAsync(true, requestID, request, new Callback<Exception>() {
			
			@Override
			public void onCallback(Exception e) {
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {
			
			@Override
			public void onCallback(String result) {
				ToastUtils.showCustomToast("收藏成功");
			}
		});
	}
	
	@Override
	public void onEventMainThread(AnyEventType event) {
		if(event!=null&&Constant.Config.UPDATE_STAR.equals(event.message)){
			getDataView();
		}
	};

}
