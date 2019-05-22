package com.fdc.pixelcare.Activities.Opinions;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.Adapters.Opinions.ChatHistoryAdapter;
import com.fdc.pixelcare.DataModel.ChatHistory;
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
import com.fdc.pixelcare.Views.CustomEditText;
import com.fdc.pixelcare.Views.CustomTextView;
import com.fdc.pixelcare.Views.CustomTextViewSemiBold;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OpinionDetailsActivity extends AppCompatActivity  implements Response.Listener, Response.ErrorListener {

    public static final String REQUEST_TAG = "OpinionDetailsActivity";
    private RequestQueue mQueue;

    int USER_ID,USER_LOGINTYPE;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION, FAMILY_MEMBER_LIST, OPINIONS_LIST;
    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    ArrayList<FamilyMember> memberArraylist;
    int LOGIN_MEMBER_ID;
    String LOGIN_MEMBER_NAME;
    ProgressDialog pd;

    int PATIENT_ID;
    String PATIENT_NAME, PATIENT_DOC_NAME, PATIENT_GENDER, PATIENT_AGE, PATIENT_WEIGHT, PATIENT_HYPERTESNSION, PATIENT_DIABETES,
            PATIENT_ADDRESS, PATIENT_CITY, PATIENT_STATE, PATIENT_COUNTRY, PATIENT_MEDICAL_COMPALINT, PATIENT_DESCRIPTION,
            PATIENT_QUERY_DOC, PATIENT_MOBILE, PATIENT_EMAIL, PATIENT_STATUS;

    CustomTextView _edt_name, _edt_reg_date,  _edt_age,  _edt_gender,  _edt_hypertenstion,  _edt_diabetes,  _edt_address,  _edt_query,  _edt_complaint,  _edt_description, _edt_doc_name;
    CustomTextViewSemiBold _edt_doc_label;
    RecyclerView chat_history_recyclerview;
    ArrayList<ChatHistory> chathistoryArraylist;
    private ChatHistoryAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_details);
        chathistoryArraylist = new ArrayList<>();
        memberArraylist = new ArrayList<>();
        LOGIN_MEMBER_ID = 0;
        LOGIN_MEMBER_NAME = "";


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);
            PATIENT_ID = bundle.getInt("PATIENT_ID");

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

            Log.d(AppUtils.TAG, " *********** OpinionDetailsActivity **************** ");
            Log.d(AppUtils.TAG + " UserID: ", String.valueOf(USER_ID));
            Log.d(AppUtils.TAG + " name: ", USER_NAME);
            Log.d(AppUtils.TAG + " email: ", USER_EMAIL);
            Log.d(AppUtils.TAG + " MOBILE: ", USER_MOBILE);
            Log.d(AppUtils.TAG + " Location: ", USER_LOCATION);
            Log.d(AppUtils.TAG + " LOGIN_MEMBER_ID: ", String.valueOf(LOGIN_MEMBER_ID));
            Log.d(AppUtils.TAG + " LOGIN_MEMB_NAME: ", LOGIN_MEMBER_NAME);
            Log.d(AppUtils.TAG + " logintype: ", String.valueOf(USER_LOGINTYPE));
            Log.d(AppUtils.TAG + " PATIENT_ID: ", String.valueOf(PATIENT_ID));
        }

        initializeViews();
    }

    private void initializeViews() {

        _edt_name = (CustomTextView) findViewById(R.id.pat_name);
        _edt_reg_date = (CustomTextView) findViewById(R.id.pat_regdate);
        _edt_age = (CustomTextView) findViewById(R.id.pat_age);
        _edt_gender = (CustomTextView) findViewById(R.id.pat_gender);
        _edt_hypertenstion = (CustomTextView) findViewById(R.id.pat_hypertension);
        _edt_diabetes = (CustomTextView) findViewById(R.id.pat_diabetes);
        _edt_address = (CustomTextView) findViewById(R.id.pat_address);
        _edt_query = (CustomTextView) findViewById(R.id.pat_medical_query);
        _edt_complaint = (CustomTextView) findViewById(R.id.pat_medical_complaint);
        _edt_description = (CustomTextView) findViewById(R.id.pat_description);
        _edt_doc_name = (CustomTextView) findViewById(R.id.pat_doc_name);
        _edt_doc_label = (CustomTextViewSemiBold) findViewById(R.id.pat_doc_label);

        chat_history_recyclerview = (RecyclerView) findViewById(R.id.chatresponse_recyclerview);
        mAdapter = new ChatHistoryAdapter(chathistoryArraylist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        chat_history_recyclerview.setLayoutManager(mLayoutManager);
        chat_history_recyclerview.setItemAnimator(new DefaultItemAnimator());
        chat_history_recyclerview.setAdapter(mAdapter);

        if (NetworkUtil.getConnectivityStatusString(OpinionDetailsActivity.this).equalsIgnoreCase("enabled")) {
            sendOpinionDetailsRequestToServer();
        } else {
            AppUtils.showCustomAlertMessage(OpinionDetailsActivity.this, MHConstants.INTERNET, MHConstants.INTERNET_CHECK, "OK", null, null);
        }
    }

    private void sendOpinionDetailsRequestToServer() {
        pd = new ProgressDialog(OpinionDetailsActivity.this, R.style.CustomDialog);
        pd.setCancelable(false);
        pd.setMessage("Loading Opinion details...");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();

        // Instantiate the RequestQueue.
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        StringRequest postRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_OPINION_DETAILS,
                this,this ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(APIParam.KEY_API, APIClass.API_KEY);
                params.put(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID));
                params.put(APIParam.KEY_MEMBER_ID, String.valueOf(LOGIN_MEMBER_ID));
                params.put(APIParam.KEY_PATIENT_ID, String.valueOf(PATIENT_ID));
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
        AppUtils.showCustomAlertMessage(OpinionDetailsActivity.this, "Warning !!!",error.getMessage(), "OK", null, null);
    }

    @Override
    public void onResponse(Object response) {
        Log.d(AppUtils.TAG +" Result: ", response.toString());
        String staus_res = null, appointment_details_result = null, chat_response = null;

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

            if (jsonObject.has("opinion_chat")) {
                chat_response = jsonObject.getString("opinion_chat");
            }

            if(staus_res != null && !staus_res.isEmpty() && appointment_details_result != null && !appointment_details_result.isEmpty()) {
                if(staus_res.equalsIgnoreCase("failure")) {
                    Toast.makeText(OpinionDetailsActivity.this, "Load Opinion Details \n"+jsonObject.getString("err_msg"), Toast.LENGTH_SHORT).show();
                }
                else {

                    JSONArray jsonArray1 = jsonObject.getJSONArray("opinion_details");
                    if(jsonArray1.length() > 0 ) {

                        _edt_name.setText(jsonArray1.getJSONObject(0).getString("pat_name")+" (#"+String.valueOf(jsonArray1.getJSONObject(0).getInt("pat_id"))+")");

                        if(jsonArray1.getJSONObject(0).getString("pat_status_time").equals("")) { }
                        else {
                            String[] status_date_arrray = jsonArray1.getJSONObject(0).getString("pat_status_time").split(" ");
                            status_date_arrray[0] = status_date_arrray[0].replace("-", "/");
                            DateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");
                            DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
                            String inputDateStr= status_date_arrray[0];
                            Date date = null;
                            try {
                                date = inputFormat.parse(inputDateStr);
                                String outputDateStr = outputFormat.format(date);
                                _edt_reg_date.setText(outputDateStr);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        _edt_age.setText(jsonArray1.getJSONObject(0).getString("pat_age"));

                        if(jsonArray1.getJSONObject(0).getString("patient_gen").equals("1")) {
                            _edt_gender.setText("Male");
                        }
                        else if(jsonArray1.getJSONObject(0).getString("patient_gen").equals("2")) {
                            _edt_gender.setText("Female");
                        }
                        else {
                            _edt_gender.setText("");
                        }

                        if(jsonArray1.getJSONObject(0).getString("hyper_cond").equals("1")) {
                            _edt_hypertenstion.setText("Yes");
                        }
                        else if(jsonArray1.getJSONObject(0).getString("hyper_cond").equals("2")) {
                            _edt_hypertenstion.setText("No");
                        }
                        else {
                            _edt_hypertenstion.setText("#N/A");
                        }

                        if(jsonArray1.getJSONObject(0).getString("diabetes_cond").equals("1")) {
                            _edt_diabetes.setText("Yes");
                        }
                        else if(jsonArray1.getJSONObject(0).getString("diabetes_cond").equals("2")) {
                            _edt_diabetes.setText("No");
                        }
                        else {
                            _edt_diabetes.setText("#N/A");
                        }

                        _edt_address.setText(jsonArray1.getJSONObject(0).getString("pat_loc")+"\n"+jsonArray1.getJSONObject(0).getString("patient_addrs")+"\n"+jsonArray1.getJSONObject(0).getString("pat_state")+"\n"+jsonArray1.getJSONObject(0).getString("pat_country"));
                        _edt_query.setText(jsonArray1.getJSONObject(0).getString("pat_query"));
                        _edt_complaint.setText(jsonArray1.getJSONObject(0).getString("patient_complaint"));
                        _edt_description.setText(jsonArray1.getJSONObject(0).getString("patient_desc"));

                    }

                    JSONArray jsonArray2 = jsonObject.getJSONArray("opinion_chat");
                    if (jsonArray2.length() > 0) {
                        _edt_doc_label.setText("Given By: ");
                        _edt_doc_name.setText(jsonArray1.getJSONObject(0).getString("pat_doc_name"));
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            chathistoryArraylist.add(new ChatHistory(jsonArray2.getJSONObject(i).getString("chat_note"),jsonArray2.getJSONObject(i).getString("TImestamp")));
                        }

                        if(chathistoryArraylist.size() > 0) {
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }

            } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
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

}
