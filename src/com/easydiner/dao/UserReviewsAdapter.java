package com.easydiner.dao;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.classes.CircularImageView;
import com.easydiner.R;
import com.easydiner.dataobject.UserReviewList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class UserReviewsAdapter extends BaseAdapter {

	private Context context;
	ArrayList<UserReviewList> arrayList;
	LayoutInflater inflater;
	int posn;
	private ImageLoader imageLoader;
	private String monthArray[] = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
			"JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };

	public UserReviewsAdapter(Context context,
			ArrayList<UserReviewList> arrayList) {
		this.context = context;
		this.arrayList = arrayList;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public UserReviewList getItem(int position) {
		// TODO Auto-generated method stub
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ViewHolder") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = convertView;

		rowView = inflater.inflate(R.layout.user_reviews_item, null);
		ViewHolder holder = new ViewHolder();
		holder.userImage = (CircularImageView) rowView.findViewById(R.id.ivProfileImg);
		holder.userDate = (TextView) rowView.findViewById(R.id.tvRewiewdate);
		
		holder.userReviews = (TextView) rowView.findViewById(R.id.tvReviewText);
		holder.userReviewsHeading = (TextView) rowView
				.findViewById(R.id.tvReviewHeading);
		holder.ivUserRating = (ImageView) rowView.findViewById(R.id.ivUserRating);

		posn = position;

		rowView.setTag(holder);

		final ViewHolder newHolder = (ViewHolder) rowView.getTag();
		
		String userReview = getItem(position).getUserReviews();
		if(getItem(position).getUserReviews().length() > 100)
		{
			userReview = userReview.substring(0, 90);
			userReview += "..>>";
		}
		
		newHolder.userReviews.setText(userReview);
		String arrDate[] = getItem(position).getReviewDate().split("-");

		newHolder.userDate
				.setText(monthArray[(Integer.parseInt(arrDate[1]) - 1)] + " "
						+ arrDate[0]);
		newHolder.userReviewsHeading.setText("\""+getItem(position)
				.getReviewSubject()+"\"");

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true)
				.showImageOnLoading(R.drawable.default_image_details)
				.showImageOnFail(R.drawable.default_image_details)
				.showImageForEmptyUri(R.drawable.default_image_details)
				.delayBeforeLoading(100).build();

		imageLoader.displayImage(getItem(position).getUserImage(),
				newHolder.userImage, options, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub
						Animation anim = AnimationUtils.loadAnimation(context,
								android.R.anim.fade_in);
						newHolder.userImage.setAnimation(anim);
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
		
		switch (getItem(position).getUserRating()) {
		case 0:
			newHolder.ivUserRating
					.setImageResource(R.drawable.zero_star);
			break;

		case 1:
			newHolder.ivUserRating
					.setImageResource(R.drawable.one_star);
			break;

		case 2:
			newHolder.ivUserRating
					.setImageResource(R.drawable.two_star);
			break;

		case 3:
			newHolder.ivUserRating
					.setImageResource(R.drawable.three_star);
			break;

		case 4:
			newHolder.ivUserRating
					.setImageResource(R.drawable.four_star);
			break;

		case 5:
			newHolder.ivUserRating
					.setImageResource(R.drawable.five_star);
			break;

		default:
			break;
		}

		return rowView;
	}

	class ViewHolder {
		CircularImageView userImage;
		TextView userDate, userReviews, userReviewsHeading;
		ImageView ivUserRating;
	}

}
