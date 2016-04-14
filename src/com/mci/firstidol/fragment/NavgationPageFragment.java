package com.mci.firstidol.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.mci.firstidol.R;
import com.mci.firstidol.activity.MainActivity;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NavgationPageFragment extends Fragment implements OnClickListener{
	private static final String KEY_CONTENT = "NavgationPageFragment:DrawleID";
	private  int drawleID;
	private ImageView imageView;
	private Button btn_go;
	private View view;
	public static  NavgationPageFragment newInstance(int drawleId) {
		NavgationPageFragment fragment = new NavgationPageFragment();
//		drawleID = drawleId;
		
		fragment.drawleID = drawleId;
  
        return fragment;
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
        	drawleID = savedInstanceState.getInt(KEY_CONTENT);
        }
    }
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CONTENT, drawleID);
    }
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        view = inflater.inflate(R.layout.layout_fragment_navgation_page, null);
        findView();
        initView();
        initData();
        return view;
    }

	

	@SuppressWarnings("deprecation")
	protected void findView() {
		// TODO Auto-generated method stub
		imageView = (ImageView) view.findViewById(R.id.iv_nav_show);
		btn_go = (Button) view.findViewById(R.id.btn_go);
		
//		imageView.setBackgroundResource(drawleID);
//		imageView.setBackgroundDrawable(getResources().getDrawable(drawleID));
	}
	
	protected void initView() {
		// TODO Auto-generated method stub
		if (drawleID == R.drawable.new_feature_3) {//最后一张
			btn_go.setVisibility(View.VISIBLE);
			btn_go.setOnClickListener(this);
		} else {
			btn_go.setVisibility(View.GONE);
		}
		
		
	}
	
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		goToMain();
	}
	
	/**
	 * 跳往主页面
	 */
	public void goToMain() {
		getActivity().finish();
		Utily.go2Activity(getActivity(), MainActivity.class);
	}

}
