package com.fdc.pixelcare.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by salma on 19/12/18.
 */

public class ShareadPreferenceClass {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ShareadPreferenceClass(Context mContext) {
        this.context = mContext;
        sharedPreferences = context.getSharedPreferences(MHConstants.PREF_NAADLE_SYSTEM, Context.MODE_PRIVATE);

    }

    public int user_id;
    public String user_name, user_location, user_dob, user_mobile, user_email, user_address, user_password;

    //----Start Set and Get SharedPreference---
    public void preLogin(int user_id, String user_name,  String user_email, String mobile, String location, int loginType) {
        if (sharedPreferences != null) {
            //  Log.d(NRConstants.TAG, "SharedID: "+ String.valueOf(user_id));
            editor = sharedPreferences.edit();
            editor.putInt(MHConstants.PREF_USER_ID, user_id);
            editor.putString(MHConstants.PREF_USER_NAME, user_name);
            editor.putString(MHConstants.PREF_USER_EMAIL, user_email);
            editor.putString(MHConstants.PREF_USER_MOBILE, mobile);
            editor.putString(MHConstants.PREF_USER_LOCATION, location);
            editor.putInt(MHConstants.PREF_USER_LOGINTYPE, loginType);
            editor.commit();
        }
    }
    //----End Set and Get SharedPreference---

    //----Start Set and Get SharedPreference Family Member---
    public void preFamilyMembers(int member_id, int userid, int gender, String member_name, String relationship, String dob, String age) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putInt(MHConstants.PREF_MEMBER_ID, member_id);
            editor.putInt(MHConstants.PREF_MEMBER_USERID, userid);
            editor.putInt(MHConstants.PREF_MEMBER_GENDER, gender);
            editor.putString(MHConstants.PREF_MEMBER_NAME, member_name);
            editor.putString(MHConstants.PREF_MEMBER_RELATION, relationship);
            editor.putString(MHConstants.PREF_MEMBER_DOB, dob);
            editor.putString(MHConstants.PREF_MEMBER_AGE, age);
            editor.commit();
        }
    }
    //----End Set and Get SharedPreference---

    public void preLocation(String location) {
        String location_caps = capitalize(location);
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_USER_LOCATION, location_caps);
            editor.commit();
        }
    }

    private String capitalize(String location) {
        return Character.toUpperCase(location.charAt(0)) + location.substring(1);
    }
    //----End Set and Get SharedPreference---

    public void setLocation(String location) {
        String location_caps = capitalize(location);
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_USER_LOCATION, location_caps);
            editor.commit();
        }
    }

    public void setHomeCategoty(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_POPULAR_CATEGOTY, json);
            editor.commit();
        }
    }

    public void setVendorAccountStatus(int status) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putInt(MHConstants.PREF_USER_ACCOUNT_STATUS, status);
            editor.commit();
        }
    }

    public void clearHomeCategory() {
        if (sharedPreferences != null) {

            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_POPULAR_CATEGOTY).commit();

        }
    }

    public void clearData() {
        if (sharedPreferences != null) {

            editor = sharedPreferences.edit();
            editor.clear().commit();

        }
    }

    public SharedPreferences getSharedPreferences(Context mContext) {
        this.context = mContext;
        sharedPreferences = context.getSharedPreferences(MHConstants.PREF_NAADLE_SYSTEM, Context.MODE_PRIVATE);

        return sharedPreferences;
    }

    public void setBusinessList(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_BUSINESS_LISTS, json);
            editor.commit();
        }
    }

    public void clearBusinessLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_BUSINESS_LISTS).commit();

        }
    }

    public void setSpecializationList(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_SPECIALIZATION_LIST, json);
            editor.commit();
        }
    }

    public void clearSpecializationLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_SPECIALIZATION_LIST).commit();

        }
    }

    public void setDoctorsList(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_DOCTORS_LIST, json);
            editor.commit();
        }
    }

    public void clearDoctorsLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_DOCTORS_LIST).commit();

        }
    }

    public void setBlogsList(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_BLOGS_LIST, json);
            editor.commit();
        }
    }

    public void clearBlogsLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_BLOGS_LIST).commit();

        }
    }

    public void setFamilyMemberList(String json_family_member) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_FAMILY_MEMBERS, json_family_member);
            editor.commit();
        }
    }

    public void clearFamilyMemberLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_FAMILY_MEMBERS).commit();

        }
    }

    public void setUserAddressList(String json_address) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_USER_ADDRESS, json_address);
            editor.commit();
        }
    }

    public void clearUserAddressLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_USER_ADDRESS).commit();

        }
    }

    public void setAppointmentList(String json_address) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_APPOINTMENTS_LIST, json_address);
            editor.commit();
        }
    }

    public void clearAppointmentLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_APPOINTMENTS_LIST).commit();

        }
    }

    public void setLoginMemberID(int json_memberID) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putInt(MHConstants.PREF_LOGIN_MEMBER_ID, json_memberID);
            editor.commit();
        }
    }

    public void clearLoginMemberID() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_LOGIN_MEMBER_ID).commit();

        }
    }

    public void setLoginMemberName(String json_memberName) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_LOGIN_MEMBER_NEME, json_memberName);
            editor.commit();
        }
    }

    public void clearLoginMemberName() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_LOGIN_MEMBER_NEME).commit();

        }
    }

    public void setDoctorsMapList(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_DOCTORS_MAP_LIST, json);
            editor.commit();
        }
    }

    public void clearDoctorsMapLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_DOCTORS_MAP_LIST).commit();

        }
    }

    public void setConsultationsList(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_CONSULTATIONS_LIST, json);
            editor.commit();
        }
    }

    public void clearConsultationsLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_CONSULTATIONS_LIST).commit();

        }
    }

    public void setHospitalsMapList(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_HOSPITALS_MAP_LIST, json);
            editor.commit();
        }
    }

    public void clearHospitalsMapLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_HOSPITALS_MAP_LIST).commit();

        }
    }

    public void setOpinionList(String json_address) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_OPINIONS_LIST, json_address);
            editor.commit();
        }
    }

    public void clearOpinionLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_OPINIONS_LIST).commit();

        }
    }

    public void setCitiesList(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_CITIES_LIST, json);
            editor.commit();
        }
    }

    public void clearCitiesLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_CITIES_LIST).commit();

        }
    }

    public void setCityID(int json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putInt(MHConstants.PREF_CITY_ID, json);
            editor.commit();
        }
    }

    public void clearCityID() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_CITY_ID).commit();

        }
    }

    public void setSpecialtyID(int json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putInt(MHConstants.PREF_SPECIALTY_ID, json);
            editor.commit();
        }
    }

    public void clearSpecialtyID() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_SPECIALTY_ID).commit();

        }
    }

    public void setMyLatitude(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_MY_LATITUDE, json);
            editor.commit();
        }
    }

    public void clearMyLatitude() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_MY_LATITUDE).commit();

        }
    }

    public void setMyLongitude(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_MY_LONGITUDE, json);
            editor.commit();
        }
    }

    public void clearMyLongitude() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_MY_LONGITUDE).commit();

        }
    }

    public void setFilterType(int json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putInt(MHConstants.PREF_HOME_FILTER, json);
            editor.commit();
        }
    }

    public void clearFilterType() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_HOME_FILTER).commit();

        }
    }

    public void setChoosenLatitude(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_CHOOSEN_LATITUDE, json);
            editor.commit();
        }
    }

    public void clearChoosenLatitude() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_CHOOSEN_LATITUDE).commit();

        }
    }

    public void setChoosenLongitude(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(MHConstants.PREF_CHOOSEN_LONGITUDE, json);
            editor.commit();
        }
    }

    public void clearChoosenLongitude() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(MHConstants.PREF_CHOOSEN_LONGITUDE).commit();

        }
    }
}
