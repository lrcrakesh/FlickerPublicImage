/*
 * This SingleViewActivity.java file is for showing title, back button and large image of user selection
 */



package com.astegic.flickerpublicimage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleViewActivity extends Activity {

	
	TextView textTitleView;
	ImageView imageView;
	Bitmap bmp;

	private Handler imageHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			imageView.setImageBitmap(bmp);

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_detail);

		Intent intent = getIntent();
		String imageTitle = intent.getStringExtra("imageTitle");
		final String imageURL = intent.getStringExtra("imageURL");

		textTitleView = (TextView) findViewById(R.id.textViewName);
		textTitleView.setSelected(true);
		textTitleView.setText(imageTitle);

		imageView = (ImageView) findViewById(R.id.imageView);

		new Thread(new Runnable() {
			public void run() {
				bmp = FlickerImage.BitmapLoader.preloadBitmap(imageURL);
				imageHandler.sendEmptyMessage(1);

			}
		}).start();

		ImageButton back = (ImageButton) findViewById(R.id.imageButtonBack);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});
	}
}
