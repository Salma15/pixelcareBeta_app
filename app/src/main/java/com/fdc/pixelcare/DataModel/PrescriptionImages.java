package com.fdc.pixelcare.DataModel;

/**
 * Created by salma on 10/03/18.
 */

public class PrescriptionImages {

    private String title;
    private int image;
    private String fundus_image;

    public PrescriptionImages(String title,  int image) {
        this.title = title;
        this.image = image;
    }

    public PrescriptionImages(int image) {
        this.image = image;
    }

    public PrescriptionImages(String images) {
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
