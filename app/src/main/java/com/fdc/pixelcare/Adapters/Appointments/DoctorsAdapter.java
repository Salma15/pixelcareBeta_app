package com.fdc.pixelcare.Adapters.Appointments;

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
import com.fdc.pixelcare.Activities.Opinions.OpinionListActivity;
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
 * Created by SALMA on 22/12/18.
 */

public class DoctorsAdapter extends ArrayAdapter<DoctorList> {
    private Activity activity;
    int doctor_hospitalID = 0;

    public DoctorsAdapter(Activity activity, int resource, int textViewResourceId, List<DoctorList> countries) {
        super(activity, resource, textViewResourceId, countries);
        this.activity = activity;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.doctor_list_item, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        }  else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        final DoctorList country = getItem(position);

        holder.docName.setText(country.getDocName());
     //   holder.docSpec.setText(country.getDocSpecName());
     //   holder.docCity.setText(country.getDocCity());

        StringBuilder sb = new StringBuilder();
        if(country.getDocSpecialization().size() >0) {
            for (int i = 0; i < country.getDocSpecialization().size(); i++) {
                sb.append(country.getDocSpecialization().get(i).getSpecializationName().toString());
                sb.append(", ");
            }
            if (sb.toString().length() > 2) {
                holder.docSpec.setText(sb.toString().substring(0, sb.toString().length() - 2));
            } else {
                holder.docSpec.setText(sb.toString());
            }
        }


        StringBuilder sb1 = new StringBuilder();
        if(country.getDocHospital().size() >0) {
            for (int i = 0; i < country.getDocHospital().size(); i++) {
                sb1.append(country.getDocHospital().get(i).getHospitalName().toString() + ", " + country.getDocHospital().get(i).getHospitalCity().toString());
                sb1.append("\n");
            }
            holder.docCity.setText(sb1.toString());
        }

        if(country.getDocPhoto().equals("")) {
            holder.docImage.setImageResource(R.drawable.doctor_icon1);
        }
        else {
            String DOWNLOAD_PROFILE = APIClass.DRS_DOCTOR_PROFILE_URL+String.valueOf(country.getDocId())+"/"+country.getDocPhoto().trim();
            String urlStr = DOWNLOAD_PROFILE;
            Log.d(AppUtils.TAG +" PROFILE: ", DOWNLOAD_PROFILE);
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
                    .placeholder(R.drawable.doctor_icon1)
                    .error(R.drawable.doctor_icon1)
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
                i1.putExtra("DOC_ID",country.getDocId());
                i1.putExtra("DOC_NAME",country.getDocName());
                i1.putExtra("DOC_QUALIFICATION",country.getDocQualification());
                i1.putExtra("DOC_EXPERIENCE",country.getDocExperience());
                i1.putExtra("DOC_CITY",country.getDocCity());
                i1.putExtra("DOC_SPEC_ID",country.getDocSpecId());
                i1.putExtra("DOC_SPEC_NAME",country.getDocSpecName());
                i1.putExtra("DOC_ENCRYPT_ID",country.getDocEncryptID());
                i1.putExtra("DOC_AREA_INTEREST",country.getDocAreaInterest());
                i1.putExtra("DOC_PHOTO",country.getDocPhoto());
                i1.putExtra("DOC_HOSP_NAME",country.getDocHospName());
                i1.putExtra("DOC_HOSP_ADDRESS",country.getDocHospAddress());
                i1.putExtra("DOC_HOSP_CITY",country.getDocHospCity());
                i1.putExtra("DOC_HOSP_STATE",country.getDocHospState());
                i1.putExtra("DOC_HOSP_COUNTRY",country.getDocHospCountry());
                i1.putExtra("SPECIALIZATION_LIST", (Serializable) country.getDocSpecialization());
                i1.putExtra("HOSPITAL_LIST", (Serializable) country.getDocHospital());
                activity.startActivity(i1);
            }
        });

        holder.docShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder sb = new StringBuilder();
                for(int  i =0; i < country.getDocSpecialization().size(); i++)
                {
                    sb.append(country.getDocSpecialization().get(i).getSpecializationName().toString());
                    sb.append(", ");
                }

                StringBuilder sb1 = new StringBuilder();
                for(int  i =0; i < country.getDocHospital().size(); i++)
                {
                    sb1.append(country.getDocHospital().get(i).getHospitalName().toString()+", "+country.getDocHospital().get(i).getHospitalCity().toString());
                    sb1.append("\n");
                    doctor_hospitalID = country.getDocHospital().get(i).getHospitalId();
                }

                Intent sharingProfileIntent = new Intent(Intent.ACTION_SEND);
                sharingProfileIntent.setType("text/plain");
                sharingProfileIntent.putExtra(Intent.EXTRA_SUBJECT, "Pixel Care");
                sharingProfileIntent.putExtra(Intent.EXTRA_TEXT, country.getDocName()+"\n" + sb.toString() + "\n" + country.getDocCity() + "\n\n"+ "You can connect with doctor & also book an appointment by visiting the link below: "+"\n"+APIClass.BASE_URL+"SendRequestLink/?d="+country.getDocEncryptID()+"&hid="+md5(String.valueOf(doctor_hospitalID)));
                activity.startActivity(Intent.createChooser(sharingProfileIntent, "Share Doctor Profile Via"));

            }
        });

        holder.docAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(activity, BookAppointmentActivity.class);
                i1.putExtra("title","Book Appointment");
                i1.putExtra("DOC_ID",country.getDocId());
                i1.putExtra("DOC_NAME",country.getDocName());
                i1.putExtra("DOC_SPEC_ID",country.getDocSpecId());
                i1.putExtra("DOC_SPEC_NAME",country.getDocSpecName());
                i1.putExtra("DOC_QUALIFICATION",country.getDocQualification());
                i1.putExtra("DOC_CITY",country.getDocHospCity());
                i1.putExtra("DOC_ADDRESS",country.getDocHospAddress());
                i1.putExtra("DOC_PHOTO",country.getDocPhoto());
                i1.putExtra("SPECIALIZATION_LIST", (Serializable) country.getDocSpecialization());
                i1.putExtra("HOSPITAL_LIST", (Serializable) country.getDocHospital());
                activity.startActivity(i1);
            }
        });

        holder.docOpinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i2 = new Intent(activity, MedicalOpinionDocActivity.class);
                i2.putExtra("title","Medical Opinion");
                i2.putExtra("DOC_ID",country.getDocId());
                i2.putExtra("DOC_NAME",country.getDocName());
                i2.putExtra("DOC_SPEC_ID",country.getDocSpecId());
                i2.putExtra("DOC_SPEC_NAME",country.getDocSpecName());
                i2.putExtra("DOC_QUALIFICATION",country.getDocQualification());
                i2.putExtra("DOC_CITY",country.getDocHospCity());
                i2.putExtra("DOC_ADDRESS",country.getDocHospAddress());
                i2.putExtra("DOC_PHOTO",country.getDocPhoto());
                activity.startActivity(i2);*/

                Intent i2 = new Intent(activity, MedicalOpinionActivity.class);
                i2.putExtra("title","Medical Opinion");
                i2.putExtra("DOC_ID",country.getDocId());
                i2.putExtra("DOC_NAME",country.getDocName());
                activity.startActivity(i2);
            }
        });


        return convertView;
    }

    private static class ViewHolder {
        private CustomTextView docName, docSpec, docCity;
        private ImageView docImage, docFavourite;
        private CardView docCardView;
        private LinearLayout docAppointment, docOpinion, docShare;

        public ViewHolder(View v) {
            docName = (CustomTextView) v.findViewById(R.id.doc_list_name);
            docSpec = (CustomTextView) v.findViewById(R.id.doc_list_spec);
            docCity = (CustomTextView) v.findViewById(R.id.doc_list_city);
            docImage = (ImageView) v.findViewById(R.id.doc_list_image);
            docCardView = (CardView) v.findViewById(R.id.doc_list_cardview);
            docAppointment = (LinearLayout) v.findViewById(R.id.doc_list_appointment);
            docOpinion = (LinearLayout) v.findViewById(R.id.doc_list_opinion);
            docShare = (LinearLayout) v.findViewById(R.id.doc_list_share);
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
