package com.mci.firstidol.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.utils.ConnectionService;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.ToastUtils;

public class UploadStarTripActivity extends BaseActivity implements
		OnClickListener {

	private final int SHOW_DATAPICK = 0;
	private final int DATE_DIALOG_ID = 1;

	private TextView text_time;// 时间
	private TextView text_address;// 地点
	private TextView text_event;// 事件

	private Button btn_upload;// 上传
	
	private int mYear;// 年
	private int mMonth;// 月
	private int mDay;// 日
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//时间格式化格式

	@Override
	protected int getViewId() {
		return R.layout.activity_uploadtrip;
	}

	@Override
	protected void initNavBar() {
		setTitle(R.string.add_star_trip);
	}

	@Override
	protected void initView() {
		text_time = (TextView) findViewById(R.id.edit_time);
		text_address = (TextView) findViewById(R.id.edit_address);
		text_event = (TextView) findViewById(R.id.edit_event);

		btn_upload = (Button) findViewById(R.id.btn_upload);
		text_time.setOnClickListener(this);
		btn_upload.setOnClickListener(this);

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public void rightNavClick() {

	}

	@Override
	protected void findViewById() {

	}

	@Override
	protected void initData() {

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_DATAPICK:
				showDialog(DATE_DIALOG_ID);
				break;

			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_upload:
			uploadTripClick();
			break;
		case R.id.edit_time:
			handler.sendEmptyMessage(SHOW_DATAPICK);
			break;
		default:
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}

		return null;
	}

	/**
	 * 设置日期
	 */
	private void setDateTime() {
		final Calendar c = Calendar.getInstance();

		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		updateDateDisplay();
	}

	/**
	 * 更新日期显示
	 */
	private void updateDateDisplay() {
		text_time.setText(new StringBuilder().append(mYear).append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-").append((mDay < 10) ? "0" + mDay : mDay));
	}

	/**
	 * 日期控件的事件
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			updateDateDisplay();
		}
	};
	
	/**
	 * 上传事件触发
	 */
	public void uploadTripClick(){
		String timeStr = text_time.getText().toString().trim();
		String addressStr = text_address.getText().toString().trim();
		String eventStr = text_event.getText().toString().trim();
		
		boolean check = true;
		
		if(timeStr==null||timeStr.equals("")){
			check = false;
			ToastUtils.showCustomToast(context, "请选择时间");
			return;
		}
		
		if(addressStr==null||addressStr.equals("")){
			check = false;
			ToastUtils.showCustomToast(context, "请输入地点");
			return;
		}
		
		if(eventStr==null||eventStr.equals("")){
			check = false;
			ToastUtils.showCustomToast(context, "请输入事件");
			return;
		}
		
		if(check){
			try {
				Date date = sdf.parse(timeStr);
				String tripData = "/Date("+String.valueOf(date.getTime())+")/";
				applyStarTrip(eventStr, addressStr, tripData);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 添加明星行程
	 * @param TripContent
	 * @param Address
	 * @param TripDate
	 */
	public void applyStarTrip(String TripContent,String Address,String TripDate){
		pd.setMessage("添加行程");
		baseHandler.sendEmptyMessage(SHOWDIALOG);
		String processURL = Constant.RequestContstants.Request_add_trip;
		HashMap<String, String> params = new HashMap<String, String>();
		if (Constant.starModel != null) {
			params.put("StarId", String.valueOf(Constant.starModel.starId));
		}
		params.put("TripContent", TripContent);
		params.put("Address", Address);
		params.put("TripDate", TripDate);
		
		String paramStr = Utily.map2json(params);
		paramStr = "{\"trip\":" + paramStr + "}";
		Log.e("pnl", paramStr);
		
		ConnectionService.getInstance().serviceConn(context, processURL,
				paramStr, new StringCallBack() {

					@Override
					public void getSuccessString(String Result) {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						try {
							JSONObject jsonObject = new JSONObject(Result);
							boolean isCommon = parseJson.isCommon(jsonObject);
							if (isCommon) {
								ToastUtils.showCustomToast(context, "添加行程成功");
								exitThis();
							} else {
								ToastUtils.showCustomToast(context, "添加行程失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCustomToast(context, "添加行程失败");
						}
					}

					@Override
					public void getError() {
						baseHandler.sendEmptyMessage(HIDEDIALOG);
						ToastUtils.showCustomToast(context, "添加行程失败");
					}
			
		});
		
	}

}
