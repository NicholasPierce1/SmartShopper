package com.example.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
//@Author Matthew Berry

public class AdminProductScreenActivity extends AppCompatActivity {
    String empid;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.admin_product);
        Intent ini = getIntent();
        empid = ini.getStringExtra("EMPID");

    }

    public void goBackAction(View v){
        Intent data = new Intent();
        data.putExtra("EMPID", empid);
        setResult(0, data);
        finish();
    }
    //All other buttons don't get made until M2 or M3 as they don't change screens

}