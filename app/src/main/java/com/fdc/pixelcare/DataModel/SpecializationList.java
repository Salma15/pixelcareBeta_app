package com.fdc.pixelcare.DataModel;

import java.io.Serializable;

/**
 * Created by salma on 08/03/18.
 */

public class SpecializationList implements Serializable {

    public static final String SPECIALIZATION_TABLE_NAME = "SPECIALIZATIONLIST";
    public static final String SPECIALIZATION_AUTO_ID = "ID";
    public static final String SPECIALIZATION_ID = "SPECIALIZATIONID";
    public static final String SPECIALIZATION_NAME = "SPECIALIZATION";

    int specialization_id, user_id, doc_id, doc_type, spec_group_id;
    String specialization_name, login_type;

    public SpecializationList() {
    }

    public SpecializationList(int specId, String speacNmae) {
        this.specialization_id = specId;
        this.specialization_name = speacNmae;
    }


    public SpecializationList(int specId, String speacNmae, int userid, String loginType) {
        this.specialization_id = specId;
        this.specialization_name = speacNmae;
        this.user_id = userid;
        this.login_type = loginType;
    }


    public SpecializationList(String speacNmae) {
        this.specialization_name = speacNmae;
    }

    public SpecializationList(int spec_id, String spec_name, int doc_id, int doc_type, int spec_group_id) {
        this.specialization_id = spec_id;
        this.specialization_name = spec_name;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.spec_group_id = spec_group_id;
    }

    public int getSpecializationId() { return specialization_id; }
    public void setSpecializationId(int specialization_id) {
        this.specialization_id = specialization_id;
    }

    public String getSpecializationName() {
        return specialization_name;
    }
    public void setSpecializationName(String specialization_name) {
        this.specialization_name = specialization_name;
    }

    public int getSpecializationDocId() { return doc_id; }
    public void setSpecializationDocId(int doc_id) {
        this.doc_id = doc_id;
    }

    public int getSpecializationDocType() {
        return doc_type;
    }
    public void setSpecializationDocType(int doc_type) {
        this.doc_type = doc_type;
    }

    public int getSpecializationGroupID() {
        return spec_group_id;
    }
    public void setSpecializationGroupID(int spec_group_id) {
        this.spec_group_id = spec_group_id;
    }
}
