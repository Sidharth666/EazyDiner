package com.easydiner.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.classes.Constant;
import com.classes.EasydinerClass;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.dao.ExpItemListAdapter;
import com.easydiner.dataobject.ListItem;
import com.easydiner.fragment.DetailsFragment;
import com.easydiner.fragment.MapFragment;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class DetailsActivity extends EasyDinerBaseActivity {
	public RelativeLayout RlDetailBookNow;
	private RelativeLayout rlPopupCancel,rlDetailConcirgeCall;
	private LinearLayout llSearchDetails, details_near_and_search;
	public static ImageView /*ivDetailNearList,*/ ivSearchItemDetail,
			ivMenuDetails/*, ivDashboardDetails, ivHomeDetails*/;
	private TextView textSearchDetails;
	public static ImageView ivDetailMapView;
	private int len, flag = 0, flagMenu = 0;
	private PopupWindow popupWindowExtlist;
	private ExpItemListAdapter expAdapter;
	public static ArrayList<ListItem> arrayList;
	private static String resultCount;
	public static String restaurantName, restaurantDeals;
	public static int restaurantId;
	private Constant _constant;
	private PopupWindow popupWindow;
	private int bookPopupFlag = 0;
	private SharedPreferences _pref;
	private SharedPreferences.Editor _pEditor;
	private View footerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("DetailsActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		RlDetailBookNow = (RelativeLayout) findViewById(R.id.RlDetailBookNow);
		rlDetailConcirgeCall = (RelativeLayout) findViewById(R.id.rlDetailConcirgeCall);
		/*ivDetailNearList = (ImageView) findViewById(R.id.ivDetailNearList);*/
		ivDetailMapView = (ImageView) findViewById(R.id.ivDetailMapView);
		ivSearchItemDetail = (ImageView) findViewById(R.id.ivSearchItemDetail);
		llSearchDetails = (LinearLayout) findViewById(R.id.llSearchDetails);
		details_near_and_search = (LinearLayout) findViewById(R.id.details_near_and_search);
		ivMenuDetails = (ImageView) findViewById(R.id.ivMenuDetails);
		textSearchDetails = (TextView) findViewById(R.id.textSearchDetails);
		/*ivDashboardDetails = (ImageView) findViewById(R.id.ivDashboardDetails);
		ivHomeDetails = (ImageView) findViewById(R.id.ivHomeDetails);*/
		_pref = new Pref(DetailsActivity.this).getSharedPreferencesInstance();
		_pEditor = new Pref(DetailsActivity.this)
				.getSharedPreferencesEditorInstance();
		_constant = new Constant();
		setTestFont();
		loadListFragment();

		/*if (_pref.getString("accessToken", "").equalsIgnoreCase("")) {
			Log.v("in not", "in not");
			ivDashboardDetails.setVisibility(View.GONE);

		} else {
			Log.v("in not", "in not");
			ivDashboardDetails.setVisibility(View.VISIBLE);
		}

		ivDashboardDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(DetailsActivity.this,
						DashboardActivity.class));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		ivHomeDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(DetailsActivity.this,
						HomeActivity.class));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});*/

		RlDetailBookNow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				RlDetailBookNow.setBackgroundColor(Color.parseColor("#9B9A9A"));
//				BooknowPopupActivity.rlBookNowBultton = RlDetailBookNow;
				Intent intent = new Intent(DetailsActivity.this,BooknowPopupActivity.class);
				intent.putExtra("restaurantName", restaurantName);
				intent.putExtra("restaurantId", restaurantId);
				intent.putExtra("restaurantDeals", restaurantDeals);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);
			}
		});

		/*ivDetailNearList.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_constant.NEAR_BY_LIST = 1;
				startActivity(new Intent(DetailsActivity.this,
						AlllistActivity.class));
				// overridePendingTransition(R.anim.slide_in_left,
				// R.anim.slide_out_right);
			}
		});*/
		
		ivDetailMapView.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_constant.MAP_DETAILS = 1;
				loadMapFragment();
			}
		});

		ivSearchItemDetail.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (flag == 0) {
					ivSearchItemDetail.setSelected(true);
					flag = 1;
				} else {
					ivSearchItemDetail.setSelected(false);
					flag = 0;
				}

				if (llSearchDetails.getVisibility() == View.GONE) {
					llSearchDetails.setVisibility(View.VISIBLE);
					Animation animation = AnimationUtils.loadAnimation(
							DetailsActivity.this, R.anim.animation_open);
					animation.setDuration(500);
					llSearchDetails.setAnimation(animation);
					llSearchDetails.animate();
					animation.start();
				} else {
					llSearchDetails.setVisibility(View.GONE);
					Animation animation = AnimationUtils.loadAnimation(
							DetailsActivity.this, R.anim.animation_close);
					animation.setDuration(500);
					llSearchDetails.setAnimation(animation);
					llSearchDetails.animate();
					animation.start();
				}
			}
		});

		llSearchDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(DetailsActivity.this,
						SearchListActivity.class));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		rlDetailConcirgeCall.setOnClickListener(new OnClickListener() {

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

		ivMenuDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(DetailsActivity.this,
						MenuListActivity.class));
				overridePendingTransition(R.anim.slide_down_info,
						R.anim.slide_up_info);
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (bookPopupFlag == 1) {
			popupWindow.dismiss();
			bookPopupFlag = 0;
		} else {
			super.onBackPressed();
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}
	}

	private void loadListFragment() {
		unSelectTab();
		//ivDetailNearList.setSelected(true);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();
		DetailsFragment mvAc = DetailsFragment.newInstance();
		tran.replace(R.id.tabFrameLayoutDetails, mvAc);
		tran.commit();
	}

	private void loadMapFragment() {
		unSelectTab();
		ivDetailMapView.setSelected(true);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putSerializable("list", arrayList);
		bundle.putString("resultCount", resultCount);
		MapFragment mvAc = MapFragment.newInstance(bundle);
		tran.replace(R.id.tabFrameLayoutDetails, mvAc);
		tran.commit();
	}

	public static void unSelectTab() {
		//ivDetailNearList.setSelected(false);
		ivDetailMapView.setSelected(false);
	}

	private void setTestFont(){
		String fontPath1 = "fonts/Aller_Lt.ttf";
		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		textSearchDetails.setTypeface(tf1);
	}
}
