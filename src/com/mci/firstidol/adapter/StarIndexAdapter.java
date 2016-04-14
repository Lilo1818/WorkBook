package com.mci.firstidol.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;
import com.mci.firstidol.adapter.StarTripAdapter.ViewHolder;
import com.mci.firstidol.base.MsBaseAdapter;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.model.HotComment;
import com.mci.firstidol.model.StarIndexModel;
import com.mci.firstidol.model.SquareModel.SquareFoundModel;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.DateHelper;
import com.mci.firstidol.utils.DeviceUtils;
import com.mci.firstidol.utils.DisplayUtil;
import com.mci.firstidol.utils.SyncImageLoader;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.PopAnimView;
import com.mci.firstidol.view.PopMenu;
import com.mci.firstidol.view.RoundedImageView;
import com.mci.firstidol.view.PopAnimView.PopupAnimViewListener;
import com.mci.firstidol.view.PopMenu.PopMenuListener;
import com.mci.firstidol.view.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class StarIndexAdapter extends MsBaseAdapter implements OnClickListener,
		PopMenuListener {

	private Handler handler;
	private boolean isShowRight;
	private PopMenu popMenu;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
	private StarListAdapterListener starListAdapterListener;
	private Context context;
	//public ParseJson parseJson;// json解析工具
	private boolean is_masters;// 用户是否是版主

	public StarIndexAdapter(Context context, List<Object> modelList,
			ListView listView, Handler handler,
			StarListAdapterListener starListAdapterListener) {
		super(context, modelList, listView);
		this.handler = handler;
		this.context = context;
		popMenu = new PopMenu(context);
		// 菜单项点击监听器
		popMenu.setPopMenuListener(this);
		this.starListAdapterListener = starListAdapterListener;
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.item_square_found,
					parent, false);
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

			holder.ll_comment = (android.widget.LinearLayout) convertView
					.findViewById(R.id.ll_comment);
			holder.ll_comment_item1 = (LinearLayout) convertView
					.findViewById(R.id.ll_comment_list_item1);
			holder.ll_comment_item2 = (LinearLayout) convertView
					.findViewById(R.id.ll_comment_list_item2);
			holder.tv_comment_content1 = (TextView) convertView
					.findViewById(R.id.tv_comment_content1);
			holder.tv_comment_content2 = (TextView) convertView
					.findViewById(R.id.tv_comment_content2);
			holder.tv_comment_nickname1 = (TextView) convertView
					.findViewById(R.id.tv_comment_nickname1);
			holder.tv_comment_nickname2 = (TextView) convertView
					.findViewById(R.id.tv_comment_nickname2);

			convertView.setTag(holder);

			initPopAnimView(holder, position);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	private void initPopAnimView(ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		isMasterRequest();
		holder.view_meng.setStaticImage(R.drawable.a0);
		holder.view_meng.setAnimId(R.anim.anim_meng);
		holder.view_meng.setDrawableAnimId(R.anim.anim_meng_drawable);
		holder.view_meng.setTitle(mContext.getResources().getString(
				R.string.anim_meng));

		holder.view_tian.setStaticImage(R.drawable.b0);
		holder.view_tian.setAnimId(R.anim.anim_meng);
		holder.view_tian.setDrawableAnimId(R.anim.anim_tian_drawable);
		holder.view_tian.setTitle(mContext.getResources().getString(
				R.string.anim_tian));

		holder.view_shuai.setStaticImage(R.drawable.c0);
		holder.view_shuai.setAnimId(R.anim.anim_meng);
		holder.view_shuai.setDrawableAnimId(R.anim.anim_shuai_drawable);
		holder.view_shuai.setTitle(mContext.getResources().getString(
				R.string.anim_shuai));

		holder.view_jia.setStaticImage(R.drawable.d0);
		holder.view_jia.setAnimId(R.anim.anim_meng);
		holder.view_jia.setDrawableAnimId(R.anim.anim_jia_drawable);
		holder.view_jia.setTitle(mContext.getResources().getString(
				R.string.anim_jia));
	}

	private void initListener(ViewHolder holder, final int position) {
		// TODO Auto-generated method stub
		holder.ib_user_operate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 再次判断是否登录
				if (DataManager.getInstance().isLogin
						&& DataManager.getInstance().userModel.IsVip == 1) {// VIP
					popMenu.show(v, position);
				} else {// 普通用户
					//isMasterRequest();
					StarIndexModel starIndexModel = (StarIndexModel) modelList
							.get(position);
					/*Toast.makeText(mContext, "值为+"+starIndexModel.UserId, 0).show();
					Toast.makeText(mContext, "值2为+"+DataManager.getInstance().userModel.UserId, 0).show();*/
					//Toast.makeText(mContext, "值为+"+is_master, 0).show();
					if (starIndexModel.UserId == DataManager.getInstance().userModel.UserId) {
						if(is_masters==false){
							popMenu.setShowMore(false);
							popMenu.setShowReport(false);
							popMenu.show(v, position);
							//ToastUtils.show(mContext, "非版主1");
						}else{
							popMenu.setShowMore(true);
							popMenu.setShowReport(false);
							popMenu.show(v, position);
						}
					} else {
						if(is_masters==false){
							popMenu.setShowMore(false);
							popMenu.setShowReport(true);
							popMenu.show(v, position);
							//ToastUtils.show(mContext, "非版主2");
						}else{
							popMenu.setShowMore(true);
							popMenu.show(v, position);
						}
					}
					
				}

			}
		});

		holder.view_meng.setPopupAnimViewListener(new PopupAnimViewListener() {

			@Override
			public void popupAnimViewClick(View v) {
				starListAdapterListener.starPopupAnimViewClick(0, position);
			}
		});
		holder.view_tian.setPopupAnimViewListener(new PopupAnimViewListener() {

			@Override
			public void popupAnimViewClick(View v) {
				starListAdapterListener.starPopupAnimViewClick(1, position);
			}
		});
		holder.view_shuai.setPopupAnimViewListener(new PopupAnimViewListener() {

			@Override
			public void popupAnimViewClick(View v) {
				starListAdapterListener.starPopupAnimViewClick(2, position);
			}
		});
		holder.view_jia.setPopupAnimViewListener(new PopupAnimViewListener() {

			@Override
			public void popupAnimViewClick(View v) {
				starListAdapterListener.starPopupAnimViewClick(3, position);
			}
		});
		holder.btn_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				starListAdapterListener.starPopupAnimViewClick(4, position);
			}
		});
	}
	//判断时候是版主的方法
	/**
	 * 是否是版主
	 */
	public void isMasterRequest() {
		String userId = null;
		String starId = null;
		if (DataManager.userModel != null) {
			userId = String.valueOf(DataManager.userModel.UserId);
		}
		if (Constant.starModel != null) {
			starId = String.valueOf(Constant.starModel.starId);
		}
		String processURL = Constant.RequestContstants.Request_isMaster
				+ starId + "/" + userId;
		ConnectionService.getInstance().serviceConnUseGet(context, processURL,
				null, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						//has_check_master = true;
						try {
							JSONObject jsonObject = new JSONObject(Result);
							is_masters = jsonObject.getBoolean("result");
							//boolean check = parseJson.isCommon(jsonObject);
							//Toast.makeText(mContext, "值+"+is_masters, Toast.LENGTH_SHORT).show();
						} catch (Exception e) {
							e.printStackTrace();
							//Toast.makeText(mContext, "异常", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void getError() {

					}
				});
	}
	
	
	
	
	
	
	
	@Override
	public void bindData(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = (ViewHolder) convertView.getTag();
		StarIndexModel starIndexModel = (StarIndexModel) modelList
				.get(position);

		ImageLoader.getInstance().displayImage(starIndexModel.UserAvatar,
				holder.iv_user_logo, BaseApp.circleOptions);
		holder.tv_nickname.setText(starIndexModel.UserNickName);
		holder.tv_time.setText(DateHelper.getStringFromDateStr(
				starIndexModel.PublishDate, "MM-dd HH:mm"));

		// 设置图片的位置
		MarginLayoutParams margin9 = new MarginLayoutParams(
				holder.iv_content.getLayoutParams());
		LinearLayout.LayoutParams layoutParams9 = new LinearLayout.LayoutParams(
				margin9);

		layoutParams9.height = starIndexModel.IcoWidth == 0 ? DisplayUtil
				.dip2px(mContext, 200)
				: (int) (DeviceUtils.getWidthPixels((Activity) mContext)
						* starIndexModel.IcoHeight / starIndexModel.IcoWidth);// 设置图片的高度

		layoutParams9.width = DeviceUtils.getWidthPixels((Activity) mContext); // 设置图片的宽度
		holder.iv_content.setLayoutParams(layoutParams9);

		if (starIndexModel.IcoWidth == 0 || starIndexModel.IcoHeight == 0) {
			holder.iv_content.setScaleType(ScaleType.CENTER);
		}
		ImageLoader.getInstance().displayImage(starIndexModel.Ico,
				holder.iv_content);
		;
		if(starIndexModel.Title==null||starIndexModel.Title.equals("")){
			holder.tv_content.setText(starIndexModel.Content);
		}else{
			holder.tv_content.setText(starIndexModel.Title);
		}
		

		int readCount = (int) (starIndexModel.UpCount + starIndexModel.UpCount2
				+ starIndexModel.UpCount3 + starIndexModel.UpCount4);
		holder.tv_read.setText((int) starIndexModel.ViewCount + "");

		holder.tv_collection.setText(readCount + "个朋友推荐!");
		holder.btn_comment
				.setText("评论 "
						+ (starIndexModel.CommentCount > 0 ? starIndexModel.CommentCount
								: ""));

		// init 监听
		initListener(holder, position);

		if (starIndexModel.HasStore) {
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

		// 热门回复
		initHotComments(holder, starIndexModel);

	}

	/**
	 * 初始化热门回复
	 * 
	 * @param holder
	 * @param starIndexModel
	 */
	private void initHotComments(ViewHolder holder,
			StarIndexModel starIndexModel) {
		List<HotComment> hotCommments = starIndexModel.HotComments;

		if (hotCommments != null && hotCommments.size() > 0) {
			holder.ll_comment.setVisibility(View.VISIBLE);
			if (hotCommments.size() == 1) {
				holder.ll_comment_item1.setVisibility(View.VISIBLE);
				holder.ll_comment_item2.setVisibility(View.GONE);

				holder.tv_comment_nickname1.setText(hotCommments.get(0)
						.getUserNickName());
				holder.tv_comment_content1.setText(hotCommments.get(0)
						.getContent());
			} else {
				holder.ll_comment_item1.setVisibility(View.VISIBLE);
				holder.ll_comment_item2.setVisibility(View.VISIBLE);

				holder.tv_comment_nickname1.setText(hotCommments.get(0)
						.getUserNickName());
				holder.tv_comment_content1.setText(hotCommments.get(0)
						.getContent());

				holder.tv_comment_nickname2.setText(hotCommments.get(1)
						.getUserNickName());
				holder.tv_comment_content2.setText(hotCommments.get(1)
						.getContent());
			}
		} else {
			holder.ll_comment.setVisibility(View.GONE);
		}
	}

	@Override
	public void bindEvent(int position, View convertView, ViewGroup parent) {

	}

	public class ViewHolder {
		ImageView iv_user_logo;// 用户图标
		TextView tv_nickname;// 昵称
		TextView tv_time;// 发表时间
		ImageButton ib_user_operate;// 弹出收藏等按钮
		ImageView iv_content;// 内容图片
		TextView tv_content;// 文章内容
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

	public interface StarListAdapterListener {
		// 点击右上角弹出菜单元素
		public void starPopMenuClickIndex(int index, int position);

		// 点击底部弹窗按钮
		public void starPopupAnimViewClick(int index, int position);
	}

	@Override
	public void popMenuClickIndex(int index, int position) {
		starListAdapterListener.starPopMenuClickIndex(index, position);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
