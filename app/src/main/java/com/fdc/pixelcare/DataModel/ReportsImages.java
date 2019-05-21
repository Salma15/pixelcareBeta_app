package com.fdc.pixelcare.DataModel;

/**
 * Created by salma on 22/03/18.
 */

public class ReportsImages {


    private String title;
    private int image;
    private String fundus_image;

    public ReportsImages(String title,  int image) {
        this.title = title;
        this.image = image;
    }

    public ReportsImages(int image) {
        this.image = image;
    }

    public ReportsImages(String images) {
        this.fundus_image = images;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }
    public String getFundusImage() {
        return fundus_image;
    }
}
