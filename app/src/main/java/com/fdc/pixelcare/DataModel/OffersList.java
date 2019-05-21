package com.fdc.pixelcare.DataModel;

/**
 * Created by HP on 08-04-2018.
 */

public class OffersList {
    private int offerId;
    private String title, description, validity, contact_num, note, images;

    public OffersList() {
    }

    public OffersList(int offerId, String title, String description, String validity, String contact_num, String note, String images) {
        this.offerId = offerId;
        this.title = title;
        this.description = description;
        this.validity = validity;
        this.contact_num = contact_num;
        this.note = note;
        this.images = images;
    }

    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getContactNumber() { return contact_num; }
    public void setContactNumber(String contact_num) { this.contact_num = contact_num; }

    public int getOfferId() { return offerId; }
    public void setOfferId(int offerId) { this.offerId = offerId; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}
