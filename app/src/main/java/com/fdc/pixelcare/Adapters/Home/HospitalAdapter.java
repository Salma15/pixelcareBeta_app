package com.fdc.pixelcare.Adapters.Home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fdc.pixelcare.Activities.Home.HospitalDoctorsActivity;
import com.fdc.pixelcare.DataModel.HospitalList;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Views.CustomTextView;

import java.util.List;

/**
 * Created by SALMA on 21-12-2018.
 */
public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.MyViewHolder>  {
    private Context mContext;
    private List<HospitalList> hospitalListArraylist;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView hospName, hospAddress, hospState, hospCountry;
        private ImageView hospImage, starImage;
        private CardView hospCardView;

        public MyViewHolder(View v) {
            super(v);
            hospCardView = (CardView) v.findViewById(R.id.hosp_map_list_cardview);
            hospImage = (ImageView) v.findViewById(R.id.hosp_map_list_image);
            hospName = (CustomTextView) v.findViewById(R.id.hosp_map_list_name);
            hospAddress = (CustomTextView) v.findViewById(R.id.hosp_map_list_address);
            hospState = (CustomTextView) v.findViewById(R.id.hosp_map_list_state);
            hospCountry = (CustomTextView) v.findViewById(R.id.hosp_map_list_country);
            starImage = (ImageView) v.findViewById(R.id.home_map_list_star);
        }
    }

    public HospitalAdapter(Context context, List<HospitalList> hospList) {
        this.mContext = context;
        this.hospitalListArraylist = hospList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hospital_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HospitalList hosp = hospitalListArraylist.get(position);

        holder.hospName.setText(hosp.getHospitalName().trim());
        holder.hospAddress.setText(hosp.getHospitalAddress().trim()+"\n"+hosp.getHospitalCity().trim());
        holder.hospState.setText(hosp.getHospitalState().trim());
        holder.hospCountry.setText(hosp.getHospitalCountry().trim());

        if(hosp.getHospitalConsulted() ==1) {
            holder.starImage.setVisibility(View.VISIBLE);
        }
        else {
            holder.starImage.setVisibility(View.GONE);
        }

        holder.hospCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, " click: "+hosp.getHospitalId(), Toast.LENGTH_SHORT).show();
                Intent i1 = new Intent(mContext, HospitalDoctorsActivity.class);
                i1.putExtra("title", "View Doctors");
                i1.putExtra("HOSPITAL_ID", hosp.getHospitalId());
                mContext.startActivity(i1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hospitalListArraylist.size();
    }

}
