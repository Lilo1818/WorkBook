package com.mci.firstidol.activity;


import java.util.ArrayList;

import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.SquareModel;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.SquareModel.LiveAnnex;
import com.mci.firstidol.model.SquareModel.SquareLiveChatModel;
import com.mci.firstidol.model.SquareRequest.ArticleComment;
import com.mci.firstidol.model.SquareRequest.CommentAction;
import com.mci.firstidol.model.SquareRequest.SquareFoundDetaiCommentAddRequest;
import com.mci.firstidol.model.SquareRequest.SquareFoundDetaiCommentLikelRequest;
import com.mci.firstidol.model.SquareRequest.SquareLiveChatSaveRequest;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.utils.LogUtils;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.NumberLimitEditText;
import com.mci.firstidol.view.ToastUtils;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.mci.firstidol.R;

import de.greenrobot.event.EventBus;

public class SquareFoundDetailAddReplyActivity extends BaseActivity{

	
	
	private NumberLimitEditText et_input;
	private long articleID,userRole,chatId;
	private String from;
	

	@Override
	protected void initNavBar() {
		// TODO Auto-generated method stub
		setTitle(getResources().getString(R.string.square_add_reply_title));
		setRightBtnBackgroundDrawable(getResources().getDrawable(R.drawable.square_found_send));
	}

	protected void findViewById() {
		// TODO Auto-generated method stub
		et_input = (NumberLimitEditText) findViewById(R.id.et_input);
	}

	protected void initView() {
		// TODO Auto-generated method stub
		et_input.setTextCount(140);
		et_input.setToastLog(getResources().getString(R.string.square_add_reply_hint));
	}


	protected void initData() {
		// TODO Auto-generated method stub
		articleID = getIntent().getExtras().getLong(Constant.IntentKey.articleID);
		from = getIntent().getExtras().getString(Constant.IntentKey.from);
		userRole = getIntent().getExtras().getLong(Constant.IntentKey.userRole);
		chatId = getIntent().getExtras().getLong(Constant.IntentKey.chatID);
	}

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.layout_fragment_square_found_detail_add_reply;
	}

	@Override
	public void rightNavClick() {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(et_input.getText())) {
			ToastUtils.showCustomToast("请输入评价内容!");
			return;
		} else if (et_input.getText().length()<5) {
			ToastUtils.showCustomToast("内容不能低于5个字符");
			return;
		}
		if ("Live".equals(from)) {//直播
			obtainChatSaveRequest(null);
		} else {
			obtainFoundDetailCommentAddRequest();
		}
		
	}

	
	/**
	 * 广场-发现详情-添加评论
	 * @param isCancelFav
	 */
	public void obtainFoundDetailCommentAddRequest(){
		SquareFoundDetaiCommentAddRequest request = SquareRequest.squareFoundDetaiCommentAddRequest();
		request.requestMethod = Constant.Request_POST;
		
		ArticleComment articleComment = SquareRequest.articleComment();
		articleComment.ArticleId = articleID;
		articleComment.ParentId = 0;
		articleComment.Content = et_input.getText().toString();
		articleComment.CommentType = 0;
		articleComment.UserNickName = dataManager.userModel.NickName;
		articleComment.UserAvatar = dataManager.userModel.Avatar;
				
		request.articleComment = articleComment;
		//请求地址
		String requestID = Constant.RequestContstants.Request_Square_ArticleComment_Add;		
		doAsync(true, requestID, request, new Callback<Exception>() {
			
			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {
			
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				ToastUtils.showPointToast("5");//回复加5分
//				ToastUtils.showCustomToast("发布成功");
				//成功时
				changeData();
				Constant.isComment = true;
				finishActivity(SquareFoundDetailAddReplyActivity.this);
			}
		});
	}
	
	/**
	 * 更新数据请求
	 */
	public void changeData(){
		AnyEventType type = new AnyEventType(Constant.Config.UPDATE_COMMENT_INFO,
				AnyEventType.Type_Default);
		EventBus.getDefault().post(type);
	}
	
	
	/**
     * 保存聊天信息
     * @param attachments
     */
	protected void obtainChatSaveRequest(ArrayList<LiveAnnex> attachments) {
		// TODO Auto-generated method stub
		SquareLiveChatSaveRequest request = SquareRequest.squareLiveChatSaveRequest();// 其它参数需要继承此类
		request.requestMethod = Constant.Request_POST;
		request.liveChat = initLiveChatModel();
		request.liveAnnexList = attachments;
		String requestID = Constant.RequestContstants.Request_LiveChat_Save;
		doAsync(true, requestID, request, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				 ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				LogUtils.i(result);
				ToastUtils.showPointToast("5");//回复加5分
//				ToastUtils.showCustomToast("发布成功");
				finishActivity(SquareFoundDetailAddReplyActivity.this);
			}
		});
	}

	private SquareLiveChatModel initLiveChatModel() {
		// TODO Auto-generated method stub
		SquareLiveChatModel squareLiveChatModel = new SquareModel().new SquareLiveChatModel();
		squareLiveChatModel.setArticleId(articleID);
		squareLiveChatModel.setChatContent(et_input.getText().toString());
		squareLiveChatModel.setUserId(dataManager.userModel.UserId);
		squareLiveChatModel.setUserRole(userRole);
		squareLiveChatModel.setParentId(chatId);
		squareLiveChatModel.setUserAvatar(dataManager.userModel.Avatar);
		squareLiveChatModel.setUserNickName(dataManager.userModel.NickName);
		
		
		return squareLiveChatModel;
	}

}
