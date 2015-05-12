package com.easydiner.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classes.Constant;
import com.classes.EasydinerClass;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.activities.DetailsActivity;
import com.easydiner.dataobject.ListItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

	private MapView m;
	private static GoogleMap googleMap;
	private static ArrayList<ListItem> arrayList;
	private static int resultCount = 0;
	private Constant _constant;
	private LatLng currentPos;
	private SharedPreferences _pref;
	private SharedPreferences.Editor _pEditor;
	private TextView name, type, address;
	private LinearLayout llMapRestaurantName, llTransitLayout, llCarLayout;
	private int position;

	@SuppressWarnings("unchecked")
	public static MapFragment newInstance(Bundle bundleToPass) {
		MapFragment frag = new MapFragment();
		arrayList = (ArrayList<ListItem>) bundleToPass.getSerializable("list");
		resultCount = (int) bundleToPass.getInt("resultCount");
//		frag.setArguments(bundleToPass);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		_constant = new Constant();
		View rootView = inflater.inflate(R.layout.map_fragment_layout,container, false);
		
		Tracker t = ((EasydinerClass) getActivity().getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("MapFragment");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    

		m = (MapView) rootView.findViewById(R.id.map);
		m.onCreate(savedInstanceState);
		m.onResume();
		_pref = new Pref(getActivity()).getSharedPreferencesInstance();
		_pEditor = new Pref(getActivity()).getSharedPreferencesEditorInstance();
		Log.e("list item", String.valueOf(resultCount));
		Log.e("MAP_DETAILS", String.valueOf(_constant.MAP_DETAILS));
		create();
		return rootView;
	}
	
	@Override
	public void onDestroyView() {
		
		super.onDestroyView();
		System.out.println("onDestroyView");
		
		
	}

	@SuppressWarnings("static-access")
	private void create() {

		try {

			MapsInitializer.initialize(getActivity());
			googleMap = m.getMap();
			// map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//			googleMap.setMyLocationEnabled(true);

			// For showing a move to my loction button For dropping a marker at a point on the Map map.addMarker(new MarkerOptions().position(new LatLng(latitude,
			// longitude)).title("My Home").snippet("Home Address")); For zooming automatically to the Dropped PIN Location map.animateCamera(CameraUpdateFactory.newLatLngZoom(new
			// LatLng(latitude,longitude), 12.0f));

			final LatLngBounds.Builder builder = new LatLngBounds.Builder();

			if (_constant.MAP_DETAILS == 0) {
				for (int i = 0; i < arrayList.size(); i++) {
					ListItem _item = arrayList.get(i);

					if (!_item.getLat().equalsIgnoreCase("(null)") && !_item.getLng().equalsIgnoreCase("(null)")
							&& !_item.getLat().equalsIgnoreCase("") && !_item.getLng().equalsIgnoreCase("")) {
						final LatLng pos = new LatLng(Float.parseFloat(_item.getLat()), Float.parseFloat(_item.getLng()));
						builder.include(pos);
						if (_item.getReviewed()) {
							BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.map_balloon);
							googleMap.addMarker(new MarkerOptions().position(pos).title(_item.getItemname()).snippet(
											_item.getItemtype() + "\n"+ _item.getItemlocation()).icon(icon));
						} else {
							BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.map_balloon_nonrated);
							googleMap.addMarker(new MarkerOptions().position(pos).title(_item.getItemname()).snippet(
											_item.getItemtype() + "\n"+ _item.getItemlocation()).icon(icon));
						}

					}
				}
			} else {

				final LatLng pos = new LatLng(Float.parseFloat(_pref.getString("restaurent_lat", "")), Float.parseFloat(_pref
						.getString("restaurent_long", "")));
				builder.include(pos);
				BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.map_balloon);
				googleMap.addMarker(new MarkerOptions().position(pos).title(_pref.getString("restaurent_name", "")).snippet(
								_pref.getString("restaurent_type", "")+ "\n"+ _pref.getString("restaurent_location", "")).icon(icon));
			}
			
			/*LatLngBounds bounds = builder.build();
			int padding = 0; // offset from edges of the map in pixels
			CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
			googleMap.moveCamera(cu);*/

			if (_constant.MAP_DETAILS == 0) {
				currentPos = new LatLng(Float.parseFloat(_pref.getString("currLat", "")), Float.parseFloat(_pref.getString("currLng", "")));
			}

			else {
				currentPos = new LatLng(Float.parseFloat(_pref.getString("restaurent_lat", "")), Float.parseFloat(_pref.getString("restaurent_long", "")));
			}

			
				googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

							public void onCameraChange(CameraPosition arg0) {
								
								if (arrayList!=null) {
									googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 10));
								}else{
									googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 14.0f));
								}
								googleMap.setOnCameraChangeListener(null);
								
							}
						});

			if (currentPos != null) {/*
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 10.0f));
				googleMap.setOnCameraChangeListener(null);
			*/}
			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getActivity(), "Sorry! unable to create maps",Toast.LENGTH_SHORT).show();
			}

			googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

				@Override
				public View getInfoContents(Marker arg0) {
					return null;
				}

				@Override
				public View getInfoWindow(Marker selectedMarker) {
					View mapdetailsView = getActivity().getLayoutInflater().inflate(R.layout.map_popup_layout, null);
					name = (TextView) mapdetailsView.findViewById(R.id.tvMapPopupItemName);
					type = (TextView) mapdetailsView.findViewById(R.id.tvMapPopupItemType);
					address = (TextView) mapdetailsView.findViewById(R.id.tvMapPopupItemAddress);
					llMapRestaurantName = (LinearLayout) mapdetailsView.findViewById(R.id.llMapRestaurantName);
					llTransitLayout = (LinearLayout) mapdetailsView.findViewById(R.id.llTransitLayout);
					llCarLayout = (LinearLayout) mapdetailsView.findViewById(R.id.llCarLayout);

					setTextFont();
					String[] id = selectedMarker.getId().toString().split("m");
					position = Integer.parseInt(id[1]);
					if (_constant.MAP_DETAILS == 0) {
						name.setText(arrayList.get(position).getItemname());
						type.setText(arrayList.get(position).getItemtype());
						address.setText(arrayList.get(position).getItemlocation());
					} else {
						name.setText(_pref.getString("restaurent_name", ""));
						type.setText(_pref.getString("restaurent_type", ""));
						address.setText(_pref.getString("restaurent_location",""));
					}

					llCarLayout.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							Log.v("test", "ttt");
						}
					});

					return mapdetailsView;
				}

			});

			googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

						@Override
						public void onInfoWindowClick(Marker selectedMarker) {

							if(_constant.MAP_DETAILS == 0){
								String[] id = selectedMarker.getId().toString().split("m");
								position = Integer.parseInt(id[1]);
								Intent intent = new Intent(getActivity(),DetailsActivity.class);
								_pEditor.putString("itemId", String.valueOf(arrayList.get(position).getItemId()));
								_pEditor.commit();
								Constant.NEAR_BY_LIST = 0;
								startActivity(intent);
								getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
							}

							}
					});

		} catch (Exception e) {
			System.out.println("ExceptionMap::: " + e);
		}
		
	}

	private void setTextFont() {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(),fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(),fontPath2);
		name.setTypeface(tf1);
		type.setTypeface(tf2);
		address.setTypeface(tf2);
	}
}
