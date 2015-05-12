package com.easydiner.activities;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classes.CommonFunction.TimeoutDialogTryAgainListener;
import com.classes.Constant;
import com.classes.CustomAlertProgressDialog;
import com.classes.EasydinerClass;
import com.classes.JsonobjectPost;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.dao.ExpItemListAdapter;
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

public class MenuListActivity extends EasyDinerBaseActivity implements ConnectionCallbacks, OnConnectionFailedListener , TimeoutDialogTryAgainListener{

	private ExpandableListView expandableListViewPopup;
	private View headerLayout, footerView;
	private ExpItemListAdapter expAdapter;
	private ImageView ivMenuMenu, ivMenuFbLogin, ivMenuGpLogin, ivMenuGenLogin,ivSearchItemMenu, ivMapViewMenu;
	private LinearLayout llWhatsHot, llDashBoard, llLogout;
	private TextView textCityName, textChangeCity, textWhatsHot,tvRegistranExtPopup, textDealsReg, textEazyRrendsReg,
			textDashBoard, textLogout, textDeals, textEazyRrends;
	private RelativeLayout rlChangeCity, rlEazyTrendsReg, rlDealsReg,rlEazyTrends, rlDeals;
	private SharedPreferences _pref;
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private SharedPreferences.Editor _pEditor;
	private List<NameValuePair> nameValuePairs;
	private Constant _constant;
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_DATA = "data";
	private static final String TAG_LOGOUT = "logout";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_LOGIN = "login";
	private static final String TAG_ACCESSTOKEN = "accessToken";
	private static final String TAG_USRID = "usrId";
	private static final String TAG_USRNM = "usrNm";
	private static final String TAG_USRIMG = "usrImg";
	private static final String TAG_MEMBERSHIP = "membership";
	private static final String TAG_POINTS = "points";
	private static final String TAG_TOKEN = "token";
	private static final String TAG_EXPIRY = "expiry";
	private static final String TAG_OAUTHDTLS = "oauthDtls";
	private static final String TAG_TYPE = "type";
	private static final String TAG_AUTHID = "authId";
	private static final String TAG_SCLNME = "sclNme";
	private static final String TAG_PHONE = "phone_no";

	private String profImageWhole;
	private int flagLogout = 0;
	private SimpleFacebook simpleFB;
	private static final int RC_SIGN_IN = 0;
	private GoogleApiClient mGoogleApiClient;
	private boolean mSignInClicked = true;
	private boolean mIntentInProgress;
	private AlertDialog dialogAuthLogin;
	public static int toAllListActivity = 0;
	private Context con;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ext_list_popup_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("MenuListActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		con = this;
		initilize();
		setTextFont();
		expandableListViewPopup.addFooterView(footerView);
		expandableListViewPopup.addHeaderView(headerLayout);
		expandableListViewPopup.setAdapter(expAdapter);
		//expandableListViewPopup.setGroupIndicator(null);
		expAdapter.notifyDataSetChanged();
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;

		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
			expandableListViewPopup.setIndicatorBounds(width- GetPixelFromDips(50), width - GetPixelFromDips(20));
		} else {
			expandableListViewPopup.setIndicatorBoundsRelative(width- GetPixelFromDips(50), width - GetPixelFromDips(20));
		}

		onClick();
	}

	public int GetPixelFromDips(float pixels) {
		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (pixels * scale + 0.5f);
	}

	private void initilize() {
		expandableListViewPopup = (ExpandableListView) findViewById(R.id.elvItemPopup);
		headerLayout = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_list_header, null, false);
		_pref = new Pref(MenuListActivity.this).getSharedPreferencesInstance();
		_pEditor = new Pref(MenuListActivity.this).getSharedPreferencesEditorInstance();
		expAdapter = new ExpItemListAdapter(MenuListActivity.this,Constant.expListItems);
		_constant = new Constant();

		llWhatsHot = (LinearLayout) headerLayout.findViewById(R.id.llWhatsHot);
		textCityName = (TextView) headerLayout.findViewById(R.id.textCityName);
		textChangeCity = (TextView) headerLayout.findViewById(R.id.textChangeCity);
		textWhatsHot = (TextView) headerLayout.findViewById(R.id.textWhatsHot);
		rlChangeCity = (RelativeLayout) headerLayout.findViewById(R.id.rlChangeCity);

		ivMapViewMenu = (ImageView) findViewById(R.id.ivMapViewMenu);
		ivSearchItemMenu = (ImageView) findViewById(R.id.ivSearchItemMenu);

		ivMenuMenu = (ImageView) findViewById(R.id.ivMenuMenu);
		jObjList = new JSONObject();
		mGoogleApiClient = new GoogleApiClient.Builder(MenuListActivity.this).addConnectionCallbacks(MenuListActivity.this)
				.addOnConnectionFailedListener(MenuListActivity.this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();

		if (_pref.getString("accessToken", "").equalsIgnoreCase("")) {
			footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.registration_layout, null, false);
			tvRegistranExtPopup = (TextView) footerView.findViewById(R.id.tvRegistranExtPopup);
			textDealsReg = (TextView) footerView.findViewById(R.id.textDealsReg);
			textEazyRrendsReg = (TextView) footerView.findViewById(R.id.textEazyRrendsReg);
			rlEazyTrendsReg = (RelativeLayout) footerView.findViewById(R.id.rlEazyTrendsReg);
			rlDealsReg = (RelativeLayout) footerView.findViewById(R.id.rlDealsReg);
			ivMenuFbLogin = (ImageView) footerView.findViewById(R.id.ivMenuFbLogin);
			ivMenuGpLogin = (ImageView) footerView.findViewById(R.id.ivMenuGpLogin);
			ivMenuGenLogin = (ImageView) footerView.findViewById(R.id.ivMenuGenLogin);

		} else {
			footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popupexpl_dashboard_footer, null, false);

			llDashBoard = (LinearLayout) footerView.findViewById(R.id.llDashBoard);
			llLogout = (LinearLayout) footerView.findViewById(R.id.llLogout);
			textDashBoard = (TextView) footerView.findViewById(R.id.textDashBoard);
			textLogout = (TextView) footerView.findViewById(R.id.textLogout);
			textDeals = (TextView) footerView.findViewById(R.id.textDeals);
			textEazyRrends = (TextView) footerView.findViewById(R.id.textEazyRrends);
			rlEazyTrends = (RelativeLayout) footerView.findViewById(R.id.rlEazyTrends);
			rlDeals = (RelativeLayout) footerView.findViewById(R.id.rlDeals);
		}
	}

	private void onClick() {

		ivMenuMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		rlChangeCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				textChangeCity.setText("More Cities Coming Soon");
			}
		});

		llWhatsHot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		if (_pref.getString("accessToken", "").equalsIgnoreCase("")) {
			
			ivMenuFbLogin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialogAuthLogin = CustomAlertProgressDialog.getCustomDialog(MenuListActivity.this,"Please wait...");
					dialogAuthLogin.show();
					simpleFB.login(onLoginListener);
				}
			});
			ivMenuGpLogin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mSignInClicked = true;
					dialogAuthLogin = CustomAlertProgressDialog.getCustomDialog(MenuListActivity.this,"Please wait...");
					dialogAuthLogin.show();
					mGoogleApiClient.connect();
				}
			});
			ivMenuGenLogin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(MenuListActivity.this,SignupActivity.class));
					overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);
				}
			});

			rlEazyTrendsReg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(MenuListActivity.this,EazyTrendsActivity.class));
					overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
				}
			});

			rlDealsReg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MenuListActivity.this,AlllistActivity.class);
					_pEditor.putString("catId", "menu/DEALS/0");
					_pEditor.putString("SubCatId", "menu/DEALS/0");
					_pEditor.commit();
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
				}
			});
		} else {
			rlEazyTrends.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(MenuListActivity.this,EazyTrendsActivity.class));
					overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
				}
			});

			rlDeals.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MenuListActivity.this,AlllistActivity.class);
					_pEditor.putString("catId", "menu/DEALS/0");
					_pEditor.putString("SubCatId", "menu/DEALS/0");
					_pEditor.commit();
					
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
				}
			});

			llDashBoard.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(MenuListActivity.this,DashboardHomeActivity.class));
					overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
				}
			});

			llLogout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					jsonObject1 = new JSONObject();
					jsonObject2 = new JSONObject();
					try {
						jsonObject2.put(TAG_ACCESSTOKN,_pref.getString("accessToken", ""));
						jsonObject2.put(TAG_EMAIL,_pref.getString("userId", ""));
						jsonObject1.put(TAG_LOGOUT, jsonObject2);

						Log.v("input data", jsonObject1.toString());

						AstClassLogout astClassOuthLogin = new AstClassLogout();
						astClassOuthLogin.execute("");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}

		expandableListViewPopup.setOnGroupClickListener(new OnGroupClickListener() {

					@SuppressWarnings("static-access")
					@SuppressLint("ResourceAsColor")
					@Override
					public boolean onGroupClick(ExpandableListView parent,View v, int groupPosition, long id) {

						if (expAdapter.getGroup(groupPosition).getOpenTagAlllist() == 0) {
							v.setBackgroundColor(Color.parseColor("#A8A09F"));
							expAdapter.getGroup(groupPosition).setOpenTagAlllist(1);
							// symbol.setImageResource(R.drawable.minus_white);
						} else {
							v.setBackgroundColor(Color.TRANSPARENT);
							expAdapter.getGroup(groupPosition).setOpenTagAlllist(0);
							// symbol.setImageResource(R.drawable.plus_white);
						}

						if (expAdapter.getGroup(groupPosition).getMainItem().equalsIgnoreCase("eazytrends")) {
							startActivity(new Intent(MenuListActivity.this,EazyTrendsActivity.class));
							overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
						}
						if (expAdapter.getGroup(groupPosition).getMainItem().equalsIgnoreCase("deals")) {
							Intent intent = new Intent(MenuListActivity.this,AlllistActivity.class);
							_pEditor.putString("catId", Constant.expListItems.get(groupPosition).getId());
							_pEditor.putString("SubCatId",Constant.expListItems.get(groupPosition).getId());
							_pEditor.commit();
							
							intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
							startActivity(intent);
						}
						// symbol.destroyDrawingCache();
						return false;
					}
				});

		ivSearchItemMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ivSearchItemMenu.setSelected(true);
				ivMapViewMenu.setSelected(false);

				startActivity(new Intent(MenuListActivity.this,SearchListActivity.class));
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});

		ivMapViewMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ivSearchItemMenu.setSelected(false);

				if (toAllListActivity == 1) {

					ivMapViewMenu.setSelected(true);

					Intent intent = new Intent(MenuListActivity.this,AlllistActivity.class);
					intent.putExtra("selectMapView", "1");
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

					toAllListActivity = 0;

				}

			}
		});
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (flagLogout == 1) {
			if (Constant.INDASHBOARD == 1) {
				
				startActivity(new Intent(MenuListActivity.this,HomeActivity.class));
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				finish();
			} else {
				HomeActivity.ivDashboardHome.setVisibility(View.GONE);
				HomeActivity.llSignupLayout.setVisibility(View.VISIBLE);
				HomeActivity.rlUsersummary.setVisibility(View.GONE);
				overridePendingTransition(R.anim.slide_down_info,R.anim.slide_up_info);
				finish();
			}
		} else {
			if (Constant.INDASHBOARD == 1) {
				
				startActivity(new Intent(MenuListActivity.this,HomeActivity.class));
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				finish();
			} else {
				overridePendingTransition(R.anim.slide_down_info,R.anim.slide_up_info);
				finish();
			}
		}
	}

	private void setTextFont() {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";

		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
		textWhatsHot.setTypeface(tf2);

		if (_pref.getString("accessToken", "").equalsIgnoreCase("")) {

			textDealsReg.setTypeface(tf2);
			textEazyRrendsReg.setTypeface(tf2);
			tvRegistranExtPopup.setTypeface(tf2);

		} else {
			textDashBoard.setTypeface(tf2);
			textLogout.setTypeface(tf2);
			textDeals.setTypeface(tf2);
			textEazyRrends.setTypeface(tf2);
		}
	}

	private class AstClassLogout extends AsyncTask<String, String, Long> {

		private AlertDialog dialog;

		public AstClassLogout() {
			dialog = CustomAlertProgressDialog.getCustomDialog(MenuListActivity.this,"Please wait...");
			dialog.show();
		}

		@Override
		protected Long doInBackground(String... params) {
			try {
				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL + "logout",nameValuePairs, jsonObject1.toString(),con,this);

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			dialog.dismiss();
			try {
				if (jObjList.toString() != null) {

					JSONObject objData = jObjList.getJSONObject(TAG_DATA);
					JSONObject objErr = objData.getJSONObject(TAG_ERNODE);
					if (objErr.getString(TAG_ERCODE).equalsIgnoreCase("0")) {
						_pEditor.putString("userName", "");
						_pEditor.putString("userId", "");
						_pEditor.putString("userImg", "");
						_pEditor.putString("membershipNo", "");
						_pEditor.putString("userPhoneNo", "");
						_pEditor.putString("points", "");
						_pEditor.putString("accessToken", "");
						_pEditor.putString("expiry", "");
						_pEditor.commit();

						Toast.makeText(MenuListActivity.this,objErr.getString(TAG_ERMSG), Toast.LENGTH_LONG).show();
						flagLogout = 1;
						onBackPressed();
					} else {
						Toast.makeText(MenuListActivity.this,objErr.getString(TAG_ERMSG), Toast.LENGTH_LONG).show();
					}

				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	OnLoginListener onLoginListener = new OnLoginListener() {

		@Override
		public void onFail(String reason) {
			dialogAuthLogin.dismiss();
		}

		@Override
		public void onException(Throwable throwable) {
			dialogAuthLogin.dismiss();
		}

		@Override
		public void onThinking() {

		}

		@Override
		public void onNotAcceptingPermissions(Type type) {
			dialogAuthLogin.dismiss();
		}

		@Override
		public void onLogin() {
			simpleFB.getProfile(new OnProfileListener() {
				public void onFail(String reason) {
					dialogAuthLogin.dismiss();
				};

				public void onException(Throwable throwable) {
					dialogAuthLogin.dismiss();
				};

				public void onThinking() {

				};

				public void onComplete(com.sromku.simple.fb.entities.Profile response) {
					jsonObject1 = new JSONObject();
					jsonObject2 = new JSONObject();
					try {
						jsonObject2.put(TAG_EMAIL, response.getEmail());
						jsonObject2.put(TAG_TYPE, "fb");
						jsonObject2.put(TAG_AUTHID, response.getId());
						jsonObject2.put(TAG_SCLNME, response.getName());
						jsonObject2.put(TAG_USRIMG,"http://graph.facebook.com/" + response.getId()+ "/picture?type=large");
						jsonObject1.put(TAG_OAUTHDTLS, jsonObject2);
						AstClassOuthLogin astClassOuthLogin = new AstClassOuthLogin();
						astClassOuthLogin.execute("");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				};
			});
		}
	};

	@Override
	public void onConnected(Bundle arg0) {
		mGoogleApiClient.connect();
		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
			Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
			Log.v("name", currentPerson.getDisplayName());
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
				jsonObject2.put(TAG_SCLNME, currentPerson.getDisplayName());
				jsonObject1.put(TAG_OAUTHDTLS, jsonObject2);

				Log.v("input data", jsonObject1.toString());

				AstClassOuthLogin astClassOuthLogin = new AstClassOuthLogin();
				astClassOuthLogin.execute("");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onConnectionSuspended(int result) {

	}

	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress && mSignInClicked && result.hasResolution()) {
			try {
				mSignInClicked = false;
				dialogAuthLogin.dismiss();
				mIntentInProgress = true;
				startIntentSenderForResult(result.getResolution().getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
			} catch (SendIntentException e) {
				// The intent was canceled before it was sent. Return to the default state and attempt to connect to get an updated ConnectionResult.
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

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
			dialogAuthLogin.dismiss();
			if (jObjList.toString() != null) {

				try {
					JSONObject objData = jObjList.getJSONObject(TAG_DATA);
					JSONObject objLogin = objData.getJSONObject(TAG_LOGIN);
					JSONObject objAccessToken = objData.getJSONObject(TAG_ACCESSTOKEN);
					JSONObject objErr = jObjList.getJSONObject(TAG_ERNODE);
					if (objErr.getString(TAG_ERCODE).equalsIgnoreCase("0")) {
						_pEditor.putString("userName",objLogin.getString(TAG_USRNM));
						_pEditor.putString("userId",objLogin.getString(TAG_USRID));
						_pEditor.putString("userImg",objLogin.getString(TAG_USRIMG));

						_pEditor.putString("accessToken",objAccessToken.getString(TAG_TOKEN));
						_pEditor.putString("expiry",objAccessToken.getString(TAG_EXPIRY));

						_pEditor.putString("membershipNo",objLogin.getString(TAG_MEMBERSHIP));
						_pEditor.putString("points",objLogin.getString(TAG_POINTS));
						_pEditor.putString("userPhoneNo",objLogin.getString(TAG_PHONE));
						_pEditor.commit();

						HomeActivity.ivDashboardHome.setVisibility(View.VISIBLE);
						HomeActivity.llSignupLayout.setVisibility(View.GONE);
						HomeActivity.rlUsersummary.setVisibility(View.VISIBLE);

						onBackPressed();

					} else {

						Toast.makeText(MenuListActivity.this,objErr.getString(TAG_ERMSG), Toast.LENGTH_LONG).show();
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void tryAgain(AsyncTask<String, String, Long> conAsync) {
		if(conAsync instanceof AstClassOuthLogin){
			conAsync = new AstClassOuthLogin();
			conAsync.execute("");
		}else{
//			conAsync = new AstClassLogout();
		}
		
		
	}
}
