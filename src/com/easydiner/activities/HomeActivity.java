
package com.easydiner.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.easydiner.dao.ExpItemListAdapter;
import com.easydiner.dao.RestaurantAdapter;
import com.easydiner.dataobject.ExpListItem;
import com.easydiner.dataobject.RestorentItem;
import com.easydiner.dataobject.SubExpListItem;
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

@SuppressLint("NewApi")
public class HomeActivity extends EasyDinerBaseActivity implements ConnectionCallbacks, OnConnectionFailedListener, TimeoutDialogTryAgainListener {

	private ExpandableListView expandableListView;
	private ArrayList<ExpListItem> expListItems;
	private ExpItemListAdapter expAdapter;
	private ImageView ivMenuHome, ivFbLogin, ivGpLogin, ivGenLogin;
	private RelativeLayout reviews, Rlheader, rlHomeBtnBooknow,rlHomeBtnEazyCon, rlSearchByKeyword, rlEazyTrands, rlDeals;
	private TextView textEazyTraindsHome, textDealsHome, textSearchHome, textDealsLower, textUpcomingBooking, textEazyPoints,
			textSignupLogin;
	public static TextView tvEDPointHome;
	private LinearLayout llSearch, llRestName, llEdPoints;
	public static ImageView ivDashboardHome;
	public static RelativeLayout rlUsersummary;
	public static LinearLayout llSignupLayout;
	private static final String TAG_DATA = "data";
	private static final String TAG_CATID = "catId";
	private static final String TAG_CATNAME = "catName";
	private static final String TAG_SUBCATLIST = "subcatList";
	private static final String TAG_SUBCATID = "subcatId";
	private static final String TAG_SUBCATNAME = "subcatName";
	private SharedPreferences _pref;
	private String jsonListString;
	private JSONObject jObj;
	private JSONArray jsonArray;
	private Constant _constant;
	private EditText etResturentName;
	private ArrayAdapter<String> adapter;
	private PopupWindow popupWindow;
	private int bookPopupFlag = 0;
	private SharedPreferences.Editor _pEditor;
	private String profImageWhole;
	private int rescFlag = 0, rescShow = 0;
	private int restaurenId = 0, personNo = 1;
	private PopupWindow popupWindowRestorent;
	private RestaurantAdapter adpterObj;
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private List<NameValuePair> nameValuePairs;
	private ArrayList<RestorentItem> arrayList;
	private static final String TAG_ITEMDATA = "itemdata";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_OAUTHDTLS = "oauthDtls";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_TYPE = "type";
	private static final String TAG_AUTHID = "authId";
	private static final String TAG_SCLNME = "sclNme";
	private static final String TAG_LOGIN = "login";
	private static final String TAG_USRID = "usrId";
	private static final String TAG_USRNM = "usrNm";
	private static final String TAG_USRIMG = "usrImg";
	private static final String TAG_MEMBERSHIP = "membership";
	private static final String TAG_POINTS = "points";
	private static final String TAG_TOKEN = "token";
	private static final String TAG_EXPIRY = "expiry";
	private static final String TAG_GETITEM = "getItem";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_ACCESSTOKEN = "accessToken";
	private static final String TAG_RESNAME = "resName";
	private static final String TAG_PHONE = "phone_no";
	private View footerViewMain, footerView;
	int touchYCount = 0;
	private SimpleFacebook simpleFB;
	private static final int RC_SIGN_IN = 0;
	private GoogleApiClient mGoogleApiClient;
	private boolean mSignInClicked = true;
	private String personName;
	private boolean mIntentInProgress;
	private AlertDialog dialog;
	private ConnectionDetector _connectionDetector;
	private Context con;
	AlertDialog alertDialog ;

	@SuppressLint("CutPasteId")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("HomeActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		con = this;
		expListItems = new ArrayList<ExpListItem>();
		expandableListView = (ExpandableListView) findViewById(R.id.elvItem);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;

		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
			expandableListView.setIndicatorBounds(width - GetPixelFromDips(50),width - GetPixelFromDips(20));
		} else {
			expandableListView.setIndicatorBoundsRelative(width- GetPixelFromDips(50), width - GetPixelFromDips(20));
		}

		llSearch = (LinearLayout) findViewById(R.id.llSearch);
		textSearchHome = (TextView) findViewById(R.id.textSearchHome);

		Rlheader = (RelativeLayout) findViewById(R.id.Rlheader);
		rlHomeBtnEazyCon = (RelativeLayout) findViewById(R.id.rlHomeBtnEazyCon);
		rlHomeBtnBooknow = (RelativeLayout) findViewById(R.id.rlHomeBtnBooknow);
		ivDashboardHome = (ImageView) findViewById(R.id.ivDashboardHome);

		rlSearchByKeyword = (RelativeLayout) findViewById(R.id.search_by_keyword);
		ivMenuHome = (ImageView) findViewById(R.id.ivMenuHome);
		footerViewMain = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.homelist_footer_layout, null, false);
		rlUsersummary = (RelativeLayout) footerViewMain.findViewById(R.id.rlUsersummary);
		llSignupLayout = (LinearLayout) footerViewMain.findViewById(R.id.llSignupLayout);
		llEdPoints = (LinearLayout) footerViewMain.findViewById(R.id.llEdPoints);
		tvEDPointHome = (TextView) footerViewMain.findViewById(R.id.tvEDPointHome);
		textDealsLower = (TextView) footerViewMain.findViewById(R.id.textDealsLower);

		ImageView ivUpcomingBookingsHome = (ImageView) footerViewMain.findViewById(R.id.ivUpcomingBookingsHome);
		textEazyTraindsHome = (TextView) footerViewMain.findViewById(R.id.textEazyTraindsHome);
		textDealsHome = (TextView) footerViewMain.findViewById(R.id.textDealsHome);
		ivFbLogin = (ImageView) footerViewMain.findViewById(R.id.ivFbLogin);
		ivGpLogin = (ImageView) footerViewMain.findViewById(R.id.ivGpLogin);
		ivGenLogin = (ImageView) footerViewMain.findViewById(R.id.ivGenLogin);
		textUpcomingBooking = (TextView) footerViewMain.findViewById(R.id.textUpcomingBooking);
		textEazyPoints = (TextView) footerViewMain.findViewById(R.id.textEazyPoints);
		textSignupLogin = (TextView) footerViewMain.findViewById(R.id.textSignupLogin);

		rlEazyTrands = (RelativeLayout) footerViewMain.findViewById(R.id.rlEazyTrands);
		rlDeals = (RelativeLayout) footerViewMain.findViewById(R.id.rlDeals);

		_pref = new Pref(this).getSharedPreferencesInstance();
		_pEditor = new Pref(HomeActivity.this).getSharedPreferencesEditorInstance();
		_connectionDetector = new ConnectionDetector(HomeActivity.this);

		_constant = new Constant();

		mGoogleApiClient = new GoogleApiClient.Builder(HomeActivity.this).addConnectionCallbacks(HomeActivity.this)
				.addOnConnectionFailedListener(HomeActivity.this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();

		if (_pref.getString("accessToken", "").equalsIgnoreCase("")) {
			ivDashboardHome.setVisibility(View.GONE);
			llSignupLayout.setVisibility(View.VISIBLE);
			rlUsersummary.setVisibility(View.GONE);
		} else {
			String fontPath1 = "fonts/Aller_Lt.ttf";
			Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);

			tvEDPointHome.setTypeface(tf1);
			ivDashboardHome.setVisibility(View.VISIBLE);
			llSignupLayout.setVisibility(View.GONE);
			rlUsersummary.setVisibility(View.VISIBLE);
			tvEDPointHome.setText(_pref.getString("points", ""));
		}
		setTestFont();
		expandableListView.addFooterView(footerViewMain);
		// expandableListView.setGroupIndicator(null);
		expAdapter = new ExpItemListAdapter(this, expListItems);
		expandableListView.setAdapter(expAdapter);
		arrayList = new ArrayList<RestorentItem>();
		adpterObj = new RestaurantAdapter(HomeActivity.this, arrayList);

		try {
			jsonListString = getStringFromFile(_pref.getString(Constant.JSON_SERVICE_FILE_NAME, ""));
			jObj = new JSONObject(jsonListString);
			jsonArray = jObj.getJSONArray(TAG_DATA);

			for (int j = 0; j < jsonArray.length(); j++) {

				JSONObject objCategory = jsonArray.getJSONObject(j);
				if (!objCategory.getString(TAG_CATNAME).equalsIgnoreCase("DEALS")&& !objCategory.getString(TAG_CATNAME).equalsIgnoreCase("EAZYTRENDS")) {
					ExpListItem _group1 = new ExpListItem();
					_group1.setMainItem(objCategory.getString(TAG_CATNAME));
					_group1.setId(objCategory.getString(TAG_CATID));
					_group1.setOpenTagHome(0);
					_group1.setOpenTagEazyTrands(0);
					_group1.setOpenTagDetails(0);
					_group1.setOpenTagAlllist(0);

					if (objCategory.has(TAG_SUBCATLIST)) {
						JSONArray jsonArrSubcat = objCategory.getJSONArray(TAG_SUBCATLIST);

						ArrayList<SubExpListItem> _arrArrayList = new ArrayList<SubExpListItem>();

						for (int i = 0; i < jsonArrSubcat.length(); i++) {
							SubExpListItem _sub1 = new SubExpListItem();

							JSONObject objSubcat = jsonArrSubcat.getJSONObject(i);

							_sub1.setSubItem(objSubcat.getString(TAG_SUBCATNAME));
							_sub1.setId(objSubcat.getString(TAG_SUBCATID));
							_arrArrayList.add(_sub1);
						}

						_group1.setArrayList(_arrArrayList);
					}
					expListItems.add(_group1);
					if (Constant.EMPTY_CHECK == 0) {
						Constant.expListItems.add(_group1);
					}
				}
			}
			Constant.EMPTY_CHECK = 1;

		} catch (Exception e) {
			e.printStackTrace();
		}

		expAdapter.notifyDataSetChanged();

		setTextFont();

		ivUpcomingBookingsHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(HomeActivity.this,DashboardHomeActivity.class);
				i.putExtra("upcoming_booking", "1");
				startActivity(i);
			}
		});

		llEdPoints.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(HomeActivity.this,DashboardActivity.class);
				startActivity(i);
			}
		});

		llSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this,SearchListActivity.class));
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});
		rlHomeBtnBooknow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				rlHomeBtnBooknow.setBackgroundColor(Color.parseColor("#9B9A9A"));
//				BooknowPopupActivity.rlBookNowBultton = rlHomeBtnBooknow;
				startActivity(new Intent(HomeActivity.this,BookRestaurentActivity.class));
				overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);
			}
		});

		rlHomeBtnEazyCon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String number = "7861004444";
				Intent dial = new Intent();
				dial.setAction("android.intent.action.DIAL");
				dial.setData(Uri.parse("tel:" + number));
				startActivity(dial);
			}
		});

		ivMenuHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this,MenuListActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_down_info,R.anim.slide_up_info);
			}
		});

		rlDeals.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this,AlllistActivity.class);
				_pEditor.putString("catId", "menu/DEALS/0");
				_pEditor.putString("SubCatId", "menu/DEALS/0");
				_pEditor.commit();
				Constant.NEAR_BY_LIST = 0;
				Constant.FILTER_BY_LIST = 0;
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});

		rlEazyTrands.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this,EazyTrendsActivity.class));
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});

		ivDashboardHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this,DashboardHomeActivity.class));
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});

		ivFbLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (_connectionDetector.isConnectingToInternet()) {
					dialog = CustomAlertProgressDialog.getCustomDialog(HomeActivity.this,"Please wait...");
					dialog.show();
					simpleFB.login(onLoginListener);
				} else {
					Toast.makeText(HomeActivity.this,"No internet connection available",Toast.LENGTH_LONG).show();
				}
			}
		});

		ivGpLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (_connectionDetector.isConnectingToInternet()) {
					mSignInClicked = true;
					dialog = CustomAlertProgressDialog.getCustomDialog(HomeActivity.this,"Please wait...");
					dialog.show();
					mGoogleApiClient.connect();
				} else {
					Toast.makeText(HomeActivity.this,"No internet connection available",Toast.LENGTH_LONG).show();
				}
			}
		});

		ivGenLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this,SignupActivity.class));
				overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);
			}
		});
	}

	public int GetPixelFromDips(float pixels) {
		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (pixels * scale + 0.5f);
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

	public String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		reader.close();
		return sb.toString();
	}

	public String getStringFromFile(String filePath) throws Exception {
		File fl = new File(filePath);
		FileInputStream fin = new FileInputStream(fl);
		String ret = convertStreamToString(fin);
		// Make sure you close all streams.
		fin.close();
		return ret;
	}

	@Override
	public void onBackPressed() {
		if (bookPopupFlag == 1) {
			popupWindow.dismiss();
			bookPopupFlag = 0;
		} else {
			
			final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);
			alertDialogBuilder.setTitle("Confirm Exit");
			alertDialogBuilder.setMessage("Are you sure you want to Exit?");
			alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0,int arg1) {
							Intent intent = new Intent(Intent.ACTION_MAIN);
							intent.addCategory(Intent.CATEGORY_HOME);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}
					});
			
			
			alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					alertDialog.cancel();
				}
			});

			alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			
			
		}
	}

	/*private void displayRescPopup() {
		rescShow = 1;

		LayoutInflater layoutinflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View popupView = layoutinflater.inflate(R.layout.restorent_list_layout,null);

		popupWindowRestorent = new PopupWindow(popupView,android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		popupWindowRestorent.setContentView(popupView);
		popupWindowRestorent.setFocusable(true);
		popupWindow.setFocusable(false);
		
		 * int OFFSET_X = -20; int OFFSET_Y = 95;
		 

		final ListView lvRestListPopup = (ListView) popupView.findViewById(R.id.lvRestList);

		lvRestListPopup.setAdapter(adpterObj);

		lvRestListPopup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				rescFlag = 1;
				etResturentName.setText(arrayList.get(position).getName());
				restaurenId = arrayList.get(position).getId();
				popupWindowRestorent.dismiss();
			}
		});

		
		 * popupWindowRestorent.showAtLocation(layout, Gravity.NO_GRAVITY, p.x +
		 * OFFSET_X, p.y + OFFSET_Y);
		 
		findViewById(R.id.rl_home_mainlayout).post(new Runnable() {

			@Override
			public void run() {
				popupWindowRestorent.showAsDropDown(llRestName);
			}
		});

	}*/

	/*private class AstClassRestName extends AsyncTask<String, String, Long> {
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {

				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL+ "autofillrestaurant", nameValuePairs,jsonObject1.toString());

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
					JSONArray listArray = objData.getJSONArray(TAG_ITEMDATA);
					JSONObject objErr = jObjList.getJSONObject(TAG_ERNODE);
					arrayList.clear();
					if (objErr.get(TAG_ERCODE).toString().equalsIgnoreCase("0")) {
						for (int i = 0; i < listArray.length(); i++) {
							JSONObject jsonObject = listArray.getJSONObject(i);
							RestorentItem _item = new RestorentItem();
							_item.setId(jsonObject.getInt(TAG_ID));
							_item.setName(jsonObject.getString(TAG_NAME));

							arrayList.add(_item);
						}

						adpterObj.notifyDataSetChanged();
						if (rescShow == 1) {
							popupWindowRestorent.dismiss();
						}
						displayRescPopup();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}
	}*/

	private void setTextFont() {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/Aller_Lt.ttf";

		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
		Typeface tf3 = Typeface.createFromAsset(getAssets(), fontPath3);

		textEazyTraindsHome.setTypeface(tf2);
		textDealsHome.setTypeface(tf2);
		textDealsLower.setTypeface(tf3);
		textUpcomingBooking.setTypeface(tf2);
		textEazyPoints.setTypeface(tf2);
		textSignupLogin.setTypeface(tf2);
	}

	OnLoginListener onLoginListener = new OnLoginListener() {

		@Override
		public void onFail(String reason) {
			dialog.dismiss();
		}

		@Override
		public void onException(Throwable throwable) {
			dialog.dismiss();
		}

		@Override
		public void onThinking() {

		}

		@Override
		public void onNotAcceptingPermissions(Type type) {
			dialog.dismiss();
		}

		@Override
		public void onLogin() {
			simpleFB.getProfile(new OnProfileListener() {
				public void onFail(String reason) {
					dialog.dismiss();
				};

				public void onException(Throwable throwable) {
					dialog.dismiss();
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
						jsonObject2.put(TAG_USRIMG,"http://graph.facebook.com/" + response.getId()+ "/picture?type=large");
						jsonObject2.put(TAG_SCLNME, response.getName());
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

	public void onConnected(Bundle arg0) {
		mGoogleApiClient.connect();
		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
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

				Log.v("input data", jsonObject1.toString());

				AstClassOuthLogin astClassOuthLogin = new AstClassOuthLogin();
				astClassOuthLogin.execute("");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void onConnectionSuspended(int arg0) {
		Log.e("suspended", "suspended");
	}

	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress && mSignInClicked && result.hasResolution()) {
			try {
				mSignInClicked = false;
				dialog.dismiss();
				mIntentInProgress = true;
				startIntentSenderForResult(result.getResolution().getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
			} catch (SendIntentException e) {
				// The intent was canceled before it was sent. Return to the default state and attempt to connect to get an updated ConnectionResult.
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	private void setTestFont() {
		String fontPath1 = "fonts/Aller_Lt.ttf";
		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		textSearchHome.setTypeface(tf1);
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

							String fontPath1 = "fonts/Aller_Lt.ttf";
							Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);

							tvEDPointHome.setTypeface(tf1);
							ivDashboardHome.setVisibility(View.VISIBLE);
							llSignupLayout.setVisibility(View.GONE);
							rlUsersummary.setVisibility(View.VISIBLE);
							tvEDPointHome.setText(_pref.getString("points", ""));

						}

						else {
							Toast.makeText(HomeActivity.this,objLogin.getString("message"),Toast.LENGTH_LONG).show();
						}

					} else {
						Toast.makeText(HomeActivity.this,objErr.getString(TAG_ERMSG), Toast.LENGTH_LONG).show();
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
