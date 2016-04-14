package com.mci.firstidol.base;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import com.mci.firstidol.R;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.utils.ConnectionService;

import java.util.HashMap;
import java.util.List;


/**
 * Created by wang on 2015/6/15.
 */
public abstract class BaseReflushActivity extends BaseActivity{

    public LinearLayout loading_layout;//圈圈页
    public LinearLayout content_layout;//数据页
    public LinearLayout error_layout;//错误页

    public ImageView image_error;//错误页icon
    public TextView text_error;//错误页info

    public String processURL;//路径地址
    private HashMap<String,String> requestParams;//请求参数

    public Class<?> targetModel;//目标类
    public Object modelObject;//目标对象

    public boolean getDataNet = true;//是否从网络拉取数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.base_content_layout);

        initNavBar();
        //得到圈圈页 错误页 内容页
        loading_layout = (LinearLayout) findViewById(R.id.loading_layout);
        content_layout = (LinearLayout) findViewById(R.id.layout_content);
        error_layout = (LinearLayout) findViewById(R.id.error_layout);
        image_error = (ImageView) findViewById(R.id.image_errorView);
        view_topline.setVisibility(View.VISIBLE);

        initView();

        processURL = getProcessURL();
        targetModel = setModelClass();

        if(getDataNet){
            showLoadingView();
            getDataFromServer();
        }

        image_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingView();
                getDataFromServer();
            }
        });
    }
    
    @Override
    protected int getViewId() {
    	// TODO Auto-generated method stub
    	return R.layout.base_content_layout;
    }

    /**
     * 显示圈圈页
     */
    public void showLoadingView() {
        loading_layout.setVisibility(View.VISIBLE);
        content_layout.setVisibility(View.GONE);
        error_layout.setVisibility(View.GONE);
    }

    /**
     * 显示数据页
     */
    public void showContentView() {
        loading_layout.setVisibility(View.GONE);
        content_layout.setVisibility(View.VISIBLE);
        error_layout.setVisibility(View.GONE);
    }

    /**
     * 显示错误页 和空白页
     */
    public void showErrorView() {
        image_error.setBackgroundResource(R.drawable.no_network);
        loading_layout.setVisibility(View.GONE);
        content_layout.setVisibility(View.GONE);
        error_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 设置底部
     * @param resId
     */
    public void setContentLayoutView(int resId){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View footer = inflater.inflate(resId, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        content_layout.addView(footer, layoutParams);
    }


    @Override
    public void setOnRightBtnClickListener(View v) {

    }

    /**
     * 从服务器获取数据
     */
    public void getDataFromServer() {

        requestParams = getRequestParams();

        ConnectionService.getInstance().serviceConnUseGet(context, processURL, requestParams, new StringCallBack() {
            @Override
            public void getSuccessString(String Result) {
                try {
                    JSONObject jsonObject = new JSONObject(Result);
                    boolean check = parseJson.isCommon(jsonObject);
                    if (check) {
                        modelObject = parseJson.getModelObject(jsonObject,targetModel);
                        showContentView();
                        initDataView();
                    }else{
                        showErrorView();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorView();
                }
            }

            @Override
            public void getError() {
                showErrorView();
            }
        });
    }

    /**
     * 得到请求地址
     * @return
     */
    public abstract String getProcessURL();

    /**
     * 得到请求参数
     * @return
     */
    public abstract HashMap<String,String> getRequestParams();

    /**
     * 设置目标model
     *
     * @return
     */
    public abstract Class<?> setModelClass();

    /**
     * 初始化数据页面
     */
    public abstract void initDataView();

}
