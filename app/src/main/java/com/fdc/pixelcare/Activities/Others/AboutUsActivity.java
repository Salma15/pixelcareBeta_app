package com.fdc.pixelcare.Activities.Others;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
import com.fdc.pixelcare.Views.CustomTextViewSemiBold;

/**
 * Created by SALMA on 31-12-2018.
 */

public class AboutUsActivity extends AppCompatActivity {
    public static final String REQUEST_TAG = "AboutUsActivity";

    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int USER_ID, USER_LOGINTYPE;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION;

    CustomTextViewSemiBold txt_verion;
    LinearLayout about_web, about_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            setTitle(title);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(AboutUsActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(AboutUsActivity.this);

        if(sharedPreferences !=null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE  = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);

            Log.d(AppUtils.TAG , " *********** AboutUsActivity **************** ");
            Log.d(AppUtils.TAG + " UserID: ", String.valueOf(USER_ID));
            Log.d(AppUtils.TAG + " name: ", USER_NAME);
            Log.d(AppUtils.TAG + " email: ", USER_EMAIL);
            Log.d(AppUtils.TAG + " MOBILE: ", USER_MOBILE);
            Log.d(AppUtils.TAG + " Location: ", USER_LOCATION);
        }

        initializationView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void initializationView() {
        txt_verion = (CustomTextViewSemiBold) findViewById(R.id.aboutus_versionname);
        about_web = (LinearLayout) findViewById(R.id.aboutus_website);
        about_email = (LinearLayout) findViewById(R.id.aboutus_email);

        PackageManager packageManager = getPackageManager();
        String packageName = getPackageName();

        String myVersionName = "1.0"; // initialize String

        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
            txt_verion.setText("Version "+myVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        about_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(about_web.getWindowToken(), 0);

                String url = "https://pixeleyecare.com/";

                try {
                    Uri webpage = Uri.parse(url);
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(AboutUsActivity.this, "No application can handle this request. Please install a web browser !!!",  Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        about_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm2 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(about_email.getWindowToken(), 0);

                String mailto = "mailto:medical@medisense.me" +
                        "?cc=" + "medisensedev@medisense.me" +
                        "&subject=" + Uri.encode("Contact Us - Pixel Care Android App") +
                        "&body=" + Uri.encode("Hi, its a help request for Pixel Care android application");

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));

                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    //TODO: Handle case where no email app is available
                    Toast.makeText(AboutUsActivity.this, "No application can handle this request. Please install an email application !!!",  Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
