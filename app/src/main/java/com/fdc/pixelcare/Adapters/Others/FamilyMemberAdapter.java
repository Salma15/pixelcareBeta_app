package com.fdc.pixelcare.Adapters.Others;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fdc.pixelcare.DataModel.FamilyMember;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Views.CustomTextView;

import java.util.List;

/**
 * Created by SALMA on 31-12-2018.
 */
public class FamilyMemberAdapter extends RecyclerView.Adapter<FamilyMemberAdapter.ViewHolder> {

    private List<FamilyMember> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private String USER_NAME;

    // data is passed into the constructor
    public FamilyMemberAdapter(Context context, List<FamilyMember> data, String userName) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.USER_NAME = userName;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.family_member_list_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FamilyMember animal = mData.get(position);

        if(USER_NAME.equalsIgnoreCase(animal.getMemberName())) {
            holder.myTextView.setText(animal.getMemberName()+" (Self)");
        }
        else {
            holder.myTextView.setText(animal.getMemberName());
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CustomTextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvAnimalName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition(), mData.get(getAdapterPosition()).getMemberName(),mData.get(getAdapterPosition()).getMemberid(),mData.get(getAdapterPosition()).getGender(),mData.get(getAdapterPosition()).getMemberAge(),mData.get(getAdapterPosition()).getRelationship(),mData.get(getAdapterPosition()).getMemberDOB(),mData.get(getAdapterPosition()).getMemberPhoto());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData.get(id).getMemberName();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, String memberName, int memberId, String gender, String age, String relation, String dob, String image);
    }
}
