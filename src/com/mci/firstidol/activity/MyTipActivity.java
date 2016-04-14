package com.mci.firstidol.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.mci.firstidol.R;
import com.mci.firstidol.adapter.TextTagsAdapter;
import com.mci.firstidol.base.BaseActivity;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.utils.AnyEventType;
import com.mci.firstidol.view.BaseAlertDialog;
import com.mci.firstidol.view.tag.TagCloudView;

import de.greenrobot.event.EventBus;

public class MyTipActivity extends BaseActivity {

	private String Tags;// 标签
	private List<String> tips = new ArrayList<>();// 标签list

	private TextView text_tags;// 标签文本
	private TagCloudView tagCloudView;
	private BaseAlertDialog dialog;

	@Override
	protected int getViewId() {
		Intent intent = getIntent();
		if (intent != null) {
			Tags = intent.getStringExtra(Constant.IntentKey.tip);
		}
		return R.layout.activity_tip;
	}

	@Override
	protected void initNavBar() {
		setTitle(R.string.tip);
		setRightBtnBackgroundResource(R.drawable.done);
	}

	@Override
	protected void initView() {
		text_tags = (TextView) findViewById(R.id.text_tags);
		tagCloudView = (TagCloudView) findViewById(R.id.tag_cloud);
	}

	@Override
	public void rightNavClick() {
		String tagStr = text_tags.getText().toString().trim();
		updateShowText(tagStr);
		exitThis();
	}

	@Override
	protected void findViewById() {

	}

	@Override
	protected void initData() {
		text_tags.setText(Tags);

		tips.add("+添加标签");
		tips.add("舔屏党");
		tips.add("脑残粉");
		tips.add("围观党");
		tips.add("大叔控");
		tips.add("迷妹");
		tips.add("韩剧控");
		tips.add("正太控");
		tips.add("日剧控");
		tips.add("CP党");
		tips.add("手控");

		TextTagsAdapter tagsAdapter = new TextTagsAdapter(tips, handler);
		tagCloudView.setAdapter(tagsAdapter);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				int position = (int) msg.obj;
				String info = tips.get(position);
				String tagStr = text_tags.getText().toString().trim();
				if (tagStr != null && !tagStr.contains(info)) {
					if ("+添加标签".equals(info)) {
						showAddContent();
					} else {
						text_tags.setText(tagStr + "," + info);
					}
				}
				break;

			default:
				break;
			}
		};
	};
	
	/**
	 * 显示添加标签
	 */
	public void showAddContent(){
		
		final View view = LayoutInflater.from(context).inflate(R.layout.layout_edit, null);
		final EditText edit_input = (EditText) view.findViewById(R.id.edit_input);
		final String tagStr = text_tags.getText().toString().trim();
	    dialog = new BaseAlertDialog(context).builder();
		dialog.addView(view);
		dialog.setMsg("自定义标签");
		dialog.setNegativeButton("取消", null);
		dialog.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String info = edit_input.getText().toString().trim();
                if(info!=null&&!info.equals("")){
                	text_tags.setText(tagStr + "," + info);
                	dialog.close();
                }				
			}
		}, true);
		dialog.show();
	}

	/**
	 * 更新文本
	 */
	public void updateShowText(String info) {
		AnyEventType type = new AnyEventType(Constant.Config.UPDATE_TIP,
				AnyEventType.Type_Default);
		type.info = info;
		EventBus.getDefault().post(type);
	}

}
