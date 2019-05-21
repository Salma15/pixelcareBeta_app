package com.fdc.pixelcare.Activities.HomeOld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.Activities.Appointments.DoctorsListActivity;
import com.fdc.pixelcare.Activities.Consultations.ConsultationListActivity;
import com.fdc.pixelcare.Activities.Opinions.MedicalOpinionActivity;
import com.fdc.pixelcare.Activities.Others.BlogListActivity;
import com.fdc.pixelcare.Adapters.HomeOld.HomeBlogsAdapter;
import com.fdc.pixelcare.Adapters.HomeOld.HomeDoctorListAdapter;
import com.fdc.pixelcare.Adapters.HomeOld.HomePageSliderAdapter;
import com.fdc.pixelcare.DataModel.BlogsList;
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
import com.fdc.pixelcare.Views.CustomTextViewBold;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeOldFragment extends Fragment implements View.OnClickListener, Response.Listener,
        Response.ErrorListener {

    public static final String REQUEST_TAG = "HomeFragment";
    ViewPager viewPager;
    HomePageSliderAdapter viewPagerAdapter;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    Timer timer;

    LinearLayout book_appointment, medical_opinion, medical_profile, order_medicine, offers, international_patients;
    RecyclerView doctorlist_recyclerview;
    HomeDoctorListAdapter groceryAdapter;
    LinearLayoutManager horizontalLayoutManager;
    List<DoctorList> doctorsListArraylist;
    DoctorList doctors;
    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int USER_ID, USER_LOGINTYPE;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION, DOCTORS_LIST, BLOGS_LIST;
    private RequestQueue mQueue;

    RecyclerView blogslist_recyclerview;
    HomeBlogsAdapter blogsAdapter;
    List<BlogsList> blogsListArraylist, homeBlogListArraylist;
    BlogsList blogs;
    CustomTextViewBold view_all_doctors_btn, view_all_blogs_btn;

    ArrayList<SpecializationList> specilizationDocArraylist;
    ArrayList<HospitalList> hospitalDocArraylist;

    final int duration = 10;
    final int pixelsToMove = 30;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable SCROLLING_RUNNABLE = new Runnable() {

        @Override
        public void run() {
            doctorlist_recyclerview.smoothScrollBy(pixelsToMove, 0);
            mHandler.postDelayed(this, duration);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_old,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doctorsListArraylist = new ArrayList<>();
        blogsListArraylist = new ArrayList<>();
        homeBlogListArraylist = new ArrayList<>();
        specilizationDocArraylist = new ArrayList<>();
        hospitalDocArraylist = new ArrayList<>();

        shareadPreferenceClass = new ShareadPreferenceClass(getActivity());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getActivity());

        if(sharedPreferences !=null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE  = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);
            DOCTORS_LIST = sharedPreferences.getString(MHConstants.PREF_DOCTORS_LIST, "");
            BLOGS_LIST = sharedPreferences.getString(MHConstants.PREF_BLOGS_LIST, "");

            Log.d(AppUtils.TAG , " *********** Home Page **************** ");
            Log.d(AppUtils.TAG + " UserID: ", String.valueOf(USER_ID));
            Log.d(AppUtils.TAG + " name: ", USER_NAME);
            Log.d(AppUtils.TAG + " email: ", USER_EMAIL);
            Log.d(AppUtils.TAG + " MOBILE: ", USER_MOBILE);
            Log.d(AppUtils.TAG + " Location: ", USER_LOCATION);
            Log.d(AppUtils.TAG + " logintype: ", String.valueOf(USER_LOGINTYPE));
        }
        initializeViews();
    }

    private void initializeViews() {

        book_appointment = (LinearLayout) getActivity().findViewById(R.id.home_book_appointment);
        medical_opinion = (LinearLayout) getActivity().findViewById(R.id.home_medical_opinion);
        medical_profile = (LinearLayout) getActivity().findViewById(R.id.home_medical_profile);
        order_medicine = (LinearLayout) getActivity().findViewById(R.id.home_order_medicine);
        offers = (LinearLayout) getActivity().findViewById(R.id.home_offers);
        international_patients = (LinearLayout) getActivity().findViewById(R.id.home_international_patients);
        book_appointment.setOnClickListener(this);
        medical_opinion.setOnClickListener(this);
        medical_profile.setOnClickListener(this);
        order_medicine.setOnClickListener(this);
        offers.setOnClickListener(this);
        international_patients.setOnClickListener(this);

        doctorlist_recyclerview = (RecyclerView) getActivity().findViewById(R.id.home_doctorlist_recyclerview);
        doctorlist_recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));
        groceryAdapter = new HomeDoctorListAdapter(doctorsListArraylist, getActivity());
        horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        doctorlist_recyclerview.setLayoutManager(horizontalLayoutManager);
        doctorlist_recyclerview.setAdapter(groceryAdapter);
        populateDoctorsList();

        DividerItemDecoration verticalDecoration = new DividerItemDecoration(doctorlist_recyclerview.getContext(),
                DividerItemDecoration.HORIZONTAL);
        Drawable verticalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.vertical_divider);
        verticalDecoration.setDrawable(verticalDivider);
        doctorlist_recyclerview.addItemDecoration(verticalDecoration);
        viewPager = (ViewPager) getActivity().findViewById(R.id.homepage_viewPager);
        sliderDotspanel = (LinearLayout) getActivity().findViewById(R.id.homepage_SliderDots);
        viewPagerAdapter = new HomePageSliderAdapter(getActivity());
        viewPager.setAdapter(viewPagerAdapter);


        blogslist_recyclerview = (RecyclerView) getActivity().findViewById(R.id.home_blogslist_recyclerview);
        blogsAdapter = new HomeBlogsAdapter(getActivity(), blogsListArraylist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        blogslist_recyclerview.setLayoutManager(mLayoutManager);
        blogslist_recyclerview.setItemAnimator(new DefaultItemAnimator());
        blogslist_recyclerview.setAdapter(blogsAdapter);
        prepareBlogListsData();

        view_all_doctors_btn = (CustomTextViewBold)  getActivity().findViewById(R.id.home_doctorlist_view);
        view_all_doctors_btn.setOnClickListener(this);

       // view_all_blogs_btn = (CustomTextViewBold)  getActivity().findViewById(R.id.home_bloglist_view);
       // view_all_blogs_btn.setOnClickListener(this);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                viewPager.post(new Runnable(){

                    @Override
                    public void run() {
                        viewPager.setCurrentItem((viewPager.getCurrentItem()+1)%dotscount);
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 3000);


    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_book_appointment:
               /* Intent i1 = new Intent(getActivity(), NewBookAppointmentActivity.class);
                i1.putExtra("title","Book Appointment");
                startActivity(i1);*/
                Intent i1 = new Intent(getActivity(), DoctorsListActivity.class);
                i1.putExtra("title","View Doctors");
                startActivity(i1);
                break;
            case R.id.home_medical_opinion:
                Intent i2 = new Intent(getActivity(), DoctorsListActivity.class);
                i2.putExtra("title","Doctors Opinion");
                startActivity(i2);
                break;
            case R.id.home_medical_profile:
                Intent i3 = new Intent(getActivity(), ConsultationListActivity.class);
                i3.putExtra("title","My Consultations");
                startActivity(i3);
                break;
            case R.id.home_order_medicine:
              //  Toast.makeText(getActivity(), "Will be coming soon !!!", Toast.LENGTH_SHORT).show();
                Intent i4 = new Intent(getActivity(), BlogListActivity.class);
                i4.putExtra("title","Blogs");
                startActivity(i4);
                break;
            case R.id.home_doctorlist_view:
                Intent i5 = new Intent(getActivity(), DoctorsListActivity.class);
                i5.putExtra("title","View Doctors");
                startActivity(i5);
                break;
          /*  case R.id.home_offers:
                Intent i6 = new Intent(getActivity(), OffersListActivity.class);
                i6.putExtra("title","Offers");
                startActivity(i6);
                break;
            case R.id.home_international_patients:
                Intent i7 = new Intent(getActivity(), InternationalPatientsActivity.class);
                i7.putExtra("title","International Patients");
                startActivity(i7);
                break;
            case R.id.home_bloglist_view:
                Intent i8 = new Intent(getActivity(), BlogsListActivity.class);
                i8.putExtra("title","Blogs");
                startActivity(i8);
                break;*/
        }
    }

    private void prepareBlogListsData() {

        Gson gson = new Gson();
        if (BLOGS_LIST.equals("")) {
            Log.d(AppUtils.TAG, " prepareBlogListsData  0" );
            if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                collectBlogsListsDetails();
            } else {
                AppUtils.showCustomAlertMessage(getActivity(), MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            blogsListArraylist = gson.fromJson(BLOGS_LIST, new TypeToken<List<BlogsList>>() {
            }.getType());
            if(blogsListArraylist.size() > 0 ) {
                Log.d(AppUtils.TAG, " prepareBlogListsData " +blogsListArraylist.size());
                prepareBlogsLists(blogsListArraylist);
            }
        }
    }


    private void populateDoctorsList() {

        Gson gson = new Gson();
        if (DOCTORS_LIST.equals("")) {
            Log.d(AppUtils.TAG, " populateDoctorsList  0" );
            if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                collectDoctorsListsDetails();
            } else {
                AppUtils.showCustomAlertMessage(getActivity(), MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            doctorsListArraylist = gson.fromJson(DOCTORS_LIST, new TypeToken<List<DoctorList>>() {
            }.getType());
            if(doctorsListArraylist.size() > 0 ) {
                Log.d(AppUtils.TAG, " populateDoctorsList " +doctorsListArraylist.size());
                prepareDoctorsLists(doctorsListArraylist);
            }
        }
    }

    private void collectBlogsListsDetails() {

        // Instantiate the RequestQueue.
        mQueue = CustomVolleyRequestQueue.getInstance(getActivity()).getRequestQueue();

        StringRequest postRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_BLOGS,
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

    private void collectDoctorsListsDetails() {
        // Instantiate the RequestQueue.
        mQueue = CustomVolleyRequestQueue.getInstance(getActivity()).getRequestQueue();

        StringRequest postRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_DOCTOR_LIST,
                this,this ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(APIParam.KEY_API, APIClass.API_KEY);
                params.put(APIParam.KEY_DOC_FILTER_TYPE, "1");
                params.put(APIParam.KEY_DOC_SPECID, "0");
                params.put(APIParam.KEY_DOC_CITY, "");
                params.put(APIParam.KEY_PAGINATION, "1");
                return params;
            }
        };

        postRequest.setTag(REQUEST_TAG);
        mQueue.add(postRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        VolleyLog.e(AppUtils.TAG +" Error: ", error.getMessage());
    }

    @Override
    public void onResponse(Object response) {
        specilizationDocArraylist = new ArrayList<>();
        hospitalDocArraylist = new ArrayList<>();

        try {
            Log.d(AppUtils.TAG +" Result: ", response.toString());
            JSONArray jArray = null, jArrayBlogs = null;
            JSONObject json = new JSONObject(response.toString());
            // JSONArray jArray = new JSONArray(json.getString("doctor_list"));
            //  JSONArray jArrayBlogs = new JSONArray(json.getString("blog_details"));

            if (json.has("doctor_list")) {
                jArray = new JSONArray(json.getString("doctor_list"));
            }
            else { }

            if (json.has("blog_details")) {
                jArrayBlogs = new JSONArray(json.getString("blog_details"));
            }
            else { }

            if((jArray != null ) && (jArray.length() > 0 )) {
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);

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

                    doctorsListArraylist.add(new DoctorList(jObject.getInt("ref_id"), jObject.getString("ref_name"),
                            jObject.getString("doc_photo"), jObject.getString("doc_city"),
                            jObject.getString("doc_interest"), jObject.getString("doc_exp"),
                            jObject.getString("doc_qual"), jObject.getString("doc_encyid"), jObject.getString("geo_latitude"),
                            jObject.getString("geo_longitude"), jObject.getInt("doc_consulted"), specilizationDocArraylist, hospitalDocArraylist));
                }

                if (doctorsListArraylist.size() > 0) {
                    Gson gson = new Gson();
                    String jsonText = gson.toJson(doctorsListArraylist);
                    if (sharedPreferences != null) {
                        shareadPreferenceClass.clearDoctorsLists();
                        shareadPreferenceClass.setDoctorsList(jsonText);
                    }
                }

                prepareDoctorsLists(doctorsListArraylist);

                /*for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    doctors = new DoctorList(jObject.getInt("ref_id"), jObject.getString("ref_name"),jObject.getInt("spec_id"),
                            jObject.getString("spec_name"), jObject.getString("doc_photo"), jObject.getString("doc_city"),
                            jObject.getString("doc_encyid"),jObject.getString("doc_interest"), jObject.getString("doc_exp"),
                            jObject.getString("doc_qual"),jObject.getString("hosp_name"), jObject.getString("hosp_addrs"),
                            jObject.getString("hosp_city"), jObject.getString("hosp_state"),jObject.getString("hosp_country"), jObject.getString("doc_encyid") );
                    doctorsListArraylist.add(doctors);

                }

                if(doctorsListArraylist.size() > 0) {
                    Gson gson = new Gson();
                    String jsonText = gson.toJson(doctorsListArraylist);
                    if (sharedPreferences != null) {
                        shareadPreferenceClass.clearDoctorsLists();
                        shareadPreferenceClass.setDoctorsList(jsonText);
                    }
                }

                prepareDoctorsLists(doctorsListArraylist);*/
            }

            if((jArrayBlogs != null ) && (jArrayBlogs.length() > 0 )) {
                for (int i = 0; i < jArrayBlogs.length(); i++) {
                    JSONObject JOStuff = jArrayBlogs.getJSONObject(i);
                    blogsListArraylist.add(new BlogsList(JOStuff.getInt("post_id"),JOStuff.getString("title"),
                            JOStuff.getString("description"),JOStuff.getString("post_image"),
                            JOStuff.getString("created_date"),JOStuff.getString("listing_type"),
                            JOStuff.getString("username"),JOStuff.getString("userprof"),
                            JOStuff.getString("userimg"), JOStuff.getString("contactInfo"),
                            JOStuff.getString("attachments"), JOStuff.getInt("company_id"),
                            JOStuff.getString("transaction_id"), JOStuff.getString("num_views"),
                            JOStuff.getString("video_url"),JOStuff.getString("from_to_date")));

                }

                if(blogsListArraylist.size() > 0) {
                    Gson gson = new Gson();
                    String jsonText = gson.toJson(blogsListArraylist);
                    if (sharedPreferences != null) {
                        shareadPreferenceClass.clearBlogsLists();
                        shareadPreferenceClass.setBlogsList(jsonText);
                    }
                }

                prepareBlogsLists(blogsListArraylist);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void prepareBlogsLists(List<BlogsList> blogsListArraylist) {

//        blogsAdapter = new HomeBlogsAdapter(getActivity(), blogsListArraylist);
//        blogslist_recyclerview.setAdapter(blogsAdapter);

        homeBlogListArraylist = new ArrayList<>();

        if(blogsListArraylist.size()> 2) {
            for (int i = 0; i < 2; i++) {
                homeBlogListArraylist.add(new BlogsList(blogsListArraylist.get(i).getBlogId(),blogsListArraylist.get(i).getBlogTitle(),
                        blogsListArraylist.get(i).getBlogDescription(),blogsListArraylist.get(i).getBlogImage(),
                        blogsListArraylist.get(i).getBlogPostdate(),blogsListArraylist.get(i).getBlogPostType(),
                        blogsListArraylist.get(i).getBlogUserName(),blogsListArraylist.get(i).getBlogUserProf(),
                        blogsListArraylist.get(i).getBlogUserImage(), blogsListArraylist.get(i).getBlogContactInfo(),
                        blogsListArraylist.get(i).getBlogAttachments(), blogsListArraylist.get(i).getBlogCompanyId(),
                        blogsListArraylist.get(i).getBlogTransactionID(), blogsListArraylist.get(i).getBlogNumberViews(),
                        blogsListArraylist.get(i).getBlogVideoURL(), blogsListArraylist.get(i).getBlogFromToDate()));
            }
            blogsAdapter = new HomeBlogsAdapter(getActivity(), homeBlogListArraylist);
            blogslist_recyclerview.setAdapter(blogsAdapter);
        }
        else {
            blogsAdapter = new HomeBlogsAdapter(getActivity(), blogsListArraylist);
            blogslist_recyclerview.setAdapter(blogsAdapter);
        }

    }

    private void prepareDoctorsLists(List<DoctorList> doctorsListArraylist) {
        groceryAdapter = new HomeDoctorListAdapter(doctorsListArraylist, getActivity());
        doctorlist_recyclerview.setAdapter(groceryAdapter);

        doctorlist_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = horizontalLayoutManager.findLastCompletelyVisibleItemPosition();
                if(lastItem == horizontalLayoutManager.getItemCount()-1){
                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                    Handler postHandler = new Handler();
                    postHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doctorlist_recyclerview.setAdapter(null);
                            doctorlist_recyclerview.setAdapter(groceryAdapter);
                            mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);
                        }
                    }, 2000);
                }
            }
        });
        mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

}
