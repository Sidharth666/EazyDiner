package com.easydiner.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.classes.CommonFunction.TimeoutDialogTryAgainListener;
import com.classes.ConnectionDetector;
import com.classes.Constant;
import com.classes.CustomAlertProgressDialog;
import com.classes.EasydinerClass;
import com.classes.JsonobjectPost;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.dao.EazyTrendsListAdapter;
import com.easydiner.dao.ExpItemListAdapter;
import com.easydiner.dataobject.EazyTrendsListItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class EazyTrendsActivity extends EasyDinerBaseActivity implements TimeoutDialogTryAgainListener{

	private ListView lvList;
	private EazyTrendsListAdapter adpterObj;
	private ArrayList<EazyTrendsListItem> arrayList;
	private RelativeLayout eazytrands_list_header, rlHomeBtnBooknowEazyTrend,
			rlEazyTrendCall;
//	private ImageView ivSearchItemEazyTrends; 
	private ImageView ivMenuEazyTrends;
	private ExpItemListAdapter expAdapter;
	private int flag = 0;
	private LinearLayout llSearchByKeyword;
	private int flagMenu = 0;
	private static final String TAG_DATA = "data";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_ICON_IMG = "icon_img";
	private static final String TAG_TITLE = "title";
	private static final String TAG_BODY = "body";
	private static final String TAG_ID = "id";
	private static final String TAG_DETAILS = "details";
	private static final String TAG_IMG = "img";
	private static final String TAG_COUNT = "like_count";
	private static final String TAG_TOTALPAGE = "totalPage";
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private String errorCode, errorMsg;
	private List<NameValuePair> nameValuePairs;
	private Constant _constant;
	private PopupWindow popupWindow;
	private int bookPopupFlag = 0;
	private SharedPreferences.Editor _pEditor;
	private SharedPreferences _pref;
	private View footerView;
	private ConnectionDetector _connectionDetector;
	private static final String TAG_CURRENTPAGE = "currentPage";
	int load_count = 0;
	private int total_page = 0, current_page = 1, flagPagi = 0;
	private View listFooter;
	private Context context;
	
	@SuppressLint("ServiceCast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.easy_trends_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("EazyTrendsActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		context = this;
		arrayList = new ArrayList<EazyTrendsListItem>();
		lvList = (ListView) findViewById(R.id.lvItemEazyTrend);
		llSearchByKeyword = (LinearLayout) findViewById(R.id.llSearchDetailsEazyTrend);
//		ivSearchItemEazyTrends = (ImageView) findViewById(R.id.ivSearchItemEazyTrends);
		ivMenuEazyTrends = (ImageView) findViewById(R.id.ivMenuEazyTrends);
		_pref = new Pref(EazyTrendsActivity.this).getSharedPreferencesInstance();
		_pEditor = new Pref(EazyTrendsActivity.this).getSharedPreferencesEditorInstance();
		adpterObj = new EazyTrendsListAdapter(EazyTrendsActivity.this,arrayList);
		_connectionDetector = new ConnectionDetector(EazyTrendsActivity.this);
		lvList.setAdapter(adpterObj);
		eazytrands_list_header = (RelativeLayout) findViewById(R.id.eazytrands_list_header);
		rlEazyTrendCall = (RelativeLayout) findViewById(R.id.rlEazyTrendCall);
		rlHomeBtnBooknowEazyTrend = (RelativeLayout) findViewById(R.id.rlHomeBtnBooknowEazyTrend);
		_constant = new Constant();
		if (_connectionDetector.isConnectingToInternet()) {
			loadList();
		} else {
			Toast.makeText(EazyTrendsActivity.this,"No internet connection available", Toast.LENGTH_LONG).show();
		}
		
		listFooter = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.pagination_footer, null, false);
		

		/*ivSearchItemEazyTrends.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (flag == 0) {
					ivSearchItemEazyTrends.setSelected(true);
					flag = 1;
				} else {
					ivSearchItemEazyTrends.setSelected(false);
					flag = 0;
				}

				if (llSearchByKeyword.getVisibility() == View.GONE) {
					llSearchByKeyword.setVisibility(View.VISIBLE);
					Animation animation = AnimationUtils.loadAnimation(
							EazyTrendsActivity.this, R.anim.animation_open);
					animation.setDuration(500);
					llSearchByKeyword.setAnimation(animation);
					llSearchByKeyword.animate();
					animation.start();
				} else {
					llSearchByKeyword.setVisibility(View.GONE);
					Animation animation = AnimationUtils.loadAnimation(
							EazyTrendsActivity.this, R.anim.animation_close);
					animation.setDuration(500);
					llSearchByKeyword.setAnimation(animation);
					llSearchByKeyword.animate();
					animation.start();
				}
			}
		});*/

		llSearchByKeyword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(EazyTrendsActivity.this,
						SearchListActivity.class));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		eazytrands_list_header.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(EazyTrendsActivity.this,
						HomeActivity.class));
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});

		rlEazyTrendCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String number = "7861004444";
				Intent dial = new Intent();
				dial.setAction("android.intent.action.DIAL");
				dial.setData(Uri.parse("tel:" + number));
				startActivity(dial);
			}
		});

		rlHomeBtnBooknowEazyTrend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				rlHomeBtnBooknowEazyTrend.setBackgroundColor(Color.parseColor("#9B9A9A"));
//				BooknowPopupActivity.rlBookNowBultton = rlHomeBtnBooknowEazyTrend;
				startActivity(new Intent(EazyTrendsActivity.this,BookRestaurentActivity.class));
				overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);
			}
		});

		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EazyTrendsActivity.this,EazyTrendDetailsActivity.class);
				intent.putExtra("trendsId", arrayList.get(position).getListId());
				intent.putExtra("trendsName", arrayList.get(position).getListTitle());
				intent.putExtra("trendsImage", arrayList.get(position).getDetailsImg());
				intent.putExtra("trendsBody", arrayList.get(position).getListBody());
				intent.putExtra("trendsDetails", arrayList.get(position).getListDetails());
				intent.putExtra("trendsLikeCount", arrayList.get(position).getListCount());
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});

		ivMenuEazyTrends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(EazyTrendsActivity.this,MenuListActivity.class));
				overridePendingTransition(R.anim.slide_down_info,R.anim.slide_up_info);
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
				}
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		/*if (load_count > 0) {
			arrayList.clear();
			loadList();
		}

		load_count++;*/

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (bookPopupFlag == 1) {
			popupWindow.dismiss();
			bookPopupFlag = 0;
		} else {
			super.onBackPressed();
			overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
		}
	}

	public void loadList() {

		jsonObject1 = new JSONObject();
		jsonObject2 = new JSONObject();

		try {
			jsonObject2.put("accessTokn", "");
			jsonObject2.put("city", "Delhi");
			jsonObject2.put(TAG_CURRENTPAGE, current_page );
			jsonObject1.put("eazy_Trends", jsonObject2);

			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("data", jsonObject1.toString()));
			ListAstClass listAstClass = new ListAstClass();
			listAstClass.execute("");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class ListAstClass extends AsyncTask<String, String, Long> {
		private AlertDialog dialog;

		public ListAstClass() {
			if(current_page==1){
				dialog = CustomAlertProgressDialog.getCustomDialog(EazyTrendsActivity.this,"Please wait...");
				dialog.show();
			}
		}

		@SuppressWarnings("static-access")
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {

				JsonobjectPost j_parser = new JsonobjectPost();
				jObjList = j_parser.getJSONObj(Constant.BASE_URL + "eazytrends", nameValuePairs,jsonObject1.toString(),context, this);

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}

			return null;
		}

		protected void onProgerssUpdate(String... values) {

		}

		@SuppressWarnings("static-access")
		protected void onPostExecute(Long result) {
			Log.v("onPostExecute", "onPostExecute");
			flagPagi = 0;
			JSONArray objData;
			try {
				objData = jObjList.getJSONArray(TAG_DATA);
				JSONObject errorObj = jObjList.getJSONObject(TAG_ERNODE);

				errorCode = errorObj.getString(TAG_ERCODE);
				errorMsg = errorObj.getString(TAG_ERMSG);
				total_page = Integer.parseInt(jObjList.getString(TAG_TOTALPAGE));

				if (errorCode.equalsIgnoreCase("0")) {

					if (objData.length() > 0) {
						for (int i = 0; i < objData.length(); i++) {
							JSONObject jsonObject = objData.getJSONObject(i);
							EazyTrendsListItem _item = new EazyTrendsListItem();
							_item.setListImg(jsonObject.getString(TAG_ICON_IMG));
							_item.setListTitle(jsonObject.getString(TAG_TITLE));
							_item.setListId(jsonObject.getInt(TAG_ID));
							_item.setListDetails(jsonObject.getString(TAG_DETAILS));
							_item.setListBody(jsonObject.getString(TAG_BODY));
							_item.setDetailsImg(jsonObject.getString(TAG_IMG));
							_item.setListCount(jsonObject.getInt(TAG_COUNT));

							Log.v("count", String.valueOf(jsonObject.getInt(TAG_COUNT)));

							arrayList.add(_item);
						}

						adpterObj.notifyDataSetChanged();
					} else {
						Toast.makeText(EazyTrendsActivity.this,"No Item Available", Toast.LENGTH_LONG).show();
					}
					
					if (current_page == 1 && objData.length() > 4) {
						lvList.addFooterView(listFooter);
					}

					if (total_page == current_page && flagPagi != 1) {
						if (listFooter.isActivated()) {
							lvList.removeFooterView(listFooter);
						}
					}
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(EazyTrendsActivity.this, "Error in server side",
						Toast.LENGTH_LONG).show();
			}

			if (current_page == 1) {
				dialog.dismiss();
			}
		}

	}

	@Override
	public void tryAgain(AsyncTask<String, String, Long> conAsync) {
		conAsync = new ListAstClass();
		conAsync.execute("");
		
	}
}
