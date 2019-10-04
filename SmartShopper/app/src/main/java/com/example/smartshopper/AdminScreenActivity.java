package com.example.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
//@Author Matthew Berry
public class AdminScreenActivity extends AppCompatActivity {

    public String empid, name, rank;

    TextView nameTV, empIDTV, rankTV;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.admin_screen);
        Intent ini = getIntent();
        empid = ini.getStringExtra("EMPID");
        nameTV = findViewById(R.id.NameTV);
        name = (nameTV.getText().toString() + nameFinder(empid));
        nameTV.setText(name);
        empIDTV = findViewById(R.id.EmployeeIDTV);
        empIDTV.setText(empIDTV.getText().toString() + empid);
        rankTV = findViewById(R.id.RantTV);
        rank = (rankTV.getText().toString() + rankFinder(empid) );
        rankTV.setText(rank);

    }
    private String nameFinder(String eid){
        //Here we will do a key value search with the employee id for the name.
        //For now we're just gonna hard code a value and retunr in
        return "Mister Admin";
    }
    private String rankFinder(String eid){
        //Here we will do a key value search with the employee id for the name.
        //For now we're just gonna hard code a value and retunr in
        boolean isStoreAdmin = true;
        Button adminCreateBTN = findViewById(R.id.adminBTN);
        if (isStoreAdmin){
            adminCreateBTN.setVisibility(View.VISIBLE);
            return "Store Admin";
        }
       else{
           adminCreateBTN.setVisibility(View.INVISIBLE);
           return "Stnadard Admin";
        }
    }
    public void adminCreateAction(View v){
        Intent toAdminCreate = new Intent(this, CreateAdminScreenActivity.class);
        toAdminCreate.putExtra("EMPID", empid);
        startActivity(toAdminCreate);
    }
    public void modifyProductAction(View v){
        Intent toProductModifer = new Intent(this, AdminProductScreenActivity.class);
        toProductModifer.putExtra("EMPID", empid);
        startActivity(toProductModifer);
    }

    public void onActivityResult(int req, int result, Intent data) {
        super.onActivityResult(req, result, data);
        if (req == 6) {
            if (result == 0) {
                return;
            }
        }
    }
    public void logOutAction(View V){
        //There might be more functionality here later. For now we just want to create an intent
        //and go to main activity
        Intent logOut = new Intent(this, Welcome_screenActivity.class);
        startActivity(logOut);
    }
}