package com.fdc.pixelcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fdc.pixelcare.Activities.DashboardActivity;
import com.fdc.pixelcare.Activities.LoginActivity;
import com.fdc.pixelcare.Network.NetworkUtil;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    final Handler ha=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        shareadPreferenceClass = new ShareadPreferenceClass(getApplicationContext());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                if (NetworkUtil.getConnectivityStatusString(SplashActivity.this).equalsIgnoreCase("enabled")) {
                    startNextActivity();
                } else {
                    AppUtils.showCustomAlertMessage(SplashActivity.this, MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
                    ha.postDelayed(this, 15000);
                }

            }
        }, SPLASH_TIME_OUT);

    }

    private void startNextActivity() {

        if(sharedPreferences != null) {
            int user_id = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            String string_username = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");

            if ((string_username.length() > 0) && (user_id > 0)) {
                Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();

            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }
}
