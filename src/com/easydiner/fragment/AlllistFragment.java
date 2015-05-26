package com.easydiner.fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classes.CommonFunction.TimeoutDialogTryAgainListener;
import com.classes.ConnectionDetector;
import com.classes.Constant;
import com.classes.CustomAlertProgressDialog;
import com.classes.EasydinerClass;
import com.classes.JsonobjectPost;
import com.classes.MyFixedListView;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.activities.AlllistActivity;
import com.easydiner.activities.DetailsActivity;
import com.easydiner.dao.ItemlistAdapter;
import com.easydiner.dataobject.FilterChildItem;
import com.easydiner.dataobject.FilterGroupItem;
import com.easydiner.dataobject.ListItem;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class AlllistFragment extends Fragment implements TimeoutDialogTryAgainListener{

	private MyFixedListView lvList;
	private ItemlistAdapter adpterObj;
	private ArrayList<ListItem> arrayList;
	public static HashMap<Integer, String> filterItem;
	private ExpandableListView elvItemFilterCuisine;
	private FilterItemAdapter filterCuisieAdapter;
	private ArrayList<FilterGroupItem> filterGroupCuisie;
	private static final String TAG_DATA = "data";
	private static final String TAG_GETNEARITEM = "getNearItem";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_LAT = "lat";
	private static final String TAG_LONG = "long";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_GETCATITEM = "getCatItem";
	private static final String TAG_CURRENTPAGE = "currentPage";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_PRICE = "price";
	private static final String TAG_TOTALPAGE = "totalPage";
	private static final String TAG_SPECIALITY = "speciality";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_DISTANCE = "distance";
	private static final String TAG_IMG = "img";
	private static final String TAG_CRITICRATING = "criticRating";
	private static final String TAG_USERRATING = "userRating";
	private static final String TAG_REVIEWED = "reviewed";
	private static final String TAG_NEWTAG = "newTag";
	private static final String TAG_EAZYDEALS = "eazyDeals";
	private static final String TAG_CATDTLS = "catDtls";
	private static final String TAG_FILTERDTLS = "filterDtls";
	private static final String TAG_CUISINE_ID = "cuisine_id";
	private static final String TAG_LOCATION_ID = "location_id";
	private static final String TAG_PRICE_ID = "price_id";
	private static final String TAG_SUBCATID = "subCatId";
	private static final String TAG_CATID = "catId";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_HEADING = "heading";
	private static final String TAG_FILTERING_VALUE = "filtering_value";
	private static final String TAG_SEARCHITEM = "searchItem";
	private SharedPreferences _pref;
	private SharedPreferences.Editor _pEditor;
	private String errorCode, errorMsg, jsonListString, cuisine_id = "",location_id = "", price_id = "";
	private JSONObject jObjList, jsonObject1, jsonObject2, jsonObject3;
	private List<NameValuePair> nameValuePairs;
	private Constant _constant;
	private DetailsActivity detailsActivity;
	private LinearLayout testLayoutFilter;
	private RelativeLayout testLayout, rlClear;
	private int total_page = 0, current_page = 1, flagPagi = 0,flagFilterShow = 0, flagFilterDeals = 0, flagCuisineSelection = 0;;
	private View listFooter, inflaterFilterFooter;
	private TextView tvFilterHeader, textFilter;
	public static TextView textClear;
	public static ImageView ivFilter;
	private ConnectionDetector _connectionDetector;
	private LocationManager locationManager;
	private boolean isGPSEnabled = false;
	private String CatId, SubCatId;
	private int[] selectedPosition = { -1, -1};
	private HashMap<String, String> filterParameter;
	private View rootView;
	private static final String TAG_CUISINEID = "cuisineId";
	private static final String TAG_PRICEID = "priceId";
	private Context con;

	public static AlllistFragment newInstance() {

		AlllistFragment frag = new AlllistFragment();
		return frag;
	}

	@SuppressLint({ "UseSparseArrays", "CutPasteId" })
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.allist_fragment_layout, container,false);
		
		Tracker t = ((EasydinerClass) getActivity().getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("AlllistFragment");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
		
		con = getActivity();
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!isGPSEnabled) {
			Constant.GPS_STATUS = false;
		} else {
			Constant.GPS_STATUS = true;
		}
		Log.v("gps", String.valueOf(Constant.GPS_STATUS));
		filterItem = new HashMap<Integer, String>();
		lvList = (MyFixedListView) rootView.findViewById(R.id.lvMainList);
		arrayList = new ArrayList<ListItem>();
		_constant = new Constant();
		_pref = new Pref(getActivity()).getSharedPreferencesInstance();
		adpterObj = new ItemlistAdapter(getActivity(), arrayList,_pref);
		_pEditor = new Pref(getActivity()).getSharedPreferencesEditorInstance();
		testLayoutFilter = (LinearLayout) rootView.findViewById(R.id.testLayoutFilter);
		testLayout = (RelativeLayout) rootView.findViewById(R.id.testLayout);
		ivFilter = (ImageView) rootView.findViewById(R.id.ivFilter);
		textFilter = (TextView) rootView.findViewById(R.id.textFilter);
		textClear = (TextView) rootView.findViewById(R.id.textClear);
		rlClear = (RelativeLayout) rootView.findViewById(R.id.rlClear);

		/*
		 * elvItemFilterLocation = (ExpandableListView) rootView
		 * .findViewById(R.id.elvItemFilterLocation); elvItemFilterPrice =
		 * (ExpandableListView) rootView .findViewById(R.id.elvItemFilterPrice);
		 */
		_connectionDetector = new ConnectionDetector(getActivity());

		loadFilterList();
		/*
		 * elvItemFilterLocation.setAdapter(filterLocationAdapter);
		 * elvItemFilterPrice.setAdapter(filterPriceAdapter);
		 */

		detailsActivity = new DetailsActivity();
		lvList.setAdapter(adpterObj);
		listFooter = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.pagination_footer, null, false);

		onClick();

		return rootView;
	}

	private void onClick() {

		testLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (flagFilterShow == 0) {
					CatId = _pref.getString("catId", "");
					SubCatId = _pref.getString("SubCatId", "");
					cuisine_id = _pref.getString("SubCatName", "");
					Log.v("cuisine_id", cuisine_id);
					// loadFilterList();
					elvItemFilterCuisine.setVisibility(View.VISIBLE);
					/*
					 * elvItemFilterLocation.setVisibility(View.VISIBLE);
					 * elvItemFilterPrice.setVisibility(View.VISIBLE);
					 */
					flagFilterShow = 1;
				} else {
					elvItemFilterCuisine.setVisibility(View.GONE);
					/*
					 * elvItemFilterLocation.setVisibility(View.GONE);
					 * elvItemFilterPrice.setVisibility(View.GONE);
					 */
					flagFilterShow = 0;
				}
			}
		});

		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Log.v("item id",String.valueOf(arrayList.get(position).getItemId()));
				//Flurry event
				Map<String, String> allListParams = new HashMap<String, String>();
				String restName = arrayList.get(position).getItemname();
				allListParams.put("ListingScreen", restName);
				FlurryAgent.logEvent("ListingScreen", allListParams,true);
				
				Intent i = new Intent(getActivity(), DetailsActivity.class);
				_pEditor.putString("itemImage", arrayList.get(position).getImage());
				_pEditor.putString("itemId",String.valueOf(arrayList.get(position).getItemId()));
				_pEditor.putString("itemType", arrayList.get(position).getItemtype());
				_pEditor.putString("itemPosition", String.valueOf(position));
				_pEditor.putBoolean("itemReviewed", arrayList.get(position).getReviewed());
				_pEditor.commit();
				startActivity(i);
				getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
				
				FlurryAgent.endTimedEvent("ListingScreen");
			}
		});

		lvList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {

				if (totalItemCount == (visibleItemCount + firstVisibleItem) && totalItemCount != 0) {

					if (total_page > current_page && flagPagi != 1) {
						current_page++;
						Log.e("total_page", total_page + "");
						Log.e("current_page", current_page + "");
						flagPagi = 1;
						loadList();
					}
					if (total_page == current_page && flagPagi != 1) {
							lvList.removeFooterView(listFooter);
					}
				}
			}
		});


		rlClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				clearFilters();
				loadList();
			}
		});
	}

	@SuppressWarnings("static-access")
	public void loadList() {
		try {
			Log.e("Constant.FILTER_BY_LIST",String.valueOf(Constant.FILTER_BY_LIST));
			if (Constant.FILTER_BY_LIST == 0) {
				if (Constant.NEAR_BY_LIST == 1) {
					jsonObject2 = new JSONObject();
					jsonObject1 = new JSONObject();
					jsonObject2.put(TAG_ACCESSTOKN, "");
					jsonObject2.put(TAG_LAT, _pref.getString("currLat", ""));
					jsonObject2.put(TAG_LONG, _pref.getString("currLng", ""));
					jsonObject2.put(TAG_CURRENTPAGE, current_page);
					jsonObject1.put(TAG_GETNEARITEM, jsonObject2);
				} else if (Constant.NEAR_BY_LIST == 2) {
					jsonObject2 = new JSONObject();
					jsonObject1 = new JSONObject();
					jsonObject2.put(TAG_ACCESSTOKN, "");
					jsonObject2.put(TAG_EMAIL, "");
					jsonObject2.put(TAG_LAT, _pref.getString("currLat", ""));
					jsonObject2.put(TAG_LONG, _pref.getString("currLng", ""));
					jsonObject2.put(TAG_CURRENTPAGE, current_page);
					jsonObject1.put(TAG_GETCATITEM, jsonObject2);
				} else if (Constant.NEAR_BY_LIST == 3) {
					/*jsonObject2 = new JSONObject();
					jsonObject1 = new JSONObject();
					jsonObject2.put(TAG_ACCESSTOKN, "");
					jsonObject2.put(TAG_EMAIL, "");
					jsonObject2.put(TAG_LAT, _pref.getString("currLat", ""));
					jsonObject2.put(TAG_LONG, _pref.getString("currLng", ""));
					jsonObject2.put(TAG_CURRENTPAGE, current_page);
					jsonObject2.put(TAG_SEARCHITEM,_pref.getString("searchFor", ""));
					jsonObject1.put(TAG_GETCATITEM, jsonObject2);*/
					jsonObject2 = new JSONObject();
					jsonObject1 = new JSONObject();
					jsonObject3 = new JSONObject();
					jsonObject3.put(TAG_CATID, _pref.getString("catId", ""));
					jsonObject3.put(TAG_SUBCATID,"keyword/"+_pref.getString("searchFor", ""));
					jsonObject2.put(TAG_LAT, _pref.getString("currLat", ""));
					jsonObject2.put(TAG_LONG, _pref.getString("currLng", ""));
					jsonObject2.put(TAG_CURRENTPAGE, current_page);
					jsonObject2.put(TAG_CATDTLS, jsonObject3);
					jsonObject2.put(TAG_EMAIL, "");
					jsonObject2.put(TAG_ACCESSTOKN, "");
					jsonObject2.put(TAG_CUISINEID, cuisine_id);
					jsonObject2.put(TAG_PRICEID, price_id);
					jsonObject1.put(TAG_GETCATITEM, jsonObject2);
					Log.v("Input Data", jsonObject1.toString());
					
				} else {
					//NEARBY
					if(_pref.getString("SubCatId", "").contains("NEARBY")){
						Constant.setNewLocation(getActivity());
					}
					
					jsonObject2 = new JSONObject();
					jsonObject1 = new JSONObject();
					jsonObject3 = new JSONObject();
					jsonObject3.put(TAG_CATID, _pref.getString("catId", ""));
					jsonObject3.put(TAG_SUBCATID,_pref.getString("SubCatId", ""));
					jsonObject2.put(TAG_LAT, _pref.getString("currLat", ""));
					jsonObject2.put(TAG_LONG, _pref.getString("currLng", ""));
					jsonObject2.put(TAG_CURRENTPAGE, current_page);
					jsonObject2.put(TAG_CATDTLS, jsonObject3);
					jsonObject2.put(TAG_EMAIL, "");
					jsonObject2.put(TAG_ACCESSTOKN, "");
					jsonObject2.put(TAG_CUISINEID, cuisine_id);
					jsonObject2.put(TAG_PRICEID, price_id);
					jsonObject1.put(TAG_GETCATITEM, jsonObject2);
					Log.v("Input Data", jsonObject1.toString());
				}
				nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("data", jsonObject1.toString()));
				ListAstClass listAstClass = new ListAstClass();
				listAstClass.execute("");
			} else {
				if (current_page == 1) {
					arrayList.clear();
				}
				if (flagFilterDeals == 0) {

					jsonObject2 = new JSONObject();
					jsonObject1 = new JSONObject();
					jsonObject3 = new JSONObject();

					jsonObject3.put(TAG_CUISINE_ID, cuisine_id);
					jsonObject3.put(TAG_LOCATION_ID, location_id);
					jsonObject3.put(TAG_PRICE_ID, price_id);
					jsonObject2.put(TAG_LAT, _pref.getString("currLat", ""));
					jsonObject2.put(TAG_LONG, _pref.getString("currLng", ""));

					jsonObject2.put(TAG_CURRENTPAGE, current_page);
					jsonObject2.put(TAG_FILTERDTLS, jsonObject3);
					jsonObject2.put(TAG_EMAIL, "");
					jsonObject2.put(TAG_ACCESSTOKN, "");
					jsonObject1.put(TAG_GETCATITEM, jsonObject2);

					nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("data",jsonObject1.toString()));

				} else {
					jsonObject2 = new JSONObject();
					jsonObject1 = new JSONObject();
					jsonObject3 = new JSONObject();
					jsonObject3.put(TAG_CATID, _pref.getString("catId", ""));
					jsonObject3.put(TAG_SUBCATID,_pref.getString("SubCatId", ""));
					jsonObject2.put(TAG_LAT, _pref.getString("currLat", ""));
					jsonObject2.put(TAG_LONG, _pref.getString("currLng", ""));
					jsonObject2.put(TAG_CURRENTPAGE, current_page);
					jsonObject2.put(TAG_CATDTLS, jsonObject3);
					jsonObject2.put(TAG_EMAIL, "");
					jsonObject2.put(TAG_ACCESSTOKN, "");
					jsonObject1.put(TAG_GETCATITEM, jsonObject2);
					Log.v("Input Data", jsonObject1.toString());
				}

				ListAstClass listAstClass = new ListAstClass();
				listAstClass.execute("");
			}

		} catch (JSONException e) {
			Log.e("error", e.toString());
		}
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
//		loadList();
		if (_connectionDetector.isConnectingToInternet()) {
			loadList();
		} else {
			Toast.makeText(getActivity(), "No internet connection available",Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * @Override public void onStart() { // TODO Auto-generated method stub
	 * super.onStart(); //arrayList.clear(); loadList(); }
	 */

	public class ListAstClass extends AsyncTask<String, String, Long> {

		private AlertDialog dialog;

		public ListAstClass() {
			if (current_page == 1) {
				dialog = CustomAlertProgressDialog.getCustomDialog(getActivity(),"Please wait...");
				dialog.show();
			}
		}

		@SuppressWarnings("static-access")
		@Override
		protected Long doInBackground(String... params) {
			try {

				JsonobjectPost j_parser = new JsonobjectPost();
				Log.v("input data", jsonObject1.toString());
				if (Constant.FILTER_BY_LIST == 0) {
					if (Constant.NEAR_BY_LIST == 1) {
						jObjList = j_parser.getJSONObj(Constant.BASE_URL+ "nearBy", nameValuePairs,jsonObject1.toString());
					} else if (Constant.NEAR_BY_LIST == 2) {
						Log.v("input", jsonObject1.toString());
						jObjList = j_parser.getJSONObj(Constant.BASE_URL+ "getReservableRestList", nameValuePairs,jsonObject1.toString());
					} else if (Constant.NEAR_BY_LIST == 3) {
						Log.v("input", jsonObject1.toString());
						jObjList = j_parser.getJSONObj(Constant.BASE_URL+ "search", nameValuePairs,jsonObject1.toString());//getSearchList
					} else {
						jObjList = j_parser.getJSONObj(Constant.BASE_URL+ "search", nameValuePairs,jsonObject1.toString());
					}
				} else {
					if (flagFilterDeals == 0) {
						jObjList = j_parser.getJSONObj(Constant.BASE_URL+ "getFiltedList", nameValuePairs,jsonObject1.toString());
					} else {
						jObjList = j_parser.getJSONObj(Constant.BASE_URL+ "search", nameValuePairs,jsonObject1.toString());
					}
				}

			} catch (Exception e) {
				Log.v("Exception", e.toString());

			}

			return null;
		}

		@SuppressWarnings("static-access")
		protected void onPostExecute(Long result) {
			flagPagi = 0;
			JSONObject objData;
			JSONArray listArray;
			
		
			try {
				objData = jObjList.getJSONObject(TAG_DATA);
				JSONObject errorObj = jObjList.getJSONObject(TAG_ERNODE);

				errorCode = errorObj.getString(TAG_ERCODE);
				errorMsg = errorObj.getString(TAG_ERMSG);
				total_page = Integer.parseInt(jObjList.getString(TAG_TOTALPAGE));

				if (errorCode.equalsIgnoreCase("0")) {
					listArray = objData.getJSONArray(TAG_GETCATITEM);

					if (listArray.length() > 0) {
						for (int i = 0; i < listArray.length(); i++) {
							JSONObject jsonObject = listArray.getJSONObject(i);
							ListItem _item = new ListItem();
							_item.setItemId(jsonObject.getInt(TAG_ID));

							_item.setItemname(jsonObject.getString(TAG_NAME));
							// Log.e("id", jsonObject.getString(TAG_NAME));
							_item.setItemlocation(jsonObject.getString(TAG_ADDRESS));
							String restaurantImg = jsonObject.getString(TAG_IMG).replaceAll(" ", "%20");
							_item.setImage(restaurantImg);

							_item.setItemdistence(jsonObject.getString(TAG_DISTANCE));

							_item.setItemprice(jsonObject.getDouble(TAG_PRICE));
							_item.setItemtype(jsonObject.getString(TAG_SPECIALITY));
							_item.setReviewed(jsonObject.getBoolean(TAG_REVIEWED));
							if (!jsonObject.getString(TAG_USERRATING).equalsIgnoreCase("")) {
								float rating = Float.parseFloat(jsonObject.getString(TAG_USERRATING));
								int ratingInt = (int) rating;
								_item.setItemUserRating(ratingInt);
							} else {
								_item.setItemUserRating(0);
							}

							_item.setItemEazyDeals(jsonObject.getString(TAG_EAZYDEALS));
							_item.setNewItemStatus(jsonObject.getString(TAG_NEWTAG));

							_item.setItemCritcsRating(jsonObject.getInt(TAG_CRITICRATING));
							_item.setLat(jsonObject.getString(TAG_LAT));
							_item.setLng(jsonObject.getString(TAG_LONG));

							arrayList.add(_item);
						}
						
						 if (lvList.getAdapter() instanceof HeaderViewListAdapter) {
							 ((BaseAdapter)((HeaderViewListAdapter)lvList.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
						}else{
							((BaseAdapter) lvList.getAdapter()).notifyDataSetChanged(); 
						}
						
						/*if(lvList.getFooterViewsCount()!=0){
							((BaseAdapter)((HeaderViewListAdapter)lvList.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
						}else{
							lvList.removeFooterView(listFooter);
							((BaseAdapter) lvList.getAdapter()).notifyDataSetChanged(); 
						}*/
						
						/*adpterObj = new ItemlistAdapter(getActivity(), arrayList);
						lvList.setAdapter(adpterObj);
						lvList.invalidate();*/
						
//						((BaseAdapter)((HeaderViewListAdapter)lvList.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
//						adpterObj.notifyDataSetChanged();
						testLayoutFilter.setVisibility(View.VISIBLE);

					} else {
						
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setMessage("No Result Found. Press back to search again").setCancelable(false)
						       .setPositiveButton("Back", new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
//						                getActivity().finish();
						                dialog.dismiss();
						                clearFilters();
						                loadList();
						           }
						       });
						AlertDialog alert = builder.create();
						alert.show();

//						Toast.makeText(getActivity(), "No Item Available",Toast.LENGTH_LONG).show();

						testLayoutFilter.setVisibility(View.GONE);

					}

					((AlllistActivity) getActivity()).arrayList = arrayList;
					if (current_page == 1 && listArray.length() > 24) {
						lvList.addFooterView(listFooter);
					}

					if (total_page == current_page && flagPagi != 1) {
						if (listFooter.isActivated()) {
							lvList.removeFooterView(listFooter);
						}
					}
				}
				
				if (current_page == 1) {
					dialog.dismiss();
				}
			} catch (Exception e) {
				Toast.makeText(getActivity(), "Server error", Toast.LENGTH_LONG).show();

				e.printStackTrace();
			}
		}
	}

	@SuppressLint("NewApi")
	private void loadFilterList() {
		try {

			elvItemFilterCuisine = (ExpandableListView) rootView.findViewById(R.id.elvItemFilterCuisine);

			DisplayMetrics metrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int width = metrics.widthPixels;

			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
				elvItemFilterCuisine.setIndicatorBounds(width- GetPixelFromDips(50), width - GetPixelFromDips(20));
			} else {
				elvItemFilterCuisine.setIndicatorBoundsRelative(width- GetPixelFromDips(50), width - GetPixelFromDips(20));
			}

			filterGroupCuisie = new ArrayList<FilterGroupItem>();

			filterCuisieAdapter = new FilterItemAdapter(getActivity(),filterGroupCuisie);

			inflaterFilterFooter = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.filterlist_footer_layout, null, false);

			tvFilterHeader = (TextView) inflaterFilterFooter.findViewById(R.id.tvFilterHeader);

			RelativeLayout rlFilterFooter = (RelativeLayout) inflaterFilterFooter.findViewById(R.id.rlFilterFooter);

			setTextFont();
			rlFilterFooter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CatId = _pref.getString("catId", "");
					SubCatId = _pref.getString("SubCatId", "");

					_pEditor.putString("catId", "menu/DEALS/0");
					_pEditor.putString("SubCatId", "menu/DEALS/0");
					_pEditor.commit();

					total_page = 0;
					current_page = 1;
					flagPagi = 0;
					flagFilterDeals = 1;
					arrayList.clear();
					Constant.NEAR_BY_LIST = 0;
					Constant.FILTER_BY_LIST = 1;

					elvItemFilterCuisine.setVisibility(View.GONE);
					AlllistFragment.ivFilter.setVisibility(View.VISIBLE);
					AlllistFragment.textClear.setVisibility(View.VISIBLE);
					
					loadList();
				}
			});

//			elvItemFilterCuisine.addFooterView(inflaterFilterFooter);

			// elvItemFilterCuisine.setGroupIndicator(null);
			elvItemFilterCuisine.setAdapter(filterCuisieAdapter);

			jsonListString = getStringFromFile(_pref.getString(Constant.JSON_SERVICE_FILTER_DATA_LIST, ""));
			JSONObject jObj = new JSONObject(jsonListString);
			JSONArray jsonFilterArray = jObj.getJSONArray(TAG_DATA);

			filterGroupCuisie.clear();
			selectedPosition[0] = -1;
			selectedPosition[1] = -1;
//			selectedPosition[2] = -1;
			filterParameter.put("cuisine_id", "");
//			filterParameter.put("location_id", "");
			filterParameter.put("price_id", "");
			Log.e("test", "create filter");
			Log.e("lenght", String.valueOf(jsonFilterArray.length()));

			for (int i = 0; i < jsonFilterArray.length(); i++) {
				// TAG_FILTERING_VALUE TAG_HEADING
				FilterGroupItem groupItem = new FilterGroupItem();

				JSONObject filterItem = jsonFilterArray.getJSONObject(i);
				groupItem.setGroupItemName(filterItem.getString(TAG_HEADING));
				groupItem.setOpenedList(0);
				JSONArray jsonChildArray = filterItem.getJSONArray(TAG_FILTERING_VALUE);
				ArrayList<FilterChildItem> filterChildItems = new ArrayList<FilterChildItem>();
				for (int j = 0; j < jsonChildArray.length(); j++) {
					JSONObject childItem = jsonChildArray.getJSONObject(j);
					FilterChildItem filterChildItem = new FilterChildItem();
					filterChildItem.setChildId(childItem.getString(TAG_ID));
					filterChildItem.setChildItemName(childItem.getString(TAG_NAME));
					filterChildItems.add(filterChildItem);
				}
				groupItem.setChildItems(filterChildItems);

				filterGroupCuisie.add(groupItem);

			}
			filterCuisieAdapter.notifyDataSetChanged();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void clearFilters() {
		
		flagCuisineSelection = 0;

		_pEditor.putString("catId", CatId);
		_pEditor.putString("SubCatId", SubCatId);
		_pEditor.commit();

		ivFilter.setVisibility(View.GONE);
		textClear.setVisibility(View.GONE);
		filterGroupCuisie.clear();
		arrayList.clear();
//		elvItemFilterCuisine.removeFooterView(inflaterFilterFooter);
		loadFilterList();
		elvItemFilterCuisine.setVisibility(View.GONE);
		total_page = 0;
		current_page = 1;
		flagPagi = 0;
		flagFilterShow = 0;
		flagFilterDeals = 0;
		price_id = "";
		cuisine_id = "";
	}

	public int GetPixelFromDips(float pixels) {
		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (pixels * scale + 0.5f);
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

	@SuppressWarnings("static-access")
	public void listFiltering(String cui_id, String pric_id) throws JSONException {
		cuisine_id = cui_id;
//		location_id = loc_id;
		price_id = pric_id;

		flagFilterDeals = 0;
		total_page = 0;
		current_page = 1;
		flagPagi = 0;

		Log.e("cuisine_id", cuisine_id);
//		Log.e("location_id", location_id);
		Log.e("price_id", price_id);

		loadList();
	}

	private class FilterItemAdapter extends BaseExpandableListAdapter {

		private Context context;
		private ArrayList<FilterGroupItem> filterGroupItems;
		private LayoutInflater inflater;

		public FilterItemAdapter(Context context,ArrayList<FilterGroupItem> filterGroupItems) {
			this.context = context;
			this.filterGroupItems = filterGroupItems;
			inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			filterParameter = new HashMap<String, String>();
			filterParameter.put("cuisine_id", "");
//			filterParameter.put("location_id", "");
			filterParameter.put("price_id", "");
		}

		@Override
		public int getGroupCount() {
			return filterGroupItems.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return filterGroupItems.get(groupPosition).getChildItems().size();
		}

		@Override
		public FilterGroupItem getGroup(int groupPosition) {
			return filterGroupItems.get(groupPosition);
		}

		@Override
		public FilterChildItem getChild(int groupPosition, int childPosition) {
			return filterGroupItems.get(groupPosition).getChildItems().get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
			View rowView = convertView;
			if (rowView == null) {
				rowView = inflater.inflate(R.layout.filter_group_layout, null);
				GroupHolder groupHolder = new GroupHolder();
				groupHolder.tvFilterHeader = (TextView) rowView.findViewById(R.id.tvFilterHeader);
				// groupHolder.elvSymbolFilter = (ImageView) rowView.findViewById(R.id.elvSymbolFilter);
				// groupHolder.elvSymbolFilter.setTag("close");
				setTextFontForGroup(groupHolder);
				rowView.setTag(groupHolder);
			}

			GroupHolder newGroupHolder = (GroupHolder) rowView.getTag();
			newGroupHolder.tvFilterHeader.setText(getGroup(groupPosition).getGroupItemName());

			return rowView;
		}

		@Override
		public View getChildView(final int groupPosition,final int childPosition, boolean isLastChild, View convertView,ViewGroup parent) {
			View childRowView = convertView;

			childRowView = inflater.inflate(R.layout.filter_child_layout, null);
			ChildHolder childHolder = new ChildHolder();
			childHolder.tvFilterChildItem = (TextView) childRowView.findViewById(R.id.tvFilterChildItem);
			childHolder.rbSelect = (RadioButton) childRowView.findViewById(R.id.rbSelectFilter);
			childHolder.rbSelect.setChecked(childPosition == selectedPosition[groupPosition]);
			childHolder.rbSelect.setTag(childPosition);

			setTextFontForChild(childHolder);
			childRowView.setTag(childHolder);

			final ChildHolder newChildHolder = (ChildHolder) childRowView.getTag();
			newChildHolder.tvFilterChildItem.setText(getChild(groupPosition,childPosition).getChildItemName());
			childHolder.rbSelect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					arrayList.clear();
					selectedPosition[groupPosition] = (Integer) newChildHolder.rbSelect.getTag();
					AlllistFragment.filterItem.put(0, getGroup(groupPosition).getGroupItemName());

					elvItemFilterCuisine.setVisibility(View.GONE);

					notifyDataSetInvalidated();

					if (groupPosition == 0) {
						filterParameter.put("cuisine_id",getGroup(groupPosition).getChildItems().get(childPosition).getChildId());
					} else if (groupPosition == 2) {
//						filterParameter.put("location_id",getGroup(groupPosition).getChildItems().get(childPosition).getChildId());
					} else if (groupPosition == 1) {
						filterParameter.put("price_id", getGroup(groupPosition).getChildItems().get(childPosition).getChildId());
					}

					try {
						listFiltering(filterParameter.get("cuisine_id"),filterParameter.get("price_id"));
					} catch (JSONException e) {
						e.printStackTrace();
					}

					elvItemFilterCuisine.setVisibility(View.GONE);
					flagFilterShow = 0;
					AlllistFragment.ivFilter.setVisibility(View.VISIBLE);
					AlllistFragment.textClear.setVisibility(View.VISIBLE);
				}
			});
			childRowView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					arrayList.clear();
					selectedPosition[groupPosition] = (Integer) newChildHolder.rbSelect.getTag();
					AlllistFragment.filterItem.put(0, getGroup(groupPosition).getGroupItemName());

					elvItemFilterCuisine.setVisibility(View.GONE);

					notifyDataSetInvalidated();
					if (groupPosition == 0) {
						filterParameter.put("cuisine_id",getGroup(groupPosition).getChildItems().get(childPosition).getChildId());
					} else if (groupPosition == 2) {
//						filterParameter.put("location_id",getGroup(groupPosition).getChildItems().get(childPosition).getChildId());
					} else if (groupPosition == 1) {
						filterParameter.put("price_id", getGroup(groupPosition).getChildItems().get(childPosition).getChildId());
					}

					try {
						listFiltering(filterParameter.get("cuisine_id"),filterParameter.get("price_id"));
					} catch (JSONException e) {
						e.printStackTrace();
					}

					elvItemFilterCuisine.setVisibility(View.GONE);
					flagFilterShow = 0;
					AlllistFragment.ivFilter.setVisibility(View.VISIBLE);
					AlllistFragment.textClear.setVisibility(View.VISIBLE);
				}
			});
			return childRowView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

		private class GroupHolder {
			private TextView tvFilterHeader;
			private ImageView elvSymbolFilter;
		}

		private class ChildHolder {
			private TextView tvFilterChildItem;
			private RadioButton rbSelect;
		}

		private void setTextFontForGroup(GroupHolder holder) {
			String fontPath1 = "fonts/Aller_Bd.ttf";
			String fontPath2 = "fonts/Aller_Rg.ttf";
			Typeface tf1 = Typeface.createFromAsset(context.getAssets(),fontPath2);

			holder.tvFilterHeader.setTypeface(tf1);
		}

		private void setTextFontForChild(ChildHolder holder) {
			String fontPath1 = "fonts/Aller_Bd.ttf";
			String fontPath2 = "fonts/Aller_Rg.ttf";
			Typeface tf1 = Typeface.createFromAsset(context.getAssets(),fontPath2);

			holder.tvFilterChildItem.setTypeface(tf1);
		}
	}

	private void setTextFont() {
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(),fontPath2);

		tvFilterHeader.setTypeface(tf1);
		textFilter.setTypeface(tf1);
		textClear.setTypeface(tf1);
	}

	@Override
	public void tryAgain(AsyncTask<String, String, Long> conAsync) {
		conAsync = new ListAstClass();
		conAsync.execute("");
		
	}

}
