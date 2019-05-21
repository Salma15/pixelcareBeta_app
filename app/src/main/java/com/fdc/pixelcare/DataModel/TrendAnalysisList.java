package com.fdc.pixelcare.DataModel;

/**
 * Created by HP on 02-04-2018.
 */

public class TrendAnalysisList {

    int trend_id, user_id, patient_id;
    String trend_date, sysytolic, diastolic, preprandial, postprandial, hba1c, triglycerides, total_cholestrol, hdl, ldl, vldl,
            patient_type, loginType;

    public TrendAnalysisList(int trend_id, String date_added, String systolic, String diastolic, String bp_beforefood_count,
                             String bp_afterfood_count, String hbA1c, String triglyceride, String cholesterol, String hdl,
                             String ldl, String vldl, int patient_id, String patient_type, int user_id, String user_login_type)
    {
        this.trend_id = trend_id;
        this.trend_date = date_added;
        this.sysytolic = systolic;
        this.diastolic = diastolic;
        this.preprandial = bp_beforefood_count;
        this.postprandial = bp_afterfood_count;
        this.hba1c = hbA1c;
        this.triglycerides = triglyceride;
        this.total_cholestrol = cholesterol;
        this.hdl = hdl;
        this.ldl = ldl;
        this.vldl = vldl;
        this.patient_id = patient_id;
        this.patient_type = patient_type;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public String getLoginType() {
        return loginType;
    }
    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public int getUserId() {
        return user_id;
    }
    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getPatientType() {
        return patient_type;
    }
    public void setPatientType(String patient_type) {
        this.patient_type = patient_type;
    }

    public String getVldl() {
        return vldl;
    }
    public void setVldl(String vldl) {
        this.vldl = vldl;
    }

    public String getLdl() {
        return ldl;
    }
    public void setLdl(String hdl) {
        this.ldl = ldl;
    }

    public String getHdl() {
        return hdl;
    }
    public void setHdl(String hdl) {
        this.hdl = hdl;
    }

    public String getTotalCholestrol() {
        return total_cholestrol;
    }
    public void setTotalCholestrol(String total_cholestrol) {
        this.total_cholestrol = total_cholestrol;
    }

    public String getTriglycerides() {
        return triglycerides;
    }
    public void setTriglycerides(String triglycerides) {
        this.triglycerides = triglycerides;
    }

    public String getHba1c() {
        return hba1c;
    }
    public void setHba1c(String hba1c) {
        this.hba1c = hba1c;
    }

    public String getPostPrandial() {
        return postprandial;
    }
    public void setPostPrandial(String postprandial) {
        this.postprandial = postprandial;
    }

    public String getPrePrandial() {
        return preprandial;
    }
    public void setPrePrandial(String preprandial) {
        this.preprandial = preprandial;
    }

    public String getDiastolic() {
        return diastolic;
    }
    public void setDiastolic(String diastolic) {
        this.diastolic = diastolic;
    }

    public String getSystolic() {
        return sysytolic;
    }
    public void setSystolic(String sysytolic) {
        this.sysytolic = sysytolic;
    }

    public int getTrendID() {
        return trend_id;
    }
    public void setTrendID(int trend_id) {
        this.trend_id = trend_id;
    }

    public String getTrendDate() {
        return trend_date;
    }
    public void setTrendDate(String trend_date) {
        this.trend_date = trend_date;
    }

}
