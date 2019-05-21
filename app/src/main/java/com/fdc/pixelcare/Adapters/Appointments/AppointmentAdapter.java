package com.fdc.pixelcare.Adapters.Appointments;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.DataModel.AppointmentList;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppController;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Views.CustomTextView;
import com.fdc.pixelcare.Views.CustomTextViewSemiBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by SALMA on 22/12/2018.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder>  {

    private List<AppointmentList> apptList;
    private Context mContext;
    Spinner appoint_date_spinner, appoint_time_spinner;
    String PATIENT_APPOINTMENT_DATE = "", PATIENT_APPOINTMENT_TIME = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView patient_name,  status;
        public CardView table_row;
        public CustomTextView visit_date, visit_time;
        public CustomTextViewSemiBold visit_docname;
        public LinearLayout visit_reschedule;

        public MyViewHolder(View view) {
            super(view);
            patient_name = (CustomTextView) view.findViewById(R.id.appt_patientname);
            visit_date = (CustomTextView) view.findViewById(R.id.appt_visit_date);
            visit_time = (CustomTextView) view.findViewById(R.id.appt_time);
            status = (CustomTextView) view.findViewById(R.id.appt_status);
            visit_docname  = (CustomTextViewSemiBold) view.findViewById(R.id.appt_docname);
            table_row = (CardView) view.findViewById(R.id.appt_tableRow);
            visit_reschedule = (LinearLayout) view.findViewById(R.id.appt_visit_reschedule);
        }
    }

    public AppointmentAdapter(Context context, List<AppointmentList> apptList) {
        this.apptList = apptList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AppointmentList appt = apptList.get(position);

        holder.patient_name.setText("Patient Name: "+appt.getPatientName());
        holder.visit_time.setText(appt.getVisitTime());
        holder.status.setText(appt.getAppointPayStatus());
        holder.visit_docname.setText(appt.getAppointDoctorName());

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = null;
        try {
            date = fmt.parse(appt.getVisitDate().trim());
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
            String visit_date_convt = fmtOut.format(date);
            holder.visit_date.setText(visit_date_convt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.table_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent i3 = new Intent(mContext, AppointDetailsActivity.class);
                i3.putExtra("title","Appointment Details");
                i3.putExtra("PATIENT_NAME", appt.getPatientName());
                i3.putExtra("PATIENT_VISIT_DATE", appt.getVisitDate());
                i3.putExtra("PATIENT_VISIT_TIME", appt.getVisitTime());
                i3.putExtra("PATIENT_PAY_STATUS", appt.getAppointPayStatus());
                i3.putExtra("PATIENT_TRANSACTION_ID",appt.getTransactionID());
                i3.putExtra("PATIENT_MOBILE",appt.getAppointMobile());
                i3.putExtra("PATIENT_EMAIL",appt.getAppointEmail());
                mContext.startActivity(i3);*/
            }
        });

        holder.visit_reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = null;
                try {
                    date = inputFormat.parse(appt.getVisitDate().trim());
                    String appoint_date_str = outputFormat.format(date);

                    Calendar cal = Calendar.getInstance();
                    Date sysDate = cal.getTime();
                    if(date.compareTo(sysDate)>0 && appt.getAppointPayStatus().equalsIgnoreCase("Pending")) {
                        //Log.d(AppUtils.TAG, " date_is_outdated false future" );      // Future appointment
                      //  Toast.makeText(mContext, "Reschedule appointment !!!", Toast.LENGTH_SHORT).show();
                        customRescheduleAppointment(appt.getAppointID(),appt.getTransactionID(),appt.getAppointDoctorID(), appt.getAppointHospitalID());
                    }else{
                        //Log.d(AppUtils.TAG, " date_is_outdated true today" );         // Walk In appointment. Today's appointments
                        Toast.makeText(mContext, "Appointment cannot be rescheduled !!!", Toast.LENGTH_SHORT).show();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onItemDismiss(int position) {
        if(position!=-1 && position<apptList.size())
        {
            apptList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    @Override
    public int getItemCount() {
        return apptList.size();
    }

    private void customRescheduleAppointment(final int appointID, final String transactionID, final int appointDoctorID, final int appointHospitalID) {
        Log.d(AppUtils.TAG, " appointID: "+appointID );
        Log.d(AppUtils.TAG, " transactionID: "+transactionID );
        Log.d(AppUtils.TAG, " appointDoctorID: "+appointDoctorID );
        Log.d(AppUtils.TAG, " appointHospitalID: "+appointHospitalID );

        final Dialog dialog = new Dialog(mContext, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_appointment_reschedule);

        appoint_date_spinner = (Spinner) dialog.findViewById(R.id.appintment_reschedule_date_spinner);
        appoint_time_spinner = (Spinner) dialog.findViewById(R.id.appintment_reschedule_time_spinner);

        new getJsonAppointmentDates().execute(String.valueOf(appointID),String.valueOf(transactionID), String.valueOf(appointDoctorID), String.valueOf(appointHospitalID));

        Button submit_btn = (Button) dialog.findViewById(R.id.appt_reschedule_submit);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(AppUtils.TAG, " PATIENT_APPOINTMENT_DATE: "+PATIENT_APPOINTMENT_DATE);
                Log.d(AppUtils.TAG, " PATIENT_APPOINTMENT_TIME: "+PATIENT_APPOINTMENT_TIME);

                if(PATIENT_APPOINTMENT_DATE.equals("")) {
                    Toast.makeText(mContext, "Select appointment date !!!", Toast.LENGTH_SHORT).show();
                }
                else if(PATIENT_APPOINTMENT_TIME.equals("")) {
                    Toast.makeText(mContext, "Select appointment time !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                    submitRescheduleAppointmentToServer(PATIENT_APPOINTMENT_DATE, PATIENT_APPOINTMENT_TIME, appointID, transactionID, appointDoctorID, appointHospitalID );
                }
            }
        });

        dialog.show();
    }

    class getJsonAppointmentDates extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... key) {
            String appointIDVal = key[0];
            String transactionIDVal = key[1];
            final String appointDoctorIDVal = key[2];
            final String appointHospitalIDVal = key[3];

            Log.d(AppUtils.TAG, " appointIDVal: " + appointIDVal);
            Log.d(AppUtils.TAG, " transactionIDVal: " + transactionIDVal);
            Log.d(AppUtils.TAG, " appointDoctorIDVal: " + appointDoctorIDVal);
            Log.d(AppUtils.TAG, " appointHospitalIDVal: " + appointHospitalIDVal);

            // suggest_state = new ArrayList<String>();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_APPOINTMENT_DATES,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(AppUtils.TAG, " appoint dates: "+ response.toString());

                            if (response != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String staus_res = jsonObject.getString("status");
                                    if(staus_res.equals("true"))
                                    {
                                        JSONArray jsonArray3 = jsonObject.getJSONArray("appoint_details");
                                        ArrayList<String> appoint_dateArray = new ArrayList<String>();
                                        final ArrayList<String> appoint_dateIDArray = new ArrayList<String>();
                                        if (jsonArray3.length() > 0) {
                                            appoint_dateArray.add("--Select--");
                                            appoint_dateIDArray.add("0");
                                            for (int i = 0; i < jsonArray3.length(); i++) {
                                                appoint_dateArray.add(jsonArray3.getJSONObject(i).getString("appt_date"));
                                                appoint_dateIDArray.add(jsonArray3.getJSONObject(i).getString("appt_id"));
                                            }

                                            ArrayAdapter<String> apptdateAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text, appoint_dateArray );
                                            appoint_date_spinner.setAdapter(apptdateAdapter);

                                            appoint_date_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view,
                                                                           int position, long id) {
                                                    Log.d(AppUtils.TAG, " appoint_date: "+ (String) parent.getItemAtPosition(position));
                                                    String get_state = (String) parent.getItemAtPosition(position);
                                                    Log.d(AppUtils.TAG, "appt_date_ID: "+ appoint_dateIDArray.get(position).toString());

                                                    if(appoint_dateIDArray.get(position).toString().equals("0")) {
                                                        PATIENT_APPOINTMENT_DATE = "";

                                                    }
                                                    else {
                                                        PATIENT_APPOINTMENT_DATE = appoint_dateIDArray.get(position).toString();
                                                        // APPOINT_DATE = PATIENT_APPOINTMENT_DATE;

                                                        new getJsonAppointmentTimings().execute(appoint_dateIDArray.get(position).toString(), appointHospitalIDVal, appointDoctorIDVal);
                                                    }
                                                }
                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {
                                                    PATIENT_APPOINTMENT_DATE = "";

                                                }
                                            });
                                        }

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
                            Log.d(AppUtils.TAG + "ERR ",error.toString());
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIParam.KEY_API, APIClass.API_KEY);
                    map.put(APIParam.KEY_HOSPITAL_ID, appointHospitalIDVal);
                    map.put(APIParam.KEY_DOCTOR_ID, appointDoctorIDVal);
                    return map;
                }
            };
            RequestQueue requestQueue = AppController.getInstance(mContext).
                    getRequestQueue();
            AppController.getInstance(mContext).addToRequestQueue(stringRequest);
            return null;
        }
    }

    class getJsonAppointmentTimings extends AsyncTask<String,String,String> {
        String appoint_date_id, hospital_id, doctor_id;
        @Override
        protected String doInBackground(String... key) {
            appoint_date_id = key[0];
            hospital_id = key[1];
            doctor_id = key[2];

            Log.d(AppUtils.TAG, " selected appoint_date_id: " + appoint_date_id);
            Log.d(AppUtils.TAG, " selected hospital_id: " + hospital_id);

            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_APPOINTMENT_TIMINGS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(AppUtils.TAG, " timings info: "+ response.toString());
                            if(response != null) {
                                JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;
                                try {
                                    jsonObject1 = new JSONObject(response);
                                    String staus_res = jsonObject1.getString("status");

                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray1 = jsonObject1.getJSONArray("appoint_timing_details");
                                        ArrayList<String> appoint_TimeArray = new ArrayList<String>();
                                        final ArrayList<String> appoint_timeIDArray = new ArrayList<String>();
                                        if (jsonArray1.length() > 0) {
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                appoint_TimeArray.add(jsonArray1.getJSONObject(i).getString("aapt_time"));
                                                appoint_timeIDArray.add(jsonArray1.getJSONObject(i).getString("aapt_time_id"));
                                            }

                                            ArrayAdapter<String> apptdateAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text, appoint_TimeArray );
                                            appoint_time_spinner.setAdapter(apptdateAdapter);

                                            appoint_time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view,
                                                                           int position, long id) {
                                                    Log.d(AppUtils.TAG, " appoint_time: "+ (String) parent.getItemAtPosition(position));
                                                    String get_state = (String) parent.getItemAtPosition(position);
                                                    Log.d(AppUtils.TAG, "appt_time_ID: "+ appoint_timeIDArray.get(position).toString());

                                                    if(appoint_timeIDArray.get(position).toString().equals("0")) {
                                                        PATIENT_APPOINTMENT_TIME = "";

                                                    }
                                                    else {
                                                        PATIENT_APPOINTMENT_TIME = appoint_timeIDArray.get(position).toString();

                                                     }
                                                }
                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {
                                                    PATIENT_APPOINTMENT_TIME = "";

                                                }
                                            });
                                        }
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
                    map.put(APIParam.KEY_API,APIClass.API_KEY);
                    map.put(APIParam.KEY_DOCTOR_ID, String.valueOf(doctor_id));
                    map.put(APIParam.KEY_APPOINT_DATE_ID, appoint_date_id);
                    map.put(APIParam.KEY_HOSPITAL_ID, String.valueOf(hospital_id));
                    return map;
                }
            };

            stringRequest.setRetryPolicy(policy);

            RequestQueue requestQueue = AppController.getInstance(mContext).
                    getRequestQueue();
            AppController.getInstance(mContext).addToRequestQueue(stringRequest);

            return null;
        }
    }

    private void submitRescheduleAppointmentToServer(final String patient_appointment_date, final String patient_appointment_time, final int appointID,
                                                     final String transactionID, final int appointDoctorID, final int appointHospitalID) {
        Log.d(AppUtils.TAG, " patient_appointment_date: " + patient_appointment_date);
        Log.d(AppUtils.TAG, " patient_appointment_time: " + patient_appointment_time);
        Log.d(AppUtils.TAG, " appointID: " + appointID);
        Log.d(AppUtils.TAG, " transactionID: " + transactionID);
        Log.d(AppUtils.TAG, " appointDoctorID: " + appointDoctorID);
        Log.d(AppUtils.TAG, " appointHospitalID: " + appointHospitalID);


        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_APPOINTMENT_RESCHEDULE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(AppUtils.TAG, " timings info: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;
                            try {
                                jsonObject1 = new JSONObject(response);
                                String staus_res = jsonObject1.getString("status");

                                if (staus_res.equals("true")) {
                                    final Dialog dialog = new Dialog(mContext, R.style.CustomDialog);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.setContentView(R.layout.custom_success);

                                    CustomTextView text = (CustomTextView) dialog.findViewById(R.id.text_success_dialog);
                                    text.setText("Appointment Rescheduled Successfully !!!");

                                    Button dialogButton = (Button) dialog.findViewById(R.id.text_success_submit);
                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();
                                } else {
                                    Toast.makeText(mContext, "Appointment Reschedule Failed !!!", Toast.LENGTH_SHORT).show();
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
                map.put(APIParam.KEY_API,APIClass.API_KEY);
                map.put(APIParam.KEY_DOCTOR_ID, String.valueOf(appointDoctorID));
                map.put(APIParam.KEY_HOSPITAL_ID, String.valueOf(appointHospitalID));
                map.put(APIParam.KEY_BOOK_APPOINT_DATE, patient_appointment_date);
                map.put(APIParam.KEY_BOOK_APPOINT_TIME, String.valueOf(patient_appointment_time));
                map.put(APIParam.KEY_BOOK_APPOINT_TRANSACTION, transactionID);
                map.put(APIParam.KEY_BOOK_APPOINT_ID, String.valueOf(appointID));
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(mContext).
                getRequestQueue();
        AppController.getInstance(mContext).addToRequestQueue(stringRequest);
    }
}
