package com.easydiner.activities;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.classes.ConnectionDetector;
import com.classes.Constant;
import com.classes.CustomAlertProgressDialog;
import com.classes.EasydinerClass;
import com.classes.JsonobjectPost;
import com.easydiner.R;
import com.easydiner.dao.RestaurantAdapter;
import com.easydiner.dataobject.RestorentItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class BookRestaurentActivity extends EasyDinerBaseActivity{
	
	private EditText etResturentName;
	private Button btnNxt;
	private ArrayList<RestorentItem> arrayList;
	private RestaurantAdapter adpterObj;
	private Context context;
	private ListView lvSearchRes;
	private ImageView arrow_down;
	ConnectionDetector _connectionDetector;
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private static final String TAG_GETITEM = "getItem";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_RESNAME = "resName";
	private static final String TAG_LOCATION = "location";
	private static final String TAG_DATA = "data";
	private static final String TAG_ITEMDATA = "itemdata";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private List<NameValuePair> nameValuePairs, nameValuePairsBooking;
	protected String restaurantName;
	protected String restaurantLoc;
	protected String restaurantDeals;
	protected int restaurenId;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookrestaurant);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("BookRestaurentActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		context = this;
		initialize();
		lvSearchRes.setAdapter(adpterObj);
		
		arrow_down.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		etResturentName.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				etResturentName.setHint("");
				if(lvSearchRes.getVisibility()==View.GONE){
					lvSearchRes.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});
		
		btnNxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(restaurenId!=0){
					Intent intent = new Intent(context,BooknowPopupActivity.class);
					Bundle bundle = new Bundle();
				    bundle.putString("restaurantName", restaurantName);
				    bundle.putInt("restaurantId", restaurenId);
					intent.putExtras(bundle);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);
				}else{
					Toast.makeText(context, "Please select a restaurant", Toast.LENGTH_SHORT).show();
				}
				
				
			}
		});
		
		lvSearchRes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				restaurantName = arrayList.get(position).getName();
				restaurantLoc = arrayList.get(position).getLocation();
				etResturentName.setText(restaurantName+ " "+ restaurantLoc);
				etResturentName.setSelection(restaurantName.length()+restaurantLoc.length());
				restaurenId = arrayList.get(position).getId();
				lvSearchRes.setVisibility(View.GONE);
				
			}
		});
		
		etResturentName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {

				if (_connectionDetector.isConnectingToInternet()) {
					String restName = etResturentName.getText().toString();
					if (!restName.equalsIgnoreCase("") && restName.length()>1) {
						jsonObject1 = new JSONObject();
						jsonObject2 = new JSONObject();

						try {
								jsonObject2.put(TAG_ACCESSTOKN, "");
								jsonObject2.put(TAG_RESNAME, etResturentName.getText().toString());
								jsonObject1.put(TAG_GETITEM, jsonObject2);
								nameValuePairs = new ArrayList<NameValuePair>(2);
								nameValuePairs.add(new BasicNameValuePair("data", jsonObject1.toString()));

								AstClassRestName astClassRestName = new AstClassRestName();
								astClassRestName.execute("");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else {
						
					}
				} else {
					Toast.makeText(context,"No internet connection available",Toast.LENGTH_LONG).show();
				}
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
	}
	

	private void initialize() {
		etResturentName = (EditText) findViewById(R.id.etResturentName);
		btnNxt = (Button) findViewById(R.id.btnNxt);
		lvSearchRes = (ListView) findViewById(R.id.lvSearchRes);
		arrow_down = (ImageView) findViewById(R.id.arrow_down);
		arrayList = new ArrayList<RestorentItem>();
		adpterObj = new RestaurantAdapter(context, arrayList);
		_connectionDetector = new ConnectionDetector(context);
		
	}
	
	private class AstClassRestName extends AsyncTask<String, String, Long> {
		
		private AlertDialog dialog;
		
		/*public AstClassRestName() {
			dialog = CustomAlertProgressDialog.getCustomDialog(BookRestaurentActivity.this,"Please wait...");
			dialog.show();
		}*/
		
		@Override
		protected Long doInBackground(String... params) {
			try {

				JsonobjectPost j_parser = new JsonobjectPost();
				jObjList = j_parser.getJSONObj(Constant.BASE_URL+ "autofillrestaurant", nameValuePairs,jsonObject1.toString());

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
//			dialog.dismiss();
			if (jObjList != null) {
				try {
					JSONObject objData = jObjList.getJSONObject(TAG_DATA);
					JSONArray listArray = objData.getJSONArray(TAG_ITEMDATA);
					JSONObject objErr = jObjList.getJSONObject(TAG_ERNODE);
					arrayList.clear();
					if (objErr.get(TAG_ERCODE).toString().equalsIgnoreCase("0")) {
						
						for (int i = 0; i < listArray.length(); i++) {
							JSONObject jsonObject = listArray.getJSONObject(i);
							RestorentItem _item = new RestorentItem();
							if(listArray.length()==1 && jsonObject.getString(TAG_NAME).equals("")){
								break;
							}
							_item.setId(jsonObject.getInt(TAG_ID));
							_item.setName(jsonObject.getString(TAG_NAME));
							_item.setLocation(jsonObject.getString(TAG_LOCATION));
							arrayList.add(_item);
						}
						adpterObj.notifyDataSetChanged();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
