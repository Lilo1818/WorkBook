package com.mci.firstidol.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mci.firstidol.R;
import com.mci.firstidol.adapter.StarVideoAdapter.ViewHolder;
import com.mci.firstidol.base.MsBaseAdapter;
import com.mci.firstidol.model.StarTripModel;
import com.mci.firstidol.utils.Utily;

@SuppressLint("ResourceAsColor")
public class StarTripAdapter extends MsBaseAdapter {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public StarTripAdapter(Context context, List<Object> modelList,
			ListView listView) {
		super(context, modelList, listView);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.item_startrip,
					parent, false);
			viewHolder.image_click = (ImageView) convertView
					.findViewById(R.id.image_click);
			viewHolder.text_date = (TextView) convertView
					.findViewById(R.id.text_date);
			viewHolder.text_weekend = (TextView) convertView
					.findViewById(R.id.text_weekend);
			viewHolder.text_title = (TextView) convertView
					.findViewById(R.id.text_title);
			viewHolder.text_address = (TextView) convertView
					.findViewById(R.id.text_address);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		StarTripModel starTripModel = (StarTripModel) modelList.get(position);
		Date tripDate = null;
		// 封装时间
		String publishData = starTripModel.TripDate;
		try {
			if (publishData != null && publishData.length() > 10) {
				String dateStr = publishData.substring(6,
						publishData.length() - 2);
				tripDate = new Date(Long.parseLong(dateStr));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (tripDate != null) {
			String weekend = Utily.getWeekOfDate(tripDate);
			String canclder = String.valueOf(tripDate.getDate());

			viewHolder.text_weekend.setText(weekend);
			viewHolder.text_date.setText(canclder);
		}

		viewHolder.text_address.setText("地点：" + starTripModel.Address);
		viewHolder.text_title.setText(starTripModel.TripContent);

		if (starTripModel.HasRemind) {
			viewHolder.image_click
					.setBackgroundResource(R.drawable.trip_click_p);
			viewHolder.text_address.setTextColor(mContext.getResources()
					.getColor(R.color.color_main));
			viewHolder.text_title.setTextColor(mContext.getResources()
					.getColor(R.color.color_main));
		} else {
			viewHolder.image_click
					.setBackgroundResource(R.drawable.trip_click_normal);
			viewHolder.text_title.setTextColor(mContext.getResources()
					.getColor(R.color.text_color_login_gray));
			viewHolder.text_address.setTextColor(mContext.getResources()
					.getColor(R.color.text_color_login_gray));
		}

	}

	@Override
	public void bindEvent(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

	}

	class ViewHolder {
		public TextView text_date;// 日期
		public TextView text_weekend;// 周几
		public ImageView image_click;// 闹钟
		public TextView text_title;// 标题
		public TextView text_address;// 地址
	}

}
