package com.mci.firstidol.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mci.firstidol.R;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.utils.GsonUtils;
import com.mci.firstidol.utils.PreferencesUtils;
import com.mci.firstidol.utils.Utily;
import com.mci.firstidol.view.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FlashActivity extends BaseActivity implements OnClickListener {

	private LinearLayout ll_splash;
	private ImageView iv_splash, iv_maohand, iv_maolove, iv_soeasy;
	private Button btn_skip;
	private Timer timer;
	private int stayTime;
	
	private int num;

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				goToMain();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	/**
	 * 跳往主页面
	 */
	public void goToMain() {
		
		if (timer!=null) {
			timer.cancel();
			timer = null;
		}
		
		exitThisOnly();
		Utily.go2Activity(context, MainActivity.class);
		
	}

	public FlashActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.layout_splash;
	}

	@Override
	protected void initNavBar() {
		// TODO Auto-generated method stub
		hideNavBar();
	}

	@Override
	public void rightNavClick() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		ll_splash = (LinearLayout) findViewById(R.id.ll_splash);
		iv_splash = (ImageView) findViewById(R.id.iv_splash);
		iv_maohand = (ImageView) findViewById(R.id.iv_maohand);
		iv_maolove = (ImageView) findViewById(R.id.iv_maolove);
		iv_soeasy = (ImageView) findViewById(R.id.iv_soeasy);

		btn_skip = (Button) findViewById(R.id.btn_skip);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		btn_skip.setOnClickListener(this);

		Animation animation1 = AnimationUtils.loadAnimation(FlashActivity.this,
				R.anim.splash_up_to_bottom);
		Animation animation2 = AnimationUtils.loadAnimation(FlashActivity.this,
				R.anim.splash_alpha_in_to_alpha_out);
		Animation animation3 = AnimationUtils.loadAnimation(FlashActivity.this,
				R.anim.splash_alpha_out_to_toalpha_in);
		Animation animation4 = AnimationUtils.loadAnimation(FlashActivity.this,
				R.anim.splash_right_to_left);

		ll_splash.clearAnimation();
		iv_maohand.clearAnimation();
		iv_maolove.clearAnimation();
		iv_soeasy.clearAnimation();

		ll_splash.startAnimation(animation1);
		iv_maohand.startAnimation(animation2);
		iv_maolove.startAnimation(animation3);
		iv_soeasy.startAnimation(animation4);
	}

	TimerTask task = new TimerTask() {
		public void run() {
			num++;
			if (num >= stayTime) {
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
			
		}
	};

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		String path = PreferencesUtils.getString(context, "Path");
		ImageLoader.getInstance().displayImage(path, iv_splash);

		//
		stayTime = (int)PreferencesUtils.getFloat(context, "StayTime");
		timer = new Timer(true);
		timer.schedule(task, 0, 1000); // 延时1000ms后执行，1000ms执行一次
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		goToMain();
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
			if (timer!=null) {
				timer.cancel();
				timer = null;
			}
	}

}
