package com.mohtaref.clinics;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.mohtaref.clinics.utility.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class Splash_Screen extends AppCompatActivity {
String version_name;
int version_code;
String base_url= Constant.APIbaseLink;;
HashMap<String,String> version_app;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    Branch.getInstance().initSession(branchReferralInitListener, getIntent() != null ?
            getIntent().getData() : null, this);


//        version_name= BuildConfig.VERSION_NAME;
//        version_code=BuildConfig.VERSION_CODE;
//        if(isConnected(this))
//        new CheckVersion().execute();
//        else {
    if(isConnected(this)){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent=new Intent(Splash_Screen.this,LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        }, 1500);
    }


//        }


    }


    public void Appupdation()
    {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(Splash_Screen.this);
        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            100);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                // Request the update.
            }
        });
    }


    @Override public void onStart() {
        super.onStart();
        // listener (within Main Activity's onStart)
        Branch.getInstance().initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {

                    // option 1: log data
                    Log.i("BRANCH SDK", referringParams.toString());

                    // option 2: save data to be used later
                    SharedPreferences preferences = Splash_Screen.this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    preferences.edit().putString("branchData", referringParams.toString()).apply();

                    // option 3: navigate to page
                    Intent intent = new Intent(Splash_Screen.this, HomePage.class);
                    startActivity(intent);

                    // option 4: display data
                    Toast.makeText(Splash_Screen.this, referringParams.toString(), Toast.LENGTH_LONG).show();

                } else {
                    Log.i("BRANCH SDK", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // if activity is in foreground (or in backstack but partially visible) launching the same
        // activity will skip onStart, handle this case with reInitSession
        Branch.getInstance().reInitSession(this, branchReferralInitListener);
    }
    private Branch.BranchReferralInitListener branchReferralInitListener =
            new Branch.BranchReferralInitListener() {
                @Override
                public void onInitFinished(@Nullable JSONObject referringParams, @Nullable BranchError error) {

                    try {
                        String navLink=referringParams.get("$canonical_identifier").toString();
                        if(navLink.equals("inviting"));
                        {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            };





    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null&&netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null&& mobile.isConnectedOrConnecting()) || (wifi != null&&wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }




    public void setLocale(String lang) {
        // get language return 2 first letters lower case of the language //// in the other hand get display language return the name of the language
        String CurrentLang = Locale.getDefault().getDisplayLanguage();
        String Applang= Resources.getSystem().getConfiguration().locale.getDisplayLanguage();




        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        conf.setLocale(new Locale(lang));
        res.updateConfiguration(conf, dm);
        SharedPreferences.Editor editor=getSharedPreferences("Settings", Activity.MODE_PRIVATE).edit();
        editor.putString("Mylang",lang);
        editor.apply();
        if(!CurrentLang.equals(Applang)){
            recreate(); }



    }
    public void loadlocal(){
        SharedPreferences pref=getSharedPreferences("Settings",Activity.MODE_PRIVATE);
        String lng=pref.getString("Mylang","");
        if(lng!=null&&lng!="")
        setLocale(lng);
        else{
            String lang=Resources.getSystem().getConfiguration().locale.getLanguage();
            SharedPreferences.Editor editor=getSharedPreferences("Settings", Activity.MODE_PRIVATE).edit();
            editor.putString("Mylang",lang);
            editor.apply();
            setLocale(lang);

        }
    }

    public void setLocale_en(View view ) {
        String lang="en";
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        conf.setLocale(new Locale(lang));
        res.updateConfiguration(conf, dm);
        SharedPreferences.Editor editor=getSharedPreferences("Settings", Activity.MODE_PRIVATE).edit();
        editor.putString("Mylang",lang);
        editor.apply();
        recreate();
    }
    public void setLocale_ar(View view) {
        String lang="ar";
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        conf.setLocale(new Locale(lang));
        res.updateConfiguration(conf, dm);

        SharedPreferences.Editor editor=getSharedPreferences("Settings", Activity.MODE_PRIVATE).edit();
        editor.putString("Mylang",lang);
        editor.apply();
        recreate();
    }



}
