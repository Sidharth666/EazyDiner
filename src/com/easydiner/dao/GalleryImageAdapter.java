package com.easydiner.dao;

import java.net.URI;
import java.util.ArrayList;

import com.classes.SquareImageView;
import com.easydiner.R;
import com.easydiner.dao.ItemlistAdapter.ViewHolder;
import com.easydiner.dataobject.GalleryImageList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GalleryImageAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<GalleryImageList> galleryList;
	private LayoutInflater inflate;
	
	public GalleryImageAdapter(Context context, ArrayList<GalleryImageList> galleryList)
	{
		this.context = context;
		this.galleryList = galleryList;
		inflate = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return galleryList.size();
	}

	@Override
	public GalleryImageList getItem(int position) {
		// TODO Auto-generated method stub
		return galleryList.get(position);
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
			
			rowView = inflate.inflate(R.layout.galleryimageitem, null);
			Holder holder = new Holder();
			holder.galleryImg = (SquareImageView)rowView.findViewById(R.id.sivGalleryImage);
			//holder.cbSelectImage = (CheckBox)rowView.findViewById(R.id.cbSelectImage);
			rowView.setTag(holder);
		
		Holder newHolder = (Holder) rowView.getTag();
		newHolder.galleryImg.setImageBitmap(decodeSampledBitmapFromResource(getItem(position).getImageAbsPath(), 100, 100));
		//newHolder.galleryImg.setImageBitmap(BitmapFactory.decodeFile(getItem(position).getImageAbsPath()));
		getItem(position).setItemView(rowView);
		return rowView;
	}
	
	private class Holder
	{
		private SquareImageView galleryImg;
		private CheckBox cbSelectImage;
	}
	
	public static int calculateInSampleSize( 
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image 
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	 
	    if (height > reqHeight || width > reqWidth) {
	 
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	 
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both 
	        // height and width larger than the requested height and width. 
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        } 
	    } 
	 
	    return inSampleSize;
	}
	
	public static Bitmap decodeSampledBitmapFromResource(String resPath,
	        int reqWidth, int reqHeight) {
	 
	    // First decode with inJustDecodeBounds=true to check dimensions 
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(resPath, options);
	 
	    // Calculate inSampleSize 
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	 
	    // Decode bitmap with inSampleSize set 
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(resPath, options);
	}

}
