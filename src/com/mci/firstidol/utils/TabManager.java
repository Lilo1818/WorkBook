package com.mci.firstidol.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by wang on 2015/6/8.
 */
@SuppressLint("NewApi")
public class TabManager implements OnTabChangeListener {

    private FragmentActivity mActivity;
    private Fragment fragment_parent;//父类的fragment

    // 保存tab
    private Map<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
    private TabHost mTabHost;
    private int mContainerID;
    private TabInfo mLastTab;
    private Context context;

    private ChangeTable changeTable;

    /**
     * @param
     * @param tabHost     tab
     * @param containerID fragment's parent note
     */
    public TabManager(FragmentActivity activity, Fragment fragment_parent, TabHost tabHost,
                      int containerID, Context context,ChangeTable changeTable) {
        mActivity = activity;
        mTabHost = tabHost;
        mContainerID = containerID;
        mTabHost.setOnTabChangedListener(this);
        this.fragment_parent = fragment_parent;
        this.context = context;
        this.changeTable = changeTable;
    }

    /**
     * @param
     * @param tabHost     tab
     * @param containerID fragment's parent note
     */
    public TabManager(FragmentActivity activity, Fragment fragment_parent, TabHost tabHost,
                      int containerID, Context context) {
        mActivity = activity;
        mTabHost = tabHost;
        mContainerID = containerID;
        mTabHost.setOnTabChangedListener(this);
        this.fragment_parent = fragment_parent;
        this.context = context;
    }

    class TabInfo {
        private final String tag;
        private final Fragment clss;
        private final Bundle args;
        private Fragment fragment;

        TabInfo(String _tag, Fragment fragemnt, Bundle _args) {
            tag = _tag;
            clss = fragemnt;
            args = _args;
        }
    }

    static class TabFactory implements TabHost.TabContentFactory {
        private Context mContext;

        TabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumHeight(0);
            v.setMinimumWidth(0);
            return v;
        }
    }

    // 加入tab
    public void addTab(TabHost.TabSpec tabSpec, Fragment clss, Bundle args) {
        if (mActivity != null) {
            tabSpec.setContent(new TabFactory(mActivity));
        } else {
            tabSpec.setContent(new TabFactory(context));
        }
        String tag = tabSpec.getTag();

        TabInfo info = new TabInfo(tag, clss, args);
        FragmentManager fm = null;
        if (mActivity != null) {
            fm = mActivity.getSupportFragmentManager();
        } else {
            fm = fragment_parent.getChildFragmentManager();
        }
        info.fragment = fm.findFragmentByTag(tag);
        // isDetached分离状态
        if (info.fragment != null && !info.fragment.isDetached()) {
            FragmentTransaction ft = fm.beginTransaction();

            ft.detach(info.fragment);
//            ft.commit();
            ft.commitAllowingStateLoss();
        }
        mTabs.put(tag, info);
        mTabHost.addTab(tabSpec);
    }

    /**
     * 清空所有的tab
     */
    public void clearTab() {
        FragmentManager fm = null;
        if (mActivity != null) {
            fm = mActivity.getSupportFragmentManager();
        } else {
            fm = fragment_parent.getChildFragmentManager();
        }
        FragmentTransaction ft = fm.beginTransaction();

        List<Fragment> fragments = fm.getFragments();
        if (fragments != null && fragments.size() > 0) {
            int size = fragments.size();
            for (int i = 0; i < size; i++) {
                ft.remove(fragments.get(i));
            }
//            mTabHost.setCurrentTab(1);
            mTabHost.clearAllTabs();
        }

    }

    @Override
    public void onTabChanged(String tabId) {
        if(changeTable!=null){
            changeTable.getChangeTitle(tabId);
        }
        TabInfo newTab = mTabs.get(tabId);
        if (mLastTab != newTab) {

            FragmentManager fragmentManager = null;
            if (mActivity != null) {
                fragmentManager = mActivity.getSupportFragmentManager();
            } else {
                fragmentManager = fragment_parent.getChildFragmentManager();
            }

            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();

            // 脱离之前的tab
            if (mLastTab != null && mLastTab.fragment != null) {
                fragmentTransaction.detach(mLastTab.fragment);
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = newTab.clss;
                    fragmentTransaction.add(mContainerID, newTab.fragment,
                            newTab.tag);
                } else {
                    // 激活
                    fragmentTransaction.attach(newTab.fragment);
                }
            }
            mLastTab = newTab;
            fragmentTransaction.commit();
  //          fragmentManager.executePendingTransactions();
        }
    }

    public interface ChangeTable{
        public void getChangeTitle(String title);
    }

}
