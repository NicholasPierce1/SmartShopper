package com.example.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginScreenActivity extends AppCompatActivity {
    TextView errorTV;
    //@Author Matthew Berry
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.admin_login);
        //we might need to do more here later
        Intent ini = getIntent();
        errorTV = findViewById(R.id.ErrorTV);
        errorTV.setVisibility(View.INVISIBLE);

    }
    public  void loginAction(View v){
        EditText usernameET = findViewById(R.id.userNameET);
        String user = usernameET.getText().toString();
        EditText passwordET = findViewById(R.id.PasswordET);
        String pw = passwordET.getText().toString();
        if(validateLogin(user, pw)){
            Intent toHub = new Intent(this, AdminScreenActivity.class);
            toHub.putExtra("EMPID", user);
            startActivity(toHub);
        }
        else{
            errorTV.setVisibility(View.VISIBLE);
        }

    }
    private boolean validateLogin(String user, String pw){
        //For now this wull be a hard codeed thing.
        // In the Future we will have a method that validates username and password.

      if(AdminMockModelClass.adminUserNames.contains(user)){
         int index = AdminMockModelClass.adminUserNames.indexOf(user);
         if(pw.equals(AdminMockModelClass.adminPw.get(index))){
             return true;
         }
         else return false;
      }
      else return false;
    }

    public void cancleAction(View v){
        Intent goBack = new Intent(this, Welcome_screenActivity.class);
        startActivity(goBack);
    }


}
