package com.easydiner.dao;

import java.util.ArrayList;

import com.easydiner.R;
import com.easydiner.dataobject.RestorentItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("ViewHolder") public class RestorantAdaptor extends BaseAdapter {

	private Context context;
	private ArrayList<RestorentItem> restorentItems;
	LayoutInflater inflater;
	
	public RestorantAdaptor(Context context, ArrayList<RestorentItem> restorentItems) {
		// TODO Auto-generated constructor stub
		
		this.context = context;
		this.restorentItems = restorentItems;
		inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			holder.restName = (TextView)rowView.findViewById(R.id.tvRestNameList);
			
			rowView.setTag(holder);
		//Log.e("adp", getItem(position).getName());
		
		ViewHolder newHolder = (ViewHolder)rowView.getTag();
		newHolder.restName.setText(getItem(position).getName());
		
		return rowView;
	}
	
	class ViewHolder
	{
		TextView restName;
	}

}
