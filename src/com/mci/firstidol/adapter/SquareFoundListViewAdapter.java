package com.mci.firstidol.adapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import com.mci.firstidol.activity.LoginActivity;
import com.mci.firstidol.activity.MyInfoActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.constant.Constant.IntentKey;
import com.mci.firstidol.model.HotComment;
import com.mci.firstidol.model.SquareModel;
import com.mci.firstidol.model.SquareModel.SquareFoundModel;
import com.mci.firstidol.utils.DateHelper;
import com.mci.firstidol.utils.DeviceUtils;
import com.mci.firstidol.utils.DisplayUtil;
import com.mci.firstidol.utils.LogUtils;
import com.mci.firstidol.utils.PreferencesUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.PopAnimView;
import com.mci.firstidol.view.PopMenu;
import com.mci.firstidol.view.PopAnimView.PopupAnimViewListener;
import com.mci.firstidol.view.PopMenu.PopMenuListener;
import com.mci.firstidol.view.PowerImageView;
import com.mci.firstidol.view.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;

public class SquareFoundListViewAdapter extends BaseAdapter implements
		OnClickListener, PopMenuListener {

	private Context context;
	private ArrayList<SquareFoundModel> listData;
	private LayoutInflater inflater;
//	private SquareFoundModel squareFoundModel;
	private boolean isShowRight;
	private PopMenu popMenu;

	private SquareFoundListViewAdapterListener squareFoundListViewAdapterListener;

	public SquareFoundListViewAdapter(Context context,
			ArrayList<SquareFoundModel> listData, boolean isShowRight) {
		super();
		this.context = context;
		this.listData = listData;
		this.isShowRight = isShowRight;
		if (context!=null) {
			this.inflater = LayoutInflater.from(context);
			
			popMenu = new PopMenu(context);
			// 菜单项点击监听器
			popMenu.setPopMenuListener(this);
		}
		

		

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
	public SquareFoundModel getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setListData(ArrayList<SquareFoundModel> listData) {
		// this.listData = listData;
		// notifyDataSetChanged();
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
			convertView = inflater.inflate(R.layout.item_square_found, null);
			holder.ll_user_info = (LinearLayout) convertView.findViewById(R.id.ll_user_info);
			holder.iv_user_logo = (ImageView) convertView
					.findViewById(R.id.iv_user_logo);
			holder.tv_nickname = (android.widget.TextView) convertView
					.findViewById(R.id.tv_nickname);
			holder.tv_time = (android.widget.TextView) convertView
					.findViewById(R.id.tv_time);
			holder.ib_user_operate = (android.widget.ImageButton) convertView
					.findViewById(R.id.ib_user_operate);
			holder.iv_content = (ImageView) convertView
					.findViewById(R.id.iv_content);
			holder.tv_content = (android.widget.TextView) convertView
					.findViewById(R.id.tv_content);

			holder.view_meng = (PopAnimView) convertView
					.findViewById(R.id.view_meng);
			holder.view_tian = (PopAnimView) convertView
					.findViewById(R.id.view_tian);
			holder.view_shuai = (PopAnimView) convertView
					.findViewById(R.id.view_shuai);
			holder.view_jia = (PopAnimView) convertView
					.findViewById(R.id.view_jia);

			holder.btn_comment = (android.widget.Button) convertView
					.findViewById(R.id.btn_comment);
			holder.tv_read = (android.widget.TextView) convertView
					.findViewById(R.id.tv_read);
			holder.tv_collection = (android.widget.TextView) convertView
					.findViewById(R.id.tv_collection);
			
			
			holder.ll_comment = (android.widget.LinearLayout) convertView.findViewById(R.id.ll_comment);
			holder.ll_comment_item1 = (LinearLayout) convertView.findViewById(R.id.ll_comment_list_item1);
			holder.ll_comment_item2 = (LinearLayout) convertView.findViewById(R.id.ll_comment_list_item2);
			holder.tv_comment_content1 = (TextView) convertView.findViewById(R.id.tv_comment_content1);
			holder.tv_comment_content2 = (TextView) convertView.findViewById(R.id.tv_comment_content2);
			holder.tv_comment_nickname1 = (TextView) convertView.findViewById(R.id.tv_comment_nickname1);
			holder.tv_comment_nickname2 = (TextView) convertView.findViewById(R.id.tv_comment_nickname2);

			convertView.setTag(holder);

			initPopAnimView(holder, position);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (listData != null && listData.size() > 0) {
			final SquareFoundModel squareFoundModel = listData.get(position);

			ImageLoader.getInstance().displayImage(
					squareFoundModel.getUserAvatar(), holder.iv_user_logo,
					BaseApp.circleOptions);
			holder.tv_nickname.setText(squareFoundModel.getUserNickName());
			holder.tv_time.setText(DateHelper.getStringFromDateStr(squareFoundModel.getPublishDate(), "yyyy-MM-dd HH:mm"));

			// 设置图片的位置
			MarginLayoutParams margin9 = new MarginLayoutParams(
					holder.iv_content.getLayoutParams());
			LinearLayout.LayoutParams layoutParams9 = new LinearLayout.LayoutParams(
					margin9);
			
			layoutParams9.height = squareFoundModel
					.getIcoWidth() == 0 ? DisplayUtil.dip2px(context, 200):(int) (DeviceUtils
					.getWidthPixels((Activity) context)
					* squareFoundModel.getIcoHeight() / squareFoundModel
					.getIcoWidth());// 设置图片的高度

			layoutParams9.width = DeviceUtils
					.getWidthPixels((Activity) context); // 设置图片的宽度
			holder.iv_content.setLayoutParams(layoutParams9);

			if (squareFoundModel.getModelType() == 2) {//视频
				ImageLoader.getInstance().displayImage(squareFoundModel.getVideoIco(),
						holder.iv_content);
			} else {
				Log.v("TextGif", "图片为"+squareFoundModel.getIco());
				ImageLoader.getInstance().displayImage(squareFoundModel.getIco(),
						holder.iv_content);
				/*new AsyncTask<Void, Void, byte[]>() {

					@Override
					protected byte[] doInBackground(Void... params) {
						byte[] gifbyte = null;
						HttpURLConnection conn = null;
						try {
							URL url = new URL(squareFoundModel.getIco());
							conn = (HttpURLConnection) url.openConnection();
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							InputStream in = conn.getInputStream();
							if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
								// 连接不成功
								//Log.i(TAG, "连接不成功");
								return null;
							}

							byte[] buffer = new byte[1024];
							int len = 0;
							while ((len = in.read(buffer)) > 0) {
								out.write(buffer, 0, len);
							}
							gifbyte = out.toByteArray();
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							conn.disconnect();
						}

						// 写入文件

						
						 * FileOutputStream fos = null; try {
						 * 
						 * File root = Environment.getExternalStorageDirectory(); File
						 * myFile = new File(root, "test.jpg"); Log.v(TAG,
						 * myFile.getAbsolutePath()); fos = new
						 * FileOutputStream(myFile); fos.write(gifbyte); } catch
						 * (FileNotFoundException e) { e.printStackTrace(); } catch
						 * (IOException e) { e.printStackTrace(); } finally { if (fos !=
						 * null) { try { fos.close(); } catch (IOException e) {
						 * 
						 * e.printStackTrace(); } } }
						 

						return gifbyte;

					}

					protected void onPostExecute(byte[] gifbyte) {

						// 判断是否是gif图
						Movie gif = Movie.decodeByteArray(gifbyte, 0, gifbyte.length);
						if (gif != null) {
							//Log.v(TAG, "是gif图片");
							GifDrawable gifDrawable = null;
							try {
								gifDrawable = new GifDrawable(gifbyte);
							} catch (IOException e) {
								e.printStackTrace();
							}
							holder.iv_content.setImageDrawable(gifDrawable);
						} else {
							Bitmap gifBitmap = BitmapFactory.decodeByteArray(gifbyte,
									0, gifbyte.length);
							holder.iv_content.setImageBitmap(gifBitmap);
						}

					};

				}.execute();*/

			}
			
			
//			holder.tv_content.setText(squareFoundModel.getContent());
			holder.tv_content.setText(squareFoundModel.getTitle());

			int readCount = (int) (squareFoundModel.getUpCount()
					+ squareFoundModel.getUpCount2()
					+ squareFoundModel.getUpCount3() + squareFoundModel
					.getUpCount4());
			holder.tv_read.setText((int) squareFoundModel.getViewCount() + "");
			
			holder.tv_collection.setText(readCount + "个朋友推荐!");
			// holder.tv_collection.setText(squareFoundModel.getCommentCount() +
			// "个朋友推荐!");
			// holder.ll_comment
			holder.btn_comment.setText("评论 " + (squareFoundModel.getCommentCount()>0?squareFoundModel.getCommentCount():""));

			// init 监听
			initListener(holder, position,squareFoundModel);
			
			
			if (squareFoundModel.isHasUp()) {
				holder.view_meng.setAnim(false);
				holder.view_tian.setAnim(false);
				holder.view_shuai.setAnim(false);
				holder.view_jia.setAnim(false);
			} else {
				holder.view_meng.setAnim(true);
				holder.view_tian.setAnim(true);
				holder.view_shuai.setAnim(true);
				holder.view_jia.setAnim(true);
			}
			
			//热门回复
			initHotComments(holder,squareFoundModel);

		}

		return convertView;
	}


	private void initHotComments(ViewHolder holder,
			SquareFoundModel squareFoundModel) {
		// TODO Auto-generated method stub
		ArrayList<HotComment> hotCommments = squareFoundModel.getHotComments();
		
		if (hotCommments!=null && hotCommments.size()>0) {
			holder.ll_comment.setVisibility(View.VISIBLE);
			if (hotCommments.size() == 1) {
				holder.ll_comment_item1.setVisibility(View.VISIBLE);
				holder.ll_comment_item2.setVisibility(View.GONE);
				
				holder.tv_comment_nickname1.setText(hotCommments.get(0).getUserNickName());
				holder.tv_comment_content1.setText(hotCommments.get(0).getContent());
			} else {
				holder.ll_comment_item1.setVisibility(View.VISIBLE);
				holder.ll_comment_item2.setVisibility(View.VISIBLE);
				
				holder.tv_comment_nickname1.setText(hotCommments.get(0).getUserNickName());
				holder.tv_comment_content1.setText(hotCommments.get(0).getContent());
				
				holder.tv_comment_nickname2.setText(hotCommments.get(1).getUserNickName());
				holder.tv_comment_content2.setText(hotCommments.get(1).getContent());
			}
		} else {
			holder.ll_comment.setVisibility(View.GONE);
		}
	}

	private void initPopAnimView(ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		SquareFoundModel squareFoundModel = listData.get(position);
		holder.view_meng.setStaticImage(R.drawable.a0);
		holder.view_meng.setAnimId(R.anim.anim_meng);
		holder.view_meng.setDrawableAnimId(R.anim.anim_meng_drawable);
		holder.view_meng.setTitle(context.getResources().getString(
				R.string.anim_meng));

		holder.view_tian.setStaticImage(R.drawable.b0);
		holder.view_tian.setAnimId(R.anim.anim_meng);
		holder.view_tian.setDrawableAnimId(R.anim.anim_tian_drawable);
		holder.view_tian.setTitle(context.getResources().getString(
				R.string.anim_tian));

		holder.view_shuai.setStaticImage(R.drawable.c0);
		holder.view_shuai.setAnimId(R.anim.anim_meng);
		holder.view_shuai.setDrawableAnimId(R.anim.anim_shuai_drawable);
		holder.view_shuai.setTitle(context.getResources().getString(
				R.string.anim_shuai));

		holder.view_jia.setStaticImage(R.drawable.d0);
		holder.view_jia.setAnimId(R.anim.anim_meng);
		holder.view_jia.setDrawableAnimId(R.anim.anim_jia_drawable);
		holder.view_jia.setTitle(context.getResources().getString(
				R.string.anim_jia));
	}

	private void initListener(final ViewHolder holder, final int position,final SquareFoundModel squareFoundModel) {
		// TODO Auto-generated method stub
		holder.ll_user_info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				ToastUtils.showCustomToast("sss");
				Bundle bundle = new Bundle();
				bundle.putLong(Constant.IntentKey.userID, squareFoundModel.getUserId());
				Utily.go2Activity(context, MyInfoActivity.class,bundle);
			}
		});
		
		holder.ib_user_operate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!DataManager.getInstance().isLogin) {
					Utily.go2Activity(context, LoginActivity.class);
				} else {
					//再次判断是否登录
					if (DataManager.getInstance().isLogin && DataManager.getInstance().userModel.IsVip==1) {//VIP
						popMenu.show(v, position);
					} else {//普通用户
						popMenu.setShowMore(false);
						popMenu.show(v, position);
					}
				}
				
				
				 
			}
		});

		holder.view_meng.setPopupAnimViewListener(new PopupAnimViewListener() {

			@Override
			public void popupAnimViewClick(View v) {
				// TODO Auto-generated method stub
				zanEvent(0,position);
				
			}
		});
		holder.view_tian.setPopupAnimViewListener(new PopupAnimViewListener() {

			@Override
			public void popupAnimViewClick(View v) {
				// TODO Auto-generated method stub
				zanEvent(1,position);
			}
		});
		holder.view_shuai.setPopupAnimViewListener(new PopupAnimViewListener() {

			@Override
			public void popupAnimViewClick(View v) {
				// TODO Auto-generated method stub
				zanEvent(2,position);
			}
		});
		holder.view_jia.setPopupAnimViewListener(new PopupAnimViewListener() {

			@Override
			public void popupAnimViewClick(View v) {
				// TODO Auto-generated method stub
				zanEvent(3,position);
			}
		});
		holder.btn_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!DataManager.getInstance().isLogin) {
					Utily.go2Activity(context, LoginActivity.class);
					return;
				}
				squareFoundListViewAdapterListener
				.squareFoundPopupAnimViewClick(4, position);
			}
		});
	}
	
	public void zanEvent(int index,int position){
//		if (!DataManager.getInstance().isLogin) {
//			Utily.go2Activity(context, LoginActivity.class);
//			return;
//		}
		squareFoundListViewAdapterListener
				.squareFoundPopupAnimViewClick(3, position);
	}

	public class ViewHolder {
		LinearLayout ll_user_info;
		ImageView iv_user_logo;// 用户图标
		TextView tv_nickname;// 昵称
		TextView tv_time;// 发表时间
		ImageButton ib_user_operate;// 弹出收藏等按钮
		ImageView iv_content;// 内容图片
		//GifImageView iv_content1;
		TextView tv_content;// 文章内容
		// Button btn_meng;// 萌萌哒
		// Button btn_tian;// 舔一下
		// Button btn_shuai;// 帅炸天
		// Button btn_jia;// 嫁给他
		Button btn_comment;// 评论

		PopAnimView view_meng;
		PopAnimView view_tian;
		PopAnimView view_jia;
		PopAnimView view_shuai;

		TextView tv_read;// 阅读数
		TextView tv_collection;// 收藏数

		LinearLayout ll_comment;// 评论布局
		LinearLayout ll_comment_item1;
		LinearLayout ll_comment_item2;
		TextView tv_comment_content1;
		TextView tv_comment_content2;
		TextView tv_comment_nickname1;
		TextView tv_comment_nickname2;
	}

	public void refershData(ArrayList<SquareFoundModel> contents) {
		// TODO Auto-generated method stub
		listData = contents;
		notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public void setSquareFoundListViewAdapterListener(
			SquareFoundListViewAdapterListener squareFoundListViewAdapterListener) {
		this.squareFoundListViewAdapterListener = squareFoundListViewAdapterListener;
	}

	public interface SquareFoundListViewAdapterListener {
//		public void squareFoundListItemClick(int index);

		// 点击右上角弹出菜单元素
		public void suareFoundPopMenuClickIndex(int index, int position);

		// 点击底部弹窗按钮
		public void squareFoundPopupAnimViewClick(int index, int position);
	}

	@Override
	public void popMenuClickIndex(int index, int position) {
		// TODO Auto-generated method stub
//		ToastUtils.showCustomToast(index + "");
		squareFoundListViewAdapterListener.suareFoundPopMenuClickIndex(index,
				position);
	}
}
