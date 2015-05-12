package com.classes;

import java.util.HashMap;

import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import android.util.Log;

import com.easydiner.R;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

@ReportsCrashes(formKey = "EasydinerClass", formUri =  Constant.BASE_URL + "Appmail")
public class EasydinerClass extends Application {
	
	public static String MY_FLURRY_APIKEY = "4GJF6MPZY44QFHW98T3T";
	public static String PROPERTY_ID = "UA-61222599-2";
	Permission[] permissions = new Permission[] { Permission.USER_PHOTOS,Permission.EMAIL, Permission.PUBLISH_ACTION };
	
	public enum TrackerName {
		  APP_TRACKER, // Tracker used only in this app.
	      GLOBAL_TRACKER,
	      E_COMMERCE_TRACKER,
		}

		HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		
		
		FlurryAgent.setLogEnabled(true);
		FlurryAgent.setLogEvents(true);
		FlurryAgent.setLogLevel(Log.VERBOSE);
		FlurryAgent.init(this, MY_FLURRY_APIKEY);
		
//		ACRA.init(this);
		SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder().setAppId("320917718073942").setNamespace("easydiner")
				.setPermissions(permissions).build();

		SimpleFacebook.setConfiguration(configuration);

		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2).memoryCacheSize(1500000).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).build();

		ImageLoader.getInstance().init(config);

		// UNIVERSAL IMAGE LOADER SETUP
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY).displayer(new FadeInBitmapDisplayer(300)).build();

		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config2 = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new WeakMemoryCache()).discCacheSize(100 * 1024 * 1024).build();

		ImageLoader.getInstance().init(config2);
		// END - UNIVERSAL IMAGE LOADER SETUP
	}
	
	public synchronized Tracker getTracker(TrackerName trackerId) {

        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = analytics.newTracker(R.xml.analytics_global_config);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);}
}
