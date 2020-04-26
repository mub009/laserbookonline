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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

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
import com.mohtaref.clinics.adapter.ListMenuAdapter;
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
import java.util.List;
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
    private RecyclerView recyclerView_menu,recyclerView_offer;
    private Adapter menuAdapter,offerAdapter;
    private List<MenuModel> MenuModelList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.content_home_page, container, false);
        listprog = (ProgressBar) view.findViewById(R.id.listprogress);
        sliderLayout = view.findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setSliderTransformAnimation(SliderAnimations.VERTICALFLIPTRANSFORMATION);
        sliderLayout.setScrollTimeInSec(3); //set scroll delay in seconds :
        //        initialzing Clossing
        mHandler = new HomePageFragment.MyHandler();
        homeMainLayout = (NestedScrollView)  view.findViewById(R.id.viewscroll);
        homeMainLayout.setNestedScrollingEnabled(false);
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

    public class GetCategories extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SharedPreferences pref = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);
            String data = pref.getString("data", "");
            try {
                JSONObject userdata = new JSONObject(data);
                token2 = userdata.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (longitude != 0.0 && latitude != 0.0) {
                lng = String.valueOf(longitude);
                lat = String.valueOf(latitude);
                Log.e("lat string is ", lat);
            }
        }

        @Override
        protected String doInBackground(Void... arg0) {
//            final FormActivity formobject=new FormActivity();
            HttpHandlerGet sh = new HttpHandlerGet();
            // Making a request to url and getting response
            String urlget = base_url + "categoriesv1";
            String jsonStr = sh.makeServiceCall(urlget, token2);
            //Log.e("what is this chips", "idon't know?" + jsonStr);
            // Log.e("what to ", "doooooo");
            Log.e("is ", "Response from url countries: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);
                    //   Log.e("Jsonarray size", "that is : " + jsonarray.length());
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject c = jsonarray.getJSONObject(i);
                        String categoryId = c.getString("categoryId");
                        String categoryName_en = c.getString("categoryName_en");
                        String categoryName_ar = c.getString("categoryName_ar");
                        String deleted = c.getString("deleted");
                        String created_at = c.getString("created_at");
                        String last_modify = c.getString("last_modify");
                        String fullimg=c.getString("fullimg");

                        // tmp hash map for single offer
                        HashMap<String, String> Categories_ob = new HashMap<>();

                        // adding each child node to HashMap key => value

                        Categories_ob.put("categoryId", categoryId);
                        Categories_ob.put("categoryName_en", categoryName_en);
                        Categories_ob.put("categoryName_ar", categoryName_ar);
                        Categories_ob.put("deleted", deleted);
                        Categories_ob.put("created_at", created_at);
                        Categories_ob.put("last_modify", last_modify);
                        Categories_ob.put("fullimg", fullimg);

                        categories_list.add(Categories_ob);
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


            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(categories_list!=null&&!categories_list.isEmpty()){
                SharedPreferences pref = getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
                String langugae = pref.getString("Mylang", "");
                Log.e("language", langugae);
                recyclerView_menu = (RecyclerView) view.findViewById(R.id.RecyclerViewCategory);
                menuAdapter = new ListMenuAdapter(MenuModelList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                recyclerView_menu.setLayoutManager(mLayoutManager);
                recyclerView_menu.setItemAnimator(new DefaultItemAnimator());
                recyclerView_menu.setAdapter((RecyclerView.Adapter) menuAdapter);

                MenuModel MenuModel;

                if(langugae.equals("ar"))
                {
                    MenuModel = new MenuModel("", " بالقرب","",lng ,lat);
                    MenuModelList.add(MenuModel);
                }
                else
                {
                    MenuModel = new MenuModel("", "Near By","",lng ,lat);
                    MenuModelList.add(MenuModel);
                }



                for (int i=0;i<categories_list.size();i++)
                {
                    if(langugae.equals("ar")) {
                        MenuModel = new MenuModel(categories_list.get(i).get("fullimg"), categories_list.get(i).get("categoryName_ar"),categories_list.get(i).get("categoryId"),lat,lng );
                        MenuModelList.add(MenuModel);
                    }
                    else
                    {
                        MenuModel = new MenuModel(categories_list.get(i).get("fullimg"), categories_list.get(i).get("categoryName_en"),categories_list.get(i).get("categoryId"),lat,lng);
                        MenuModelList.add(MenuModel);
                    }

                }
                ((RecyclerView.Adapter) menuAdapter).notifyDataSetChanged();

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

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            SharedPreferences pref = getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
            String lng = pref.getString("Mylang", "");
            Log.e("language", lng);
            Log.e("after it", "size is ; " + offers_list.size());
            if(offers_list!=null&&!offers_list.isEmpty()){
                Log.e("offers no empty","yeah full");

                Integer CountOfferLength=offers_list.size();
                if(CountOfferLength >10)
                {
                    CountOfferLength=10;
                }

                for(int o=0;o<CountOfferLength;o++){
                    part_offers_list.add(offers_list.get(o));
                    numofItmesDown++;
                    Log.e("value of","is "+numofItmesDown);

                }
            }
            else{
                Log.e("offers is empty","yeah");
            }

            list = (ListView) view.findViewById(R.id.list);
            //set the adapter of CustomList Adapter
            adapter = new CustomListAdapter(
                    getActivity(), R.layout.list_item, part_offers_list,lng
            );

            list.setAdapter(adapter);

            setListViewHeightBasedOnChildren(list);


//
//            recyclerView_offer = (RecyclerView) findViewById(R.id.RecyclerViewofferList);
//            offerAdapter = new OfferAdapter(OfferModelList);
//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
//            recyclerView_offer.setLayoutManager(mLayoutManager);
//            recyclerView_offer.setItemAnimator(new DefaultItemAnimator());
//            recyclerView_offer.setAdapter((RecyclerView.Adapter) offerAdapter);
//            for (int i=0;i<part_offers_list.size();i++)
//            {
//                if(lng.equals("ar")) {
//                    OfferModel OfferModel = new OfferModel(part_offers_list.get(i).get("img"),part_offers_list.get(i).get("serviceName_en"),part_offers_list.get(i).get("discount"),part_offers_list.get(i).get("distance"),part_offers_list.get(i).get("clinicName_ar"),part_offers_list.get(i).get("cost"),part_offers_list.get(i).get("postCost"));
//                    OfferModelList.add(OfferModel);
//                }
//                else
//                {
//                    OfferModel OfferModel = new OfferModel(part_offers_list.get(i).get("img"),part_offers_list.get(i).get("serviceName_en"),part_offers_list.get(i).get("discount"),part_offers_list.get(i).get("distance"),part_offers_list.get(i).get("clinicName_en"),part_offers_list.get(i).get("cost"),part_offers_list.get(i).get("postCost"));
//                    OfferModelList.add(OfferModel);
//                }
//
//            }
//            ((RecyclerView.Adapter) offerAdapter).notifyDataSetChanged();

//            recyclerView_offer.addOnScrollListener(new RecyclerViewPaginationListener((LinearLayoutManager) mLayoutManager) {
//                @Override
//                protected void loadMoreItems() {
//                    isLoading = true;
//                    currentPage++;
//                    new offers().execute();
//                }
//
//                @Override
//                public boolean isLastPage() {
//                    return isLastPage;
//                }
//
//                @Override
//                public boolean isLoading() {
//                    return isLoading;
//                }
//            });




            //            list.setOnScrollListener(new AbsListView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(AbsListView absListView, int i) {
//
//                }
//
//                @Override
//                public void onScroll(AbsListView absListView, int firstVisibleItem, int VisibleItemCount, int totalItemCount) {
//                //check when scroll to last item listview in this tt , init data in listview= 10 items
//                    Log.e("list count","is "+list.getCount());
//                    Log.e("loaidng ","is "+isLoading);
//                    Log.e("totalItemCount","is "+totalItemCount);
//                    Log.e("lastpostion","is "+absListView.getLastVisiblePosition());
//
//                    if(absListView.getLastVisiblePosition()==totalItemCount-1&&list.getCount()>=10&& isLoading==false){
//                        Log.e("reached "," last");
//                        isLoading=true;
//                        pagesize=false;
//                        Thread thread=new ThreadGetMoreData();
//                        thread.start();
//
//                    }

//                }
//            });

//            list.setOnScrollListener(new EndlessScrollListener() {
//                @Override
//                public void onLoadMore(int page, int totalItemsCount) {
//                    for(int o=0;o<10;o++){
//                        part_offers_list.add(offers_list.get(numofItmes));
//                        numofItmes++;
//
//
//                    }
//                    adapter.notifyDataSetChanged();
//
//
//                }
//
//            });
            homeMainLayout.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if(v.getChildAt(v.getChildCount() - 1) != null) {

                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY&&isLoading==false) {
                        //code to fetch more data for endless scrolling
                        Log.e("ListActivity","LoadeMore");
                        Log.e("reached old X",""+oldScrollX);
                        Log.e("reached old Y",""+oldScrollY);
                        Log.e("reached X",""+scrollX);
                        Log.e("reached Y",""+scrollY);

                        if(removed){
                            Scrollofy=scrollY-oldScrollY;
                            Scrollofx=scrollX-oldScrollX;
                            Scrollofy=oldScrollY-9100;
                            Scrollofx=0;
                            Log.e("reomved item ","scrollofy: "+Scrollofy);
                            Log.e("reomved item"," Scrollofx " +Scrollofx);
                        }
                        isLoading=true;
                        downTriggerd=true;
                        pagesize=false;
                        Thread thread=new HomePageFragment.ThreadGetMoreData();
                        thread.start();
                        if(!removed){
                            //     homeMainLayout.scrollTo(oldScrollX,oldScrollY)
                            Scrollofy=oldScrollY;
                            Scrollofx=oldScrollX;
                            Log.e("not reomved item"," Scrollofx " +Scrollofx);
                            Log.e("not reomved item ","scrollofy: "+Scrollofy);

                        }

                        //   setListViewHeightBasedOnChildren(list);

                    }
                    if(scrollY==0&&isLoading==false){
                        Log.e("we are at", "TOP SCROLL");
                        isLoading=true;
                        downTriggerd=false;
                        pagesize=false;
                        if(removed){
                            Scrollofy=scrollY-oldScrollY;
                            Scrollofx=scrollX-oldScrollX;
                            Scrollofy=9100;
                            Scrollofx=0;
                            Log.e("reomved item ","scrollofy: "+Scrollofy);
                            Log.e("reomved item"," Scrollofx " +Scrollofx);
                        }
                        Thread thread=new HomePageFragment.ThreadGetMoreData();
                        thread.start();
                        if(!removed){
                            Scrollofx=scrollX;
                            Scrollofy=scrollY;
                        }

//                        Scrollofy=scrollY-oldScrollY;
//                        Scrollofx=scrollX-oldScrollX;
//                        Scrollofy=oldScrollY-Scrollofy;
//                        Scrollofx=oldScrollX-Scrollofx;
                        //homeMainLayout.scrollTo(oldScrollX,oldScrollY);


                    }
                }
            });

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String, String> offers = (HashMap<String, String>) parent.getAdapter().getItem(position);
                    Intent intent= new Intent(getActivity(),ClinicPage.class);
                    intent.putExtra("Clinic", offers);
                    startActivity(intent);

                    //here i want to get the items
                }
            });

            setSliderViews(slider_offers_list);
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                   // pd.cancel();

                }
            };

            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 2000);

        }
    }

    public class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //add
                    // list.addFooterView(ftView);
                    listprog.setVisibility(View.VISIBLE);

                    break;

                case 1:
                    //update
                    if(refresh){Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                listprog.setVisibility(View.GONE);

                                //Do something after 100ms
                                adapter.addListItemToAdapter(map,downTriggerd);

                                // list.removeFooterView(ftView);
                                //Remove loading view
                                if(removed){
                                    homeMainLayout.scrollTo(Scrollofx,Scrollofy);

                                }
                                setListViewHeightBasedOnChildren(list);

//                            if(!removed){
//                                homeMainLayout.scrollTo(Scrollofx,Scrollofy);
//
//                            }

                                isLoading=false;
                            }
                        }, 2500);
                    }
                    else{

                    }

                default:
                    break;
            }
        }
    }
    private ArrayList<HashMap<String,String>> getMoreData(){

        ArrayList<HashMap<String,String>>offeraddList=new ArrayList<>();
        ArrayList<HashMap<String,String>>offeraddListRemove=new ArrayList<>();

        for(int o=0;o<10;o++){
            if(numofItmesDown!=offers_list.size()-1&&numofItmesUP!=offers_list.size()-1){
                //down scroll
                if(downTriggerd){
                    if(numofItmesDown>=50){
                        offeraddList.add(offers_list.get(numofItmesDown));
                        numofItmesDown++;
                        numofItmesUP++;
                        offeraddListRemove.add(offers_list.get(numofItmesUP));
                        removed=true;
                        refresh=true;


                    }
                    else{
                        offeraddList.add(offers_list.get(numofItmesDown));
                        numofItmesDown++;
                        removed=false;
                        refresh=true;

                    }

                }
//up scroll
                else{
                    if(numofItmesUP>=0){
                        if(numofItmesDown>=50){
                            Log.e("up counter"," "+numofItmesUP);
                            Log.e("down counter"," "+numofItmesUP);

                            offeraddList.add(offers_list.get(numofItmesUP));
                            offeraddListRemove.add(offers_list.get(numofItmesDown));
                            removed=true;
                            refresh=true;

                            numofItmesDown--;
                            numofItmesUP--;

                        }
                        else{
                            if(numofItmesUP!=0){
                                offeraddList.add(offers_list.get(numofItmesUP));
                                numofItmesDown--;
                                numofItmesUP--;
                            }
                            else if(numofItmesUP==0) {
                                offeraddList.add(offers_list.get(numofItmesUP));
                            }
//
                            map.put("offeraddList",offeraddList);
                            map.put("offeraddListRemove",offeraddListRemove);
                            return offeraddList;
                        }
                    }
                    else {
                        // refresh=false;
                        map.put("offeraddList",offeraddList);
                        map.put("offeraddListRemove",offeraddListRemove);
                        return offeraddList;

                    }


                }

            }

            else if(downTriggerd&&numofItmesDown==offers_list.size()-1) {
                refresh=false;
                Log.e("End of it","no more offers");
                map.put("offeraddList",offeraddList);
                map.put("offeraddListRemove",offeraddListRemove);
                return offeraddList;
            }
            else if (!downTriggerd&&numofItmesDown!=offers_list.size()-1){
                refresh=true;
                offeraddList.add(offers_list.get(numofItmesUP));
                offeraddListRemove.add(offers_list.get(numofItmesDown));
                removed=true;

                numofItmesDown--;
                numofItmesUP++;

            }
            else if(!downTriggerd&&numofItmesUP==offers_list.size()-1){
                refresh=false;
                Log.e("End of it","no more offers");
                map.put("offeraddList",offeraddList);
                map.put("offeraddListRemove",offeraddListRemove);
                return offeraddList;
            }
            else if(downTriggerd&&numofItmesUP==offers_list.size()-1){
                refresh=true;
                offeraddList.add(offers_list.get(numofItmesUP));
                offeraddListRemove.add(offers_list.get(numofItmesDown));
                removed=true;

                numofItmesDown++;
                numofItmesUP--;
            }
        }
        map.put("offeraddList",offeraddList);
        map.put("offeraddListRemove",offeraddListRemove);
        return offeraddList;
    }
    //        if(numofItmesUP>0){
//            offeraddList.remove(numofItmesUP);
//
//            numofItmesUP--;
//        }
//        else{
//
//
//        }
    public class ThreadGetMoreData extends Thread{
        @Override
        public void run() {
            //add footer view afte get data
            if(refresh)
                mHandler.sendEmptyMessage(0);
            //search more data
            // ArrayList<HashMap<String,String>> listResult=
            getMoreData();
            //delay time to show loading footer when debug, remove it when relese
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            //send result to handle
            Message msg= mHandler.obtainMessage(1,map);
            mHandler.sendMessage(msg);

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
