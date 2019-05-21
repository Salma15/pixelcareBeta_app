package com.fdc.pixelcare.Adapters.Home;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fdc.pixelcare.Activities.Appointments.BookAppointmentActivity;
import com.fdc.pixelcare.Activities.Appointments.DoctorsProfileActivity;
import com.fdc.pixelcare.Activities.Opinions.MedicalOpinionActivity;
import com.fdc.pixelcare.DataModel.DoctorList;
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
import java.util.List;

/**
 * Created by SALMA on 21-12-2018.
 */
public class DoctorsListAdapter  extends ArrayAdapter<DoctorList> {

    private Activity activity;
    int doctor_hospitalID = 0;

    public DoctorsListAdapter(Activity activity, int resource, int textViewResourceId, List<DoctorList> doctors) {
        super(activity, resource, textViewResourceId, doctors);
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.consulted_doctors_list_item, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        }  else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        final DoctorList docs = getItem(position);

        holder.docName.setText(docs.getDocName());
       // holder.docOpinion.setVisibility(View.GONE);

        StringBuilder sb = new StringBuilder();
        for(int  i =0; i < docs.getDocSpecialization().size(); i++)
        {
            sb.append(docs.getDocSpecialization().get(i).getSpecializationName().toString());
            sb.append(", ");
        }

        if(sb.toString().length() > 0) {
              holder.docSpec.setText(sb.toString().substring(0, sb.toString().length() - 2) );
        }
        else {
            holder.docSpec.setText(sb.toString() );
        }


        StringBuilder sb1 = new StringBuilder();
        for(int  i =0; i < docs.getDocHospital().size(); i++)
        {
            sb1.append(docs.getDocHospital().get(i).getHospitalName().toString()+", "+docs.getDocHospital().get(i).getHospitalCity().toString());
            sb1.append("\n");
        }
        holder.docCity.setText(sb1.toString());


        if(docs.getDocConsulted() == 1) {
            holder.docStarImg.setVisibility(View.VISIBLE);
        }
        else {
            holder.docStarImg.setVisibility(View.GONE);
        }

        if(docs.getDocPhoto().equals("")) {
            holder.docImage.setImageResource(R.drawable.doctor_icon);
        }
        else {
            String DOWNLOAD_PROFILE = APIClass.DRS_DOCTOR_PROFILE_URL+String.valueOf(docs.getDocId())+"/"+docs.getDocPhoto().trim();
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
            Picasso.with(activity).load(String.valueOf(url))
                    .placeholder(R.drawable.doctor_icon)
                    .error(R.drawable.doctor_icon)
                    .resize(200, 200)
                    .centerInside()
                    .into(holder.docImage, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }

        holder.docCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(activity, DoctorsProfileActivity.class);
                i1.putExtra("title","Doctor Profile");
                i1.putExtra("DOC_ID",docs.getDocId());
                i1.putExtra("DOC_NAME",docs.getDocName());
                i1.putExtra("DOC_QUALIFICATION",docs.getDocQualification());
                i1.putExtra("DOC_EXPERIENCE",docs.getDocExperience());
                i1.putExtra("DOC_CITY",docs.getDocCity());
                i1.putExtra("DOC_SPEC_ID",docs.getDocSpecId());
                i1.putExtra("DOC_SPEC_NAME",docs.getDocSpecName());
                i1.putExtra("DOC_ENCRYPT_ID",docs.getDocEncryptID());
                i1.putExtra("DOC_AREA_INTEREST",docs.getDocAreaInterest());
                i1.putExtra("DOC_PHOTO",docs.getDocPhoto());
                i1.putExtra("DOC_HOSP_NAME",docs.getDocHospName());
                i1.putExtra("DOC_HOSP_ADDRESS",docs.getDocHospAddress());
                i1.putExtra("DOC_HOSP_CITY",docs.getDocHospCity());
                i1.putExtra("DOC_HOSP_STATE",docs.getDocHospState());
                i1.putExtra("DOC_HOSP_COUNTRY",docs.getDocHospCountry());
                i1.putExtra("SPECIALIZATION_LIST", (Serializable) docs.getDocSpecialization());
                i1.putExtra("HOSPITAL_LIST", (Serializable) docs.getDocHospital());
                activity.startActivity(i1);
            }
        });

        holder.docShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder sb = new StringBuilder();
                for(int  i =0; i < docs.getDocSpecialization().size(); i++)
                {
                    sb.append(docs.getDocSpecialization().get(i).getSpecializationName().toString());
                    sb.append(", ");
                }

                StringBuilder sb1 = new StringBuilder();
                for(int  i =0; i < docs.getDocHospital().size(); i++)
                {
                    sb1.append(docs.getDocHospital().get(i).getHospitalName().toString()+", "+docs.getDocHospital().get(i).getHospitalCity().toString());
                    sb1.append("\n");
                    doctor_hospitalID = docs.getDocHospital().get(i).getHospitalId();
                }

                String URL = "https://medisensecrm.com/SendRequestLink/?d="+docs.getDocEncryptID()+"&hid="+md5(String.valueOf(doctor_hospitalID));
                Log.d(AppUtils.TAG, " URL: "+URL);

                Intent sharingProfileIntent = new Intent(Intent.ACTION_SEND);
                sharingProfileIntent.setType("text/plain");
                sharingProfileIntent.putExtra(Intent.EXTRA_SUBJECT, "Medisense");
                sharingProfileIntent.putExtra(Intent.EXTRA_TEXT, docs.getDocName()+"\n" + sb.toString() + "\n" + docs.getDocCity() + "\n\n"+ "You can connect with doctor & also book an appointment by visiting the link below: "+"\n"+"https://medisensecrm.com/SendRequestLink/?d="+docs.getDocEncryptID()+"&hid="+md5(String.valueOf(doctor_hospitalID)));
                activity.startActivity(Intent.createChooser(sharingProfileIntent, "Share Doctor Profile Via"));

            }
        });

        holder.docAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(activity, BookAppointmentActivity.class);
                i1.putExtra("title","Book Appointment");
                i1.putExtra("DOC_ID",docs.getDocId());
                i1.putExtra("DOC_NAME",docs.getDocName());
                i1.putExtra("DOC_SPEC_ID",docs.getDocSpecId());
                i1.putExtra("DOC_SPEC_NAME",docs.getDocSpecName());
                i1.putExtra("DOC_QUALIFICATION",docs.getDocQualification());
                i1.putExtra("DOC_CITY",docs.getDocHospCity());
                i1.putExtra("DOC_ADDRESS",docs.getDocHospAddress());
                i1.putExtra("DOC_PHOTO",docs.getDocPhoto());
                i1.putExtra("SPECIALIZATION_LIST", (Serializable) docs.getDocSpecialization());
                i1.putExtra("HOSPITAL_LIST", (Serializable) docs.getDocHospital());
                activity.startActivity(i1);
            }
        });

        holder.docOpinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(activity, MedicalOpinionActivity.class);
                i2.putExtra("title","Medical Opinion");
                i2.putExtra("DOC_ID", docs.getDocId());
                i2.putExtra("DOC_NAME", docs.getDocName());
                activity.startActivity(i2);
            }
        });

        return convertView;
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

    private static class ViewHolder {
        private CustomTextView docName, docSpec, docCity;
        private ImageView docImage, docStarImg;
        private CardView docCardView;
        private LinearLayout docAppointment, docOpinion, docShare;

        public ViewHolder(View v) {
            docName = (CustomTextView) v.findViewById(R.id.map_list_name);
            docSpec = (CustomTextView) v.findViewById(R.id.map_list_spec);
            docCity = (CustomTextView) v.findViewById(R.id.map_list_city);
            docImage = (ImageView) v.findViewById(R.id.map_list_image);
            docCardView = (CardView) v.findViewById(R.id.map_list_cardview);
            docAppointment = (LinearLayout) v.findViewById(R.id.map_list_appointment);
            docOpinion = (LinearLayout) v.findViewById(R.id.map_list_opinion);
            docShare = (LinearLayout) v.findViewById(R.id.map_list_share);
            docStarImg = (ImageView) v.findViewById(R.id.map_list_star);
        }
    }

}

