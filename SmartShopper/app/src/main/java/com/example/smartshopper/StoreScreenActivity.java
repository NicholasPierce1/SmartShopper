package com.example.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class StoreScreenActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.store_screen);

    }

    // configures the "emulated tab bar intents" to insert functionality into tabs

    // invokes by third image button on tab application, intents to search activity and preserves stack (w/o re-ordering)
    public void searchIntent(View v){
        Intent searchIntent = new Intent(this, StoreScreenActivity.class);
        startActivity(searchIntent);
    }

    // invokes by first image button on tab application, intents to welcome and preserves stack (w/o re-ordering)
    public void welcomeScreenIntent(View v){
        Intent searchIntent = new Intent(this, Welcome_screenActivity.class);
        startActivity(searchIntent);
    }

    // current context- no action performed
    public void storeScreenIntent(View v){
        return;
    }

}
