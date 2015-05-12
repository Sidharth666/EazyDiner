package com.easydiner.dao;

import java.util.ArrayList;
import java.util.List;

import com.easydiner.R;
import com.easydiner.dao.RestorantAdaptor.ViewHolder;
import com.easydiner.dataobject.RestorentItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint("ViewHolder") public class SecondResturentAdaptor extends ArrayAdapter<RestorentItem> {

	Context context;
	ArrayList<RestorentItem> arrayList;
	LayoutInflater inflater;
	
	public SecondResturentAdaptor(Context context, ArrayList<RestorentItem> arrayList) {
		 super(context, 0, arrayList);
		// TODO Auto-generated constructor stub
		 this.context = context;
		 this.arrayList = arrayList;
		 inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	class ViewHolder
	{
		TextView restName;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = convertView;
		
			rowView = inflater.inflate(R.layout.restorent_list_item, null);
			ViewHolder holder = new ViewHolder();
			holder.restName = (TextView)rowView.findViewById(R.id.tvRestNameList);
			
			rowView.setTag(holder);
	
		ViewHolder newHolder = (ViewHolder)rowView.getTag();
		newHolder.restName.setText(getItem(position).getName());
        
        return rowView;
	}
}
