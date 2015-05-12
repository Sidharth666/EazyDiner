package com.easydiner.activities;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
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
import com.classes.Constant;
import com.classes.CustomAlertProgressDialog;
import com.classes.EasydinerClass;
import com.classes.JsonobjectPost;
import com.classes.Pref;
import com.easydiner.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class ForgetPasswordActivity extends EasyDinerBaseActivity implements TimeoutDialogTryAgainListener{

	private TextView tvTitleForgotPassword, tvText1ForgotPassword,
			tvText2ForgotPassword, tvSendForgotPassword;
	private EditText etEmailForgotPassword;
	private LinearLayout llSendForgotPassword;
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private static final String TAG_GETITEM = "getItem";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_DATA = "data";
	private static final String TAG_ITEMDATA = "itemdata";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_USEREMAIL = "userEmail";
	private List<NameValuePair> nameValuePairs;
	private SharedPreferences _pref;
	private Context con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("ForgetPasswordActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		con = this;
		initialize();
		setFont();
		onClick();
	}

	private void setFont() {
		// TODO Auto-generated method stub
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";

		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);

		tvTitleForgotPassword.setTypeface(tf1);
		tvSendForgotPassword.setTypeface(tf1);
		tvText1ForgotPassword.setTypeface(tf2);
		tvText2ForgotPassword.setTypeface(tf2);
	}

	private void initialize() {
		// TODO Auto-generated method stub
		tvTitleForgotPassword = (TextView) findViewById(R.id.tvTitleForgotPassword);
		tvText1ForgotPassword = (TextView) findViewById(R.id.tvText1ForgotPassword);
		tvText2ForgotPassword = (TextView) findViewById(R.id.tvText2ForgotPassword);
		etEmailForgotPassword = (EditText) findViewById(R.id.etEmailForgotPassword);
		tvSendForgotPassword = (TextView) findViewById(R.id.tvSendForgotPassword);
		llSendForgotPassword = (LinearLayout) findViewById(R.id.llSendForgotPassword);
		_pref = new Pref(ForgetPasswordActivity.this).getSharedPreferencesInstance();
	}

	private void onClick() {
		etEmailForgotPassword.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				etEmailForgotPassword.setHint("");
				return false;
			}
		});
		llSendForgotPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (etEmailForgotPassword.getText().toString().equalsIgnoreCase("")) {
					Toast.makeText(ForgetPasswordActivity.this,"Enter Your Email ID", Toast.LENGTH_LONG).show();
				} else if (etEmailForgotPassword.getText().toString().indexOf("@") == -1&& etEmailForgotPassword.getText().toString().indexOf(".") == -1) {
					Toast.makeText(ForgetPasswordActivity.this,"Enter Currect Email ID", Toast.LENGTH_LONG).show();
				} else {
					jsonObject1 = new JSONObject();
					jsonObject2 = new JSONObject();

					try {

						jsonObject2.put(TAG_ACCESSTOKN,_pref.getString("accessToken", ""));
						jsonObject2.put(TAG_USEREMAIL, etEmailForgotPassword.getText().toString());

						jsonObject1.put(TAG_GETITEM, jsonObject2);

						AstClassForgetPassword astClassForgetPassword = new AstClassForgetPassword();
						astClassForgetPassword.execute("");

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private class AstClassForgetPassword extends AsyncTask<String, String, Long> {

		private AlertDialog dialog;

		public AstClassForgetPassword() {
			dialog = CustomAlertProgressDialog.getCustomDialog(ForgetPasswordActivity.this,"Please wait...");
			dialog.show();
		}

		@Override
		protected Long doInBackground(String... params) {
			try {
				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL+ "SetForgetPassword", nameValuePairs,jsonObject1.toString(),con, this);

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
					JSONObject objErrer = objData.getJSONObject(TAG_ERNODE);
					if (objErrer.getString(TAG_ERCODE).equalsIgnoreCase("0")) {
						JSONObject objItem = objData.getJSONObject(TAG_ITEMDATA);

						Toast.makeText(ForgetPasswordActivity.this,objItem.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
					}
				}
			} catch (Exception e) {
				Toast.makeText(ForgetPasswordActivity.this,"Error in server side", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void tryAgain(AsyncTask<String, String, Long> conAsync) {
		conAsync = new AstClassForgetPassword();
		conAsync.execute("");
		
	}
}
