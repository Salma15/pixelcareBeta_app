package com.fdc.pixelcare.Activities.Consultations;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.Adapters.Consultations.ChiefMedicalViewAdapters;
import com.fdc.pixelcare.Adapters.Consultations.DiagnosisViewAdapters;
import com.fdc.pixelcare.Adapters.Consultations.ExaminationsAdapters;
import com.fdc.pixelcare.Adapters.Consultations.InvestigationAdapters;
import com.fdc.pixelcare.Adapters.Consultations.InvestigationOphthalAdapters;
import com.fdc.pixelcare.Adapters.Consultations.PrescriptionViewAdapters;
import com.fdc.pixelcare.Adapters.Consultations.TreatmentsViewAdapters;
import com.fdc.pixelcare.DataModel.ChiefMedicalComplaint;
import com.fdc.pixelcare.DataModel.Diagnosis;
import com.fdc.pixelcare.DataModel.DiagnosticCentreList;
import com.fdc.pixelcare.DataModel.FrequentPrescription;
import com.fdc.pixelcare.DataModel.Investigations;
import com.fdc.pixelcare.DataModel.Lids;
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
import com.fdc.pixelcare.DataModel.PharmaCentreList;
import com.fdc.pixelcare.DataModel.Treatments;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
import com.fdc.pixelcare.Views.CustomEditText;
import com.fdc.pixelcare.Views.CustomTextView;
import com.fdc.pixelcare.Views.CustomTextViewSemiBold;
import com.google.gson.Gson;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SALMA on 01-01-2019.
 */
public class ViewOphthalVisitDetailsActivity extends AppCompatActivity  implements View.OnClickListener   {

    int EPISODE_ID, PATIENT_ID;
    String FOLLOWUP_DATE, DIAGNOSIS_DETAILS, TREATMENT_DETAILS, EPISODE_CREATED_DATE, CONSULTATION_FEES, PRESCRIPTION_NOTES;
    List<ChiefMedicalComplaint> PATIENT_CHIEF_MEDCOMPLAINT_ARRAY;
    List<Investigations> PATIENT_INVESTIGATION_GENERAL_ARRAY, PATIENT_INVESTIGATION_OPHTHAL_ARRAY,PATIENT_INVESTIGATION_ARRAY;
    List<Diagnosis> PATIENT_DAIGNOSIS_ARRAY;
    List<Treatments> PATIENT_TREATMENT_ARRAY;
    List<FrequentPrescription> PATIENT_PRESCRIPTION_ARRAY;
    /*List<Lids> PATIENT_LIDS_ARRAY;
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
    List<OphthalFundus> PATIENT_FUNDUS_ARRAY; */

    String GET_DV_SPHERE_RIGHT, GET_DV_CYCLE_RIGHT, GET_DV_AXIS_RIGHT;
    String GET_NV_SPHERE_RIGHT, GET_NV_CYCLE_RIGHT, GET_NV_AXIS_RIGHT;
    String GET_IPD_RIGHT;
    String GET_DV_SPHERE_LEFT, GET_DV_CYCLE_LEFT, GET_DV_AXIS_LEFT;
    String GET_NV_SPHERE_LEFT, GET_NV_CYCLE_LEFT, GET_NV_AXIS_LEFT;
    String GET_IPD_LEFT;
    String DISTANCE_VISION_RE, DISTANCE_VISION_LE, NEAR_VISION_RE, NEAR_VISION_LE;
    String REFRACTION_RE_VALUE1, REFRACTION_RE_VALUE2, REFRACTION_LE_VALUE1, REFRACTION_LE_VALUE2;

    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int USER_ID, USER_LOGINTYPE;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION;
    String MEMBER_NAME, MEMBER_GENDER, MEMBER_AGE,MEMBER_RELATIONSHIP,MEMBER_DOB, MEMBER_PHOTO;

    LinearLayout chief_medcomp_btn, chief_medcomp_contents;
    boolean view_chief_medcomp_status = false;
    ImageView chief_medcomp_image;
    RecyclerView chiefComplaintRecyclerview;
    ChiefMedicalViewAdapters medcomplaintAdapter;

    // Investigation Tests
    LinearLayout investigation_btn, investigation_contents;
    boolean view_investigation_status = false;
    ImageView investigation_image;
    private InvestigationAdapters investigationAdapter;
    RecyclerView investigationRecyclerview;
    LinearLayout investigation_gen_tab_header,investigation_ophthal_tab_header;
    RecyclerView investigationOphthalRecyclerview;
    private InvestigationOphthalAdapters investigationOphthalAdapter;

    // Examinations
    LinearLayout examination_btn, examination_contents;
    boolean view_examination_status = false;
    ImageView examination_image;
    private ExaminationsAdapters examinationAdapter;
    RecyclerView examinationRecyclerview;

    // Diagnosis
    LinearLayout diagnosis_btn, diagnosis_contents;
    boolean view_diagnosis_status = false;
    ImageView diagnosis_image;
    private DiagnosisViewAdapters diagnosisAdapter;
    RecyclerView diagnosisRecyclerview;

    // Treatments Section
    LinearLayout treatment_btn, treatment_contents;
    boolean view_treatment_status = false;
    ImageView treatment_image;
    private TreatmentsViewAdapters treatmentAdapter;
    RecyclerView treatmentRecyclerview;

    // Prescriptions Section
    LinearLayout prescription_btn, prescription_contents;
    boolean view_prescription_status = false;
    ImageView prescription_image;
    LinearLayout presc_tab_header;
    private PrescriptionViewAdapters prescriptionAdapter;
    RecyclerView prescriptionRecyclerview;
    CustomTextView _edt_prescription_note;

    CustomTextView consultation_fees, next_followup_date, diagnosis_details, treatment_details;
    CustomTextViewSemiBold update_investigation_btn, refer_diagnostics_btn;

    List<DiagnosticCentreList> diagnosticsListArraylist = new ArrayList<>();
    List<DiagnosticCentreList> selectedDiagnoCentreArraylist = new ArrayList<>();
    Gson gson = new Gson();
    LinearLayout addNew_contents, list_contents;

    CustomTextViewSemiBold refer_pharma_btn, send_presc_sms_btn, send_presc_email_btn;
    List<PharmaCentreList> pharmaListArraylist = new ArrayList<>();
    List<PharmaCentreList> selectedPharmaArraylist = new ArrayList<>();

    Spinner distance_vision_re_spinner,  distance_vision_le_spinner, near_vision_re_spinner,  near_vision_le_spinner;
    List<Lids> lidsListArraylist, PATIENT_LIDS_ARRAY;
    LinearLayout lids_btn, lids_contents;
    boolean view_lids_status = false;
    ImageView add_lids, ophthal_lids_image;
    AutoCompleteTextView _edt_add_lids;
    LinearLayout lids_gridview_RE, lids_gridview_LE;

    LinearLayout conjuctiva_btn, conjuctiva_contents;
    boolean view_conjuctiva_status = false;
    LinearLayout conjuctiva_gridview_RE, conjuctiva_gridview_LE;
    ImageView add_conjuctiva, ophthal_conjuctiva_image;
    AutoCompleteTextView _edt_add_conjuctiva;
    List<OphthalConjuctiva> conjuctivaListArraylist, PATIENT_CONJUCTIVA_ARRAY;

    LinearLayout sclera_btn, sclera_contents;
    boolean view_sclera_status = false;
    LinearLayout sclera_gridview_RE, sclera_gridview_LE;
    ImageView add_sclera, ophthal_sclera_image;
    AutoCompleteTextView _edt_add_sclera;
    List<OphthalSclera> scleraListArraylist, PATIENT_SCLERA_ARRAY;

    LinearLayout cornea_anterior_btn, cornea_anterior_contents;
    boolean view_cornea_anterior_status = false;
    LinearLayout cornea_anterior_gridview_RE, cornea_anterior_gridview_LE;
    ImageView add_cornea_anterior, ophthal_cornea_anterior_image;
    AutoCompleteTextView _edt_add_cornea_anterior;
    List<OphthalCornearAnteriorSurface> corneaAnteriorListArraylist, PATIENT_CORNEA_ANTERIOR_ARRAY;

    LinearLayout cornea_posterior_btn, cornea_posterior_contents;
    boolean view_cornea_posterior_status = false;
    LinearLayout cornea_posterior_gridview_RE, cornea_posterior_gridview_LE;
    ImageView add_cornea_posterior, ophthal_cornea_posterior_image;
    AutoCompleteTextView _edt_add_cornea_posterior;
    List<OphthalCornearPosteriorSurface> corneaPosteriorListArraylist, PATIENT_CORNEA_POSTERIOR_ARRAY;

    LinearLayout anterior_chamber_btn, anterior_chamber_contents;
    boolean view_anterior_chamber_status = false;
    LinearLayout anterior_chamber_gridview_RE, anterior_chamber_gridview_LE;
    ImageView add_anterior_chamber, ophthal_anterior_chamber_image;
    AutoCompleteTextView _edt_add_anterior_chamber;
    List<OphthalAnteriorChamber> anteriorChamberListArraylist, PATIENT_ANTERIOR_CHAMBER_ARRAY;

    LinearLayout iris_btn, iris_contents;
    boolean view_iris_status = false;
    LinearLayout iris_gridview_RE, iris_gridview_LE;
    ImageView add_iris, ophthal_iris_image;
    AutoCompleteTextView _edt_add_iris;
    List<OphthalIris> irisListArraylist, PATIENT_IRIS_ARRAY;

    LinearLayout pupil_btn, pupil_contents;
    boolean view_pupil_status = false;
    LinearLayout pupil_gridview_RE, pupil_gridview_LE;
    ImageView add_pupil, ophthal_pupil_image;
    AutoCompleteTextView _edt_add_pupil;
    List<OphthalPupil> pupilListArraylist, PATIENT_PUPIL_ARRAY;

    LinearLayout angle_btn, angle_contents;
    boolean view_angle_status = false;
    LinearLayout angle_gridview_RE, angle_gridview_LE;
    ImageView add_angle, ophthal_angle_image;
    AutoCompleteTextView _edt_add_angle;
    List<OphthalAngleAnteriorChamber> angleListArraylist, PATIENT_ANGLE_ARRAY;

    LinearLayout lens_btn, lens_contents;
    boolean view_lens_status = false;
    LinearLayout lens_gridview_RE, lens_gridview_LE;
    ImageView add_lens, ophthal_lens_image;
    AutoCompleteTextView _edt_add_lens;
    List<OphthalLens> lensListArraylist, PATIENT_LENS_ARRAY;

    LinearLayout viterous_btn, viterous_contents;
    boolean view_viterous_status = false;
    LinearLayout viterous_gridview_RE, viterous_gridview_LE;
    ImageView add_viterous, ophthal_viterous_image;
    AutoCompleteTextView _edt_add_viterous;
    List<OphthalViterous> viterousListArraylist, PATIENT_VITEROUS_ARRAY;

    LinearLayout fundus_btn,fundus_contents;
    boolean view_fundus_status = false;
    LinearLayout fundus_gridview_RE, fundus_gridview_LE;
    ImageView add_fundus, ophthal_fundus_image;
    AutoCompleteTextView _edt_add_fundus;
    List<OphthalFundus> fundusListArraylist, PATIENT_FUNDUS_ARRAY;

    LinearLayout refraction_btn,refraction_contents;
    boolean view_refraction_status = false;
    ImageView  ophthal_refraction_image;

    CustomEditText _edt_refraction_RE_top, _edt_refraction_RE_bottom, _edt_refraction_LE_top, _edt_refraction_LE_bottom;
    LinearLayout spectacle_prescription_btn, spectacle_prescription_contents;
    boolean view_spectacle_prescription_status = false;
    private CustomTextView _edt_right_dv_sphere, _edt_right_dv_cyl, _edt_right_dv_axis;
    private CustomTextView _edt_right_nv_sphere, _edt_right_nv_cyl, _edt_right_nv_axis;
    private CustomTextView _edt_right_ipd;

    private CustomTextView _edt_left_dv_sphere, _edt_left_dv_cyl, _edt_left_dv_axis;
    private CustomTextView _edt_left_nv_sphere, _edt_left_nv_cyl, _edt_left_nv_axis;
    private CustomTextView _edt_left_ipd;

    LinearLayout conaultationFeesLayout, nextFollowupLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ophthal_visit_details);

        PATIENT_CHIEF_MEDCOMPLAINT_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_GENERAL_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_OPHTHAL_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_ARRAY = new ArrayList<>();
        PATIENT_DAIGNOSIS_ARRAY = new ArrayList<>();
        PATIENT_TREATMENT_ARRAY = new ArrayList<>();
        PATIENT_PRESCRIPTION_ARRAY = new ArrayList<>();
        diagnosticsListArraylist = new ArrayList<>();
        selectedDiagnoCentreArraylist = new ArrayList<>();
        pharmaListArraylist = new ArrayList<>();
        selectedPharmaArraylist = new ArrayList<>();
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);
            Log.d(AppUtils.TAG, " ************ ViewOphthalVisitDetailsActivity *********** ");

            EPISODE_ID = bundle.getInt("EPISODE_ID");
            PATIENT_ID = bundle.getInt("PATIENT_ID");
            FOLLOWUP_DATE = bundle.getString("FOLLOWUP_DATE");
            DIAGNOSIS_DETAILS = bundle.getString("DIAGNOSIS_DETAILS");
            TREATMENT_DETAILS = bundle.getString("TREATMENT_DETAILS");
            PRESCRIPTION_NOTES = bundle.getString("PRESCRIPTION_NOTES");
            EPISODE_CREATED_DATE = bundle.getString("EPISODE_CREATED_DATE");
            CONSULTATION_FEES = bundle.getString("CONSULTATION_FEES");

            PATIENT_CHIEF_MEDCOMPLAINT_ARRAY =  (List<ChiefMedicalComplaint>) bundle.getSerializable("CHIEF_MEDICAL_LIST");
            PATIENT_INVESTIGATION_GENERAL_ARRAY =  (List<Investigations>) bundle.getSerializable("INVESTIGATION_GENERAL_LIST");
            PATIENT_INVESTIGATION_OPHTHAL_ARRAY =  (List<Investigations>) bundle.getSerializable("INVESTIGATION_OPHTHAL_LIST");
            PATIENT_DAIGNOSIS_ARRAY =  (List<Diagnosis>) bundle.getSerializable("DIAGNOSIS_LIST");
            PATIENT_TREATMENT_ARRAY =  (List<Treatments>) bundle.getSerializable("TREATMENT_LIST");
            PATIENT_PRESCRIPTION_ARRAY =  (List<FrequentPrescription>) bundle.getSerializable("PRESCRIPTION_LIST");
            PATIENT_LIDS_ARRAY =  (List<Lids>) bundle.getSerializable("LIDS_LIST");
            PATIENT_CONJUCTIVA_ARRAY =  (List<OphthalConjuctiva>) bundle.getSerializable("CONJUCTIVA_LIST");
            PATIENT_SCLERA_ARRAY =  (List<OphthalSclera>) bundle.getSerializable("SCLERA_LIST");
            PATIENT_CORNEA_ANTERIOR_ARRAY =  (List<OphthalCornearAnteriorSurface>) bundle.getSerializable("CORNEA_ANTERIOR_LIST");
            PATIENT_CORNEA_POSTERIOR_ARRAY =  (List<OphthalCornearPosteriorSurface>) bundle.getSerializable("CORNEA_POSTERIOR_LIST");
            PATIENT_ANTERIOR_CHAMBER_ARRAY =  (List<OphthalAnteriorChamber>) bundle.getSerializable("ANTERIOR_CHAMBER_LIST");
            PATIENT_IRIS_ARRAY =  (List<OphthalIris>) bundle.getSerializable("IRIS_LIST");
            PATIENT_PUPIL_ARRAY =  (List<OphthalPupil>) bundle.getSerializable("PUPIL_LIST");
            PATIENT_ANGLE_ARRAY =  (List<OphthalAngleAnteriorChamber>) bundle.getSerializable("ANGLE_LIST");
            PATIENT_LENS_ARRAY =  (List<OphthalLens>) bundle.getSerializable("LENS_LIST");
            PATIENT_VITEROUS_ARRAY =  (List<OphthalViterous>) bundle.getSerializable("VITERIOUS_LIST");
            PATIENT_FUNDUS_ARRAY =  (List<OphthalFundus>) bundle.getSerializable("FUNDUS_LIST");

            REFRACTION_RE_VALUE1 = bundle.getString("REFRACTION_RE_VALUE1");
            REFRACTION_RE_VALUE2 = bundle.getString("REFRACTION_RE_VALUE2");
            REFRACTION_LE_VALUE1 = bundle.getString("REFRACTION_LE_VALUE1");
            REFRACTION_LE_VALUE2 = bundle.getString("REFRACTION_LE_VALUE2");
            DISTANCE_VISION_RE = bundle.getString("DISTANCE_VISION_RE");
            DISTANCE_VISION_LE = bundle.getString("DISTANCE_VISION_LE");
            NEAR_VISION_RE = bundle.getString("NEAR_VISION_RE");
            NEAR_VISION_LE = bundle.getString("NEAR_VISION_LE");
            GET_DV_SPHERE_RIGHT = bundle.getString("DV_SPHERE_RIGHT");
            GET_DV_CYCLE_RIGHT = bundle.getString("DV_CYCLE_RIGHT");
            GET_DV_AXIS_RIGHT = bundle.getString("DV_AXIS_RIGHT");
            GET_NV_SPHERE_RIGHT = bundle.getString("NV_SPHERE_RIGHT");
            GET_NV_CYCLE_RIGHT = bundle.getString("NV_CYCLE_RIGHT");
            GET_NV_AXIS_RIGHT = bundle.getString("DV_AXIS_RIGHT");
            GET_IPD_RIGHT = bundle.getString("IPD_RIGHT");
            GET_DV_SPHERE_LEFT = bundle.getString("DV_SPHERE_LEFT");
            GET_DV_CYCLE_LEFT = bundle.getString("DV_CYCLE_LEFT");
            GET_DV_AXIS_LEFT = bundle.getString("DV_AXIS_LEFT");
            GET_NV_SPHERE_LEFT = bundle.getString("NV_SPHERE_LEFT");
            GET_NV_CYCLE_LEFT = bundle.getString("NV_CYCLE_LEFT");
            GET_NV_AXIS_LEFT = bundle.getString("NV_AXIS_LEFT");
            GET_IPD_LEFT = bundle.getString("IPD_LEFT");


            Log.d(AppUtils.TAG, " EPISODE_ID: "+EPISODE_ID);

            if (PATIENT_CHIEF_MEDCOMPLAINT_ARRAY != null && PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.size() > 0) {
                Log.d(AppUtils.TAG + " MEDCOMPL:", String.valueOf(PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.size()));
            }

            if (PATIENT_INVESTIGATION_GENERAL_ARRAY != null && PATIENT_INVESTIGATION_GENERAL_ARRAY.size() > 0) {
                Log.d(AppUtils.TAG + " INVEST_GEN:", String.valueOf(PATIENT_INVESTIGATION_GENERAL_ARRAY.size()));
            }

            if (PATIENT_INVESTIGATION_OPHTHAL_ARRAY != null && PATIENT_INVESTIGATION_OPHTHAL_ARRAY.size() > 0) {
                Log.d(AppUtils.TAG + " INVEST_OPHT:", String.valueOf(PATIENT_INVESTIGATION_OPHTHAL_ARRAY.size()));
            }

            if (PATIENT_DAIGNOSIS_ARRAY != null && PATIENT_DAIGNOSIS_ARRAY.size() > 0) {
                Log.d(AppUtils.TAG + " DAIGN:", String.valueOf(PATIENT_DAIGNOSIS_ARRAY.size()));
            }

            if (PATIENT_TREATMENT_ARRAY != null && PATIENT_TREATMENT_ARRAY.size() > 0) {
                Log.d(AppUtils.TAG + " TREAT:", String.valueOf(PATIENT_TREATMENT_ARRAY.size()));
            }

            if (PATIENT_PRESCRIPTION_ARRAY != null && PATIENT_PRESCRIPTION_ARRAY.size() > 0) {
                Log.d(AppUtils.TAG + " PRESC:", String.valueOf(PATIENT_PRESCRIPTION_ARRAY.size()));
            }

        }

        shareadPreferenceClass = new ShareadPreferenceClass(ViewOphthalVisitDetailsActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(ViewOphthalVisitDetailsActivity.this);

        if(sharedPreferences !=null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE  = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);

            Log.d(AppUtils.TAG , " *********** MedicalCasesNewActivity **************** ");
            Log.d(AppUtils.TAG + " UserID: ", String.valueOf(USER_ID));
            Log.d(AppUtils.TAG + " name: ", USER_NAME);
            Log.d(AppUtils.TAG + " email: ", USER_EMAIL);
            Log.d(AppUtils.TAG + " MOBILE: ", USER_MOBILE);
            Log.d(AppUtils.TAG + " Location: ", USER_LOCATION);
        }

        initializationViews();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void initializationViews() {
        gson = new Gson();
        view_chief_medcomp_status = false;
        view_investigation_status = false;
        view_examination_status = false;
        view_diagnosis_status = false;
        view_treatment_status = false;
        view_prescription_status = false;

        chief_medcomp_btn = (LinearLayout) findViewById(R.id.view_chief_medcomp_btn);
      //  chief_medcomp_btn.setOnClickListener(this);
        chief_medcomp_contents = (LinearLayout) findViewById(R.id.view_chief_medcomp_contents);
        chief_medcomp_image = (ImageView) findViewById(R.id.view_chief_medcomp_image);

        chief_medcomp_contents.setVisibility(View.VISIBLE);
        view_chief_medcomp_status = true;

        chiefComplaintRecyclerview = (RecyclerView) findViewById(R.id.view_chiefcomp_recycler);
        medcomplaintAdapter = new ChiefMedicalViewAdapters(ViewOphthalVisitDetailsActivity.this, PATIENT_CHIEF_MEDCOMPLAINT_ARRAY);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(ViewOphthalVisitDetailsActivity.this);
        chiefComplaintRecyclerview.setLayoutManager(mLayoutManager1);
        chiefComplaintRecyclerview.setItemAnimator(new DefaultItemAnimator());
        chiefComplaintRecyclerview.setAdapter(medcomplaintAdapter);


        /* ***************** Investigations ************************************* */

        investigation_btn = (LinearLayout) findViewById(R.id.view_investigation_btn);
        investigation_btn.setOnClickListener(this);
        investigation_contents = (LinearLayout) findViewById(R.id.view_investigation_contents);
        investigation_image = (ImageView) findViewById(R.id.view_investigation_image);
        investigation_gen_tab_header = (LinearLayout) findViewById(R.id.view_invest_gen_header);
        investigation_ophthal_tab_header= (LinearLayout) findViewById(R.id.view_invest_ophthal_tab_header);
        investigation_gen_tab_header.setVisibility(View.GONE);
        investigation_ophthal_tab_header.setVisibility(View.GONE);

        investigation_contents.setVisibility(View.GONE);
      //  view_investigation_status = true;

        if (PATIENT_INVESTIGATION_GENERAL_ARRAY != null && PATIENT_INVESTIGATION_GENERAL_ARRAY.size() > 0) {
            Log.d(AppUtils.TAG + " INVEST_GEN:", String.valueOf(PATIENT_INVESTIGATION_GENERAL_ARRAY.size()));
            investigation_gen_tab_header.setVisibility(View.VISIBLE);

            for(int i=0;i<PATIENT_INVESTIGATION_GENERAL_ARRAY.size();i++) {
                Log.d(AppUtils.TAG + " ivestID:", String.valueOf(PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getInvestigationId()));
            }
        }

        if (PATIENT_INVESTIGATION_OPHTHAL_ARRAY != null && PATIENT_INVESTIGATION_OPHTHAL_ARRAY.size() > 0) {
            Log.d(AppUtils.TAG + " INVEST_OPHT:", String.valueOf(PATIENT_INVESTIGATION_OPHTHAL_ARRAY.size()));
            investigation_ophthal_tab_header.setVisibility(View.VISIBLE);

            for(int i=0;i<PATIENT_INVESTIGATION_OPHTHAL_ARRAY.size();i++) {
                Log.d(AppUtils.TAG + " ivestOphID:", String.valueOf(PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getInvestigationId()));
            }
        }

        investigationRecyclerview = (RecyclerView) findViewById(R.id.view_investigation_recycler);
        investigationAdapter = new InvestigationAdapters(ViewOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_GENERAL_ARRAY);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(ViewOphthalVisitDetailsActivity.this);
        investigationRecyclerview.setLayoutManager(mLayoutManager2);
        investigationRecyclerview.setItemAnimator(new DefaultItemAnimator());
        investigationRecyclerview.setAdapter(investigationAdapter);

        investigationOphthalRecyclerview = (RecyclerView) findViewById(R.id.view_visit_investophthal_recycler);
        investigationOphthalAdapter = new InvestigationOphthalAdapters(ViewOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_OPHTHAL_ARRAY);
        RecyclerView.LayoutManager mLayoutManager2a = new LinearLayoutManager(ViewOphthalVisitDetailsActivity.this);
        investigationOphthalRecyclerview.setLayoutManager(mLayoutManager2a);
        investigationOphthalRecyclerview.setItemAnimator(new DefaultItemAnimator());
        investigationOphthalRecyclerview.setAdapter(investigationOphthalAdapter);

        /* ********************* Diagnosis *********************** */

        diagnosis_btn = (LinearLayout) findViewById(R.id.view_diagnosis_btn);
        diagnosis_btn.setOnClickListener(this);
        diagnosis_contents = (LinearLayout) findViewById(R.id.view_diagnosis_contents);
        diagnosis_image = (ImageView) findViewById(R.id.view_diagnosis_image);

        diagnosis_contents.setVisibility(View.GONE);
      //  view_diagnosis_status = true;

        diagnosisRecyclerview = (RecyclerView) findViewById(R.id.view_diagnosis_recycler);
        diagnosisAdapter = new DiagnosisViewAdapters(ViewOphthalVisitDetailsActivity.this, PATIENT_DAIGNOSIS_ARRAY);
        RecyclerView.LayoutManager mLayoutManager4 = new LinearLayoutManager(ViewOphthalVisitDetailsActivity.this);
        diagnosisRecyclerview.setLayoutManager(mLayoutManager4);
        diagnosisRecyclerview.setItemAnimator(new DefaultItemAnimator());
        diagnosisRecyclerview.setAdapter(diagnosisAdapter);

        /* ***************** Treatments *************************** */
        treatment_btn = (LinearLayout) findViewById(R.id.view_treatment_btn);
        treatment_btn.setOnClickListener(this);
        treatment_contents = (LinearLayout) findViewById(R.id.view_treatments_contents);
        treatment_image = (ImageView) findViewById(R.id.view_treatments_image);

        treatment_contents.setVisibility(View.GONE);
       // view_treatment_status = true;

        treatmentRecyclerview = (RecyclerView) findViewById(R.id.view_treatments_recycler);
        treatmentAdapter = new TreatmentsViewAdapters(ViewOphthalVisitDetailsActivity.this, PATIENT_TREATMENT_ARRAY);
        RecyclerView.LayoutManager mLayoutManager5 = new LinearLayoutManager(ViewOphthalVisitDetailsActivity.this);
        treatmentRecyclerview.setLayoutManager(mLayoutManager5);
        treatmentRecyclerview.setItemAnimator(new DefaultItemAnimator());
        treatmentRecyclerview.setAdapter(treatmentAdapter);

        /* ******************* Prescription Section ********************* */
        prescription_btn = (LinearLayout) findViewById(R.id.view_prescription_btn);
       // prescription_btn.setOnClickListener(this);
        prescription_contents = (LinearLayout) findViewById(R.id.view_prescription_contents);
        prescription_image = (ImageView) findViewById(R.id.view_prescription_image);
        presc_tab_header = (LinearLayout) findViewById(R.id.view_prescription_tabheader);
        presc_tab_header.setVisibility(View.GONE);

        prescription_contents.setVisibility(View.VISIBLE);
        view_prescription_status = true;

        _edt_prescription_note = (CustomTextView) findViewById(R.id.view_visitpresc_note);
        _edt_prescription_note.setText(PRESCRIPTION_NOTES);

        if (PATIENT_PRESCRIPTION_ARRAY != null && PATIENT_PRESCRIPTION_ARRAY.size() > 0) {
            presc_tab_header.setVisibility(View.VISIBLE);
        }

        prescriptionRecyclerview = (RecyclerView) findViewById(R.id.view_prescription_recycler);
        prescriptionAdapter = new PrescriptionViewAdapters(ViewOphthalVisitDetailsActivity.this, PATIENT_PRESCRIPTION_ARRAY);
        RecyclerView.LayoutManager mLayoutManager6 = new LinearLayoutManager(ViewOphthalVisitDetailsActivity.this);
        prescriptionRecyclerview.setLayoutManager(mLayoutManager6);
        prescriptionRecyclerview.setItemAnimator(new DefaultItemAnimator());
        prescriptionRecyclerview.setAdapter(prescriptionAdapter);

        /* ************** Consultaion fees, Followup dates ************ */
        consultation_fees = (CustomTextView) findViewById(R.id.view_visit_consultationFee);
        next_followup_date = (CustomTextView) findViewById(R.id.view_visit_followupdate);
        diagnosis_details  = (CustomTextView) findViewById(R.id.view_diagnosis_details);
        treatment_details = (CustomTextView) findViewById(R.id.view_treatment_details);

        conaultationFeesLayout = (LinearLayout)  findViewById(R.id.consultaion_layout);
        nextFollowupLayout = (LinearLayout)  findViewById(R.id.nextfollowup_layout);

       // next_followup_date.setText(FOLLOWUP_DATE);
        diagnosis_details.setText(DIAGNOSIS_DETAILS);
        treatment_details.setText(TREATMENT_DETAILS);

        if((CONSULTATION_FEES.equals("null")) || (CONSULTATION_FEES.length() <=0)) {
            conaultationFeesLayout.setVisibility(View.GONE);
            consultation_fees.setText("");
        }
        else {
            conaultationFeesLayout.setVisibility(View.VISIBLE);
            consultation_fees.setText(CONSULTATION_FEES);
        }

        if((FOLLOWUP_DATE.equals("null")) || (FOLLOWUP_DATE.length() <=0)) {
            nextFollowupLayout.setVisibility(View.GONE);
            next_followup_date.setText("");
        }
        else {
            nextFollowupLayout.setVisibility(View.VISIBLE);
            next_followup_date.setText(FOLLOWUP_DATE);
        }

        update_investigation_btn = (CustomTextViewSemiBold)findViewById(R.id.view_visit_investupdate);
        update_investigation_btn.setOnClickListener(this);
        update_investigation_btn.setVisibility(View.GONE);
        refer_diagnostics_btn = (CustomTextViewSemiBold)findViewById(R.id.view_visit_invest_refer);
        refer_diagnostics_btn.setOnClickListener(this);

        refer_pharma_btn = (CustomTextViewSemiBold)findViewById(R.id.view_visit_presc_refer);
        refer_pharma_btn.setOnClickListener(this);
        send_presc_sms_btn = (CustomTextViewSemiBold)findViewById(R.id.view_visit_presc_sms);
        send_presc_sms_btn.setOnClickListener(this);
        send_presc_email_btn  = (CustomTextViewSemiBold)findViewById(R.id.view_visit_presc_email);
        send_presc_email_btn.setOnClickListener(this);

        /* ******************** Examinations ************************* */
        examination_btn = (LinearLayout) findViewById(R.id.view_examination_btn);
        examination_btn.setOnClickListener(this);
        examination_contents = (LinearLayout) findViewById(R.id.view_examination_contents);
        examination_contents.setVisibility(View.GONE);
        examination_image = (ImageView) findViewById(R.id.view_examinations_image);

        distance_vision_re_spinner = (Spinner) findViewById(R.id.ophthal_distance_vision_re_spinner);
        distance_vision_le_spinner = (Spinner) findViewById(R.id.ophthal_distance_vision_le_spinner);
        near_vision_re_spinner = (Spinner) findViewById(R.id.ophthal_near_vision_re_spinner);
        near_vision_le_spinner = (Spinner) findViewById(R.id.ophthal_near_vision_le_spinner);

        ArrayAdapter<String> distance_vision_RE_Array = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.distance_vision_results) );
        distance_vision_RE_Array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distance_vision_re_spinner.setAdapter(distance_vision_RE_Array);

        ArrayAdapter<String> distance_vision_LE_Array = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.distance_vision_results) );
        distance_vision_LE_Array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distance_vision_le_spinner.setAdapter(distance_vision_LE_Array);

        ArrayAdapter<String> near_vision_RE_Array = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.near_vision_results) );
        near_vision_RE_Array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        near_vision_re_spinner.setAdapter(near_vision_RE_Array);

        ArrayAdapter<String> near_vision_LE_Array = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.near_vision_results) );
        near_vision_LE_Array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        near_vision_le_spinner.setAdapter(near_vision_LE_Array);

        if(DISTANCE_VISION_RE.equals("") || DISTANCE_VISION_RE != null) {
            int spinnerPosition = distance_vision_RE_Array.getPosition(DISTANCE_VISION_RE);
            distance_vision_re_spinner.setSelection(spinnerPosition);
        }

        if(DISTANCE_VISION_LE.equals("") || DISTANCE_VISION_LE != null) {
            int spinnerPosition = distance_vision_LE_Array.getPosition(DISTANCE_VISION_LE);
            distance_vision_le_spinner.setSelection(spinnerPosition);
        }

        if(NEAR_VISION_RE.equals("") || NEAR_VISION_RE != null) {
            int spinnerPosition = near_vision_RE_Array.getPosition(NEAR_VISION_RE);
            near_vision_re_spinner.setSelection(spinnerPosition);
        }

        if(NEAR_VISION_LE.equals("") || NEAR_VISION_LE != null) {
            int spinnerPosition = near_vision_LE_Array.getPosition(NEAR_VISION_LE);
            near_vision_le_spinner.setSelection(spinnerPosition);
        }

        lids_gridview_RE = (LinearLayout) findViewById(R.id.lids_RE_grids);
        lids_gridview_LE = (LinearLayout) findViewById(R.id.lids_LE_grids);
        lids_btn = (LinearLayout) findViewById(R.id.ophthal_lids_btn);
        lids_btn.setOnClickListener(this);
        lids_contents = (LinearLayout) findViewById(R.id.ophthal_lids_contents);
        lids_contents.setVisibility(View.GONE);
        ophthal_lids_image = (ImageView) findViewById(R.id.ophthal_lids_image);
      //  _edt_add_lids = (AutoCompleteTextView) findViewById(R.id.lids_add_edit);

        if (PATIENT_LIDS_ARRAY != null && PATIENT_LIDS_ARRAY.size() > 0) {
            for(int i=0; i<PATIENT_LIDS_ARRAY.size(); i++) {
                Log.d(AppUtils.TAG, " type: "+ PATIENT_LIDS_ARRAY.get(i).getRightEye());
                if(PATIENT_LIDS_ARRAY.get(i).getRightEye().equals("1")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_LIDS_ARRAY.get(i).getLidsId());
                    chk.setText(PATIENT_LIDS_ARRAY.get(i).getLidsName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    lids_gridview_RE.addView(chk);
                }
                else if(PATIENT_LIDS_ARRAY.get(i).getRightEye().equals("2")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_LIDS_ARRAY.get(i).getLidsId());
                    chk.setText(PATIENT_LIDS_ARRAY.get(i).getLidsName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    lids_gridview_LE.addView(chk);
                }
            }
        }

        // ***************** CONJUCTIVA SECTION ************************* /
        conjuctiva_btn = (LinearLayout)findViewById(R.id.ophthal_conjuctiva_btn);
        conjuctiva_btn.setOnClickListener(this);
        conjuctiva_contents = (LinearLayout) findViewById(R.id.ophthal_conjuctiva_contents);
        conjuctiva_contents.setVisibility(View.GONE);
        ophthal_conjuctiva_image = (ImageView)  findViewById(R.id.ophthal_conjuctiva_image);
        conjuctiva_gridview_RE = (LinearLayout) findViewById(R.id.conjuctiva_RE_grids);
        conjuctiva_gridview_LE = (LinearLayout) findViewById(R.id.conjuctiva_LE_grids);

        if (PATIENT_CONJUCTIVA_ARRAY != null && PATIENT_CONJUCTIVA_ARRAY.size() > 0) {
            for(int i=0; i<PATIENT_CONJUCTIVA_ARRAY.size(); i++) {
                Log.d(AppUtils.TAG, " type: "+ PATIENT_CONJUCTIVA_ARRAY.get(i).getRightEye());
                if(PATIENT_CONJUCTIVA_ARRAY.get(i).getRightEye().equals("1")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_CONJUCTIVA_ARRAY.get(i).getConjuctivaId());
                    chk.setText(PATIENT_CONJUCTIVA_ARRAY.get(i).getConjuctivaName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    conjuctiva_gridview_RE.addView(chk);
                }
                else if(PATIENT_CONJUCTIVA_ARRAY.get(i).getRightEye().equals("2")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_CONJUCTIVA_ARRAY.get(i).getConjuctivaId());
                    chk.setText(PATIENT_CONJUCTIVA_ARRAY.get(i).getConjuctivaName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    conjuctiva_gridview_LE.addView(chk);
                }
            }
        }

        /* *************** SCLERA SECTION *************************** */
        sclera_btn = (LinearLayout) findViewById(R.id.ophthal_sclera_btn);
        sclera_btn.setOnClickListener(this);
        sclera_contents  = (LinearLayout) findViewById(R.id.ophthal_sclera_contents);
        sclera_contents.setVisibility(View.GONE);
        ophthal_sclera_image = (ImageView) findViewById(R.id.ophthal_sclera_image);
        sclera_gridview_RE = (LinearLayout) findViewById(R.id.sclera_RE_grids);
        sclera_gridview_LE = (LinearLayout) findViewById(R.id.sclera_LE_grids);

        if (PATIENT_SCLERA_ARRAY != null && PATIENT_SCLERA_ARRAY.size() > 0) {
            for(int i=0; i<PATIENT_SCLERA_ARRAY.size(); i++) {
                Log.d(AppUtils.TAG, " type: "+ PATIENT_SCLERA_ARRAY.get(i).getRightEye());
                if(PATIENT_SCLERA_ARRAY.get(i).getRightEye().equals("1")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_SCLERA_ARRAY.get(i).getScleraId());
                    chk.setText(PATIENT_SCLERA_ARRAY.get(i).getScleraName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    sclera_gridview_RE.addView(chk);
                }
                else if(PATIENT_SCLERA_ARRAY.get(i).getRightEye().equals("2")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_SCLERA_ARRAY.get(i).getScleraId());
                    chk.setText(PATIENT_SCLERA_ARRAY.get(i).getScleraName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    sclera_gridview_LE.addView(chk);
                }
            }
        }

        /* ******************* CORNEA ANTERIOR SURFACE SECTION **************************** */
        cornea_anterior_btn = (LinearLayout) findViewById(R.id.ophthal_cornea_anterior_btn);
        cornea_anterior_btn.setOnClickListener(this);
        cornea_anterior_contents  = (LinearLayout)findViewById(R.id.ophthal_cornea_anterior_contents);
        cornea_anterior_contents.setVisibility(View.GONE);
        ophthal_cornea_anterior_image = (ImageView) findViewById(R.id.ophthal_cornea_anterior_image);
        cornea_anterior_gridview_RE = (LinearLayout) findViewById(R.id.cornea_anterior_RE_grids);
        cornea_anterior_gridview_LE = (LinearLayout) findViewById(R.id.cornea_anterior_LE_grids);

        if (PATIENT_CORNEA_ANTERIOR_ARRAY != null && PATIENT_CORNEA_ANTERIOR_ARRAY.size() > 0) {
            for(int i=0; i<PATIENT_CORNEA_ANTERIOR_ARRAY.size(); i++) {
                Log.d(AppUtils.TAG, " ant type: "+ PATIENT_CORNEA_ANTERIOR_ARRAY.get(i).getRightEye());
                if(PATIENT_CORNEA_ANTERIOR_ARRAY.get(i).getRightEye().equals("1")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_CORNEA_ANTERIOR_ARRAY.get(i).getCorneaAnteriorId());
                    chk.setText(PATIENT_CORNEA_ANTERIOR_ARRAY.get(i).getCorneaAnteriorName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    cornea_anterior_gridview_RE.addView(chk);
                }
                else if(PATIENT_CORNEA_ANTERIOR_ARRAY.get(i).getRightEye().equals("2")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_CORNEA_ANTERIOR_ARRAY.get(i).getCorneaAnteriorId());
                    chk.setText(PATIENT_CORNEA_ANTERIOR_ARRAY.get(i).getCorneaAnteriorName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    cornea_anterior_gridview_LE.addView(chk);
                }
            }
        }

        /* ******************* CORNEA POSTERIOR SURFACE SECTION **************************** */
        cornea_posterior_btn = (LinearLayout) findViewById(R.id.ophthal_cornea_posterior_btn);
        cornea_posterior_btn.setOnClickListener(this);
        cornea_posterior_contents  = (LinearLayout) findViewById(R.id.ophthal_cornea_posterior_contents);
        cornea_posterior_contents.setVisibility(View.GONE);
        ophthal_cornea_posterior_image = (ImageView) findViewById(R.id.ophthal_cornea_posterior_image);
        cornea_posterior_gridview_RE = (LinearLayout) findViewById(R.id.cornea_posterior_RE_grids);
        cornea_posterior_gridview_LE = (LinearLayout) findViewById(R.id.cornea_posterior_LE_grids);

        if (PATIENT_CORNEA_POSTERIOR_ARRAY != null && PATIENT_CORNEA_POSTERIOR_ARRAY.size() > 0) {
            for(int i=0; i<PATIENT_CORNEA_POSTERIOR_ARRAY.size(); i++) {
                Log.d(AppUtils.TAG, " type: "+ PATIENT_CORNEA_POSTERIOR_ARRAY.get(i).getRightEye());
                if(PATIENT_CORNEA_POSTERIOR_ARRAY.get(i).getRightEye().equals("1")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_CORNEA_POSTERIOR_ARRAY.get(i).getCorneaPosteriorId());
                    chk.setText(PATIENT_CORNEA_POSTERIOR_ARRAY.get(i).getCorneaPosteriorName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    cornea_posterior_gridview_RE.addView(chk);
                }
                else if(PATIENT_CORNEA_POSTERIOR_ARRAY.get(i).getRightEye().equals("2")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_CORNEA_POSTERIOR_ARRAY.get(i).getCorneaPosteriorId());
                    chk.setText(PATIENT_CORNEA_POSTERIOR_ARRAY.get(i).getCorneaPosteriorName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    cornea_posterior_gridview_LE.addView(chk);
                }
            }
        }

        /* *************************** ANTERIOR CHAMBER******************************** */
        anterior_chamber_btn = (LinearLayout) findViewById(R.id.ophthal_anterior_chamber_btn);
        anterior_chamber_btn.setOnClickListener(this);
        anterior_chamber_contents  = (LinearLayout) findViewById(R.id.ophthal_anterior_chamber_contents);
        anterior_chamber_contents.setVisibility(View.GONE);
        ophthal_anterior_chamber_image = (ImageView)  findViewById(R.id.ophthal_anterior_chamber_image);
        anterior_chamber_gridview_RE = (LinearLayout) findViewById(R.id.anterior_chamber_RE_grids);
        anterior_chamber_gridview_LE = (LinearLayout) findViewById(R.id.anterior_chamber_LE_grids);

        if (PATIENT_ANTERIOR_CHAMBER_ARRAY != null && PATIENT_ANTERIOR_CHAMBER_ARRAY.size() > 0) {
            for(int i=0; i<PATIENT_ANTERIOR_CHAMBER_ARRAY.size(); i++) {
                Log.d(AppUtils.TAG, " type: "+ PATIENT_ANTERIOR_CHAMBER_ARRAY.get(i).getRightEye());
                if(PATIENT_ANTERIOR_CHAMBER_ARRAY.get(i).getRightEye().equals("1")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_ANTERIOR_CHAMBER_ARRAY.get(i).getAnteriorChamberId());
                    chk.setText(PATIENT_ANTERIOR_CHAMBER_ARRAY.get(i).getAnteriorChamberName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    anterior_chamber_gridview_RE.addView(chk);
                }
                else if(PATIENT_ANTERIOR_CHAMBER_ARRAY.get(i).getRightEye().equals("2")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_ANTERIOR_CHAMBER_ARRAY.get(i).getAnteriorChamberId());
                    chk.setText(PATIENT_ANTERIOR_CHAMBER_ARRAY.get(i).getAnteriorChamberName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    anterior_chamber_gridview_LE.addView(chk);
                }
            }
        }

        /* *************************** IRIS SECTION ****************************** */
        iris_btn = (LinearLayout) findViewById(R.id.ophthal_iris_btn);
        iris_btn.setOnClickListener(this);
        iris_contents  = (LinearLayout) findViewById(R.id.ophthal_iris_contents);
        iris_contents.setVisibility(View.GONE);
        ophthal_iris_image = (ImageView) findViewById(R.id.ophthal_iris_image);
        iris_gridview_RE = (LinearLayout) findViewById(R.id.iris_RE_grids);
        iris_gridview_LE = (LinearLayout) findViewById(R.id.iris_LE_grids);

        if (PATIENT_IRIS_ARRAY != null && PATIENT_IRIS_ARRAY.size() > 0) {
            for(int i=0; i<PATIENT_IRIS_ARRAY.size(); i++) {
                Log.d(AppUtils.TAG, " type: "+ PATIENT_IRIS_ARRAY.get(i).getRightEye());
                if(PATIENT_IRIS_ARRAY.get(i).getRightEye().equals("1")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_IRIS_ARRAY.get(i).getIrisId());
                    chk.setText(PATIENT_IRIS_ARRAY.get(i).getIrisName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    iris_gridview_RE.addView(chk);
                }
                else if(PATIENT_IRIS_ARRAY.get(i).getRightEye().equals("2")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_IRIS_ARRAY.get(i).getIrisId());
                    chk.setText(PATIENT_IRIS_ARRAY.get(i).getIrisName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    iris_gridview_LE.addView(chk);
                }
            }
        }

        /* *************************** PUPIL SECTION ************************** */
        pupil_btn = (LinearLayout) findViewById(R.id.ophthal_pupil_btn);
        pupil_btn.setOnClickListener(this);
        pupil_contents  = (LinearLayout) findViewById(R.id.ophthal_pupil_contents);
        pupil_contents.setVisibility(View.GONE);
        ophthal_pupil_image = (ImageView) findViewById(R.id.ophthal_pupil_image);
        pupil_gridview_RE = (LinearLayout) findViewById(R.id.pupil_RE_grids);
        pupil_gridview_LE = (LinearLayout) findViewById(R.id.pupil_LE_grids);

        if (PATIENT_PUPIL_ARRAY != null && PATIENT_PUPIL_ARRAY.size() > 0) {
            for(int i=0; i<PATIENT_PUPIL_ARRAY.size(); i++) {
                Log.d(AppUtils.TAG, " type: "+ PATIENT_PUPIL_ARRAY.get(i).getRightEye());
                if(PATIENT_PUPIL_ARRAY.get(i).getRightEye().equals("1")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_PUPIL_ARRAY.get(i).getPupilId());
                    chk.setText(PATIENT_PUPIL_ARRAY.get(i).getPupilName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    pupil_gridview_RE.addView(chk);
                }
                else if(PATIENT_PUPIL_ARRAY.get(i).getRightEye().equals("2")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_PUPIL_ARRAY.get(i).getPupilId());
                    chk.setText(PATIENT_PUPIL_ARRAY.get(i).getPupilName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    pupil_gridview_LE.addView(chk);
                }
            }
        }

        /* ********************* ANGLE OF ANTERIOR CHAMBER SECTION ***************************** */
        angle_btn = (LinearLayout) findViewById(R.id.ophthal_angle_btn);
        angle_btn.setOnClickListener(this);
        angle_contents  = (LinearLayout) findViewById(R.id.ophthal_angle_contents);
        angle_contents.setVisibility(View.GONE);
        ophthal_angle_image = (ImageView) findViewById(R.id.ophthal_angle_image);
        angle_gridview_RE = (LinearLayout) findViewById(R.id.angle_RE_grids);
        angle_gridview_LE = (LinearLayout) findViewById(R.id.angle_LE_grids);

        if (PATIENT_ANGLE_ARRAY != null && PATIENT_ANGLE_ARRAY.size() > 0) {
            for(int i=0; i<PATIENT_ANGLE_ARRAY.size(); i++) {
                Log.d(AppUtils.TAG, " type: "+ PATIENT_ANGLE_ARRAY.get(i).getRightEye());
                if(PATIENT_ANGLE_ARRAY.get(i).getRightEye().equals("1")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_ANGLE_ARRAY.get(i).getAngleId());
                    chk.setText(PATIENT_ANGLE_ARRAY.get(i).getAngleName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    angle_gridview_RE.addView(chk);
                }
                else if(PATIENT_ANGLE_ARRAY.get(i).getRightEye().equals("2")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_ANGLE_ARRAY.get(i).getAngleId());
                    chk.setText(PATIENT_ANGLE_ARRAY.get(i).getAngleName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    angle_gridview_LE.addView(chk);
                }
            }
        }

        /* ********************* LENS SECTION ***************************** */
        lens_btn = (LinearLayout) findViewById(R.id.ophthal_lens_btn);
        lens_btn.setOnClickListener(this);
        lens_contents  = (LinearLayout) findViewById(R.id.ophthal_lens_contents);
        lens_contents.setVisibility(View.GONE);
        ophthal_lens_image = (ImageView) findViewById(R.id.ophthal_lens_image);
        lens_gridview_RE = (LinearLayout) findViewById(R.id.lens_RE_grids);
        lens_gridview_LE = (LinearLayout) findViewById(R.id.lens_LE_grids);

        if (PATIENT_LENS_ARRAY != null && PATIENT_LENS_ARRAY.size() > 0) {
            for(int i=0; i<PATIENT_LENS_ARRAY.size(); i++) {
                Log.d(AppUtils.TAG, " type: "+ PATIENT_LENS_ARRAY.get(i).getRightEye());
                if(PATIENT_LENS_ARRAY.get(i).getRightEye().equals("1")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_LENS_ARRAY.get(i).getLensId());
                    chk.setText(PATIENT_LENS_ARRAY.get(i).getLensName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    lens_gridview_RE.addView(chk);
                }
                else if(PATIENT_LENS_ARRAY.get(i).getRightEye().equals("2")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_LENS_ARRAY.get(i).getLensId());
                    chk.setText(PATIENT_LENS_ARRAY.get(i).getLensName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    lens_gridview_LE.addView(chk);
                }
            }
        }

        /* *********************** VITEROUS SECTION *************************** */
        viterous_btn = (LinearLayout) findViewById(R.id.ophthal_viterous_btn);
        viterous_btn.setOnClickListener(this);
        viterous_contents  = (LinearLayout) findViewById(R.id.ophthal_viterous_contents);
        viterous_contents.setVisibility(View.GONE);
        ophthal_viterous_image = (ImageView) findViewById(R.id.ophthal_viterous_image);
        viterous_gridview_RE = (LinearLayout) findViewById(R.id.viterous_RE_grids);
        viterous_gridview_LE = (LinearLayout) findViewById(R.id.viterous_LE_grids);

        if (PATIENT_VITEROUS_ARRAY != null && PATIENT_VITEROUS_ARRAY.size() > 0) {
            for(int i=0; i<PATIENT_VITEROUS_ARRAY.size(); i++) {
                Log.d(AppUtils.TAG, " type: "+ PATIENT_VITEROUS_ARRAY.get(i).getRightEye());
                if(PATIENT_VITEROUS_ARRAY.get(i).getRightEye().equals("1")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_VITEROUS_ARRAY.get(i).getViterousId());
                    chk.setText(PATIENT_VITEROUS_ARRAY.get(i).getViterousName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    viterous_gridview_RE.addView(chk);
                }
                else if(PATIENT_VITEROUS_ARRAY.get(i).getRightEye().equals("2")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_VITEROUS_ARRAY.get(i).getViterousId());
                    chk.setText(PATIENT_VITEROUS_ARRAY.get(i).getViterousName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    viterous_gridview_LE.addView(chk);
                }
            }
        }

        /* ************************* FUNDUS SECTION ************************* */
        fundus_btn = (LinearLayout) findViewById(R.id.ophthal_fundus_btn);
        fundus_btn.setOnClickListener(this);
        fundus_contents  = (LinearLayout) findViewById(R.id.ophthal_fundus_contents);
        fundus_contents.setVisibility(View.GONE);
        ophthal_fundus_image = (ImageView) findViewById(R.id.ophthal_fundus_image);
        fundus_gridview_RE = (LinearLayout) findViewById(R.id.fundus_RE_grids);
        fundus_gridview_LE = (LinearLayout) findViewById(R.id.fundus_LE_grids);

        if (PATIENT_FUNDUS_ARRAY != null && PATIENT_FUNDUS_ARRAY.size() > 0) {
            for(int i=0; i<PATIENT_FUNDUS_ARRAY.size(); i++) {
                Log.d(AppUtils.TAG, " type: "+ PATIENT_FUNDUS_ARRAY.get(i).getRightEye());
                if(PATIENT_FUNDUS_ARRAY.get(i).getRightEye().equals("1")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_FUNDUS_ARRAY.get(i).getFundusId());
                    chk.setText(PATIENT_FUNDUS_ARRAY.get(i).getFundusName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    fundus_gridview_RE.addView(chk);
                }
                else if(PATIENT_FUNDUS_ARRAY.get(i).getRightEye().equals("2")) {
                    CheckBox chk=new CheckBox(this);
                    chk.setId(PATIENT_FUNDUS_ARRAY.get(i).getFundusId());
                    chk.setText(PATIENT_FUNDUS_ARRAY.get(i).getFundusName());
                    chk.setTextColor(getResources().getColor(R.color.textPrimary));
                    chk.setPadding(0,10,0,5);
                    chk.setChecked(true);
                    chk.setClickable(false);
                    fundus_gridview_LE.addView(chk);
                }
            }
        }

        /* *********************** REFRACTION SECTION *************************** */
        refraction_btn = (LinearLayout) findViewById(R.id.ophthal_refraction_btn);
        refraction_btn.setOnClickListener(this);
        refraction_contents  = (LinearLayout) findViewById(R.id.ophthal_refraction_contents);
        refraction_contents.setVisibility(View.GONE);
        ophthal_refraction_image = (ImageView) findViewById(R.id.ophthal_refraction_image);

        _edt_refraction_RE_top = (CustomEditText) findViewById(R.id.ophthal_refraction_RE_top);
        _edt_refraction_RE_bottom = (CustomEditText) findViewById(R.id.ophthal_refraction_RE_bottom);
        _edt_refraction_LE_top = (CustomEditText) findViewById(R.id.ophthal_refraction_LE_top);
        _edt_refraction_LE_bottom = (CustomEditText) findViewById(R.id.ophthal_refraction_LE_bottom);

        _edt_refraction_RE_top.setText(REFRACTION_RE_VALUE1);
        _edt_refraction_RE_bottom.setText(REFRACTION_RE_VALUE2);
        _edt_refraction_LE_top.setText(REFRACTION_LE_VALUE1);
        _edt_refraction_LE_bottom.setText(REFRACTION_LE_VALUE2);

        /* ************************ SPECTACLE PRESCRIPTION SECTION ************************* */
        spectacle_prescription_btn = (LinearLayout) findViewById(R.id.ophthal_spectacle_prescription_btn);
      //  spectacle_prescription_btn.setOnClickListener(this);
        spectacle_prescription_contents = (LinearLayout) findViewById(R.id.ophthal_spectacle_prescription_contents);
        spectacle_prescription_contents.setVisibility(View.VISIBLE);

        _edt_right_dv_sphere = (CustomTextView) findViewById(R.id.spect_dv_sphere_right);
        _edt_right_dv_cyl = (CustomTextView) findViewById(R.id.spect_dv_cyl_right);
        _edt_right_dv_axis = (CustomTextView) findViewById(R.id.spect_dv_axis_right);
        _edt_right_nv_sphere = (CustomTextView) findViewById(R.id.spect_nv_sphere_right);
        _edt_right_nv_cyl = (CustomTextView)findViewById(R.id.spect_nv_cyl_right);
        _edt_right_nv_axis = (CustomTextView)findViewById(R.id.spect_nv_axis_right);
        _edt_right_ipd = (CustomTextView) findViewById(R.id.spect_ipd_right);

        _edt_left_dv_sphere = (CustomTextView) findViewById(R.id.spect_dv_sphere_left);
        _edt_left_dv_cyl = (CustomTextView) findViewById(R.id.spect_dv_cyl_left);
        _edt_left_dv_axis = (CustomTextView) findViewById(R.id.spect_dv_axis_left);
        _edt_left_nv_sphere = (CustomTextView) findViewById(R.id.spect_nv_sphere_left);
        _edt_left_nv_cyl = (CustomTextView) findViewById(R.id.spect_nv_cyl_left);
        _edt_left_nv_axis = (CustomTextView) findViewById(R.id.spect_nv_axis_left);
        _edt_left_ipd = (CustomTextView) findViewById(R.id.spect_ipd_left);

        _edt_right_dv_sphere.setText(GET_DV_SPHERE_RIGHT);
        _edt_right_dv_cyl.setText(GET_DV_CYCLE_RIGHT);
        _edt_right_dv_axis.setText(GET_DV_AXIS_RIGHT);
        _edt_right_nv_sphere.setText(GET_NV_SPHERE_RIGHT);
        _edt_right_nv_cyl.setText(GET_NV_CYCLE_RIGHT);
        _edt_right_nv_axis.setText(GET_NV_AXIS_RIGHT);
        _edt_right_ipd.setText(GET_IPD_RIGHT);

        _edt_left_dv_sphere.setText(GET_DV_SPHERE_LEFT);
        _edt_left_dv_cyl.setText(GET_DV_CYCLE_LEFT);
        _edt_left_dv_axis.setText(GET_DV_AXIS_LEFT);
        _edt_left_nv_sphere.setText(GET_NV_SPHERE_LEFT);
        _edt_left_nv_cyl.setText(GET_NV_CYCLE_LEFT);
        _edt_left_nv_axis.setText(GET_NV_AXIS_LEFT);
        _edt_left_ipd.setText(GET_IPD_LEFT);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_chief_medcomp_btn:
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(chief_medcomp_btn.getWindowToken(), 0);

                if(view_chief_medcomp_status == false) {
                    chief_medcomp_contents.setVisibility(View.VISIBLE);
                    chief_medcomp_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_chief_medcomp_status = true;
                }
                else  if(view_chief_medcomp_status == true) {
                    chief_medcomp_contents.setVisibility(View.GONE);
                    chief_medcomp_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_chief_medcomp_status = false;
                }
                break;
            case R.id.view_investigation_btn:
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(investigation_btn.getWindowToken(), 0);

                if(view_investigation_status == false) {
                    investigation_contents.setVisibility(View.VISIBLE);
                    investigation_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_investigation_status = true;
                }
                else  if(view_investigation_status == true) {
                    investigation_contents.setVisibility(View.GONE);
                    investigation_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_investigation_status = false;
                }
                break;
            case R.id.view_examination_btn:
                InputMethodManager imm3 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(examination_btn.getWindowToken(), 0);
                if(view_examination_status == false) {
                    examination_contents.setVisibility(View.VISIBLE);
                    examination_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_examination_status = true;
                }
                else  if(view_examination_status == true) {
                    examination_contents.setVisibility(View.GONE);
                    examination_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_examination_status = false;
                }
                break;
            case R.id.view_diagnosis_btn:
                InputMethodManager imm4 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm4.hideSoftInputFromWindow(diagnosis_btn.getWindowToken(), 0);
                if(view_diagnosis_status == false) {
                    diagnosis_contents.setVisibility(View.VISIBLE);
                    diagnosis_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_diagnosis_status = true;
                }
                else  if(view_diagnosis_status == true) {
                    diagnosis_contents.setVisibility(View.GONE);
                    diagnosis_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_diagnosis_status = false;
                }
                break;
            case R.id.view_treatment_btn:
                InputMethodManager imm5 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm5.hideSoftInputFromWindow(treatment_btn.getWindowToken(), 0);
                if(view_treatment_status == false) {
                    treatment_contents.setVisibility(View.VISIBLE);
                    treatment_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_treatment_status = true;
                }
                else  if(view_treatment_status == true) {
                    treatment_contents.setVisibility(View.GONE);
                    treatment_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_treatment_status = false;
                }
                break;
            case R.id.view_prescription_btn:
                InputMethodManager imm6 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm6.hideSoftInputFromWindow(prescription_btn.getWindowToken(), 0);
                if(view_prescription_status == false) {
                    prescription_contents.setVisibility(View.VISIBLE);
                    prescription_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_prescription_status = true;
                }
                else  if(view_prescription_status == true) {
                    prescription_contents.setVisibility(View.GONE);
                    prescription_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_prescription_status = false;
                }
                break;
            case R.id.view_visit_investupdate:
                InputMethodManager imm7 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm7.hideSoftInputFromWindow(update_investigation_btn.getWindowToken(), 0);

                sendUpdateInvestigationDetails();
                break;
            case R.id.view_visit_invest_refer:
                InputMethodManager imm8 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm8.hideSoftInputFromWindow(refer_diagnostics_btn.getWindowToken(), 0);
                collectDiagnosticCentreList();
                break;
            case R.id.view_visit_presc_refer:
                InputMethodManager imm9 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm9.hideSoftInputFromWindow(refer_pharma_btn.getWindowToken(), 0);
                collectPharmaCentreList();
                break;
            case R.id.view_visit_presc_sms:
                InputMethodManager imm10 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm10.hideSoftInputFromWindow(send_presc_sms_btn.getWindowToken(), 0);
                // sendPrescriptionToPatient(1); // 1- Send SMS
                sharePrescriptions();
                break;
            case R.id.view_visit_presc_email:
                InputMethodManager imm11 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm11.hideSoftInputFromWindow(send_presc_email_btn.getWindowToken(), 0);
                sendPrescriptionToPatient(2); // 1- Send EMAIL
                break;
            case R.id.ophthal_lids_btn:
                InputMethodManager imm41 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm41.hideSoftInputFromWindow(lids_btn.getWindowToken(), 0);
                if(view_lids_status == false) {
                    lids_contents.setVisibility(View.VISIBLE);
                    ophthal_lids_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_lids_status = true;
                }
                else  if(view_lids_status == true) {
                    lids_contents.setVisibility(View.GONE);
                    ophthal_lids_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_lids_status = false;
                }
                break;
            case R.id.ophthal_conjuctiva_btn:
                InputMethodManager imm61 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm61.hideSoftInputFromWindow(conjuctiva_btn.getWindowToken(), 0);
                if(view_conjuctiva_status == false) {
                    conjuctiva_contents.setVisibility(View.VISIBLE);
                    ophthal_conjuctiva_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_conjuctiva_status = true;
                }
                else  if(view_conjuctiva_status == true) {
                    conjuctiva_contents.setVisibility(View.GONE);
                    ophthal_conjuctiva_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_conjuctiva_status = false;
                }
                break;
            case R.id.ophthal_sclera_btn:
                InputMethodManager imm81 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm81.hideSoftInputFromWindow(sclera_btn.getWindowToken(), 0);
                if(view_sclera_status == false) {
                    sclera_contents.setVisibility(View.VISIBLE);
                    ophthal_sclera_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_sclera_status = true;
                }
                else  if(view_sclera_status == true) {
                    sclera_contents.setVisibility(View.GONE);
                    ophthal_sclera_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_sclera_status = false;
                }
                break;
            case R.id.ophthal_cornea_anterior_btn:
                InputMethodManager imm101 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm101.hideSoftInputFromWindow(cornea_anterior_btn.getWindowToken(), 0);
                if(view_cornea_anterior_status == false) {
                    cornea_anterior_contents.setVisibility(View.VISIBLE);
                    ophthal_cornea_anterior_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_cornea_anterior_status = true;
                }
                else  if(view_cornea_anterior_status == true) {
                    cornea_anterior_contents.setVisibility(View.GONE);
                    ophthal_cornea_anterior_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_cornea_anterior_status = false;
                }
                break;
            case R.id.ophthal_cornea_posterior_btn:
                InputMethodManager imm121 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm121.hideSoftInputFromWindow(cornea_posterior_btn.getWindowToken(), 0);
                if(view_cornea_posterior_status == false) {
                    cornea_posterior_contents.setVisibility(View.VISIBLE);
                    ophthal_cornea_posterior_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_cornea_posterior_status = true;
                }
                else  if(view_cornea_posterior_status == true) {
                    cornea_posterior_contents.setVisibility(View.GONE);
                    ophthal_cornea_posterior_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_cornea_posterior_status = false;
                }
                break;
            case R.id.ophthal_anterior_chamber_btn:
                InputMethodManager imm14 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm14.hideSoftInputFromWindow(anterior_chamber_btn.getWindowToken(), 0);
                if(view_anterior_chamber_status == false) {
                    anterior_chamber_contents.setVisibility(View.VISIBLE);
                    ophthal_anterior_chamber_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_anterior_chamber_status = true;
                }
                else  if(view_anterior_chamber_status == true) {
                    anterior_chamber_contents.setVisibility(View.GONE);
                    ophthal_anterior_chamber_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_anterior_chamber_status = false;
                }
                break;
            case R.id.ophthal_iris_btn:
                InputMethodManager imm16 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm16.hideSoftInputFromWindow(iris_btn.getWindowToken(), 0);
                if(view_iris_status == false) {
                    iris_contents.setVisibility(View.VISIBLE);
                    ophthal_iris_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_iris_status = true;
                }
                else  if(view_iris_status == true) {
                    iris_contents.setVisibility(View.GONE);
                    ophthal_iris_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_iris_status = false;
                }
                break;
            case R.id.ophthal_pupil_btn:
                InputMethodManager imm18 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm18.hideSoftInputFromWindow(pupil_btn.getWindowToken(), 0);
                if(view_pupil_status == false) {
                    pupil_contents.setVisibility(View.VISIBLE);
                    ophthal_pupil_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_pupil_status = true;
                }
                else  if(view_pupil_status == true) {
                    pupil_contents.setVisibility(View.GONE);
                    ophthal_pupil_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_pupil_status = false;
                }
                break;
            case R.id.ophthal_angle_btn:
                InputMethodManager imm20 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm20.hideSoftInputFromWindow(angle_btn.getWindowToken(), 0);
                if(view_angle_status == false) {
                    angle_contents.setVisibility(View.VISIBLE);
                    ophthal_angle_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_angle_status = true;
                }
                else  if(view_angle_status == true) {
                    angle_contents.setVisibility(View.GONE);
                    ophthal_angle_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_angle_status = false;
                }
                break;
            case R.id.ophthal_lens_btn:
                InputMethodManager imm22 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm22.hideSoftInputFromWindow(lens_btn.getWindowToken(), 0);
                if(view_lens_status == false) {
                    lens_contents.setVisibility(View.VISIBLE);
                    ophthal_lens_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_lens_status = true;
                }
                else  if(view_lens_status == true) {
                    lens_contents.setVisibility(View.GONE);
                    ophthal_lens_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_lens_status = false;
                }
                break;
            case R.id.ophthal_viterous_btn:
                InputMethodManager imm24 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm24.hideSoftInputFromWindow(viterous_btn.getWindowToken(), 0);
                if(view_viterous_status == false) {
                    viterous_contents.setVisibility(View.VISIBLE);
                    ophthal_viterous_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_viterous_status = true;
                }
                else  if(view_viterous_status == true) {
                    viterous_contents.setVisibility(View.GONE);
                    ophthal_viterous_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_viterous_status = false;
                }
                break;
            case R.id.ophthal_fundus_btn:
                InputMethodManager imm26 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm26.hideSoftInputFromWindow(fundus_btn.getWindowToken(), 0);
                if(view_fundus_status == false) {
                    fundus_contents.setVisibility(View.VISIBLE);
                    ophthal_fundus_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_fundus_status = true;
                }
                else  if(view_fundus_status == true) {
                    fundus_contents.setVisibility(View.GONE);
                    ophthal_fundus_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_fundus_status = false;
                }
                break;
            case R.id.ophthal_refraction_btn:
                InputMethodManager imm28 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm28.hideSoftInputFromWindow(refraction_btn.getWindowToken(), 0);
                if(view_refraction_status == false) {
                    refraction_contents.setVisibility(View.VISIBLE);
                    ophthal_refraction_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_refraction_status = true;
                }
                else  if(view_refraction_status == true) {
                    refraction_contents.setVisibility(View.GONE);
                    ophthal_refraction_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_refraction_status = false;
                }
                break;
        /*    case R.id.addvisit_investigation_btn:
                InputMethodManager imm29 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm29.hideSoftInputFromWindow(investigation_btn.getWindowToken(), 0);

                if(view_investigation_status == false) {
                    investigation_contents.setVisibility(View.VISIBLE);
                    view_investigation_status = true;
                }
                else  if(view_investigation_status == true) {
                    investigation_contents.setVisibility(View.GONE);
                    view_investigation_status = false;
                }
                break;*/
            case R.id.ophthal_spectacle_prescription_btn:
                InputMethodManager imm36 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm36.hideSoftInputFromWindow(spectacle_prescription_btn.getWindowToken(), 0);
                if(view_spectacle_prescription_status == false) {
                    spectacle_prescription_contents.setVisibility(View.VISIBLE);
                    view_spectacle_prescription_status = true;
                }
                else  if(view_spectacle_prescription_status == true) {
                    spectacle_prescription_contents.setVisibility(View.GONE);
                    view_spectacle_prescription_status = false;
                }
                break;
        }
    }

    private void sharePrescriptions() {

       /* try {
            String shareURL = APIClass.DRS_SHARE_PRESCRIPTION_LINK + md5(String.valueOf(PATIENT_ID))+ ".jpg";
            Log.d(Utils.TAG, " shareURL: "+ shareURL);
            Intent i=new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Premium EMR");
            i.putExtra(android.content.Intent.EXTRA_TEXT, "This is your digitalized prescription sent by Dr. "+USER_NAME+" \n\nPlease visit the following Link: \n"+shareURL+ "\n\n - Thank You, \n Medisense");
            startActivity(Intent.createChooser(i,"Share via"));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
*/

    }

    public static String md5(String input) throws NoSuchAlgorithmException {
        String result = input;
        if(input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while(result.length() < 32) { //31 position string
                result = "0" + result;
            }
        }
        return result;
    }

    private void sendUpdateInvestigationDetails() {
        PATIENT_INVESTIGATION_ARRAY = new ArrayList<>();

        Log.d(AppUtils.TAG, " ************* sendUpdateInvestigationDetails ************************** ");
        Log.d(AppUtils.TAG, " investigationGeneral size: "+ PATIENT_INVESTIGATION_GENERAL_ARRAY.size());
        Log.d(AppUtils.TAG, " investigationOphthal size: "+ PATIENT_INVESTIGATION_OPHTHAL_ARRAY.size());

        for(int i=0;i<PATIENT_INVESTIGATION_GENERAL_ARRAY.size();i++)
        {
            PATIENT_INVESTIGATION_ARRAY.add(new Investigations(PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getInvestigationId(),
                    PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getTestId(),PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getGroupTestId(),PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getTestName(),
                    PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getNormalRange(),PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getTestActualValue(),
                    PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getRightEye(),PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getLeftEye(),PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getInvestDepartment(),
                    PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getUserId(),PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getLoginType()));

        }
        for(int i=0;i<PATIENT_INVESTIGATION_OPHTHAL_ARRAY.size();i++)
        {
            PATIENT_INVESTIGATION_ARRAY.add(new Investigations(PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getInvestigationId(),
                    PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getTestId(),PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getGroupTestId(),PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getTestName(),
                    PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getNormalRange(),PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getTestActualValue(),
                    PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getRightEye(),PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getLeftEye(),PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getInvestDepartment(),
                    PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getUserId(),PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getLoginType()));

        }
        Log.d(AppUtils.TAG, " investigation size: "+ PATIENT_INVESTIGATION_ARRAY.size());
        for(int i=0;i<PATIENT_INVESTIGATION_ARRAY.size();i++)
        {
            Log.d(AppUtils.TAG, " investID: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getInvestigationId());
            Log.d(AppUtils.TAG, " testID: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getTestId());
            Log.d(AppUtils.TAG, " grouptestID: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getGroupTestId());
            Log.d(AppUtils.TAG, " testName: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getTestName());
            Log.d(AppUtils.TAG, " groupTest: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getGroupTest());
            Log.d(AppUtils.TAG, " mfRange: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getMFRange());
            Log.d(AppUtils.TAG, " normalRange: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getNormalRange());
            Log.d(AppUtils.TAG, " actualValue: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getTestActualValue());
            Log.d(AppUtils.TAG, " rightEye: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getRightEye());
            Log.d(AppUtils.TAG, " leftEye: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getLeftEye());
            Log.d(AppUtils.TAG, " department: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getInvestDepartment());
            Log.d(AppUtils.TAG, " userid: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getUserId());
            Log.d(AppUtils.TAG, " logintype: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getLoginType());
        }

        submiitUpdateInvestigationRequestToServer(PATIENT_INVESTIGATION_ARRAY);
    }

    private void submiitUpdateInvestigationRequestToServer(final List<Investigations> patient_investigation_array) {
        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(ViewOphthalVisitDetailsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

            }

            @Override
            protected Boolean doInBackground(Void... params) {

              /*  try {

                    JSONObject jsonObject = JSONParser.updateInvestigations(PATIENT_ID, patient_investigation_array, USER_ID, USER_LOGIN_TYPE);

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                        if (jsonObject.getString("result").equals("success")) {
                            status = true;

                        } else {
                            status = false;
                        }
                    }
                    // return true;
                } catch (JSONException e) {
                    Log.i(Utils.TAG, "Error : " + e.getLocalizedMessage());
                    return false;
                }*/
                return status;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (progressDialog != null)
                    progressDialog.dismiss();
                Log.e(AppUtils.TAG, "aBoolean: " + String.valueOf(aBoolean));
                if (aBoolean) {

                    // Toast.makeText(SignUpActivity.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewOphthalVisitDetailsActivity.this, R.style.CustomDialog);
                    alertDialogBuilder.setMessage("Updated successfully. ");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(ViewOphthalVisitDetailsActivity.this, "Failed to update !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }

    private void collectDiagnosticCentreList() {
       /* diagnosticsListArraylist = new ArrayList<>();
        selectedDiagnoCentreArraylist = new ArrayList<>();
        if (DIAGNOSTIC_CENTRE_LIST.equals("")) {
            Log.d(Utils.TAG, "DIAGNOSTIC_CENTRE_LIST EMPTY ");

            if (NetworkUtil.getConnectivityStatusString(ViewOphthalVisitDetailsActivity.this).equalsIgnoreCase("enabled")) {
                collectDiagnosticsListFromServer();
            } else {
                AppUtils.showCustomAlertMessage(ViewOphthalVisitDetailsActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            diagnosticsListArraylist = gson.fromJson(DIAGNOSTIC_CENTRE_LIST, new TypeToken<List<DiagnosticCentreList>>() {
            }.getType());
            if(diagnosticsListArraylist.size() > 0 ) {
                Log.d(Utils.TAG, "DIAGNOSTIC_CENTRE_LIST > 0 " + diagnosticsListArraylist.size());
                prepareDiagnostiCentreData(diagnosticsListArraylist);
            }
        }*/
    }

    private void collectDiagnosticsListFromServer() {
        diagnosticsListArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(ViewOphthalVisitDetailsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Diagnostics...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        /*StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_DIAGNOSTICS_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "diagno list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("diagnostics_details");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            diagnosticsListArraylist.add(new DiagnosticCentreList(jsonArray.getJSONObject(i).getInt("diagnostic_id"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_name"),jsonArray.getJSONObject(i).getString("diagnosis_city"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_state"),jsonArray.getJSONObject(i).getString("diagnosis_country"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_contact_person"),jsonArray.getJSONObject(i).getString("diagnosis_contact_num"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_email"),USER_ID, USER_LOGIN_TYPE));

                                        }

                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(diagnosticsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearDiagnosticCentres();
                                            shareadPreferenceClass.setDiagnosticCentres(jsonDiagnoText);
                                        }

                                        prepareDiagnostiCentreData(diagnosticsListArraylist);
                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(diagnosticsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearDiagnosticCentres();
                                            shareadPreferenceClass.setDiagnosticCentres(jsonDiagnoText);
                                        }
                                        prepareDiagnostiCentreData(diagnosticsListArraylist);
                                    }
                                }
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(ViewOphthalVisitDetailsActivity.this).
                getRequestQueue();
        AppController.getInstance(ViewOphthalVisitDetailsActivity.this).addToRequestQueue(stringRequest);*/
    }

    private void prepareDiagnostiCentreData(List<DiagnosticCentreList> diagnosticsListArraylist) {
       /* selectedDiagnoCentreArraylist = new ArrayList<>();
        final Dialog dialog = new Dialog(ViewOphthalVisitDetailsActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_diagnostic_center);
        dialog.setTitle("Refer to Diagnosis");
        dialog.setCanceledOnTouchOutside(true);

        addNew_contents = dialog.findViewById(R.id.diagnostic_addNew_content);
        addNew_contents.setVisibility(View.GONE);


        list_contents = dialog.findViewById(R.id.diagnostic_list_content);
        list_contents.setVisibility(View.VISIBLE);

        final ImageView close_btn = (ImageView)  dialog.findViewById(R.id.diagnostic_close);
        final ImageView refresh_btn = (ImageView)  dialog.findViewById(R.id.diagnostic_refresh);
        CustomTextViewBold add_new_btn = (CustomTextViewBold) dialog.findViewById(R.id.diagnostic_add_new);
        final CustomTextViewBold submit_new_btn = (CustomTextViewBold) dialog.findViewById(R.id.edt_diagnostic_submit);
        final CustomTextViewBold cancel_new_btn = (CustomTextViewBold) dialog.findViewById(R.id.edt_diagnostic_cancel);

        final CustomEditText _edit_name = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_name);
        final CustomEditText _edit_email = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_email);
        final CustomEditText _edit_mobile = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_mobile);
        final CustomEditText _edit_city = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_city);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(close_btn.getWindowToken(), 0);
            }
        });

        add_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNew_contents.setVisibility(View.VISIBLE);
                list_contents.setVisibility(View.GONE);
            }
        });

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectDiagnosticsListFromServer();
            }
        });

        submit_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(submit_new_btn.getWindowToken(), 0);

                String diagno_name = _edit_name.getText().toString().trim();
                String diagno_email = _edit_email.getText().toString().trim();
                String diagno_mobile = _edit_mobile.getText().toString().trim();
                String diagno_city = _edit_city.getText().toString().trim();

                if(diagno_name.equals("")) {
                    Toast.makeText(ViewOphthalVisitDetailsActivity.this, "Enter Diagnostic Names", Toast.LENGTH_SHORT).show();
                }
                else  if((diagno_mobile.equals("")) || (diagno_mobile.length() <10)) {
                    Toast.makeText(ViewOphthalVisitDetailsActivity.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendDiagnosticNewToServer(diagno_name,diagno_email,diagno_mobile,diagno_city);
                    addNew_contents.setVisibility(View.GONE);
                    list_contents.setVisibility(View.VISIBLE);
                }
            }
        });

        cancel_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm3 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(cancel_new_btn.getWindowToken(), 0);
                addNew_contents.setVisibility(View.GONE);
                list_contents.setVisibility(View.VISIBLE);
            }
        });

        final SearchView diagnostic_searchView = (SearchView) dialog.findViewById(R.id.searchview_diagnostic);
        ListView diagnostic_listview = (ListView) dialog.findViewById(R.id.diagnostic_list);

        final CustomDiagnosticCenterFilterAdapter diagnoAdapter = new CustomDiagnosticCenterFilterAdapter(ViewOphthalVisitDetailsActivity.this,
                R.layout.activity_listview, diagnosticsListArraylist);
        diagnostic_listview.setAdapter(diagnoAdapter);
        diagnostic_listview.setTextFilterEnabled(true);

        diagnostic_searchView.setIconifiedByDefault(false);
        diagnostic_searchView.setQueryHint("Search Diagnostic Center");
        diagnostic_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                diagnostic_searchView.setQuery("", false);
                diagnostic_searchView.clearFocus();
                diagnostic_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                    diagnoAdapter.filter(newText.toString());
                    // specialization_listview.clearTextFilter();
                } else {
                    //  specialization_listview.setFilterText(newText.toString());
                    diagnoAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        diagnostic_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                diagnostic_searchView.setQuery("", false);
                diagnostic_searchView.setIconified(false);
                diagnostic_searchView.clearFocus();
                return false;
            }
        });

        diagnostic_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DiagnosticCentreList myCustomAdapterItem = (DiagnosticCentreList) parent.getItemAtPosition(position);
              *//* Toast.makeText(getActivity(),  myCustomAdapterItem.getSymptomsName() + " added.",
                        Toast.LENGTH_SHORT)
                        .show();*//*
                diagnostic_searchView.setQuery("", false);
                diagnostic_searchView.setIconified(false);
                diagnostic_searchView.clearFocus();

                selectedDiagnoCentreArraylist = new ArrayList<>();
                selectedDiagnoCentreArraylist.add(new DiagnosticCentreList(myCustomAdapterItem.getDiagnoId(),myCustomAdapterItem.getDiagnoName()));

                sendReferDiagnoRequestToserver(selectedDiagnoCentreArraylist);
                dialog.dismiss();
            }
        });

        dialog.show();*/
    }


    private void sendDiagnosticNewToServer(final String diagno_name, final String diagno_email, final String diagno_mobile, final String diagno_city) {
       /* diagnosticsListArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(ViewOphthalVisitDetailsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding New Diagnostic Center...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_DIAGNOSTICS_ADD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "diagno list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("diagnostics_details");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            diagnosticsListArraylist.add(new DiagnosticCentreList(jsonArray.getJSONObject(i).getInt("diagnostic_id"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_name"),jsonArray.getJSONObject(i).getString("diagnosis_city"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_state"),jsonArray.getJSONObject(i).getString("diagnosis_country"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_contact_person"),jsonArray.getJSONObject(i).getString("diagnosis_contact_num"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_email"),USER_ID, USER_LOGIN_TYPE));

                                        }

                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(diagnosticsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearDiagnosticCentres();
                                            shareadPreferenceClass.setDiagnosticCentres(jsonDiagnoText);
                                        }

                                        prepareDiagnostiCentreData(diagnosticsListArraylist);
                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(diagnosticsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearDiagnosticCentres();
                                            shareadPreferenceClass.setDiagnosticCentres(jsonDiagnoText);
                                        }
                                    }
                                }
                                progressDialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                map.put(APIClass.KEY_DIAGNO_NAME, diagno_name);
                map.put(APIClass.KEY_DIAGNO_EMAIL, diagno_email);
                map.put(APIClass.KEY_DIAGNO_MOBILE, diagno_mobile);
                map.put(APIClass.KEY_DIAGNO_CITY, diagno_city);
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(ViewOphthalVisitDetailsActivity.this).
                getRequestQueue();
        AppController.getInstance(ViewOphthalVisitDetailsActivity.this).addToRequestQueue(stringRequest);*/
    }


    private void sendReferDiagnoRequestToserver(final List<DiagnosticCentreList> selectedDiagnoCentreArraylist) {

      /*  if(selectedDiagnoCentreArraylist.size() > 0) {
            for (int i = 0; i < selectedDiagnoCentreArraylist.size(); i++) {
                Log.i(Utils.TAG, selectedDiagnoCentreArraylist.get(i).getDiagnoId() + " : " + selectedDiagnoCentreArraylist.get(i).getDiagnoName());
            }

            Log.d(Utils.TAG, " diagnoSize " + selectedDiagnoCentreArraylist.size());
            new AsyncTask<Void, Integer, Boolean>() {
                boolean status = false;
                ProgressDialog progressDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = new ProgressDialog(ViewOphthalVisitDetailsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();

                }

                @Override
                protected Boolean doInBackground(Void... params) {

                    try {

                        JSONObject jsonObject = JSONParser.sendDiagnosticReferal(String.valueOf(PATIENT_ID), String.valueOf(EPISODE_ID),
                                selectedDiagnoCentreArraylist, USER_ID, USER_LOGIN_TYPE);

                        if (jsonObject != null) {
                            Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                            if (jsonObject.getString("result").equals("success")) {
                                status = true;
                            } else {
                                status = false;
                            }
                        }
                        // return true;
                    } catch (JSONException e) {
                        Log.i(Utils.TAG, "Error : " + e.getLocalizedMessage());
                        return false;
                    }
                    return status;
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    Log.e(Utils.TAG, "aBoolean: " + String.valueOf(aBoolean));
                    if (aBoolean) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewOphthalVisitDetailsActivity.this, R.style.CustomDialog);
                        alertDialogBuilder.setMessage("Referred to diagnostics successfully !!!. ");
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        Toast.makeText(ViewOphthalVisitDetailsActivity.this, "Failed to send referal. \nTry later !!!", Toast.LENGTH_SHORT).show();
                    }

                }
            }.execute();
        }*/
    }

    private void collectPharmaCentreList() {
      /*  pharmaListArraylist = new ArrayList<>();
        if (PHARMA_CENTRE_LIST.equals("")) {
            Log.d(Utils.TAG, "PHARMA_CENTRE_LIST EMPTY ");

            if (NetworkUtil.getConnectivityStatusString(ViewOphthalVisitDetailsActivity.this).equalsIgnoreCase("enabled")) {
                collectPharmaListFromServer();
            } else {
                AppUtils.showCustomAlertMessage(ViewOphthalVisitDetailsActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            pharmaListArraylist = gson.fromJson(PHARMA_CENTRE_LIST, new TypeToken<List<PharmaCentreList>>() {
            }.getType());
            if(pharmaListArraylist.size() > 0 ) {
                Log.d(Utils.TAG, "PHARMA_CENTRE_LIST > 0 " + pharmaListArraylist.size());
                preparePharmaCentreData(pharmaListArraylist);
            }
        }*/

    }

    private void collectPharmaListFromServer() {
       /* pharmaListArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(ViewOphthalVisitDetailsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Pharmacy lists...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PHARMA_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "pharma list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("pharma_details");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            pharmaListArraylist.add(new PharmaCentreList(jsonArray.getJSONObject(i).getInt("pharma_id"),
                                                    jsonArray.getJSONObject(i).getString("pharma_name"),jsonArray.getJSONObject(i).getString("pharma_city"),
                                                    jsonArray.getJSONObject(i).getString("pharma_state"),jsonArray.getJSONObject(i).getString("pharma_country"),
                                                    jsonArray.getJSONObject(i).getString("phrama_contact_person"),jsonArray.getJSONObject(i).getString("pharma_contact_num"),
                                                    jsonArray.getJSONObject(i).getString("pharma_email"),USER_ID, USER_LOGIN_TYPE));

                                        }

                                        //Set the values
                                        gson = new Gson();
                                        String jsonPharmaText = gson.toJson(pharmaListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearPharmaCentres();
                                            shareadPreferenceClass.setPharmaCentres(jsonPharmaText);
                                        }

                                        preparePharmaCentreData(pharmaListArraylist);
                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonPharmaText = gson.toJson(pharmaListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearPharmaCentres();
                                            shareadPreferenceClass.setPharmaCentres(jsonPharmaText);
                                        }
                                        preparePharmaCentreData(pharmaListArraylist);
                                    }
                                }
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(ViewOphthalVisitDetailsActivity.this).
                getRequestQueue();
        AppController.getInstance(ViewOphthalVisitDetailsActivity.this).addToRequestQueue(stringRequest);*/
    }

    private void preparePharmaCentreData(List<PharmaCentreList> pharmaListArraylist) {
       /* selectedPharmaArraylist = new ArrayList<>();

        final Dialog dialog = new Dialog(ViewOphthalVisitDetailsActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_diagnostic_center);
        dialog.setTitle("Refer to Pharmacy");
        dialog.setCanceledOnTouchOutside(true);

        final LinearLayout addNew_contents = dialog.findViewById(R.id.diagnostic_addNew_content);
        addNew_contents.setVisibility(View.GONE);


        final LinearLayout list_contents = dialog.findViewById(R.id.diagnostic_list_content);
        list_contents.setVisibility(View.VISIBLE);

        final ImageView close_btn = (ImageView)  dialog.findViewById(R.id.diagnostic_close);
        final ImageView refresh_btn = (ImageView)  dialog.findViewById(R.id.diagnostic_refresh);
        CustomTextViewBold add_new_btn = (CustomTextViewBold) dialog.findViewById(R.id.diagnostic_add_new);
        final CustomTextViewBold submit_new_btn = (CustomTextViewBold) dialog.findViewById(R.id.edt_diagnostic_submit);
        final CustomTextViewBold cancel_new_btn = (CustomTextViewBold) dialog.findViewById(R.id.edt_diagnostic_cancel);

        final CustomEditText _edit_name = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_name);
        final CustomEditText _edit_email = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_email);
        final CustomEditText _edit_mobile = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_mobile);
        final CustomEditText _edit_city = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_city);
        CustomTextViewSemiBold title = (CustomTextViewSemiBold)dialog.findViewById(R.id.diagnostic_addNew_title);
        title.setText("Add New Pharmacy");
        _edit_name.setHint("Pharmacy Name");
        add_new_btn.setText("Add New Pharmacy");

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(close_btn.getWindowToken(), 0);
            }
        });

        add_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNew_contents.setVisibility(View.VISIBLE);
                list_contents.setVisibility(View.GONE);
            }
        });

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectPharmaListFromServer();
            }
        });

        submit_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(submit_new_btn.getWindowToken(), 0);

                String pharma_name = _edit_name.getText().toString().trim();
                String pharma_email = _edit_email.getText().toString().trim();
                String pharma_mobile = _edit_mobile.getText().toString().trim();
                String pharma_city = _edit_city.getText().toString().trim();

                if(pharma_name.equals("")) {
                    Toast.makeText(ViewOphthalVisitDetailsActivity.this, "Enter Pharmacy Names", Toast.LENGTH_SHORT).show();
                }
                else  if((pharma_mobile.equals("")) || (pharma_mobile.length() <10)) {
                    Toast.makeText(ViewOphthalVisitDetailsActivity.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendPharmaNewToServer(pharma_name,pharma_email,pharma_mobile,pharma_city);
                    addNew_contents.setVisibility(View.GONE);
                    list_contents.setVisibility(View.VISIBLE);
                }
            }
        });

        cancel_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm3 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(cancel_new_btn.getWindowToken(), 0);
                addNew_contents.setVisibility(View.GONE);
                list_contents.setVisibility(View.VISIBLE);
            }
        });

        final SearchView pharma_searchView = (SearchView) dialog.findViewById(R.id.searchview_diagnostic);
        ListView pharma_listview = (ListView) dialog.findViewById(R.id.diagnostic_list);

        final CustomPharmaCenterFilterAdapter pharmaAdapter = new CustomPharmaCenterFilterAdapter(ViewOphthalVisitDetailsActivity.this,
                R.layout.activity_listview, pharmaListArraylist);
        pharma_listview.setAdapter(pharmaAdapter);
        pharma_listview.setTextFilterEnabled(true);

        pharma_searchView.setIconifiedByDefault(false);
        pharma_searchView.setQueryHint("Search Pharmacy");
        pharma_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                pharma_searchView.setQuery("", false);
                pharma_searchView.clearFocus();
                pharma_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                    pharmaAdapter.filter(newText.toString());
                    // specialization_listview.clearTextFilter();
                } else {
                    //  specialization_listview.setFilterText(newText.toString());
                    pharmaAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        pharma_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                pharma_searchView.setQuery("", false);
                pharma_searchView.setIconified(false);
                pharma_searchView.clearFocus();
                return false;
            }
        });

        pharma_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PharmaCentreList myCustomAdapterItem = (PharmaCentreList) parent.getItemAtPosition(position);
              *//* Toast.makeText(getActivity(),  myCustomAdapterItem.getSymptomsName() + " added.",
                        Toast.LENGTH_SHORT)
                        .show();*//*
                pharma_searchView.setQuery("", false);
                pharma_searchView.setIconified(false);
                pharma_searchView.clearFocus();

                selectedPharmaArraylist = new ArrayList<>();
                selectedPharmaArraylist.add(new PharmaCentreList(myCustomAdapterItem.getPharmaId(),myCustomAdapterItem.getPharmaName()));

                sendPharmacyReferralToServer(selectedPharmaArraylist);
                dialog.dismiss();
            }
        });

        dialog.show();*/
    }

    private void sendPharmaNewToServer(final String pharma_name, final String pharma_email, final String pharma_mobile, final String pharma_city) {
      /*  pharmaListArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(ViewOphthalVisitDetailsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding New Pharmacy...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PHARMA_ADD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "pharma list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("pharma_details");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            pharmaListArraylist.add(new PharmaCentreList(jsonArray.getJSONObject(i).getInt("pharma_id"),
                                                    jsonArray.getJSONObject(i).getString("pharma_name"),jsonArray.getJSONObject(i).getString("pharma_city"),
                                                    jsonArray.getJSONObject(i).getString("pharma_state"),jsonArray.getJSONObject(i).getString("pharma_country"),
                                                    jsonArray.getJSONObject(i).getString("phrama_contact_person"),jsonArray.getJSONObject(i).getString("pharma_contact_num"),
                                                    jsonArray.getJSONObject(i).getString("pharma_email"),USER_ID, USER_LOGIN_TYPE));

                                        }

                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(pharmaListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearPharmaCentres();
                                            shareadPreferenceClass.setPharmaCentres(jsonDiagnoText);
                                        }

                                        preparePharmaCentreData(pharmaListArraylist);
                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(pharmaListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearPharmaCentres();
                                            shareadPreferenceClass.setPharmaCentres(jsonDiagnoText);
                                        }
                                        preparePharmaCentreData(pharmaListArraylist);
                                    }
                                }
                                progressDialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                map.put(APIClass.KEY_PHARMA_NAME, pharma_name);
                map.put(APIClass.KEY_PHARMA_EMAIL, pharma_email);
                map.put(APIClass.KEY_PHARMA_MOBILE, pharma_mobile);
                map.put(APIClass.KEY_PHARMA_CITY, pharma_city);
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(ViewOphthalVisitDetailsActivity.this).
                getRequestQueue();
        AppController.getInstance(ViewOphthalVisitDetailsActivity.this).addToRequestQueue(stringRequest);*/
    }

    private void sendPharmacyReferralToServer(final List<PharmaCentreList> selectedPharmaArraylist) {
      /*  new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(ViewOphthalVisitDetailsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {

                    JSONObject jsonObject = JSONParser.sendPharmaReferal(String.valueOf(PATIENT_ID), String.valueOf(EPISODE_ID),
                            selectedPharmaArraylist, USER_ID, USER_LOGIN_TYPE);

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                        if (jsonObject.getString("result").equals("success")) {
                            status = true;
                        } else {
                            status = false;
                        }
                    }
                    // return true;
                } catch (JSONException e) {
                    Log.i(Utils.TAG, "Error : " + e.getLocalizedMessage());
                    return false;
                }
                return status;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (progressDialog != null)
                    progressDialog.dismiss();
                Log.e(Utils.TAG, "aBoolean: " + String.valueOf(aBoolean));
                if (aBoolean) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewOphthalVisitDetailsActivity.this, R.style.CustomDialog);
                    alertDialogBuilder.setMessage("Referred to pharmacy successfully !!!. ");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(ViewOphthalVisitDetailsActivity.this, "Failed to send referal. \nTry later !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();*/
    }


    private void sendPrescriptionToPatient(final int send_presc_type) {       // 1- SMS, 2- EMAIL
      /*  new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(ViewOphthalVisitDetailsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {

                    JSONObject jsonObject = JSONParser.sendPrescToPatient(PATIENT_ID,EPISODE_ID,
                            USER_ID, USER_LOGIN_TYPE,send_presc_type, USER_NAME);

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                        if (jsonObject.getString("result").equals("success")) {
                            status = true;
                        } else {
                            status = false;
                        }
                    }
                    // return true;
                } catch (JSONException e) {
                    Log.i(Utils.TAG, "Error : " + e.getLocalizedMessage());
                    return false;
                }
                return status;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (progressDialog != null)
                    progressDialog.dismiss();
                Log.e(Utils.TAG, "aBoolean: " + String.valueOf(aBoolean));
                if (aBoolean) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewOphthalVisitDetailsActivity.this, R.style.CustomDialog);
                    alertDialogBuilder.setMessage("Prescription sent successfully !!!. ");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(ViewOphthalVisitDetailsActivity.this, "Failed to send prescription. \nTry later !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();*/
    }

}

