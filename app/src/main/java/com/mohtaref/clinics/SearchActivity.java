package com.mohtaref.clinics;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mohtaref.clinics.utility.Constant;

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
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends Activity{

    public EditText search = null;
    ArrayAdapter<String> adapter = null;
    ArrayList<HashMap<String, String>> search_list=null;
    ArrayList<HashMap<String, String>> search_list_ar=null;
    private Timer timer=new Timer();
    private final long DELAY = 500; // milliseconds
    String token;
    String search_string;
    TextView result_count;
    private String url_base= Constant.APIbaseLink;;
    ArrayList<HashMap<String, String>>Search_data;
    public ArrayList<String> Search_names_en;
    String z="";
    public ArrayList<String> Search_names_ar;
    ListView serachListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Search_data = new ArrayList<>();
        search_list_ar=new ArrayList<>();
        search_list=new ArrayList<>();
        setContentView(R.layout.activity_search);
        ArrayList<HashMap<String, String>> categories_list;
        serachListView=(ListView)findViewById(R.id.list);
       // new Searchdata().execute();
        search = (EditText) findViewById(R.id.Search);
        search.addTextChangedListener(filterTextWatcher);
        Button backButton = (Button)this.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        finish();
    }


//            timer.cancel();
//    timer = new Timer();
//            timer.schedule(
//                    new TimerTask() {
//        @Override
//        public void run() {
//            new Searchdata(s).execute();
//
//        }
//    },
//    DELAY
//            );
    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(final Editable s) {

//            adapter.getFilter().filter(s);
            timer.cancel();
            timer = new Timer();
            timer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            new Searchdata(s).execute();

                        }
                    },
                    DELAY
            );

        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        search.removeTextChangedListener(filterTextWatcher);

    }
    @Override
    public void onPause() {
        super.onPause();
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



    public class Searchdata extends AsyncTask<Void, Void, String> {
        boolean state = false;
        final Editable as;
        public Searchdata(Editable s) {
        as=s;
        }

        //0531042553/123456  phone + pass

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Search_data.clear();
            search_list_ar.clear();
            search_list.clear();
            if(adapter!=null){
                adapter.clear();
            }

            SharedPreferences pref = getSharedPreferences("user", Activity.MODE_PRIVATE);
            String data = pref.getString("data", "");
            try {
                JSONObject userdata = new JSONObject(data);
                token = userdata.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("user token is :", token);
            Log.e("stringa is ","dat "+search.getText().toString());
            search_string= search.getText().toString();
        }


        @Override
        protected String doInBackground(Void... params) {

            String response = null;
            String jsonStr = null;
            try {
                URL url = new URL(url_base+"searchwithdoctor"); //Enter URL here
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                httpURLConnection.setRequestProperty("Client-Auth-Token", "Bearer"+" "+token);
                httpURLConnection.connect();


                JSONObject jsonParam = new JSONObject();

                Log.e("text sent is","this "+ URLDecoder.decode(search_string,"UTF-8"));
                Log.e("text sent is","this "+search_string);
                Log.e("text sent is gg ","this "+search_string);


                try {
                    jsonParam.put("text", search_string);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                Log.e("Before sending", "1" + jsonParam.toString());
               wr.writeBytes(jsonParam.toString());
                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                response = convertStreamToString(in);
                Log.e("real data is ","this "+response);
                jsonStr = response;


                wr.flush();
                wr.close();

                Log.e("result ", "Response from url countries: " + jsonStr);
                if(jsonStr!=null){
                    try {
                        JSONArray jsonarray = new JSONArray(jsonStr);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject c = jsonarray.getJSONObject(i);
                            String Id = c.getString("Id");
                            String Title_en=c.getString("Title_en");
                            String Title_ar=c.getString("Title_ar");
                            String type=c.getString("type");
                            String SubTitle_en=c.getString("SubTitle_en");
                            String SubTitle_ar=c.getString("SubTitle_ar");
                            String DoctorEmployeeId=c.getString("DoctorEmployeeId");
                            String duration=c.getString("duration");
                            String clinicId=c.getString("clinicId");

                            HashMap<String, String> data = new HashMap<>();

                            // adding each child node to HashMap key => value
                            data.put("Id",Id);
                            data.put("Title_en", Title_en);
                            data.put("Title_ar", Title_ar);
                            data.put("type", type);
                            data.put("SubTitle_en", SubTitle_en);
                            data.put("SubTitle_ar", SubTitle_ar);
                            data.put("DoctorEmployeeId", DoctorEmployeeId);
                            data.put("duration", duration);
                            data.put("clinicId", clinicId);
                            data.put("serviceId",Id);
                            //           Search_names_en.add(clinicName_en);
                            //         Search_names_ar.add(clinicName_ar);
                            Search_data.add(data);
                            HashMap<String, String> data_en = new HashMap<>();
                            data_en.put("Title_en",Title_en);
                            search_list.add(data_en);
                            HashMap<String, String> data_ar = new HashMap<>();
                            data_ar.put("Title_ar",Title_ar);
                            search_list_ar.add(data_ar);
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
                }



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("right here ", "Response from url here:" + jsonStr);

            return response;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("data is here","dataUpdated: "+result);
            Log.e("data is here","dataUpdated: "+search_list_ar.size());
            Log.e("data is here","dataUpdated: "+search_list.size());

            Log.e("data is here","dataUpdated: "+Search_data.size());
            SharedPreferences pref=getSharedPreferences("Settings",Activity.MODE_PRIVATE);
            String lng=pref.getString("Mylang","");
            Log.e("language",lng);
            result_count=(TextView)findViewById(R.id.result_count);
            if(lng.equals("ar")){
                result_count.setText("("+Search_data.size()+") "+getResources().getString(R.string.ac_select));
            }
            else{
                result_count.setText(getResources().getString(R.string.ac_select)+"("+Search_data.size()+")");
            }

            serachListView=(ListView)findViewById(R.id.ggs);
           // if(search_list.size()!=0){
            if(Search_data!=null){


                CustomListAdapter_search adapter = new CustomListAdapter_search(
                        getApplicationContext(), R.layout.list_item_search, Search_data,lng
                );



            serachListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            //click event
            serachListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String, String> offers = (HashMap<String, String>) parent.getAdapter().getItem(position);
                    if(offers.get("type").equals("clinic"))
                    {
                        Intent intent=new Intent(SearchActivity.this,ClinicPage.class);
                        intent.putExtra("Clinic",offers);
                        startActivity(intent);
                        finishAffinity();
                    }
                    else if(offers.get("type").equals("doctor"))
                    {
                        Intent intent=new Intent(SearchActivity.this,DatePickerPage.class);
                        intent.putExtra("offer",offers);
                        startActivity(intent);
                        finishAffinity();
                    }


                    //here i want to get the items
                }
            });

           // }

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
