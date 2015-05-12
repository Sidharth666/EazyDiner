package com.classes;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.easydiner.dataobject.ExpListItem;
import com.easydiner.dataobject.ListItem;

public class Constant {
	
	public static Bitmap FACEBOOK_IMAGE_BMP = null;

	//public static final String BASE_URL = "https://www.eazydiner.com/admin/mobile/";
	//public static final String BASE_URL = "http://sandbox.eazydiner.com/admin/mobile/";
	//public static final String BASE_URL = "http://mo.bluehorse.in/eazydiner/admin/mobile/";
	public static final String BASE_URL = "https://www.eazydiner.com/admin/mobile/";
	public static final String JSON_SERVICE_FILE_NAME = "home_list.json";
	public static final String JSON_SERVICE_FILTER_DATA_LIST = "filter_data_list.json";
	public static int NEAR_BY_LIST = 0;
	public static int FILTER_BY_LIST = 0;
	public static int MAP_DETAILS = 0;
	public static final ArrayList<ExpListItem> expListItems = new ArrayList<ExpListItem>();
	public static int EMPTY_CHECK = 0;
	public static int RESERVATION_LIST = 1;
	public static int INDASHBOARD = 0;
	public static boolean GPS_STATUS = true;
	private static SharedPreferences.Editor _prefEditor;

	public static ArrayList<String> imgSelected = new ArrayList<String>();
	public static ArrayList<ListItem> arrayListItem;
	
	public static void setNewLocation(Context con){
		GPSTrackerSecond _GpsTrackerSecond = new GPSTrackerSecond(con.getApplicationContext());
		_prefEditor = new Pref(con.getApplicationContext()).getSharedPreferencesEditorInstance();
		_prefEditor.putString("city", _GpsTrackerSecond.getCityName());
		_prefEditor.putString("currLat",String.valueOf(_GpsTrackerSecond.getLatitude()));
		_prefEditor.putString("currLng",String.valueOf(_GpsTrackerSecond.getLongitude()));
		_prefEditor.commit();
		
	}
}
