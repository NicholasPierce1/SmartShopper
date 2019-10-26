package com.example.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
//@Author Matthew Berry

public class AdminProductScreenActivity extends AppCompatActivity {
    String empid;
    TextView nameTV, vendorTV, deptTV, isleTV, priceTV, resultTV;
    EditText barCodeET, nameET, vendorET, deptET, isleET, priceET;
    Button crateBTN, modifyBTN, deleteBTN, pSubmitBTN, pCancleBTN, backBTN;


    @Override
    public void onCreate(Bundle savedInstanceState){
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

        populateFakeData();


    }

    public void goBackAction(View v){
        Intent data = new Intent();
        data.putExtra("EMPID", empid);
        setResult(0, data);
        finish();
    }

    private void populateFakeData(){
        CommodityMockModel.commodityList.add(CommodityMockModel.c1);
        CommodityMockModel.barCodes.add(CommodityMockModel.c1.barcode);
        CommodityMockModel.names.add(CommodityMockModel.c1.name);
        CommodityMockModel.vendors.add(CommodityMockModel.c1.vendorName);
        CommodityMockModel.commodityList.add(CommodityMockModel.c2);
        CommodityMockModel.barCodes.add(CommodityMockModel.c2.barcode);
        CommodityMockModel.names.add(CommodityMockModel.c2.name);
        CommodityMockModel.vendors.add(CommodityMockModel.c2.vendorName);

    }

   private void hideAllAndClear(){
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


}