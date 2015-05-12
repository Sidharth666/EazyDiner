package com.easydiner.dao;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageGalleryAdapter extends PagerAdapter {
	
	private Context context;
	private ArrayList<String> imgUrl;
	
	public ImageGalleryAdapter(Context context, ArrayList<String> imgUrl) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.imgUrl = imgUrl;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgUrl.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == ((SubsamplingScaleImageView) object);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
						.cacheOnDisk(true)
						.resetViewBeforeLoading(true)
						.cacheInMemory(true)
						.build();
		
		SubsamplingScaleImageView subsamplingScaleImageView = new SubsamplingScaleImageView(context);
		subsamplingScaleImageView.setImage(ImageSource.uri(imgUrl.get(position)));
		/*ImageView imageView = new ImageView(context);
		
		imageLoader.displayImage(imgUrl.get(position), subsamplingScaleImageView, options);*/
		
		((ViewPager) container).addView(subsamplingScaleImageView, 0);
		
		return subsamplingScaleImageView;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		//super.destroyItem(container, position, object);
		((ViewPager) container).removeView((ImageView) object);
	}

}
