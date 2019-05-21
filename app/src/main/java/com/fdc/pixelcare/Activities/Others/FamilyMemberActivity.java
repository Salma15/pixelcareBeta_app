package com.fdc.pixelcare.Activities.Others;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.Adapters.Others.FamilyMemberAdapter;
import com.fdc.pixelcare.DataModel.FamilyMember;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.Network.CustomVolleyRequestQueue;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
import com.fdc.pixelcare.Views.CustomEditText;
import com.fdc.pixelcare.Views.CustomTextView;
import com.fdc.pixelcare.Views.CustomTextViewBold;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
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
 * Created by SALMA on 31-12-2018.
 */
public class FamilyMemberActivity extends AppCompatActivity implements View.OnClickListener, FamilyMemberAdapter.ItemClickListener,
        Response.Listener, Response.ErrorListener {

    public static final String REQUEST_TAG = "FamilyMemberActivity";
    FamilyMemberAdapter adapter;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int USER_ID, USER_LOGINTYPE;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION, FAMILY_MEMBER_LIST;
    FloatingActionButton fab_Add;
    ArrayList<String> animalNames;

    ArrayList<String> memberNameArray;
    ArrayList<FamilyMember> memberArraylist;
    String PATIENT_NAME= "", PATIENT_GENDER = "0", GET_RELATIONSHIP_TYPE, PATIENT_DOB;
    int PATIENT_ID = 0;
    Calendar myCalendar;
    CustomTextView choose_dob;
    private RequestQueue mQueue;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_member);

        memberNameArray = new ArrayList<String>();
        memberArraylist = new ArrayList<>();
        myCalendar = Calendar.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            setTitle(title);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(FamilyMemberActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(FamilyMemberActivity.this);

        if(sharedPreferences !=null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE  = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);
            FAMILY_MEMBER_LIST = sharedPreferences.getString(MHConstants.PREF_FAMILY_MEMBERS, "");

            Log.d(AppUtils.TAG , " *********** FamilyMemberActivity **************** ");
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
        // data to populate the RecyclerView with
        animalNames = new ArrayList<>();
        animalNames.add(USER_NAME + " (Self)");

        Gson gson = new Gson();
        if(FAMILY_MEMBER_LIST != null && !FAMILY_MEMBER_LIST.isEmpty() ) {
            memberArraylist = new ArrayList<>();
            memberArraylist = gson.fromJson(FAMILY_MEMBER_LIST, new TypeToken<List<FamilyMember>>() {
            }.getType());
            Log.d(AppUtils.TAG +" memberSize: ", String.valueOf(memberArraylist.size()));

            for (int i = 0; i < memberArraylist.size(); i++) {
                //  memberNameArray.add(memberArraylist.get(i).getMemberName());
                if(USER_NAME.equalsIgnoreCase(memberArraylist.get(i).getMemberName())) {
                    memberNameArray.add(memberArraylist.get(i).getMemberName()+" (Self)");
                }
                else {
                    memberNameArray.add(memberArraylist.get(i).getMemberName());
                }
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.medprof_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FamilyMemberAdapter(this, memberArraylist, USER_NAME);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        fab_Add = (FloatingActionButton) findViewById(R.id.medprof_list_fab);
        fab_Add.setOnClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position, String memberName, int memberId, String gender, String age, String relation, String dob, String image) {
        //  Toast.makeText(this, "You have selected " + adapter.getItem(position) + " to view medical details " +memberName + " id: "+memberId+ " age: "+gender, Toast.LENGTH_SHORT).show();

        //  Intent i3 = new Intent(MedicalProfileActivity.this, MedicalCasesActivity.class);
       /* Intent i3 = new Intent(MedicalProfileActivity.this, MedicalCasesNewActivity.class);
        i3.putExtra("title","Health Profile");
        i3.putExtra("MEMBER_NAME",memberName);
        i3.putExtra("MEMBER_ID",memberId);
        i3.putExtra("MEMBER_GENDER",gender);
        i3.putExtra("MEMBER_AGE",age);
        i3.putExtra("MEMBER_RELATIONSHIP",relation);
        i3.putExtra("MEMBER_DOB",dob);
        i3.putExtra("MEMBER_PHOTO",image);
        startActivity(i3);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.medprof_list_fab:
                addNewMemberProfile();
                break;
        }
    }

    private void addNewMemberProfile() {
        final Dialog dialog = new Dialog(FamilyMemberActivity.this,  R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_member);
        dialog.setTitle("Add New Member");

        ImageView cancel_btn  = (ImageView) dialog.findViewById(R.id.addmember_cancel);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final TextInputLayout name_TL = (TextInputLayout) dialog.findViewById(R.id.addmember_name_TL);
        final CustomEditText _edt_name = (CustomEditText)  dialog.findViewById(R.id.addmember_name);
        final CustomTextViewBold submit_btn = (CustomTextViewBold)  dialog.findViewById(R.id.addmember_submit);
        Spinner spinner_relation = (Spinner) dialog.findViewById(R.id.addmember_relationSpinner);
        choose_dob = (CustomTextView)  dialog.findViewById(R.id.addmember_dob);
        final RadioGroup radioGenderGroup = (RadioGroup) dialog.findViewById(R.id.addmember_radioGroupGender);

        ArrayAdapter<String> spinnerCountShoesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.relationship_types));
        spinner_relation.setAdapter(spinnerCountShoesArrayAdapter);

        spinner_relation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //    Toast.makeText(BookAppointmentActivity.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                GET_RELATIONSHIP_TYPE = adapterView.getSelectedItem().toString().trim();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        choose_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_dob.setText("");
                new DatePickerDialog(FamilyMemberActivity.this, dob_date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int checkedRadioButtonId = radioGenderGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {   // No items Selected
                    PATIENT_GENDER = "0";
                }
                else{
                    if (checkedRadioButtonId == R.id.addmember_radioMale) {
                        PATIENT_GENDER = "1";
                    }
                    else if (checkedRadioButtonId == R.id.addmember_radioFemale) {
                        PATIENT_GENDER = "2";
                    }
                    else if (checkedRadioButtonId == R.id.addmember_radioTrans) {
                        PATIENT_GENDER = "3";
                    }
                    else {
                        PATIENT_GENDER = "0";
                    }
                }

                if(choose_dob.getText().toString().equals("--Select Date--")) {
                    PATIENT_DOB = "";
                }
                else {
                    PATIENT_DOB = choose_dob.getText().toString();
                }

                if(_edt_name.getText().toString().equals("")) {
                    name_TL.setError("Enter Name");
                }
                else if(PATIENT_GENDER.equals("0")) {
                    Toast.makeText(FamilyMemberActivity.this, "Select Gender !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm1.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);

                    submitAddMemberToServer(_edt_name.getText().toString().trim(), PATIENT_GENDER, GET_RELATIONSHIP_TYPE, PATIENT_DOB);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    DatePickerDialog.OnDateSetListener dob_date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Calendar dareSelected = Calendar.getInstance();
            dareSelected.set(year, monthOfYear, dayOfMonth);
            Calendar currentDate = Calendar.getInstance();

            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            choose_dob.setText(sdf.format(dareSelected.getTime()));
        }
    };

    private void submitAddMemberToServer(final String patient_name, final String get_gender, final String ger_relationship_type, final String patient_dob) {
        Log.d(AppUtils.TAG, " patient_name " + patient_name);
        Log.d(AppUtils.TAG, " get_gender " + get_gender);
        Log.d(AppUtils.TAG, " ger_relationship_type " + ger_relationship_type);
        Log.d(AppUtils.TAG, " patient_dob " + patient_dob);

        // Instantiate the RequestQueue.
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        StringRequest postRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_ADD_FAMILY_MEMBER,
                this,this ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(APIParam.KEY_API, APIClass.API_KEY);
                params.put(APIParam.KEY_MEMBER_NAME, patient_name);
                params.put(APIParam.KEY_MEMBER_GENDER, get_gender);
                params.put(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID));

                if(ger_relationship_type != null) {
                    params.put(APIParam.KEY_MEMBER_RELATIONSHIP, ger_relationship_type);
                }

                if(patient_dob != null) {

                    DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = inputFormat.parse(patient_dob);
                        String dob_new = outputFormat.format(date);
                        params.put(APIParam.KEY_MEMBER_DOB, dob_new);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

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

    @Override
    public void onErrorResponse(VolleyError error) {
        VolleyLog.e(AppUtils.TAG +" Error: ", error.getMessage());
       /* if(pd!=null || pd.isShowing())
            pd.dismiss();*/
        AppUtils.showCustomAlertMessage(FamilyMemberActivity.this, "Warning !!!",error.getMessage(), "OK", null, null);

    }

    @Override
    public void onResponse(Object response) {
        Log.d(AppUtils.TAG +" Result: ", response.toString());
        String staus_res = null, opinion_result = null;

      /*  if(pd!=null || pd.isShowing())
            pd.dismiss();*/

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(String.valueOf(response));

            if (jsonObject.has("result")) {
                staus_res = jsonObject.getString("result");
            }

            if (jsonObject.has("result_opinion")) {
                opinion_result = jsonObject.getString("result_opinion");
            }

            if(staus_res != null && !staus_res.isEmpty()) {
                if(staus_res.equalsIgnoreCase("failure")) {
                    Toast.makeText(FamilyMemberActivity.this, "Add Member \n"+jsonObject.getString("err_msg"), Toast.LENGTH_SHORT).show();
                }
                else {
                    AppUtils.showCustomSuccessMessage(FamilyMemberActivity.this, "Add Member", "Member added successfully !!!", "OK", null, null);

                    JSONArray jsonArray1 = jsonObject.getJSONArray("family_details");
                    if(jsonArray1.length() > 0 ) {
                        memberArraylist.clear();
                        memberArraylist = new ArrayList<FamilyMember>();
                        memberNameArray.clear();
                        memberNameArray = new ArrayList<String>();
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            memberArraylist.add(new FamilyMember(jsonArray1.getJSONObject(i).getInt("member_id"),jsonArray1.getJSONObject(i).getInt("user_id"),
                                    jsonArray1.getJSONObject(i).getString("gender"),jsonArray1.getJSONObject(i).getString("member_name"),
                                    jsonArray1.getJSONObject(i).getString("relationship"),jsonArray1.getJSONObject(i).getString("dob"),
                                    jsonArray1.getJSONObject(i).getString("age"),jsonArray1.getJSONObject(i).getString("member_photo")));
                        }
                        //Set the values Family Member
                        Gson gson = new Gson();
                        String jsonText = gson.toJson(memberArraylist);
                        if (sharedPreferences != null) {
                            shareadPreferenceClass.clearFamilyMemberLists();
                            shareadPreferenceClass.setFamilyMemberList(jsonText);
                        }
                        Log.d(AppUtils.TAG +" size: ", String.valueOf(memberArraylist.size()));
                        for (int i = 0; i < memberArraylist.size(); i++) {
                            //  memberNameArray.add(memberArraylist.get(i).getMemberName());
                            if(USER_NAME.equalsIgnoreCase(memberArraylist.get(i).getMemberName())) {
                                memberNameArray.add(memberArraylist.get(i).getMemberName()+" (Self)");
                            }
                            else {
                                memberNameArray.add(memberArraylist.get(i).getMemberName());
                            }
                        }

                        adapter = new FamilyMemberAdapter(this, memberArraylist, USER_NAME);
                        adapter.setClickListener(this);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
