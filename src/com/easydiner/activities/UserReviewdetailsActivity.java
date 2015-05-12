package com.easydiner.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.classes.CircularImageView;
import com.classes.EasydinerClass;
import com.easydiner.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserReviewdetailsActivity extends Activity {

	private TextView tvUserHeadingUserreviewDetails,
			tvRatingDateUserreviewDetails, tvUserNameUserreviewDetails,
			tvNoOfReviewsUserreviewDetails, tvUserAddressUserreviewDetails,
			tvReviewDescriptionUserreviewDetails, tvReviwNoUpUserreviewDetails,
			tvPhotoNoUpUserreviewDetails;
//	private CircularImageView civUserImageUserreviewDetails;
	private ImageLoader imageLoader;
	private ImageView ivUserRatingsUserreviewDetails,
			ivSearchItemUserreviewDetails, ivMenuUserreviewDetails;
	private String monthArray[] = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
			"JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
	private String userImage;
	private RelativeLayout rlUserreviewDetailsBtnEazyCon,
			rlUserreviewDetails_book_now;
	private LinearLayout llSearchUserreviewDetails;
	private int flag = 0, userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userreview_details_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("UserReviewdetailsActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
		
		initialize();
		setTextFont();
		onClick();
		tvUserNameUserreviewDetails.setText(getIntent().getExtras().getString(
				"user_name"));

		String arrDate[] = getIntent().getExtras().getString("user_reviewdate")
				.split("-");
		String date[] = arrDate[2].split(" ");

		tvRatingDateUserreviewDetails.setText(date[0] + " "
				+ monthArray[(Integer.parseInt(arrDate[1]) - 1)] + " "
				+ arrDate[0]);

		userId = getIntent().getExtras().getInt("user_id");

		tvUserHeadingUserreviewDetails.setText("\""
				+ getIntent().getExtras().getString("user_heading") + "\"");
		tvNoOfReviewsUserreviewDetails.setText("("
				+ String.valueOf(getIntent().getExtras().getInt(
						"user_review_no")
						+ " Reviews)"));
		/*tvUserAddressUserreviewDetails.setText(getIntent().getExtras()
				.getString("user_address"));*/
		tvReviewDescriptionUserreviewDetails.setText(getIntent().getExtras()
				.getString("user_review"));
		/*tvReviwNoUpUserreviewDetails.setText(String.valueOf(getIntent()
				.getExtras().getInt("user_review_no") + " User Reviews"));*/
		// tvPhotoNoUpUserreviewDetails
		userImage = getIntent().getExtras()
				.getString("user_image");
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true)
				.showImageOnLoading(R.drawable.default_image_for_circuler)
				.showImageForEmptyUri(R.drawable.default_image_for_circuler)
				.showImageOnFail(R.drawable.default_image_for_circuler).build();

//		imageLoader.displayImage(userImage, civUserImageUserreviewDetails,options);

		int rating = getIntent().getExtras().getInt("user_rating");
		switch (rating) {
		case 0:
			ivUserRatingsUserreviewDetails
					.setImageResource(R.drawable.zero_star);
			break;

		case 1:
			ivUserRatingsUserreviewDetails
					.setImageResource(R.drawable.one_star);
			break;

		case 2:
			ivUserRatingsUserreviewDetails
					.setImageResource(R.drawable.two_star);
			break;

		case 3:
			ivUserRatingsUserreviewDetails
					.setImageResource(R.drawable.three_star);
			break;

		case 4:
			ivUserRatingsUserreviewDetails
					.setImageResource(R.drawable.four_star);
			break;

		case 5:
			ivUserRatingsUserreviewDetails
					.setImageResource(R.drawable.five_star);
			break;

		default:
			break;
		}

	}

	private void initialize() {
		tvUserHeadingUserreviewDetails = (TextView) findViewById(R.id.tvUserHeadingUserreviewDetails);
		tvRatingDateUserreviewDetails = (TextView) findViewById(R.id.tvRatingDateUserreviewDetails);
		tvUserNameUserreviewDetails = (TextView) findViewById(R.id.tvUserNameUserreviewDetails);
		tvNoOfReviewsUserreviewDetails = (TextView) findViewById(R.id.tvNoOfReviewsUserreviewDetails);
//		tvUserAddressUserreviewDetails = (TextView) findViewById(R.id.tvUserAddressUserreviewDetails);
		tvReviewDescriptionUserreviewDetails = (TextView) findViewById(R.id.tvReviewDescriptionUserreviewDetails);
//		civUserImageUserreviewDetails = (CircularImageView) findViewById(R.id.civUserImageUserreviewDetails);
		ivUserRatingsUserreviewDetails = (ImageView) findViewById(R.id.ivUserRatingsUserreviewDetails);
//		tvReviwNoUpUserreviewDetails = (TextView) findViewById(R.id.tvReviwNoUpUserreviewDetails);
//		tvPhotoNoUpUserreviewDetails = (TextView) findViewById(R.id.tvPhotoNoUpUserreviewDetails);
		ivSearchItemUserreviewDetails = (ImageView) findViewById(R.id.ivSearchItemUserreviewDetails);
		ivMenuUserreviewDetails = (ImageView) findViewById(R.id.ivMenuUserreviewDetails);
		rlUserreviewDetailsBtnEazyCon = (RelativeLayout) findViewById(R.id.rlUserreviewDetailsBtnEazyCon);
		rlUserreviewDetails_book_now = (RelativeLayout) findViewById(R.id.rlUserreviewDetails_book_now);
		llSearchUserreviewDetails = (LinearLayout) findViewById(R.id.llSearchUserreviewDetails);
		imageLoader = ImageLoader.getInstance();
	}

	private void setTextFont() {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/Aller_Lt.ttf";
		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
		Typeface tf3 = Typeface.createFromAsset(getAssets(), fontPath3);
		tvUserHeadingUserreviewDetails.setTypeface(tf1);
		tvRatingDateUserreviewDetails.setTypeface(tf2);
		tvUserNameUserreviewDetails.setTypeface(tf2);
		tvNoOfReviewsUserreviewDetails.setTypeface(tf2);
//		tvUserAddressUserreviewDetails.setTypeface(tf2);
		tvReviewDescriptionUserreviewDetails.setTypeface(tf3);
//		tvReviwNoUpUserreviewDetails.setTypeface(tf2);
//		tvPhotoNoUpUserreviewDetails.setTypeface(tf2);
	}

	public void onClick() {
		ivSearchItemUserreviewDetails.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (flag == 0) {
					ivSearchItemUserreviewDetails.setSelected(true);
					flag = 1;
				} else {
					ivSearchItemUserreviewDetails.setSelected(false);
					flag = 0;
				}

				if (llSearchUserreviewDetails.getVisibility() == View.GONE) {
					llSearchUserreviewDetails.setVisibility(View.VISIBLE);
					Animation animation = AnimationUtils.loadAnimation(
							UserReviewdetailsActivity.this,
							R.anim.animation_open);
					animation.setDuration(500);
					llSearchUserreviewDetails.setAnimation(animation);
					llSearchUserreviewDetails.animate();
					animation.start();
				} else {
					llSearchUserreviewDetails.setVisibility(View.GONE);
					Animation animation = AnimationUtils.loadAnimation(
							UserReviewdetailsActivity.this,
							R.anim.animation_close);
					animation.setDuration(500);
					llSearchUserreviewDetails.setAnimation(animation);
					llSearchUserreviewDetails.animate();
					animation.start();
				}
			}
		});

		ivMenuUserreviewDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(UserReviewdetailsActivity.this,
						MenuListActivity.class));
				overridePendingTransition(R.anim.slide_down_info,
						R.anim.slide_up_info);
			}
		});

		rlUserreviewDetailsBtnEazyCon.setOnClickListener(new OnClickListener() {

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

		rlUserreviewDetails_book_now.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				rlUserreviewDetails_book_now.setBackgroundColor(Color.parseColor("#9B9A9A"));
//				BooknowPopupActivity.rlBookNowBultton = rlUserreviewDetails_book_now;
				startActivity(new Intent(UserReviewdetailsActivity.this,
						BooknowPopupActivity.class));
				overridePendingTransition(R.anim.slide_up_in,
						R.anim.slide_down_out);
			}
		});

		llSearchUserreviewDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(UserReviewdetailsActivity.this,
						SearchListActivity.class));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		/*civUserImageUserreviewDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(UserReviewdetailsActivity.this,
						ReviewerProfileActivity.class);
				intent.putExtra("user_id", userId);
				intent.putExtra("user_image", userImage);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});*/
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
}
