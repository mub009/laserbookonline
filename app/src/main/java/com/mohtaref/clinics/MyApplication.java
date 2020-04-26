package com.mohtaref.clinics;

import android.app.Activity;
import android.app.Application;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        ConnectivityReceiver connectivityReceiver=new ConnectivityReceiver();
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver,filter);

        SharedPreferences pref=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String lng=pref.getString("Mylang","");
        if(lng.equals(""))
        {
            setLocale("en");
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


        }
        else
        {
            setLocale(lng);
        }

        mInstance = this;

        // Branch logging for debugging
        Branch.enableDebugMode();

        // Branch object initialization
        Branch.getAutoInstance(this);

        JSONObject params = Branch.getInstance().getFirstReferringParams();

        try {
            if ((params.has("+clicked_branch_link")) && (params.getBoolean("+clicked_branch_link")))
            Branch.getInstance(getApplicationContext()).userCompletedAction("installAfterReferral");
        } catch (JSONException e) {
            e.printStackTrace();
        }


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
             }



    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
