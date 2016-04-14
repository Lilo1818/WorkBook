
package com.mci.firstidol.fragment.square;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mci.firstidol.R;
import com.mci.firstidol.activity.SquareLiveActivity;
import com.mci.firstidol.activity.SquareLiveChatDetailActivity;
import com.mci.firstidol.adapter.ChatAdapter;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.listener.SquareFragmentListener;
import com.mci.firstidol.model.SquareModel.SquareLiveChatModel;
import com.mci.firstidol.view.SelectPicPopupWindow;
import com.mci.firstidol.view.XCDanmuView;

public class ZhiBoFragment extends BaseFragment  implements OnItemClickListener{
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
    
    private XCDanmuView mDanmuView;
	private List<View> mViewList;
	/*private String[] mStrItems = {
	            "搜狗","百度",
	            "腾讯","360",
	            "阿里巴巴","搜狐",
	            "网易","新浪",
	            "搜狗-上网从搜狗开始","百度一下,你就知道",
	            "必应搜索-有求必应","好搜-用好搜，特顺手",
	            "Android-谷歌","IOS-苹果",
	            "Windows-微软","Linux"
	 };*/
	
	private ImageView shielding;
	
	private SelectPicPopupWindow menuWindow;
	
	private ImageView heaven;
	
//    private static ZhiBoFragment instance = null;
	public static ZhiBoFragment newInstance(String content) {
//		if (null == instance) {
//            instance = new ZhiBoFragment();
//        }
		ZhiBoFragment instance = new ZhiBoFragment();
        return instance;
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }



	
	
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
//            case MessageCode.GET_LIVE_CHAT_LIST:
//                if (msg.getData().getInt(Utils.KEY_REQUEST_ID) == mRequestChatId) {
//                    mRequestChatId = Utils.INVALID_ID;
//                    if (msg.obj != null) {
//                        ArrayList<ChatDbWarpper> chats = (ArrayList<ChatDbWarpper>) msg.obj;
//                        if (chats != null && !chats.isEmpty()) {
//                            mChats.addAll(chats);
//                        }
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
//            case MessageCode.GET_NEWEST_LIVE_CHAT_LIST:
//                if (msg.getData().getInt(Utils.KEY_REQUEST_ID) == mRequestNewestChatId) {
//                    mRequestNewestChatId = Utils.INVALID_ID;
//                    if (msg.obj != null) {
//                        ArrayList<ChatDbWarpper> chats = (ArrayList<ChatDbWarpper>) msg.obj;
//                        if (chats != null && !chats.isEmpty()) {
//                            deleteTopChats();
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
//        mRequestChatId = mDataEngineContext.requestLiveChatList(Common.PAGE_SIZE, mArticleId, mChatId);
    }

    private void requestNewestChatList() {
//		int chatId=getMaxChatId();
//		if(chatId<1){
//			requestChatList();
//		}else{
//			mRequestNewestChatId = mDataEngineContext.requestNewestLiveChatList(mArticleId, chatId);
//		}
    }

    private int getMaxChatId() {
        int chatId = -1;
//        for (ChatDbWarpper chat : mChats) {
//            if (chat.IsTop != 1 && chat.ChatId > chatId) {
//                chatId = chat.ChatId;
//            }
//        }
        return chatId;
    }

    private void deleteTopChats() {
//        ArrayList<ChatDbWarpper> topChats = new ArrayList<ChatDbWarpper>();
//        for (ChatDbWarpper chat : mChats) {
//            if (chat.IsTop == 1) {
//                topChats.add(chat);
//            } else {
//                break;
//            }
//        }
//        mChats.removeAll(topChats);
    }

//    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (ZhiBoActivity.ACTION_USER_ROLE_CHANGED.equals(intent.getAction())) {
//                int userRole = intent.getIntExtra("user_role", 0);
//                if (mUserRole != userRole) {
//                    mUserRole = userRole;
//                    mAdapter.setUserRole(mUserRole);
//                    mAdapter.notifyDataSetChanged();
//                }
//            } else if (ZhiBoActivity.ACTION_USER_LOGIN.equals(intent.getAction())) {
//                mAdapter.setUserInfo(mDataEngineContext.getUserInfo());
//            }
//        }
//    };

    


    @Override
	protected void findViewById() {
		// TODO Auto-generated method stub
    	isScrollTop = true;
		mLoadingView = findViewById(R.id.loading);
        mContentView = findViewById(R.id.container);
        mFailedView = findViewById(R.id.loading_failed);
        shielding = (ImageView) findViewById(R.id.shielding);
        heaven = (ImageView) findViewById(R.id.heaven);
        initListener();

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
		squareFragmentListener.pullRefresh(isRefresh, 0, 0,"",0);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mChats = new ArrayList<SquareLiveChatModel>();
		mDanmuView = (XCDanmuView)findViewById(R.id.xDanmu);
        mAdapter = new ChatAdapter(mContext, mChats);
        mAdapter.setType(0);
        mAdapter.setUserRole(SquareLiveActivity.userRole);
        mAdapter.setUserInfo(DataManager.getInstance().userModel);
        mListView.setAdapter(mAdapter);
//        showLoading();
        requestChatList();
	}

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_zhibo;
	}

	@Override
	protected void initData() {
        
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
		Toast.makeText(mContext, "值为"+ contents.size() , Toast.LENGTH_SHORT).show();
		if (isRefresh) {// 下拉刷新
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
		/*String[] mStrItems = getDanMu(contents);
        mDanmuView.initDanmuItemViews(mStrItems);*/
		
		String[] mStrItems = new String[contents.size()];
		for (int i = 0; i < contents.size(); i++) {
			mStrItems[i] = contents.get(i).getChatContent();
		}
		mDanmuView.initDanmuItemViews(mStrItems);
	}

	
	public void setSquareFragmentListener(
			SquareFragmentListener squareFragmentListener) {
		this.squareFragmentListener = squareFragmentListener;
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		findViewById(R.id.danmu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDanmuView.isWorking()) {
                    mDanmuView.hide();
                    //((ImageButton) view).setText("开启弹幕");
                    shielding.setVisibility(View.VISIBLE);
                } else {
                    mDanmuView.start();
                    //((ImageButton) view).setText("关闭弹幕");
                    shielding.setVisibility(View.GONE);
                }
            }
        });
		findViewById(R.id.gift).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//实例化SelectPicPopupWindow
				menuWindow = new SelectPicPopupWindow(mContext, itemsOnClick);
				//显示窗口
				menuWindow.showAtLocation(((Activity) mContext).findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 10, 10); //设置layout在PopupWindow中显示的位置
			}
			
		});
	}
	private Animation mAnimation = null;
	//为弹出窗口实现监听类
    private OnClickListener  itemsOnClick = new OnClickListener(){

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.sending_flowers://送花
				Toast.makeText(mContext, "送花", Toast.LENGTH_SHORT).show();
				break;
			case R.id.a_kiss://亲一下
				Toast.makeText(mContext, "亲一下", Toast.LENGTH_SHORT).show();
				break;
			case R.id.send_him://上天
				Toast.makeText(mContext, "上天", Toast.LENGTH_SHORT).show();
				//heaven
				/**加载移动动画**/
				mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.translate);
				/**播放移动动画**/
				heaven.startAnimation(mAnimation);
				break;
			default:
				break;
			}
			
			
				
		}
    	
    };
	
	private String[] getDanMu(ArrayList<SquareLiveChatModel> mChats){
		String[] mStrItems = new String[mChats.size()];
		for (int i = 0; i <= mChats.size(); i++) {
			mStrItems[i] = mChats.get(i).getChatContent().toString();
		}
		return mStrItems;
	}
	
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
//		ToastUtils.showCustomToast(""+position);
		
		selectPosition = position - 1;
		SquareLiveChatModel squareLiveChatModel = contents.get(selectPosition);
		Bundle bundle = new Bundle();
		bundle.putString(Constant.IntentKey.from, "LIVE");
		bundle.putLong(Constant.IntentKey.userRole, ((SquareLiveActivity)getActivity()).userRole);
		bundle.putSerializable(Constant.IntentKey.squareLiveChatModel, squareLiveChatModel);
//		Utily.go2Activity(mContext, SquareLiveChatDetailActivity.class,bundle);
		
		Intent intent = new Intent(mContext, SquareLiveChatDetailActivity.class);
		intent.putExtras(bundle);
		startActivityForResult(intent, 900);
		activity.overridePendingTransition(R.anim.activity_right_in,
				R.anim.activity_left_harf_out);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK && requestCode == 900) {//来自直播 CHAT
			contents.remove(selectPosition);
			mAdapter.refershData(contents);
		}
	}
	
}