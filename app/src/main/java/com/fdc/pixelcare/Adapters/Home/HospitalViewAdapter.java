package com.fdc.pixelcare.Adapters.Home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fdc.pixelcare.Activities.Home.HospitalDoctorsActivity;
import com.fdc.pixelcare.DataModel.HospitalList;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Views.CustomTextView;
import com.fdc.pixelcare.Views.CustomTextViewSemiBold;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Created by SALMA on 22-10-2018.
 */
public class HospitalViewAdapter extends RecyclerView.Adapter<HospitalViewAdapter.MyViewHolder>  {
    private Context mContext;
    private List<HospitalList> hospitalListArraylist;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView hosp_image, star_image;
        public CustomTextViewSemiBold hosp_name;
        public CustomTextView hosp_city;
        public LinearLayout item_row;

        public MyViewHolder(View view) {
            super(view);
            star_image = (ImageView) view.findViewById(R.id.hosp_view_star);
            hosp_image = (ImageView) view.findViewById(R.id.hosp_view_image);
            hosp_name = (CustomTextViewSemiBold) view.findViewById(R.id.hosp_view_name);
            hosp_city = (CustomTextView) view.findViewById(R.id.hosp_view_city);
            item_row = (LinearLayout) view.findViewById(R.id.hosp_view_item);
        }
    }

    public HospitalViewAdapter(Context context, List<HospitalList> hospList) {
        this.mContext = context;
        this.hospitalListArraylist = hospList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_hospital_view_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HospitalList hosp = hospitalListArraylist.get(position);
        holder.hosp_name.setText(hosp.getHospitalName());
        holder.hosp_city.setText(hosp.getHospitalCity());

        if(hosp.getHospitalConsulted() == 1) {
            holder.star_image.setVisibility(View.VISIBLE);
        }
        else {
            holder.star_image.setVisibility(View.GONE);
        }

        holder.item_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i1 = new Intent(mContext, HospitalDoctorsActivity.class);
                i1.putExtra("title", "View Doctors");
                i1.putExtra("HOSPITAL_ID", hosp.getHospitalId());
                mContext.startActivity(i1);
            }
        });

       /* if(hosp.getHospitalPhoto().equals("")) {
            holder.hosp_image.setImageResource(R.drawable.doctor_icon);
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
                    .placeholder(R.mipmap.hospital_icon)
                    .error(R.mipmap.hospital_icon)
                    .into(holder.hosp_image, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }*/
    }

    @Override
    public int getItemCount() {
        return hospitalListArraylist.size();
    }

}
