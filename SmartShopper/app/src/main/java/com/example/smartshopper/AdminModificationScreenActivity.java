package com.example.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//@Author Matthew Berry

public class AdminModificationScreenActivity extends AppCompatActivity implements AdminModCBMethods{
    String empid;
    Admin user;
    EditText adminIDET, nameET, passwordET, usernameET;
    TextView outcomeTV, rankTV,  employeeTV, nameTV, passwordTV;
    CheckBox middleAdminCB;
    CheckBox ownerCB;
    String wrong = "";
    Button createBTN, modifyBTN, deleteBTN,cancelBTN;
    Button submitBTN;
    String cAdminID = ""; //ID of admin we want to change
    int submitCode = -1;
    Model model;
    Store store;
    String cName, cpw, username;
    AdminLevel level;
    Admin subject;
    final AdminModificationScreenActivity myActivity = this;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.admin_modify);
        Intent ini = getIntent();
        empid = ini.getStringExtra("EMPID");
        user = (Admin)ini.getSerializableExtra("admin");
        if(user == null){
            Log.d("AdminValid", "user is null");
        }
        adminIDET = findViewById(R.id.AdminIDET);
        outcomeTV = findViewById(R.id.outcomeTV);
        middleAdminCB = findViewById(R.id.middleAdminCB);
        ownerCB = findViewById(R.id.ownerCB);
        rankTV = findViewById(R.id.RankTV);
        employeeTV = findViewById(R.id.EmployeeIDTV);
        nameTV = findViewById(R.id.NameTV);
        passwordTV = findViewById(R.id.PasswordTV);
        nameET = findViewById(R.id.nameET);
        passwordET = findViewById(R.id.PasswordET);

        model = Model.getShared();
        store = model.getStore();
        createBTN = findViewById(R.id.CreateAdminBTN);
        modifyBTN = findViewById(R.id.ModifyAdminBTN);
        deleteBTN = findViewById(R.id.RemoveAdminBTN);
        cancelBTN = findViewById(R.id.cancelBTN);
        submitBTN = findViewById(R.id.submitBTN);
        usernameET = findViewById(R.id.EmployeeIdDisET);
        hideAndCelar();

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // clears output text from last command
                outcomeTV.setText("");
                adminIDET.setText("");

                if(submitCode == -1){
                    outcomeTV.setText("Not able to submit at this time");
                }
                else if (submitCode == 1){
                    Log.d("AdminValid" , "In submit button submitcode ==1" );
                    if(isInputValid()){
                        Log.d("AdminValid", "Input is valid");
                        cName = nameET.getText().toString();
                        cpw = passwordET.getText().toString();
                        username = usernameET.getText().toString();
                        Log.d("AdminValid", "Username in controller: " + username);
                        level = levelFinder();
                        model.checkForExistingUsername(submitCode, username, user, myActivity);
                    }
                else{
                    Log.d("C", "In else for invalid input");
                    outcomeTV.setText("Invalid input: " +wrong);
                }
            }
                else if(submitCode == 2){
                    if(isInputValid()){
                        cName = nameET.getText().toString();
                        cpw = passwordET.getText().toString();
                        username = usernameET.getText().toString();
                        usernameET.setClickable(false);
                         level = levelFinder();
                        //We would make a real model call to create it, but for now...
                        subject.name = cName;
                        subject.password = cpw;
                        subject.userName = username;
                        subject.empID = cAdminID;
                        subject.adminLevel = level;
                        model.updateAdmin(subject, myActivity);
                    }
                    else{
                        outcomeTV.setText("Invalid input: " +wrong);
                    }
                }
                else if(submitCode == 3){
                    model.deleteAdmin(user, subject, myActivity);


                }



            }

        });
         cancelBTN.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 // clears output text from last command
                 outcomeTV.setText("");
                 hideAndCelar();
                 createBTN.setVisibility(View.VISIBLE);
                 modifyBTN.setVisibility(View.VISIBLE);
                 deleteBTN.setVisibility(View.VISIBLE);
             }
         });
        createBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AdminValid", "Username of user:" + user.userName);
                // clears output text from last command
                outcomeTV.setText("");
                //So you can't do other things
                modifyBTN.setVisibility(View.INVISIBLE);
                deleteBTN.setVisibility(View.INVISIBLE);
                createBTN.setVisibility(View.INVISIBLE);
                //Need to make sure the id does not exist
                hideAndCelar();
                  cAdminID = adminIDET.getText().toString();
                  if(cAdminID.length() !=3){
                      Toast.makeText(getApplicationContext(), "Id must be three characters.", Toast.LENGTH_SHORT);
                  }
                  else {
                      Log.d("AdminValid", "ID in controller: " + cAdminID);
                      model.findAdminByID(1, cAdminID, user, myActivity);
                  }
            }
        });
        modifyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBTN.setVisibility(View.INVISIBLE);
                deleteBTN.setVisibility(View.INVISIBLE);
                modifyBTN.setVisibility(View.INVISIBLE);
                //Need to make sure the id does not exist
                hideAndCelar();
                cAdminID = adminIDET.getText().toString();
                if(cAdminID.length() !=3){
                    Toast.makeText(getApplicationContext(), "Id must be three characters.", Toast.LENGTH_SHORT);
                }
                else {
                    Log.d("AdminValid", "ID in controller: " + cAdminID);
                    Log.d("AdminValid", "Username of user:" + user.userName);
                    model.findAdminByID(2, cAdminID, user, myActivity);
                }
            }
        });
        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBTN.setVisibility(View.INVISIBLE);
                modifyBTN.setVisibility(View.INVISIBLE);
                deleteBTN.setVisibility(View.INVISIBLE);
                //Need to make sure the id does not exist
                hideAndCelar();
                cAdminID = adminIDET.getText().toString();
                if(cAdminID.length() !=3){
                    Toast.makeText(getApplicationContext(), "Id must be three characters.", Toast.LENGTH_SHORT);
                }
                else {
                    Log.d("AdminValid", "Username in controller: " + cAdminID);
                    model.findAdminByID(3, cAdminID, user, myActivity);
                }
            }
        });
    }
    public void adminIdCheckCB(int oppCode, boolean exists, boolean hasPermission, @Nullable Admin a){
        Log.d("AdminValid", "Exits: " + exists);
        if(oppCode ==1) {
            if (exists) {
                String no = "Admin with that user name already exists";
                outcomeTV.setText("" + no);
            }

            else {
                outcomeTV.setText("");
                //Now we are going to show all of the fields
                submitCode = 1;
                showFields();
                if (user.adminLevel.equals(AdminLevel.owner)) {
                    rankTV.setVisibility(View.VISIBLE);
                    middleAdminCB.setVisibility(View.VISIBLE);
                    ownerCB.setVisibility(View.VISIBLE);
                }

                Toast.makeText(getApplicationContext(), "Enter new admin credentials", Toast.LENGTH_LONG).show();

            }
        } else if (oppCode == 2) {

            if(!exists){
                String no = "Admin id does not exist";
                outcomeTV.setText("" + no);
            }
            else if(!hasPermission){
                Log.d("AdminValid", "User has permission: " + hasPermission);
                outcomeTV.setText("Insufficient Permissions");
            }
            else{
                outcomeTV.setText("");
                submitCode = 2;
                //Now we are going to show all of the fields
                showFields();
                subject = a;
                if(user.adminLevel.equals(AdminLevel.owner)){
                    rankTV.setVisibility(View.VISIBLE);
                    middleAdminCB.setVisibility(View.VISIBLE);
                    ownerCB.setVisibility(View.VISIBLE);
                    if(subject.adminLevel.equals(AdminLevel.owner))
                        ownerCB.setChecked(true);
                    else if(subject.adminLevel.equals(AdminLevel.managingStoreAdmin))
                        middleAdminCB.setChecked(true);

                }
                nameET.setText("" + subject.name);
                passwordET.setText("" + subject.password);

                Toast.makeText(getApplicationContext(),"Modify Admin Credentials", Toast.LENGTH_LONG).show();

            }
        }
        else if(oppCode == 3){
            if(!exists){
                String no = "Admin id does not exist";
                outcomeTV.setText("" + no);
            }
            else if(!hasPermission){
                Log.d("AdminValid", "User has permission: " + hasPermission);
                outcomeTV.setText("Insufficient Permissions");
            }
            else{
                submitCode = 3;
                //Now we are going to show all of the fields
                showFields();
                subject = a;
                if(user.adminLevel.equals(AdminLevel.owner)){
                    rankTV.setVisibility(View.VISIBLE);
                    middleAdminCB.setClickable(false);
                    middleAdminCB.setVisibility(View.VISIBLE);
                    ownerCB.setClickable(false);
                    ownerCB.setVisibility(View.VISIBLE);
                    if(subject.adminLevel.equals(AdminLevel.owner))
                        ownerCB.setChecked(true);
                    else if(subject.adminLevel.equals(AdminLevel.managingStoreAdmin))
                        middleAdminCB.setChecked(true);

                }
                nameET.setClickable(false);
                nameET.setText("" + subject.name);
                passwordET.setClickable(false);
                passwordET.setText("" + subject.password);

                Toast.makeText(getApplicationContext(),"Confirm Deletion", Toast.LENGTH_LONG).show();

            }
        }
    }


    public void goBackAction(View v){
        Intent data = new Intent();
        data.putExtra("EMPID", empid);
        setResult(0, data);
        finish();
    }
    private boolean hasPermission(Admin using, Admin chaning){
        if(using.userName.equals(chaning.userName))
            return false;
        else if(using.adminLevel.equals(AdminLevel.owner))
          return true;
        else if (chaning.adminLevel.equals(AdminLevel.storeAdmin))
          return true;
        else return false;

    }
    public void hideAndCelar(){


       nameTV.setVisibility(View.INVISIBLE);
        nameET.setText("");
        nameET.setVisibility(View.INVISIBLE);
        employeeTV.setVisibility(View.INVISIBLE);
        usernameET.setVisibility(View.INVISIBLE);
        usernameET.setText("");
        passwordTV.setVisibility(View.INVISIBLE);
        passwordET.setText("");
        passwordET.setVisibility(View.INVISIBLE);
        rankTV.setVisibility(View.INVISIBLE);
        middleAdminCB.setChecked(false);
        middleAdminCB.setVisibility(View.INVISIBLE);
        ownerCB.setChecked(false);
        ownerCB.setVisibility(View.INVISIBLE);
    }
    public void showFields(){
        nameTV.setVisibility(View.VISIBLE);
        nameET.setVisibility(View.VISIBLE);
        usernameET.setVisibility(View.VISIBLE);
        nameET.setClickable(true);
        employeeTV.setVisibility(View.VISIBLE);
        passwordTV.setVisibility(View.VISIBLE);
        passwordET.setVisibility(View.VISIBLE);
        passwordET.setClickable(true);
        ownerCB.setClickable(true);
        middleAdminCB.setClickable(true);

    }
    private boolean isInputValid(){
        Log.d("AdminValid", "In is input valid");
        wrong = "";
        String name = nameET.getText().toString();

        if(nameET.equals("") || nameET.equals(" ") || nameET.equals(null) || ! nameET.getText().toString().contains(" ")){
            wrong +="Invalid name. Must have first and last name.";
        }
        else if(passwordET.getText().toString().length() <8){
            wrong += "Password must be eight characters long";
        }
        Log.d("AdminValid", "Leaving input valid");
        return wrong.equals("");
    }
    private AdminLevel levelFinder(){
        if(ownerCB.isChecked())
            return AdminLevel.owner;
        else if(middleAdminCB.isChecked())
            return  AdminLevel.managingStoreAdmin;
        else return AdminLevel.storeAdmin;
    }

    @Override
    public void adminNotFound() {
        Toast.makeText(getApplicationContext(), "Admin find Failure. Please restart and try again", Toast.LENGTH_SHORT);

    }
    public void aCreateCB(boolean success){
       if(success) {
           Log.d("AdminValid", "Admin was added successfully");
           outcomeTV.setText("Creation Success!");
           hideAndCelar();
           showButtons();
       }
       else Toast.makeText(getApplicationContext(), "Unable to create admin at this time", Toast.LENGTH_LONG);
    }
    public void aModifyCB(boolean success){
        if(success) {
            outcomeTV.setText("Update Success!");
            hideAndCelar();
            showButtons();
        }
        else Toast.makeText(getApplicationContext(), "Unable to update admin at this time", Toast.LENGTH_LONG);

    }

    public void aDelCB(boolean success){
        if(success) {
            outcomeTV.setText("Deletion Success!");
            hideAndCelar();
            showButtons();
        }
        else Toast.makeText(getApplicationContext(), "Unable to delete admin at this time", Toast.LENGTH_LONG);

    }
    private void showButtons(){
        createBTN.setVisibility(View.VISIBLE);
        modifyBTN.setVisibility(View.VISIBLE);
        deleteBTN.setVisibility(View.VISIBLE);
    }

    public void adminUsernameCB(boolean valid){
        if(!valid) {
            model.createAdmin(cName, cAdminID, username, cpw, level, myActivity);
        }
        else{
            outcomeTV.setText("Username already exists or is invalid");
        }
    }


}