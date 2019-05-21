package com.fdc.pixelcare.Activities.Appointments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.Adapters.Appointments.CustomCityFilterAdapter;
import com.fdc.pixelcare.Adapters.Appointments.CustomSpecFilterAdapter;
import com.fdc.pixelcare.Adapters.Appointments.DoctorsAdapter;
import com.fdc.pixelcare.DataModel.DoctorList;
import com.fdc.pixelcare.DataModel.HospitalList;
import com.fdc.pixelcare.DataModel.SpecializationList;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.Network.CustomVolleyRequestQueue;
import com.fdc.pixelcare.Network.NetworkUtil;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
import com.fdc.pixelcare.Views.CustomTextViewSemiBold;
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
 * Created by SALMA on 22/12/18.
 */

public class DoctorsListActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener, Response.ErrorListener  {
    public static final String REQUEST_TAG = "DoctorListActivity";
    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int USER_ID, USER_LOGINTYPE;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION, SPECIALIZATION_LIST, DOCTORS_LIST;
    LinearLayout filter_specialization, filter_city;
    String DOCTOR_FILTER_TYPE = "1",DOCTOR_CITY = "";
    private RequestQueue mQueue;
    int DOCTOR_SPEC_ID = 0;

    private int pageCount = 0;
    private DoctorsAdapter adapter;
    private ListView listView;
    private ProgressDialog dialog;
    private ArrayList<DoctorList> countries;

    private String url_page1 = "";
    private static String url_page2 = "";

    List<SpecializationList> specializationListArraylist;
    SpecializationList specialization;
    ProgressDialog progressDialog1;
    List<DoctorList> doctorsListArraylist;
    DoctorList doctors;
    int PAGINATION_NEXT = 1;
    RelativeLayout progress_layout;
    ArrayList<String> citiesList;

    LinearLayout direct_appointment;

    ArrayList<SpecializationList> specilizationDocArraylist;
    ArrayList<HospitalList> hospitalDocArraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctots_list);
        specializationListArraylist = new ArrayList<>();
        citiesList = new ArrayList<>();
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
        }

        shareadPreferenceClass = new ShareadPreferenceClass(DoctorsListActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(DoctorsListActivity.this);

        if(sharedPreferences !=null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE  = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);
            SPECIALIZATION_LIST = sharedPreferences.getString(MHConstants.PREF_SPECIALIZATION_LIST, "");
            DOCTORS_LIST = sharedPreferences.getString(MHConstants.PREF_DOCTORS_LIST, "");

            Log.d(AppUtils.TAG , " *********** Doctors List **************** ");
            Log.d(AppUtils.TAG + " UserID: ", String.valueOf(USER_ID));
            Log.d(AppUtils.TAG + " name: ", USER_NAME);
            Log.d(AppUtils.TAG + " email: ", USER_EMAIL);
            Log.d(AppUtils.TAG + " MOBILE: ", USER_MOBILE);
            Log.d(AppUtils.TAG + " Location: ", USER_LOCATION);
            Log.d(AppUtils.TAG + " logintype: ", String.valueOf(USER_LOGINTYPE));
        }

        initializationView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_search:
               // Toast.makeText(DoctorsListActivity.this,"Search doctors", Toast.LENGTH_SHORT).show();

                Intent i2 = new Intent(DoctorsListActivity.this, DoctorSearchActivity.class);
                i2.putExtra("title","Doctor Search");
                startActivity(i2);
                break;
        }
        return true;
    }


    private void initializationView() {
        doctorsListArraylist = new ArrayList<>();
        filter_specialization = (LinearLayout) findViewById(R.id.doclist_spec_filter);
        filter_city = (LinearLayout) findViewById(R.id.doclist_city_filter);
        filter_specialization.setOnClickListener(this);
        filter_city.setOnClickListener(this);

        progress_layout = (RelativeLayout)  findViewById(R.id.doclist_progressbar);

        direct_appointment = (LinearLayout)  findViewById(R.id.doclist_direct_appointment);
        direct_appointment.setOnClickListener(this);
        direct_appointment.setVisibility(View.GONE);

        url_page1 = APIClass.DRS_HOST_DOCTOR_LIST;
        url_page2 = url_page1;
//        listView = (ListView) findViewById(R.id.doclist_listview);
//        setListViewAdapter();
//        listView.setOnScrollListener(onScrollListener());

        listView = (ListView) findViewById(R.id.doclist_listview);
        setListViewAdapter();
       // getDataFromUrl(url_page1);        // REMOVE For dynamically adding
        listView.setOnScrollListener(onScrollListener());

        Gson gson = new Gson();
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
        }

    }

    private void collectDoctorsListsDetails() {
        // url_page1 = APIClass.DRS_HOST_DOCTOR_LIST+"?"+APIParam.KEY_API+"="+APIClass.API_KEY+"&"+APIParam.KEY_DOC_FILTER_TYPE+"="+DOCTOR_FILTER_TYPE+"&"+APIParam.KEY_DOC_SPECID+"="+String.valueOf(DOCTOR_SPEC_ID)+"&"+APIParam.KEY_DOC_HOSPID+"="+String.valueOf(DOCTOR_SPEC_HOSPID);


        getDataFromUrl(url_page1);
        Log.d(AppUtils.TAG +" URL: ", url_page1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.doclist_spec_filter:

                Gson gson = new Gson();
                if (SPECIALIZATION_LIST.equals("")) {
                    if (NetworkUtil.getConnectivityStatusString(DoctorsListActivity.this).equalsIgnoreCase("enabled")) {
                        collectSpecializationDetails();
                    } else {
                        AppUtils.showCustomAlertMessage(DoctorsListActivity.this, MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
                    }

                } else {
                    specializationListArraylist = gson.fromJson(SPECIALIZATION_LIST, new TypeToken<List<SpecializationList>>() {
                    }.getType());
                    if(specializationListArraylist.size() > 0 ) {
                       prepareSpecializationLists(specializationListArraylist);
                    }
                }

                break;
            case R.id.doclist_city_filter:
                citiesList = new ArrayList<>();
                prepareCityLists(citiesList);
                break;
            case R.id.doclist_direct_appointment:
                /*Intent i1 = new Intent(DoctorsListActivity.this, NewBookAppointmentActivity.class);
                i1.putExtra("title","Book Appointment");
                startActivity(i1);*/
                break;
        }
    }

    private void setListViewAdapter() {
        countries = new ArrayList<DoctorList>();
        adapter = new DoctorsAdapter(this, R.layout.doctor_list_item, R.id.doc_list_name, countries);
        listView.setAdapter(adapter);
    }

    // calling asynctask to get json data from internet
    private void getDataFromUrl(String url) {
        progress_layout.setVisibility(View.VISIBLE);
        new LoadDoctorsFromUrlTask(this, url, DOCTOR_FILTER_TYPE, DOCTOR_SPEC_ID, DOCTOR_CITY, PAGINATION_NEXT).execute();
    }

    private AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
                int count = listView.getCount();

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (listView.getLastVisiblePosition() >= count - threshold && pageCount < countries.size()) {
                        Log.i(AppUtils.TAG, "loading more data" + countries.size());
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
        Log.d(AppUtils.TAG, " DOC RESULT:"+ result);
        specilizationDocArraylist = new ArrayList<>();
        hospitalDocArraylist = new ArrayList<>();

        progress_layout.setVisibility(View.GONE);
        pageCount++;
        try {
            JSONObject json = new JSONObject(result);
            PAGINATION_NEXT = json.getInt("pagination_val");
            Log.i(AppUtils.TAG, " pagination_val:"+  json.getInt("pagination_val"));
            JSONArray jArray = new JSONArray(json.getString("doctor_list"));
            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);
                DoctorList country = new DoctorList();
               /* country.setDocId(jObject.getInt("ref_id"));
                country.setDocName(jObject.getString("ref_name"));
                country.setDocSpecId(jObject.getInt("spec_id"));
                country.setDocSpecName(jObject.getString("spec_name"));
                country.setDocPhoto(jObject.getString("doc_photo"));
                country.setDocCity(jObject.getString("doc_city"));
                country.setDocEncryptID(jObject.getString("doc_encyid"));
                country.setDocAreaInterest(jObject.getString("doc_interest"));
                country.setDocExperience(jObject.getString("doc_exp"));
                country.setDocQualification(jObject.getString("doc_qual"));
                country.setDocHospName(jObject.getString("hosp_name"));
                country.setDocHospAddress(jObject.getString("hosp_addrs"));
                country.setDocHospCity(jObject.getString("hosp_city"));
                country.setDocHospState(jObject.getString("hosp_state"));
                country.setDocHospCountry(jObject.getString("hosp_country"));*/

                JSONArray jsonArray1 = jObject.getJSONArray("doc_specializations");
                if (jsonArray1.length() > 0) {
                    specilizationDocArraylist = new ArrayList<>();
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        specilizationDocArraylist.add(new SpecializationList(jsonArray1.getJSONObject(j).getInt("spec_id"), jsonArray1.getJSONObject(j).getString("spec_name"),
                                jsonArray1.getJSONObject(j).getInt("doc_id"), jsonArray1.getJSONObject(j).getInt("doc_type"), jsonArray1.getJSONObject(j).getInt("spec_group_id")));
                    }
                }

                JSONArray jsonArray2 = jObject.getJSONArray("doc_hospitals");
                if (jsonArray2.length() > 0) {
                    hospitalDocArraylist = new ArrayList<>();
                    for (int k = 0; k < jsonArray2.length(); k++) {
                        hospitalDocArraylist.add(new HospitalList(jsonArray2.getJSONObject(k).getInt("hosp_id"),
                                jsonArray2.getJSONObject(k).getString("hosp_name"),
                                jsonArray2.getJSONObject(k).getString("hosp_addrs"), jsonArray2.getJSONObject(k).getString("hosp_city"),
                                jsonArray2.getJSONObject(k).getString("hosp_state"), jsonArray2.getJSONObject(k).getString("hosp_country"),
                                jsonArray2.getJSONObject(k).getInt("doc_id")));
                    }

                }

                countries.add(new DoctorList(jObject.getInt("ref_id"), jObject.getString("ref_name"),
                        jObject.getString("doc_photo"), jObject.getString("doc_city"),
                        jObject.getString("doc_interest"), jObject.getString("doc_exp"),
                        jObject.getString("doc_qual"), jObject.getString("doc_encyid"), jObject.getString("geo_latitude"),
                        jObject.getString("geo_longitude"), jObject.getInt("doc_consulted"), specilizationDocArraylist, hospitalDocArraylist));


              //  countries.add(country);
            }

            adapter.notifyDataSetChanged();
            if (dialog != null) {
                dialog.dismiss();
            }

            if(PAGINATION_NEXT == 2) {
                 if((jArray != null ) && (jArray.length() > 0 )) {
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                  /*  doctors = new DoctorList(jObject.getInt("ref_id"), jObject.getString("ref_name"), jObject.getInt("spec_id"),
                            jObject.getString("spec_name"), jObject.getString("doc_photo"), jObject.getString("doc_city"),
                            jObject.getString("doc_encyid"), jObject.getString("doc_interest"), jObject.getString("doc_exp"),
                            jObject.getString("doc_qual"), jObject.getString("hosp_name"), jObject.getString("hosp_addrs"),
                            jObject.getString("hosp_city"), jObject.getString("hosp_state"), jObject.getString("hosp_country"), jObject.getString("doc_encyid"));
*/
                    doctorsListArraylist.add(new DoctorList(jObject.getInt("ref_id"), jObject.getString("ref_name"),
                            jObject.getString("doc_photo"), jObject.getString("doc_city"),
                            jObject.getString("doc_interest"), jObject.getString("doc_exp"),
                            jObject.getString("doc_qual"), jObject.getString("doc_encyid"), jObject.getString("geo_latitude"),
                            jObject.getString("geo_longitude"), jObject.getInt("doc_consulted"), specilizationDocArraylist, hospitalDocArraylist));

                //    doctorsListArraylist.add(doctors);

                }

                if (doctorsListArraylist.size() > 0) {
                    Gson gson = new Gson();
                    String jsonText = gson.toJson(doctorsListArraylist);
                    if (sharedPreferences != null) {
                        shareadPreferenceClass.clearDoctorsLists();
                        shareadPreferenceClass.setDoctorsList(jsonText);
                    }
                }
            }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void prepareDoctorsLists(List<DoctorList> doctorsListArraylist) {
        countries = new ArrayList<DoctorList>();
        for (int i = 0; i < doctorsListArraylist.size(); i++) {

            DoctorList country = new DoctorList();
          /*  country.setDocId(doctorsListArraylist.get(i).getDocId());
            country.setDocName(doctorsListArraylist.get(i).getDocName());
            country.setDocSpecId(doctorsListArraylist.get(i).getDocSpecId());
            country.setDocSpecName(doctorsListArraylist.get(i).getDocSpecName());
            country.setDocPhoto(doctorsListArraylist.get(i).getDocPhoto());
            country.setDocCity(doctorsListArraylist.get(i).getDocCity());
            country.setDocEncryptID(doctorsListArraylist.get(i).getDocEncryptID());
            country.setDocAreaInterest(doctorsListArraylist.get(i).getDocAreaInterest());
            country.setDocExperience(doctorsListArraylist.get(i).getDocExperience());
            country.setDocQualification(doctorsListArraylist.get(i).getDocQualification());
            country.setDocHospName(doctorsListArraylist.get(i).getDocHospName());
            country.setDocHospAddress(doctorsListArraylist.get(i).getDocHospAddress());
            country.setDocHospCity(doctorsListArraylist.get(i).getDocHospCity());
            country.setDocHospState(doctorsListArraylist.get(i).getDocHospState());
            country.setDocHospCountry(doctorsListArraylist.get(i).getDocHospCountry());*/

            countries.add(new DoctorList(doctorsListArraylist.get(i).getDocId(), doctorsListArraylist.get(i).getDocName(),
                    doctorsListArraylist.get(i).getDocPhoto(), doctorsListArraylist.get(i).getDocCity(), doctorsListArraylist.get(i).getDocAreaInterest(),
                    doctorsListArraylist.get(i).getDocExperience(), doctorsListArraylist.get(i).getDocQualification(), doctorsListArraylist.get(i).getDocEncryptID(),
                    doctorsListArraylist.get(i).getDocGeoLatitude(), doctorsListArraylist.get(i).getDocGeoLongitude(), doctorsListArraylist.get(i).getDocConsulted(),
                    doctorsListArraylist.get(i).getDocSpecialization(), doctorsListArraylist.get(i).getDocHospital()));

            //   countries.add(country);
        }
        adapter = new DoctorsAdapter(this, R.layout.doctor_list_item, R.id.doc_list_name, countries);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void collectSpecializationDetails() {
        progressDialog1 = new ProgressDialog(DoctorsListActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog1.setIndeterminate(true);
        progressDialog1.setMessage("Loading Specialization....");
        progressDialog1.show();

        specializationListArraylist = new ArrayList<>();

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        StringRequest postRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_SPECIALIZATION,
                this,this ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(APIParam.KEY_API, APIClass.API_KEY);
                return params;
            }
        };

        postRequest.setTag(REQUEST_TAG);
        mQueue.add(postRequest);
    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
       if( progressDialog1.isShowing()) {
           progressDialog1.dismiss();
       }
        VolleyLog.e(AppUtils.TAG +" Error: ", error.getMessage());
        AppUtils.showCustomAlertMessage(DoctorsListActivity.this, "Doctors",error.getMessage(), "OK", null, null);

    }

    @Override
    public void onResponse(Object response) {
        if( progressDialog1.isShowing()) {
            progressDialog1.dismiss();
        }
        Log.d(AppUtils.TAG +" SpecResult: ", response.toString());
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
                            specialization = new SpecializationList(jsonArray.getJSONObject(i).getInt("spec_id"), jsonArray.getJSONObject(i).getString("spec_name"));
                            specializationListArraylist.add(specialization);
                        }

                        prepareSpecializationLists(specializationListArraylist);
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

    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    private void prepareSpecializationLists(List<SpecializationList> specializationListArraylist) {

        final Dialog dialog = new Dialog(DoctorsListActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_doctor_filter);
        dialog.setCanceledOnTouchOutside(true);

        final ListView specialization_listview = (ListView) dialog.findViewById(R.id.specialization_list);
        final CustomTextViewSemiBold all_doctors_list = (CustomTextViewSemiBold) dialog.findViewById(R.id.specialization_alldoctors);

        all_doctors_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromUrl(url_page1);        // 1 - Universal Doctors
                dialog.dismiss();
            }
        });

        final CustomSpecFilterAdapter specAdapter = new CustomSpecFilterAdapter(DoctorsListActivity.this,
                R.layout.activity_listview, specializationListArraylist);
        specialization_listview.setAdapter(specAdapter);
        specialization_listview.setTextFilterEnabled(true);

        final SearchView specialization_searchView = (SearchView) dialog.findViewById(R.id.doctor_specialization_search_view);
        specialization_searchView.setIconifiedByDefault(false);
        specialization_searchView.setQueryHint("Search Specialization Here");

        specialization_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                specialization_searchView.setQuery("", false);
                specialization_searchView.clearFocus();
                specialization_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(AppUtils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(AppUtils.TAG, "is empty " );
                    specAdapter.filter(newText.toString());
                    // specialization_listview.clearTextFilter();
                } else {
                    //  specialization_listview.setFilterText(newText.toString());
                    specAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        specialization_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                specialization_searchView.setQuery("", false);
                specialization_searchView.setIconified(false);
                specialization_searchView.clearFocus();
                return false;
            }
        });

        specialization_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SpecializationList myCustomAdapterItem = (SpecializationList) parent.getItemAtPosition(position);
              /*  Toast.makeText(getActivity(), "Clicked Position " + position + ": " + myCustomAdapterItem.getSpecializationId()+" : "+myCustomAdapterItem.getSpecializationName(),
                        Toast.LENGTH_SHORT)
                        .show();*/
                specialization_searchView.setQuery("", false);
                specialization_searchView.setIconified(false);
                specialization_searchView.clearFocus();

                DOCTOR_FILTER_TYPE = "2";
                DOCTOR_SPEC_ID = myCustomAdapterItem.getSpecializationId();
                DOCTOR_CITY = "";
                PAGINATION_NEXT = 1;
                getDataFromUrlSpecialization(url_page1);        // 2 - Specialization doctors
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    // calling asynctask to get json data from internet
    private void getDataFromUrlSpecialization(String url) {

        countries = new ArrayList<DoctorList>();
        adapter = new DoctorsAdapter(this, R.layout.doctor_list_item, R.id.doc_list_name, countries);
        listView.setAdapter(adapter);
        adapter.clear();
        adapter.notifyDataSetChanged();

        Log.d(AppUtils.TAG,  " URL DOCTOR_FILTER_TYPE"+  DOCTOR_FILTER_TYPE);
        Log.d(AppUtils.TAG,  " URL DOCTOR_SPEC_ID"+  DOCTOR_SPEC_ID);
        Log.d(AppUtils.TAG,  " URL DOCTOR_CITY"+  DOCTOR_CITY);
        new LoadDoctorsFromUrlTask(this, url, DOCTOR_FILTER_TYPE, DOCTOR_SPEC_ID, DOCTOR_CITY, PAGINATION_NEXT).execute();
    }


    private void prepareCityLists(ArrayList<String> citiesList) {

        citiesList.add("Amritsar");
        citiesList.add("Aurangabad");
        citiesList.add("Bareilly");
        citiesList.add("Bengaluru");
        citiesList.add("Bharatpur");
        citiesList.add("Bhubaneswar");
        citiesList.add("Bikramganj");
        citiesList.add("Chandigarh");
        citiesList.add("Chennai");
        citiesList.add("Chittagong");
        citiesList.add("Coimbatore");
        citiesList.add("Delhi");
        citiesList.add("Ghaziabad");
        citiesList.add("Hyderabad");
        citiesList.add("Indore");
        citiesList.add("Jaipur");
        citiesList.add("kolkata");
        citiesList.add("Manipal");
        citiesList.add("Mumbai");
        citiesList.add("Nashik");
        citiesList.add("Navi Mumbai");
        citiesList.add("New Delhi");
        citiesList.add("Pune");
        citiesList.add("Raipur");
        citiesList.add("Thane");
        citiesList.add("Udupi");
        citiesList.add("Vijayawada");

        final Dialog dialog = new Dialog(DoctorsListActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_doctor_filter_city);
        dialog.setCanceledOnTouchOutside(true);

        final ListView cities_listview = (ListView) dialog.findViewById(R.id.city_list);
        final CustomTextViewSemiBold all_doctors_list = (CustomTextViewSemiBold) dialog.findViewById(R.id.city_alldoctors);

        all_doctors_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromUrl(url_page1);        // 1 - Universal Doctors
                dialog.dismiss();
            }
        });

        final CustomCityFilterAdapter specAdapter = new CustomCityFilterAdapter(DoctorsListActivity.this,
                R.layout.activity_listview, citiesList);
        cities_listview.setAdapter(specAdapter);
        cities_listview.setTextFilterEnabled(true);

        final SearchView city_searchView = (SearchView) dialog.findViewById(R.id.doctor_city_search_view);
        city_searchView.setIconifiedByDefault(false);
        city_searchView.setQueryHint("Search City Here");

        city_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                city_searchView.setQuery("", false);
                city_searchView.clearFocus();
                city_searchView.setIconified(true);

                DOCTOR_FILTER_TYPE = "3";
                DOCTOR_SPEC_ID = 0;
                DOCTOR_CITY = newText;
                PAGINATION_NEXT = 1;
                getDataFromUrlSpecialization(url_page1);        // 2 - Specialization doctors
                dialog.dismiss();


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(AppUtils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(AppUtils.TAG, "is empty " );
                    specAdapter.filter(newText.toString());
                    // specialization_listview.clearTextFilter();
                } else {
                    //  specialization_listview.setFilterText(newText.toString());
                    specAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        city_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                city_searchView.setQuery("", false);
                city_searchView.setIconified(false);
                city_searchView.clearFocus();
                return false;
            }
        });

        cities_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String myCustomAdapterItem = (String) parent.getItemAtPosition(position);
              /*  Toast.makeText(DoctorsListActivity.this, "Clicked Position " + position + ": " + myCustomAdapterItem,
                        Toast.LENGTH_SHORT)
                        .show();*/
                city_searchView.setQuery("", false);
                city_searchView.setIconified(false);
                city_searchView.clearFocus();

                DOCTOR_FILTER_TYPE = "3";
                DOCTOR_SPEC_ID = 0;
                DOCTOR_CITY = myCustomAdapterItem;
                PAGINATION_NEXT = 1;
                getDataFromUrlSpecialization(url_page1);        // 3 - Cities doctors
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem item1 = menu.findItem(R.id.action_doctors);
        item1.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.action_filter);
        item2.setVisible(false);

        return true;
    }

}
