package com.fdc.pixelcare.Activities.Opinions;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.Activities.Appointments.AppointmentListActivity;
import com.fdc.pixelcare.Activities.Appointments.DoctorsListActivity;
import com.fdc.pixelcare.Adapters.Opinions.OpinionAdapter;
import com.fdc.pixelcare.DataModel.FamilyMember;
import com.fdc.pixelcare.DataModel.OpinionList;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.Network.CustomVolleyRequestQueue;
import com.fdc.pixelcare.Network.NetworkUtil;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
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

/**
 * Created by SALMA on 31-12-2018.
 */
public class OpinionListActivity extends AppCompatActivity  implements Response.Listener, Response.ErrorListener{

    public static final String REQUEST_TAG = "OpinionListActivity";
    private RequestQueue mQueue;

    int USER_ID,USER_LOGINTYPE;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION, FAMILY_MEMBER_LIST, OPINIONS_LIST;
    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    ArrayList<FamilyMember> memberArraylist;
    int LOGIN_MEMBER_ID;
    String LOGIN_MEMBER_NAME;

    RecyclerView recyclerView;
    OpinionAdapter adapter;
    ArrayList<OpinionList> opinionArraylist;
    ProgressDialog pd;
    CustomTextViewItalicBold no_data;
    String OPINION_FILTER_TYPE = "1"; // 1 - initial Load, 2 - Member list load
    CustomTextViewItalicBold member_name_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_list);
        memberArraylist = new ArrayList<>();
        LOGIN_MEMBER_ID = 0;
        LOGIN_MEMBER_NAME = "";
        OPINION_FILTER_TYPE = "1";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);

        }

        shareadPreferenceClass = new ShareadPreferenceClass(getApplicationContext());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getApplicationContext());

        if (sharedPreferences != null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);
            FAMILY_MEMBER_LIST = sharedPreferences.getString(MHConstants.PREF_FAMILY_MEMBERS, "");
            LOGIN_MEMBER_ID = sharedPreferences.getInt(MHConstants.PREF_LOGIN_MEMBER_ID, 0);
            LOGIN_MEMBER_NAME = sharedPreferences.getString(MHConstants.PREF_LOGIN_MEMBER_NEME, "");
            OPINIONS_LIST = sharedPreferences.getString(MHConstants.PREF_OPINIONS_LIST, "");

            Log.d(AppUtils.TAG, " *********** OpinionListActivity **************** ");
            Log.d(AppUtils.TAG + " UserID: ", String.valueOf(USER_ID));
            Log.d(AppUtils.TAG + " name: ", USER_NAME);
            Log.d(AppUtils.TAG + " email: ", USER_EMAIL);
            Log.d(AppUtils.TAG + " MOBILE: ", USER_MOBILE);
            Log.d(AppUtils.TAG + " Location: ", USER_LOCATION);
            Log.d(AppUtils.TAG + " LOGIN_MEMBER_ID: ", String.valueOf(LOGIN_MEMBER_ID));
            Log.d(AppUtils.TAG + " LOGIN_MEMB_NAME: ", LOGIN_MEMBER_NAME);
            Log.d(AppUtils.TAG + " logintype: ", String.valueOf(USER_LOGINTYPE));
        }

        initializeViews();
    }

    private void initializeViews() {
        opinionArraylist = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.myopinion_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OpinionAdapter(this, opinionArraylist);
        recyclerView.setAdapter(adapter);


        no_data = (CustomTextViewItalicBold) findViewById(R.id.myopinion_list_nodata);
        no_data.setText("No opinion requested !!!");
        no_data.setVisibility(View.GONE);

        member_name_text = (CustomTextViewItalicBold) findViewById(R.id.opinion_membername);
        member_name_text.setText("Patient Name: "+LOGIN_MEMBER_NAME);

        // sendOpinionRequestToServer();

        Gson gson = new Gson();
        if (OPINIONS_LIST.equals("")) {
            if (NetworkUtil.getConnectivityStatusString(OpinionListActivity.this).equalsIgnoreCase("enabled")) {
                sendOpinionRequestToServer();
            } else {
                AppUtils.showCustomAlertMessage(OpinionListActivity.this, MHConstants.INTERNET, MHConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            opinionArraylist = gson.fromJson(OPINIONS_LIST, new TypeToken<List<OpinionList>>() {
            }.getType());
            if (opinionArraylist.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                no_data.setVisibility(View.GONE);
                prepareOpinionLists(opinionArraylist);
            }
            else {
                recyclerView.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
            }
        }
    }


    private void sendOpinionRequestToServer() {

        pd = new ProgressDialog(OpinionListActivity.this, R.style.CustomDialog);
        pd.setCancelable(false);
        pd.setMessage("Loading Opinions...");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();

        // Instantiate the RequestQueue.
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        StringRequest postRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_OPINION_LIST,
                this,this ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(APIParam.KEY_API, APIClass.API_KEY);
                params.put(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID));
                params.put(APIParam.KEY_MEMBER_ID, String.valueOf(LOGIN_MEMBER_ID));
                params.put(APIParam.KEY_APPOINT_FILTER_TYPE, String.valueOf(OPINION_FILTER_TYPE));
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
        AppUtils.showCustomAlertMessage(OpinionListActivity.this, "Warning !!!",error.getMessage(), "OK", null, null);
    }

    @Override
    public void onResponse(Object response) {
        Log.d(AppUtils.TAG +" Result: ", response.toString());
        String staus_res = null, appointment_details_result = null;

        if(pd!=null || pd.isShowing())
            pd.dismiss();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(String.valueOf(response));

            if (jsonObject.has("result")) {
                staus_res = jsonObject.getString("result");
            }

            if (jsonObject.has("opinion_details")) {
                appointment_details_result = jsonObject.getString("opinion_details");
            }

            if(staus_res != null && !staus_res.isEmpty() && appointment_details_result != null && !appointment_details_result.isEmpty()) {
                if(staus_res.equalsIgnoreCase("failure")) {
                    Toast.makeText(OpinionListActivity.this, "Load Opinion \n"+jsonObject.getString("err_msg"), Toast.LENGTH_SHORT).show();
                }
                else {

                    JSONArray jsonArray1 = jsonObject.getJSONArray("opinion_details");
                    if(jsonArray1.length() > 0 ) {
                        recyclerView.setVisibility(View.VISIBLE);
                        no_data.setVisibility(View.GONE);

                        opinionArraylist.clear();
                        opinionArraylist = new ArrayList<OpinionList>();

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            opinionArraylist.add(new OpinionList(jsonArray1.getJSONObject(i).getInt("pat_id"), jsonArray1.getJSONObject(i).getString("pat_name"),
                                    jsonArray1.getJSONObject(i).getString("pat_age"), jsonArray1.getJSONObject(i).getString("pat_loc"),
                                    jsonArray1.getJSONObject(i).getInt("pat_status"),jsonArray1.getJSONObject(i).getString("pat_doc_name"),
                                    jsonArray1.getJSONObject(i).getInt("pat_doc_id"),
                                    jsonArray1.getJSONObject(i).getString("pat_refered_by"),
                                    LOGIN_MEMBER_ID,jsonArray1.getJSONObject(i).getString("pat_status_time"),
                                    jsonArray1.getJSONObject(i).getString("patient_addrs"),jsonArray1.getJSONObject(i).getString("pat_state"),
                                    jsonArray1.getJSONObject(i).getString("pat_country"),jsonArray1.getJSONObject(i).getString("weight"),
                                    jsonArray1.getJSONObject(i).getString("hyper_cond"),jsonArray1.getJSONObject(i).getString("diabetes_cond"),
                                    jsonArray1.getJSONObject(i).getString("patient_gen"),jsonArray1.getJSONObject(i).getString("patient_desc"),
                                    jsonArray1.getJSONObject(i).getString("pat_query"),jsonArray1.getJSONObject(i).getString("patient_complaint"),
                                    jsonArray1.getJSONObject(i).getString("patient_mob"),jsonArray1.getJSONObject(i).getString("patient_email")));
                        }

                        if(OPINION_FILTER_TYPE.equals("1")) {
                            Gson gson = new Gson();
                            String jsonText = gson.toJson(opinionArraylist);
                            if (sharedPreferences != null) {
                                shareadPreferenceClass.clearOpinionLists();
                                shareadPreferenceClass.setOpinionList(jsonText);
                            }
                            Log.d(AppUtils.TAG +" size: ", String.valueOf(opinionArraylist.size()));
                        }


                        prepareOpinionLists(opinionArraylist);

                    }
                    else {
                        recyclerView.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);

                        if(OPINION_FILTER_TYPE.equals("1")) {
                            Gson gson = new Gson();
                            String jsonText = gson.toJson(opinionArraylist);
                            if (sharedPreferences != null) {
                                shareadPreferenceClass.clearOpinionLists();
                                shareadPreferenceClass.setOpinionList(jsonText);
                            }
                        }

                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void prepareOpinionLists(ArrayList<OpinionList> opinionArraylist) {
        adapter = new OpinionAdapter(this, opinionArraylist);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_appointment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item_appoint_swich_user:
                // Toast.makeText(getApplicationContext(),"Switch Member",Toast.LENGTH_LONG).show();
                if(sharedPreferences != null) {
                    LOGIN_MEMBER_ID = sharedPreferences.getInt(MHConstants.PREF_LOGIN_MEMBER_ID,0);
                    LOGIN_MEMBER_NAME = sharedPreferences.getString(MHConstants.PREF_LOGIN_MEMBER_NEME,"");
                }
                showSwitchMemberDialog();
                return true;
            case android.R.id.home:
                finish();
                return true;
            case R.id.item_appoint_add:
              /*  Intent i2 = new Intent(OpinionListActivity.this, MedicalOpinionActivity.class);
                i2.putExtra("title","Medical Opinion");
                startActivity(i2);*/

                Intent i1 = new Intent(OpinionListActivity.this, DoctorsListActivity.class);
                i1.putExtra("title","View Doctors");
                startActivity(i1);
                return true;
            case R.id.item_appoint_refresh:
                OPINION_FILTER_TYPE = "1";
                if (NetworkUtil.getConnectivityStatusString(OpinionListActivity.this).equalsIgnoreCase("enabled")) {
                    sendOpinionRequestToServer();
                } else {
                    AppUtils.showCustomAlertMessage(OpinionListActivity.this, MHConstants.INTERNET, MHConstants.INTERNET_CHECK, "OK", null, null);
                }
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
        final Dialog dialog = new Dialog(OpinionListActivity.this, R.style.CustomDialog);
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
            RadioButton rb = new RadioButton(OpinionListActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
            rb.setPadding(10, 40, 10, 40);
            rb.setTag(strinIDList.get(i));
            rb.setTextSize(18);
            rg.addView(rb);

            if(LOGIN_MEMBER_ID == strinIDList.get(i) && !OPINION_FILTER_TYPE.equals("1")) {
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
                    member_name_text.setText("Patient Name: "+LOGIN_MEMBER_NAME);
                }

                OPINION_FILTER_TYPE = "2";           // 2- Particular Member
                sendOpinionRequestToServer();
                dialog.dismiss();

            }
        });

        dialog.show();
    }
}
