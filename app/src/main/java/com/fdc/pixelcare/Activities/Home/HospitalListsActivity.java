package com.fdc.pixelcare.Activities.Home;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.Adapters.Home.HospitalListAdapter;
import com.fdc.pixelcare.DataModel.CitiesList;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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
 * Created by SALMA on 22-12-2018.
 */
public class HospitalListsActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.OnConnectionFailedListener {

    int USER_ID, USER_LOGINTYPE;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION, FAMILY_MEMBER_LIST, CITIES_LIST, SPECIALIZATION_LIST;
    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    ArrayList<FamilyMember> memberArraylist;
    List<CitiesList> citiesArraylist;
    List<SpecializationList> specializationListArraylist;
    int LOGIN_MEMBER_ID;
    String LOGIN_MEMBER_NAME;
    int PAGINATION_NEXT = 1;
    int SELECTED_CITY_ID = 0, SELECTED_SPEC_ID = 0, SELECTED_FILTER_TYPE = 0;
    double SELECTED_GEO_LATITUDE = 0.00, SELECTED_GEO_LONGITUDE = 0.00;

    private int pageCount = 0;
    private HospitalListAdapter hospitalAdapter;
    private ListView listView;
    private ProgressDialog dialog;
    List<HospitalList> hospitalListArraylist;

    List<HospitalList> hospitalVisibleListArraylist;

    private String url_page1 = "";
    RelativeLayout progress_layout;
    ArrayList<String> citiesList;
    CustomTextViewItalicBold no_data;

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";
    LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    double GEO_LATITUDE = 0.0, GEO_LONGITUDE = 0.0, RADIUS, selected_radius;
    double CHOOSEN_LOCATION_LATITUDE = 0.00, CHOOSEN_LOCATION_LONGITUDE = 0.00;
    CustomTextView gpsSelectedItemLocation;
    CustomTextViewItalicBold text_current_location, text_current_specialty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_hosplist);

        memberArraylist = new ArrayList<>();
        hospitalListArraylist = new ArrayList<>();
        citiesArraylist = new ArrayList<>();
        specializationListArraylist = new ArrayList<>();

        hospitalVisibleListArraylist = new ArrayList<>();
        CHOOSEN_LOCATION_LATITUDE = 0.00;
        CHOOSEN_LOCATION_LONGITUDE = 0.00;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(HospitalListsActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(HospitalListsActivity.this);

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
            SPECIALIZATION_LIST = sharedPreferences.getString(MHConstants.PREF_SPECIALIZATION_LIST, "");

            SELECTED_CITY_ID = sharedPreferences.getInt(MHConstants.PREF_CITY_ID, 0);
            SELECTED_SPEC_ID = sharedPreferences.getInt(MHConstants.PREF_SPECIALTY_ID, 0);
            SELECTED_FILTER_TYPE = sharedPreferences.getInt(MHConstants.PREF_HOME_FILTER, 0);
            SELECTED_GEO_LATITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_MY_LATITUDE, "0.00"));
            SELECTED_GEO_LONGITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_MY_LONGITUDE, "0.00"));
            CHOOSEN_LOCATION_LATITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_CHOOSEN_LATITUDE, "0.00"));
            CHOOSEN_LOCATION_LONGITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_CHOOSEN_LONGITUDE, "0.00"));


            Log.d(AppUtils.TAG, " *********** HospitalListsActivity **************** ");
            Log.d(AppUtils.TAG + " UserID: ", String.valueOf(USER_ID));
            Log.d(AppUtils.TAG + " name: ", USER_NAME);
            Log.d(AppUtils.TAG + " email: ", USER_EMAIL);
            Log.d(AppUtils.TAG + " MOBILE: ", USER_MOBILE);
            Log.d(AppUtils.TAG + " Location: ", USER_LOCATION);
            Log.d(AppUtils.TAG + " LOGIN_MEMBER_ID: ", String.valueOf(LOGIN_MEMBER_ID));
            Log.d(AppUtils.TAG + " LOGIN_MEMB_NAME: ", LOGIN_MEMBER_NAME);
            Log.d(AppUtils.TAG + " logintype: ", String.valueOf(USER_LOGINTYPE));

            Log.d(AppUtils.TAG, "SELECTED_CITY_ID: " + SELECTED_CITY_ID);
            Log.d(AppUtils.TAG, "SELECTED_SPEC_ID: " + SELECTED_SPEC_ID);
            Log.d(AppUtils.TAG, "SELECTED_GEO_LATITUDE: " + SELECTED_GEO_LATITUDE);
            Log.d(AppUtils.TAG, "SELECTED_GEO_LONGITUDE: " + SELECTED_GEO_LONGITUDE);
            Log.d(AppUtils.TAG, "SELECTED_FILTER_TYPE: " + SELECTED_FILTER_TYPE);
            Log.d(AppUtils.TAG, "CHOOSEN_LOCATION_LATITUDE: " + CHOOSEN_LOCATION_LATITUDE);
            Log.d(AppUtils.TAG, "CHOOSEN_LOCATION_LONGITUDE: " + CHOOSEN_LOCATION_LONGITUDE);
        }
        initializeViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_search:
                // Toast.makeText(DoctorsListActivity.this,"Search doctors", Toast.LENGTH_SHORT).show();
                Intent i2 = new Intent(HospitalListsActivity.this, HospitalSearchActivity.class);
                i2.putExtra("title", "Hospital Search");
                startActivity(i2);
                return true;
            case R.id.action_filter:
                ChooseFilterCustomDialog(citiesArraylist, specializationListArraylist);
                return true;
            case R.id.action_doctors:
                Intent i1 = new Intent(HospitalListsActivity.this, HospitalMapDoctorsActivity.class);
                i1.putExtra("title", "View Doctors");
                startActivity(i1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeViews() {
        hospitalVisibleListArraylist = new ArrayList<>();
        progress_layout = (RelativeLayout) findViewById(R.id.hosplist_progressbar);
        url_page1 = APIClass.DRS_HOST_HOSPITALS_MAP_NEW_LIST;

        listView = (ListView) findViewById(R.id.hosplist_listview);
        setListViewAdapter();
        // getDataFromUrl(url_page1);        // REMOVE For dynamically adding
        listView.setOnScrollListener(onScrollListener());

        no_data = (CustomTextViewItalicBold) findViewById(R.id.hosplist_empty);
        no_data.setVisibility(View.GONE);

        text_current_location = (CustomTextViewItalicBold) findViewById(R.id.current_location);
        text_current_specialty = (CustomTextViewItalicBold) findViewById(R.id.current_specialty);
        text_current_location.setText("Location: "+SELECTED_GEO_LATITUDE+", "+SELECTED_GEO_LONGITUDE);
        text_current_specialty.setText("Specialty: "+ "All");

        /*Gson gson = new Gson();
        if (DOCTORS_LIST.equals("")) {
            if (NetworkUtil.getConnectivityStatusString(DoctorsListActivity.this).equalsIgnoreCase("enabled")) {
                collectDoctorsListsDetails();
            } else {
                AppUtils.showCustomAlertMessage(DoctorsListActivity.this, MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            doctorsListArraylist = gson.fromJson(DOCTORS_LIST, new TypeToken<List<DoctorList>>() {
            }.getType());
            if(doctorsListArraylist.size() > 0 ) {
                PAGINATION_NEXT = 2;            // temporary value
                progress_layout.setVisibility(View.GONE);       // TEMPORARY
                prepareDoctorsLists(doctorsListArraylist);
            }
        }*/


        Gson gson1 = new Gson();
        if (CITIES_LIST.equals("")) {
            if (NetworkUtil.getConnectivityStatusString(HospitalListsActivity.this).equalsIgnoreCase("enabled")) {
                collectCitiesDetails();
            } else {
                AppUtils.showCustomAlertMessage(HospitalListsActivity.this, MHConstants.INTERNET, MHConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            citiesArraylist = gson1.fromJson(CITIES_LIST, new TypeToken<List<CitiesList>>() {
            }.getType());
        }

        Gson gson = new Gson();
        if (SPECIALIZATION_LIST.equals("")) {
            if (NetworkUtil.getConnectivityStatusString(HospitalListsActivity.this).equalsIgnoreCase("enabled")) {
                collectSpecializationDetails();
            } else {
                AppUtils.showCustomAlertMessage(HospitalListsActivity.this, MHConstants.INTERNET, MHConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            specializationListArraylist = gson.fromJson(SPECIALIZATION_LIST, new TypeToken<List<SpecializationList>>() {
            }.getType());
        }

        if (NetworkUtil.getConnectivityStatusString(HospitalListsActivity.this).equalsIgnoreCase("enabled")) {
            collectHospitalsListsDetails();
        } else {
            AppUtils.showCustomAlertMessage(HospitalListsActivity.this, MHConstants.INTERNET, MHConstants.INTERNET_CHECK, "OK", null, null);
        }

    }

    private void collectHospitalsListsDetails() {
        if(sharedPreferences != null) {
            SELECTED_CITY_ID = sharedPreferences.getInt(MHConstants.PREF_CITY_ID, 0);
            SELECTED_SPEC_ID = sharedPreferences.getInt(MHConstants.PREF_SPECIALTY_ID, 0);
            SELECTED_FILTER_TYPE = sharedPreferences.getInt(MHConstants.PREF_HOME_FILTER, 0);
            SELECTED_GEO_LATITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_MY_LATITUDE, "0.00"));
            SELECTED_GEO_LONGITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_MY_LONGITUDE, "0.00"));
            CHOOSEN_LOCATION_LATITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_CHOOSEN_LATITUDE, "0.00"));
            CHOOSEN_LOCATION_LONGITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_CHOOSEN_LONGITUDE, "0.00"));
            Log.d(AppUtils.TAG, " SELECTED_SPEC_ID"+SELECTED_SPEC_ID);

            if(SELECTED_FILTER_TYPE == 1) {
                text_current_location.setText("Location: "+ String.format("%.2f", SELECTED_GEO_LATITUDE) +", "+String.format("%.2f", SELECTED_GEO_LONGITUDE));

                if(specializationListArraylist.size() > 0) {
                    String specialty_name = "All";
                    for(int i=0;i<specializationListArraylist.size();i++) {
                        if(SELECTED_SPEC_ID == specializationListArraylist.get(i).getSpecializationId() && SELECTED_SPEC_ID !=0) {
                            specialty_name = specializationListArraylist.get(i).getSpecializationName();
                            Log.d(AppUtils.TAG, " specialty_name"+specialty_name);
                        }
                    }
                    text_current_specialty.setText("Specialty: "+ specialty_name);
                }
            }
            else  if(SELECTED_FILTER_TYPE == 2) {
                if(SELECTED_GEO_LATITUDE != 0.0 && SELECTED_GEO_LONGITUDE !=0.0 && SELECTED_CITY_ID != 0) {
                    if(citiesArraylist.size() > 0 && SELECTED_CITY_ID !=0 ) {
                        String city_name = String.format("%.2f", CHOOSEN_LOCATION_LATITUDE) +", "+ String.format("%.2f", CHOOSEN_LOCATION_LONGITUDE);
                        for(int j=0;j<citiesArraylist.size();j++) {
                            if(SELECTED_CITY_ID == citiesArraylist.get(j).getCityId()) {
                                city_name = citiesArraylist.get(j).getCityName();
                                Log.d(AppUtils.TAG, " city_name"+city_name);
                            }
                        }
                        text_current_location.setText("Location: "+ city_name);
                    }
                    else {
                        text_current_location.setText("Location: "+  String.format("%.2f", CHOOSEN_LOCATION_LATITUDE) +", "+ String.format("%.2f", CHOOSEN_LOCATION_LONGITUDE));
                    }
                }
                else {
                    text_current_location.setText("Location: "+ String.format("%.2f", SELECTED_GEO_LATITUDE) +", "+String.format("%.2f", SELECTED_GEO_LONGITUDE));
                }

                if(specializationListArraylist.size() > 0) {
                    String specialty_name = "All";
                    for(int i=0;i<specializationListArraylist.size();i++) {
                        if(SELECTED_SPEC_ID == specializationListArraylist.get(i).getSpecializationId() && SELECTED_SPEC_ID !=0) {
                            specialty_name = specializationListArraylist.get(i).getSpecializationName();
                            Log.d(AppUtils.TAG, " specialty_name"+specialty_name);
                        }
                    }
                    text_current_specialty.setText("Specialty: "+ specialty_name);
                }

            }
            else if(SELECTED_FILTER_TYPE == 3) {
                if(citiesArraylist.size() > 0 && SELECTED_CITY_ID !=0 ) {
                    String city_name = String.format("%.2f", CHOOSEN_LOCATION_LATITUDE) +", "+ String.format("%.2f", CHOOSEN_LOCATION_LONGITUDE);
                    for(int j=0;j<citiesArraylist.size();j++) {
                        if(SELECTED_CITY_ID == citiesArraylist.get(j).getCityId()) {
                            city_name = citiesArraylist.get(j).getCityName();
                            Log.d(AppUtils.TAG, " city_name"+city_name);
                        }
                    }
                    text_current_location.setText("Location: "+ city_name);
                }
                else {
                    text_current_location.setText("Location: "+  String.format("%.2f", CHOOSEN_LOCATION_LATITUDE) +", "+ String.format("%.2f", CHOOSEN_LOCATION_LONGITUDE));
                }

                if(specializationListArraylist.size() > 0) {
                    String specialty_name = "All";
                    for(int i=0;i<specializationListArraylist.size();i++) {
                        if(SELECTED_SPEC_ID == specializationListArraylist.get(i).getSpecializationId() && SELECTED_SPEC_ID !=0) {
                            specialty_name = specializationListArraylist.get(i).getSpecializationName();
                            Log.d(AppUtils.TAG, " specialty_name"+specialty_name);
                        }
                    }
                    text_current_specialty.setText("Specialty: "+ specialty_name);
                }
            }
            else if(SELECTED_FILTER_TYPE == 4) {
                if(SELECTED_GEO_LATITUDE != 0.0 && SELECTED_GEO_LONGITUDE !=0.0 && SELECTED_CITY_ID == 0) {
                    text_current_location.setText("Location: "+  String.format("%.2f", SELECTED_GEO_LATITUDE) +", "+ String.format("%.2f", SELECTED_GEO_LONGITUDE));
                }
                else if(SELECTED_GEO_LATITUDE == 0.0 && SELECTED_GEO_LONGITUDE == 0.0 && SELECTED_CITY_ID != 0) {
                    if(citiesArraylist.size() > 0 && SELECTED_CITY_ID !=0 ) {
                        String city_name = String.format("%.2f", CHOOSEN_LOCATION_LATITUDE) +", "+ String.format("%.2f", CHOOSEN_LOCATION_LONGITUDE);
                        for(int j=0;j<citiesArraylist.size();j++) {
                            if(SELECTED_CITY_ID == citiesArraylist.get(j).getCityId()) {
                                city_name = citiesArraylist.get(j).getCityName();
                                Log.d(AppUtils.TAG, " city_name"+city_name);
                            }
                        }
                        text_current_location.setText("Location: "+ city_name);
                    }
                    else {
                        text_current_location.setText("Location: "+  String.format("%.2f", CHOOSEN_LOCATION_LATITUDE) +", "+ String.format("%.2f", CHOOSEN_LOCATION_LONGITUDE));
                    }
                }
                else if(SELECTED_GEO_LATITUDE != 0.0 && SELECTED_GEO_LONGITUDE != 0.0 && SELECTED_CITY_ID != 0) {
                    text_current_location.setText("Location: "+  String.format("%.2f", SELECTED_GEO_LATITUDE) +", "+ String.format("%.2f", SELECTED_GEO_LONGITUDE));
                }
                else {
                    text_current_location.setText("Location: "+  String.format("%.2f", CHOOSEN_LOCATION_LATITUDE) +", "+ String.format("%.2f", CHOOSEN_LOCATION_LONGITUDE));
                }

                if(specializationListArraylist.size() > 0) {
                    String specialty_name = "All";
                    for(int i=0;i<specializationListArraylist.size();i++) {
                        if(SELECTED_SPEC_ID == specializationListArraylist.get(i).getSpecializationId() && SELECTED_SPEC_ID !=0) {
                            specialty_name = specializationListArraylist.get(i).getSpecializationName();
                            Log.d(AppUtils.TAG, " specialty_name"+specialty_name);
                        }
                    }
                    text_current_specialty.setText("Specialty: "+ specialty_name);
                }
            }
            else if(SELECTED_FILTER_TYPE == 5) {
                String city_name = String.format("%.2f", CHOOSEN_LOCATION_LATITUDE) +", "+ String.format("%.2f", CHOOSEN_LOCATION_LONGITUDE);
                for(int j=0;j<citiesArraylist.size();j++) {
                    if(SELECTED_CITY_ID == citiesArraylist.get(j).getCityId()) {
                        city_name = citiesArraylist.get(j).getCityName();
                        Log.d(AppUtils.TAG, " city_name"+city_name);
                    }
                }
                text_current_location.setText("Location: "+ city_name);

                if(specializationListArraylist.size() > 0) {
                    String specialty_name = "All";
                    for(int i=0;i<specializationListArraylist.size();i++) {
                        if(SELECTED_SPEC_ID == specializationListArraylist.get(i).getSpecializationId() && SELECTED_SPEC_ID !=0) {
                            specialty_name = specializationListArraylist.get(i).getSpecializationName();
                            Log.d(AppUtils.TAG, " specialty_name"+specialty_name);
                        }
                    }
                    text_current_specialty.setText("Specialty: "+ specialty_name);
                }
            }
        }

        getDataFromUrl(url_page1);
        Log.d(AppUtils.TAG + " URL: ", url_page1);
    }

    private void setListViewAdapter() {
        hospitalListArraylist = new ArrayList<HospitalList>();
        hospitalAdapter = new HospitalListAdapter(this, R.layout.doctor_list_item, R.id.doc_list_name, hospitalListArraylist);
        listView.setAdapter(hospitalAdapter);
    }

    // calling asynctask to get json data from internet
    private void getDataFromUrl(String url) {
        progress_layout.setVisibility(View.VISIBLE);
        new LoadHospitalsFromUrlTask(this, url, USER_ID, LOGIN_MEMBER_ID, SELECTED_CITY_ID, SELECTED_FILTER_TYPE, SELECTED_GEO_LATITUDE, SELECTED_GEO_LONGITUDE, SELECTED_SPEC_ID, PAGINATION_NEXT).execute();
    }

    private AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
                int count = listView.getCount();

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (listView.getLastVisiblePosition() >= count - threshold && pageCount < hospitalListArraylist.size()) {
                        Log.i(AppUtils.TAG, "loading more data" + hospitalListArraylist.size());
                        Log.d(AppUtils.TAG + " PAGINATION_NEXT: ", String.valueOf(PAGINATION_NEXT));
                        if (PAGINATION_NEXT > 1) {
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
        Log.i(AppUtils.TAG, " RESULT:" + result);
        hospitalListArraylist = new ArrayList<>();          // remove this line if pagination added
        hospitalVisibleListArraylist = new ArrayList<>();  // remove this line if pagination added
        progress_layout.setVisibility(View.GONE);
        pageCount++;
        try {
            JSONObject jsonObject = new JSONObject(result);
            PAGINATION_NEXT = jsonObject.getInt("pagination_val");
            Log.i(AppUtils.TAG, " pagination_val:" + jsonObject.getInt("pagination_val"));

            JSONArray jsonArray = jsonObject.getJSONArray("hospital_list");

            if (jsonArray.length() > 0) {
                no_data.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);

                    hospitalListArraylist.add(new HospitalList(jObject.getInt("hosp_id"), jObject.getString("hosp_name"),
                            jObject.getString("hosp_addrs"), jObject.getString("hosp_city"), jObject.getString("hosp_state"),
                            jObject.getString("hosp_country"), jObject.getString("hosp_contact"), jObject.getString("hosp_email"),
                            jObject.getString("geo_latitude"), jObject.getString("geo_longitude"), jObject.getString("hosp_logo"),
                            jObject.getInt("hosp_consulted")));
                }

                Log.d(AppUtils.TAG, " resultsize: " + hospitalListArraylist.size());

                // hospitalAdapter.notifyDataSetChanged();

                prepareHospitalLists(hospitalListArraylist);
            } else {
                listView.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
            }

            if (dialog != null) {
                dialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void prepareHospitalLists(List<HospitalList> hospitalListArraylist) {
        hospitalVisibleListArraylist = new ArrayList<>();

     /*   hospitalAdapter = new HospitalListAdapter(this, R.layout.doctor_list_item, R.id.doc_list_name, hospitalListArraylist);
        listView.setAdapter(hospitalAdapter);
        hospitalAdapter.notifyDataSetChanged();*/
        for (int i = 0; i < hospitalListArraylist.size(); i++) {
            if (hospitalListArraylist.get(i).getHospitalConsulted() == 1) {
                hospitalVisibleListArraylist.add(new HospitalList(hospitalListArraylist.get(i).getHospitalId(), hospitalListArraylist.get(i).getHospitalName(),
                        hospitalListArraylist.get(i).getHospitalAddress(), hospitalListArraylist.get(i).getHospitalCity(), hospitalListArraylist.get(i).getHospitalState(),
                        hospitalListArraylist.get(i).getHospitalCountry(), hospitalListArraylist.get(i).getHospitalMobile(), hospitalListArraylist.get(i).getHospitalEmail(),
                        hospitalListArraylist.get(i).getHospitalGeoLatitude(), hospitalListArraylist.get(i).getHospitalGeoLongitude(), hospitalListArraylist.get(i).getHospitalPhoto(),
                        hospitalListArraylist.get(i).getHospitalConsulted()));
            }
        }

        for (int i = 0; i < hospitalListArraylist.size(); i++) {
            if (hospitalListArraylist.get(i).getHospitalConsulted() == 0) {
                hospitalVisibleListArraylist.add(new HospitalList(hospitalListArraylist.get(i).getHospitalId(), hospitalListArraylist.get(i).getHospitalName(),
                        hospitalListArraylist.get(i).getHospitalAddress(), hospitalListArraylist.get(i).getHospitalCity(), hospitalListArraylist.get(i).getHospitalState(),
                        hospitalListArraylist.get(i).getHospitalCountry(), hospitalListArraylist.get(i).getHospitalMobile(), hospitalListArraylist.get(i).getHospitalEmail(),
                        hospitalListArraylist.get(i).getHospitalGeoLatitude(), hospitalListArraylist.get(i).getHospitalGeoLongitude(), hospitalListArraylist.get(i).getHospitalPhoto(),
                        hospitalListArraylist.get(i).getHospitalConsulted()));
            }
        }

        hospitalAdapter = new HospitalListAdapter(this, R.layout.doctor_list_item, R.id.doc_list_name, hospitalVisibleListArraylist);
        listView.setAdapter(hospitalAdapter);
        hospitalAdapter.notifyDataSetChanged();
    }

    private void collectCitiesDetails() {
        citiesArraylist = new ArrayList<>();

        final ProgressDialog progressDialog1 = new ProgressDialog(HospitalListsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
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
                                            citiesArraylist.add(new CitiesList(jsonArray.getJSONObject(i).getInt("city_id"), jsonArray.getJSONObject(i).getString("city_name"),
                                                    jsonArray.getJSONObject(i).getString("latitude"), jsonArray.getJSONObject(i).getString("longitude"),
                                                    jsonArray.getJSONObject(i).getString("state")));
                                        }

                                        Log.d(AppUtils.TAG, " size: " + citiesArraylist.size());
                                        if (citiesArraylist.size() > 0) {
                                            Gson gson = new Gson();
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

                        } else {
                            progressDialog1.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog1.dismiss();
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
        RequestQueue requestQueue = AppController.getInstance(HospitalListsActivity.this).getRequestQueue();
        AppController.getInstance(HospitalListsActivity.this).addToRequestQueue(stringRequest);
    }

    private void collectSpecializationDetails() {
        final ProgressDialog progressDialog1 = new ProgressDialog(HospitalListsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
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
                        Log.d(AppUtils.TAG, "spec list: " + response.toString());
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

                                    if (specializationListArraylist.size() > 0) {
                                        Gson gson = new Gson();
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

                        } else {
                            progressDialog1.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog1.dismiss();
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

        RequestQueue requestQueue = AppController.getInstance(HospitalListsActivity.this).
                getRequestQueue();
        AppController.getInstance(HospitalListsActivity.this).addToRequestQueue(stringRequest);
    }

    private void ChooseFilterCustomDialog(List<CitiesList> citiesArraylist, List<SpecializationList> specializationListArraylist) {

        final Dialog dialog = new Dialog(HospitalListsActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_map_filter_new);

        LinearLayout detect_gps_location = (LinearLayout) dialog.findViewById(R.id.hosp_map_filter_gps);
        gpsSelectedItemLocation = (CustomTextView) dialog.findViewById(R.id.hosp_map_filter_gps_loc);
        gpsSelectedItemLocation.setVisibility(View.GONE);


        if(SELECTED_GEO_LATITUDE != 0.0 && SELECTED_GEO_LONGITUDE !=0.0  && (SELECTED_FILTER_TYPE ==1  || SELECTED_CITY_ID == 0)) {
            gpsSelectedItemLocation.setVisibility(View.VISIBLE);
            gpsSelectedItemLocation.setText("Current Latitude: " + String.format("%.2f", SELECTED_GEO_LATITUDE) + "\n" + "Current Longitude: " + String.format("%.2f", SELECTED_GEO_LONGITUDE));
          }

        detect_gps_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SELECTED_SPEC_ID =0;
                gpslocationCollect();
            }
        });

        final LinearLayout city_btn = (LinearLayout) dialog.findViewById(R.id.hosp_map_filter_city);
        final CustomTextView city_names_text = (CustomTextView) dialog.findViewById(R.id.hosp_map_filter_names);

       // Log.d(AppUtils.TAG, " test: "+CHOOSEN_LOCATION_LATITUDE+ " new "+CHOOSEN_LOCATION_LONGITUDE);

        if(CHOOSEN_LOCATION_LATITUDE != 0.0 && CHOOSEN_LOCATION_LONGITUDE !=0.0 && SELECTED_CITY_ID != 0 && SELECTED_FILTER_TYPE != 1 ) {
            if (citiesArraylist.size() > 0 && SELECTED_CITY_ID != 0) {
                String city_name = String.format("%.2f", CHOOSEN_LOCATION_LATITUDE) + ", " + String.format("%.2f", CHOOSEN_LOCATION_LONGITUDE);
                for (int j = 0; j < citiesArraylist.size(); j++) {
                    if (SELECTED_CITY_ID == citiesArraylist.get(j).getCityId()) {
                        city_name = citiesArraylist.get(j).getCityName();
                        Log.d(AppUtils.TAG, " city_name" + city_name);
                    }
                }
                city_names_text.setText("Location: " + city_name);
            } else {
                city_names_text.setText("Location: " + String.format("%.2f", CHOOSEN_LOCATION_LATITUDE) + ", " + String.format("%.2f", CHOOSEN_LOCATION_LONGITUDE));
            }
        }

        final LinearLayout specialty_btn = (LinearLayout) dialog.findViewById(R.id.hosp_map_filter_specialty);
        final CustomTextView specialty_names_text = (CustomTextView) dialog.findViewById(R.id.hosp_map_filter_specialtynames);

        if(specializationListArraylist.size() > 0) {
            String specialty_name = "All";
            for(int i=0;i<specializationListArraylist.size();i++) {
                if(SELECTED_SPEC_ID == specializationListArraylist.get(i).getSpecializationId() && SELECTED_SPEC_ID !=0) {
                    specialty_name = specializationListArraylist.get(i).getSpecializationName();
                    Log.d(AppUtils.TAG, " specialty_name"+specialty_name);
                }
            }
            specialty_names_text.setText("Specialty: "+ specialty_name);
        }

        CustomTextViewBold filter_submit = (CustomTextViewBold) dialog.findViewById(R.id.hosp_map_filter_submit);

        ArrayList<String> city_names = new ArrayList<String>();
        final ArrayList<Integer> city_id = new ArrayList<Integer>();
        for (int i = 0; i < citiesArraylist.size(); i++) {
            city_names.add(citiesArraylist.get(i).getCityName());
            city_id.add(citiesArraylist.get(i).getCityId());
        }

        SpinnerDialog spinnerDialog = new SpinnerDialog(HospitalListsActivity.this, city_names, "Select or Search City", "Close Button Text");// With No Animation
        spinnerDialog = new SpinnerDialog(HospitalListsActivity.this, city_names, "Select or Search City", R.style.DialogAnimations_SmileWindow, "CLOSE");// With  Animation

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                city_names_text.setText("Your Location: " + item);
                Log.d(AppUtils.TAG, " name: " + item + " id: " + city_id.get(position).toString());
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
        for (int i = 0; i < specializationListArraylist.size(); i++) {
            spec_names.add(specializationListArraylist.get(i).getSpecializationName());
            spec_id.add(specializationListArraylist.get(i).getSpecializationId());
        }

        SpinnerDialog spinnerDialog1 = new SpinnerDialog(HospitalListsActivity.this, spec_names, "Select or Search Specialty", "Close Button Text");// With No Animation
        spinnerDialog1 = new SpinnerDialog(HospitalListsActivity.this, spec_names, "Select or Search Specialty", R.style.DialogAnimations_SmileWindow, "CLOSE");// With  Animation

        spinnerDialog1.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                //  Toast.makeText(DashboardActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                // selectedItems.setText(item + " Position: " + position);
                specialty_names_text.setText("Specialty: " + item);
                Log.d(AppUtils.TAG, " name: " + item + " id: " + spec_id.get(position).toString());
                SELECTED_SPEC_ID = spec_id.get(position);

                if (city_names_text.getText().toString().equals("")) {
                    SELECTED_FILTER_TYPE = 4;
                } else {
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

                if (sharedPreferences != null) {
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

                    shareadPreferenceClass.clearChoosenLatitude();
                    shareadPreferenceClass.setChoosenLatitude(String.valueOf(CHOOSEN_LOCATION_LATITUDE));

                    shareadPreferenceClass.clearChoosenLongitude();
                    shareadPreferenceClass.setChoosenLongitude(String.valueOf(CHOOSEN_LOCATION_LONGITUDE));
                }

                dialog.dismiss();

                if (NetworkUtil.getConnectivityStatusString(HospitalListsActivity.this).equalsIgnoreCase("enabled")) {
                    hospitalAdapter.clear();
                    hospitalAdapter.notifyDataSetChanged();
                    PAGINATION_NEXT = 1;
                    collectHospitalsListsDetails();
                } else {
                    AppUtils.showCustomAlertMessage(HospitalListsActivity.this, MHConstants.INTERNET, MHConstants.INTERNET_CHECK, "OK", null, null);
                }

            }
        });

        dialog.show();
    }

    private void gpslocationCollect() {
        initGoogleAPIClient();//Init Google API Client
        checkPermissions();//Check Permission
    }

    /* Initiate Google API Client  */
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(HospitalListsActivity.this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(HospitalListsActivity.this)
                .build();
        mGoogleApiClient.connect();
    }

    /* Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(HospitalListsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else
                showSettingDialog();
        } else
            showSettingDialog();
    }

    /*  Show Popup to access User Permission  */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(HospitalListsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(HospitalListsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);

        } else {
            ActivityCompat.requestPermissions(HospitalListsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }

    /* Show Location Access Dialog */
    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        updateGPSStatus("GPS is Enabled in your device");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(HospitalListsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.e("Settings", "Result OK");
                        updateGPSStatus("GPS is Enabled in your device");
                        //startLocationUpdates();
                        break;
                    case RESULT_CANCELED:
                        Log.e("Settings", "Result Cancel");
                        updateGPSStatus("GPS is Disabled in your device");
                        break;
                }
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(AppUtils.TAG, " Hospital List resume");

        registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));//Register broadcast receiver to check the status of GPS
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister receiver on destroy
        if (gpsLocationReceiver != null)
            unregisterReceiver(gpsLocationReceiver);
    }
    //Run on UI
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {

            if(mGoogleApiClient != null){
                mGoogleApiClient.connect();
                showSettingDialog();
            }


        }
    };

    /* Broadcast receiver to check status of GPS */
    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //If Action is Location
            if (intent.getAction().matches(BROADCAST_ACTION)) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                //Check if GPS is turned ON or OFF
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.e("About GPS", "GPS is Enabled in your device");
                    updateGPSStatus("GPS is Enabled in your device");
                } else {
                    //If GPS turned OFF show Location Dialog
                    new Handler().postDelayed(sendUpdatesToUI, 10);
                    // showSettingDialog();
                    updateGPSStatus("GPS is Disabled in your device");
                    Log.e("About GPS", "GPS is Disabled in your device");
                }

            }
        }
    };
    //Method to update GPS status text
    private void updateGPSStatus(String status) {
        // gps_status.setText(status);
        if(status.equals("GPS is Enabled in your device")) {
            // Toast.makeText(BusinessMapActivity.this, status, Toast.LENGTH_SHORT).show();
            continueLocationFetch();

        }
        else if(status.equals("GPS is Disabled in your device")) {
            Toast.makeText(HospitalListsActivity.this, status + "\nTry Again", Toast.LENGTH_SHORT).show();
        }
    }

    /* On Request permission method to check the permisison is granted or not for Marshmallow+ Devices  */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_INTENT_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //If permission granted show location dialog if APIClient is not null
                    if (mGoogleApiClient == null) {
                        initGoogleAPIClient();
                        showSettingDialog();
                    } else
                        showSettingDialog();


                } else {
                    updateGPSStatus("Location Permission denied.");
                    Toast.makeText(HospitalListsActivity.this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void continueLocationFetch() {
        getLocation();
    }

    @Override
    public void onLocationChanged(Location locations) {
        this.location = locations;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public Location getLocation() {

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( HospitalListsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( HospitalListsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            Log.d(AppUtils.TAG, " GEO_LATITUDE:"+latitude+" GEO_LONGITUDE:"+longitude);

            if((latitude == 0.0) && (longitude == 0.0)) {
                Toast.makeText(HospitalListsActivity.this, "No address found by the service ", Toast.LENGTH_SHORT).show();
            }
            else {
                gpsSelectedItemLocation.setVisibility(View.VISIBLE);
                gpsSelectedItemLocation.setText("Current Latitude: " + String.format("%.2f", latitude) + "\n" + "Current Longitude: " + String.format("%.2f", longitude));

                SELECTED_FILTER_TYPE = 1;
                SELECTED_GEO_LATITUDE = latitude;
                SELECTED_GEO_LONGITUDE = longitude;

                CHOOSEN_LOCATION_LATITUDE = latitude;
                CHOOSEN_LOCATION_LONGITUDE = longitude;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }
}
