
package com.mci.firstidol.activity;

import java.util.ArrayList;

import com.mci.firstidol.R;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.model.SquareModel.ArticlePicture;
import com.mci.firstidol.model.SquareModel.LiveAnnex;
import com.mci.firstidol.model.SquareModel.SquareLiveChatModel;
import com.mci.firstidol.view.BasePagerAdapter.OnItemClickListener;
import com.mci.firstidol.view.FilePagerAdapter.FilePagerAdapterListener;
import com.mci.firstidol.view.FilePagerAdapter;
import com.mci.firstidol.view.GalleryViewPager;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera.Size;
import android.os.Bundle;


public class ZhiboPicturesActivity extends Activity implements FilePagerAdapterListener{
    private GalleryViewPager mViewPager;
    private ArrayList<LiveAnnex> mAttachments;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhibo_picture);
        mViewPager = (GalleryViewPager) findViewById(R.id.viewpager);
        initData();
    }

    @SuppressWarnings("unchecked")
    public void initData() {
//        Intent intent = getIntent();
//        mPosition = intent.getIntExtra("position", 0);
//        mAttachments = (ArrayList<LiveAnnex>) intent.getSerializableExtra("attachments");
    	
    	mPosition = getIntent().getExtras().getInt("position", 0);
    	String from = getIntent().getExtras().getString(Constant.IntentKey.from);//来自与哪？
    	ArrayList<String> resources;
    	if ("ArticleDetail".equals(from)) {//来自与发现详情
    		ArrayList<ArticlePicture> mArticlePictures = (ArrayList<ArticlePicture>) getIntent().getExtras().getSerializable("attachments");

            resources = new ArrayList<String>();
            for (ArticlePicture articlePicture : mArticlePictures) {
                resources.add(articlePicture.getPicPath());
            }
		} else if("WebArticleDetail".equals(from)){
			resources = (ArrayList<String>) getIntent().getExtras().getSerializable("attachments");
		} else {
			mAttachments = (ArrayList<LiveAnnex>) getIntent().getExtras().getSerializable("attachments");

	        resources = new ArrayList<String>();
	        for (LiveAnnex attachment : mAttachments) {
	            resources.add(attachment.getAnnexUrl());
	        }
		}
    	
        
        FilePagerAdapter pagerAdapter = new FilePagerAdapter(this, resources);
        pagerAdapter.setFilePagerAdapterListener(this);
        pagerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                finish();
            }
        });

        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(resources.size());
        mViewPager.setCurrentItem(mPosition);
    }

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		finish();
	}
}