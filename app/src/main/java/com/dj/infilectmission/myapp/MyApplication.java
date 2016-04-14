package com.dj.infilectmission.myapp;

import android.app.Application;

public class MyApplication extends Application {

    public static final String TAG = MyApplication.class
            .getSimpleName();

    private static MyApplication mInstance;

    private MyPreferenceManager pref;
    private ConnectionDetector mConDetector;
    private ResourceReader mResReader;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


    public synchronized MyPreferenceManager getPrefManager() {

        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }
        return pref;
    }


    public synchronized ConnectionDetector getmConDetector() {

        if (mConDetector == null) {
            mConDetector = ConnectionDetector.getInstance(this);
        }

        return mConDetector;
    }


    public synchronized ResourceReader getResourceReader() {

        if (mResReader == null) {
            mResReader = new ResourceReader(this);
        }
        return mResReader;
    }

}