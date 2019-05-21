package com.fdc.pixelcare.Adapters.HomeOld;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fdc.pixelcare.Activities.Appointments.DoctorsProfileActivity;
import com.fdc.pixelcare.DataModel.DoctorList;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Views.CustomTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Created by salma on 28/03/19.
 */

public class HomeDoctorListAdapter extends RecyclerView.Adapter<HomeDoctorListAdapter.GroceryViewHolder> {
    private List<DoctorList> horizontalGrocderyList;
    Context context;

    public HomeDoctorListAdapter(List<DoctorList> horizontalGrocderyList, Context context) {
        this.horizontalGrocderyList = horizontalGrocderyList;
        this.context = context;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_doctorlist_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
    //    holder.imageView.setImageResource(horizontalGrocderyList.get(position).getDocSpecId());

        if(horizontalGrocderyList.get(position).getDocPhoto().equals("")) {
            holder.imageView.setImageResource(R.drawable.user_profile);
        }
        else {
            String DOWNLOAD_PROFILE = APIClass.DRS_DOCTOR_PROFILE_URL+String.valueOf(horizontalGrocderyList.get(position).getDocId())+"/"+horizontalGrocderyList.get(position).getDocPhoto().trim();
            Log.d(AppUtils.TAG +" PROFILE: ", DOWNLOAD_PROFILE);
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
            Picasso.with(context).load(String.valueOf(url))
                    .placeholder(R.drawable.user_profile)
                    .error(R.drawable.user_profile)
                    .resize(200, 200)
                    .centerInside()
                    .into(holder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }

        holder.txtview.setText(horizontalGrocderyList.get(position).getDocName());
      //  holder.txtSpec.setText(horizontalGrocderyList.get(position).getDocSpecName());
        holder.txtCity.setText(horizontalGrocderyList.get(position).getDocCity());

        StringBuilder sb = new StringBuilder();
        if(horizontalGrocderyList.get(position).getDocSpecialization().size() >0) {
            for (int i = 0; i < horizontalGrocderyList.get(position).getDocSpecialization().size(); i++) {
                sb.append(horizontalGrocderyList.get(position).getDocSpecialization().get(i).getSpecializationName().toString());
                sb.append(", ");
            }
            if (sb.toString().length() > 2) {
                holder.txtSpec.setText(sb.toString().substring(0, sb.toString().length() - 2));
            } else {
                holder.txtSpec.setText(sb.toString());
            }
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = horizontalGrocderyList.get(position).getDocSpecName().toString();
               // Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
                Intent i1 = new Intent(context, DoctorsProfileActivity.class);
                i1.putExtra("title","Doctor Profile");
                i1.putExtra("DOC_ID",horizontalGrocderyList.get(position).getDocId());
                i1.putExtra("DOC_NAME",horizontalGrocderyList.get(position).getDocName());
                i1.putExtra("DOC_QUALIFICATION",horizontalGrocderyList.get(position).getDocQualification());
                i1.putExtra("DOC_EXPERIENCE",horizontalGrocderyList.get(position).getDocExperience());
                i1.putExtra("DOC_CITY",horizontalGrocderyList.get(position).getDocCity());
                i1.putExtra("DOC_SPEC_ID",horizontalGrocderyList.get(position).getDocSpecId());
                i1.putExtra("DOC_SPEC_NAME",horizontalGrocderyList.get(position).getDocSpecName());
                i1.putExtra("DOC_ENCRYPT_ID",horizontalGrocderyList.get(position).getDocEncryptID());
                i1.putExtra("DOC_AREA_INTEREST",horizontalGrocderyList.get(position).getDocAreaInterest());
                i1.putExtra("DOC_PHOTO",horizontalGrocderyList.get(position).getDocPhoto());
                i1.putExtra("DOC_HOSP_NAME",horizontalGrocderyList.get(position).getDocHospName());
                i1.putExtra("DOC_HOSP_ADDRESS",horizontalGrocderyList.get(position).getDocHospAddress());
                i1.putExtra("DOC_HOSP_CITY",horizontalGrocderyList.get(position).getDocHospCity());
                i1.putExtra("DOC_HOSP_STATE",horizontalGrocderyList.get(position).getDocHospState());
                i1.putExtra("DOC_HOSP_COUNTRY",horizontalGrocderyList.get(position).getDocHospCountry());
                context.startActivity(i1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CustomTextView txtview, txtSpec, txtCity;
        LinearLayout linearLayout;

        public GroceryViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.home_doclist_image);
            txtview = view.findViewById(R.id.home_doclist_name);
            txtSpec = view.findViewById(R.id.home_doclist_specialty);
            txtCity = view.findViewById(R.id.home_doclist_city);
            linearLayout = view.findViewById(R.id.home_doclist_layout);
        }
    }
}
