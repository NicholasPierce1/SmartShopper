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
        return (s == null || s.equals("") || s.equals(" "));
    }

    public void buttonPressed(int code, String barcode){
      if(!isEmpty(barcode))
       model.validateBarcode( barcode, code, this);
      else{
          Toast.makeText(this, "Barcode is empty", Toast.LENGTH_SHORT);
      }
    }

    public void buttonPressedCB(int code, String barcode, int createCase, Commodity commodity) {
        this.barcode = barcode;
        FragmentTransaction transaction = manager.beginTransaction();
        if (code == 1) { //create
            if (createCase == 2) {
                String no = "Product with that Barcode name already exists";
                resultTV.setText("" + no);
            }
            else if(createCase == 1){
                //Now we are going to show all of the fields
                transaction.show(input);
                transaction.hide(cud);
                submitCode = code;
                createCode = createCase;
                Toast.makeText(getApplicationContext(), "Enter commodity details", Toast.LENGTH_LONG).show();

            }
            else if(createCase == 3){
                //Now we are going to show all of the fields
                transaction.show(input);
                transaction.hide(cud);
                submitCode = code;
                createCode = createCase;

                Toast.makeText(getApplicationContext(), "Enter commodity details", Toast.LENGTH_LONG).show();

            }


        } else if (code == 2) { //update
            Log.d("ProductProbs", "Probs: " +createCase);
            if (createCase != 2) {
                String no = "Barcode  does not exist";
                resultTV.setText("" + no);
            } else {
                transaction.show(input);
                transaction.hide(cud);
                submitCode = code;
                //Now we are going to show all of the fields
                Toast.makeText(getApplicationContext(), "Modify Commodity details", Toast.LENGTH_LONG).show();

            }
        }
        else if (code == 3) { //delete
            if (createCase != 2) {
                String no = "Barcode  does not exist";
                resultTV.setText("" + no);
            } else {
                transaction.show(input);
                transaction.hide(cud);
                submitCode = code;
                Toast.makeText(getApplicationContext(), "Confrim Deletion", Toast.LENGTH_LONG).show();

            }
        }
        input.setCreateCase(createCase);
        input.setSubmitCode(submitCode);
        transaction.commit();
        msgCenterTV.setText("Enter prodcut details");
        input.prepareFragmentForPresentation(commodity,model.getDepartmentName());
    }

    public void submitButtonPushed(int actionCode, boolean check, Bundle c) {
        c.putString("barcode", barcode);
        if (submitCode == -1) {
            resultTV.setText("Not able to submit at this time");
        }
        else if (submitCode == 1 || submitCode == 2) {
            if(submitCode ==1 )
             c.putString("barcode", barcode);
            model.validateComodityInput(check, actionCode, c, this);
            
        }
        else if (submitCode == 3) {
            // TODO: 11/11/2019 do database stuff here for delete and use if else block to validate input on call back 
            model.deleteItem((Commodity)c.getSerializable("C"), this);

        }

    }

    @Override
    public void cancelButtonPushed() {
       showCUDFragment();
        submitCode =-1;
    }




    @Override
    public void validationCB(String wrong, int opp, Bundle b) {
        Log.d("ProductProbs", wrong);
        if(!wrong.equals("")){
            resultTV.setText("Invalid input: \n" + wrong );
        }
        //we no KNOW that stuff works soooo....
        if(opp == 1){
            b.putString("barcode", barcode);
            model.createItem(b, this);
        }
        else model.updateItem(b, this);


    }
//
    @Override
    public void createCB(boolean cb) {
        if(cb){
           Toast.makeText(getApplicationContext(), "Creation was a success!", Toast.LENGTH_SHORT);
           showCUDFragment();
        }
        else{
            Toast.makeText(getApplicationContext(), "Creation failed. Please try again later", Toast.LENGTH_SHORT);
        }

    }

    @Override
    public void updateCB(boolean cb) {
        if(cb){
            Toast.makeText(getApplicationContext(), "Update was a success!", Toast.LENGTH_SHORT);
            showCUDFragment();
        }
        else{
            Toast.makeText(getApplicationContext(), "Update failed. Please try again later", Toast.LENGTH_SHORT);
        }

    }

    @Override
    public void delCB(boolean cb) {
        if(cb){
            Toast.makeText(getApplicationContext(), "Deletion was a success!", Toast.LENGTH_SHORT);
            showCUDFragment();
        }
        else{
            Toast.makeText(getApplicationContext(), "Deletion failed. Please try again later", Toast.LENGTH_SHORT);
        }

    }

    private void showCUDFragment(){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(input);
        transaction.show(cud);
        transaction.commit();
    }
    public ArrayList<String> getDeptListForFragment(){
        return model.getDepartmentName();
    }

}