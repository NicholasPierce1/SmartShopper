package com.example.smartshopper;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
//@Author Matthew Berry

public class AdminProductScreenActivity extends AppCompatActivity {
    String empid, barcode, vendor, name, dept;
    double price;
    String wrong = "";
    int submitCode = -1;
    TextView nameTV, vendorTV, deptTV, isleTV, priceTV, resultTV;
    EditText barCodeET, nameET, vendorET, deptET, isleET, priceET;
    Button crateBTN, modifyBTN, deleteBTN, pSubmitBTN, pCancleBTN, backBTN;


    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.admin_product);
        Intent ini = getIntent();
        empid = ini.getStringExtra("EMPID");

        nameTV = findViewById(R.id.NameTV);
        vendorTV = findViewById(R.id.VendorTV);
        deptTV = findViewById(R.id.DeptTV);
        isleTV = findViewById(R.id.islieTV);
        priceTV = findViewById(R.id.priceTV);
        resultTV = findViewById(R.id.ResultTV);
        barCodeET = findViewById(R.id.BarcodeET);
        nameET = findViewById(R.id.NameET);
        vendorET = findViewById(R.id.DeptET);
        deptET = findViewById(R.id.DeptET);
        isleET = findViewById(R.id.IsleET);
        priceET = findViewById(R.id.priceET);
        //Button crateBTN, modifyBTN, deleteBTN, pSubmitBTN, pCancleBTN, backBTN;
        crateBTN = findViewById(R.id.createBTN);
        modifyBTN = findViewById(R.id.ModifyBTN);
        deleteBTN = findViewById(R.id.RemoveBTN);
        pSubmitBTN = findViewById(R.id.pSubmitBTN);
        pCancleBTN = findViewById(R.id.pCancelBTN);
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
            @Override
            public void onClick(View view) {
                vendor = vendorET.getText().toString();
                price = Double.valueOf(vendorET.getText().toString());
                dept = deptET.getText().toString();
                int isleNum = Integer.valueOf(isleET.getText().toString());
                //STUBBED LOGIC


                if(submitCode == -1){
                    resultTV.setText("Not able to submit at this time");
                }
                else if (submitCode == 1){
                    if(CommodityMockModelTakeTwo.shared.addCommodity(barcode,name,vendor,dept,isleNum,price)){
                        // success
                    }
                    else{
                        // fail
                    }

                }
                else if(submitCode == 2){
                    if(CommodityMockModelTakeTwo.shared.modifyCommodityFromBarcode(barcode,name,vendor,dept,isleNum,price)){
                        // success
                    }
                    else{
                        // fail
                    }
                }
                else if(submitCode == 3){
                    if(CommodityMockModelTakeTwo.shared.removeItem(barcode)){
                        // success
                    }
                    else{
                        // fail
                    }

                }
                crateBTN.setVisibility(View.VISIBLE);
                modifyBTN.setVisibility(View.VISIBLE);
                deleteBTN.setVisibility(View.VISIBLE);
            }

        });
        pCancleBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAndClear();
                crateBTN.setVisibility(View.VISIBLE);
                modifyBTN.setVisibility(View.VISIBLE);
                deleteBTN.setVisibility(View.VISIBLE);
            }
        });
        crateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //So you can't do other things
                modifyBTN.setVisibility(View.INVISIBLE);
                deleteBTN.setVisibility(View.INVISIBLE);
                //Need to make sure the id does not exist
                hideAndClear();
                barcode = barCodeET.getText().toString();
                if(AdminInputValidationHandler.isExistingValue(true, barcode)){
                    String no = "Product with that Barcode name already exists";
                    resultTV.setText("" + no);
                }
                else{
                    //Now we are going to show all of the fields
                    submitCode = 1;
                    showFields();
                    Toast.makeText(getApplicationContext(),"Enter commodity details", Toast.LENGTH_LONG).show();

                }
            }
        });
        modifyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crateBTN.setVisibility(View.INVISIBLE);
                deleteBTN.setVisibility(View.INVISIBLE);
                //Need to make sure the id does not exist
                hideAndClear();
                barcode= barCodeET.getText().toString();
                if(!AdminInputValidationHandler.isExistingValue(true, barcode)){
                    String no = "Barcode  does not exist";
                    resultTV.setText("" + no);
                }

                else{
                    submitCode = 2;
                    //Now we are going to show all of the fields
                    showFields();

                    Commodity subject = CommodityMockModelTakeTwo.shared.getCommodityFromBarcode(barcode);

                    nameET.setText("" + subject.name);
                    vendorET.setText("" + subject.vendorName);
                    priceET.setText("" + subject.price);
                    deptET.setText("" + subject.department);
                    isleET.setText("" + subject.location);


                    Toast.makeText(getApplicationContext(),"Modify Commodity details", Toast.LENGTH_LONG).show();

                }
            }
        });
        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crateBTN.setVisibility(View.INVISIBLE);
                modifyBTN.setVisibility(View.INVISIBLE);
                //Need to make sure the id does not exist
                hideAndClear();
                barcode= barCodeET.getText().toString();
                if(!AdminInputValidationHandler.isExistingValue(true, barcode)){
                    String no = "Barcode  does not exist";
                    resultTV.setText("" + no);
                }

                else{
                    submitCode = 3;
                    //Now we are going to show all of the fields
                    showFields();

                    Commodity subject = CommodityMockModelTakeTwo.shared.getCommodityFromBarcode(barcode);
                    nameET.setClickable(false);
                    vendorET.setClickable(false);
                    priceET.setClickable(false);
                    deptET.setClickable(false);
                    isleET.setClickable(false);
                    nameET.setText("" + subject.name);
                    vendorET.setText("" + subject.vendorName);
                    priceET.setText("" + subject.price);
                    deptET.setText("" + subject.department);
                    isleET.setText("" + subject.location);
                    Toast.makeText(getApplicationContext(),"Confrim Deletion", Toast.LENGTH_LONG).show();

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


   private void hideAndClear(){
        nameTV.setVisibility(View.INVISIBLE);
        nameET.setVisibility(View.INVISIBLE);
        nameET.setText("");
        vendorTV.setVisibility(View.INVISIBLE);
        vendorET.setVisibility(View.INVISIBLE);
        vendorET.setText("");
       deptTV.setVisibility(View.INVISIBLE);
       deptET.setVisibility(View.INVISIBLE);
       deptET.setText("");
       isleTV.setVisibility(View.INVISIBLE);
       isleET.setVisibility(View.INVISIBLE);
       isleET.setText("");
       priceTV.setVisibility(View.INVISIBLE);
       priceET.setVisibility(View.INVISIBLE);
       priceET.setText("");
       nameET.setClickable(true);
       vendorET.setClickable(true);
       priceET.setClickable(true);
       deptET.setClickable(true);
       isleET.setClickable(true);


    }
    private  void showFields(){
        nameTV.setVisibility(View.VISIBLE);
        nameET.setVisibility(View.VISIBLE);
        vendorTV.setVisibility(View.VISIBLE);
        vendorET.setVisibility(View.VISIBLE);
        deptTV.setVisibility(View.VISIBLE);
        deptET.setVisibility(View.VISIBLE);
        isleTV.setVisibility(View.VISIBLE);
        isleET.setVisibility(View.VISIBLE);
        priceTV.setVisibility(View.VISIBLE);
        priceET.setVisibility(View.VISIBLE);
    }


    private boolean isEmpty(String s){
        return (s == null || s.equals("") || s.equals(" "));
    }



}