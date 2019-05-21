package com.fdc.pixelcare.DataModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by salma on 05/03/18.
 */

public class User {
    private Integer userid;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    private String name, mobile, email, city;

    public User(int login_id, String sub_name, String sub_contact, String sub_email, String sub_city) {
        this.userid = login_id;
        this.name = sub_name;
        this.mobile = sub_contact;
        this.email = sub_email;
        this.city = sub_city;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public Integer getUserid() {
        return userid;
    }
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

}
