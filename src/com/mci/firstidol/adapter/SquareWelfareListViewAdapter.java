package com.mci.firstidol.adapter;

import java.util.ArrayList;

import com.mci.firstidol.model.SquareLiveModel;
import com.mci.firstidol.model.SquareModel.SquareFoundModel;
import com.mci.firstidol.utils.DateHelper;
import com.mci.firstidol.view.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SquareWelfareListViewAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<SquareLiveModel> listData;
	private LayoutInflater inflater;
	private SquareFoundModel squareFoundModel;
	private boolean isShowRight;

	public SquareWelfareListViewAdapter(Context context,
			ArrayList<SquareLiveModel> listData, boolean isShowRight) {
		super();
		this.context = context;
		this.listData = listData;
		this.isShowRight = isShowRight;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (listData != null) {
			return listData.size();
		} else {
			return 0;
		}
	}

	@Override
	public SquareLiveModel getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setListData(ArrayList<SquareLiveModel> listData) {
		this.listData = listData;
		notifyDataSetChanged();
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView instanceof TextView) {// 这是无数据时，添加的缓存view，有数据时需要清除，否则不为空时，影响下面的判断
			convertView = null;
		}

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_square_weflare, null);
			holder.iv_icon = (ImageView) convertView
					.findViewById(R.id.iv_icon);
			holder.tv_title = (android.widget.TextView) convertView
					.findViewById(R.id.tv_title);
			holder.tv_days_hint = (android.widget.TextView) convertView
					.findViewById(R.id.tv_days_hint);
			holder.ib_buy = (ImageButton) convertView.findViewById(R.id.ib_buy);
			holder.ll_days_hint = (LinearLayout) convertView.findViewById(R.id.ll_days_hint);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		
		if (listData!= null && listData.size()>0) {
			SquareLiveModel squareLiveModel = listData.get(position);
			
			
			holder.tv_title.setText(squareLiveModel.getTitle());
			ImageLoader.getInstance().displayImage(squareLiveModel.getIco(), holder.iv_icon,BaseApp.cornerCircleOptions);
			
			
			//判断福利是否开始
		    if (squareLiveModel.getEventsProgress() == 0) {
		        //未开始
		        //计算距离多长时间开始
		    	holder.tv_days_hint.setText("距离\n开始\n"+ DateHelper.getIntervalDays(squareLiveModel.getEventsStart()));
		    	holder.ll_days_hint.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.square_welfare_hongqi));
		    }else if (squareLiveModel.getEventsProgress() == 1) {
		        //进行中
		    	holder.tv_days_hint.setText("剩余\n"+ DateHelper.getIntervalDays(squareLiveModel.getEventsStart()));
		    	holder.ll_days_hint.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.square_welfare_hongqi));
		    }else {
		        //已结束
		    	holder.tv_days_hint.setText("已经\n结束");
		    	holder.ll_days_hint.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.square_welfare_huiqi));
		}
			
			
			holder.ib_buy.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					ToastUtils.showCustomToast("xxx");
				}
			});
		}
		

		return convertView;
	}

	public class ViewHolder {
		ImageView iv_icon;// 
		TextView tv_title;// 
		TextView tv_days_hint;// 
		ImageButton ib_buy;// 
		LinearLayout ll_days_hint;
	}

	public void refershData(ArrayList<SquareLiveModel> contents) {
		// TODO Auto-generated method stub
		listData = contents;
		notifyDataSetChanged();
	}

}
