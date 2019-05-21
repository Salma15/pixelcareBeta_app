package com.fdc.pixelcare.Activities.Home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.fdc.pixelcare.Adapters.Home.HospitalAdapter;
import com.fdc.pixelcare.Adapters.Home.HospitalSeachMapAdapter;
import com.fdc.pixelcare.DataModel.HospitalList;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppController;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
import com.fdc.pixelcare.Views.CustomTextViewItalic;
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
 * Created by SALMA on 21-12-2018.
 */
public class HospitalSearchActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String REQUEST_TAG = "HospitalSearchActivity";
    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int USER_ID, USER_LOGINTYPE;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION, SPECIALIZATION_LIST, DOCTORS_LIST, HOSPITAL_MAP_LIST;

    int SELECTED_CITY_ID = 0, SELECTED_SPEC_ID = 0, SELECTED_FILTER_TYPE = 0;
    double SELECTED_GEO_LATITUDE = 0.00, SELECTED_GEO_LONGITUDE = 0.00;

    AutoCompleteTextView _header_seach_autotext;
    ImageView _header_search_cancel;

    List<HospitalList> hospitalListArraylist = new ArrayList<>();
    RecyclerView recyclerView_hospitallist;
    HospitalAdapter adapter;
    CustomTextViewItalic no_data;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);
        hospitalListArraylist = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle b = getIntent().getExtras();
        String title = b.getString("title");
        setTitle(title);

        shareadPreferenceClass = new ShareadPreferenceClass(HospitalSearchActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(HospitalSearchActivity.this);

        if(sharedPreferences !=null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE  = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);
            SPECIALIZATION_LIST = sharedPreferences.getString(MHConstants.PREF_SPECIALIZATION_LIST, "");
            DOCTORS_LIST = sharedPreferences.getString(MHConstants.PREF_DOCTORS_LIST, "");
            HOSPITAL_MAP_LIST = sharedPreferences.getString(MHConstants.PREF_HOSPITALS_MAP_LIST, "");

            SELECTED_CITY_ID = sharedPreferences.getInt(MHConstants.PREF_CITY_ID, 0);
            SELECTED_SPEC_ID = sharedPreferences.getInt(MHConstants.PREF_SPECIALTY_ID, 0);
            SELECTED_FILTER_TYPE  = sharedPreferences.getInt(MHConstants.PREF_HOME_FILTER, 0);
            SELECTED_GEO_LATITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_MY_LATITUDE, ""));
            SELECTED_GEO_LONGITUDE = Double.parseDouble(sharedPreferences.getString(MHConstants.PREF_MY_LONGITUDE, ""));

            Log.d(AppUtils.TAG , " *********** HospitalSearchActivity **************** ");
            Log.d(AppUtils.TAG + " UserID: ", String.valueOf(USER_ID));
            Log.d(AppUtils.TAG + " name: ", USER_NAME);
            Log.d(AppUtils.TAG + " email: ", USER_EMAIL);
            Log.d(AppUtils.TAG + " MOBILE: ", USER_MOBILE);
            Log.d(AppUtils.TAG + " Location: ", USER_LOCATION);
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

        hospitalListArraylist = new ArrayList<>();

        _header_seach_autotext = (AutoCompleteTextView) findViewById(R.id.search_autocompletetext);
        _header_seach_autotext.setHint("Search Hospital Here...");
        _header_search_cancel = (ImageView) findViewById(R.id.search_cancel);
        _header_search_cancel.setOnClickListener(this);
        _header_search_cancel.setVisibility(View.GONE);

        Gson gson = new Gson();
        if (HOSPITAL_MAP_LIST.equals("")) {} else {
            hospitalListArraylist = gson.fromJson(HOSPITAL_MAP_LIST, new TypeToken<List<HospitalList>>() {
            }.getType());
        }

        recyclerView_hospitallist = (RecyclerView) findViewById(R.id.search_recycleriew);
        no_data = (CustomTextViewItalic) findViewById(R.id.search_noresult);
        no_data.setVisibility(View.GONE);

        adapter = new HospitalAdapter(HospitalSearchActivity.this, hospitalListArraylist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HospitalSearchActivity.this);
        recyclerView_hospitallist.setLayoutManager(mLayoutManager);
        recyclerView_hospitallist.setItemAnimator(new DefaultItemAnimator());
        recyclerView_hospitallist.setHasFixedSize(true);
        recyclerView_hospitallist.setItemViewCacheSize(20);
        recyclerView_hospitallist.setDrawingCacheEnabled(true);
        recyclerView_hospitallist.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        _header_seach_autotext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    _header_search_cancel.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    String search_result = _header_seach_autotext.getText().toString();
                    collectHospitalSearchResultFromServer(search_result);
                    return true;
                }
                return false;
            }
        });

        _header_seach_autotext.setThreshold(1);//will start working from first character
        _header_seach_autotext.setTextColor(getResources().getColor(R.color.colorPrimary));
        final HospitalSeachMapAdapter adapter = new HospitalSeachMapAdapter(HospitalSearchActivity.this, R.layout.activity_search_map_item, R.id.lbl_name, hospitalListArraylist);
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

                adapter = new HospitalAdapter(HospitalSearchActivity.this, hospitalListArraylist);
                recyclerView_hospitallist.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
        }
    }


    private void collectHospitalSearchResultFromServer(final String search_result) {

        Log.d(AppUtils.TAG + " search_result: ", search_result);
        Log.d(AppUtils.TAG + " SELECT_CITY_ID: ", String.valueOf(SELECTED_CITY_ID));
        Log.d(AppUtils.TAG + " GEO_LATITUDE: ", String.valueOf(SELECTED_GEO_LATITUDE));
        Log.d(AppUtils.TAG + " GEO_LONGITUDE: ", String.valueOf(SELECTED_GEO_LONGITUDE));
        Log.d(AppUtils.TAG + " SELECT_SPEC_ID: ", String.valueOf(SELECTED_SPEC_ID));
        Log.d(AppUtils.TAG + " HOME_FILT_TYPE: ", String.valueOf(SELECTED_FILTER_TYPE));

        final ProgressDialog progressDialog = new ProgressDialog(HospitalSearchActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Searching Result...");
        progressDialog.show();

        hospitalListArraylist.clear();
        hospitalListArraylist = new ArrayList<>();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_HOSPITALS_MAP_NEW_SEARCH_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(AppUtils.TAG, "map list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1, jsonArray2;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("hospital_list");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jObject = jsonArray.getJSONObject(i);

                                            hospitalListArraylist.add(new HospitalList(jObject.getInt("hosp_id"),jObject.getString("hosp_name"),
                                                    jObject.getString("hosp_addrs"),jObject.getString("hosp_city"),jObject.getString("hosp_state"),
                                                    jObject.getString("hosp_country"),jObject.getString("hosp_contact"),jObject.getString("hosp_email"),
                                                    jObject.getString("geo_latitude"),jObject.getString("geo_longitude"),jObject.getString("hosp_logo"),
                                                    0));
                                        }

                                        Log.d(AppUtils.TAG, " resultsize: " + hospitalListArraylist.size());

                                        refreshRecyclerViews(hospitalListArraylist);
                                    } else {
                                        no_data.setVisibility(View.VISIBLE);
                                        no_data.setText("No Hospital Records Found !!!");
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
                map.put("search_string", search_result);
                map.put(APIParam.KEY_HOME_CITY_ID, String.valueOf(SELECTED_CITY_ID));
                map.put(APIParam.KEY_HOME_LATITUDE, String.valueOf(SELECTED_GEO_LATITUDE));
                map.put(APIParam.KEY_HOME_LONGITUDE, String.valueOf(SELECTED_GEO_LONGITUDE));
                map.put(APIParam.KEY_HOME_SPEC_ID, String.valueOf(SELECTED_SPEC_ID));
                map.put(APIParam.KEY_PAGINATION, String.valueOf(1));
                map.put(APIParam.KEY_HOME_FILTER_TYPE, String.valueOf(SELECTED_FILTER_TYPE));

                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(HospitalSearchActivity.this).
                getRequestQueue();
        AppController.getInstance(HospitalSearchActivity.this).addToRequestQueue(stringRequest);
    }

    private void refreshRecyclerViews(List<HospitalList> hospitalListArraylist) {
        adapter = new HospitalAdapter(HospitalSearchActivity.this, hospitalListArraylist);
        recyclerView_hospitallist.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
