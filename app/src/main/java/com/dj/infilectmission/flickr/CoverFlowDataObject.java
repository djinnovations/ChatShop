package com.dj.infilectmission.flickr;

/**
 * Created by COMP on 4/13/2016.
 */
public class CoverFlowDataObject {

    private String picTitle = "Photo title";
    private String imageUrl;
    private int imgResId = 0;

    public CoverFlowDataObject(String picTitle, String imageUrl, int imgResId) {
        if (picTitle != null)
            this.picTitle = picTitle;

        this.imageUrl = imageUrl;
        this.imgResId = imgResId;
    }

    public CoverFlowDataObject(String picTitle, String imageUrl) {
        if (picTitle != null)
            this.picTitle = picTitle;

        this.imageUrl = imageUrl;
    }


    public String getPicTitle() {
        return picTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getImgResId() {
        return imgResId;
    }
}
