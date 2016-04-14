package com.dj.infilectmission.myapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.dj.infilectmission.utils.InfilectConstants;

/**
 * Created by COMP on 2/9/2016.
 */
public class ResourceReader {

    private Context mContext;

    ResourceReader(Context mContext) {

        this.mContext = mContext;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public int getColorFromResource(int colorResId){

        if(InfilectConstants.CURRENT_API_LEVEL == 23)
            return mContext.getResources().getColor(colorResId, mContext.getTheme());
        else
            return mContext.getResources().getColor(colorResId);
    }


    public String getStringFromResource(int stringResId){

        return mContext.getResources().getString(stringResId);
    }
}
