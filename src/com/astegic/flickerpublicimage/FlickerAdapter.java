/*
 * This FlickerAdapter.java file is adapter providing image data and setting on click listner
 */

package com.astegic.flickerpublicimage;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FlickerAdapter extends BaseAdapter {

	private Context context;
	private FlickerImage[] FlickrAdapterImage;
	
	
	FlickerAdapter(Context c, FlickerImage[] fImage) {
		context = c;
		FlickrAdapterImage = fImage;
	}

	public int getCount() {

		return FlickrAdapterImage.length;
	}

	public Object getItem(int position) {

		return FlickrAdapterImage[position];
	}

	public long getItemId(int position) {

		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView image;
		if (convertView == null) {
			image = new ImageView(context);
		} else {
			image = (ImageView) convertView;
		}
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ConstantValues.GRIDVIEW_IMAGE_WIDTH, ConstantValues.GRIDVIEW_IMAGE_HEIGHT);
		image.setLayoutParams(layoutParams);
		image.setPadding(ConstantValues.GRIDVIEW_IMAGE_SPACING,ConstantValues.GRIDVIEW_IMAGE_SPACING,ConstantValues.GRIDVIEW_IMAGE_SPACING,ConstantValues.GRIDVIEW_IMAGE_SPACING);
		image.setScaleType(ImageView.ScaleType.FIT_XY);		
		image.setImageBitmap(FlickrAdapterImage[position].getBitmap());
		image.setOnClickListener(new ImageGridViewCellOnClickListener(position));

		return image;

	}

	class ImageGridViewCellOnClickListener implements View.OnClickListener {
		private int position;

		public ImageGridViewCellOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			
			String imageURL = FlickrAdapterImage[position].getImageURL();
			String title = FlickrAdapterImage[position].getTitle();
			
			Intent intent = new Intent(context, SingleViewActivity.class);
			intent.putExtra("imageTitle", title);
			intent.putExtra("imageURL", imageURL);
			context.startActivity(intent);
		}
		public int getPosition(){
			return position;
		}
	}
}
