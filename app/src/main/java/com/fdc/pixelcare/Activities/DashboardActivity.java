package com.fdc.pixelcare.Activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.Activities.Appointments.AppointmentListActivity;
import com.fdc.pixelcare.Activities.Appointments.DoctorsListActivity;
import com.fdc.pixelcare.Activities.Consultations.ConsultationListActivity;
import com.fdc.pixelcare.Activities.Home.HospitalMapFragment;
import com.fdc.pixelcare.Activities.Home.SignOutFragment;
import com.fdc.pixelcare.Activities.HomeOld.HomeOldFragment;
import com.fdc.pixelcare.Activities.Opinions.OpinionListActivity;
import com.fdc.pixelcare.Activities.Others.AboutUsActivity;
import com.fdc.pixelcare.Activities.Others.BlogListActivity;
import com.fdc.pixelcare.Activities.Others.ContactUsActivity;
import com.fdc.pixelcare.Activities.Others.FamilyMemberActivity;
import com.fdc.pixelcare.Activities.Others.WebViewActivity;
import com.fdc.pixelcare.DataModel.CitiesList;
import com.fdc.pixelcare.DataModel.DrugAbuse;
import com.fdc.pixelcare.DataModel.DrugAllery;
import com.fdc.pixelcare.DataModel.FamilyHistory;
import com.fdc.pixelcare.DataModel.FamilyMember;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

/**
 * Created by SALMA on 20-12-2018.
 */
public class DashboardActivity extends AppCompatActivity  implements LocationListener, GoogleApiClient.OnConnectionFailedListener  {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    TextView header_title;
    MenuItem nav_location;

    int USER_ID,USER_LOGINTYPE;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION, FAMILY_MEMBER_LIST, CITIES_LIST;
    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    ArrayList<FamilyMember> memberArraylist;
    int LOGIN_MEMBER_ID;
    String LOGIN_MEMBER_NAME;
    List<CitiesList> citiesArraylist;

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

    CustomTextView gpsSelectedItemLocation;
    List<SpecializationList> specializationArraylist;
    LinearLayout choose_specialty_layout, choose_specialty_btn;
    CustomTextView specialty_names;

    int SELECTED_CITY_ID = 0, SELECTED_SPEC_ID = 0, SELECTED_FILTER_TYPE = 0;
    double SELECTED_GEO_LATITUDE = 0.00, SELECTED_GEO_LONGITUDE = 0.00;

    double CHOOSEN_LOCATION_LATITUDE = 0.00, CHOOSEN_LOCATION_LONGITUDE = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        memberArraylist = new ArrayList<>();
        citiesArraylist = new ArrayList<>();
        specializationArraylist = new ArrayList<>();
        LOGIN_MEMBER_ID = 0;
        LOGIN_MEMBER_NAME = "";
        CHOOSEN_LOCATION_LATITUDE = 0.00;
        CHOOSEN_LOCATION_LONGITUDE = 0.00;

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        shareadPreferenceClass = new ShareadPreferenceClass(getApplicationContext());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getApplicationContext());

        if(sharedPreferences !=null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE  = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);
            FAMILY_MEMBER_LIST = sharedPreferences.getString(MHConstants.PREF_FAMILY_MEMBERS, "");
            LOGIN_MEMBER_ID = sharedPreferences.getInt(MHConstants.PREF_LOGIN_MEMBER_ID,0);
            LOGIN_MEMBER_NAME  = sharedPreferences.getString(MHConstants.PREF_LOGIN_MEMBER_NEME,"");
            CITIES_LIST = sharedPreferences.getString(MHConstants.PREF_CITIES_LIST, "");

            getSupportActionBar().setTitle(LOGIN_MEMBER_NAME);

            Log.d(AppUtils.TAG , " *********** Dashboard **************** ");
            Log.d(AppUtils.TAG + " UserID: ", String.valueOf(USER_ID));
            Log.d(AppUtils.TAG + " name: ", USER_NAME);
            Log.d(AppUtils.TAG + " email: ", USER_EMAIL);
            Log.d(AppUtils.TAG + " MOBILE: ", USER_MOBILE);
            Log.d(AppUtils.TAG + " Location: ", USER_LOCATION);
            Log.d(AppUtils.TAG + " LOGIN_MEMBER_ID: ", String.valueOf(LOGIN_MEMBER_ID));
            Log.d(AppUtils.TAG + " LOGIN_MEMB_NAME: ", LOGIN_MEMBER_NAME);
            Log.d(AppUtils.TAG + " logintype: ", String.valueOf(USER_LOGINTYPE));
        }

        hideItem();

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View mHeaderView = navigationView.getHeaderView(0);
        TextView textView = (TextView) mHeaderView.findViewById(R.id.username);
        TextView textView_email = (TextView) mHeaderView.findViewById(R.id.email);
        ImageView user_image = (ImageView)  mHeaderView.findViewById(R.id.profile_image);
        textView.setText("Dear "+USER_NAME+",");
        textView_email.setText("Mobile: "+USER_MOBILE);
        // user_image.setImageResource(R.drawable.user_profile);

        Gson gson = new Gson();
        if(FAMILY_MEMBER_LIST != null && !FAMILY_MEMBER_LIST.isEmpty() ) {
            memberArraylist = new ArrayList<>();
            memberArraylist = gson.fromJson(FAMILY_MEMBER_LIST, new TypeToken<List<FamilyMember>>() {
            }.getType());
            Log.d(AppUtils.TAG +" memberSize: ", String.valueOf(memberArraylist.size()));

            for (int i = 0; i < memberArraylist.size(); i++) {
                //  memberNameArray.add(memberArraylist.get(i).getMemberName());
                if(USER_NAME.equalsIgnoreCase(memberArraylist.get(i).getMemberName())) {
                    String urlStr = APIClass.DRS_MEMBER_PROFILE_IMAGE+String.valueOf(memberArraylist.get(i).getMemberid())+"/"+memberArraylist.get(i).getMemberPhoto().trim();
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
                            .placeholder(R.drawable.user_profile)
                            .error(R.drawable.user_profile)
                            .fit()
                            .into(user_image, new Callback() {
                                @Override
                                public void onSuccess() {
                                }
                                @Override
                                public void onError() {
                                }
                            });

                }
                else {
                    user_image.setImageResource(R.drawable.user_profile);
                }
            }
        }

        // Uncomment below code for Map version
       /* boolean isFirstRunShow = sharedPreferences.getBoolean("FIRSTMAPLOAD", true);
        if (isFirstRunShow) {
            Log.d(AppUtils.TAG, " firstloadMap: "+ isFirstRunShow);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("FIRSTMAPLOAD", false);
            editor.commit();

            Gson gson1 = new Gson();
            if (CITIES_LIST.equals("")) {
                if (NetworkUtil.getConnectivityStatusString(DashboardActivity.this).equalsIgnoreCase("enabled")) {
                    collectCitiesDetails();
                } else {
                    AppUtils.showCustomAlertMessage(DashboardActivity.this, MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
                }

            } else {
                citiesArraylist = gson1.fromJson(CITIES_LIST, new TypeToken<List<CitiesList>>() {
                }.getType());
                if(citiesArraylist.size() > 0 ) {
                    ChooseFilterCustomDialog(citiesArraylist);
                }
            }
        }
        else {
            Log.d(AppUtils.TAG, " firstloadMap Next: "+ isFirstRunShow);

            HospitalMapFragment fragment_hospitals = new HospitalMapFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,fragment_hospitals);
            fragmentTransaction.commit();
        } */

        // Comment Below code to remove Old Dashboard version
        HomeOldFragment fragment_old = new HomeOldFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment_old);
        fragmentTransaction.commit();


        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){

                  /*  case R.id.slide_home:
                        Toast.makeText(getApplicationContext(),"Home Selected",Toast.LENGTH_SHORT).show();
                        toolbar.setTitle("Naadle");
                        CustomerHomeFragment fragment = new CustomerHomeFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame,fragment);
                        fragmentTransaction.commit();
                        return true;*/

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.slide_location:

                      /*  Intent i1 = new Intent(DashboardActivity.this, TakeTourActivity.class);
                        i1.putExtra("title","Introduction");
                        startActivity(i1);*/
                        return true;

                    case R.id.slide_login:
                        Toast.makeText(getApplicationContext(),"Stared Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.slide_register:
                        Toast.makeText(getApplicationContext(),"Send Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.slide_profile:
                       /* List<DrugAllery> PATIENT_DRUG_ALLERTY_ARRAY = new ArrayList<>();
                        List<DrugAbuse> PATIENT_DRUG_ABUSE_ARRAY = new ArrayList<>();
                        List<FamilyHistory> PATIENT_FAMILY_HISTORY_ARRAY = new ArrayList<>();
                        Intent bundle2 = new Intent(DashboardActivity.this, MedicalHistoryActivity.class);
                        bundle2.putExtra("title","Medical History");
                        bundle2.putExtra("MEMBER_ID",LOGIN_MEMBER_ID);
                        bundle2.putExtra("PATIENT_ID",USER_ID);
                        bundle2.putExtra("PATIENT_NAME","");
                        bundle2.putExtra("PATIENT_GENDER", "");
                        bundle2.putExtra("PATIENT_WEIGHT", "");
                        bundle2.putExtra("PATIENT_HYPERTENSION", "");
                        bundle2.putExtra("PATIENT_DIABETES", "");
                        bundle2.putExtra("PATIENT_SMOKING", "");
                        bundle2.putExtra("PATIENT_ALCOHOL", "");
                        bundle2.putExtra("PATIENT_OTHER_DETAILS", "");
                        bundle2.putExtra("PATIENT_PREV_INTERVENTIONS", "");
                        bundle2.putExtra("PATIENT_NEURO_ISSUES", "");
                        bundle2.putExtra("PATIENT_KIDNEY_ISSUES", "");
                        bundle2.putExtra("PATIENT_HEIGHT", "");
                        bundle2.putExtra("PATIENT_DRUG_ALLERTY_ARRAY", (Serializable) PATIENT_DRUG_ALLERTY_ARRAY);
                        bundle2.putExtra("PATIENT_DRUG_ABUSE_ARRAY", (Serializable) PATIENT_DRUG_ABUSE_ARRAY);
                        bundle2.putExtra("PATIENT_FAMILY_HISTORY_ARRAY", (Serializable) PATIENT_FAMILY_HISTORY_ARRAY);
                        startActivity(bundle2);*/
                        return true;
                    case R.id.slide_consultations:
                        Intent intent9 = new Intent(DashboardActivity.this, ConsultationListActivity.class);
                        intent9.putExtra("title","My Consultations");
                        startActivity(intent9);
                        return true;
                    case R.id.slide_enquiries:
                        Intent intent = new Intent(DashboardActivity.this, AppointmentListActivity.class);
                        intent.putExtra("title","My Appointments");
                        startActivity(intent);
                        return true;
                    case R.id.slide_reviews:
                        Intent intent1 = new Intent(DashboardActivity.this, OpinionListActivity.class);
                        intent1.putExtra("title","My Opinions");
                        startActivity(intent1);
                        return true;
                    case R.id.slide_family_member:
                        Intent intent2 = new Intent(DashboardActivity.this, FamilyMemberActivity.class);
                        intent2.putExtra("title","Family Members");
                        startActivity(intent2);
                        return true;
                    case R.id.slide_contactus:
                        Intent i5 = new Intent(DashboardActivity.this, ContactUsActivity.class);
                        i5.putExtra("title","Contact Us");
                        startActivity(i5);
                        return true;
                    case R.id.slide_about:
                        Intent i6 = new Intent(DashboardActivity.this, AboutUsActivity.class);
                        i6.putExtra("title","About Us");
                        startActivity(i6);
                        return true;
                    case R.id.slide_privacy_policy:
                       // Toast.makeText(getApplicationContext(),"Coming Soon !!!",Toast.LENGTH_SHORT).show();
                        Intent i7 = new Intent(DashboardActivity.this, WebViewActivity.class);
                        i7.putExtra("title","Privacy Policy");
                        i7.putExtra("webId","1");
                        startActivity(i7);
                        return true;
                    case R.id.slide_terms:
                       // Toast.makeText(getApplicationContext(),"Coming Soon !!!",Toast.LENGTH_SHORT).show();
                        Intent i8 = new Intent(DashboardActivity.this, WebViewActivity.class);
                        i8.putExtra("title","Terms and Conditions");
                        i8.putExtra("webId","2");
                        startActivity(i8);
                        return true;
                    case R.id.slide_signout:
                        //   Toast.makeText(getApplicationContext(),"Spam Selected",Toast.LENGTH_SHORT).show();

                        alertSignOutMessage();

                        return true;
                    case R.id.slide_blogs:
                        Intent i9= new Intent(DashboardActivity.this, BlogListActivity.class);
                        i9.putExtra("title","Blogs");
                        startActivity(i9);
                        return true;
                   /* case R.id.slide_international:
                        Intent i10 = new Intent(DashboardActivity.this, InternationalPatientsActivity.class);
                        i10.putExtra("title","International Patients");
                        startActivity(i10);
                        return true;*/
                    case R.id.slide_doctors:
                        Intent i11 = new Intent(DashboardActivity.this, DoctorsListActivity.class);
                        i11.putExtra("title","View Doctors");
                        startActivity(i11);
                        return true;
                    case R.id.slide_shareapp:

                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Pixel Care");
                        //  sharingIntent.putExtra(Intent.EXTRA_TEXT, "I would like you to try Medisense, all-in-one app that allows you to take charge of your health care \n\nThis app is accessible through iOS and Android devices, as well as online. \n\nDownload the app using - \nAndroid: https://play.google.com/store/apps/details?id=com.medisense.healthcare&hl=en \n\nWeb: https://medisensehealth.com/  \n\n");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "I would like you to try Pixel Care, all-in-one app that allows you to book appointments and to take second opinion from doctors. \n\nDownload the app using -  \n http://139.59.99.109/FDCPixel/fdc_pixelcare_apk/app-release.apk \n");
                        startActivity(Intent.createChooser(sharingIntent, "Share App via"));

                        return true;
                    default:
                        HospitalMapFragment fragment_hospitals = new HospitalMapFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame,fragment_hospitals);
                        fragmentTransaction.commit();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void alertSignOutMessage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        builder.setMessage("Do you want to close this application?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SignOutFragment fragment_signout = new SignOutFragment();
                        FragmentTransaction fragmentTransaction16 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction16.replace(R.id.frame, fragment_signout);
                        fragmentTransaction16.commit();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        alert.setTitle("Exit the app");
        alert.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        Menu nav_Menu = navigationView.getMenu();

        nav_location = nav_Menu.findItem(R.id.slide_location);
        nav_location.setTitle("Take Tour");
        nav_location.setVisible(false);
    }

    private void hideItem()
    {
        Log.d(AppUtils.TAG, "hideItem: " + "Udupi");
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        Menu nav_Menu = navigationView.getMenu();

        nav_location = nav_Menu.findItem(R.id.slide_location);
        nav_location.setTitle("Take Tour");
        nav_location.setVisible(false);

        nav_Menu.findItem(R.id.slide_profile).setVisible(false);
        nav_Menu.findItem(R.id.slide_enquiries).setVisible(true);
        nav_Menu.findItem(R.id.slide_reviews).setVisible(true);
        nav_Menu.findItem(R.id.slide_signout).setVisible(true);
        nav_Menu.findItem(R.id.slide_consultations).setVisible(true);
        nav_Menu.findItem(R.id.slide_login).setVisible(false);
        nav_Menu.findItem(R.id.slide_register).setVisible(false);
        nav_Menu.findItem(R.id.slide_businesslist).setVisible(false);
        nav_Menu.findItem(R.id.slide_leads).setVisible(false);
        nav_Menu.findItem(R.id.slide_profile_vendor).setVisible(false);
       // nav_Menu.findItem(R.id.slide_international).setVisible(true);
        nav_Menu.findItem(R.id.slide_shareapp).setVisible(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item_swich_user:
                //  Toast.makeText(getApplicationContext(),"Switch Hospital",Toast.LENGTH_LONG).show();
                if(sharedPreferences != null) {
                    LOGIN_MEMBER_ID = sharedPreferences.getInt(MHConstants.PREF_LOGIN_MEMBER_ID,0);
                    LOGIN_MEMBER_NAME = sharedPreferences.getString(MHConstants.PREF_LOGIN_MEMBER_NEME,"");
                }
                showSwitchMemberDialog();
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
        final Dialog dialog = new Dialog(DashboardActivity.this, R.style.CustomDialog);
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
            RadioButton rb = new RadioButton(DashboardActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
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

                    getSupportActionBar().setTitle(LOGIN_MEMBER_NAME);
                }

            }
        });

        dialog.show();
    }

    private void ChooseFilterCustomDialog(List<CitiesList> citiesArraylist) {

        final Dialog dialog = new Dialog(DashboardActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_choose_mylocation);
        dialog.setCancelable(false);

        LinearLayout detect_gps_location = (LinearLayout)dialog.findViewById(R.id.custom_mylocation_gps);
        gpsSelectedItemLocation = (CustomTextView) dialog.findViewById(R.id.custom_mylocation_gps_text);

        final CustomTextView selectedItems = (CustomTextView) dialog.findViewById(R.id.custom_mylocation_dropdown);
        final LinearLayout btn = (LinearLayout) dialog.findViewById(R.id.custom_mylocation_city);

        choose_specialty_layout = (LinearLayout) dialog.findViewById(R.id.custom_specilty_layout);
        choose_specialty_layout.setVisibility(View.GONE);
        choose_specialty_btn = (LinearLayout) dialog.findViewById(R.id.custom_specialty_btn);
        specialty_names = (CustomTextView) dialog.findViewById(R.id.custom_specialty_dropdown);

        ArrayList<String> city_names = new ArrayList<String>();
        final ArrayList<Integer> city_id = new ArrayList<Integer>();
        final ArrayList<String> city_latitude = new ArrayList<String>();
        final ArrayList<String> city_longitude = new ArrayList<String>();
        for(int i=0; i<citiesArraylist.size(); i++) {
            city_names.add(citiesArraylist.get(i).getCityName());
            city_id.add(citiesArraylist.get(i).getCityId());

            // Log.d(AppUtils.TAG, " cityLat: " + citiesArraylist.get(i).getCityLatitude().substring(0,5));
           //  Log.d(AppUtils.TAG, " cityLong: " + citiesArraylist.get(i).getCityLongitude().substring(0,5));

            city_latitude.add(citiesArraylist.get(i).getCityLatitude().substring(0,5));
            city_longitude.add(citiesArraylist.get(i).getCityLongitude().substring(0,5));
        }

        SpinnerDialog spinnerDialog = new SpinnerDialog(DashboardActivity.this,city_names,"Select or Search City","Close Button Text");// With No Animation
        spinnerDialog = new SpinnerDialog(DashboardActivity.this,city_names,"Select or Search City",R.style.DialogAnimations_SmileWindow,"CLOSE");// With  Animation

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                //  Toast.makeText(DashboardActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                // selectedItems.setText(item + " Position: " + position);
                selectedItems.setVisibility(View.VISIBLE);
                selectedItems.setText("Your Location: "+item);
                Log.d(AppUtils.TAG, " name: "+ item+" id: "+ city_id.get(position).toString());
                specialty_names.setText("");
                SELECTED_SPEC_ID = 0;
                SELECTED_CITY_ID = city_id.get(position);

                CHOOSEN_LOCATION_LATITUDE = Double.parseDouble(city_latitude.get(position));
                CHOOSEN_LOCATION_LONGITUDE = Double.parseDouble(city_longitude.get(position));

                new getJsonSpecialty().execute(city_id.get(position).toString(), "2", "", "");
            }
        });

        final SpinnerDialog finalSpinnerDialog = spinnerDialog;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalSpinnerDialog.showSpinerDialog();
            }
        });

        detect_gps_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpslocationCollect();
            }
        });

        Button submit_btn = (Button) dialog.findViewById(R.id.custom_submit);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(AppUtils.TAG , "******************** SUBMIT FILTER ********************* ");
                Log.d(AppUtils.TAG , "SELECTED_CITY_ID: "+ SELECTED_CITY_ID);
                Log.d(AppUtils.TAG , "SELECTED_SPEC_ID: "+ SELECTED_SPEC_ID);
                Log.d(AppUtils.TAG , "SELECTED_GEO_LATITUDE: "+ SELECTED_GEO_LATITUDE);
                Log.d(AppUtils.TAG , "SELECTED_GEO_LONGITUDE: "+ SELECTED_GEO_LONGITUDE);
                Log.d(AppUtils.TAG , "SELECTED_FILTER_TYPE: "+ SELECTED_FILTER_TYPE);

                Log.d(AppUtils.TAG , "CHOOSEN_LOCATION_LATITUDE: "+ CHOOSEN_LOCATION_LATITUDE);
                Log.d(AppUtils.TAG , "CHOOSEN_LOCATION_LONGITUDE: "+ CHOOSEN_LOCATION_LONGITUDE);

                if((SELECTED_CITY_ID == 0) && (SELECTED_GEO_LATITUDE ==0.0 && SELECTED_GEO_LONGITUDE == 0.0)) {
                    Toast.makeText(DashboardActivity.this, "Choose City/ Current Location !!!", Toast.LENGTH_SHORT).show();
                }
              /*  else if(SELECTED_SPEC_ID == 0) {
                    Toast.makeText(DashboardActivity.this, "Choose Specialty !!!", Toast.LENGTH_SHORT).show();
                }*/
                else {
                    dialog.dismiss();

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

                    HospitalMapFragment fragment_hospitals = new HospitalMapFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame,fragment_hospitals);
                    fragmentTransaction.commit();
                }
            }
        });

        dialog.show();
    }

    private void collectCitiesDetails() {
        citiesArraylist = new ArrayList<>();

        final ProgressDialog progressDialog1 = new ProgressDialog(DashboardActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
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

                                        ChooseFilterCustomDialog(citiesArraylist);
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
        RequestQueue requestQueue = AppController.getInstance(DashboardActivity.this).getRequestQueue();
        AppController.getInstance(DashboardActivity.this).addToRequestQueue(stringRequest);
    }

    private void gpslocationCollect() {
        initGoogleAPIClient();//Init Google API Client
        checkPermissions();//Check Permission
    }
    /* Initiate Google API Client  */
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(DashboardActivity.this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(DashboardActivity.this)
                .build();
        mGoogleApiClient.connect();
    }

    /* Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(DashboardActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else
                showSettingDialog();
        } else
            showSettingDialog();
    }

    /*  Show Popup to access User Permission  */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(DashboardActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);

        } else {
            ActivityCompat.requestPermissions(DashboardActivity.this,
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
                            status.startResolutionForResult(DashboardActivity.this, REQUEST_CHECK_SETTINGS);
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
            Toast.makeText(DashboardActivity.this, status + "\nTry Again", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DashboardActivity.this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
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
    public void onLocationChanged(Location location) {

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
                ContextCompat.checkSelfPermission( DashboardActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( DashboardActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            GEO_LATITUDE = location.getLatitude();
            GEO_LONGITUDE = location.getLongitude();
            Log.d(AppUtils.TAG, " GEO_LATITUDE:"+GEO_LATITUDE+" GEO_LONGITUDE:"+GEO_LONGITUDE);
            if((latitude == 0.0) && (longitude == 0.0)) {
                Toast.makeText(DashboardActivity.this, "No address found by the service ", Toast.LENGTH_SHORT).show();
            }
            else {
                gpsSelectedItemLocation.setVisibility(View.VISIBLE);
                gpsSelectedItemLocation.setText("Latitude: "+String.format("%.2f", GEO_LATITUDE) + "\nLongitude: "+String.format("%.2f", GEO_LONGITUDE));

                choose_specialty_layout.setVisibility(View.GONE);
                specialty_names.setText("");
                SELECTED_SPEC_ID = 0;
                SELECTED_GEO_LATITUDE = Double.parseDouble(String.format("%.2f", GEO_LATITUDE));
                SELECTED_GEO_LONGITUDE = Double.parseDouble(String.format("%.2f", GEO_LONGITUDE));

                CHOOSEN_LOCATION_LATITUDE = GEO_LATITUDE;
                CHOOSEN_LOCATION_LONGITUDE = GEO_LONGITUDE;

                new getJsonSpecialty().execute("", "1", String.format("%.2f", GEO_LATITUDE), String.format("%.2f", GEO_LONGITUDE));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    class getJsonSpecialty extends AsyncTask<String,String,String> {
        String city_id, filter_type, gps_latitude, gps_longitude;
        @Override
        protected String doInBackground(String... key) {
            city_id = key[0];
            filter_type = key[1];               // 1- GPS Location, 2- Select By City
            gps_latitude = key[2];
            gps_longitude = key[3];
            SELECTED_FILTER_TYPE = Integer.parseInt(filter_type);

            Log.d(AppUtils.TAG, " city_id: " + city_id);
            Log.d(AppUtils.TAG, " filter_type: " + filter_type);
            Log.d(AppUtils.TAG, " gps_latitude: " + gps_latitude);
            Log.d(AppUtils.TAG, " gps_longitude: " + gps_longitude);


            specializationArraylist = new ArrayList<>();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_SPECIALTY_FILTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(AppUtils.TAG, " spec res: "+ response.toString());

                            if (response != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String staus_res = jsonObject.getString("status");
                                    if(staus_res.equals("true"))
                                    {
                                        specializationArraylist = new ArrayList<>();
                                        JSONArray jsonArray = jsonObject.getJSONArray("specialization_details");

                                        if(jsonArray.length()>0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                specializationArraylist.add(new SpecializationList(jsonArray.getJSONObject(i).getInt("spec_id"),jsonArray.getJSONObject(i).getString("spec_name")));
                                            }

                                            //  choose_specialty_layout.setVisibility(View.VISIBLE);
                                            choose_specialty_layout.setVisibility(View.GONE);

                                            ArrayList<String> spec_names = new ArrayList<String>();
                                            final ArrayList<Integer> spec_id = new ArrayList<Integer>();
                                            for(int i=0; i<specializationArraylist.size(); i++) {
                                                spec_names.add(specializationArraylist.get(i).getSpecializationName());
                                                spec_id.add(specializationArraylist.get(i).getSpecializationId());
                                            }

                                            SpinnerDialog spinnerDialog1 = new SpinnerDialog(DashboardActivity.this,spec_names,"Select or Search Specialty","Close Button Text");// With No Animation
                                            spinnerDialog1 = new SpinnerDialog(DashboardActivity.this,spec_names,"Select or Search Specialty",R.style.DialogAnimations_SmileWindow,"CLOSE");// With  Animation

                                            spinnerDialog1.bindOnSpinerListener(new OnSpinerItemClick() {
                                                @Override
                                                public void onClick(String item, int position) {
                                                    //  Toast.makeText(DashboardActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                                    // selectedItems.setText(item + " Position: " + position);
                                                    specialty_names.setText("Specialty: "+item);
                                                    Log.d(AppUtils.TAG, " name: "+ item+" id: "+ spec_id.get(position).toString());
                                                    SELECTED_SPEC_ID = spec_id.get(position);
                                                }
                                            });

                                            final SpinnerDialog finalSpinnerDialog1 = spinnerDialog1;
                                            choose_specialty_btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    finalSpinnerDialog1.showSpinerDialog();
                                                }
                                            });
                                        }
                                        else {
                                            //    choose_specialty_layout.setVisibility(View.VISIBLE);
                                            choose_specialty_layout.setVisibility(View.GONE);
                                            specialty_names.setText("-- No specilaty available --");
                                            SELECTED_SPEC_ID = 0;
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
                            Log.d(AppUtils.TAG+"ERR",error.toString());
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIParam.KEY_API,APIClass.API_KEY);
                    map.put(APIParam.KEY_HOME_CITY_ID,city_id);
                    map.put(APIParam.KEY_HOME_FILTER_TYPE,filter_type);
                    map.put(APIParam.KEY_HOME_LATITUDE,gps_latitude);
                    map.put(APIParam.KEY_HOME_LONGITUDE,gps_longitude);
                    return map;
                }
            };
            RequestQueue requestQueue = AppController.getInstance(DashboardActivity.this).
                    getRequestQueue();
            AppController.getInstance(DashboardActivity.this).addToRequestQueue(stringRequest);
            return null;
        }
    }

    public static void changeTitleName(String memeber_name, Context mContext) {
        AppCompatActivity activity = (AppCompatActivity) mContext;
        activity.getSupportActionBar().setTitle(memeber_name);
    }

}
