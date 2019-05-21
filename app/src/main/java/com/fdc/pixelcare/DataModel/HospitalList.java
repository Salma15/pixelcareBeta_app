package com.fdc.pixelcare.DataModel;

import java.io.Serializable;

/**
 * Created by SALMA on 25-09-2018.
 */
public class HospitalList implements Serializable {

    private int hospital_id, doc_id, hosp_consulted;
    private String hosp_name, hosp_address, hosp_city, hosp_state, hosp_country, hosp_mobile, hosp_email, hosp_photo, geo_latitudes, geo_longitudes;
    private double latitude, longitude, current_latitudes, current_logitudes;

    public HospitalList(int hosp_id, String hosp_name, String hosp_addrs, String hosp_city, String hosp_state,
                        String hosp_country, String hosp_contact, String hosp_email, String geo_latitude,
                        String geo_longitude, String hosp_logo, int consulted) {
        this.hospital_id = hosp_id;
        this.hosp_name = hosp_name;
        this.hosp_address = hosp_addrs;
        this.hosp_city = hosp_city;
        this.hosp_state = hosp_state;
        this.hosp_country = hosp_country;
        this.hosp_mobile = hosp_contact;
        this.hosp_email = hosp_email;
        this.geo_latitudes = geo_latitude;
        this.geo_longitudes = geo_longitude;
        this.hosp_photo = hosp_logo;
        this.hosp_consulted = consulted;
    }

    public HospitalList(int hospitalId, String hospitalName, String hospitalAddress, String hospitalCity,
                        String hospitalState, String hospitalCountry, String hospitalMobile, String hospitalEmail,
                        String hospitalGeoLatitudes, String hospitalGeoLongitude, String hospitalPhoto,
                        double get_current_latititude, double get_current_longitude) {
        this.hospital_id = hospitalId;
        this.hosp_name = hospitalName;
        this.hosp_address = hospitalAddress;
        this.hosp_city = hospitalCity;
        this.hosp_state = hospitalState;
        this.hosp_country = hospitalCountry;
        this.hosp_mobile = hospitalMobile;
        this.hosp_email = hospitalEmail;
        this.geo_latitudes = hospitalGeoLatitudes;
        this.geo_longitudes = hospitalGeoLongitude;
        this.hosp_photo = hospitalPhoto;
        this.current_latitudes = get_current_latititude;
        this.current_logitudes = get_current_longitude;
    }

    public HospitalList(int hosp_id, String hosp_name, String hosp_addrs, String hosp_city, String hosp_state,
                        String hosp_country, int doc_id) {
        this.hospital_id = hosp_id;
        this.hosp_name = hosp_name;
        this.hosp_address = hosp_addrs;
        this.hosp_city = hosp_city;
        this.hosp_state = hosp_state;
        this.hosp_country = hosp_country;
        this.doc_id = doc_id;
    }

    public int getHospitalId() { return hospital_id; }
    public void setHospitalId(int hospital_id) { this.hospital_id = hospital_id; }

    public String getHospitalName() { return hosp_name; }
    public void setHospitalName(String hosp_name) { this.hosp_name = hosp_name; }

    public String getHospitalAddress() { return hosp_address; }
    public void setHospitalAddress(String hosp_address) { this.hosp_address = hosp_address; }

    public String getHospitalCity() { return hosp_city; }
    public void setHospitalCity(String hosp_city) { this.hosp_city = hosp_city; }

    public String getHospitalState() { return hosp_state; }
    public void setHospitalState(String hosp_state) { this.hosp_state = hosp_state; }

    public String getHospitalCountry() { return hosp_country; }
    public void setHospitalCountry(String hosp_country) { this.hosp_country = hosp_country; }

    public String getHospitalMobile() { return hosp_mobile; }
    public void setHospitalMobile(String hosp_mobile) { this.hosp_mobile = hosp_mobile; }

    public String getHospitalEmail() { return hosp_email; }
    public void setHospitalEmail(String hosp_email) { this.hosp_email = hosp_email; }

    public String getHospitalPhoto() { return hosp_photo; }
    public void setHospitalPhoto(String hosp_photo) { this.hosp_photo = hosp_photo; }

    public String getHospitalGeoLatitude() { return geo_latitudes; }
    public void setHospitalGeoLatitude(String geo_latitudes) { this.geo_latitudes = geo_latitudes; }

    public String getHospitalGeoLongitude() { return geo_longitudes; }
    public void setHospitalGeoLongitude(String geo_longitudes) { this.geo_longitudes = geo_longitudes; }

    public double getHospitalLongitudes() { return longitude; }
    public void setHospitalLongitudes(double longitude) { this.longitude = longitude; }

    public double getHospitalLatitudes() { return latitude; }
    public void setHospitalLatitudes(double latitude) { this.latitude = latitude; }

    public double getCurrentLongitudes() { return current_logitudes; }
    public void setCurrentLongitudes(double current_logitudes) { this.current_logitudes = current_logitudes; }

    public double getCurrentLatitudes() { return current_latitudes; }
    public void setCurrentLatitudes(double current_latitudes) { this.current_latitudes = current_latitudes; }

    public int getHospitalDocId() { return doc_id; }
    public void setHospitalDocId(int doc_id) { this.doc_id = doc_id; }

    public int getHospitalConsulted() { return hosp_consulted; }
    public void setHospitalConsulted(int hosp_consulted) { this.hosp_consulted = hosp_consulted; }

}
