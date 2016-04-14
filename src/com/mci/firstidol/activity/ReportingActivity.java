package com.mci.firstidol.activity;

import com.mci.firstidol.R;
import com.mci.firstidol.view.ToastUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ReportingActivity extends Activity implements OnClickListener{
	private ImageButton title_back;//返回
	private Button title_submit_no;//提交
	private TextView title_text_no;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reporting);
		setTitle();
	}
	
	
	
	private void setTitle(){
		title_back = (ImageButton) findViewById(R.id.title_back);
		title_submit_no = (Button) findViewById(R.id.title_submit_no);
		title_text_no = (TextView) findViewById(R.id.title_text_no);
		title_text_no.setText("举报须知");
		title_submit_no.setVisibility(View.GONE);
		title_back.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		default:
			break;
		}
	}
}
