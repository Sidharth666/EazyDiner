package com.easydiner.dao;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.classes.Constant;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.dataobject.CriticReviewItem;
import com.easydiner.dataobject.ExtCriticReviewListItem;

public class ExtReviewItemAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<ExtCriticReviewListItem> expListItems;
	private LayoutInflater inflater;
	private SharedPreferences.Editor _pEditor;
	private Constant _constant;

	public ExtReviewItemAdapter(Context context,
			ArrayList<ExtCriticReviewListItem> expListItems) {
		this.context = context;
		this.expListItems = expListItems;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_pEditor = new Pref(context).getSharedPreferencesEditorInstance();
		_constant = new Constant();
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return expListItems.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return expListItems.get(groupPosition).getSubList().size();
	}

	@Override
	public ExtCriticReviewListItem getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return expListItems.get(groupPosition);
	}

	@Override
	public CriticReviewItem getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return expListItems.get(groupPosition).getSubList().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return (groupPosition * childPosition);
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = convertView;
		/*
		 * if(rowView == null) {
		 */
		rowView = inflater.inflate(R.layout.review_group_list_layout, null);
		ViewGroupHolder holder = new ViewGroupHolder();
		holder.itemName = (TextView) rowView.findViewById(R.id.tvCrtRestName);
		holder.tvNewTag = (TextView) rowView.findViewById(R.id.tvNewTag);
		holder.tvCriticRatingReviewList = (TextView) rowView.findViewById(R.id.tvCriticRatingReviewList);
		holder.textTotalRatingReviewList = (TextView) rowView.findViewById(R.id.textTotalRatingReviewList);
		setTextFontGroup(holder);

		rowView.setTag(holder);
		// }
		ViewGroupHolder newHolder = (ViewGroupHolder) rowView.getTag();
		String restName = getGroup(groupPosition).getItemName();
		if (restName.length() > 27) {
			restName = restName.substring(0, 25) + "..";
		}

		newHolder.itemName.setText(restName);
		holder.tvCriticRatingReviewList.setText(getGroup(groupPosition).getItemRating());
		return rowView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View subView = convertView;
		subView = inflater.inflate(R.layout.review_item_layout, null);

		ViewChildHolder childHolder = new ViewChildHolder();
		childHolder.itemReviews = (TextView) subView
				.findViewById(R.id.tvCrtRstReview);
		setTextFontChild(childHolder);
		subView.setTag(childHolder);
		ViewChildHolder newChaildHlder = (ViewChildHolder) subView.getTag();
		newChaildHlder.itemReviews.setText(getChild(groupPosition,
				childPosition).getReviews());

		return subView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	class ViewGroupHolder {
		TextView itemName, tvNewTag, tvCriticRatingReviewList, textTotalRatingReviewList;
	}

	class ViewChildHolder {
		TextView itemReviews;
	}

	private void setTextFontGroup(ViewGroupHolder holder) {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/avenir-light.ttf";
		Typeface tf1 = Typeface.createFromAsset(context.getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(context.getAssets(), fontPath2);
		Typeface tf3 = Typeface.createFromAsset(context.getAssets(), fontPath3);

		holder.tvNewTag.setTypeface(tf1);
		holder.itemName.setTypeface(tf1);
		holder.tvCriticRatingReviewList.setTypeface(tf3);
		holder.textTotalRatingReviewList.setTypeface(tf3);
	}

	private void setTextFontChild(ViewChildHolder holder) {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf1 = Typeface.createFromAsset(context.getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(context.getAssets(), fontPath2);

		holder.itemReviews.setTypeface(tf2);
	}

}
