package com.mci.firstidol.view;


import com.mci.firstidol.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class BaseAlertDialog {
    private Context context;
    public Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_msg;
    private Button btn_neg;
    private Button btn_pos;
    private Button btn_middle;
    private ImageView img_line;
    private ImageView img_line2;
    private Display display;
    private boolean showMsg = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;
    private View view;
    private LinearLayout view_add;//添加的View

    protected int xml_id;

    public BaseAlertDialog(Context context) {
        initXmlId();
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    protected void initXmlId() {
        xml_id = R.layout.view_base_alertdialog;
    }

    public View getView() {
        return view;
    }

    public BaseAlertDialog builder() {
        view = LayoutInflater.from(context).inflate(
                xml_id, null);

        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        btn_neg = (Button) view.findViewById(R.id.btn_neg);
        btn_neg.setVisibility(View.GONE);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        btn_middle = (Button) view.findViewById(R.id.btn_middle);
        btn_middle.setVisibility(View.GONE);

        img_line = (ImageView) view.findViewById(R.id.img_line);
        img_line.setVisibility(View.GONE);

        img_line2 = (ImageView) view.findViewById(R.id.img_line2);
        img_line2.setVisibility(View.GONE);

        view_add = (LinearLayout) view.findViewById(R.id.view_add);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LayoutParams.WRAP_CONTENT));


        return this;
    }

    public boolean isShowing(){
        if(dialog!=null){
            return dialog.isShowing();
        }else{
            return false;
        }
    }

    public BaseAlertDialog setMsg(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            txt_msg.setText("");
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }

    public BaseAlertDialog addView(View view) {
        if (view_add != null) {
            view_add.addView(view);
        }
        return this;
    }


    public BaseAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public BaseAlertDialog setPositiveButton(String text,
                                             final OnClickListener listener, final boolean isDismiss) {
        showPosBtn = true;
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick(v);
                }
                if(isDismiss){
                    dialog.dismiss();
                }
            }
        });
        return this;
    }

    public BaseAlertDialog setMiddleButton(String text,
                                             final OnClickListener listener) {
        img_line2.setVisibility(View.VISIBLE);
        btn_middle.setVisibility(View.VISIBLE);
        showPosBtn = true;
        if ("".equals(text)) {
            btn_middle.setText("那啥");
        } else {
            btn_middle.setText(text);
        }
        btn_middle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }

    public BaseAlertDialog setNegativeButton(String text,
                                             final OnClickListener listener) {
        showNegBtn = true;
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        return this;
    }

    private void setLayout() {

        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
            btn_pos.setText("确定");
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
            btn_pos.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
            img_line.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }
    }

    public void close() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void show() {
        setLayout();
                    dialog.show();
    }
}
