package com.easydiner.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classes.CircularImageView;
import com.classes.CommonFunction.TimeoutDialogTryAgainListener;
import com.classes.ConnectionDetector;
import com.classes.Constant;
import com.classes.CustomAlertProgressDialog;
import com.classes.EasydinerClass;
import com.classes.JsonobjectPost;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.dao.ExpItemListAdapter;
import com.easydiner.dataobject.ListItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CriticDetailsActivity extends EasyDinerBaseActivity implements TimeoutDialogTryAgainListener{

	private static ImageView ivMenuAlllist;
	private static CircularImageView ivCriticDetailsIma;
	private TextView tvCriticDetailsName, tvCriticDetailsDesignation,
			tvCriticDetailsReview, textReadReview, textSearchCriticDetails;
	private PopupWindow popupWindow;
	private LinearLayout llSearchAlllist, llAlllistSearchItem,
			llSearchCriticReview;
	private ExpItemListAdapter expAdapter;
	public static ArrayList<ListItem> arrayList;
	private RelativeLayout rlReadReview, allist_book_now,
			rlCriticDetailsConCall;
	private int len, flag = 0, flagMenu = 0;
	private int bookPopupFlag = 0, listPosition = -1;
	private Constant _constant;
	private SharedPreferences _pref;
	private SharedPreferences.Editor _pEditor;
	private static final String TAG_DATA = "data";
	private static final String TAG_GETREVIEWLIST = "getreviewlist";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_IMAGE = "image";
	private static final String TAG_DESIGNATION = "designation";
	private static final String TAG_REVIEW = "review";
	private static final String TAG_GETITEM = "getItem";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_CRITICS_ID = "critics_id";
	private String errorCode, errorMsg;
	private JSONObject jObjList, jsonObject1, jsonObject2, jsonObject3;
	private List<NameValuePair> nameValuePairs;
	private ImageLoader imageLoader;
	private View footerView;
	private ConnectionDetector _connectionDetector;
	private Context con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.critic_review_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("CriticDetailsActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		con =this;
		initialize();

		allist_book_now.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				allist_book_now.setBackgroundColor(Color.parseColor("#9B9A9A"));
//				BooknowPopupActivity.rlBookNowBultton = allist_book_now;
				startActivity(new Intent(CriticDetailsActivity.this,BookRestaurentActivity.class));
				overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);
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
							CriticDetailsActivity.this, R.anim.animation_open);
					animation.setDuration(500);
					llSearchAlllist.setAnimation(animation);
					llSearchAlllist.animate();
					animation.start();
				} else {
					llSearchAlllist.setVisibility(View.GONE);
					Animation animation = AnimationUtils.loadAnimation(
							CriticDetailsActivity.this, R.anim.animation_close);
					animation.setDuration(500);
					llSearchAlllist.setAnimation(animation);
					llSearchAlllist.animate();
					animation.start();
				}
				llAlllistSearchItem.setVisibility(View.GONE);
			}
		});*/

		rlCriticDetailsConCall.setOnClickListener(new OnClickListener() {

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

		ivMenuAlllist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CriticDetailsActivity.this,
						MenuListActivity.class));
				overridePendingTransition(R.anim.slide_down_info,
						R.anim.slide_up_info);
			}
		});

		rlReadReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent_info = new Intent(CriticDetailsActivity.this,CriticRestaurentRevActivity.class);
				startActivity(intent_info);
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});

		llSearchCriticReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CriticDetailsActivity.this,
						SearchListActivity.class));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		if (_connectionDetector.isConnectingToInternet()) {
			loadItem();
		} else {
			Toast.makeText(CriticDetailsActivity.this,
					"No internet connection available",
					Toast.LENGTH_LONG).show();
		}
	}

	@SuppressLint("CutPasteId")
	private void initialize() {
//		ivSearchItemAlllist = (ImageView) findViewById(R.id.ivSearchItemCriticReview);
		llSearchCriticReview = (LinearLayout) findViewById(R.id.llSearchCriticReview);
		ivMenuAlllist = (ImageView) findViewById(R.id.ivMenuCriticReview);
		allist_book_now = (RelativeLayout) findViewById(R.id.criticreview_book_now);
		llSearchAlllist = (LinearLayout) findViewById(R.id.llSearchCriticReview);
		llAlllistSearchItem = (LinearLayout) findViewById(R.id.llCriticReviewSearchItem);
		rlReadReview = (RelativeLayout) findViewById(R.id.rlReadReview);
		ivCriticDetailsIma = (CircularImageView) findViewById(R.id.ivCriticDetailsIma);
		rlCriticDetailsConCall = (RelativeLayout) findViewById(R.id.rlCriticDetailsConCall);
		setTextFont();
		_pEditor = new Pref(CriticDetailsActivity.this)
				.getSharedPreferencesEditorInstance();
		_pref = new Pref(CriticDetailsActivity.this)
				.getSharedPreferencesInstance();
		_constant = new Constant();
		imageLoader = ImageLoader.getInstance();
		_connectionDetector = new ConnectionDetector(CriticDetailsActivity.this);
	}

	private void loadItem() {
		// TODO Auto-generated method stub
		super.onStart();
		jsonObject2 = new JSONObject();
		jsonObject1 = new JSONObject();
		try {
			jsonObject2.put(TAG_ACCESSTOKN, "");
			jsonObject2.put(TAG_CRITICS_ID, _pref.getString("criticsId", ""));
			jsonObject1.put(TAG_GETITEM, jsonObject2);

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
			dialog = CustomAlertProgressDialog.getCustomDialog(CriticDetailsActivity.this,"Please wait...");
			dialog.show();
		}

		@SuppressWarnings("static-access")
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {

				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL
						+ "CriticDetails", nameValuePairs,
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

			JSONObject objData, itemObject;
			try {
				objData = jObjList.getJSONObject(TAG_DATA);
				JSONObject errorObj = jObjList.getJSONObject(TAG_ERNODE);

				errorCode = errorObj.getString(TAG_ERCODE);
				errorMsg = errorObj.getString(TAG_ERMSG);

				if (errorCode.equalsIgnoreCase("0")) {
					itemObject = objData.getJSONObject(TAG_GETREVIEWLIST);
					//ivCriticDetailsIma.setScaleType(ImageView.ScaleType.FIT_XY);
					
					DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
													.showImageOnLoading(R.drawable.default_image_for_circuler)
													.showImageForEmptyUri(R.drawable.default_image_for_circuler)
													.showImageOnFail(R.drawable.default_image_for_circuler).build();
					imageLoader.displayImage(itemObject.getString(TAG_IMAGE), ivCriticDetailsIma, options);
					
					tvCriticDetailsName.setText(itemObject.getString(TAG_NAME));
					_pEditor.putString("selectCriticName",itemObject.getString(TAG_NAME));
					_pEditor.commit();
					tvCriticDetailsDesignation.setText(itemObject.getString(TAG_DESIGNATION));
					String detailText = itemObject.getString(TAG_REVIEW).replace("\\n\\n", "\r\n\r \r\n\r");
					tvCriticDetailsReview.setText(detailText);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			dialog.dismiss();
			rlReadReview.setVisibility(View.VISIBLE);
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
		tvCriticDetailsName = (TextView) findViewById(R.id.tvCriticDetailsName);
		tvCriticDetailsDesignation = (TextView) findViewById(R.id.tvCriticDetailsDesignation);
		tvCriticDetailsReview = (TextView) findViewById(R.id.ivCriticDetailsReview);
		textReadReview = (TextView) findViewById(R.id.textReadReview);
		textSearchCriticDetails = (TextView) findViewById(R.id.textSearchCriticDetails);

		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/Aller_Lt.ttf";
		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
		Typeface tf3 = Typeface.createFromAsset(getAssets(), fontPath3);

		tvCriticDetailsName.setTypeface(tf1);
		textReadReview.setTypeface(tf1);
		tvCriticDetailsDesignation.setTypeface(tf2);
		tvCriticDetailsReview.setTypeface(tf2);
		textSearchCriticDetails.setTypeface(tf3);
	}

	@Override
	public void tryAgain(AsyncTask<String, String, Long> conAsync) {
		conAsync = new ListAstClass();
		conAsync.execute("");
		
	}

}
