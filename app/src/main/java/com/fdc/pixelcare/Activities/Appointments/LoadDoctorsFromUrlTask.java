package com.fdc.pixelcare.Activities.Appointments;

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
 * Created by SALMA on 22/12/18.
 */

public class LoadDoctorsFromUrlTask  extends AsyncTask<Void, Void, String> {

    private Activity activity;
    private String url;
    private ProgressDialog dialog;
    private final static String TAG = LoadDoctorsFromUrlTask.class.getSimpleName();
    String DOCTOR_FILTER_TYPE, DOCTOR_CITY;
    int DOCTOR_SPEC_ID, PAGINATION_NEXT;

    public LoadDoctorsFromUrlTask(Activity activity, String url, String doc_filtertype, int SpecID, String HospID, int pagination_val) {
        super();
        this.activity = activity;
        this.url = url;
        this.DOCTOR_FILTER_TYPE = doc_filtertype;
        this.DOCTOR_SPEC_ID = SpecID;
        this.DOCTOR_CITY = HospID;
        this.PAGINATION_NEXT = pagination_val;
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
     //   Log.d(AppUtils.TAG, "search url : " + this.url + "DOCTOR_FILTER_TYPE : " + this.DOCTOR_FILTER_TYPE);
        // call load JSON from url method
        return loadJSON(this.url).toString();
    }

    @Override
    protected void onPostExecute(String result) {
        ((DoctorsListActivity) activity).parseJsonResponse(result);

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
            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair(APIParam.KEY_API, APIClass.API_KEY));
                nameValuePairs.add(new BasicNameValuePair(APIParam.KEY_DOC_FILTER_TYPE, DOCTOR_FILTER_TYPE));
                nameValuePairs.add(new BasicNameValuePair(APIParam.KEY_DOC_SPECID, String.valueOf(DOCTOR_SPEC_ID)));
                nameValuePairs.add(new BasicNameValuePair(APIParam.KEY_DOC_CITY, String.valueOf(DOCTOR_CITY)));
                nameValuePairs.add(new BasicNameValuePair(APIParam.KEY_PAGINATION, String.valueOf(PAGINATION_NEXT)));
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
