package com.fdc.pixelcare.multipleImageUpload;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fdc.pixelcare.Activities.Opinions.MedicalOpinionActivity;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppUtils;

import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by SALMA on 31/12/18.
 */

public class MedicalReportsActivity extends AppCompatActivity {
    public ArrayList<String> REPORTS_PHOTOS = new ArrayList<String>();

    private ImageAdapter imageAdapter;
    private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;
    ArrayList<String> selectedItems;

    String CONTACT_PERSON, MOBILE_NUM, EMAIL_ID, ADDRESS, CITY, STATE, COUNTRY, AGE, WEIGHT, MEDICAL_COMPLAINT, BRIEF_DESCRIPTION, QUERY_DOCTOR;
    int SPECIALIZATION_ID = 0, DOCTOR_ID;
    String SPECIALIZATION_NAME = "", DOCTOR_NAME;
    String PATIENT_HYPERTENSION = "0", PATIENT_DIABETES = "0";
    String PATIENT_NAME= "", PATIENT_GENDER = "0";
    int PATIENT_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_multi_photo_select);

        REPORTS_PHOTOS = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle b = getIntent().getExtras();
        if(b!=null) {
            String title = b.getString("title");
            setTitle(title);

            DOCTOR_ID = b.getInt("DOC_ID");
            DOCTOR_NAME = b.getString("DOC_NAME");
            CONTACT_PERSON = b.getString("CONTACT_PERSON");
            MOBILE_NUM = b.getString("MOBILE_NUM");
            EMAIL_ID = b.getString("EMAIL_ID");
            ADDRESS = b.getString("ADDRESS");
            CITY = b.getString("CITY");
            STATE = b.getString("STATE");
            COUNTRY = b.getString("COUNTRY");
            PATIENT_NAME = b.getString("PATIENT_NAME");
            PATIENT_ID = b.getInt("PATIENT_ID");
            PATIENT_GENDER = b.getString("PATIENT_GENDER");
            AGE = b.getString("AGE");
            WEIGHT = b.getString("WEIGHT");
            PATIENT_HYPERTENSION = b.getString("PATIENT_HYPERTENSION");
            PATIENT_DIABETES = b.getString("PATIENT_DIABETES");
            SPECIALIZATION_ID = b.getInt("SPECIALIZATION_ID");
            SPECIALIZATION_NAME = b.getString("SPECIALIZATION_NAME");
            MEDICAL_COMPLAINT = b.getString("MEDICAL_COMPLAINT");
            BRIEF_DESCRIPTION = b.getString("BRIEF_DESCRIPTION");
            QUERY_DOCTOR = b.getString("QUERY_DOCTOR");
            REPORTS_PHOTOS = b.getStringArrayList("REPORTS_PHOTOS");

            Log.d(AppUtils.TAG , " ****** uPLOAD rePORTS ********* ");
            Log.d(AppUtils.TAG + " PATIENT_NAME: ", PATIENT_NAME + " ID: "+ String.valueOf(PATIENT_ID));

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
        populateImagesFromGallery();

        Button select_image = (Button) findViewById(R.id.button1);
        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChoosePhotosClick();
            }
        });
    }

    public void btnChoosePhotosClick(){

        selectedItems = imageAdapter.getCheckedItems();

        if (selectedItems!= null && selectedItems.size() > 0) {

            for(int i=0; i<selectedItems.size(); i++) {
                // GET_NEONATES_PHOTOS.add(selectedItems.get(i).toString());
                if(REPORTS_PHOTOS!=null) {
                    REPORTS_PHOTOS.add(selectedItems.get(i).toString());
                }
                else {
                    REPORTS_PHOTOS = new ArrayList<String>();
                    REPORTS_PHOTOS.add(selectedItems.get(i).toString());
                }
            }


           // Toast.makeText(MedicalReportsActivity.this,  selectedItems.size() + " Reports Selected", Toast.LENGTH_SHORT).show();

            Intent i1 = new Intent(MedicalReportsActivity.this, MedicalOpinionActivity.class);
            i1.putExtra("title","Medical Opinion");
            i1.putExtra("DOC_ID",DOCTOR_ID);
            i1.putExtra("DOC_NAME",DOCTOR_NAME);
            i1.putExtra("CONTACT_PERSON",CONTACT_PERSON);
            i1.putExtra("MOBILE_NUM",MOBILE_NUM);
            i1.putExtra("EMAIL_ID",EMAIL_ID);
            i1.putExtra("ADDRESS",ADDRESS);
            i1.putExtra("CITY",CITY);
            i1.putExtra("STATE",STATE);
            i1.putExtra("COUNTRY",COUNTRY);
            i1.putExtra("PATIENT_NAME",PATIENT_NAME);
            i1.putExtra("PATIENT_ID",PATIENT_ID);
            i1.putExtra("PATIENT_GENDER",PATIENT_GENDER);
            i1.putExtra("AGE",AGE);
            i1.putExtra("WEIGHT",WEIGHT);
            i1.putExtra("PATIENT_HYPERTENSION",PATIENT_HYPERTENSION);
            i1.putExtra("PATIENT_DIABETES",PATIENT_DIABETES);
            i1.putExtra("SPECIALIZATION_ID",SPECIALIZATION_ID);
            i1.putExtra("SPECIALIZATION_NAME",SPECIALIZATION_NAME);
            i1.putExtra("MEDICAL_COMPLAINT",MEDICAL_COMPLAINT);
            i1.putExtra("BRIEF_DESCRIPTION",BRIEF_DESCRIPTION);
            i1.putExtra("QUERY_DOCTOR",QUERY_DOCTOR);
            i1.putStringArrayListExtra("REPORTS_PHOTOS",REPORTS_PHOTOS);
            startActivity(i1);
            finish();
        }
        else {
            Toast.makeText(MedicalReportsActivity.this, "Please select Reports", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateImagesFromGallery() {

        ArrayList<String> imageUrls = loadPhotosFromNativeGallery();
        initializeRecyclerView(imageUrls);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {

            case REQUEST_FOR_STORAGE_PERMISSION: {

                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        populateImagesFromGallery();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MedicalReportsActivity.this, READ_EXTERNAL_STORAGE)) {
                            showPermissionRationaleSnackBar();
                        } else {
                            Toast.makeText(MedicalReportsActivity.this, "Go to settings and enable permission", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                break;
            }
        }
    }
    private ArrayList<String> loadPhotosFromNativeGallery() {
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");

        ArrayList<String> imageUrls = new ArrayList<String>();

        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));
           // System.out.println("=====> Array path => "+imageUrls.get(i));
        }
        return imageUrls;
    }
    private void initializeRecyclerView(ArrayList<String> imageUrls) {
        imageAdapter = new ImageAdapter(MedicalReportsActivity.this, imageUrls);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MedicalReportsActivity.this,4);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemOffsetDecoration(MedicalReportsActivity.this, R.dimen.item_offset));
        recyclerView.setAdapter(imageAdapter);
    }
    private void showPermissionRationaleSnackBar() {
        Snackbar.make(MedicalReportsActivity.this.findViewById(R.id.button1), "Storage permission is needed for fetching images from Gallery.",
                Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Request the permission
                ActivityCompat.requestPermissions(MedicalReportsActivity.this,
                        new String[]{READ_EXTERNAL_STORAGE},
                        REQUEST_FOR_STORAGE_PERMISSION);
            }
        }).show();

    }

}
