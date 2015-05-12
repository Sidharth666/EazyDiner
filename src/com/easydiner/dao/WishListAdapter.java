package com.easydiner.dao;

import java.util.ArrayList;
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
import com.easydiner.dataobject.WishlistItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class WishListAdapter extends BaseAdapter {

	private Context _context;
	private ArrayList<WishlistItem> arrayList;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;

	public WishListAdapter(Context context, ArrayList<WishlistItem> arrayList) {
		this._context = context;
		this.arrayList = arrayList;
		inflater = (LayoutInflater) this._context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public WishlistItem getItem(int position) {
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

			rowView = inflater.inflate(R.layout.wishlist_item, null);
			ViewHolder holder = new ViewHolder();
			holder.listTitle = (TextView) rowView
					.findViewById(R.id.tvWishListListHeading);
			holder.listImg = (ImageView) rowView
					.findViewById(R.id.imgItemlistWishList);

			setTextFont(holder);

			rowView.setTag(holder);
		}

		final ViewHolder newHolder = (ViewHolder) rowView.getTag();
		newHolder.listTitle.setText(getItem(position).getListTitle());
		newHolder.listImg.setScaleType(ImageView.ScaleType.FIT_XY);
		@SuppressWarnings("deprecation")
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true)
				.showImageOnLoading(R.drawable.default_eazytrands_img)
				.showStubImage(R.drawable.default_eazytrands_img)
				.delayBeforeLoading(200).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.default_eazytrands_img)
				.build();

		imageLoader.displayImage(getItem(position).getListImage(),
				newHolder.listImg, options, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub
						Animation anim = AnimationUtils.loadAnimation(_context,
								android.R.anim.fade_in);
						newHolder.listImg.setAnimation(anim);
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
		ImageView listImg;
		TextView listTitle;
	}

	private void setTextFont(ViewHolder holder) {

		String fontPath1 = "fonts/Aller_Rg.ttf";

		Typeface tf1 = Typeface
				.createFromAsset(_context.getAssets(), fontPath1);

		holder.listTitle.setTypeface(tf1);

	}
}
