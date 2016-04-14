package com.mci.firstidol.activity;

import android.content.Intent;

import com.mci.firstidol.R;
import com.mci.firstidol.adapter.MyCollectionAdapter;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.base.BaseListFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.fragment.CollectionListFragment;
import com.mci.firstidol.fragment.CommentListFragment;
import com.mci.firstidol.fragment.NoticeListFragment;
import com.mci.firstidol.fragment.PersonalFragment;
import com.mci.firstidol.fragment.StarListFragment;
import com.mci.firstidol.view.ToastUtils;

public class OnlyFragemntActivity extends BaseActivity {

	private BaseFragment baseFragment;// 列表的fragment
	private String typeCode;// 类型
	private String titleInfo;// 标题

	private boolean isEdit = false;// 是都处于编辑状态

	private EditInterface editInterface;// 编辑回调接口

	@Override
	protected int getViewId() {
		initDataInfo();
		return R.layout.activity_frame_list;
	}

	@Override
	protected void initNavBar() {
		setTitle(titleInfo);
		if (Constant.IntentValue.ACTIVITY_STAR.equals(typeCode)) {
			setRightBtnBackgroundResource(R.drawable.edit);
		}
	}

	@Override
	public void rightNavClick() {
		if (editInterface.isEdit(!isEdit)) {
			if (isEdit) {
				setRightBtnBackgroundResource(R.drawable.edit);
				isEdit = false;
			} else {
				setRightBtnBackgroundResource(R.drawable.done);
				isEdit = true;
			}
		}else{
			ToastUtils.showCustomToast(context, "还未获取数据");
		}
	}

	/**
	 * 初始化数据
	 */
	public void initDataInfo() {
		Intent intent = getIntent();
		if (intent != null) {
			typeCode = intent.getStringExtra(Constant.IntentKey.typeCode);
		}

		if (Constant.IntentValue.ACTIVITY_COLLECTION.equals(typeCode)) {
			titleInfo = context.getResources()
					.getString(R.string.my_collection);
			baseFragment = new CollectionListFragment();
		} else if (Constant.IntentValue.ACTIVITY_COMMENT.equals(typeCode)) {
			titleInfo = context.getResources().getString(R.string.my_comment);
			baseFragment = new CommentListFragment();
		} else if (Constant.IntentValue.ACTIVITY_NOTICE.equals(typeCode)) {
			titleInfo = context.getResources().getString(R.string.my_notice);
			baseFragment = new NoticeListFragment();
		} else if (Constant.IntentValue.ACTIVITY_STAR.equals(typeCode)) {
			titleInfo = context.getResources().getString(R.string.my_star);
			baseFragment = new StarListFragment();
			editInterface = (EditInterface) baseFragment;
		} else if (Constant.IntentValue.ACTIVITY_PERSONAL.equals(typeCode)) {
			titleInfo = context.getResources().getString(R.string.personal);
			baseFragment = new PersonalFragment();
		}
	}

	@Override
	protected void initView() {
		// view的替换
		if (baseFragment != null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.my_data, baseFragment).commit();
		}

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

	}

	/**
	 * 是否删除
	 * 
	 * @author wanghaixiao
	 * 
	 */
	public interface EditInterface {
		boolean isEdit(boolean isEdit);
	}

}
