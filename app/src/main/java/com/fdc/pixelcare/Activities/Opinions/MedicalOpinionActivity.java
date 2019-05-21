package com.fdc.pixelcare.Activities.Opinions;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.Adapters.Opinions.ReportsImageAdapter;
import com.fdc.pixelcare.DataModel.FamilyMember;
import com.fdc.pixelcare.DataModel.ReportsImages;
import com.fdc.pixelcare.DataModel.SpecializationList;
import com.fdc.pixelcare.DataModel.UserAddress;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.Network.CustomVolleyRequestQueue;
import com.fdc.pixelcare.Network.NetworkUtil;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppController;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
import com.fdc.pixelcare.Views.CustomEditText;
import com.fdc.pixelcare.Views.CustomTextView;
import com.fdc.pixelcare.Views.CustomTextViewBold;
import com.fdc.pixelcare.multipleImageUpload.MedicalReportsActivity;
import com.fdc.pixelcare.parser.JSONParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by SALMA on 31/12/18.
 */

public class MedicalOpinionActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener, Response.ErrorListener  {
    public static final String REQUEST_TAG = "MedicalOpinionActivity";
    LinearLayout contact_layout_btn;
    ImageView contact_btn_image;
    boolean view_contact_status;
    CardView contact_cardview;

    TextInputLayout contactperston_TL, mobile_TL, email_TL, address_TL, city_TL, state_TL, country_TL, pincode_TL;
    CustomEditText _edt_contactperson, _edt_mobile, _edt_email, _edt_address, _edt_city, _edt_state, _edt_country, _edt_pincode;


    LinearLayout medical_layout_btn;
    ImageView medical_btn_image;
    boolean view_medical_status;
    CardView medical_cardview;

    LinearLayout detailed_layout_btn;
    ImageView detailed_btn_image;
    boolean view_detailed_status;
    CardView detailed_cardview;

    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int USER_ID, USER_LOGINTYPE, DOCTOR_ID;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION,  FAMILY_MEMBER_LIST, USER_ADDRESS_LIST, DOCTOR_NAME;

    Spinner patient_name_spinner;
    ImageView add_member_btn;

    RadioGroup genderGroup;
    RadioButton rb_male, rb_female, rb_trans;
    ArrayList<String> memberNameArray;
    ArrayList<FamilyMember> memberArraylist;
    ArrayList<UserAddress> addressArraylist;
    String PATIENT_NAME= "", PATIENT_GENDER = "0", GET_RELATIONSHIP_TYPE, PATIENT_DOB, PATIENT_AGE;
    int PATIENT_ID = 0;
    Calendar myCalendar;
    CustomTextView choose_dob;
    private RequestQueue mQueue;
    ProgressDialog pd;

    TextInputLayout age_TL, weight_TL;
    CustomEditText _edt_age, _edt_weight;
    RadioGroup hypertensionGroup, diabetesGroup;
    RadioButton rb_hyperYes, rb_hyperNo, rb_diabetesYes, rb_diabetesNo;
    String PATIENT_HYPERTENSION = "0", PATIENT_DIABETES = "0";

    TextInputLayout med_complaint_TL, med_brief_desc_TL, query_to_doc_TL;
    CustomEditText _edt_medcomplaint, _edt_med_brief_desc, _edt_query_to_doc;
    CustomTextViewBold submit_btn;

    LinearLayout reports_layout_btn;
    ImageView reports_btn_image;
    boolean view_reports_status;
    CardView reports_cardview;

    LinearLayout upload_reports_btn;
    RecyclerView reports_recyclerview;
    List<ReportsImages> reportImageList = new ArrayList<>();
    private ReportsImageAdapter reportImageAdapter;

    public ArrayList<String> REPORTS_PHOTOS = new ArrayList<String>();
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private String userChoosenTask;

    String CONTACT_PERSON, MOBILE_NUM, EMAIL_ID, ADDRESS, CITY, STATE, COUNTRY, AGE, WEIGHT, MEDICAL_COMPLAINT, BRIEF_DESCRIPTION, QUERY_DOCTOR;
    int SPECIALIZATION_ID = 0;
    String SPECIALIZATION_NAME = "";
    List<SpecializationList> specializationListArraylist;
    SpecializationList specialization;
    Spinner specialization_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_opinion);
        memberArraylist = new ArrayList<>();
        REPORTS_PHOTOS = new ArrayList<String>();
        reportImageList = new ArrayList<>();
        myCalendar = Calendar.getInstance();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            DOCTOR_ID = b.getInt("DOC_ID");
            DOCTOR_NAME = b.getString("DOC_NAME");
            setTitle(title);

            CONTACT_PERSON = b.getString("CONTACT_PERSON");
            MOBILE_NUM = b.getString("MOBILE_NUM");
            EMAIL_ID = b.getString("EMAIL_ID");
            ADDRESS = b.getString("ADDRESS");
            CITY = b.getString("CITY");
            STATE = b.getString("STATE");
            COUNTRY = b.getString("COUNTRY");
            PATIENT_NAME = b.getString("PATIENT_NAME");
            PATIENT_ID = b.getInt("PATIENT_ID");
            PATIENT_GENDER = b.getString("PATIENT_GENDER");
            AGE = b.getString("AGE");
            WEIGHT = b.getString("WEIGHT");
            PATIENT_HYPERTENSION = b.getString("PATIENT_HYPERTENSION");
            PATIENT_DIABETES = b.getString("PATIENT_DIABETES");
            SPECIALIZATION_ID = b.getInt("SPECIALIZATION_ID");
            SPECIALIZATION_NAME = b.getString("SPECIALIZATION_NAME");
            MEDICAL_COMPLAINT = b.getString("MEDICAL_COMPLAINT");
            BRIEF_DESCRIPTION = b.getString("BRIEF_DESCRIPTION");
            QUERY_DOCTOR = b.getString("QUERY_DOCTOR");
            REPORTS_PHOTOS = b.getStringArrayList("REPORTS_PHOTOS");
        }

        if (REPORTS_PHOTOS != null && REPORTS_PHOTOS.size() > 0) {
            Log.d(AppUtils.TAG + "PH SIZE:", String.valueOf(REPORTS_PHOTOS.size()));
            ArrayList aList = new ArrayList(Arrays.asList(REPORTS_PHOTOS.toString().substring(1, REPORTS_PHOTOS.toString().length()-1).toString().split(",")));
            for(int i=0;i<aList.size();i++)
            {
                System.out.println(AppUtils.TAG+" -->"+aList.get(i));
                reportImageList.add(new ReportsImages(aList.get(i).toString()));
            }

        }

        shareadPreferenceClass = new ShareadPreferenceClass(MedicalOpinionActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(MedicalOpinionActivity.this);

        if(sharedPreferences !=null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE  = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);
            FAMILY_MEMBER_LIST = sharedPreferences.getString(MHConstants.PREF_FAMILY_MEMBERS, "");
            USER_ADDRESS_LIST = sharedPreferences.getString(MHConstants.PREF_USER_ADDRESS, "");


            Log.d(AppUtils.TAG , " *********** MedicalOpinionActivity **************** ");
            Log.d(AppUtils.TAG + " UserID: ", String.valueOf(USER_ID));
            Log.d(AppUtils.TAG + " name: ", USER_NAME);
            Log.d(AppUtils.TAG + " email: ", USER_EMAIL);
            Log.d(AppUtils.TAG + " MOBILE: ", USER_MOBILE);
            Log.d(AppUtils.TAG + " Location: ", USER_LOCATION);
            Log.d(AppUtils.TAG + " DOCTOR_ID: ", String.valueOf(DOCTOR_ID));
            Log.d(AppUtils.TAG + " DOCTOR_NAME: ", DOCTOR_NAME);
        }

        initializationView();
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

    private void initializationView() {
        view_contact_status = false;
        view_medical_status = false;
        view_detailed_status = false;
        view_reports_status = false;
        memberNameArray = new ArrayList<String>();
        addressArraylist = new ArrayList<>();

        permissionStatus = getSharedPreferences("permissionStatus", Context.MODE_PRIVATE);


        Gson gson = new Gson();
        if(FAMILY_MEMBER_LIST != null && !FAMILY_MEMBER_LIST.isEmpty() ) {
            memberArraylist = new ArrayList<>();
            memberArraylist = gson.fromJson(FAMILY_MEMBER_LIST, new TypeToken<List<FamilyMember>>() {
            }.getType());
            Log.d(AppUtils.TAG +" memberSize: ", String.valueOf(memberArraylist.size()));

            for (int i = 0; i < memberArraylist.size(); i++) {
              //  memberNameArray.add(memberArraylist.get(i).getMemberName());
                if(USER_NAME.equalsIgnoreCase(memberArraylist.get(i).getMemberName())) {
                    memberNameArray.add(memberArraylist.get(i).getMemberName()+" (Self)");
                }
                else {
                    memberNameArray.add(memberArraylist.get(i).getMemberName());
                }
            }
        }

        Gson gson1 = new Gson();
        if(USER_ADDRESS_LIST != null && !USER_ADDRESS_LIST.isEmpty() ) {
            addressArraylist = new ArrayList<>();
            addressArraylist = gson1.fromJson(USER_ADDRESS_LIST, new TypeToken<List<UserAddress>>() {
            }.getType());
            Log.d(AppUtils.TAG +" addressSize: ", String.valueOf(addressArraylist.size()));
        }

        contact_layout_btn = (LinearLayout) findViewById(R.id.medop_contact_btn);
        contact_layout_btn.setOnClickListener(this);
        contact_btn_image = (ImageView)  findViewById(R.id.medop_contact_img);
        contact_cardview = (CardView) findViewById(R.id.medop_contact_cardview);
//        contact_cardview.setVisibility(View.VISIBLE);
//        view_contact_status = true;

        contactperston_TL = (TextInputLayout) findViewById(R.id.medop_contact_TL);
        _edt_contactperson = (CustomEditText) findViewById(R.id.medop_contact);
        mobile_TL = (TextInputLayout) findViewById(R.id.medop_mobile_TL);
        _edt_mobile = (CustomEditText) findViewById(R.id.medop_mobile);
        email_TL  = (TextInputLayout) findViewById(R.id.medop_email_TL);
        _edt_email = (CustomEditText) findViewById(R.id.medop_email);
        address_TL = (TextInputLayout) findViewById(R.id.medop_address_TL);
        _edt_address = (CustomEditText) findViewById(R.id.medop_address);
        city_TL = (TextInputLayout) findViewById(R.id.medop_city_TL);
        _edt_city = (CustomEditText) findViewById(R.id.medop_city);
        state_TL = (TextInputLayout) findViewById(R.id.medop_state_TL);
        _edt_state = (CustomEditText) findViewById(R.id.medop_state);
        country_TL = (TextInputLayout) findViewById(R.id.medop_country_TL);
        _edt_country = (CustomEditText) findViewById(R.id.medop_country);
        pincode_TL = (TextInputLayout) findViewById(R.id.medop_pincode_TL);
        _edt_pincode  = (CustomEditText) findViewById(R.id.medop_pincode);

        _edt_pincode.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(s.length() == 6) {
                    _edt_city.setText("");
                    _edt_state.setText("");
                    _edt_country.setText("");
                    new getJsonAddress().execute(s.toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        medical_layout_btn = (LinearLayout) findViewById(R.id.medop_medical_btn);
        medical_layout_btn.setOnClickListener(this);
        medical_btn_image = (ImageView)  findViewById(R.id.medop_medical_img);
        medical_cardview = (CardView) findViewById(R.id.medop_medical_cardview);
      //  medical_cardview.setVisibility(View.GONE);

        medical_cardview.setVisibility(View.VISIBLE);
        view_medical_status = true;

        detailed_layout_btn = (LinearLayout) findViewById(R.id.medop_detaildesc_btn);
        detailed_layout_btn.setOnClickListener(this);
        detailed_btn_image = (ImageView)  findViewById(R.id.medop_detaildesc_img);
        detailed_cardview = (CardView) findViewById(R.id.medop_detaildesc_cardview);
        detailed_cardview.setVisibility(View.GONE);


        patient_name_spinner = (Spinner) findViewById(R.id.medop_patname_spinner);
        add_member_btn = (ImageView)  findViewById(R.id.medop_add_member);
        add_member_btn.setOnClickListener(this);

        genderGroup = (RadioGroup)  findViewById(R.id.medop_radioGroupGender);
        rb_male = (RadioButton) findViewById(R.id.medop_radioMale);
        rb_female = (RadioButton) findViewById(R.id.medop_radioFemale);
        rb_trans = (RadioButton) findViewById(R.id.medop_radioTrans);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(MedicalOpinionActivity.this, R.layout.spinner_text, memberNameArray );
        locationAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        patient_name_spinner.setAdapter(locationAdapter);

        Log.d(AppUtils.TAG + " PATIENT_NAME: ", PATIENT_NAME + " ID: "+ String.valueOf(PATIENT_ID));

        if(PATIENT_NAME != null && !PATIENT_NAME.isEmpty()) {
            Log.d(AppUtils.TAG + " PATIENT_NAME: ", String.valueOf(PATIENT_NAME));
            int spinnerPosition = locationAdapter.getPosition(PATIENT_NAME);
            patient_name_spinner.setSelection(spinnerPosition);
        }
        else {
            Log.d(AppUtils.TAG , " PATIENT_NAME empty ");
            patient_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    Log.d(AppUtils.TAG, " patName: "+ (String) parent.getItemAtPosition(position));
                    String specialization_name = (String) parent.getItemAtPosition(position);
                    PATIENT_NAME = (String) parent.getItemAtPosition(position);
                    PATIENT_ID = memberArraylist.get(position).getMemberid();
                    PATIENT_GENDER = memberArraylist.get(position).getGender();
                    PATIENT_AGE = memberArraylist.get(position).getMemberAge();
                    Log.d(AppUtils.TAG, " patId " + PATIENT_ID);

                    if(PATIENT_GENDER.equals("1")) {
                        rb_male.setChecked(true);
                    }
                    else if(PATIENT_GENDER.equals("2")) {
                        rb_female.setChecked(true);
                    }
                    else if(PATIENT_GENDER.equals("3")) {
                        rb_trans.setChecked(true);
                    }
                    else if(PATIENT_GENDER.equals("0")){
                        rb_male.setChecked(false);
                        rb_female.setChecked(false);
                        rb_trans.setChecked(false);
                    }
                    else {
                        rb_male.setChecked(false);
                        rb_female.setChecked(false);
                        rb_trans.setChecked(false);
                    }

                    _edt_age.setText(PATIENT_AGE);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    PATIENT_NAME = "--Select--";
                    PATIENT_ID = 0;
                    PATIENT_GENDER = "0";
                    Log.d(AppUtils.TAG, "SpecIdNothing " + PATIENT_ID);
                }
            });
        }



        hypertensionGroup = (RadioGroup)  findViewById(R.id.medop_radioGroupHyper);
        rb_hyperYes = (RadioButton) findViewById(R.id.medop_radiohyperYes);
        rb_hyperNo = (RadioButton) findViewById(R.id.medop_radiohyperNo);

        diabetesGroup = (RadioGroup)  findViewById(R.id.medop_radioGroupDiabetes);
        rb_diabetesYes = (RadioButton) findViewById(R.id.medop_radiodianetesYes);
        rb_diabetesNo = (RadioButton) findViewById(R.id.medop_radiodiabetesNo);

        age_TL = (TextInputLayout) findViewById(R.id.medop_age_TL);
        _edt_age = (CustomEditText) findViewById(R.id.medop_age);
        weight_TL = (TextInputLayout) findViewById(R.id.medop_weight_TL);
        _edt_weight = (CustomEditText) findViewById(R.id.medop_weight);

        med_complaint_TL = (TextInputLayout) findViewById(R.id.medop_medcomplaint_TL);
        _edt_medcomplaint = (CustomEditText) findViewById(R.id.medop_medcomplaint);
        med_brief_desc_TL = (TextInputLayout) findViewById(R.id.medop_meddesc_TL);
        _edt_med_brief_desc = (CustomEditText) findViewById(R.id.medop_medmeddesc);
        query_to_doc_TL = (TextInputLayout) findViewById(R.id.medop_query_TL);
        _edt_query_to_doc = (CustomEditText) findViewById(R.id.medop_query);

        submit_btn = (CustomTextViewBold) findViewById(R.id.medop_submit);
        submit_btn.setOnClickListener(this);

        reports_layout_btn = (LinearLayout) findViewById(R.id.medop_reports_btn);
        reports_layout_btn.setOnClickListener(this);
        reports_btn_image = (ImageView)  findViewById(R.id.medop_reports_img);
        reports_cardview = (CardView) findViewById(R.id.medop_reports_cardview);
        reports_cardview.setVisibility(View.GONE);

        upload_reports_btn = (LinearLayout) findViewById(R.id.medop_reports_upload);
        upload_reports_btn.setOnClickListener(this);
        reports_recyclerview = (RecyclerView) findViewById(R.id.medop_reports_recyclerview);

        reportImageAdapter=new ReportsImageAdapter(MedicalOpinionActivity.this, reportImageList, REPORTS_PHOTOS, "ADD");
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MedicalOpinionActivity.this, LinearLayoutManager.HORIZONTAL, false);
        reports_recyclerview.setLayoutManager(horizontalLayoutManagaer);
        reports_recyclerview.setAdapter(reportImageAdapter);
        Log.d(AppUtils.TAG + "reportsList:", String.valueOf(reportImageList.size()));

        specialization_spinner = (Spinner) findViewById(R.id.medop_spec_spinner);

        if (NetworkUtil.getConnectivityStatusString(MedicalOpinionActivity.this).equalsIgnoreCase("enabled")) {
            BackgroundTask task = new BackgroundTask(MedicalOpinionActivity.this);
            task.execute();
        } else {
            AppUtils.showCustomAlertMessage(MedicalOpinionActivity.this, MHConstants.INTERNET, MHConstants.INTERNET_CHECK, "OK", null, null);
        }

        if(PATIENT_GENDER != null && !PATIENT_GENDER.isEmpty() ) {
            Log.d(AppUtils.TAG + " PATIENT_GENDER: ", String.valueOf(PATIENT_GENDER));
            if(PATIENT_GENDER.equals("1")) {
                rb_male.setChecked(true);
            }
            else if(PATIENT_GENDER.equals("2")) {
                rb_female.setChecked(true);
            }
            else if(PATIENT_GENDER.equals("3")) {
                rb_trans.setChecked(true);
            }
            else if(PATIENT_GENDER.equals("0")){
                rb_male.setChecked(false);
                rb_female.setChecked(false);
                rb_trans.setChecked(false);
            }
            else {
                rb_male.setChecked(false);
                rb_female.setChecked(false);
                rb_trans.setChecked(false);
            }
        }
        else  {
            Log.d(AppUtils.TAG , " PATIENT_GENDER: EMPTY");
        }

        if(PATIENT_HYPERTENSION != null && !PATIENT_HYPERTENSION.isEmpty() ) {
            Log.d(AppUtils.TAG + " PATIENT_GENDER: ", String.valueOf(PATIENT_GENDER));
            if(PATIENT_HYPERTENSION.equals("1")) {
                rb_hyperYes.setChecked(true);
            }
            else if(PATIENT_HYPERTENSION.equals("2")) {
                rb_hyperNo.setChecked(true);
            }
            else if(PATIENT_HYPERTENSION.equals("0")){
                rb_hyperYes.setChecked(false);
                rb_hyperNo.setChecked(false);
            }
            else {
                rb_hyperYes.setChecked(false);
                rb_hyperNo.setChecked(false);
            }
        }

        if(PATIENT_DIABETES != null && !PATIENT_DIABETES.isEmpty() ) {
            Log.d(AppUtils.TAG + " PATIENT_GENDER: ", String.valueOf(PATIENT_GENDER));
            if(PATIENT_DIABETES.equals("1")) {
                rb_diabetesYes.setChecked(true);
            }
            else if(PATIENT_HYPERTENSION.equals("2")) {
                rb_diabetesNo.setChecked(true);
            }
            else if(PATIENT_HYPERTENSION.equals("0")){
                rb_diabetesYes.setChecked(false);
                rb_diabetesNo.setChecked(false);
            }
            else {
                rb_diabetesYes.setChecked(false);
                rb_diabetesNo.setChecked(false);
            }
        }

        if(CONTACT_PERSON != null && !CONTACT_PERSON.isEmpty()) {
            _edt_contactperson.setText(CONTACT_PERSON);
            _edt_contactperson.setSelection(_edt_contactperson.getText().length());
        }
        else {
            _edt_contactperson.setText(USER_NAME);
            _edt_contactperson.setSelection(_edt_contactperson.getText().length());
        }

        if(MOBILE_NUM != null && !MOBILE_NUM.isEmpty()) {
            _edt_mobile.setText(MOBILE_NUM);
        }
        else {
            _edt_mobile.setText(USER_MOBILE);
        }

        if(EMAIL_ID != null && !EMAIL_ID.isEmpty()) {
            _edt_email.setText(EMAIL_ID);
        }
        else {
            _edt_email.setText(USER_EMAIL);
        }

        if(ADDRESS != null && !ADDRESS.isEmpty()) {
            _edt_address.setText(ADDRESS);
        }
        else {
            if(addressArraylist.size() > 0) {
                _edt_address.setText(addressArraylist.get(0).getAddressName());
            }
        }

        if(CITY != null && !CITY.isEmpty()) {
            _edt_city.setText(CITY);
        }
        else {
            if(addressArraylist.size() > 0) {
                _edt_city.setText(addressArraylist.get(0).getCity());
            }
        }

        if(STATE != null && !STATE.isEmpty()) {
            _edt_state.setText(STATE);
        }
        else {
            if(addressArraylist.size() > 0) {
                _edt_state.setText(addressArraylist.get(0).getState());
            }
        }

        if(COUNTRY != null && !COUNTRY.isEmpty()) {
            _edt_country.setText(COUNTRY);
        }
        else {
            if(addressArraylist.size() > 0) {
                _edt_country.setText(addressArraylist.get(0).getCountry());
            }
        }

        if(MEDICAL_COMPLAINT != null && !MEDICAL_COMPLAINT.isEmpty()) {
            _edt_medcomplaint.setText(MEDICAL_COMPLAINT);
        }

        if(BRIEF_DESCRIPTION != null && !BRIEF_DESCRIPTION.isEmpty()) {
            _edt_med_brief_desc.setText(BRIEF_DESCRIPTION);
        }

        if(QUERY_DOCTOR != null && !QUERY_DOCTOR.isEmpty()) {
            _edt_query_to_doc.setText(QUERY_DOCTOR);
        }

        if(AGE != null && !AGE.isEmpty()) {
            _edt_age.setText(AGE);
        }

        if(WEIGHT != null && !WEIGHT.isEmpty()) {
            _edt_weight.setText(WEIGHT);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.medop_contact_btn:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(contact_layout_btn.getWindowToken(), 0);

                if(view_contact_status == false) {
                    contact_cardview.setVisibility(View.VISIBLE);
                    contact_btn_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_contact_status = true;
                }
                else  if(view_contact_status == true) {
                    contact_cardview.setVisibility(View.GONE);
                    contact_btn_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_contact_status = false;
                }
                break;
            case R.id.medop_medical_btn:
                InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(medical_layout_btn.getWindowToken(), 0);

                if(view_medical_status == false) {
                    medical_cardview.setVisibility(View.VISIBLE);
                    medical_btn_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_medical_status = true;
                }
                else  if(view_medical_status == true) {
                    medical_cardview.setVisibility(View.GONE);
                    medical_btn_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_medical_status = false;
                }
                break;
            case R.id.medop_detaildesc_btn:
                InputMethodManager imm2 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(detailed_layout_btn.getWindowToken(), 0);

                if(view_detailed_status == false) {
                    detailed_cardview.setVisibility(View.VISIBLE);
                    detailed_btn_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_detailed_status = true;
                }
                else  if(view_detailed_status == true) {
                    detailed_cardview.setVisibility(View.GONE);
                    detailed_btn_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_detailed_status = false;
                }
                break;
            case R.id.medop_add_member:
                PATIENT_GENDER = "0";
                customAddFamilyMember();
                break;
            case R.id.medop_reports_btn:
                InputMethodManager imm3 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(reports_layout_btn.getWindowToken(), 0);

                if(view_reports_status == false) {
                    reports_cardview.setVisibility(View.VISIBLE);
                    reports_btn_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_reports_status = true;
                }
                else  if(view_reports_status == true) {
                    reports_cardview.setVisibility(View.GONE);
                    reports_btn_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_reports_status = false;
                }
                break;
            case R.id.medop_reports_upload:
                InputMethodManager imm4 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm4.hideSoftInputFromWindow(upload_reports_btn.getWindowToken(), 0);

                requestAttachmentPermissions();
                break;

            case R.id.medop_submit:
                InputMethodManager imm5 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm5.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);

                validateOpinionForm();
                break;
        }
    }

    private void customAddFamilyMember() {
        final Dialog dialog = new Dialog(MedicalOpinionActivity.this,  R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_member);
        dialog.setTitle("Add New Member");

        ImageView cancel_btn  = (ImageView) dialog.findViewById(R.id.addmember_cancel);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final TextInputLayout name_TL = (TextInputLayout) dialog.findViewById(R.id.addmember_name_TL);
        final CustomEditText _edt_name = (CustomEditText)  dialog.findViewById(R.id.addmember_name);
        final CustomTextViewBold submit_btn = (CustomTextViewBold)  dialog.findViewById(R.id.addmember_submit);
        Spinner spinner_relation = (Spinner) dialog.findViewById(R.id.addmember_relationSpinner);
        choose_dob = (CustomTextView)  dialog.findViewById(R.id.addmember_dob);
        final RadioGroup radioGenderGroup = (RadioGroup) dialog.findViewById(R.id.addmember_radioGroupGender);

        ArrayAdapter<String> spinnerCountShoesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.relationship_types));
        spinner_relation.setAdapter(spinnerCountShoesArrayAdapter);

        spinner_relation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //    Toast.makeText(BookAppointmentActivity.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                GET_RELATIONSHIP_TYPE = adapterView.getSelectedItem().toString().trim();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        choose_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_dob.setText("");
                new DatePickerDialog(MedicalOpinionActivity.this, dob_date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int checkedRadioButtonId = radioGenderGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {   // No items Selected
                    PATIENT_GENDER = "0";
                }
                else{
                    if (checkedRadioButtonId == R.id.addmember_radioMale) {
                        PATIENT_GENDER = "1";
                    }
                    else if (checkedRadioButtonId == R.id.addmember_radioFemale) {
                        PATIENT_GENDER = "2";
                    }
                    else if (checkedRadioButtonId == R.id.addmember_radioTrans) {
                        PATIENT_GENDER = "3";
                    }
                    else {
                        PATIENT_GENDER = "0";
                    }
                }

                if(choose_dob.getText().toString().equals("--Select Date--")) {
                    PATIENT_DOB = "";
                }
                else {
                    PATIENT_DOB = choose_dob.getText().toString();
                }

                if(_edt_name.getText().toString().equals("")) {
                    name_TL.setError("Enter Name");
                }
                else if(PATIENT_GENDER.equals("0")) {
                    Toast.makeText(MedicalOpinionActivity.this, "Select Gender !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm1.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);

                    submitAddMemberToServer(_edt_name.getText().toString().trim(), PATIENT_GENDER, GET_RELATIONSHIP_TYPE, PATIENT_DOB);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    DatePickerDialog.OnDateSetListener dob_date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Calendar dareSelected = Calendar.getInstance();
            dareSelected.set(year, monthOfYear, dayOfMonth);
            Calendar currentDate = Calendar.getInstance();

            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            choose_dob.setText(sdf.format(dareSelected.getTime()));
        }
    };

    private void submitAddMemberToServer(final String patient_name, final String get_gender, final String ger_relationship_type, final String patient_dob) {
        Log.d(AppUtils.TAG, " patient_name " + patient_name);
        Log.d(AppUtils.TAG, " get_gender " + get_gender);
        Log.d(AppUtils.TAG, " ger_relationship_type " + ger_relationship_type);
        Log.d(AppUtils.TAG, " patient_dob " + patient_dob);

        // Instantiate the RequestQueue.
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        StringRequest postRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_ADD_FAMILY_MEMBER,
                this,this ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(APIParam.KEY_API, APIClass.API_KEY);
                params.put(APIParam.KEY_MEMBER_NAME, patient_name);
                params.put(APIParam.KEY_MEMBER_GENDER, get_gender);
                params.put(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID));

                if(ger_relationship_type != null) {
                    params.put(APIParam.KEY_MEMBER_RELATIONSHIP, ger_relationship_type);
                }

                if(patient_dob != null) {
                    params.put(APIParam.KEY_MEMBER_DOB, patient_dob);
                }

                return params;
            }
        };

        postRequest.setTag(REQUEST_TAG);
        mQueue.add(postRequest);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        VolleyLog.e(AppUtils.TAG +" Error: ", error.getMessage());
       /* if(pd!=null || pd.isShowing())
            pd.dismiss();*/
        AppUtils.showCustomAlertMessage(MedicalOpinionActivity.this, "Warning !!!",error.getMessage(), "OK", null, null);

    }

    @Override
    public void onResponse(Object response) {
        Log.d(AppUtils.TAG +" Result: ", response.toString());
        String staus_res = null, opinion_result = null;

      /*  if(pd!=null || pd.isShowing())
            pd.dismiss();*/

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(String.valueOf(response));

            if (jsonObject.has("result")) {
                staus_res = jsonObject.getString("result");
            }

            if (jsonObject.has("result_opinion")) {
                opinion_result = jsonObject.getString("result_opinion");
            }

            if(staus_res != null && !staus_res.isEmpty()) {
                if(staus_res.equalsIgnoreCase("failure")) {
                    Toast.makeText(MedicalOpinionActivity.this, "Add Member \n"+jsonObject.getString("err_msg"), Toast.LENGTH_SHORT).show();
                }
                else {
                    AppUtils.showCustomSuccessMessage(MedicalOpinionActivity.this, "Add Member", "Member added successfully !!!", "OK", null, null);

                    JSONArray jsonArray1 = jsonObject.getJSONArray("family_details");
                    if(jsonArray1.length() > 0 ) {
                        memberArraylist.clear();
                        memberArraylist = new ArrayList<FamilyMember>();
                        memberNameArray.clear();
                        memberNameArray = new ArrayList<String>();
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            memberArraylist.add(new FamilyMember(jsonArray1.getJSONObject(i).getInt("member_id"),jsonArray1.getJSONObject(i).getInt("user_id"),
                                    jsonArray1.getJSONObject(i).getString("gender"),jsonArray1.getJSONObject(i).getString("member_name"),
                                    jsonArray1.getJSONObject(i).getString("relationship"),jsonArray1.getJSONObject(i).getString("dob"),
                                    jsonArray1.getJSONObject(i).getString("age"),jsonArray1.getJSONObject(i).getString("member_photo")));
                        }
                        //Set the values Family Member
                        Gson gson = new Gson();
                        String jsonText = gson.toJson(memberArraylist);
                        if (sharedPreferences != null) {
                            shareadPreferenceClass.clearFamilyMemberLists();
                            shareadPreferenceClass.setFamilyMemberList(jsonText);
                        }
                        Log.d(AppUtils.TAG +" size: ", String.valueOf(memberArraylist.size()));
                        for (int i = 0; i < memberArraylist.size(); i++) {
                           // memberNameArray.add(memberArraylist.get(i).getMemberName());
                            if(USER_NAME.equalsIgnoreCase(memberArraylist.get(i).getMemberName())) {
                                memberNameArray.add(memberArraylist.get(i).getMemberName()+" (Self)");
                            }
                            else {
                                memberNameArray.add(memberArraylist.get(i).getMemberName());
                            }
                        }

                        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(MedicalOpinionActivity.this, R.layout.spinner_text, memberNameArray );
                        locationAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                        patient_name_spinner.setAdapter(locationAdapter);
                    }
                }
            }

            if(opinion_result != null && !opinion_result.isEmpty()) {
                if(opinion_result.equalsIgnoreCase("failure")) {
                    Toast.makeText(MedicalOpinionActivity.this, "Medical Opinion \n"+jsonObject.getString("err_msg"), Toast.LENGTH_SHORT).show();
                }
                else {
                    final Dialog dialog = new Dialog(MedicalOpinionActivity.this, R.style.CustomDialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.custom_success);

                    CustomTextView text = (CustomTextView) dialog.findViewById(R.id.text_success_dialog);
                    text.setText("Medical opinion sent successfully !!!");

                    Button dialogButton = (Button) dialog.findViewById(R.id.text_success_submit);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            finish();
                        }
                    });

                    dialog.show();
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    private void requestAttachmentPermissions() {
        if (ActivityCompat.checkSelfPermission(MedicalOpinionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MedicalOpinionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MedicalOpinionActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MedicalOpinionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MedicalOpinionActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                AlertDialog.Builder builder1 = builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(MedicalOpinionActivity.this, "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(MedicalOpinionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,true);
            editor.commit();

        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }

    private void proceedAfterPermission() {
        //   Toast.makeText(getActivity(), "We got the Storage Permission", Toast.LENGTH_LONG).show();
        selectAttachementFromDevice();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MedicalOpinionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(MedicalOpinionActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(MedicalOpinionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(MedicalOpinionActivity.this,"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(MedicalOpinionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void selectAttachementFromDevice() {
        final CharSequence[] items = {"Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MedicalOpinionActivity.this);
        builder.setTitle("Upload Attachments");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";

                    CONTACT_PERSON = _edt_contactperson.getText().toString().trim();
                    MOBILE_NUM = _edt_mobile.getText().toString().trim();
                    EMAIL_ID = _edt_email.getText().toString().trim();
                    ADDRESS = _edt_address.getText().toString().trim();
                    CITY = _edt_city.getText().toString().trim();
                    STATE = _edt_state.getText().toString().trim();
                    COUNTRY = _edt_country.getText().toString().trim();
                    AGE = _edt_age.getText().toString().trim();
                    WEIGHT = _edt_weight.getText().toString().trim();
                    MEDICAL_COMPLAINT = _edt_medcomplaint.getText().toString().trim();
                    BRIEF_DESCRIPTION = _edt_med_brief_desc.getText().toString().trim();
                    QUERY_DOCTOR = _edt_query_to_doc.getText().toString().trim();

                    int checkedRadioButtonIdGender = genderGroup.getCheckedRadioButtonId();       // 1 - Male, 2 - Female, 3 - other
                    if (checkedRadioButtonIdGender == -1) {   // No items Selected
                        PATIENT_GENDER = "0";
                    }
                    else{
                        if (checkedRadioButtonIdGender == R.id.medop_radioMale) {
                            PATIENT_GENDER = "1";
                        }
                        else if (checkedRadioButtonIdGender == R.id.medop_radioFemale) {
                            PATIENT_GENDER = "2";
                        }
                        else if (checkedRadioButtonIdGender == R.id.medop_radioTrans) {
                            PATIENT_GENDER = "3";
                        }
                        else {
                            PATIENT_GENDER = "0";
                        }
                    }

                    int checkedRadioButtonIdHyper = hypertensionGroup.getCheckedRadioButtonId();       // 1 - Yes, 2 - No
                    if (checkedRadioButtonIdHyper == -1) {   // No items Selected
                        PATIENT_HYPERTENSION = "0";
                    }
                    else{
                        if (checkedRadioButtonIdHyper == R.id.medop_radiohyperYes) {
                            PATIENT_HYPERTENSION = "1";
                        }
                        else if (checkedRadioButtonIdHyper == R.id.medop_radiohyperNo) {
                            PATIENT_HYPERTENSION = "2";
                        }
                        else {
                            PATIENT_HYPERTENSION = "0";
                        }
                    }

                    int checkedRadioButtonIdDiabetes = diabetesGroup.getCheckedRadioButtonId();       // 1 - Yes, 2 - No
                    if (checkedRadioButtonIdDiabetes == -1) {   // No items Selected
                        PATIENT_DIABETES = "0";
                    }
                    else{
                        if (checkedRadioButtonIdDiabetes == R.id.medop_radiodianetesYes) {
                            PATIENT_DIABETES = "1";
                        }
                        else if (checkedRadioButtonIdDiabetes == R.id.medop_radiodiabetesNo) {
                            PATIENT_DIABETES = "2";
                        }
                        else {
                            PATIENT_DIABETES = "0";
                        }
                    }

                    Intent i1 = new Intent(MedicalOpinionActivity.this, MedicalReportsActivity.class);
                    i1.putExtra("title","Upload Reports");
                    i1.putExtra("CONTACT_PERSON",CONTACT_PERSON);
                    i1.putExtra("MOBILE_NUM",MOBILE_NUM);
                    i1.putExtra("EMAIL_ID",EMAIL_ID);
                    i1.putExtra("ADDRESS",ADDRESS);
                    i1.putExtra("CITY",CITY);
                    i1.putExtra("STATE",STATE);
                    i1.putExtra("COUNTRY",COUNTRY);
                    i1.putExtra("PATIENT_NAME",PATIENT_NAME);
                    i1.putExtra("PATIENT_ID",PATIENT_ID);
                    i1.putExtra("PATIENT_GENDER",PATIENT_GENDER);
                    i1.putExtra("AGE",AGE);
                    i1.putExtra("WEIGHT",WEIGHT);
                    i1.putExtra("PATIENT_HYPERTENSION",PATIENT_HYPERTENSION);
                    i1.putExtra("PATIENT_DIABETES",PATIENT_DIABETES);
                    i1.putExtra("SPECIALIZATION_ID",SPECIALIZATION_ID);
                    i1.putExtra("SPECIALIZATION_NAME",SPECIALIZATION_NAME);
                    i1.putExtra("MEDICAL_COMPLAINT",MEDICAL_COMPLAINT);
                    i1.putExtra("BRIEF_DESCRIPTION",BRIEF_DESCRIPTION);
                    i1.putExtra("QUERY_DOCTOR",QUERY_DOCTOR);
                    i1.putStringArrayListExtra("REPORTS_PHOTOS",REPORTS_PHOTOS);
                    startActivity(i1);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        public BackgroundTask(Activity activity) {
            progressDialog = new ProgressDialog(MedicalOpinionActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
            progressDialog.setIndeterminate(true);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading Specialization...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if ((progressDialog != null) || (progressDialog.isShowing())) {
                progressDialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            specializationListArraylist = new ArrayList<>();
            int socketTimeout = 10000; // 10 seconds.
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_SPECIALIZATION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(AppUtils.TAG, response.toString());
                            if (response != null) {
                                JSONObject jsonObject = null;
                                JSONArray jsonArray = null;
                                ArrayList<String> specializationArray = new ArrayList<String>();
                                try {
                                    jsonObject = new JSONObject(response);
                                    String staus_res = jsonObject.getString("status");
                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray = jsonObject.getJSONArray("specialization_details");
                                        if (jsonArray.length() > 0) {
                                            specializationArray.add("--Select--");
                                            specialization = new SpecializationList(0, "--Select--", 0, "0");
                                            specializationListArraylist.add(specialization);
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                specialization = new SpecializationList(jsonArray.getJSONObject(i).getInt("spec_id"), jsonArray.getJSONObject(i).getString("spec_name"), 0, "0");
                                                specializationListArraylist.add(specialization);
                                                specializationArray.add(jsonArray.getJSONObject(i).getString("spec_name"));
                                            }
                                        }


                                        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(MedicalOpinionActivity.this, R.layout.spinner_text, specializationArray);
                                        locationAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                                        specialization_spinner.setAdapter(locationAdapter);

                                        if(SPECIALIZATION_NAME != null && !SPECIALIZATION_NAME.isEmpty()) {
                                            int spinnerPosition = locationAdapter.getPosition(SPECIALIZATION_NAME);
                                            specialization_spinner.setSelection(spinnerPosition);
                                        }
                                        else {
                                            specialization_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view,
                                                                           int position, long id) {
                                                    Log.d(AppUtils.TAG, "specName: " + (String) parent.getItemAtPosition(position));
                                                    String specialization_name = (String) parent.getItemAtPosition(position);
                                                    SPECIALIZATION_NAME = (String) parent.getItemAtPosition(position);
                                                    SPECIALIZATION_ID = specializationListArraylist.get(position).getSpecializationId();
                                                    Log.d(AppUtils.TAG, "SpecId " + SPECIALIZATION_ID);

                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {
                                                    SPECIALIZATION_NAME = "--Select--";
                                                    SPECIALIZATION_ID = 0;
                                                    Log.d(AppUtils.TAG, "SpecIdNothing " + SPECIALIZATION_ID);
                                                }
                                            });
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(APIParam.KEY_API, APIClass.API_KEY);
                    return map;
                }
            };
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = AppController.getInstance(MedicalOpinionActivity.this).
                    getRequestQueue();
            AppController.getInstance(MedicalOpinionActivity.this).addToRequestQueue(stringRequest);

            return null;
        }
    }

    private void validateOpinionForm() {
        CONTACT_PERSON = _edt_contactperson.getText().toString().trim();
        MOBILE_NUM = _edt_mobile.getText().toString().trim();
        EMAIL_ID = _edt_email.getText().toString().trim();
        ADDRESS = _edt_address.getText().toString().trim();
        CITY = _edt_city.getText().toString().trim();
        STATE = _edt_state.getText().toString().trim();
        COUNTRY = _edt_country.getText().toString().trim();
        AGE = _edt_age.getText().toString().trim();
        WEIGHT = _edt_weight.getText().toString().trim();
        MEDICAL_COMPLAINT = _edt_medcomplaint.getText().toString().trim();
        BRIEF_DESCRIPTION = _edt_med_brief_desc.getText().toString().trim();
        QUERY_DOCTOR = _edt_query_to_doc.getText().toString().trim();

        int checkedRadioButtonIdGender = genderGroup.getCheckedRadioButtonId();       // 1 - Male, 2 - Female, 3 - other
        if (checkedRadioButtonIdGender == -1) {   // No items Selected
            PATIENT_GENDER = "0";
        }
        else{
            if (checkedRadioButtonIdGender == R.id.medop_radioMale) {
                PATIENT_GENDER = "1";
            }
            else if (checkedRadioButtonIdGender == R.id.medop_radioFemale) {
                PATIENT_GENDER = "2";
            }
            else if (checkedRadioButtonIdGender == R.id.medop_radioTrans) {
                PATIENT_GENDER = "3";
            }
            else {
                PATIENT_GENDER = "0";
            }
        }

        int checkedRadioButtonIdHyper = hypertensionGroup.getCheckedRadioButtonId();       // 1 - Yes, 2 - No
        if (checkedRadioButtonIdHyper == -1) {   // No items Selected
            PATIENT_HYPERTENSION = "0";
        }
        else{
            if (checkedRadioButtonIdHyper == R.id.medop_radiohyperYes) {
                PATIENT_HYPERTENSION = "1";
            }
            else if (checkedRadioButtonIdHyper == R.id.medop_radiohyperNo) {
                PATIENT_HYPERTENSION = "2";
            }
            else {
                PATIENT_HYPERTENSION = "0";
            }
        }

        int checkedRadioButtonIdDiabetes = diabetesGroup.getCheckedRadioButtonId();       // 1 - Yes, 2 - No
        if (checkedRadioButtonIdDiabetes == -1) {   // No items Selected
            PATIENT_DIABETES = "0";
        }
        else{
            if (checkedRadioButtonIdDiabetes == R.id.medop_radiodianetesYes) {
                PATIENT_DIABETES = "1";
            }
            else if (checkedRadioButtonIdDiabetes == R.id.medop_radiodiabetesNo) {
                PATIENT_DIABETES = "2";
            }
            else {
                PATIENT_DIABETES = "0";
            }
        }

        if(EMAIL_ID.equals("")) {
            Toast.makeText(MedicalOpinionActivity.this, "Please enter email address to receive an opinion !!!", Toast.LENGTH_LONG).show();

        }
        else {
         //   Toast.makeText(MedicalOpinionActivity.this, "Success", Toast.LENGTH_SHORT).show();
            sendMedicalDataToServer(CONTACT_PERSON, MOBILE_NUM, EMAIL_ID, ADDRESS, CITY, STATE, COUNTRY, PATIENT_NAME, PATIENT_ID,
                    PATIENT_GENDER, AGE, WEIGHT, PATIENT_HYPERTENSION, PATIENT_DIABETES, SPECIALIZATION_ID, SPECIALIZATION_NAME,
                    MEDICAL_COMPLAINT, BRIEF_DESCRIPTION, QUERY_DOCTOR, REPORTS_PHOTOS);
        }

    }

    private void sendMedicalDataToServer(final String contact_person, final String mobile_num, final String email_id, final String address, final String city,
                                         final String state, final String country, final String patient_name, final int patient_id, final String patient_gender,
                                         final String age, final String weight, final String patient_hypertension, final String patient_diabetes,
                                         final int specialization_id, final String specialization_name, final String medical_complaint,
                                         final String brief_description, final String query_doctor, final ArrayList<String> reports_photos) {

        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(MedicalOpinionActivity.this, R.style.CustomDialog);
                pd.setCancelable(false);
                pd.setMessage("Sending Medical Opinion....");
                pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                pd.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {

                    JSONObject jsonObject = JSONParser.sendMedicalOpinion(contact_person, mobile_num, email_id, address, city,
                             state,  country,  patient_name,  patient_id,  patient_gender,age,  weight,  patient_hypertension,
                            patient_diabetes, specialization_id,  specialization_name,  medical_complaint, brief_description,
                            query_doctor,  reports_photos, USER_ID, USER_NAME, DOCTOR_ID, 0);

                    if (jsonObject != null) {
                        Log.e(AppUtils.TAG, " GET: " + jsonObject.getString("result_opinion"));
                        if (jsonObject.getString("result_opinion").equals("success")) {
                            status = true;
                        } else {
                            status = false;
                        }
                    }
                    // return true;
                } catch (JSONException e) {
                    Log.i(AppUtils.TAG, "Error : " + e.getLocalizedMessage());
                    return false;
                }
                return status;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (pd != null)
                    pd.dismiss();
                Log.e(AppUtils.TAG, "aBoolean: " + String.valueOf(aBoolean));
                if (aBoolean) {
                    // Toast.makeText(SignUpActivity.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                    final Dialog dialog = new Dialog(MedicalOpinionActivity.this, R.style.CustomDialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.custom_success);

                    CustomTextView text = (CustomTextView) dialog.findViewById(R.id.text_success_dialog);
                    text.setText("Medical opinion sent successfully !!!");

                    Button dialogButton = (Button) dialog.findViewById(R.id.text_success_submit);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            finish();
                        }
                    });

                    dialog.show();
                } else {
                    Toast.makeText(MedicalOpinionActivity.this, "Failed to send medical opinion !!!. \nTry later !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }

    class getJsonAddress extends AsyncTask<String,String,String> {
        String country_shortname;
        @Override
        protected String doInBackground(String... key) {
            String stateText = key[0];
            stateText = stateText.trim();
            country_shortname = stateText.trim();
            Log.d(AppUtils.TAG, "State selected1: " + stateText);
            stateText = stateText.replace(" ", "+");

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            // StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://maps.googleapis.com/maps/api/geocode/json?address="+stateText+"&sensor=true",
            StringRequest stringRequest1 = new StringRequest(Request.Method.GET, "http://postalpincode.in/api/pincode/"+stateText,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(AppUtils.TAG, response.toString());
                            if (response != null) {
                                JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;
                                ArrayList<String> stateArray = new ArrayList<String>();
                                try {
                                    jsonObject1 = new JSONObject(response);

                                    // Getting JSON Array node
                                    JSONArray contacts = jsonObject1.getJSONArray("PostOffice");
                                    // looping through All Contacts
                                    for (int i = 0; i < contacts.length(); i++) {
                                        JSONObject c = contacts.getJSONObject(i);

                                        String city = c.getString("Name");
                                        String State = c.getString("State");
                                        String Country = c.getString("Country");
                                        _edt_city.setText(city);
                                        _edt_state.setText(State);
                                        _edt_country.setText(Country);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(MedicalOpinionActivity.this).
                    getRequestQueue();
            AppController.getInstance(MedicalOpinionActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }
    }
}
