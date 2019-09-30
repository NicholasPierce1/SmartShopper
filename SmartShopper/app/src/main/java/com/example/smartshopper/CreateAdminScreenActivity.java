package com.example.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAdminScreenActivity extends AppCompatActivity {
    String empid;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.admin_modify);
        Intent ini = getIntent();
        empid = ini.getStringExtra("EMPID");
    }
    public void goBackAction(View v){
        Intent data = new Intent();
        data.putExtra("EMPID", empid);
        setResult(0, data);
        finish();
    }
}