package com.mohtaref.clinics;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import static com.mohtaref.clinics.utility.Constant.AppURL;
import static com.mohtaref.clinics.utility.Constant.WhatsappMobile;

public class Rating extends AppCompatActivity{

    String token;
    String lat;
    String lng;
    ArrayList<HashMap<String, String>> unrated_list;
    ListView unrated_list_view;
    private static final String TAG = HttpHandlerPostToken.class.getSimpleName();
    final private String base_url = Constant.APIbaseLink;;
    ProgressDialog pd;

    boolean isguest = false;
    LinearLayout profile_side;
    LinearLayout rating_side;
    LinearLayout appointment_side;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadlocal();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_burger_icon_bar);
        unrated_list = new ArrayList<>();


        SharedPreferences prefg = getSharedPreferences("user", Activity.MODE_PRIVATE);
        String data = prefg.getString("data", "");
        try {
            JSONObject userdata = new JSONObject(data);
            isguest = userdata.getBoolean("isGuest");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("user is guest :", "" + isguest);

        if (isguest) {
            profile_side = (LinearLayout) findViewById(R.id.group_login);
            rating_side = (LinearLayout) findViewById(R.id.group_Rating);
            appointment_side = (LinearLayout) findViewById(R.id.group_my_appointments);
            profile_side.setVisibility(View.GONE);
            rating_side.setVisibility(View.GONE);
            appointment_side.setVisibility(View.GONE);
        }
        new UnratedAppointments().execute();
    }






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


    public void alert_message_net(String title, String body, Context context) {
        android.support.v7.app.AlertDialog.Builder alertDialog=new android.support.v7.app.AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(body);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        android.support.v7.app.AlertDialog dialog=alertDialog.create();
        dialog.show();
        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected(context))
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();


                            finish();
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);


                        }
                    }, 1000);
                    Log.e("is connected","ture");
                }
            }
        });

    }



    public class UnratedAppointments extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // getLastLocation();
//            categories_list.clear();

            SharedPreferences pref = getSharedPreferences("user", Activity.MODE_PRIVATE);
            String data = pref.getString("data", "");
            try {
                JSONObject userdata = new JSONObject(data);
                token = userdata.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("user token is :", token);


//            "categoryId":"1", "lat": "", "lng": "","pager":"1"
//


        }

        @Override
        protected Void doInBackground(Void... arg0) {
//            final FormActivity formobject=new FormActivity();

            // Making a request to url and getting response
            String urlget = base_url + "clientUnratedAppointments";
            String jsonStr = null;

            // String jsonStr = sh.makeServiceCall(urlget, token, "", "");
            // String response = null;
            try {
                URL url = new URL(urlget);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Client-Auth-Token", "Bearer" + " " + token);
                // String offerid=ClinicData.get("offerId");


                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                jsonStr = convertStreamToString(in);
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            //   return response;
            Log.e("gg mynigga", "yeaah");
            Log.e("is offers :", "Response from url countries: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject c = jsonarray.getJSONObject(i);
                        String reservationId = c.getString("reservationId");
                        String clientId = c.getString("clientId");

                        String clinicId = c.getString("clinicId");
                        String status = c.getString("status");
                        String appointmentDate = c.getString("appointmentDate");
                        String totalDuration = c.getString("totalDuration");
                        String totalCost = c.getString("totalCost");
                        String paymentType = c.getString("paymentType");
                        String source = c.getString("source");
                        String feedbackId = c.getString("feedbackId");
                        String rejectReason = c.getString("rejectReason");
                        String cancelReason = c.getString("cancelReason");
                        String rate_notified = c.getString("rate_notified");
                        String paid = c.getString("paid");
                        String modified = c.getString("modified");
                        String deleted = c.getString("deleted");
                        String fixed = c.getString("fixed");
                        String created_at = c.getString("created_at");
                        String last_modify = c.getString("last_modify");
                        String employeeName_en = c.getString("employeeName_en");
                        String employeeName_ar = c.getString("employeeName_ar");
                        String serviceName_en = c.getString("serviceName_en");
                        String serviceName_ar = c.getString("serviceName_ar");

                        String clinicName_en = c.getString("clinicName_en");
                        String clinicName_ar = c.getString("clinicName_ar");
                        String description_en = c.getString("description_en");
                        String description_ar = c.getString("description_ar");
                        String address_en = c.getString("address_en");
                        String address_ar = c.getString("address_ar");
                        String lat_ob = c.getString("lat");
                        String lng_ob = c.getString("lng");

                        String IBAN = c.getString("IBAN");
                        String phone = c.getString("phone");
                        String clinicMobile = c.getString("clinicMobile");
                        String clinicEmail = c.getString("clinicEmail");
                        String logo = c.getString("logo");
                        String locked_period = c.getString("locked_period");
                        String active = c.getString("active");


                        HashMap<String, String> offer_ob = new HashMap<>();

                        // adding each child node to HashMap key => value
                        offer_ob.put("reservationId", reservationId);
                        offer_ob.put("clientId", clientId);
                        offer_ob.put("clinicId", clinicId);
                        offer_ob.put("status", status);
                        offer_ob.put("appointmentDate", appointmentDate);
                        offer_ob.put("totalDuration", totalDuration);
                        offer_ob.put("totalCost", totalCost);
                        offer_ob.put("paymentType", paymentType);
                        offer_ob.put("source", source);
                        offer_ob.put("feedbackId", feedbackId);
                        offer_ob.put("rejectReason", rejectReason);
                        offer_ob.put("cancelReason", cancelReason);
                        offer_ob.put("rate_notified", rate_notified);
                        offer_ob.put("paid", paid);
                        offer_ob.put("modified", modified);
                        offer_ob.put("fixed", fixed);
                        offer_ob.put("employeeName_en", employeeName_en);
                        offer_ob.put("employeeName_ar", employeeName_ar);
                        offer_ob.put("serviceName_en", serviceName_en);
                        offer_ob.put("serviceName_ar", serviceName_ar);
                        offer_ob.put("description_en", description_en);
                        offer_ob.put("description_ar", description_ar);
                        offer_ob.put("address_en", address_en);
                        offer_ob.put("address_ar", address_ar);
                        offer_ob.put("lat", lat_ob);
                        offer_ob.put("lng", lng_ob);
                        offer_ob.put("IBAN", IBAN);
                        offer_ob.put("phone", phone);
                        offer_ob.put("clinicMobile", clinicMobile);
                        offer_ob.put("clinicEmail", clinicEmail);
                        offer_ob.put("logo", logo);
                        offer_ob.put("locked_period", locked_period);

                        offer_ob.put("active", active);
                        offer_ob.put("deleted", deleted);
                        offer_ob.put("created_at", created_at);
                        offer_ob.put("last_modify", last_modify);

                        offer_ob.put("clinicName_en", clinicName_en);
                        offer_ob.put("clinicName_ar", clinicName_ar);


                        unrated_list.add(offer_ob);

                    }


                } catch (final JSONException e) {
                    Log.e("", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //  formobject.buildDialog(R.style.DialogAnimation, "NO Records found","ok");


                        }
                    });

                }

            } else {
                Log.e("", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

//                        Toast.makeText(getApplicationContext(),
//                                "Couldn't get data from server. Check your internet connection or try later",
//                                Toast.LENGTH_LONG).show();

                    }
                });
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
            String lng = pref.getString("Mylang", "");
            Log.e("language", lng);

            unrated_list_view = (ListView) findViewById(R.id.rating_list);
            //set the adapter of CustomList Adapter
            CustomListAdapterRating adapter = new CustomListAdapterRating(
                    getApplicationContext(), R.layout.rating_list_item, unrated_list, lng
            );

            unrated_list_view.setAdapter(adapter);

            unrated_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String, String> rate_appointment = (HashMap<String, String>) parent.getAdapter().getItem(position);
                    Log.e("clicked detail appoi", "is " + rate_appointment);
                    Intent intent = new Intent(Rating.this, RateAppointment.class);
                    intent.putExtra("rate_appointment", rate_appointment);
                    startActivity(intent);
                    //here i want to get the items
                }
            });
            pd.dismiss();
        }

    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public void searchPage(View view) {
        Intent i = new Intent(this, SearchActivity.class);
        startActivity(i);
        overridePendingTransition(0, 0);

    }

    public void home_page(View view) {
        Intent i = new Intent(this, HomePage.class);
        startActivity(i);
        finishAffinity();
        overridePendingTransition(0, 0);
        System.exit(0);

    }

    public void rating_page(View view) {
        Intent i = new Intent(this, Rating.class);
        startActivity(i);
        overridePendingTransition(0, 0);

    }

    public void share_app(View view) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage;
            shareMessage =  "https://labookingonline.app.link/3H1D5xI9w3"+"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Choose One"));
        } catch(Exception e) {
            //e.toString();
        }

    }

    public void my_appointments(View view) {
        Intent i = new Intent(this, MyAppointments.class);
        startActivity(i);
        overridePendingTransition(0, 0);

    }

    public void profile(View view) {
        Intent i = new Intent(this, Profile.class);
        startActivity(i);
        overridePendingTransition(0, 0);

    }


    public void About_us(View view) {
        Intent i = new Intent(this, About_us.class);
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
        startActivity(intent);

    }
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

    public void Whatsapp(View view){

        try {
            Intent intent = new Intent ( Intent.ACTION_VIEW );
            intent.setData ( Uri.parse ( "https://wa.me/" + WhatsappMobile + "/?text=" + "" ) );
            startActivity ( intent );
        } catch (Exception e) {
            e.printStackTrace ();
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
            recreate(); }



    }
    public void loadlocal(){
        SharedPreferences pref=getSharedPreferences("Settings",Activity.MODE_PRIVATE);
        String lng=pref.getString("Mylang","");
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        if(lng.equals("ar")){
            pd.setMessage("جاري التحميل");

        }
        else{
            pd.setMessage("Loading");

        }

        pd.show();
       // setLocale(lng);
    }

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
        finishAffinity();
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
        finishAffinity();
        startActivity(intent);
        overridePendingTransition(0, 0);
    }


}
