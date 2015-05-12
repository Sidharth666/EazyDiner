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

import com.classes.EasydinerClass;
import com.easydiner.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class ProfileItemlistFragment extends Fragment {

	private String listItem[] = { "EDIT", "CHANGE PASSWORD" };
	private ListView lvProfItemlist;
	private View rootView;
	private ArrayAdapter<String> arrayAdapter;

	public static ProfileItemlistFragment newInstance() {
		ProfileItemlistFragment frag = new ProfileItemlistFragment();
		return frag;
	}

	@SuppressWarnings("static-access")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.profile_itemlist_layout,container, false);
		
		Tracker t = ((EasydinerClass) getActivity().getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("ProfileItemlistFragment");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		//((DashboardActivity)getActivity()).unSelectTab();
		initilize();
		lvProfItemlist.setAdapter(arrayAdapter);
		onClick();
		return rootView;
	}

	private void initilize() {
		lvProfItemlist = (ListView) rootView.findViewById(R.id.lvProfItemlist);
		arrayAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.prof_listitem_layout, R.id.tvListitemName, listItem);
	}

	private void onClick() {
		lvProfItemlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position == 0) {
					TextView tvListitemName = (TextView) view
							.findViewById(R.id.tvListitemName);
					tvListitemName.setTextColor(Color.parseColor("#FC4415"));
				} else if (position == 1) {
					TextView tvListitemName = (TextView) view
							.findViewById(R.id.tvListitemName);
					tvListitemName.setTextColor(Color.parseColor("#FC4415"));
				}
			}
		});
	}
}
