package com.easydiner.fragment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.easydiner.R;

public class ImageZoomGalleryFragment extends Fragment {

	private String galleryImage, restaurantId;
	private int page;
	/* private SubsamplingScaleImageView subScalingImageviewItem; */
	private SubsamplingScaleImageView subScalingImageviewItem;
	ImageZoomGalleryFragment fragmentObj;
	private ViewGroup rootView;
	private Bitmap imgBitmap;
	private String iconsStoragePathFile, iconsStoragePathDir;
	private File sdIconStorageDir, sdIconStorageFile;
	private boolean isMenu;

	// newInstance constructor for creating fragment with arguments
	public static ImageZoomGalleryFragment newInstance(String galleryImage,int page, String restaurantId, boolean isMenu) {
		ImageZoomGalleryFragment fragmentObj = new ImageZoomGalleryFragment();
		Bundle args = new Bundle();
		args.putString("galleryImage", galleryImage);
		args.putInt("pageNo", page);
		args.putString("restaurantId", restaurantId);
		args.putBoolean("isMenu", isMenu);
		fragmentObj.setArguments(args);
		return fragmentObj;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		galleryImage = getArguments().getString("galleryImage");
		page = getArguments().getInt("pageNo");
		restaurantId = getArguments().getString("restaurantId");
		isMenu = getArguments().getBoolean("isMenu");
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = (ViewGroup) inflater.inflate(R.layout.image_gallery_fragment_layout, container, false);
		initialize();
		iconsStoragePathFile = Environment.getExternalStorageDirectory()
				+ File.separator + getActivity().getPackageName()
				+ File.separator + "cache_" + restaurantId + File.separator
				+ isMenu + page;
		iconsStoragePathDir = Environment.getExternalStorageDirectory()
				+ File.separator + getActivity().getPackageName()
				+ File.separator + "cache_" + restaurantId;
		sdIconStorageDir = new File(iconsStoragePathDir);
		sdIconStorageFile = new File(iconsStoragePathFile);
		if (!sdIconStorageFile.isFile()) {
			Log.v("exist", "not");
			AstClassLoadImage astClassLoadImage = new AstClassLoadImage();
			astClassLoadImage.execute("");
		} else {
			Log.v("exist", "yes");
			String path = sdIconStorageFile.toString().replaceAll(" ", "%20");
			try {
				subScalingImageviewItem.setImage(ImageSource.uri(path));
			} catch (Exception e) {
				Toast.makeText(getActivity(), "Image format not supporting",Toast.LENGTH_LONG).show();
			}
		}

		return rootView;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		rootView = null;
		subScalingImageviewItem = null;
		
	}

	private void initialize() {
		subScalingImageviewItem = (SubsamplingScaleImageView) rootView.findViewById(R.id.subScalingImageviewItem);
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);//getBitmap(connection);
			return myBitmap;
		} catch (Exception e) {
			return null;
		}
	}
	
	private static Bitmap getBitmap(HttpURLConnection connection) {

		InputStream in = null;
		try {
		    final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
		    in = connection.getInputStream();

		    // Decode image size
		    BitmapFactory.Options o = new BitmapFactory.Options();
		    o.inJustDecodeBounds = true;
		    BitmapFactory.decodeStream(in, null, o);
		    in.close();



		    int scale = 1;
		    while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
		       scale++;
		    }
		    Log.d("TAG", "scale = " + scale + ", orig-width: " + o.outWidth + ",  orig-height: " + o.outHeight);

		    Bitmap b = null;
		    in = connection.getInputStream();
		    if (scale > 1) {
		        scale--;
		        // scale to max possible inSampleSize that still yields an image
		        // larger than target
		        o = new BitmapFactory.Options();
		        o.inSampleSize = scale;
		        b = BitmapFactory.decodeStream(in, null, o);

		        // resize to desired dimensions
		        int height = b.getHeight();
		        int width = b.getWidth();
		        Log.d("TAG", "1th scale operation dimenions - width: " + width + ", height: " + height);

		        double y = Math.sqrt(IMAGE_MAX_SIZE/ (((double) width) / height));
		        double x = (y / height) * width;

		        Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x, (int) y, true);
		        b.recycle();
		        b = scaledBitmap;
		        in.close();
		        System.gc();
		    } else {
		        b = BitmapFactory.decodeStream(in);
		    }
		    in.close();

		    Log.d("TAG", "bitmap size - width: " +b.getWidth() + ", height: " + 
		       b.getHeight());
		    return b;
		} catch (Exception e) {
		    Log.e("TAG", e.getMessage(),e);
		    return null;
		}}

	private String storeImage(Bitmap imageData) {
		// get path to external storage (SD card)
		if (!sdIconStorageDir.isDirectory()) {
			sdIconStorageDir.mkdirs();
		}
		String filePath = "";

		try {
			filePath = sdIconStorageFile.toString();
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);

			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

			// choose another format if PNG doesn't suit you
			imageData.compress(CompressFormat.PNG, 100, bos);

			bos.flush();
			bos.close();

		} catch (FileNotFoundException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());

		} catch (IOException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());

		} catch (Exception e) {
			Toast.makeText(getActivity(), "Image format not supported. Retry later.",Toast.LENGTH_LONG).show();

		}

		return filePath;
	}

	private class AstClassLoadImage extends AsyncTask<String, String, Long> {

		@SuppressWarnings("static-access")
		@Override
		protected Long doInBackground(String... params) {
			try {
				
					imgBitmap = getBitmapFromURL(galleryImage);

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
		if(imgBitmap!=null){
			String imgPath = storeImage(imgBitmap).replaceAll(" ", "%20");
			Log.v("filepath", imgPath);
			try {
				subScalingImageviewItem.setImage(ImageSource.uri(imgPath));
			} catch (Exception e) {
				System.out.println("exception"+ e);
//				Toast.makeText(getActivity(), "Image format not supporting",Toast.LENGTH_LONG).show();
			}
			}
			
		}
	}
}
