package com.fdc.pixelcare.Adapters.Consultations;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fdc.pixelcare.DataModel.Investigations;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Views.CustomEditText;
import com.fdc.pixelcare.Views.CustomTextView;

import java.util.List;

/**
 * Created by SALMA on 01-01-2019.
 */

public class InvestigationAdapters extends RecyclerView.Adapter<InvestigationAdapters.MyViewHolder> {
    private List<Investigations> testList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView name, normal;
        public CustomEditText value_enter;
        public LinearLayout list_item;
        public ImageView delete_item;

        public MyViewHolder(View view) {
            super(view);
            name = (CustomTextView) view.findViewById(R.id.test_list_name);
            normal = (CustomTextView) view.findViewById(R.id.test_list_normal);
            value_enter = (CustomEditText) view.findViewById(R.id.test_list_value);
            list_item = (LinearLayout) view.findViewById(R.id.testlist_item);
            delete_item = (ImageView) view.findViewById(R.id.test_list_delete);
        }
    }

    public InvestigationAdapters(Context context, List<Investigations> testList) {
        this.mContext = context;
        this.testList = testList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Investigations list = testList.get(position);
        holder.name.setText(list.getTestName());
        holder.normal.setText(list.getNormalRange());
        holder.value_enter.setText(list.getTestActualValue());
        holder.delete_item.setVisibility(View.GONE);

        if(position%2 == 0) {
            holder.list_item.setBackgroundColor(mContext.getResources().getColor(R.color.row_even_color));
        }
        else {
            holder.list_item.setBackgroundColor(mContext.getResources().getColor(R.color.table_row_odd));
        }

        if(list.getNormalRange().equalsIgnoreCase("NULL")) {
            holder.normal.setText("");
        }
        else {
            holder.normal.setText(list.getNormalRange());
        }

        holder.value_enter.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(position!=-1 && position<testList.size()) {
                    if(testList.get(position).getInvestigationId() == list.getInvestigationId()) {
                        list.setTestActualValue(s.toString());
                    }
                }
            }
        });

     //   Log.d(Utils.TAG, "testID: " +list.getTestActualValue());
      //  holder.value_enter.setText(list.getTestActualValue());

        holder.list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                dialog1.setTitle("Test Information");
                dialog1.setMessage("Test Name: " + list.getTestName() +" \n\nTest Normal Value: "+list.getNormalRange());
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

        holder.delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                dialog.setTitle("Remove Test?");
                dialog.setMessage("Do you want to remove from test list?" );
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
        if(position!=-1 && position<testList.size())
        {
            testList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }


    @Override
    public int getItemCount() {
        return testList.size();
    }
}
