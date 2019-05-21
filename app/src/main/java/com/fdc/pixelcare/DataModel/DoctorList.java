package com.fdc.pixelcare.DataModel;

import java.io.Serializable;
import java.util.ArrayList; /**
 * Created by salma on 07/03/18.
 */

public class DoctorList {
    private int doc_id, doc_specid, doc_consulted;
    private String doc_name, doc_experience, doc_photo, doc_qual, doc_specname, doc_city, doc_enctyptId, doc_area_interest;
    private String doc_hosp_name, doc_hosp_city, doc_hosp_address, doc_hosp_state, doc_hosp_country, doc_encrypt_id;
    private boolean selected;
    private String geo_latitude, geo_longitude;
    private double latitude, longitude, current_latitudes, current_logitudes;
    ArrayList<SpecializationList> specilizationDocArraylist;
    ArrayList<HospitalList> hospitalDocArraylist;

    public DoctorList(int ref_id, String ref_name, int spec_id, String spec_name, String doc_photo, String doc_city,
                      String doc_encyid, String doc_interest, String doc_exp, String doc_qual, String hosp_name,
                      String hosp_addrs, String hosp_city, String hosp_state, String hosp_country, String doc_encryptId) {
        this.doc_id = ref_id;
        this.doc_name = ref_name;
        this.doc_specid = spec_id;
        this.doc_specname = spec_name;
        this.doc_photo = doc_photo;
        this.doc_city = doc_city;
        this.doc_enctyptId = doc_encyid;
        this.doc_area_interest = doc_interest;
        this.doc_experience = doc_exp;
        this.doc_qual = doc_qual;
        this.doc_hosp_name = hosp_name;
        this.doc_hosp_address = hosp_addrs;
        this.doc_hosp_city =hosp_city;
        this.doc_hosp_state = hosp_state;
        this.doc_hosp_country = hosp_country;
        this.doc_encrypt_id = doc_encryptId;
    }

    public DoctorList(int ref_id, String ref_name, int spec_id, String spec_name, String doc_photo, String doc_city,
                      String doc_encyid, String doc_interest, String doc_exp, String doc_qual, String hosp_name,
                      String hosp_addrs, String hosp_city, String hosp_state, String hosp_country, String doc_encryptId,
                      String latitudes, String longitudes) {
        this.doc_id = ref_id;
        this.doc_name = ref_name;
        this.doc_specid = spec_id;
        this.doc_specname = spec_name;
        this.doc_photo = doc_photo;
        this.doc_city = doc_city;
        this.doc_enctyptId = doc_encyid;
        this.doc_area_interest = doc_interest;
        this.doc_experience = doc_exp;
        this.doc_qual = doc_qual;
        this.doc_hosp_name = hosp_name;
        this.doc_hosp_address = hosp_addrs;
        this.doc_hosp_city =hosp_city;
        this.doc_hosp_state = hosp_state;
        this.doc_hosp_country = hosp_country;
        this.doc_encrypt_id = doc_encryptId;
        this.geo_latitude = latitudes;
        this.geo_longitude = longitudes;
    }

    public DoctorList() {

    }

    public DoctorList(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    public DoctorList(int docId, String docName, int docSpecId, String docSpecName, String docPhoto,
                      String docCity, String docEncryptID, String docAreaInterest, String docExperience,
                      String docQualification, String docHospName, String docHospAddress, String docHospCity,
                      String docHospState, String docHospCountry, String docEncryptID1, String docGeoLatitude,
                      String docGeoLongitude, double get_current_latititude, double get_current_longitude) {
        this.doc_id = docId;
        this.doc_name = docName;
        this.doc_specid = docSpecId;
        this.doc_specname = docSpecName;
        this.doc_photo = docPhoto;
        this.doc_city = docCity;
        this.doc_enctyptId = docEncryptID;
        this.doc_area_interest = docAreaInterest;
        this.doc_experience = docExperience;
        this.doc_qual = docQualification;
        this.doc_hosp_name = docHospName;
        this.doc_hosp_address = docHospAddress;
        this.doc_hosp_city =docHospCity;
        this.doc_hosp_state = docHospState;
        this.doc_hosp_country = docHospCountry;
        this.doc_encrypt_id = docEncryptID1;
        this.geo_latitude = docGeoLatitude;
        this.geo_longitude = docGeoLongitude;
        this.current_latitudes = get_current_latititude;
        this.current_logitudes = get_current_longitude;
    }

    public DoctorList(int ref_id, String ref_name, String doc_photo, String doc_city, String doc_interest,
                      String doc_exp, String doc_qual, String doc_encyid, String geo_latitude, String geo_longitude, int doc_consult,
                      ArrayList<SpecializationList> specilizationDocArraylist, ArrayList<HospitalList> hospitalDocArraylist) {

        this.doc_id = ref_id;
        this.doc_name = ref_name;
        this.doc_photo = doc_photo;
        this.doc_city = doc_city;
        this.doc_area_interest = doc_interest;
        this.doc_experience = doc_exp;
        this.doc_qual = doc_qual;
        this.doc_encrypt_id = doc_encyid;
        this.geo_latitude = geo_latitude;
        this.geo_longitude = geo_longitude;
        this.doc_consulted = doc_consult;
        this.specilizationDocArraylist = specilizationDocArraylist;
        this.hospitalDocArraylist = hospitalDocArraylist;
    }


    public double getCurrentLongitudes() { return current_logitudes; }
    public void setCurrentLongitudes(double current_logitudes) { this.current_logitudes = current_logitudes; }

    public double getCurrentLatitudes() { return current_latitudes; }
    public void setCurrentLatitudes(double current_latitudes) { this.current_latitudes = current_latitudes; }

    public double getDocLongitudes() { return longitude; }
    public void setDocLongitudes(double longitude) { this.longitude = longitude; }

    public double getDocLatitudes() { return latitude; }
    public void setDocLatitudes(double latitude) { this.latitude = latitude; }

    public int getDocId() { return doc_id; }
    public void setDocId(int doc_id) { this.doc_id = doc_id; }

    public String getDocName() { return doc_name; }
    public void setDocName(String doc_name) { this.doc_name = doc_name; }

    public String getDocExperience() { return doc_experience; }
    public void setDocExperience(String doc_experience) { this.doc_experience = doc_experience; }

    public String getDocPhoto() { return doc_photo; }
    public void setDocPhoto(String doc_photo) { this.doc_photo = doc_photo; }

    public String getDocQualification() { return doc_qual; }
    public void setDocQualification(String doc_qual) { this.doc_qual = doc_qual; }

    public int getDocSpecId() { return doc_specid; }
    public void setDocSpecId(int doc_specid) { this.doc_specid = doc_specid; }

    public String getDocSpecName() { return doc_specname; }
    public void setDocSpecName(String doc_specname) { this.doc_specname = doc_specname; }

    public String getDocCity() { return doc_city; }
    public void setDocCity(String doc_city) { this.doc_city = doc_city; }

    public String getDocEncryptID() { return doc_encrypt_id; }
    public void setDocEncryptID(String doc_encrypt_id) { this.doc_encrypt_id = doc_encrypt_id; }

    public String getDocAreaInterest() { return doc_area_interest; }
    public void setDocAreaInterest(String doc_area_interest) { this.doc_area_interest = doc_area_interest; }

    public String getDocHospName() { return doc_hosp_name; }
    public void setDocHospName(String doc_hosp_name) { this.doc_hosp_name = doc_hosp_name; }

    public String getDocHospCity() { return doc_hosp_city; }
    public void setDocHospCity(String doc_hosp_city) { this.doc_hosp_city = doc_hosp_city; }

    public String getDocHospAddress() { return doc_hosp_address; }
    public void setDocHospAddress(String doc_hosp_address) { this.doc_hosp_address = doc_hosp_address; }

    public String getDocHospState() { return doc_hosp_state; }
    public void setDocHospState(String doc_hosp_state) { this.doc_hosp_state = doc_hosp_state; }

    public String getDocHospCountry() { return doc_hosp_country; }
    public void setDocHospCountry(String doc_hosp_country) { this.doc_hosp_country = doc_hosp_country; }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public String getDocGeoLatitude() { return geo_latitude; }
    public void setDocGeoLatitude(String geo_latitude) { this.geo_latitude = geo_latitude; }

    public String getDocGeoLongitude() { return geo_longitude; }
    public void setDocGeoLongitude(String geo_longitude) { this.geo_longitude = geo_longitude; }

    public ArrayList<SpecializationList> getDocSpecialization() { return specilizationDocArraylist; }
    public void setDocSpecialization(ArrayList<SpecializationList> specilizationDocArraylist) { this.specilizationDocArraylist = specilizationDocArraylist; }

    public ArrayList<HospitalList> getDocHospital() { return hospitalDocArraylist; }
    public void setDocHospital(ArrayList<HospitalList> hospitalDocArraylist) { this.hospitalDocArraylist = hospitalDocArraylist; }

    public int getDocConsulted() { return doc_consulted; }
    public void setDocConsulted(int doc_consulted) { this.doc_consulted = doc_consulted; }

}
