package com.example.smartshopper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class welcome_screenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
    }
    public void adminIntent(View v){
        Intent adminIntent = new Intent(this, AdminLoginScreenActivity.class);
            startActivity(adminIntent);
        }

}
