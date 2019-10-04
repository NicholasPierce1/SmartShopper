package com.example.smartshopper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Welcome_screenActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] stores = {"Store 1"};
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        spin = findViewById(R.id.storeMenuSPNR);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stores);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), stores[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void adminIntent(View v) {
        Intent adminIntent = new Intent(this, AdminLoginScreenActivity.class);
        startActivity(adminIntent);
    }

    // renders on click action to intent over to selected store
    // TODO: current spinner has no affect on store yielded in intent- model layer call/creation will ensue
    // TODO: interpret if user made selection ( first value may be "no selection" )
    public void transitionToSelectedStore(View view){

        // creates intent (no stack reordering)
        Intent userStoreSelection = new Intent(this, StoreScreenActivity.class);

        // effectuates intent w/ no data
        super.startActivity(userStoreSelection);
    }

}
