package com.fdc.pixelcare.Activities.Others;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.Adapters.Others.BlogsAdapter;
import com.fdc.pixelcare.DataModel.BlogsList;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.Network.NetworkUtil;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppController;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
import com.fdc.pixelcare.Views.CustomTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SALMA on 31-12-2018.
 */
public class BlogListActivity extends AppCompatActivity {

    public static final String REQUEST_TAG = "BlogListActivity";
    private RequestQueue mQueue;

    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int USER_ID, USER_LOGINTYPE;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION;

    List<BlogsList> blogsListArraylist;
    BlogsAdapter blogAdapter;
    RecyclerView recyclerView_bloglist;

    CustomTextViewBold view_more_btn;
    String BLOGS_URL = APIClass.DRS_BLOGS_LIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            setTitle(title);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(BlogListActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(BlogListActivity.this);

        if(sharedPreferences !=null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE  = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);

            Log.d(AppUtils.TAG , " *********** BlogListActivity **************** ");
            Log.d(AppUtils.TAG + " UserID: ", String.valueOf(USER_ID));
            Log.d(AppUtils.TAG + " name: ", USER_NAME);
            Log.d(AppUtils.TAG + " email: ", USER_EMAIL);
            Log.d(AppUtils.TAG + " MOBILE: ", USER_MOBILE);
            Log.d(AppUtils.TAG + " Location: ", USER_LOCATION);
        }

        initializationView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_blogs, menu);

        MenuItem search_item = menu.findItem(R.id.blog_search);
        search_item.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.blog_search:
                return true;
            case android.R.id.home:
                finish();
                return true;
            case R.id.blog_refresh:
                if (NetworkUtil.getConnectivityStatusString(BlogListActivity.this).equalsIgnoreCase("enabled")) {
                    getBlogListFromServer("INITIAL");
                } else {
                    AppUtils.showCustomAlertMessage(BlogListActivity.this, MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializationView() {
        blogsListArraylist = new ArrayList<>();

        recyclerView_bloglist = (RecyclerView)findViewById(R.id.blogs_recyclerview);
        view_more_btn = (CustomTextViewBold)findViewById(R.id.bloglist_viewmore);
        view_more_btn.setVisibility(View.GONE);

        blogAdapter = new BlogsAdapter(this, blogsListArraylist);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView_bloglist.setLayoutManager(mLayoutManager);
        recyclerView_bloglist.setItemAnimator(new DefaultItemAnimator());
        recyclerView_bloglist.setHasFixedSize(true);
        recyclerView_bloglist.setItemViewCacheSize(20);
        recyclerView_bloglist.setDrawingCacheEnabled(true);

        recyclerView_bloglist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                  //  Toast.makeText(BlogListActivity.this, " Reached last item !!!", Toast.LENGTH_SHORT).show();
                    view_more_btn.setVisibility(View.VISIBLE);
                }
            }
        });

        view_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.getConnectivityStatusString(BlogListActivity.this).equalsIgnoreCase("enabled")) {
                    getBlogListFromServer("ALL");
                } else {
                    AppUtils.showCustomAlertMessage(BlogListActivity.this, MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
                }
            }
        });

       // Toast.makeText(BlogListActivity.this, " Reached last item !!!", Toast.LENGTH_SHORT).show();

        if (NetworkUtil.getConnectivityStatusString(BlogListActivity.this).equalsIgnoreCase("enabled")) {
            getBlogListFromServer("INITIAL");
        } else {
            AppUtils.showCustomAlertMessage(BlogListActivity.this, MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null); 
        }

    }

    private void getBlogListFromServer(String load_type) {
        blogsListArraylist = new ArrayList<>();

        final ProgressDialog progressDialog3 = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog3.setIndeterminate(true);
        progressDialog3.setMessage("Loading Blogs...");
        progressDialog3.show();

        if(load_type.equals("INITIAL")) {
            BLOGS_URL = APIClass.DRS_BLOGS_LIST;
        }
        else  if(load_type.equals("ALL")) {
            view_more_btn.setVisibility(View.GONE);
            BLOGS_URL = APIClass.DRS_BLOGS_ALL_LIST;
        }
        else {
            BLOGS_URL = APIClass.DRS_BLOGS_LIST;
        }

        blogsListArraylist.clear();
        blogsListArraylist = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BLOGS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(AppUtils.TAG, " blog res:"+ response.toString());

                        if (response != null) {
                            //  String response_filter =  stripHtml(response);
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;
                            try {
                                jsonObject = new JSONObject(response);

                                int success = jsonObject.getInt("status");
                                Log.d(AppUtils.TAG, "status: " + success);

                                if (success == 1) {
                                    JSONArray JAStuff = jsonObject.getJSONArray("blog_details");

                                    /*  * CHECK THE NUMBER OF RECORDS **/
                                    int intStuff = JAStuff.length();
                                    Log.d(AppUtils.TAG, "intStuff size: " + intStuff);

                                    if (intStuff != 0)  {
                                        for (int i = 0; i < JAStuff.length(); i++) {
                                            JSONObject JOStuff = JAStuff.getJSONObject(i);
                                            //  Log.d(Utils.TAG, "POSTid: " + JOStuff.getInt("post_id"));
                                            //    Log.d(Utils.TAG, "Res_detail: " + JOStuff.getString("listing_type"));
                                            blogsListArraylist.add(new BlogsList(JOStuff.getInt("post_id"),JOStuff.getString("title"),
                                                    JOStuff.getString("description"),JOStuff.getString("post_image"),
                                                    JOStuff.getString("created_date"),JOStuff.getString("listing_type"),
                                                    JOStuff.getString("username"),JOStuff.getString("userprof"),
                                                    JOStuff.getString("userimg"), JOStuff.getString("contactInfo"),
                                                    JOStuff.getString("attachments"), JOStuff.getInt("company_id"),
                                                    JOStuff.getString("transaction_id"), JOStuff.getString("num_views"),
                                                    JOStuff.getString("video_url"),JOStuff.getString("from_to_date")));

                                        }

                                        if(blogsListArraylist.size() > 0 ) {
                                            blogAdapter = new BlogsAdapter(BlogListActivity.this, blogsListArraylist);
                                            recyclerView_bloglist.setAdapter(blogAdapter);
                                            blogAdapter.notifyDataSetChanged();

                                        }
                                    }
                                    else {
                                        blogsListArraylist = new ArrayList<>();

                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog3.dismiss();
                            }
                        }
                        else {
                            progressDialog3.dismiss();
                        }

                        progressDialog3.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(AppUtils.TAG, "on error"+ error.toString());
                        progressDialog3.dismiss();
                        //     Toast.makeText(getActivity(), HCConstants.INTERNET+ "\n"+HCConstants.INTERNET_CHECK, Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIParam.KEY_API, APIClass.API_KEY);
                map.put(APIParam.KEY_DATA_SOURCE, APIClass.API_DATASOURCE_KEY);
                map.put(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID));
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(this).
                getRequestQueue();
        AppController.getInstance(this).addToRequestQueue(stringRequest);
    }

}
