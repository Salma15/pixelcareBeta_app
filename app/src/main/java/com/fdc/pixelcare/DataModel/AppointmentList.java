package com.fdc.pixelcare.DataModel;

/**
 * Created by HP on 14-05-2018.
 */

public class AppointmentList {

    int app_id, doc_id, dept_id, appt_userid, hospital_id;
    String transaction_id, visit_date, visit_time, patient_name, mobile, email, pay_status, visit_status, appt_logintype, doc_name;

    public AppointmentList(int app_id, String trans_id, int pref_doc, int dept, String visit_date, String visit_time,
                           String patient_name, String mobile, String email, String pay_status, String visit_status,
                           String doctor_name, int user_id, int hosp_id) {
        this.app_id = app_id;
        this.transaction_id = trans_id;
        this.doc_id = pref_doc;
        this.dept_id = dept;
        this.visit_date = visit_date;
        this.visit_time = visit_time;
        this.patient_name = patient_name;
        this.mobile = mobile;
        this.email = email;
        this.pay_status = pay_status;
        this.visit_status = visit_status;
        this.doc_name = doctor_name;
        this.appt_userid = user_id;
        this.hospital_id = hosp_id;
    }

    public int getAppointID() {
        return app_id;
    }
    public void setAppointID(int app_id) { this.app_id = app_id; }

    public int getAppointDoctorID() {
        return doc_id;
    }
    public void setAppointDoctorID(int doc_id) { this.doc_id = doc_id; }

    public int getAppointDeptID() {
        return dept_id;
    }
    public void setAppointDeptID(int dept_id) { this.dept_id = dept_id; }

    public int getAppointUserId() {
        return appt_userid;
    }
    public void setAppointUserId(int appt_userid) { this.appt_userid = appt_userid; }

    public String getAppointLoginType() {
        return appt_logintype;
    }
    public void setAppointLoginType(String appt_logintype) { this.appt_logintype = appt_logintype; }

    public String getTransactionID() {
        return transaction_id;
    }
    public void setTransactionID(String transaction_id) { this.transaction_id = transaction_id; }

    public String getVisitDate() {
        return visit_date;
    }
    public void setVisitDate(String visit_date) { this.visit_date = visit_date; }

    public String getVisitTime() {
        return visit_time;
    }
    public void setVisitTime(String visit_time) { this.visit_time = visit_time; }

    public String getPatientName() {
        return patient_name;
    }
    public void setPatientName(String patient_name) { this.patient_name = patient_name; }

    public String getAppointMobile() {
        return mobile;
    }
    public void setAppointMobile(String mobile) { this.mobile = mobile; }

    public String getAppointEmail() { return email; }
    public void setAppointEmail(String email) { this.email = email; }

    public String getAppointPayStatus() {
        return pay_status;
    }
    public void setAppointPayStatus(String pay_status) { this.pay_status = pay_status; }

    public String getAppointVisitStatus() {
        return visit_status;
    }
    public void setAppointVisitStatus(String visit_status) { this.visit_status = visit_status; }

    public String getAppointDoctorName() {
        return doc_name;
    }
    public void setAppointDoctorName(String doc_name) { this.doc_name = doc_name; }

    public int getAppointHospitalID() {
        return hospital_id;
    }
    public void setAppointHospitalID(int hospital_id) { this.hospital_id = hospital_id; }

}
