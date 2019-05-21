package com.fdc.pixelcare.DataModel;

import java.util.List;

/**
 * Created by lenovo on 06/07/2017.
 */

public class BusinessInform {
    private String id;
    private String name;

    int business_id, vendor_id, category_id, pagination_next;
    String business_name, address1, address2, city, pincode, state, country, status, created_time, created_by, youtube_code,
            latitude, longitude, plan_id, account_type, target_service, is_featured, featured_order, working_hours, account_expiry,
            category_name, phone_no, photo, category_code, user_location, ratings, votes, default_photo;
    List<BusinessSearch> business_array;
    String radius;
    int view_type;

    public BusinessInform() {
    }

    public BusinessInform(int id, int vendor_id, String business_name, String address1, String address2, String city_code, String pincode, String lat, String longt, String target_service, String account_type, String phone_no, String photo, String default_photo, String rating, String votes, int category_id, String category_name, String category_code, String user_location, int next_pagination, List<BusinessSearch> businessNamesArraylist) {


        this.business_id = id;
        this.vendor_id = vendor_id;
        this.business_name = business_name;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city_code;
        this.pincode = pincode;
        this.latitude = lat;
        this.longitude = longt;
        this.account_type = account_type;
        this.target_service = target_service;
        this.phone_no = phone_no;
        this.photo = photo;
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_code = category_code ;
        this.user_location = user_location ;
        this.ratings = rating;
        this.votes = votes;
        this.default_photo = default_photo;
        this.business_array = businessNamesArraylist;
        this.pagination_next = next_pagination;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessCategotyCode() {
        return category_code;
    }
    public void setBusinessCategotyCode(String category_code) { this.category_code = category_code; }

    public String getBusinessUserLocation() {
        return user_location;
    }
    public void setBusinessUserLocation(String user_location) { this.user_location = user_location; }

    public int getPaginationNext() {
        return pagination_next;
    }
    public void setPaginationNext(int pagination_next) {
        this.pagination_next = pagination_next;
    }

    public int getBusinessId() {
        return business_id;
    }
    public void setBusinessId(int business_id) {
        this.business_id = business_id;
    }

    public int getVendorId() {
        return vendor_id;
    }
    public void setVendorId(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public int getCategoryIdId() {
        return category_id;
    }
    public void setCategoryId(int category_id) {
        this.category_id = category_id;
    }

    public String getBusinessName() {
        return business_name;
    }
    public void setBusinessName(String business_name) {
        this.business_name = business_name;
    }

    public String getBusinessAddress1() {
        return address1;
    }
    public void setBusinessAddress1(String address1) {
        this.address1 = address1;
    }

    public String getBusinessAddress2() { return address2; }
    public void setBusinessAddress2(String address2) { this.address2 = address2; }

    public String getBusinessCity() { return city; }
    public void setBusinessCity(String city) { this.city = city; }

    public String getBusinessPinCode() { return pincode; }
    public void setBusinessPinCode(String pincode) { this.pincode = pincode; }

    public String getBusinessState() { return state; }
    public void setBusinessState(String state) { this.state = state; }

    public String getBusinessCountry() { return country; }
    public void setBusinessCountry(String country) { this.country = country; }

    public String getBusinessStatus() { return status; }
    public void setBusinessStatus(String status) { this.status = status; }

    public String getBusinessLatitude() { return latitude; }
    public void setBusinessLatitude(String latitude) { this.latitude = latitude; }

    public String getBusinessLongitude() { return longitude; }
    public void setBusinessLongitude(String longitude) { this.longitude = longitude; }

    public String getBusinessCategotyName() { return category_name; }
    public void setBusinessCategotyName(String category_name) { this.category_name = category_name; }

    public String getBusinessPhoneNo() { return phone_no; }
    public void setBusinessPhoneNo(String phone_no) { this.phone_no = phone_no; }

    public String getBusinessPhoto() { return photo; }
    public void setBusinessPhoto(String photo) { this.photo = photo; }

    public String getBusinessAccountType() { return account_type; }
    public void setBusinessAccountType(String account_type) { this.account_type = account_type; }

    public List<BusinessSearch> getBusinessNameArray() { return business_array; }
    public void setVaccineNameArray(List<BusinessSearch>   business_array) { this.business_array = business_array; }

    public String getBusinessRadius() { return radius; }
    public void setBusinessRadius(String radius) { this.radius = radius; }

    public String getBusinessRatings() { return ratings; }
    public void setBusinessRatings(String ratings) { this.ratings = ratings; }

    public String getBusinessVotes() { return votes; }
    public void setBusinessVotes(String votes) { this.votes = votes; }

    public String getBusinessDefaultPhoto() { return default_photo; }
    public void setBusinessVDefaultPhoto(String default_photo) { this.default_photo = default_photo; }

    public void setViewType(int i) {
        this.view_type = i;
    }
    public int getViewType() {return view_type; }

    public String getBusinessTargetService() { return target_service; }
    public void setBusinessTargetService(String target_service) { this.target_service = target_service; }

}