package com.fdc.pixelcare.DataModel;

/**
 * Created by SALMA on 30-10-2018.
 */
public class CitiesList {

    int city_id;
    String city_name, city_latitude, city_longitude, city_state;

    public CitiesList(int city_id, String city_name, String latitude, String longitude, String state) {
        this.city_id = city_id;
        this.city_name = city_name;
        this.city_latitude = latitude;
        this.city_longitude = longitude;
        this.city_state = state;
    }

    public int getCityId() {
        return city_id;
    }
    public void setCityId(int city_id) { this.city_id = city_id; }

    public String getCityName() {  return city_name; }
    public void setCityName(String city_name) { this.city_name = city_name; }

    public String getCityLatitude() {  return city_latitude; }
    public void setCityLatitude(String city_latitude) { this.city_latitude = city_latitude; }

    public String getCityLongitude() {  return city_longitude; }
    public void setCityLongitude(String city_longitude) { this.city_longitude = city_longitude; }

    public String getCityState() {  return city_state; }
    public void setCityState(String city_state) { this.city_state = city_state; }

}
