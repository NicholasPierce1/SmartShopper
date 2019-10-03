package com.example.smartshopper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class welcome_screenActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] stores = { "Store 1"};
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spin = findViewById(R.id.storeMenuSPNR);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,stores);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), stores[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void adminIntent(View v) {
        Intent adminIntent = new Intent(this, AdminLoginScreenActivity.class);
        startActivity(adminIntent);
    }

    public void searchIntent(View v){
        Intent searchIntent = new Intent(this, StoreScreenActivity.class);
        startActivity(searchIntent);
    }

    public void welcomeScreenIntent(View v){
        if(this instanceof welcome_screenActivity)
            return;

        Intent searchIntent = new Intent(this, welcome_screenActivity.class);
        startActivity(searchIntent);

    }

    public void storeScreenIntent(View v){
        Intent screenIntent = new Intent(this, StoreScreenActivity.class);
        startActivity(screenIntent);
        }

}
