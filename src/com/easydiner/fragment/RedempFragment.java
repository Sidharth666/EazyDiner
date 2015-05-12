package com.easydiner.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.classes.Constant;
import com.classes.EasydinerClass;
import com.classes.JsonobjectPost;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.activities.DashboardActivity;
import com.easydiner.dataobject.EarnItem;
import com.easydiner.dataobject.RedeempItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class RedempFragment extends Fragment {

	private TextView tvRedamption;
	private View rootView;
	private SharedPreferences _pref;
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private static final String TAG_GETITEM = "getItem";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_DATA = "data";
	private static final String TAG_ITEMDATA = "itemdata";
	private static final String TAG_REDEMPTIONTEXT = "redemptionText";
	private static final String TAG_USERID = "userId";
	private List<NameValuePair> nameValuePairs;
	private ArrayList<EarnItem> earnItems;
	private ArrayList<RedeempItem> redeempItems;
	private LinearLayout llEarnHeading, llEarnParentLayout, llRedeempHeading,
			llRedeempParentLayout;
	private TextView tvStatic;

	public static RedempFragment newInstance() {
		RedempFragment frag = new RedempFragment();
		return frag;
	}

	@SuppressWarnings("static-access")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.redemp_layout, container, false);
		
		Tracker t = ((EasydinerClass) getActivity().getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("RedempFragment");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		initialize();

		((DashboardActivity) getActivity()).unSelectTab();
		DashboardActivity.ivPiggyDashBoard.setSelected(true);
		DashboardActivity.tvDashBoardHeading.setText("POINTS AND REDEMPTION");

		loadRedenption();

		return rootView;
	}

	private void initialize() {
		// tvRedamption = (TextView) rootView.findViewById(R.id.tvRedamption);
		_pref = new Pref(getActivity()).getSharedPreferencesInstance();
		earnItems = new ArrayList<EarnItem>();
		redeempItems = new ArrayList<RedeempItem>();

		llEarnHeading = (LinearLayout) rootView
				.findViewById(R.id.llEarnHeading);
		llRedeempHeading = (LinearLayout) rootView
				.findViewById(R.id.llRedeempHeading);
		llEarnParentLayout = (LinearLayout) rootView
				.findViewById(R.id.llEarnParentLayout);
		llRedeempParentLayout = (LinearLayout) rootView
				.findViewById(R.id.llRedeempParentLayout);

		tvStatic = (TextView) rootView.findViewById(R.id.tvStatic);

		tvStatic.setText(Html
				.fromHtml("Not only this, now earn </h2><b>10 points</b> for every user review you post on EazyDiner.com!"));
		tvStatic.setTypeface(Typeface.createFromAsset(
				getActivity().getAssets(), "fonts/Aller_Rg.ttf"));
		tvStatic.setTextSize((float) 16.0);
		tvStatic.setVisibility(View.VISIBLE);

	}

	private void addItemInPage() {

		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath2);

		for (int i = 0; i < earnItems.size(); i++) {

			LinearLayout llRowContainer = new LinearLayout(getActivity());
			llRowContainer.setOrientation(LinearLayout.HORIZONTAL);
			LayoutParams llRowContainerParam = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

			TextView tvEarnPoint = new TextView(getActivity());
			tvEarnPoint.setText(String.valueOf(earnItems.get(i).getPoint())
					+ " points");
			tvEarnPoint.setTypeface(tf1);
			tvEarnPoint.setPadding(7, 2, 5, 2);
			tvEarnPoint.setTextSize((float) 16.0);

			TextView tvEarnReason = new TextView(getActivity());
			tvEarnReason.setText(earnItems.get(i).getReason());
			tvEarnReason.setTypeface(tf2);
			tvEarnReason.setPadding(5, 2, 16, 2);
			tvEarnReason.setTextSize((float) 16.0);

			llRowContainer.addView(tvEarnPoint);
			llRowContainer.addView(tvEarnReason);

			llEarnParentLayout.addView(llRowContainer, llRowContainerParam);

		}

		for (int i = 0; i < redeempItems.size(); i++) {

			LinearLayout llRowContainer = new LinearLayout(getActivity());
			llRowContainer.setOrientation(LinearLayout.HORIZONTAL);
			LayoutParams llRowContainerParam = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

			TextView tvRedeempPoint = new TextView(getActivity());
			tvRedeempPoint.setText(String.valueOf(redeempItems.get(i)
					.getPoint()) + " points");
			tvRedeempPoint.setTypeface(tf1);
			tvRedeempPoint.setPadding(7, 2, 5, 2);
			tvRedeempPoint.setTextSize((float) 16.0);

			TextView tvRedeempReason = new TextView(getActivity());
			tvRedeempReason.setText(": " + redeempItems.get(i).getReason());
			tvRedeempReason.setTypeface(tf2);
			tvRedeempReason.setPadding(5, 2, 16, 2);
			tvRedeempReason.setTextSize((float) 16.0);

			llRowContainer.addView(tvRedeempPoint);
			llRowContainer.addView(tvRedeempReason);

			llRedeempParentLayout.addView(llRowContainer, llRowContainerParam);

		}

	}

	private void loadRedenption() {
		jsonObject1 = new JSONObject();
		jsonObject2 = new JSONObject();

		try {
			jsonObject2.put(TAG_ACCESSTOKN, _pref.getString("accessToken", ""));
			jsonObject2.put(TAG_USERID, _pref.getString("membershipNo", ""));
			jsonObject1.put(TAG_GETITEM, jsonObject2);

			AstClassRedamption astClass = new AstClassRedamption();
			astClass.execute("");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class AstClassRedamption extends AsyncTask<String, String, Long> {

		private ProgressDialog dialog;

		public AstClassRedamption() {
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

				jObjList = j_parser.getJSONObj(Constant.BASE_URL
						+ "getRedemption", nameValuePairs,
						jsonObject1.toString());

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

					JSONObject jsonObj = new JSONObject(jObjList.toString());
					JSONObject dataObj = jsonObj.getJSONObject("data");
					JSONObject itemdataObj = dataObj.getJSONObject("itemdata");
					JSONArray earnArray = itemdataObj.getJSONArray("earn");

					if (earnArray.length() > 0) {

						for (int i = 0; i < earnArray.length(); i++) {

							llEarnHeading.setVisibility(View.VISIBLE);

							JSONObject earn = earnArray.getJSONObject(i);
							EarnItem _item = new EarnItem();
							_item.setPoint(Integer.parseInt(earn
									.getString("point")));
							_item.setReason(earn.getString("reason"));

							earnItems.add(_item);
						}

					}

					JSONArray redempArray = itemdataObj.getJSONArray("reedem");

					if (redempArray.length() > 0) {

						llRedeempHeading.setVisibility(View.VISIBLE);

						for (int i = 0; i < redempArray.length(); i++) {

							JSONObject redemp = redempArray.getJSONObject(i);
							RedeempItem _item = new RedeempItem();
							_item.setPoint(Integer.parseInt(redemp
									.getString("point")));
							_item.setReason(redemp.getString("reason"));

							redeempItems.add(_item);
						}
					}

					addItemInPage();

				}
			} catch (Exception e) {
				/*
				 * Toast.makeText(getActivity(), "Error in server side",
				 * Toast.LENGTH_LONG).show();
				 */

				e.printStackTrace();
			}
		}
	}
}