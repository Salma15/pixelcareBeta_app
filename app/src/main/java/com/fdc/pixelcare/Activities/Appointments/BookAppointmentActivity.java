package com.fdc.pixelcare.Activities.Appointments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.fdc.pixelcare.Adapters.Appointments.AddressAdapter;
import com.fdc.pixelcare.DataModel.FamilyMember;
import com.fdc.pixelcare.DataModel.HospitalList;
import com.fdc.pixelcare.DataModel.SpecializationList;
import com.fdc.pixelcare.DataModel.UserAddress;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.Network.CustomVolleyRequestQueue;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppController;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
import com.fdc.pixelcare.Views.CustomEditText;
import com.fdc.pixelcare.Views.CustomTextView;
import com.fdc.pixelcare.Views.CustomTextViewBold;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

/**
 * Created by salma on 21/12/18.
 */

public class BookAppointmentActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener,
        Response.ErrorListener  {
    public static final String REQUEST_TAG = "BookAppointmentActivity";
    int DOC_ID, DOC_SPEC_ID;
    String DOC_NAME, DOC_SPEC_NAME, DOC_QUALIFICATION, DOC_ADDRESS, DOC_CITY, DOC_PHOTO;

    private HorizontalCalendar horizontalCalendar;
    ImageView doctor_image;
    CustomTextView doc_name, doc_specialization, doc_city;
    String APPOINT_DATE = "", APPOINT_TIME = "";
    int APPOINT_TIME_ID = 0;
    CustomTextView appt_time8a, appt_time9a, appt_time10a, appt_time11a, appt_time12p, appt_time1p, appt_time2p, appt_time3p, appt_time4p, appt_time5p, appt_time6p, appt_time7p, appt_time8p;
    Button book_appoint_btn, add_address_address;
    LinearLayout date_time_layout, user_info_layout;
    CustomTextView scheduled_date, change_date;
    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int USER_ID, USER_LOGINTYPE;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION, FAMILY_MEMBER_LIST, USER_ADDRESS_LIST;
    ArrayList<FamilyMember> memberArraylist;
    ArrayList<UserAddress> addressArraylist;

    Spinner family_spinner_name;
    ImageView add_member_btn;
    CustomEditText _edt_mobile, _edt_email, _edt_age;
    RadioGroup genderGroup;
    RadioButton rb_male, rb_female, rb_trans;
    ArrayList<String> memberNameArray;
    String PATIENT_NAME;
    int PATIENT_ID;
    Calendar myCalendar;
    CustomTextView choose_dob;
    String GET_GENDER = "0", GER_RELATIONSHIP_TYPE, PATIENT_DOB, PATIENT_GENDER = "0";
    private RequestQueue mQueue;

    LinearLayout address_layout;
    CustomTextView change_date_addrs, change_patient_info, appoint_date_addrs;
    CustomTextView add_address_btn;
    ImageView refresh_address;
    RecyclerView address_recyclerview;
    AddressAdapter addrsAdapter;

    String PATIENT_ADDRESS = "", PATIENT_CITY = "", PATIENT_PINCODE = "", PATIENT_STATE = "", PATIENT_COUNTRY = "";
    String PATIENT_APPOINTMENT_DATE = "", PATIENT_APPOINTMENT_TIME = "";
    int PATIENT_APPOINTMENT_TIME_ID = 0;
    String PATIENT_MOBILE, PATIENT_EMAIL, PATIENT_AGE;
    ProgressDialog pd;

    List<SpecializationList> specilizationDocArraylist;
    List<HospitalList> hospitalDocArraylist;
    Spinner doc_hospital_spinner, appoint_date_spinner;
    String appoint_date_str = "";
    int appoint_type = 0, HOSPITAL_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        memberArraylist = new ArrayList<>();
        addressArraylist = new ArrayList<>();
        specilizationDocArraylist = new ArrayList<>();
        hospitalDocArraylist = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            setTitle(title);

            DOC_ID = b.getInt("DOC_ID");
            DOC_NAME = b.getString("DOC_NAME");
            DOC_SPEC_ID = b.getInt("DOC_SPEC_ID");
            DOC_SPEC_NAME = b.getString("DOC_SPEC_NAME");
            DOC_QUALIFICATION = b.getString("DOC_QUALIFICATION");
            DOC_ADDRESS = b.getString("DOC_ADDRESS");
            DOC_CITY = b.getString("DOC_CITY");
            DOC_PHOTO = b.getString("DOC_PHOTO");
            specilizationDocArraylist =  (List<SpecializationList>) b.getSerializable("SPECIALIZATION_LIST");
            hospitalDocArraylist =  (List<HospitalList>) b.getSerializable("HOSPITAL_LIST");
        }

        shareadPreferenceClass = new ShareadPreferenceClass(BookAppointmentActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(BookAppointmentActivity.this);

        if(sharedPreferences !=null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE  = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);
            FAMILY_MEMBER_LIST = sharedPreferences.getString(MHConstants.PREF_FAMILY_MEMBERS, "");
            USER_ADDRESS_LIST = sharedPreferences.getString(MHConstants.PREF_USER_ADDRESS, "");

            Log.d(AppUtils.TAG , " *********** Book Appoint List **************** ");
            Log.d(AppUtils.TAG + " UserID: ", String.valueOf(USER_ID));
            Log.d(AppUtils.TAG + " name: ", USER_NAME);
            Log.d(AppUtils.TAG + " email: ", USER_EMAIL);
            Log.d(AppUtils.TAG + " MOBILE: ", USER_MOBILE);
            Log.d(AppUtils.TAG + " Location: ", USER_LOCATION);
        }


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

    @Override
    protected void onResume() {
        super.onResume();

        if(sharedPreferences !=null) {
            USER_ADDRESS_LIST = sharedPreferences.getString(MHConstants.PREF_USER_ADDRESS, "");
            FAMILY_MEMBER_LIST = sharedPreferences.getString(MHConstants.PREF_FAMILY_MEMBERS, "");

            Log.d(AppUtils.TAG , " *********** Book Appoint List **************** ");
            Log.d(AppUtils.TAG + " UserID: ", String.valueOf(USER_ID));
            Log.d(AppUtils.TAG + " name: ", USER_NAME);
            Log.d(AppUtils.TAG + " email: ", USER_EMAIL);
            Log.d(AppUtils.TAG + " MOBILE: ", USER_MOBILE);
            Log.d(AppUtils.TAG + " Location: ", USER_LOCATION);
        }

        initializationView();
    }


    private void initializationView() {
        memberNameArray = new ArrayList<String>();
        myCalendar = Calendar.getInstance();

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

        family_spinner_name = (Spinner) findViewById(R.id.appint_book_member_spinner);
        add_member_btn = (ImageView)  findViewById(R.id.appint_book_add_member);
        add_member_btn.setOnClickListener(this);
        _edt_mobile = (CustomEditText)  findViewById(R.id.appint_book_mobile);
        _edt_email = (CustomEditText)  findViewById(R.id.appint_book_email);
        _edt_age = (CustomEditText)  findViewById(R.id.appint_book_age);
        genderGroup = (RadioGroup)  findViewById(R.id.appint_book_radioGroupGender);
        rb_male = (RadioButton) findViewById(R.id.medop_radioMale);
        rb_female = (RadioButton) findViewById(R.id.medop_radioFemale);
        rb_trans = (RadioButton) findViewById(R.id.medop_radioTrans);

        _edt_mobile.setText(USER_MOBILE);
        _edt_email.setText(USER_EMAIL);

        address_recyclerview = (RecyclerView) findViewById(R.id.appintment_book_addrs_recyclerview);
        addrsAdapter = new AddressAdapter(BookAppointmentActivity.this, addressArraylist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        address_recyclerview.setLayoutManager(mLayoutManager);
        address_recyclerview.setItemAnimator(new DefaultItemAnimator());
        address_recyclerview.setAdapter(addrsAdapter);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(BookAppointmentActivity.this, R.layout.spinner_text, memberNameArray );
        locationAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        family_spinner_name.setAdapter(locationAdapter);
        family_spinner_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        /* start 2 months ago from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -2);

        /* end after 2 months from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 2);

        // Default Date set to Today.
        final Calendar defaultSelectedDate = Calendar.getInstance();
        defaultSelectedDate.add(Calendar.DATE, 1);

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(defaultSelectedDate, endDate)
                .datesNumberOnScreen(3)
                .configure()
                .formatTopText("MMM")
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .showTopText(true)
                .showBottomText(true)
                .textColor(Color.LTGRAY, R.color.colorPrimaryDark)
                .colorTextMiddle(Color.LTGRAY, R.color.colorPrimaryDark)
                .end()
                .defaultSelectedDate(defaultSelectedDate)
                .build();

        Log.i("Default Date", DateFormat.format("EEE, MMM d, yyyy", defaultSelectedDate).toString());
        APPOINT_DATE = DateFormat.format("EEE, MMM d, yyyy", defaultSelectedDate).toString();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                String selectedDateStr = DateFormat.format("EEE, MMM d, yyyy", date).toString();
             //   Toast.makeText(BookAppointmentActivity.this, selectedDateStr + " selected!", Toast.LENGTH_SHORT).show();
                Log.i("onDateSelected", selectedDateStr + " - Position = " + position);
              //  APPOINT_DATE = selectedDateStr;
            }

        });

        doctor_image = (ImageView) findViewById(R.id.book_appt_docimage);
        doc_name = (CustomTextView) findViewById(R.id.book_appt_docname);
        doc_specialization = (CustomTextView) findViewById(R.id.book_appt_docspec);
        doc_city = (CustomTextView) findViewById(R.id.book_appt_doccity);

        if(DOC_PHOTO.equals("")) {
            doctor_image.setImageResource(R.drawable.doctor_icon);
        }
        else {
            String DOWNLOAD_PROFILE = APIClass.DRS_DOCTOR_PROFILE_URL+String.valueOf(DOC_ID)+"/"+DOC_PHOTO.trim();
            String urlStr = DOWNLOAD_PROFILE;
            URL url = null;
            try {
                url = new URL(urlStr);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            Picasso.with(this).load(String.valueOf(url))
                    .placeholder(R.drawable.doctor_icon)
                    .error(R.drawable.doctor_icon)
                    .resize(200, 200)
                    .centerInside()
                    .into(doctor_image, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }

        doc_name.setText(DOC_NAME);
       // doc_specialization.setText(DOC_SPEC_NAME);
        doc_city.setText(DOC_CITY);

        if(specilizationDocArraylist.size()>0 && specilizationDocArraylist != null) {
            Log.d(AppUtils.TAG, "docSpecSize: " +specilizationDocArraylist.size());
            StringBuilder sb = new StringBuilder();
            for(int  i =0; i < specilizationDocArraylist.size(); i++)
            {
                sb.append(specilizationDocArraylist.get(i).getSpecializationName().toString());
                sb.append(", ");
            }
            doc_specialization.setText(sb.toString().substring(0, sb.toString().length() - 2));
        }

        doc_hospital_spinner = (Spinner)findViewById(R.id.appintment_book_hospital_spinner);
        if(hospitalDocArraylist.size()>0 && hospitalDocArraylist != null) {
            List<String> doctor_hospital_array = new ArrayList<String>();
            Log.d(AppUtils.TAG, "docHospSize: " +hospitalDocArraylist.size());
            for(int j=0; j<hospitalDocArraylist.size(); j++) {
                doctor_hospital_array.add(hospitalDocArraylist.get(j).getHospitalName().trim());
            }

            ArrayAdapter<String> aAdapter_state = new ArrayAdapter<String>(BookAppointmentActivity.this, R.layout.spinner_text, doctor_hospital_array);
            doc_hospital_spinner.setAdapter(aAdapter_state);
            aAdapter_state.notifyDataSetChanged();
            doc_hospital_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    Log.d(AppUtils.TAG, "HospName: "+ (String) parent.getItemAtPosition(position));
                    Log.d(AppUtils.TAG, "HospID: "+ hospitalDocArraylist.get(position).getHospitalId()+ " name: "+ hospitalDocArraylist.get(position).getHospitalName());

                    HOSPITAL_ID = hospitalDocArraylist.get(position).getHospitalId();
                    new getJsonAppointmentDates().execute(String.valueOf(hospitalDocArraylist.get(position).getHospitalId()));
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.d(AppUtils.TAG, "Nothing hospital");
                    HOSPITAL_ID = 0;
                }
            });
        }

        appoint_date_spinner = (Spinner)findViewById(R.id.appintment_book_dates_spinner);

        appt_time8a = (CustomTextView) findViewById(R.id.book_aapt_8am);
        appt_time8a.setOnClickListener(this);
        appt_time9a = (CustomTextView) findViewById(R.id.book_aapt_9am);
        appt_time9a.setOnClickListener(this);
        appt_time10a = (CustomTextView) findViewById(R.id.book_aapt_10am);
        appt_time10a.setOnClickListener(this);
        appt_time11a = (CustomTextView) findViewById(R.id.book_aapt_11am);
        appt_time11a.setOnClickListener(this);
        appt_time12p = (CustomTextView) findViewById(R.id.book_aapt_12pm);
        appt_time12p.setOnClickListener(this);
        appt_time1p = (CustomTextView) findViewById(R.id.book_aapt_1pm);
        appt_time1p.setOnClickListener(this);
        appt_time2p = (CustomTextView) findViewById(R.id.book_aapt_2pm);
        appt_time2p.setOnClickListener(this);
        appt_time3p = (CustomTextView) findViewById(R.id.book_aapt_3pm);
        appt_time3p.setOnClickListener(this);
        appt_time4p = (CustomTextView) findViewById(R.id.book_aapt_4pm);
        appt_time4p.setOnClickListener(this);
        appt_time5p = (CustomTextView) findViewById(R.id.book_aapt_5pm);
        appt_time5p.setOnClickListener(this);
        appt_time6p = (CustomTextView) findViewById(R.id.book_aapt_6pm);
        appt_time6p.setOnClickListener(this);
        appt_time7p = (CustomTextView) findViewById(R.id.book_aapt_7pm);
        appt_time7p.setOnClickListener(this);
        appt_time8p = (CustomTextView) findViewById(R.id.book_aapt_8pm);
        appt_time8p.setOnClickListener(this);

        appt_time8a.setBackgroundResource(R.color.gray1);
        appt_time8a.setClickable(false);
        appt_time9a.setBackgroundResource(R.color.gray1);
        appt_time9a.setClickable(false);
        appt_time10a.setBackgroundResource(R.color.gray1);
        appt_time10a.setClickable(false);
        appt_time11a.setBackgroundResource(R.color.gray1);
        appt_time11a.setClickable(false);
        appt_time12p.setBackgroundResource(R.color.gray1);
        appt_time12p.setClickable(false);
        appt_time1p.setBackgroundResource(R.color.gray1);
        appt_time1p.setClickable(false);
        appt_time2p.setBackgroundResource(R.color.gray1);
        appt_time2p.setClickable(false);
        appt_time3p.setBackgroundResource(R.color.gray1);
        appt_time3p.setClickable(false);
        appt_time4p.setBackgroundResource(R.color.gray1);
        appt_time4p.setClickable(false);
        appt_time5p.setBackgroundResource(R.color.gray1);
        appt_time5p.setClickable(false);
        appt_time6p.setBackgroundResource(R.color.gray1);
        appt_time6p.setClickable(false);
        appt_time7p.setBackgroundResource(R.color.gray1);
        appt_time7p.setClickable(false);
        appt_time8p.setBackgroundResource(R.color.gray1);
        appt_time8p.setClickable(false);

        book_appoint_btn = (Button) findViewById(R.id.book_appt_submit);
        book_appoint_btn.setOnClickListener(this);
        book_appoint_btn.setVisibility(View.GONE);

        add_address_address = (Button) findViewById(R.id.book_appt_address);
        add_address_address.setOnClickListener(this);
        add_address_address.setVisibility(View.GONE);

        date_time_layout = (LinearLayout) findViewById(R.id.appintment_book_datetime_layout);
        date_time_layout.setVisibility(View.VISIBLE);
        user_info_layout = (LinearLayout) findViewById(R.id.appintment_book_userinfo_layout);
        user_info_layout.setVisibility(View.GONE);

        scheduled_date = (CustomTextView) findViewById(R.id.appintment_book_date);
        change_date = (CustomTextView) findViewById(R.id.appintment_book_changedate);
        change_date.setOnClickListener(this);


        address_layout = (LinearLayout) findViewById(R.id.appintment_book_address_layout);
        address_layout.setVisibility(View.GONE);
        change_date_addrs = (CustomTextView) findViewById(R.id.appintment_book_changedate_addrs);
        change_date_addrs.setOnClickListener(this);
        change_patient_info  = (CustomTextView) findViewById(R.id.appintment_book_userinfo_addrs);
        change_patient_info.setOnClickListener(this);
        appoint_date_addrs = (CustomTextView) findViewById(R.id.appintment_book_date_addrs);

        add_address_btn = (CustomTextView) findViewById(R.id.appintment_book_add_addrs);
        add_address_btn.setOnClickListener(this);
        refresh_address = (ImageView) findViewById(R.id.appintment_book_addrs_refresh);
        refresh_address.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.book_aapt_8am:
                date_time_layout.setVisibility(View.GONE);
                user_info_layout.setVisibility(View.VISIBLE);
                APPOINT_TIME_ID = 1;
                APPOINT_TIME = "8:00 AM";
                showUserInfo(APPOINT_DATE, APPOINT_TIME, APPOINT_TIME_ID);
                break;
            case R.id.book_aapt_9am:
                date_time_layout.setVisibility(View.GONE);
                user_info_layout.setVisibility(View.VISIBLE);
                APPOINT_TIME_ID = 2;
                APPOINT_TIME = "9:00 AM";
                showUserInfo(APPOINT_DATE, APPOINT_TIME, APPOINT_TIME_ID);
                break;
            case R.id.book_aapt_10am:
                date_time_layout.setVisibility(View.GONE);
                user_info_layout.setVisibility(View.VISIBLE);
                APPOINT_TIME_ID = 3;
                APPOINT_TIME = "10:00 AM";
                showUserInfo(APPOINT_DATE, APPOINT_TIME, APPOINT_TIME_ID);
                break;
            case R.id.book_aapt_11am:
                date_time_layout.setVisibility(View.GONE);
                user_info_layout.setVisibility(View.VISIBLE);
                APPOINT_TIME_ID = 4;
                APPOINT_TIME = "11:00 AM";
                showUserInfo(APPOINT_DATE, APPOINT_TIME, APPOINT_TIME_ID);
                break;
            case R.id.book_aapt_12pm:
                date_time_layout.setVisibility(View.GONE);
                user_info_layout.setVisibility(View.VISIBLE);
                APPOINT_TIME_ID = 5;
                APPOINT_TIME = "12:00 PM";
                showUserInfo(APPOINT_DATE, APPOINT_TIME, APPOINT_TIME_ID);
                break;
            case R.id.book_aapt_1pm:
                date_time_layout.setVisibility(View.GONE);
                user_info_layout.setVisibility(View.VISIBLE);
                APPOINT_TIME_ID = 6;
                APPOINT_TIME = "1:00 PM";
                showUserInfo(APPOINT_DATE, APPOINT_TIME, APPOINT_TIME_ID);
                break;
            case R.id.book_aapt_2pm:
                date_time_layout.setVisibility(View.GONE);
                user_info_layout.setVisibility(View.VISIBLE);
                APPOINT_TIME_ID = 7;
                APPOINT_TIME = "2:00 PM";
                showUserInfo(APPOINT_DATE, APPOINT_TIME, APPOINT_TIME_ID);
                break;
            case R.id.book_aapt_3pm:
                date_time_layout.setVisibility(View.GONE);
                user_info_layout.setVisibility(View.VISIBLE);
                APPOINT_TIME_ID = 8;
                APPOINT_TIME = "3:00 PM";
                showUserInfo(APPOINT_DATE, APPOINT_TIME, APPOINT_TIME_ID);
                break;
            case R.id.book_aapt_4pm:
                date_time_layout.setVisibility(View.GONE);
                user_info_layout.setVisibility(View.VISIBLE);
                APPOINT_TIME_ID = 9;
                APPOINT_TIME = "4:00 PM";
                showUserInfo(APPOINT_DATE, APPOINT_TIME, APPOINT_TIME_ID);
                break;
            case R.id.book_aapt_5pm:
                date_time_layout.setVisibility(View.GONE);
                user_info_layout.setVisibility(View.VISIBLE);
                APPOINT_TIME_ID = 10;
                APPOINT_TIME = "5:00 PM";
                showUserInfo(APPOINT_DATE, APPOINT_TIME, APPOINT_TIME_ID);
                break;
            case R.id.book_aapt_6pm:
                date_time_layout.setVisibility(View.GONE);
                user_info_layout.setVisibility(View.VISIBLE);
                APPOINT_TIME_ID = 11;
                APPOINT_TIME = "6:00 PM";
                showUserInfo(APPOINT_DATE, APPOINT_TIME, APPOINT_TIME_ID);
                break;
            case R.id.book_aapt_7pm:
                date_time_layout.setVisibility(View.GONE);
                user_info_layout.setVisibility(View.VISIBLE);
                APPOINT_TIME_ID = 12;
                APPOINT_TIME = "7:00 PM";
                showUserInfo(APPOINT_DATE, APPOINT_TIME, APPOINT_TIME_ID);
                break;
            case R.id.book_aapt_8pm:
                date_time_layout.setVisibility(View.GONE);
                user_info_layout.setVisibility(View.VISIBLE);
                APPOINT_TIME_ID = 13;
                APPOINT_TIME = "8:00 PM";
                showUserInfo(APPOINT_DATE, APPOINT_TIME, APPOINT_TIME_ID);
                break;
            case R.id.appintment_book_changedate:
                user_info_layout.setVisibility(View.GONE);
                date_time_layout.setVisibility(View.VISIBLE);
                book_appoint_btn.setVisibility(View.GONE);
                add_address_address.setVisibility(View.GONE);
                break;
            case R.id.appint_book_add_member:
                GET_GENDER = "0";
                customAddFamilyMember();
                break;
            case R.id.appintment_book_changedate_addrs:
                user_info_layout.setVisibility(View.GONE);
                address_layout.setVisibility(View.GONE);
                date_time_layout.setVisibility(View.VISIBLE);
                book_appoint_btn.setVisibility(View.GONE);
                add_address_address.setVisibility(View.GONE);
                change_date_addrs.setVisibility(View.GONE);
                change_patient_info.setVisibility(View.GONE);
                break;
            case R.id.appintment_book_userinfo_addrs:
                user_info_layout.setVisibility(View.VISIBLE);
                address_layout.setVisibility(View.GONE);
                date_time_layout.setVisibility(View.GONE);
                book_appoint_btn.setVisibility(View.GONE);
                add_address_address.setVisibility(View.GONE);
                change_date_addrs.setVisibility(View.GONE);
                change_patient_info.setVisibility(View.GONE);
                book_appoint_btn.setVisibility(View.GONE);
                add_address_address.setVisibility(View.VISIBLE);
                break;
            case R.id.book_appt_address:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(add_address_address.getWindowToken(), 0);

                validatePatientInfo();
                break;
            case R.id.appintment_book_add_addrs:
                Intent i4 = new Intent(BookAppointmentActivity.this, AddAddressActivity.class);
                i4.putExtra("title","Add Address");
                startActivity(i4);
                break;
            case R.id.book_appt_submit:
                InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(book_appoint_btn.getWindowToken(), 0);

                 validateBookAppointmentForm();
                break;
            case R.id.appintment_book_addrs_refresh:
                InputMethodManager imm2 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(refresh_address.getWindowToken(), 0);

                loadAddressListFromServer();
                break;
        }
    }

    private void loadAddressListFromServer() {

        pd = new ProgressDialog(BookAppointmentActivity.this, R.style.CustomDialog);
        pd.setCancelable(false);
        pd.setMessage("Loading address...");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        StringRequest postRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_ADDRESS_LISTS,
                this,this ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(APIParam.KEY_API, APIClass.API_KEY);
                params.put(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID));

                return params;
            }
        };

        postRequest.setTag(REQUEST_TAG);
        mQueue.add(postRequest);
    }

    private void validateBookAppointmentForm() {

        if(PATIENT_ADDRESS.equals("") || PATIENT_CITY.equals("")) {
            Toast.makeText(BookAppointmentActivity.this, "Select Address !!!", Toast.LENGTH_SHORT).show();
        }
        else if(HOSPITAL_ID == 0) {
            Toast.makeText(BookAppointmentActivity.this, "Select Hospital !!!", Toast.LENGTH_SHORT).show();
        }
        else if(PATIENT_ID == 0) {
            Toast.makeText(BookAppointmentActivity.this, "Select Patient Name !!!", Toast.LENGTH_SHORT).show();
        }
        else {
            submitBookAppointmentToServer();
        }

    }

    private void validatePatientInfo() {
        final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(

                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );

        int checkedRadioButtonId = genderGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {   // No items Selected
            PATIENT_GENDER = "0";
        }
        else{
            if (checkedRadioButtonId == R.id.medop_radioMale) {
                PATIENT_GENDER = "1";
            }
            else if (checkedRadioButtonId == R.id.medop_radioFemale) {
                PATIENT_GENDER = "2";
            }
            else if (checkedRadioButtonId == R.id.medop_radioTrans) {
                PATIENT_GENDER = "3";
            }
            else {
                PATIENT_GENDER = "0";
            }
        }

        if(PATIENT_NAME.equals("")) {
            Toast.makeText(BookAppointmentActivity.this, "Select Patient Name !!!", Toast.LENGTH_SHORT).show();
        }
        else if((_edt_mobile.getText().toString().equals("")) || (_edt_mobile.getText().toString().length() <10)) {
            Toast.makeText(BookAppointmentActivity.this, "Add valid 10 digits mobile no. !!!", Toast.LENGTH_SHORT).show();
        }
        else if(_edt_email.getText().toString().trim().equals("")) {
            Toast.makeText(BookAppointmentActivity.this, "Enter Email Address !!!", Toast.LENGTH_SHORT).show();
        }
        else if(_edt_age.getText().toString().trim().equals("")) {
            Toast.makeText(BookAppointmentActivity.this, "Enter Patient Age !!!", Toast.LENGTH_SHORT).show();
        }
        else if(PATIENT_GENDER.equals("0")) {
            Toast.makeText(BookAppointmentActivity.this, "Select Gender !!!", Toast.LENGTH_SHORT).show();
        }
        else {
            if(EMAIL_ADDRESS_PATTERN.matcher(_edt_email.getText().toString().trim()).matches()) {
                user_info_layout.setVisibility(View.GONE);
                address_layout.setVisibility(View.VISIBLE);
                date_time_layout.setVisibility(View.GONE);
                book_appoint_btn.setVisibility(View.GONE);
                add_address_address.setVisibility(View.GONE);
                change_date_addrs.setVisibility(View.VISIBLE);
                change_patient_info.setVisibility(View.VISIBLE);

                book_appoint_btn.setVisibility(View.VISIBLE);
                add_address_address.setVisibility(View.GONE);

                PATIENT_MOBILE = _edt_mobile.getText().toString().trim();
                PATIENT_EMAIL = _edt_email.getText().toString().trim();
                PATIENT_AGE = _edt_age.getText().toString().trim();
            }
            else {
                Toast.makeText(BookAppointmentActivity.this, "Invalid Email ID !!!!", Toast.LENGTH_SHORT).show();
            }

        }


    }

    private void showUserInfo(String appoint_date, String appoint_time, int appoint_time_id) {
        scheduled_date.setText(appoint_date  + " at "+ appoint_time);
        appoint_date_addrs.setText(appoint_date  + " at "+ appoint_time);
        book_appoint_btn.setVisibility(View.GONE);
        add_address_address.setVisibility(View.VISIBLE);
        PATIENT_APPOINTMENT_DATE = appoint_date;
        PATIENT_APPOINTMENT_TIME_ID = appoint_time_id;
        PATIENT_APPOINTMENT_TIME = appoint_time;
    }

    private void customAddFamilyMember() {
        final Dialog dialog = new Dialog(BookAppointmentActivity.this,  R.style.CustomDialog);
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
        final CustomEditText _edt_age = (CustomEditText)  dialog.findViewById(R.id.addmember_age);
        final CustomTextViewBold submit_btn = (CustomTextViewBold)  dialog.findViewById(R.id.addmember_submit);
        Spinner spinner_relation = (Spinner) dialog.findViewById(R.id.addmember_relationSpinner);
        choose_dob = (CustomTextView)  dialog.findViewById(R.id.addmember_dob);
        final RadioGroup radioGenderGroup = (RadioGroup) dialog.findViewById(R.id.addmember_radioGroupGender);

        ArrayAdapter<String> spinnerCountShoesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.relationship_types));
        spinner_relation.setAdapter(spinnerCountShoesArrayAdapter);

        spinner_relation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //    Toast.makeText(BookAppointmentActivity.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                GER_RELATIONSHIP_TYPE = adapterView.getSelectedItem().toString().trim();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        choose_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_dob.setText("");
                new DatePickerDialog(BookAppointmentActivity.this, dob_date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int checkedRadioButtonId = radioGenderGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {   // No items Selected
                    GET_GENDER = "0";
                }
                else{
                    if (checkedRadioButtonId == R.id.addmember_radioMale) {
                        GET_GENDER = "1";
                    }
                    else if (checkedRadioButtonId == R.id.addmember_radioFemale) {
                        GET_GENDER = "2";
                    }
                    else if (checkedRadioButtonId == R.id.addmember_radioTrans) {
                        GET_GENDER = "3";
                    }
                    else {
                        GET_GENDER = "0";
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
                else if(GET_GENDER.equals("0")) {
                    Toast.makeText(BookAppointmentActivity.this, "Select Gender !!!", Toast.LENGTH_SHORT).show();
                }
                else {

                    InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm1.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);

                    submitAddMemberToServer(_edt_name.getText().toString().trim(),_edt_age.getText().toString(), GET_GENDER, GER_RELATIONSHIP_TYPE, PATIENT_DOB);
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

    private void submitAddMemberToServer(final String patient_name, final String get_age, final String get_gender, final String ger_relationship_type, final String patient_dob) {
        Log.d(AppUtils.TAG, " patient_name " + patient_name);
        Log.d(AppUtils.TAG, " get_gender " + get_gender);
        Log.d(AppUtils.TAG, " get_age " + get_age);
        Log.d(AppUtils.TAG, " ger_relationship_type " + ger_relationship_type);
        Log.d(AppUtils.TAG, " patient_dob " + patient_dob);

        pd = new ProgressDialog(BookAppointmentActivity.this, R.style.CustomDialog);
        pd.setCancelable(false);
        pd.setMessage("Adding family member...");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();

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

                if(get_age != null) {
                    params.put(APIParam.KEY_MEMBER_AGE, get_age);
                }

                if(patient_dob != null) {
                    java.text.DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    java.text.DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = inputFormat.parse(patient_dob);
                        String dob_new = outputFormat.format(date);
                        params.put(APIParam.KEY_MEMBER_DOB, dob_new);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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
        if(pd!=null || pd.isShowing())
            pd.dismiss();
        AppUtils.showCustomAlertMessage(BookAppointmentActivity.this, "Warning !!!",error.getMessage(), "OK", null, null);
    }

    @Override
    public void onResponse(Object response) {
        Log.d(AppUtils.TAG +" Result: ", response.toString());
        String staus_res = null, book_appoint_result = null, user_Address_result = null, family_details_result = null;

        if(pd!=null || pd.isShowing())
            pd.dismiss();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(String.valueOf(response));

            if (jsonObject.has("result")) {
                staus_res = jsonObject.getString("result");
            }

            if (jsonObject.has("result_bookappoint")) {
                book_appoint_result = jsonObject.getString("result_bookappoint");
            }

            if (jsonObject.has("user_address")) {
                user_Address_result = jsonObject.getString("user_address");
            }

            if (jsonObject.has("family_details")) {
                family_details_result = jsonObject.getString("family_details");
            }

            if(staus_res != null && !staus_res.isEmpty() && family_details_result != null && !family_details_result.isEmpty()) {
                if(staus_res.equalsIgnoreCase("failure")) {
                    Toast.makeText(BookAppointmentActivity.this, "Add Member \n"+jsonObject.getString("err_msg"), Toast.LENGTH_SHORT).show();
                }
                else {
                    AppUtils.showCustomSuccessMessage(BookAppointmentActivity.this, "Add Member", "Member added successfully !!!", "OK", null, null);

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
                          //  memberNameArray.add(memberArraylist.get(i).getMemberName());
                            if(USER_NAME.equalsIgnoreCase(memberArraylist.get(i).getMemberName())) {
                                memberNameArray.add(memberArraylist.get(i).getMemberName()+" (Self)");
                            }
                            else {
                                memberNameArray.add(memberArraylist.get(i).getMemberName());
                            }
                        }

                        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(BookAppointmentActivity.this, R.layout.spinner_text, memberNameArray );
                        locationAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                        family_spinner_name.setAdapter(locationAdapter);
                    }
                }
            }

            if(staus_res != null && !staus_res.isEmpty() && book_appoint_result != null && !book_appoint_result.isEmpty()) {
                if(book_appoint_result.equalsIgnoreCase("failure")) {
                    Toast.makeText(BookAppointmentActivity.this, "Book Appointment \n"+jsonObject.getString("err_msg"), Toast.LENGTH_SHORT).show();
                }
                else {
                //    AppUtils.showCustomSuccessMessage(BookAppointmentActivity.this, "Book Appointment ", "Appointment Booked Successfully !!!", "OK", null, null);
                    final Dialog dialog = new Dialog(BookAppointmentActivity.this, R.style.CustomDialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.custom_success);

                    CustomTextView text = (CustomTextView) dialog.findViewById(R.id.text_success_dialog);
                    text.setText("Appointment Booked Successfully !!!");

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

            if(staus_res != null && !staus_res.isEmpty() && user_Address_result != null && !user_Address_result.isEmpty()) {
                JSONArray jsonArray2 = jsonObject.getJSONArray("user_address");
                if(jsonArray2.length() > 0 ) {
                    addressArraylist.clear();
                    addressArraylist = new ArrayList<UserAddress>();

                    for (int i = 0; i < jsonArray2.length(); i++) {
                        addressArraylist.add(new UserAddress(jsonArray2.getJSONObject(i).getInt("address_id"),jsonArray2.getJSONObject(i).getInt("user_id"),
                                jsonArray2.getJSONObject(i).getString("address"),jsonArray2.getJSONObject(i).getString("city"),
                                jsonArray2.getJSONObject(i).getString("pincode"),jsonArray2.getJSONObject(i).getString("state"),
                                jsonArray2.getJSONObject(i).getString("country")));
                    }
                    //Set the values Family Member
                    Gson gson = new Gson();
                    String jsonText = gson.toJson(addressArraylist);
                    if (sharedPreferences != null) {
                        shareadPreferenceClass.clearUserAddressLists();
                        shareadPreferenceClass.setUserAddressList(jsonText);
                    }

                    addrsAdapter = new AddressAdapter(BookAppointmentActivity.this, addressArraylist);
                    address_recyclerview.setAdapter(addrsAdapter);
                    addrsAdapter.notifyDataSetChanged();
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


    public void onClickCalled(String address, String city, String pincode, String state, String country) {
       // Toast.makeText(BookAppointmentActivity.this, "clicked item: "+address, Toast.LENGTH_SHORT).show();
        PATIENT_ADDRESS = address;
        PATIENT_CITY = city;
        PATIENT_PINCODE = pincode;
        PATIENT_STATE = state;
        PATIENT_COUNTRY = country;
    }

    private void submitBookAppointmentToServer() {
        Log.d(AppUtils.TAG, " APPT Date " + PATIENT_APPOINTMENT_DATE);
        Log.d(AppUtils.TAG, " AAPT Time " + PATIENT_APPOINTMENT_TIME);
        Log.d(AppUtils.TAG, " AAPT Time id " + PATIENT_APPOINTMENT_TIME_ID);
        Log.d(AppUtils.TAG, " patient_name " + PATIENT_NAME);
        Log.d(AppUtils.TAG, " patient_gender " + PATIENT_GENDER);
        Log.d(AppUtils.TAG, " patient_mobile" + PATIENT_MOBILE);
        Log.d(AppUtils.TAG, " patient_email " + PATIENT_EMAIL);
        Log.d(AppUtils.TAG, " patient_age " + PATIENT_AGE);
        Log.d(AppUtils.TAG, " DOC_ID " + DOC_ID);
        Log.d(AppUtils.TAG, " DOC_SPEC_ID " + DOC_SPEC_ID);
        Log.d(AppUtils.TAG, " MEMBER_ID " + PATIENT_ID);
        Log.d(AppUtils.TAG, " HOSPITAL_ID " + HOSPITAL_ID);

        SimpleDateFormat inputFormat = new SimpleDateFormat("E, MMM dd, yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try {
            date = inputFormat.parse(PATIENT_APPOINTMENT_DATE);
            appoint_date_str = outputFormat.format(date);
            Log.d(AppUtils.TAG, " appoint_date_str " + appoint_date_str);

            Calendar cal = Calendar.getInstance();
            Date sysDate = cal.getTime();
            if(date.compareTo(sysDate)>0){
                Log.d(AppUtils.TAG, " date_is_outdated false future" );
                appoint_type = 2;           // Future appointment
            }else{
                Log.d(AppUtils.TAG, " date_is_outdated true today" );
                appoint_type = 1;           // Walk In appointment. Today's appointments
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        pd = new ProgressDialog(BookAppointmentActivity.this, R.style.CustomDialog);
        pd.setCancelable(false);
        pd.setMessage("Booking appointment....");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();

        // Instantiate the RequestQueue.
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        StringRequest postRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_DOCTOR_BOOK_APPOINTMENT,
                this,this ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(APIParam.KEY_API, APIClass.API_KEY);
                params.put(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID));
                params.put(APIParam.KEY_MEMBER_ID, String.valueOf(PATIENT_ID));
                params.put(APIParam.KEY_DOCTOR_ID, String.valueOf(DOC_ID));
                params.put(APIParam.KEY_SPEC_ID, String.valueOf(DOC_SPEC_ID));
                params.put(APIParam.KEY_BOOK_APPOINT_DATE, appoint_date_str);
                params.put(APIParam.KEY_BOOK_APPOINT_TIME, String.valueOf(PATIENT_APPOINTMENT_TIME_ID));

                params.put(APIParam.KEY_BOOK_APPOINT_GENDER, PATIENT_GENDER);
                params.put(APIParam.KEY_BOOK_APPOINT_MOBILE, PATIENT_MOBILE);
                params.put(APIParam.KEY_BOOK_APPOINT_EMAIL, PATIENT_EMAIL);
                params.put(APIParam.KEY_BOOK_APPOINT_ADDRESS, PATIENT_ADDRESS);
                params.put(APIParam.KEY_BOOK_APPOINT_CITY, PATIENT_CITY);
                params.put(APIParam.KEY_BOOK_APPOINT_STATE, PATIENT_STATE);
                params.put(APIParam.KEY_BOOK_APPOINT_COUNTRY, PATIENT_COUNTRY);
                params.put(APIParam.KEY_BOOK_APPOINT_AGE, PATIENT_AGE);
                params.put(APIParam.KEY_BOOK_APPOINT_TYPE, String.valueOf(appoint_type));
                params.put(APIParam.KEY_HOSPITAL_ID, String.valueOf(HOSPITAL_ID));

                if(PATIENT_NAME != null) {
                    String input = PATIENT_NAME;
                    boolean isFound = input.indexOf(" (Self)") !=-1? true: false; //true
                    Log.d(AppUtils.TAG, " isFound: "+ String.valueOf(isFound));
                    if(isFound) {

                        String[] separated = input.split(" \\(");
                        String init_name = separated[0];
                        params.put(APIParam.KEY_BOOK_APPOINT_PATIENT_NAME, init_name);
                    }
                    else {
                        params.put(APIParam.KEY_BOOK_APPOINT_PATIENT_NAME, PATIENT_NAME);
                    }
                }


                return params;
            }
        };

        postRequest.setTag(REQUEST_TAG);
        mQueue.add(postRequest);

    }

    class getJsonAppointmentDates extends AsyncTask<String,String,String> {
        String hospital_id;
        @Override
        protected String doInBackground(String... key) {
            String hospidText = key[0];
            hospital_id = hospidText;
            Log.d(AppUtils.TAG, " hosp selected1: " + hospidText);
            Log.d(AppUtils.TAG, " DOC_ID: " + DOC_ID);
            // suggest_state = new ArrayList<String>();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_APPOINTMENT_DATES,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(AppUtils.TAG, " appoint dates: "+ response.toString());

                            if (response != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String staus_res = jsonObject.getString("status");
                                    if(staus_res.equals("true"))
                                    {
                                        JSONArray jsonArray1  = jsonObject.getJSONArray("todays_appoint_slots");
                                        if (jsonArray1.length() > 0) {
                                           /* no_slots_text.setVisibility(View.GONE);
                                            submit_appointment.setVisibility(View.VISIBLE);
                                            is_no_slots = 0;*/
                                        }
                                        else {
                                            /*no_slots_text.setVisibility(View.VISIBLE);
                                            submit_appointment.setVisibility(View.GONE);
                                            is_no_slots = 1;*/
                                        }

                                        JSONArray jsonArray3 = jsonObject.getJSONArray("appoint_details");
                                        ArrayList<String> appoint_dateArray = new ArrayList<String>();
                                        final ArrayList<String> appoint_dateIDArray = new ArrayList<String>();
                                        if (jsonArray3.length() > 0) {
                                            appoint_dateArray.add("--Select--");
                                            appoint_dateIDArray.add("0");
                                            for (int i = 0; i < jsonArray3.length(); i++) {
                                                appoint_dateArray.add(jsonArray3.getJSONObject(i).getString("appt_date"));
                                                appoint_dateIDArray.add(jsonArray3.getJSONObject(i).getString("appt_id"));
                                            }

                                            ArrayAdapter<String> apptdateAdapter = new ArrayAdapter<String>(BookAppointmentActivity.this, R.layout.spinner_text, appoint_dateArray );
                                            appoint_date_spinner.setAdapter(apptdateAdapter);

                                            appoint_date_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view,
                                                                           int position, long id) {
                                                    Log.d(AppUtils.TAG, " appoint_date: "+ (String) parent.getItemAtPosition(position));
                                                    String get_state = (String) parent.getItemAtPosition(position);
                                                    Log.d(AppUtils.TAG, "appt_date_ID: "+ appoint_dateIDArray.get(position).toString());

                                                    appt_time8a.setBackgroundResource(R.color.gray1);
                                                    appt_time8a.setClickable(false);
                                                    appt_time9a.setBackgroundResource(R.color.gray1);
                                                    appt_time9a.setClickable(false);
                                                    appt_time10a.setBackgroundResource(R.color.gray1);
                                                    appt_time10a.setClickable(false);
                                                    appt_time11a.setBackgroundResource(R.color.gray1);
                                                    appt_time11a.setClickable(false);
                                                    appt_time12p.setBackgroundResource(R.color.gray1);
                                                    appt_time12p.setClickable(false);
                                                    appt_time1p.setBackgroundResource(R.color.gray1);
                                                    appt_time1p.setClickable(false);
                                                    appt_time2p.setBackgroundResource(R.color.gray1);
                                                    appt_time2p.setClickable(false);
                                                    appt_time3p.setBackgroundResource(R.color.gray1);
                                                    appt_time3p.setClickable(false);
                                                    appt_time4p.setBackgroundResource(R.color.gray1);
                                                    appt_time4p.setClickable(false);
                                                    appt_time5p.setBackgroundResource(R.color.gray1);
                                                    appt_time5p.setClickable(false);
                                                    appt_time6p.setBackgroundResource(R.color.gray1);
                                                    appt_time6p.setClickable(false);
                                                    appt_time7p.setBackgroundResource(R.color.gray1);
                                                    appt_time7p.setClickable(false);
                                                    appt_time8p.setBackgroundResource(R.color.gray1);
                                                    appt_time8p.setClickable(false);

                                                    if(appoint_dateIDArray.get(position).toString().equals("0")) {
                                                        PATIENT_APPOINTMENT_DATE = "";
                                                        APPOINT_DATE = "";
                                                    }
                                                    else {
                                                        PATIENT_APPOINTMENT_DATE = appoint_dateIDArray.get(position).toString();
                                                       // APPOINT_DATE = PATIENT_APPOINTMENT_DATE;


                                                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                                        SimpleDateFormat outputFormat = new SimpleDateFormat("E, MMM dd, yyyy");
                                                        Date date = null;
                                                        try {
                                                            date = inputFormat.parse(PATIENT_APPOINTMENT_DATE);
                                                            APPOINT_DATE = outputFormat.format(date);
                                                            Log.d(AppUtils.TAG, " APPOINT_DATE: " + APPOINT_DATE);

                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }

                                                        new getJsonAppointmentTimings().execute(appoint_dateIDArray.get(position).toString(), hospital_id);
                                                    }
                                                }
                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {
                                                    PATIENT_APPOINTMENT_DATE = "";
                                                    APPOINT_DATE = "";
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
                            Log.d(AppUtils.TAG + "ERR ",error.toString());
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIParam.KEY_API, APIClass.API_KEY);
                    map.put(APIParam.KEY_HOSPITAL_ID, hospital_id);
                    map.put(APIParam.KEY_DOCTOR_ID, String.valueOf(DOC_ID));
                    return map;
                }
            };
            RequestQueue requestQueue = AppController.getInstance(BookAppointmentActivity.this).
                    getRequestQueue();
            AppController.getInstance(BookAppointmentActivity.this).addToRequestQueue(stringRequest);
            return null;
        }

        class getJsonAppointmentTimings extends AsyncTask<String,String,String> {
            String appoint_date_id, hospital_id;
            @Override
            protected String doInBackground(String... key) {
                appoint_date_id = key[0];
                hospital_id = key[1];
                Log.d(AppUtils.TAG, " selected appoint_date_id: " + appoint_date_id);
                Log.d(AppUtils.TAG, " selected hospital_id: " + hospital_id);

                int socketTimeout = 30000; // 30 seconds. You can change it
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_APPOINTMENT_TIMINGS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(AppUtils.TAG, " timings info: "+ response.toString());
                                if(response != null) {
                                    JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;
                                    try {
                                        jsonObject1 = new JSONObject(response);
                                        String staus_res = jsonObject1.getString("status");

                                        if (staus_res.equals("false")) {
                                        } else {
                                            jsonArray1 = jsonObject1.getJSONArray("appoint_timing_details");
                                            ArrayList<String> appoint_TimeArray = new ArrayList<String>();
                                            final ArrayList<String> appoint_timeIDArray = new ArrayList<String>();
                                            if (jsonArray1.length() > 0) {
                                                for (int i = 0; i < jsonArray1.length(); i++) {
                                                    appoint_TimeArray.add(jsonArray1.getJSONObject(i).getString("aapt_time"));
                                                    appoint_timeIDArray.add(jsonArray1.getJSONObject(i).getString("aapt_time_id"));

                                                    if(jsonArray1.getJSONObject(i).getInt("aapt_time_id")== 1) {
                                                        appt_time8a.setBackgroundColor(getResources().getColor(R.color.white));
                                                        appt_time8a.setClickable(true);
                                                    }

                                                    if(jsonArray1.getJSONObject(i).getInt("aapt_time_id") == 2) {
                                                        appt_time9a.setBackgroundColor(getResources().getColor(R.color.white));
                                                        appt_time9a.setClickable(true);
                                                    }

                                                    if(jsonArray1.getJSONObject(i).getInt("aapt_time_id") == 3) {
                                                        appt_time10a.setBackgroundColor(getResources().getColor(R.color.white));
                                                        appt_time10a.setClickable(true);
                                                    }

                                                     if(jsonArray1.getJSONObject(i).getInt("aapt_time_id") == 4) {
                                                        appt_time11a.setBackgroundColor(getResources().getColor(R.color.white));
                                                        appt_time11a.setClickable(true);
                                                     }

                                                     if(jsonArray1.getJSONObject(i).getInt("aapt_time_id") == 5) {
                                                        appt_time12p.setBackgroundColor(getResources().getColor(R.color.white));
                                                        appt_time12p.setClickable(true);
                                                     }

                                                     if(jsonArray1.getJSONObject(i).getInt("aapt_time_id") == 6) {
                                                        appt_time1p.setBackgroundColor(getResources().getColor(R.color.white));
                                                         appt_time1p.setClickable(true);
                                                     }

                                                     if(jsonArray1.getJSONObject(i).getInt("aapt_time_id") == 7) {
                                                        appt_time2p.setBackgroundColor(getResources().getColor(R.color.white));
                                                         appt_time2p.setClickable(true);
                                                     }

                                                     if(jsonArray1.getJSONObject(i).getInt("aapt_time_id") == 8) {
                                                        appt_time3p.setBackgroundColor(getResources().getColor(R.color.white));
                                                         appt_time3p.setClickable(true);
                                                     }

                                                     if(jsonArray1.getJSONObject(i).getInt("aapt_time_id") == 9) {
                                                        appt_time4p.setBackgroundColor(getResources().getColor(R.color.white));
                                                         appt_time4p.setClickable(true);
                                                     }

                                                     if(jsonArray1.getJSONObject(i).getInt("aapt_time_id") == 10) {
                                                        appt_time5p.setBackgroundColor(getResources().getColor(R.color.white));
                                                         appt_time5p.setClickable(true);
                                                     }

                                                     if(jsonArray1.getJSONObject(i).getInt("aapt_time_id") == 11) {
                                                        appt_time6p.setBackgroundColor(getResources().getColor(R.color.white));
                                                         appt_time6p.setClickable(true);
                                                     }

                                                     if(jsonArray1.getJSONObject(i).getInt("aapt_time_id") == 12) {
                                                        appt_time7p.setBackgroundColor(getResources().getColor(R.color.white));
                                                         appt_time7p.setClickable(true);
                                                     }

                                                     if(jsonArray1.getJSONObject(i).getInt("aapt_time_id") == 13) {
                                                        appt_time8p.setBackgroundColor(getResources().getColor(R.color.white));
                                                         appt_time8p.setClickable(true);
                                                     }
                                                }
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
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put(APIParam.KEY_API,APIClass.API_KEY);
                        map.put(APIParam.KEY_DOCTOR_ID, String.valueOf(DOC_ID));
                        map.put(APIParam.KEY_APPOINT_DATE_ID, appoint_date_id);
                        map.put(APIParam.KEY_HOSPITAL_ID, String.valueOf(hospital_id));
                        return map;
                    }
                };

                stringRequest.setRetryPolicy(policy);

                RequestQueue requestQueue = AppController.getInstance(BookAppointmentActivity.this).
                        getRequestQueue();
                AppController.getInstance(BookAppointmentActivity.this).addToRequestQueue(stringRequest);

                return null;
            }
        }
    }
}
