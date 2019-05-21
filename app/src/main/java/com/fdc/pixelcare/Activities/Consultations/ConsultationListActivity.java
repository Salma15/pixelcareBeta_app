package com.fdc.pixelcare.Activities.Consultations;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.Adapters.Consultations.ConsultaionListAdapter;
import com.fdc.pixelcare.DataModel.ChiefMedicalComplaint;
import com.fdc.pixelcare.DataModel.Diagnosis;
import com.fdc.pixelcare.DataModel.DrugAbuse;
import com.fdc.pixelcare.DataModel.DrugAllery;
import com.fdc.pixelcare.DataModel.EpisodesList;
import com.fdc.pixelcare.DataModel.Examinations;
import com.fdc.pixelcare.DataModel.FamilyHistory;
import com.fdc.pixelcare.DataModel.FamilyMember;
import com.fdc.pixelcare.DataModel.FrequentPrescription;
import com.fdc.pixelcare.DataModel.Investigations;
import com.fdc.pixelcare.DataModel.Lids;
import com.fdc.pixelcare.DataModel.MedicalProfileTrack;
import com.fdc.pixelcare.DataModel.OphthalAngleAnteriorChamber;
import com.fdc.pixelcare.DataModel.OphthalAnteriorChamber;
import com.fdc.pixelcare.DataModel.OphthalConjuctiva;
import com.fdc.pixelcare.DataModel.OphthalCornearAnteriorSurface;
import com.fdc.pixelcare.DataModel.OphthalCornearPosteriorSurface;
import com.fdc.pixelcare.DataModel.OphthalFundus;
import com.fdc.pixelcare.DataModel.OphthalIris;
import com.fdc.pixelcare.DataModel.OphthalLens;
import com.fdc.pixelcare.DataModel.OphthalPupil;
import com.fdc.pixelcare.DataModel.OphthalSclera;
import com.fdc.pixelcare.DataModel.OphthalViterous;
import com.fdc.pixelcare.DataModel.Treatments;
import com.fdc.pixelcare.DataModel.TrendAnalysisList;
import com.fdc.pixelcare.DataModel.TrendOphthalAnalysisList;
import com.fdc.pixelcare.DataModel.ViewReports;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.Network.CustomVolleyRequestQueue;
import com.fdc.pixelcare.Network.NetworkUtil;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
import com.fdc.pixelcare.Views.CustomTextViewItalicBold;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SALMA on 31-12-2018.
 */
public class ConsultationListActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener, Response.ErrorListener {

    public static final String REQUEST_TAG = "ConsultationListActivity";
    CustomTextViewItalicBold member_name_text;

    int USER_ID,USER_LOGINTYPE, LOGIN_MEMBER_ID;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION, LOGIN_MEMBER_NAME, FAMILY_MEMBER_LIST, EPISODES_LIST_SHARED;
    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    ArrayList<FamilyMember> memberArraylist;

    LinearLayout footer_consultation, footer_appointment, footer_opinion, footer_medicalprofile, footer_search;

    RecyclerView tracking_recyclerview;
    ConsultaionListAdapter trackAdapter;
    private List<MedicalProfileTrack> trackList = new ArrayList<>();
    CustomTextViewItalicBold no_data;

    // Ophthal Episodes
    List<EpisodesList> EPISODE_LIST = new ArrayList<>();
    List<EpisodesList> EPISODE_LIST_TEMP = new ArrayList<>();

    List<ChiefMedicalComplaint> PATIENT_CHIEF_MEDCOMPLAINT_ARRAY;
    List<Investigations> PATIENT_INVESTIGATION_GENERAL_ARRAY, PATIENT_INVESTIGATION_OPHTHAL_ARRAY;
    List<Diagnosis> PATIENT_DAIGNOSIS_ARRAY;
    List<Treatments> PATIENT_TREATMENT_ARRAY;
    List<FrequentPrescription> PATIENT_PRESCRIPTION_ARRAY;
    List<Lids> PATIENT_LIDS_ARRAY;
    List<OphthalConjuctiva> PATIENT_CONJUCTIVA_ARRAY;
    List<OphthalSclera> PATIENT_SCLERA_ARRAY;
    List<OphthalCornearAnteriorSurface> PATIENT_CORNEA_ANTERIOR_ARRAY;
    List<OphthalCornearPosteriorSurface> PATIENT_CORNEA_POSTERIOR_ARRAY;
    List<OphthalAnteriorChamber> PATIENT_ANTERIOR_CHAMBER_ARRAY;
    List<OphthalIris> PATIENT_IRIS_ARRAY;
    List<OphthalPupil> PATIENT_PUPIL_ARRAY;
    List<OphthalAngleAnteriorChamber> PATIENT_ANGLE_ARRAY;
    List<OphthalLens> PATIENT_LENS_ARRAY;
    List<OphthalViterous> PATIENT_VITEROUS_ARRAY;
    List<OphthalFundus> PATIENT_FUNDUS_ARRAY;
    List<Examinations> PATIENT_EXAMINATION_ARRAY;
    List<DrugAllery> PATIENT_DRUG_ALLERTY_ARRAY;
    List<DrugAbuse> PATIENT_DRUG_ABUSE_ARRAY;
    List<FamilyHistory> PATIENT_FAMILY_HISTORY_ARRAY;

    List<ViewReports> PATIENT_REPORT_FOLDER_ARRAY = new ArrayList<>();
    List<ViewReports> PATIENT_REPORT_ATTACHMENT_ARRAY = new ArrayList<>();

    List<TrendAnalysisList> PATIENT_TREND_ANALYSIS = new ArrayList<>();
    List<TrendOphthalAnalysisList> PATIENT_OPHTHAL_TREND_ANALYSIS = new ArrayList<>();

    private RequestQueue mQueue;
    ProgressDialog pd;
    String PATIENT_ID, PATIENT_NAME, PATIENT_GENDER, PATIENT_HEIGHT, PATIENT_WEIGHT, PATIENT_HYPERTENSION, PATIENT_DIABETES, PATIENT_SMOKING,
            PATIENT_ALCOHOLIC, PATIENT_PREV_INTERVENTION, PATIENT_NEURO, PATIENT_KIDNEY, PATIENT_OTHER_DETAILS;

    String CONSULTATION_FILTER_TYPE = "1"; // 1 - initial Load, 2 - Member list load


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_list);
        memberArraylist = new ArrayList<>();
        CONSULTATION_FILTER_TYPE = "1";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);

        }

        memberArraylist = new ArrayList<>();

        shareadPreferenceClass = new ShareadPreferenceClass(ConsultationListActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(ConsultationListActivity.this);

        if(sharedPreferences !=null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE  = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);
            LOGIN_MEMBER_ID = sharedPreferences.getInt(MHConstants.PREF_LOGIN_MEMBER_ID,0);
            LOGIN_MEMBER_NAME  = sharedPreferences.getString(MHConstants.PREF_LOGIN_MEMBER_NEME,"");
            FAMILY_MEMBER_LIST = sharedPreferences.getString(MHConstants.PREF_FAMILY_MEMBERS, "");
            EPISODES_LIST_SHARED = sharedPreferences.getString(MHConstants.PREF_CONSULTATIONS_LIST, "");

            Log.d(AppUtils.TAG , " *********** ConsultationListActivity **************** ");
            Log.d(AppUtils.TAG + " UserID: ", String.valueOf(USER_ID));
            Log.d(AppUtils.TAG + " name: ", USER_NAME);
            Log.d(AppUtils.TAG + " email: ", USER_EMAIL);
            Log.d(AppUtils.TAG + " MOBILE: ", USER_MOBILE);
            Log.d(AppUtils.TAG + " Location: ", USER_LOCATION);
            Log.d(AppUtils.TAG + " LOGIN_MEMBER_ID: ", String.valueOf(LOGIN_MEMBER_ID));
            Log.d(AppUtils.TAG + " LOGIN_MEM_NAME: ", LOGIN_MEMBER_NAME);
            Log.d(AppUtils.TAG + " logintype: ", String.valueOf(USER_LOGINTYPE));
        }

        initializeViews();
    }

    private void initializeViews() {

        EPISODE_LIST = new ArrayList<>();
        EPISODE_LIST_TEMP = new ArrayList<>();
        PATIENT_CHIEF_MEDCOMPLAINT_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_GENERAL_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_OPHTHAL_ARRAY = new ArrayList<>();
        PATIENT_DAIGNOSIS_ARRAY = new ArrayList<>();
        PATIENT_TREATMENT_ARRAY = new ArrayList<>();
        PATIENT_PRESCRIPTION_ARRAY = new ArrayList<>();
        PATIENT_LIDS_ARRAY = new ArrayList<>();
        PATIENT_CONJUCTIVA_ARRAY = new ArrayList<>();
        PATIENT_SCLERA_ARRAY = new ArrayList<>();
        PATIENT_CORNEA_ANTERIOR_ARRAY = new ArrayList<>();
        PATIENT_CORNEA_POSTERIOR_ARRAY = new ArrayList<>();
        PATIENT_ANTERIOR_CHAMBER_ARRAY = new ArrayList<>();
        PATIENT_IRIS_ARRAY = new ArrayList<>();
        PATIENT_PUPIL_ARRAY = new ArrayList<>();
        PATIENT_ANGLE_ARRAY = new ArrayList<>();
        PATIENT_LENS_ARRAY = new ArrayList<>();
        PATIENT_VITEROUS_ARRAY = new ArrayList<>();
        PATIENT_FUNDUS_ARRAY = new ArrayList<>();
        PATIENT_EXAMINATION_ARRAY = new ArrayList<>();
        PATIENT_DRUG_ALLERTY_ARRAY = new ArrayList<>();
        PATIENT_DRUG_ABUSE_ARRAY = new ArrayList<>();
        PATIENT_FAMILY_HISTORY_ARRAY = new ArrayList<>();
        PATIENT_REPORT_FOLDER_ARRAY = new ArrayList<>();
        PATIENT_REPORT_ATTACHMENT_ARRAY = new ArrayList<>();
        PATIENT_TREND_ANALYSIS = new ArrayList<>();
        PATIENT_OPHTHAL_TREND_ANALYSIS = new ArrayList<>();
        memberArraylist = new ArrayList<>();

        member_name_text = (CustomTextViewItalicBold) findViewById(R.id.consultation_membername);
        member_name_text.setText("Patient Name: "+LOGIN_MEMBER_NAME);

        tracking_recyclerview = (RecyclerView) findViewById(R.id.consultations_recyclerview);
        trackAdapter = new ConsultaionListAdapter(ConsultationListActivity.this, EPISODE_LIST, LOGIN_MEMBER_NAME);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ConsultationListActivity.this);
        tracking_recyclerview.setLayoutManager(mLayoutManager);
        tracking_recyclerview.setItemAnimator(new DefaultItemAnimator());
        tracking_recyclerview.setAdapter(trackAdapter);

        no_data = (CustomTextViewItalicBold) findViewById(R.id.consultations_empty);
        no_data.setVisibility(View.GONE);

        //   getConsultaionsListFromServer();

        Gson gson = new Gson();
        if (EPISODES_LIST_SHARED.equals("")) {
            if (NetworkUtil.getConnectivityStatusString(ConsultationListActivity.this).equalsIgnoreCase("enabled")) {
                getConsultaionsListFromServer();
            } else {
                AppUtils.showCustomAlertMessage(ConsultationListActivity.this, MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            EPISODE_LIST = gson.fromJson(EPISODES_LIST_SHARED, new TypeToken<List<EpisodesList>>() {
            }.getType());
            if(EPISODE_LIST.size() > 0 ) {
                no_data.setVisibility(View.GONE);
                tracking_recyclerview.setVisibility(View.VISIBLE);

                trackAdapter = new ConsultaionListAdapter(ConsultationListActivity.this, EPISODE_LIST, LOGIN_MEMBER_NAME);
                tracking_recyclerview.setAdapter(trackAdapter);
                trackAdapter.notifyDataSetChanged();
            }
            else {
                tracking_recyclerview.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private void getConsultaionsListFromServer() {
        pd = new ProgressDialog(ConsultationListActivity.this, R.style.CustomDialog);
        pd.setCancelable(false);
        pd.setMessage("Loading Consultations....");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();

        // Instantiate the RequestQueue.
        mQueue = CustomVolleyRequestQueue.getInstance(ConsultationListActivity.this).getRequestQueue();

        StringRequest postRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_EPISODE_LIST,
                this,this ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(APIParam.KEY_API, APIClass.API_KEY);
                params.put(APIParam.KEY_DATA_SOURCE, APIClass.API_DATASOURCE_KEY);
                params.put(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID));
                params.put(APIParam.KEY_MEMBER_ID, String.valueOf(LOGIN_MEMBER_ID));
                params.put(APIParam.KEY_APPOINT_FILTER_TYPE, String.valueOf(CONSULTATION_FILTER_TYPE));
                return params;
            }
        };

        postRequest.setTag(REQUEST_TAG);
        mQueue.add(postRequest);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        VolleyLog.e(AppUtils.TAG +" Error: ", error.getMessage());
        if(pd!=null || pd.isShowing())
            pd.dismiss();
        AppUtils.showCustomAlertMessage(ConsultationListActivity.this, "My Consultations !!!",error.getMessage(), "OK", null, null);

    }

    @Override
    public void onResponse(Object response) {
        Log.d(AppUtils.TAG +" Result: ", response.toString());
        String staus_res = null, opinion_list = null;

        EPISODE_LIST = new ArrayList<>();
        EPISODE_LIST_TEMP = new ArrayList<>();
        PATIENT_CHIEF_MEDCOMPLAINT_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_GENERAL_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_OPHTHAL_ARRAY = new ArrayList<>();
        PATIENT_DAIGNOSIS_ARRAY = new ArrayList<>();
        PATIENT_TREATMENT_ARRAY = new ArrayList<>();
        PATIENT_PRESCRIPTION_ARRAY = new ArrayList<>();
        PATIENT_EXAMINATION_ARRAY = new ArrayList<>();

        if(pd!=null || pd.isShowing())
            pd.dismiss();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(String.valueOf(response));
            if (jsonObject.has("result_list")) {
                opinion_list = jsonObject.getString("result_list");
            }

            if(opinion_list != null && !opinion_list.isEmpty()) {
                if(opinion_list.equalsIgnoreCase("failure")) {
                    Toast.makeText(ConsultationListActivity.this, "Health Tracker \n"+jsonObject.getString("err_msg"), Toast.LENGTH_SHORT).show();
                }
                else {
                    trackList = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("episode_details");
                    if (jsonArray.length() > 0) {
                        no_data.setVisibility(View.GONE);
                        tracking_recyclerview.setVisibility(View.VISIBLE);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject epObj = jsonArray.getJSONObject(i);
                            Log.d(AppUtils.TAG, " epID: "+ epObj.getString("episode_id"));

                            PATIENT_CHIEF_MEDCOMPLAINT_ARRAY = new ArrayList<>();
                            PATIENT_INVESTIGATION_GENERAL_ARRAY = new ArrayList<>();
                            PATIENT_INVESTIGATION_OPHTHAL_ARRAY = new ArrayList<>();
                            PATIENT_DAIGNOSIS_ARRAY = new ArrayList<>();
                            PATIENT_TREATMENT_ARRAY = new ArrayList<>();
                            PATIENT_PRESCRIPTION_ARRAY = new ArrayList<>();
                            PATIENT_EXAMINATION_ARRAY = new ArrayList<>();

                            PATIENT_LIDS_ARRAY = new ArrayList<>();
                            PATIENT_CONJUCTIVA_ARRAY = new ArrayList<>();
                            PATIENT_SCLERA_ARRAY = new ArrayList<>();
                            PATIENT_CORNEA_ANTERIOR_ARRAY = new ArrayList<>();
                            PATIENT_CORNEA_POSTERIOR_ARRAY = new ArrayList<>();
                            PATIENT_ANTERIOR_CHAMBER_ARRAY = new ArrayList<>();
                            PATIENT_IRIS_ARRAY = new ArrayList<>();
                            PATIENT_PUPIL_ARRAY = new ArrayList<>();
                            PATIENT_ANGLE_ARRAY = new ArrayList<>();
                            PATIENT_LENS_ARRAY = new ArrayList<>();
                            PATIENT_VITEROUS_ARRAY = new ArrayList<>();
                            PATIENT_FUNDUS_ARRAY = new ArrayList<>();
                            PATIENT_DRUG_ALLERTY_ARRAY = new ArrayList<>();
                            PATIENT_DRUG_ABUSE_ARRAY = new ArrayList<>();
                            PATIENT_FAMILY_HISTORY_ARRAY = new ArrayList<>();
                            PATIENT_REPORT_FOLDER_ARRAY = new ArrayList<>();
                            PATIENT_REPORT_ATTACHMENT_ARRAY = new ArrayList<>();
                            PATIENT_TREND_ANALYSIS = new ArrayList<>();
                            PATIENT_OPHTHAL_TREND_ANALYSIS = new ArrayList<>();

                            PATIENT_ID = epObj.getString("patient_id");
                            PATIENT_NAME = epObj.getString("patient_name");
                            PATIENT_GENDER = epObj.getString("patient_gen");
                            PATIENT_HEIGHT = epObj.getString("height");
                            PATIENT_WEIGHT = epObj.getString("weight");
                            PATIENT_HYPERTENSION = epObj.getString("hyper_cond");
                            PATIENT_DIABETES = epObj.getString("diabetes_cond");
                            PATIENT_SMOKING = epObj.getString("smoking");
                            PATIENT_ALCOHOLIC = epObj.getString("alcoholic");
                            PATIENT_PREV_INTERVENTION = epObj.getString("prev_inter");
                            PATIENT_NEURO  = epObj.getString("neuro_issue");
                            PATIENT_KIDNEY = epObj.getString("kidney_issue");
                            PATIENT_OTHER_DETAILS = epObj.getString("other_details");

                          /*  Log.d(AppUtils.TAG, " PRESC NOTE "+ epObj.getString("prescription_note"));
                            Log.d(AppUtils.TAG, " TRETA NOTE "+ epObj.getString("treatment_details"));
                            Log.d(AppUtils.TAG, " DIAGN NOTE "+ epObj.getString("diagnosis_details"));*/

                            if(epObj.has("chief_medical_complaint_result")) {
                                JSONArray jsonArrayChiefMed = epObj.getJSONArray("chief_medical_complaint_result");
                                for (int j = 0; j < jsonArrayChiefMed.length(); j++) {
                                    Log.d(AppUtils.TAG, " ChiefMedID: "+ jsonArrayChiefMed.getJSONObject(j).getString("symptoms_id"));

                                    PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.add(new ChiefMedicalComplaint(jsonArrayChiefMed.getJSONObject(j).getInt("symptoms_id"),
                                            jsonArrayChiefMed.getJSONObject(j).getString("symptoms_name"),jsonArrayChiefMed.getJSONObject(j).getInt("doc_id"),
                                            jsonArrayChiefMed.getJSONObject(j).getInt("doc_type"),0, ""));

                                }
                            }

                            if(epObj.has("investigation_result")) {
                                JSONArray jsonArrayInvestigation = epObj.getJSONArray("investigation_result");
                                for (int k = 0; k < jsonArrayInvestigation.length(); k++) {
                                    Log.d(AppUtils.TAG, " invest departmentID: "+ jsonArrayInvestigation.getJSONObject(k).getInt("department"));

                                    if(jsonArrayInvestigation.getJSONObject(k).getInt("department") == 1) {
                                        PATIENT_INVESTIGATION_GENERAL_ARRAY.add(new Investigations(jsonArrayInvestigation.getJSONObject(k).getInt("pti_id"),
                                                jsonArrayInvestigation.getJSONObject(k).getString("main_test_id"),jsonArrayInvestigation.getJSONObject(k).getString("group_test_id"),
                                                jsonArrayInvestigation.getJSONObject(k).getString("test_name"),jsonArrayInvestigation.getJSONObject(k).getString("normal_range"),
                                                jsonArrayInvestigation.getJSONObject(k).getString("test_actual_value"),jsonArrayInvestigation.getJSONObject(k).getString("right_eye"),
                                                jsonArrayInvestigation.getJSONObject(k).getString("left_eye"),jsonArrayInvestigation.getJSONObject(k).getInt("department"),
                                                0, ""));
                                    }
                                    else  if(jsonArrayInvestigation.getJSONObject(k).getInt("department") == 2) {
                                        PATIENT_INVESTIGATION_OPHTHAL_ARRAY.add(new Investigations(jsonArrayInvestigation.getJSONObject(k).getInt("pti_id"),
                                                jsonArrayInvestigation.getJSONObject(k).getString("main_test_id"),jsonArrayInvestigation.getJSONObject(k).getString("group_test_id"),
                                                jsonArrayInvestigation.getJSONObject(k).getString("test_name"),jsonArrayInvestigation.getJSONObject(k).getString("normal_range"),
                                                jsonArrayInvestigation.getJSONObject(k).getString("test_actual_value"),jsonArrayInvestigation.getJSONObject(k).getString("right_eye"),
                                                jsonArrayInvestigation.getJSONObject(k).getString("left_eye"),jsonArrayInvestigation.getJSONObject(k).getInt("department"),
                                                0, ""));
                                    }
                                }
                            }

                            if(epObj.has("diagnosis_result")) {
                                JSONArray jsonArrayDiagnosis = epObj.getJSONArray("diagnosis_result");
                                for (int m = 0; m < jsonArrayDiagnosis.length(); m++) {
                                    PATIENT_DAIGNOSIS_ARRAY.add(new Diagnosis(jsonArrayDiagnosis.getJSONObject(m).getInt("diagnosis_autoid"),jsonArrayDiagnosis.getJSONObject(m).getInt("icd_id"),
                                            jsonArrayDiagnosis.getJSONObject(m).getString("icd_code_name"),jsonArrayDiagnosis.getJSONObject(m).getInt("doc_id"),
                                            jsonArrayDiagnosis.getJSONObject(m).getInt("doc_type"),0,0, ""));
                                }
                            }

                            if(epObj.has("treatment_result")) {
                                JSONArray jsonArrayTreatment = epObj.getJSONArray("treatment_result");
                                for (int n = 0; n < jsonArrayTreatment.length(); n++) {
                                    PATIENT_TREATMENT_ARRAY.add(new Treatments(jsonArrayTreatment.getJSONObject(n).getInt("treatment_id"),
                                            jsonArrayTreatment.getJSONObject(n).getString("treatment_name"),jsonArrayTreatment.getJSONObject(n).getInt("doc_id"),
                                            jsonArrayTreatment.getJSONObject(n).getInt("doc_type"),0,0, ""));
                                }
                            }

                            if(epObj.has("prescription_result")) {
                                JSONArray jsonArrayPresc = epObj.getJSONArray("prescription_result");
                                for (int p = 0; p < jsonArrayPresc.length(); p++) {
                                    Log.d(AppUtils.TAG, " prescID: "+ jsonArrayPresc.getJSONObject(p).getInt("episode_prescription_id"));
                                    PATIENT_PRESCRIPTION_ARRAY.add(new FrequentPrescription(jsonArrayPresc.getJSONObject(p).getInt("episode_prescription_id"),
                                            jsonArrayPresc.getJSONObject(p).getInt("pp_id"), jsonArrayPresc.getJSONObject(p).getString("prescription_trade_name"),
                                            0,jsonArrayPresc.getJSONObject(p).getString("prescription_generic_name"),jsonArrayPresc.getJSONObject(p).getString("prescription_frequency"),
                                            jsonArrayPresc.getJSONObject(p).getString("timing"), jsonArrayPresc.getJSONObject(p).getString("duration"),
                                            jsonArrayPresc.getJSONObject(p).getInt("doc_id"),1,0,0, ""));
                                }
                            }

                            if(epObj.has("lids_result")) {
                                JSONArray jsonArrayLids = epObj.getJSONArray("lids_result");
                                for (int a = 0; a < jsonArrayLids.length(); a++) {
                                    PATIENT_LIDS_ARRAY.add(new Lids(jsonArrayLids.getJSONObject(a).getInt("lids_id"),
                                            jsonArrayLids.getJSONObject(a).getString("lids_name"),jsonArrayLids.getJSONObject(a).getInt("doc_id"),
                                            jsonArrayLids.getJSONObject(a).getInt("doc_type"),jsonArrayLids.getJSONObject(a).getString("left_eye"),
                                            jsonArrayLids.getJSONObject(a).getString("right_eye"),0, ""));
                                }
                            }

                            if(epObj.has("conjuctiva_result")) {
                                JSONArray jsonArrayConjuctiva = epObj.getJSONArray("conjuctiva_result");
                                for (int b = 0; b < jsonArrayConjuctiva.length(); b++) {
                                    PATIENT_CONJUCTIVA_ARRAY.add(new OphthalConjuctiva(jsonArrayConjuctiva.getJSONObject(b).getInt("conjuctiva_id"),
                                            jsonArrayConjuctiva.getJSONObject(b).getString("conjuctiva_name"),jsonArrayConjuctiva.getJSONObject(b).getInt("doc_id"),
                                            jsonArrayConjuctiva.getJSONObject(b).getInt("doc_type"),jsonArrayConjuctiva.getJSONObject(b).getString("left_eye"),
                                            jsonArrayConjuctiva.getJSONObject(b).getString("right_eye"),0, ""));
                                }
                            }

                            if(epObj.has("sclera_result")) {
                                JSONArray jsonArraySclera = epObj.getJSONArray("sclera_result");
                                for (int c = 0; c < jsonArraySclera.length(); c++) {
                                    PATIENT_SCLERA_ARRAY.add(new OphthalSclera(jsonArraySclera.getJSONObject(c).getInt("sclera_id"),
                                            jsonArraySclera.getJSONObject(c).getString("scelra_name"),jsonArraySclera.getJSONObject(c).getInt("doc_id"),
                                            jsonArraySclera.getJSONObject(c).getInt("doc_type"),jsonArraySclera.getJSONObject(c).getString("left_eye"),
                                            jsonArraySclera.getJSONObject(c).getString("right_eye"),0, ""));
                                }

                            }

                            if(epObj.has("cornea_anterior_result")) {
                                JSONArray jsonArrayCorneaAnt = epObj.getJSONArray("cornea_anterior_result");
                                for (int d = 0; d < jsonArrayCorneaAnt.length(); d++) {
                                    PATIENT_CORNEA_ANTERIOR_ARRAY.add(new OphthalCornearAnteriorSurface(jsonArrayCorneaAnt.getJSONObject(d).getInt("cornea_ant_id"),
                                            jsonArrayCorneaAnt.getJSONObject(d).getString("cornea_ant_name"),jsonArrayCorneaAnt.getJSONObject(d).getInt("doc_id"),
                                            jsonArrayCorneaAnt.getJSONObject(d).getInt("doc_type"),jsonArrayCorneaAnt.getJSONObject(d).getString("left_eye"),
                                            jsonArrayCorneaAnt.getJSONObject(d).getString("right_eye"),0, ""));
                                }

                            }

                            if(epObj.has("cornea_posterior_result")) {
                                JSONArray jsonArrayCorneaPost = epObj.getJSONArray("cornea_posterior_result");
                                for (int e = 0; e < jsonArrayCorneaPost.length(); e++) {
                                    PATIENT_CORNEA_POSTERIOR_ARRAY.add(new OphthalCornearPosteriorSurface(jsonArrayCorneaPost.getJSONObject(e).getInt("cornea_post_id"),
                                            jsonArrayCorneaPost.getJSONObject(e).getString("cornea_post_name"),jsonArrayCorneaPost.getJSONObject(e).getInt("doc_id"),
                                            jsonArrayCorneaPost.getJSONObject(e).getInt("doc_type"),jsonArrayCorneaPost.getJSONObject(e).getString("left_eye"),
                                            jsonArrayCorneaPost.getJSONObject(e).getString("right_eye"),0, ""));
                                }
                            }

                            if(epObj.has("anterior_chamber_result")) {
                                JSONArray jsonArrayAntChamber = epObj.getJSONArray("anterior_chamber_result");
                                for (int f = 0; f < jsonArrayAntChamber.length(); f++) {
                                    PATIENT_ANTERIOR_CHAMBER_ARRAY.add(new OphthalAnteriorChamber(jsonArrayAntChamber.getJSONObject(f).getInt("chamber_id"),
                                            jsonArrayAntChamber.getJSONObject(f).getString("chamber_name"),jsonArrayAntChamber.getJSONObject(f).getInt("doc_id"),
                                            jsonArrayAntChamber.getJSONObject(f).getInt("doc_type"),jsonArrayAntChamber.getJSONObject(f).getString("left_eye"),
                                            jsonArrayAntChamber.getJSONObject(f).getString("right_eye"),0, ""));
                                }
                            }

                            if(epObj.has("iris_result")) {
                                JSONArray jsonArrayIris = epObj.getJSONArray("iris_result");
                                for (int g = 0; g < jsonArrayIris.length(); g++) {
                                    PATIENT_IRIS_ARRAY.add(new OphthalIris(jsonArrayIris.getJSONObject(g).getInt("iris_id"),
                                            jsonArrayIris.getJSONObject(g).getString("iris_name"),jsonArrayIris.getJSONObject(g).getInt("doc_id"),
                                            jsonArrayIris.getJSONObject(g).getInt("doc_type"),jsonArrayIris.getJSONObject(g).getString("left_eye"),
                                            jsonArrayIris.getJSONObject(g).getString("right_eye"),0, ""));
                                }

                            }

                            if(epObj.has("pupil_result")) {
                                JSONArray jsonArrayPupil = epObj.getJSONArray("pupil_result");
                                for (int h = 0; h < jsonArrayPupil.length(); h++) {
                                    PATIENT_PUPIL_ARRAY.add(new OphthalPupil(jsonArrayPupil.getJSONObject(h).getInt("pupil_id"),
                                            jsonArrayPupil.getJSONObject(h).getString("pupil_name"),jsonArrayPupil.getJSONObject(h).getInt("doc_id"),
                                            jsonArrayPupil.getJSONObject(h).getInt("doc_type"),jsonArrayPupil.getJSONObject(h).getString("left_eye"),
                                            jsonArrayPupil.getJSONObject(h).getString("right_eye"),0, ""));
                                }
                            }

                            if(epObj.has("angle_result")) {
                                JSONArray jsonArrayAngle = epObj.getJSONArray("angle_result");
                                for (int q = 0; q < jsonArrayAngle.length(); q++) {
                                    PATIENT_ANGLE_ARRAY.add(new OphthalAngleAnteriorChamber(jsonArrayAngle.getJSONObject(q).getInt("angle_id"),
                                            jsonArrayAngle.getJSONObject(q).getString("angle_name"),jsonArrayAngle.getJSONObject(q).getInt("doc_id"),
                                            jsonArrayAngle.getJSONObject(q).getInt("doc_type"),jsonArrayAngle.getJSONObject(q).getString("left_eye"),
                                            jsonArrayAngle.getJSONObject(q).getString("right_eye"),0, ""));
                                }
                            }

                            if(epObj.has("lens_result")) {
                                JSONArray jsonArrayLens = epObj.getJSONArray("lens_result");
                                for (int r = 0; r < jsonArrayLens.length(); r++) {
                                    PATIENT_LENS_ARRAY.add(new OphthalLens(jsonArrayLens.getJSONObject(r).getInt("lens_id"),
                                            jsonArrayLens.getJSONObject(r).getString("lens_name"),jsonArrayLens.getJSONObject(r).getInt("doc_id"),
                                            jsonArrayLens.getJSONObject(r).getInt("doc_type"),jsonArrayLens.getJSONObject(r).getString("left_eye"),
                                            jsonArrayLens.getJSONObject(r).getString("right_eye"),0, ""));
                                }

                            }

                            if(epObj.has("viterous_result")) {
                                JSONArray jsonArrayViterous = epObj.getJSONArray("viterous_result");
                                for (int s = 0; s < jsonArrayViterous.length(); s++) {
                                    PATIENT_VITEROUS_ARRAY.add(new OphthalViterous(jsonArrayViterous.getJSONObject(s).getInt("viterous_id"),
                                            jsonArrayViterous.getJSONObject(s).getString("viterous_name"),jsonArrayViterous.getJSONObject(s).getInt("doc_id"),
                                            jsonArrayViterous.getJSONObject(s).getInt("doc_type"),jsonArrayViterous.getJSONObject(s).getString("left_eye"),
                                            jsonArrayViterous.getJSONObject(s).getString("right_eye"),0, ""));
                                }
                            }

                            if(epObj.has("fundus_result")) {
                                JSONArray jsonArrayFundus = epObj.getJSONArray("fundus_result");
                                for (int t = 0; t < jsonArrayFundus.length(); t++) {
                                    PATIENT_FUNDUS_ARRAY.add(new OphthalFundus(jsonArrayFundus.getJSONObject(t).getInt("fundus_id"),
                                            jsonArrayFundus.getJSONObject(t).getString("fundus_name"),jsonArrayFundus.getJSONObject(t).getInt("doc_id"),
                                            jsonArrayFundus.getJSONObject(t).getInt("doc_type"),jsonArrayFundus.getJSONObject(t).getString("left_eye"),
                                            jsonArrayFundus.getJSONObject(t).getString("right_eye"),0, ""));
                                }
                            }

                            if(epObj.has("examination_result")){
                                JSONArray jsonArrayExamination = epObj.getJSONArray("examination_result");
                                for (int l = 0; l < jsonArrayExamination.length(); l++) {
                                    PATIENT_EXAMINATION_ARRAY.add(new Examinations(jsonArrayExamination.getJSONObject(l).getInt("examination_id"),
                                            jsonArrayExamination.getJSONObject(l).getString("examination_name"),jsonArrayExamination.getJSONObject(l).getString("exam_result"),
                                            jsonArrayExamination.getJSONObject(l).getString("findings"),jsonArrayExamination.getJSONObject(l).getInt("doc_id"),
                                            jsonArrayExamination.getJSONObject(l).getInt("doc_type"),0, ""));
                                }
                            }

                            if(epObj.has("drug_allergy_result")) {
                                JSONArray jsonArray1 = epObj.getJSONArray("drug_allergy_result");
                                for (int m = 0; m < jsonArray1.length(); m++) {
                                    PATIENT_DRUG_ALLERTY_ARRAY.add(new DrugAllery(jsonArray1.getJSONObject(m).getInt("allergy_id"),jsonArray1.getJSONObject(m).getInt("generic_id"),
                                            jsonArray1.getJSONObject(m).getString("generic_name"),jsonArray1.getJSONObject(m).getInt("patient_id"),
                                            jsonArray1.getJSONObject(m).getInt("doc_id"),jsonArray1.getJSONObject(m).getInt("doc_type"),0,""));

                                }
                            }

                            if(epObj.has("drug_abuse_result")) {
                                JSONArray jsonArray1 = epObj.getJSONArray("drug_abuse_result");
                                for (int n = 0; n < jsonArray1.length(); n++) {
                                    PATIENT_DRUG_ABUSE_ARRAY.add(new DrugAbuse(0,jsonArray1.getJSONObject(n).getInt("drug_abuse_id"),jsonArray1.getJSONObject(n).getString("drug_abuse"),
                                            jsonArray1.getJSONObject(n).getInt("doc_id"),jsonArray1.getJSONObject(n).getInt("doc_type"),0,0, ""));

                                }
                            }

                            if(epObj.has("family_history_result")) {
                                JSONArray jsonArray1 = epObj.getJSONArray("family_history_result");
                                for (int p = 0; p < jsonArray1.length(); p++) {
                                    PATIENT_FAMILY_HISTORY_ARRAY.add(new FamilyHistory(0,jsonArray1.getJSONObject(p).getInt("family_history_id"),jsonArray1.getJSONObject(p).getString("family_history"),
                                            jsonArray1.getJSONObject(p).getInt("doc_id"),jsonArray1.getJSONObject(p).getInt("doc_type"),0,0, ""));

                                }
                            }

                            if(epObj.has("doc_patient_attachments")) {
                                JSONArray jsonArray1 = epObj.getJSONArray("reports_details");
                                for (int q = 0; q < jsonArray1.length(); q++) {
                                    PATIENT_REPORT_ATTACHMENT_ARRAY.add(new ViewReports(jsonArray1.getJSONObject(q).getInt("report_id"),
                                            jsonArray1.getJSONObject(q).getInt("patient_id"),jsonArray1.getJSONObject(q).getString("report_folder"),
                                            jsonArray1.getJSONObject(q).getString("attachments"),jsonArray1.getJSONObject(q).getInt("user_id"),
                                            jsonArray1.getJSONObject(q).getInt("user_type"),jsonArray1.getJSONObject(q).getString("date_added"),
                                            jsonArray1.getJSONObject(q).getString("username")));

                                    //  PATIENT_REPORT_FOLDER_ARRAY.add(jsonArray1.getJSONObject(q).getString("report_folder"));

                                }
                            }

                            if(epObj.has("trends_result")) {
                                JSONArray jsonArray1 = epObj.getJSONArray("trends_result");
                                for (int r = 0; r < jsonArray1.length(); r++) {
                                    PATIENT_TREND_ANALYSIS.add(new TrendAnalysisList(jsonArray1.getJSONObject(r).getInt("trend_id"),
                                            jsonArray1.getJSONObject(r).getString("date_added"),jsonArray1.getJSONObject(r).getString("systolic"),
                                            jsonArray1.getJSONObject(r).getString("diastolic"),jsonArray1.getJSONObject(r).getString("bp_beforefood_count"),
                                            jsonArray1.getJSONObject(r).getString("bp_afterfood_count"),jsonArray1.getJSONObject(r).getString("HbA1c"),
                                            jsonArray1.getJSONObject(r).getString("triglyceride"),jsonArray1.getJSONObject(r).getString("cholesterol"),
                                            jsonArray1.getJSONObject(r).getString("HDL"),jsonArray1.getJSONObject(r).getString("LDL"),
                                            jsonArray1.getJSONObject(r).getString("VLDL"),jsonArray1.getJSONObject(r).getInt("patient_id"),
                                            jsonArray1.getJSONObject(r).getString("patient_type"),0, ""));

                                }
                            }

                            if(epObj.has("trends_ophthal_result")) {
                                JSONArray jsonArray1 = epObj.getJSONArray("trends_ophthal_result");
                                for (int s = 0; s < jsonArray1.length(); s++) {
                                    PATIENT_OPHTHAL_TREND_ANALYSIS.add(new TrendOphthalAnalysisList(jsonArray1.getJSONObject(s).getInt("trend_id"),
                                            jsonArray1.getJSONObject(s).getString("date_added"),jsonArray1.getJSONObject(s).getString("DvSphereRE"),
                                            jsonArray1.getJSONObject(s).getString("DvCylRE"),jsonArray1.getJSONObject(s).getString("DvAxisRE"),
                                            jsonArray1.getJSONObject(s).getString("DvSpeherLE"),jsonArray1.getJSONObject(s).getString("DvCylLE"),
                                            jsonArray1.getJSONObject(s).getString("DvAxisLE"),jsonArray1.getJSONObject(s).getString("NvSpeherRE"),
                                            jsonArray1.getJSONObject(s).getString("NvCylRE"),jsonArray1.getJSONObject(s).getString("NvAxisRE"),
                                            jsonArray1.getJSONObject(s).getString("NvSpeherLE"),jsonArray1.getJSONObject(s).getString("NvCylLE"),
                                            jsonArray1.getJSONObject(s).getString("NvAxisLE"),jsonArray1.getJSONObject(s).getString("IpdRE"),
                                            jsonArray1.getJSONObject(s).getString("IpdLE"),jsonArray1.getJSONObject(s).getInt("patient_id"),
                                            jsonArray1.getJSONObject(s).getString("patient_type"),0, ""));

                                }
                            }

                            String refractionRE_value1 = "", refractionRE_value2 = "", refractionLE_value1= "", refractionLE_value2 = "";
                            String distVisionRE = "", distVisionLE = "", nearVisionRE = "", nearVisionLE = "";
                            String DvSphereRE = "", DvCylRE="",DvAxisRE="", DvSpeherLE = "", DvCylLE ="", DvAxisLE = "", NvSpeherRE = "";
                            String NvCylRE = "", NvAxisRE = "", NvSpeherLE = "", NvCylLE = "", NvAxisLE = "", IpdRE = "", IpdLE = "";

                            if (epObj.has("refractionRE_value1")) {
                                refractionRE_value1 = epObj.getString("refractionRE_value1");
                            }

                            if (epObj.has("refractionRE_value2")) {
                                refractionRE_value2 = epObj.getString("refractionRE_value2");
                            }

                            if (epObj.has("refractionLE_value1")) {
                                refractionLE_value1 = epObj.getString("refractionLE_value1");
                            }

                            if (epObj.has("refractionLE_value2")) {
                                refractionLE_value2 = epObj.getString("refractionLE_value2");
                            }

                            if (epObj.has("distVisionRE")) {
                                distVisionRE = epObj.getString("distVisionRE");
                            }

                            if (epObj.has("distVisionLE")) {
                                distVisionLE = epObj.getString("distVisionLE");
                            }

                            if (epObj.has("nearVisionRE")) {
                                nearVisionRE = epObj.getString("nearVisionRE");
                            }

                            if (epObj.has("nearVisionLE")) {
                                nearVisionLE = epObj.getString("nearVisionLE");
                            }

                            if (epObj.has("DvSphereRE")) {
                                DvSphereRE = epObj.getString("DvSphereRE");
                            }

                            if (epObj.has("DvCylRE")) {
                                DvCylRE = epObj.getString("DvCylRE");
                            }

                            if (epObj.has("DvAxisRE")) {
                                DvAxisRE = epObj.getString("DvAxisRE");
                            }

                            if (epObj.has("DvSpeherLE")) {
                                DvSpeherLE = epObj.getString("DvSpeherLE");
                            }

                            if (epObj.has("DvCylLE")) {
                                DvCylLE = epObj.getString("DvCylLE");
                            }

                            if (epObj.has("DvAxisLE")) {
                                DvAxisLE = epObj.getString("DvAxisLE");
                            }

                            if (epObj.has("NvSpeherRE")) {
                                NvSpeherRE = epObj.getString("NvSpeherRE");
                            }

                            if (epObj.has("NvCylRE")) {
                                NvCylRE = epObj.getString("NvCylRE");
                            }

                            if (epObj.has("NvAxisRE")) {
                                NvAxisRE = epObj.getString("NvAxisRE");
                            }

                            if (epObj.has("NvSpeherLE")) {
                                NvSpeherLE = epObj.getString("NvSpeherLE");
                            }

                            if (epObj.has("NvCylLE")) {
                                NvCylLE = epObj.getString("NvCylLE");
                            }

                            if (epObj.has("NvAxisLE")) {
                                NvAxisLE = epObj.getString("NvAxisLE");
                            }

                            if (epObj.has("IpdRE")) {
                                IpdRE = epObj.getString("IpdRE");
                            }

                            if (epObj.has("IpdLE")) {
                                IpdLE = epObj.getString("IpdLE");
                            }

                            EPISODE_LIST.add(new EpisodesList(epObj.getInt("episode_id"),epObj.getInt("emr_type"),epObj.getInt("ref_id"),epObj.getString("ref_name"),
                                    epObj.getInt("patient_id"),epObj.getString("patient_name"),epObj.getString("next_followup_date"),epObj.getString("diagnosis_details"),
                                    epObj.getString("treatment_details"),epObj.getString("prescription_note"),epObj.getString("date_time"),epObj.getString("consultation_fees"),
                                    PATIENT_CHIEF_MEDCOMPLAINT_ARRAY, PATIENT_INVESTIGATION_GENERAL_ARRAY, PATIENT_INVESTIGATION_OPHTHAL_ARRAY,
                                    PATIENT_DAIGNOSIS_ARRAY,PATIENT_TREATMENT_ARRAY,PATIENT_PRESCRIPTION_ARRAY, PATIENT_LIDS_ARRAY, PATIENT_CONJUCTIVA_ARRAY,
                                    PATIENT_SCLERA_ARRAY, PATIENT_CORNEA_ANTERIOR_ARRAY, PATIENT_CORNEA_POSTERIOR_ARRAY, PATIENT_ANTERIOR_CHAMBER_ARRAY,
                                    PATIENT_IRIS_ARRAY, PATIENT_PUPIL_ARRAY, PATIENT_ANGLE_ARRAY, PATIENT_LENS_ARRAY, PATIENT_VITEROUS_ARRAY, PATIENT_FUNDUS_ARRAY,PATIENT_EXAMINATION_ARRAY,
                                    PATIENT_DRUG_ALLERTY_ARRAY, PATIENT_DRUG_ABUSE_ARRAY, PATIENT_FAMILY_HISTORY_ARRAY,PATIENT_REPORT_ATTACHMENT_ARRAY,
                                    PATIENT_TREND_ANALYSIS, PATIENT_OPHTHAL_TREND_ANALYSIS,
                                    refractionRE_value1,refractionRE_value2,refractionLE_value1, refractionLE_value2,distVisionRE,distVisionLE,
                                    nearVisionRE,nearVisionLE,DvSphereRE, DvCylRE,DvAxisRE,DvSpeherLE,DvCylLE,DvAxisLE,NvSpeherRE,
                                    NvCylRE,NvAxisRE,NvSpeherLE, NvCylLE,NvAxisLE,IpdRE,IpdLE));

                            EPISODE_LIST_TEMP.add(new EpisodesList(epObj.getInt("episode_id"),epObj.getInt("emr_type"),epObj.getInt("ref_id"),epObj.getString("ref_name"),
                                    epObj.getInt("patient_id"),epObj.getString("patient_name"),epObj.getString("next_followup_date"),epObj.getString("diagnosis_details"),
                                    epObj.getString("treatment_details"),epObj.getString("prescription_note"),epObj.getString("date_time"),epObj.getString("consultation_fees"),
                                    PATIENT_CHIEF_MEDCOMPLAINT_ARRAY, PATIENT_INVESTIGATION_GENERAL_ARRAY, PATIENT_INVESTIGATION_OPHTHAL_ARRAY,
                                    PATIENT_DAIGNOSIS_ARRAY,PATIENT_TREATMENT_ARRAY,PATIENT_PRESCRIPTION_ARRAY, PATIENT_LIDS_ARRAY, PATIENT_CONJUCTIVA_ARRAY,
                                    PATIENT_SCLERA_ARRAY, PATIENT_CORNEA_ANTERIOR_ARRAY, PATIENT_CORNEA_POSTERIOR_ARRAY, PATIENT_ANTERIOR_CHAMBER_ARRAY,
                                    PATIENT_IRIS_ARRAY, PATIENT_PUPIL_ARRAY, PATIENT_ANGLE_ARRAY, PATIENT_LENS_ARRAY, PATIENT_VITEROUS_ARRAY, PATIENT_FUNDUS_ARRAY, PATIENT_EXAMINATION_ARRAY,
                                    PATIENT_DRUG_ALLERTY_ARRAY, PATIENT_DRUG_ABUSE_ARRAY, PATIENT_FAMILY_HISTORY_ARRAY,PATIENT_REPORT_ATTACHMENT_ARRAY,
                                    PATIENT_TREND_ANALYSIS, PATIENT_OPHTHAL_TREND_ANALYSIS,
                                    refractionRE_value1,refractionRE_value2,refractionLE_value1, refractionLE_value2,distVisionRE,distVisionLE,
                                    nearVisionRE,nearVisionLE,DvSphereRE, DvCylRE,DvAxisRE,DvSpeherLE,DvCylLE,DvAxisLE,NvSpeherRE,
                                    NvCylRE,NvAxisRE,NvSpeherLE, NvCylLE,NvAxisLE,IpdRE,IpdLE));

                        }

                        if(EPISODE_LIST.size() > 0) {

                            if(CONSULTATION_FILTER_TYPE.equals("1")) {
                                Gson  gson = new Gson();
                                String jsonText = gson.toJson(EPISODE_LIST);
                                if (sharedPreferences != null) {
                                    shareadPreferenceClass.clearConsultationsLists();
                                    shareadPreferenceClass.setConsultationsList(jsonText);
                                }
                            }

                        }

                        Log.d(AppUtils.TAG, " EPISODE_LIST size: "+ EPISODE_LIST.size());
                        trackAdapter = new ConsultaionListAdapter(ConsultationListActivity.this, EPISODE_LIST, LOGIN_MEMBER_NAME);
                        tracking_recyclerview.setAdapter(trackAdapter);
                        trackAdapter.notifyDataSetChanged();
                    }
                    else {
                        tracking_recyclerview.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);

                        if(CONSULTATION_FILTER_TYPE.equals("1")) {
                            Gson  gson = new Gson();
                            String jsonText = gson.toJson(EPISODE_LIST);
                            if (sharedPreferences != null) {
                                shareadPreferenceClass.clearConsultationsLists();
                                shareadPreferenceClass.setConsultationsList(jsonText);
                            }
                        }

                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_consultation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item_consult_swich_user:
                // Toast.makeText(getApplicationContext(),"Switch Member",Toast.LENGTH_LONG).show();
                if(sharedPreferences != null) {
                    LOGIN_MEMBER_ID = sharedPreferences.getInt(MHConstants.PREF_LOGIN_MEMBER_ID,0);
                    LOGIN_MEMBER_NAME = sharedPreferences.getString(MHConstants.PREF_LOGIN_MEMBER_NEME,"");
                }
                showSwitchMemberDialog();
                return true;
            case android.R.id.home:
                finish();
                return true;
            case R.id.item_consult_refresh:
                CONSULTATION_FILTER_TYPE = "1";
                if (NetworkUtil.getConnectivityStatusString(ConsultationListActivity.this).equalsIgnoreCase("enabled")) {
                    getConsultaionsListFromServer();
                } else {
                    AppUtils.showCustomAlertMessage(ConsultationListActivity.this, MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSwitchMemberDialog() {
        Gson gson = new Gson();
        if (!FAMILY_MEMBER_LIST.equals("")) {
            memberArraylist = new ArrayList<>();
            memberArraylist = gson.fromJson(FAMILY_MEMBER_LIST, new TypeToken<List<FamilyMember>>() {
            }.getType());
            if(memberArraylist.size() > 0 ) {
                Log.d(AppUtils.TAG, "memberArraylist size: " + memberArraylist.size());
                openSwitchMemberDialog(memberArraylist);
            }
        }
    }

    private void openSwitchMemberDialog(ArrayList<FamilyMember> memberArraylist) {
        final Dialog dialog = new Dialog(ConsultationListActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_login_member_switch);
        List<String> stringList=new ArrayList<>();  // here is list
        List<Integer> strinIDList=new ArrayList<>();
        for(int i=0;i<memberArraylist.size();i++) {
            stringList.add(memberArraylist.get(i).getMemberName());
            strinIDList.add(memberArraylist.get(i).getMemberid());
        }
        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        for(int i=0;i<stringList.size();i++){
            RadioButton rb = new RadioButton(ConsultationListActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
            rb.setPadding(10, 40, 10, 40);
            rb.setTag(strinIDList.get(i));
            rb.setTextSize(18);
            rg.addView(rb);

            if(LOGIN_MEMBER_ID == strinIDList.get(i)  && !CONSULTATION_FILTER_TYPE.equals("1")) {
                Log.d(AppUtils.TAG, " strinID: "+strinIDList.get(i));
                rb.setChecked(true);
                LOGIN_MEMBER_NAME = stringList.get(i);
            }
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                int mySelectedIndex = (int) radioButton.getTag();
                LOGIN_MEMBER_NAME = radioButton.getText().toString();
                // Toast.makeText(getApplicationContext()," id: "+ mySelectedIndex +" name: "+radioButton.getText(),Toast.LENGTH_LONG).show();

                if(sharedPreferences != null) {
                    shareadPreferenceClass.clearLoginMemberID();
                    shareadPreferenceClass.setLoginMemberID(mySelectedIndex);
                    shareadPreferenceClass.clearLoginMemberName();
                    shareadPreferenceClass.setLoginMemberName(LOGIN_MEMBER_NAME);
                }

                if(sharedPreferences != null) {
                    LOGIN_MEMBER_ID = sharedPreferences.getInt(MHConstants.PREF_LOGIN_MEMBER_ID,0);
                    LOGIN_MEMBER_NAME = sharedPreferences.getString(MHConstants.PREF_LOGIN_MEMBER_NEME,"");
                    member_name_text.setText("Patient Name: "+LOGIN_MEMBER_NAME);
                }

                CONSULTATION_FILTER_TYPE = "2";           // 2- Particular Member
                getConsultaionsListFromServer();
                dialog.dismiss();

            }
        });

        dialog.show();
    }
}
