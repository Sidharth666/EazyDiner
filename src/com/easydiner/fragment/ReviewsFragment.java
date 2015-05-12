package com.easydiner.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.classes.CommonFunction.TimeoutDialogTryAgainListener;
import com.classes.Constant;
import com.classes.CustomAlertProgressDialog;
import com.classes.EasydinerClass;
import com.classes.JsonobjectPost;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.activities.DashboardActivity;
import com.easydiner.dao.ReviewsDashboardAdapter;
import com.easydiner.dataobject.ReviewsDashboardChildItem;
import com.easydiner.dataobject.ReviewsDashboardItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class ReviewsFragment extends Fragment implements TimeoutDialogTryAgainListener{

	private ExpandableListView expandableListReviewView;
	private ArrayList<ReviewsDashboardItem> expDashboardItems;
	private ReviewsDashboardAdapter expReviewAdapter;
	private View rootView;
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private static final String TAG_GETITEM = "getItem";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_USERID = "userId";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_DATA = "data";
	private static final String TAG_ITEMDATA = "itemdata";
	private static final String TAG_ID = "id";
	private static final String TAG_RESTAURANTNAME = "restaurantName";
	private static final String TAG_IMAGE = "image";
	private static final String TAG_REVIEWSUBJECT = "reviewSubject";
	private static final String TAG_REVIEW = "review";

	private List<NameValuePair> nameValuePairs;
	private SharedPreferences _pref;
	private Context con;

	public static ReviewsFragment newInstance() {
		ReviewsFragment frag = new ReviewsFragment();
		return frag;
	}

	@SuppressWarnings("static-access")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.reviews_list_layout, container,false);

		Tracker t = ((EasydinerClass) getActivity().getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("ReviewsFragment");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		Initialize();
		con = getActivity();

		onclick();

		((DashboardActivity) getActivity()).unSelectTab();
		DashboardActivity.ivReviewDashBoard.setSelected(true);
		DashboardActivity.tvDashBoardHeading.setText("MY REVIEWS");

		expandableListReviewView.setAdapter(expReviewAdapter);
		expandableListReviewView.setGroupIndicator(null);
		loadList();

		return rootView;
	}

	private void onclick() {
		// TODO Auto-generated method stub

		expandableListReviewView
				.setOnGroupClickListener(new OnGroupClickListener() {

					@SuppressWarnings("static-access")
					@SuppressLint("ResourceAsColor")
					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						// TODO Auto-generated method stub
						// v.setBackgroundColor(Color.GRAY);

						ImageView symbol = (ImageView) v
								.findViewById(R.id.elvSymbol);

						if (expDashboardItems.get(groupPosition)
								.getOpenTagHome() == 0) {
							expDashboardItems.get(groupPosition)
									.setOpenTagHome(1);
							symbol.setImageResource(R.drawable.minus_black);
						} else {

							expDashboardItems.get(groupPosition)
									.setOpenTagHome(0);
							symbol.setImageResource(R.drawable.plus_black);
						}

						return false;
					}
				});

	}

	private void Initialize() {
		expandableListReviewView = (ExpandableListView) rootView
				.findViewById(R.id.expReviewsDashboard);
		expDashboardItems = new ArrayList<ReviewsDashboardItem>();
		expReviewAdapter = new ReviewsDashboardAdapter(getActivity(),
				expDashboardItems);
		_pref = new Pref(getActivity()).getSharedPreferencesInstance();
	}

	private void loadList() {
		jsonObject1 = new JSONObject();
		jsonObject2 = new JSONObject();

		try {
			jsonObject2.put(TAG_ACCESSTOKN, _pref.getString("accessToken", ""));
			jsonObject2.put(TAG_USERID, _pref.getString("membershipNo", ""));
			jsonObject1.put(TAG_GETITEM, jsonObject2);

			Log.v("result", jsonObject1.toString());

			AstClassMyReviews astClassReservation = new AstClassMyReviews();
			astClassReservation.execute("");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class AstClassMyReviews extends AsyncTask<String, String, Long> {

		private AlertDialog dialog;

		public AstClassMyReviews() {
			dialog = CustomAlertProgressDialog.getCustomDialog(getActivity(),"Please wait...");
			dialog.show();
		}

		@SuppressWarnings("static-access")
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL + "getMyReviews", nameValuePairs,jsonObject1.toString());

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

					try {
						JSONObject objErrer = jObjList
								.getJSONObject(TAG_ERNODE);
						if (objErrer.getString(TAG_ERCODE)
								.equalsIgnoreCase("0")) {
							JSONObject objData = jObjList
									.getJSONObject(TAG_DATA);

							JSONArray itemArray = objData
									.getJSONArray(TAG_ITEMDATA);

							if (itemArray.length() > 0) {
								for (int i = 0; i < itemArray.length(); i++) {
									ReviewsDashboardItem _item = new ReviewsDashboardItem();
									JSONObject _reviewItem = itemArray
											.getJSONObject(i);
									_item.setItemTitle(_reviewItem
											.getString(TAG_RESTAURANTNAME));
									_item.setItemImage(_reviewItem
											.getString(TAG_IMAGE));
									_item.setOpenTagHome(0);
									ArrayList<ReviewsDashboardChildItem> arrayListChild = new ArrayList<ReviewsDashboardChildItem>();
									ReviewsDashboardChildItem _chidItem = new ReviewsDashboardChildItem();
									_chidItem.setReviewSubject(_reviewItem
											.getString(TAG_REVIEWSUBJECT));
									_chidItem.setReview(_reviewItem
											.getString(TAG_REVIEW));
									arrayListChild.add(_chidItem);
									_item.setChildList(arrayListChild);
									expDashboardItems.add(_item);
								}
								expReviewAdapter.notifyDataSetChanged();
							} else {
								Toast.makeText(getActivity(),
										"No Reviews Available",
										Toast.LENGTH_LONG).show();
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast.makeText(getActivity(), "Error in server side",
								Toast.LENGTH_LONG).show();
					}
				}
			} catch (Exception e) {
				Toast.makeText(getActivity(), "Error in server side",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void tryAgain(AsyncTask<String, String, Long> conAsync) {
		conAsync = new AstClassMyReviews();
		conAsync.execute("");
		
	}
}
