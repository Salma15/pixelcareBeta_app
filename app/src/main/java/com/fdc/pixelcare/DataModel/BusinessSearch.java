package com.fdc.pixelcare.DataModel;

import java.io.Serializable;

/**
 * Created by hp on 14/05/2017.
 */
public class BusinessSearch implements Serializable {

    private String business_name, business_key;

    public BusinessSearch() {
    }

    public BusinessSearch(String business_name,  String business_key) {
        this.business_name = business_name;
        this.business_key = business_key;
    }

    public BusinessSearch(String businessName) {
        this.business_name = businessName;
    }


    public String getBusinessName() { return business_name; }
    public void setBusinessName(String business_name) { this.business_name = business_name; }

    public String getBusinessKey() { return business_key; }
    public void setBusinessKey(String business_key) { this.business_key = business_key; }
}
