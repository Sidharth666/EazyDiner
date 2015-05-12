package com.easydiner.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classes.Constant;
import com.classes.EasydinerClass;
import com.classes.JsonobjectPost;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.dao.ExpItemListAdapter;
import com.easydiner.dao.KeywordAdapter;
import com.easydiner.dataobject.KeywordItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class SearchListActivity extends EasyDinerBaseActivity {

	private String serchList[] = { "Hotel Dining", "Casual Dining", "Drinks",
			"North Indian", "Chinese", "Italian" };
	private String serchListValue[] = { "category/hotel dining/0",
			"category/casual dining/0", "menu/drink/0",
			"cuisine/north indian/0", "cuisine/chinese/0", "cuisine/italian/0" };
	private ListView lvSearchItem;
	private KeywordAdapter adpterObj;
	private int len, flag = 0, flagMenu = 0;
	private ArrayList<KeywordItem> arrayList;
	private SharedPreferences.Editor _pEditor;
	private Constant _constant;
	private ImageView /* ing_nearby_icon_srch, */ivMenuSearch;
	private RelativeLayout rlHomeBtnEazyConSearch, rlHomeBtnBooknowSearch,
			rlSearchRestaurantBtn;
	private PopupWindow popupWindowExtlist;
	private ExpItemListAdapter expAdapter;
	/* private LinearLayout llSearchSrchlayout; */
	private EditText etSearchItemSrch;
	private TextView textSearchListHeading;
	private static final String TAG_DATA = "data";
	private static final String TAG_GETITEM = "getItem";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_RESNAME = "resName";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_ITEMDATA = "itemdata";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_LOCATION = "location";
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private List<NameValuePair> nameValuePairs;
	private String errorCode, errorMsg;
	private View footerView;
	private SharedPreferences _pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("SearchListActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		initialize();
		lvSearchItem.setAdapter(adpterObj);
		createList();
		onClick();
//		((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
		((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	    if (hasFocus) {
	    	etSearchItemSrch.clearFocus();
	    	etSearchItemSrch.requestFocus();
	        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	        etSearchItemSrch.setSelection(etSearchItemSrch.getText().toString().length());
	    }
	}

	private void initialize() {
		lvSearchItem = (ListView) findViewById(R.id.lvSearchItem);
		arrayList = new ArrayList<KeywordItem>();
		adpterObj = new KeywordAdapter(SearchListActivity.this, arrayList);
		_pEditor = new Pref(SearchListActivity.this).getSharedPreferencesEditorInstance();
		_constant = new Constant();
		ivMenuSearch = (ImageView) findViewById(R.id.ivMenuSearch);
		rlHomeBtnEazyConSearch = (RelativeLayout) findViewById(R.id.rlHomeBtnEazyConSearch);
		rlHomeBtnBooknowSearch = (RelativeLayout) findViewById(R.id.rlHomeBtnBooknowSearch);
		etSearchItemSrch = (EditText) findViewById(R.id.etSearchItemSrch);
		textSearchListHeading = (TextView) findViewById(R.id.textSearchListHeading);
		_pref = new Pref(SearchListActivity.this).getSharedPreferencesInstance();
		rlSearchRestaurantBtn = (RelativeLayout) findViewById(R.id.rlSearchRestaurantBtn);
		setTextFont();
	}

	private void onClick() {

		ivMenuSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(SearchListActivity.this,MenuListActivity.class));
				overridePendingTransition(R.anim.slide_down_info,R.anim.slide_up_info);
			}
		});

		rlHomeBtnEazyConSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String number = "7861004444";
				Intent dial = new Intent();
				dial.setAction("android.intent.action.DIAL");
				dial.setData(Uri.parse("tel:" + number));
				startActivity(dial);
			}
		});

		rlHomeBtnBooknowSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				rlHomeBtnBooknowSearch.setBackgroundColor(Color.parseColor("#9B9A9A"));
//				BooknowPopupActivity.rlBookNowBultton = rlHomeBtnBooknowSearch;
				startActivity(new Intent(SearchListActivity.this,BookRestaurentActivity.class));
				overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);
			}
		});

		lvSearchItem.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				try {
					
					_pEditor.putString("searchFor", "");
					int d = Integer.parseInt(arrayList.get(position).getKeyId());

					Log.e("id", arrayList.get(position).getKeyId());

					Intent intent = new Intent(SearchListActivity.this,DetailsActivity.class);
					_pEditor.putString("itemId",String.valueOf(arrayList.get(position).getKeyId()));
					_pEditor.commit();
					_constant.NEAR_BY_LIST = 0;
					Constant.FILTER_BY_LIST = 0;
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

				} catch (NumberFormatException nfe) {
					Log.e("id", arrayList.get(position).getKeyId());

					Intent intent = new Intent(SearchListActivity.this,AlllistActivity.class);
					_pEditor.putString("catId", arrayList.get(position).getKeyId());
					_pEditor.putString("SubCatId", arrayList.get(position).getKeyId());
					_pEditor.commit();
					_constant.NEAR_BY_LIST = 0;
					Constant.FILTER_BY_LIST = 0;
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
				}
			}
		});

		etSearchItemSrch.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				etSearchItemSrch.setHint("");
				return false;
			}
		});

		etSearchItemSrch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!etSearchItemSrch.getText().toString().equalsIgnoreCase("")) {
					jsonObject2 = new JSONObject();
					jsonObject1 = new JSONObject();
					try {
						jsonObject2.put(TAG_ACCESSTOKN, "");
						jsonObject2.put(TAG_RESNAME, etSearchItemSrch.getText().toString());
						jsonObject1.put(TAG_GETITEM, jsonObject2);

						nameValuePairs = new ArrayList<NameValuePair>(2);
						nameValuePairs.add(new BasicNameValuePair("data",jsonObject1.toString()));

						ListAstClass astClass = new ListAstClass();
						astClass.execute("");

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					arrayList.clear();
					createList();
				}
			}
		});

		rlSearchRestaurantBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SearchListActivity.this,AlllistActivity.class);
				_pEditor.putString("searchFor", etSearchItemSrch.getText().toString());
				_pEditor.commit();
				_constant.NEAR_BY_LIST = 3;
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});
	}

	private void createList() {
		for (int i = 0; i < serchList.length; i++) {
			KeywordItem _item = new KeywordItem();
			_item.setKeyItem(serchList[i]);
			_item.setKeyLocation("");
			_item.setKeyId(serchListValue[i]);

			arrayList.add(_item);
		}

		adpterObj.notifyDataSetChanged();
	}

	private class ListAstClass extends AsyncTask<String, String, Long> {
		private ProgressDialog dialog;

		@SuppressWarnings("static-access")
		@Override
		protected Long doInBackground(String... params) {
			try {

				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL+ "KeyForSearch", nameValuePairs,jsonObject1.toString());

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}

			return null;
		}

		protected void onProgerssUpdate(String... values) {

		}

		@SuppressWarnings("static-access")
		protected void onPostExecute(Long result) {
			arrayList.clear();
			JSONObject objData;
			JSONArray listArray;
			try {
				objData = jObjList.getJSONObject(TAG_DATA);
				JSONObject errorObj = jObjList.getJSONObject(TAG_ERNODE);

				errorCode = errorObj.getString(TAG_ERCODE);
				errorMsg = errorObj.getString(TAG_ERMSG);

				if (errorCode.equalsIgnoreCase("0")) {
					listArray = objData.getJSONArray(TAG_ITEMDATA);

					if (listArray.length() > 0) {
						for (int i = 0; i < listArray.length(); i++) {
							JSONObject jsonObject = listArray.getJSONObject(i);
							KeywordItem _item = new KeywordItem();

							_item.setKeyItem(jsonObject.getString(TAG_NAME));
							_item.setKeyLocation(jsonObject.getString(TAG_LOCATION));
							_item.setKeyId(jsonObject.getString(TAG_ID));

							arrayList.add(_item);
						}

						adpterObj.notifyDataSetChanged();
					} else {
						if(!errorMsg.equalsIgnoreCase("success")){
							Toast.makeText(SearchListActivity.this, errorMsg,Toast.LENGTH_LONG).show();
						}
					}

				}
			} catch (Exception e) {
				Toast.makeText(SearchListActivity.this, "Error In Server",Toast.LENGTH_LONG).show();
			}

			// dialog.dismiss();
		}

	}

	private void setTextFont() {

		String fontPath1 = "fonts/avenir-light.ttf";
		String fontPath2 = "fonts/Aller_Lt.ttf";

		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);

		textSearchListHeading.setTypeface(tf2);
		etSearchItemSrch.setTypeface(tf2);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
}
