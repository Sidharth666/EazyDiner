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
import com.easydiner.dataobject.EazyTrendsListItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class EazyTrendsListAdapter extends BaseAdapter {

	Context context;
	ArrayList<EazyTrendsListItem> arrayList;
	LayoutInflater inflater;
	ImageLoader imageLoader;

	public EazyTrendsListAdapter(Context context,
			ArrayList<EazyTrendsListItem> arrayList) {
		// TODO Auto-generated constructor stub
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
	public EazyTrendsListItem getItem(int position) {
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

			rowView = inflater.inflate(R.layout.eazy_trends_listitem, null);
			ViewHolder holder = new ViewHolder();
			holder.listImg = (ImageView) rowView
					.findViewById(R.id.imgItemlistEazyTrends);
			holder.listTitle = (TextView) rowView
					.findViewById(R.id.tvEazyTrendsListHeading);
			holder.listItemBody = (TextView) rowView
					.findViewById(R.id.tvEazyTrendsListBody);
			setTextFont(holder);

			rowView.setTag(holder);
		}

		final ViewHolder newHolder = (ViewHolder) rowView.getTag();
		String title = "";
		String body = "";
		if(getItem(position).getListTitle().length() <= 25){
			title = getItem(position).getListTitle();
		} else{
			title = getItem(position).getListTitle().substring(0, 22);
			title = title + "..";
		}
		
		if(getItem(position).getListBody().length() <= 31){
			body = getItem(position).getListBody();
		} else{
			body = getItem(position).getListBody().substring(0, 30);
			body = body + "..";
		}
		newHolder.listTitle.setText(title);
		newHolder.listItemBody.setText(body);
		newHolder.listImg.setScaleType(ImageView.ScaleType.FIT_XY);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
										.cacheInMemory(true)
										.cacheOnDisk(true)
										.showImageOnLoading(R.drawable.default_eazytrands_img)
										.showImageOnFail(R.drawable.default_eazytrands_img)
										.showImageForEmptyUri(R.drawable.default_eazytrands_img)
										.delayBeforeLoading(200)
										.resetViewBeforeLoading(true)
										.build();
		
		imageLoader.displayImage(getItem(position).getListImg(), newHolder.listImg, options, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				Animation anim = AnimationUtils.loadAnimation(context,
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
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});

		return rowView;
	}

	class ViewHolder {
		ImageView listImg;
		TextView listTitle, listItemBody;
	}

	private void setTextFont(ViewHolder holder) {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf1 = Typeface.createFromAsset(context.getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(context.getAssets(), fontPath2);

		holder.listTitle.setTypeface(tf1);
		holder.listItemBody.setTypeface(tf2);
	}

}
