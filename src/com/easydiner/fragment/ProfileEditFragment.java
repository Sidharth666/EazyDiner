package com.easydiner.fragment;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.classes.AsyncTaskListener;
import com.classes.CommonFunction;
import com.classes.Constant;
import com.classes.CustomAlertProgressDialog;
import com.classes.EasydinerClass;
import com.classes.PostObject;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.activities.BooknowPopupActivity;
import com.easydiner.activities.DashboardActivity;
import com.easydiner.activities.DashboardHomeActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class ProfileEditFragment extends Fragment {

	private View rootView;
	private EditText etNameProfile, etPhoneProfile, etEmailProfile;
	private TextView tvPointsProfile, tvEditProfile, tvSaveProfile;
	private LinearLayout llName, llMobileNumber, llEmail, llPoint,
			llEditProfile, llSaveProfile;
	private CommonFunction _comFunc;
	private SharedPreferences _pref;
	private SharedPreferences.Editor _pEditor;

	public static ProfileEditFragment newInstance() {
		ProfileEditFragment frag = new ProfileEditFragment();
		return frag;
	}

	@SuppressWarnings("static-access")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		Tracker t = ((EasydinerClass) getActivity().getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("ProfileEditFragment");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		rootView = inflater.inflate(R.layout.profile_edit_layout, container,
				false);

		((DashboardActivity) getActivity()).unSelectTab();
		DashboardActivity.ivProfileDashBoard.setSelected(true);
		DashboardActivity.tvDashBoardHeading.setText("PROFILE");

		initialize();
		setTextFont();

		tvPointsProfile.setText(_pref.getString("points", "") + " points");
		etNameProfile.setText(_pref.getString("userName", ""));
		if (!_pref.getString("userPhoneNo", "").equalsIgnoreCase("")) {
			etPhoneProfile.setText(_pref.getString("userPhoneNo", ""));
		} else {
			etPhoneProfile.setHint("Enter Phone No.");
		}
		etEmailProfile.setText(_pref.getString("userId", ""));
		
		onClick();
		
		return rootView;
	}

	private void initialize() {

		etNameProfile = (EditText) rootView.findViewById(R.id.etNameEditProf);
		etPhoneProfile = (EditText) rootView
				.findViewById(R.id.etMobileNumberEditProf);
		etEmailProfile = (EditText) rootView.findViewById(R.id.etEmailEditProf);
		tvPointsProfile = (TextView) rootView
				.findViewById(R.id.tvPointsEditProf);
		tvPointsProfile.setText("500 points");
		tvEditProfile = (TextView) rootView.findViewById(R.id.tvEditProfile);
		tvEditProfile = (TextView) rootView.findViewById(R.id.tvEditProfile);
		tvSaveProfile = (TextView) rootView.findViewById(R.id.tvSaveProfile);
		llMobileNumber = (LinearLayout) rootView
				.findViewById(R.id.llMobileNumber);
		llEmail = (LinearLayout) rootView.findViewById(R.id.llEmail);
		llPoint = (LinearLayout) rootView.findViewById(R.id.llPoint);
		llEditProfile = (LinearLayout) rootView
				.findViewById(R.id.llEditProfile);
		llSaveProfile = (LinearLayout) rootView
				.findViewById(R.id.llSaveProfile);

		_comFunc = new CommonFunction(getActivity());
		_pref = new Pref(getActivity()).getSharedPreferencesInstance();
		_pEditor = new Pref(getActivity()).getSharedPreferencesEditorInstance();

	}

	private void setTextFont() {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/Aller_Lt.ttf";

		Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath2);
		Typeface tf3 = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath3);

		etNameProfile.setTypeface(tf2);
		etPhoneProfile.setTypeface(tf2);
		etEmailProfile.setTypeface(tf2);
		tvPointsProfile.setTypeface(tf2);
		tvEditProfile.setTypeface(tf3);
		tvSaveProfile.setTypeface(tf3);

	}

	private void onClick() {

		llEditProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etNameProfile.setEnabled(true);
				etNameProfile.setFocusable(true);
				etPhoneProfile.setEnabled(true);
				etPhoneProfile.setFocusable(true);

				llEditProfile.setVisibility(View.GONE);
				llEmail.setVisibility(View.GONE);
				llPoint.setVisibility(View.GONE);
				llSaveProfile.setVisibility(View.VISIBLE);
			}
		});

		llSaveProfile.setOnClickListener(new OnClickListener() {

			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (etNameProfile.getText().toString().equalsIgnoreCase("")) {
					Toast.makeText(getActivity(), "Please Enter Name",Toast.LENGTH_LONG).show();
				} else if (etPhoneProfile.getText().toString().equalsIgnoreCase("")) {
					Toast.makeText(getActivity(), "Please Enter Phone No.",Toast.LENGTH_LONG).show();
				} else if (etPhoneProfile.getText().length() < 10) {
					Toast.makeText(getActivity(),"Phone No. should be 10 digit", Toast.LENGTH_LONG).show();
				} else if (etEmailProfile.getText().toString().equalsIgnoreCase("")) {
					Toast.makeText(getActivity(), "Please Enter EmailId",Toast.LENGTH_LONG).show();
				} else if (etEmailProfile.getText().toString().indexOf("@") == -1 && etEmailProfile.getText().toString().indexOf(".") == -1) {
					Toast.makeText(getActivity(),"Please Enter Proper EmailId", Toast.LENGTH_LONG).show();
				} else {
					HashMap<String, PostObject> _postMap = new HashMap<String, PostObject>();

					_postMap.put("userId",_comFunc.getPostObject(_pref.getString("membershipNo", ""), false));

					_postMap.put("accessToken",_comFunc.getPostObject(_pref.getString("accessToken", ""), false));

					_postMap.put("name", _comFunc.getPostObject(etNameProfile.getText().toString(), false));

					_postMap.put("phone_no", _comFunc.getPostObject(etPhoneProfile.getText().toString(), false));

					_comFunc.callPostWebservice(Constant.BASE_URL+ "ProfileEdit", _postMap, _profileChangeAsync,true);

				}
			}
		});
	}

	AsyncTaskListener _profileChangeAsync = new AsyncTaskListener() {

		private AlertDialog pDialog;

		@Override
		public void onTaskPreExecute() {
			// TODO Auto-generated method stub

			pDialog = CustomAlertProgressDialog.getCustomDialog(getActivity(),"Please wait...");
			pDialog.show();

		}

		@SuppressLint("ShowToast")
		@Override
		public void onTaskCompleted(String result) {
			// TODO Auto-generated method stub
			Log.v("result", result);
			pDialog.dismiss();

			try {
				JSONObject jsonObj = new JSONObject(result);
				JSONObject dataObj = jsonObj.getJSONObject("data");

				String success = dataObj.getString("success");

				if (success.equalsIgnoreCase("true")) {

					_pEditor.putString("userName", etNameProfile.getText().toString());
					_pEditor.putString("userPhoneNo", etPhoneProfile.getText().toString());
					_pEditor.commit();

					Toast.makeText(getActivity(), "Profile Saved Successfully",Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			etNameProfile.setEnabled(false);
			etPhoneProfile.setEnabled(false);

			llSaveProfile.setVisibility(View.GONE);
			llEditProfile.setVisibility(View.VISIBLE);
			llEmail.setVisibility(View.VISIBLE);
			llPoint.setVisibility(View.VISIBLE);

		}
	};

}