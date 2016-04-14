package com.mci.firstidol.activity;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.mci.firstidol.adapter.SquareLiveDetailFragmentAdapter;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.base.BaseFragment;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.fragment.square.LiaotianFragment;
import com.mci.firstidol.fragment.square.TabFragment;
import com.mci.firstidol.fragment.square.ZhiBoFragment;
import com.mci.firstidol.fragment.welfare.ArticleFragment;
import com.mci.firstidol.fragment.welfare.CommonUtils;
import com.mci.firstidol.listener.SquareFragmentListener;
import com.mci.firstidol.model.BaseRequest;
import com.mci.firstidol.model.SquareLiveModel;
import com.mci.firstidol.model.SquareModel;
import com.mci.firstidol.model.SquareRequest;
import com.mci.firstidol.model.SquareModel.LiveAnnex;
import com.mci.firstidol.model.SquareModel.QiNiuTokenModel;
import com.mci.firstidol.model.SquareModel.SquareLiveChatModel;
import com.mci.firstidol.model.SquareRequest.LiveChatSave;
import com.mci.firstidol.model.SquareRequest.SquareLiveChatSaveRequest;
import com.mci.firstidol.utils.AsyncTaskUtils;
import com.mci.firstidol.utils.AudioRecorder;
import com.mci.firstidol.utils.DateHelper;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.utils.LogUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;
import com.mci.firstidol.view.SimpleViewPagerIndicator;
import com.mci.firstidol.view.SimpleViewPagerIndicator.SimpleViewPagerListener;
import com.mci.firstidol.view.StickyNavLayout;
import com.mci.firstidol.view.ToastUtils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

public class SquareLiveActivity extends BaseActivity implements OnItemClickListener,SimpleViewPagerListener,SquareFragmentListener{

	public static final String ACTION_USER_ROLE_CHANGED = "action.USER_ROLE_CHANGED";
    public static final String ACTION_USER_LOGIN = "action.USER_LOGIN";

    private static final int MAX_RECORD_TIME = 10; // 最长录制时间，单位秒，0为无时间限制
    private static final int MIN_RECORD_TIME = 1; // 最短录制时间，单位秒，0为无时间限制

    private static final int RECORD_OFF = 0; // 不在录音
    private static final int RECORD_ON = 1; // 正在录音

    private Dialog mRecordDialog;
    private AudioRecorder mAudioRecorder;
    private Thread mRecordThread;
    private UploadManager mUploadManager;

    private int recordState = 0; // 录音状态
    private float recodeTime = 0.0f; // 录音时长
    private boolean moveState = false; // 手指是否移动
    private float downY;

    private ImageView mIvRecVolume;
    private TextView mTvRecordDialogTxt;
	
	private ImageView iv_user_logo,iv_shuohua,iv_operate;
	private TextView tv_main_title,tv_subtitle,tv_look_num,tv_status;
	
	private String[] mTitles = new String[] { "LIVE", "CHAT"};
	private SimpleViewPagerIndicator mIndicator;
	private ViewPager mViewPager;
	
	private  ArrayList<String> squares = new ArrayList<String>();
	private ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();
	private SquareLiveDetailFragmentAdapter adapter;
	private FragmentManager fm;
	private StickyNavLayout stickyNavLayout;
	
	private SquareLiveModel squareLiveModel;
	// 直播相关
	private ArrayList<SquareLiveChatModel> liveModels;//直播列表
	private ArrayList<SquareLiveChatModel> chatModels;//聊天列表
	
	private long liveMaxChatId;//直播列表最大chatId
	private long chatMaxChatId;//聊天列表最大 chatId
	
	private AsyncTask liveAsyncTask;
	private AsyncTask chatAsyncTask;
	
	//用户角色
	public static int userRole;
	private boolean isRequestRoleSuc;
	//当前所在fragment 的位置
	public static int currentPosition;
	
	
	
	
	//定时查询：
	private Timer timer;  
	private TimerTask timerTask;
	
	//定时任务执行
	private boolean isCompleteQuery;//上次查询是否结束
	
	private boolean isSearch;
	private long articleID;
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 911:
				queryMsg();
				break;
			default:
				break;
			}

		}

	};
	protected void queryMsg() {
		// TODO Auto-generated method stub
		if (currentPosition == 0) {//LIVE
			obtainLiveData(true, currentPosition, liveMaxChatId);
		} else {//CHAT
			obtainChatData(true, currentPosition, chatModels.get(0).getChatId());
		}
	}
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		
//		super.onCreate(savedInstanceState);
//		setDragEdge(DragEdge.NONE);
//		setContentLayout(R.layout.layout_square_live);
//		initViews();
//		initDatas();
//		initEvents();
		
//		initNavBar();
//		findViewById();
//		initView();
//		initData();
//	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//判断是否获取角色成功
		if (!isRequestRoleSuc && dataManager.isLogin) {
			obtainUserRoleRequest(false, null);
		}
	}
	

	@Override
	protected void initNavBar() {
		// TODO Auto-generated method stub
		setTitle(R.string.live_detail);
	}

	

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
			
		mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
		mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
		
		stickyNavLayout = (StickyNavLayout) findViewById(R.id.stickynavlayout);
		
		iv_user_logo = (ImageView) findViewById(R.id.iv_user_logo);
		tv_main_title = (TextView) findViewById(R.id.tv_main_title);
		tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
		tv_look_num = (TextView) findViewById(R.id.tv_look_num);
		tv_status = (TextView) findViewById(R.id.tv_status);
		
		iv_shuohua = (ImageView) findViewById(R.id.iv_shuohua);
		iv_operate = (ImageView) findViewById(R.id.iv_operate);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setShuohuaTouchListener();
		iv_shuohua.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_operate.setSelected(!iv_operate.isSelected());
				iv_shuohua.setSelected(!iv_shuohua.isSelected());
			}
		});
		
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				
				
				
				currentPosition = position;
				if (position == 0) {// 直播
					ZhiBoFragment fragment = ((ZhiBoFragment) fragments
							.get(position));
					if (liveModels == null || liveModels.size() == 0) {
						//暂停计时器查询
						stopTimeTask();
						
						squareLiveDetailLiveRequest(fragment, false,0);
					}
				} else if (position == 1) {// 聊天
					LiaotianFragment fragment = ((LiaotianFragment) fragments
							.get(position));
					if (chatModels == null || chatModels.size()==0) {
						//暂停计时器查询
						stopTimeTask();
						
						squareLiveDetailChatRequest(fragment, false,0);
					}
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels)
			{
				mIndicator.scroll(position, positionOffset);
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{

			}
		});
	}

	
	

	protected void stopTimeTask() {
		// TODO Auto-generated method stub
		if (chatAsyncTask!=null) {
			chatAsyncTask.cancel(true);
		}
		
		if (liveAsyncTask!=null) {
			liveAsyncTask.cancel(true);
		}
		
		if (timerTask!=null) {
			timerTask.cancel();
			timerTask = null;
		}
		
		if (timer!=null) {
			timer.cancel();
			timer = null;
		}
		
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		isSearch = (boolean) getIntent().getExtras().getBoolean(Constant.IntentKey.isSearch,false);
		
		if (isSearch) {//非来自直播列表
			articleID = getIntent().getExtras().getLong(Constant.IntentKey.articleID);
//			progressActivity.showLoading();
			
			// 查询发现详情
			obtainDetailRequest();
		} else {
			squareLiveModel = (SquareLiveModel) getIntent().getExtras().getSerializable(Constant.IntentKey.squareLiveModel);
			
			initLoadView();
		}
		
		//启动定时器
//		initTimerTask();
	}
	
	
	public void initLoadView(){
		initLayoutData();
		
		//初始化fragment 数组
		initFragments();
				
		fm = getSupportFragmentManager();
				
		adapter = new SquareLiveDetailFragmentAdapter(fm,fragments,squares);
				
		mIndicator.setTitles(mTitles);
		mIndicator.setSimpleViewPagerListener(this);
		

		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);
		
		if (liveModels == null) {
			liveModels = new ArrayList<SquareLiveChatModel>();
		}
		
		if (chatModels == null) {
			chatModels = new ArrayList<SquareLiveChatModel>();
		}
		
		mUploadManager = new UploadManager();
		
		
		//获取直播列表
		obtainLiveData(false, 0,0);
	}
	
	public void obtainDetailRequest() {
		BaseRequest request = new BaseRequest();
		// 请求地址
		String requestID = String.format(
				Constant.RequestContstants.Request_Square_FoundDetail,
				articleID);
		doAsync(false, requestID, request, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				ToastUtils.showCustomToast(e.getLocalizedMessage());
//				mPullRefreshListView.onRefreshComplete();
//				String errorMsg = e.getMessage();
//				if ("无网络".equals(errorMsg)) {
//					refreshComplete(true,true);
//				} else {
//					refreshComplete(true,false);
//				}
			}
		}, new Callback<String>() {

			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub

				squareLiveModel = (SquareLiveModel) GsonUtils.jsonToBean(
						result, SquareLiveModel.class);
				
				
				initLoadView();
			}
		});
	}
	
	
	

	private void initTimerTask() {
		// TODO Auto-generated method stub
		
		timerTask = new TimerTask() {  
		    @Override  
		    public void run() {  
		        // TODO Auto-generated method stub  
		    	
		    	if (isCompleteQuery) {
		    		isCompleteQuery = !isCompleteQuery;
		    		
		    		Message message = new Message();  
			        message.what = 911;  
			        handler.sendMessage(message); 
			        LogUtils.i("*****************定时任务查询");
				}
		        
		    }  
		};  
		if (timer == null) {
			timer = new Timer();
		}
		
		timer.schedule(timerTask, 10000, 5000);//延迟10秒查询，每5秒执行一次
	}

	private void initLayoutData() {	
		// TODO Auto-generated method stub
		ImageLoader.getInstance().displayImage(squareLiveModel.getVideoIco(), iv_user_logo, BaseApp.circleOptions);
		if (squareLiveModel!=null && squareLiveModel.getTitle().split(" ").length>1) {
			tv_main_title.setText(squareLiveModel.getTitle().split(" ")[1]);
		} else {
			tv_main_title.setText(squareLiveModel.getTitle());
		}
		
		tv_subtitle.setText(squareLiveModel.getSubTitle());
		tv_look_num.setText(squareLiveModel.getChatPeopleCount()+"");
		
		
		//0：未开始，1：进行中 2：已结束
		if (squareLiveModel.getEventsProgress() == 0) {//未开始
			tv_status.setText("距离开始"+ DateHelper.getIntervalDays(squareLiveModel.getEventsStart()));
		} else if (squareLiveModel.getEventsProgress() == 1) {//进行中
			tv_status.setText("进行中");
		} else if (squareLiveModel.getEventsProgress() == 2) {//已结束
			tv_status.setText("已经结束");
		}
	}

	private void initFragments() {
		// TODO Auto-generated method stub
		ZhiBoFragment zhiBoFragment = ZhiBoFragment.newInstance(null);
		zhiBoFragment.setSquareFragmentListener(this);
		LiaotianFragment liaotianFragment = LiaotianFragment.newInstance(null);
		liaotianFragment.setSquareFragmentListener(this);
		fragments.add(zhiBoFragment);
		fragments.add(liaotianFragment);
		
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
//		startActivity(SquareFoundDetailReplyActivity.class);
		Utily.go2Activity(this, SquareFoundDetailReplyActivity.class);
	}
	

	@Override
	public void onItemClick(int position) {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(position);
	}

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.layout_square_live;
	}

	@Override
	public void rightNavClick() {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (timerTask!=null) {
			timerTask.cancel();
			timerTask = null;
		}
		
		if (timer!=null) {
			timer.cancel();  
			timer = null;
		}
		
		
		
		super.onDestroy();
	}
	
	
	
	/**
	 * 处理直播刷新事件
	 * @param isRefresh true:下拉刷新  false：上拉加载更多
	 * @param fragmentIndex
	 * @param chatId
	 */
	public void obtainLiveData(boolean isRefresh, int fragmentIndex,long chatId) {
		ZhiBoFragment fragment = ((ZhiBoFragment) fragments
				.get(fragmentIndex));
		if(fragment.isScrollTop){
			stickyNavLayout.isInControl = false;
			stickyNavLayout.isScrollDown = true;
		} else {
			stickyNavLayout.isInControl = true;
			stickyNavLayout.isScrollDown = false;
		}
		
		// 查询广场--直播详情-直播列表(下拉刷新)
		squareLiveDetailLiveRequest(fragment, isRefresh,chatId);
	}
	
	// 处理直播刷新事件
	public void obtainChatData(boolean isRefresh, int fragmentIndex,long chatId) {
		LiaotianFragment fragment = ((LiaotianFragment) fragments
				.get(fragmentIndex));
//		stickyNavLayout.isTopPartlyHidden = fragment.isScrollTop;
		if(fragment.isScrollTop){
//			stickyNavLayout.isInControl = false;
			stickyNavLayout.isScrollDown = true;
		} else {
//			stickyNavLayout.isInControl = true;
			stickyNavLayout.isScrollDown = false;
		}
		// 查询广场--直播详情-聊天列表(下拉刷新)
		squareLiveDetailChatRequest(fragment, isRefresh,chatId);
	}

	/**
	 * 广场--直播--直播详情——直播列表
	 * @param fragment：直播
	 * @param isRefresh：是否刷新，true 为下拉刷新，false：加载更多
	 * @param pageIndex：页数
	 * @param chatId：
	 */
	private void squareLiveDetailLiveRequest(final ZhiBoFragment fragment,
			final boolean isRefresh, long chatId) {
		// TODO Auto-generated method stub
		BaseRequest baseRequest = new BaseRequest();// 其它参数需要继承此类
		// baseRequest.requestMethod = "get";//默认为get
		// 请求地址
		String requestID;
		if (isRefresh) {//刷新
			requestID = String.format(
					Constant.RequestContstants.Request_Live_detail_live_down_List, squareLiveModel.getArticleId(), chatId);
		} else {
			requestID = String.format(
					Constant.RequestContstants.Request_Live_detail_live_up_List, Constant.pageNum10, squareLiveModel.getArticleId(), chatId);
		}
		
		liveAsyncTask = doAsync(false, requestID, baseRequest, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				// ToastUtils.showMyToast(e.getLocalizedMessage());
				fragment.refreshComplete();
			}
		}, new Callback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				LogUtils.i(result);
				fragment.refreshComplete();
				isCompleteQuery = true;
				Type type = new TypeToken<ArrayList<SquareLiveChatModel>>() {
				}.getType();

				ArrayList<SquareLiveChatModel> tempSquareLiveChatModels = (ArrayList<SquareLiveChatModel>) GsonUtils.jsonToList(result, type);
				boolean isComplete = false;
				if (!isRefresh) {//上拉加载更多
					if (tempSquareLiveChatModels.size()<Constant.pageNum10) {
						isComplete = true;
					}
					liveModels.addAll(tempSquareLiveChatModels);
					if (liveMaxChatId==0) {
						//获取最大chatId
						obtainLiveMaxChatId();
					}
				} else {
//					if (tempSquareLiveChatModels!=null && tempSquareLiveChatModels.size()>0) {
//						liveModels.addAll(0, tempSquareLiveChatModels);
//					}
					//读取最新数据时，会把置顶的消息也传过来，1、首先清理已经存储数据中置顶的消息，2、把查询的数据添加到已经处理过后的数据中
					//清理置顶数据
					removeTopDataFromLiveModels();
					//合并最新的数据
					liveModels.addAll(0, tempSquareLiveChatModels);
					//获取最大chatId
					obtainLiveMaxChatId();
					
				}
				
				fragment.updateContentData(liveModels, isRefresh,isComplete);
				
				if (timer == null) {
					initTimerTask();
				}

			}
		});
	}
	
	
	
	protected void obtainLiveMaxChatId() {
		// TODO Auto-generated method stub
		for (int i = 0; i < liveModels.size();i++) {
			SquareLiveChatModel squareLiveChatModel = liveModels.get(i);
			if (squareLiveChatModel.getIsTop() == 0) {//置顶
				long tempLiveMaxChatId = squareLiveChatModel.getChatId();
				if (tempLiveMaxChatId>liveMaxChatId) {
					liveMaxChatId = tempLiveMaxChatId;
				}
				
				break;
			} 
		}
	}

	protected void removeTopDataFromLiveModels() {
		// TODO Auto-generated method stub
		for (int i = 0; i < liveModels.size();) {
			SquareLiveChatModel squareLiveChatModel = liveModels.get(i);
			if (squareLiveChatModel.getIsTop() == 1) {//置顶
				liveModels.remove(i);
			} else {
				i++;
			}
		}
	}

	private void squareLiveDetailChatRequest(final LiaotianFragment fragment,
			final boolean isRefresh, long chatId) {
		// TODO Auto-generated method stub
		BaseRequest baseRequest = new BaseRequest();// 其它参数需要继承此类
		// baseRequest.requestMethod = "get";//默认为get
		// 请求地址
		String requestID;
		if (isRefresh) {//刷新
			requestID = String.format(
					Constant.RequestContstants.Request_Live_detail_chat_down_List, squareLiveModel.getArticleId(), chatId);
		} else {
			requestID = String.format(
					Constant.RequestContstants.Request_Live_detail_chat_up_List, Constant.pageNum10, squareLiveModel.getArticleId(), chatId);
		}
		chatAsyncTask = doAsync(false, requestID, baseRequest, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				// ToastUtils.showMyToast(e.getLocalizedMessage());
				fragment.refreshComplete();
			}
		}, new Callback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				LogUtils.i(result);
				fragment.refreshComplete();
				isCompleteQuery = true;
				Type type = new TypeToken<ArrayList<SquareLiveChatModel>>() {
				}.getType();

				ArrayList<SquareLiveChatModel> tempSquareLiveChatModels = (ArrayList<SquareLiveChatModel>) GsonUtils.jsonToList(result, type);
				boolean isComplete = false;
				if (!isRefresh) {//上拉加载更多
					if (tempSquareLiveChatModels.size()<Constant.pageNum10) {
						isComplete = true;
					}
					chatModels.addAll(tempSquareLiveChatModels);
				} else {//下拉刷新、加载未读数据
					if (tempSquareLiveChatModels!=null && tempSquareLiveChatModels.size()>0) {
						chatModels.addAll(0, tempSquareLiveChatModels);
					}
				}
				
				fragment.updateContentData(chatModels, isRefresh,isComplete);

				if (timer == null) {
					initTimerTask();
				}
			}
		});
	}

	/**
	 * isRefresh:TRUE：加载未读数量   FALSE：首次加载、上拉加载更多
	 */
	@Override
	public void pullRefresh(boolean isRefresh, int fragmentIndex,
			int pageIndex, String maxDate, int currentPosition) {
		// TODO Auto-generated method stub
		long chatId = 0;
		switch (fragmentIndex) {
		case 0:
			
			if (isRefresh) {//加载未读数量,chatId
				if (liveModels.size() == 0) {//无值默认为0
					chatId =  0;
				} else {//有值，取自第一条数据
//					chatId = liveModels.get(0).getChatId();
					chatId = liveMaxChatId;
				}
			} else {//加载更多
				if (liveModels.size() == 0) {//首次加载为0
					chatId =  0;
				} else {//有值，取自最后一条数据
					chatId = liveModels.get(liveModels.size() - 1).getChatId();
				}
			}
			obtainLiveData(isRefresh, fragmentIndex, chatId);
			break;
		case 1:
			if (isRefresh) {//加载未读数量,chatId
				if (chatModels.size() == 0) {
					chatId =  0;
				} else {
					chatId = chatModels.get(0).getChatId();
//					chatId = chatMaxChatId;
				}
			} else {
				if (chatModels.size() == 0) {
					chatId =  0;
				} else {
					chatId = chatModels.get(chatModels.size() - 1).getChatId();
				}
			}
			obtainChatData(isRefresh, fragmentIndex, chatId);
			break;
		
		default:
			break;
		}
	}
	
	
	
	
	
	
	private void setShuohuaTouchListener() {
        iv_operate.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            	if (!dataManager.isLogin) {
					Utily.go2Activity(context, LoginActivity.class);
					return false;
				}
            	if (iv_operate.isSelected()) {//处于语音发布状态
            		
            		
            		switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (recordState != RECORD_ON) {
                            downY = event.getY();
                            mAudioRecorder = new AudioRecorder();
                            recordState = RECORD_ON;
                            mAudioRecorder.start();
                            recordTimethread();
                            showVoiceDialog(0);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float moveY = event.getY();
                        if (moveY < 0) {
                            if (moveY - downY < 0) {
                                moveState = true;
                                showVoiceDialog(1);
                            }
                            if (moveY - downY > (-downY - 50)) {
                                moveState = false;
                                showVoiceDialog(0);
                            }
                        } else {
                            if (moveY - downY > 50) {
                                moveState = true;
                                showVoiceDialog(1);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
//                        mShuohua.setImageResource(R.drawable.anzhu_shuohua_default);
                        if (recordState == RECORD_ON) {
                            recordState = RECORD_OFF;
                            if (mRecordDialog.isShowing()) {
                                mRecordDialog.dismiss();
                            }
                            mAudioRecorder.stop();
                            mRecordThread.interrupt();

                            if (moveState) {
                                mAudioRecorder.deleteAudioFile();
                            } else {
                                if (recodeTime < MIN_RECORD_TIME) {
                                    mAudioRecorder.deleteAudioFile();
                                    ToastUtils.showCustomToast("时间太短,录音失败");
                                } else {//发送录音
                                	//先获取上传七牛时需要的 token
                                	obtainQiNiuTokenRequest();
                                }
                            }
                            moveState = false;
                        }
                        break;
                }
				} else {//处于普通发布状态
					 if (event.getAction() == MotionEvent.ACTION_UP) {
						//跳转到发布页面
						startUploadActivity(0, 0);
					}
				}
            	
                
                return true;
            }
        });
    }
	
	

	public void startUploadActivity(int type, int parentId) {
        Intent intent = new Intent(this, UploadActivity.class);
        intent.putExtra("articleId", squareLiveModel.getArticleId());
        intent.putExtra("userId", dataManager.userModel.UserId);
        intent.putExtra("isRequestRoleSuc", isRequestRoleSuc);
        intent.putExtra("userRole", userRole);
        intent.putExtra("parentId", parentId);
        intent.putExtra("nickName", dataManager.userModel.NickName);
        intent.putExtra("avatar", dataManager.userModel.Avatar);
        intent.putExtra("type", type);
//        startActivity(intent);
        
        Utily.go2Activity(context, intent);
    }
	
	private void showVoiceDialog(int flag) {
        if (mRecordDialog == null) {
            mRecordDialog = new Dialog(this, R.style.DialogStyle);
            mRecordDialog.setContentView(R.layout.record_dialog);
            mIvRecVolume = (ImageView) mRecordDialog.findViewById(R.id.record_dialog_img);
            mTvRecordDialogTxt = (TextView) mRecordDialog.findViewById(R.id.record_dialog_txt);
        }

        if (flag == 1) {
            mIvRecVolume.setImageResource(R.drawable.songkai_cancel);
            mTvRecordDialogTxt.setText("手指松开,取消发布");
        } else {
            mIvRecVolume.setImageResource(R.drawable.shanghua_cancel);
            mTvRecordDialogTxt.setText("手指上滑,取消发送");
        }
        mRecordDialog.show();
    }

    private void recordTimethread() {
        mRecordThread = new Thread(recordThread);
        mRecordThread.start();
    }
    
    private Runnable recordThread = new Runnable() {
        @Override
        public void run() {
            recodeTime = 0.0f;
            while (recordState == RECORD_ON) {
                if (recodeTime >= MAX_RECORD_TIME && MAX_RECORD_TIME != 0) {
                    recodeTime = MAX_RECORD_TIME;
                    mHandler.sendEmptyMessage(0);
                } else {
                    try {
                        Thread.sleep(150);
                        recodeTime += 0.15;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mAudioRecorder.stop();
            ToastUtils.showCustomToast("录制时间超过最长时间");
          //先获取上传七牛时需要的 token
        	obtainQiNiuTokenRequest();
        }
    };
    
    
    /**
     * 获取用户角色
     */
    protected void obtainUserRoleRequest(final boolean isRequest, final ArrayList<LiveAnnex> attachments) {
		// TODO Auto-generated method stub
    	BaseRequest baseRequest = new BaseRequest();// 其它参数需要继承此类
		// 请求地址 annexType      1：图片，2：音频 3：视频
		String requestID = String.format(Constant.RequestContstants.Request_LiveChat_UserRole, squareLiveModel.getArticleId(),dataManager.userModel.UserId);
		doAsync(false, requestID, baseRequest, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				 ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				//{"ArticleId":15279.0,"CreateDate":"/Date(1453098142538+0800)/","CreateUser":0.0,"IsDeleted":0.0,"LiveHostId":0.0,"UserId":56521.0,"UserNickName":"","UserRole":0.0}
				userRole = (int)Float.parseFloat(GsonUtils.getJsonValue(result, "UserRole").toString());
				
				if (isRequest) {
					obtainChatSaveRequest(attachments);
				}
			}
		});
	}
    
    /**
     * 获取七牛 token
     */
    protected void obtainQiNiuTokenRequest() {
		// TODO Auto-generated method stub
    	BaseRequest baseRequest = new BaseRequest();// 其它参数需要继承此类
		// 请求地址 annexType      1：图片，2：音频 3：视频
		String requestID = String.format(Constant.RequestContstants.Request_QiNiu_Token, 2);
		doAsync(false, requestID, baseRequest, new Callback<Exception>() {

			@Override
			public void onCallback(Exception e) {
				// TODO Auto-generated method stub
				 ToastUtils.showCustomToast(e.getLocalizedMessage());
			}
		}, new Callback<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(String result) {
				// TODO Auto-generated method stub
				LogUtils.i(result);
				QiNiuTokenModel qiNiuTokenModel = (QiNiuTokenModel) GsonUtils.jsonToBean(result, QiNiuTokenModel.class);
				
				//获取到 token 时，调用七牛接口把数据上传到七牛服务器
				uploadAudio(qiNiuTokenModel);
			}
		});
	}
    
    /**
     * 上传音频到七牛服务器
     * @param time
     */
    private void uploadAudio(final QiNiuTokenModel qiNiuTokenModel) {
        final String path = mAudioRecorder.getAudioRecordPath();
        mUploadManager.put(path, CommonUtils.createUploadKey(LiveAnnex.TYPE_AUDIO), qiNiuTokenModel.getUpToken(), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                try {
                    if (info.isOK()) {
                    	//上传成功后，
                        File file = new File(path);
                        ArrayList<LiveAnnex> attachments = new ArrayList<LiveAnnex>();
                        LiveAnnex attachment = new SquareModel().new LiveAnnex();
                        attachment.setAnnexType(2);
                        attachment.setAnnexUrl(AsyncTaskUtils.File_URL +  qiNiuTokenModel.getAudioName());
                        attachment.setDuration((int)recodeTime + "\"");
                        attachment.setPersistentId(response.get("persistentId").toString());
                        attachments.add(attachment);
                        
                        //判断是否获取到用户角色
                        if (!isRequestRoleSuc) {
							obtainUserRoleRequest(true,attachments);
						} else {
							obtainChatSaveRequest(attachments);
						}
                        
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);
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
//				LogUtils.i(result);
//				ToastUtils.showCustomToast("发布成功");
				ToastUtils.showPointToast("5");//回复加5分
			}
		});
	}

	private SquareLiveChatModel initLiveChatModel() {
		// TODO Auto-generated method stub
		SquareLiveChatModel squareLiveChatModel = new SquareModel().new SquareLiveChatModel();
		squareLiveChatModel.setArticleId(squareLiveModel.getArticleId());
		squareLiveChatModel.setChatContent(null);
		squareLiveChatModel.setUserId(dataManager.userModel.UserId);
		squareLiveChatModel.setUserRole(userRole);
		squareLiveChatModel.setParentId(0);
		squareLiveChatModel.setUserAvatar(dataManager.userModel.Avatar);
		squareLiveChatModel.setUserNickName(dataManager.userModel.NickName);
		
		return squareLiveChatModel;
	}
}
