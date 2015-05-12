package com.easydiner.activities;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.classes.Constant;
import com.classes.EasydinerClass;
import com.classes.JsonobjectPost;
import com.classes.Pref;
import com.easydiner.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class EazyTrendDetailsActivity extends EasyDinerBaseActivity implements
		AnimationListener {

	private ImageView ivMenuEazyTrendDetail, ivSearchItemEazyTrendDetail,
			ivDetailEazyTrendsHeaderImage, ivEazyTrendsAddwishList;
	private RelativeLayout rlDetailEazyTrendsConcirgeCall,
			RlDetailEazyTrendsBookNow;
	private TextView tvDetailEazyTrendsName, tvDetailEazyTrendsBody,
			tvDetailEazyTrendsIn, tvEazyTrendsLikeCount,
			textSearchEazyTrendsDetails;
	private LinearLayout llSearchDetailEazyTrends;
	private int flag = 0, trendsId, like_count;
	private ImageLoader imageLoader;
	private Animation sequentialAnimOne, smallZoom;
	private SharedPreferences _pref;
	private SharedPreferences.Editor _pEditor;

	JSONObject jsonObjectWishLike, jObjList;
	private List<NameValuePair> nameValuePairs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eazy_trends_details_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("EazyTrendDetailsActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		initialize();
		setTestFont();
		onClick();

		sequentialAnimOne.setAnimationListener(this);

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true)
				.showImageOnLoading(R.drawable.default_image_restaurant)
				.showImageForEmptyUri(R.drawable.ic_background)
				.showImageOnFail(R.drawable.ic_background)
				.imageScaleType(ImageScaleType.EXACTLY).build();
		imageLoader.displayImage(
				getIntent().getExtras().getString("trendsImage"),
				ivDetailEazyTrendsHeaderImage, options,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub
						Animation anim = AnimationUtils.loadAnimation(
								EazyTrendDetailsActivity.this,
								android.R.anim.fade_in);
						ivDetailEazyTrendsHeaderImage.setAnimation(anim);
						anim.start();
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub

					}
				});
		trendsId = getIntent().getExtras().getInt("trendsId");
		tvDetailEazyTrendsName.setText(getIntent().getExtras().getString(
				"trendsName"));
		tvDetailEazyTrendsBody.setText(getIntent().getExtras().getString(
				"trendsBody"));

		if (_pref.getString("wishlist_eazytrends_" + trendsId, "")
				.equalsIgnoreCase("1")) {
			ivEazyTrendsAddwishList
					.setImageResource(R.drawable.like_icon_hover);
		} else {
			ivEazyTrendsAddwishList.setImageResource(R.drawable.like_icon);
		}

		String detailsTrends = getIntent().getExtras()
				.getString("trendsDetails").replace("\r\n\r\n", "<br/> <br/>");
		tvDetailEazyTrendsIn.setText(Html.fromHtml(detailsTrends));
		like_count = getIntent().getExtras().getInt("trendsLikeCount");
		tvEazyTrendsLikeCount.setText(String.valueOf(like_count));
	}

	private void initialize() {
		ivDetailEazyTrendsHeaderImage = (ImageView) findViewById(R.id.ivDetailEazyTrendsHeaderImage);
		tvDetailEazyTrendsName = (TextView) findViewById(R.id.tvDetailEazyTrendsName);
		tvDetailEazyTrendsBody = (TextView) findViewById(R.id.tvDetailEazyTrendsBody);
		tvDetailEazyTrendsIn = (TextView) findViewById(R.id.tvDetailEazyTrendsIn);
		tvEazyTrendsLikeCount = (TextView) findViewById(R.id.tvEazyTrendsLikeCount);
		llSearchDetailEazyTrends = (LinearLayout) findViewById(R.id.llSearchDetailEazyTrends);
		rlDetailEazyTrendsConcirgeCall = (RelativeLayout) findViewById(R.id.rlDetailEazyTrendsConcirgeCall);
		RlDetailEazyTrendsBookNow = (RelativeLayout) findViewById(R.id.RlDetailEazyTrendsBookNow);
		ivMenuEazyTrendDetail = (ImageView) findViewById(R.id.ivMenuEazyTrendDetail);
		ivEazyTrendsAddwishList = (ImageView) findViewById(R.id.ivEazyTrendsAddwishList);
//		ivSearchItemEazyTrendDetail = (ImageView) findViewById(R.id.ivSearchItemEazyTrendDetail);
		textSearchEazyTrendsDetails = (TextView) findViewById(R.id.textSearchEazyTrendsDetails);
		imageLoader = ImageLoader.getInstance();
		sequentialAnimOne = AnimationUtils.loadAnimation(
				EazyTrendDetailsActivity.this, R.anim.sequential_amin);
		smallZoom = AnimationUtils.loadAnimation(EazyTrendDetailsActivity.this,
				R.anim.small_zoom_out);
		_pref = new Pref(EazyTrendDetailsActivity.this)
				.getSharedPreferencesInstance();
		_pEditor = new Pref(EazyTrendDetailsActivity.this)
				.getSharedPreferencesEditorInstance();
	}

	private void onClick() {

		rlDetailEazyTrendsConcirgeCall
				.setOnClickListener(new OnClickListener() {

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

		ivMenuEazyTrendDetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(EazyTrendDetailsActivity.this,
						MenuListActivity.class));
				overridePendingTransition(R.anim.slide_down_info,
						R.anim.slide_up_info);
			}
		});

		/*ivSearchItemEazyTrendDetail.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (flag == 0) {
					ivSearchItemEazyTrendDetail.setSelected(true);
					flag = 1;
				} else {
					ivSearchItemEazyTrendDetail.setSelected(false);
					flag = 0;
				}

				if (llSearchDetailEazyTrends.getVisibility() == View.GONE) {
					llSearchDetailEazyTrends.setVisibility(View.VISIBLE);
					Animation animation = AnimationUtils.loadAnimation(
							EazyTrendDetailsActivity.this,
							R.anim.animation_open);
					animation.setDuration(500);
					llSearchDetailEazyTrends.setAnimation(animation);
					llSearchDetailEazyTrends.animate();
					animation.start();
				} else {
					llSearchDetailEazyTrends.setVisibility(View.GONE);
					Animation animation = AnimationUtils.loadAnimation(
							EazyTrendDetailsActivity.this,
							R.anim.animation_close);
					animation.setDuration(500);
					llSearchDetailEazyTrends.setAnimation(animation);
					llSearchDetailEazyTrends.animate();
					animation.start();
				}
			}
		});*/

		llSearchDetailEazyTrends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(EazyTrendDetailsActivity.this,
						SearchListActivity.class));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		RlDetailEazyTrendsBookNow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				RlDetailEazyTrendsBookNow.setBackgroundColor(Color.parseColor("#9B9A9A"));
//				BooknowPopupActivity.rlBookNowBultton = RlDetailEazyTrendsBookNow;
				startActivity(new Intent(EazyTrendDetailsActivity.this,
						BookRestaurentActivity.class));
				overridePendingTransition(R.anim.slide_up_in,
						R.anim.slide_down_out);
			}
		});

		ivEazyTrendsAddwishList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (_pref.getString("wishlist_eazytrends_" + trendsId, "")
						.equalsIgnoreCase("1")) {
					_pEditor.putString("wishlist_eazytrends_" + trendsId, "0");
					like_count = like_count - 1;
					tvEazyTrendsLikeCount.setText(String.valueOf(like_count));
				} else {
					_pEditor.putString("wishlist_eazytrends_" + trendsId, "1");
					like_count = like_count + 1;
					tvEazyTrendsLikeCount.setText(String.valueOf(like_count));
				}
				_pEditor.commit();
				ivEazyTrendsAddwishList.startAnimation(sequentialAnimOne);

				jsonObjectWishLike = new JSONObject();
				JSONObject jsonObject2 = new JSONObject();

				try {
					jsonObject2.put("accessTokn",
							_pref.getString("accessToken", ""));
					jsonObject2.put("eazytrendsId", trendsId);
					jsonObject2.put("userId",
							_pref.getString("membershipNo", ""));
					jsonObject2.put("status", _pref.getString(
							"wishlist_eazytrends_" + trendsId, ""));
					jsonObjectWishLike.put("getItem", jsonObject2);

					Log.v("input data", jsonObjectWishLike.toString());

					AstClassSetLikeWish astClassSetLikeWish = new AstClassSetLikeWish();
					astClassSetLikeWish.execute("");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	private void setTestFont() {
		String fontPath3 = "fonts/Aller_Lt.ttf";
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf3 = Typeface.createFromAsset(getAssets(), fontPath3);
		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
		textSearchEazyTrendsDetails.setTypeface(tf3);
		tvDetailEazyTrendsName.setTypeface(tf1);
		tvDetailEazyTrendsBody.setTypeface(tf2);
		tvDetailEazyTrendsIn.setTypeface(tf2);
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		if (_pref.getString("wishlist_eazytrends_" + trendsId, "")
				.equalsIgnoreCase("1")) {
			ivEazyTrendsAddwishList
					.setImageResource(R.drawable.like_icon_hover);
		} else {
			ivEazyTrendsAddwishList.setImageResource(R.drawable.like_icon);
		}
		ivEazyTrendsAddwishList.startAnimation(smallZoom);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	private class AstClassSetLikeWish extends AsyncTask<String, String, Long> {

		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL
						+ "setEazyTrendsWishList", nameValuePairs,
						jsonObjectWishLike.toString());
				Log.v("Like", "Like");

			} catch (Exception e) {
				e.printStackTrace();
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub

		}
	}
}
