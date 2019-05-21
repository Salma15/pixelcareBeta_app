package com.fdc.pixelcare.Activities.Home;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.fdc.pixelcare.Activities.DashboardActivity;
import com.fdc.pixelcare.Adapters.Home.HospitalViewAdapter;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by SALMA on 20-12-2018.
 */

public class HospitalMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener, LocationListener, OnMapReadyCallback, View.OnClickListener {

    int USER_ID,USER_LOGINTYPE;
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

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";

    LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    private GoogleMap map;
    private ImageView imgMyLocation;
    Location location; // location
    double latitude, longitude; // latitude, longitude

    List<HospitalList> hospitalListArraylist;
    List<HospitalList> hospitalConsultedListArraylist;
    List<HospitalList> hospitalVisibleListArraylist;

    private RecyclerView recyclerView;
    private HospitalViewAdapter mAdapter;
    ImageView view_more_btn;
    CustomTextViewItalicBold empty_data;
    LinearLayout list_layout;

    double CHOOSEN_LOCATION_LATITUDE = 0.00, CHOOSEN_LOCATION_LONGITUDE = 0.00;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_new, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        memberArraylist = new ArrayList<>();
        hospitalListArraylist = new ArrayList<>();
        hospitalConsultedListArraylist = new ArrayList<>();
        hospitalVisibleListArraylist = new ArrayList<>();
        citiesArraylist = new ArrayList<>();
        specializationListArraylist  = new ArrayList<>();
        CHOOSEN_LOCATION_LATITUDE = 0.00;
        CHOOSEN_LOCATION_LONGITUDE = 0.00;

        shareadPreferenceClass = new ShareadPreferenceClass(getActivity());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getActivity());

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
            SELECTED_GEO_LATITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_MY_LATITUDE, "0.00"));
            SELECTED_GEO_LONGITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_MY_LONGITUDE, "0.00"));
            CHOOSEN_LOCATION_LATITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_CHOOSEN_LATITUDE, "0.00"));
            CHOOSEN_LOCATION_LONGITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_CHOOSEN_LONGITUDE, "0.00"));

            Log.d(AppUtils.TAG, " *********** HospitalMapFragment **************** ");
            Log.d(AppUtils.TAG + " UserID: ", String.valueOf(USER_ID));
            Log.d(AppUtils.TAG + " name: ", USER_NAME);
            Log.d(AppUtils.TAG + " email: ", USER_EMAIL);
            Log.d(AppUtils.TAG + " MOBILE: ", USER_MOBILE);
            Log.d(AppUtils.TAG + " Location: ", USER_LOCATION);
            Log.d(AppUtils.TAG + " LOGIN_MEMBER_ID: ", String.valueOf(LOGIN_MEMBER_ID));
            Log.d(AppUtils.TAG + " LOGIN_MEMB_NAME: ", LOGIN_MEMBER_NAME);
            Log.d(AppUtils.TAG + " logintype: ", String.valueOf(USER_LOGINTYPE));

            Log.d(AppUtils.TAG , "SELECTED_CITY_ID: "+ SELECTED_CITY_ID);
            Log.d(AppUtils.TAG , "SELECTED_SPEC_ID: "+ SELECTED_SPEC_ID);
            Log.d(AppUtils.TAG , "SELECTED_GEO_LATITUDE: "+ SELECTED_GEO_LATITUDE);
            Log.d(AppUtils.TAG , "SELECTED_GEO_LONGITUDE: "+ SELECTED_GEO_LONGITUDE);
            Log.d(AppUtils.TAG , "SELECTED_FILTER_TYPE: "+ SELECTED_FILTER_TYPE);
            Log.d(AppUtils.TAG , "CHOOSEN_LOCATION_LATITUDE: "+ CHOOSEN_LOCATION_LATITUDE);
            Log.d(AppUtils.TAG , "CHOOSEN_LOCATION_LONGITUDE: "+ CHOOSEN_LOCATION_LONGITUDE);
        }
        initializeViews();
    }

    private void initializeViews() {

        hospitalListArraylist = new ArrayList<>();
        hospitalConsultedListArraylist = new ArrayList<>();
        hospitalVisibleListArraylist = new ArrayList<>();

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.home_new_recyclerview);
        view_more_btn = (ImageView) getActivity().findViewById(R.id.home_new_more);
        view_more_btn.setOnClickListener(this);
        empty_data = (CustomTextViewItalicBold)getActivity().findViewById(R.id.home_new_list_empty);
        empty_data.setVisibility(View.GONE);
        list_layout = (LinearLayout) getActivity().findViewById(R.id.home_new_list_layout);
        list_layout.setVisibility(View.VISIBLE);

        mAdapter = new HospitalViewAdapter(getActivity(), hospitalConsultedListArraylist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        Gson gson1 = new Gson();
        if (CITIES_LIST.equals("")) {
            if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                collectCitiesDetails();
            } else {
                AppUtils.showCustomAlertMessage(getActivity(), MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            citiesArraylist = gson1.fromJson(CITIES_LIST, new TypeToken<List<CitiesList>>() {
            }.getType());
           /* if(citiesArraylist.size() > 0 ) {
                ChooseFilterCustomDialog(citiesArraylist);
            }*/
        }

        Gson gson = new Gson();
        if (SPECIALIZATION_LIST.equals("")) {
            if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                collectSpecializationDetails();
            } else {
                AppUtils.showCustomAlertMessage(getActivity(), MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            specializationListArraylist = gson.fromJson(SPECIALIZATION_LIST, new TypeToken<List<SpecializationList>>() {
            }.getType());
           /* if(specializationListArraylist.size() > 0 ) {
                showFilterCustomDialog(specializationListArraylist);
            }*/
        }

        initGoogleAPIClient();//Init Google API Client
        checkPermissions();//Check Permission
    }

    /* Initiate Google API Client  */
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /* Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(),
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);

        } else {
            ActivityCompat.requestPermissions(getActivity(),
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
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));//Register broadcast receiver to check the status of GPS
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Unregister receiver on destroy
        if (gpsLocationReceiver != null) {
            getActivity().unregisterReceiver(gpsLocationReceiver);
        }
    }

    //Run on UI
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            // showSettingDialog();
            if (mGoogleApiClient != null) {
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
                LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
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

        if (status.equals("GPS is Enabled in your device")) {
            // Toast.makeText(BusinessMapActivity.this, status, Toast.LENGTH_SHORT).show();
            continueWithMap();

        } else if (status.equals("GPS is Disabled in your device")) {
            Toast.makeText(getActivity(), status + "\nEnable GPS to continue", Toast.LENGTH_SHORT).show();
        }
    }

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
                    Toast.makeText(getActivity(), "Location Permission denied.", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public void onLocationChanged(Location locations) {
        this.location = (Location) locations;
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

    private void continueWithMap() {
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String name= marker.getTitle();
        String hosp_id= marker.getSnippet();
        //  Toast.makeText(getActivity(), " Marker Click: "+name+" ID: "+doc_id, Toast.LENGTH_SHORT).show();

        if(!hosp_id.equals("") && hosp_id != null) {

            Intent i1 = new Intent(getActivity(), HospitalDoctorsActivity.class);
            i1.putExtra("title", "View Doctors");
            i1.putExtra("HOSPITAL_ID", Integer.parseInt(hosp_id));
            getActivity().startActivity(i1);
        }

        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        Log.d(AppUtils.TAG+ " Network", "onMapReady");

        loadHospitalMapViewLists();
    }

    private void loadHospitalMapViewLists() {
        map.clear();

        if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
            sendHospitalRequestToServer(SELECTED_CITY_ID, SELECTED_SPEC_ID, SELECTED_FILTER_TYPE, SELECTED_GEO_LATITUDE, SELECTED_GEO_LONGITUDE);
        } else {
            AppUtils.showCustomAlertMessage(getActivity(), MHConstants.INTERNET, MHConstants.INTERNET_CHECK, "OK", null, null);
        }

        continueLKoadingMapViews();
    }

    private void continueLKoadingMapViews() {

        imgMyLocation = (ImageView) getActivity().findViewById(R.id.map_currentlocation);
        imgMyLocation.setOnClickListener(this);

        // Get Current Location in MapView
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        try {
            locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

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

            if ((latitude == 0.0) && (longitude == 0.0)) {
                Toast.makeText(getActivity(), "No address found by the service ", Toast.LENGTH_SHORT).show();
            } else {
                double selected_latitute = latitude;
                double selected_longitude = longitude;
                LatLng latLng = new LatLng(selected_latitute, selected_longitude);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                map.setMyLocationEnabled(false);
//                map.addMarker(new MarkerOptions().position(latLng).title("Patient Name: " + USER_NAME)
//                        .snippet("")
//                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_patient_marker)));
                map.animateCamera(cameraUpdate);
                map.setOnMarkerClickListener(this);

                if (map != null && getActivity().findViewById(Integer.parseInt("1")) != null) {
                    // Get the button view
                    View locationButton = ((View) getActivity().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                    // and next place it, on bottom right (as Google Maps app)
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                            locationButton.getLayoutParams();
                    locationButton.setBackgroundResource(R.mipmap.gps_location);
                    // position on right top
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                    layoutParams.setMargins(0, 40, 30, 0);
                }

                int GEO_RADIUS = 60;            //  specified in meters, It should be zero or greater.
                int strokeColor = 0xffff0000;   //  red outline
                int shadeColor = 0x44ff0000;    //  opaque red fill
                CircleOptions circleOptions = new CircleOptions().center(latLng).radius(GEO_RADIUS).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
                Circle mCircle = map.addCircle(circleOptions);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_currentlocation:
                getMyLocation();
                break;
            case R.id.home_new_more:
                InputMethodManager imm2 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(view_more_btn.getWindowToken(), 0);

                Intent i1 = new Intent(getActivity(), HospitalListsActivity.class);
                i1.putExtra("title", "View Hospitals");
                startActivity(i1);
                break;
        }
    }

    private void getMyLocation() {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        map.addMarker(new MarkerOptions().position(latLng).title("Patient Name: " + USER_NAME)
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_patient_marker)));
        map.animateCamera(cameraUpdate);
    }


    private void sendHospitalRequestToServer(final int selected_city_id, final int selected_spec_id, final int selected_filter_type,
                                             final double selected_geo_latitude, final double selected_geo_longitude) {

        Log.d(AppUtils.TAG, "*********** sendHospitalRequestToServer ************* ");
        Log.d(AppUtils.TAG, " sel_city_id: " + selected_city_id);
        Log.d(AppUtils.TAG, " sel_spec_id: " + selected_spec_id);
        Log.d(AppUtils.TAG, " sel_filter_type: " + selected_filter_type);
        Log.d(AppUtils.TAG, " sel_geo_latitude: " + selected_geo_latitude);
        Log.d(AppUtils.TAG, " sel_geo_longitude: " + selected_geo_longitude);
        Log.d(AppUtils.TAG, " PAGINATION_NEXT: " + PAGINATION_NEXT);

        hospitalListArraylist = new ArrayList<>();
        hospitalConsultedListArraylist = new ArrayList<>();
        hospitalVisibleListArraylist = new ArrayList<>();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Hospital Lists...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_HOSPITALS_MAP_NEW_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(AppUtils.TAG, "HOSP list: " + response.toString());
                        if (response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1, jsonArray2;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("hospital_list");
                                    if (jsonArray.length() > 0) {
                                        empty_data.setVisibility(View.GONE);
                                        list_layout.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jObject = jsonArray.getJSONObject(i);

                                            hospitalListArraylist.add(new HospitalList(jObject.getInt("hosp_id"),jObject.getString("hosp_name"),
                                                    jObject.getString("hosp_addrs"),jObject.getString("hosp_city"),jObject.getString("hosp_state"),
                                                    jObject.getString("hosp_country"),jObject.getString("hosp_contact"),jObject.getString("hosp_email"),
                                                    jObject.getString("geo_latitude"),jObject.getString("geo_longitude"),jObject.getString("hosp_logo"),
                                                    jObject.getInt("hosp_consulted")));
                                        }

                                        Log.d(AppUtils.TAG, " resultsize: " + hospitalListArraylist.size());

                                        //Set the values
                                        Gson gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(hospitalListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearHospitalsMapLists();
                                            shareadPreferenceClass.setHospitalsMapList(jsonDiagnoText);
                                        }

                                        LoadingGoogleMapMarkers(hospitalListArraylist);
                                    } else {
                                        Toast.makeText(getActivity(), "Oops !! No hospitals found !!!", Toast.LENGTH_SHORT).show();
                                        LoadingGoogleMapMarkers(hospitalListArraylist);
                                        empty_data.setVisibility(View.VISIBLE);
                                        list_layout.setVisibility(View.GONE);
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(APIParam.KEY_API, APIClass.API_KEY);
                map.put(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID));
                map.put(APIParam.KEY_MEMBER_ID, String.valueOf(LOGIN_MEMBER_ID));
                map.put(APIParam.KEY_HOME_CITY_ID, String.valueOf(selected_city_id));
                map.put(APIParam.KEY_HOME_FILTER_TYPE, String.valueOf(selected_filter_type));
                map.put(APIParam.KEY_HOME_LATITUDE, String.valueOf(selected_geo_latitude));
                map.put(APIParam.KEY_HOME_LONGITUDE, String.valueOf(selected_geo_longitude));
                map.put(APIParam.KEY_HOME_SPEC_ID, String.valueOf(selected_spec_id));
                map.put(APIParam.KEY_PAGINATION, String.valueOf(PAGINATION_NEXT));
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(getActivity()).
                getRequestQueue();
        AppController.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void LoadingGoogleMapMarkers(List<HospitalList> hospitalListArraylist) {
        hospitalConsultedListArraylist = new ArrayList<>();
        hospitalVisibleListArraylist = new ArrayList<>();

        for (int i = 0; i < hospitalListArraylist.size(); i++) {
            double Latitude = Double.parseDouble(hospitalListArraylist.get(i).getHospitalGeoLatitude());
            double Longitude = Double.parseDouble(hospitalListArraylist.get(i).getHospitalGeoLongitude());
            String name = hospitalListArraylist.get(i).getHospitalName().toString();
            MarkerOptions marker = new MarkerOptions().position(new LatLng(Latitude, Longitude)).title(name).snippet(String.valueOf(hospitalListArraylist.get(i).getHospitalId())).visible(true);

          /*  marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_hospital_icon));
            map.addMarker(marker);*/

            if (hospitalListArraylist.get(i).getHospitalConsulted() == 1) {
                Log.d(AppUtils.TAG, " consulted:" + hospitalListArraylist.get(i).getHospitalConsulted());
                //  marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_user_marker));

                View markerVie = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
                CustomTextView numTxt = (CustomTextView) markerVie.findViewById(R.id.num_txt);
                numTxt.setText(hospitalListArraylist.get(i).getHospitalName());

                marker.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), markerVie)));


                hospitalVisibleListArraylist.add(new HospitalList(hospitalListArraylist.get(i).getHospitalId(), hospitalListArraylist.get(i).getHospitalName(),
                        hospitalListArraylist.get(i).getHospitalAddress(), hospitalListArraylist.get(i).getHospitalCity(), hospitalListArraylist.get(i).getHospitalState(),
                        hospitalListArraylist.get(i).getHospitalCountry(), hospitalListArraylist.get(i).getHospitalMobile(), hospitalListArraylist.get(i).getHospitalEmail(),
                        hospitalListArraylist.get(i).getHospitalGeoLatitude(), hospitalListArraylist.get(i).getHospitalGeoLongitude(), hospitalListArraylist.get(i).getHospitalPhoto(),
                        hospitalListArraylist.get(i).getHospitalConsulted()));

                hospitalConsultedListArraylist.add(new HospitalList(hospitalListArraylist.get(i).getHospitalId(), hospitalListArraylist.get(i).getHospitalName(),
                        hospitalListArraylist.get(i).getHospitalAddress(), hospitalListArraylist.get(i).getHospitalCity(), hospitalListArraylist.get(i).getHospitalState(),
                        hospitalListArraylist.get(i).getHospitalCountry(), hospitalListArraylist.get(i).getHospitalMobile(), hospitalListArraylist.get(i).getHospitalEmail(),
                        hospitalListArraylist.get(i).getHospitalGeoLatitude(), hospitalListArraylist.get(i).getHospitalGeoLongitude(), hospitalListArraylist.get(i).getHospitalPhoto(),
                        hospitalListArraylist.get(i).getHospitalConsulted()));

            }
            else if (hospitalListArraylist.get(i).getHospitalConsulted() == 0) {

                View markerView1 = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout1, null);
                CustomTextView numTxt1= (CustomTextView) markerView1.findViewById(R.id.num_txt1);
                numTxt1.setText(hospitalListArraylist.get(i).getHospitalName());

               /* if(i>=40) {
                    marker.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), markerView1)));
                }
                else {
                    marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_hospital_icon));
                }*/

                marker.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView1(getActivity(), markerView1, hospitalListArraylist.get(i).getHospitalName())));
            //    marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_hospital_icon));

            }
            else {
                marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_hospital_icon));
            }
            map.addMarker(marker);

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

        if(SELECTED_GEO_LATITUDE != 0.00 && SELECTED_GEO_LONGITUDE != 0.00 && SELECTED_FILTER_TYPE == 1) {
            if(sharedPreferences != null) {
                CHOOSEN_LOCATION_LATITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_CHOOSEN_LATITUDE, "0.00"));
                CHOOSEN_LOCATION_LONGITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_CHOOSEN_LONGITUDE, "0.00"));
            }

            Log.d(AppUtils.TAG, " lat:" + SELECTED_GEO_LATITUDE + " long:" + SELECTED_GEO_LONGITUDE);
            Log.d(AppUtils.TAG, " chooselat:" + CHOOSEN_LOCATION_LATITUDE + " chooselong:" + CHOOSEN_LOCATION_LONGITUDE);
            LatLng latLng = new LatLng(CHOOSEN_LOCATION_LATITUDE, CHOOSEN_LOCATION_LONGITUDE);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.addMarker(new MarkerOptions().position(latLng).title("Patient Name: " + USER_NAME)
                    .snippet("")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_patient_marker)));
            map.setMyLocationEnabled(false);
            map.animateCamera(cameraUpdate);

            int GEO_RADIUS = 60 *1000;             //  specified in meters, It should be zero or greater.  x1000 to convert it into kilometers
            int strokeColor = 0xffff0000;   //  red outline
            int shadeColor = 0x44ff0000;    //  opaque red fill
            CircleOptions circleOptions = new CircleOptions().center(latLng).radius(GEO_RADIUS).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
            Circle mCircle = map.addCircle(circleOptions);
        }
        else if(SELECTED_FILTER_TYPE == 2) {
            if(sharedPreferences != null) {
                CHOOSEN_LOCATION_LATITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_CHOOSEN_LATITUDE, "0.00"));
                CHOOSEN_LOCATION_LONGITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_CHOOSEN_LONGITUDE, "0.00"));
            }
            Log.d(AppUtils.TAG, " Citylat:" + SELECTED_GEO_LATITUDE + " Citylong:" + SELECTED_GEO_LONGITUDE);
            Log.d(AppUtils.TAG, " chooselat:" + CHOOSEN_LOCATION_LATITUDE + " chooselong:" + CHOOSEN_LOCATION_LONGITUDE);

            LatLng latLng = new LatLng(CHOOSEN_LOCATION_LATITUDE, CHOOSEN_LOCATION_LONGITUDE);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.addMarker(new MarkerOptions().position(latLng).title("Patient Name: " + USER_NAME)
                    .snippet("")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_patient_marker)));
            map.setMyLocationEnabled(false);
            map.animateCamera(cameraUpdate);

            int GEO_RADIUS = 60 *1000;             //  specified in meters, It should be zero or greater.  x1000 to convert it into kilometers
            int strokeColor = 0xffff0000;   //  red outline
            int shadeColor = 0x44ff0000;    //  opaque red fill
            CircleOptions circleOptions = new CircleOptions().center(latLng).radius(GEO_RADIUS).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
            Circle mCircle = map.addCircle(circleOptions);
        }
        else if ((SELECTED_FILTER_TYPE ==3 || SELECTED_FILTER_TYPE ==4 ||  SELECTED_FILTER_TYPE ==5) && (hospitalVisibleListArraylist.size() > 0)) {
           /* LatLng latLng = new LatLng(Double.parseDouble(hospitalListArraylist.get(0).getHospitalGeoLatitude()), Double.parseDouble(hospitalListArraylist.get(0).getHospitalGeoLongitude()));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(false);
            map.animateCamera(cameraUpdate);

            int GEO_RADIUS = 60 *1000;             //  specified in meters, It should be zero or greater.  x1000 to convert it into kilometers
            int strokeColor = 0xffff0000;   //  red outline
            int shadeColor = 0x44ff0000;    //  opaque red fill
            CircleOptions circleOptions = new CircleOptions().center(latLng).radius(GEO_RADIUS).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
            Circle mCircle = map.addCircle(circleOptions);*/

            if(sharedPreferences != null) {
                CHOOSEN_LOCATION_LATITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_CHOOSEN_LATITUDE, "0.00"));
                CHOOSEN_LOCATION_LONGITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_CHOOSEN_LONGITUDE, "0.00"));
            }

            LatLng latLng = new LatLng(CHOOSEN_LOCATION_LATITUDE, CHOOSEN_LOCATION_LONGITUDE);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.addMarker(new MarkerOptions().position(latLng).title("Patient Name: " + USER_NAME)
                    .snippet("")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_patient_marker)));
            map.setMyLocationEnabled(false);
            map.animateCamera(cameraUpdate);

            int GEO_RADIUS = 60 *1000;             //  specified in meters, It should be zero or greater.  x1000 to convert it into kilometers
            int strokeColor = 0xffff0000;   //  red outline
            int shadeColor = 0x44ff0000;    //  opaque red fill
            CircleOptions circleOptions = new CircleOptions().center(latLng).radius(GEO_RADIUS).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
            Circle mCircle = map.addCircle(circleOptions);

        }
        refreshRecyclerViews();
    }

    private void refreshRecyclerViews() {

      /*  if(hospitalConsultedListArraylist.size()>0) {
            mAdapter = new HospitalViewAdapter(getActivity(), hospitalConsultedListArraylist);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        else {
            mAdapter = new HospitalViewAdapter(getActivity(), hospitalVisibleListArraylist);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }*/

        mAdapter = new HospitalViewAdapter(getActivity(), hospitalVisibleListArraylist);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view)  {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    public static Bitmap createDrawableFromView1(Context context, View view, String hosp_name)  {
       /* DisplayMetrics displayMetrics = new DisplayMetrics();
        ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);*/

        IconGenerator icg = new IconGenerator(context);
      //  icg.setBackground(context.getResources().getDrawable(R.mipmap.map_marker_hospital_icon));
        ImageView mImageView = new ImageView(context);
        icg.setContentView(view);
       // mImageView.setImageResource(R.mipmap.map_marker_hospital_icon);
        icg.setBackground(new ColorDrawable(Color.TRANSPARENT));
        Bitmap bm = icg.makeIcon(String.format("%.10s", hosp_name));

        return bm;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_map_new, menu);

        MenuItem item = menu.findItem(R.id.item_map_doctors);
        item.setVisible(false);
        MenuItem item1 = menu.findItem(R.id.item_map_hospital);
        item1.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item_map_swich_user:
                //  Toast.makeText(getActivity(),"Switch Hospital",Toast.LENGTH_LONG).show();
                if(sharedPreferences != null) {
                    LOGIN_MEMBER_ID = sharedPreferences.getInt(MHConstants.PREF_LOGIN_MEMBER_ID,0);
                    LOGIN_MEMBER_NAME = sharedPreferences.getString(MHConstants.PREF_LOGIN_MEMBER_NEME,"");
                }
                showSwitchMemberDialog();
                return true;
            case R.id.item_map_doctors:
                return true;
            case R.id.item_map_hospital:
                return true;
            case R.id.item_map_filter:
               /* Gson gson1 = new Gson();
                if (CITIES_LIST.equals("")) {
                    if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                        collectCitiesDetails();
                    } else {
                        AppUtils.showCustomAlertMessage(getActivity(), MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
                    }

                } else {
                    citiesArraylist = gson1.fromJson(CITIES_LIST, new TypeToken<List<CitiesList>>() {
                    }.getType());
                    if(citiesArraylist.size() > 0 ) {
                        ChooseFilterCustomDialog(citiesArraylist, specializationListArraylist);
                    }
                }*/
                ChooseFilterCustomDialog(citiesArraylist, specializationListArraylist);
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
        final Dialog dialog = new Dialog(getActivity(), R.style.CustomDialog);
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
            RadioButton rb = new RadioButton(getActivity()); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
            rb.setPadding(10, 40, 10, 40);
            rb.setTag(strinIDList.get(i));
            rb.setTextSize(18);
            rg.addView(rb);

            if(LOGIN_MEMBER_ID == strinIDList.get(i)) {
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
                }

                DashboardActivity.changeTitleName(LOGIN_MEMBER_NAME, getActivity());

               dialog.dismiss();

            }
        });

        dialog.show();
    }

    private void collectCitiesDetails() {
        citiesArraylist = new ArrayList<>();

        final ProgressDialog progressDialog1 = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
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
        RequestQueue requestQueue = AppController.getInstance(getActivity()).getRequestQueue();
        AppController.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void ChooseFilterCustomDialog(List<CitiesList> citiesArraylist, List<SpecializationList> specializationListArraylist) {

        final Dialog dialog = new Dialog(getActivity(), R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_map_filter_new);

        final LinearLayout gps_btn = (LinearLayout) dialog.findViewById(R.id.hosp_map_filter_gps);
        final CustomTextView gps_location_text = (CustomTextView) dialog.findViewById(R.id.hosp_map_filter_gps_loc);
        gps_location_text.setText("");
        gps_location_text.setVisibility(View.GONE);

        if(SELECTED_GEO_LATITUDE != 0.0 && SELECTED_GEO_LONGITUDE !=0.0 && (SELECTED_FILTER_TYPE ==1  || SELECTED_CITY_ID == 0) ) {
            gps_location_text.setVisibility(View.VISIBLE);
            gps_location_text.setText("Current Latitude: " + String.format("%.2f", SELECTED_GEO_LATITUDE) + "\n" + "Current Longitude: " + String.format("%.2f", SELECTED_GEO_LONGITUDE));
        }

        gps_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(AppUtils.TAG, " current Lat: "+ latitude);
                Log.d(AppUtils.TAG, " current Long: "+ longitude);
                gps_location_text.setVisibility(View.VISIBLE);
                gps_location_text.setText("Current Latitude: "+ String.format("%.2f", latitude) + "\n"+"Current Longitude: "+ String.format("%.2f", longitude) );
                SELECTED_SPEC_ID =0;
                SELECTED_FILTER_TYPE = 1;
                SELECTED_GEO_LATITUDE = latitude;
                SELECTED_GEO_LONGITUDE = longitude;

                CHOOSEN_LOCATION_LATITUDE = latitude;
                CHOOSEN_LOCATION_LONGITUDE = longitude;
            }
        });

        final LinearLayout city_btn = (LinearLayout) dialog.findViewById(R.id.hosp_map_filter_city);
        final CustomTextView city_names_text = (CustomTextView) dialog.findViewById(R.id.hosp_map_filter_names);

        if(CHOOSEN_LOCATION_LATITUDE != 0.0 && CHOOSEN_LOCATION_LONGITUDE !=0.0  && SELECTED_CITY_ID != 0 && SELECTED_FILTER_TYPE != 1 ) {
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
        final ArrayList<String> city_latitude = new ArrayList<String>();
        final ArrayList<String> city_longitude = new ArrayList<String>();
        for(int i=0; i<citiesArraylist.size(); i++) {
            city_names.add(citiesArraylist.get(i).getCityName());
            city_id.add(citiesArraylist.get(i).getCityId());

            city_latitude.add(citiesArraylist.get(i).getCityLatitude().substring(0,5));
            city_longitude.add(citiesArraylist.get(i).getCityLongitude().substring(0,5));
        }

        SpinnerDialog spinnerDialog = new SpinnerDialog(getActivity(),city_names,"Select or Search City","Close Button Text");// With No Animation
        spinnerDialog = new SpinnerDialog(getActivity(),city_names,"Select or Search City",R.style.DialogAnimations_SmileWindow,"CLOSE");// With  Animation

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                city_names_text.setText("Your Location: "+item);
                Log.d(AppUtils.TAG, " name: "+ item+" id: "+ city_id.get(position).toString());
                specialty_names_text.setText("");
                SELECTED_SPEC_ID = 0;
                SELECTED_CITY_ID = city_id.get(position);

                CHOOSEN_LOCATION_LATITUDE = Double.parseDouble(city_latitude.get(position));
                CHOOSEN_LOCATION_LONGITUDE = Double.parseDouble(city_longitude.get(position));

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

        SpinnerDialog spinnerDialog1 = new SpinnerDialog(getActivity(),spec_names,"Select or Search Specialty","Close Button Text");// With No Animation
        spinnerDialog1 = new SpinnerDialog(getActivity(),spec_names,"Select or Search Specialty",R.style.DialogAnimations_SmileWindow,"CLOSE");// With  Animation

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

                if(sharedPreferences != null) {
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

                loadHospitalMapViewLists();

            }
        });

        dialog.show();
    }

    private void collectSpecializationDetails() {
        final ProgressDialog progressDialog1 = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
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

        RequestQueue requestQueue = AppController.getInstance(getActivity()).
                getRequestQueue();
        AppController.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
