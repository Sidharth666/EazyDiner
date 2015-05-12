package com.easydiner.fragment;

import java.util.ArrayList;

import com.classes.EasydinerClass;
import com.easydiner.R;
import com.easydiner.activities.DashboardActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReservitionDetailsFragment extends Fragment {

	View rootView;

	TextView tvRestaurantName, tvDate, tvMonth, tvWhere, tvWhereValue, tvWhen,
			tvWhenValue, tvAt, tvAtValue, tvPerson, tvPersonValue, tvDeal,
			tvDealValue, tvConfirmation, tvConfirmationValue, tvHeldUntil;

	static ArrayList<String> resvDetails = new ArrayList<String>();

	@SuppressWarnings("unchecked")
	public static ReservitionDetailsFragment newInstance(Bundle bundleToPass) {

		ReservitionDetailsFragment frag = new ReservitionDetailsFragment();

		resvDetails = (ArrayList<String>) bundleToPass
				.getSerializable("reservationDetails");
		frag.setArguments(bundleToPass);

		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.reservition_details, container,false);

		Tracker t = ((EasydinerClass) getActivity().getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("ReservitionDetailsFragment");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		((DashboardActivity) getActivity()).unSelectTab();
		DashboardActivity.ivReservationDashBoard.setSelected(true);
		DashboardActivity.tvDashBoardHeading.setText("RESERVATIONS");

		initialize();
		setTextFont();
		setvalue();

		return rootView;
	}

	private void initialize() {
		// TODO Auto-generated method stub

		tvRestaurantName = (TextView) rootView
				.findViewById(R.id.tvRestaurantName);
		tvDate = (TextView) rootView.findViewById(R.id.tvDate);
		tvMonth = (TextView) rootView.findViewById(R.id.tvMonth);
		tvWhere = (TextView) rootView.findViewById(R.id.tvWhere);
		tvWhereValue = (TextView) rootView.findViewById(R.id.tvWhereValue);
		tvWhen = (TextView) rootView.findViewById(R.id.tvWhen);
		tvWhenValue = (TextView) rootView.findViewById(R.id.tvWhenValue);
		tvAt = (TextView) rootView.findViewById(R.id.tvAt);
		tvAtValue = (TextView) rootView.findViewById(R.id.tvAtValue);
		tvPerson = (TextView) rootView.findViewById(R.id.tvPerson);
		tvPersonValue = (TextView) rootView.findViewById(R.id.tvPersonValue);
		tvDeal = (TextView) rootView.findViewById(R.id.tvDeal);
		tvDealValue = (TextView) rootView.findViewById(R.id.tvDealValue);
		tvConfirmation = (TextView) rootView.findViewById(R.id.tvConfirmation);
		tvConfirmationValue = (TextView) rootView
				.findViewById(R.id.tvConfirmationValue);
		tvHeldUntil = (TextView) rootView.findViewById(R.id.tvHeldUntil);

	}

	private void setTextFont() {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/Aller_Lt.ttf";
		String fontPath4 = "fonts/avenir-light.ttf";

		Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath2);
		Typeface tf3 = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath3);
		Typeface tf4 = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath4);

		tvWhere.setTypeface(tf1);
		tvWhen.setTypeface(tf1);
		tvAt.setTypeface(tf1);
		tvPerson.setTypeface(tf1);
		tvDeal.setTypeface(tf1);
		tvConfirmationValue.setTypeface(tf1);
		tvRestaurantName.setTypeface(tf2);
		tvMonth.setTypeface(tf3);
		tvWhereValue.setTypeface(tf3);
		tvWhenValue.setTypeface(tf3);
		tvAtValue.setTypeface(tf3);
		tvPersonValue.setTypeface(tf3);
		tvDealValue.setTypeface(tf3);
		tvConfirmation.setTypeface(tf3);
		tvHeldUntil.setTypeface(tf3);
		tvDate.setTypeface(tf4);

	}

	@SuppressLint("DefaultLocale")
	private void setvalue() {
		// TODO Auto-generated method stub

		tvRestaurantName.setText(resvDetails.get(0));
		tvWhenValue.setText(resvDetails.get(1));
		tvAtValue.setText(resvDetails.get(2));
		if (resvDetails.get(3).equals("1")) {
			tvPersonValue.setText("1 person");
		} else {
			tvPersonValue.setText(resvDetails.get(3) + " persons");
		}
		tvWhereValue.setText(resvDetails.get(4));
		if (resvDetails.get(5).equals("")) {
			tvDeal.setVisibility(View.GONE);
			tvDealValue.setVisibility(View.GONE);
		} else {
			tvDealValue.setText(resvDetails.get(5));
		}
		tvDealValue.setText(resvDetails.get(5));
		tvConfirmationValue.setText(resvDetails.get(6));
		tvHeldUntil.setText("The reservation will be valid upto "
				+ resvDetails.get(7));

		String bookingDate[] = resvDetails.get(1).split(" ");
		tvDate.setText(bookingDate[1]);
		tvMonth.setText(" " + bookingDate[2].toUpperCase());

	}

}
