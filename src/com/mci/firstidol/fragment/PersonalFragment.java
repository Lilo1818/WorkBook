package com.mci.firstidol.fragment;

import java.util.HashMap;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.shinsoft.query.Delete;

import com.mci.firstidol.R;
import com.mci.firstidol.activity.LoginActivity;
import com.mci.firstidol.activity.MyInfoActivity;
import com.mci.firstidol.activity.MyInfoEditActivity;
import com.mci.firstidol.activity.OnlyFragemntActivity;
import com.mci.firstidol.activity.SettingActivity;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.model.StarModel;
import com.mci.firstidol.model.UserModel;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.PreferencesUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.RoundedImageView;

import de.greenrobot.event.EventBus;

public class PersonalFragment extends BaseFragment implements OnClickListener {

	private UserModel userModel;// 用户对象

	private RoundedImageView userIcon;// 用户头像
	private TextView text_name;// 用户名称
	private TextView text_info;// 用户信息
	private ImageView image_edit;// 用户编辑图片
	private TextView text_integration;// 用户积分
	private TextView text_dynamic;// 我的动态
	private TextView text_follow;// 关注
	private TextView text_follower;// 粉丝
	
	private Button btn_logout;//退出登录
	private ImageView btn_login;//登录

	private LinearLayout layout_content;//内容页
	private RelativeLayout layout_nodata;//空数据页面
	private RelativeLayout layout_star;// 追星
	private RelativeLayout layout_collection;// 收藏
	private RelativeLayout layout_Comment;// 评论
	private RelativeLayout layout_notice;// 通知
	private RelativeLayout layout_setting;// 设置
	
	private LinearLayout layout_dynamic;//动态布局 
	private LinearLayout layout_follow;//关注布局
	private LinearLayout layout_follower;//粉丝布局

	@Override
	protected int getViewId() {
		return R.layout.fragment_personal;
	}

	@Override
	protected void initView() {
		userIcon = (RoundedImageView) findViewById(R.id.icon_mine);

		text_name = (TextView) findViewById(R.id.text_name);
		text_info = (TextView) findViewById(R.id.text_content);

		image_edit = (ImageView) findViewById(R.id.image_edit);

		text_integration = (TextView) findViewById(R.id.text_integration);
		text_dynamic = (TextView) findViewById(R.id.text_dynamic);
		text_follow = (TextView) findViewById(R.id.text_follow);
		text_follower = (TextView) findViewById(R.id.text_follower);

		layout_content = (LinearLayout) findViewById(R.id.layout_content);
		layout_nodata = (RelativeLayout) findViewById(R.id.layout_nodata);
		layout_star = (RelativeLayout) findViewById(R.id.layout_star);
		layout_collection = (RelativeLayout) findViewById(R.id.layout_collection);
		layout_Comment = (RelativeLayout) findViewById(R.id.layout_comment);
		layout_notice = (RelativeLayout) findViewById(R.id.layout_notice);
		layout_setting = (RelativeLayout) findViewById(R.id.layout_setting);
		
		layout_dynamic = (LinearLayout) findViewById(R.id.layout_dynamic);
		layout_follow = (LinearLayout) findViewById(R.id.layout_follow);
		layout_follower = (LinearLayout) findViewById(R.id.layout_follower);
		
		btn_logout = (Button) findViewById(R.id.btn_logout);
		btn_login = (ImageView) findViewById(R.id.btn_login);

		image_edit.setOnClickListener(this);
		layout_star.setOnClickListener(this);
		layout_collection.setOnClickListener(this);
		layout_Comment.setOnClickListener(this);
		layout_notice.setOnClickListener(this);
		layout_setting.setOnClickListener(this);
		btn_logout.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		userIcon.setOnClickListener(this);
		
		layout_dynamic.setOnClickListener(this);
		layout_follow.setOnClickListener(this);
		layout_follower.setOnClickListener(this);

		initData();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(DataManager.isLogin){
			showContentView();
			if(Constant.is_userinfo_change){
				initData();
				Constant.is_userinfo_change = true;
			}
		}else{
			showNodataView();
		}
	}

	/**
	 * 初始化数据
	 */
	public void initData() {
		userModel = DataManager.userModel;
		if (userModel != null) {

			// 头像
			loadImage(userModel.Avatar, userIcon);

			// 名称
			text_name.setText(userModel.NickName);

			// 信息
			String info = userModel.Signature;
			if (info != null && !info.equals("")) {
				text_info.setText(info);
			} else {
				text_info.setText(activity.getResources().getString(
						R.string.info_content));
			}

			// 积分
			text_integration.setText(activity.getResources().getString(
					R.string.integration)
					+ "\r\n" + userModel.Points);

			// 粉丝数
			text_follower.setText(userModel.FansCount+"");

			// 动态数
			text_dynamic.setText(userModel.DynamicCount+"");

			// 关注数
			text_follow.setText(userModel.UserFollowCount+"");

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_star:
			HashMap<String, String> pa = new HashMap<String, String>();
			pa.put(Constant.IntentKey.typeCode,
					Constant.IntentValue.ACTIVITY_STAR);

			Utily.go2Activity(activity, OnlyFragemntActivity.class, pa, null);
			
			break;
		case R.id.layout_collection:
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(Constant.IntentKey.typeCode,
					Constant.IntentValue.ACTIVITY_COLLECTION);

			Utily.go2Activity(activity, OnlyFragemntActivity.class, params, null);
			
			break;
		case R.id.layout_comment:
			HashMap<String, String> param = new HashMap<String, String>();
			param.put(Constant.IntentKey.typeCode,
					Constant.IntentValue.ACTIVITY_COMMENT);

			Utily.go2Activity(activity, OnlyFragemntActivity.class, param, null);
			
			break;
		case R.id.layout_notice:
			HashMap<String, String> para = new HashMap<String, String>();
			para.put(Constant.IntentKey.typeCode,
					Constant.IntentValue.ACTIVITY_NOTICE);

			Utily.go2Activity(activity, OnlyFragemntActivity.class, para, null);
			
			break;
		case R.id.layout_setting:
			Utily.go2Activity(activity, SettingActivity.class, null, null);
			
			break;
		case R.id.image_edit:
			Utily.go2Activity(activity, MyInfoEditActivity.class, null,null);
			
			break;
		case R.id.btn_logout:
			initLogoutData();
			showNodataView();
			sendLoginOutNotice();
			break;
		case R.id.btn_login:
			//getActivity().finish();
			Utily.go2Activity(activity, LoginActivity.class);
			break;
		case R.id.icon_mine:
			Utily.go2Activity(activity, MyInfoEditActivity.class, null,null);
			break;
		case R.id.layout_dynamic:
			HashMap<String, String> dynaParam = new HashMap<String,String>();
			dynaParam.put(Constant.IntentKey.typeCode, Constant.IntentValue.ACTIVITY_DYNAMIC);
			Utily.go2Activity(activity, MyInfoActivity.class, dynaParam, null);
			break;
		case R.id.layout_follow:
			HashMap<String, String> followParam = new HashMap<String,String>();
			followParam.put(Constant.IntentKey.typeCode, Constant.IntentValue.ACTIVITY_FOLLOW);
			Utily.go2Activity(activity, MyInfoActivity.class, followParam, null);
			break;
		case R.id.layout_follower:
			HashMap<String, String> followerParam = new HashMap<String,String>();
			followerParam.put(Constant.IntentKey.typeCode, Constant.IntentValue.ACTIVITY_DYNAMIC);
			Utily.go2Activity(activity, MyInfoActivity.class, followerParam, null);
			break;
		default:
			break;
		}

	}
	
	/**
	 * 登出消息
	 */
	public void sendLoginOutNotice(){
		AnyEventType type = new AnyEventType(Constant.Config.LOGINOUT, AnyEventType.Type_Default);
        EventBus.getDefault().post(type);
	}
	
	/**
	 * 显示内容页
	 */
	public void showContentView(){
		layout_nodata.setVisibility(View.GONE);
		layout_content.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 显示空页面
	 */
	public void showNodataView(){
		layout_nodata.setVisibility(View.VISIBLE);
		layout_content.setVisibility(View.GONE);
	}

	private void initLogoutData() {
		// TODO Auto-generated method stub
		//清除登录相关数据
		DataManager.getInstance().userModel = null;
		DataManager.getInstance().isLogin = false;
		Constant.starModel = null;
		
		PreferencesUtils.putComplexObject(getActivity().getApplicationContext(), "UserModel", null);
		PreferencesUtils.putBoolean(getActivity().getApplicationContext(), "IsLogin", false);
		
		//清除第三方登录凭证
		Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
		if (wechat!= null && wechat.isValid()) {
			wechat.removeAccount();
		}
		
		Platform qzone = ShareSDK.getPlatform(QZone.NAME);
		if (qzone!= null && wechat.isValid()) {
			qzone.removeAccount();
		}
		
		Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
		if (sina!= null && wechat.isValid()) {
			sina.removeAccount();
		}
		
		deleteStarFromLocal();
		
	}
	
	@Override
	public void onEventMainThread(AnyEventType event) {
		if(event!=null&&Constant.Config.UPDATE_FOLLOW.equals(event.message)){
			initData();
		}
	}
	
	/**
	 * 删除缓存的明星数据
	 */
	public void deleteStarFromLocal(){
		new Delete().from(StarModel.class).execute();
	}


	@Override
	protected void findViewById() {
		
	}

}
