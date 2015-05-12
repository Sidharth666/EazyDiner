package com.easydiner.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.classes.AsyncTaskListener;
import com.classes.CommonFunction;
import com.classes.Constant;
import com.classes.CustomAlertProgressDialog;
import com.classes.EasydinerClass;
import com.classes.JsonobjectPost;
import com.classes.PostObject;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.activities.BooknowPopupActivity;
import com.easydiner.activities.DashboardActivity;
import com.easydiner.activities.ImageSelectorActivity;
import com.easydiner.dao.RestaurantAdapter;
import com.easydiner.dataobject.RestorentItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class ReviewformDashbordFragmant extends Fragment {

	private View rootView;

	EditText etResturentName, etSubjectBashboardReview,
			etWriteReviewsBashboardReview;
	TextView tvUploadImagesReview, textMyRatingDashboard;
	LinearLayout llUploadImagesReview, llRestName, llPostReview,
			llReadPreviousReview;
	RatingBar foodRating;

	int restaurantId = 0;
	String userRating = "0.0";

	protected static final int CAMERA_REQUEST = 2;
	protected static final int GALLERY_PICTURE = 1;
	private Intent pictureActionIntent;

	private SharedPreferences _pref;
	private SharedPreferences.Editor _pEditor;

	private CommonFunction _comFunc;

	private static final String TAG_DATA = "data";
	private static final String TAG_ITEMDATA = "itemdata";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_GETITEM = "getItem";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_RESNAME = "resName";
	private static final String TAG_LOCATION = "location";
	private PopupWindow popupWindowRestorent;
	private RestaurantAdapter adpterObj;
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private List<NameValuePair> nameValuePairs;
	private ArrayList<RestorentItem> arrayList;
	private int rescFlag = 0, rescShow = 0;

	public static ReviewformDashbordFragmant newInstance() {
		ReviewformDashbordFragmant frag = new ReviewformDashbordFragmant();
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.reviewform_dashbord_layout,container, false);
		
		Tracker t = ((EasydinerClass) getActivity().getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("ReviewformDashbordFragmant");
	    t.send(new HitBuilders.ScreenViewBuilder().build());

		((DashboardActivity) getActivity()).unSelectTab();
		DashboardActivity.ivReviewDashBoard.setSelected(true);
		DashboardActivity.tvDashBoardHeading.setText("MY REVIEWS");

		initialize();
		setTextFont();
		onClick();

		return rootView;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	private void initialize() {

		_pEditor = new Pref(getActivity()).getSharedPreferencesEditorInstance();
		_pref = new Pref(getActivity()).getSharedPreferencesInstance();

		_comFunc = new CommonFunction(getActivity());

		arrayList = new ArrayList<RestorentItem>();
		adpterObj = new RestaurantAdapter(getActivity(), arrayList);

		etResturentName = (EditText) rootView
				.findViewById(R.id.etRestNameBashboardReview);
		etSubjectBashboardReview = (EditText) rootView
				.findViewById(R.id.etSubjectBashboardReview);
		etWriteReviewsBashboardReview = (EditText) rootView
				.findViewById(R.id.etWriteReviewsBashboardReview);
		tvUploadImagesReview = (TextView) rootView
				.findViewById(R.id.tvUploadImagesReview);
		llRestName = (LinearLayout) rootView.findViewById(R.id.llRestName);
		llUploadImagesReview = (LinearLayout) rootView
				.findViewById(R.id.llUploadImagesReview);
		llPostReview = (LinearLayout) rootView.findViewById(R.id.llPostReview);
		llReadPreviousReview = (LinearLayout) rootView
				.findViewById(R.id.llReadPreviousReview);
		foodRating = (RatingBar) rootView.findViewById(R.id.userRating);
		textMyRatingDashboard = (TextView) rootView
				.findViewById(R.id.textMyRatingDashboard);
	}

	private void onClick() {

		etResturentName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

				if (!etResturentName.getText().toString().equalsIgnoreCase("")) {
					jsonObject1 = new JSONObject();
					jsonObject2 = new JSONObject();

					try {
						if (rescFlag == 0) {
							jsonObject2.put(TAG_ACCESSTOKN, "");
							jsonObject2.put(TAG_RESNAME, etResturentName
									.getText().toString());
							jsonObject1.put(TAG_GETITEM, jsonObject2);
							nameValuePairs = new ArrayList<NameValuePair>(2);
							nameValuePairs.add(new BasicNameValuePair("data",
									jsonObject1.toString()));
							AstClassRestName astClassRestName = new AstClassRestName();
							astClassRestName.execute("");
						} else {
							rescFlag = 0;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Log.v("input", "fff");
					rescShow = 0;
					try {
						popupWindowRestorent.dismiss();
					} catch (NullPointerException e) {
						e.printStackTrace();
					}

				}

			}
		});

		foodRating
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						// TODO Auto-generated method stub
						userRating = Float.toString(rating);
					}
				});

		llUploadImagesReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_pEditor.putString("postRevSubject", etSubjectBashboardReview
						.getText().toString());
				_pEditor.putString("postRev", etWriteReviewsBashboardReview
						.getText().toString());
				_pEditor.commit();
				startDialog();
			}
		});

		llPostReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (_pref.getString("accessToken", "").equalsIgnoreCase("")) {
					showRemainderPopup();
				} else {

					String[] review_count = etWriteReviewsBashboardReview
							.getText().toString().split(" ");

					HashMap<String, PostObject> _postMap = new HashMap<String, PostObject>();

					if (etResturentName.getText().toString()
							.equalsIgnoreCase("")) {
						Toast.makeText(getActivity(),
								"Please give the restaurant name",
								Toast.LENGTH_LONG).show();

					} else if (etSubjectBashboardReview.getText().toString()
							.equalsIgnoreCase("")) {
						Toast.makeText(getActivity(),
								"Please give the subject of your review",
								Toast.LENGTH_LONG).show();
					} else if (etWriteReviewsBashboardReview.getText()
							.toString().equalsIgnoreCase("")) {
						Toast.makeText(getActivity(),
								"Please give your review", Toast.LENGTH_LONG)
								.show();
					} else if (etWriteReviewsBashboardReview.getText()
							.toString().split(" ").length < 50) {
						Toast.makeText(getActivity(),
								"Review should be minimum 50 words",
								Toast.LENGTH_LONG).show();

					} else if (userRating.equals("0.0")) {
						Toast.makeText(getActivity(),
								"Please give rating for review",
								Toast.LENGTH_LONG).show();
					} else {

						Log.v("userId", _pref.getString("membershipNo", ""));
						Log.v("myRating", userRating);
						Log.v("restaurantId", String.valueOf(restaurantId));

						_postMap.put("subject", _comFunc.getPostObject(
								etSubjectBashboardReview.getText().toString(),
								false));

						_postMap.put("review", _comFunc.getPostObject(
								etWriteReviewsBashboardReview.getText()
										.toString(), false));

						_postMap.put("acessToken", _comFunc.getPostObject(
								_pref.getString("accessToken", ""), false));

						_postMap.put("userId", _comFunc.getPostObject(
								_pref.getString("membershipNo", ""), false));

						_postMap.put("myRating",
								_comFunc.getPostObject(userRating, false));

						_postMap.put(
								"restaurantId",
								_comFunc.getPostObject(
										String.valueOf(restaurantId), false));

						_postMap.put("image_no", _comFunc.getPostObject(
								String.valueOf(Constant.imgSelected.size()),
								false));

						for (int i = 0; i < Constant.imgSelected.size(); i++) {
							_postMap.put(
									"image." + i,
									_comFunc.getPostObject(
											Constant.imgSelected.get(i), true));
						}

						_comFunc.callPostWebservice(Constant.BASE_URL
								+ "PostUserReviews", _postMap,
								_profileChangeAsync, true);

					}
				}
			}
		});

		etResturentName.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				setEdittextHint();
				etResturentName.setHint("");
				return false;
			}
		});
		etSubjectBashboardReview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				setEdittextHint();
				etSubjectBashboardReview.setHint("");
				return false;
			}
		});
		etWriteReviewsBashboardReview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				setEdittextHint();
				etWriteReviewsBashboardReview.setHint("");
				return false;
			}
		});

		llReadPreviousReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				((DashboardActivity) getActivity()).reviewFrament();

			}
		});
	}

	private void startDialog() {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
				getActivity());
		myAlertDialog.setTitle("Upload Pictures Option");
		myAlertDialog.setMessage("How do you want to set your picture?");

		myAlertDialog.setPositiveButton("Gallery",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

						Intent intent = new Intent(getActivity(),
								ImageSelectorActivity.class);
						startActivity(intent);
					}
				});

		myAlertDialog.setNegativeButton("Camera",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						pictureActionIntent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(pictureActionIntent,
								CAMERA_REQUEST);

					}
				});
		myAlertDialog.show();
	}

	private void showRemainderPopup() {
		final Dialog dialog = new Dialog(getActivity());

		dialog.setContentView(R.layout.remainder_popup_layout);
		dialog.setTitle("Login Remainder");
		LinearLayout llCancelPopup = (LinearLayout) dialog
				.findViewById(R.id.llCancelPopup);

		llCancelPopup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private class AstClassRestName extends AsyncTask<String, String, Long> {
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {

				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL + "autofillrestaurant", nameValuePairs,jsonObject1.toString());

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			if (jObjList.toString() != null) {
				try {
					JSONObject objData = jObjList.getJSONObject(TAG_DATA);
					JSONArray listArray = objData.getJSONArray(TAG_ITEMDATA);
					JSONObject objErr = jObjList.getJSONObject(TAG_ERNODE);
					arrayList.clear();
					if (objErr.get(TAG_ERCODE).toString().equalsIgnoreCase("0")) {
						for (int i = 0; i < listArray.length(); i++) {
							JSONObject jsonObject = listArray.getJSONObject(i);
							RestorentItem _item = new RestorentItem();

							_item.setId(jsonObject.getInt(TAG_ID));
							_item.setName(jsonObject.getString(TAG_NAME));
							_item.setLocation(jsonObject
									.getString(TAG_LOCATION));
							arrayList.add(_item);
						}

						adpterObj.notifyDataSetChanged();
						if (rescShow == 1) {
							popupWindowRestorent.dismiss();
						}
						displayRescPopup();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					popupWindowRestorent.dismiss();
					e.printStackTrace();
				}

			}
		}
	}

	public void displayRescPopup() {

		LayoutInflater layoutinflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View popupView = layoutinflater.inflate(R.layout.restorent_list_layout,
				null);

		popupWindowRestorent = new PopupWindow(popupView,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		ListView lvRestListPopup = (ListView) popupView
				.findViewById(R.id.lvRestList);
		lvRestListPopup.setAdapter(adpterObj);

		lvRestListPopup
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub

						rescFlag = 1;
						etResturentName.setText(arrayList.get(position)
								.getName());
						restaurantId = arrayList.get(position).getId();
						Log.v("rest_id", String.valueOf(restaurantId));
						popupWindowRestorent.dismiss();

					}
				});

		rescShow = 1;
		popupWindowRestorent.showAsDropDown(llRestName);
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

			etResturentName.setText("");
			restaurantId = 0;
			etSubjectBashboardReview.setText("");
			etWriteReviewsBashboardReview.setText("");
			foodRating.setRating(0);

			Toast.makeText(getActivity(), "Your review is posted successfully",
					Toast.LENGTH_LONG).show();
		}
	};

	private void setTextFont() {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/Aller_LtIt.ttf";
		String fontPath4 = "fonts/avenir-light.ttf";

		Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath2);
		Typeface tf3 = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath3);
		Typeface tf4 = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath4);

		etResturentName.setTypeface(tf2);
		etSubjectBashboardReview.setTypeface(tf2);
		etWriteReviewsBashboardReview.setTypeface(tf2);
		tvUploadImagesReview.setTypeface(tf1);
		textMyRatingDashboard.setTypeface(tf2);
	}

	private void setEdittextHint() {
		etResturentName.setHint("Restaurant Name");
		etSubjectBashboardReview.setHint("Subject");
		etWriteReviewsBashboardReview.setHint("Write your reviews...");
	}
}
