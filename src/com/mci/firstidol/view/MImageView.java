package com.mci.firstidol.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * Date: 13-11-21 Time: 下午2:56
 */
public class MImageView extends ImageView {

	private String expand;

	public MImageView(Context context) {
		super(context);
	}

	public MImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public String getExpand() {
		return expand;
	}

	public void setExpand(String expand) {
		this.expand = expand;
	}

}
