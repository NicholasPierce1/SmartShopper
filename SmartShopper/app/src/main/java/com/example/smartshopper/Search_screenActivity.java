package com.example.smartshopper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Search_screenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);
    }

    public void welcomeScreenIntent(View v){
        Intent welcomeIntent = new Intent(this, Welcome_screenActivity.class);
        startActivity(welcomeIntent);
    }

    public void storeScreenIntent(View v){
        Intent storeScreen = new Intent(this, StoreScreenActivity.class);
        startActivity(storeScreen);
    }

    public void findItemIntent(View v){
        return;
    }

}