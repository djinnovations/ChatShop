package com.dj.infilectmission.utils;

import android.os.Build;

/**
 * Created by COMP on 4/12/2016.
 */
public class InfilectConstants {

    public static final String ENDPOINT = "https://api.flickr.com/services/rest/?";
    public static final String IMAGE_SIZE = "b";

    public static final int CURRENT_API_LEVEL = Build.VERSION.SDK_INT;

    public static final String FLICKR_API_KEY = "5147a9f021eab9643fc1122aa39a3d40";
    public static final String FLICKR_API_SECRET = "6f1634ae8e0fbf4e";
    public static final String FLICKR_USER_ID = "140154043@N05";
    public static final String FLICKR_PER_PAGE = "15";
    public static final String FLICKR_PAGE = "1";
    public static final String FLICKR_FORMAT_JSON = "json";
    public static final String FLICKR_AUTH_SIGN_HASHED = "6f1634ae8e0fbf4e5147a9f021eab9643fc1122aa39a3d40jsonDjphy";

    public static final int APPAREL_SHIRT = 100;
    public static final int APPAREL_TOP = 101;
    public static final int APPAREL_PANT = 102;
    public static final int APPAREL_DRESS = 103;


    public static final String KEY_PARCEL_PHOTOS = "com.dj.infilect.PhotoData";
    public static final String KEY_PARCEL_TITLE = "com.dj.infilect.Title";
    public static final String DEFAULT_TITLE = "Default";

    public static final String PREFERENCE_NAME = "flick_images_url";
}
