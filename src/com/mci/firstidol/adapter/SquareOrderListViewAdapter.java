package com.mci.firstidol.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.mci.firstidol.model.SquareModel.SquareFoundModel;
import com.mci.firstidol.model.SquareModel.SquareListFanModel;
import com.mci.firstidol.model.SquareModel.SquareListStarModel;
import com.mci.firstidol.utils.DeviceUtils;
import com.mci.firstidol.utils.DisplayUtil;
import com.mci.firstidol.utils.LogUtils;
import com.mci.firstidol.utils.PreferencesUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SquareOrderListViewAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Object> listData;
	private LayoutInflater inflater;
	private SquareFoundModel squareFoundModel;
	private boolean isShowRight;

	public SquareOrderListViewAdapter(Context context,
			ArrayList<Object> listData, boolean isShowRight) {
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
			return listData.size()-3;
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setListData(ArrayList<Object> listData) {
		this.listData = listData;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView instanceof TextView) {// 这是无数据时，添加的缓存view，有数据时需要清除，否则不为空时，影响下面的判断
			convertView = null;
		}
		
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_square_order, null);
			holder.iv_user_img = (ImageView) convertView.findViewById(R.id.iv_user_img);
			holder.tv_num = (android.widget.TextView) convertView
					.findViewById(R.id.tv_num);
			holder.tv_title = (android.widget.TextView) convertView
					.findViewById(R.id.tv_title);
			holder.tv_subtitle = (android.widget.TextView) convertView
					.findViewById(R.id.tv_subtitle);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//Log.i("xxxxxxxxxxxxx", "updateContent1" + "----" + listData);
		if (listData!=null && listData.size()>0) {
			int mPosition = position + 3;
			Object object = listData.get(mPosition);//从第四个开始
			
			holder.tv_num.setText((mPosition+1)+"");
			if (object instanceof SquareListStarModel) {
				SquareListStarModel squareListStarModel = (SquareListStarModel)object;
				ImageLoader.getInstance().displayImage(squareListStarModel.getAvatar(), holder.iv_user_img,BaseApp.circleOptions);

				holder.tv_title.setText(squareListStarModel.getStarName());
				holder.tv_subtitle.setText("活跃度:"+(int)squareListStarModel.getLate7DayPoints());
				
			} else if(object instanceof SquareListFanModel){
				SquareListFanModel squareListFanModel = (SquareListFanModel)object;
				ImageLoader.getInstance().displayImage(squareListFanModel.getAvatar(), holder.iv_user_img,BaseApp.circleOptions);
				
				
				holder.tv_title.setText(squareListFanModel.getNickName());
				holder.tv_subtitle.setText("积分:"+(int)squareListFanModel.getExperience());
			}
		}
		
		
		
		

		return convertView;
	}

	public class ViewHolder {
		ImageView iv_user_img;// 用户图标
		TextView tv_num;// 
		TextView tv_title;// 
		TextView tv_subtitle;// 
	}

	public void refershData(ArrayList<Object> contents) {
		// TODO Auto-generated method stub
		listData = contents;
		notifyDataSetChanged();
	}

}
