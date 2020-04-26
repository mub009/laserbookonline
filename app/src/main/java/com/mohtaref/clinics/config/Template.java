package com.mohtaref.clinics.config;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mohtaref.clinics.About_us;
import com.mohtaref.clinics.HomePage;
import com.mohtaref.clinics.LoginActivity;
import com.mohtaref.clinics.MyAppointments;
import com.mohtaref.clinics.Profile;
import com.mohtaref.clinics.R;
import com.mohtaref.clinics.Rating;
import com.mohtaref.clinics.SearchActivity;
import com.mohtaref.clinics.fragment.HomePageFragment;
import com.mohtaref.clinics.fragment.MoreFragment;
import com.mohtaref.clinics.fragment.ProfileFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import static com.mohtaref.clinics.utility.Constant.AppURL;
import static com.mohtaref.clinics.utility.Constant.WhatsappMobile;

public class Template extends AppCompatActivity {

    Fragment HomePageFragmentObj;
    EditText search_bar;
    TextView languageEnglish,languageArabic;
    boolean isguest = false;
    LinearLayout profile_side;
    LinearLayout rating_side;
    LinearLayout appointment_side;
    LinearLayout group_login_real;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainfragment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        variableInitialize();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_burger_icon_bar);
        search_bar = (EditText) findViewById(R.id.Search);
        SharedPreferences pref = getSharedPreferences("user", Activity.MODE_PRIVATE);
        String data = pref.getString("data", "");
        try {
            JSONObject userdata = new JSONObject(data);
            isguest = userdata.getBoolean("isGuest");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (isguest) {
            profile_side = (LinearLayout) findViewById(R.id.group_login);
            rating_side = (LinearLayout) findViewById(R.id.group_Rating);
            appointment_side = (LinearLayout) findViewById(R.id.group_my_appointments);
            group_login_real = (LinearLayout) findViewById(R.id.group_login_real);
            profile_side.setVisibility(View.GONE);
            rating_side.setVisibility(View.GONE);
            appointment_side.setVisibility(View.GONE);
            group_login_real.setVisibility(View.VISIBLE);
          }


        languageEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lang="en";
                Locale myLocale = new Locale(lang);
                Locale.setDefault(myLocale);
                android.content.res.Configuration config = new android.content.res.Configuration();
                config.locale = myLocale;
                getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
                SharedPreferences.Editor editor=getSharedPreferences("Settings", Activity.MODE_PRIVATE).edit();
                editor.putString("Mylang",lang);
                editor.apply();
                Intent intent=getIntent();
                finish();
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.HomePageFragmentId, new HomePageFragment())
//                .commit();


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.HomePageFragmentId, new MoreFragment())
                .commit();


    }

    public void variableInitialize()
    {
        languageEnglish=(TextView) findViewById(R.id.English);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {



        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_home:
//                    toolbar.setTitle("Shop");
                    fragment = new HomePageFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_consultation:
//                    toolbar.setTitle("My Gifts");
                    fragment = new HomePageFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_profile:
//                    toolbar.setTitle("Cart");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_more:
            //        toolbar.setTitle("Profile");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.HomePageFragmentId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    //Language

    public void setLocale_en(View view ) {
        String lang="en";
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor=getSharedPreferences("Settings", Activity.MODE_PRIVATE).edit();
        editor.putString("Mylang",lang);
        editor.apply();
        Intent intent=getIntent();
        finish();
        startActivity(intent);
        overridePendingTransition(0, 0);

    }
    public void setLocale_ar(View view) {
        String lang="ar";
        Locale myLocale = new Locale(lang);
        //saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor=getSharedPreferences("Settings", Activity.MODE_PRIVATE).edit();
        editor.putString("Mylang",lang);
        editor.apply();
        Intent intent=getIntent();
        finish();
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    //Side Menu Onclick

    public void share_app(View view) {
        try {
            Intent i = new Intent(getApplicationContext(), Template.class);
            i.putExtra("transaction_id", "8686695");
            startActivity(i);
            finishAffinity();
        }catch (Exception e) {
            e.printStackTrace ();
        }

    }

    public void Whatsapp(View view){

        try {
            Intent intent = new Intent ( Intent.ACTION_VIEW );
            intent.setData ( Uri.parse ( "https://wa.me/" + WhatsappMobile + "/?text=" + "" ) );
            startActivity ( intent );
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
    public void searchPage(View view) {
        Intent i = new Intent(this, SearchActivity.class);
        startActivity(i);
        overridePendingTransition(0, 0);

    }

    public void home_page(View view) {
        finish();
        startActivity(getIntent());

    }

    public void rating_page(View view) {
        Intent i = new Intent(getApplicationContext(), Rating.class);
        startActivity(i);
        overridePendingTransition(0, 0);

    }

    public void my_appointments(View view) {
        Intent i = new Intent(getApplicationContext(), MyAppointments.class);
        startActivity(i);
        overridePendingTransition(0, 0);

    }

    public void profile(View view) {
        Intent i = new Intent(getApplicationContext(), Profile.class);
        startActivity(i);
        overridePendingTransition(0, 0);

    }


    public void About_us(View view) {
        Intent i = new Intent(getApplicationContext(), About_us.class);
        startActivity(i);
        overridePendingTransition(0, 0);

    }

    public void Privacy_policy_page(View view) {
        Uri uri = Uri.parse("https://laserbookingonline.com/Privacy_Policy/"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }

    public void Terms_page(View view) {
        SharedPreferences pref=getSharedPreferences("Settings",Activity.MODE_PRIVATE);
        String lng=pref.getString("Mylang","");
        Uri uri=Uri.parse(AppURL+"terms_"+lng+".php"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }

    public void Register_Your_Clinic(View view) {
        SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String lng = pref.getString("Mylang", "");
        Log.e("language", lng);
        if (lng.equals("ar")) {
            Uri uri = Uri.parse("https://laserbookingonline.com/register.php?lang=ar"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else {
            Uri uri = Uri.parse("https://laserbookingonline.com/register.php?lang=en"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }


    }

    public void callNumPhone(View view) {

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getResources().getString(R.string.phone_number)));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);



    }


    public void logout(View view) {
        SharedPreferences.Editor editor=getSharedPreferences("user", Activity.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        Intent i=new Intent(this, LoginActivity.class);
        startActivity(i);
        finishAffinity();
        System.exit(0);

    }

    //social media


    public void FaceBook(View view) {
        Uri uri = Uri.parse("https://www.facebook.com/bookinglaser/"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }
    public void Snapchat(View view) {
        Uri uri = Uri.parse("https://www.snapchat.com/add/laserbooking"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }
    public void Twitter(View view) {
        Uri uri = Uri.parse("https://twitter.com/bookinglaser"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }
    public void Instagram(View view) {
        Uri uri = Uri.parse("https://www.instagram.com/laser.booking/"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }



}
