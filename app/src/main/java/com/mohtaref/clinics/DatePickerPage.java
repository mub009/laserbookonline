package com.mohtaref.clinics;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.os.Build;
import android.os.Bundle;

import com.mohtaref.clinics.utility.Constant;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;


import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.mohtaref.clinics.utility.Constant.AppURL;
import static com.mohtaref.clinics.utility.Constant.WhatsappMobile;


public class DatePickerPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    Button btnGet,btnOpenTicket;
    TextView tvw;
    DatePicker picker;
    ImageView Drprofile;
    TextView Drprofile_Details,DrprofileName;
    boolean isguest = false;
    LinearLayout profile_side;
    LinearLayout rating_side;
    LinearLayout appointment_side;
    LinearLayout group_login_real,LLdrprofile,LLproverselecter,LLTimePickerMain;
    HashMap<String, String> offer;
    Calendar calendar;
    String is_doctor;
    private Calendar lastSelectedCalendar = null;
    private static final String TAG = HttpHandlerPostToken.class.getSimpleName();
    DatePickerDialog dpd;
    final private String base_url = Constant.APIbaseLink;;
    HashMap<String, String> selected_provider;
    String token;
    ArrayList<Integer> days_off;
    ArrayList<HashMap<String, String>> providers_list;
    HashMap<String, String> Dr_Profile;
    HashMap<String, String> DrProfile;
    public ArrayList<String> provider_list_en;
    public ArrayList<String> provider_list_ar;
    ProgressDialog pd;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        loadlocal();
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_burger_icon_bar);
        Drprofile_Details=findViewById(R.id.Drprofile_Details);
        Drprofile=findViewById(R.id.Drprofile);

        DrprofileName=findViewById(R.id.DrprofileName);



// If you're calling this from a support Fragment
        //for arabic datapicker remove the commented part gg
//        Locale locale = new Locale("ar");
//        Locale.setDefault(locale);
//        Configuration config =
//                getBaseContext().getResources().getConfiguration();
//        config.setLocale(locale);
//        createConfigurationContext(config);

        //     picker=(DatePicker)findViewById(R.id.datePicker);
        tv = (TextView) findViewById(R.id.more);
        btnGet = (Button) findViewById(R.id.date_pick);
        btnOpenTicket= (Button) findViewById(R.id.openTicket);
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        // List<EventDay> events = new ArrayList<>();
        provider_list_en = new ArrayList<>();
        provider_list_ar = new ArrayList<>();
        DrProfile=new HashMap<String, String>();
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
            group_login_real = (LinearLayout) findViewById(R.id.group_login_real);

            group_login_real.setVisibility(View.VISIBLE);
            profile_side.setVisibility(View.GONE);
            rating_side.setVisibility(View.GONE);
            appointment_side.setVisibility(View.GONE);
        }

        LLdrprofile= (LinearLayout) findViewById(R.id.LLdrprofile);
        LLproverselecter=(LinearLayout) findViewById(R.id.LLproverselecter);
        LLTimePickerMain=(LinearLayout) findViewById(R.id.timepickermain);

        //Calendar calendar = Calendar.getInstance();
//        events.add(new EventDay(calendar, R.drawable.ic_person_black_24dp));

        //     calendarView.setEvents();

        //   picker.setMinDate(System.currentTimeMillis() - 1000);

        //cland.setMaxDate(max);
        // dialog.setTitle("Datepicker");
        // dialog.show();

        offer = (HashMap<String, String>) getIntent().getExtras().getSerializable("offer");




        Log.e("welcome to datepicker", "here is your offer " + offer.toString());
        //    String k= getIntent().getStringExtra("lat");
        //  Log.e("k= ","is "+k);

      /*  picker.getCalendarView().setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Log.d("tag", "finally found the listener, the date is: year " + year + ", month "  + month + ", dayOfMonth " + dayOfMonth);
            }
        });*/
      /*  btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvw.setText("Selected Date: "+ picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear());
            }
        });*/
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        btnOpenTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatePickerPage.this, Checkout.class);
                intent.putExtra("provider", new HashMap<>());
                intent.putExtra("offer", offer);
                intent.putExtra("appointmentDate", "openticket");
                startActivity(intent);
            }
        });


        days_off = new ArrayList<>();
        providers_list = new ArrayList<>();
        Dr_Profile=new HashMap<String, String>();

        new GetProviders().execute();
        new GetDaysoff().execute();

    }


    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }


    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }


    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "View More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }



    public void logout(View view) {
        SharedPreferences.Editor editor = getSharedPreferences("user", Activity.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finishAffinity();
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

    }

    public void rating_page(View view) {
        Intent i = new Intent(this, Rating.class);
        startActivity(i);
        overridePendingTransition(0, 0);

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
     //   setLocale(lng);
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


    public void alert_message_net(String title, String body,Context context) {
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




    public class GetDaysoff extends AsyncTask<Void, Void, Void> {
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
            String urlget = base_url + "getOffDays";
            String jsonStr = null;

            // String jsonStr = sh.makeServiceCall(urlget, token, "", "");
            // String response = null;
            try {
                URL url = new URL(urlget);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Client-Auth-Token", "Bearer"+" "+token);
                // String offerid=ClinicData.get("offerId");
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("clinicId",offer.get("clinicId") );
                //     jsonParam.put("offerId",offerid);


                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                Log.e("Before sending", "1" + jsonParam.toString());
                wr.writeBytes(jsonParam.toString());

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
            Log.e("gg mynigga","yeaah");
            Log.e("is offers :", "Response from url countries: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONArray c = new JSONArray(jsonStr);

                    //     listDataHeader.add(clinic_ob);
                    for (int i = 0; i < c.length(); i++) {
                        int day= (Integer) c.get(i)+1;

                        days_off.add(day);



                    }
                    Log.e("nery list is :",""+days_off.toString());

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

        @SuppressLint("ResourceAsColor")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
            String lng = pref.getString("Mylang", "");
            Log.e("language", lng);
            if (lng.equals("ar")){
                Locale locale = new Locale("ar");
                Locale.setDefault(locale);
            }
            else {
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
            }

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            Calendar now = Calendar.getInstance();
             dpd= DatePickerDialog.newInstance(
                    DatePickerPage.this,
                    now.get(Calendar.YEAR), // Initial year selection
                    now.get(Calendar.MONTH), // Initial month selection
                    now.get(Calendar.DAY_OF_MONTH)// Inital day selection
            );

            dpd.setFirstDayOfWeek(Calendar.SUNDAY);
            Calendar min_date_c = Calendar.getInstance();
            Log.e("current date","is "+min_date_c);
            //dpd.setMinDate(min_date_c);
            //minimum date
            String currentDateTimeString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            SimpleDateFormat formmat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date today=null;
            try {
                today=formmat1.parse(currentDateTimeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(offer.get("offer_start")!=null){
                String strDate=offer.get("offer_start");


                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = fmt.parse(strDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                Log.e("date now right now is","da "+formmat1);

//            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
//             fmtOut.format(date);
                Calendar cals= Calendar.getInstance();
                cals.setTime(date);

                if(today.equals(date)){
                    dpd.setMinDate(cals);



                }
                else if(today.after(date)){
                    dpd.setMinDate(min_date_c);

                }
                else{
                    dpd.setMinDate(cals);

                }
            }
            else{
                dpd.setMinDate(min_date_c);

            }

            if(offer.get("offer_end")!=null){
                String endOffer=offer.get("offer_end");

                Calendar max_date_c = Calendar.getInstance();

                SimpleDateFormat fmts = new SimpleDateFormat("yyyy-MM-dd");
                Date offerDateEnd = null;
                try {

                    offerDateEnd = fmts.parse(endOffer);
                    max_date_c.setTime(offerDateEnd);
                    dpd.setMaxDate(max_date_c);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            else {
                Calendar max_date_c = Calendar.getInstance();

                max_date_c.add(Calendar.YEAR, 1);
                Log.e("date end ","dat "+max_date_c);
                // add 4 years to min date to have 2 years after now
                dpd.setMaxDate(max_date_c);
            }

            Calendar max_date_c = Calendar.getInstance();
            max_date_c.add(Calendar.YEAR, 1);
            //dateTextView = (TextView) findViewById(R.id.dateTextView);

            Log.e("array of services", "size is " + Calendar.SUNDAY);
            Log.e("array of services", "size is " + days_off.toString());
            for (Calendar loopdate = min_date_c; min_date_c.before(max_date_c); min_date_c.add(Calendar.DATE, 1), loopdate = min_date_c) {
                int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
                for (int i = 0; i < days_off.size(); i++) {
                    if (dayOfWeek == days_off.get(i)) {

                        Calendar[] disabledDays = new Calendar[1];
                        disabledDays[0] = loopdate;
                        dpd.setDisabledDays(disabledDays);
                    }

                }
            }
            dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                    String date =year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                    Log.e("picked date is ",": "+date);
                    Intent intent=new Intent(DatePickerPage.this,TimePickerPage.class);
                    intent.putExtra("provider",selected_provider);
                    intent.putExtra("offer",offer);
                    intent.putExtra("appointmentDate",date);
                    intent.putExtra("providers_en",provider_list_en);
                    intent.putExtra("providers_ar",provider_list_ar);
                    intent.putExtra("providers_list",providers_list);

                    startActivity(intent);

                }
            });
            pd.dismiss();

            dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Log.d("TimePicker", "Dialog was cancelled");
                  //  onBackPressed();
                }
            });//            DatePickerDialog dpds = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag("Datepickerdialog");

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Calendar c=Calendar.getInstance();
            c.get(Calendar.YEAR); // I forgot if you need the ; here when using Java
            c.get(Calendar.MONTH);
            c.get(Calendar.DAY_OF_MONTH);
            //dpd.show(fm, "Datepickerdialog");

            dpd.setAccentColor(getColor(R.color.laser_pink));
           // dpd.show(ft,"DatePickerDialog");
           // dpd.show(fm, "DatePickerDialog");

            btnGet=(Button)findViewById(R.id.date_pick);
            btnGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dpd.show(getFragmentManager(), "DatePickerDialog");

                }
            });
//            ft.commitAllowingStateLoss();
        }

    }

    public class GetProviders extends AsyncTask<Void, Void, Void> {
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
            String urlget = base_url + "providersDetails";
            String jsonStr = null;

            // String jsonStr = sh.makeServiceCall(urlget, token, "", "");
            // String response = null;
            try {
                URL url = new URL(urlget);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Client-Auth-Token", "Bearer" + " " + token);
                // String offerid=ClinicData.get("offerId");
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("serviceId", offer.get("serviceId"));
                //     jsonParam.put("offerId",offerid);


                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                Log.e("Before sending", "1" + jsonParam.toString());
                wr.writeBytes(jsonParam.toString());

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
                    JSONObject providers = new JSONObject(jsonStr);

                    JSONArray c=providers.getJSONArray("providers");
                    is_doctor=providers.getString("is_doctor");

                    if(is_doctor.equals("1"))
                    {
                        JSONObject DrDetails=providers.getJSONObject("DrDetails");

                        // tmp hash map for single offer
                        HashMap<String, String> Dr_Profile_ob = new HashMap<>();

                       // selected_provider=DrDetails.getString("employeeId");
                        Dr_Profile_ob.put("AboutUs_ar", DrDetails.getString("AboutUs_ar"));
                        Dr_Profile_ob.put("AboutUs_en", DrDetails.getString("AboutUs_en"));
                        Dr_Profile_ob.put("employeeName_en", DrDetails.getString("employeeName_en"));
                        Dr_Profile_ob.put("employeeName_ar", DrDetails.getString("employeeName_ar"));
                        Dr_Profile_ob.put("image", DrDetails.getString("image"));
                        Dr_Profile=Dr_Profile_ob;
                    }
                   // is_doctor
                    //providers

                    //     listDataHeader.add(clinic_ob);
                    for (int i = 0; i < c.length(); i++) {
                        JSONObject provider = c.getJSONObject(i);

                        String nationalityName_en = provider.getString("nationalityName_en");
                        String nationalityName_ar = provider.getString("nationalityName_ar");
                        String employeeId = provider.getString("employeeId");
                        String employeeSeed = provider.getString("employeeSeed");
                        String role = provider.getString("role");
                        String employeeName_en = provider.getString("employeeName_en");
                        String employeeName_ar = provider.getString("employeeName_ar");
                        String gender = provider.getString("gender");
                        String nationalityId = provider.getString("nationalityId");
                        String email = provider.getString("email");
                        String username = provider.getString("username");
                        String password = provider.getString("password");
                        String lang = provider.getString("lang");
                        String permissions = provider.getString("permissions");
                        String device_token = provider.getString("device_token");
                        String forgot_code = provider.getString("forgot_code");
                        String active = provider.getString("active");
                        String deleted = provider.getString("deleted");
                        String created_at = provider.getString("created_at");
                        String last_modify = provider.getString("last_modify");

                        // tmp hash map for single offer
                        HashMap<String, String> Categories_ob = new HashMap<>();

                        // adding each child node to HashMap key => value

                        Categories_ob.put("nationalityName_en", nationalityName_en);
                        Categories_ob.put("nationalityName_ar", nationalityName_ar);
                        Categories_ob.put("employeeId", employeeId);
                        Categories_ob.put("employeeSeed", employeeSeed);
                        Categories_ob.put("role", role);
                        Categories_ob.put("employeeName_en", employeeName_en);
                        Categories_ob.put("employeeName_ar", employeeName_ar);
                        Categories_ob.put("gender", gender);
                        Categories_ob.put("nationalityId", nationalityId);
                        Categories_ob.put("email", email);
                        Categories_ob.put("username", username);
                        Categories_ob.put("password", password);
                        Categories_ob.put("lang", lang);
                        Categories_ob.put("permissions", permissions);
                        Categories_ob.put("device_token", device_token);
                        Categories_ob.put("forgot_code", forgot_code);
                        Categories_ob.put("active", active);
                        Categories_ob.put("deleted", deleted);
                        Categories_ob.put("created_at", created_at);
                        Categories_ob.put("last_modify", last_modify);
                        provider_list_en.add(employeeName_en);
                        provider_list_ar.add(employeeName_ar);
                        providers_list.add(Categories_ob);


                    }
                    Log.e("nery list is :", "" + providers_list.toString());

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

            SharedPreferences pref=getSharedPreferences("Settings",Activity.MODE_PRIVATE);
            String lng=pref.getString("Mylang","");
            Log.e("language",lng);
            Log.e("f;aj;",provider_list_en.toString());
            Log.e("f;aj;",provider_list_ar.toString());

//            final TextInputLayout city=(TextInputLayout)findViewById(R.id.city_input);
//
//            city.setVisibility(View.VISIBLE);
            if(lng.equals("ar")){

                final Spinner provider_spinner = (Spinner) findViewById(R.id.service_provider);
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        DatePickerPage.this,
                        R.layout.simple_spinner_dropdown_item,
                        provider_list_ar
                );
                adapter.insert("اي مقدم خدمة",0);
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

                provider_spinner.setAdapter(adapter);
                //  adapter.notifyDataSetChanged();
                //    int spinnerPosition = adapter.getPosition("المدينة");
                //provider_spinner.setSelection(0);
                // spinner_region.setBackgroundResource(arrow_left);

                if(is_doctor.equals("1"))
                {

                    LLdrprofile.setVisibility(View.VISIBLE);
                    LLTimePickerMain.setGravity(Gravity.NO_GRAVITY);
                    LLproverselecter.setVisibility(View.GONE);
                    Drprofile_Details.setText(Dr_Profile.get("AboutUs_ar"));
                    if(!Dr_Profile.get("image").equals(""))
                    {
                        Picasso.get().load(Dr_Profile.get("image")).fit().into(Drprofile);
                    }
                    DrprofileName.setText(Dr_Profile.get("employeeName_ar"));

                }
                else
                {
                    provider_spinner.setSelection(0);
                }



                if(Drprofile_Details.getLineCount()>2)
                {
                    makeTextViewResizable(Drprofile_Details, 2, "عرض المزيد", true);
                }

            }
            else{

                final Spinner provider_spinner = (Spinner) findViewById(R.id.service_provider);
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        DatePickerPage.this,
                        R.layout.simple_spinner_dropdown_item,
                        provider_list_en
                );
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

                adapter.insert("Any Provider",0);


                provider_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int pos, long id) {

                        if (pos ==0) {

                            //     To reset a spinner to default value:

                            selected_provider=null;
                        } else{
                            selected_provider=providers_list.get(pos-1);

                        }


                        Log.e("selected is :","dat "+selected_provider);

                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                        Log.e("noting :","dat ");

                    }

                });
                provider_spinner.setAdapter(adapter);
                //  adapter.notifyDataSetChanged();
                //int spinnerPosition = adapter.getPosition("City");
                if(is_doctor.equals("1"))
                {
                    LLTimePickerMain.setGravity(Gravity.NO_GRAVITY);
                    LLdrprofile.setVisibility(View.VISIBLE);
                    LLproverselecter.setVisibility(View.GONE);
//
                    Drprofile_Details.setText(Dr_Profile.get("AboutUs_en"));
                    if(!Dr_Profile.get("image").equals(""))
                    {
                        Picasso.get().load(Dr_Profile.get("image")).fit().into(Drprofile);

                    }
                    DrprofileName.setText(Dr_Profile.get("employeeName_en"));
                }
                else
                {
                    provider_spinner.setSelection(0);
                }

                if(Drprofile_Details.getLineCount()>2)
                {
                    makeTextViewResizable(Drprofile_Details, 2, Drprofile_Details.getLineCount()+"View More", true);
                }

            }



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
}
