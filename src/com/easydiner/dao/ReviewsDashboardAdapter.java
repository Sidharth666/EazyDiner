package com.easydiner.dao;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.easydiner.R;
import com.easydiner.dao.ExpItemListAdapter.ViewChildHolder;
import com.easydiner.dao.ExpItemListAdapter.ViewGroupHolder;
import com.easydiner.dataobject.ReviewsDashboardChildItem;
import com.easydiner.dataobject.ReviewsDashboardItem;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ReviewsDashboardAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<ReviewsDashboardItem> dashboardItems;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;

	public ReviewsDashboardAdapter(Context context,
			ArrayList<ReviewsDashboardItem> dashboardItems) {
		this.context = context;
		this.dashboardItems = dashboardItems;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return dashboardItems.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return dashboardItems.get(groupPosition).getChildList().size();
	}

	@Override
	public ReviewsDashboardItem getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return dashboardItems.get(groupPosition);
	}

	@Override
	public ReviewsDashboardChildItem getChild(int groupPosition,
			int childPosition) {
		// TODO Auto-generated method stub
		return dashboardItems.get(groupPosition).getChildList()
				.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
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
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.dashboard_review_group_list,
					null);
			ViewGroupHolder holder = new ViewGroupHolder();
			holder.tvReviewedRestName = (TextView) rowView
					.findViewById(R.id.tvReviewedRestName);
			setTextFontForGroup(holder);
			rowView.setTag(holder);
		}
		final ViewGroupHolder newHolder = (ViewGroupHolder) rowView.getTag();
		newHolder.tvReviewedRestName.setText(getGroup(groupPosition)
				.getItemTitle());
		return rowView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View subView = convertView;
		if (subView == null) {
			subView = inflater.inflate(R.layout.reviews_dashboard_layout, null);
			ViewChildHolder childHolder = new ViewChildHolder();
			childHolder.tvReviewReviewDasboard = (TextView) subView
					.findViewById(R.id.tvReviewReviewDasboard);
			setTextFontForChild(childHolder);
			subView.setTag(childHolder);
		}
		ViewChildHolder newChaildHlder = (ViewChildHolder) subView.getTag();
		newChaildHlder.tvReviewReviewDasboard.setText(getChild(groupPosition,
				childPosition).getReview());
		return subView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	class ViewGroupHolder {

		TextView tvReviewedRestName;
	}

	class ViewChildHolder {

		TextView tvReviewReviewDasboard;
	}

	private void setTextFontForGroup(ViewGroupHolder holder) {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/Aller_Lt.ttf";

		Typeface tf1 = Typeface.createFromAsset(context.getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(context.getAssets(), fontPath2);
		Typeface tf3 = Typeface.createFromAsset(context.getAssets(), fontPath3);

		holder.tvReviewedRestName.setTypeface(tf2);
	}

	private void setTextFontForChild(ViewChildHolder holder) {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/Aller_Lt.ttf";

		Typeface tf1 = Typeface.createFromAsset(context.getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(context.getAssets(), fontPath2);
		Typeface tf3 = Typeface.createFromAsset(context.getAssets(), fontPath3);

		holder.tvReviewReviewDasboard.setTypeface(tf3);
	}

}
