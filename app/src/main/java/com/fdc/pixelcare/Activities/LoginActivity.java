package com.fdc.pixelcare.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.Network.APIParam;
import com.fdc.pixelcare.Network.CustomVolleyRequestQueue;
import com.fdc.pixelcare.Network.NetworkUtil;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Utils.MHConstants;
import com.fdc.pixelcare.Views.CustomEditText;
import com.fdc.pixelcare.Views.CustomTextView;
import com.fdc.pixelcare.Views.CustomTextViewSemiBold;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SALMA on 20-12-2018.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener, Response.ErrorListener  {
    public static final String REQUEST_TAG = "LoginActivity";

    CustomTextView signin_btn;
    CustomEditText _edt_username, _edt_mobile, _edt_email, _edt_city;
    ImageView login_facebook_btn, login_gplus_btn;

    CallbackManager callbackManager;
    boolean boolean_login, boolean_login_gplus;
    private static final int RC_SIGN_IN = 007;
    //   private GoogleApiClient mGoogleApiClient;
    private static String google_access_tokenid = "";
    private boolean googleplus_login = false;
    String fb_profile_image ="";

    private RequestQueue mQueue;
    String LOGIN_USERNAME, LOGIN_MOBILRE, LOGIN_EMAIL;
    CountryCodePicker ccp;
    int COUNTRY_CODE = 91;
    CustomTextViewSemiBold email_note;

    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
    }

    private void initializeViews() {

        _edt_username = (CustomEditText) findViewById(R.id.login_username);
        _edt_mobile = (CustomEditText) findViewById(R.id.login_mobile);
        _edt_email = (CustomEditText) findViewById(R.id.login_email);
       // _edt_city = (CustomEditText) findViewById(R.id.login_city);
        signin_btn = (CustomTextView) findViewById(R.id.login_signin);
        signin_btn.setOnClickListener(this);
        login_facebook_btn = (ImageView) findViewById(R.id.login_facebook);
        login_gplus_btn = (ImageView) findViewById(R.id.login_googleplus);
        login_facebook_btn.setOnClickListener(this);
        login_gplus_btn.setOnClickListener(this);
        ccp = (CountryCodePicker) findViewById(R.id.login_ccp);
        email_note = (CustomTextViewSemiBold) findViewById(R.id.login_email_note);
        email_note.setVisibility(View.GONE);

        Log.d(AppUtils.TAG, " DefCountryCode: "+ccp.getDefaultCountryCode());
        Log.d(AppUtils.TAG, " DefCountryName: "+ccp.getDefaultCountryName());
        COUNTRY_CODE = Integer.parseInt(ccp.getDefaultCountryCode());
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Log.d(AppUtils.TAG, " CountryCode: "+ccp.getSelectedCountryCode());
                Log.d(AppUtils.TAG, " CountryName: "+ccp.getSelectedCountryName());
                COUNTRY_CODE = Integer.parseInt(ccp.getSelectedCountryCode());
                Toast.makeText(LoginActivity.this, " Code:"+ccp.getSelectedCountryCode(), Toast.LENGTH_SHORT).show();

                if(ccp.getSelectedCountryCode().equals("91")) {
                    email_note.setVisibility(View.GONE);
                    _edt_email.setHint("Enter Email ID (Optional)");
                }
                else {
                    _edt_email.setHint("Enter Email ID");
                    email_note.setVisibility(View.VISIBLE);
                }
            }
        });

      /*  try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.fdc.pixelcare",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(AppUtils.TAG+ "KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(AppUtils.TAG+ "KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d(AppUtils.TAG+ "KeyHash:", e.toString());
        }

        hashFromSHA1("89:1C:BB:A8:C0:D7:44:F1:62:52:66:61:C1:13:88:CD:10:D7:15:93"); // App Signin ceriticate SHA1 fingerprint no. Generate KeyShash for facebook from live playstore app and add in fb dev account

        FacebookSdk.sdkInitialize(LoginActivity.this.getApplicationContext());

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        Log.d(AppUtils.TAG, " default_web_client_id: "+getString(R.string.default_web_client_id) );

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    System.out.println("User logged in");
                }
                else{
                    System.out.println("User not logged in");
                }
            }
        };*/
    }

    public void hashFromSHA1(String sha1) {
        String[] arr = sha1.split(":");
        byte[] byteArr = new  byte[arr.length];

        for (int i = 0; i< arr.length; i++) {
            byteArr[i] = Integer.decode("0x" + arr[i]).byteValue();
        }

        Log.d(AppUtils.TAG+ " FB AccKeyHash : ", Base64.encodeToString(byteArr, Base64.NO_WRAP));
    }

  /*  public void onStop(){
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();       // Google Plus Signout
    } */


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_signin:
                InputMethodManager imm2 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(signin_btn.getWindowToken(), 0);
                validatingLoginDetails();
                break;
            case R.id.login_facebook:
                InputMethodManager imm3 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(login_facebook_btn.getWindowToken(), 0);

                /*if (boolean_login) {
                    boolean_login = false;
                    LoginManager.getInstance().logOut();
                } else {
                    // LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile,email,user_birthday,user_location"));
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile,email"));
                    continueWithFaebookLogin();
                }*/

                break;
            case R.id.login_googleplus:
                InputMethodManager imm4 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm4.hideSoftInputFromWindow(login_gplus_btn.getWindowToken(), 0);

              /*  Log.d(AppUtils.TAG, "boolean_login_gplus: "+boolean_login_gplus);

                if(boolean_login_gplus) {
                    boolean_login_gplus =  false;
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();

                    mAuth.signOut();

                    mGoogleSignInClient.signOut().addOnCompleteListener(this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    updateUI(null);
                                }
                            });
                }
                else {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }*/
                break;
        }
    }

    private void validatingLoginDetails() {

        String get_name = _edt_username.getText().toString().trim();
        String get_mobile = _edt_mobile.getText().toString().trim();
        String get_email = _edt_email.getText().toString().trim();

        if(get_name.equals("")) {
            Toast.makeText(LoginActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
        }
        else if((get_mobile.equals("") || (get_mobile.length() <10))) {
            Toast.makeText(LoginActivity.this, "Enter 10 digits mobile number", Toast.LENGTH_SHORT).show();
        }
        else  if(get_email.equals("") && COUNTRY_CODE != 91) {        // India Country Code = 91
            Toast.makeText(LoginActivity.this, "Enter Email ID !!!", Toast.LENGTH_SHORT).show();
        }
        else {

            //  Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();

            if (NetworkUtil.getConnectivityStatusString(LoginActivity.this).equalsIgnoreCase("enabled")) {
                Intent i = new Intent(LoginActivity.this, VerifyOTPActivity.class);
                i.putExtra("USER_NAME", get_name);
                i.putExtra("USER_MOBILE", get_mobile);
                i.putExtra("USER_EMAIL", get_email);
                startActivity(i);
                finish();
            } else {
                AppUtils.showCustomAlertMessage(LoginActivity.this, MHConstants.INTERNET,MHConstants.INTERNET_CHECK, "OK", null, null);
            }

        }
    }

    private void continueWithFaebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("ONSUCCESS", "User ID: " + loginResult.getAccessToken().getUserId()
                        + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken()
                );
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {

                                    boolean_login = true;
                                    // tv_facebook.setText("Logout from Facebook");

                                    Log.e("object", object.toString());
                                    //  str_facebookname = object.getString("name");

                                    try {
                                        //     str_facebookemail = object.getString("email");
                                    } catch (Exception e) {
                                        //    str_facebookemail = "";
                                        e.printStackTrace();
                                    }

                                    try {
                                        //      str_facebookid = object.getString("id");
                                    } catch (Exception e) {
                                        //     str_facebookid = "";
                                        e.printStackTrace();

                                    }

                                    fn_profilepic(object.getString("name"), object.getString("email"), object.getString("id"));

                                } catch (Exception e) {

                                }
                            }
                        });
                Bundle parameters = new Bundle();
                // parameters.putString("fields", "id, name, email,gender,birthday,location");
                parameters.putString("fields", "id, name, email");

                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                if (AccessToken.getCurrentAccessToken() == null) {
                    return; // already logged out
                }
                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile,email"));
                        continueWithFaebookLogin();

                    }
                }).executeAsync();


            }

            @Override
            public void onError(FacebookException e) {
                Log.e("ON ERROR", "Login attempt failed.");
                AccessToken.setCurrentAccessToken(null);
                //    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile,email,user_birthday"));
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile,email"));

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {

        }

       /* if (requestCode == RC_SIGN_IN) {
           // GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
         //   handleSignInResult(result);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
          //  handleSignInResult(task);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(AppUtils.TAG, " HANDLE"+ account.getDisplayName());
            } catch (ApiException e) {
                e.printStackTrace();
                Log.d(AppUtils.TAG, " handleErr: "+ e.toString());
            }
        }*/

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d(AppUtils.TAG, "Google sign in failed", e);
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        boolean_login_gplus = true;
        Log.d(AppUtils.TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(AppUtils.TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(AppUtils.TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                    }
                });
    }


    private void updateUI(FirebaseUser currentUser) {

        if(currentUser != null) {
            Log.d(AppUtils.TAG, " currentUser: "+ currentUser.getDisplayName());
            Log.d(AppUtils.TAG, " currentMobile: "+ currentUser.getPhoneNumber());
            Log.d(AppUtils.TAG, " currentEmail: "+ currentUser.getEmail());

            LOGIN_USERNAME = currentUser.getDisplayName();
            LOGIN_MOBILRE = "";
            LOGIN_EMAIL = currentUser.getEmail();

            vefifyLoginDetailsFromServer(currentUser.getDisplayName(), currentUser.getEmail(), "");

            mAuth.signOut();

            mGoogleSignInClient.signOut().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updateUI(null);
                        }
                    });
        }

        //  FirebaseAuth.getInstance().signOut();

    }

    private void fn_profilepic(final String name, final String email, final String id) {

        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        params.putString("type", "large");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "me/picture",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        Log.e("Response 2", response + "");

                        try {
                            String str_facebookimage = (String) response.getJSONObject().getJSONObject("data").get("url");
                            Log.e("Picture", str_facebookimage);

                            fb_profile_image = str_facebookimage;
                            //   Toast.makeText(LoginActivity.this, " Name: "+ name + "\nEmail: "+ email+ "\n ID: "+ id + "\nPhoto: "+str_facebookimage, Toast.LENGTH_SHORT).show();

                            //   Glide.with(MainActivity.this).load(str_facebookimage).skipMemoryCache(true).into(iv_image);

                            Toast.makeText(LoginActivity.this, "Facebook login success !!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Unable to login with facebook !!!", Toast.LENGTH_SHORT).show();
                        }

                        Log.d(AppUtils.TAG, " str_facebookProfile: "+ fb_profile_image);
                        Log.d(AppUtils.TAG, " str_facebookname: "+ name);
                        Log.d(AppUtils.TAG, " str_facebookemail: "+ email);
                        Log.d(AppUtils.TAG, " str_facebookID: "+ id);

                        LOGIN_USERNAME = name;
                        LOGIN_MOBILRE = "";
                        LOGIN_EMAIL = email;

                        vefifyLoginDetailsFromServer(name, email, "");


                     /*   Intent i = new Intent(LoginActivity.this, FacebookVerifyOTPActivity.class);
                        i.putExtra("USER_NAME", name);
                        i.putExtra("USER_MOBILE", "");
                        i.putExtra("USER_EMAIL", email);
                        i.putExtra("USER_PROFILEPIC", fb_profile_image);
                        startActivity(i);
                        finish();*/
                    }
                }
        ).executeAsync();
    }

   /* @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }*/

 /*   private void handleSignInResult(GoogleSignInResult result) {
        Log.d(AppUtils.TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            String authCode = "";
            authCode = acct.getServerAuthCode();

            if ( (!authCode.equals("")) && (!authCode.equals(null))) {
                // GetAccessTokenFromGoogle(authCode);
            }

        } else {
            Log.e(AppUtils.TAG,"GPLus Result Failure "+ result.getStatus());
            Toast.makeText(LoginActivity.this, "GPlus Login Failed" , Toast.LENGTH_SHORT).show();
        }
    }*/

    private void vefifyLoginDetailsFromServer(final String name, final String email, final String mobile_num) {

        // Instantiate the RequestQueue.
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        StringRequest postRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_LOGIN_VERIFICATION,
                this,this ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(APIParam.KEY_API, APIClass.API_KEY);
                params.put(APIParam.KEY_USERNAME, name);
                params.put(APIParam.KEY_MOBILE, mobile_num);

                if(email != null) {
                    params.put(APIParam.KEY_EMAIL, email);
                }

                return params;
            }
        };

        postRequest.setTag(REQUEST_TAG);
        mQueue.add(postRequest);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //  mTextView.setText(error.getMessage());
        VolleyLog.e(AppUtils.TAG +" Error: ", error.getMessage());
        // Toast.makeText(VerifyOTPActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
        AppUtils.showCustomAlertMessage(LoginActivity.this, "Login Account ",error.getMessage(), "OK", null, null);

    }

    @Override
    public void onResponse(Object response) {
        // Toast.makeText(VerifyOTPActivity.this, "Response: "+response.toString(), Toast.LENGTH_SHORT).show();
        Log.d(AppUtils.TAG +" Result: ", response.toString());
        try {
            String staus_res = null, login_status = null;

            JSONObject jsonObject = new JSONObject(String.valueOf(response));

            if (jsonObject.has("result")) {
                staus_res = jsonObject.getString("result");
            }

            if (jsonObject.has("login_permission")) {
                login_status = jsonObject.getString("login_permission");
            }

            if(staus_res != null && !staus_res.isEmpty()) {

                if(staus_res.equalsIgnoreCase("success") && login_status.equals("1")) {

                    String mobile_number = jsonObject.getString("mobile_num");

                    Log.d(AppUtils.TAG, " Login Status Success : " + mobile_number);
                    Intent i = new Intent(LoginActivity.this, FacebookVerifyOTPActivity.class);
                    i.putExtra("USER_NAME", LOGIN_USERNAME);
                    i.putExtra("USER_MOBILE", mobile_number);
                    i.putExtra("USER_EMAIL", LOGIN_EMAIL);
                    i.putExtra("USER_PROFILEPIC", fb_profile_image);
                    i.putExtra("USER_VERIFY_STATUS", "1");
                    startActivity(i);
                    finish();
                }
                else {
                    Log.d(AppUtils.TAG, " Login Status Failed");
                    Intent i = new Intent(LoginActivity.this, FacebookVerifyOTPActivity.class);
                    i.putExtra("USER_NAME", LOGIN_USERNAME);
                    i.putExtra("USER_MOBILE", LOGIN_MOBILRE);
                    i.putExtra("USER_EMAIL", LOGIN_EMAIL);
                    i.putExtra("USER_PROFILEPIC", fb_profile_image);
                    i.putExtra("USER_VERIFY_STATUS", "0");
                    startActivity(i);
                    finish();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        };

    }
}
