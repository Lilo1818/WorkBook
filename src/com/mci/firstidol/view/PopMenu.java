package com.mci.firstidol.view;
import com.mci.firstidol.R;
import com.mci.firstidol.activity.ReportActivity;
import com.mci.firstidol.activity.SquareFoundDetailActivity;
import com.mci.firstidol.activity.StarActivty;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.utils.DisplayUtil;
import com.mci.firstidol.utils.Utily;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

public class PopMenu implements OnClickListener{
	private Context context;
	private PopupWindow popupWindow;

	private LinearLayout ll_popup,ll_more,ll_rore;
	private Button btn_top,btn_delete,btn_collection,btn_report;
	
	private PopMenuListener popMenuListener;
	
	private boolean isShowMore;
	private int position;//点击列表的位置
	private boolean isReport;

	@SuppressWarnings("deprecation")
	public PopMenu(Context context) {
		this.context = context;

		View view = LayoutInflater.from(context)
				.inflate(R.layout.layout_popupwidow_leftmenu, null);
		ll_more = (LinearLayout) view.findViewById(R.id.ll_more);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		btn_top = (Button) view.findViewById(R.id.btn_top);
		btn_delete = (Button) view.findViewById(R.id.btn_delete);
		btn_collection = (Button) view.findViewById(R.id.btn_collection);
		btn_report = (Button) view.findViewById(R.id.btn_report);
		ll_rore = (LinearLayout) view.findViewById(R.id.ll_rore);
		btn_top.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		btn_collection.setOnClickListener(this);
		btn_report.setOnClickListener(this);
		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, DisplayUtil.dip2px(context, 32));

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	public void setShowMore(boolean isShowMore) {
		this.isShowMore = isShowMore;
		if (isShowMore) {
			ll_more.setVisibility(View.VISIBLE);
		} else {
			ll_more.setVisibility(View.GONE);
		}
	}
	
	public void setShowReport(boolean isReport){
		this.isReport = isReport;
		if(isReport){
			//btn_report.setVisibility(View.VISIBLE);
			ll_rore.setVisibility(View.VISIBLE);
		}else{
			//btn_report.setVisibility(View.GONE);
			ll_rore.setVisibility(View.GONE);
		}
	}
	// 下拉式 弹出 pop菜单 v 左边
	public void show(View v,int position) {
//		popupWindow.showAsDropDown(v,10,context.getResources().getDimensionPixelSize(R.dimen.dp10));
		this.position = position;
		
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
        ll_popup.measure(w, h);  
        int height =ll_popup.getMeasuredHeight();  
        int width =ll_popup.getMeasuredWidth();  
		 
		 
		int[] location = new int[3];  
        v.getLocationOnScreen(location); 
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]-width, location[1]);  

		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状态
		popupWindow.update();
	}

	// 隐藏菜单
	public void dismiss() {
		popupWindow.dismiss();
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		popMenuListener.popMenuClickIndex(Integer.parseInt(v.getTag().toString()),position);
		popupWindow.dismiss();
	}

	public void setPopMenuListener(PopMenuListener popMenuListener) {
		this.popMenuListener = popMenuListener;
	}
	public interface PopMenuListener{
		public void popMenuClickIndex(int index,int position);
	}
}
