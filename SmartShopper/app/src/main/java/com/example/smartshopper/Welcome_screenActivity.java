package com.example.smartshopper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
//        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, convertFromStoreArray(stores));
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spin.setAdapter(aa);
    }


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //TODO: update local ref to user selection
        
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


    @Override
    public void storeCB(boolean success, List<Store> store) {

    }

    @Override
    public void departmentCB(boolean success) {

    }
}
