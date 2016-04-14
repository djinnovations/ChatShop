package com.dj.infilectmission.flickr.server;

import android.net.Uri;
import android.util.Log;

import com.dj.infilectmission.utils.CustomPair;
import com.dj.infilectmission.utils.InfilectConstants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by COMP on 4/13/2016.
 */
public class ServerCommunication {

    public interface SearchPhotosListener{
        void onPhotoSearchFinish(ArrayList<CustomPair> mList);
    }

    private SearchPhotosListener mSearchPicListener;

    private static ServerCommunication ourInstance = new ServerCommunication();

    public static ServerCommunication getInstance() {
        return ourInstance;
    }

    private ServerCommunication() {
    }


    public ArrayList<CustomPair> getPublicPhotosUrlList(final String tag, final SearchPhotosListener mSearchPicListener){

        this.mSearchPicListener = mSearchPicListener;
        new Thread() {

            public void run() {

                Uri.Builder uriBuilder = Uri.parse(InfilectConstants.ENDPOINT).buildUpon();

                uriBuilder.appendQueryParameter(ServerKeys.KEY_METHOD, ServerKeys.REQUEST_METHOD_SEARCH);
                uriBuilder.appendQueryParameter(ServerKeys.KEY_APIKEY, InfilectConstants.FLICKR_API_KEY);
                uriBuilder.appendQueryParameter(ServerKeys.KEY_TAGS, tag);
                uriBuilder.appendQueryParameter(ServerKeys.KEY_PER_PAGE, InfilectConstants.FLICKR_PER_PAGE);
                uriBuilder.appendQueryParameter(ServerKeys.KEY_PAGE, InfilectConstants.FLICKR_PAGE);
                uriBuilder.appendQueryParameter(ServerKeys.KEY_FORMAT, InfilectConstants.FLICKR_FORMAT_JSON);
                /*checkUserUri.appendQueryParameter(ServerKeys.KEY_APISIG, getMd5HashedString(InfilectConstants.FLICKR_API_SECRET
                +ServerKeys.KEY_APIKEY+InfilectConstants.FLICKR_API_KEY
                +"format"+"json"+ServerKeys.KEY_METHOD+ServerKeys.REQUEST_METHOD_FROB));*/
                //checkUserUri.appendQueryParameter(ServerJsonKeys.PLATFORM_KEY, getEncodedString(platform));

                String queryUrl = uriBuilder.build().toString();
                JSONObject jsonResponse = exceuteGetRequest(queryUrl);
                if (jsonResponse != null)
                    Log.d("dj", "check user method: " + jsonResponse.toString());

                mSearchPicListener.onPhotoSearchFinish(parseForPhotoUrl(jsonResponse));
            }

        }.start();

        return null;
    }

    private ArrayList<CustomPair> parseForPhotoUrl(JSONObject jsonResponse) {

        try {
            ArrayList<CustomPair> mList = new ArrayList<>();
            JSONObject photoJsonObj = jsonResponse.getJSONObject(ServerKeys.KEY_PHOTOS);
            JSONArray photosJsonArr = photoJsonObj.getJSONArray(ServerKeys.KEY_PHOTO);
            for (int i=0; i < photosJsonArr.length(); i++){

               mList.add(extractPhotoData(photosJsonArr.getJSONObject(i)));
            }

            return mList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private CustomPair extractPhotoData(JSONObject jsonObject){

        try {
            String photoId = jsonObject.getString(ServerKeys.KEY_PHOTO_ID);
            String photoTitle = jsonObject.getString(ServerKeys.KEY_PHOTO_TITLE);
            String farm = jsonObject.getString(ServerKeys.KEY_PHOTO_FARM);
            String secret = jsonObject.getString(ServerKeys.KEY_PHOTO_SECRET);
            String server = jsonObject.getString(ServerKeys.KEY_PHOTO_SERVER);

            String url = ServerUtils.getInstance().getUrlForPic(farm, photoId, server, secret);
            Log.d("dj", "photo url - extractPhotoData: "+url);
            Log.d("dj", "photo id - extractPhotoData: "+photoId);
            Log.d("dj", "photoTitle - extractPhotoData: "+photoTitle);
            Log.d("dj", "farm - extractPhotoData: "+farm);
            Log.d("dj", "pic secret - extractPhotoData: "+secret);
            Log.d("dj", "pic server - extractPhotoData: "+server);

            return new CustomPair(url, photoTitle);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    protected JSONObject exceuteGetRequest(String queryUrl){

        Log.d("dj", "query url - exceuteGetRequest: "+queryUrl);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet genericHttpGetRequest = new HttpGet(queryUrl);

        InputStream is = null;
        // Making HTTP Request
        try {
            HttpResponse response = httpClient.execute(genericHttpGetRequest);
            HttpEntity httpEntity = response.getEntity();
            is = httpEntity.getContent();
            // writing response to log
        } catch (ClientProtocolException e) {
            // writing exception to log
            e.printStackTrace();
        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();

        }

        return getJsonFromInputStream(is);
    }


    private JSONObject getJsonFromInputStream(InputStream is){

        JSONObject genericJsonObj = null;
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            String jsonResponse = sb.toString();
            String temp = jsonResponse.replace("jsonFlickrApi","").replace("(","").replace(")","");
            Log.d("dj", "response - getJsonFromInputStream: "+temp);
            genericJsonObj = new JSONObject(temp);
            is.close();

        } catch (Exception e) {

            Log.e("Buffer Error", "Error converting result " + e.toString());
            e.printStackTrace();
        }

        return genericJsonObj;

    }

}
