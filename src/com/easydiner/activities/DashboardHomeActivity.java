package com.easydiner.activities;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.classes.AsyncTaskListener;
import com.classes.CircularImageView;
import com.classes.CommonFunction;
import com.classes.CommonFunction.TimeoutDialogTryAgainListener;
import com.classes.Constant;
import com.classes.CustomAlertProgressDialog;
import com.classes.EasydinerClass;
import com.classes.ImageCompressCropp;
import com.classes.ImageRoundCropp;
import com.classes.JsonobjectPost;
import com.classes.PostObject;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.dao.ReserveDetailsAdapter;
import com.easydiner.dataobject.ReservDetailsItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class DashboardHomeActivity extends EasyDinerBaseActivity implements TimeoutDialogTryAgainListener{

	private ImageView ivGoHome, ivMenuHome, ivReservationsDashBoardHome,
			ivProfileDashBoardHome, ivReviewImageListDashBoardHome,
			ivWishlistDashBoardHome, ivPiggyDashBoardHome;
	private CircularImageView ivProfileImageDashboardHome;
	private TextView text1DashboardHome, text2DashboardHome,
			text3DashboardHome, text4DashboardHome, tvUserPoints,
			textSearchDashboardHome;
	private RelativeLayout rlShowRedeemp, rlReservationsDashBoardHome,
			rlProfileDashBoardHome, rlReviewImageListDashBoardHome,
			rlWishlistDashBoardHome, rlPiggyDashBoardHome,
			rlDashBoardHomeBtnEazyCon, DashBoardHome_book_now;
	private LinearLayout llSearchDashBoardHome;
	private ListView lvResDetailsList;
	private ArrayList<ReservDetailsItem> detailsItems;
	private ReserveDetailsAdapter revAdapter;
	private SharedPreferences _pref;
	private static final String TAG_GETITEM = "getItem";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_USERID = "userId";
	private static final String TAG_DATA = "data";
	private static final String TAG_ITEMDATA = "itemdata";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_IMG_URL = "img_url";
	private static final String TAG_ID = "id";
	private static final String TAG_RESTAURANTNAME = "restaurantName";
	private static final String TAG_RESERVATIONDATE = "reservationDate";
	private static final String TAG_RESERVATIONTIME = "reservationTime";
	private static final String TAG_NOOFPEOPLE = "noOFPeople";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private List<NameValuePair> nameValuePairs;
	private ImageLoader imageLoader;
	private int flag = 0;

	protected static final int CAMERA_REQUEST = 0;
	protected static final int GALLERY_PICTURE = 1;
	final int PIC_CROP = 2;

	private Bitmap imgbitmap, roundimgbitmap;
	private ImageCompressCropp objCompressCropp;
	private ImageRoundCropp objRoundCropp;

	private String profileimagestring = "";
	private String imagePath = "", picturePath = "not in there";
	private static String TEMP_PHOTO_FILE;

	private SharedPreferences.Editor _pEditor;
	private CommonFunction _comFunc;
	private Context con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_home_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("DashboardHomeActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		con = this;
		Constant.INDASHBOARD = 1;

		initialize();
		setTextFont();
		onClick();

		lvResDetailsList.setAdapter(revAdapter);
		ivReservationsDashBoardHome.setSelected(true);

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true)
				.showImageOnLoading(R.drawable.default_image_for_circuler)
				.showImageForEmptyUri(R.drawable.default_image_for_circuler)
				.showImageOnFail(R.drawable.default_image_for_circuler)
				.imageScaleType(ImageScaleType.EXACTLY).build();
		
		Log.e("img url", _pref.getString("userImg", ""));
		
		 imageLoader.displayImage(_pref.getString("userImg", ""),ivProfileImageDashboardHome, options);
		

		/*
		 * imageLoader .displayImage(
		 * "https://lh5.googleusercontent.com/--iXpSthy74k/AAAAAAAAAAI/AAAAAAAAABE/J1gKn-PXv70/photo.jpg"
		 * , ivProfileImageDashboardHome, options);
		 */

		tvUserPoints.setText(_pref.getString("points", ""));

		loadItem();
	}
	
	private void initialize() {

		ivGoHome = (ImageView) findViewById(R.id.ivGoHome);
		ivMenuHome = (ImageView) findViewById(R.id.ivMenuHome);
		ivProfileImageDashboardHome = (CircularImageView) findViewById(R.id.ivProfileImageDashboardHome);
		ivReservationsDashBoardHome = (ImageView) findViewById(R.id.ivReservationsDashBoardHome);
		ivProfileDashBoardHome = (ImageView) findViewById(R.id.ivProfileDashBoardHome);
//		ivReviewImageListDashBoardHome = (ImageView) findViewById(R.id.ivReviewImageListDashBoardHome);
//		ivWishlistDashBoardHome = (ImageView) findViewById(R.id.ivWishlistDashBoardHome);
		ivPiggyDashBoardHome = (ImageView) findViewById(R.id.ivPiggyDashBoardHome);
		text1DashboardHome = (TextView) findViewById(R.id.text1DashboardHome);
		text2DashboardHome = (TextView) findViewById(R.id.text2DashboardHome);
		text3DashboardHome = (TextView) findViewById(R.id.text3DashboardHome);
		text4DashboardHome = (TextView) findViewById(R.id.text4DashboardHome);
		tvUserPoints = (TextView) findViewById(R.id.tvUserPointsDashboardHome);
		rlShowRedeemp = (RelativeLayout) findViewById(R.id.rlShowRedeemp);
		rlReservationsDashBoardHome = (RelativeLayout) findViewById(R.id.rlReservationsDashBoardHome);
		rlProfileDashBoardHome = (RelativeLayout) findViewById(R.id.rlProfileDashBoardHome);
//		rlReviewImageListDashBoardHome = (RelativeLayout) findViewById(R.id.rlReviewImageListDashBoardHome);
//		rlWishlistDashBoardHome = (RelativeLayout) findViewById(R.id.rlWishlistDashBoardHome);
		rlPiggyDashBoardHome = (RelativeLayout) findViewById(R.id.rlPiggyDashBoardHome);
		lvResDetailsList = (ListView) findViewById(R.id.lvResDetailsList);

		detailsItems = new ArrayList<ReservDetailsItem>();
		revAdapter = new ReserveDetailsAdapter(DashboardHomeActivity.this,detailsItems);

		_comFunc = new CommonFunction(DashboardHomeActivity.this);
		_pref = new Pref(DashboardHomeActivity.this).getSharedPreferencesInstance();
		_pEditor = new Pref(DashboardHomeActivity.this).getSharedPreferencesEditorInstance();
		imageLoader = ImageLoader.getInstance();
		objCompressCropp = new ImageCompressCropp(DashboardHomeActivity.this);
		objRoundCropp = new ImageRoundCropp();

		// ivSearchItemDashBoardHome = (ImageView)
		// findViewById(R.id.ivSearchItemDashBoardHome);
		// ivMenuDashBoardHome = (ImageView)
		// findViewById(R.id.ivMenuDashBoardHome);
		rlDashBoardHomeBtnEazyCon = (RelativeLayout) findViewById(R.id.rlDashBoardHomeBtnEazyCon);
		DashBoardHome_book_now = (RelativeLayout) findViewById(R.id.DashBoardHome_book_now);
		llSearchDashBoardHome = (LinearLayout) findViewById(R.id.llSearchDashBoardHome);
		textSearchDashboardHome = (TextView) findViewById(R.id.textSearchDashboardHome);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		startActivity(new Intent(DashboardHomeActivity.this, HomeActivity.class));
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
		finish();
	}

	private void onClick() {

		ivMenuHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DashboardHomeActivity.this,MenuListActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_down_info,R.anim.slide_up_info);
			}
		});

		ivGoHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(DashboardHomeActivity.this,HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
		});

		/*
		 * lvResDetailsList.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { // TODO
		 * Auto-generated method stub
		 * v.getParent().requestDisallowInterceptTouchEvent(true); return false;
		 * } });
		 */

		ivProfileImageDashboardHome
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						displayImageUploadDialog();

					}

				});

		lvResDetailsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				DashboardActivity.activeTab = 6;

				ArrayList<String> resvDetails = new ArrayList<String>();
				resvDetails.add(detailsItems.get(position).getItemName());
				resvDetails.add(detailsItems.get(position).getItemDate());
				resvDetails.add(detailsItems.get(position).getItemTime());
				resvDetails.add(detailsItems.get(position).getItemPerson());
				resvDetails.add(detailsItems.get(position).getItemLocation());
				resvDetails.add(detailsItems.get(position).getItemDeal());
				resvDetails.add(detailsItems.get(position).getItemConfirmNo());
				resvDetails.add(detailsItems.get(position).getItemHoldingTime());

				DashboardActivity.reservationDetails = resvDetails;

				Intent intent = new Intent(DashboardHomeActivity.this,DashboardActivity.class);

				startActivity(intent);

			}
		});

		rlShowRedeemp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// unSelectTab();
				// ivPiggyDashBoardHome.setSelected(true);
				DashboardActivity.activeTab = 5;
				Intent intent = new Intent(DashboardHomeActivity.this,DashboardActivity.class);
				startActivity(intent);

			}
		});

		rlReservationsDashBoardHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ivReservationsDashBoardHome.setSelected(true);

			}
		});
		rlProfileDashBoardHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// unSelectTab();
				// ivProfileDashBoardHome.setSelected(true);
				DashboardActivity.activeTab = 2;
				Intent intent = new Intent(DashboardHomeActivity.this,
						DashboardActivity.class);
				startActivity(intent);

			}
		});
		/*rlReviewImageListDashBoardHome
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// unSelectTab();
						// ivReviewImageListDashBoardHome.setSelected(true);
						DashboardActivity.activeTab = 3;
						Intent intent = new Intent(DashboardHomeActivity.this,
								DashboardActivity.class);
						startActivity(intent);

					}
				});*/
		/*rlWishlistDashBoardHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// unSelectTab();
				// ivWishlistDashBoardHome.setSelected(true);
				DashboardActivity.activeTab = 4;
				Intent intent = new Intent(DashboardHomeActivity.this,
						DashboardActivity.class);
				startActivity(intent);

			}
		});*/
		rlPiggyDashBoardHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// unSelectTab();
				// ivPiggyDashBoardHome.setSelected(true);
				DashboardActivity.activeTab = 5;
				Intent intent = new Intent(DashboardHomeActivity.this,
						DashboardActivity.class);
				startActivity(intent);

			}
		});

		rlDashBoardHomeBtnEazyCon.setOnClickListener(new OnClickListener() {

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
		DashBoardHome_book_now.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				DashBoardHome_book_now.setBackgroundColor(Color.parseColor("#9B9A9A"));
//				BooknowPopupActivity.rlBookNowBultton = DashBoardHome_book_now;
				startActivity(new Intent(DashboardHomeActivity.this,
						BookRestaurentActivity.class));
				overridePendingTransition(R.anim.slide_up_in,
						R.anim.slide_down_out);
			}
		});

		llSearchDashBoardHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(DashboardHomeActivity.this,SearchListActivity.class));
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});
	}

	private void loadItem() {
		jsonObject1 = new JSONObject();
		jsonObject2 = new JSONObject();

		try {
			jsonObject2.put(TAG_ACCESSTOKN, _pref.getString("accessToken",""));
			jsonObject2.put(TAG_USERID, _pref.getString("membershipNo", ""));
			jsonObject1.put(TAG_GETITEM, jsonObject2);

			AstClassReservation astClassReservation = new AstClassReservation();
			astClassReservation.execute("");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void unSelectTab() {
		ivReservationsDashBoardHome.setSelected(false);
		ivProfileDashBoardHome.setSelected(false);
//		ivReviewImageListDashBoardHome.setSelected(false);
//		ivWishlistDashBoardHome.setSelected(false);
		ivPiggyDashBoardHome.setSelected(false);
	}

	private void setTextFont() {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/Aller_LtIt.ttf";
		String fontPath4 = "fonts/avenir-light.ttf";
		String fontPath5 = "fonts/Aller_Lt.ttf";

		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
		Typeface tf3 = Typeface.createFromAsset(getAssets(), fontPath3);
		Typeface tf4 = Typeface.createFromAsset(getAssets(), fontPath4);
		Typeface tf5 = Typeface.createFromAsset(getAssets(), fontPath5);

		text1DashboardHome.setTypeface(tf5);
		text2DashboardHome.setTypeface(tf5);
		text3DashboardHome.setTypeface(tf5);
		text4DashboardHome.setTypeface(tf5);
		tvUserPoints.setTypeface(tf2);
		textSearchDashboardHome.setTypeface(tf5);
	}

	private class AstClassReservation extends AsyncTask<String, String, Long> {

		private AlertDialog dialog;

		public AstClassReservation() {
			dialog = CustomAlertProgressDialog.getCustomDialog(DashboardHomeActivity.this,"Please wait...");
			dialog.show();
		}

		@SuppressWarnings("static-access")
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL + "getReservationAllList", nameValuePairs,jsonObject1.toString(),con, this);
				Log.v("url1", jsonObject1.toString());
				Log.v("url2", jObjList.toString());

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			try {
				if (jObjList.toString() != null) {

					try {
						JSONObject objErrer = jObjList
								.getJSONObject(TAG_ERNODE);
						if (objErrer.getString(TAG_ERCODE)
								.equalsIgnoreCase("0")) {
							JSONObject objData = jObjList
									.getJSONObject(TAG_DATA);

							JSONArray itemArray = objData
									.getJSONArray(TAG_ITEMDATA);

							if (itemArray.length() > 0) {
								for (int i = 0; i < itemArray.length(); i++) {
									JSONObject obj = itemArray.getJSONObject(i);
									ReservDetailsItem _item = new ReservDetailsItem();
									_item.setId(obj.getInt(TAG_ID));
									_item.setItemName(obj
											.getString(TAG_RESTAURANTNAME));
									_item.setItemDate(obj
											.getString(TAG_RESERVATIONDATE));
									_item.setItemTime(obj
											.getString(TAG_RESERVATIONTIME));
									_item.setItemPerson(obj
											.getString(TAG_NOOFPEOPLE));
									_item.setItemLocation(obj
											.getString("location"));
									_item.setItemDeal(obj
											.getString("bookingDeal"));
									_item.setItemConfirmNo(obj
											.getString("confirmationNo"));
									_item.setItemHoldingTime(obj
											.getString("holdingTime"));

									detailsItems.add(_item);
								}

								revAdapter.notifyDataSetChanged();
							} else {
								Toast.makeText(DashboardHomeActivity.this,
										"No Reservation Available",
										Toast.LENGTH_LONG).show();
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast.makeText(DashboardHomeActivity.this,
								"Error in server side", Toast.LENGTH_LONG)
								.show();
					}
				}
			} catch (Exception e) {
				Toast.makeText(DashboardHomeActivity.this,
						"Error in server side", Toast.LENGTH_LONG).show();
			}
		}
	}

	private void displayImageUploadDialog() {
		// TODO Auto-generated method stub

		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
				DashboardHomeActivity.this);
		myAlertDialog.setTitle("Upload Pictures Option");
		myAlertDialog.setMessage("How do you want to set your picture?");

		myAlertDialog.setPositiveButton("Gallery",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						Intent gallerypickerIntent = new Intent(Intent.ACTION_PICK);
						gallerypickerIntent.setType("image/*");
						startActivityForResult(gallerypickerIntent,GALLERY_PICTURE);
					}
				});

		myAlertDialog.setNegativeButton("Camera",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						Intent pictureActionIntent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(pictureActionIntent,
								CAMERA_REQUEST);

					}
				});
		myAlertDialog.show();

	}
	
	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
               || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.e("requestCode", String.valueOf(requestCode));
		Log.e("resultCode", String.valueOf(resultCode));
		Log.e("data", String.valueOf(data));

		if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK && data != null) {
			Uri selectedImage = data.getData();
			
			try {
				imgbitmap = decodeUri(selectedImage);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			String[] filePathColumn = { MediaStore.Images.ImageColumns.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);

			Log.e("img path", picturePath);

//			imgbitmap = objCompressCropp.compressImageForPath(picturePath);

		} else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK
				&& data != null) {
			Bitmap camBitmap = (Bitmap) data.getExtras().get("data");

			Uri uriImgPath = objCompressCropp.getImageUri(camBitmap);
			Log.e("uriImgPath", uriImgPath.toString());

			picturePath = objCompressCropp.getRealPathFromURI(uriImgPath);
			Log.e("img path", picturePath);

			imgbitmap = objCompressCropp.compressImageForPath(picturePath);

		}

//		picturePath = objCompressCropp.getImageFilePath();

		Log.e("img path1", picturePath);
		ivProfileImageDashboardHome.setImageBitmap(imgbitmap);

		HashMap<String, PostObject> _postMap = new HashMap<String, PostObject>();

		_postMap.put("userId", _comFunc.getPostObject(_pref.getString("membershipNo", ""), false));

		Log.d("userId", _pref.getString("membershipNo", ""));

		_postMap.put("accessToken", _comFunc.getPostObject(_pref.getString("accessToken", ""), false));

		Log.d("accessToken", _pref.getString("accessToken", ""));

		if(picturePath!=null){
			_postMap.put("prof_image", _comFunc.getPostObject(picturePath, true));
		}else{
			_postMap.put("prof_image", _comFunc.getPostObject("", true));
		}
		

		Log.d("prof_image", picturePath);

		_comFunc.callPostWebservice(Constant.BASE_URL + "SetProfileImage",_postMap, _profileChangeAsync, true);
	}

	AsyncTaskListener _profileChangeAsync = new AsyncTaskListener() {

		private AlertDialog pDialog;

		@Override
		public void onTaskPreExecute() {
			// TODO Auto-generated method stub

			pDialog = CustomAlertProgressDialog.getCustomDialog(DashboardHomeActivity.this,"Please wait...");
			pDialog.show();

		}

		@SuppressLint("ShowToast")
		@Override
		public void onTaskCompleted(String result) {
			// TODO Auto-generated method stub
			try {
				Log.v("result", result);
				JSONObject jsonObj = new JSONObject(result);
				JSONObject jsonData = jsonObj.getJSONObject(TAG_DATA);
				JSONObject jsonErrorNode = jsonObj.getJSONObject(TAG_ERNODE);
				if (jsonErrorNode.getString(TAG_ERCODE).equalsIgnoreCase("0")) {
					_pEditor.putString("userImg",jsonData.getString(TAG_IMG_URL));
					_pEditor.commit();
				} else {
					Toast.makeText(DashboardHomeActivity.this,"Error in profile updation.", Toast.LENGTH_LONG).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			pDialog.dismiss();

		}
	};

	@Override
	public void tryAgain(AsyncTask<String, String, Long> conAsync) {
		conAsync = new AstClassReservation();
		conAsync.execute("");
		
	}

}
