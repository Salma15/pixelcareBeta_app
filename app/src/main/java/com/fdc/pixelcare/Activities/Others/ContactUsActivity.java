package com.fdc.pixelcare.Activities.Others;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.Network.NetworkUtil;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppController;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
import com.fdc.pixelcare.Views.CustomEditText;
import com.fdc.pixelcare.Views.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SALMA on 31-12-2018.
 */

public class ContactUsActivity extends AppCompatActivity {
    public static final String REQUEST_TAG = "ContactUsActivity";
    private RequestQueue mQueue;

    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int USER_ID, USER_LOGINTYPE;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION;

    CustomEditText _txt_feedback;
    Button _btn_sendfeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            setTitle(title);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(ContactUsActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(ContactUsActivity.this);

        if(sharedPreferences !=null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE  = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);

            Log.d(AppUtils.TAG , " *********** ContactUsActivity **************** ");
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

        _txt_feedback = (CustomEditText) findViewById(R.id.contactus_text);
        _btn_sendfeedback = (Button) findViewById(R.id.contactus_submit);

        _btn_sendfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm2 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(_btn_sendfeedback.getWindowToken(), 0);
                collectUpdateDetails();
            }
        });

        _txt_feedback.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    _txt_feedback.setHint("Write your query here....");
                else
                    _txt_feedback.setHint("Write your query here....");
            }
        });
    }

    private void collectUpdateDetails() {
        if((_txt_feedback.getText().toString().equals(""))) {
            Toast.makeText(ContactUsActivity.this,"Write your query !!!", Toast.LENGTH_SHORT).show();
        }
        else {
            if (NetworkUtil.getConnectivityStatusString(ContactUsActivity.this).equalsIgnoreCase("enabled")) {
                updateFeedbackToServer(_txt_feedback.getText().toString().trim());
            } else {
                AppUtils.showCustomAlertMessage(ContactUsActivity.this, MHConstants.INTERNET, MHConstants.INTERNET_CHECK, "OK", null, null);
            }
        }
    }

    private void updateFeedbackToServer(final String feedback_text) {
        final ProgressDialog progressDialog = new ProgressDialog(ContactUsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending feedback...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_CONTACT_US,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Log.d(Utils.TAG, response.toString());

                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                String update_result = jsonObject.getString("feedback_res");
                                Log.d(AppUtils.TAG, staus_res.toString());
//                                AppUtils.showCustomSuccessMessage(ContactUsActivity.this, "Done!",update_result.toString(), "OK", null, null);
//                                _txt_feedback.setText("");

                                final Dialog dialog = new Dialog(ContactUsActivity.this, R.style.CustomDialog);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.custom_success);

                                CustomTextView text = (CustomTextView) dialog.findViewById(R.id.text_success_dialog);
                                text.setText(update_result.toString());

                                Button dialogButton = (Button) dialog.findViewById(R.id.text_success_submit);
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });

                                dialog.show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        AppUtils.showCustomAlertMessage(ContactUsActivity.this, MHConstants.INTERNET, MHConstants.INTERNET_CHECK, "OK", null, null);
                        Log.d(AppUtils.TAG+"ERR",error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIParam.KEY_API, APIClass.API_KEY);
                map.put(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID));
                map.put(APIParam.KEY_CONTACTUS_TEXT, feedback_text);

                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(ContactUsActivity.this).
                getRequestQueue();
        AppController.getInstance(ContactUsActivity.this).addToRequestQueue(stringRequest);
    }
}
