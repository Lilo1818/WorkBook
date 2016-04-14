package com.mci.firstidol.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.mci.firstidol.model.SquareLiveModel;
import com.mci.firstidol.model.SquareModel.SquareFoundModel;
import com.mci.firstidol.utils.DateHelper;
import com.mci.firstidol.utils.DeviceUtils;
import com.mci.firstidol.utils.DisplayUtil;
import com.mci.firstidol.utils.LogUtils;
import com.mci.firstidol.utils.PreferencesUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
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
import android.widget.Toast;

public class SquareLiveListViewAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<SquareLiveModel> listData;
	private LayoutInflater inflater;
	private boolean isShowRight;

	public SquareLiveListViewAdapter(Context context,
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView instanceof TextView) {// 这是无数据时，添加的缓存view，有数据时需要清除，否则不为空时，影响下面的判断
			convertView = null;
		}

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_square_live, null);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);//月日
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);//时分
			holder.iv_status = (ImageView) convertView.findViewById(R.id.iv_status);//状态
			holder.iv_live_hint = (ImageView) convertView.findViewById(R.id.iv_live_hint);
			holder.rl_time_bg = (RelativeLayout) convertView.findViewById(R.id.rl_time_bg);
			holder.tv_hint_date = (TextView) convertView.findViewById(R.id.tv_hint_date);//提示时间
			holder.tv_starname = (TextView) convertView.findViewById(R.id.tv_starname);//明星名称
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);//标题
			holder.iv_person_num = (ImageView) convertView.findViewById(R.id.iv_person_num);
			holder.tv_person_num = (TextView) convertView.findViewById(R.id.tv_person_num);//人员数量
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);//
			holder.iv_dujia = (ImageView) convertView.findViewById(R.id.iv_dujia);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (listData!= null && listData.size()>0) {
			SquareLiveModel squareLiveModel = listData.get(position);
			
			
			
			//EventsStart ，EventsProgress，StarName,Title,Ico,ChatPeopleCount
			
			holder.tv_date.setText(DateHelper.getStringFromDateStr(squareLiveModel.getEventsStart(),"MM月dd日"));;//月日
			holder.tv_time.setText(DateHelper.getStringFromDateStr(squareLiveModel.getEventsStart(),"HH:mm"));;//时分
			
			//0：未开始，1：进行中 2：已结束
			if (squareLiveModel.getEventsProgress() == 0) {//未开始
				holder.iv_status.setImageResource(R.drawable.square_live_start);
				if(squareLiveModel.getEventsStart().isEmpty()){
					Toast.makeText(context, "值为空", Toast.LENGTH_SHORT).show();
				}else{
					String time = DateHelper.getIntervalDays(squareLiveModel.getEventsStart());
					/*Toast.makeText(context, "值为+"+time, Toast.LENGTH_SHORT).show();
					String hint = DateHelper.getIntervalDays(DateHelper.getStringFromDateStr(squareLiveModel.getEventsStart(),"yyyy-MM-dd HH:mm:ss"));*/
					holder.tv_hint_date.setText(time);
				}
				
				
				holder.iv_live_hint.setImageResource(R.drawable.square_live_blue);
				holder.rl_time_bg.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.square_live_dialog_blue_bg));
				int colorId = context.getResources().getColor(R.color.color_blue_01);
				holder.tv_hint_date.setTextColor(colorId);
				holder.tv_starname.setTextColor(colorId);
				holder.tv_title.setTextColor(colorId);
				holder.iv_person_num.setImageResource(R.drawable.square_live_blue_preson_num);
				holder.tv_person_num.setTextColor(colorId);
			} else if (squareLiveModel.getEventsProgress() == 1) {//进行中
				holder.iv_status.setImageResource(R.drawable.square_live_running);
				holder.tv_hint_date.setText("");
				
				holder.iv_live_hint.setImageResource(R.drawable.square_live_red);
				holder.rl_time_bg.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.square_live_dialog_bg));
				int colorId = context.getResources().getColor(R.color.color_purple_01);
				holder.tv_starname.setTextColor(colorId);
				holder.tv_title.setTextColor(colorId);
				holder.iv_person_num.setImageResource(R.drawable.square_live_red_person_num);
				holder.tv_person_num.setTextColor(colorId);
			} else if (squareLiveModel.getEventsProgress() == 2) {//已结束
				holder.iv_status.setImageResource(R.drawable.square_live_end);
				holder.tv_hint_date.setText("");
				
				holder.iv_live_hint.setImageResource(R.drawable.square_live_gray);
				holder.rl_time_bg.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.square_live_dialog_gray_bg));
				int colorId = context.getResources().getColor(R.color.color_gray_02);
				holder.tv_starname.setTextColor(colorId);
				holder.tv_title.setTextColor(colorId);
				holder.iv_person_num.setImageResource(R.drawable.square_live_gray_preson_num);
				holder.tv_person_num.setTextColor(colorId);
			}
			
			holder.tv_starname.setText(squareLiveModel.getStarName());;//明星名称
			holder.tv_title.setText(squareLiveModel.getTitle());//标题
			holder.tv_person_num.setText((int)squareLiveModel.getChatPeopleCount()+"");;//人员数量
			String url;
			if (squareLiveModel.getModelType() == 2) {
				url = squareLiveModel.getIco();
			} else {
				url = squareLiveModel.getIco2();
				if (TextUtils.isEmpty(url)) {
					url = squareLiveModel.getIco();
				}
			}
			ImageLoader.getInstance().displayImage(url, holder.iv_icon,BaseApp.cornerCircleOptions);
			
			
			
//			
//			ImageLoader.getInstance().displayImage(squareFoundModel.getUserAvatar(), holder.iv_user_logo,BaseApp.circleOptions);;
//			holder.tv_nickname.setText(squareFoundModel.getUserNickName());;
//			holder.tv_time.setText(squareFoundModel.getPublishDate());;
////			holder.ib_user_operate;
//			
//			//设置图片的位置
//	        MarginLayoutParams margin9 = new MarginLayoutParams(
//	        		holder.iv_content.getLayoutParams());
//	        LinearLayout.LayoutParams layoutParams9 = new LinearLayout.LayoutParams(margin9);
//	        layoutParams9.height = (int) (DeviceUtils.getWidthPixels((Activity)context)*squareFoundModel.getIcoHeight()/squareFoundModel.getIcoWidth());//设置图片的高度
//	        layoutParams9.width = DeviceUtils.getWidthPixels((Activity)context); //设置图片的宽度
//	        holder.iv_content.setLayoutParams(layoutParams9);
//	        
//	        
//			ImageLoader.getInstance().displayImage(squareFoundModel.getIco(), holder.iv_content);;
//			holder.tv_content.setText(squareFoundModel.getContent());
////			holder.btn_meng;
////			holder.btn_tian;
////			holder.btn_shuai;
////			holder.btn_jia;
////			holder.btn_comment;
//			
//			int readCount = (int)(squareFoundModel.getUpCount() + squareFoundModel.getUpCount2() + squareFoundModel.getUpCount3() + squareFoundModel.getUpCount4()); 
//			holder.tv_read.setText((int)squareFoundModel.getViewCount() + "");;
//			holder.tv_collection.setText(readCount + "个朋友推荐!");
////			holder.tv_collection.setText(squareFoundModel.getCommentCount() + "个朋友推荐!");
////			holder.ll_comment
		}
		
		
		
		
		

		return convertView;
	}

	public class ViewHolder {
		TextView tv_date;//月日
		TextView tv_time;//时分
		ImageView iv_status;//状态
		TextView tv_hint_date;//提示时间
		TextView tv_starname;//明星名称
		TextView tv_title;//标题
		ImageView iv_person_num;//---
		TextView tv_person_num;//人员数量
		ImageView iv_icon;//
		ImageView iv_dujia;
		ImageView iv_live_hint;//时间点--
		RelativeLayout rl_time_bg;//时间背景--
		
	}

	public void refershData(ArrayList<SquareLiveModel> contents) {
		// TODO Auto-generated method stub
		listData = contents;
		notifyDataSetChanged();
	}

}
