package com.example.smartshopper;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
//@Author Matthew Berry

public class AdminProductScreenActivity extends AppCompatActivity
implements ProductCUD.CUDopperations, ProductInput.buttonInput, AdminProductCBMethods {
    private static AdminProductScreenActivity shared = new AdminProductScreenActivity();

    public static AdminProductScreenActivity getShared(){
        return  shared;
    }

    ProductCUD cud;
    ProductInput input;
    String empid,name;
    public static int submitCode = -1;
    public static int createCode = -1;
    public static String barcode;
    TextView resultTV, msgCenterTV;
    FragmentManager manager;
   private Model model = Model.getShared();


    @Override
    public void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.admin_product);
        Intent ini = getIntent();
        msgCenterTV = findViewById(R.id.msgCenterTV);
        msgCenterTV.setText("Enter Barcode Number");
        empid = ini.getStringExtra("EMPID");
        manager = getSupportFragmentManager();
        //Creates new fragments for use
        final FragmentTransaction transaction = manager.beginTransaction();
        cud = new ProductCUD();
        transaction.add(R.id.modifyCTNR, cud);
        input = new ProductInput();
        transaction.add(R.id.modifyCTNR, input);
        transaction.show(cud);
        transaction.hide(input);
        transaction.commit();
        resultTV = findViewById(R.id.ResultTV);

    }

    public void goBackAction(View v) {
        Intent data = new Intent();
        data.putExtra("EMPID", empid);
        setResult(0, data);
        finish();
    }

    private boolean isEmpty(String s) {
        return (s == null || s.equals("") || s.equals(" ") || s.length()!=6);
    }

    public void buttonPressed(int code, String barcode){
      if(!isEmpty(barcode))
       model.validateBarcode( barcode, code, this);
      else{
          Toast.makeText(this, "Barcode must be six numbers", Toast.LENGTH_SHORT).show();
      }
    }

    public void buttonPressedCB(int code, String barcode, int createCase, Commodity commodity) {
        boolean needToBlock = false;
        this.barcode = barcode;
        FragmentTransaction transaction = manager.beginTransaction();
        if (code == 1) { //create
            if (createCase == 2) {
                String no = "Product with that Barcode name already exists";
                resultTV.setText("" + no);
                needToBlock = true;
            }
            else if(createCase == 1){
                //Now we are going to show all of the fields
                transaction.show(input);
                transaction.hide(cud);
                submitCode = code;
                createCode = createCase;
                Toast.makeText(this, "Enter commodity details", Toast.LENGTH_LONG).show();
                needToBlock = false;

            }
            else if(createCase == 3){
                //Now we are going to show all of the fields
                transaction.show(input);
                transaction.hide(cud);
                submitCode = code;
                createCode = createCase;
                needToBlock = false;
                Toast.makeText(this, "Enter commodity details", Toast.LENGTH_LONG).show();

            }


        } else if (code == 2) { //update
            Log.d("ProductProbs", "Probs: " +createCase);
            if (createCase != 2) {
                Log.d("ProductProbs", "Product has deemed invalid for updation");
                String no = "Barcode  does not exist";
                needToBlock = true;
                resultTV.setText("" + no);
            } else {
                Log.d("ProductProbs", "Product has been validated for updation");
                transaction.show(input);
                transaction.hide(cud);
                submitCode = code;
                needToBlock = false;
                //Now we are going to show all of the fields
                Toast.makeText(getApplicationContext(), "Modify Commodity details", Toast.LENGTH_LONG).show();

            }
        }
        else if (code == 3) { //delete
            if (createCase != 2) {
                Log.d("ProductProbs", "Product has deemed invalid for deletion");
                String no = "Barcode  does not exist";
                resultTV.setText("" + no);
                needToBlock = true;
            } else {
                needToBlock = false;
                Log.d("ProductProbs", "Product has been validated for deletion");
                transaction.show(input);
                transaction.hide(cud);
                submitCode = code;
                Toast.makeText(getApplicationContext(), "Confirm Deletion", Toast.LENGTH_LONG).show();

            }
        }
        if(!needToBlock){
        input.setCreateCase(createCase);
        input.setSubmitCode(submitCode);
        transaction.commit();
        msgCenterTV.setText("Enter product details");
        resultTV.setText("");
        input.prepareFragmentForPresentation(commodity,model.getDepartmentName());
        }
    }

    public void submitButtonPushed(int actionCode, boolean check, Bundle c) {
        if(c.getBoolean("isSafe")) {//need to make sure that there was nothing wrong when
            //the bundle was created
            c.putString("barcode", barcode);
            if (submitCode == -1) {
                resultTV.setText("Not able to submit at this time");
            } else if (submitCode == 1 || submitCode == 2) {
                if (submitCode == 1)
                    c.putString("barcode", barcode);
                model.validateComodityInput(check, actionCode, c, this);

            } else if (submitCode == 3) {
                model.deleteItem((Commodity) c.getSerializable("C"), this);

            }
        }
    }

    @Override
    public void cancelButtonPushed() {
        submitCode =-1;
       showCUDFragment();

    }




    @Override
    public void validationCB(String wrong, int opp, Bundle b) {
        Log.d("ProductProbs", wrong);
        if(!wrong.equals("")){
            Toast.makeText(this,"Invalid input:" + wrong, Toast.LENGTH_LONG).show();
        }
        else {
            //we no KNOW that stuff works soooo....
            if (opp == 1) {
                b.putString("barcode", barcode);
                model.createItem(b, this, createCode == 1);
            } else model.updateItem(b, this);
        }

    }
//
    @Override
    public void createCB(boolean cb) {
        Log.d("CreateSuccess", "In create cb");
        if(cb){
            Log.d("CreateSuccess", "In create if statement");
           Toast.makeText(this, "Creation was a success!", Toast.LENGTH_SHORT).show();
           Log.d("CreateSuccess", "Toast message was sent");
           showCUDFragment();
        }
        else{
            Log.d("CreateSuccess", "In create else statement");

            Toast.makeText(this, "Creation failed. Please try again later", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void updateCB(boolean cb) {
        if(cb){
            Toast.makeText(this, "Update was a success!", Toast.LENGTH_SHORT).show();
            showCUDFragment();
        }
        else{
            Toast.makeText(this, "Update failed. Please try again later", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void delCB(boolean cb) {
        if(cb){
            Toast.makeText(this, "Deletion was a success!", Toast.LENGTH_SHORT).show();
            showCUDFragment();
        }
        else{
            Toast.makeText(this, "Deletion failed. Please try again later", Toast.LENGTH_SHORT).show();
        }

    }

    private void showCUDFragment(){
        FragmentTransaction transaction = manager.beginTransaction();
        cud.removeBarcode();
        transaction.hide(input);
        transaction.show(cud);
        transaction.commit();
        resultTV.setText("");

    }
    public ArrayList<String> getDeptListForFragment(){
        return model.getDepartmentName();
    }

}