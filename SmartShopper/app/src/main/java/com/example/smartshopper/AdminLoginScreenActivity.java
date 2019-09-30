package com.example.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginScreenActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.admin_login);
        //we might need to do more here later
        Intent ini = getIntent();

    }
    public void loginAction(View v){
        EditText usernameET = findViewById(R.id.userNameET);
        String user = usernameET.getText().toString();
        EditText passwordET = findViewById(R.id.PasswordET);
        String pw = passwordET.getText().toString();
        if(validateLogin(user, pw)){
            Intent toHub = new Intent(this, AdminScreenActivity.class);
            toHub.putExtra("EMPID", "001");
            startActivity(toHub);
        }
        else{
            TextView result = findViewById(R.id.ResultTV);
            result.setVisibility(View.VISIBLE);
        }

    }
    private boolean validateLogin(String user, String pw){
        //For now this wull be a hard codeed thing.
        // In the Future we will have a method that validates username and password.
        if(user.equals("001")){
            if(pw.equals("admin")){
                return true;
            }
            else return false;
        }
        else return false;
    }

    public void cancleAction(View v){
        Intent goBack = new Intent(this, MainActivity.class);
        startActivity(goBack);
    }


}
