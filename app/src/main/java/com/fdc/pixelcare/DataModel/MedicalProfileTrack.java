package com.fdc.pixelcare.DataModel;

/**
 * Created by HP on 23-03-2018.
 */

public class MedicalProfileTrack {
    private int tracker_id, member_id, login_user_id;
    private String age, weight, height, BMI, hypertension, hypertension_date, systolic, diastolic, diabetes, diabetes_date,
            fasting, random, hba1c, cholestrol, cholestrol_date, triglycerides, totalCholestrol, hdl, ldl, vldl, allergies, created_date ;

    public MedicalProfileTrack(int tracker_id, int member_id, int login_user_id, String age, String weight, String height,
                               String bmi, String hypertension, String hypertension_date, String systolic, String diastolic,
                               String diabetes, String diabetes_date, String fasting, String random, String hba1c,
                               String cholestrol, String cholestrol_date, String triglycerides, String totalCholestrol,
                               String hdl, String ldl, String vldl, String allergies, String timestamp) {
        this.tracker_id = tracker_id;
        this.member_id = member_id;
        this.login_user_id = login_user_id;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.BMI = bmi;
        this.hypertension = hypertension;
        this.hypertension_date = hypertension_date;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.diabetes = diabetes;
        this.diabetes_date = diabetes_date;
        this.fasting = fasting;
        this.random = random;
        this.hba1c = hba1c;
        this.cholestrol = cholestrol;
        this.cholestrol_date = cholestrol_date;
        this.triglycerides = triglycerides;
        this.totalCholestrol = totalCholestrol;
        this.hdl = hdl;
        this.ldl = ldl;
        this.vldl = vldl;
        this.allergies = allergies;
        this.created_date = timestamp;
    }

    public int getTrackerid() {
        return tracker_id;
    }
    public void setTrackerid(int tracker_id) {
        this.tracker_id = tracker_id;
    }

    public int getMemberid() {
        return member_id;
    }
    public void setMemberid(int member_id) {
        this.member_id = member_id;
    }

    public int getLoginUserid() {
        return login_user_id;
    }
    public void setLoginUserid(int login_user_id) {
        this.login_user_id = login_user_id;
    }

    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }
    public void setHeight(String height) {
        this.height = height;
    }

    public String getBMI() {
        return BMI;
    }
    public void setBMI(String BMI) {
        this.BMI = BMI;
    }

    public String getHypertension() {
        return hypertension;
    }
    public void setHypertension(String hypertension) {
        this.hypertension = hypertension;
    }

    public String getHypertensionDate() {
        return hypertension_date;
    }
    public void setHypertensionDate(String hypertension_date) {
        this.hypertension_date = hypertension_date;
    }

    public String getSystolic() {
        return systolic;
    }
    public void setSystolic(String systolic) {
        this.systolic = systolic;
    }

    public String getDiastolic() {
        return diastolic;
    }
    public void setDiastolic(String diastolic) {
        this.diastolic = diastolic;
    }

    public String getDiabetes() {
        return diabetes;
    }
    public void setDiabetes(String diabetes) {
        this.diabetes = diabetes;
    }

    public String getDiabetesDate() {
        return diabetes_date;
    }
    public void setDiabetesDate(String diabetes_date) {
        this.diabetes_date = diabetes_date;
    }

    public String getFasting() {
        return fasting;
    }
    public void setFasting(String fasting) {
        this.fasting = fasting;
    }

    public String getRandom() {
        return random;
    }
    public void setRandom(String random) {
        this.random = random;
    }

    public String getHba1c() {
        return hba1c;
    }
    public void setHba1c(String hba1c) {
        this.hba1c = hba1c;
    }

    public String getCholestrol() {
        return cholestrol;
    }
    public void setCholestrol(String cholestrol) {
        this.cholestrol = cholestrol;
    }

    public String getCholestrolDate() {
        return cholestrol_date;
    }
    public void setCholestrolDate(String cholestrol_date) {
        this.cholestrol_date = cholestrol_date;
    }

    public String getTriglycerides() {
        return triglycerides;
    }
    public void setTriglycerides(String triglycerides) {
        this.triglycerides = triglycerides;
    }

    public String getTotalCholestrol() {
        return totalCholestrol;
    }
    public void setTotalCholestrol(String totalCholestrol) {
        this.totalCholestrol = totalCholestrol;
    }

    public String getHDL() {
        return hdl;
    }
    public void setHDL(String hdl) {
        this.hdl = hdl;
    }

    public String getLDL() {
        return ldl;
    }
    public void setLDL(String ldl) {
        this.ldl = ldl;
    }

    public String getVLDL() {
        return vldl;
    }
    public void setVLDL(String vldl) {
        this.vldl = vldl;
    }

    public String getAllergies() {
        return allergies;
    }
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getCreatedDate() {
        return created_date;
    }
    public void setCreatedDate(String created_date) {
        this.created_date = created_date;
    }

}
