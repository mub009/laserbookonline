package com.mohtaref.clinics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GiftIntroductionPage extends AppCompatActivity {

    Button continuous_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_introdution);
        continuous_btn=findViewById(R.id.continuous_btn);
        continuous_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(GiftIntroductionPage.this,GiftPaymentPage.class);
                startActivity(intent);
            }
        });
    }
}
