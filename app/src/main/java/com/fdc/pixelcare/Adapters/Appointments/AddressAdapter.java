package com.fdc.pixelcare.Adapters.Appointments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.fdc.pixelcare.Activities.Appointments.BookAppointmentActivity;
import com.fdc.pixelcare.DataModel.UserAddress;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Views.CustomTextView;
import com.fdc.pixelcare.Views.CustomTextViewBold;

import java.util.List;

/**
 * Created by salma on 21/12/18.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    private List<UserAddress> addressList;
    private int lastSelectedPosition = -1;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextViewBold address;
        public CustomTextView city, pincode, state, country;
        public RadioButton address_radio;

        public MyViewHolder(View view) {
            super(view);
            address = (CustomTextViewBold) view.findViewById(R.id.text_address);
            city = (CustomTextView) view.findViewById(R.id.text_city);
            pincode = (CustomTextView) view.findViewById(R.id.text_pincode);
            state = (CustomTextView) view.findViewById(R.id.text_state);
            country = (CustomTextView) view.findViewById(R.id.text_country);
            address_radio = (RadioButton) view.findViewById(R.id.radio_address_select);

            address_radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                    ((BookAppointmentActivity) mContext).onClickCalled(addressList.get(lastSelectedPosition).getAddressName(), addressList.get(lastSelectedPosition).getCity(),  addressList.get(lastSelectedPosition).getPincode(), addressList.get(lastSelectedPosition).getState(), addressList.get(lastSelectedPosition).getCountry());

                }
            });
        }
    }


    public AddressAdapter(Context context, List<UserAddress> addrsList) {
        this.mContext = context;
        this.addressList = addrsList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        UserAddress addr = addressList.get(position);
        holder.address.setText(addr.getAddressName());
        holder.city.setText(addr.getCity());
        holder.pincode.setText(addr.getPincode());
        holder.state.setText(addr.getState());
        holder.country.setText(addr.getCountry());

        holder.address_radio.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

}
