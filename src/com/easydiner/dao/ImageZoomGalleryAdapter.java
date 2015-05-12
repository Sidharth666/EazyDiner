package com.easydiner.dao;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.easydiner.fragment.ImageZoomGalleryFragment;

public class ImageZoomGalleryAdapter extends FragmentStatePagerAdapter {

	private ArrayList<String> arrayList;
	private Context _context;
	private String restaurantId;
	private boolean isMenu;

	public ImageZoomGalleryAdapter(FragmentManager fm,ArrayList<String> arrayList, String restaurantId, Context context, boolean isMenu) {
		super(fm);
		this.arrayList = arrayList;
		this.restaurantId = restaurantId;
		this._context = context;
		this.isMenu = isMenu;
	}

	@Override
	public Fragment getItem(int position) {
		return ImageZoomGalleryFragment.newInstance(arrayList.get(position),position, restaurantId, isMenu);
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

}
