package com.fdc.pixelcare.Activities.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fdc.pixelcare.Activities.LoginActivity;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;

/**
 * Created by salma on 20/12/18.
 */

public class SignOutFragment extends Fragment {

    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;

    FragmentManager fragManager;
    FragmentTransaction fragTransaction;

    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, vg, false);

        shareadPreferenceClass = new ShareadPreferenceClass(getActivity());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getActivity());

        if (sharedPreferences != null) {
            shareadPreferenceClass.clearData();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }
        return root;
    }

}
