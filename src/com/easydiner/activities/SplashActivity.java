package com.easydiner.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.classes.CommonFunction;
import com.classes.Constant;
import com.classes.EasydinerClass;
import com.classes.GPSTrackerSecond;
import com.classes.JsonobjectPost;
import com.classes.Pref;
import com.easydiner.R;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.services.HomelistSevice;

public class SplashActivity extends Activity {

	private CommonFunction _comFunction;
	private SharedPreferences.Editor _pEditor;
	private SharedPreferences _pref;
	private GPSTrackerSecond _tracker;
	/*
	private ImageView image;
	private int count = 0;
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private static final String TAG_LOGOUT = "logout";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_DATA = "data";
	private List<NameValuePair> nameValuePairs;
*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("SplashActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());

		_comFunction = new CommonFunction(this);
		_pEditor = new Pref(SplashActivity.this).getSharedPreferencesEditorInstance();
		_pref = new Pref(SplashActivity.this).getSharedPreferencesInstance();

		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!isGPSEnabled) {

			AlertDialog.Builder loctionTrackerBuilder = new AlertDialog.Builder(SplashActivity.this);
			loctionTrackerBuilder.setMessage("Please switch on location providers-GPS Services for a better browsing");

			loctionTrackerBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							gotoNext();
						}
					});
			loctionTrackerBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

							Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivityForResult(i, 1);
							finish();
						}
					});

			AlertDialog dialog = loctionTrackerBuilder.create();
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();

		} else {

			gotoNext();
		}

		// checkLoginExpiry();

	}
	
	@Override
	protected void onStart() {
		
		super.onStart();
		FlurryAgent.onStartSession(this);
	}
	
	@Override
	protected void onStop() {
		
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	private void gotoNext() {

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				_tracker = new GPSTrackerSecond(SplashActivity.this);
				if (_pref.getString("userId", "").equalsIgnoreCase(null)|| _pref.getString("userId", "").equalsIgnoreCase("")) {
					_pEditor.putString("device_id",_comFunction.getGCMRegistrationId());
					_pEditor.putString("currLat",String.valueOf(_tracker.getLatitude()));
					_pEditor.putString("currLng",String.valueOf(_tracker.getLongitude()));
					_pEditor.putString("accessToken", "");
					_pEditor.putString("expiry", "");
					_pEditor.commit();
					startActivity(new Intent(SplashActivity.this,LandingActivity.class));
					SplashActivity.this.finish();
					startService(new Intent(SplashActivity.this,HomelistSevice.class));
				} else {
					_pEditor.putString("device_id",_comFunction.getGCMRegistrationId());
					_pEditor.putString("currLat",String.valueOf(_tracker.getLatitude()));
					_pEditor.putString("currLng",String.valueOf(_tracker.getLongitude()));
					startActivity(new Intent(SplashActivity.this,HomeActivity.class));

					SplashActivity.this.finish();
				}
			}
		}, 3000);

	}

/*	@SuppressLint("SimpleDateFormat")
	private void checkLoginExpiry() {

		Toast.makeText(SplashActivity.this, _pref.getString("expiry", ""), 5000)
				.show();

		if (!_pref.getString("expiry", "").equalsIgnoreCase("")) {

			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentDateStr = String.valueOf(Calendar.getInstance().get(Calendar.YEAR))+ "-"+ String.valueOf(Calendar.getInstance().get(
								Calendar.MONTH) + 1) + "-"+ String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))+ " "
						+ String.valueOf(Calendar.getInstance().get(Calendar.HOUR))
						+ ":"+ String.valueOf(Calendar.getInstance().get(Calendar.MINUTE))+ ":"+ String.valueOf(Calendar.getInstance().get(Calendar.SECOND));

				Date currentDate = sdf.parse(currentDateStr);
				Date expDate = sdf.parse(_pref.getString("expiry", ""));

				if (currentDate.compareTo(expDate) > 0) {

					Log.v("compare",String.valueOf(currentDate.compareTo(expDate)));

					_pEditor.putString("userName", "");
					_pEditor.putString("userId", "");
					_pEditor.putString("userImg", "");
					_pEditor.putString("membershipNo", "");
					_pEditor.putString("points", "");
					_pEditor.putString("accessToken", "");
					_pEditor.putString("expiry", "");
					_pEditor.commit();

					jsonObject1 = new JSONObject();
					jsonObject2 = new JSONObject();

					jsonObject2.put(TAG_ACCESSTOKN,_pref.getString("accessToken", ""));
					jsonObject2.put(TAG_EMAIL, _pref.getString("userId", ""));
					jsonObject1.put(TAG_LOGOUT, jsonObject2);

					AstClassLogout astClassOuthLogin = new AstClassLogout();
					astClassOuthLogin.execute("");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private class AstClassLogout extends AsyncTask<String, String, Long> {

		@Override
		protected Long doInBackground(String... params) {
			try {
				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL + "logout",nameValuePairs, jsonObject1.toString());

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			if (jObjList.toString() != null) {

				try {
					JSONObject objData = jObjList.getJSONObject(TAG_DATA);
					JSONObject objErr = objData.getJSONObject(TAG_ERNODE);
					if (objErr.getString(TAG_ERCODE).equalsIgnoreCase("0")) {

					} else {
						Toast.makeText(SplashActivity.this,objErr.getString(TAG_ERMSG), Toast.LENGTH_LONG).show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}*/
}
