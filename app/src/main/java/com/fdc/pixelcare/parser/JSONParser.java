package com.fdc.pixelcare.parser;

import android.util.Log;

import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.Utils.AppUtils;

import org.json.JSONObject;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by SALMA on 31/12/18.
 */

public class JSONParser {
    public static JSONObject sendMedicalOpinion(String contact_person, String mobile_num, String email_id, String address,
                                                String city, String state, String country, String patient_name, int patient_id,
                                                String patient_gender, String age, String weight, String patient_hypertension,
                                                String patient_diabetes, int specialization_id, String specialization_name,
                                                String medical_complaint, String brief_description, String query_doctor,
                                                ArrayList<String> reports_photos, int user_id, String user_name, int doc_id, int spec_id) {

        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            if (reports_photos != null && reports_photos.size() > 0) {
                Log.d(AppUtils.TAG + "PH SIZE:", String.valueOf(reports_photos.size()));
                ArrayList aList = new ArrayList(Arrays.asList(reports_photos.toString().substring(1, reports_photos.toString().length()-1).toString().split(",")));
                for(int i=0;i<aList.size();i++)
                {
                    System.out.println(AppUtils.TAG+" -->"+aList.get(i));
                    File file = new File(aList.get(i).toString());
                    if (file.exists()) {
                        final MediaType MEDIA_TYPE = MediaType.parse("image/*");
                        builder.addFormDataPart("file-3[]", file.getName(), RequestBody.create(MEDIA_TYPE, file));
                    } else {
                        Log.d(AppUtils.TAG, "file not exist ");
                    }
                }

            }

            builder.addFormDataPart(APIParam.KEY_API, APIClass.API_KEY);
            builder.addFormDataPart(APIParam.KEY_DATA_SOURCE, APIClass.API_DATASOURCE_KEY);
            builder.addFormDataPart(APIParam.KEY_LOGIN_ID, String.valueOf(user_id));
            builder.addFormDataPart(APIParam.KEY_MEMBER_ID, String.valueOf(patient_id));
            builder.addFormDataPart(APIParam.KEY_DOCTOR_ID, String.valueOf(doc_id));
            builder.addFormDataPart(APIParam.KEY_SPEC_ID, String.valueOf(specialization_id));

            builder.addFormDataPart(APIParam.KEY_MEDOP_USERNAME, contact_person);
            builder.addFormDataPart(APIParam.KEY_MEDOP_AGE, age);
            builder.addFormDataPart(APIParam.KEY_MEDOP_GENDER, patient_gender);
            builder.addFormDataPart(APIParam.KEY_MEDOP_WEIGHT, weight);
            builder.addFormDataPart(APIParam.KEY_MEDOP_HYPERTENSION, patient_hypertension);
            builder.addFormDataPart(APIParam.KEY_MEDOP_DIABETES, patient_diabetes);
            builder.addFormDataPart(APIParam.KEY_MEDOP_EMAIL, email_id);
            builder.addFormDataPart(APIParam.KEY_MEDOP_MOBILE, mobile_num);
            builder.addFormDataPart(APIParam.KEY_MEDOP_ADDRESS, address);
            builder.addFormDataPart(APIParam.KEY_MEDOP_CITY, city);
            builder.addFormDataPart(APIParam.KEY_MEDOP_STATE, state);
            builder.addFormDataPart(APIParam.KEY_MEDOP_COUNTRY, country);

            if(patient_name != null) {
                String input = patient_name;
                boolean isFound = input.indexOf(" (Self)") !=-1? true: false; //true
                Log.d(AppUtils.TAG, " isFound: "+ String.valueOf(isFound));
                if(isFound) {

                    String[] separated = input.split(" \\(");
                    String init_name = separated[0];
                    builder.addFormDataPart(APIParam.KEY_MEDOP_PATIENT_NAME, init_name);
                }
                else {
                    builder.addFormDataPart(APIParam.KEY_MEDOP_PATIENT_NAME, patient_name);
                }
            }

            if(medical_complaint != null) {
                builder.addFormDataPart(APIParam.KEY_MEDOP_COMPLAINTS, medical_complaint);
            }

            if(brief_description != null) {
                builder.addFormDataPart(APIParam.KEY_MEDOP_DESCRIPTIONS, brief_description);
            }

            if(query_doctor != null) {
                builder.addFormDataPart(APIParam.KEY_MEDOP_QUERY, query_doctor);
            }


            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_HOST_MEDICAL_OPINION)
                    .post(requestBody)
                    .build();

            //   OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(AppUtils.TAG, " create: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(AppUtils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(AppUtils.TAG, "Other Error: " + e.getLocalizedMessage());
        }


        return null;
    }

    public static JSONObject addHealthTrack(int member_id, String hyperDate, String diabetesDate, String cholestrolDate,
                                            String systolic_val, String diastolic_val, String preprandial_val,
                                            String postprandial_val, String hba1c_val, String triglycerides_val,
                                            String total_cholestrol_val, String hdl_val, String ldl_val, String vldl_val,
                                            int user_id, int logintype) {

        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart(APIParam.KEY_API, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", String.valueOf(logintype));
            builder.addFormDataPart("se_member_id", String.valueOf(member_id));

            if(hyperDate != null){
                DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = inputFormat.parse(hyperDate);
                    String hyperDate_new = outputFormat.format(date);
                    builder.addFormDataPart("se_hyper_date", hyperDate_new);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if(diabetesDate != null){
                builder.addFormDataPart("se_diabetes_date", diabetesDate);
            }
            if(cholestrolDate != null){
                builder.addFormDataPart("se_cholestroldate", cholestrolDate);
            }

            if(systolic_val != null) {
                builder.addFormDataPart("se_systolic", systolic_val);
            }

            if(diastolic_val != null) {
                builder.addFormDataPart("se_diastolic", diastolic_val);
            }

            if(preprandial_val != null) {
                builder.addFormDataPart("se_preprandial", preprandial_val);
            }

            if(postprandial_val != null) {
                builder.addFormDataPart("se_postprandial", postprandial_val);
            }

            if(hba1c_val != null) {
                builder.addFormDataPart("se_hba1c", hba1c_val);
            }

            if(triglycerides_val != null) {
                builder.addFormDataPart("se_trigycerides", triglycerides_val);
            }

            if(total_cholestrol_val != null) {
                builder.addFormDataPart("se_total_cholestrol", total_cholestrol_val);
            }

            if(hdl_val != null) {
                builder.addFormDataPart("se_hdl", hdl_val);
            }

            if(ldl_val != null) {
                builder.addFormDataPart("se_ldl", ldl_val);
            }

            if(vldl_val != null) {
                builder.addFormDataPart("se_vldl", vldl_val);
            }


            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_HOST_ADD_TRENDS)
                    .post(requestBody)
                    .build();

            //   OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(AppUtils.TAG, " create: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(AppUtils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(AppUtils.TAG, "Other Error: " + e.getLocalizedMessage());
        }

        return null;
    }

    public static JSONObject uploadEditProfile(String patient_name, String pat_age, String patient_gender,
                                               String get_relationship_type, String patient_dob, String patient_image,
                                               int member_id, int user_id) {

        try {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            File file = new File(patient_image);
            if(file.exists()) {
                final MediaType MEDIA_TYPE = MediaType.parse("image/*");
                builder.addFormDataPart("txtPhoto",file.getName(),RequestBody.create(MEDIA_TYPE,file));
            }
            else {
                Log.d(AppUtils.TAG, "file not exist ");
            }

            builder.addFormDataPart(APIParam.KEY_API, APIClass.API_KEY);
            builder.addFormDataPart(APIParam.KEY_MEMBER_NAME, patient_name);
            builder.addFormDataPart(APIParam.KEY_MEMBER_GENDER, patient_gender);
            builder.addFormDataPart(APIParam.KEY_MEMBER_ID, String.valueOf(member_id));
            builder.addFormDataPart(APIParam.KEY_LOGIN_ID, String.valueOf(user_id));

            if(patient_dob != null) {
                DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = inputFormat.parse(patient_dob);
                    String dob_new = outputFormat.format(date);
                    builder.addFormDataPart(APIParam.KEY_MEMBER_DOB, dob_new);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if(get_relationship_type != null) {
                builder.addFormDataPart(APIParam.KEY_MEMBER_RELATIONSHIP, get_relationship_type);
            }

            if(pat_age != null) {
                builder.addFormDataPart(APIParam.KEY_MEMBER_AGE, pat_age);
            }


            RequestBody requestBody = builder.build();


            Request request = new Request.Builder()
                    .url(APIClass.DRS_HOST_UPDATE_FAMILY_MEMBER)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(AppUtils.TAG, "edit: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(AppUtils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(AppUtils.TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }
}
