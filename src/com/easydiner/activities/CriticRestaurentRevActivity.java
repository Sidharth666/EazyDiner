package com.easydiner.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classes.CommonFunction.TimeoutDialogTryAgainListener;
import com.classes.Constant;
import com.classes.CustomAlertProgressDialog;
import com.classes.EasydinerClass;
import com.classes.JsonobjectPost;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.dao.ExtReviewItemAdapter;
import com.easydiner.dataobject.CriticReviewItem;
import com.easydiner.dataobject.ExtCriticReviewListItem;
import com.easydiner.dataobject.ListItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class CriticRestaurentRevActivity extends EasyDinerBaseActivity implements TimeoutDialogTryAgainListener{

	private ExpandableListView expandableListReviewView;
	private ArrayList<ExtCriticReviewListItem> expListReviewItems;
	private ExtReviewItemAdapter expReviewAdapter;

	private static ImageView ivSearchItemAlllist, ivMenuAlllist;
	private TextView textSearchCriticreviewList;
	private PopupWindow popupWindow;
	private LinearLayout llSearchAlllist, llSearchCriticReviewList;
	public static ArrayList<ListItem> arrayList;
	private RelativeLayout allist_book_now, rlPopupCancel,
			rlCriticRListConCall;
	private int len, flag = 0;
	private int bookPopupFlag = 0, listPosition = -1;
	private Constant _constant;
	private SharedPreferences.Editor _pEditor;
	private static final String TAG_DATA = "data";
	private static final String TAG_ITEMDATA = "itemdata";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_SPECIALITY = "speciality";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_REVIEW = "review";
	private static final String TAG_GETITEM = "getItem";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_CRITICS_ID = "critics_id";
	private static final String TAG_CURRENTPAGE = "currentPage";
	private static final String TAG_TOTALPAGE = "totalPage";
	private static final String TAG_RATING = "rating";
	private Context con;
	

	private String errorCode, errorMsg;
	private JSONObject jObjList, jsonObject1, jsonObject2, jsonObject3;
	private List<NameValuePair> nameValuePairs;
	private SharedPreferences _pref;
	private int total_page = 0, current_page = 1, flagPagi = 0;
	private View listFooter;
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.critic_reviewlist_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("CriticRestaurentRevActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		con = this;
		initialize();
		setTextFont();
		expandableListReviewView.setGroupIndicator(null);
		listFooter = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.pagination_footer, null, false);
		expandableListReviewView.setAdapter(expReviewAdapter);

		rlCriticRListConCall.setOnClickListener(new OnClickListener() {

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

		llSearchCriticReviewList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CriticRestaurentRevActivity.this,
						SearchListActivity.class));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		allist_book_now.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				allist_book_now.setBackgroundColor(Color.parseColor("#9B9A9A"));
//				BooknowPopupActivity.rlBookNowBultton = allist_book_now;
				startActivity(new Intent(CriticRestaurentRevActivity.this,
						BookRestaurentActivity.class));
				overridePendingTransition(R.anim.slide_up_in,
						R.anim.slide_down_out);
			}
		});
		/*ivSearchItemAlllist.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (flag == 0) {
					ivSearchItemAlllist.setSelected(true);
					flag = 1;
				} else {
					ivSearchItemAlllist.setSelected(false);
					flag = 0;
				}

				if (llSearchAlllist.getVisibility() == View.GONE) {
					llSearchAlllist.setVisibility(View.VISIBLE);
					Animation animation = AnimationUtils.loadAnimation(
							CriticRestaurentRevActivity.this,
							R.anim.animation_open);
					animation.setDuration(500);
					llSearchAlllist.setAnimation(animation);
					llSearchAlllist.animate();
					animation.start();
				} else {
					llSearchAlllist.setVisibility(View.GONE);
					Animation animation = AnimationUtils.loadAnimation(
							CriticRestaurentRevActivity.this,
							R.anim.animation_close);
					animation.setDuration(500);
					llSearchAlllist.setAnimation(animation);
					llSearchAlllist.animate();
					animation.start();
				}
			}
		});*/

		ivMenuAlllist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CriticRestaurentRevActivity.this,
						MenuListActivity.class));
				overridePendingTransition(R.anim.slide_down_info,
						R.anim.slide_up_info);
			}
		});

		expandableListReviewView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (totalItemCount == (visibleItemCount + firstVisibleItem)
						&& totalItemCount != 0) {

					if (total_page > current_page && flagPagi != 1) {
						current_page++;
						Log.e("total_page", total_page + "");
						Log.e("current_page", current_page + "");
						flagPagi = 1;
						loadItem();
					}
				}
			}
		});

		expandableListReviewView
				.setOnGroupClickListener(new OnGroupClickListener() {

					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						// TODO Auto-generated method stub

						/*
						 * ImageView symbol = (ImageView) v
						 * .findViewById(R.id.tvCrtRestSign);
						 * 
						 * if
						 * (expListReviewItems.get(groupPosition).getIsOpened()
						 * == 0) {
						 * symbol.setImageResource(R.drawable.minus_black);
						 * expListReviewItems.get(groupPosition)
						 * .setIsOpened(1); } else {
						 * symbol.setImageResource(R.drawable.plus_black);
						 * expListReviewItems.get(groupPosition)
						 * .setIsOpened(0); }
						 */

						Intent intent = new Intent(
								CriticRestaurentRevActivity.this,
								DetailsActivity.class);
						_pEditor.putString(
								"itemId",
								String.valueOf(expListReviewItems.get(
										groupPosition).getItemId()));
						_pEditor.commit();
						Constant.NEAR_BY_LIST = 0;
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);

						return true;
					}
				});

		loadItem();

	}

	@SuppressLint("CutPasteId")
	private void initialize() {
		expandableListReviewView = (ExpandableListView) findViewById(R.id.elvCriticReviewedItem);
		expListReviewItems = new ArrayList<ExtCriticReviewListItem>();
		expReviewAdapter = new ExtReviewItemAdapter(
				CriticRestaurentRevActivity.this, expListReviewItems);
//		ivSearchItemAlllist = (ImageView) findViewById(R.id.ivSearchItemCriticReviewList);
		llSearchCriticReviewList = (LinearLayout) findViewById(R.id.llSearchCriticReviewList);
		ivMenuAlllist = (ImageView) findViewById(R.id.ivMenuCriticReviewList);
		allist_book_now = (RelativeLayout) findViewById(R.id.criticreviewlist_book_now);
		llSearchAlllist = (LinearLayout) findViewById(R.id.llSearchCriticReviewList);
		rlCriticRListConCall = (RelativeLayout) findViewById(R.id.rlCriticRListConCall);
		textSearchCriticreviewList = (TextView) findViewById(R.id.textSearchCriticreviewList);
		_pref = new Pref(CriticRestaurentRevActivity.this)
				.getSharedPreferencesInstance();
		_pEditor = new Pref(CriticRestaurentRevActivity.this)
				.getSharedPreferencesEditorInstance();
	}

	private void loadItem() {
		jsonObject2 = new JSONObject();
		jsonObject1 = new JSONObject();
		try {
			jsonObject2.put(TAG_ACCESSTOKN, "");
			jsonObject2.put(TAG_CRITICS_ID, _pref.getString("criticsId", ""));
			jsonObject2.put(TAG_CURRENTPAGE, current_page);
			jsonObject1.put(TAG_GETITEM, jsonObject2);
			
			Log.v("input data", jsonObject1.toString());

			ListAstClass listAstClass = new ListAstClass();
			listAstClass.execute("");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class ListAstClass extends AsyncTask<String, String, Long> {

		public ListAstClass() {
			if (current_page == 1) {
				dialog = CustomAlertProgressDialog.getCustomDialog(CriticRestaurentRevActivity.this,"Please wait...");
				dialog.show();
			}
		}

		@SuppressWarnings("static-access")
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {

				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL
						+ "ListCriticReviewResturent", nameValuePairs,
						jsonObject1.toString(),con,this);

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}

			return null;
		}

		protected void onProgerssUpdate(String... values) {

		}

		@SuppressWarnings("static-access")
		protected void onPostExecute(Long result) {
			dialog.dismiss();
			Log.v("onPostExecute", "onPostExecute");
			flagPagi = 0;
			JSONObject objData;
			JSONArray listArray;
			try {
				objData = jObjList.getJSONObject(TAG_DATA);
				JSONObject errorObj = jObjList.getJSONObject(TAG_ERNODE);

				errorCode = errorObj.getString(TAG_ERCODE);
				errorMsg = errorObj.getString(TAG_ERMSG);

				if (errorCode.equalsIgnoreCase("0")) {
					total_page = jObjList.getInt(TAG_TOTALPAGE);
					listArray = objData.getJSONArray(TAG_ITEMDATA);

					if (listArray.length() > 0) {
						for (int i = 0; i < listArray.length(); i++) {
							JSONObject jsonObject = listArray.getJSONObject(i);
							ExtCriticReviewListItem _listItem = new ExtCriticReviewListItem();

							_listItem.setItemId(jsonObject.getInt(TAG_ID));
							_listItem.setItemName(jsonObject
									.getString(TAG_NAME));
							String arrAdderss[] = jsonObject.getString(
									TAG_ADDRESS).split(",");
							_listItem.setItemAddress(arrAdderss[0] + ", "
									+ arrAdderss[1]);
							_listItem.setItemType(jsonObject
									.getString(TAG_SPECIALITY));
							_listItem.setIsOpened(0);
							_listItem.setItemRating(jsonObject
									.getString(TAG_RATING));

							ArrayList<CriticReviewItem> _arrList = new ArrayList<CriticReviewItem>();

							CriticReviewItem _subItem = new CriticReviewItem();
							String review = jsonObject.getString(TAG_REVIEW)
									.replace("<\\/li><li>", "\r\n\r \r\n\r");
							_subItem.setReviews(review);
							_arrList.add(_subItem);
							_listItem.setSubList(_arrList);
							expListReviewItems.add(_listItem);

						}
						expReviewAdapter.notifyDataSetChanged();
					} else {
						Toast.makeText(CriticRestaurentRevActivity.this,
								"No Item Available", Toast.LENGTH_LONG).show();
					}
					if (current_page == 1 && listArray.length() > 24) {
						expandableListReviewView.addFooterView(listFooter);
					}

					if (total_page == current_page && flagPagi != 1) {
						expandableListReviewView.removeFooterView(listFooter);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Log.e("bookPopupFlag", String.valueOf(bookPopupFlag));
		if (bookPopupFlag == 1) {
			popupWindow.dismiss();
			bookPopupFlag = 0;
		} else {
			super.onBackPressed();
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}
	}

	private void setTextFont() {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/Aller_Lt.ttf";
		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
		Typeface tf3 = Typeface.createFromAsset(getAssets(), fontPath3);

		textSearchCriticreviewList.setTypeface(tf3);
	}

	@Override
	public void tryAgain(AsyncTask<String, String, Long> conAsync) {
		conAsync = new ListAstClass();
		conAsync.execute("");
		
	}
}
