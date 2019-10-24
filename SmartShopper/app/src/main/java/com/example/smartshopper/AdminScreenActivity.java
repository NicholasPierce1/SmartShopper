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

    public static String[] a1 = {"Matthew Berry", "001", "admin", "Owner", "001"};

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

    public static Admin adminBuilder(String adminID){
        String[] adminParams = getAdminDetails(adminID);
        String nme, aid, passwrd, adlevel, sid;
        AdminLevel aLevel;
        Store aStore = new Store();
        nme = adminParams[0];
        aid = adminParams[1];
        passwrd = adminParams[2];
        adlevel = adminParams[3];
        if(adlevel.equals("Admin")){
            aLevel = AdminLevel.storeAdmin;
        }
        else if(adlevel.equals("Store Admin")){
            aLevel = AdminLevel.storeAdmin;
        }
        else aLevel = AdminLevel.owner;
        sid = adminParams[4];
        aStore = Store.storeBuilder(sid);

        Admin admin = new Admin(nme, aid, passwrd, aLevel, aStore);
        return admin;
    }

    private static String[] getAdminDetails(String adminID){
        //At some point this calls the class that class the db and retunrs an object based on adminin
        return a1;
    }
}