package com.easydiner.activities;

import java.util.ArrayList;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.classes.Constant;
import com.classes.EasydinerClass;
import com.easydiner.R;
import com.easydiner.fragment.ProfileEditFragment;
import com.easydiner.fragment.RedempFragment;
import com.easydiner.fragment.ReservitionDetailsFragment;
import com.easydiner.fragment.ReviewformDashbordFragmant;
import com.easydiner.fragment.ReviewsFragment;
import com.easydiner.fragment.WishlistFragment;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class DashboardActivity extends EasyDinerBaseActivity {

	public static ImageView ivGoHome, ivMenuHome, ivReservationDashBoard,
			ivProfileDashBoard, ivReviewDashBoard, ivWishListDashBoard,
			ivPiggyDashBoard;
	public static TextView tvDashBoardHeading;
	public static LinearLayout llSearchDashBoard, llReservationDashBoard,
			llProfileDashBoard, llReviewDashBoard, llWishListDashBoard,
			llPiggyDashBoard;
	private RelativeLayout rlDashBoardBtnEazyCon, DashBoard_book_now;
	public static int activeTab, flagRedeemp = 0;

	public static ArrayList<String> reservationDetails = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("DashboardActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		intialize();
		setTextFont();
		onClick();

		switch (activeTab) {
		case 1:
			reservationFragment();
			break;

		case 2:
			profileFragment();
			break;

		case 3:
			reviewFormFrament();
			break;

		case 4:
			wishListFragment();
			break;

		case 5:
			loadRedempFrag();
			break;
		case 6:
			loadreservationDetailsFragment();
			break;

		default:
			loadRedempFrag();
			break;
		}
	}

	public void intialize() {
		// TODO Auto-generated method stub

		ivGoHome = (ImageView) findViewById(R.id.ivGoHome);
		ivMenuHome = (ImageView) findViewById(R.id.ivMenuHome);
		ivReservationDashBoard = (ImageView) findViewById(R.id.ivReservationDashBoard);
		ivProfileDashBoard = (ImageView) findViewById(R.id.ivProfileDashBoard);
//		ivReviewDashBoard = (ImageView) findViewById(R.id.ivReviewImageListDashBoard);
//		ivWishListDashBoard = (ImageView) findViewById(R.id.ivWishListDashBoard);
		ivPiggyDashBoard = (ImageView) findViewById(R.id.ivPiggyDashBoard);
		tvDashBoardHeading = (TextView) findViewById(R.id.tvDashBoardHeading);
		llReservationDashBoard = (LinearLayout) findViewById(R.id.llReservationDashBoard);
		llProfileDashBoard = (LinearLayout) findViewById(R.id.llProfileDashBoard);
//		llReviewDashBoard = (LinearLayout) findViewById(R.id.llReviewImageListDashBoard);
//		llWishListDashBoard = (LinearLayout) findViewById(R.id.llWishListDashBoard);
		llPiggyDashBoard = (LinearLayout) findViewById(R.id.llPiggyDashBoard);
		rlDashBoardBtnEazyCon = (RelativeLayout) findViewById(R.id.rlDashBoardBtnEazyCon);
		DashBoard_book_now = (RelativeLayout) findViewById(R.id.DashBoard_book_now);
	}

	@Override
	public void onBackPressed() { // TODO Auto-generated method stub
		super.onBackPressed();

		// DashboardActivity.activeTab = 1;

		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		finish();

	}

	public void reservationFragment() {
		// TODO Auto-generated method stub
		unSelectTab();
		ivReservationDashBoard.setSelected(true);

		Intent intent = new Intent(DashboardActivity.this,
				DashboardHomeActivity.class);
		startActivity(intent);
		finish();
	}

	public void loadreservationDetailsFragment() {
		// unSelectTab();
		// ivProfileDashBoard.setSelected(true);
		// tvDashBoardHeading.setText("PROFILE");
		// llReadPreviousReview.setVisibility(View.GONE);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();

		Bundle bundle = new Bundle();
		bundle.putSerializable("reservationDetails", reservationDetails);
		ReservitionDetailsFragment mFrg = ReservitionDetailsFragment
				.newInstance(bundle);
		tran.replace(R.id.flDashBoardFrameLayout, mFrg);
		tran.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
		// tran.addToBackStack("reservation");
		tran.commit();
	}

	public void profileFragment() {
		// unSelectTab();
		// ivProfileDashBoard.setSelected(true);
		// tvDashBoardHeading.setText("PROFILE");
		// llReadPreviousReview.setVisibility(View.GONE);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();
		ProfileEditFragment mFrg = ProfileEditFragment.newInstance();
		tran.replace(R.id.flDashBoardFrameLayout, mFrg);
		tran.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
		// tran.addToBackStack("profile");
		tran.commit();
	}

	public void reviewFormFrament() {
		// unSelectTab();
		// ivReviewDashBoard.setSelected(true);
		// tvDashBoardHeading.setText("MY REVIEWS");
		// llReadPreviousReview.setVisibility(View.VISIBLE);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();
		ReviewformDashbordFragmant mFrg = ReviewformDashbordFragmant
				.newInstance();
		tran.replace(R.id.flDashBoardFrameLayout, mFrg);
		tran.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
		tran.addToBackStack("reviewsform");
		tran.commit();
	}

	public void reviewFrament() {
		// unSelectTab();
		// ivReviewDashBoard.setSelected(true);
		// tvDashBoardHeading.setText("MY REVIEWS");
		// llReadPreviousReview.setVisibility(View.GONE);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();
		ReviewsFragment mFrg = ReviewsFragment.newInstance();
		tran.replace(R.id.flDashBoardFrameLayout, mFrg);
		tran.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
		// tran.addToBackStack("reviews");
		tran.commit();
	}

	private void wishListFragment() {
		// unSelectTab();
		// ivWishListDashBoard.setSelected(true);
		// tvDashBoardHeading.setText("WISHLIST");
		// llReadPreviousReview.setVisibility(View.GONE);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();
		WishlistFragment mFrg = WishlistFragment.newInstance();
		tran.replace(R.id.flDashBoardFrameLayout, mFrg);
		tran.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
		// tran.addToBackStack("wishlist");
		tran.commit();
	}

	public void loadRedempFrag() {
		// unSelectTab();
		// ivPiggyDashBoard.setSelected(true);
		// tvDashBoardHeading.setText("REDEMPTION OFFER");
		// llReadPreviousReview.setVisibility(View.GONE);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();
		RedempFragment mFrg = RedempFragment.newInstance();
		tran.replace(R.id.flDashBoardFrameLayout, mFrg);
		tran.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
		// tran.addToBackStack("redemption");
		tran.commit();
	}

	public void unSelectTab() {
		ivReservationDashBoard.setSelected(false);
		ivProfileDashBoard.setSelected(false);
//		ivReviewDashBoard.setSelected(false);
//		ivWishListDashBoard.setSelected(false);
		ivPiggyDashBoard.setSelected(false);
	}

	public void onClick() {
		// TODO Auto-generated method stub

		ivMenuHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DashboardActivity.this,
						MenuListActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_down_info,
						R.anim.slide_up_info);
			}
		});

		ivGoHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(getApplicationContext(),
						HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
		});

		llReservationDashBoard.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// reservationFragment();
				unSelectTab();
				ivReservationDashBoard.setSelected(true);
				Intent i = new Intent(DashboardActivity.this,
						DashboardHomeActivity.class);
				i.putExtra("upcoming_booking", "1");
				startActivity(i);
				finish();

			}
		});
		llProfileDashBoard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				profileFragment();

			}
		});
		/*llReviewDashBoard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reviewFormFrament();

			}
		});*/
		/*llWishListDashBoard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wishListFragment();
			}
		});*/
		llPiggyDashBoard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadRedempFrag();
			}
		});

		rlDashBoardBtnEazyCon.setOnClickListener(new OnClickListener() {

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

		DashBoard_book_now.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				DashBoard_book_now.setBackgroundColor(Color.parseColor("#9B9A9A"));
//				BooknowPopupActivity.rlBookNowBultton = DashBoard_book_now;
				startActivity(new Intent(DashboardActivity.this,
						BookRestaurentActivity.class));
				overridePendingTransition(R.anim.slide_up_in,
						R.anim.slide_down_out);
			}
		});
	}

	private void setTextFont() {
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath2);

		tvDashBoardHeading.setTypeface(tf1);
	}
}
