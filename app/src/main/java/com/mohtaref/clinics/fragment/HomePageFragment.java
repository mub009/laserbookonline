package com.mohtaref.clinics.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mohtaref.clinics.ClinicPage;
import com.mohtaref.clinics.CustomListAdapter;
import com.mohtaref.clinics.HomePage;
import com.mohtaref.clinics.HttpHandlerGet;
import com.mohtaref.clinics.HttpHandlerPostToken;
import com.mohtaref.clinics.R;
import com.mohtaref.clinics.model.MenuModel;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mohtaref.clinics.ClinicPage.setListViewHeightBasedOnChildren;
import static com.mohtaref.clinics.utility.Constant.APIbaseLink;

public class HomePageFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    String token;
    String token2;
    String lat;
    String lng;
    boolean firstitemchangelang=true;
    public int Scrollofy = 0;
    public int Scrollofx = 0;
    SliderLayout sliderLayout;
//    final private String base_url = APIbaseLink;

    final private String base_url="https://laserbookingonline.com/manager/APIs/clientV2/";
    boolean doubleBackToExitPressedOnce = false;
    public int numofItmesDown = 0;
    int numofItmesUP = -1;
    ArrayList<HashMap<String, String>> categories_list= new ArrayList<>();
    ArrayList<HashMap<String, String>> offers_list= new ArrayList<>();;
    ArrayList<HashMap<String, String>> slider_offers_list= new ArrayList<>();
    ArrayList<HashMap<String, String>> part_offers_list= new ArrayList<>();
    public boolean pagesize = false;
    boolean downTriggerd = true;
    TextView cati1;
    TextView cati2;
    TextView cati3;
    TextView cati4;
    TextView cati5;
    TextView cati6;
    TextView cati7;
    ImageView img_cati1;
    ImageView img_cati2;
    ImageView img_cati3;
    ImageView img_cati4;
    ImageView img_cati5;
    ImageView img_cati6;
    ImageView img_cati7;
    ListView list;
    double latitude = 0.0;
    double longitude = 0.0;
    Location locationdata;
    public LocationManager lm;
    LocationManager locationManager;
    LocationManager mLocationManager;
    String locationText;
    String locationLatitude;
    String locationLongitude;
    Boolean requestingLocationUpdates = false;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    Boolean gps_enabled = false;
    Context context;
    EditText search_bar;
    ProgressDialog pd;
    Button cancel;
    Button nearby;
    boolean isguest = false;
    LinearLayout profile_side;
    LinearLayout rating_side;
    LinearLayout appointment_side;
    LinearLayout group_login_real;
    NestedScrollView homeMainLayout;
    public CustomListAdapter adapter;
    public Handler mHandler;
    public View ftView;
    public boolean isLoading = false;
    Map<String, ArrayList<HashMap<String, String>>> map = new HashMap();
    ProgressBar listprog;
    public boolean removed = false;
    public boolean refresh = true;
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(
                R.layout.activity_home_page, container, false);


        //        initialzing Openning


        sliderLayout = view.findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setSliderTransformAnimation(SliderAnimations.VERTICALFLIPTRANSFORMATION);
        sliderLayout.setScrollTimeInSec(3); //set scroll delay in seconds :

        //        initialzing Clossing


        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


        if (gps_enabled) {
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(1 * 100)        // 10 seconds, in milliseconds
                    .setFastestInterval(1 * 10); // 1 second, in milliseconds

            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();


            Long time = mLocationRequest.getExpirationTime();
            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                           tasks();
                }
            }, 2000);

        } else {

            tasks();

            if (longitude != 0.0 && latitude != 0.0) {
                lng = String.valueOf(longitude);
                lat = String.valueOf(latitude);
                Log.e("lat string is ", lat);
            } else
                Log.e("it'sempty", ": its empty");
        }

        return view;
    }


    public void tasks() {

        new HomePageFragment.GetCategories().execute();

        new HomePageFragment.offers().execute();


    }

    @SuppressLint("StaticFieldLeak")
    public class GetCategories extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SharedPreferences pref = getContext().getSharedPreferences("user", Activity.MODE_PRIVATE);
            String data = pref.getString("data", "");
            try {
                JSONObject userdata = new JSONObject(data);
                token2 = userdata.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("user token is :", token2);
            if (longitude != 0.0 && latitude != 0.0) {
                lng = String.valueOf(longitude);
                lat = String.valueOf(latitude);
                Log.e("lat string is ", lat);
            }
        }

        @Override
        protected String doInBackground(Void... arg0) {

            HttpHandlerGet sh = new HttpHandlerGet();
            // Making a request to url and getting response
            String urlget = base_url + "categories";
            String jsonStr = sh.makeServiceCall(urlget, token2);
            //Log.e("what is this chips", "idon't know?" + jsonStr);
            // Log.e("what to ", "doooooo");
            Log.e("is ", "Response from url countries: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject c = jsonarray.getJSONObject(i);
                        HashMap<String, String> Categories_ob = new HashMap<>();
                        // adding each child node to HashMap key => value
                        Categories_ob.put("categoryId", c.getString("categoryId"));
                        Categories_ob.put("categoryName_en", c.getString("categoryName_en"));
                        Categories_ob.put("categoryName_ar", c.getString("categoryName_ar"));
                        Categories_ob.put("fullimg", c.getString("fullimg"));
                        categories_list.add(Categories_ob);

                    }
                } catch (final JSONException e) {
                    Log.e("", "Json parsing error: " + e.getMessage());
                    Activity activity = getActivity();
                    if (activity != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //  formobject.buildDialog(R.style.DialogAnimation, "NO Records found","ok");


                            }
                        });
                    }
                }

            } else {
                Log.e("", "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

//                        Toast.makeText(getApplicationContext(),
//                                "Couldn't get data from server. Check your internet connection or try later",
//                                Toast.LENGTH_LONG).show();

                    }
                });
            }


            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(categories_list!=null&&!categories_list.isEmpty()){
                SharedPreferences pref = getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
                String lng = pref.getString("Mylang", "");
                Log.e("language", lng);
                cati3 = (TextView) view.findViewById(R.id.catgori3);
                cati4 = (TextView) view.findViewById(R.id.catgori4);
                cati5 = (TextView) view.findViewById(R.id.catgori5);
                cati6 = (TextView) view.findViewById(R.id.catgori6);
                cati7 = (TextView) view.findViewById(R.id.catgori7);

                if(lng.equals("ar")){
                    String cat1 = categories_list.get(0).get("categoryName_ar");
                    String cat2 = categories_list.get(1).get("categoryName_ar");
                    String cat3 = categories_list.get(2).get("categoryName_ar");
                    String cat4 = categories_list.get(3).get("categoryName_ar");
                    String cat5 = categories_list.get(4).get("categoryName_ar");

                    // String cat5 = categories_list.get(4).get("categoryName_ar");

                    cati3.setText(cat1);
                    cati4.setText(cat2);
                    cati5.setText(cat3);
                    cati6.setText(cat4);
                    cati7.setText(cat5);


                }
                else {
                    String cat1 = categories_list.get(0).get("categoryName_en");
                    String cat2 = categories_list.get(1).get("categoryName_en");
                    String cat3 = categories_list.get(2).get("categoryName_en");
                    String cat4 = categories_list.get(3).get("categoryName_en");
                    String cat5 = categories_list.get(4).get("categoryName_en");

                    cati3.setText(cat1);
                    cati4.setText(cat2);
                    cati5.setText(cat3);
                    cati6.setText(cat4);
                    cati7.setText(cat5);

                }
                ///// set text






                ///// set icon image
                String img_cat1_string = categories_list.get(0).get("fullimg");
                String img_cat2_string = categories_list.get(1).get("fullimg");
                String img_cat3_string = categories_list.get(2).get("fullimg");
                String img_cat4_string = categories_list.get(3).get("fullimg");
                String img_cat5_string = categories_list.get(4).get("fullimg");


                img_cati3 =(ImageView)view.findViewById(R.id.img_btn3);
                img_cati4 =(ImageView)view.findViewById(R.id.img_btn4);
                img_cati5 =(ImageView)view.findViewById(R.id.img_btn5);
                img_cati6 =(ImageView)view.findViewById(R.id.img_btn6);
                img_cati7 =(ImageView)view.findViewById(R.id.img_btn7);


                Picasso.get().load(img_cat1_string).fit().into(img_cati3);
                Picasso.get().load(img_cat2_string).fit().into(img_cati4);
                Picasso.get().load(img_cat3_string).fit().into(img_cati5);
                Picasso.get().load(img_cat4_string).fit().into(img_cati6);
                Picasso.get().load(img_cat5_string).fit().into(img_cati7);

            }
        }
    }



    @SuppressLint("StaticFieldLeak")
    public class offers extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // getLastLocation();
//            categories_list.clear();

            SharedPreferences pref = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);
            String data = pref.getString("data", "");
            try {
                JSONObject userdata = new JSONObject(data);
                token2 = userdata.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("user token is :", token2);



        }

        @Override
        protected Void doInBackground(Void... arg0) {
//            final FormActivity formobject=new FormActivity();

            HttpHandlerPostToken sh = new HttpHandlerPostToken();

            // Making a request to url and getting response
            String urlget = base_url + "offers";
            Log.e("url is this","that "+urlget);
            Log.e("url is this","that "+token2);
            Log.e("url is this","that "+lng);
            Log.e("url is this","that "+lat);

            String jsonStr = sh.makeServiceCall(urlget, token2, lng, lat);

            Log.e("is offers :", "Response from url countries: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject c = jsonarray.getJSONObject(i);
                        String offerId = c.getString("offerId");
                        String serviceId = c.getString("serviceId");
                        String offerTitle_en = c.getString("offerTitle_en");
                        String offerTitle_ar = c.getString("offerTitle_ar");
                        String offerDescription_en = c.getString("offerDescription_en");
                        String offerDescription_ar = c.getString("offerDescription_ar");
                        String offer_start = c.getString("offer_start");
                        String offer_end = c.getString("offer_end");
                        String priority = c.getString("priority");
                        String discount = c.getString("discount");
                        String paymentTypes = c.getString("paymentTypes");
                        String img = c.getString("img");
                        String img_m = c.getString("img_m");
                        String img_s = c.getString("img_s");
                        String sessionId = c.getString("sessionId");
                        String featured = c.getString("featured");
                        String active = c.getString("active");
                        String deleted = c.getString("deleted");
                        String created_at = c.getString("created_at");
                        String last_modify = c.getString("last_modify");
                        String serviceSeed = c.getString("serviceSeed");
                        String categoryId = c.getString("categoryId");
                        String clinicId = c.getString("clinicId");
                        String serviceName_en = c.getString("serviceName_en");
                        String serviceName_ar = c.getString("serviceName_ar");
                        String serviceDescription_en = c.getString("serviceDescription_en");
                        String serviceDescription_ar = c.getString("serviceDescription_ar");
                        String cost = c.getString("cost");
                        String duration = c.getString("duration");
                        String clinicName_en = c.getString("clinicName_en");
                        String clinicName_ar = c.getString("clinicName_ar");
                        String postCost = c.getString("postCost");


                        HashMap<String, String> offer_ob = new HashMap<>();

                        // adding each child node to HashMap key => value
                        offer_ob.put("offerId", offerId);
                        offer_ob.put("serviceId", serviceId);
                        offer_ob.put("offerTitle_en", offerTitle_en);
                        offer_ob.put("offerTitle_ar", offerTitle_ar);
                        offer_ob.put("offerDescription_en", offerDescription_en);
                        offer_ob.put("offerDescription_ar", offerDescription_ar);
                        offer_ob.put("offer_start", offer_start);
                        offer_ob.put("offer_end", offer_end);
                        offer_ob.put("priority", priority);
                        offer_ob.put("discount", discount);
                        offer_ob.put("paymentTypes", paymentTypes);
                        offer_ob.put("img", img);
                        offer_ob.put("img_m", img_m);
                        offer_ob.put("img_s", img_s);
                        offer_ob.put("sessionId", sessionId);
                        offer_ob.put("featured", featured);
                        offer_ob.put("active", active);
                        offer_ob.put("deleted", deleted);
                        offer_ob.put("created_at", created_at);
                        offer_ob.put("last_modify", last_modify);
                        offer_ob.put("serviceSeed", serviceSeed);
                        offer_ob.put("categoryId", categoryId);
                        offer_ob.put("clinicId", clinicId);
                        offer_ob.put("serviceName_en", serviceName_en);
                        offer_ob.put("serviceName_ar", serviceName_ar);
                        offer_ob.put("serviceDescription_en", serviceDescription_en);
                        offer_ob.put("serviceDescription_ar", serviceDescription_ar);
                        offer_ob.put("cost", cost);
                        offer_ob.put("duration", duration);
                        offer_ob.put("clinicName_en", clinicName_en);
                        offer_ob.put("clinicName_ar", clinicName_ar);
                        offer_ob.put("postCost", postCost);
                        if (lat != null && lng != null) {
                            String distance = c.getString("distance");
                            offer_ob.put("distance", distance);

                        }
                        offers_list.add(offer_ob);

                        if(slider_offers_list.size()<10&&featured.equals("1")){
                            slider_offers_list.add(offer_ob);
                        }

                    }
                } catch (final JSONException e) {
                    Log.e("", "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //  formobject.buildDialog(R.style.DialogAnimation, "NO Records found","ok");


                        }
                    });

                }

            } else {
                Log.e("", "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
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

            SharedPreferences pref = getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
            String lng = pref.getString("Mylang", "");
            Log.e("language", lng);
            Log.e("after it", "size is ; " + offers_list.size());


            if(offers_list!=null&&!offers_list.isEmpty()){

                //offers_list.size()

                for(int o=0;o<5;o++){
                    part_offers_list.add(offers_list.get(o));
                }
            }
            else{
                Log.e("offers is empty","yeah");
            }

            list = (ListView) getActivity().findViewById(R.id.list);

            //set the adapter of CustomList Adapter
            adapter = new CustomListAdapter(
                    getActivity().getApplicationContext(), R.layout.list_item, part_offers_list,lng
            );

            list.setAdapter(adapter);

            setListViewHeightBasedOnChildren(list);

            setSliderViews(slider_offers_list);


            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
//                    pd.cancel();

                }
            };

            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 2000);

        }
    }




    private void setSliderViews(ArrayList<HashMap<String, String>> offers) {

        for (int i = 0; i < offers.size(); i++) {

            DefaultSliderView sliderView = new DefaultSliderView(getContext());
            sliderView.setImageUrl(offers.get(i).get("img").toString());


            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            //  sliderView.setDescription("setDescription " + (i + 1));
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    HashMap<String, String> offers_slider = offers.get(finalI);
                    Intent intent = new Intent(getActivity(), ClinicPage.class);
                    intent.putExtra("Clinic", offers_slider);
                    startActivity(intent);

                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
