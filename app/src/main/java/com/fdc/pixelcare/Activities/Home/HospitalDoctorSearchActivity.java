package com.fdc.pixelcare.Activities.Home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.Adapters.Home.ConsultedDoctorsAdapter;
import com.fdc.pixelcare.Adapters.Home.DoctorsSeachMapAdapter;
import com.fdc.pixelcare.DataModel.CitiesList;
import com.fdc.pixelcare.DataModel.DoctorList;
import com.fdc.pixelcare.DataModel.FamilyMember;
import com.fdc.pixelcare.DataModel.HospitalList;
import com.fdc.pixelcare.DataModel.SpecializationList;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppController;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
import com.fdc.pixelcare.Views.CustomTextViewItalic;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SALMA on 21-12-2018.
 */
public class HospitalDoctorSearchActivity extends AppCompatActivity implements View.OnClickListener {

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

    AutoCompleteTextView _header_seach_autotext;
    ImageView _header_search_cancel;

    List<DoctorList> doctorListArraylist = new ArrayList<>();
    RecyclerView recyclerView_doctorlist;
    CustomTextViewItalic no_data;
    Gson gson;

    List<DoctorList> doctorsListArraylist;
    ConsultedDoctorsAdapter adapter;
    ArrayList<SpecializationList> specilizationDocArraylist;
    ArrayList<HospitalList> hospitalDocArraylist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);

        memberArraylist = new ArrayList<>();
        doctorsListArraylist = new ArrayList<>();
        specilizationDocArraylist  = new ArrayList<>();
        hospitalDocArraylist = new ArrayList<>();
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

        shareadPreferenceClass = new ShareadPreferenceClass(HospitalDoctorSearchActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(HospitalDoctorSearchActivity.this);

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

            Log.d(AppUtils.TAG, " *********** HospitalDoctorSearchActivity **************** ");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializationViews(Bundle savedInstanceState) {

        doctorListArraylist = new ArrayList<>();
        doctorListArraylist = new ArrayList<>();
        _header_seach_autotext = (AutoCompleteTextView) findViewById(R.id.search_autocompletetext);
        _header_seach_autotext.setHint("Search Doctor Here...");
        _header_search_cancel = (ImageView) findViewById(R.id.search_cancel);
        _header_search_cancel.setOnClickListener(this);
        _header_search_cancel.setVisibility(View.GONE);


        recyclerView_doctorlist = (RecyclerView) findViewById(R.id.search_recycleriew);
        no_data = (CustomTextViewItalic) findViewById(R.id.search_noresult);
        no_data.setVisibility(View.GONE);

        adapter = new ConsultedDoctorsAdapter(HospitalDoctorSearchActivity.this,doctorListArraylist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HospitalDoctorSearchActivity.this);
        recyclerView_doctorlist.setLayoutManager(mLayoutManager);
        recyclerView_doctorlist.setItemAnimator(new DefaultItemAnimator());
        recyclerView_doctorlist.setHasFixedSize(true);
        recyclerView_doctorlist.setItemViewCacheSize(20);
        recyclerView_doctorlist.setDrawingCacheEnabled(true);
        recyclerView_doctorlist.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        _header_seach_autotext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    _header_search_cancel.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    String search_result = _header_seach_autotext.getText().toString();
                    collectDoctorsSearchResultFromServer(search_result);
                    return true;
                }
                return false;
            }
        });

        _header_seach_autotext.setThreshold(1);//will start working from first character
        _header_seach_autotext.setTextColor(getResources().getColor(R.color.colorPrimary));
        final DoctorsSeachMapAdapter adapter = new DoctorsSeachMapAdapter(HospitalDoctorSearchActivity.this, R.layout.activity_search_map_item, R.id.lbl_name, doctorListArraylist);
        _header_seach_autotext.setAdapter(adapter);

        _header_seach_autotext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.notifyDataSetChanged();
                _header_search_cancel.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_cancel:
                _header_search_cancel.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                adapter = new ConsultedDoctorsAdapter(HospitalDoctorSearchActivity.this, doctorListArraylist);
                recyclerView_doctorlist.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void collectDoctorsSearchResultFromServer(final String SEARCH_QUERY) {

        final ProgressDialog progressDialog = new ProgressDialog(HospitalDoctorSearchActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Searching Result...");
        progressDialog.show();

        doctorListArraylist.clear();
        specilizationDocArraylist = new ArrayList<>();
        hospitalDocArraylist = new ArrayList<>();
        doctorListArraylist = new ArrayList<>();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_HOSP_DOC_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(AppUtils.TAG, " list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1, jsonArray2;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("doctor_list");
                                    if (jsonArray.length() > 0) {
                                        no_data.setVisibility(View.GONE);

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

                                            Log.d(AppUtils.TAG, " initspeclist: "+ specilizationDocArraylist.size());

                                            doctorListArraylist.add(new DoctorList(jObject.getInt("ref_id"),jObject.getString("ref_name"),
                                                    jObject.getString("doc_photo"),jObject.getString("doc_city"),
                                                    jObject.getString("doc_interest"),jObject.getString("doc_exp"),
                                                    jObject.getString("doc_qual"),jObject.getString("doc_encyid"),jObject.getString("geo_latitude"),
                                                    jObject.getString("geo_longitude"), jObject.getInt("doc_consulted"), specilizationDocArraylist, hospitalDocArraylist));
                                        }

                                        refreshRecyclerViews(doctorListArraylist);
                                    }
                                    else {
                                        no_data.setVisibility(View.VISIBLE);
                                        no_data.setText("No Doctor Records Found !!!");
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
                map.put(APIParam.KEY_API, APIClass.API_KEY);
                map.put(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID));
                map.put("search_string", SEARCH_QUERY);
                map.put(APIParam.KEY_HOSPITAL_ID, String.valueOf(HOSPITAL_ID));
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(HospitalDoctorSearchActivity.this).
                getRequestQueue();
        AppController.getInstance(HospitalDoctorSearchActivity.this).addToRequestQueue(stringRequest);
    }

    private void refreshRecyclerViews(List<DoctorList> doctorsListArraylist) {

        adapter = new ConsultedDoctorsAdapter(HospitalDoctorSearchActivity.this, doctorsListArraylist);
        recyclerView_doctorlist.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
