package com.mci.firstidol.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;








import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mci.firstidol.R;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.utils.ConnectionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by wang on 2015/6/5.
 */
public abstract class BaseListActivity extends BaseActivity{

    protected final int INIT_DATA = 1;
    private final int HIDEPROCESS = 2;
    public List<Object> modelList;//数据模型列表

    public PullToRefreshListView refreshListView;
    public ListView listView;//数据视图

    public BaseAdapter adapter;//数据适配器

    public LinearLayout loading_layout;//圈圈页
   // public LinearLayout list_layout;//数据页
    public LinearLayout error_layout;//错误页

    public ImageView image_error;//错误页icon
    public TextView text_error;//错误页info

    private String processURL;//数据请求地址

    private HashMap<String, String> requestParams;//数据请求参数

    public int index = 0;//当前第几页

    protected int pageSize = 10;//每一页的数据

    private boolean isFirstLoading = true;//是否是第一次请求

    private Class<?> targetModel;//目标model类

    public boolean isParse = false;//是否在子类解析json

    public int isShowError = 0;//是否显示错误页  1：错误页   2：空数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmContentLayout();

        //得到配置数据  抽象方法
        setProfileData();

        //得到listview视图
        refreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        listView = refreshListView.getRefreshableView();

        initmListView(listView);

        //得到圈圈页 错误页 内容页
        loading_layout = (LinearLayout) findViewById(R.id.loading_layout);
        //list_layout = (LinearLayout) findViewById(R.id.layout_list);
        error_layout = (LinearLayout) findViewById(R.id.error_layout);

        image_error = (ImageView) findViewById(R.id.image_errorView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dataItemClick(position-1);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dataLongItemClick(position - 1);
                return true;
            }
        });

        //添加更新，加载
        refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            //下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                doRefresh(refreshView);
            }

            //上拉加载
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                doloadmore(refreshView);
            }

        });

        //初始化空页事件和错误页事件
        image_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowError == 1) {
                    retryClick();
                } else if (isShowError == 2) {
                    nodataClick();
                }
            }
        });

        initView();
        getDataFromServer();
        showLoadingView();

    }

    /**
     * 重新获取数据
     */
    public void retryClick(){
        isFirstLoading = true;
        index = 0;
        showLoadingView();
        getDataFromServer();
    }

    /**
     * 空数据触发
     */
    public void nodataClick(){
        isFirstLoading = true;
        index = 0;
        showLoadingView();
        getDataFromServer();
    }


    protected void setmContentLayout() {
        setContentLayout(R.layout.base_layout);
    }

    public void initmListView(ListView listView) {
    }

    protected void doloadmore(PullToRefreshBase<ListView> refreshView) {
        String label = DateUtils.formatDateTime(context,
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        // 更新最后一次刷新时间
        refreshView.getLoadingLayoutProxy()
                .setLastUpdatedLabel(label);
        index++;
        getDataFromServer();
    }

    protected void doRefresh(PullToRefreshBase<ListView> refreshView) {
        String label = DateUtils.formatDateTime(context,
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        // 更新最后一次刷新时间
        refreshView.getLoadingLayoutProxy()
                .setLastUpdatedLabel(label);

        //设置初始化
        index = 0;
        getDataFromServer();
    }

   protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INIT_DATA:
                    initData();
                    showContentView();
                    break;
                case HIDEPROCESS:
                    refreshListView.onRefreshComplete();
                    break;
            }
        }
    };

    @Override
    protected void initNavBar() {

    }

    /**
     * 显示圈圈页
     */
    public void showLoadingView() {
        loading_layout.setVisibility(View.VISIBLE);
        refreshListView.setVisibility(View.GONE);
        error_layout.setVisibility(View.GONE);
    }

    /**
     * 显示数据页
     */
    public void showContentView() {
        loading_layout.setVisibility(View.GONE);
        refreshListView.setVisibility(View.VISIBLE);
        error_layout.setVisibility(View.GONE);
        loading_layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                doSetListViewModel();

            }
        }, 200);
    }

    /**
     * 显示错误页 和空白页
     */
    public void showErrorView() {
        isShowError = 1;
        //image_error.setBackgroundResource(R.drawable.no_network);
        loading_layout.setVisibility(View.GONE);
        refreshListView.setVisibility(View.GONE);
        error_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 显示空白页
     */
    public void showNodataView(){
        isShowError = 2;
        //image_error.setBackgroundResource(R.drawable.nodata);
        loading_layout.setVisibility(View.GONE);
        refreshListView.setVisibility(View.GONE);
        error_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 从服务器获取数据
     */
    public void getDataFromServer() {

        HashMap<String,String> params  = new HashMap<String,String>();

        requestParams = setParams();
        if(requestParams!=null&&requestParams.size()>0){
            int length = requestParams.size();
            for (String key : requestParams.keySet()) {
                if (requestParams.get(key) != null) {
                    String value = requestParams.get(key);
                    params.put(key,value);
                }
            }
        }

        params.put("limit", String.valueOf(pageSize));
        setOffSet(params);
//        params.put("limit", String.valueOf(pageSize));
//        params.put("offset", String.valueOf(index * pageSize));

        ConnectionService.getInstance().serviceConnUseGet(context, processURL, params, new StringCallBack() {
            @Override
            public void getSuccessString(String Result) {
                processResult(Result);
                try {
                    JSONObject jsonObject = new JSONObject(Result);
                    boolean check = parseJson.isCommon(jsonObject);
                    if (check) {
                        List<Object> dataList = new ArrayList<Object>();
                        if (isParse) {
                            dataList = parseDataJson(jsonObject);
                        } else {
                            dataList = parseJson.getModelList(jsonObject, targetModel);
                        }

                        if (dataList != null && dataList.size() > 0) {
                            addData(dataList);
                            handler.sendEmptyMessage(INIT_DATA);
                        } else {
                            if (index == 0) {
                                showNodataView();
                            }
                        }
                        isFirstLoading = false;
                    } else {
                        if (isFirstLoading) {
                            showErrorView();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (isFirstLoading) {
                        showErrorView();
                    }
                }
                finally {
                    hideDialog();
                }

                handler.sendEmptyMessage(HIDEPROCESS);

                doRefreshEnd();
            }

            @Override
            public void getError() {
                hideDialog();
            }
        });
    }

    protected void setOffSet(HashMap<String, String> params) {
        params.put("from", String.valueOf(index * pageSize));
    }

    protected void doRefreshEnd() {

    }

    /***
     * 子类可以处理里面的内容
     * @param result
     */
    protected void processResult(String result) {

    }

    /**
     * 添加数据
     *
     * @param dataList
     */
    public void addData(List<Object> dataList) {

        if (modelList == null) {
            modelList = new ArrayList<Object>();
        }
        if (index == 0) {
            modelList.clear();
        }

        int length = dataList.size();
        for (int i = 0; i < length; i++) {
            modelList.add(dataList.get(i));
        }
    }

    /**
     * 初始化列表数据
     */
    @Override
    public void initData() {
        if (index == 0) {
            adapter = setAdapter();
            listView.setAdapter(adapter);

//            ToastUtils.showCustomToast(context,"setadapter");
            //adapter.notifyDataSetChanged();
        } else {
            adapter.notifyDataSetChanged();

        }
    }

    protected void doSetListViewModel() {
    }

    @Override
    public void rightNavClick() {

    }

    @Override
    public void setOnRightBtnClickListener(View v) {

    }



    /**
     * 设置属性数据
     */
    public void setProfileData() {

        processURL = setProcessURL();
        targetModel = setModelClass();

    }

    /**
     * 设置数据列表的触发
     */
    public abstract void dataItemClick(int position);

    /**
     * 长按触发事件
     *
     * @param position
     */
    public abstract void dataLongItemClick(int position);

    /**
     * 设置数据适配器
     */
    public abstract BaseAdapter setAdapter();

    /**
     * 设置url地址
     *
     * @return
     */
    public abstract String setProcessURL();

    /**
     * 设置请求参数
     *
     * @return
     */
    public abstract HashMap<String, String> setParams();

    /**
     * 设置目标model
     *
     * @return
     */
    public abstract Class<?> setModelClass();

    /**
     * 父类解析json对象
     * @param jsonObject
     */
    public abstract List<Object> parseDataJson(JSONObject jsonObject);
}
