package com.mci.firstidol.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.mci.firstidol.adapter.SquareFoundDetailListViewAdapter;
import com.mci.firstidol.adapter.SquareFoundDetailReplyListViewAdapter;
import com.mci.firstidol.adapter.SquareFoundDetailReplyListViewAdapter.SquareFoundDetailListViewAdapterListener;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.CommentModel;
import com.mci.firstidol.model.SquareModel;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.SquareModel.LiveAnnex;
import com.mci.firstidol.model.SquareModel.SquareLiveChatModel;
import com.mci.firstidol.model.SquareRequest.ArticleComment;
import com.mci.firstidol.model.SquareRequest.CommentAction;
import com.mci.firstidol.model.SquareRequest.SquareFoundDetaiCommentAddRequest;
import com.mci.firstidol.model.SquareRequest.SquareFoundDetaiCommentLikelRequest;
import com.mci.firstidol.model.SquareRequest.SquareLiveChatSaveRequest;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.ProgressActivity;
import com.mci.firstidol.view.ReplyPopupWindow;
import com.mci.firstidol.view.ReplyPopupWindow.OnReplyClickListener;
import com.mci.firstidol.view.ToastUtils;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;

public class SquareFoundDetailReplyActivity extends BaseActivity implements OnItemClickListener,OnClickListener,
SquareFoundDetailListViewAdapterListener,OnReplyClickListener{

	private  PullToRefreshListView mPullRefreshListView;
	private ListView listView;
	private SquareFoundDetailReplyListViewAdapter adapter;
	
	private ImageButton btn_reply;
	
	private ArrayList<CommentModel> commentModels;
	private long articleID;
	
	private ReplyPopupWindow replyWindow;
	private String replyHint;
	private CommentModel selectCommentModel;
	private LinearLayout ll_square_livechat_detail;
	
	private boolean isFirst;
	
	private ProgressActivity progressActivity;
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		
//		super.onCreate(savedInstanceState);
//		
////		setContentLayout(R.layout.layout_fragment_square_found_detail_reply);
//		
//		initNavBar();
//		findViewById();
//		initView();
//		initData();
//	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!isFirst) {
			obtainFoundDetailCommentRequest(true);
		} else {
			isFirst = false;
		}
	}

	protected void initNavBar() {
		// TODO Auto-generated method stub
		setTitle(R.string.comment);
	}

	protected void findViewById() {
		// TODO Auto-generated method stub
		progressActivity = (ProgressActivity) findViewById(R.id.progressActivity);
		progressActivity.showLoading();
		
		ll_square_livechat_detail = (LinearLayout) findViewById(R.id.ll_square_livechat_detail);
		
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		listView = mPullRefreshListView.getRefreshableView();
		
		btn_reply = (ImageButton) findViewById(R.id.btn_reply);
	}

	protected void initView() {
		// TODO Auto-generated method stub
		mPullRefreshListView.setMode(Mode.BOTH);//设置刷新方式，上拉、下拉等
		
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(
						BaseApp.getInstance().getApplicationContext(),
						System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				// 更新最后一次刷新时间
				refreshView.getLoadingLayoutProxy()
						.setLastUpdatedLabel(label);
				
				obtainFoundDetailCommentRequest(true);
				
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(
						BaseApp.getInstance().getApplicationContext(),
						System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				// 更新最后一次刷新时间
				refreshView.getLoadingLayoutProxy()
						.setLastUpdatedLabel(label);
				
				obtainFoundDetailCommentRequest(false);
			}
		});

		// Add an end-of-list listener
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if (mPullRefreshListView.getMode() == Mode.PULL_FROM_START) {
//					ToastUtils.showC(getApplicationContext(), "数据已全部加载");
				}
			}
		});;
		//选择事件
		mPullRefreshListView.setOnItemClickListener(this);
		
		adapter = new SquareFoundDetailReplyListViewAdapter(this, null, false);
		adapter.setSquareFoundDetailListViewAdapterListener(this);
		mPullRefreshListView.setAdapter(adapter);
		
//		View headerView = LayoutInflater.from(this).inflate(R.layout.item_square_found_detail_reply_header, null);
//		listView.addHeaderView(headerView);
		
		btn_reply.setOnClickListener(this);
	}


	protected void initData() {
		// TODO Auto-generated method stub
		isFirst = true;
		articleID = getIntent().getExtras().getLong(Constant.IntentKey.articleID);
		obtainFoundDetailCommentRequest(false);
	}

	public void headerRefresh(){
		mPullRefreshListView.onRefreshComplete();
		mPullRefreshListView.setShowViewWhileRefreshing(true);
		mPullRefreshListView.setCurrentModeRefresh();
		mPullRefreshListView.setRefreshing(true);
	}

	@SuppressWarnings("static-access")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (dataManager.isLogin) {
			selectCommentModel = commentModels.get(position-1);
			replyHint = "@"+ selectCommentModel.getUserNickName();
			popupReplyWindow();
		} else {
			Utily.go2Activity(context, LoginActivity.class);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_reply:
//			startActivity(SquareFoundDetailAddReplyActivity.class);
			Bundle bundle = new Bundle();
			bundle.putLong(Constant.IntentKey.articleID, articleID);
			Utily.go2Activity(this, SquareFoundDetailAddReplyActivity.class,bundle);
			break;

		default:
			break;
		}
	}

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.layout_fragment_square_found_detail_reply;
	}

	@Override
	public void rightNavClick() {
		// TODO Auto-generated method stub
		
	}
	
	private void popupReplyWindow() {
		// TODO Auto-generated method stub
		// 实例化PopupWindow
		if (replyWindow == null) {
			replyWindow = new ReplyPopupWindow(SquareFoundDetailReplyActivity.this);
			replyWindow.setOnReplyClickListener(this);
		}
		replyWindow.setTextHint(replyHint);
		// 显示窗口
		replyWindow.showAtLocation(ll_square_livechat_detail, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		openKeyboard();
	}
	
	@Override
	public void onReplyClick(View v) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(replyWindow.getMessage())) {
			ToastUtils.showCustomToast("回复内容不能为空");
			return;
		} else if(replyWindow.getMessage().length()<5){
			ToastUtils.showCustomToast("长度不能少于5个字符");
			return;
		} else if(replyWindow.getMessage().length()>140){
			ToastUtils.showCustomToast("长度不能大于140个字符");
			return;
		}
		//发表评论
		obtainFoundDetailCommentAddRequest();
	}
	
	
	/**
	 * 广场--详情-评论回复
	 */
	private void obtainFoundDetailCommentAddRequest() {
		// TODO Auto-generated method stub
		SquareFoundDetaiCommentAddRequest request = SquareRequest.squareFoundDetaiCommentAddRequest();
		request.requestMethod = Constant.Request_POST;

		ArticleComment articleComment = SquareRequest.articleComment();
		articleComment.ArticleId = articleID;
//		articleComment.ParentId = selectCommentModel.getCommentId();
		articleComment.ParentId = 0;
		articleComment.Content =  replyHint +" " + replyWindow.getMessage().toString();
		articleComment.CommentType = 0;
		articleComment.UserNickName = dataManager.userModel.NickName;
		articleComment.UserAvatar = dataManager.userModel.Avatar;
		articleComment.AtUserId = selectCommentModel.getUserId();
				
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
				//{"ArticleId":15036.0,"CommentId":65866.0,"CommentType":0.0,"CommentUpCount":0.0,"Content":"@测试小编  666666666666666666","CreateDate":"/Date(1452450041000)/","IP":"114.111.166.34","ParentId":65863.0,"State":0.0,"UserAvatar":"http://qn.ieasy.tv/image/201601/1452158360385_440.png","UserId":0.0,"UserNickName":"测试小编","ChildCount":0.0}
				//成功时
				ToastUtils.showPointToast("5");//回复加5分
				replyWindow.dismiss();
				replyWindow.reset();
				obtainFoundDetailCommentRequest(true);
			}
		});
	}
	
	
	/***
	 * 文章评价列表
	 *  articleId: 	文章 ID
		parentId:父级编号，如果添加二级评论，parentId  则为父级评论编号；一级评论 parentid 为0
		MaxId :  最大 ID,  获取最新时  maxid=0,获取更多数据时请填写最大的 maxid=commentId，
		Count:  获取条数
		commentType:0 文章评论，1：文章图集评论
	 */
	public void obtainFoundDetailCommentRequest(final boolean isRefresh){
		BaseRequest request = new BaseRequest();
		//请求地址
		long maxId = isRefresh?0:((commentModels==null || commentModels.size() == 0)?0:commentModels.get(commentModels.size()-1).getCommentId());
		String requestID = String.format(Constant.RequestContstants.Request_Article_Comment, articleID,0,maxId,Constant.pageNum,0);		
		doAsync(false, requestID, request, new Callback<Exception>() {
			
			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
//				ToastUtils.showCustomToast(e.getLocalizedMessage());
//				mPullRefreshListView.onRefreshComplete();
				String errorMsg = e.getMessage();
				if ("无网络".equals(errorMsg)) {
					refreshComplete(true,true);
				} else {
					refreshComplete(true,false);
				}
			}
		}, new Callback<String>() {
			
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
//				mPullRefreshListView.onRefreshComplete();
				
				Type type = new TypeToken<ArrayList<CommentModel>>() {
				}.getType();
				
				@SuppressWarnings("unchecked")
				ArrayList<CommentModel> tempCommentModels = (ArrayList<CommentModel>) GsonUtils
						.jsonToList(result, type);
				boolean isComplete = false;
				if (!isRefresh) {//上拉加载更多
					if (tempCommentModels.size()<Constant.pageNum) {
						isComplete = true;
					}
					if (commentModels==null) {
						commentModels = new ArrayList<CommentModel>();
					}
					commentModels.addAll(tempCommentModels);
					
					if (isComplete) {
						mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					}
				} else {
					commentModels = tempCommentModels;
					
					if (commentModels != null && commentModels.size() < Constant.pageNum) {
						mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					} else {
						mPullRefreshListView.setMode(Mode.BOTH);
					}
				}
				
				adapter.refershData(commentModels);
				refreshComplete(false,false);
			}
		});
	}

	@Override
	public void squareFoundDetailCommentLikeClick(int position) {
		// TODO Auto-generated method stub
		CommentModel commentModel = commentModels.get(position);
		obtainFoundDetailCommentLikeRequest(commentModel);
	}
	
	/**
	 * 广场-发现详情-评论点赞
	 * @param isCancelFav
	 */
	public void obtainFoundDetailCommentLikeRequest(final CommentModel commentModel){
		SquareFoundDetaiCommentLikelRequest request = SquareRequest.squareFoundDetaiCommentLikelRequest();
		request.requestMethod = Constant.Request_POST;
		
		CommentAction commentAction = SquareRequest.commentAction();
		commentAction.ActType = "1";
		commentAction.ActName = "up";
		commentAction.CommentId = commentModel.getCommentId();
				commentAction.ActUserId = dataManager.isLogin?(dataManager.userModel.UserId+""):"-1";
				commentAction.CommentType = 0;
				
		request.action = commentAction;
		//请求地址
		String requestID = Constant.RequestContstants.Request_Square_CommentActions_Add;		
		doAsync(false, requestID, request, new Callback<Exception>() {
			
			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {
			
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				//成功时，更改图标状态
				commentModel.setComment(true);
				commentModel.setCommentUpCount((long)Float.parseFloat(result));
				adapter.refershData(commentModels);
			}
		});
	}
	
	
	
	
	/**
	 * 
	 * @param isError 是否请求错误
	 * @param isNoNetwork 是否网络请求错误
	 */
	public void refreshComplete(boolean isError,boolean isNoNetwork) {
//		isError = true;
//		isNoNetwork = false;
		try {
			int drawableID = 0;
			String title = null,content = null,btnTitle = null;
			if (isError && isNoNetwork && (commentModels==null || commentModels.size() == 0)) {
				drawableID = R.drawable.error_network;
				title = getResources().getString(R.string.hint_error_network_title);
				content = getResources().getString(R.string.hint_error_network_content);
				btnTitle = getResources().getString(R.string.hint_error_button_title);
			} else if (isError  && (commentModels==null || commentModels.size() == 0)) {
				drawableID = R.drawable.failed;
				title = getResources().getString(R.string.hint_error_request_title);
				content = getResources().getString(R.string.hint_error_request_content);
				btnTitle = getResources().getString(R.string.hint_error_button_title);
			} else if(commentModels==null || commentModels.size() == 0){
				drawableID = R.drawable.nodata;
				title = getResources().getString(R.string.hint_error_nodata_title);
				content = getResources().getString(R.string.hint_error_nodata_content);
				btnTitle = getResources().getString(R.string.hint_error_button_title);
			}
			
			if (drawableID>0 && progressActivity!=null) {
				progressActivity.showError(getResources().getDrawable(drawableID), title, content, btnTitle, new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (commentModels==null || commentModels.size() == 0) {
							progressActivity.showLoading();
						}
						obtainFoundDetailCommentRequest(false);
					}
				});
			} else {
				progressActivity.showContent();
			}
			
			
			if (mPullRefreshListView != null) {
				mPullRefreshListView.onRefreshComplete();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
