package com.easydiner.activities;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.classes.Constant;
import com.classes.ImageCompressCropp;
import com.easydiner.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageSelectorActivity extends EasyDinerBaseActivity {

	private ArrayList<String> imageUrls;
	private DisplayImageOptions options;
	private ImageAdapter imageAdapter;
	private ImageCompressCropp _imgCompress;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageselectorlayout);
		final String[] columns = { MediaStore.Images.Media.DATA,
				MediaStore.Images.Media._ID };
		final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
		Cursor imagecursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, orderBy + " DESC");

		this.imageUrls = new ArrayList<String>();
		_imgCompress = new ImageCompressCropp(ImageSelectorActivity.this);

		for (int i = 0; i < imagecursor.getCount(); i++) {
			imagecursor.moveToPosition(i);
			int dataColumnIndex = imagecursor
					.getColumnIndex(MediaStore.Images.Media.DATA);
			imageUrls.add(imagecursor.getString(dataColumnIndex));

			System.out.println("=====> Array path => " + imageUrls.get(i));
		}

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_image_restaurant)
				.showImageForEmptyUri(R.drawable.default_image_restaurant)
				.cacheInMemory(true)
				.cacheInMemory(true).build();

		imageAdapter = new ImageAdapter(this, imageUrls);

		GridView gridView = (GridView) findViewById(R.id.gvImageGallery);
		gridView.setAdapter(imageAdapter);
		/*
		 * gridView.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) { startImageGalleryActivity(position); } });
		 */

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		imageLoader.stop();
		super.onStop();
	}

	@SuppressWarnings("static-access")
	public void btnChoosePhotosClick(View v) {

		ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
		Toast.makeText(ImageSelectorActivity.this,
				"Total photos selected: " + selectedItems.size(),
				Toast.LENGTH_SHORT).show();
		Log.d(ImageSelectorActivity.class.getSimpleName(), "Selected Items: "
				+ selectedItems.toString());
		Constant.imgSelected.clear();
		for (int i = 0; i < selectedItems.size(); i++) {
			_imgCompress.compressImageForPath(selectedItems.get(i));
			Constant.imgSelected.add(_imgCompress.getImageFilePath());
		}
		Log.d(ImageSelectorActivity.class.getSimpleName(), "Selected Items: "
				+ Constant.imgSelected.toString());
		onBackPressed();
	}

	public class ImageAdapter extends BaseAdapter {

		ArrayList<String> mList;
		LayoutInflater mInflater;
		Context mContext;
		SparseBooleanArray mSparseBooleanArray;

		public ImageAdapter(Context context, ArrayList<String> imageList) {
			// TODO Auto-generated constructor stub
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
			mSparseBooleanArray = new SparseBooleanArray();
			mList = new ArrayList<String>();
			this.mList = imageList;

		}

		public ArrayList<String> getCheckedItems() {
			ArrayList<String> mTempArry = new ArrayList<String>();

			for (int i = 0; i < mList.size(); i++) {
				if (mSparseBooleanArray.get(i)) {
					mTempArry.add(mList.get(i));
				}
			}

			return mTempArry;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageUrls.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null) {
                convertView = mInflater.inflate(R.layout.galleryimageitem, null);
            }
 
            CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.sivGalleryImage);
            
            imageLoader.displayImage("file://" + imageUrls.get(position), imageView, options, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					Animation anim = AnimationUtils.loadAnimation(ImageSelectorActivity.this, android.R.anim.fade_in);
                    imageView.setAnimation(anim);
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
 
            mCheckBox.setTag(position);
            mCheckBox.setChecked(mSparseBooleanArray.get(position));
            mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
 
            return convertView;
		}

		OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				mSparseBooleanArray.put((Integer) buttonView.getTag(),
						isChecked);
			}
		};
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
