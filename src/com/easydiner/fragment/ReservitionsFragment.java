package com.easydiner.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.classes.Constant;
import com.classes.EasydinerClass;
import com.easydiner.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class ReservitionsFragment extends Fragment {

	private ListView lvReservitionListDashboard;
	private ArrayAdapter<String> adapter;
	private View rootView;
	private String listItem[] = { "ALL", "UPCOMING", "BOOKING HISTORY" };
	private Constant _constant;
	
	public static ReservitionsFragment newInstance() {
		ReservitionsFragment frag = new ReservitionsFragment();
		return frag;
	}

	@SuppressWarnings("static-access")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.reservition_list_layout,container, false);
		
		Tracker t = ((EasydinerClass) getActivity().getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("ReservitionsFragment");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		initiatize();
		//((DashboardActivity)getActivity()).unSelectTab();
		lvReservitionListDashboard.setAdapter(adapter);
		onClick();
		return rootView;
	}

	private void initiatize() {
		lvReservitionListDashboard = (ListView) rootView
				.findViewById(R.id.lvReservitionListDashboard);
		adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.prof_listitem_layout, R.id.tvListitemName, listItem);
		_constant = new Constant();
	}
	
	private void onClick()
	{
		lvReservitionListDashboard.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					_constant.RESERVATION_LIST = 1;
					break;
					
				case 1:
					_constant.RESERVATION_LIST = 2;
					break;
					
				case 2:
					_constant.RESERVATION_LIST = 3;
					break;

				default:
					break;
				}
				TextView textItem = (TextView)view.findViewById(R.id.tvListitemName);
				textItem.setTextColor(Color.parseColor("#FC4415"));
			}
		});
	}
}
