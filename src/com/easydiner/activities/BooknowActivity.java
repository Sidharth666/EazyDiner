package com.easydiner.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.classes.CommonFunction;
import com.classes.CommonFunction.TimeoutDialogTryAgainListener;
import com.classes.Constant;
import com.classes.CustomTimePickerDialog;
import com.classes.JsonobjectPost;
import com.easydiner.R;
import com.easydiner.dao.RestorantAdaptor;
import com.easydiner.dataobject.RestorentItem;

public class BooknowActivity extends Activity implements TimeoutDialogTryAgainListener{
	
	private EditText etBookingDate, etBookingTime, etReaturentNameBooking, etPersonName, etPhone, etEmail;
	private TextView tvBookingText1, tvBookingText2, tvBookingText3, tvBookingText4;
	private int year;
	private int month;
	private int day;
	private int hour;
    private int minute;
    private String selectTime;
	static final int DATE_DIALOG_ID = 999;
	static final int TIME_DIALOG_ID = 1111;
	private Spinner spnBookingPersonNo;
	private RestorantAdaptor adpterObj;
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private static final String TAG_GETITEM = "getItem";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_RESNAME = "resName";
	private static final String TAG_RESTURENTID = "resturentId";
	private static final String TAG_ITEMDATA = "itemdata";
	private static final String TAG_EMAILID = "emailid";
	private static final String TAG_MOBILE = "mobile";
	private static final String TAG_DATE = "date";
	private static final String TAG_TIME = "time";
	private static final String TAG_NOOFPERSON = "noOfPerson";
	private static final String TAG_BOOKDATA = "bookData";
	private static final String TAG_ID = "id";
	private static final String TAG_DATA = "data";
	private static final String TAG_NAME = "name";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_BOOKINGID = "bookingId";
	private List<NameValuePair> nameValuePairs, nameValuePairsBooking;
	private PopupWindow popupWindowRestorent;
	private ArrayList<RestorentItem> arrayList;
	private LinearLayout llRestLayout;
	private RelativeLayout rlSlideBookNow;
	private int rescFlag = 0, rescShow = 0;
	private int restaurenId = 0, personNo=1;
	AstClassBooking astClassBooking;
	private Context con;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.booknow_layout);
		con = this;
		etBookingDate = (EditText)findViewById(R.id.etBookingDate);
		etBookingTime = (EditText)findViewById(R.id.etBookingTime);
		spnBookingPersonNo = (Spinner)findViewById(R.id.spnBookingPersonNo);
		etReaturentNameBooking = (EditText)findViewById(R.id.etReaturentNameBooking);
		llRestLayout=(LinearLayout)findViewById(R.id.llRestLayout);
		rlSlideBookNow = (RelativeLayout)findViewById(R.id.rlSlideBookNow);
		etPersonName = (EditText)findViewById(R.id.etNameBooking);
		etPhone = (EditText)findViewById(R.id.etMobileBooking);
		etEmail = (EditText)findViewById(R.id.etEmailBooking);
		arrayList = new ArrayList<RestorentItem>();
		adpterObj = new RestorantAdaptor(BooknowActivity.this, arrayList);
		setTextFont();
		
		etReaturentNameBooking.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				jsonObject1 = new JSONObject();
				jsonObject2 = new JSONObject();
				
					try {
						if(rescFlag == 0)
						{	
							jsonObject2.put(TAG_ACCESSTOKN, "");
							jsonObject2.put(TAG_RESNAME, etReaturentNameBooking.getText().toString());
							jsonObject1.put(TAG_GETITEM, jsonObject2);
							nameValuePairs = new ArrayList<NameValuePair>(2);
							nameValuePairs.add(new BasicNameValuePair("data", jsonObject1.toString()));
							AstClass astClass = new AstClass();
							astClass.execute("");
						}
						else
						{
							rescFlag = 0;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
			
			}
		});
		
		etBookingDate.setText(new StringBuilder().append(Calendar.getInstance().get(Calendar.YEAR)).append("-")
					.append(Calendar.getInstance().get(Calendar.MONTH) + 1).append("-")
					.append(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).append(" "));
		
		if(Calendar.getInstance().get(Calendar.MINUTE) > 30)
		{
			minute = 0;
			hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1;
		}
		else {
			minute = 30;
			hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		}
		
		updateTime(hour, minute);
		etBookingDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		
		etBookingTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CustomTimePickerDialog timePick = new CustomTimePickerDialog(BooknowActivity.this, timePickerListener,
						Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE),true);
				timePick.show();
			}
		});
		
		spnBookingPersonNo.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				if(position < 4){
					personNo = position + 1;
				}else{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BooknowActivity.this);
				    alertDialogBuilder.setTitle("Notice");
					alertDialogBuilder.setMessage("For more than 4 guests, contact EazyConcierge at 768 100 4444");
				    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						
				         @Override
				         public void onClick(DialogInterface arg0, int arg1) {
				        	String number = "7861004444";
							Intent dial = new Intent();
							dial.setAction("android.intent.action.DIAL");
							dial.setData(Uri.parse("tel:" + number));
							startActivity(dial);
							
				         }
				      });
				      alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							
				         @Override
				         public void onClick(DialogInterface dialog, int which) {
				        	 dialog.dismiss();
						 }
				      });
					    
				      AlertDialog alertDialog = alertDialogBuilder.create();
				      alertDialog.show();
				}	
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		rlSlideBookNow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(restaurenId == 0)
				{
					Toast.makeText(BooknowActivity.this, "Please Select Restaurent Name", Toast.LENGTH_LONG).show();
				}
				else if (etPersonName.getText().toString().equalsIgnoreCase("")) {
					Toast.makeText(BooknowActivity.this, "Please Enter Your Name", Toast.LENGTH_LONG).show();
				}
				else if (etPhone.getText().toString().equalsIgnoreCase("")) {
					Toast.makeText(BooknowActivity.this, "Please Enter Your Mobile No.", Toast.LENGTH_LONG).show();				
				}
				else if (etEmail.getText().toString().equalsIgnoreCase("")) {
					Toast.makeText(BooknowActivity.this, "Please Enter Your Email Id", Toast.LENGTH_LONG).show();
				}
				else if (etPhone.getText().toString().length() < 10) {
					Toast.makeText(BooknowActivity.this, "Please Enter A Proper Mobile No.", Toast.LENGTH_LONG).show();
				}
				else
				{	
					jsonObject1 = new JSONObject();
					jsonObject2 = new JSONObject();
					try {
						jsonObject2.put(TAG_ACCESSTOKN, "");
						jsonObject2.put(TAG_RESTURENTID, restaurenId);
						jsonObject2.put(TAG_NAME, etPersonName.getText().toString().trim());
						jsonObject2.put(TAG_EMAILID, etEmail.getText().toString().trim());
						jsonObject2.put(TAG_MOBILE, etPhone.getText().toString().trim());
						jsonObject2.put(TAG_DATE, etBookingDate.getText().toString().trim());
						jsonObject2.put(TAG_TIME, selectTime);
						jsonObject2.put(TAG_NOOFPERSON, personNo);
						
						jsonObject1.put(TAG_BOOKDATA, jsonObject2);
						
						Log.v("rest_value", jsonObject1.toString());
						
						nameValuePairsBooking = new ArrayList<NameValuePair>(2);
						nameValuePairsBooking.add(new BasicNameValuePair("data", jsonObject1.toString()));
						astClassBooking = new AstClassBooking();
						astClassBooking.execute("");
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}	
			}
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener,Calendar.getInstance().get(Calendar.YEAR),
					 Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
			Calendar cal = Calendar.getInstance(); 
			cal.add(Calendar.DATE, 90);
			datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
			datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
			return datePickerDialog;
		case TIME_DIALOG_ID:
			// set time picker as current date
			hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			minute = Calendar.getInstance().get(Calendar.MINUTE);
			return new TimePickerDialog(this, timePickerListener, hour, minute,false);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
	
		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
		
			// set selected date into textview
			etBookingDate.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).append(" "));
		
			// set selected date into datepicker also
			// dpResult.init(year, month, day, null);
		
		}
	};
	
	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        
		  
        @Override 
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            hour   = hourOfDay;
            minute = minutes;
  
            updateTime(hour,minute);
              
         } 
  
    }; 
    
    private static String utilTime(int value) {
        
        if (value < 10)
            return "0" + String.valueOf(value);
        else 
            return String.valueOf(value);
    } 
      
    // Used to convert 24hr format to 12hr format with AM/PM values 
    private void updateTime(int hours, int mins) {
          
    	selectTime = String.valueOf(hours)+":"+String.valueOf(mins);
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else 
            timeSet = "AM";
  
          
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else 
            minutes = String.valueOf(mins);
  
        // Append in a StringBuilder 
         String aTime = new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();
  
         etBookingTime.setText(aTime);
    }
    
    private class AstClass extends AsyncTask<String, String, Long> {
		@Override
		protected Long doInBackground(String... params) {
			try {

				 JsonobjectPost j_parser = new JsonobjectPost();
				
				 jObjList = j_parser.getJSONObj(Constant.BASE_URL +"autofillrestaurant", nameValuePairs, jsonObject1.toString());
				

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			 if (jObjList.toString() != null) {
				 try {
					JSONObject objData = jObjList.getJSONObject(TAG_DATA);
					JSONArray listArray = objData.getJSONArray(TAG_ITEMDATA);
					JSONObject objErr = jObjList.getJSONObject(TAG_ERNODE);
					arrayList.clear();
					if(objErr.get(TAG_ERCODE).toString().equalsIgnoreCase("0"))
					{
						for(int i=0; i<listArray.length(); i++)
						{
							JSONObject jsonObject = listArray.getJSONObject(i);
							RestorentItem _item = new RestorentItem();
							_item.setId(jsonObject.getInt(TAG_ID));
							_item.setName(jsonObject.getString(TAG_NAME));
							
							Log.e("name", jsonObject.getString(TAG_NAME));
							
							arrayList.add(_item);
						}
						
						
						adpterObj.notifyDataSetChanged();
						if(rescShow == 1)
						{
							popupWindowRestorent.dismiss();
						}
						displayRescPopup();
					}
				 	
				 } catch (JSONException e) {
						e.printStackTrace();
				 }

			 }
		}
	}
    
    private class AstClassBooking extends AsyncTask<String, String, Long> {
    	
    	private ProgressDialog dialog;

		public AstClassBooking() {	
			dialog = new ProgressDialog(BooknowActivity.this);
			dialog.setMessage("Please Wait..");
			dialog.show();
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
		}
    	
		@Override
		protected Long doInBackground(String... params) {
			try {
				 JsonobjectPost j_parser = new JsonobjectPost();
				
				 jObjList = j_parser.getJSONObj(Constant.BASE_URL +"resturentbooking", nameValuePairsBooking, jsonObject1.toString(),con, this);
				
			} catch (Exception e) {
				CommonFunction comm = new CommonFunction(con);
				comm.showTimeoutDialog(this);
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			 dialog.dismiss();
			 if (jObjList.toString() != null) {
				 
				 try {
					JSONObject objData = jObjList.getJSONObject(TAG_DATA);
					JSONObject objItemData = objData.getJSONObject(TAG_ITEMDATA);
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BooknowActivity.this);
					alertDialogBuilder.setTitle("Booking Details");
					alertDialogBuilder.setMessage("Thank You\nYour Booking Id Is "+objItemData.getString(TAG_BOOKINGID));
					alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							
					         @Override
					         public void onClick(DialogInterface arg0, int arg1) {
					        	 startActivity(new Intent(BooknowActivity.this, HomeActivity.class));
								
					         }
					      });
					      AlertDialog alertDialog = alertDialogBuilder.create();
					      alertDialog.show();
					//}
					
				 } catch (JSONException e) {
						e.printStackTrace();
				 }

			 }
			
		}
	}
    
    private void setTextFont()
    {
    	tvBookingText1 = (TextView)findViewById(R.id.tvBookingText1);
	    tvBookingText2 = (TextView)findViewById(R.id.tvBookingText2);
	    tvBookingText3 = (TextView)findViewById(R.id.tvBookingText3);
	    tvBookingText4 = (TextView)findViewById(R.id.tvBookingText4);
		
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
		
		tvBookingText1.setTypeface(tf1);
		tvBookingText2.setTypeface(tf1);
		tvBookingText3.setTypeface(tf2);
		tvBookingText4.setTypeface(tf1);
    }
    
    private void displayRescPopup()
	{
    	rescShow = 1;
    	
		LayoutInflater layoutinflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View popupView = layoutinflater.inflate(R.layout.restorent_list_layout, null);
		
		popupWindowRestorent = new PopupWindow(popupView,android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		
		final ListView lvRestListPopup = (ListView)popupView.findViewById(R.id.lvRestList);
		
		lvRestListPopup.setAdapter(adpterObj);
		
		lvRestListPopup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				rescFlag = 1;
				etReaturentNameBooking.setText(arrayList.get(position).getName());
				restaurenId = arrayList.get(position).getId();
				popupWindowRestorent.dismiss();
			}
		});
		
		popupWindowRestorent.showAsDropDown(llRestLayout);
	}
    
    @Override
    public void onBackPressed() {
    	if(rescShow == 1)
    	{
    		popupWindowRestorent.dismiss();
    		rescShow = 0;
    	}
    	else
    	{
    		super.onBackPressed();
    	}	
    }

	@Override
	public void tryAgain(AsyncTask<String, String, Long> conAsync) {
		conAsync.execute("");
		
	}

    
}
