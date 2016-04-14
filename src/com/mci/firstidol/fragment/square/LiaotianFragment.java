
package com.mci.firstidol.fragment.square;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mci.firstidol.R;
import com.mci.firstidol.activity.SquareLiveActivity;
import com.mci.firstidol.activity.SquareLiveChatDetailActivity;
import com.mci.firstidol.adapter.ChatAdapter;
import com.mci.firstidol.adapter.ChatDbWarpper;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.base.BaseTempFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.listener.SquareFragmentListener;
import com.mci.firstidol.model.SquareModel.SquareFoundModel;
import com.mci.firstidol.model.SquareModel.SquareLiveChatModel;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.ToastUtils;

public class LiaotianFragment extends BaseFragment implements OnItemClickListener{
    private static final int REQUEST_NEWEST_CHAT_TIME = 30 * 1000;

    private View mLoadingView;
    private View mContentView;
    private View mFailedView;

    private Context mContext;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private ChatAdapter mAdapter;

    private int mChatId = -1;

    private int mArticleId;
    private int mUserRole;
    private ArrayList<SquareLiveChatModel> mChats;
    
    private SquareFragmentListener squareFragmentListener;
    private ArrayList<SquareLiveChatModel> contents;
    public boolean isScrollTop;

    private int selectPosition;
    
    private static LiaotianFragment instance = null;
	public static LiaotianFragment newInstance(String content) {
//		if (null == instance) {
//            instance = new LiaotianFragment();
//        }
		LiaotianFragment instance = new LiaotianFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_zhibo, container, false);
//        mLoadingView = view.findViewById(R.id.loading);
//        mContentView = view.findViewById(R.id.container);
//        mFailedView = view.findViewById(R.id.loading_failed);
//
//        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.fragment_news_list);
//        mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
//                        | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//                requestChatList();
//            }
//
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                requestNewestChatList();
//            }
//        });
//        mListView = mPullToRefreshListView.getRefreshableView();
//
//        return view;
//    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        initData();
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
    }

//    @Override
//    @SuppressWarnings("unchecked")
//    public void onMessage(Message msg) {
//        switch (msg.what) {
//            case MessageCode.GET_CHAT_LIST:
//                if (msg.getData().getInt(Utils.KEY_REQUEST_ID) == mRequestChatId) {
//                    mRequestChatId = Utils.INVALID_ID;
//                    if (msg.obj != null) {
//                        ArrayList<ChatDbWarpper> chats = (ArrayList<ChatDbWarpper>) msg.obj;
//                        ArrayList<ChatDbWarpper> normalChats = new ArrayList<ChatDbWarpper>();
//                        for (ChatDbWarpper chat : chats) {
//                            if (chat.IsTop != 1) {
//                                normalChats.add(chat);
//                            }
//                        }
//                        if (!normalChats.isEmpty()) {
//                            mChats.addAll(normalChats);
//                        }
//
//                        mAdapter.notifyDataSetChanged();
//                        showContent();
//                    } else {
//                        if (mChats.isEmpty()) {
//                            showLoadingFailed();
//                        }
//                    }
//
//                    mPullToRefreshListView.onRefreshComplete();
//                }
//                break;
//            case MessageCode.GET_NEWEST_CHAT_LIST:
//                if (msg.getData().getInt(Utils.KEY_REQUEST_ID) == mRequestNewestChatId) {
//                    mRequestNewestChatId = Utils.INVALID_ID;
//                    if (msg.obj != null) {
//                        ArrayList<ChatDbWarpper> chats = (ArrayList<ChatDbWarpper>) msg.obj;
//                        if (chats != null && !chats.isEmpty()) {
//                            mChats.addAll(0, chats);
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    }
//                }
//                mPullToRefreshListView.onRefreshComplete();
//                break;
//        }
//    }

    

    private void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);
        mFailedView.setVisibility(View.GONE);
    }

    private void showContent() {
        mFailedView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
    }

    private void showLoadingFailed() {
        mFailedView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mContentView.setVisibility(View.GONE);
    }

    private void requestChatList() {
//        if (!mChats.isEmpty()) {
//            mChatId = mChats.get(mChats.size() - 1).ChatId;
//        }
//        mRequestChatId = mDataEngineContext.requestChatList(Common.PAGE_SIZE, mArticleId, mChatId);
    }

    private void requestNewestChatList() {
		int chatId=getMaxChatId();
//		if(chatId<1){
//			requestChatList();
//		}else{
//			mRequestNewestChatId = mDataEngineContext.requestNewestChatList(mArticleId, chatId);
//		}
    }

    private int getMaxChatId() {
        int chatId = -1;
//        if (!mChats.isEmpty()) {
//            chatId = mChats.get(0).ChatId;
//        }
        return chatId;
    }

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_zhibo;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
		isScrollTop = true;
		
        mLoadingView = findViewById(R.id.loading);
        mContentView = findViewById(R.id.container);
        mFailedView = findViewById(R.id.loading_failed);

        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.id_stickynavlayout_innerscrollview);
        mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//                requestChatList();
                didLoadDataRequest(false);
            }

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                requestNewestChatList();
            	didLoadDataRequest(true);
            }
        });
        mListView = mPullToRefreshListView.getRefreshableView();
        
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				// 判断滚动到顶部  
				  
			    if(view.getFirstVisiblePosition() == 0){  
			    	isScrollTop = true;
			    } else {
			    	isScrollTop = false;
			    }
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void didLoadDataRequest(final boolean isRefresh) {
		squareFragmentListener.pullRefresh(isRefresh, 1, 0,"",0);
	}
	@Override
	protected void initData() {
        mChats = new ArrayList<SquareLiveChatModel>();
        mAdapter = new ChatAdapter(mContext, mChats);
        mAdapter.setType(1);
        mAdapter.setUserRole(SquareLiveActivity.userRole);
        mAdapter.setUserInfo(DataManager.getInstance().userModel);
        mListView.setAdapter(mAdapter);
//        showLoading();
        requestChatList();
    }


	public void refreshComplete() {
		if (mPullToRefreshListView != null) {
			mPullToRefreshListView.onRefreshComplete();
		}
	}
	
	public void updateContentData(
			ArrayList<SquareLiveChatModel> squareLiveChatModels, boolean isRefresh,
			boolean isComplete) {
		mPullToRefreshListView.onRefreshComplete();
		contents = squareLiveChatModels;
		mAdapter.setUserRole(SquareLiveActivity.userRole);
		mAdapter.refershData(contents);
		if (isRefresh) {// 下拉刷新
		// contents = squareFoundModels;
		// adapter.refershData(contents);

			if (contents != null && contents.size() < Constant.pageNum10) {
				mPullToRefreshListView.setMode(Mode.PULL_FROM_START);
			} else {
				mPullToRefreshListView.setMode(Mode.BOTH);
			}
		} else {// 上拉加载更多

			if (isComplete) {
				mPullToRefreshListView.setMode(Mode.PULL_FROM_START);
			}
		}
	}
	
	public void setSquareFragmentListener(
			SquareFragmentListener squareFragmentListener) {
		this.squareFragmentListener = squareFragmentListener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
//		ToastUtils.showCustomToast(""+position);
		selectPosition = position - 1;
		SquareLiveChatModel squareLiveChatModel = contents.get(selectPosition);
		Bundle bundle = new Bundle();
		bundle.putLong(Constant.IntentKey.userRole, ((SquareLiveActivity)getActivity()).userRole);
		bundle.putSerializable(Constant.IntentKey.squareLiveChatModel, squareLiveChatModel);
//		Utily.go2Activity(mContext, SquareLiveChatDetailActivity.class,bundle);
		
		Intent intent = new Intent(mContext, SquareLiveChatDetailActivity.class);
		intent.putExtras(bundle);
		startActivityForResult(intent, 901);
		activity.overridePendingTransition(R.anim.activity_right_in,
				R.anim.activity_left_harf_out);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK && requestCode == 901) {//来自直播 CHAT
			contents.remove(selectPosition);
			mAdapter.refershData(contents);
		}
	}
}