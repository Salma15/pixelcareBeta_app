package com.fdc.pixelcare.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.fdc.pixelcare.DataModel.FamilyMember;
import com.fdc.pixelcare.DataModel.User;
import com.fdc.pixelcare.DataModel.UserAddress;
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
import com.hbb20.CountryCodePicker;
import com.omjoonkim.skeletonloadingview.SkeletonLoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SALMA on 20-12-2018.
 */
public class FacebookVerifyOTPActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener, Response.ErrorListener {

    public static final String REQUEST_TAG = "FacebookVerifyOTPActivity";

    String USER_NAME, USER_MOBILE_NUM, USER_EMAIL, USER_OTP, USER_PROFILEPIC, USER_VERIFY_STATUS = "0";
    CustomTextView mobile_txt, name_txt;
    ImageView next_btn;
    CustomEditText _edt_otp;
    CustomEditText _edi_new_otp;

    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int USER_LOGIN_TYPE;
    double selected_latitute, selected_longitude;
    String USER_LOCATION, LOGIN_MEMBER_NAME;
    int USER_LOGINTYPE, LOGIN_MEMBER_ID;  // Login Type 1-Patients, 0-Guest

    SkeletonLoadingView loading_view;
    ImageView loading_done;
    private RequestQueue mQueue;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public Pattern patterns = Pattern.compile("(|^)\\d{4}");
    ArrayList<User> userArraylist;
    ArrayList<FamilyMember> memberArraylist;
    CustomTextViewSemiBold resend_otp_btn;

    ImageView profilePic_image, detail_next;
    CustomEditText detail_email, detail_mobile;
    CountryCodePicker ccp;
    int COUNTRY_CODE = 91;
    LinearLayout verify_details_layout, verify_otp_layout;

    ArrayList<UserAddress> addressArraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_verifyotp);

        Bundle b = new Bundle();
        if( b != null) {
            b = getIntent().getExtras();
            USER_NAME = b.getString("USER_NAME", "");
            USER_MOBILE_NUM = b.getString("USER_MOBILE", "");
            USER_EMAIL = b.getString("USER_EMAIL", "");
            USER_PROFILEPIC = b.getString("USER_PROFILEPIC", "");
            USER_VERIFY_STATUS = b.getString("USER_VERIFY_STATUS", "0");

            Log.d(AppUtils.TAG, " ************** FacebookVerifyOTPActivity ******************** ");
            Log.d(AppUtils.TAG, " USER_NAME: "+ USER_NAME);
            Log.d(AppUtils.TAG, " USER_MOBILE_NUM: "+ USER_MOBILE_NUM);
            Log.d(AppUtils.TAG, " USER_EMAIL: "+ USER_EMAIL);
            Log.d(AppUtils.TAG, " USER_PROFILEPIC: "+ USER_PROFILEPIC);
        }

        initializeViews();
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

    private void initializeViews() {

        name_txt = (CustomTextView) findViewById(R.id.fb_verify_otp_name);
        profilePic_image = (ImageView) findViewById(R.id.fb_verify_details_image);
        detail_email = (CustomEditText) findViewById(R.id.fb_verify_details_email);
        detail_mobile = (CustomEditText) findViewById(R.id.fb_verify_details_mobile);
        detail_next = (ImageView) findViewById(R.id.fb_verify_details_next);
        detail_next.setOnClickListener(this);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        verify_details_layout = (LinearLayout) findViewById(R.id.fb_verify_details_layout);
        verify_details_layout.setVisibility(View.VISIBLE);
        verify_otp_layout = (LinearLayout) findViewById(R.id.fb_verify_otp_layout);
        verify_otp_layout.setVisibility(View.GONE);

        if(USER_VERIFY_STATUS.equals("0")) {
            verify_details_layout.setVisibility(View.VISIBLE);
            verify_otp_layout.setVisibility(View.GONE);
        }
        else  if(USER_VERIFY_STATUS.equals("1")) {
            verify_details_layout.setVisibility(View.GONE);
            verify_otp_layout.setVisibility(View.VISIBLE);
        }
        else {
            verify_details_layout.setVisibility(View.VISIBLE);
            verify_otp_layout.setVisibility(View.GONE);
        }

        if(!USER_PROFILEPIC.equals("")) {
            Glide.with(FacebookVerifyOTPActivity.this).load(USER_PROFILEPIC).skipMemoryCache(true).into(profilePic_image);
        }

        name_txt.setText("Hi, " + USER_NAME +"!");
        detail_email.setText(USER_EMAIL);
        detail_mobile.setText(USER_MOBILE_NUM);

        Log.d(AppUtils.TAG, " DefCountryCode: "+ccp.getDefaultCountryCode());
        Log.d(AppUtils.TAG, " DefCountryName: "+ccp.getDefaultCountryName());
        COUNTRY_CODE = Integer.parseInt(ccp.getDefaultCountryCode());
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Log.d(AppUtils.TAG, " CountryCode: "+ccp.getSelectedCountryCode());
                Log.d(AppUtils.TAG, " CountryName: "+ccp.getSelectedCountryName());
                COUNTRY_CODE = Integer.parseInt(ccp.getSelectedCountryCode());
                Toast.makeText(FacebookVerifyOTPActivity.this, " Code:"+ccp.getSelectedCountryCode(), Toast.LENGTH_SHORT).show();
            }
        });


        // ****************** Validate OTP Section *******************
        userArraylist = new ArrayList<>();
        memberArraylist = new ArrayList<>();
        addressArraylist = new ArrayList<>();
        LOGIN_MEMBER_ID = 0;
        LOGIN_MEMBER_NAME = "";

        shareadPreferenceClass = new ShareadPreferenceClass(FacebookVerifyOTPActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(FacebookVerifyOTPActivity.this);

        USER_LOCATION = "#N/A";

        mobile_txt = (CustomTextView) findViewById(R.id.fb_verify_otp_mobile);
        next_btn = (ImageView)  findViewById(R.id.fb_verify_otp_next);
        next_btn.setOnClickListener(this);
        _edt_otp = (CustomEditText)  findViewById(R.id.fb_verify_otp_text);
        _edt_otp.setEnabled(false);
        _edi_new_otp = (CustomEditText) findViewById(R.id.fb_verify_new_otp);

        loading_view = (SkeletonLoadingView) findViewById(R.id.fb_otpverify_skeletonView);
        loading_done = (ImageView)  findViewById(R.id.fb_otpverify_loadingdone);
        loading_view.setVisibility(View.VISIBLE);
        loading_done.setVisibility(View.GONE);

        resend_otp_btn = (CustomTextViewSemiBold) findViewById(R.id.fb_verify_resend_otp);
        resend_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTPRequest(USER_EMAIL, USER_MOBILE_NUM);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb_verify_details_next:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(detail_next.getWindowToken(), 0);

                validateUserDetails();

                break;
            case R.id.fb_verify_otp_next:

                InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(next_btn.getWindowToken(), 0);

                if (NetworkUtil.getConnectivityStatusString(FacebookVerifyOTPActivity.this).equalsIgnoreCase("enabled")) {
                    validateOTPsent();
                } else {
                    AppUtils.showCustomAlertMessage(FacebookVerifyOTPActivity.this, MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
                }

                break;
        }
    }

    private void validateOTPsent() {
        String otp_number = _edi_new_otp.getText().toString().trim();

        if(otp_number.equals("")) {
            Toast.makeText(FacebookVerifyOTPActivity.this, "Enter OTP !!!", Toast.LENGTH_SHORT).show();
        }
        else {
            USER_OTP = otp_number;
            continueOTPConfirm();
            /* USER_LOGINTYPE = 1;   // Type-1 for Patients
             if (sharedPreferences != null) {
                 shareadPreferenceClass.preLogin(1,USER_NAME,"user@gmail.com","verified",USER_LOCATION,USER_LOGINTYPE);

             }

             Intent i = new Intent(VerifyOTPActivity.this, DashboardActivity.class);
             startActivity(i);
             finish();*/
        }
    }

    private void continueOTPConfirm() {

        // Instantiate the RequestQueue.
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        StringRequest postRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_OTP,
                this,this ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(APIParam.KEY_API, APIClass.API_KEY);
                params.put(APIParam.KEY_USERNAME, USER_NAME);
                params.put(APIParam.KEY_MOBILE, USER_MOBILE_NUM);
                params.put(APIParam.KEY_OTP, USER_OTP);
                return params;
            }
        };

        postRequest.setTag(REQUEST_TAG);
        mQueue.add(postRequest);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }


    private void validateUserDetails() {
        Log.d(AppUtils.TAG, " COUNTRY_CODE: "+COUNTRY_CODE);

        if(detail_email.getText().toString().equals("") && COUNTRY_CODE != 91) {        // India Country Code = 91
            Toast.makeText(FacebookVerifyOTPActivity.this, "Enter Email ID !!!", Toast.LENGTH_SHORT).show();
        }
        else  if(detail_mobile.getText().toString().equals("")) {
            Toast.makeText(FacebookVerifyOTPActivity.this, "Enter valid 10 digits Mobile No. !!!", Toast.LENGTH_SHORT).show();
        }
        else {
        //    Toast.makeText(FacebookVerifyOTPActivity.this, "Success ", Toast.LENGTH_SHORT).show();
            verify_details_layout.setVisibility(View.GONE);
            verify_otp_layout.setVisibility(View.VISIBLE);

            mobile_txt.setText(detail_mobile.getText().toString());
            //calling the method to display the heroes
            USER_MOBILE_NUM = detail_mobile.getText().toString();
            USER_EMAIL = detail_email.getText().toString();
            sendOTPRequest(detail_email.getText().toString(),detail_mobile.getText().toString() );
        }
    }

    private void sendOTPRequest(final String email_id, final String mobile_num) {

        // Instantiate the RequestQueue.
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        StringRequest postRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_LOGIN,
                this,this ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(APIParam.KEY_API, APIClass.API_KEY);
                params.put(APIParam.KEY_USERNAME, USER_NAME);
                params.put(APIParam.KEY_MOBILE, mobile_num);

                if(email_id != null) {
                    params.put(APIParam.KEY_EMAIL, email_id);
                }

                return params;
            }
        };

        postRequest.setTag(REQUEST_TAG);
        mQueue.add(postRequest);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //  mTextView.setText(error.getMessage());
        VolleyLog.e(AppUtils.TAG +" Error: ", error.getMessage());
        // Toast.makeText(VerifyOTPActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
        AppUtils.showCustomAlertMessage(FacebookVerifyOTPActivity.this, "Verify Account",error.getMessage(), "OK", null, null);

    }

    @Override
    public void onResponse(Object response) {
        // Toast.makeText(VerifyOTPActivity.this, "Response: "+response.toString(), Toast.LENGTH_SHORT).show();
        Log.d(AppUtils.TAG +" Result: ", response.toString());
        try {
            String staus_res = null, otp_result = null;

            JSONObject jsonObject = new JSONObject(String.valueOf(response));

            if (jsonObject.has("result")) {
                staus_res = jsonObject.getString("result");
            }

            if (jsonObject.has("result_otp")) {
                otp_result = jsonObject.getString("result_otp");

            }

            if(staus_res != null && !staus_res.isEmpty()) {
                if(staus_res.equalsIgnoreCase("failure")) {
                    Toast.makeText(FacebookVerifyOTPActivity.this, "Verification Status \n"+jsonObject.getString("err_msg"), Toast.LENGTH_SHORT).show();
                }
               /* else {
                    // Login OTP create success   - Hide if not necessary
                    String otp_number = jsonObject.getString("otp_num");
                    _edi_new_otp.setText(otp_number);
                    _edi_new_otp.setSelection(_edi_new_otp.getText().length());
                    loading_view.setVisibility(View.GONE);
                    loading_done.setVisibility(View.VISIBLE);
                }*/
            }

            if(otp_result != null && !otp_result.isEmpty()) {
                if(otp_result.equalsIgnoreCase("failure")) {
                    Toast.makeText(FacebookVerifyOTPActivity.this, "Verification Status \n"+jsonObject.getString("err_msg"), Toast.LENGTH_SHORT).show();
                }
                else {
                    String user_name = "", user_email = "", user_mobile = "", user_city = "";
                    int user_id = 0;
                    int memember_id = 0, memberuserId = 0, gender=0;
                    String member_name = "", member_relation = "", member_dob = "" , member_age = "";

                    String otp_status = jsonObject.getString("otp_status");
                    if(otp_status.equalsIgnoreCase("verified")) {
                        userArraylist = new ArrayList<>();
                        memberArraylist = new ArrayList<>();
                        LOGIN_MEMBER_ID = 0;
                        LOGIN_MEMBER_NAME = "";
                        JSONArray jsonArray = jsonObject.getJSONArray("user_details");
                        JSONArray jsonArray1 = jsonObject.getJSONArray("family_details");
                        JSONArray jsonArray2 = jsonObject.getJSONArray("user_address");
                        if(jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                userArraylist.add(new User(jsonArray.getJSONObject(i).getInt("login_id"),jsonArray.getJSONObject(i).getString("sub_name"),
                                        jsonArray.getJSONObject(i).getString("sub_contact"),jsonArray.getJSONObject(i).getString("sub_email"),
                                        jsonArray.getJSONObject(i).getString("sub_city")));

                                user_id = jsonArray.getJSONObject(i).getInt("login_id");
                                user_name = jsonArray.getJSONObject(i).getString("sub_name");
                                user_email = jsonArray.getJSONObject(i).getString("sub_email");
                                user_mobile = jsonArray.getJSONObject(i).getString("sub_contact");
                                user_city = jsonArray.getJSONObject(i).getString("sub_city");

                            }
                            USER_LOGINTYPE = 1;   // Type-1 for Patients
                            if (sharedPreferences != null) {
                                shareadPreferenceClass.preLogin(user_id,user_name,user_email,user_mobile,user_city,USER_LOGINTYPE);

                            }
                        }

                        if(jsonArray1.length() > 0 ) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                memberArraylist.add(new FamilyMember(jsonArray1.getJSONObject(i).getInt("member_id"),jsonArray1.getJSONObject(i).getInt("user_id"),
                                        jsonArray1.getJSONObject(i).getString("gender"),jsonArray1.getJSONObject(i).getString("member_name"),
                                        jsonArray1.getJSONObject(i).getString("relationship"),jsonArray1.getJSONObject(i).getString("dob"),
                                        jsonArray1.getJSONObject(i).getString("age"),jsonArray1.getJSONObject(i).getString("member_photo")));

                                memember_id = jsonArray1.getJSONObject(i).getInt("member_id");
                                memberuserId = jsonArray1.getJSONObject(i).getInt("user_id");
                                gender = jsonArray1.getJSONObject(i).getInt("gender");
                                member_name =  jsonArray1.getJSONObject(i).getString("member_name");
                                member_relation = jsonArray1.getJSONObject(i).getString("relationship");
                                member_dob = jsonArray1.getJSONObject(i).getString("dob");
                                member_age = jsonArray1.getJSONObject(i).getString("age");
                            }

                            LOGIN_MEMBER_ID = memberArraylist.get(0).getMemberid();
                            LOGIN_MEMBER_NAME = memberArraylist.get(0).getMemberName();
                            //Set the values Family Member
                            Gson gson = new Gson();
                            String jsonText = gson.toJson(memberArraylist);
                            if (sharedPreferences != null) {
                                shareadPreferenceClass.clearFamilyMemberLists();
                                shareadPreferenceClass.setFamilyMemberList(jsonText);
                            }

                            if (sharedPreferences != null) {
                                shareadPreferenceClass.preFamilyMembers(memember_id,memberuserId,gender,member_name,member_relation,member_dob,member_age);
                                shareadPreferenceClass.clearLoginMemberID();
                                shareadPreferenceClass.setLoginMemberID(LOGIN_MEMBER_ID);
                                shareadPreferenceClass.clearLoginMemberName();
                                shareadPreferenceClass.setLoginMemberName(LOGIN_MEMBER_NAME);
                            }
                        }

                        if(jsonArray2.length() > 0 ) {
                            addressArraylist.clear();
                            addressArraylist = new ArrayList<UserAddress>();

                            for (int i = 0; i < jsonArray2.length(); i++) {
                                addressArraylist.add(new UserAddress(jsonArray2.getJSONObject(i).getInt("address_id"),jsonArray2.getJSONObject(i).getInt("user_id"),
                                        jsonArray2.getJSONObject(i).getString("address"),jsonArray2.getJSONObject(i).getString("city"),
                                        jsonArray2.getJSONObject(i).getString("pincode"),jsonArray2.getJSONObject(i).getString("state"),
                                        jsonArray2.getJSONObject(i).getString("country")));
                            }
                            //Set the values Family Member
                            Gson gson = new Gson();
                            String jsonText = gson.toJson(addressArraylist);
                            if (sharedPreferences != null) {
                                shareadPreferenceClass.clearUserAddressLists();
                                shareadPreferenceClass.setUserAddressList(jsonText);
                            }
                        }


                        Intent i = new Intent(FacebookVerifyOTPActivity.this, DashboardActivity.class);
                        startActivity(i);
                        finish();

                    }
                    else  if(otp_result.equalsIgnoreCase("failure")) {
                        loading_view.setVisibility(View.GONE);
                        loading_done.setVisibility(View.VISIBLE);
                        Toast.makeText(FacebookVerifyOTPActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        //  _edi_new_otp.setText(otp_number);
                        //   _edi_new_otp.setSelection(_edi_new_otp.getText().length());
                        loading_view.setVisibility(View.GONE);
                        loading_done.setVisibility(View.VISIBLE);
                        Toast.makeText(FacebookVerifyOTPActivity.this, "Account verification status is pending. ", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        };

    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                Log.d(AppUtils.TAG, " ReceipeOTP: "+ message);
                if(message!=null)
                {
                    Matcher m = patterns.matcher(message);
                    if(m.find()) {
                        _edi_new_otp.setText(m.group(0));
                        _edi_new_otp.setSelection(_edi_new_otp.getText().length());
                    }
                    else
                    {
                        //something went wrong
                    }
                    loading_view.setVisibility(View.GONE);
                    loading_done.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}
