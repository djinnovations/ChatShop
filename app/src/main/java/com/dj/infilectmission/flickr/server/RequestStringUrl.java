package com.dj.infilectmission.flickr.server;

/**
 * Created by COMP on 4/13/2016.
 */
public class RequestStringUrl {


    private static RequestStringUrl reqUrlInstance = new RequestStringUrl();

    public static RequestStringUrl getInstance() {
        return reqUrlInstance;
    }

    private RequestStringUrl() {
    }


    public String getLoginUrl(){

        return ServerKeys.REQUEST_AUTH;

    }

}
