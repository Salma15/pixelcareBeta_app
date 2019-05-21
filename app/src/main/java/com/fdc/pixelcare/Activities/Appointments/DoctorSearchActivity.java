package com.fdc.pixelcare.Activities.Appointments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.fdc.pixelcare.Adapters.Appointments.DoctorsAdapter;
import com.fdc.pixelcare.DataModel.DoctorList;
import com.fdc.pixelcare.DataModel.HospitalList;
import com.fdc.pixelcare.DataModel.SpecializationList;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
import com.fdc.pixelcare.Views.CustomTextViewItalicBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SALMA on 22/12/18.
 */

public class DoctorSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private int pageCount = 0;
    private DoctorsAdapter adapter;
    private ListView listView;
    private ArrayList<DoctorList> countries;
    private String url_page1 = APIClass.DRS_HOST_DOCTOR_SEARCH;
    List<DoctorList> doctorsListArraylist;
    DoctorList doctors;
    int PAGINATION_NEXT = 1;
    RelativeLayout progress_layout;
    public static final String REQUEST_TAG = "DoctorSearchActivity";
    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int USER_ID, USER_LOGINTYPE;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION, SPECIALIZATION_LIST, DOCTORS_LIST;
    LinearLayout filter_specialization, filter_city;
    String DOCTOR_FILTER_TYPE = "1",DOCTOR_CITY = "";
    private RequestQueue mQueue;
    int DOCTOR_SPEC_ID = 0;

    AutoCompleteTextView _header_seach_autotext;
    CustomTextViewItalicBold no_data;
    ImageView _header_search_cancel;

    ArrayList<SpecializationList> specilizationDocArraylist;
    ArrayList<HospitalList> hospitalDocArraylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        specilizationDocArraylist = new ArrayList<>();
        hospitalDocArraylist = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle b = getIntent().getExtras();
        String title = b.getString("title");
        setTitle(title);

        shareadPreferenceClass = new ShareadPreferenceClass(DoctorSearchActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(DoctorSearchActivity.this);

        if(sharedPreferences !=null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE  = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);
            SPECIALIZATION_LIST = sharedPreferences.getString(MHConstants.PREF_SPECIALIZATION_LIST, "");
            DOCTORS_LIST = sharedPreferences.getString(MHConstants.PREF_DOCTORS_LIST, "");

            Log.d(AppUtils.TAG , " *********** Doctors Searc **************** ");
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
        countries = new ArrayList<DoctorList>();
        progress_layout = (RelativeLayout)  findViewById(R.id.search_progressbar);
        progress_layout.setVisibility(View.GONE);
        url_page1 = APIClass.DRS_HOST_DOCTOR_SEARCH;

        listView = (ListView) findViewById(R.id.search_listview);
        setListViewAdapter();
      //  getDataFromUrl(url_page1);
        listView.setOnScrollListener(onScrollListener());

        _header_seach_autotext = (AutoCompleteTextView) findViewById(R.id.search_autocompletetext);
        _header_seach_autotext.setHint("Search by name, location, specialty...");
        _header_search_cancel = (ImageView) findViewById(R.id.search_cancel);
        _header_search_cancel.setOnClickListener(this);
        _header_search_cancel.setVisibility(View.GONE);

        no_data = (CustomTextViewItalicBold) findViewById(R.id.search_noresult);
        no_data.setVisibility(View.GONE);

        _header_seach_autotext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    _header_search_cancel.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    String search_result = _header_seach_autotext.getText().toString();
                  //  collectDoctorsSearchResultFromServer(search_result);
                    DOCTOR_CITY =  search_result;
                    DOCTOR_FILTER_TYPE = "3";
                    DOCTOR_SPEC_ID = 0;
                    PAGINATION_NEXT = 1;
                    getDataFromUrlDoctors(url_page1);
                    return true;
                }
                return false;
            }
        });

        _header_seach_autotext.setThreshold(1);//will start working from first character
        _header_seach_autotext.setTextColor(getResources().getColor(R.color.colorPrimary));
         adapter = new DoctorsAdapter(this, R.layout.doctor_list_item, R.id.doc_list_name, countries);
        _header_seach_autotext.setAdapter(adapter);

        _header_seach_autotext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // adapter.notifyDataSetChanged();
                _header_search_cancel.setVisibility(View.VISIBLE);

              /*  DOCTOR_CITY =  _header_seach_autotext.getText().toString();
                DOCTOR_FILTER_TYPE = "3";
                DOCTOR_SPEC_ID = 0;
                PAGINATION_NEXT = 1;
                getDataFromUrlDoctors(url_page1);*/
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        _header_seach_autotext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    _header_search_cancel.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    DOCTOR_CITY =  _header_seach_autotext.getText().toString();
                    DOCTOR_FILTER_TYPE = "3";
                    DOCTOR_SPEC_ID = 0;
                    PAGINATION_NEXT = 1;
                    getDataFromUrlDoctors(url_page1);
                    return true;
                }
                return false;
            }
        });
    }

    private void getDataFromUrlDoctors(String url_page1) {
        countries.clear();
        countries = new ArrayList<DoctorList>();
        adapter = new DoctorsAdapter(this, R.layout.doctor_list_item, R.id.doc_list_name, countries);
        listView.setAdapter(adapter);
        adapter.clear();
        adapter.notifyDataSetChanged();

        Log.d(AppUtils.TAG,  " URL DOCTOR_FILTER_TYPE"+  DOCTOR_FILTER_TYPE);
        Log.d(AppUtils.TAG,  " URL DOCTOR_SPEC_ID"+  DOCTOR_SPEC_ID);
        Log.d(AppUtils.TAG,  " URL DOCTOR_CITY"+  DOCTOR_CITY);
        new LoadDoctorsSearchFromUrlTask(this, url_page1, DOCTOR_FILTER_TYPE, DOCTOR_SPEC_ID, DOCTOR_CITY, PAGINATION_NEXT).execute();

    }

    private void setListViewAdapter() {
        countries = new ArrayList<DoctorList>();
        adapter = new DoctorsAdapter(this, R.layout.doctor_list_item, R.id.doc_list_name, countries);
        listView.setAdapter(adapter);
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

    private void getDataFromUrl(String url) {
        progress_layout.setVisibility(View.VISIBLE);
        new LoadDoctorsSearchFromUrlTask(this, url, DOCTOR_FILTER_TYPE, DOCTOR_SPEC_ID, DOCTOR_CITY, PAGINATION_NEXT).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_cancel:
                _header_search_cancel.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
        }
    }

    //parsing json after getting from Internet
    public void parseJsonResponse(String result) {
          Log.d(AppUtils.TAG, " SEARCH RESULT:"+ result);

        specilizationDocArraylist = new ArrayList<>();
        hospitalDocArraylist = new ArrayList<>();

        progress_layout.setVisibility(View.GONE);
        pageCount++;
        try {
            JSONObject json = new JSONObject(result);
            PAGINATION_NEXT = json.getInt("pagination_val");
            Log.i(AppUtils.TAG, " pagination_val:"+  json.getInt("pagination_val"));
            JSONArray jArray = new JSONArray(json.getString("doctor_list"));
            if(jArray.length() > 0) {
                listView.setVisibility(View.VISIBLE);
                no_data.setVisibility(View.GONE);
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);
                  /*  DoctorList country = new DoctorList();
                    country.setDocId(jObject.getInt("ref_id"));
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
                          //  Log.d(AppUtils.TAG, "specid: "+jsonArray1.getJSONObject(j).getInt("spec_id")+ " groupID:"+ jsonArray1.getJSONObject(j).getInt("spec_group_id"));
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


                    //   countries.add(country);
                }

                adapter.notifyDataSetChanged();
            }
            else {
                listView.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
