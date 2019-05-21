package com.fdc.pixelcare.DataModel;

/**
 * Created by salma on 16/03/18.
 */

public class UserAddress {
    private int userid, address_id;
    private String address, city, pincode, state, country;

    public UserAddress(int address_id, int userid, String address, String city, String pincode, String state, String country) {
        this.address_id = address_id;
        this.userid = userid;
        this.address = address;
        this.city = city;
        this.pincode = pincode;
        this.state = state;
        this.country = country;
    }

    public int getAddressid() {
        return address_id;
    }
    public void setAddressid(int address_id) { this.address_id = address_id; }

    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) { this.userid = userid; }

    public String getAddressName() {
        return address;
    }
    public void setAddressName(String address) { this.address = address; }

    public String getCity() {
        return city;
    }
    public void setCity(String city) { this.city = city; }

    public String getPincode() {
        return pincode;
    }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getState() {
        return state;
    }
    public void setState(String state) { this.state = state; }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) { this.country = country; }

}
