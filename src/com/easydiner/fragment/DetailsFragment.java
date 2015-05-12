package com.easydiner.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classes.AsyncTaskListener;
import com.classes.CircularImageView;
import com.classes.CommonFunction;
import com.classes.CommonFunction.TimeoutDialogTryAgainListener;
import com.classes.ConnectionDetector;
import com.classes.Constant;
import com.classes.CustomAlertProgressDialog;
import com.classes.EasydinerClass;
import com.classes.ImageCompressCropp;
import com.classes.ImageRoundCropp;
import com.classes.JsonobjectPost;
import com.classes.PostObject;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.activities.DetailsActivity;
import com.easydiner.activities.GalleryActivity;
import com.easydiner.activities.ImageSelectorActivity;
import com.easydiner.activities.ImageZoomGalleryActivity;
import com.easydiner.activities.SignupActivity;
import com.easydiner.activities.UserReviewdetailsActivity;
import com.easydiner.dataobject.UserReviewList;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sromku.simple.fb.Permission.Type;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

@SuppressLint("ClickableViewAccessibility")
public class DetailsFragment extends Fragment implements AnimationListener,
		ConnectionCallbacks, OnConnectionFailedListener ,TimeoutDialogTryAgainListener{

	private RatingBar foodRating;
	private EditText etUserReviewsSubject, etUserReviews;
	private ImageView ivGetCameraStart, ivProfileImg, ivDetailsReviews,
			ivHeaderImage, ivPriceImageDetails, tvUserReviewDetails, btnLike,
			btnShare;
	private TextView tvDetailsName, tvDetailsType, tvFstAddtessDetails,
			tvSndAddtessDetails, tvContactDetails, tvLunchTimeDetails,
			tvDinerTimeDetails, tvPriceDetails, tvCriticReviewDetails,
			tvReviewer, textUserRatingDetails, textTotalRatingDetails,
			textCriticRatingDetails, tvReviewDetails, textEazyDealTag,
			tvEazyDealsDetails, tvInsiderTips, tvReviewCount, tvPhotoCount,
			textInsiderTipsHeading, textMenu, textGallery, textReviewReating,
			tvShareCount, tvLikeCount;
	/* private ImageLoader imageLoader; */
	private ImageLoader imageLoader;
	protected static final int CAMERA_REQUEST = 2;
	protected static final int GALLERY_PICTURE = 1;
	private Intent pictureActionIntent;
	private RelativeLayout rlUserRatingArea, rlEazydealLayout, rlImageGallery,rlPostReviews, rlRestaurantMenu;
	private LinearLayout details_review, llCriticReviewsDetails,llBtnContainer, llHotelState, llUserReviewsArea, llContactDetails;
	private static final int SELECT_PICTURE = 1;
	private String imagePath = "", picturePath = "not in there",userRating = "0.0";
	private Bitmap imgbitmap, roundimgbitmap;
	private ImageCompressCropp objCompressCropp;
	private ImageRoundCropp objRoundCropp;
	private static final String TAG_DATA = "data";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_ITEM_ID = "item_id";
	private static final String TAG_GETCATITEM = "getCatItem";
	private static final String TAG_GETITEM = "getItem";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_SPECIALITY = "speciality";
	private static final String TAG_OPENINGHOUR = "openingHour";
	private static final String TAG_COST = "cost";
	private static final String TAG_REVIEWED_BY = "reviewed_by";
	private static final String TAG_REVIEW = "review";
	private static final String TAG_REVIEWED = "reviewed";
	private static final String TAG_NO_PEOPLE_PRICE = "no_people_price";
	private static final String TAG_CONTACT_NO = "contact_no";
	private static final String TAG_CRITIC_RATING = "critic_rating";
	private static final String TAG_USER_RATING = "user_rating";
	private static final String TAG_REVIEW_NO = "review_no";
	private static final String TAG_POSTED_IMG = "posted_img";
	private static final String TAG_IMG = "img";
	private static final String TAG_SORTORDER = "sortorder";
	private static final String TAG_IMAGE = "image";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_USER_REVIEWS = "user_reviews";
	private static final String TAG_USER_ID = "user_id";
	private static final String TAG_USER_NAME = "user_name";
	private static final String TAG_REVIEW_DATE = "review_date";
	private static final String TAG_USER_USER_IMAGE = "user_image";
	private static final String TAG_USER_REVIEW = "user_review";
	private static final String TAG_EAZY_DEALS = "eazy_deals";
	private static final String TAG_USERID = "userId";
	private static final String TAG_STATUS = "status";
	private static final String TAG_LATITUDE = "latitude";
	private static final String TAG_LONGITUDE = "longitude";
	private static final String TAG_CUST_ID = "user_id";
	private static final String TAG_SUBJECT = "subject";
	private static final String TAG_RESTAURANTID = "restaurantId";
	private static final String TAG_INSIDER_TIP = "insider_tip";
	private static final String TAG_MENU = "menu";
	private static final String TAG_MENUPAGE_URL = "menupage_url";
	private static final String TAG_RESERVABLE = "reservable";
	private static final String TAG_OAUTHDTLS = "oauthDtls";
	private static final String TAG_TYPE = "type";
	private static final String TAG_AUTHID = "authId";
	private static final String TAG_SCLNME = "sclNme";
	private static final String TAG_LOGIN = "login";
	private static final String TAG_ACCESSTOKEN = "accessToken";
	private static final String TAG_USRID = "usrId";
	private static final String TAG_USRNM = "usrNm";
	private static final String TAG_USRIMG = "usrImg";
	private static final String TAG_MEMBERSHIP = "membership";
	private static final String TAG_POINTS = "points";
	private static final String TAG_TOKEN = "token";
	private static final String TAG_EXPIRY = "expiry";

	private static final String TAG_LIKE_COUNT = "like_count";
	private static final String TAG_WISHLIST_COUNT = "wishlist_count";
	private static final String TAG_PHONE = "phone_no";

	private String likeCount, wishlistCount;

	private Dialog dialog;
	private JSONObject jObjList, jsonObject1, jsonObject2, jsonObjectWishLike;
	private String errorCode, errorMsg, restaurantId;
	private List<NameValuePair> nameValuePairs;
	private SharedPreferences _pref;
	private SharedPreferences.Editor _pEditor;
	// private ListView lvUserReviews;
	// private UserReviewsAdapter adpterObj;
	private ArrayList<UserReviewList> arrayList;
	private ArrayList<String> imgList, menuList;
	private Animation sequentialAnimOne, sequentialAnimTwo, smallZoom;
	private int flagLike = 0, flagShare = 0, noOfReviews = 0,noOfPhotos = 0;
	public int reservable;
	private String profImageWhole;
	private CommonFunction _comFunc;
	private HashMap<String, String> userReviewDetails;
	private ConnectionDetector _connectionDetector;
	private String monthArray[] = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN","JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
	private SimpleFacebook simpleFB;
	private AlertDialog dialogAuthLogin;
	private static final int RC_SIGN_IN = 0;
	private GoogleApiClient mGoogleApiClient;
	private boolean mSignInClicked = true;
	private boolean mIntentInProgress;
	private Context con;

	public static DetailsFragment newInstance() {

		DetailsFragment frag = new DetailsFragment();
		return frag;
	}

	@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.details_fragment_layout, container, false);
		
		Tracker t = ((EasydinerClass) getActivity().getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("DetailsFragment");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		con = getActivity();
		setTextFont(rootView);
		foodRating = (RatingBar) rootView.findViewById(R.id.userRating);
		ivGetCameraStart = (ImageView) rootView.findViewById(R.id.ivGetCameraStart);
		ivProfileImg = (ImageView) rootView.findViewById(R.id.ivProfileImg);
		tvUserReviewDetails = (ImageView) rootView.findViewById(R.id.tvUserReviewDetails);
		ivHeaderImage = (ImageView) rootView.findViewById(R.id.ivHeaderImage);
		ivPriceImageDetails = (ImageView) rootView.findViewById(R.id.ivPriceImageDetails);
		rlUserRatingArea = (RelativeLayout) rootView.findViewById(R.id.rlUserRatingArea);
		llUserReviewsArea = (LinearLayout) rootView.findViewById(R.id.llUserReviewsArea);
		details_review = (LinearLayout) rootView.findViewById(R.id.details_review);
		ivDetailsReviews = (ImageView) rootView.findViewById(R.id.ivDetailsReviews);
		rlEazydealLayout = (RelativeLayout) rootView.findViewById(R.id.rlEazydealLayout);
		rlImageGallery = (RelativeLayout) rootView.findViewById(R.id.rlImageGallery);
		rlPostReviews = (RelativeLayout) rootView.findViewById(R.id.rlPostReviews);
		rlRestaurantMenu = (RelativeLayout) rootView.findViewById(R.id.rlRestaurantMenu);
		llCriticReviewsDetails = (LinearLayout) rootView.findViewById(R.id.llCriticReviewsDetails);
		llHotelState = (LinearLayout) rootView.findViewById(R.id.llHotelState);
		llBtnContainer = (LinearLayout) rootView.findViewById(R.id.llBtnContainer);
		llContactDetails = (LinearLayout) rootView.findViewById(R.id.llContactDetails);
		imgList = new ArrayList<String>();
		menuList = new ArrayList<String>();
		btnLike = (ImageView) rootView.findViewById(R.id.btnLike);
		btnShare = (ImageView) rootView.findViewById(R.id.btnShare);
		tvShareCount = (TextView) rootView.findViewById(R.id.tvShareCount);
		tvLikeCount = (TextView) rootView.findViewById(R.id.tvLikeCount);

		/*
		 * zoomOut = AnimationUtils.loadAnimation(getActivity(),
		 * R.anim.zoom_out); zoomIn =
		 * AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
		 */
		sequentialAnimOne = AnimationUtils.loadAnimation(getActivity(),R.anim.sequential_amin);
		sequentialAnimTwo = AnimationUtils.loadAnimation(getActivity(),R.anim.sequential_amin);
		smallZoom = AnimationUtils.loadAnimation(getActivity(),R.anim.small_zoom_out);

		sequentialAnimOne.setAnimationListener(this);
		sequentialAnimTwo.setAnimationListener(this);

		objCompressCropp = new ImageCompressCropp(getActivity());
		objRoundCropp = new ImageRoundCropp();
		_comFunc = new CommonFunction(getActivity());
		// lvUserReviews = (ListView) rootView.findViewById(R.id.lvUserReviews);
		arrayList = new ArrayList<UserReviewList>();
		// adpterObj = new UserReviewsAdapter(getActivity(), arrayList);
		// lvUserReviews.setAdapter(adpterObj);
		_pEditor = new Pref(getActivity()).getSharedPreferencesEditorInstance();
		_pref = new Pref(getActivity()).getSharedPreferencesInstance();
		userReviewDetails = new HashMap<String, String>();
		imageLoader = ImageLoader.getInstance();
		_connectionDetector = new ConnectionDetector(getActivity());

		mGoogleApiClient = new GoogleApiClient.Builder(getActivity()
				.getApplicationContext()).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		final String userReviewsSubjectHint = etUserReviewsSubject.getHint().toString();
		final String userReviewsHint = etUserReviews.getHint().toString();
		/*
		 * ivHeaderImage.setScaleType(ImageView.ScaleType.FIT_XY);
		 * imageLoader.DisplayImage(_pref.getString("itemImage", ""),
		 * ivHeaderImage);
		 */
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		if (_connectionDetector.isConnectingToInternet()) {
			loadDetails();
		} else {
			Toast.makeText(getActivity(), "No internet connection available",Toast.LENGTH_LONG).show();
		}

		foodRating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,float rating, boolean fromUser) {
						userRating = Float.toString(rating);
					}
				});
		etUserReviewsSubject.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				etUserReviewsSubject.setHint("");
				etUserReviews.setHint(userReviewsHint);
				return false;
			}
		});
		etUserReviews.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				etUserReviews.setHint("");
				etUserReviewsSubject.setHint(userReviewsSubjectHint);
				return false;
			}
		});
		llContactDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phoneNumber = tvContactDetails.getText().toString();
				Intent dial = new Intent();
				dial.setAction("android.intent.action.DIAL");
				dial.setData(Uri.parse("tel:" + phoneNumber));
				startActivity(dial);
			}
		});
		ivGetCameraStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_pEditor.putString("postRevSubject", etUserReviewsSubject.getText().toString());
				_pEditor.putString("postRev", etUserReviews.getText().toString());
				_pEditor.commit();
				startDialog();
			}
		});

		btnLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (_pref.getString("accessToken", "").equalsIgnoreCase("")) {
					showRemainderPopup();
				} else {
					flagLike = 1;
					flagShare = 0;
					int lkCnt = Integer.parseInt(tvLikeCount.getText().toString());
					if (_pref.getString("like_" + restaurantId, "").equalsIgnoreCase("0")) {
						_pEditor.putString("like_" + restaurantId, "1");
						lkCnt++;
					} else {
						_pEditor.putString("like_" + restaurantId, "0");
						lkCnt--;
					}
					tvLikeCount.setText(Integer.toString(lkCnt));
					_pEditor.commit();
					btnLike.startAnimation(sequentialAnimOne);

					jsonObjectWishLike = new JSONObject();
					JSONObject jsonObject2 = new JSONObject();

					try {
						jsonObject2.put(TAG_ACCESSTOKN,_pref.getString("accessToken", ""));
						jsonObject2.put(TAG_RESTAURANTID, restaurantId);
						jsonObject2.put(TAG_USERID,_pref.getString("membershipNo", ""));
						jsonObject2.put(TAG_STATUS,_pref.getString("like_" + restaurantId, ""));
						jsonObjectWishLike.put(TAG_GETITEM, jsonObject2);

						Log.v("input data", jsonObject2.toString());

						AstClassSetLikeWish astClassSetLikeWish = new AstClassSetLikeWish();
						astClassSetLikeWish.execute("");

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});

		btnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (_pref.getString("accessToken", "").equalsIgnoreCase("")) {
					showRemainderPopup();
				} else {
					flagLike = 0;
					flagShare = 1;
					int wsklstCnt = Integer.parseInt(tvShareCount.getText().toString());
					if (_pref.getString("share_" + restaurantId, "").equalsIgnoreCase("0")) {
						_pEditor.putString("share_" + restaurantId, "1");
						wsklstCnt++;
					} else {
						_pEditor.putString("share_" + restaurantId, "0");
						wsklstCnt--;
					}
					tvShareCount.setText(Integer.toString(wsklstCnt));
					_pEditor.commit();
					btnShare.startAnimation(sequentialAnimTwo);

					jsonObjectWishLike = new JSONObject();
					JSONObject jsonObject2 = new JSONObject();

					try {
						jsonObject2.put(TAG_ACCESSTOKN,_pref.getString("accessToken", ""));
						jsonObject2.put(TAG_RESTAURANTID, restaurantId);
						jsonObject2.put(TAG_USERID,_pref.getString("membershipNo", ""));
						jsonObject2.put(TAG_STATUS,_pref.getString("share_" + restaurantId, ""));
						jsonObjectWishLike.put(TAG_GETITEM, jsonObject2);

						Log.v("input data", jsonObjectWishLike.toString());

						AstClassSetLikeWish astClassSetLikeWish = new AstClassSetLikeWish();
						astClassSetLikeWish.execute("");

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});

		rlPostReviews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (_pref.getString("accessToken", "").equalsIgnoreCase("")) {
					showRemainderPopup();
				} else {
					HashMap<String, PostObject> _postMap = new HashMap<String, PostObject>();

					if (etUserReviewsSubject.getText().toString().equalsIgnoreCase("")) {
						Toast.makeText(getActivity(),"Please give the subject of post",Toast.LENGTH_LONG).show();
					} else if (userRating.equals("0.0")) {
						Toast.makeText(getActivity(),"Please give rating for post",Toast.LENGTH_LONG).show();
					} else if (etUserReviews.getText().toString().equalsIgnoreCase("")) {
						Toast.makeText(getActivity(),"Please give the content of post",Toast.LENGTH_LONG).show();
					} else {

						_postMap.put("subject", _comFunc.getPostObject(etUserReviewsSubject.getText().toString(),false));

						_postMap.put("review", _comFunc.getPostObject(etUserReviews.getText().toString(), false));

						_postMap.put("acessToken", _comFunc.getPostObject(_pref.getString("accessToken", ""), false));

						_postMap.put("userId", _comFunc.getPostObject(_pref.getString("membershipNo", ""), false));

						_postMap.put("myRating",_comFunc.getPostObject(userRating, false));

						_postMap.put("restaurantId",_comFunc.getPostObject(restaurantId, false));

						_postMap.put("image_no", _comFunc.getPostObject(String.valueOf(Constant.imgSelected.size()),false));

						for (int i = 0; i < Constant.imgSelected.size(); i++) {
							_postMap.put("image." + i,_comFunc.getPostObject(Constant.imgSelected.get(i), true));
						}

						/*
						 * _comFunc.callPostWebservice(
						 * "http://205.147.110.243/admin_dev/index.php/alluser/testImage"
						 * , _postMap, _profileChangeAsync, true);
						 */
						_comFunc.callPostWebservice(Constant.BASE_URL+ "PostUserReviews", _postMap,_profileChangeAsync, true);
					}
				}
			}
		});

		rlRestaurantMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),ImageZoomGalleryActivity.class);
				i.putStringArrayListExtra("manuImage", menuList);
				i.putExtra("rest_id", restaurantId);
				i.putExtra("isMenu", true);
				startActivity(i);
			}
		});

		rlImageGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(getActivity(), GalleryActivity.class);
				i.putStringArrayListExtra("gallery_image", imgList);
				i.putExtra("rest_id", restaurantId);
				i.putExtra("isMenu", false);
				startActivity(i);
			}
		});

		return rootView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
	}

	private void startDialog() {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
		myAlertDialog.setTitle("Upload Pictures Option");
		myAlertDialog.setMessage("How do you want to set your picture?");

		myAlertDialog.setPositiveButton("Gallery",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

						Intent intent = new Intent(getActivity(),ImageSelectorActivity.class);
						startActivity(intent);
					}
				});

		myAlertDialog.setNegativeButton("Camera",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						pictureActionIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(pictureActionIntent,CAMERA_REQUEST);
					}
				});
		myAlertDialog.show();
	}

	private void showRemainderPopup() {
		dialog = new Dialog(getActivity());
		
		Display display = getActivity().getWindowManager().getDefaultDisplay(); 
		int width = display.getWidth();  // deprecated
		int height = display.getHeight();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.remainder_popup_layout);
		
//		dialog.setTitle(R.id.dialog_title);
		dialog.getWindow().setLayout((width*4)/5, (height*2)/4);
		LinearLayout llCancelPopup = (LinearLayout) dialog.findViewById(R.id.llCancelPopup);
		ImageView ivPopupReminderFbLogin = (ImageView) dialog.findViewById(R.id.ivPopupReminderFbLogin);
		ImageView ivPopupReminderGpLogin = (ImageView) dialog.findViewById(R.id.ivPopupReminderGpLogin);
		ImageView ivPopupReminderGenLogin = (ImageView) dialog.findViewById(R.id.ivPopupReminderGenLogin);

		llCancelPopup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		ivPopupReminderFbLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogAuthLogin = CustomAlertProgressDialog.getCustomDialog(getActivity(),"Please wait...");
				dialogAuthLogin.show();
				simpleFB.login(onLoginListener);
			}
		});

		ivPopupReminderGpLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mSignInClicked = true;
				dialogAuthLogin = CustomAlertProgressDialog.getCustomDialog(getActivity(),"Please wait...");
				dialogAuthLogin.show();
				mGoogleApiClient.connect();

			}
		});

		ivPopupReminderGenLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), SignupActivity.class));
				getActivity().overridePendingTransition(R.anim.slide_up_in,
						R.anim.slide_down_out);
			}
		});

		dialog.show();
	}

	private void loadDetails() {
		try {
			Log.v("details item id", _pref.getString("itemId", ""));
			jsonObject2 = new JSONObject();
			jsonObject1 = new JSONObject();
			jsonObject2.put(TAG_ACCESSTOKN, "");
			jsonObject2.put(TAG_USERID, _pref.getString("membershipNo", ""));
			jsonObject2.put(TAG_ITEM_ID, _pref.getString("itemId", ""));
			jsonObject1.put(TAG_GETITEM, jsonObject2);

			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("data", jsonObject1.toString()));
			ListAstClass listAstClass = new ListAstClass();
			listAstClass.execute("");
		} catch (Exception e) {
			Log.e("error", e.toString());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		simpleFB = SimpleFacebook.getInstance(getActivity());
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.e("requestCode", String.valueOf(requestCode));
		Log.e("resultCode", String.valueOf(resultCode));
		Log.e("data", String.valueOf(data));

		simpleFB.onActivityResult(getActivity(), requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}

		if (requestCode == GALLERY_PICTURE && resultCode == getActivity().RESULT_OK && data != null) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);

			Log.e("img path", picturePath);

			imgbitmap = objCompressCropp.compressImageForPath(picturePath);

			roundimgbitmap = objRoundCropp.getCroppedBitmap(imgbitmap);

			ivProfileImg.setImageBitmap(roundimgbitmap);
		} else if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
			Bitmap camBitmap = (Bitmap) data.getExtras().get("data");

			Uri uriImgPath = objCompressCropp.getImageUri(camBitmap);

			Log.e("uriImgPath", uriImgPath.toString());

			picturePath = objCompressCropp.getRealPathFromURI(uriImgPath);

			Log.e("img path", picturePath);

			imgbitmap = objCompressCropp.compressImageForPath(picturePath);

			Constant.imgSelected.clear();

			Constant.imgSelected.add(objCompressCropp.getImageFilePath());
		}
	}

	OnLoginListener onLoginListener = new OnLoginListener() {

		@Override
		public void onFail(String reason) {
			dialogAuthLogin.dismiss();
		}

		@Override
		public void onException(Throwable throwable) {
			dialogAuthLogin.dismiss();
		}

		@Override
		public void onThinking() {
		}

		@Override
		public void onNotAcceptingPermissions(Type type) {
			dialogAuthLogin.dismiss();
		}

		@Override
		public void onLogin() {
			simpleFB.getProfile(new OnProfileListener() {
				public void onFail(String reason) {
					dialogAuthLogin.dismiss();
				};

				public void onException(Throwable throwable) {
					dialogAuthLogin.dismiss();
				};

				public void onThinking() {

				};

				public void onComplete(
						com.sromku.simple.fb.entities.Profile response) {
					jsonObject1 = new JSONObject();
					jsonObject2 = new JSONObject();
					try {
						jsonObject2.put(TAG_EMAIL, response.getEmail());
						jsonObject2.put(TAG_TYPE, "fb");
						jsonObject2.put(TAG_AUTHID, response.getId());
						jsonObject2.put(TAG_USRIMG,"http://graph.facebook.com/" + response.getId()+ "/picture?type=large");
						jsonObject2.put(TAG_SCLNME, response.getName());
						jsonObject1.put(TAG_OAUTHDTLS, jsonObject2);
						AstClassOuthLogin astClassOuthLogin = new AstClassOuthLogin();
						astClassOuthLogin.execute("");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				};
			});
		}
	};

	private class ListAstClass extends AsyncTask<String, String, Long> {
		private AlertDialog dialog;

		public ListAstClass() {
			dialog = CustomAlertProgressDialog.getCustomDialog(getActivity(),"Please wait...");
			dialog.show();
		}

		@SuppressWarnings("static-access")
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {

				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL+ "getitemdetail", nameValuePairs,jsonObject1.toString());

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

			JSONObject objData, listItem;
			JSONArray userReviewArray, imgArray, menuArray;
			try {
				objData = jObjList.getJSONObject(TAG_DATA);
				JSONObject errorObj = jObjList.getJSONObject(TAG_ERNODE);
				errorCode = errorObj.getString(TAG_ERCODE);
				errorMsg = errorObj.getString(TAG_ERMSG);
				if (errorCode.equalsIgnoreCase("0")) {

					listItem = objData.getJSONObject(TAG_GETITEM);
					restaurantId = listItem.getString(TAG_ID);
					imgArray = listItem.getJSONArray(TAG_IMG);
					menuArray = listItem.getJSONArray(TAG_MENU);

					likeCount = listItem.getString(TAG_LIKE_COUNT);
					tvLikeCount.setText(likeCount);
					Log.d("MyTestLike", likeCount);
					wishlistCount = listItem.getString(TAG_WISHLIST_COUNT);
					tvShareCount.setText(wishlistCount);
					Log.d("MyTestWish", wishlistCount);

					noOfPhotos = imgArray.length();

					for (int imgCount = 0; imgCount < imgArray.length(); imgCount++) {
						JSONObject _jsonitem = imgArray.getJSONObject(imgCount);
						String imgUrl = _jsonitem.getString(TAG_IMAGE).replaceAll(" ", "%20");
						imgList.add(imgUrl);
					}

					if (menuArray.length() > 0) {
						rlRestaurantMenu.setVisibility(View.VISIBLE);
						for (int imgCount = 0; imgCount < menuArray.length(); imgCount++) {
							JSONObject _jsonitem = menuArray.getJSONObject(imgCount);
							String imgUrl = _jsonitem.getString(TAG_MENUPAGE_URL).replaceAll(" ", "%20");
							menuList.add(imgUrl);
						}
					} else {
						rlRestaurantMenu.setVisibility(View.INVISIBLE);
					}
					tvDetailsName.setText(listItem.getString(TAG_NAME));
					tvDetailsType.setText(listItem.getString(TAG_SPECIALITY));
					Log.v("speciality", listItem.getString(TAG_SPECIALITY));
					if (listItem.getBoolean(TAG_REVIEWED)) {
						llCriticReviewsDetails.setVisibility(View.VISIBLE);
					} else {
						llCriticReviewsDetails.setVisibility(View.INVISIBLE);
						llHotelState.setBackgroundColor(Color.parseColor("#E6E6E6"));
						llBtnContainer.setBackgroundColor(Color.parseColor("#E6E6E6"));
						details_review.setBackgroundColor(Color.parseColor("#E6E6E6"));
						rlImageGallery.setBackgroundResource(R.drawable.menu_image_grey);
						rlRestaurantMenu.setBackgroundResource(R.drawable.menu_image_grey);
					}
					reservable = listItem.getInt(TAG_RESERVABLE);

					if (reservable == 1) {
						((DetailsActivity) getActivity()).restaurantName = listItem.getString(TAG_NAME);
						((DetailsActivity) getActivity()).restaurantId = Integer.parseInt(listItem.getString(TAG_ID));
						if (!listItem.getString(TAG_EAZY_DEALS).equalsIgnoreCase("")) {
							((DetailsActivity) getActivity()).restaurantDeals = listItem.getString(TAG_EAZY_DEALS);
						} else {
							((DetailsActivity) getActivity()).restaurantDeals = "";
						}
					} else {
						RelativeLayout rlDetailBookNow = (RelativeLayout) getActivity().findViewById(R.id.RlDetailBookNow);
						rlDetailBookNow.setBackgroundColor(Color.parseColor("#9B9A9A"));
						rlDetailBookNow.setClickable(false);
						((DetailsActivity) getActivity()).restaurantName = "";
						((DetailsActivity) getActivity()).restaurantId = 0;
						if (!listItem.getString(TAG_EAZY_DEALS).equalsIgnoreCase("")) {
							((DetailsActivity) getActivity()).restaurantDeals = listItem.getString(TAG_EAZY_DEALS);
						} else {
							((DetailsActivity) getActivity()).restaurantDeals = "";
						}
					}

					String addressArray[] = listItem.getString(TAG_ADDRESS).split(",");
					if (!listItem.getString(TAG_EAZY_DEALS).equalsIgnoreCase("")) {
						tvEazyDealsDetails.setText("EAZYDEAL: "+ listItem.getString(TAG_EAZY_DEALS));
						rlEazydealLayout.setVisibility(View.VISIBLE);
					}
					/*
					 * tvFstAddtessDetails.setText(addressArray[0] + "," +
					 * addressArray[1] + ",");
					 */
					tvFstAddtessDetails.setText(listItem.getString(TAG_ADDRESS));
					if (addressArray.length > 3) {
						tvSndAddtessDetails.setText(addressArray[2] + ","+ addressArray[3]);
					} else {
						tvSndAddtessDetails.setText(addressArray[2] + ",");
					}
					tvContactDetails.setText(listItem.getString(TAG_CONTACT_NO));
					String timeArray[] = listItem.getString(TAG_OPENINGHOUR).split(",");
					tvLunchTimeDetails.setText("Lunch: " + timeArray[0]);
					if (timeArray.length > 1) {
						tvDinerTimeDetails.setText("Dinner: " + timeArray[1]);
					} else {
						tvDinerTimeDetails.setVisibility(View.GONE);
					}
					int person_no = Integer.parseInt(listItem.getString(TAG_NO_PEOPLE_PRICE));
					String person_str = "";
					switch (person_no) {
					case 1:
						person_str = "For One: Rs.";
						break;

					case 2:
						person_str = "For Two: Rs.";
						break;

					case 3:
						person_str = "For Three: Rs.";
						break;

					case 4:
						person_str = "For Four: Rs.";
						break;

					default:
						person_str = "For Five Or More Preson: Rs.";
						break;
					}

					if (Integer.parseInt(listItem.getString(TAG_COST)) <= 500) {
						ivPriceImageDetails.setImageResource(R.drawable.price_1);
					} else if (Integer.parseInt(listItem.getString(TAG_COST)) <= 2000
							&& Integer.parseInt(listItem.getString(TAG_COST)) >= 501) {
						ivPriceImageDetails.setImageResource(R.drawable.price_2);
					} else if (Integer.parseInt(listItem.getString(TAG_COST)) <= 6000
							&& Integer.parseInt(listItem.getString(TAG_COST)) >= 2001) {
						ivPriceImageDetails.setImageResource(R.drawable.price_3);
					} else {
						ivPriceImageDetails.setImageResource(R.drawable.price_4);
					}

					tvPriceDetails.setText(person_str+ listItem.getString(TAG_COST));
					tvCriticReviewDetails.setText(listItem.getString(TAG_CRITIC_RATING));

					ivHeaderImage.setScaleType(ImageView.ScaleType.FIT_XY);

					String insider_tip = listItem.getString(TAG_INSIDER_TIP).replace("</li><li>", "\r\n\r\u2022 ");
					insider_tip = "\u2022 " + insider_tip;

					tvInsiderTips.setText(insider_tip);

					@SuppressWarnings("deprecation")
					DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
							.showImageForEmptyUri(R.drawable.default_image_details)
							.showImageOnFail(R.drawable.default_image_details).showImageOnLoading(
									R.drawable.default_image_details).resetViewBeforeLoading(true).build();
					String restaurantImg = listItem.getString(TAG_IMAGE).replaceAll(" ", "%20");
					imageLoader.displayImage(restaurantImg, ivHeaderImage,options);

					_pEditor.putString("restaurent_lat",listItem.getString(TAG_LATITUDE));
					_pEditor.putString("restaurent_long",listItem.getString(TAG_LONGITUDE));
					_pEditor.putString("restaurent_name",listItem.getString(TAG_NAME));
					_pEditor.putString("restaurent_type",listItem.getString(TAG_SPECIALITY));
					_pEditor.putString("restaurent_location",listItem.getString(TAG_ADDRESS));

					_pEditor.commit();

					String critisName[] = listItem.getString(TAG_REVIEWED_BY).split(" ");
					if (critisName.length > 1) {
						tvReviewer.setText(critisName[0] + "\'s Verdict");
					} else {
						tvReviewer.setText(critisName[0] + " Speaks");
					}
					tvReviewDetails.setText(listItem.getString(TAG_REVIEW));

					float rating = Float.parseFloat(listItem.getString(TAG_USER_RATING));
					int ratingInt = (int) rating;
					switch (ratingInt) {
					case 0:
						tvUserReviewDetails.setImageResource(R.drawable.zero_star);
						break;

					case 1:
						tvUserReviewDetails.setImageResource(R.drawable.one_star);
						break;

					case 2:
						tvUserReviewDetails.setImageResource(R.drawable.two_star);
						break;

					case 3:
						tvUserReviewDetails.setImageResource(R.drawable.three_star);
						break;

					case 4:
						tvUserReviewDetails.setImageResource(R.drawable.four_star);
						break;

					case 5:
						tvUserReviewDetails.setImageResource(R.drawable.five_star);
						break;

					default:
						break;
					}

					userReviewArray = listItem.getJSONArray(TAG_USER_REVIEWS);

					if (userReviewArray.length() > 0) {
						noOfReviews = userReviewArray.length();
						rlUserRatingArea.setVisibility(View.VISIBLE);
						llUserReviewsArea.setVisibility(View.VISIBLE);
						for (int k = 0; k < userReviewArray.length(); k++) {
							JSONObject objReview = userReviewArray.getJSONObject(k);
							UserReviewList _item = new UserReviewList();
							_item.setUserId(objReview.getInt(TAG_CUST_ID));
							_item.setReviewSubject(objReview.getString(TAG_SUBJECT));
							_item.setAddress(objReview.getString(TAG_ADDRESS));
							_item.setReviewOn(objReview.getInt(TAG_REVIEW_NO));
							_item.setUserName(objReview.getString(TAG_USER_NAME));
							_item.setUserImage(objReview.getString(TAG_USER_USER_IMAGE));
							_item.setUserRating(objReview.getInt(TAG_USER_RATING));
							_item.setUserReviews(objReview.getString(TAG_REVIEW));
							_item.setReviewDate(objReview.getString(TAG_REVIEW_DATE));

							arrayList.add(_item);
						}

						// adpterObj.notifyDataSetChanged();

					}

					if (_pref.getBoolean("itemReviewed", true) && !listItem.getString(TAG_REVIEW).equalsIgnoreCase("")) {
						details_review.setVisibility(View.VISIBLE);
						ivDetailsReviews.setVisibility(View.VISIBLE);
					}

					if (_pref.getString("like_" + restaurantId, "").equalsIgnoreCase(null) || _pref.getString("like_" + restaurantId, "")
									.equalsIgnoreCase("") || _pref.getString("like_" + restaurantId, "").equalsIgnoreCase("0")) {
						Log.v("like", "0, " + restaurantId);
						btnLike.setImageResource(R.drawable.like_icon);
						_pEditor.putString("like_" + restaurantId, "0");
						_pEditor.commit();
					} else {
						Log.v("like", "1, " + restaurantId);
						btnLike.setImageResource(R.drawable.like_icon_hover);
						_pEditor.putString("like_" + restaurantId, "1");
						_pEditor.commit();
					}

					if (_pref.getString("share_" + restaurantId, "").equalsIgnoreCase(null) || _pref.getString("share_" + restaurantId, "")
									.equalsIgnoreCase("") || _pref.getString("share_" + restaurantId, "").equalsIgnoreCase("0")) {
						Log.v("share", "0, " + restaurantId);
						btnShare.setImageResource(R.drawable.like_star);
						_pEditor.putString("share_" + restaurantId, "0");
						_pEditor.commit();
					} else {
						Log.v("share", "1, " + restaurantId);
						btnShare.setImageResource(R.drawable.like_star_hover);
						_pEditor.putString("share_" + restaurantId, "1");
						_pEditor.commit();
					}
					if (noOfReviews == 1) {

						tvReviewCount.setText("1 User Review");

					} else {
						tvReviewCount.setText(String.valueOf(noOfReviews)+ " User Reviews");
					}
					tvPhotoCount.setText(String.valueOf(noOfPhotos) + " Photos");
					if (arrayList.size() > 0) {
						llUserReviewsArea.setVisibility(View.VISIBLE);
						loadUserReviewList();
					} else {
						llUserReviewsArea.setVisibility(View.GONE);
					}

				} else {
					Toast.makeText(getActivity(), "No Item Available",Toast.LENGTH_LONG).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			dialog.dismiss();
		}
	}

	private void setListFont(TextView tvView) {
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(),fontPath2);
		tvView.setTypeface(tf1);
	}

	private void setTextFont(View rootView) {
		tvDetailsName = (TextView) rootView.findViewById(R.id.tvDetailsName);
		tvDetailsType = (TextView) rootView.findViewById(R.id.tvDetailsType);
		tvFstAddtessDetails = (TextView) rootView.findViewById(R.id.tvFstAddtessDetails);
		tvSndAddtessDetails = (TextView) rootView.findViewById(R.id.tvSndAddtessDetails);
		tvContactDetails = (TextView) rootView.findViewById(R.id.tvContactDetails);
		tvLunchTimeDetails = (TextView) rootView.findViewById(R.id.tvLunchTimeDetails);
		tvDinerTimeDetails = (TextView) rootView.findViewById(R.id.tvDinerTimeDetails);
		tvPriceDetails = (TextView) rootView.findViewById(R.id.tvPriceDetails);
		tvCriticReviewDetails = (TextView) rootView.findViewById(R.id.tvCriticReviewDetails);
		tvReviewer = (TextView) rootView.findViewById(R.id.tvReviewer);
		tvReviewDetails = (TextView) rootView.findViewById(R.id.tvReviewDetails);
		textUserRatingDetails = (TextView) rootView.findViewById(R.id.textUserRatingDetails);
		textTotalRatingDetails = (TextView) rootView.findViewById(R.id.textTotalRatingDetails);
		textCriticRatingDetails = (TextView) rootView.findViewById(R.id.textCriticRatingDetails);
		textEazyDealTag = (TextView) rootView.findViewById(R.id.textEazyDealTag);

		tvEazyDealsDetails = (TextView) rootView.findViewById(R.id.tvEazyDealsDetails);
		tvInsiderTips = (TextView) rootView.findViewById(R.id.tvInsiderTips);
		textInsiderTipsHeading = (TextView) rootView.findViewById(R.id.textInsiderTipsHeading);
		tvReviewCount = (TextView) rootView.findViewById(R.id.tvReviewCount);
		tvPhotoCount = (TextView) rootView.findViewById(R.id.tvPhotoCount);
		textMenu = (TextView) rootView.findViewById(R.id.textMenu);
		textGallery = (TextView) rootView.findViewById(R.id.textGallery);
		textReviewReating = (TextView) rootView.findViewById(R.id.textReviewReating);
		etUserReviewsSubject = (EditText) rootView.findViewById(R.id.etUserReviewsSubject);
		etUserReviews = (EditText) rootView.findViewById(R.id.etUserReviews);

		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/avenir-light.ttf";
		Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(),fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(),fontPath2);
		Typeface tf3 = Typeface.createFromAsset(getActivity().getAssets(),fontPath3);

		tvDetailsName.setTypeface(tf1);
		tvDetailsType.setTypeface(tf2);
		tvFstAddtessDetails.setTypeface(tf2);
		tvSndAddtessDetails.setTypeface(tf2);
		tvContactDetails.setTypeface(tf2);
		tvLunchTimeDetails.setTypeface(tf2);
		tvDinerTimeDetails.setTypeface(tf2);
		tvPriceDetails.setTypeface(tf2);
		tvReviewer.setTypeface(tf1);
		tvReviewDetails.setTypeface(tf2);
		tvCriticReviewDetails.setTypeface(tf3);
		textUserRatingDetails.setTypeface(tf2);
		textTotalRatingDetails.setTypeface(tf3);
		textCriticRatingDetails.setTypeface(tf2);
		textEazyDealTag.setTypeface(tf2);
		tvEazyDealsDetails.setTypeface(tf2);
		tvInsiderTips.setTypeface(tf2);
		textInsiderTipsHeading.setTypeface(tf1);
		tvReviewCount.setTypeface(tf2);
		tvPhotoCount.setTypeface(tf2);
		textMenu.setTypeface(tf2);
		textGallery.setTypeface(tf2);
		etUserReviewsSubject.setTypeface(tf2);
		etUserReviews.setTypeface(tf2);
		textReviewReating.setTypeface(tf2);
	}

	private void loadUserReviewList() {
		for (int i = 0; i < arrayList.size(); i++) {
			final UserReviewList userReviewList = arrayList.get(i);
			Activity myActivity = getActivity();
			if(myActivity==null) return;
			LinearLayout parentLayout = new LinearLayout(getActivity());
			parentLayout.setOrientation(LinearLayout.VERTICAL);
			parentLayout.setPadding(25, 25, 25, 25);

			LayoutParams linLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			LinearLayout childOneLayout = new LinearLayout(getActivity());
			childOneLayout.setOrientation(LinearLayout.HORIZONTAL);
			LayoutParams childOneLayoutParam = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

			final CircularImageView userImg = new CircularImageView(getActivity());
			// userImg.setImageResource(R.drawable.default_image_restaurant);

			DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
					.showImageOnLoading(R.drawable.default_image_for_circuler)
					.showImageOnFail(R.drawable.default_image_for_circuler)
					.showImageForEmptyUri(R.drawable.default_image_for_circuler)
					.delayBeforeLoading(100).build();
			imageLoader.displayImage(userReviewList.getUserImage(), userImg,options, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							Animation anim = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
							userImg.setAnimation(anim);
							anim.start();
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,FailReason failReason) {

						}

						@Override
						public void onLoadingComplete(String imageUri,View view, Bitmap loadedImage) {

						}

						@Override
						public void onLoadingCancelled(String imageUri,View view) {

						}
					});

			LayoutParams circuterLayoutParam = new LayoutParams(120, 120);
//			childOneLayout.addView(userImg, circuterLayoutParam);

			LinearLayout childThreeLayout = new LinearLayout(getActivity());
			childThreeLayout.setOrientation(LinearLayout.VERTICAL);
			childThreeLayout.setPadding(0, 25, 0, 0);
			LayoutParams childThreeLayoutParam = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

			TextView tvReviewSubject = new TextView(getActivity());
			setListFont(tvReviewSubject);
			tvReviewSubject.setTextColor(Color.parseColor("#000000"));
			tvReviewSubject.setTextSize((float) 15.0);
			tvReviewSubject.setText("\"" + userReviewList.getReviewSubject()
					+ "\"");
			childThreeLayout.addView(tvReviewSubject);

			LinearLayout childFourLayout = new LinearLayout(getActivity());
			childFourLayout.setOrientation(LinearLayout.HORIZONTAL);
			childFourLayout.setPadding(0, 10, 0, 0);
			LayoutParams childFourLayoutParam = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			ImageView userRatingImg = new ImageView(getActivity());
			switch (userReviewList.getUserRating()) {
			case 0:
				userRatingImg.setImageResource(R.drawable.zero_star);
				break;

			case 1:
				userRatingImg.setImageResource(R.drawable.one_star);
				break;

			case 2:
				userRatingImg.setImageResource(R.drawable.two_star);
				break;

			case 3:
				userRatingImg.setImageResource(R.drawable.three_star);
				break;

			case 4:
				userRatingImg.setImageResource(R.drawable.four_star);
				break;

			case 5:
				userRatingImg.setImageResource(R.drawable.five_star);
				break;

			default:
				break;
			}
			userRatingImg.setPadding(0, 5, 0, 0);
			childFourLayout.addView(userRatingImg);

			TextView tvReviewDate = new TextView(getActivity());
			setListFont(tvReviewDate);
			String arrDate[] = userReviewList.getReviewDate().split("-");
			String arrPertiDate[] = arrDate[2].split(" ");
			tvReviewDate.setText(arrPertiDate[0] + " "
					+ monthArray[(Integer.parseInt(arrDate[1]) - 1)] + " "
					+ arrDate[0]);
			tvReviewDate.setTextSize((float) 12.0);
			tvReviewDate.setPadding(10, 0, 0, 0);
			childFourLayout.addView(tvReviewDate);

			childThreeLayout.addView(childFourLayout, childFourLayoutParam);
			childOneLayout.addView(childThreeLayout, childThreeLayoutParam);
			parentLayout.addView(childOneLayout, childOneLayoutParam);

			LinearLayout childTwoLayout = new LinearLayout(getActivity());
			childTwoLayout.setOrientation(LinearLayout.HORIZONTAL);
			LayoutParams childTwoLayoutParam = new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

			TextView tvReview = new TextView(getActivity());
			setListFont(tvReview);
			String userReview = userReviewList.getUserReviews();

			if (userReviewList.getUserReviews().length() > 100) {
				userReview = userReview.substring(0, 90);
				userReview += "..>>";
			}
			tvReview.setText(userReview);
			tvReview.setTextColor(Color.parseColor("#000000"));
			tvReview.setTextSize((float) 12.0);

			childTwoLayout.addView(tvReview);
			parentLayout.addView(childTwoLayout, childTwoLayoutParam);

			parentLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(),
							UserReviewdetailsActivity.class);
					intent.putExtra("user_image", userReviewList.getUserImage());
					intent.putExtra("user_name", userReviewList.getUserName());
					intent.putExtra("user_address", userReviewList.getAddress());
					intent.putExtra("user_heading",userReviewList.getReviewSubject());
					intent.putExtra("user_review_no",userReviewList.getReviewOn());
					intent.putExtra("user_rating",userReviewList.getUserRating());
					intent.putExtra("user_reviewdate",userReviewList.getReviewDate());
					intent.putExtra("user_review",userReviewList.getUserReviews());
					intent.putExtra("user_id", userReviewList.getUserId());
					startActivity(intent);

					getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
			});

			llUserReviewsArea.addView(parentLayout, linLayoutParam);

			FrameLayout borderLayout = new FrameLayout(getActivity());
			borderLayout.setBackgroundColor(Color.BLACK);
			LayoutParams borderLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, 1);
			llUserReviewsArea.addView(borderLayout, borderLayoutParams);
		}
	}

	@Override
	public void onAnimationStart(Animation animation) {
		Log.v("amin start", "amin start");
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if (flagLike == 1) {
			if (_pref.getString("like_" + restaurantId, "").equalsIgnoreCase("1")) {
				btnLike.setImageResource(R.drawable.like_icon_hover);
			} else {
				btnLike.setImageResource(R.drawable.like_icon);
			}
			btnLike.startAnimation(smallZoom);
		}
		if (flagShare == 1) {
			if (_pref.getString("share_" + restaurantId, "").equalsIgnoreCase("1")) {
				btnShare.setImageResource(R.drawable.like_star_hover);
			} else {
				btnShare.setImageResource(R.drawable.like_star);
			}
			btnShare.startAnimation(smallZoom);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	private class AstClassSetLikeWish extends AsyncTask<String, String, Long> {

		@SuppressWarnings("static-access")
		@Override
		protected Long doInBackground(String... params) {
			try {
				JsonobjectPost j_parser = new JsonobjectPost();
				if (flagLike == 1) {
					jObjList = j_parser.getJSONObj(Constant.BASE_URL+ "setLikeRestaurant", nameValuePairs,jsonObjectWishLike.toString());
					Log.v("Like", "Like");
				} else if (flagShare == 1) {
					jObjList = j_parser.getJSONObj(Constant.BASE_URL+ "setWishlistRestaurant", nameValuePairs,jsonObjectWishLike.toString());
					Log.v("Wish", "Wish");
				}

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {

		}
	}

	AsyncTaskListener _profileChangeAsync = new AsyncTaskListener() {

		private AlertDialog pDialog;

		@Override
		public void onTaskPreExecute() {
			// TODO Auto-generated method stub

			pDialog = CustomAlertProgressDialog.getCustomDialog(getActivity(),"Please wait...");
			pDialog.show();

		}

		@Override
		public void onTaskCompleted(String result) {
			// TODO Auto-generated method stub
			pDialog.dismiss();
			Log.e("img_data", result);
			_pEditor.putString("postRevSubject", "");
			_pEditor.putString("postRev", "");
			_pEditor.commit();
			try {
				JSONObject jsonResult = new JSONObject(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			etUserReviewsSubject.setText("");
			etUserReviewsSubject.setHint("Title");
			etUserReviews.setText("");
			etUserReviews.setHint("Write your review..");
			foodRating.setRating(0);
			Constant.imgSelected.clear();

			Toast.makeText(getActivity(),"Thank you for your review. Your review will be visible soon!!",Toast.LENGTH_LONG).show();
		}
	};

	private class AstClassOuthLogin extends AsyncTask<String, String, Long> {

		@Override
		protected Long doInBackground(String... params) {
			try {
				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL + "oauthchkreg",nameValuePairs, jsonObject1.toString(),con,this);

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			dialogAuthLogin.dismiss();
			if (jObjList.toString() != null) {

				try {
					JSONObject objData = jObjList.getJSONObject(TAG_DATA);
					JSONObject objLogin = objData.getJSONObject(TAG_LOGIN);
					JSONObject objAccessToken = objData.getJSONObject(TAG_ACCESSTOKEN);
					JSONObject objErr = jObjList.getJSONObject(TAG_ERNODE);
					if (objErr.getString(TAG_ERCODE).equalsIgnoreCase("0")) {
						if (objLogin.getString("success").equalsIgnoreCase("true")) {
							_pEditor.putString("userName",objLogin.getString(TAG_USRNM));
							_pEditor.putString("userId",objLogin.getString(TAG_USRID));
							_pEditor.putString("userImg",objLogin.getString(TAG_USRIMG));
							_pEditor.putString("accessToken",objAccessToken.getString(TAG_TOKEN));
							_pEditor.putString("expiry",objAccessToken.getString(TAG_EXPIRY));

							_pEditor.putString("membershipNo",objLogin.getString(TAG_MEMBERSHIP));
							_pEditor.putString("points",objLogin.getString(TAG_POINTS));
							_pEditor.putString("userPhoneNo",objLogin.getString(TAG_PHONE));
							_pEditor.commit();
							dialog.dismiss();
							Toast.makeText(getActivity(),"Successfully Login, Try The Activity Again",Toast.LENGTH_LONG).show();
						}
						else {
							Toast.makeText(getActivity(),objLogin.getString("message"),Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(getActivity(),objErr.getString(TAG_ERMSG), Toast.LENGTH_LONG).show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress && mSignInClicked && result.hasResolution()) {
			try {
				mSignInClicked = false;
				dialogAuthLogin.dismiss();
				mIntentInProgress = true;
				getActivity().startIntentSenderForResult(
						result.getResolution().getIntentSender(), RC_SIGN_IN,
						null, 0, 0, 0);
			} catch (SendIntentException e) {
				// The intent was canceled before it was sent. Return to the default
				// state and attempt to connect to get an updated ConnectionResult.
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		mGoogleApiClient.connect();
		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
			Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
			Log.v("name", currentPerson.getDisplayName());
			profImageWhole = currentPerson.getImage().getUrl();
			Log.v("gp+ image", profImageWhole);
			int ind = profImageWhole.indexOf("?sz");
			Log.v("gp+ image", profImageWhole.substring(0, ind));
			@SuppressWarnings("unused")
			String personGooglePlusProfile = currentPerson.getUrl();
			jsonObject1 = new JSONObject();
			jsonObject2 = new JSONObject();
			try {
				jsonObject2.put(TAG_EMAIL,Plus.AccountApi.getAccountName(mGoogleApiClient));
				jsonObject2.put(TAG_TYPE, "gp");
				jsonObject2.put(TAG_AUTHID, currentPerson.getId());
				jsonObject2.put(TAG_USRIMG, profImageWhole.substring(0, ind));
				jsonObject2.put(TAG_SCLNME, currentPerson.getDisplayName());
				jsonObject1.put(TAG_OAUTHDTLS, jsonObject2);

				Log.v("input data", jsonObject1.toString());

				AstClassOuthLogin astClassOuthLogin = new AstClassOuthLogin();
				astClassOuthLogin.execute("");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
	}

	@Override
	public void tryAgain(AsyncTask<String, String, Long> conAsync) {
		if(conAsync instanceof AstClassSetLikeWish){
			conAsync = new AstClassSetLikeWish();
		}else{
			conAsync = new ListAstClass();
		}
		conAsync.execute("");
		
	}
}
