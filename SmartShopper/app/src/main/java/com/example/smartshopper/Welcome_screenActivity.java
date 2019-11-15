package com.example.smartshopper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Welcome_screenActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, WelcomeScreenModelMethods{

    // list of store objects from the model/broker
    private List<Store> storeList = null;

    // local member to hold currently selected store
    private Store currentStore = null;

    // holds intent to invoke conditionally on model's callback result
    private Intent intentToExecute;

    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        spin = findViewById(R.id.storeMenuSPNR);
        spin.setOnItemSelectedListener(this);

        // important: enjoin broker to get stores and init data persistence w/ app context
        Model.getShared().findAllStores(super.getApplicationContext(), this);
    }


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        this.currentStore = this.storeList.get(position);
        
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        this.currentStore = this.storeList.get(0);
    }

    // renders on click action to instigate intent and init depts
    public void commenceTransition(View v) {

        // renders local intent member creation
        this.setIntentToTranstitionTo((Button)v);

        // acquires current store and passes into init depts
        Model.getShared().initializeDeapartmentListForStore(this.currentStore, this);
    }

    // local method to differentiate between two intent views and returns what intent to effectuate
    @NonNull
    private void setIntentToTranstitionTo(@NonNull Button btn){

        // if btn's id is start searching then return intent to store + converse
        this.intentToExecute = btn.getId() == R.id.startSearchingBTN ? new Intent(this, StoreScreenActivity.class) : new Intent(this, AdminLoginScreenActivity.class);
    }


    // if success is true-- create array adapter, set array adapter to spinner & set list to 'this.storeList'
    // else -- create alert dialog informing user error occured, ask user if they want a re-try
    @Override
    public void storeCB(boolean success, List<Store> stores) {
        if(success = true){
            this.storeList = stores;
            // invokes helper to convert list of stores to list of strings
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, this.convertStoreListToStringList() );
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(arrayAdapter);
        }
        else{
            // local ref to this
            final Welcome_screenActivity welcome_screenActivity = this;

            // create local Alert Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // sets title
            builder.setTitle("Oops...");

            // sets message of error
            builder.setMessage("Looks like an error occurred getting your stores! Please try again or come back later!");

            // sets only, positive message
            builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    // invoke operation again
                    Model.getShared().findAllStores(getApplicationContext(), welcome_screenActivity);
                }
            });

            // shows dialog
            builder.create().show();
        }
    }

    // if success is true- get intent that was set and start it, else alert dialog to inform user and ask if they want a re-try
    @Override
    public void departmentCB(boolean success) {

        // acquires current store and passes into init depts

        if(success){
            startActivity(this.intentToExecute);
        }
        else{
            // local ref to this
            final Welcome_screenActivity welcome_screenActivity = this;

            // create local Alert Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // sets title
            builder.setTitle("Oops...");

            // sets message of error
            builder.setMessage("Looks like an error occur proccessing your selection.");

            // sets only, positive message
            builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    // invoke operation again
                    Model.getShared().initializeDeapartmentListForStore(welcome_screenActivity.currentStore, welcome_screenActivity);
                }
            });

            // shows dialog
            builder.create().show();
        }
    }

    // helper to convert list of stores to list of friendly strings (store names)
    @NonNull
    private List<String> convertStoreListToStringList(){

        final List<String> listOfStoreNames = new ArrayList<String>();

        for(Store store: this.storeList)
            listOfStoreNames.add(store.name);

        return listOfStoreNames;
    }
}
