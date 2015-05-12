package com.easydiner.activities;

import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classes.CommonFunction.TimeoutDialogTryAgainListener;
import com.classes.ConnectionDetector;
import com.classes.Constant;
import com.classes.CustomAlertProgressDialog;
import com.classes.EasydinerClass;
import com.classes.JsonobjectPost;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.dao.MediaPagerAdapter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.sromku.simple.fb.Permission.Type;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.viewpagerindicator.CirclePageIndicator;

public class LandingActivity extends EasyDinerBaseActivity implements ConnectionCallbacks, OnConnectionFailedListener,TimeoutDialogTryAgainListener {
	ViewPager pagercontainer;
	LayoutInflater _lInflater;
	CirclePageIndicator indicator;
	private TextView tvPage1Txt1, tvPage1Txt2, tvPage1Txt3, tvPage1Txt4,
			tvPage1Txt5, tvPage2Txt1, tvPage2Txt2, tvPage2Txt3, tvPage2Txt4,
			tvPage3Txt1, tvPage3Txt2, tvPage3Txt3, tvPage3Txt4, tvPage3Txt5,
			tvPage4Txt1, tvPage4Txt2, tvPage4Txt3, tvPage4Txt4, tvPage4Txt5,
			tvPage4Txt6, tvPage4Txt7, textSigninSignup;

	private static final String TAG_DATA = "data";
	private static final String TAG_OAUTHDTLS = "oauthDtls";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_TYPE = "type";
	private static final String TAG_AUTHID = "authId";
	private static final String TAG_SCLNME = "sclNme";
	private static final String TAG_LOGIN = "login";
	private static final String TAG_USRID = "usrId";
	private static final String TAG_USRNM = "usrNm";
	private static final String TAG_USRIMG = "usrImg";
	private static final String TAG_ACCESSTOKEN = "accessToken";
	private static final String TAG_MEMBERSHIP = "membership";
	private static final String TAG_POINTS = "points";
	private static final String TAG_TOKEN = "token";
	private static final String TAG_EXPIRY = "expiry";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_PHONE = "phone_no";

	private View page2, page3, page4, page5;
	private RelativeLayout rlSkip;
	private SimpleFacebook simpleFB;
	private static final int RC_SIGN_IN = 0;
	private GoogleApiClient mGoogleApiClient;
	private boolean mSignInClicked = true;
	private String personName;
	private String profImageWhole;
	private boolean mIntentInProgress;
	private ImageView ivFbloginLanding, ivGploginLanding, ivGenloginLanding;
	private ConnectionDetector _connectionDetector;
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private SharedPreferences.Editor _pEditor;
	private List<NameValuePair> nameValuePairs;
	private AlertDialog dialog;
	private Context con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.landing_activity);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("LandingActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		con = this;
		if (!Constant.GPS_STATUS) {
			AlertDialog.Builder loctionTrackerBuilder = new AlertDialog.Builder(LandingActivity.this);
			loctionTrackerBuilder.setMessage("Please put on Location Services for a better browsing");
			loctionTrackerBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			AlertDialog dialog = loctionTrackerBuilder.create();
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		_lInflater = getLayoutInflater();

		pagercontainer = (ViewPager) findViewById(R.id.pagercontainer);
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		ivFbloginLanding = (ImageView) findViewById(R.id.ivFbloginLanding);
		ivGploginLanding = (ImageView) findViewById(R.id.ivGploginLanding);
		ivGenloginLanding = (ImageView) findViewById(R.id.ivGenloginLanding);
		textSigninSignup = (TextView) findViewById(R.id.textSigninSignup);
		_connectionDetector = new ConnectionDetector(LandingActivity.this);
		mGoogleApiClient = new GoogleApiClient.Builder(LandingActivity.this).addConnectionCallbacks(LandingActivity.this)
				.addOnConnectionFailedListener(LandingActivity.this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();
		_pEditor = new Pref(LandingActivity.this).getSharedPreferencesEditorInstance();
		jObjList = new JSONObject();

		Vector<View> pages = new Vector<View>();

		page2 = _lInflater.inflate(R.layout.pagercontent_one, null);
		page3 = _lInflater.inflate(R.layout.pagercontent_two, null);
		page4 = _lInflater.inflate(R.layout.pagercontent_three, null);
		page5 = _lInflater.inflate(R.layout.pagercontent_four, null);

		setTextFont();

		pages.add(page2);
		pages.add(page3);
		pages.add(page4);
		pages.add(page5);

		rlSkip = (RelativeLayout) page5.findViewById(R.id.rlSkip);
		MediaPagerAdapter adapter = new MediaPagerAdapter(this, pages);
		pagercontainer.setAdapter(adapter);
		indicator.setViewPager(pagercontainer);
		indicator.setClickable(true);
		indicator.setSnap(true);

		onClick();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		simpleFB = SimpleFacebook.getInstance(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		simpleFB.onActivityResult(this, requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void onClick() {
		rlSkip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (_connectionDetector.isConnectingToInternet()) {
					startActivity(new Intent(LandingActivity.this,HomeActivity.class));
					overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
					finish();
				} else {
					Toast.makeText(LandingActivity.this,"No internet connection available",Toast.LENGTH_LONG).show();
				}
			}
		});

		ivFbloginLanding.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (_connectionDetector.isConnectingToInternet()) {
					dialog = CustomAlertProgressDialog.getCustomDialog(LandingActivity.this,"Please wait...");
					dialog.show();
					simpleFB.login(onLoginListener);
				} else {
					Toast.makeText(LandingActivity.this,"No internet connection available",Toast.LENGTH_LONG).show();
				}
			}
		});
		ivGploginLanding.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (_connectionDetector.isConnectingToInternet()) {
					mSignInClicked = true;
					dialog = CustomAlertProgressDialog.getCustomDialog(LandingActivity.this,"Please wait...");
					dialog.show();
					mGoogleApiClient.connect();
				} else {
					Toast.makeText(LandingActivity.this,"No internet connection available",Toast.LENGTH_LONG).show();
				}
			}
		});
		ivGenloginLanding.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (_connectionDetector.isConnectingToInternet()) {
					startActivity(new Intent(LandingActivity.this,SignupActivity.class));
					overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);
				} else {
					Toast.makeText(LandingActivity.this,"No internet connection available",Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private void setTextFont() {
		tvPage1Txt1 = (TextView) page2.findViewById(R.id.tvPage1Txt1);
		tvPage1Txt2 = (TextView) page2.findViewById(R.id.tvPage1Txt2);
		tvPage1Txt3 = (TextView) page2.findViewById(R.id.tvPage1Txt3);
		tvPage1Txt4 = (TextView) page2.findViewById(R.id.tvPage1Txt4);
		tvPage1Txt5 = (TextView) page2.findViewById(R.id.tvPage1Txt5);

		tvPage2Txt1 = (TextView) page3.findViewById(R.id.tvPage2Txt1);
		tvPage2Txt2 = (TextView) page3.findViewById(R.id.tvPage2Txt2);
		tvPage2Txt3 = (TextView) page3.findViewById(R.id.tvPage2Txt3);
		tvPage2Txt4 = (TextView) page3.findViewById(R.id.tvPage2Txt4);

		tvPage3Txt1 = (TextView) page4.findViewById(R.id.tvPage3Txt1);
		tvPage3Txt2 = (TextView) page4.findViewById(R.id.tvPage3Txt2);
		tvPage3Txt3 = (TextView) page4.findViewById(R.id.tvPage3Txt3);
		tvPage3Txt4 = (TextView) page4.findViewById(R.id.tvPage3Txt4);
		tvPage3Txt5 = (TextView) page4.findViewById(R.id.tvPage3Txt5);

		tvPage4Txt1 = (TextView) page5.findViewById(R.id.tvPage4Txt1);
		tvPage4Txt2 = (TextView) page5.findViewById(R.id.tvPage4Txt2);
		tvPage4Txt3 = (TextView) page5.findViewById(R.id.tvPage4Txt3);
		tvPage4Txt4 = (TextView) page5.findViewById(R.id.tvPage4Txt4);
		tvPage4Txt5 = (TextView) page5.findViewById(R.id.tvPage4Txt5);
		tvPage4Txt6 = (TextView) page5.findViewById(R.id.tvPage4Txt6);
		tvPage4Txt7 = (TextView) page5.findViewById(R.id.tvPage4Txt7);

		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);

		textSigninSignup.setTypeface(tf2);

		tvPage1Txt1.setTypeface(tf1);
		tvPage1Txt2.setTypeface(tf1);
		tvPage1Txt3.setTypeface(tf2);
		tvPage1Txt4.setTypeface(tf2);
		tvPage1Txt5.setTypeface(tf2);

		tvPage2Txt1.setTypeface(tf1);
		tvPage2Txt2.setTypeface(tf1);
		tvPage2Txt3.setTypeface(tf2);
		tvPage2Txt4.setTypeface(tf2);

		tvPage3Txt1.setTypeface(tf1);
		tvPage3Txt2.setTypeface(tf1);
		tvPage3Txt3.setTypeface(tf2);
		tvPage3Txt4.setTypeface(tf2);
		tvPage3Txt5.setTypeface(tf2);

		tvPage4Txt1.setTypeface(tf1);
		tvPage4Txt2.setTypeface(tf1);
		tvPage4Txt3.setTypeface(tf2);
		tvPage4Txt4.setTypeface(tf2);
		tvPage4Txt5.setTypeface(tf2);
		tvPage4Txt6.setTypeface(tf2);
		tvPage4Txt7.setTypeface(tf2);
	}

	@Override
	public void onConnected(Bundle arg0) {
		mGoogleApiClient.connect();
		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
			Log.v("in", "onConnected");
			Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
			personName = currentPerson.getDisplayName();
			profImageWhole = currentPerson.getImage().getUrl();
			Log.v("gp+ image", profImageWhole);
			int ind = profImageWhole.indexOf("?sz");
			Log.v("gp+ image", profImageWhole.substring(0, ind));
			@SuppressWarnings("unused")
			String personGooglePlusProfile = currentPerson.getUrl();
			jsonObject1 = new JSONObject();
			jsonObject2 = new JSONObject();
			try {
				jsonObject2.put(TAG_EMAIL,Plus.AccountApi.getAccountName(mGoogleApiClient));
				jsonObject2.put(TAG_TYPE, "gp");
				jsonObject2.put(TAG_AUTHID, currentPerson.getId());
				jsonObject2.put(TAG_USRIMG, profImageWhole.substring(0, ind));
				jsonObject2.put(TAG_SCLNME, personName);
				jsonObject1.put(TAG_OAUTHDTLS, jsonObject2);

				Log.v("input json", jsonObject1.toString());

				AstClassOuthLogin astClassOuthLogin = new AstClassOuthLogin();
				astClassOuthLogin.execute("");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		Log.v("in", "onConnectionSuspended");
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress && mSignInClicked && result.hasResolution()) {
			try {
				Log.v("in", "onConnectionFailed try3");
				mSignInClicked = false;
				dialog.dismiss();
				mIntentInProgress = true;
				startIntentSenderForResult(result.getResolution().getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);

			} catch (SendIntentException e) {
				// The intent was canceled before it was sent. Return to the default state and attempt to connect to get an updated ConnectionResult.
				Log.v("in", "onConnectionFailed catch");
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	OnLoginListener onLoginListener = new OnLoginListener() {

		@SuppressLint("ShowToast")
		@Override
		public void onFail(String reason) {
			Log.e("onFail up", "onFail");
			Toast.makeText(LandingActivity.this, "onFail up", Toast.LENGTH_LONG);
			dialog.dismiss();
		}

		@SuppressLint("ShowToast")
		@Override
		public void onException(Throwable throwable) {
			Log.e("onException up", throwable.getMessage());
			Toast.makeText(LandingActivity.this, "onException up",Toast.LENGTH_LONG);
			dialog.dismiss();
		}

		@SuppressLint("ShowToast")
		@Override
		public void onThinking() {
			Log.e("onThinking up", "onThinking");
			Toast.makeText(LandingActivity.this, "onThinking up",Toast.LENGTH_LONG);
			// dialog.dismiss();
		}

		@SuppressLint("ShowToast")
		@Override
		public void onNotAcceptingPermissions(Type type) {
			// TODO Auto-generated method stub
			Log.e("onNotAcceptingPermissions up", "onNotAcceptingPermissions");
			Toast.makeText(LandingActivity.this,"onNotAcceptingPermissions up", Toast.LENGTH_LONG);
			dialog.dismiss();
		}

		@SuppressLint("ShowToast")
		@Override
		public void onLogin() {
			simpleFB.getProfile(new OnProfileListener() {
				@SuppressLint("ShowToast")
				public void onFail(String reason) {
					Log.e("onFail", "onFail");
					Toast.makeText(LandingActivity.this, "onFail",Toast.LENGTH_LONG);
					dialog.dismiss();
				};

				@SuppressLint("ShowToast")
				public void onException(Throwable throwable) {
					Log.e("onException", "onException");
					Toast.makeText(LandingActivity.this, "onException",Toast.LENGTH_LONG);
					dialog.dismiss();
				};

				public void onThinking() {
					Log.e("onThinking", "onThinking");
					Toast.makeText(LandingActivity.this, "onThinking",Toast.LENGTH_LONG);
					// dialog.dismiss();
				};

				public void onComplete(com.sromku.simple.fb.entities.Profile response) {

					Log.e("name", response.getName());

					jsonObject1 = new JSONObject();
					jsonObject2 = new JSONObject();
					try {
						jsonObject2.put(TAG_EMAIL, response.getEmail());
						jsonObject2.put(TAG_TYPE, "fb");
						jsonObject2.put(TAG_AUTHID, response.getId());
						jsonObject2.put(TAG_SCLNME, response.getName());
						jsonObject2.put(TAG_USRIMG,"http://graph.facebook.com/" + response.getId()+ "/picture?type=large");
						jsonObject1.put(TAG_OAUTHDTLS, jsonObject2);

						Log.v("input json", jsonObject1.toString());

						AstClassOuthLogin astClassOuthLogin = new AstClassOuthLogin();
						astClassOuthLogin.execute("");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				};
			});
		}
	};

	private class AstClassOuthLogin extends AsyncTask<String, String, Long> {

		@Override
		protected Long doInBackground(String... params) {
			try {
				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL + "oauthchkreg",nameValuePairs, jsonObject1.toString(),con,this);

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			dialog.dismiss();
			if (jObjList.toString() != null) {

				try {
					JSONObject objData = jObjList.getJSONObject(TAG_DATA);
					JSONObject objLogin = objData.getJSONObject(TAG_LOGIN);
					JSONObject objAccessToken = objData.getJSONObject(TAG_ACCESSTOKEN);
					JSONObject objErr = jObjList.getJSONObject(TAG_ERNODE);
					if (objErr.getString(TAG_ERCODE).equalsIgnoreCase("0")) {
						if (objLogin.getString("success").equalsIgnoreCase("true")) {
							_pEditor.putString("userName",objLogin.getString(TAG_USRNM));
							_pEditor.putString("userId",objLogin.getString(TAG_USRID));
							_pEditor.putString("userImg",objLogin.getString(TAG_USRIMG));
							_pEditor.putString("accessToken",objAccessToken.getString(TAG_TOKEN));
							_pEditor.putString("expiry",objAccessToken.getString(TAG_EXPIRY));

							_pEditor.putString("membershipNo",objLogin.getString(TAG_MEMBERSHIP));
							_pEditor.putString("points",objLogin.getString(TAG_POINTS));
							_pEditor.putString("userPhoneNo",objLogin.getString(TAG_PHONE));
							_pEditor.commit();

							startActivity(new Intent(LandingActivity.this,HomeActivity.class));
							overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
						} else {
							Toast.makeText(LandingActivity.this,objLogin.getString("message"),Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(LandingActivity.this,objErr.getString(TAG_ERMSG), Toast.LENGTH_LONG).show();
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void tryAgain(AsyncTask<String, String, Long> conAsync) {
		conAsync = new AstClassOuthLogin();
		conAsync.execute("");
		
	}
}
