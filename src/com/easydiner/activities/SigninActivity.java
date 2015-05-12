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

public class SigninActivity extends EasyDinerBaseActivity implements TimeoutDialogTryAgainListener{

	private EditText etEmailSignin, etPasswordSignin;
	private LinearLayout llSignInSignin, llSigninBack, llGotoSignup,
			llForgetPassword;
	private static final String TAG_DATA = "data";
	private static final String TAG_LOGIN = "login";
	private static final String TAG_USRID = "usrId";
	private static final String TAG_USRNM = "usrNm";
	private static final String TAG_USRIMG = "usrImg";
	private static final String TAG_PHONE_NO = "phone_no";
	private static final String TAG_ACCESSTOKEN = "accessToken";
	private static final String TAG_TOKEN = "token";
	private static final String TAG_EXPIRY = "expiry";
	private static final String TAG_MEMBERSHIP = "membership";
	private static final String TAG_POINTS = "points";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_LOGINDTLS = "loginDtls";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_PSW = "psw";
	private List<NameValuePair> nameValuePairs;
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private SharedPreferences.Editor _pEditor;
	private ConnectionDetector _connectionDetector;
	private TextView textSigninOne, textSignupGoto, textForgetPassword;
	private Context con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("SigninActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		con = this;
		initialization();
		setTextFont();
		onClick();
	}

	private void initialization() {
		etEmailSignin = (EditText) findViewById(R.id.etEmailSignin);
		etPasswordSignin = (EditText) findViewById(R.id.etPasswordSignin);
		llSignInSignin = (LinearLayout) findViewById(R.id.llSignInSignin);
		_pEditor = new Pref(SigninActivity.this).getSharedPreferencesEditorInstance();
		llSigninBack = (LinearLayout) findViewById(R.id.llSigninBack);
		_connectionDetector = new ConnectionDetector(SigninActivity.this);
		textSigninOne = (TextView) findViewById(R.id.textSigninOne);
		llGotoSignup = (LinearLayout) findViewById(R.id.llGotoSignup);
		textSignupGoto = (TextView) findViewById(R.id.textSignupGoto);
		llForgetPassword = (LinearLayout) findViewById(R.id.llForgetPassword);
		textForgetPassword = (TextView) findViewById(R.id.textForgetPassword);
	}

	private void onClick() {

		etEmailSignin.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				setEditTextHint();
				etEmailSignin.setHint("");
				return false;
			}
		});

		etPasswordSignin.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				setEditTextHint();
				etPasswordSignin.setHint("");
				return false;
			}
		});

		llSignInSignin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (_connectionDetector.isConnectingToInternet()) {
					if (etEmailSignin.getText().toString().equals("")) {
						Toast.makeText(SigninActivity.this,"Please Give Email Id", Toast.LENGTH_LONG).show();
					} else if (etEmailSignin.getText().toString().indexOf("@") == -1&& etEmailSignin.getText().toString().indexOf(".") == -1) {
						Toast.makeText(SigninActivity.this,"Please Give Proper EmailId", Toast.LENGTH_LONG).show();
					} else if (etPasswordSignin.getText().toString().equals("")) {
						Toast.makeText(SigninActivity.this,"Please Give Password", Toast.LENGTH_LONG).show();
					} else {
						jsonObject1 = new JSONObject();
						jsonObject2 = new JSONObject();

						try {
							jsonObject2.put(TAG_EMAIL, etEmailSignin.getText().toString());
							jsonObject2.put(TAG_PSW, etPasswordSignin.getText().toString());
							jsonObject1.put(TAG_LOGINDTLS, jsonObject2);

							Log.v("input", jsonObject1.toString());

							AstClassSignIn astClassSignIn = new AstClassSignIn();
							astClassSignIn.execute("");
						} catch (JSONException e) {

						}
					}
				} else {
					Toast.makeText(SigninActivity.this,"No internet connection available",Toast.LENGTH_LONG).show();
				}

			}
		});

		llSigninBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		llGotoSignup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		llForgetPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(SigninActivity.this,ForgetPasswordActivity.class));
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});
	}

	private class AstClassSignIn extends AsyncTask<String, String, Long> {

		private AlertDialog dialog;

		public AstClassSignIn() {
			dialog = CustomAlertProgressDialog.getCustomDialog(SigninActivity.this,"Please wait...");
			dialog.show();
		}

		@Override
		protected Long doInBackground(String... params) {
			try {
				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL + "userlogin",nameValuePairs, jsonObject1.toString(),con,this);

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
							_pEditor.putString("userPhoneNo",objLogin.getString(TAG_PHONE_NO));
							_pEditor.putString("membershipNo",objLogin.getString(TAG_MEMBERSHIP));
							_pEditor.putString("points",objLogin.getString(TAG_POINTS));
							_pEditor.putString("accessToken",objAccessToken.getString(TAG_TOKEN));
							_pEditor.putString("expiry",objAccessToken.getString(TAG_EXPIRY));
							_pEditor.commit();
							startActivity(new Intent(SigninActivity.this,HomeActivity.class));
							overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
						}

						else {
							Toast.makeText(SigninActivity.this,objLogin.getString("message"),Toast.LENGTH_LONG).show();
						}
					} else {
						
						etEmailSignin.setText("");
						etPasswordSignin.setText("");
						setEditTextHint();
						
						Toast.makeText(SigninActivity.this,objErr.getString("erMsg"), Toast.LENGTH_LONG).show();
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
		etEmailSignin.setHint("EMAIL");
		etPasswordSignin.setHint("PASSWORD");
	}

	private void setTextFont() {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);

		textSigninOne.setTypeface(tf1);
		etEmailSignin.setTypeface(tf2);
		etPasswordSignin.setTypeface(tf2);
		textSignupGoto.setTypeface(tf2);
		textForgetPassword.setTypeface(tf2);
	}

	@Override
	public void tryAgain(AsyncTask<String, String, Long> conAsync) {
		conAsync = new AstClassSignIn();
		conAsync.execute("");
		
	}
}
