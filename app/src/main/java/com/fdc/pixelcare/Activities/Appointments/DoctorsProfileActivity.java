package com.fdc.pixelcare.Activities.Appointments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fdc.pixelcare.DataModel.HospitalList;
import com.fdc.pixelcare.DataModel.SpecializationList;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Views.CustomTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by salma on 21/12/18.
 */

public class DoctorsProfileActivity extends AppCompatActivity implements View.OnClickListener {
    int DOC_ID, DOC_SPEC_ID;
    String DOC_NAME, DOC_QUALIFICATION, DOC_EXPERIENCE, DOC_CITY, DOC_SPEC_NAME, DOC_ENCRYPT_ID, DOC_AREA_INTEREST, DOC_PHOTO;
    String DOC_HOSP_NAME, DOC_HOSP_ADDRESS, DOC_HOSP_CITY, DOC_HOSP_STATE, DOC_HOSP_COUNTRY;

    CustomTextView _txt_name, txt_city, _txt_qualification, _txt_specialization, _txt_experience, _txt_address, _txt_area_expert;
    ImageView doc_image;
    LinearLayout book_appint, book_opinion, share_profile;

    List<SpecializationList> specilizationDocArraylist;
    List<HospitalList> hospitalDocArraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        specilizationDocArraylist = new ArrayList<>();
        hospitalDocArraylist = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            setTitle(title);

            DOC_ID = b.getInt("DOC_ID");
            DOC_NAME = b.getString("DOC_NAME");
            DOC_QUALIFICATION = b.getString("DOC_QUALIFICATION");
            DOC_EXPERIENCE = b.getString("DOC_EXPERIENCE");
            DOC_CITY = b.getString("DOC_CITY");
            DOC_SPEC_ID = b.getInt("DOC_SPEC_ID");
            DOC_SPEC_NAME = b.getString("DOC_SPEC_NAME");
            DOC_ENCRYPT_ID = b.getString("DOC_ENCRYPT_ID");
            DOC_AREA_INTEREST = b.getString("DOC_AREA_INTEREST");
            DOC_PHOTO = b.getString("DOC_PHOTO");
            DOC_HOSP_NAME = b.getString("DOC_HOSP_NAME");
            DOC_HOSP_ADDRESS = b.getString("DOC_HOSP_ADDRESS");
            DOC_HOSP_CITY = b.getString("DOC_HOSP_CITY");
            DOC_HOSP_STATE = b.getString("DOC_HOSP_STATE");
            DOC_HOSP_COUNTRY = b.getString("DOC_HOSP_COUNTRY");
            specilizationDocArraylist =  (List<SpecializationList>) b.getSerializable("SPECIALIZATION_LIST");
            hospitalDocArraylist =  (List<HospitalList>) b.getSerializable("HOSPITAL_LIST");

            Log.d(AppUtils.TAG, " ***************** DoctorsProfileActivity ********************* ");
            Log.d(AppUtils.TAG, " DOC_ID: "+ DOC_ID);
            Log.d(AppUtils.TAG, " DOC_NAME: "+ DOC_NAME);
            Log.d(AppUtils.TAG, " DOC_QUAL: "+ DOC_QUALIFICATION);
            Log.d(AppUtils.TAG, " DOC_EXP: "+ DOC_EXPERIENCE);
            Log.d(AppUtils.TAG, " DOC_CITY: "+ DOC_CITY);
            Log.d(AppUtils.TAG, " DOC_SPEC_NAME: "+ DOC_SPEC_NAME);
            Log.d(AppUtils.TAG, " DOC_AREA_INTEREST: "+ DOC_AREA_INTEREST);
            Log.d(AppUtils.TAG, " DOC_HOSP_NAME: "+ DOC_HOSP_NAME);
            Log.d(AppUtils.TAG, " DOC_HOSP_ADDRESS: "+ DOC_HOSP_ADDRESS);
            Log.d(AppUtils.TAG, " DOC_HOSP_CITY: "+ DOC_HOSP_CITY);
            Log.d(AppUtils.TAG, " DOC_HOSP_STATE: "+ DOC_HOSP_STATE);
            Log.d(AppUtils.TAG, " DOC_HOSP_COUNTRY: "+ DOC_HOSP_COUNTRY);

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

        doc_image = (ImageView) findViewById(R.id.doc_prof_image);
        _txt_name = (CustomTextView) findViewById(R.id.doc_prof_name);
        txt_city = (CustomTextView) findViewById(R.id.doc_prof_city);
        _txt_qualification = (CustomTextView) findViewById(R.id.doc_prof_qualification);
        _txt_specialization = (CustomTextView) findViewById(R.id.doc_prof_spec);
        _txt_experience = (CustomTextView) findViewById(R.id.doc_prof_experience);
        _txt_address = (CustomTextView) findViewById(R.id.doc_prof_hosp_address);
        _txt_area_expert = (CustomTextView) findViewById(R.id.doc_prof_area_expert);
        book_appint = (LinearLayout) findViewById(R.id.doc_prof_appointment);
        book_opinion = (LinearLayout) findViewById(R.id.doc_prof_opinion);
        book_opinion.setVisibility(View.GONE);
        share_profile = (LinearLayout) findViewById(R.id.doc_prof_share);

        book_appint.setOnClickListener(this);
        book_opinion.setOnClickListener(this);
        share_profile.setOnClickListener(this);

        if(DOC_PHOTO.equals("")) {
            doc_image.setImageResource(R.drawable.doctor_icon1);
        }
        else {
            String DOWNLOAD_PROFILE = APIClass.DRS_DOCTOR_PROFILE_URL+String.valueOf(DOC_ID)+"/"+DOC_PHOTO.trim();
            String urlStr = DOWNLOAD_PROFILE;
            URL url = null;
            try {
                url = new URL(urlStr);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            Picasso.with(this).load(String.valueOf(url))
                    .placeholder(R.drawable.doctor_icon1)
                    .error(R.drawable.doctor_icon1)
                    .resize(200, 200)
                    .centerInside()
                    .into(doc_image, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });

        }

        _txt_name.setText(DOC_NAME);
        txt_city.setText(DOC_CITY);
        _txt_qualification.setText(DOC_QUALIFICATION);
       // _txt_specialization.setText(DOC_SPEC_NAME);
        _txt_experience.setText(DOC_EXPERIENCE +" years of experience");
       // _txt_address.setText(DOC_HOSP_NAME+"\n"+DOC_HOSP_ADDRESS+"\n"+DOC_HOSP_CITY+"\n"+DOC_HOSP_STATE+"\n"+ DOC_HOSP_COUNTRY);
        _txt_area_expert.setText(DOC_AREA_INTEREST);

        if (specilizationDocArraylist != null && specilizationDocArraylist.size() > 0) {
            Log.d(AppUtils.TAG + " specList:", String.valueOf(specilizationDocArraylist.size()));

            StringBuilder sb = new StringBuilder();
            for(int  i =0; i < specilizationDocArraylist.size(); i++)
            {
                sb.append(specilizationDocArraylist.get(i).getSpecializationName().toString());
                sb.append(", ");
            }
            _txt_specialization.setText(sb.toString().substring(0, sb.toString().length() - 2) );
        }

        if (hospitalDocArraylist != null && hospitalDocArraylist.size() > 0) {
            Log.d(AppUtils.TAG + " hosp_LIST:", String.valueOf(hospitalDocArraylist.size()));
            StringBuilder sb = new StringBuilder();
            for(int  j =0; j < hospitalDocArraylist.size(); j++)
            {
                sb.append(hospitalDocArraylist.get(j).getHospitalName().toString()+", "+hospitalDocArraylist.get(j).getHospitalAddress()+", "+hospitalDocArraylist.get(j).getHospitalCity()+"\n"+hospitalDocArraylist.get(j).getHospitalState()+", "+hospitalDocArraylist.get(j).getHospitalCountry());
                sb.append("\n\n");
            }
            _txt_address.setText(sb.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.doc_prof_appointment:
                Intent i1 = new Intent(this, BookAppointmentActivity.class);
                i1.putExtra("title","Book Appointment");
                i1.putExtra("DOC_ID",DOC_ID);
                i1.putExtra("DOC_NAME",DOC_NAME);
                i1.putExtra("DOC_SPEC_ID",DOC_SPEC_ID);
                i1.putExtra("DOC_SPEC_NAME",DOC_SPEC_NAME);

                i1.putExtra("title","Book Appointment");
                i1.putExtra("DOC_ID",DOC_ID);
                i1.putExtra("DOC_NAME",DOC_NAME);
                i1.putExtra("DOC_SPEC_ID",DOC_SPEC_ID);
                i1.putExtra("DOC_SPEC_NAME",DOC_SPEC_NAME);
                i1.putExtra("DOC_QUALIFICATION",DOC_QUALIFICATION);
                i1.putExtra("DOC_CITY",DOC_CITY);
                i1.putExtra("DOC_ADDRESS",DOC_HOSP_ADDRESS);
                i1.putExtra("DOC_PHOTO",DOC_PHOTO);
                i1.putExtra("SPECIALIZATION_LIST", (Serializable) specilizationDocArraylist);
                i1.putExtra("HOSPITAL_LIST", (Serializable) hospitalDocArraylist);

                startActivity(i1);
                break;
            case R.id.doc_prof_opinion:
             /*   Intent i2 = new Intent(this, MedicalOpinionDocActivity.class);
                i2.putExtra("title","Medical Opinion");

                i2.putExtra("DOC_ID",DOC_ID);
                i2.putExtra("DOC_NAME",DOC_NAME);
                i2.putExtra("DOC_SPEC_ID",DOC_SPEC_ID);
                i2.putExtra("DOC_SPEC_NAME",DOC_SPEC_NAME);
                i2.putExtra("DOC_QUALIFICATION",DOC_QUALIFICATION);
                i2.putExtra("DOC_CITY",DOC_CITY);
                i2.putExtra("DOC_ADDRESS",DOC_HOSP_ADDRESS);
                i2.putExtra("DOC_PHOTO",DOC_PHOTO);

                startActivity(i2);*/
                break;
            case R.id.doc_prof_share:
                int doctor_hospitalID = 0;
                StringBuilder sb = new StringBuilder();
                for(int  i =0; i < specilizationDocArraylist.size(); i++)
                {
                    sb.append(specilizationDocArraylist.get(i).getSpecializationName().toString());
                    sb.append(", ");
                }

                StringBuilder sb1 = new StringBuilder();
                for(int  i =0; i < hospitalDocArraylist.size(); i++)
                {
                    sb1.append(hospitalDocArraylist.get(i).getHospitalName().toString()+", "+hospitalDocArraylist.get(i).getHospitalCity().toString());
                    sb1.append("\n");
                    doctor_hospitalID = hospitalDocArraylist.get(i).getHospitalId();
                }


                Intent sharingProfileIntent = new Intent(Intent.ACTION_SEND);
                sharingProfileIntent.setType("text/plain");
                sharingProfileIntent.putExtra(Intent.EXTRA_SUBJECT, "Pixel Care");
                sharingProfileIntent.putExtra(Intent.EXTRA_TEXT, DOC_NAME+"\n" + sb.toString() + "\n" + DOC_CITY + "\n\n"+ "You can connect with doctor & also book an appointment by visiting the link below: "+"\n"+"https://pixeleyecare.com/SendRequestLink/?d="+DOC_ENCRYPT_ID+"&hid="+md5(String.valueOf(doctor_hospitalID)));
                startActivity(Intent.createChooser(sharingProfileIntent, "Share Doctor Profile Via"));

                break;
        }
    }

    private String md5(String doctor_hospitalID) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(doctor_hospitalID.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
