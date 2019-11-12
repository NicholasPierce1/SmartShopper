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
implements ProductCUD.CUDopperations, ProductInput.buttonInput {

    ProductCUD cud;
    ProductInput input;
    String empid,  vendor, name, dept;
    double price;
    String wrong = "";
    public static int submitCode = -1;
    public static String barcode;
    TextView resultTV;
    Button crateBTN, modifyBTN, deleteBTN, pSubmitBTN, pCancleBTN, backBTN;
    FragmentManager manager;


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
        hideAndClear();

        pCancleBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAndClear();
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


    private void hideAndClear() {
//        nameTV.setVisibility(View.INVISIBLE);
//        nameET.setVisibility(View.INVISIBLE);
//        nameET.setText("");
//        vendorTV.setVisibility(View.INVISIBLE);
//        vendorET.setVisibility(View.INVISIBLE);
//        vendorET.setText("");
//       deptTV.setVisibility(View.INVISIBLE);
//       deptET.setVisibility(View.INVISIBLE);
//       deptET.setText("");
//       isleTV.setVisibility(View.INVISIBLE);
//       isleET.setVisibility(View.INVISIBLE);
//       isleET.setText("");
//       priceTV.setVisibility(View.INVISIBLE);
//       priceET.setVisibility(View.INVISIBLE);
//       priceET.setText("");
//       nameET.setClickable(true);
//       vendorET.setClickable(true);
//       priceET.setClickable(true);
//       deptET.setClickable(true);
//       isleET.setClickable(true);


    }

    private void showFields() {
//        nameTV.setVisibility(View.VISIBLE);
//        nameET.setVisibility(View.VISIBLE);
//        vendorTV.setVisibility(View.VISIBLE);
//        vendorET.setVisibility(View.VISIBLE);
//        deptTV.setVisibility(View.VISIBLE);
//        deptET.setVisibility(View.VISIBLE);
//        isleTV.setVisibility(View.VISIBLE);
//        isleET.setVisibility(View.VISIBLE);
//        priceTV.setVisibility(View.VISIBLE);
//        priceET.setVisibility(View.VISIBLE);
    }


    private boolean isEmpty(String s) {
        return (s == null || s.equals("") || s.equals(" "));
    }

    public void buttonPressed(int code, String barcode) {
        this.barcode = barcode;
        FragmentTransaction transaction = manager.beginTransaction();
        if (code == 1) {
            //So you can't do other things

            //Need to make sure the id does not exist

//                barcode = barCodeET.getText().toString();
            if (AdminInputValidationHandler.isExistingValue(true, barcode)) {
                String no = "Product with that Barcode name already exists";
                resultTV.setText("" + no);
            } else {
                //Now we are going to show all of the fields
                transaction.show(input);
                transaction.hide(cud);
                submitCode = 1;
                Toast.makeText(getApplicationContext(), "Enter commodity details", Toast.LENGTH_LONG).show();

            }
        } else if (code == 2) {
            if (!AdminInputValidationHandler.isExistingValue(true, barcode)) {
                String no = "Barcode  does not exist";
                resultTV.setText("" + no);
            } else {
                transaction.show(input);
                transaction.hide(cud);
                submitCode = 2;
                //Now we are going to show all of the fields


                Commodity subject = CommodityMockModelTakeTwo.shared.getCommodityFromBarcode(barcode);
//
//                    nameET.setText("" + subject.name);
//                    vendorET.setText("" + subject.vendorName);
//                    priceET.setText("" + subject.price);
//                    deptET.setText("" + subject.department);
//                    isleET.setText("" + subject.location);


                Toast.makeText(getApplicationContext(), "Modify Commodity details", Toast.LENGTH_LONG).show();

            }
        } else if (code == 3) {
            crateBTN.setVisibility(View.INVISIBLE);
            modifyBTN.setVisibility(View.INVISIBLE);
            //Need to make sure the id does not exist
            hideAndClear();
//                barcode= barCodeET.getText().toString();
            if (!AdminInputValidationHandler.isExistingValue(true, barcode)) {
                String no = "Barcode  does not exist";
                resultTV.setText("" + no);
            } else {
                transaction.show(input);
                transaction.hide(cud);
                submitCode = 3;


                //   Commodity subject = CommodityMockModelTakeTwo.shared.getCommodityFromBarcode(barcode);
//                    nameET.setClickable(false);
//                    vendorET.setClickable(false);
//                    priceET.setClickable(false);
//                    deptET.setClickable(false);
//                    isleET.setClickable(false);
//                    nameET.setText("" + subject.name);
//                    vendorET.setText("" + subject.vendorName);
//                    priceET.setText("" + subject.price);
//                    deptET.setText("" + subject.department);
//                    isleET.setText("" + subject.location);
                Toast.makeText(getApplicationContext(), "Confrim Deletion", Toast.LENGTH_LONG).show();

            }
        }

        transaction.commit();
    }

    public void submitButtonPushed(int actionCode, Commodity c) {
        //NICK
        final AdminProductScreenActivity controller = this;
        if (submitCode == -1) {
            resultTV.setText("Not able to submit at this time");
        } else if (submitCode == 1) {
            if (isInputValid(c)) {
                // TODO: 11/11/2019 do database stuff here for creation 
            } else {
                Toast.makeText(controller, "Failure: " + wrong, Toast.LENGTH_LONG).show();
            }
        } else if (submitCode == 2) {
            if (isInputValid(c)) {
                // TODO: 11/11/2019 Do Database stuff here for update 
            } else {
                Toast.makeText(controller, "Failure: " + wrong, Toast.LENGTH_LONG).show();
            }
        } else if (submitCode == 3) {
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
}