package com.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.classes.Constant;
import com.classes.GPSTrackerSecond;
import com.classes.JsonobjectPost;
import com.classes.Pref;
import com.flurry.android.FlurryAgent;

public class HomelistSevice extends Service {
	private JSONObject jsonObject1, jsonObject2;
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_ID = "id";
	private static final String TAG_CITY = "city";
	private static final String TAG_GETCATLIST = "getCatList";
	private List<NameValuePair> nameValuePairs;
	private JSONObject jObjList, jObjList2;
	private SharedPreferences.Editor _prefEditor;
	private SharedPreferences _pref;
	private GPSTrackerSecond _GpsTrackerSecond;
	private Context context;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		FlurryAgent.onStartSession(this);
		context = this;
		_prefEditor = new Pref(getApplicationContext()).getSharedPreferencesEditorInstance();
		_pref = new Pref(getApplicationContext()).getSharedPreferencesInstance();
		/*_GpsTrackerSecond = new GPSTrackerSecond(getApplicationContext());
		_prefEditor.putString("city", _GpsTrackerSecond.getCityName());
		_prefEditor.putString("currLat",String.valueOf(_GpsTrackerSecond.getLatitude()));
		_prefEditor.putString("currLng",String.valueOf(_GpsTrackerSecond.getLongitude()));
		_prefEditor.commit();*/
		getHomeList();
		getDeviceDetails();
		return super.onStartCommand(intent, flags, startId);
	}

	private void getDeviceDetails() {
		TelephonyManager telephonyManager =  (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		String networkOperator = telephonyManager.getNetworkOperator();
		String build = Build.DISPLAY;
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		System.out.println("details "+ imei+ " "+ networkOperator+ " "+ build+ " "+ display);
	}

	private void getHomeList() {
		jsonObject1 = new JSONObject();
		jsonObject2 = new JSONObject();
		try {
			jsonObject2.put(TAG_ACCESSTOKN, "");
			jsonObject2.put(TAG_ID, "");
			jsonObject2.put(TAG_CITY, "Delhi");
			jsonObject1.put(TAG_GETCATLIST, jsonObject2);

			Log.e("json", jsonObject1.toString());

			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("data", jsonObject1.toString()));

			AstClassSyn astClassSyn = new AstClassSyn();
			astClassSyn.execute("");

			AstClassFilter astClassFilter = new AstClassFilter();
			astClassFilter.execute("");
			
			FlurryAgent.onEndSession(context);
			stopSelf();

		} catch (Exception e) {
			Log.e("error", "error");
		}
	}

	private class AstClassSyn extends AsyncTask<String, String, Long> {
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {

				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL+ "syncserverdata", nameValuePairs,jsonObject1.toString());

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			try {
				String fileDIr = Environment.getExternalStorageDirectory()+ File.separator + getPackageName() + File.separator;
				File f = new File(fileDIr);
				if (!f.exists()) {
					f.mkdirs();
				}

				Log.e("fileDIr", fileDIr);

				String filename = f.getAbsolutePath() + File.separator+ Constant.JSON_SERVICE_FILE_NAME;

				Log.e("filename", filename);

				_prefEditor.putString(Constant.JSON_SERVICE_FILE_NAME, filename);

				_prefEditor.commit();
				File jsonF = new File(filename);

				if (jsonF.exists()) {
					jsonF.createNewFile();
				}

				String data = jObjList.toString();
				// String data =
				// "{\"data\":[{\"catId\":\"1\",\"catName\":\"DINE\",\"subcatList\":[{\"subcatId\":\"14\",\"subcatName\":\"JAPANESE\"},{\"subcatId\":\"15\",\"subcatName\":\"ITALIAN\"},{\"subcatId\":\"16\",\"subcatName\":\"MEDITERRANEAN\"},{\"subcatId\":\"17\",\"subcatName\":\"CAFE & BAKERY\"},{\"subcatId\":\"18\",\"subcatName\":\"TAKE AWAY\"},{\"subcatId\":\"13\",\"subcatName\":\"REGIONAL INDIAN\"},{\"subcatId\":\"12\",\"subcatName\":\"CASUAL ECLECTIC\"},{\"subcatId\":\"11\",\"subcatName\":\"HOME DELIVERY\"},{\"subcatId\":\"10\",\"subcatName\":\"VEGETARIAN ONLY\"},{\"subcatId\":\"9\",\"subcatName\":\"EUROPEAN\"},{\"subcatId\":\"8\",\"subcatName\":\"PAN-ASIAN\"},{\"subcatId\":\"7\",\"subcatName\":\"CHINESE\"},{\"subcatId\":\"6\",\"subcatName\":\"NORTH INDIAN\"},{\"subcatId\":\"5\",\"subcatName\":\"NEARBY\"}]},{\"catId\":\"2\",\"catName\":\"DRINK\",\"subcatList\":[{\"subcatId\":\"21\",\"subcatName\":\"NIGHT LIFE\"},{\"subcatId\":\"20\",\"subcatName\":\"BARS\"},{\"subcatId\":\"19\",\"subcatName\":\"HOTEL BARS\"}]},{\"catId\":\"3\",\"catName\":\"DEALS\",\"subcatList\":[]},{\"catId\":\"4\",\"catName\":\"EAZYTRENDS\",\"subcatList\":[]},{\"catId\":\"22\",\"catName\":\"EAZYDINER REVIEWS\",\"subcatList\":[{\"subcatId\":\"23\",\"subcatName\":\"VIR\u2019S VERDICT\"},{\"subcatId\":\"24\",\"subcatName\":\"PHANTOM SPEAKS\"}]}]}";
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(jsonF.getAbsolutePath());
					fos.write(data.getBytes());
					fos.close();
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class AstClassFilter extends AsyncTask<String, String, Long> {
		@Override
		protected Long doInBackground(String... params) {
			try {

				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL+ "GetFilterData", nameValuePairs,jsonObject1.toString());

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			
			try {
				String fileDIr = Environment.getExternalStorageDirectory()+ File.separator + getPackageName() + File.separator;
				File f = new File(fileDIr);
				if (!f.exists()) {
					f.mkdirs();
				}

				Log.e("fileDIr", fileDIr);

				String filename = f.getAbsolutePath() + File.separator+ Constant.JSON_SERVICE_FILTER_DATA_LIST;
				
				Log.e("filename", filename);

				_prefEditor.putString(Constant.JSON_SERVICE_FILTER_DATA_LIST, filename);
				
				_prefEditor.commit();
				File jsonF = new File(filename);

				if (jsonF.exists()) {
					jsonF.createNewFile();
				}

				String data = jObjList.toString();
				//String data = "{\"data\":[{\"heading\":\"CUISINE\",\"filtering_value\":[{\"id\":\"cuisine All Day Dining\",\"name\":\"All Day Dining\"},{\"id\":\"cuisine American\",\"name\":\"American\"},{\"id\":\"cuisine Bakery\",\"name\":\"Bakery\"},{\"id\":\"cuisine Biryani\",\"name\":\"Biryani\"},{\"id\":\"cuisine Cafe\",\"name\":\"Cafe\"},{\"id\":\"cuisine Casual Eclectic\",\"name\":\"Casual Eclectic\"},{\"id\":\"cuisine Chinese\",\"name\":\"Chinese\"},{\"id\":\"cuisine Cocktail Menu\",\"name\":\"Cocktail Menu\"},{\"id\":\"cuisine Desserts\",\"name\":\"Desserts\"},{\"id\":\"cuisine Fast Food\",\"name\":\"Fast Food\"},{\"id\":\"cuisine Healthy\",\"name\":\"Healthy\"},{\"id\":\"cuisine Indian\",\"name\":\"Indian\"},{\"id\":\"cuisine Italian\",\"name\":\"Italian\"},{\"id\":\"cuisine Japanese\",\"name\":\"Japanese\"},{\"id\":\"cuisine Mithai\",\"name\":\"Mithai\"},{\"id\":\"cuisine Modern Indian\",\"name\":\"Modern Indian\"},{\"id\":\"cuisine North Indian\",\"name\":\"North Indian\"},{\"id\":\"cuisine Pan Asian\",\"name\":\"Pan Asian\"},{\"id\":\"cuisine Regional Indian\",\"name\":\"Regional Indian\"},{\"id\":\"cuisine South Indian\",\"name\":\"South Indian\"},{\"id\":\"cuisine Thai\",\"name\":\"Thai\"}]},{\"heading\":\"LOCATION\",\"filtering_value\":[{\"id\":\"location East Delhi\",\"name\":\"East Delhi\"},{\"id\":\"location West Delhi\",\"name\":\"West Delhi\"},{\"id\":\"location North Delhi\",\"name\":\"North Delhi\"},{\"id\":\"location South Delhi\",\"name\":\"South Delhi\"},{\"id\":\"location Central Delhi\",\"name\":\"Central Delhi\"},{\"id\":\"location Gurgaon\",\"name\":\"Gurgaon\"},{\"id\":\"location Noida\",\"name\":\"Noida\"},{\"id\":\"location Ghaziabad\",\"name\":\"Ghaziabad\"},{\"id\":\"location Faridabad\",\"name\":\"Faridabad\"},{\"id\":\"location Old Delhi\",\"name\":\"Old Delhi\"}]},{\"heading\":\"COST FOR 2\",\"filtering_value\":[{\"id\":\"price 1-500\",\"name\":\"Up to Rs. 500\"},{\"id\":\"price 501-2000\",\"name\":\"Rs. 501-2000\"},{\"id\":\"price 2001-6000\",\"name\":\"Rs. 2001-6000\"},{\"id\":\"price 6001-10000\",\"name\":\"Rs. 6000++\"}]}],\"erNode\":{\"erCode\":0,\"erMsg\":\"\"}}";
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(jsonF.getAbsolutePath());
					fos.write(data.getBytes());
					fos.close();

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
