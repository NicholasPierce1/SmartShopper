package com.example.smartshopper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SearchMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);
    }

    public void adminActionIntent(View v){
        Intent adminIntent = new Intent(this, AdminLoginScreenActivity.class){
            
        }
    }
}
