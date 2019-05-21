package com.fdc.pixelcare.Activities.Home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.Adapters.Home.DoctorsListAdapter;
import com.fdc.pixelcare.DataModel.CitiesList;
import com.fdc.pixelcare.DataModel.DoctorList;
import com.fdc.pixelcare.DataModel.FamilyMember;
import com.fdc.pixelcare.DataModel.HospitalList;
import com.fdc.pixelcare.DataModel.SpecializationList;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.Network.NetworkUtil;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppController;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
import com.fdc.pixelcare.Views.CustomTextView;
import com.fdc.pixelcare.Views.CustomTextViewBold;
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

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

/**
 * Created by SALMA on 21-12-2018.
 */
public class HospitalMapDoctorsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int USER_ID, USER_LOGINTYPE, LOGIN_MEMBER_ID, HOSPITAL_ID;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION, LOGIN_MEMBER_NAME, FAMILY_MEMBER_LIST, DOCTORS_MAP_LIST, CITIES_LIST, SPECIALIZATION_LIST;
    ArrayList<FamilyMember> memberArraylist;
    List<CitiesList> citiesArraylist;
    List<SpecializationList> specializationListArraylist;
    int PAGINATION_NEXT = 1;
    int SELECTED_CITY_ID = 0, SELECTED_SPEC_ID = 0, SELECTED_FILTER_TYPE = 0;
    double SELECTED_GEO_LATITUDE = 0.00, SELECTED_GEO_LONGITUDE = 0.00;


    List<DoctorList> doctorsListArraylist;
    ArrayList<SpecializationList> specilizationDocArraylist;
    ArrayList<HospitalList> hospitalDocArraylist;
    List<DoctorList> doctorsViewListArraylist;

    private int pageCount = 0;
    private DoctorsListAdapter doctorsAdapter;
    private ListView listView;
    private ProgressDialog dialog;
    private String url_page1 = "";
    RelativeLayout progress_layout;
    ArrayList<String> citiesList;
    CustomTextViewItalicBold empty_dats;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_doc_list);

        memberArraylist = new ArrayList<>();
        doctorsListArraylist = new ArrayList<>();
        doctorsViewListArraylist = new ArrayList<>();
        LOGIN_MEMBER_ID = 0;
        LOGIN_MEMBER_NAME = "";

        memberArraylist = new ArrayList<>();
        citiesArraylist = new ArrayList<>();
        specializationListArraylist  = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String title = bundle.getString("title");
            HOSPITAL_ID = bundle.getInt("HOSPITAL_ID");
            setTitle(title);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(HospitalMapDoctorsActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(HospitalMapDoctorsActivity.this);

        if (sharedPreferences != null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);
            LOGIN_MEMBER_ID = sharedPreferences.getInt(MHConstants.PREF_LOGIN_MEMBER_ID, 0);
            LOGIN_MEMBER_NAME = sharedPreferences.getString(MHConstants.PREF_LOGIN_MEMBER_NEME, "");
            FAMILY_MEMBER_LIST = sharedPreferences.getString(MHConstants.PREF_FAMILY_MEMBERS, "");
            CITIES_LIST = sharedPreferences.getString(MHConstants.PREF_CITIES_LIST, "");
            SPECIALIZATION_LIST  = sharedPreferences.getString(MHConstants.PREF_SPECIALIZATION_LIST, "");

            SELECTED_CITY_ID = sharedPreferences.getInt(MHConstants.PREF_CITY_ID, 0);
            SELECTED_SPEC_ID = sharedPreferences.getInt(MHConstants.PREF_SPECIALTY_ID, 0);
            SELECTED_FILTER_TYPE  = sharedPreferences.getInt(MHConstants.PREF_HOME_FILTER, 0);
            SELECTED_GEO_LATITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_MY_LATITUDE, ""));
            SELECTED_GEO_LONGITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_MY_LONGITUDE, ""));

            Log.d(AppUtils.TAG, " *********** HospitalMapDoctorsActivity **************** ");
            Log.d(AppUtils.TAG + " UserID: ", String.valueOf(USER_ID));
            Log.d(AppUtils.TAG + " name: ", USER_NAME);
            Log.d(AppUtils.TAG + " email: ", USER_EMAIL);
            Log.d(AppUtils.TAG + " MOBILE: ", USER_MOBILE);
            Log.d(AppUtils.TAG + " Location: ", USER_LOCATION);
            Log.d(AppUtils.TAG + " LOGIN_MEMBER_ID: ", String.valueOf(LOGIN_MEMBER_ID));
            Log.d(AppUtils.TAG + " LOGIN_MEMB_NAME: ", LOGIN_MEMBER_NAME);
            Log.d(AppUtils.TAG + " HOSPITAL_ID: ", String.valueOf(HOSPITAL_ID));
        }

        initializationViews(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item1 = menu.findItem(R.id.action_filter);
        item1.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.action_doctors);
        item2.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_search:
                // Toast.makeText(DoctorsListActivity.this,"Search doctors", Toast.LENGTH_SHORT).show();
                Intent i2 = new Intent(HospitalMapDoctorsActivity.this, HospitalMapDoctorSearchActivity.class);
                i2.putExtra("title","Doctor Search");
                i2.putExtra("HOSPITAL_ID",HOSPITAL_ID);
                startActivity(i2);
                return true;
            case R.id.action_filter:
                ChooseFilterCustomDialog(citiesArraylist, specializationListArraylist);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializationViews(Bundle savedInstanceState) {

        doctorsListArraylist = new ArrayList<>();
        specilizationDocArraylist = new ArrayList<>();
        hospitalDocArraylist = new ArrayList<>();
        doctorsViewListArraylist = new ArrayList<>();

        progress_layout = (RelativeLayout)  findViewById(R.id.hosp_doclist_progressbar);
        url_page1 = APIClass.DRS_HOST_HOSPITAL_MAP_DOCTORS_LIST;

        listView = (ListView) findViewById(R.id.hosp_doclist_listview);
        setListViewAdapter();
        // getDataFromUrl(url_page1);        // REMOVE For dynamically adding
        listView.setOnScrollListener(onScrollListener());

        empty_dats = (CustomTextViewItalicBold) findViewById(R.id.hosp_doclist_empty);
        empty_dats.setVisibility(View.GONE);

        if (NetworkUtil.getConnectivityStatusString(HospitalMapDoctorsActivity.this).equalsIgnoreCase("enabled")) {
            collectHospitalsListsDetails();
        } else {
            AppUtils.showCustomAlertMessage(HospitalMapDoctorsActivity.this, MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
        }


        Gson gson1 = new Gson();
        if (CITIES_LIST.equals("")) {
            if (NetworkUtil.getConnectivityStatusString(HospitalMapDoctorsActivity.this).equalsIgnoreCase("enabled")) {
                collectCitiesDetails();
            } else {
                AppUtils.showCustomAlertMessage(HospitalMapDoctorsActivity.this, MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            citiesArraylist = gson1.fromJson(CITIES_LIST, new TypeToken<List<CitiesList>>() {
            }.getType());
        }

        Gson gson = new Gson();
        if (SPECIALIZATION_LIST.equals("")) {
            if (NetworkUtil.getConnectivityStatusString(HospitalMapDoctorsActivity.this).equalsIgnoreCase("enabled")) {
                collectSpecializationDetails();
            } else {
                AppUtils.showCustomAlertMessage(HospitalMapDoctorsActivity.this, MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            specializationListArraylist = gson.fromJson(SPECIALIZATION_LIST, new TypeToken<List<SpecializationList>>() {
            }.getType());
        }
    }

    private void collectHospitalsListsDetails() {
        getDataFromUrl(url_page1);
        Log.d(AppUtils.TAG +" URL: ", url_page1);
    }

    private void setListViewAdapter() {
        doctorsListArraylist = new ArrayList<DoctorList>();
        doctorsAdapter = new DoctorsListAdapter(this, R.layout.doctor_list_item, R.id.doc_list_name, doctorsListArraylist);
        listView.setAdapter(doctorsAdapter);
    }

    // calling asynctask to get json data from internet
    private void getDataFromUrl(String url) {
        progress_layout.setVisibility(View.VISIBLE);
        new LoadHospitalDoctorsFromUrlTask(this, url, USER_ID, LOGIN_MEMBER_ID, SELECTED_CITY_ID, SELECTED_FILTER_TYPE, SELECTED_GEO_LATITUDE, SELECTED_GEO_LONGITUDE, SELECTED_SPEC_ID, PAGINATION_NEXT, HOSPITAL_ID).execute();
    }

    private AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
                int count = listView.getCount();

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (listView.getLastVisiblePosition() >= count - threshold && pageCount < doctorsListArraylist.size()) {
                        Log.i(AppUtils.TAG, "loading more data" + doctorsListArraylist.size());
                        Log.d(AppUtils.TAG +" PAGINATION_NEXT: ", String.valueOf(PAGINATION_NEXT));
                        if(PAGINATION_NEXT > 1) {
                            getDataFromUrl(url_page1);
                        }

                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }

        };
    }

    //parsing json after getting from Internet
    public void parseJsonResponse(String result) {
        Log.i(AppUtils.TAG, " RESULT: "+ result);
        // hospitalListArraylist = new ArrayList<>();

        progress_layout.setVisibility(View.GONE);
        pageCount++;
        try {
            JSONObject jsonObject = new JSONObject(result);
            PAGINATION_NEXT = jsonObject.getInt("pagination_val");
            Log.i(AppUtils.TAG, " pagination_val:"+  jsonObject.getInt("pagination_val"));
            JSONArray jsonArray, jsonArray1, jsonArray2;
            jsonArray = jsonObject.getJSONArray("doctor_list");

            if(jsonArray.length() > 0) {
                empty_dats.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);

                    jsonArray1 = jObject.getJSONArray("doc_specializations");
                    if(jsonArray1.length() >0) {
                        specilizationDocArraylist = new ArrayList<>();
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            specilizationDocArraylist.add(new SpecializationList(jsonArray1.getJSONObject(j).getInt("spec_id"),jsonArray1.getJSONObject(j).getString("spec_name"),
                                    jsonArray1.getJSONObject(j).getInt("doc_id"),jsonArray1.getJSONObject(j).getInt("doc_type"),jsonArray1.getJSONObject(j).getInt("spec_group_id")));
                        }
                    }

                    jsonArray2 = jObject.getJSONArray("doc_hospitals");
                    if(jsonArray2.length() >0) {
                        hospitalDocArraylist = new ArrayList<>();
                        for (int k = 0; k < jsonArray2.length(); k++) {
                            hospitalDocArraylist.add(new HospitalList(jsonArray2.getJSONObject(k).getInt("hosp_id"),
                                    jsonArray2.getJSONObject(k).getString("hosp_name"),
                                    jsonArray2.getJSONObject(k).getString("hosp_addrs"),jsonArray2.getJSONObject(k).getString("hosp_city"),
                                    jsonArray2.getJSONObject(k).getString("hosp_state"),jsonArray2.getJSONObject(k).getString("hosp_country"),
                                    jsonArray2.getJSONObject(k).getInt("doc_id")));
                        }

                    }

                    doctorsListArraylist.add(new DoctorList(jObject.getInt("ref_id"),jObject.getString("ref_name"),
                            jObject.getString("doc_photo"),jObject.getString("doc_city"),
                            jObject.getString("doc_interest"),jObject.getString("doc_exp"),
                            jObject.getString("doc_qual"),jObject.getString("doc_encyid"),jObject.getString("geo_latitude"),
                            jObject.getString("geo_longitude"), jObject.getInt("doc_consulted"), specilizationDocArraylist, hospitalDocArraylist));

                }

                Log.d(AppUtils.TAG, " resultsize: " + doctorsListArraylist.size());

                prepareDoctorsLists(doctorsListArraylist);
               //  doctorsAdapter.notifyDataSetChanged();
            }
            else {
                empty_dats.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }


            if (dialog != null) {
                dialog.dismiss();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void prepareDoctorsLists(List<DoctorList> doctorsListArraylist) {
        doctorsViewListArraylist = new ArrayList<>();

        for (int i = 0; i < doctorsListArraylist.size(); i++) {
            if (doctorsListArraylist.get(i).getDocConsulted() == 1) {
                doctorsViewListArraylist.add(new DoctorList(doctorsListArraylist.get(i).getDocId(), doctorsListArraylist.get(i).getDocName(),
                        doctorsListArraylist.get(i).getDocPhoto(), doctorsListArraylist.get(i).getDocCity(), doctorsListArraylist.get(i).getDocAreaInterest(),
                        doctorsListArraylist.get(i).getDocExperience(), doctorsListArraylist.get(i).getDocQualification(), doctorsListArraylist.get(i).getDocEncryptID(),
                        doctorsListArraylist.get(i).getDocGeoLatitude(), doctorsListArraylist.get(i).getDocGeoLongitude(), doctorsListArraylist.get(i).getDocConsulted(),
                        doctorsListArraylist.get(i).getDocSpecialization(), doctorsListArraylist.get(i).getDocHospital()));
            }
        }

        for (int i = 0; i < doctorsListArraylist.size(); i++) {
            if (doctorsListArraylist.get(i).getDocConsulted() == 0) {
                doctorsViewListArraylist.add(new DoctorList(doctorsListArraylist.get(i).getDocId(), doctorsListArraylist.get(i).getDocName(),
                        doctorsListArraylist.get(i).getDocPhoto(), doctorsListArraylist.get(i).getDocCity(), doctorsListArraylist.get(i).getDocAreaInterest(),
                        doctorsListArraylist.get(i).getDocExperience(), doctorsListArraylist.get(i).getDocQualification(), doctorsListArraylist.get(i).getDocEncryptID(),
                        doctorsListArraylist.get(i).getDocGeoLatitude(), doctorsListArraylist.get(i).getDocGeoLongitude(), doctorsListArraylist.get(i).getDocConsulted(),
                        doctorsListArraylist.get(i).getDocSpecialization(), doctorsListArraylist.get(i).getDocHospital()));
            }
        }

        doctorsAdapter = new DoctorsListAdapter(this, R.layout.doctor_list_item, R.id.doc_list_name, doctorsViewListArraylist);
        listView.setAdapter(doctorsAdapter);
        doctorsAdapter.notifyDataSetChanged();
    }

    private void collectSpecializationDetails() {
        final ProgressDialog progressDialog1 = new ProgressDialog(HospitalMapDoctorsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog1.setIndeterminate(true);
        progressDialog1.setMessage("Loading Specialization...");
        progressDialog1.show();

        specializationListArraylist = new ArrayList<>();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_SPECIALIZATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(AppUtils.TAG, "spec list: "+ response.toString());
                        if (response != null) {
                            JSONArray jsonArray = null;
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("false")) {
                                } else {
                                    jsonArray = jsonObject.getJSONArray("specialization_details");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            specializationListArraylist.add(new SpecializationList(jsonArray.getJSONObject(i).getInt("spec_id"), jsonArray.getJSONObject(i).getString("spec_name")));
                                        }

                                        //ChooseFilterCustomDialog(citiesArraylist, specializationListArraylist);
                                    }

                                    if(specializationListArraylist.size() > 0) {
                                        Gson  gson = new Gson();
                                        String jsonText = gson.toJson(specializationListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearSpecializationLists();
                                            shareadPreferenceClass.setSpecializationList(jsonText);
                                        }
                                    }
                                }
                                progressDialog1.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog1.dismiss();
                            }

                        }else {
                            progressDialog1.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog1.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIParam.KEY_API, APIClass.API_KEY);

                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(HospitalMapDoctorsActivity.this).
                getRequestQueue();
        AppController.getInstance(HospitalMapDoctorsActivity.this).addToRequestQueue(stringRequest);
    }

    private void collectCitiesDetails() {
        citiesArraylist = new ArrayList<>();

        final ProgressDialog progressDialog1 = new ProgressDialog(HospitalMapDoctorsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog1.setIndeterminate(true);
        progressDialog1.setMessage("Loading cities lists...");
        progressDialog1.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_CITIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d(AppUtils.TAG, " cityList: "+ response.toString());
                        if (response != null) {
                            JSONArray jsonArray = null;
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("false")) {
                                } else {
                                    jsonArray = jsonObject.getJSONArray("cities_details");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            citiesArraylist.add(new CitiesList(jsonArray.getJSONObject(i).getInt("city_id"),jsonArray.getJSONObject(i).getString("city_name"),
                                                    jsonArray.getJSONObject(i).getString("latitude"), jsonArray.getJSONObject(i).getString("longitude"),
                                                    jsonArray.getJSONObject(i).getString("state")));
                                        }

                                        Log.d(AppUtils.TAG, " size: "+ citiesArraylist.size());
                                        if(citiesArraylist.size() > 0) {
                                            Gson  gson = new Gson();
                                            String jsonText = gson.toJson(citiesArraylist);
                                            if (sharedPreferences != null) {
                                                shareadPreferenceClass.clearCitiesLists();
                                                shareadPreferenceClass.setCitiesList(jsonText);
                                            }
                                        }

                                        //    ChooseFilterCustomDialog(citiesArraylist, specializationListArraylist);
                                    }

                                }
                                progressDialog1.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog1.dismiss();
                            }

                        }else {
                            progressDialog1.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog1.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIParam.KEY_API, APIClass.API_KEY);
                return map;
            }
        };
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = AppController.getInstance(HospitalMapDoctorsActivity.this).getRequestQueue();
        AppController.getInstance(HospitalMapDoctorsActivity.this).addToRequestQueue(stringRequest);
    }

    private void ChooseFilterCustomDialog(List<CitiesList> citiesArraylist, List<SpecializationList> specializationListArraylist) {

        final Dialog dialog = new Dialog(HospitalMapDoctorsActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_map_filter_new);

        final LinearLayout city_btn = (LinearLayout) dialog.findViewById(R.id.hosp_map_filter_city);
        final CustomTextView city_names_text = (CustomTextView) dialog.findViewById(R.id.hosp_map_filter_names);

        final LinearLayout specialty_btn = (LinearLayout) dialog.findViewById(R.id.hosp_map_filter_specialty);
        final CustomTextView specialty_names_text = (CustomTextView) dialog.findViewById(R.id.hosp_map_filter_specialtynames);

        CustomTextViewBold filter_submit = (CustomTextViewBold) dialog.findViewById(R.id.hosp_map_filter_submit);

        ArrayList<String> city_names = new ArrayList<String>();
        final ArrayList<Integer> city_id = new ArrayList<Integer>();
        for(int i=0; i<citiesArraylist.size(); i++) {
            city_names.add(citiesArraylist.get(i).getCityName());
            city_id.add(citiesArraylist.get(i).getCityId());

            if((SELECTED_CITY_ID >0 || SELECTED_CITY_ID !=0) && SELECTED_CITY_ID == citiesArraylist.get(i).getCityId()) {
                city_names_text.setText("Your Location: "+citiesArraylist.get(i).getCityName());
            }

        }

        SpinnerDialog spinnerDialog = new SpinnerDialog(HospitalMapDoctorsActivity.this,city_names,"Select or Search City","Close Button Text");// With No Animation
        spinnerDialog = new SpinnerDialog(HospitalMapDoctorsActivity.this,city_names,"Select or Search City",R.style.DialogAnimations_SmileWindow,"CLOSE");// With  Animation

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                city_names_text.setText("Your Location: "+item);
                Log.d(AppUtils.TAG, " name: "+ item+" id: "+ city_id.get(position).toString());
                specialty_names_text.setText("");
                SELECTED_SPEC_ID = 0;
                SELECTED_CITY_ID = city_id.get(position);

                SELECTED_FILTER_TYPE = 3;
            }
        });

        final SpinnerDialog finalSpinnerDialog = spinnerDialog;
        city_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalSpinnerDialog.showSpinerDialog();
            }
        });

        ArrayList<String> spec_names = new ArrayList<String>();
        final ArrayList<Integer> spec_id = new ArrayList<Integer>();
        for(int i=0; i<specializationListArraylist.size(); i++) {
            spec_names.add(specializationListArraylist.get(i).getSpecializationName());
            spec_id.add(specializationListArraylist.get(i).getSpecializationId());
        }

        SpinnerDialog spinnerDialog1 = new SpinnerDialog(HospitalMapDoctorsActivity.this,spec_names,"Select or Search Specialty","Close Button Text");// With No Animation
        spinnerDialog1 = new SpinnerDialog(HospitalMapDoctorsActivity.this,spec_names,"Select or Search Specialty",R.style.DialogAnimations_SmileWindow,"CLOSE");// With  Animation

        spinnerDialog1.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                //  Toast.makeText(DashboardActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                // selectedItems.setText(item + " Position: " + position);
                specialty_names_text.setText("Specialty: "+item);
                Log.d(AppUtils.TAG, " name: "+ item+" id: "+ spec_id.get(position).toString());
                SELECTED_SPEC_ID = spec_id.get(position);

                if(city_names_text.getText().toString().equals("")) {
                    SELECTED_FILTER_TYPE = 4;
                }
                else {
                    SELECTED_FILTER_TYPE = 5;
                }

            }
        });

        final SpinnerDialog finalSpinnerDialog1 = spinnerDialog1;
        specialty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalSpinnerDialog1.showSpinerDialog();
            }
        });

        filter_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(AppUtils.TAG, "*********** Filter Submit ************* ");
                Log.d(AppUtils.TAG, " sel_city_id: " + SELECTED_CITY_ID);
                Log.d(AppUtils.TAG, " sel_spec_id: " + SELECTED_SPEC_ID);
                Log.d(AppUtils.TAG, " sel_geo_latitude: " + SELECTED_GEO_LATITUDE);
                Log.d(AppUtils.TAG, " sel_geo_longitude: " + SELECTED_GEO_LONGITUDE);
                Log.d(AppUtils.TAG, " sel_filter_type: " + SELECTED_FILTER_TYPE);

              /*  if(sharedPreferences != null) {
                    shareadPreferenceClass.clearCityID();
                    shareadPreferenceClass.setCityID(SELECTED_CITY_ID);

                    shareadPreferenceClass.clearSpecialtyID();
                    shareadPreferenceClass.setSpecialtyID(SELECTED_SPEC_ID);

                    shareadPreferenceClass.clearMyLatitude();
                    shareadPreferenceClass.setMyLatitude(String.valueOf(SELECTED_GEO_LATITUDE));

                    shareadPreferenceClass.clearMyLongitude();
                    shareadPreferenceClass.setMyLongitude(String.valueOf(SELECTED_GEO_LONGITUDE));

                    shareadPreferenceClass.clearFilterType();
                    shareadPreferenceClass.setFilterType(SELECTED_FILTER_TYPE);
                }*/

                dialog.dismiss();

                if (NetworkUtil.getConnectivityStatusString(HospitalMapDoctorsActivity.this).equalsIgnoreCase("enabled")) {
                    doctorsAdapter.clear();
                    doctorsAdapter.notifyDataSetChanged();
                    collectHospitalsListsDetails();
                } else {
                    AppUtils.showCustomAlertMessage(HospitalMapDoctorsActivity.this, MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
                }

            }
        });

        dialog.show();
    }
}
