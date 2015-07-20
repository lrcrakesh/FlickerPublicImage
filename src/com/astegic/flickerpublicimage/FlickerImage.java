/* This class is for storing fetched image data and getting bmp image
 *  this represent the attribute and image in flicker
 * 
 */


package com.astegic.flickerpublicimage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FlickerImage {

	private String fId;
	private String fOwner;
	private String fSecret;
	private String fServer;
	private String fFarm;
	private String fTitle;
	private String imageURL;

	Bitmap fBitmap;

	FlickerImage(String id, String owner, String secret, String server,
			String farm, String title) {

		fId = id;
		fOwner = owner;
		fSecret = secret;
		fServer = server;
		fFarm = farm;
		fTitle = title;
		imageURL = "http://farm" + fFarm + ".static.flickr.com/"
				+ fServer + "/" + fId + "_" + fSecret + "_m.jpg";
		
		fBitmap = BitmapLoader.preloadBitmap(imageURL);
	}

	
	
    public Bitmap getBitmap(){
        return fBitmap;
       }
    
    public String getTitle(){
    	return fTitle;
    }
    
    public String getImageURL()
    {
    	return imageURL;
    }
    
    
    static class BitmapLoader
    {
    	public static Bitmap preloadBitmap(String imageURL) {
    		Bitmap bm = null;

    		

    		URL FlickrPhotoUrl = null;

    		try {
    			FlickrPhotoUrl = new URL(imageURL);

    			HttpURLConnection httpConnection = (HttpURLConnection) FlickrPhotoUrl
    					.openConnection();
    			httpConnection.setDoInput(true);
    			httpConnection.connect();
    			InputStream inputStream = httpConnection.getInputStream();
    			bm = BitmapFactory.decodeStream(inputStream);

    		} catch (MalformedURLException e) {
    			
    			e.printStackTrace();
    		} catch (IOException e) {
    			
    			e.printStackTrace();
    		}

    		return bm;
    	}
    }

}
