package com.fdc.pixelcare.Adapters.Home;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.fdc.pixelcare.Activities.Home.HospitalDoctorsActivity;
import com.fdc.pixelcare.DataModel.HospitalList;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Views.CustomTextView;

import java.util.List;

/**
 * Created by SALMA on 21-12-2018.
 */
public class HospitalListAdapter extends ArrayAdapter<HospitalList> {
    private Activity activity;

    public HospitalListAdapter(Activity activity, int resource, int textViewResourceId, List<HospitalList> hospitals) {
        super(activity, resource, textViewResourceId, hospitals);
        this.activity = activity;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.hospital_list_item, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        }  else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        final HospitalList hosp = getItem(position);

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
              //  Toast.makeText(mContext, " click: "+hosp.getHospitalId(), Toast.LENGTH_SHORT).show();
               // Log.d(AppUtils.TAG, " hospID: "+ hosp.getHospitalId());
                Intent i1 = new Intent(activity, HospitalDoctorsActivity.class);
                i1.putExtra("title", "View Doctors");
                i1.putExtra("HOSPITAL_ID", hosp.getHospitalId());
                activity.startActivity(i1);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        private CustomTextView hospName, hospAddress, hospState, hospCountry;
        private ImageView hospImage, starImage;
        private CardView hospCardView;

        public ViewHolder(View v) {
            hospCardView = (CardView) v.findViewById(R.id.hosp_map_list_cardview);
            hospImage = (ImageView) v.findViewById(R.id.hosp_map_list_image);
            hospName = (CustomTextView) v.findViewById(R.id.hosp_map_list_name);
            hospAddress = (CustomTextView) v.findViewById(R.id.hosp_map_list_address);
            hospState = (CustomTextView) v.findViewById(R.id.hosp_map_list_state);
            hospCountry = (CustomTextView) v.findViewById(R.id.hosp_map_list_country);
            starImage = (ImageView) v.findViewById(R.id.home_map_list_star);
        }
    }
}

