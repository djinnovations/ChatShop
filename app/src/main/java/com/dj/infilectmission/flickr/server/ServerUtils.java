package com.dj.infilectmission.flickr.server;

/**
 * Created by COMP on 4/13/2016.
 */
public class ServerUtils {

    private static ServerUtils mServerUtilsInstance = new ServerUtils();

    public static ServerUtils getInstance() {
        return mServerUtilsInstance;
    }

    private ServerUtils() {
    }



    public String getUrlForPic(String farmId, String photoId, String serverId, String photoSecret){

        return "https://farm" + farmId + ".static.flickr.com/" + serverId +
                "/"+photoId + "_"+photoSecret +"_b"+ ".jpg";
    }
}
