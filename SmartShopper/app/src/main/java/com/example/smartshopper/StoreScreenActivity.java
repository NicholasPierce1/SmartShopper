package com.example.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

public class StoreScreenActivity extends AppCompatActivity {

    // enumerates local member state

    // holds all locations pertaining to the store's schematic: textviews are found via departments of searched commodity
    // from the search commodity, the location is referenced to acquire the text view
    private HashMap<DepartmentType, HashMap<Location, TextView>> departmentToTextViewInfoReference;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.store_screen);

    }

    // configures the "emulated tab bar intents" to insert functionality into tabs

    // invokes by third image button on tab application, intents to search activity and preserves stack (w/o re-ordering)
    public void searchIntent(View v){
        Intent searchIntent = new Intent(this, Search_screenActivity.class);
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
