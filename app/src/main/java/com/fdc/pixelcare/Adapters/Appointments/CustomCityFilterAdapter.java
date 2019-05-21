package com.fdc.pixelcare.Adapters.Appointments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Views.CustomTextView;
import com.fdc.pixelcare.Views.CustomTextViewBold;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by SALMA on 22/12/18.
 */

public class CustomCityFilterAdapter extends ArrayAdapter<String> {


    Context mContext;
    private List<String> animalNamesList = null;
    private ArrayList<String> arraylist;
    LayoutInflater inflater;

    public CustomCityFilterAdapter(Context context, int resourceId,
                                   List<String> items) {
        super(context, resourceId, items);
        this.mContext = context;
        this.animalNamesList = items;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<String>();
        this.arraylist.addAll(animalNamesList);
    }

    /*private view holder class*/
    private class ViewHolder {
        CustomTextViewBold m_pic;
        CustomTextView m_title;
        LinearLayout m_layout;
    }

    public View getView(final int position, View convertView, ViewGroup parent)    {
        ViewHolder holder = null;
        String rowItem = getItem(position);

        //  LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.specialization_filter_listitem, null);
            holder = new ViewHolder();
            holder.m_title = (CustomTextView) convertView.findViewById(R.id.m_textview);
            holder.m_pic = (CustomTextViewBold) convertView.findViewById(R.id.m_imageview);
            holder.m_layout = (LinearLayout)  convertView.findViewById(R.id.m_btn);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        holder.m_title.setText(rowItem);

        if(rowItem != null ) {
            String test = rowItem.trim();
            String spec_image_text = String.valueOf(test.charAt(0));
            holder.m_pic.setText(spec_image_text);
        }

        return convertView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        animalNamesList.clear();
        if (charText.length() == 0) {
            animalNamesList.addAll(arraylist);
        } else {
            for (String wp : arraylist) {
                if (wp.toLowerCase(Locale.getDefault()).contains(charText)) {
                    animalNamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();

    }

}
