package com.fdc.pixelcare.Adapters.Home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fdc.pixelcare.Activities.Appointments.BookAppointmentActivity;
import com.fdc.pixelcare.Activities.Appointments.DoctorsProfileActivity;
import com.fdc.pixelcare.DataModel.DoctorList;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Views.CustomTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Created by SALMA on 22-12-2018.
 */
public class ConsultedDoctorsAdapter extends RecyclerView.Adapter<ConsultedDoctorsAdapter.MyViewHolder>   {
    private Context mContext;
    private List<DoctorList> doctorsConsultedListArraylist;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView docName, docSpec, docCity;
        private ImageView docImage, docStarImg;
        private CardView docCardView;
        private LinearLayout docAppointment, docOpinion, docShare;

        public MyViewHolder(View v) {
            super(v);
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


    public ConsultedDoctorsAdapter(Context context, List<DoctorList> docList) {
        this.mContext = context;
        this.doctorsConsultedListArraylist = docList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.consulted_doctors_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DoctorList docs = doctorsConsultedListArraylist.get(position);

        holder.docName.setText(docs.getDocName());
        holder.docOpinion.setVisibility(View.GONE);

        StringBuilder sb = new StringBuilder();
        for(int  i =0; i < docs.getDocSpecialization().size(); i++)
        {
            sb.append(docs.getDocSpecialization().get(i).getSpecializationName().toString());
            sb.append(", ");
        }
        holder.docSpec.setText(sb.toString().substring(0, sb.toString().length() - 2) );

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
            Picasso.with(mContext).load(String.valueOf(url))
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
                Intent i1 = new Intent(mContext, DoctorsProfileActivity.class);
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
                mContext.startActivity(i1);
            }
        });

        holder.docShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingProfileIntent = new Intent(Intent.ACTION_SEND);
                sharingProfileIntent.setType("text/plain");
                sharingProfileIntent.putExtra(Intent.EXTRA_SUBJECT, "Medisense");
                sharingProfileIntent.putExtra(Intent.EXTRA_TEXT, docs.getDocName()+"\n" + docs.getDocSpecName() + "\n" + docs.getDocCity() + "\n\n"+ "You can connect with doctor & also book an appointment by visiting the link below: "+"\n"+"https://medisensecrm.com/SendRequestLink/RefLink?d="+docs.getDocEncryptID());
                mContext.startActivity(Intent.createChooser(sharingProfileIntent, "Share Doctor Profile Via"));

            }
        });

        holder.docAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(mContext, BookAppointmentActivity.class);
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
                mContext.startActivity(i1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctorsConsultedListArraylist.size();
    }

}
