package com.easydiner.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.classes.EasydinerClass;
import com.easydiner.R;
import com.easydiner.dao.ImageGalleryAdapter;
import com.easydiner.dao.ImageZoomGalleryAdapter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class GalleryActivity extends EasyDinerBaseActivity {
	
	ViewPager imagePager;
	private ImageGalleryAdapter galleryAdapter;
	private ArrayList<String> galleryImg;
	private String restaurantId;
	private ImageZoomGalleryAdapter zoomGalleryAdapter;
	private boolean isMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.galleryview_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("GalleryActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		galleryImg = getIntent().getExtras().getStringArrayList("gallery_image");
		restaurantId = getIntent().getExtras().getString("rest_id");
		isMenu = getIntent().getExtras().getBoolean("isMenu");
		imagePager = (ViewPager) findViewById(R.id.pagerGalleryContainer);
		zoomGalleryAdapter = new ImageZoomGalleryAdapter(getSupportFragmentManager(), galleryImg, restaurantId,GalleryActivity.this, isMenu);
//		galleryAdapter = new ImageGalleryAdapter(GalleryActivity.this, galleryImg);
		imagePager.setAdapter(zoomGalleryAdapter);
	}
}
