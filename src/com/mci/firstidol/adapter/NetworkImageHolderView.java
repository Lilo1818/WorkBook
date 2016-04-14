package com.mci.firstidol.adapter;

import java.util.HashMap;
import java.util.Map;

import com.mci.firstidol.BaseApp;
import com.mci.firstidol.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mci.firstidol.view.convenientbanner.holder.Holder;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements Holder<HashMap<String, String>> {
    private ImageView imageView;
    private TextView textView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
    	View view = LayoutInflater.from(context).inflate(R.layout.layout_banner_item, null);
//        imageView = new ImageView(context);
        
        imageView = (ImageView) view.findViewById(R.id.iv_show);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//    	imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    	textView = (TextView) view.findViewById(R.id.tv_title);
        return view;
    }

    @Override
    public void UpdateUI(Context context,int position, HashMap<String, String> data) {
    	String uri = data.get("icon").trim();
    	String title = data.get("title");
//        imageView.setImageResource(R.drawable.ic_default_adimage);
        ImageLoader.getInstance().displayImage(uri,imageView);
        textView.setText(title.trim());
    }
}
