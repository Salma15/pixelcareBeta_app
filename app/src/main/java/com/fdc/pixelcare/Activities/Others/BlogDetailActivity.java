package com.fdc.pixelcare.Activities.Others;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fdc.pixelcare.Adapters.Others.CommentsAdapter;
import com.fdc.pixelcare.DataModel.Comments;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppController;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Utils.ShareadPreferenceClass;
import com.fdc.pixelcare.Views.CustomEditText;
import com.fdc.pixelcare.Views.CustomTextView;
import com.fdc.pixelcare.Views.CustomTextViewItalicBold;
import com.fdc.pixelcare.Views.CustomTextViewSemiBold;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SALMA on 31-12-2018.
 */
public class BlogDetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String REQUEST_TAG = "BlogDetailActivity";
    private RequestQueue mQueue;

    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int USER_ID, USER_LOGINTYPE;
    String USER_NAME, USER_EMAIL, USER_MOBILE, USER_LOCATION;

    int BLOG_ID, BLOG_COMPANY_ID;
    String BLOG_IMAGE, BLOG_TITLE, BLOG_AUTHOR, BLOG_DATE, BLOG_DESCRIPTION, BLOG_TYPE, BLOG_USERNAME, BLOG_PROFESSION, BLOG_USERIMAGE;
    String DOWNLOAD_BLOG_IMAGE_URL, BLOG_CONTACTINFO, BLOG_ATTACHMENTS, BLOG_TRANSACTIONID, BLOG_NUM_VIEWS;

    ImageView send_comments, blog_Image, blog_like, blog_comment, send_email, share_facebook, share_twitter, share_whatsapp, share_googleplus, share_linkedin;
    CustomTextView blog_like_count, blog_comment_count;
    CustomTextViewItalicBold blogdetail_desc_readmore;
    LinearLayout blog_writelayout;
    CustomEditText blog_write_comment, blog_email;
    boolean comment_visible = false;
    CustomTextView blog_title, blog_description,  blog_numviews, blog_username;
    CustomTextViewSemiBold blog_postdate;

    private List<Comments> commentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommentsAdapter mAdapter;
    Comments comment;
    ImageView userProfile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            setTitle(title);

            BLOG_ID = b.getInt("BLOG_ID", 0);
            BLOG_IMAGE = b.getString("BLOG_IMAGE", "");
            BLOG_TITLE = b.getString("BLOG_TITLE", "");
            BLOG_DATE = b.getString("BLOG_DATE", "");
            BLOG_DESCRIPTION = b.getString("BLOG_DESCRIPTION", "");
            BLOG_TYPE = b.getString("BLOG_TYPE", "");
            BLOG_USERNAME  = b.getString("BLOG_USERNAME", "");
            BLOG_PROFESSION = b.getString("BLOG_USERPROFESSION", "");
            BLOG_USERIMAGE  = b.getString("BLOG_USERIMAGE", "");
            BLOG_CONTACTINFO = b.getString("BLOG_CONTACTINFO", "");
            BLOG_ATTACHMENTS = b.getString("BLOG_ATTACHMENT", "");
            BLOG_COMPANY_ID  = b.getInt("BLOG_COMPANY_ID", 0);
            BLOG_TRANSACTIONID  = b.getString("BLOG_TRANSACTIONID", "");
            BLOG_NUM_VIEWS = b.getString("BLOG_VIEWS", "");

            Log.d(AppUtils.TAG , "******************* BlogDetailActivity *************");
            Log.d(AppUtils.TAG + "Blog ID:", String.valueOf(BLOG_ID));
            Log.d(AppUtils.TAG + "Blog Image:", BLOG_IMAGE);
            Log.d(AppUtils.TAG + "Blog Title:", BLOG_TITLE);
            Log.d(AppUtils.TAG + "Blog Type:", BLOG_TYPE);
            Log.d(AppUtils.TAG + "Blog Date:", BLOG_DATE);
            Log.d(AppUtils.TAG + "Blog Desc:", BLOG_DESCRIPTION);
            Log.d(AppUtils.TAG + "Blog uname:", BLOG_USERNAME);
            Log.d(AppUtils.TAG + "Blog prof:", BLOG_PROFESSION);
            Log.d(AppUtils.TAG + "Blog image:", BLOG_USERIMAGE);
            Log.d(AppUtils.TAG + "Blog COMPANY:", String.valueOf(BLOG_COMPANY_ID));
            Log.d(AppUtils.TAG + "Blog transId:", BLOG_TRANSACTIONID);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(BlogDetailActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(BlogDetailActivity.this);

        if(sharedPreferences !=null) {
            USER_ID = sharedPreferences.getInt(MHConstants.PREF_USER_ID, 0);
            USER_NAME = sharedPreferences.getString(MHConstants.PREF_USER_NAME, "");
            USER_EMAIL = sharedPreferences.getString(MHConstants.PREF_USER_EMAIL, "");
            USER_MOBILE = sharedPreferences.getString(MHConstants.PREF_USER_MOBILE, "");
            USER_LOCATION = sharedPreferences.getString(MHConstants.PREF_USER_LOCATION, "");
            USER_LOGINTYPE  = sharedPreferences.getInt(MHConstants.PREF_USER_LOGINTYPE, 0);

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
        comment_visible = false;
        blog_Image = (ImageView) findViewById(R.id.blogdetail_image);
        blog_title = (CustomTextView) findViewById(R.id.blogdetail_title);
        blog_description = (CustomTextView) findViewById(R.id.blogdetail_desc);
        blog_like = (ImageView) findViewById(R.id.blogdetail_like);
        blog_comment = (ImageView) findViewById(R.id.blogdetail_comment);
        blog_writelayout = (LinearLayout) findViewById(R.id.blogdetail_comment_layout);
        blog_write_comment = (CustomEditText) findViewById(R.id.blogdetail_write_comment);
        send_comments = (ImageView) findViewById(R.id.blogdetail_send_comment);
        blog_email = (CustomEditText) findViewById(R.id.blogdetail_email);
        send_email = (ImageView) findViewById(R.id.blogdetail_send_email);
        share_facebook = (ImageView) findViewById(R.id.blogdetail_facebook);
        share_twitter = (ImageView) findViewById(R.id.blogdetail_twitter);
        share_whatsapp = (ImageView) findViewById(R.id.blogdetail_whatsapp);
        share_linkedin = (ImageView) findViewById(R.id.blogdetail_linkedin);
        blog_like_count = (CustomTextView) findViewById(R.id.blogdetail_like_count);
        blog_comment_count  = (CustomTextView) findViewById(R.id.blogdetail_comment_count);
        blog_postdate = (CustomTextViewSemiBold) findViewById(R.id.blogdetail_postedon);
        blog_numviews = (CustomTextView) findViewById(R.id.blogdetail_views);
        blog_username = (CustomTextView) findViewById(R.id.blogdetail_userName);
        blog_like.setOnClickListener(this);
        blog_comment.setOnClickListener(this);
        send_comments.setOnClickListener(this);
        send_email.setOnClickListener(this);
        share_facebook.setOnClickListener(this);
        share_twitter.setOnClickListener(this);
        share_whatsapp.setOnClickListener(this);
        share_linkedin.setOnClickListener(this);
        blogdetail_desc_readmore = (CustomTextViewItalicBold) findViewById(R.id.blogdetail_desc_readmore);
        blogdetail_desc_readmore.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.blogdetail_recyclerview);

        userProfile_image = (ImageView) findViewById(R.id.blogdetail_userprofileImg);

        mAdapter = new CommentsAdapter(this, commentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        setRequestToServer();

        if(BLOG_USERIMAGE.equals("")) {

        }
        else {
            String urlProStr = BLOG_USERIMAGE;
            URL url_pof = null;
            try {
                url_pof = new URL(urlProStr);
                URI uri = new URI(url_pof.getProtocol(), url_pof.getUserInfo(), url_pof.getHost(), url_pof.getPort(), url_pof.getPath(), url_pof.getQuery(), url_pof.getRef());
                url_pof = uri.toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            Picasso.with(this).load(String.valueOf(url_pof))
                    .placeholder(R.drawable.user_profile)
                    .error(R.drawable.user_profile)
                    .fit()
                    .into(userProfile_image, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }

        if(BLOG_IMAGE.equals("")) {
            blog_Image.setVisibility(View.GONE);
        }
        else {
            blog_Image.setVisibility(View.VISIBLE);

            DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_HEALTH_BLOGS_IMAGE_URL+String.valueOf(BLOG_ID)+"/"+BLOG_IMAGE;

            //  Picasso.with(getActivity()).load(DOWNLOAD_BLOG_IMAGE_URL).into(_image);
            String urlBlogStr = DOWNLOAD_BLOG_IMAGE_URL;
            URL url = null;
            try {
                url = new URL(urlBlogStr);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            Picasso.with(this).load(String.valueOf(url))
                    .placeholder(R.drawable.blogs_empty_img)
                    .error(R.drawable.blogs_empty_img)
                    .resize(400, 400)
                    .centerInside()
                    .into(blog_Image, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }

        String Tile_Desc = stripHtml(BLOG_TITLE);
        blog_title.setText(Tile_Desc);
        String Description = stripHtml(BLOG_DESCRIPTION);
        blog_description.setText(Description);
        blog_numviews.setText("Views: "+BLOG_NUM_VIEWS);
        blog_username.setText(BLOG_USERNAME);

        if(BLOG_DATE.equals("")) {

        }
        else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date newDate = null;
            try {
                newDate = format.parse(BLOG_DATE);
                format = new SimpleDateFormat("dd MMM yy");
                String post_date = format.format(newDate);
                blog_postdate.setText(post_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.blogdetail_like:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(blog_like.getWindowToken(), 0);
                blog_like.setImageResource(R.mipmap.like_filled);
                int count = Integer.parseInt(blog_like_count.getText().toString());
                blog_like_count.setText(String.valueOf(count+1));
                sendEventRequsetToServer();
                blog_like.setEnabled(false);
                break;
            case R.id.blogdetail_comment:
                InputMethodManager imm3 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(blog_comment.getWindowToken(), 0);
                viewAllComments(commentList);
                break;
            case R.id.blogdetail_send_comment:
                InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(send_comments.getWindowToken(), 0);
                if(blog_write_comment.getText().toString().equals("")) {
                    Toast.makeText(this, "Please write comments !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendBlogCommmentLink(blog_write_comment.getText().toString());
                }
                break;
            case R.id.blogdetail_send_email:
                InputMethodManager imm2 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(send_email.getWindowToken(), 0);
                if(blog_email.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendBlogEmailLink(blog_email.getText().toString());
                }
                break;
            case R.id.blogdetail_facebook:
                shareViaFacebookLink();
                break;
            case R.id.blogdetail_twitter:
                shareViaTwiterLink();
                break;
            case R.id.blogdetail_whatsapp:
                shareViaWhatsAppLink();
                break;
            case R.id.blogdetail_linkedin:
                shareViaLinkedinLink();
                break;
            case R.id.blogdetail_desc_readmore:
                if (blogdetail_desc_readmore.getText().toString().equals("Read More >>")) {
                    blogdetail_desc_readmore.setText("<< Read Less");
                    blog_description.setSingleLine(false);
                    blog_description.setMaxLines(5000);
                } else if (blogdetail_desc_readmore.getText().toString().equals("<< Read Less")) {
                    blogdetail_desc_readmore.setText("Read More >>");
                    blog_description.setSingleLine(false);
                    blog_description.setEllipsize(TextUtils.TruncateAt.END);
                    blog_description.setLines(5);
                }
                break;
        }
    }

    private void shareViaLinkedinLink() {
        Log.d(AppUtils.TAG + "TITLE:", BLOG_TITLE);
        Log.d(AppUtils.TAG + "TRANS:", BLOG_TRANSACTIONID);

        String LINKEDIN_SHARE_LINK = APIClass.DRS_HEALTH_BLOG_SHARE+BLOG_TITLE+"/"+BLOG_TRANSACTIONID;
        String LINKEDIN_LINK = LINKEDIN_SHARE_LINK.replace(" ", "-");
        Log.d(AppUtils.TAG + "SHARE:", LINKEDIN_LINK);

        Intent linkedinIntent = new Intent(Intent.ACTION_SEND);
        linkedinIntent.putExtra(Intent.EXTRA_TEXT, BLOG_TITLE +" \nFor more info visit link \n"+ LINKEDIN_LINK);
        linkedinIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(linkedinIntent,  PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for(ResolveInfo resolveInfo: resolvedInfoList){
            if(resolveInfo.activityInfo.packageName.startsWith("com.linkedin.android")){
                linkedinIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name );
                resolved = true;
                break;
            }
        }
        if(resolved){
            startActivity(linkedinIntent);
        }else{
            Toast.makeText(this, "Linkedin app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private void shareViaWhatsAppLink() {
        Log.d(AppUtils.TAG + "TITLE:", BLOG_TITLE);
        Log.d(AppUtils.TAG + "TRANS:", BLOG_TRANSACTIONID);

        String BLOG_TITLE_NEW = BLOG_TITLE.replace("?", "");

        String WHATSAPP_SHARE_LINK = APIClass.DRS_HEALTH_BLOG_SHARE+BLOG_TITLE_NEW+"/"+BLOG_TRANSACTIONID;
        String WHATSAPP_LINK = WHATSAPP_SHARE_LINK.replace(" ", "-");
        Log.d(AppUtils.TAG + "SHARE:", WHATSAPP_LINK);

        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, BLOG_TITLE +" \nFor more info visit link \n"+ WHATSAPP_LINK);
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Whatsapp have not been installed", Toast.LENGTH_LONG).show();
        }
    }

    private void shareVialGooglePlusLink() {
        Log.d(AppUtils.TAG + "TITLE:", BLOG_TITLE);
        Log.d(AppUtils.TAG + "TRANS:", BLOG_TRANSACTIONID);

        String BLOG_TITLE_NEW = BLOG_TITLE.replace("?", "");

        String GPLUS_SHARE_LINK = APIClass.DRS_HEALTH_BLOG_SHARE+BLOG_TITLE_NEW+"/"+BLOG_TRANSACTIONID;
        String GPLUS_LINK = GPLUS_SHARE_LINK.replace(" ", "-");
        Log.d(AppUtils.TAG + "SHARE:", GPLUS_LINK);

        Intent googleIntent = new Intent(Intent.ACTION_SEND);
        googleIntent.setType("text/plain");
        googleIntent.putExtra(Intent.EXTRA_TEXT, BLOG_TITLE +" \nFor more info visit link \n"+ GPLUS_LINK);

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(googleIntent,  PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for(ResolveInfo resolveInfo: resolvedInfoList){
            if(resolveInfo.activityInfo.packageName.startsWith("com.google.android.apps.plus")){
                googleIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name );
                resolved = true;
                break;
            }
        }
        if(resolved){
            startActivity(googleIntent);
        }else{
            Toast.makeText(this, "Google Plus app isn't found", Toast.LENGTH_LONG).show();
        }

    }

    private void shareViaTwiterLink() {

        Log.d(AppUtils.TAG + "TITLE:", BLOG_TITLE);
        Log.d(AppUtils.TAG + "TRANS:", BLOG_TRANSACTIONID);

        String BLOG_TITLE_NEW = BLOG_TITLE.replace("?", "");

        String TWITTER_SHARE_LINK = APIClass.DRS_HEALTH_BLOG_SHARE+BLOG_TITLE_NEW+"/"+BLOG_TRANSACTIONID;
        String TWITTER_LINK = TWITTER_SHARE_LINK.replace(" ", "-");
        Log.d(AppUtils.TAG + "SHARE:", TWITTER_LINK);

        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, BLOG_TITLE +" \nFor more info visit link \n"+ TWITTER_LINK);
        tweetIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent,  PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for(ResolveInfo resolveInfo: resolvedInfoList){
            if(resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")){
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name );
                resolved = true;
                break;
            }
        }
        if(resolved){
            startActivity(tweetIntent);
        }else{
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private void shareViaFacebookLink() {
        Log.d(AppUtils.TAG + "TITLE:", BLOG_TITLE);
        Log.d(AppUtils.TAG + "TRANS:", BLOG_TRANSACTIONID);

        String BLOG_TITLE_NEW = BLOG_TITLE.replace("?", "");

        String FACEBOOK_SHARE_LINK = APIClass.DRS_PRACTICE_SURGICAL_SHARE+BLOG_TITLE_NEW+"/"+BLOG_TRANSACTIONID;
        String FACEBOOK_LINK = FACEBOOK_SHARE_LINK.replace(" ", "-");
        Log.d(AppUtils.TAG + "SHARE:", FACEBOOK_LINK);

        Intent facebkIntent = new Intent(Intent.ACTION_SEND);
        facebkIntent.putExtra(Intent.EXTRA_TEXT, BLOG_TITLE +" \nFor more info visit link \n"+ FACEBOOK_LINK);
        facebkIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(facebkIntent,  PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for(ResolveInfo resolveInfo: resolvedInfoList){
            if(resolveInfo.activityInfo.packageName.startsWith("com.facebook.katana")){
                facebkIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name );
                resolved = true;
                break;
            }
        }
        if(resolved){
            startActivity(facebkIntent);
        }else{
            Toast.makeText(this, "Facebook app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private void setRequestToServer() {
        commentList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_BLOG_LIKE_COMMENT_COUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(AppUtils.TAG, " blog likes: "+ response.toString());

                        if (response != null) {
                            // String success;
                            int likes_check, likes_count, commentCount;
                            JSONObject jsonObject;
                            JSONArray jsonArray;
                            try {
                                jsonObject = new JSONObject(response);
                                JSONObject  success = jsonObject.getJSONObject("like");
                                Log.d(AppUtils.TAG, "status: " + success);
                                likes_check = success.getInt("checkLike");
                                likes_count = success.getInt("countLike");
                                commentCount  = success.getInt("countComment");
                                if(likes_check > 0 ) {
                                    blog_like_count.setText(String.valueOf(likes_count));
                                    blog_comment_count.setText(String.valueOf(commentCount));
                                    blog_like.setImageResource(R.mipmap.like_filled);
                                    blog_like.setEnabled(false);
                                }
                                else {
                                    blog_like_count.setText(String.valueOf(likes_count));
                                    blog_comment_count.setText(String.valueOf(commentCount));
                                    blog_like.setImageResource(R.mipmap.like_empty);
                                    blog_like.setEnabled(true);
                                }

                                jsonArray = jsonObject.getJSONArray("comments");
                                Log.d(AppUtils.TAG, "array: " + jsonArray.length());
                                if(jsonArray.length() > 0) {
                                    for(int i=0;i<jsonArray.length(); i++)
                                    {
                                        JSONObject JOStuff = jsonArray.getJSONObject(i);
                                        comment = new Comments(JOStuff.getString("username"),JOStuff.getString("user_image"),
                                                JOStuff.getString("comments"),JOStuff.getString("post_date"),JOStuff.getString("topic_id"),
                                                JOStuff.getString("topic_type"));
                                        commentList.add(comment);
                                    }

                                    Log.d(AppUtils.TAG, "size: " + commentList.size());
                                    if(commentList.size() > 0 ) {
                                        mAdapter = new CommentsAdapter(BlogDetailActivity.this, commentList);
                                        recyclerView.setAdapter(mAdapter);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(AppUtils.TAG, error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIParam.KEY_API, APIClass.API_KEY);
                map.put(APIParam.KEY_DATA_SOURCE, APIClass.API_DATASOURCE_KEY);
                map.put(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID));
                map.put(APIParam.KEY_BLOG_ID, String.valueOf(BLOG_ID));
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(BlogDetailActivity.this).
                getRequestQueue();
        AppController.getInstance(BlogDetailActivity.this).addToRequestQueue(stringRequest);
    }

    private void sendEventRequsetToServer() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_LIKES_UPDATES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(AppUtils.TAG," likeUpdate: "+ response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(AppUtils.TAG, error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put(APIParam.KEY_API, APIClass.API_KEY);
                map.put(APIParam.KEY_DATA_SOURCE, APIClass.API_DATASOURCE_KEY);
                map.put(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID));
                map.put(APIParam.KEY_BLOG_ID, String.valueOf(BLOG_ID));

                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(BlogDetailActivity.this).
                getRequestQueue();
        AppController.getInstance(BlogDetailActivity.this).addToRequestQueue(stringRequest);
    }

    private void viewAllComments(List<Comments> commentList) {
        final Dialog dialogViewCard = new Dialog(BlogDetailActivity.this, R.style.CustomDialog);
        dialogViewCard.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogViewCard.setContentView(R.layout.cooments_view);
        dialogViewCard.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationView; //style id
        dialogViewCard.show();
        ProgressBar loadingBar  = (ProgressBar) dialogViewCard.findViewById(R.id.comments_progress_bar);
        final CustomEditText _edt_comment_msg = (CustomEditText) dialogViewCard.findViewById(R.id.comments_write_comment);
        ImageView send_comment = (ImageView) dialogViewCard.findViewById(R.id.comments_send_comment);

        RecyclerView recyclerView = (RecyclerView) dialogViewCard.findViewById(R.id.comments_recyclerview);

        mAdapter = new CommentsAdapter(BlogDetailActivity.this, commentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(BlogDetailActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if(commentList.size() > 0 ) {
            mAdapter = new CommentsAdapter(BlogDetailActivity.this, commentList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_edt_comment_msg.getText().toString().equals("")) {
                    Toast.makeText(BlogDetailActivity.this, "Please write comments !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialogViewCard.cancel();
                    sendBlogCommmentLink(_edt_comment_msg.getText().toString());
                }
            }
        });
    }

    private void sendBlogCommmentLink(final String comment_text) {
        Log.d(AppUtils.TAG, " comment: "+comment_text);

        final ProgressDialog progressDialog = new ProgressDialog(BlogDetailActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_BLOGS_COMMENT_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(AppUtils.TAG, response.toString());
                        if (response != null) {
                            JSONObject jsonObject = null;
                            progressDialog.dismiss();
                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("result");

                                if(staus_res.equalsIgnoreCase("success")) {
                                    Toast.makeText(BlogDetailActivity.this, "Comments added successfully.", Toast.LENGTH_SHORT).show();
                                    blog_write_comment.setText("");
                                    setRequestToServer();
                                }
                                else   if(staus_res.equalsIgnoreCase("failure")) {
                                    Toast.makeText(BlogDetailActivity.this, "Failed to add comment.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(APIParam.KEY_API, APIClass.API_KEY);
                map.put(APIParam.KEY_DATA_SOURCE, APIClass.API_DATASOURCE_KEY);
                map.put(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID));
                map.put(APIParam.KEY_BLOG_ID, String.valueOf(BLOG_ID));
                map.put(APIParam.KEY_BLOG_COMMENT, comment_text);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(BlogDetailActivity.this).
                getRequestQueue();
        AppController.getInstance(BlogDetailActivity.this).addToRequestQueue(stringRequest);
    }

    private void sendBlogEmailLink(final String email_address) {

        Log.d(AppUtils.TAG, "email: "+email_address);
        Log.d(AppUtils.TAG, "trans: "+BLOG_TRANSACTIONID);
        Log.d(AppUtils.TAG, "type: "+BLOG_TYPE);

        final ProgressDialog progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending Email...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_SHARE_BLOG_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(AppUtils.TAG, response.toString());
                        if (response != null) {
                            JSONObject jsonObject = null;
                            progressDialog.dismiss();
                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("result");

                                if(staus_res.equalsIgnoreCase("success")) {
                                    Toast.makeText(BlogDetailActivity.this, "Email sent successfully.", Toast.LENGTH_SHORT).show();
                                    blog_email.setText("");
                                }
                                else   if(staus_res.equalsIgnoreCase("failure")) {
                                    Toast.makeText(BlogDetailActivity.this, "Failed to send an email.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put(APIParam.KEY_API, APIClass.API_KEY);
                map.put(APIParam.KEY_DATA_SOURCE, APIClass.API_DATASOURCE_KEY);
                map.put(APIParam.KEY_LOGIN_ID, String.valueOf(USER_ID));
                map.put(APIParam.KEY_BLOG_ID, String.valueOf(BLOG_ID));
                map.put(APIParam.KEY_EMAIL, email_address);
                map.put(APIParam.KEY_BLOG_TRANSACTION_ID, BLOG_TRANSACTIONID);

                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(BlogDetailActivity.this).
                getRequestQueue();
        AppController.getInstance(BlogDetailActivity.this).addToRequestQueue(stringRequest);
    }
}
