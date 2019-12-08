package com.example.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//@Author Matthew Berry
public class AdminScreenActivity extends AppCompatActivity {

    public String empid, name, rank;

    TextView nameTV, empIDTV, rankTV, msgCntrTV;
    private Admin admin;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.admin_screen);
        Intent ini = getIntent();

//
//        Log.d("LoginDeBug", "Size of admin list is: "+
//                AdminMockModelClass.adminList.size());
//        Log.d("LoginDeBug", "Testing admin in activity: " +
//                AdminMockModelClass.adminList.get(AdminMockModelClass.adminUserNames.indexOf(empid)).name);
//        Log.d("LoginDeBug", "empid: "+ empid);

        admin = (Admin)ini.getSerializableExtra("Admin");
        if(admin ==null){
            Log.d("AdminNull", "Admin is null");
        }
        msgCntrTV = findViewById(R.id.msgCenterTV);
        String base = msgCntrTV.getText().toString();
        String add = admin.name.substring(0, admin.name.indexOf(" ")); //Trying to get the first name;
        base  +=add;
        msgCntrTV.setText(""+base);
         empid = admin.empID;
        nameTV = findViewById(R.id.NameTV);
        name = (nameTV.getText().toString() + admin.name);
        nameTV.setText(name);
        empIDTV = findViewById(R.id.EmployeeIDTV);
        empIDTV.setText(empIDTV.getText().toString() + empid);
        rankTV = findViewById(R.id.RantTV);
        rank = (rankTV.getText().toString() + rankFinder(admin) );
        rankTV.setText(rank);

    }

    private String rankFinder(Admin a){
        //Here we will do a key value search with the employee id for the name.
        //For now we're just gonna hard code a value and retunr in
        boolean isStoreAdmin = (a.adminLevel.equals(AdminLevel.owner) ||
                a.adminLevel.equals(AdminLevel.managingStoreAdmin));
        Button adminCreateBTN = findViewById(R.id.adminBTN);
        if (isStoreAdmin){
            adminCreateBTN.setVisibility(View.VISIBLE);
            return "Elevated Admin";
        }
       else{
           adminCreateBTN.setVisibility(View.INVISIBLE);
           return "Standard Admin";
        }
    }
    public void adminCreateAction(View v){
        Intent toAdminCreate = new Intent(this, AdminModificationScreenActivity.class);
        toAdminCreate.putExtra("admin", admin);
        startActivity(toAdminCreate);
    }
    public void modifyProductAction(View v){
        Intent toProductModifer = new Intent(this, AdminProductScreenActivity.class);
        toProductModifer.putExtra("EMPID", admin);
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