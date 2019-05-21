package com.fdc.pixelcare.Adapters.Consultations;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fdc.pixelcare.DataModel.DrugAllery;
import com.fdc.pixelcare.DataModel.FrequentPrescription;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
import com.fdc.pixelcare.Views.CustomTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SALMA on 01-01-2019.
 */

public class PrescriptionViewAdapters  extends RecyclerView.Adapter<PrescriptionViewAdapters.MyViewHolder>  {
    private List<FrequentPrescription> prescList;
    private Context mContext;
    List<DrugAllery> drugAlleryListArraylist;
    int USER_ID;
    String USER_NAME,USER_LOGIN_TYPE;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView trade_name, generic_name;
        public CustomTextView dosage;
        public LinearLayout list_item;
        public ImageView delete_item, danger_icon;

        public MyViewHolder(View view) {
            super(view);
            trade_name = (CustomTextView) view.findViewById(R.id.presc_trade_name);
            generic_name = (CustomTextView) view.findViewById(R.id.presc_gen_name);
            dosage = (CustomTextView) view.findViewById(R.id.presc_dosage);
            list_item = (LinearLayout) view.findViewById(R.id.presc_list_item);
            delete_item = (ImageView) view.findViewById(R.id.presc_list_delete);
            danger_icon = (ImageView) view.findViewById(R.id.presc_danger_image);
        }
    }

    public PrescriptionViewAdapters(Context context, List<FrequentPrescription> prescList) {
        this.mContext = context;
        this.prescList = prescList;
        this.drugAlleryListArraylist = new ArrayList<>();
        this.shareadPreferenceClass = new ShareadPreferenceClass(context);
        this.sharedPreferences = shareadPreferenceClass.getSharedPreferences(context);
        if(sharedPreferences !=null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_LOGIN_TYPE = "";
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prescription_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final FrequentPrescription list = prescList.get(position);

        if(position%2 == 0) {
            holder.list_item.setBackgroundColor(mContext.getResources().getColor(R.color.row_even_color));
        }
        else {
            holder.list_item.setBackgroundColor(mContext.getResources().getColor(R.color.table_row_odd));
        }

       /* List<DrugAllery> freqAllergy = MedisensePracticeDB.getAllDrugAllery(mContext, USER_LOGIN_TYPE, USER_ID);
        if(freqAllergy.size() > 0) {
            drugAlleryListArraylist = new ArrayList<>();
            for (int j = 0; j < freqAllergy.size(); j++) {
                drugAlleryListArraylist.add(new DrugAllery(freqAllergy.get(j).getAllergyId(),freqAllergy.get(j).getGenericId(),
                        freqAllergy.get(j).getGenericName(),freqAllergy.get(j).getPatientId(),
                        freqAllergy.get(j).getDocId(),freqAllergy.get(j).getDocType(),
                        freqAllergy.get(j).getUserId(),freqAllergy.get(j).getLoginType()));

                if(freqAllergy.get(j).getGenericId() == list.getGenericId()) {
                    holder.danger_icon.setVisibility(View.VISIBLE);

                    Animation animation = new AlphaAnimation(1, 0);
                    animation.setDuration(1500);
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setRepeatCount(Animation.INFINITE);
                    animation.setRepeatMode(Animation.REVERSE);
                    holder.danger_icon.startAnimation(animation);
                }
                else {
                    holder.danger_icon.setVisibility(View.GONE);
                }

            }
            Log.d(Utils.TAG, "total freqAllergy: " + freqAllergy.size());
        }*/

        holder.trade_name.setText(list.getTradeName());
        holder.generic_name.setText(list.getGenericName());
        holder.dosage.setText(list.getDosage());

        holder.list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                dialog1.setTitle("Prescription Information");
                dialog1.setMessage("Product ID: " + list.getPrescriptionId() +"\n\nTrade Name: "+ list.getTradeName()
                        +"\n\nGeneric Name: "+ list.getTradeName()+"\n\nFrequency: "+ list.getDosage()
                        +"\n\nTimings: "+ list.getTimings()+"\n\nDuration: "+ list.getDuration());
                dialog1.setPositiveButton("Done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                final AlertDialog alert = dialog1.create();
                alert.show();
            }
        });


        holder.delete_item.setVisibility(View.INVISIBLE);

        holder.delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                dialog.setTitle("Remove Prescription?");
                dialog.setMessage("Do you want to remove from Prescription list?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        onItemDismiss(position);
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Action for "Cancel".
                                dialog.cancel();
                            }
                        });

                final AlertDialog alert = dialog.create();
                alert.show();

            }
        });
    }

    public void onItemDismiss(int position) {
        if (position != -1 && position < prescList.size()) {
            prescList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }


    @Override
    public int getItemCount() {
        return prescList.size();
    }
}