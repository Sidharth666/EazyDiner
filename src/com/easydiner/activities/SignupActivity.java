package com.easydiner.activities;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class SignupActivity extends EasyDinerBaseActivity implements TimeoutDialogTryAgainListener{

	private EditText etNameSignup, etMobileSignup, etEmailSignup,etPasswordSignup, etConpasswordSignup;
	private LinearLayout llSignUpSignup, llGotoSignin, llSignupBack;
	private static final String TAG_DATA = "data";
	private static final String TAG_LOGIN = "login";
	private static final String TAG_USRID = "usrId";
	private static final String TAG_USRNM = "usrNm";
	private static final String TAG_USRIMG = "usrImg";
	private static final String TAG_PHONE_NO = "phone_no";
	private static final String TAG_ACCESSTOKEN = "accessToken";
	private static final String TAG_TOKEN = "token";
	private static final String TAG_MEMBERSHIP = "membership";
	private static final String TAG_POINTS = "points";
	private static final String TAG_EXPIRY = "expiry";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_SINUPDTLS = "sinupDtls";
	private static final String TAG_NME = "nme";
	private static final String TAG_PHNO = "phno";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_PSW = "psw";
	private List<NameValuePair> nameValuePairs;
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private SharedPreferences.Editor _pEditor;
	private ConnectionDetector _connectionDetector;
	private TextView textSignupOne, textSignupTwo, textSignupThree,tvBookingText6;
	private Context con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("SignupActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		con = this;
		initialized();
		setTextFont();
		onClick();
	}

	private void initialized() {
		etNameSignup = (EditText) findViewById(R.id.etNameSignup);
		etMobileSignup = (EditText) findViewById(R.id.etMobileSignup);
		etEmailSignup = (EditText) findViewById(R.id.etEmailSignup);
		etPasswordSignup = (EditText) findViewById(R.id.etPasswordSignup);
		etConpasswordSignup = (EditText) findViewById(R.id.etConpasswordSignup);
		llSignUpSignup = (LinearLayout) findViewById(R.id.llSignUpSignup);
		_pEditor = new Pref(SignupActivity.this).getSharedPreferencesEditorInstance();
		llGotoSignin = (LinearLayout) findViewById(R.id.llGotoSignin);
		llSignupBack = (LinearLayout) findViewById(R.id.llSignupBack);
		_connectionDetector = new ConnectionDetector(SignupActivity.this);
		textSignupOne = (TextView) findViewById(R.id.textSignupOne);
		textSignupTwo = (TextView) findViewById(R.id.textSignupTwo);
		textSignupThree = (TextView) findViewById(R.id.textSignupThree);
		tvBookingText6 = (TextView) findViewById(R.id.tvBookingText6);
	}

	private void onClick() {

		etNameSignup.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				setEditTextHint();
				etNameSignup.setHint("");
				return false;
			}
		});
		etMobileSignup.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				setEditTextHint();
				etMobileSignup.setHint("");
				return false;
			}
		});
		etEmailSignup.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				setEditTextHint();
				etEmailSignup.setHint("");
				return false;
			}
		});
		etPasswordSignup.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				setEditTextHint();
				etPasswordSignup.setHint("");
				return false;
			}
		});
		etConpasswordSignup.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				setEditTextHint();
				etConpasswordSignup.setHint("");
				return false;
			}
		});

		llSignUpSignup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (_connectionDetector.isConnectingToInternet()) {
					if (etNameSignup.getText().toString().equals("")) {
						Toast.makeText(SignupActivity.this, "Please Give Name",Toast.LENGTH_LONG).show();
					} else if (etMobileSignup.getText().toString().equals("")) {
						Toast.makeText(SignupActivity.this,"Please Give Mobile No.", Toast.LENGTH_LONG).show();
					} else if (etMobileSignup.getText().toString().length() < 10) {
						Toast.makeText(SignupActivity.this,"Please Give A Proper Mobile No.",Toast.LENGTH_LONG).show();
					} else if (etEmailSignup.getText().toString().equals("")) {
						Toast.makeText(SignupActivity.this,"Please Give Email Id", Toast.LENGTH_LONG).show();
					} else if (etEmailSignup.getText().toString().indexOf("@") == -1&& etEmailSignup.getText().toString().indexOf(".") == -1) {
						Toast.makeText(SignupActivity.this,"Please Give Proper EmailId", Toast.LENGTH_LONG).show();
					} else if (etPasswordSignup.getText().toString().equals("")) {
						Toast.makeText(SignupActivity.this,"Please Give Password", Toast.LENGTH_LONG).show();
					} else if (etConpasswordSignup.getText().toString().equals("")) {
						Toast.makeText(SignupActivity.this,"Please Give Confirm Password",Toast.LENGTH_LONG).show();
					} else if (!etConpasswordSignup.getText().toString().equals(etPasswordSignup.getText().toString())) {
						Toast.makeText(SignupActivity.this,"Password and Confirm Password Are Not Same",Toast.LENGTH_LONG).show();
					} else {

						jsonObject1 = new JSONObject();
						jsonObject2 = new JSONObject();

						try {
							jsonObject2.put(TAG_NME, etNameSignup.getText().toString());
							jsonObject2.put(TAG_PHNO, etMobileSignup.getText().toString());
							jsonObject2.put(TAG_EMAIL, etEmailSignup.getText().toString());
							jsonObject2.put(TAG_PSW, etPasswordSignup.getText().toString());
							jsonObject1.put(TAG_SINUPDTLS, jsonObject2);

							AstClassSignUp astClassSignUp = new AstClassSignUp();
							astClassSignUp.execute("");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} else {
					Toast.makeText(SignupActivity.this,"No internet connection available",Toast.LENGTH_LONG).show();
				}
			}
		});

		llGotoSignin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SignupActivity.this,SigninActivity.class));
				overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);
			}
		});

		llSignupBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private class AstClassSignUp extends AsyncTask<String, String, Long> {

		private AlertDialog dialog;

		public AstClassSignUp() {
			dialog = CustomAlertProgressDialog.getCustomDialog(SignupActivity.this,"Please wait...");
			dialog.show();
		}

		@Override
		protected Long doInBackground(String... params) {
			try {
				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL + "signup",nameValuePairs, jsonObject1.toString(),con, this);

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
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
							_pEditor.putString("userPhoneNo",objLogin.getString(TAG_PHONE_NO));
							_pEditor.putString("accessToken",objAccessToken.getString(TAG_TOKEN));
							_pEditor.putString("expiry",objAccessToken.getString(TAG_EXPIRY));
							_pEditor.putString("membershipNo",objLogin.getString(TAG_MEMBERSHIP));
							_pEditor.putString("points",objLogin.getString(TAG_POINTS));
							_pEditor.commit();
							startActivity(new Intent(SignupActivity.this,HomeActivity.class));
							overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

						}

						else {

							Toast.makeText(SignupActivity.this,objLogin.getString("message"),Toast.LENGTH_LONG).show();
						}

					} else {

						etNameSignup.setText("");
						etMobileSignup.setText("");
						etEmailSignup.setText("");
						etPasswordSignup.setText("");
						etConpasswordSignup.setText("");
						setEditTextHint();

						Toast.makeText(SignupActivity.this,objErr.getString("erMsg"), Toast.LENGTH_LONG).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
	}

	private void setEditTextHint() {
		etNameSignup.setHint("NAME");
		etMobileSignup.setHint("MOBILE");
		etEmailSignup.setHint("EMAIL");
		etPasswordSignup.setHint("PASSWORD");
		etConpasswordSignup.setHint("CONFIRM PASSWORD");
	}

	private void setTextFont() {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);

		textSignupOne.setTypeface(tf1);
		textSignupTwo.setTypeface(tf1);
		textSignupThree.setTypeface(tf1);
		tvBookingText6.setTypeface(tf2);
		etNameSignup.setTypeface(tf2);
		etMobileSignup.setTypeface(tf2);
		etEmailSignup.setTypeface(tf2);
		etPasswordSignup.setTypeface(tf2);
		etConpasswordSignup.setTypeface(tf2);
	}

	@Override
	public void tryAgain(AsyncTask<String, String, Long> conAsync) {
		conAsync = new AstClassSignUp();
		conAsync.execute("");
		
	}
}
