package com.mci.firstidol.activity;

import java.util.HashMap;

import org.json.JSONObject;

import com.mci.firstidol.R;
import com.mci.firstidol.R.color;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.ToastUtils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ReportActivity extends Activity implements OnClickListener{
	
	private ImageButton title_back;//返回
	private Button title_submit_no;//提交
	
	private RelativeLayout report_layout_no,report_layout_no1,report_layout_no2,
							report_layout_no3,report_layout_no4,report_layout_no5,report_layout_no6,
							report_layout_no7,report_layout_no8;//（色情低俗，广告骚扰，诱导分享，谣言，政治敏感，违法，其他，侵权举报，售假投诉）
	
	private ImageView report_image , report_image1 ,report_image2,report_image3,report_image4,report_image5,report_image6,report_image7,report_image8;
	
	private Button report_but_no;	
	
	
	private String ActName;
	private String ActType;
	private String RefId;
	private String ModType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		setTitle();
		init();
	}
	private void init(){
		report_layout_no = (RelativeLayout) findViewById(R.id.report_layout_no);//色情低俗
		report_layout_no1 = (RelativeLayout) findViewById(R.id.report_layout_no1);//广告骚扰
		report_layout_no2 = (RelativeLayout) findViewById(R.id.report_layout_no2);//诱导分享
		report_layout_no3 = (RelativeLayout) findViewById(R.id.report_layout_no3);//谣言
		report_layout_no4 = (RelativeLayout) findViewById(R.id.report_layout_no4);//政治蜜柑
		report_layout_no5 = (RelativeLayout) findViewById(R.id.report_layout_no5);//违法
		report_layout_no6 = (RelativeLayout) findViewById(R.id.report_layout_no6);//其他
		report_layout_no7 = (RelativeLayout) findViewById(R.id.report_layout_no7);//侵权
		report_layout_no8 = (RelativeLayout) findViewById(R.id.report_layout_no8);//售假
		
		report_image = (ImageView) findViewById(R.id.report_image);
		report_image1 = (ImageView) findViewById(R.id.report_image1);
		report_image2 = (ImageView) findViewById(R.id.report_image2);
		report_image3 = (ImageView) findViewById(R.id.report_image3);
		report_image4 = (ImageView) findViewById(R.id.report_image4);
		report_image5 = (ImageView) findViewById(R.id.report_image5);
		report_image6 = (ImageView) findViewById(R.id.report_image6);
		report_image7 = (ImageView) findViewById(R.id.report_image7);
		report_image8 = (ImageView) findViewById(R.id.report_image8);
		
		report_layout_no.setOnClickListener(this);
		report_layout_no1.setOnClickListener(this);
		report_layout_no2.setOnClickListener(this);
		report_layout_no3.setOnClickListener(this);
		report_layout_no4.setOnClickListener(this);
		report_layout_no5.setOnClickListener(this);
		report_layout_no6.setOnClickListener(this);
		report_layout_no7.setOnClickListener(this);
		report_layout_no8.setOnClickListener(this);
		report_but_no = (Button) findViewById(R.id.report_but_no);
		report_but_no.setOnClickListener(this);
	}
	
	private void setTitle(){
		title_back = (ImageButton) findViewById(R.id.title_back);
		title_submit_no = (Button) findViewById(R.id.title_submit_no);
		title_back.setOnClickListener(this);
		title_submit_no.setOnClickListener(this);
	}

	//请求举报的方法
	private void RegiestReport(String ModType,String RefId,String ActType,String ActName){
		String processURL = Constant.RequestContstants.Report;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("ModType", ModType);
		params.put("RefId", RefId);
		params.put("ActType", ActType);
		params.put("ActName", ActName);
		String paramStr = Utily.map2json(params);
		paramStr = "{\"action\":" + paramStr + "}";
		ConnectionService.getInstance().serviceConn(this, processURL,
				paramStr, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean isCommon = jsonObject.getBoolean("isSuc");
							if (isCommon) {
								ToastUtils.showCustomToast(ReportActivity.this, "举报成功");
								finish();
							} else {
								ToastUtils.showCustomToast(ReportActivity.this, "举报失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCustomToast(ReportActivity.this, "举报失败");
						}
					}

					@Override
					public void getError() {
						ToastUtils.showCustomToast(ReportActivity.this, "举报失败");
					}
				});
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.title_submit_no:
			//ToastUtils.show(getApplicationContext(), "你点击了提交");
			ModType = getIntent().getExtras().getString("ModType");
			RefId = String.valueOf(getIntent().getExtras().getLong("RefId"));
			RegiestReport(ModType,RefId,ActType,ActName);
			break;
		case R.id.report_but_no:
			Bundle bundle = new Bundle();
    		Utily.go2Activity(this, ReportingActivity.class,
    				bundle);	
			break;
			
		case R.id.report_layout_no:
			//ToastUtils.show(getApplicationContext(), "色情低俗");
			ActName = "色情低俗";
			ActType = "1";
			report_layout_no.setBackgroundColor(color.crimson);
			report_layout_no1.setBackgroundColor(Color.WHITE);
			report_layout_no2.setBackgroundColor(Color.WHITE);
			report_layout_no3.setBackgroundColor(Color.WHITE);
			report_layout_no4.setBackgroundColor(Color.WHITE);
			report_layout_no5.setBackgroundColor(Color.WHITE);
			report_layout_no6.setBackgroundColor(Color.WHITE);
			report_layout_no7.setBackgroundColor(Color.WHITE);
			report_layout_no8.setBackgroundColor(Color.WHITE);
			
			report_image.setVisibility(View.VISIBLE);
			report_image1.setVisibility(View.GONE);
			report_image2.setVisibility(View.GONE);
			report_image3.setVisibility(View.GONE);
			report_image4.setVisibility(View.GONE);
			report_image5.setVisibility(View.GONE);
			report_image6.setVisibility(View.GONE);
			report_image7.setVisibility(View.GONE);
			report_image8.setVisibility(View.GONE);
			break;
		case R.id.report_layout_no1:
			//ToastUtils.show(getApplicationContext(), "举报须知");
			ActName = "广告骚扰";
			ActType = "2";
			report_layout_no1.setBackgroundColor(color.crimson);
			report_layout_no.setBackgroundColor(Color.WHITE);
			report_layout_no2.setBackgroundColor(Color.WHITE);
			report_layout_no3.setBackgroundColor(Color.WHITE);
			report_layout_no4.setBackgroundColor(Color.WHITE);
			report_layout_no5.setBackgroundColor(Color.WHITE);
			report_layout_no6.setBackgroundColor(Color.WHITE);
			report_layout_no7.setBackgroundColor(Color.WHITE);
			report_layout_no8.setBackgroundColor(Color.WHITE);
			report_image1.setVisibility(View.VISIBLE);
			report_image.setVisibility(View.GONE);
			report_image2.setVisibility(View.GONE);
			report_image3.setVisibility(View.GONE);
			report_image4.setVisibility(View.GONE);
			report_image5.setVisibility(View.GONE);
			report_image6.setVisibility(View.GONE);
			report_image7.setVisibility(View.GONE);
			report_image8.setVisibility(View.GONE);
			break;
		case R.id.report_layout_no2:
			ActName = "诱导分享";
			ActType = "3";
			report_layout_no2.setBackgroundColor(color.crimson);
			report_layout_no.setBackgroundColor(Color.WHITE);
			report_layout_no1.setBackgroundColor(Color.WHITE);
			report_layout_no3.setBackgroundColor(Color.WHITE);
			report_layout_no4.setBackgroundColor(Color.WHITE);
			report_layout_no5.setBackgroundColor(Color.WHITE);
			report_layout_no6.setBackgroundColor(Color.WHITE);
			report_layout_no7.setBackgroundColor(Color.WHITE);
			report_layout_no8.setBackgroundColor(Color.WHITE);
			report_image2.setVisibility(View.VISIBLE);
			report_image.setVisibility(View.GONE);
			report_image1.setVisibility(View.GONE);
			report_image3.setVisibility(View.GONE);
			report_image4.setVisibility(View.GONE);
			report_image5.setVisibility(View.GONE);
			report_image6.setVisibility(View.GONE);
			report_image7.setVisibility(View.GONE);
			report_image8.setVisibility(View.GONE);
			break;
		case R.id.report_layout_no3:
			ActName = "谣言";
			ActType = "4";
			report_layout_no3.setBackgroundColor(color.crimson);
			report_layout_no.setBackgroundColor(Color.WHITE);
			report_layout_no2.setBackgroundColor(Color.WHITE);
			report_layout_no1.setBackgroundColor(Color.WHITE);
			report_layout_no4.setBackgroundColor(Color.WHITE);
			report_layout_no5.setBackgroundColor(Color.WHITE);
			report_layout_no6.setBackgroundColor(Color.WHITE);
			report_layout_no7.setBackgroundColor(Color.WHITE);
			report_layout_no8.setBackgroundColor(Color.WHITE);
			report_image3.setVisibility(View.VISIBLE);
			report_image.setVisibility(View.GONE);
			report_image2.setVisibility(View.GONE);
			report_image1.setVisibility(View.GONE);
			report_image4.setVisibility(View.GONE);
			report_image5.setVisibility(View.GONE);
			report_image6.setVisibility(View.GONE);
			report_image7.setVisibility(View.GONE);
			report_image8.setVisibility(View.GONE);
			break;
		case R.id.report_layout_no4:
			ActName = "政治敏感";
			ActType = "5";
			report_layout_no4.setBackgroundColor(color.crimson);
			report_layout_no.setBackgroundColor(Color.WHITE);
			report_layout_no2.setBackgroundColor(Color.WHITE);
			report_layout_no3.setBackgroundColor(Color.WHITE);
			report_layout_no1.setBackgroundColor(Color.WHITE);
			report_layout_no5.setBackgroundColor(Color.WHITE);
			report_layout_no6.setBackgroundColor(Color.WHITE);
			report_layout_no7.setBackgroundColor(Color.WHITE);
			report_layout_no8.setBackgroundColor(Color.WHITE);
			report_image4.setVisibility(View.VISIBLE);
			report_image.setVisibility(View.GONE);
			report_image2.setVisibility(View.GONE);
			report_image3.setVisibility(View.GONE);
			report_image1.setVisibility(View.GONE);
			report_image5.setVisibility(View.GONE);
			report_image6.setVisibility(View.GONE);
			report_image7.setVisibility(View.GONE);
			report_image8.setVisibility(View.GONE);
			break;
		case R.id.report_layout_no5:
			ActName = "违法（暴力恐怖，违禁品等）";
			ActType = "6";
			report_layout_no5.setBackgroundColor(color.crimson);
			report_layout_no.setBackgroundColor(Color.WHITE);
			report_layout_no2.setBackgroundColor(Color.WHITE);
			report_layout_no3.setBackgroundColor(Color.WHITE);
			report_layout_no4.setBackgroundColor(Color.WHITE);
			report_layout_no1.setBackgroundColor(Color.WHITE);
			report_layout_no6.setBackgroundColor(Color.WHITE);
			report_layout_no7.setBackgroundColor(Color.WHITE);
			report_layout_no8.setBackgroundColor(Color.WHITE);
			report_image5.setVisibility(View.VISIBLE);
			report_image.setVisibility(View.GONE);
			report_image2.setVisibility(View.GONE);
			report_image3.setVisibility(View.GONE);
			report_image4.setVisibility(View.GONE);
			report_image1.setVisibility(View.GONE);
			report_image6.setVisibility(View.GONE);
			report_image7.setVisibility(View.GONE);
			report_image8.setVisibility(View.GONE);
			break;
		case R.id.report_layout_no6:
			ActName = "其他（收集隐私信息等）";
			ActType = "7";
			report_layout_no6.setBackgroundColor(color.crimson);
			report_layout_no.setBackgroundColor(Color.WHITE);
			report_layout_no2.setBackgroundColor(Color.WHITE);
			report_layout_no3.setBackgroundColor(Color.WHITE);
			report_layout_no4.setBackgroundColor(Color.WHITE);
			report_layout_no5.setBackgroundColor(Color.WHITE);
			report_layout_no1.setBackgroundColor(Color.WHITE);
			report_layout_no7.setBackgroundColor(Color.WHITE);
			report_layout_no8.setBackgroundColor(Color.WHITE);
			report_image6.setVisibility(View.VISIBLE);
			report_image.setVisibility(View.GONE);
			report_image2.setVisibility(View.GONE);
			report_image3.setVisibility(View.GONE);
			report_image4.setVisibility(View.GONE);
			report_image5.setVisibility(View.GONE);
			report_image1.setVisibility(View.GONE);
			report_image7.setVisibility(View.GONE);
			report_image8.setVisibility(View.GONE);
			break;
		case R.id.report_layout_no7:
			ActName = "侵权举报（诽谤，抄袭，冒用。。）";
			ActType = "8";
			report_layout_no7.setBackgroundColor(color.crimson);
			report_layout_no.setBackgroundColor(Color.WHITE);
			report_layout_no2.setBackgroundColor(Color.WHITE);
			report_layout_no3.setBackgroundColor(Color.WHITE);
			report_layout_no4.setBackgroundColor(Color.WHITE);
			report_layout_no5.setBackgroundColor(Color.WHITE);
			report_layout_no6.setBackgroundColor(Color.WHITE);
			report_layout_no1.setBackgroundColor(Color.WHITE);
			report_layout_no8.setBackgroundColor(Color.WHITE);
			report_image7.setVisibility(View.VISIBLE);
			report_image.setVisibility(View.GONE);
			report_image2.setVisibility(View.GONE);
			report_image3.setVisibility(View.GONE);
			report_image4.setVisibility(View.GONE);
			report_image5.setVisibility(View.GONE);
			report_image6.setVisibility(View.GONE);
			report_image1.setVisibility(View.GONE);
			report_image8.setVisibility(View.GONE);
			break;
		case R.id.report_layout_no8:
			ActName = "售假投诉";
			ActType = "9";
			report_layout_no8.setBackgroundColor(color.crimson);
			report_layout_no.setBackgroundColor(Color.WHITE);
			report_layout_no2.setBackgroundColor(Color.WHITE);
			report_layout_no3.setBackgroundColor(Color.WHITE);
			report_layout_no4.setBackgroundColor(Color.WHITE);
			report_layout_no5.setBackgroundColor(Color.WHITE);
			report_layout_no6.setBackgroundColor(Color.WHITE);
			report_layout_no7.setBackgroundColor(Color.WHITE);
			report_layout_no1.setBackgroundColor(Color.WHITE);
			report_image8.setVisibility(View.VISIBLE);
			report_image.setVisibility(View.GONE);
			report_image2.setVisibility(View.GONE);
			report_image3.setVisibility(View.GONE);
			report_image4.setVisibility(View.GONE);
			report_image5.setVisibility(View.GONE);
			report_image6.setVisibility(View.GONE);
			report_image7.setVisibility(View.GONE);
			report_image1.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

}
