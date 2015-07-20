/*
 * This ConstantVales.java file is for writting constant values
 */

package com.astegic.flickerpublicimage;

public class ConstantValues {
	

	/*
	 * FlickrQuery = FlickrQuery_url + FlickrQuery_per_page +
	 * FlickrQuery_nojsoncallback + FlickrQuery_format + FlickrQuery_tag + q +
	 * FlickrQuery_key + FlickrApiKey
	 */
	public static final String FlickrQuery_url = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
	public static final String FlickrQuery_per_page = "&per_page=50";
	public static final String FlickrQuery_nojsoncallback = "&nojsoncallback=1";
	public static final String FlickrQuery_format = "&format=json";
	public static final String FlickrQuery_tag = "&tags=";
	public static final String FlickrQuery_key = "&api_key=";
	// Apply your Flickr API:
	// www.flickr.com/services/apps/create/apply/?
	public static final String FlickrApiKey = "c086e5bca84c2de6306cd4096897136e";
	
	
    public static final int GRIDVIEW_SPACING = 3;
    public static final int GRIDVIEW_COLUMN_WIDTH = 100;
    public static final int GRIDVIEW_NO_COLUMN_POTRAIT = 4;
    public static final int GRIDVIEW_NO_COLUMN_LAND = 8;
    public static final int GRIDVIEW_HO_SPACING = 5;
    public static final int GRIDVIEW_VO_SPACING = 5;
    
    public static final int GRIDVIEW_IMAGE_WIDTH = 250;
    public static final int GRIDVIEW_IMAGE_HEIGHT = 250;
    public static final int GRIDVIEW_IMAGE_SPACING = 10;
    
}
