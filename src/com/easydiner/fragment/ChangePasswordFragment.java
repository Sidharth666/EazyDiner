package com.easydiner.fragment;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.classes.Constant;
import com.classes.EasydinerClass;
import com.classes.JsonobjectPost;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.activities.DashboardActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class ChangePasswordFragment extends Fragment {

	private View rootView;
	private EditText etOldPasswdBashboard, etNewPasswdBashboard,
			etConfPasswdBashboard;
	private LinearLayout llBtnChangePassword;
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private static final String TAG_GETITEM = "getItem";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_OLDPASSWORD = "oldPassword";
	private static final String TAG_NEWPASSWORD = "newPassword";
	private static final String TAG_DATA = "data";
	private static final String TAG_ITEMDATA = "itemdata";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private List<NameValuePair> nameValuePairs;
	private SharedPreferences _pref;

	public static ChangePasswordFragment newInstance() {
		ChangePasswordFragment frag = new ChangePasswordFragment();
		return frag;
	}

	@SuppressWarnings("static-access")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.change_password_layout, container,false);
		
		Tracker t = ((EasydinerClass) getActivity().getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("ChangePasswordFragment");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
		
		((DashboardActivity) getActivity()).unSelectTab();
		initialize();
		onClick();
		return rootView;
	}

	private void initialize() {
		etOldPasswdBashboard = (EditText) rootView
				.findViewById(R.id.etOldPasswdBashboard);
		etNewPasswdBashboard = (EditText) rootView
				.findViewById(R.id.etNewPasswdBashboard);
		etConfPasswdBashboard = (EditText) rootView
				.findViewById(R.id.etConfPasswdBashboard);
		llBtnChangePassword = (LinearLayout) rootView
				.findViewById(R.id.llBtnChangePassword);
		_pref = new Pref(getActivity()).getSharedPreferencesInstance();
	}

	private void onClick() {
		llBtnChangePassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (etOldPasswdBashboard.getText().toString()
						.equalsIgnoreCase("")) {
					Toast.makeText(getActivity(), "Emter Old Password",
							Toast.LENGTH_LONG).show();
				} else if (etNewPasswdBashboard.getText().toString()
						.equalsIgnoreCase("")) {
					Toast.makeText(getActivity(), "Emter New Password",
							Toast.LENGTH_LONG).show();
				} else if (etConfPasswdBashboard.getText().toString()
						.equalsIgnoreCase("")) {
					Toast.makeText(getActivity(), "Emter Confirm Password",
							Toast.LENGTH_LONG).show();
				} else if (!etNewPasswdBashboard
						.getText()
						.toString()
						.equalsIgnoreCase(
								etConfPasswdBashboard.getText().toString())) {
					Toast.makeText(getActivity(),
							"New and confirm password do not match",
							Toast.LENGTH_LONG).show();
				} else {
					jsonObject1 = new JSONObject();
					jsonObject2 = new JSONObject();

					try {
						jsonObject2.put(TAG_ACCESSTOKN,
								_pref.getString("accessToken", ""));
						jsonObject2.put(TAG_OLDPASSWORD, etOldPasswdBashboard
								.getText().toString());
						jsonObject2.put(TAG_NEWPASSWORD,
								_pref.getString("accessToken", ""));
						jsonObject1.put(TAG_GETITEM, etNewPasswdBashboard
								.getText().toString());

						/*
						 * AstClassChangePassword astClassReservation = new
						 * AstClassChangePassword();
						 * astClassReservation.execute("");
						 */

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	private class AstClassChangePassword extends
			AsyncTask<String, String, Long> {

		private ProgressDialog dialog;

		public AstClassChangePassword() {
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage("Please Wait..");
			dialog.show();
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
		}

		@SuppressWarnings("static-access")
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser
						.getJSONObj(Constant.BASE_URL + "getWishList",
								nameValuePairs, jsonObject1.toString());

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			try {
				if (jObjList.toString() != null) {
					JSONObject objErrer = jObjList.getJSONObject(TAG_ERNODE);
					if (objErrer.getString(TAG_ERCODE).equalsIgnoreCase("0")) {
						JSONObject objData = jObjList.getJSONObject(TAG_DATA);
						JSONObject objItem = objData
								.getJSONObject(TAG_ITEMDATA);

						Toast.makeText(getActivity(),
								objItem.getString(TAG_MESSAGE),
								Toast.LENGTH_LONG).show();
					}
				}
			} catch (Exception e) {
				Toast.makeText(getActivity(), "Error in server side",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
