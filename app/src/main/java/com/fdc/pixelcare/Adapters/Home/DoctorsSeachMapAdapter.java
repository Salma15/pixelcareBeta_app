package com.fdc.pixelcare.Adapters.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.fdc.pixelcare.DataModel.DoctorList;
import com.fdc.pixelcare.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SALMA on 21-12-2018.
 */
public class DoctorsSeachMapAdapter extends ArrayAdapter<DoctorList> {
    Context context;
    int resource, textViewResourceId;
    List<DoctorList> items, tempItems, suggestions;

    public DoctorsSeachMapAdapter(Context context, int resource, int textViewResourceId, List<DoctorList> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<DoctorList>(items); // this makes the difference.
        suggestions = new ArrayList<DoctorList>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_search_map_item, parent, false);
        }
        DoctorList people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(people.getDocName());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((DoctorList) resultValue).getDocName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (DoctorList people : tempItems) {
                    if (people.getDocName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<DoctorList> filterList = (ArrayList<DoctorList>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (DoctorList people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
