package com.easydiner.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.classes.CommonFunction.TimeoutDialogTryAgainListener;
import com.classes.Constant;
import com.classes.CustomAlertProgressDialog;
import com.classes.EasydinerClass;
import com.classes.JsonobjectPost;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.activities.DashboardActivity;
import com.easydiner.activities.DetailsActivity;
import com.easydiner.dao.WishListAdapter;
import com.easydiner.dataobject.WishlistItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class WishlistFragment extends Fragment implements TimeoutDialogTryAgainListener{

	private ListView lvList;
	private WishListAdapter adpterObj;
	private ArrayList<WishlistItem> arrayList;
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
	private List<NameValuePair> nameValuePairs;
	private SharedPreferences _pref;
	private SharedPreferences.Editor _pEditor;
	int load_count = 0;
	private Context con;

	public static WishlistFragment newInstance() {
		WishlistFragment frag = new WishlistFragment();
		return frag;
	}

	@SuppressWarnings("static-access")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.wishlist_list_layout, container,false);
		
		Tracker t = ((EasydinerClass) getActivity().getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("WishlistFragment");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		con = getActivity();
		initialize();
		onClick();
		((DashboardActivity) getActivity()).unSelectTab();
		DashboardActivity.ivWishListDashBoard.setSelected(true);
		DashboardActivity.tvDashBoardHeading.setText("WISHLIST");

		lvList.setAdapter(adpterObj);
		loadListItem();

		return rootView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (load_count > 0) {
			arrayList.clear();
			adpterObj.notifyDataSetChanged();
			loadListItem();
		}

		load_count++;

	}
	
	private void onClick() {
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), DetailsActivity.class);
				_pEditor.putString("itemId",
						String.valueOf(arrayList.get(position).getId()));
				_pEditor.commit();
				Constant.NEAR_BY_LIST = 0;
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}

	private void initialize() {
		arrayList = new ArrayList<WishlistItem>();
		lvList = (ListView) rootView.findViewById(R.id.lvWishlist);
		adpterObj = new WishListAdapter(getActivity(), arrayList);
		_pref = new Pref(getActivity()).getSharedPreferencesInstance();
		_pEditor = new Pref(getActivity()).getSharedPreferencesEditorInstance();
	}

	private void loadListItem() {
		jsonObject1 = new JSONObject();
		jsonObject2 = new JSONObject();

		try {
			jsonObject2.put(TAG_ACCESSTOKN, _pref.getString("accessToken", ""));
			jsonObject2.put(TAG_USERID, _pref.getString("membershipNo", ""));
			jsonObject1.put(TAG_GETITEM, jsonObject2);

			Log.v("input", jsonObject1.toString());

			AstClassWishlist astClassReservation = new AstClassWishlist();
			astClassReservation.execute("");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class AstClassWishlist extends AsyncTask<String, String, Long> {

		private AlertDialog dialog;

		public AstClassWishlist() {
			dialog = CustomAlertProgressDialog.getCustomDialog(getActivity(),"Please wait...");
			dialog.show();
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
									JSONObject wishItem = itemArray
											.getJSONObject(i);
									WishlistItem _listItem = new WishlistItem();
									_listItem.setListImage(wishItem
											.getString(TAG_IMAGE));
									_listItem.setId(wishItem.getInt(TAG_ID));
									_listItem.setListTitle(wishItem
											.getString(TAG_RESTAURANTNAME));
									arrayList.add(_listItem);
								}
								adpterObj.notifyDataSetChanged();
							} else {
								Toast.makeText(getActivity(),
										"No Wishlist Item Available",
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
		conAsync = new AstClassWishlist();
		conAsync.execute("");
		
	}

}
