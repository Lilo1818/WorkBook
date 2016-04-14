package com.mci.firstidol.base;

import com.mci.firstidol.R;
import com.mci.firstidol.utils.AsyncTaskUtils;
import com.mci.firstidol.utils.AsyncTaskUtils.Callback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class BaseTempFragment extends Fragment {
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.i("xxxxxxxxxxxxxxx", "BaseFragment onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("xxxxxxxxxxxxxxx", "BaseFragment onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("xxxxxxxxxxxxxxx", "BaseFragment onDestroy");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.i("xxxxxxxxxxxxxxx", "BaseFragment onDestroyView");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.i("xxxxxxxxxxxxxxx", "BaseFragment onDetach");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("xxxxxxxxxxxxxxx", "BaseFragment onPause");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("xxxxxxxxxxxxxxx", "BaseFragment onResume");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("xxxxxxxxxxxxxxx", "BaseFragment onStop");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		Log.i("xxxxxxxxxxxxxxx", "BaseFragment onSaveInstanceState");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

//		initNavInfo();
//		findViewById();
//		initView();
//		initData();

		Log.i("xxxxxxxxxxxxxxx", "BaseFragment onStart");
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);

		if (!hidden) {
			// 相当于Fragment的onResume
			Log.i("xxxxxxxxxxxxxxx", "Fragment--- onResume");
//			initNavInfo();
		} else {
			// 相当于Fragment的onPause
			Log.i("xxxxxxxxxxxxxxx", "Fragment--- onPause");
		}
	}
	
	
	protected <T> void doAsync(final boolean isShowPorgress,
			final CharSequence requestID, final Object requestObj,
			final Callback<Exception> pExceptionCallback,
			final Callback<T> pCallback) {
		
		AsyncTaskUtils.doAsync(getActivity(), null,
				getActivity().getResources().getString(R.string.request_loading), requestID,
				requestObj, pCallback, pExceptionCallback, false,
				isShowPorgress);
	}
}
