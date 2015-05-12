package com.easydiner.dao;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easydiner.R;
import com.easydiner.dataobject.KeywordItem;

public class KeywordAdapter extends BaseAdapter {

	Context context;
	ArrayList<KeywordItem> arrayList;
	LayoutInflater inflater;

	public KeywordAdapter(Context context, ArrayList<KeywordItem> arrayList) {
		this.context = context;
		this.arrayList = arrayList;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public KeywordItem getItem(int position) {
		// TODO Auto-generated method stub
		return arrayList.get(position);
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
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.search_option_layout, null);
			ViewHolder holder = new ViewHolder();
			holder.keyWord = (TextView) rowView.findViewById(R.id.textSearchItem);
			holder.textSearchItemLocation = (TextView) rowView.findViewById(R.id.textSearchItemLocation);
			setTextFont(holder);
			
			rowView.setTag(holder);
		}

		ViewHolder newHolder = (ViewHolder) rowView.getTag();
		
		String itemName = getItem(position).getKeyItem();
		
		if(!getItem(position).getKeyLocation().equalsIgnoreCase("")){
			itemName += ", ";
		}	
		newHolder.keyWord.setText(itemName);
		newHolder.textSearchItemLocation.setText(getItem(position).getKeyLocation());

		return rowView;
	}

	private class ViewHolder {
		TextView keyWord, textSearchItemLocation;
	}

	private void setTextFont(ViewHolder holder) {

		String fontPath1 = "fonts/Aller_Lt.ttf";

		Typeface tf2 = Typeface.createFromAsset(context.getAssets(), fontPath1);

		holder.keyWord.setTypeface(tf2);
		holder.textSearchItemLocation.setTypeface(tf2);

	}

}
