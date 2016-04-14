package com.mci.firstidol.listener;

import android.R.integer;

public interface SquareFragmentListener {
//	public void pullLoadMore(int fragmentIndex,int pageIndex);
//	public void pullRefresh(int fragmentIndex,int pageIndex);
	/**
	 * 
	 * @param isRefresh 是否是下拉刷新
	 * @param fragmentIndex 当前主 fragment 的位置
	 * @param pageIndex 当前页面索引
	 * @param maxDate 最大日期/分类
	 * @param currentPosition 当前子 fragment 的位置，如果没有子 Fragment，此参数无效
	 */
	public void pullRefresh(boolean isRefresh, int fragmentIndex,int pageIndex,String maxDate,int currentPosition);
}
