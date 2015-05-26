package com.easydiner.dao;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.classes.Constant;
import com.classes.Pref;
import com.easydiner.R;
import com.easydiner.dataobject.ListItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ItemlistAdapter extends BaseAdapter {

	Context context;
	ArrayList<ListItem> arrayList;
	LayoutInflater inflater;
	ImageLoader imageLoader;
	private SharedPreferences _pref;

	public ItemlistAdapter(Context context, ArrayList<ListItem> arrayList, SharedPreferences _pref) {
		this.context = context;
		this.arrayList = arrayList;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getInstance();
		this._pref = _pref;
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public ListItem getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;

		if (rowView == null) {

			rowView = inflater.inflate(R.layout.list_item, null);
			ViewHolder holder = new ViewHolder();
			holder.image = (ImageView) rowView.findViewById(R.id.ivListImage);
			holder.itemName = (TextView) rowView.findViewById(R.id.ivNameList);
			holder.itemLocation = (TextView) rowView.findViewById(R.id.tvLocationList);
			holder.itemType = (TextView) rowView.findViewById(R.id.tvTypeList);
			holder.itemPrice = (ImageView) rowView.findViewById(R.id.ivPriceList);
			holder.itemDistence = (TextView) rowView.findViewById(R.id.tvDistenceList);
			holder.reviewImage = (ImageView) rowView.findViewById(R.id.ivReviewImageList);
			holder.itemCriticRatingList = (TextView) rowView.findViewById(R.id.tvCriticRatingList);
			holder.itemUserRating = (ImageView) rowView.findViewById(R.id.ivUserRatingList);
			holder.llListItem = (LinearLayout) rowView.findViewById(R.id.llListItem);
			holder.textCriticRating = (TextView) rowView.findViewById(R.id.textCriticRating);
			holder.textUserRating = (TextView) rowView.findViewById(R.id.textUserRating);
			holder.textTotalRating = (TextView) rowView.findViewById(R.id.textTotalRating);
			holder.ivPriceList = (ImageView) rowView.findViewById(R.id.ivPriceList);
			holder.ivPinTag = (ImageView) rowView.findViewById(R.id.ivPinTag);
			holder.llListEazydeal = (LinearLayout) rowView.findViewById(R.id.llListEazydeal);
			holder.tvEazydeal = (TextView) rowView.findViewById(R.id.tvEazydeal);
			holder.textEazyDealTag = (TextView) rowView.findViewById(R.id.textEazyDealTag);
			holder.llCriticRatingAll = (LinearLayout) rowView.findViewById(R.id.llCriticRatingAll);
			holder.llDistenceLayout = (LinearLayout) rowView.findViewById(R.id.llDistenceLayout);

			setTextFont(holder);

			rowView.setTag(holder);
		}

		final ViewHolder newHolder = (ViewHolder) rowView.getTag();
		newHolder.itemName.setText(getItem(position).getItemname());
		newHolder.itemLocation.setText(getItem(position).getItemlocation());
		newHolder.itemType.setText(getItem(position).getItemtype());

		if (!getItem(position).getItemdistence().equalsIgnoreCase("null")
				&& !getItem(position).getItemdistence().equals("")) {
			try {
				float dst = Math.round(Float.parseFloat(getItem(position).getItemdistence()) * 100) / 100;
				if(_pref.getString("SubCatId", "").contains("NEARBY")){
					newHolder.itemDistence.setText(String.valueOf(dst) + "Km");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
		}
		newHolder.itemCriticRatingList.setText(String.valueOf(getItem(position).getItemCritcsRating()));
		newHolder.itemLocation.setText(getItem(position).getItemlocation());
		if (getItem(position).getReviewed()) {
			newHolder.llListItem.setBackgroundColor(Color.parseColor("#FBE2CF"));
			newHolder.reviewImage.setVisibility(View.VISIBLE);

			newHolder.llCriticRatingAll.setVisibility(View.VISIBLE);
			if (getItem(position).getNewItemStatus().equalsIgnoreCase("false")) {
				newHolder.ivPinTag.setImageResource(R.drawable.filter_image_orange);
			} else {
				newHolder.ivPinTag.setImageResource(R.drawable.filter_image_orange_new);
			}

		} else {
			newHolder.llListItem.setBackgroundColor(Color.parseColor("#E6E6E6"));
			newHolder.reviewImage.setVisibility(View.GONE);
			newHolder.llCriticRatingAll.setVisibility(View.INVISIBLE);
			newHolder.ivPinTag.setImageResource(R.drawable.filter_image_grey);
		}
		/*
		 * newHolder.image.setScaleType(ImageView.ScaleType.FIT_XY);
		 * imageLoader.DisplayImage(getItem(position).getImage(),
		 * newHolder.image);
		 */

		@SuppressWarnings("deprecation")
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
				.showImageForEmptyUri(R.drawable.default_image_restaurant)
				.showImageOnFail(R.drawable.default_image_restaurant)
				.showImageOnLoading(R.drawable.default_image_restaurant)
				.resetViewBeforeLoading(true).delayBeforeLoading(200).build();
		imageLoader.displayImage(getItem(position).getImage(), newHolder.image,
				options, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						Animation anim = AnimationUtils.loadAnimation(context,android.R.anim.fade_in);
						newHolder.image.setAnimation(anim);
						anim.start();
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,FailReason failReason) {

					}

					@Override
					public void onLoadingComplete(String imageUri, View view,Bitmap loadedImage) {

					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {

					}
				});

		if (getItem(position).getItemprice() <= 500) {
			newHolder.ivPriceList.setImageResource(R.drawable.price_1);
		} else if (getItem(position).getItemprice() <= 2000 && getItem(position).getItemprice() >= 501) {
			newHolder.ivPriceList.setImageResource(R.drawable.price_2);
		} else if (getItem(position).getItemprice() <= 6000 && getItem(position).getItemprice() >= 2001) {
			newHolder.ivPriceList.setImageResource(R.drawable.price_3);
		} else {
			newHolder.ivPriceList.setImageResource(R.drawable.price_4);
		}

		switch (getItem(position).getItemUserRating()) {
		case 0:
			newHolder.itemUserRating.setImageResource(R.drawable.zero_star);
			break;

		case 1:
			newHolder.itemUserRating.setImageResource(R.drawable.one_star);
			break;

		case 2:
			newHolder.itemUserRating.setImageResource(R.drawable.two_star);
			break;

		case 3:
			newHolder.itemUserRating.setImageResource(R.drawable.three_star);
			break;

		case 4:
			newHolder.itemUserRating.setImageResource(R.drawable.four_star);
			break;

		case 5:
			newHolder.itemUserRating.setImageResource(R.drawable.five_star);
			break;

		default:
			break;
		}

		if (getItem(position).getItemEazyDeals().equalsIgnoreCase("")) {
			newHolder.llListEazydeal.setVisibility(View.GONE);
		} else {
			newHolder.llListEazydeal.setVisibility(View.VISIBLE);
			newHolder.tvEazydeal.setText("EAZYDEAL: " + getItem(position).getItemEazyDeals());
		}

		if (Constant.GPS_STATUS) {
			newHolder.itemDistence.setVisibility(View.VISIBLE);
		} else {
			newHolder.itemDistence.setVisibility(View.INVISIBLE);
		}

		return rowView;
	}

	private void setTextFont(ViewHolder holder) {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/Aller_LtIt.ttf";
		String fontPath4 = "fonts/avenir-light.ttf";

		Typeface tf1 = Typeface.createFromAsset(context.getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(context.getAssets(), fontPath2);
		Typeface tf3 = Typeface.createFromAsset(context.getAssets(), fontPath3);
		Typeface tf4 = Typeface.createFromAsset(context.getAssets(), fontPath4);

		holder.itemName.setTypeface(tf1);
		holder.itemLocation.setTypeface(tf3);
		holder.itemType.setTypeface(tf2);
		holder.itemDistence.setTypeface(tf2);
		holder.textCriticRating.setTypeface(tf2);
		holder.textUserRating.setTypeface(tf2);
		holder.textTotalRating.setTypeface(tf4);
		holder.itemCriticRatingList.setTypeface(tf4);
		holder.textEazyDealTag.setTypeface(tf2);
		holder.tvEazydeal.setTypeface(tf2);
	}

	class ViewHolder {
		ImageView reviewImage, itemPrice, itemUserRating, ivPriceList,ivPinTag, image;
		// SquareImageView image;
		TextView itemName, itemType, itemLocation, itemDistence,
				itemCriticRatingList, textUserRating, textCriticRating,
				textTotalRating, tvEazydeal, textEazyDealTag;
		LinearLayout llListItem, llListEazydeal, llCriticRatingAll,
				llDistenceLayout;
	}
}
