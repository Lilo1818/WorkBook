package com.mci.firstidol.adapter;

import java.util.List;

import com.mci.firstidol.R;
import com.mci.firstidol.base.MsBaseAdapter;
import com.mci.firstidol.model.ShoppingModel;
import com.mci.firstidol.model.SquareLiveModel;
import com.mci.firstidol.utils.Utily;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ShoppingAdapter extends MsBaseAdapter {

	private int width;
	public ShoppingAdapter(Context context, List<Object> modelList,
			ListView listView) {
		super(context, modelList, listView);
		width = Utily.getWidth(mContext)- Utily.dip2px(mContext, 20);
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item_shopping,
					parent, false);
			viewHolder = new ViewHolder();
			viewHolder.image_back = (ImageView) convertView
					.findViewById(R.id.image_bitmap);
			viewHolder.text_info = (TextView) convertView
					.findViewById(R.id.text_info);
			viewHolder.text_price = (TextView) convertView
					.findViewById(R.id.text_price);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		SquareLiveModel shoppingModel = (SquareLiveModel) modelList.get(position);
		viewHolder.text_info.setText(shoppingModel.getTitle());
		String price = shoppingModel.getPrice();
		if(price!=null&&!price.equals("")&&!price.equals("null")){
			viewHolder.text_price.setText("￥" + shoppingModel.getPrice());
		}else{
			viewHolder.text_price.setText("￥0");
		}
		
		String url = shoppingModel.getIco();
		
		LayoutParams layoutParams = viewHolder.image_back.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = width/2;
		viewHolder.image_back.setLayoutParams(layoutParams);
		
		ImageLoader.getInstance().displayImage(url, viewHolder.image_back);
	}

	@Override
	public void bindEvent(int position, View convertView, ViewGroup parent) {

	}

	class ViewHolder {
		public ImageView image_back;
		public TextView text_info;
		public TextView text_price;
	}

}
