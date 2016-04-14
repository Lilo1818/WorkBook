package com.mci.firstidol.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mci.firstidol.R;
import com.mci.firstidol.base.MsBaseAdapter;
import com.mci.firstidol.model.StarVideoModel;
import com.mci.firstidol.utils.BitmapUtil;
import com.mci.firstidol.utils.SyncImageLoader;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class StarVideoAdapter extends MsBaseAdapter {

	public int image_width;
	public int image_height;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
	private int width;
	public StarVideoAdapter(Context context, List<Object> modelList,
			ListView listView) {
		super(context, modelList, listView);
		width = Utily.getWidth(mContext);
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.item_starvideo,
					parent, false);
			viewHolder.icon_view = (RoundedImageView) convertView
					.findViewById(R.id.icon);
			viewHolder.image_back = (RoundedImageView) convertView
					.findViewById(R.id.image_back);
			viewHolder.text_comment = (TextView) convertView
					.findViewById(R.id.text_comment);
			viewHolder.text_see = (TextView) convertView
					.findViewById(R.id.text_see);
			viewHolder.text_zan = (TextView) convertView
					.findViewById(R.id.text_zan);
			viewHolder.text_info = (TextView) convertView
					.findViewById(R.id.text_info);
			viewHolder.text_Name = (TextView) convertView
					.findViewById(R.id.text_title);
			viewHolder.text_time = (TextView) convertView
					.findViewById(R.id.text_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		StarVideoModel starVideoModel = (StarVideoModel) modelList
				.get(position);

		String back_url = starVideoModel.VideoIco;
		String head_url = starVideoModel.UserAvatar;

		LayoutParams layoutParams = viewHolder.image_back.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = width/2;
		viewHolder.image_back.setLayoutParams(layoutParams);
		
		ImageLoader.getInstance().displayImage(back_url, viewHolder.image_back);
		loadImage(head_url, viewHolder.icon_view);

		viewHolder.text_comment.setText(starVideoModel.CommentCount);
		viewHolder.text_info.setText(starVideoModel.Title);
		viewHolder.text_Name.setText(starVideoModel.UserNickName);
		viewHolder.text_see.setText(starVideoModel.ViewCount);
		viewHolder.text_zan.setText(starVideoModel.UpCoun);
		
		String publishData = starVideoModel.PublishDate;
		try {
			if(publishData!=null&&publishData.length()>10){
				String dateStr = publishData.substring(6, publishData.length()-2);
				viewHolder.text_time.setText(sdf.format(new Date(Long.parseLong(dateStr))));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void bindEvent(int position, View convertView, ViewGroup parent) {

	}

	public SyncImageLoader.OnImageLoadListener imageListener = new SyncImageLoader.OnImageLoadListener() {
		@Override
		public void onImageLoad(Integer t, Bitmap bm, View parent) {
			if (parent != null) {
				if (bm == null || bm.isRecycled()) {
					((ImageView) parent)
							.setImageResource(R.drawable.ic_launcher);
				} else {

					// 设置iv的大小
					int bitmap_height = bm.getHeight();
					int bitmap_width = bm.getWidth();
					image_height = (int) ((float) image_width / (float) bitmap_width)
							* bitmap_height;
					LayoutParams params = parent.getLayoutParams();
					params.height = image_height;
					parent.setLayoutParams(params);
					//Bitmap bitm = BitmapUtil.toRoundBitmap(mContext, bm, 10);

					((ImageView) parent).setImageBitmap(bm);
				}
			}
		}

		@Override
		public void onError(Integer t, View parent) {
			if (parent != null) {
				((ImageView) parent).setImageResource(R.drawable.ic_launcher);
			}
		}
	};

	public void loadBackImage(String path, ImageView iv) {
		if (path != null && !path.equals("")) {
			Bitmap bm = syncImageLoader.getBitmapFromMemory(path);
			if (bm == null || bm.isRecycled()) {
				syncImageLoader.loadImage(-1, path, imageListener, iv, 1);
			} else {
				iv.setImageBitmap(bm);
			}
		} else {
			iv.setImageResource(R.drawable.ic_launcher);
		}

	}

	class ViewHolder {
		public RoundedImageView image_back;// 图片背景图

		public TextView text_zan;// 赞的文本

		public TextView text_comment;// 评论文本

		public TextView text_see;// 多少人关注

		public RoundedImageView icon_view;// 头像

		public TextView text_Name;// 名称

		public TextView text_time;// 时间

		public TextView text_info;// 内容
	}

}
