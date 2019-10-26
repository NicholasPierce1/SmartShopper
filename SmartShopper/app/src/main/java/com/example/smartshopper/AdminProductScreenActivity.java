package com.example.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
//@Author Matthew Berry

public class AdminProductScreenActivity extends AppCompatActivity {
    String empid, barcode;
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
        populateFakeData();
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
                    Toast.makeText(getApplicationContext(),"Enter new admin credentials", Toast.LENGTH_LONG).show();

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

                    Commodity subject = CommodityMockModel.commodityFinder(barcode);

                    nameET.setText("" + subject.name);
                    vendorET.setText("" + subject.vendorName);
                    priceET.setText("" + subject.price);
                    deptET.setText("" + subject.department);
                    isleET.setText("" + subject.location);


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

    private void populateFakeData(){
        if(CommodityMockModel.firstTime) {
            CommodityMockModel.firstTime = false;
            CommodityMockModel.commodityList.add(CommodityMockModel.c1);
            CommodityMockModel.barCodes.add(CommodityMockModel.c1.barcode);
            CommodityMockModel.names.add(CommodityMockModel.c1.name);
            CommodityMockModel.vendors.add(CommodityMockModel.c1.vendorName);
            CommodityMockModel.commodityList.add(CommodityMockModel.c2);
            CommodityMockModel.barCodes.add(CommodityMockModel.c2.barcode);
            CommodityMockModel.names.add(CommodityMockModel.c2.name);
            CommodityMockModel.vendors.add(CommodityMockModel.c2.vendorName);
        }
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

    }
    private  void showFields(){
        nameTV.setVisibility(View.VISIBLE);
        nameET.setVisibility(View.VISIBLE);
        deptTV.setVisibility(View.VISIBLE);
        deptET.setVisibility(View.VISIBLE);
        isleTV.setVisibility(View.VISIBLE);
        isleET.setVisibility(View.VISIBLE);
        priceTV.setVisibility(View.VISIBLE);
        priceET.setVisibility(View.VISIBLE);
    }
    private Location locationFinder(int isle){
        if(isle ==1){
            return Location.aisleOneLeft;

        }
        else if(isle == 2){
            return Location.aisleOneRight;
        }
        else if(isle == 3){
            return Location.aisleTwoLeft;
        }
        else if(isle == 4){
            return Location.aisleTwoRight;
        }
       return Location.aisleTwoRight; //deafault so it doesn't get mad. Needs to change obvisouly
    }


}