package com.fdc.pixelcare.DataModel;

/**
 * Created by HP on 15-05-2018.
 */

public class OpinionList {

    int patient_id,patient_status,pat_doc_id, login_userid;
    String patient_name, patient_age, patient_city, patient_docname, loginType, referBy, statusTime;
    String address, state, country, weight, hyperCond, diabetesCond, gender, patDesc, queryToDoc, medicalComplaint, mobile, email;

    public OpinionList() {
    }

    public OpinionList(int pId,String pName, String pAge, String pCity, int pStatus, String pDocname,
                       int pat_doc_id, String referBy, int loginUserid, String status_time, String address, String state,
                       String country, String weight, String hyperCond, String diabetesCond, String gender, String patDesc,
                       String queryToDoc, String medicalComplaint, String mobile, String email) {
        this.patient_id = pId;
        this.patient_status = pStatus;
        this.patient_name = pName;
        this.patient_age = pAge;
        this.patient_city = pCity;
        this.patient_docname = pDocname;
        this.pat_doc_id = pat_doc_id;
        this.loginType = loginType;
        this.referBy = referBy;
        this.login_userid = loginUserid;
        this.statusTime = status_time;
        this.address = address;
        this.state = state;
        this.country = country;
        this.weight = weight;
        this.hyperCond = hyperCond;
        this.diabetesCond = diabetesCond;
        this.gender = gender;
        this.patDesc = patDesc;
        this.queryToDoc = queryToDoc;
        this.medicalComplaint = medicalComplaint;
        this.mobile = mobile;
        this.email = email;
    }

    public String getPatientEmail() { return email; }
    public void setPatientEmail(String email) {
        this.email = email;
    }

    public String getPatientMobile() { return mobile; }
    public void setPatientMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPatientMedicalComplaint() { return medicalComplaint; }
    public void setPatientMedicalComplaint(String medicalComplaint) {
        this.medicalComplaint = medicalComplaint;
    }

    public String getPatientQueryToDoc() { return queryToDoc; }
    public void setPatientQueryToDoc(String queryToDoc) {
        this.queryToDoc = queryToDoc;
    }

    public String getPatientDescription() { return patDesc; }
    public void setPatientDescription(String patDesc) {
        this.patDesc = patDesc;
    }

    public String getPatientGender() { return gender; }
    public void setPatientGender(String gender) {
        this.gender = gender;
    }

    public String getPatientDiabetesCondition() { return diabetesCond; }
    public void setPatientDiabetesCondition(String diabetesCond) {
        this.diabetesCond = diabetesCond;
    }

    public String getPatientHyperCondition() { return hyperCond; }
    public void setPatientHyperCondition(String hyperCond) {
        this.hyperCond = hyperCond;
    }

    public String getPatientWeight() { return weight; }
    public void setPatientWeight(String weight) {
        this.weight = weight;
    }

    public String getPatientCountry() { return country; }
    public void setPatientCountry(String country) {
        this.country = country;
    }

    public String getPatientState() { return state; }
    public void setPatientState(String state) {
        this.state = state;
    }

    public String getPatientAddress() { return address; }
    public void setPatientAddress(String address) {
        this.address = address;
    }

    public int getPatientId() { return patient_id; }
    public void setPatientId(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getPatientStatus() { return patient_status; }
    public void setPatientStatus(int patient_status) {
        this.patient_status = patient_status;
    }

    public String getPatientName() {
        return patient_name;
    }
    public void setPatientName(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatientAge() {
        return patient_age;
    }
    public void setPatientAge(String patient_age) {
        this.patient_age = patient_age;
    }

    public String getPatientCity() {
        return patient_city;
    }
    public void setPatientCity(String patient_city) {
        this.patient_city = patient_city;
    }

    public String getPatientDocName() {
        return patient_docname;
    }
    public void setPatientDocName(String patient_docname) { this.patient_docname = patient_docname; }

    public int getPatientDocId() { return pat_doc_id; }
    public void setPatienDocId(int pat_doc_id) {
        this.pat_doc_id = pat_doc_id;
    }

    public String getPatientLoginType() {
        return loginType;
    }
    public void setPatientLoginType(String loginType) { this.loginType = loginType; }

    public String getPatientReferBy() {
        return referBy;
    }
    public void setPatientReferBy(String referBy) { this.referBy = referBy; }

    public int getPatientLoginUserId() { return login_userid; }
    public void setPatientLoginUserId(int login_userid) {
        this.login_userid = login_userid;
    }

    public String getPatientStatusTime() { return statusTime; }
    public void setPatientStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }

}
