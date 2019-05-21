package com.fdc.pixelcare.Activities.Appointments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.DataModel.UserAddress;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.Network.CustomVolleyRequestQueue;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppController;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
import com.fdc.pixelcare.Views.CustomEditText;
import com.fdc.pixelcare.Views.CustomTextView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SALMA on 22/12/18.
 */

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener,
        Response.ErrorListener {
    public static final String REQUEST_TAG = "AddAddressActivity";
    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int USER_ID, USER_LOGINTYPE;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION;

    CustomEditText _edt_name, _edt_mobile, _edt_address, _edt_city, _edt_pincode, _edt_state, _edt_country;
    Button save_btn;
    private RequestQueue mQueue;
    ArrayList<UserAddress> addressArraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        addressArraylist = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle b = getIntent().getExtras();
        String title = b.getString("title");
        setTitle(title);

        shareadPreferenceClass = new ShareadPreferenceClass(AddAddressActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(AddAddressActivity.this);

        if(sharedPreferences !=null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE  = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);

            Log.d(AppUtils.TAG , " *********** AddDeliveryAddressActivity **************** ");
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

       // _edt_name = (CustomEditText) findViewById(R.id.add_addess_name);
       // _edt_mobile = (CustomEditText) findViewById(R.id.add_addess_mobile);
        _edt_address = (CustomEditText) findViewById(R.id.add_addess_address);
        _edt_city = (CustomEditText) findViewById(R.id.add_addess_city);
        _edt_pincode = (CustomEditText) findViewById(R.id.add_addess_pincode);
        _edt_state = (CustomEditText) findViewById(R.id.add_addess_state);
        _edt_country = (CustomEditText) findViewById(R.id.add_addess_country);
        save_btn = (Button) findViewById(R.id.add_address_save);
        save_btn.setOnClickListener(this);

      //  _edt_name.setVisibility(View.GONE);
       // _edt_mobile.setVisibility(View.GONE);

      //  _edt_name.setText(USER_NAME);
      //  _edt_mobile.setText(USER_MOBILE);
        _edt_city.setText(USER_LOCATION);

        _edt_pincode.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(s.length() == 6) {
                    _edt_city.setText("");
                    _edt_state.setText("");
                    _edt_country.setText("");
                    new getJsonAddress().execute(s.toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_address_save:
//                Intent i4 = new Intent(AddAddressActivity.this, SelectDeliveryAddressActivity.class);
//                i4.putExtra("title","Select Address");
//                startActivity(i4);
                validateAddressForm();
                break;
        }
    }

    private void validateAddressForm() {
        String address = _edt_address.getText().toString().trim();
        String city = _edt_city.getText().toString().trim();
        String pincode = _edt_pincode.getText().toString().trim();
        String state = _edt_state.getText().toString().trim();
        String country = _edt_country.getText().toString().trim();

        if(address.equals("")) {
            Toast.makeText(AddAddressActivity.this, "Enter Address !!!", Toast.LENGTH_SHORT).show();
        }
        else if(city.equals("")) {
            Toast.makeText(AddAddressActivity.this, "Enter City !!!", Toast.LENGTH_SHORT).show();
        }
        else if(state.equals("")) {
            Toast.makeText(AddAddressActivity.this, "Enter State !!!", Toast.LENGTH_SHORT).show();
        }
        else if(country.equals("")) {
            Toast.makeText(AddAddressActivity.this, "Enter Country !!!", Toast.LENGTH_SHORT).show();
        }
        else {
            submitAddressRequestToServer(address, city, pincode, state,  country);
        }

    }

    private void submitAddressRequestToServer(final String address, final String city, final String pincode, final String state, final String country) {
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        StringRequest postRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_ADD_ADDRESS,
                this,this ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(APIParam.KEY_API, APIClass.API_KEY);
                params.put(APIParam.KEY_ADDRS_ADDRESS, address);
                params.put(APIParam.KEY_ADDRS_CITY, city);
                params.put(APIParam.KEY_ADDRS_STATE, state);
                params.put(APIParam.KEY_ADDRS_COUNTRY, country);
                params.put(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID));

                if(pincode != null) {
                    params.put(APIParam.KEY_ADDRS_PINCODE, pincode);
                }
                return params;
            }
        };

        postRequest.setTag(REQUEST_TAG);
        mQueue.add(postRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        VolleyLog.e(AppUtils.TAG +" Error: ", error.getMessage());
        AppUtils.showCustomAlertMessage(AddAddressActivity.this, "Warning !!!",error.getMessage(), "OK", null, null);

    }

    @Override
    public void onResponse(Object response) {
        Log.d(AppUtils.TAG +" Result: ", response.toString());
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(String.valueOf(response));
            String staus_res = jsonObject.getString("result");
            if(staus_res.equalsIgnoreCase("failure")) {
                Toast.makeText(AddAddressActivity.this, "Add Address \n"+jsonObject.getString("err_msg"), Toast.LENGTH_SHORT).show();
            }
            else {
               // AppUtils.showCustomSuccessMessage(AddAddressActivity.this, "Add Address", "Member added successfully !!!", "OK", null, null);

                JSONArray jsonArray1 = jsonObject.getJSONArray("user_address");
                if(jsonArray1.length() > 0 ) {
                    addressArraylist.clear();
                    addressArraylist = new ArrayList<UserAddress>();

                    for (int i = 0; i < jsonArray1.length(); i++) {
                        addressArraylist.add(new UserAddress(jsonArray1.getJSONObject(i).getInt("address_id"),jsonArray1.getJSONObject(i).getInt("user_id"),
                                jsonArray1.getJSONObject(i).getString("address"),jsonArray1.getJSONObject(i).getString("city"),
                                jsonArray1.getJSONObject(i).getString("pincode"),jsonArray1.getJSONObject(i).getString("state"),
                                jsonArray1.getJSONObject(i).getString("country")));
                    }
                    //Set the values Family Member
                    Gson gson = new Gson();
                    String jsonText = gson.toJson(addressArraylist);
                    if (sharedPreferences != null) {
                        shareadPreferenceClass.clearUserAddressLists();
                        shareadPreferenceClass.setUserAddressList(jsonText);
                    }
                }

                final Dialog dialog = new Dialog(AddAddressActivity.this, R.style.CustomDialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_success);

                CustomTextView text = (CustomTextView) dialog.findViewById(R.id.text_success_dialog);
                text.setText("Address added successfully !!!");

                Button dialogButton = (Button) dialog.findViewById(R.id.text_success_submit);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });

                dialog.show();
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

    class getJsonAddress extends AsyncTask<String,String,String> {
        String country_shortname;
        @Override
        protected String doInBackground(String... key) {
            String stateText = key[0];
            stateText = stateText.trim();
            country_shortname = stateText.trim();
            Log.d(AppUtils.TAG, "State selected1: " + stateText);
            stateText = stateText.replace(" ", "+");

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.GET, "http://postalpincode.in/api/pincode/"+stateText,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(AppUtils.TAG, response.toString());
                            if (response != null) {
                                JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;
                                ArrayList<String> stateArray = new ArrayList<String>();
                                try {
                                    jsonObject1 = new JSONObject(response);

                                    // Getting JSON Array node
                                    JSONArray contacts = jsonObject1.getJSONArray("PostOffice");
                                    // looping through All Contacts
                                    for (int i = 0; i < contacts.length(); i++) {
                                        JSONObject c = contacts.getJSONObject(i);

                                        String city = c.getString("Name");
                                        String State = c.getString("State");
                                        String Country = c.getString("Country");
                                        _edt_city.setText(city);
                                        _edt_state.setText(State);
                                        _edt_country.setText(Country);
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
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(AddAddressActivity.this).
                    getRequestQueue();
            AppController.getInstance(AddAddressActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }
    }
}
