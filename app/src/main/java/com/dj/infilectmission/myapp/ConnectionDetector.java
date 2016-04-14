package com.dj.infilectmission.myapp;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionDetector {

    private Context mContext;
    private static ConnectionDetector mConDetector;

    static ConnectionDetector getInstance(Context mContext){

        if (mConDetector == null){
            mConDetector = new ConnectionDetector(mContext);
        }

        return mConDetector;
    }

    private ConnectionDetector(Context context) {
        this.mContext = context;
    }




    /**
     * Checking for all possible internet providers
     **/
    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public boolean canGetLocation() {
        LocationManager locationManager = (LocationManager) mContext
                .getSystemService(mContext.LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.d("dj", "gps stat: " + isGPSEnabled);

        if (isGPSEnabled && isNetworkAvailable()) {
            Log.d("dj", "can get location : true");
            return true;
        } else {
            Log.d("dj", "can get location : false");
            return false;
        }
    }



}
