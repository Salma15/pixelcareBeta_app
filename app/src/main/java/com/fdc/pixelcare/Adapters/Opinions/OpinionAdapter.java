package com.fdc.pixelcare.Adapters.Opinions;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fdc.pixelcare.Activities.Opinions.OpinionDetailsActivity;
import com.fdc.pixelcare.DataModel.OpinionList;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Views.CustomTextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by SALMA on 31-12-2018.
 */

public class OpinionAdapter extends RecyclerView.Adapter<OpinionAdapter.MyViewHolder>  {
    private List<OpinionList> patientList;
    Context mContext;
    Date status_date;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView pName, pAge, pCity, pStatus, pDocName, pID, pStatusDate, pStatusLabel;
        public CardView cardViewPatient;
        RelativeLayout plist;
        public ImageView doc_icon;

        public MyViewHolder(View view) {
            super(view);
            pName = (CustomTextView) view.findViewById(R.id.edit_patient_name);
            pCity = (CustomTextView) view.findViewById(R.id.edit_patient_city);
            pStatus = (CustomTextView) view.findViewById(R.id.edit_patient_status);
            cardViewPatient = (CardView) view.findViewById(R.id.patient_list_cardview);
            pDocName = (CustomTextView) view.findViewById(R.id.edit_patient_docname);
            pID = (CustomTextView) view.findViewById(R.id.edit_patient_id);
            pStatusDate = (CustomTextView) view.findViewById(R.id.edit_patient_refondate);
        }
    }

    public OpinionAdapter(Context context, List<OpinionList> psList) {
        this.patientList = psList;
        this.mContext = context;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.opinion_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final OpinionList patient = patientList.get(position);
        String status = null;
        holder.pName.setText(patient.getPatientName().trim());

        if(patient.getPatientCity().equals("")) {
            holder.pCity.setVisibility(View.GONE);
        }
        else {
            holder.pCity.setText(patient.getPatientCity().trim());
        }

        holder.pDocName.setText(patient.getPatientDocName().trim());
        holder.pID.setText("Reg ID: "+String.valueOf(patient.getPatientId()));

        final int patient_id = patient.getPatientId();
        final int doc_refid = patient.getPatientDocId();

        if(patient.getPatientStatus() == 1){
            status = "New";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_safron);
        }
        else if(patient.getPatientStatus() == 2){
            status = "Pending";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_yellow);
        }
        else if(patient.getPatientStatus() == 3){
            status = "Awaiting";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_red);
        }
        else if(patient.getPatientStatus() == 4 ){
            status = "Not Qualified";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_red);
        }
        else if(patient.getPatientStatus() == 5){
            status = "Responded";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_green);
        }
        else if(patient.getPatientStatus() == 6){
            status = "Response P-Failed";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_green);
        }
        else if(patient.getPatientStatus() == 7){
            status = "Staged";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_green);
        }
        else if(patient.getPatientStatus() == 8){
            status = "OP Desired";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_green);
        }
        else if(patient.getPatientStatus() == 9){
            status = "IP Treated";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_green);
        }
        else if(patient.getPatientStatus() == 10){
            status = "Not Converted";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_red);
        }
        else if(patient.getPatientStatus() == 11){
            status = "Invoiced";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_safron);
        }
        else if(patient.getPatientStatus() == 12){
            status = "Payment Received";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_safron);
        }
        else if(patient.getPatientStatus() == 13){
            status = "OP Visited";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_green);
        }
        else if(patient.getPatientStatus() == 14){
            status = "Not Responded";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_red);
        }

        holder.pStatus.setText(status);

        String[] status_date_arrray = patient.getPatientStatusTime().split(" ");
        status_date_arrray[0] = status_date_arrray[0].replace("-", "/");

        DateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String inputDateStr= status_date_arrray[0];
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
            String outputDateStr = outputFormat.format(date);
            holder.pStatusDate.setText(outputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.pDocName.setText(patient.getPatientDocName().trim());

        holder.cardViewPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(mContext, OpinionDetailsActivity.class);
                i3.putExtra("title","Opinion Details");
                i3.putExtra("PATIENT_ID", patient.getPatientId());
                i3.putExtra("PATIENT_NAME", patient.getPatientName());
                i3.putExtra("PATIENT_DOC_NAME", patient.getPatientDocName());
                i3.putExtra("PATIENT_GENDER", patient.getPatientGender());
                i3.putExtra("PATIENT_AGE", patient.getPatientAge());
                i3.putExtra("PATIENT_WEIGHT",patient.getPatientWeight());
                i3.putExtra("PATIENT_HYPERTESNSION",patient.getPatientHyperCondition());
                i3.putExtra("PATIENT_DIABETES",patient.getPatientDiabetesCondition());
                i3.putExtra("PATIENT_ADDRESS",patient.getPatientAddress());
                i3.putExtra("PATIENT_CITY",patient.getPatientCity());
                i3.putExtra("PATIENT_STATE",patient.getPatientState());
                i3.putExtra("PATIENT_COUNTRY",patient.getPatientCountry());
                i3.putExtra("PATIENT_MEDICAL_COMPALINT",patient.getPatientMedicalComplaint());
                i3.putExtra("PATIENT_DESCRIPTION",patient.getPatientDescription());
                i3.putExtra("PATIENT_QUERY_DOC",patient.getPatientQueryToDoc());
                i3.putExtra("PATIENT_MOBILE",patient.getPatientMobile());
                i3.putExtra("PATIENT_EMAIL",patient.getPatientEmail());
                i3.putExtra("PATIENT_STATUS",patient.getPatientStatus());

                mContext.startActivity(i3);
            }
        });

    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

}
