package com.easydiner.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.classes.Constant;
import com.classes.EasydinerClass;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.activities.AlllistActivity;
import com.easydiner.activities.CriticDetailsActivity;
import com.easydiner.activities.CriticRestaurentRevActivity;
import com.easydiner.dataobject.ExpListItem;
import com.easydiner.dataobject.SubExpListItem;
import com.flurry.android.FlurryAgent;

public class ExpItemListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<ExpListItem> expListItems;
	private LayoutInflater inflater;
	private SharedPreferences.Editor _pEditor;
	private Constant _constant;
	private SharedPreferences _pref;
	private Intent intent;

	public ExpItemListAdapter(Context context,ArrayList<ExpListItem> expListItems) {
		this.context = context;
		this.expListItems = expListItems;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_pEditor = new Pref(context).getSharedPreferencesEditorInstance();
		_pref = new Pref(context).getSharedPreferencesInstance();
		_constant = new Constant();
	}

	@Override
	public int getGroupCount() {
		return expListItems.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return expListItems.get(groupPosition).getArrayList().size();
	}

	@Override
	public ExpListItem getGroup(int groupPosition) {
		return expListItems.get(groupPosition);
	}

	@Override
	public SubExpListItem getChild(int groupPosition, int childPosition) {
		return expListItems.get(groupPosition).getArrayList().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return (groupPosition * childPosition);
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_group, null);
			ViewGroupHolder holder = new ViewGroupHolder();
			holder.groupItem = (TextView) rowView.findViewById(R.id.listHeader);
			setTextFontForGroup(holder);
			rowView.setTag(holder);
		}
		ViewGroupHolder newHolder = (ViewGroupHolder) rowView.getTag();
		newHolder.groupItem.setText(getGroup(groupPosition).getMainItem());
		return rowView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View subView = convertView;
		subView = inflater.inflate(R.layout.extlist_item, null);
		final ViewChildHolder childHolder = new ViewChildHolder();
		childHolder.groupChildItem = (TextView) subView.findViewById(R.id.expListItem);
		setTextFontForChild(childHolder);

		subView.setTag(childHolder);
		ViewChildHolder newChaildHlder = (ViewChildHolder) subView.getTag();
		newChaildHlder.groupChildItem.setText(getChild(groupPosition,childPosition).getSubItem());

		subView.setOnClickListener(new OnClickListener() {

			@SuppressLint("ResourceAsColor")
			@SuppressWarnings("static-access")
			@Override
			public void onClick(View v) {
				childHolder.groupChildItem.setTextColor(Color.parseColor("#FC4415"));
				//Flurry event
				Map<String, String> homeListParams = new HashMap<String, String>();
				String id = expListItems.get(groupPosition).getArrayList().get(childPosition).getId();
				homeListParams.put("HomeScreen", id);
				FlurryAgent.logEvent("HomeScreen", homeListParams,true);
				
				
				if (!expListItems.get(groupPosition).getMainItem().equalsIgnoreCase("EAZYDINER REVIEWS")) {
					Intent intent = new Intent(context, AlllistActivity.class);
					_pEditor.putString("catId", expListItems.get(groupPosition).getId());
					_pEditor.putString("SubCatId",expListItems.get(groupPosition).getArrayList().get(childPosition).getId());
					_pEditor.putString("SubCatName",expListItems.get(groupPosition).getArrayList().get(childPosition).getSubItem());
					_pEditor.commit();
					_constant.NEAR_BY_LIST = 0;
					Constant.FILTER_BY_LIST = 0;
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					context.startActivity(intent);
				} else {
					String sutCatArr[] = expListItems.get(groupPosition).getArrayList().get(childPosition).getId().split("/");
					_pEditor.putString("criticsId", sutCatArr[2]);
					if(_pref.getBoolean("VirRead", false) && sutCatArr[2].equals("5")){
						
						intent = new Intent(context,CriticRestaurentRevActivity.class);
						
					}else if(_pref.getBoolean("PhantomRead", false) && sutCatArr[2].equals("7")){
						
						intent = new Intent(context,CriticRestaurentRevActivity.class);
					}
					
					else{
						intent = new Intent(context,CriticDetailsActivity.class);
						if(sutCatArr[2].equals("5")){
							_pEditor.putBoolean("VirRead", true);
						}else{
							_pEditor.putBoolean("PhantomRead", true);
						}
					}
					_pEditor.commit();
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					context.startActivity(intent);
				}
			}
		});
		FlurryAgent.endTimedEvent("HomeScreen");
		return subView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	class ViewGroupHolder {
		TextView groupItem;
		ImageView indicator;
	}

	class ViewChildHolder {
		TextView groupChildItem;
	}

	private void setTextFontForGroup(ViewGroupHolder holder) {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf1 = Typeface.createFromAsset(context.getAssets(), fontPath2);

		holder.groupItem.setTypeface(tf1);
	}

	private void setTextFontForChild(ViewChildHolder holder) {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf1 = Typeface.createFromAsset(context.getAssets(), fontPath2);

		holder.groupChildItem.setTypeface(tf1);
	}

}
