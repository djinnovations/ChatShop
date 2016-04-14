package com.dj.infilectmission.myapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dj.infilectmission.utils.InfilectConstants;

import java.util.ArrayList;

/**
 * Created by COMP on 4/14/2016.
 */
public class MyPreferenceManager {

    private static final String KEY_IMAGE_URL = "image_url";
    // Shared Preferences
    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    // Shared pref mode
    int PRIVATE_MODE = 0;


    private final String PREF_NAME = InfilectConstants.PREFERENCE_NAME;

    MyPreferenceManager(Context context) {

        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public ArrayList<String> getUrlList() {

        ArrayList<String> urlList = new ArrayList<>();
        String urlFromPref = getUrl();
        if (urlFromPref != null) {
            String[] urlArr = urlFromPref.split(",");
            for (String url : urlArr) {
                //String refined = url.replace(",", "");
                urlList.add(url);
            }
            if (urlList.size() > 15) {
                ArrayList<String> newRecentList = new ArrayList<>();
                Log.d("dj", "refining list");
                newRecentList.addAll(urlList.subList(urlList.size() - 15, urlList.size()));
                return newRecentList;
            } else
                return urlList;
        } else return null;

    }

    private String getUrl() {

        String url = pref.getString(KEY_IMAGE_URL, null);
        Log.d("dj","url - getUrl: "+url);
        return url;
    }

    public void addUrlToPref(String urlForImage) {

        String oldData = getUrl();
        if (oldData != null){
            editor.putString(KEY_IMAGE_URL, oldData+","+urlForImage);
        }else editor.putString(KEY_IMAGE_URL, urlForImage);

        boolean stat = editor.commit();
        Log.d("dj","write stat - addUrlToPref: "+stat);
    }
}
