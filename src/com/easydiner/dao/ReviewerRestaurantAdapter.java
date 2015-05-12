package com.easydiner.dao;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easydiner.R;
import com.easydiner.dataobject.ReviewerRestaurantListItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ReviewerRestaurantAdapter extends BaseAdapter {

	private Context _context;
	private ArrayList<ReviewerRestaurantListItem> restaurantListItems;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;

	public ReviewerRestaurantAdapter(Context _context,
			ArrayList<ReviewerRestaurantListItem> restaurantListItems) {
		this._context = _context;
		this.restaurantListItems = restaurantListItems;
		inflater = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return restaurantListItems.size();
	}

	@Override
	public ReviewerRestaurantListItem getItem(int position) {
		// TODO Auto-generated method stub
		return restaurantListItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressWarnings("null")
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = inflater.inflate(
				R.layout.reviewer_restaurant_list_layout, null);

		ViewHolder holder = new ViewHolder();
		holder.tvReviewerRestaurantName = (TextView) rowView
				.findViewById(R.id.tvReviewerRestaurantName);
		holder.tvReviewerRestaurantLocation = (TextView) rowView
				.findViewById(R.id.tvReviewerRestaurantLocation);
		holder.tvReviewerRestaurantSpeciality = (TextView) rowView
				.findViewById(R.id.tvReviewerRestaurantSpeciality);
		holder.ivReviewerRestaurantImage = (ImageView) rowView
				.findViewById(R.id.ivReviewerRestaurantImage);
		setTextFont(holder);

		rowView.setTag(holder);

		final ViewHolder newHolder = (ViewHolder) rowView.getTag();

		newHolder.tvReviewerRestaurantName.setText(getItem(position)
				.getItemName());
		newHolder.tvReviewerRestaurantLocation.setText(getItem(position)
				.getItemLocation());
		newHolder.tvReviewerRestaurantSpeciality.setText(getItem(position)
				.getItemSpeciality());
		newHolder.ivReviewerRestaurantImage.setScaleType(ImageView.ScaleType.FIT_XY);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true)
				.showImageOnLoading(R.drawable.default_image_restaurant)
				.showImageForEmptyUri(R.drawable.default_image_restaurant)
				.showImageOnFail(R.drawable.default_image_restaurant)
				.delayBeforeLoading(200)
				.build();
		imageLoader.displayImage(getItem(position).getItemImg(),
				newHolder.ivReviewerRestaurantImage, options,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub
						Animation anim = AnimationUtils.loadAnimation(_context,
								android.R.anim.fade_in);
						newHolder.ivReviewerRestaurantImage.setAnimation(anim);
						anim.start();
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub

					}
				});

		return rowView;
	}

	private class ViewHolder {
		TextView tvReviewerRestaurantName, tvReviewerRestaurantLocation,
				tvReviewerRestaurantSpeciality;
		ImageView ivReviewerRestaurantImage;
	}
	
	private void setTextFont(ViewHolder holder) {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/Aller_Lt.ttf";
		String fontPath4 = "fonts/Aller_LtIt.ttf";

		Typeface tf1 = Typeface.createFromAsset(_context.getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(_context.getAssets(), fontPath2);
		Typeface tf3 = Typeface.createFromAsset(_context.getAssets(), fontPath3);
		Typeface tf4 = Typeface.createFromAsset(_context.getAssets(), fontPath4);
		
		holder.tvReviewerRestaurantName.setTypeface(tf2);
		holder.tvReviewerRestaurantLocation.setTypeface(tf3);
		holder.tvReviewerRestaurantSpeciality.setTypeface(tf4);

	}


}
