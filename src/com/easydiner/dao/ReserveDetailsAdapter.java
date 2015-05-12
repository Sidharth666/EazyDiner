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
import com.easydiner.dataobject.ReservDetailsItem;

public class ReserveDetailsAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<ReservDetailsItem> detailsItems;
	private LayoutInflater inflater;

	public ReserveDetailsAdapter(Context context,
			ArrayList<ReservDetailsItem> detailsItems) {
		this.context = context;
		this.detailsItems = detailsItems;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return detailsItems.size();
	}

	@Override
	public ReservDetailsItem getItem(int position) {
		// TODO Auto-generated method stub
		return detailsItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rootView = convertView;
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.reservition_item, null);
			ViewHolder holder = new ViewHolder();

			holder.itemName = (TextView) rootView
					.findViewById(R.id.tvRestNameReserv);
			holder.itemDate = (TextView) rootView
					.findViewById(R.id.tvRestDateReserv);
			holder.itemMonth = (TextView) rootView
					.findViewById(R.id.tvRestMonthReserv);
			setTextFont(holder);
			rootView.setTag(holder);
		}
		ViewHolder newholder = (ViewHolder) rootView.getTag();
		newholder.itemName.setText(getItem(position).getItemName());
		String bookingDate[] = getItem(position).getItemDate().split(" ");
		newholder.itemDate.setText(bookingDate[1]);
		newholder.itemMonth.setText(bookingDate[2].toUpperCase());
		return rootView;
	}

	private class ViewHolder {
		private TextView itemName, itemDate, itemMonth;
	}

	private void setTextFont(ViewHolder holder) {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath5 = "fonts/Aller_Lt.ttf";
		String fontPath3 = "fonts/Aller_LtIt.ttf";
		String fontPath4 = "fonts/avenir-light.ttf";

		Typeface tf1 = Typeface.createFromAsset(context.getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(context.getAssets(), fontPath2);
		Typeface tf3 = Typeface.createFromAsset(context.getAssets(), fontPath3);
		Typeface tf4 = Typeface.createFromAsset(context.getAssets(), fontPath4);
		Typeface tf5 = Typeface.createFromAsset(context.getAssets(), fontPath5);

		holder.itemName.setTypeface(tf2);
		holder.itemDate.setTypeface(tf4);
		holder.itemMonth.setTypeface(tf2);
	}

}
