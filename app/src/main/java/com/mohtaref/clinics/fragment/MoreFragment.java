package com.mohtaref.clinics.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohtaref.clinics.R;

import static com.mohtaref.clinics.utility.Constant.WhatsappMobile;

public class MoreFragment  extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.activity_more, container, false);

        return view;
    }

    public void Register_Your_Clinic(View view) {
        SharedPreferences pref = getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
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


}
