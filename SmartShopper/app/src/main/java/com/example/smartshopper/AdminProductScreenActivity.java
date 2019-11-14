package com.example.smartshopper;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
//@Author Matthew Berry

public class AdminProductScreenActivity extends AppCompatActivity
implements ProductCUD.CUDopperations, ProductInput.buttonInput, AdminProductCBMethods {
    private static AdminProductScreenActivity shared = new AdminProductScreenActivity();

    public static AdminProductScreenActivity getShared(){
        return  shared;
    }

    ProductCUD cud;
    ProductInput input;
    String empid,  vendor, name, dept;
    double price;
    String wrong = "";
    public static int submitCode = -1;
    public static int createCode = -1;
    public static String barcode;
    TextView resultTV;
    Button crateBTN, modifyBTN, deleteBTN, pSubmitBTN, pCancleBTN, backBTN;
    FragmentManager manager;
   private Model model = Model.getShared();


    @Override
    public void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.admin_product);
        Intent ini = getIntent();
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


        //Button crateBTN, modifyBTN, deleteBTN, pSubmitBTN, pCancleBTN, backBTN;

        backBTN = findViewById(R.id.BackBTN);


        pCancleBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                crateBTN.setVisibility(View.VISIBLE);
                modifyBTN.setVisibility(View.VISIBLE);
                deleteBTN.setVisibility(View.VISIBLE);
            }
        });

        pSubmitBTN.setOnClickListener(new View.OnClickListener() {
            // TODO: 11/10/2019 MOVE THIS TO FRAGMENT 
            @Override
            public void onClick(View view) {
//                vendor = vendorET.getText().toString();
//                price = Double.valueOf(vendorET.getText().toString());
//                dept = deptET.getText().toString();
//                int isleNum = Integer.valueOf(isleET.getText().toString());
                //STUBBED LOGIC


            }

        });
        pCancleBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


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
    }

    public void buttonPressedCB(int code, String barcode, int createCase, Commodity commodity) {
        this.barcode = barcode;
        Bundle bundle = new Bundle();
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
            if (createCase != 4) {
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

            //Need to make sure the id does not exist

//                barcode= barCodeET.getText().toString();
            if (createCase != 4) {
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
        input.prepareFragmentForPresentation(commodity);
    }

    // TODO: 11/12/2019 Change paramaters to what you only need not a full comodity 
    public void submitButtonPushed(int actionCode, Bundle c) {
        c.putString("barcode", barcode);
        //NICK
        final AdminProductScreenActivity controller = this;
        if (submitCode == -1) {
            resultTV.setText("Not able to submit at this time");
        }
        else if (submitCode == 1 || submitCode == 2) {


        }
        else if (submitCode == 3) {
            // TODO: 11/11/2019 do database stuff here for delete and use if else block to validate input on call back 
            if (CommodityMockModelTakeTwo.shared.removeItem(barcode)) {
                // success
                Toast.makeText(controller, "success", Toast.LENGTH_SHORT).show();
            } else {
                // fail
                Toast.makeText(controller, "fail", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void cancelButtonPushed() {
        FragmentTransaction transaction1 = manager.beginTransaction();
        transaction1.show(cud);
        transaction1.hide(input);
        transaction1.commit();
        submitCode =-1;
    }


    private boolean isInputValid(Commodity c) {
        //NICK
        wrong = "";
        // TODO: 11/11/2019 replace mocck model with real model methods
        if (isEmpty(c.vendorName) || isEmpty(c.department.toString()) || isEmpty(c.name)) {
            wrong += "Blank fields are not allowed";
        }
        if (c.price <= 0) {
            wrong += "Price cannnot be less than zero";
        }
        if (CommodityMockModel.vendors.contains(vendor)) {
            if (name.equals(CommodityMockModel.names.get(CommodityMockModel.vendors.indexOf(vendor)))) {
                wrong += "Vendor already has a product with this name";
            }
        } else ;
        return wrong.equals("");

    }

    // TODO: 11/12/2019 Get nick to create and adapter method that spits out a comodity baased off
    // TODO: 11/12/2019 a barcode per case one of admin create. 
    public void getNameandVendorForFragment(String barcode){
        model.getNameFromBarcode(barcode, this);

    }
    public void getNameandVendorForFragmentCB(String name, String vendor){
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("vendor", vendor);
        
    }

    @Override
    public void validationCB(String wrong, int opp, Bundle b) {
        if(!wrong.equals("")){
            resultTV.setText("Invalid input: \n" + wrong );
        }
        //we no KNOW that stuff works soooo....
        if(opp == 1){
            model.createItem(b);
        }
        // TODO: 11/14/2019 update thingy 

    }

    @Override
    public void createCB(boolean cb) {
        if(cb){
            // TODO: 11/14/2019 Neeed to create toast logic and swap fragments
        }

    }

    @Override
    public void updateCB(boolean cb) {

    }

    @Override
    public void delCB(boolean cb) {

    }
}