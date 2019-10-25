package com.example.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;
//@Author Matthew Berry

public class CreateAdminScreenActivity extends AppCompatActivity {
    String empid;
    Admin user;
    EditText adminIDET, nameET, passwordET;
    TextView outcomeTV, rankTV, employeeDisTV, employeeTV, nameTV, passwordTV, adminIDTV;
    CheckBox middleAdminCB;
    CheckBox ownerCB;
    String wrong = "";
    Button createBTN, modifyBTN, deleteBTN,cancelBTN;
    Button submitBTN;
    String empIDDIS = "";
    int submitCode = -1;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.admin_modify);
        Intent ini = getIntent();
        empid = ini.getStringExtra("EMPID");
        user = AdminMockModelClass.adminFinder(empid);
        adminIDET = findViewById(R.id.AdminIDET);
        outcomeTV = findViewById(R.id.outcomeTV);
        middleAdminCB = findViewById(R.id.middleAdminCB);
        ownerCB = findViewById(R.id.ownerCB);
        rankTV = findViewById(R.id.RankTV);
        employeeDisTV = findViewById(R.id.EmployeeIdDisTV);
        employeeTV = findViewById(R.id.EmployeeIDTV);
        nameTV = findViewById(R.id.NameTV);
        passwordTV = findViewById(R.id.PasswordTV);
        nameET = findViewById(R.id.nameET);
        passwordET = findViewById(R.id.PasswordET);
        employeeDisTV.setText("");


        createBTN = findViewById(R.id.CreateAdminBTN);
        modifyBTN = findViewById(R.id.ModifyAdminBTN);
        deleteBTN = findViewById(R.id.RemoveAdminBTN);
        cancelBTN = findViewById(R.id.cancelBTN);
        submitBTN = findViewById(R.id.submitBTN);
        boolean isNull = nameET == null;
        Log.d("NullFun", "Is name null:" + isNull);
        hideAndCelar();
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(submitCode == -1){
                    outcomeTV.setText("Not able to submit at this time");
                }
                else if (submitCode == 1){
                if(isInputValid()){
                    String name = nameET.getText().toString();
                    String pw = passwordET.getText().toString();
                    AdminLevel level = levelFinder();
                    Store store = AdminMockModelClass.storeBuilder();
                    Admin newAdmin = new Admin(name, empIDDIS, pw, level, store);
                    //We would make a real model call to create it, but for now...
                    AdminMockModelClass.fakeCreator(newAdmin);
                    outcomeTV.setText("Creation Success!");
                    hideAndCelar();
                }
                else{
                    outcomeTV.setText("Invalid input: " +wrong);
                }
            }
                else if(submitCode == 2){
                    if(isInputValid()){
                        String name = nameET.getText().toString();
                        String pw = passwordET.getText().toString();
                        AdminLevel level = levelFinder();
                        Store store = AdminMockModelClass.storeBuilder();
                        Admin newAdmin = new Admin(name, empIDDIS, pw, level, store);
                        //We would make a real model call to create it, but for now...
                        AdminMockModelClass.fakeUpdator(newAdmin);
                        outcomeTV.setText("Update Success!");
                        hideAndCelar();
                    }
                    else{
                        outcomeTV.setText("Invalid input: " +wrong);
                    }
                }
                else if(submitCode == 3){
                    AdminMockModelClass.fakeDestroyer(empIDDIS);
                    outcomeTV.setText("" + "Admin was removed");
                    hideAndCelar();

                }
                createBTN.setVisibility(View.VISIBLE);
                modifyBTN.setVisibility(View.VISIBLE);
                deleteBTN.setVisibility(View.VISIBLE);
            }

        });
         cancelBTN.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 hideAndCelar();
                 createBTN.setVisibility(View.VISIBLE);
                 modifyBTN.setVisibility(View.VISIBLE);
                 deleteBTN.setVisibility(View.VISIBLE);
             }
         });
        createBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //So you can't do other things
                modifyBTN.setVisibility(View.INVISIBLE);
                deleteBTN.setVisibility(View.INVISIBLE);
                //Need to make sure the id does not exist
                hideAndCelar();
                  empIDDIS = adminIDET.getText().toString();
                if(AdminInputValidationHandler.isExistingValue(false, empIDDIS)){
                   String no = "Admin with that user name already exists";
                   outcomeTV.setText("" + no);
                }
                else{
                    //Now we are going to show all of the fields
                    submitCode = 1;
                    showFields();
                    employeeDisTV.setText("" + empIDDIS);
                    if(user.adminLevel.equals(AdminLevel.owner)){
                        rankTV.setVisibility(View.VISIBLE);
                        middleAdminCB.setVisibility(View.VISIBLE);
                            ownerCB.setVisibility(View.VISIBLE);
                        }

                    Toast.makeText(getApplicationContext(),"Enter new admin credentials", Toast.LENGTH_LONG).show();

                }
            }
        });
        modifyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBTN.setVisibility(View.INVISIBLE);
                deleteBTN.setVisibility(View.INVISIBLE);
                //Need to make sure the id does not exist
                hideAndCelar();
                empIDDIS= adminIDET.getText().toString();
                if(!AdminInputValidationHandler.isExistingValue(false, empIDDIS)){
                    String no = "Admin id does not exist";
                    outcomeTV.setText("" + no);
                }
                else if(!hasPermission(user, AdminMockModelClass.adminFinder(empIDDIS))){
                    outcomeTV.setText("Insufficient Permissions");
                }
                else{
                    submitCode = 2;
                    //Now we are going to show all of the fields
                    showFields();
                    employeeDisTV.setText("" + empIDDIS);
                    Admin subject = AdminMockModelClass.adminFinder(empIDDIS);
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
        });
        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBTN.setVisibility(View.INVISIBLE);
                modifyBTN.setVisibility(View.INVISIBLE);
                //Need to make sure the id does not exist
                hideAndCelar();
                empIDDIS= adminIDET.getText().toString();
                if(!AdminInputValidationHandler.isExistingValue(false, empIDDIS)){
                    String no = "Admin id does not exist";
                    outcomeTV.setText("" + no);
                }
                else if(!hasPermission(user, AdminMockModelClass.adminFinder(empIDDIS))){
                    outcomeTV.setText("Insufficient Permissions");
                }
                else{
                    submitCode = 3;
                    //Now we are going to show all of the fields
                    showFields();
                    employeeDisTV.setText("" + empIDDIS);
                    Admin subject = AdminMockModelClass.adminFinder(empIDDIS);
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

                    Toast.makeText(getApplicationContext(),"Modify Admin Credentials", Toast.LENGTH_LONG).show();

                }
            }
        });
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
        employeeDisTV.setVisibility(View.INVISIBLE);
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
        nameET.setClickable(true);
        employeeTV.setVisibility(View.VISIBLE);
        employeeDisTV.setVisibility(View.VISIBLE);
        passwordTV.setVisibility(View.VISIBLE);
        passwordET.setVisibility(View.VISIBLE);
        passwordET.setClickable(true);
        ownerCB.setClickable(true);
        middleAdminCB.setClickable(true);

    }
    private boolean isInputValid(){
        wrong = "";
        String name = nameET.getText().toString();

        if(nameET.equals("") || nameET.equals(" ") || nameET.equals(null)){
            wrong +="Invalid name. ";
        }
        else if(passwordET.getText().toString().length() <8){
            wrong += "Password must be eight characters long";
        }
        return wrong.equals("");
    }
    private AdminLevel levelFinder(){
        if(ownerCB.isChecked())
            return AdminLevel.owner;
        else if(middleAdminCB.isChecked())
            return  AdminLevel.managingStoreAdmin;
        else return AdminLevel.storeAdmin;
    }

}