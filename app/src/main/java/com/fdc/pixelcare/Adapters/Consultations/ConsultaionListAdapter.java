package com.fdc.pixelcare.Adapters.Consultations;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fdc.pixelcare.Activities.Consultations.ViewOphthalVisitDetailsActivity;
import com.fdc.pixelcare.DataModel.EpisodesList;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Views.CustomTextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by SALMA on 31-12-2018.
 */

public class ConsultaionListAdapter extends RecyclerView.Adapter<ConsultaionListAdapter.MyViewHolder> {

    private List<EpisodesList> episodeList;
    private Context mContext;
    private String MEMBER_NAME;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView episode_num, referredby_name, referred_date, followup_date, patient_name;
        public LinearLayout item_row;

        public MyViewHolder(View view) {
            super(view);
            episode_num = (CustomTextView) view.findViewById(R.id.track_episode_num);
            referredby_name = (CustomTextView) view.findViewById(R.id.track_referredby_name);
            referred_date = (CustomTextView) view.findViewById(R.id.track_referred_date);
            followup_date = (CustomTextView) view.findViewById(R.id.track_followup_date);
            item_row = (LinearLayout) view.findViewById(R.id.track_item_row);
            patient_name = (CustomTextView) view.findViewById(R.id.track_patient_name);
        }
    }

    public ConsultaionListAdapter(Context context, List<EpisodesList> epsList, String member_name) {
        this.mContext = context;
        this.episodeList = epsList;
        this.MEMBER_NAME = member_name;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.consultaion_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final EpisodesList eps = episodeList.get(position);

        int num_pos = position;
        num_pos = Integer.valueOf(episodeList.size()) - num_pos;
        holder.episode_num.setText("Visit:  #"+ String.valueOf(num_pos) +": ");
        holder.referredby_name.setText("By: "+eps.getEpisodeRefName());
        holder.patient_name.setText("Patient Name: "+eps.getEpisodePatientName());

        if(eps.getEpisodeCreatedDate().equals("")) { }
        else {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
            String inputDateStr= eps.getEpisodeCreatedDate();
            Date date = null;
            try {
                date = inputFormat.parse(inputDateStr);
                String outputDateStr = outputFormat.format(date);
                holder.referred_date.setText(outputDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        if(eps.getEpisodeFollowupDate().equals("")) {}
        else {
            DateFormat inputFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat1 = new SimpleDateFormat("dd MMM yyyy");
            String inputDateStr1= eps.getEpisodeFollowupDate();
            Date date1 = null;
            try {
                date1 = inputFormat1.parse(inputDateStr1);
                String outputDateStr1 = outputFormat1.format(date1);
                holder.followup_date.setText("Next Followup: "+ outputDateStr1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        holder.item_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(AppUtils.TAG +" emrType: ", String.valueOf(eps.getEMRType()));

                if(eps.getEMRType() == 1) {
                   /* Intent bundle2 = new Intent(mContext, ViewCardioVisitDetailsActivity.class);
                    bundle2.putExtra("title","Visit Details");
                    bundle2.putExtra("EPISODE_ID", eps.getEpisodeID());
                    bundle2.putExtra("PATIENT_ID", eps.getEpisodePatientID());
                    bundle2.putExtra("FOLLOWUP_DATE", eps.getEpisodeFollowupDate());
                    bundle2.putExtra("DIAGNOSIS_DETAILS", eps.getEpisodeDiagnosisDetails());
                    bundle2.putExtra("TREATMENT_DETAILS", eps.getEpisodeTreatmentDetails());
                    bundle2.putExtra("PRESCRIPTION_NOTES", eps.getEpisodePrescriptionNotes());
                    bundle2.putExtra("EPISODE_CREATED_DATE", eps.getEpisodeCreatedDate());
                    bundle2.putExtra("CONSULTATION_FEES", eps.getEpisodeConsultaionFees());
                    bundle2.putExtra("CHIEF_MEDICAL_LIST", (Serializable) eps.getChiefMedicalList());
                    bundle2.putExtra("INVESTIGATION_GENERAL_LIST",  (Serializable) eps.getInvestigationGeneralList());
                    bundle2.putExtra("INVESTIGATION_OPHTHAL_LIST", (Serializable) eps.getInvestigationOphthalList());
                    bundle2.putExtra("EXAMINATION_LIST", (Serializable) eps.getExaminationList());
                    bundle2.putExtra("DIAGNOSIS_LIST", (Serializable) eps.getDiagnosisList());
                    bundle2.putExtra("TREATMENT_LIST", (Serializable) eps.getTreatmentList());
                    bundle2.putExtra("PRESCRIPTION_LIST", (Serializable) eps.getPrescriptionList());
                    mContext.startActivity(bundle2);*/
                }
                else if(eps.getEMRType() == 2) {
                    Intent bundle2 = new Intent(mContext, ViewOphthalVisitDetailsActivity.class);
                    bundle2.putExtra("title","Visit Details");
                    bundle2.putExtra("EPISODE_ID", eps.getEpisodeID());
                    bundle2.putExtra("PATIENT_ID", eps.getEpisodePatientID());
                    bundle2.putExtra("FOLLOWUP_DATE", eps.getEpisodeFollowupDate());
                    bundle2.putExtra("DIAGNOSIS_DETAILS", eps.getEpisodeDiagnosisDetails());
                    bundle2.putExtra("TREATMENT_DETAILS", eps.getEpisodeTreatmentDetails());
                    bundle2.putExtra("PRESCRIPTION_NOTES", eps.getEpisodePrescriptionNotes());
                    bundle2.putExtra("EPISODE_CREATED_DATE", eps.getEpisodeCreatedDate());
                    bundle2.putExtra("CONSULTATION_FEES", eps.getEpisodeConsultaionFees());
                    bundle2.putExtra("CHIEF_MEDICAL_LIST", (Serializable) eps.getChiefMedicalList());
                    bundle2.putExtra("INVESTIGATION_GENERAL_LIST",  (Serializable) eps.getInvestigationGeneralList());
                    bundle2.putExtra("INVESTIGATION_OPHTHAL_LIST", (Serializable) eps.getInvestigationOphthalList());
                    bundle2.putExtra("DIAGNOSIS_LIST", (Serializable) eps.getDiagnosisList());
                    bundle2.putExtra("TREATMENT_LIST", (Serializable) eps.getTreatmentList());
                    bundle2.putExtra("PRESCRIPTION_LIST", (Serializable) eps.getPrescriptionList());
                    bundle2.putExtra("LIDS_LIST", (Serializable) eps.getLidsList());
                    bundle2.putExtra("CONJUCTIVA_LIST", (Serializable) eps.getConjuctivaList());
                    bundle2.putExtra("SCLERA_LIST", (Serializable) eps.getScleraList());
                    bundle2.putExtra("CORNEA_ANTERIOR_LIST", (Serializable) eps.getCorneaAnteriorList());
                    bundle2.putExtra("CORNEA_POSTERIOR_LIST", (Serializable) eps.getCorneaPosteriorList());
                    bundle2.putExtra("ANTERIOR_CHAMBER_LIST", (Serializable) eps.getAnteriorChamberList());
                    bundle2.putExtra("IRIS_LIST", (Serializable) eps.getIrisList());
                    bundle2.putExtra("PUPIL_LIST", (Serializable) eps.getPupilList());
                    bundle2.putExtra("ANGLE_LIST", (Serializable) eps.getAngleList());
                    bundle2.putExtra("LENS_LIST", (Serializable) eps.getLensList());
                    bundle2.putExtra("VITERIOUS_LIST", (Serializable) eps.getViterousList());
                    bundle2.putExtra("FUNDUS_LIST", (Serializable) eps.getFundusList());
                    bundle2.putExtra("REFRACTION_RE_VALUE1",  eps.getEpisodeRefractionRE1());
                    bundle2.putExtra("REFRACTION_RE_VALUE2",  eps.getEpisodeRefractionRE2());
                    bundle2.putExtra("REFRACTION_LE_VALUE1",  eps.getEpisodeRefractionLE1());
                    bundle2.putExtra("REFRACTION_LE_VALUE2",  eps.getEpisodeRefractionLE2());
                    bundle2.putExtra("DISTANCE_VISION_RE", eps.getEpisodeDistanceVisionRE());
                    bundle2.putExtra("DISTANCE_VISION_LE",  eps.getEpisodeDistanceVisionLE());
                    bundle2.putExtra("NEAR_VISION_RE", eps.getEpisodeNearVisionRE());
                    bundle2.putExtra("NEAR_VISION_LE",  eps.getEpisodeNearVisionLE());
                    bundle2.putExtra("DV_SPHERE_RIGHT",  eps.getEpisodeDvSphereRE());
                    bundle2.putExtra("DV_CYCLE_RIGHT", eps.getEpisodeDvCycleRE());
                    bundle2.putExtra("DV_AXIS_RIGHT",  eps.getEpisodeDvAxisRE());
                    bundle2.putExtra("NV_SPHERE_RIGHT",  eps.getEpisodeNvSphereRE());
                    bundle2.putExtra("NV_CYCLE_RIGHT", eps.getEpisodeNvCycleRE());
                    bundle2.putExtra("NV_AXIS_RIGHT",  eps.getEpisodeNvAxisRE());
                    bundle2.putExtra("IPD_RIGHT", eps.getEpisodeIPDRE());
                    bundle2.putExtra("DV_SPHERE_LEFT", eps.getEpisodeDvSphereLE());
                    bundle2.putExtra("DV_CYCLE_LEFT", eps.getEpisodeDvCycleLE());
                    bundle2.putExtra("DV_AXIS_LEFT", eps.getEpisodeDvAxisLE());
                    bundle2.putExtra("NV_SPHERE_LEFT", eps.getEpisodeNvSphereLE());
                    bundle2.putExtra("NV_CYCLE_LEFT", eps.getEpisodeNvCycleLE());
                    bundle2.putExtra("NV_AXIS_LEFT", eps.getEpisodeNvAxisLE());
                    bundle2.putExtra("IPD_LEFT", eps.getEpisodeIPDLE());
                    mContext.startActivity(bundle2);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }
}
