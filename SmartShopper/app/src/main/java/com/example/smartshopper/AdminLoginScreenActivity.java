package com.example.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginScreenActivity extends AppCompatActivity implements LoginCB {
    TextView errorTV;
    Model model;
    //@Author Matthew Berry
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.admin_login);
        //we might need to do more here later
        Intent ini = getIntent();
        errorTV = findViewById(R.id.ErrorTV);
        errorTV.setVisibility(View.INVISIBLE);
        model = Model.getShared();
            Log.d("loginbugs", "fake data populated");

    }
    public  void loginAction(View v){
        EditText usernameET = findViewById(R.id.userNameET);
        String user = usernameET.getText().toString();
        EditText passwordET = findViewById(R.id.PasswordET);
        String pw = passwordET.getText().toString();
        model.login(user, pw, this);

    }
//    private boolean validateLogin(String user, String pw){
//        Log.d("LoginDeBug", "In Login Validation");
//        //For now this will be a hard codeed thing.
//        // In the Future we will have a method that validates username and password.
//        int index = AdminMockModelClass.adminUserNames.indexOf(user);
//        Log.d("LoginDeBug", AdminMockModelClass.adminUserNames.get(index));
//
//        Log.d("LoginDeBug", "Username used: " + user + "\n User retrived:" + AdminMockModelClass.adminUserNames.get(index)
//                + "With index: " + index);
//        Log.d("LoginDeBug", "Password used:" + pw + "\n Passwrod retrived: " + AdminMockModelClass.adminPw.get(index)
//                + "\nWith index: " + AdminMockModelClass.adminPw.indexOf(pw) + "Index used: " + index);
//
//      if(AdminMockModelClass.adminUserNames.contains(user)){
//
//
//         if(pw.equals(AdminMockModelClass.adminPw.get(index))){
//             return true;
//         }
//         else return false;
//      }
//      else return false;
//
//    }

    public void cancleAction(View v){
        Intent goBack = new Intent(this, Welcome_screenActivity.class);
        startActivity(goBack);
    }

    public void loginCB(boolean success, boolean foundInStore, Admin admin){
        if(success && foundInStore){

            Intent toHub = new Intent(this, AdminScreenActivity.class);
            toHub.putExtra("Admin", admin);
            startActivity(toHub);
        }
        else{
            errorTV.setVisibility(View.VISIBLE);
        }
    }



}
