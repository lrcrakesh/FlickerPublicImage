package com.astegic.flickerpublicimage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends Activity {

	FlickerImage[] myFlickrImage;
	EditText searchText;
	Button searchButton;
	GridView gridView;
	Bitmap bmFlickr;
	TextView qResult;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		searchText = (EditText) findViewById(R.id.searchtext);
		searchButton = (Button) findViewById(R.id.searchbutton);
		qResult = (TextView) findViewById(R.id.queryresult);
		gridView = (GridView) findViewById(R.id.gridView);

		initializeGrid();
		if(searchButton.isEnabled()){
			searchButton.setOnClickListener(searchButtonOnClickListener);
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		
		super.onConfigurationChanged(newConfig);
		UpadetNumOfCOlms();
	}
	
	public int getNumColumns() {
		DisplayMetrics dm = this.getResources().getDisplayMetrics();

		int totalSize = 0;
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			int minSize =ConstantValues.GRIDVIEW_IMAGE_WIDTH
					+ ConstantValues.GRIDVIEW_IMAGE_SPACING;
			totalSize = dm.widthPixels / minSize;
		} else {
			int minSize = ConstantValues.GRIDVIEW_IMAGE_WIDTH
					+ ConstantValues.GRIDVIEW_IMAGE_SPACING;
			totalSize = dm.widthPixels / minSize;
		}
		return totalSize;
	}
	
	private void UpadetNumOfCOlms()
	{
		if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			//gridView.setNumColumns(ConstantValues.GRIDVIEW_NO_COLUMN_LAND);
			gridView.setNumColumns(getNumColumns());
		}
		else
		{
			//gridView.setNumColumns( ConstantValues.GRIDVIEW_NO_COLUMN_POTRAIT);
			gridView.setNumColumns( getNumColumns());
		}
	}
	
	private void initializeGrid() {
		float spacing = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				ConstantValues.GRIDVIEW_SPACING, getResources().getDisplayMetrics());
		
		UpadetNumOfCOlms();
		
		gridView.setColumnWidth(ConstantValues.GRIDVIEW_COLUMN_WIDTH);
		gridView.setPadding((int) spacing, (int) spacing, (int) spacing, (int) spacing);
		gridView.setVerticalSpacing((int) spacing);
		gridView.setHorizontalSpacing((int) spacing);
	}
	
	private void hideKeyboard() {   
	    // Check if no view has focus:
		InputMethodManager inputMgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		EditText editText = (EditText)findViewById(R.id.searchtext);
		inputMgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);		
	}


	private Button.OnClickListener searchButtonOnClickListener = new Button.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
		
			//searchText.setFocusable(false);
			hideKeyboard();
			String searchQ = searchText.getText().toString().trim();
			if(  (searchQ.isEmpty())|| (searchQ.equalsIgnoreCase("Text of Photo To Search"))){

				qResult.setText(R.string.no_input);
				qResult.setVisibility(View.VISIBLE);
				return;
			}
			searchQ = searchQ.replaceAll(" ","+"); // to replace space from search string
			
			FlickerNetDownload FlickerNetDownloadTask = new FlickerNetDownload();
			FlickerNetDownloadTask.execute(searchQ);
			

		}
	};
	
	private class FlickerNetDownload extends AsyncTask<String, Integer, FlickerImage[]>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			qResult.setText(R.string.loading_img);
			qResult.setVisibility(View.VISIBLE);
			hideKeyboard();
			searchButton.setEnabled(false);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			
			qResult.setText("Loading images from Flickr %s/%s. Please wait..."+ values[0]+ " "+ values[1]);
		}

		@Override
		protected FlickerImage[] doInBackground(String... params) {
			String searchResult = QueryToFlickr(params[0]);

			myFlickrImage = ParseJSON(searchResult);

			return myFlickrImage;
		}
		
		@Override
		protected void onPostExecute(FlickerImage[] fImageArr) {
			
		
			
			if(null == fImageArr){
				qResult.setText(R.string.no_result);
			}else{
				qResult.setVisibility(View.INVISIBLE);
				gridView.setAdapter(new FlickerAdapter(MainActivity.this,
						fImageArr));
			}
			searchButton.setEnabled(true);
			super.onPostExecute(fImageArr);
			
		}
		
	}

	private String QueryToFlickr(String q) {

		String qResult = null;

		String qString = ConstantValues.FlickrQuery_url + ConstantValues.FlickrQuery_per_page
				+ ConstantValues.FlickrQuery_nojsoncallback + ConstantValues.FlickrQuery_format
				+ ConstantValues.FlickrQuery_tag + q + ConstantValues.FlickrQuery_key + ConstantValues.FlickrApiKey;

		HttpClient httpClient = new DefaultHttpClient();
		
		HttpGet httpGet = new HttpGet(qString);

		try {
			HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				Reader in = new InputStreamReader(inputStream);
				BufferedReader bufferedreader = new BufferedReader(in);
				StringBuilder stringBuilder = new StringBuilder();

				String stringReadLine = null;

				while ((stringReadLine = bufferedreader.readLine()) != null) {
					stringBuilder.append(stringReadLine + "\n");
				}

				qResult = stringBuilder.toString();
				inputStream.close();
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return qResult;
	}

	private FlickerImage[] ParseJSON(String json) {

		FlickerImage[] flickrImage = null;

		bmFlickr = null;
		String flickrId;
		String flickrOwner;
		String flickrSecret;
		String flickrServer;
		String flickrFarm;
		String flickrTitle;

		try {
			JSONObject JsonObject = new JSONObject(json);
			JSONObject Json_photos = JsonObject.getJSONObject("photos");
			JSONArray JsonArray_photo = Json_photos.getJSONArray("photo");

			flickrImage = new FlickerImage[JsonArray_photo.length()];
			for (int i = 0; i < JsonArray_photo.length(); i++) {
				JSONObject FlickrPhoto = JsonArray_photo.getJSONObject(i);
				flickrId = FlickrPhoto.getString("id");
				flickrOwner = FlickrPhoto.getString("owner");
				flickrSecret = FlickrPhoto.getString("secret");
				flickrServer = FlickrPhoto.getString("server");
				flickrFarm = FlickrPhoto.getString("farm");
				flickrTitle = FlickrPhoto.getString("title");
				flickrImage[i] = new FlickerImage(flickrId, flickrOwner,
						flickrSecret, flickrServer, flickrFarm, flickrTitle);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flickrImage;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public FlickerImage[] getFlickerImage(){
		return myFlickrImage;
	}

}
