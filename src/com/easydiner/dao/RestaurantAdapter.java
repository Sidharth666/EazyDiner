package com.easydiner.dao;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easydiner.R;
import com.easydiner.dao.ItemlistAdapter.ViewHolder;
import com.easydiner.dataobject.RestorentItem;

@SuppressLint("ViewHolder")
public class RestaurantAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<RestorentItem> restorentItems;
	LayoutInflater inflater;

	public RestaurantAdapter(Context context,ArrayList<RestorentItem> restorentItems) {
		// TODO Auto-generated constructor stub

		this.context = context;
		this.restorentItems = restorentItems;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return restorentItems.size();
	}

	@Override
	public RestorentItem getItem(int position) {
		// TODO Auto-generated method stub
		return restorentItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = convertView;

		rowView = inflater.inflate(R.layout.restorent_list_item, null);
		ViewHolder holder = new ViewHolder();
		holder.restName = (TextView) rowView.findViewById(R.id.tvRestNameList);
		holder.tvRestLocationList = (TextView) rowView.findViewById(R.id.tvRestLocationList);
		setTextFont(holder);
		rowView.setTag(holder);
		// Log.e("adp", getItem(position).getName());

		ViewHolder newHolder = (ViewHolder) rowView.getTag();
		newHolder.restName.setText(getItem(position).getName() + ", ");
		newHolder.tvRestLocationList.setText(getItem(position).getLocation());

		return rowView;
	}

	class ViewHolder {
		TextView restName, tvRestLocationList;
	}
	
	private void setTextFont(ViewHolder holder) {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/Aller_Lt.ttf";

		Typeface tf1 = Typeface.createFromAsset(context.getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(context.getAssets(), fontPath2);
		Typeface tf3 = Typeface.createFromAsset(context.getAssets(), fontPath3);
		
		holder.restName.setTypeface(tf3);
		holder.tvRestLocationList.setTypeface(tf3);
	}	

}
