package com.easydiner.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.classes.CircularImageView;
import com.classes.CommonFunction.TimeoutDialogTryAgainListener;
import com.classes.Constant;
import com.classes.CustomAlertProgressDialog;
import com.classes.EasydinerClass;
import com.classes.JsonobjectPost;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.dao.ReviewerRestaurantAdapter;
import com.easydiner.dataobject.ReviewerRestaurantListItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ReviewerProfileActivity extends EasyDinerBaseActivity implements TimeoutDialogTryAgainListener{

	private CircularImageView ivUserImageReviewerprof;
	private ImageView ivSearchItemReviewerprof, ivMenuReviewerprof;
	private TextView tvUserNameReviewerprof, tvUserPlaceReviewerprof,
			tvUserDesignationReviewerprof, tvNoOfUserReviewReviewerprof,
			tvNoOfUserPhotoReviewerprof, tvNoOfUserBadgeReviewerprof,
			textUserReviewReviewerprof, textUserPhotoReviewerprof,
			textUserBadgeReviewerprof, textSearchReviewerprof;
	private ListView lvRestorentListReviewerprof;
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private List<NameValuePair> nameValuePairs;
	private ArrayList<String> imgUrl;
	private ArrayList<ReviewerRestaurantListItem> restaurantListItems;
	private ReviewerRestaurantAdapter restaurantAdapter;
	private static final String TAG_DATA = "data";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_GETCATLIST = "getCatList";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_REVIEWERID = "reviewerId";
	private static final String TAG_NAME = "name";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_DESIGNATION = "designation";
	private static final String TAG_TOTAL_REVIEW = "total_review";
	private static final String TAG_TOTAL_PHOTO = "total_photo";
	private static final String TAG_TOTAL_BADGES = "total_badges";
	private static final String TAG_PHOTO = "photo";
	private static final String TAG_REVIEWED_RESTAURANT = "reviewed_restaurant";
	private static final String TAG_ID = "id";
	private static final String TAG_RESTAURANTNAME = "restaurantName";
	private static final String TAG_LOCATION = "location";
	private static final String TAG_SPECIALITY = "speciality";
	private static final String TAG_IMG = "image";
	private SharedPreferences _pref;
	private SharedPreferences.Editor _pEditor;
	private int userId;
	private String userImg;
	private ImageLoader imageLoader;
	private LinearLayout llSearchReviewerprof;
	private RelativeLayout rlEazyconBtnReviewerprof, Reviewerprof_book_now;
	private int flag = 0;
	private Context con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reviewerprofile_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("ReviewerProfileActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		con = this;
		initialize();
		setTextFont();
		userId = getIntent().getExtras().getInt("user_id");
		userImg = getIntent().getExtras().getString("user_image");
		lvRestorentListReviewerprof.setAdapter(restaurantAdapter);
		loadDetails();
		onClick();
	}

	private void initialize() {
		ivUserImageReviewerprof = (CircularImageView) findViewById(R.id.ivUserImageReviewerprof);
		tvUserNameReviewerprof = (TextView) findViewById(R.id.tvUserNameReviewerprof);
		tvUserPlaceReviewerprof = (TextView) findViewById(R.id.tvUserPlaceReviewerprof);
		tvUserDesignationReviewerprof = (TextView) findViewById(R.id.tvUserDesignationReviewerprof);
		tvNoOfUserReviewReviewerprof = (TextView) findViewById(R.id.tvNoOfUserReviewReviewerprof);
		tvNoOfUserPhotoReviewerprof = (TextView) findViewById(R.id.tvNoOfUserPhotoReviewerprof);
		tvNoOfUserBadgeReviewerprof = (TextView) findViewById(R.id.tvNoOfUserBadgeReviewerprof);
		lvRestorentListReviewerprof = (ListView) findViewById(R.id.lvRestorentListReviewerprof);
		textUserReviewReviewerprof = (TextView) findViewById(R.id.textUserReviewReviewerprof);
		textUserPhotoReviewerprof = (TextView) findViewById(R.id.textUserPhotoReviewerprof);
		textUserBadgeReviewerprof = (TextView) findViewById(R.id.textUserBadgeReviewerprof);
		textSearchReviewerprof = (TextView) findViewById(R.id.textSearchReviewerprof);
		llSearchReviewerprof = (LinearLayout) findViewById(R.id.llSearchReviewerprof);
//		ivSearchItemReviewerprof = (ImageView) findViewById(R.id.ivSearchItemReviewerprof);
		ivMenuReviewerprof = (ImageView) findViewById(R.id.ivMenuReviewerprof);
		rlEazyconBtnReviewerprof = (RelativeLayout) findViewById(R.id.rlEazyconBtnReviewerprof);
		Reviewerprof_book_now = (RelativeLayout) findViewById(R.id.Reviewerprof_book_now);
		imageLoader = ImageLoader.getInstance();
		imgUrl = new ArrayList<String>();
		restaurantListItems = new ArrayList<ReviewerRestaurantListItem>();
		restaurantAdapter = new ReviewerRestaurantAdapter(
				ReviewerProfileActivity.this, restaurantListItems);
		_pref = new Pref(ReviewerProfileActivity.this)
				.getSharedPreferencesInstance();
		_pEditor = new Pref(ReviewerProfileActivity.this)
				.getSharedPreferencesEditorInstance();
	}

	private void onClick() {
		lvRestorentListReviewerprof
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(
								ReviewerProfileActivity.this,
								DetailsActivity.class);
						_pEditor.putString("itemId", String
								.valueOf(restaurantListItems.get(position)
										.getItemId()));
						_pEditor.commit();
						Constant.NEAR_BY_LIST = 0;
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}
				});
		
		/*ivSearchItemReviewerprof.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi") @Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (flag == 0) {
					ivSearchItemReviewerprof.setSelected(true);
					flag = 1;
				} else {
					ivSearchItemReviewerprof.setSelected(false);
					flag = 0;
				}

				if (llSearchReviewerprof.getVisibility() == View.GONE) {
					llSearchReviewerprof.setVisibility(View.VISIBLE);
					Animation animation = AnimationUtils.loadAnimation(
							ReviewerProfileActivity.this, R.anim.animation_open);
					animation.setDuration(500);
					llSearchReviewerprof.setAnimation(animation);
					llSearchReviewerprof.animate();
					animation.start();
				} else {
					llSearchReviewerprof.setVisibility(View.GONE);
					Animation animation = AnimationUtils.loadAnimation(
							ReviewerProfileActivity.this, R.anim.animation_close);
					animation.setDuration(500);
					llSearchReviewerprof.setAnimation(animation);
					llSearchReviewerprof.animate();
					animation.start();
				}
			}
		});*/
		
		ivMenuReviewerprof.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ReviewerProfileActivity.this,
						MenuListActivity.class));
				overridePendingTransition(R.anim.slide_down_info,
						R.anim.slide_up_info);
			}
		});
		
		 rlEazyconBtnReviewerprof.setOnClickListener(new OnClickListener() {
			
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
		 
		 Reviewerprof_book_now.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Reviewerprof_book_now.setBackgroundColor(Color.parseColor("#9B9A9A"));
//				BooknowPopupActivity.rlBookNowBultton = Reviewerprof_book_now;
				startActivity(new Intent(ReviewerProfileActivity.this,BookRestaurentActivity.class));
				overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);
			}
		});
		 
		 llSearchReviewerprof.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ReviewerProfileActivity.this,SearchListActivity.class));
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});
	}

	private void loadDetails() {
		jsonObject1 = new JSONObject();
		jsonObject2 = new JSONObject();

		try {
			jsonObject2.put(TAG_ACCESSTOKN, _pref.getString("", ""));
			jsonObject2.put(TAG_REVIEWERID, String.valueOf(userId));
			jsonObject1.put(TAG_GETCATLIST, jsonObject2);

			Log.v("input data", jsonObject1.toString());
			
			AstClassReviewerDetails astClassReviewerDetails = new AstClassReviewerDetails();
			astClassReviewerDetails.execute("");

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private class AstClassReviewerDetails extends AsyncTask<String, String, Long> {
		private AlertDialog dialog;

		public AstClassReviewerDetails() {
			dialog = CustomAlertProgressDialog.getCustomDialog(ReviewerProfileActivity.this,"Please wait...");
			dialog.show();
		}

		@SuppressWarnings("static-access")
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL
						+ "getReviewerProfile", nameValuePairs,
						jsonObject1.toString(),con,this);

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			JSONObject jObjData, jObjError;
			try {
				jObjData = jObjList.getJSONObject(TAG_DATA);
				jObjError = jObjList.getJSONObject(TAG_ERNODE);
				if (jObjError.getInt(TAG_ERCODE) == 0) {
					tvUserNameReviewerprof
							.setText(jObjData.getString(TAG_NAME));
					tvUserPlaceReviewerprof.setText(jObjData
							.getString(TAG_ADDRESS));
					tvUserDesignationReviewerprof.setText(jObjData
							.getString(TAG_DESIGNATION));
					tvNoOfUserReviewReviewerprof.setText(String
							.valueOf(jObjData.getInt(TAG_TOTAL_REVIEW)));
					tvNoOfUserPhotoReviewerprof.setText(String.valueOf(jObjData
							.getInt(TAG_TOTAL_PHOTO)));
					tvNoOfUserBadgeReviewerprof.setText(String.valueOf(jObjData
							.getInt(TAG_TOTAL_BADGES)));
					DisplayImageOptions options = new DisplayImageOptions.Builder()
							.cacheInMemory(true)
							.cacheOnDisk(true)
							.showImageOnLoading(
									R.drawable.default_image_for_circuler)
							.showImageForEmptyUri(
									R.drawable.default_image_for_circuler)
							.showImageOnFail(
									R.drawable.default_image_for_circuler)
							.build();
					
					imageLoader.displayImage(userImg, ivUserImageReviewerprof,
							options);

					JSONArray imgJsonArray = jObjData.getJSONArray(TAG_PHOTO);
					for (int i = 0; i < imgJsonArray.length(); i++) {
						JSONObject _item = imgJsonArray.getJSONObject(i);
						imgUrl.add(_item.getString(TAG_IMG));
					}

					JSONArray restJsonArray = jObjData
							.getJSONArray(TAG_REVIEWED_RESTAURANT);
					for (int i = 0; i < restJsonArray.length(); i++) {
						JSONObject listItem = restJsonArray.getJSONObject(i);
						ReviewerRestaurantListItem _item = new ReviewerRestaurantListItem();
						_item.setItemId(listItem.getInt(TAG_ID));
						_item.setItemName(listItem
								.getString(TAG_RESTAURANTNAME));
						_item.setItemLocation(listItem.getString(TAG_LOCATION));
						_item.setItemSpeciality(listItem
								.getString(TAG_SPECIALITY));
						_item.setItemImg(listItem.getString(TAG_IMG));

						restaurantListItems.add(_item);
					}
					restaurantAdapter.notifyDataSetChanged();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			dialog.dismiss();
		}
	}

	private void setTextFont() {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/Aller_Lt.ttf";

		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
		Typeface tf3 = Typeface.createFromAsset(getAssets(), fontPath3);

		tvUserNameReviewerprof.setTypeface(tf1);
		tvUserPlaceReviewerprof.setTypeface(tf3);
		tvUserDesignationReviewerprof.setTypeface(tf3);
		tvNoOfUserReviewReviewerprof.setTypeface(tf2);
		tvNoOfUserPhotoReviewerprof.setTypeface(tf2);
		tvNoOfUserBadgeReviewerprof.setTypeface(tf2);
		textUserReviewReviewerprof.setTypeface(tf3);
		textUserPhotoReviewerprof.setTypeface(tf3);
		textUserBadgeReviewerprof.setTypeface(tf3);
		textSearchReviewerprof.setTypeface(tf3);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public void tryAgain(AsyncTask<String, String, Long> conAsync) {
		conAsync = new AstClassReviewerDetails();
		conAsync.execute("");
		
	}
}
