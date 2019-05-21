package com.fdc.pixelcare.Activities.Home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SALMA on 21-12-2018.
 */
public class LoadDoctorsFromUrlTask  extends AsyncTask<Void, Void, String> {

    private Activity activity;
    private String url;
    private ProgressDialog dialog;
    private final static String TAG = LoadDoctorsFromUrlTask.class.getSimpleName();
    String DOCTOR_FILTER_TYPE, DOCTOR_CITY;
    int USER_ID, LOGIN_MEMBER_ID, PAGINATION_NEXT;

    int SELECTED_CITY_ID = 0, SELECTED_SPEC_ID = 0, SELECTED_FILTER_TYPE = 0, HOSPITAL_ID;
    double SELECTED_GEO_LATITUDE = 0.00, SELECTED_GEO_LONGITUDE = 0.00;

    public LoadDoctorsFromUrlTask(HospitalDoctorsActivity doclListsActivity, String url, int user_id, int login_member_id,
                                    int selected_city_id, int selected_filter_type, double selected_geo_latitude,
                                    double selected_geo_longitude, int selected_spec_id, int pagination_next, int hospital_id) {
        super();
        this.activity = doclListsActivity;
        this.url = url;
        this.USER_ID = user_id;
        this.LOGIN_MEMBER_ID = login_member_id;
        this.SELECTED_CITY_ID = selected_city_id;
        this.SELECTED_FILTER_TYPE = selected_filter_type;
        this.SELECTED_GEO_LATITUDE = selected_geo_latitude;
        this.SELECTED_GEO_LONGITUDE = selected_geo_longitude;
        this.SELECTED_SPEC_ID = selected_spec_id;
        this.PAGINATION_NEXT = pagination_next;
        this.HOSPITAL_ID = hospital_id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Create a progress dialog
        dialog = new ProgressDialog(activity, R.style.CustomDialog);
        // dialog.setTitle("Doctors Lists");
        //  dialog.setMessage("Loading doctors...");
        //   dialog.setIndeterminate(false);
        //   dialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {

        // call load JSON from url method
        return loadJSON(this.url).toString();
    }

    @Override
    protected void onPostExecute(String result) {
        ((HospitalDoctorsActivity) activity).parseJsonResponse(result);

        if(dialog.isShowing() && dialog != null) {
            dialog.dismiss();
        }
        Log.i(TAG, result);
    }

    public JSONObject loadJSON(String url) {
        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();

        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);

        return json;
    }

    private class JSONParser {

        private InputStream is = null;
        private JSONObject jObj = null;
        private String json = "";

        // constructor
        public JSONParser() {

        }

        public JSONObject getJSONFromUrl(String url) {
            Log.i(AppUtils.TAG, " Page Val" + PAGINATION_NEXT);

            Log.i(AppUtils.TAG, " url " + url);
            Log.i(AppUtils.TAG, " SELECTED_CITY_ID " + SELECTED_CITY_ID);
            Log.i(AppUtils.TAG, " SELECTED_FILTER_TYPE " + SELECTED_FILTER_TYPE);
            Log.i(AppUtils.TAG, " SELECTED_SPEC_ID " + SELECTED_SPEC_ID);
            Log.i(AppUtils.TAG, " SELECTED_GEO_LATITUDE " + SELECTED_GEO_LATITUDE);
            Log.i(AppUtils.TAG, " SELECTED_GEO_LONGITUDE " + SELECTED_GEO_LONGITUDE);
            Log.i(AppUtils.TAG, " HOSPITAL_ID " + HOSPITAL_ID);

            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair(APIParam.KEY_API, APIClass.API_KEY));
                nameValuePairs.add(new BasicNameValuePair(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID)));
                nameValuePairs.add(new BasicNameValuePair(APIParam.KEY_MEMBER_ID, String.valueOf(LOGIN_MEMBER_ID)));
                nameValuePairs.add(new BasicNameValuePair(APIParam.KEY_HOME_CITY_ID, String.valueOf(SELECTED_CITY_ID)));
                nameValuePairs.add(new BasicNameValuePair(APIParam.KEY_HOME_FILTER_TYPE, String.valueOf(SELECTED_FILTER_TYPE)));  // 1- GPS, 2 - CITY, 3-MAP FILTER CITY, 4-ONLY SPECIALTY, 5- BOTH MAP CITY + SPECIALTY
                nameValuePairs.add(new BasicNameValuePair(APIParam.KEY_HOME_LATITUDE, String.valueOf(SELECTED_GEO_LATITUDE)));
                nameValuePairs.add(new BasicNameValuePair(APIParam.KEY_HOME_LONGITUDE, String.valueOf(SELECTED_GEO_LONGITUDE)));
                nameValuePairs.add(new BasicNameValuePair(APIParam.KEY_HOME_SPEC_ID, String.valueOf(SELECTED_SPEC_ID)));
                nameValuePairs.add(new BasicNameValuePair(APIParam.KEY_PAGINATION, String.valueOf(PAGINATION_NEXT)));
                nameValuePairs.add(new BasicNameValuePair(APIParam.KEY_HOSPITAL_ID, String.valueOf(HOSPITAL_ID)));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),
                        8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            // try parse the string to a JSON object
            try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            // return JSON String
            return jObj;
        }
    }
}
