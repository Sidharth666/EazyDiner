package com.easydiner.activities;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.classes.CommonFunction.TimeoutDialogTryAgainListener;
import com.classes.Constant;
import com.classes.EasydinerClass;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.dataobject.ListItem;
import com.easydiner.fragment.AlllistFragment;
import com.easydiner.fragment.MapFragment;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class AlllistActivity extends EasyDinerBaseActivity implements TimeoutDialogTryAgainListener{

	private static ImageView /* ivNearList, */ivSearchItemAlllist,ivMenuAlllist;
	private TextView textSearchAlllist;
	private static ImageView ivMapView;/* , ivDashboardAlllist, ivHomeAlllist; */
	private RelativeLayout /* Rlalllistheader, */allist_book_now,rlPopupCancel, rlAllistBtnEazyCon;
	private LinearLayout llSearchAlllist;
	private int len, flag = 0;
	public static ArrayList<ListItem> arrayList;
	private Constant _constant;
	private PopupWindow popupWindow;
	private int bookPopupFlag = 0;
	private SharedPreferences.Editor _pEditor;
	private SharedPreferences _pref;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alllist_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("AlllistActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
	    
		ivMapView = (ImageView) findViewById(R.id.ivMapView);
//		ivSearchItemAlllist = (ImageView) findViewById(R.id.ivSearchItemAlllist);
		allist_book_now = (RelativeLayout) findViewById(R.id.allist_book_now);
		rlAllistBtnEazyCon = (RelativeLayout) findViewById(R.id.rlAllistBtnEazyCon);
		llSearchAlllist = (LinearLayout) findViewById(R.id.llSearchAlllist);
		ivMenuAlllist = (ImageView) findViewById(R.id.ivMenuAlllist);
		textSearchAlllist = (TextView) findViewById(R.id.textSearchAlllist);
		_pref = new Pref(AlllistActivity.this).getSharedPreferencesInstance();
		_pEditor = new Pref(AlllistActivity.this).getSharedPreferencesEditorInstance();
		_constant = new Constant();
		setTestFont();
		
//		((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(textSearchAlllist.getWindowToken(), 0);
		
//		((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
		
		if (getIntent().hasExtra("selectMapView")) {
			arrayList = Constant.arrayListItem;
			loadMapFragment();
		} else {
			loadListFragment();

		}
		

		ivMapView.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_constant.NEAR_BY_LIST = 1;
				_constant.MAP_DETAILS = 0;
				loadMapFragment();
			}
		});

		
		allist_book_now.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				allist_book_now.setBackgroundColor(Color.parseColor("#9B9A9A"));
//				BooknowPopupActivity.rlBookNowBultton = allist_book_now;
				startActivity(new Intent(AlllistActivity.this,BookRestaurentActivity.class));
				overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);
			}
		});
		rlAllistBtnEazyCon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String number = "7861004444";
				Intent dial = new Intent();
				dial.setAction("android.intent.action.DIAL");
				dial.setData(Uri.parse("tel:" + number));
				startActivity(dial);
			}
		});
		/*ivSearchItemAlllist.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if (flag == 0) {
					ivSearchItemAlllist.setSelected(true);
					
				} else {
					ivSearchItemAlllist.setSelected(false);
					
				}

				if (llSearchAlllist.getVisibility() == View.GONE) {
					llSearchAlllist.setVisibility(View.VISIBLE);
					Animation animation = AnimationUtils.loadAnimation(AlllistActivity.this, R.anim.animation_open);
					animation.setDuration(500);
					llSearchAlllist.setAnimation(animation);
					llSearchAlllist.animate();
					animation.start();
				} else {
					llSearchAlllist.setVisibility(View.GONE);
					Animation animation = AnimationUtils.loadAnimation(AlllistActivity.this, R.anim.animation_close);
					animation.setDuration(500);
					llSearchAlllist.setAnimation(animation);
					llSearchAlllist.animate();
					animation.start();
				}

			}
		});*/

		llSearchAlllist.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AlllistActivity.this,SearchListActivity.class));
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});

		ivMenuAlllist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MenuListActivity.toAllListActivity = 1;
				startActivity(new Intent(AlllistActivity.this,MenuListActivity.class));
				overridePendingTransition(R.anim.slide_down_info,R.anim.slide_up_info);

			}
		});

		llSearchAlllist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(AlllistActivity.this,SearchListActivity.class));
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});
	}

	@Override
	public void onBackPressed() {
		Log.e("bookPopupFlag", String.valueOf(bookPopupFlag));
		if (getFragmentManager().getBackStackEntryCount() == 0) {
	        this.finish();
	    } else {
	        getFragmentManager().popBackStack();
	    }
		/*if (bookPopupFlag == 1) {
			popupWindow.dismiss();
			bookPopupFlag = 0;
		} else {
			startActivity(new Intent(AlllistActivity.this, HomeActivity.class));*/
			overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
//		}
	}

	private void loadListFragment() {
		unSelectTab();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();
		AlllistFragment mvAc = AlllistFragment.newInstance();
		String tag = mvAc.getTag();
		tran.addToBackStack(null);
		tran.replace(R.id.tabFrameLayout, mvAc);
		tran.commit();
	}

	public void loadMapFragment() {
		unSelectTab();
		ivMapView.setSelected(true);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putSerializable("list", arrayList);

		if (!arrayList.isEmpty()) {
			bundle.putInt("resultCount", arrayList.size());
		} else {
			bundle.putInt("resultCount", 0);
		}
		MapFragment mvAc = MapFragment.newInstance(bundle);
		tran.replace(R.id.tabFrameLayout, mvAc);
		tran.commit();
	}

	public static void unSelectTab() {
		ivMapView.setSelected(false);
	}

	private void setTestFont() {
		String fontPath1 = "fonts/Aller_Lt.ttf";
		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		textSearchAlllist.setTypeface(tf1);
	}

	@Override
	public void tryAgain(AsyncTask<String, String, Long> conAsync) {
		AlllistFragment frag = new AlllistFragment();
		conAsync = frag.new ListAstClass();
		conAsync.execute("");
		
	}

}
