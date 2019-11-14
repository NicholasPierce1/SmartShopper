package com.example.smartshopper;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Model implements BrokerCallbackDelegate {
    //This model is Beetlejuice approved
    private List<Department> departmentList;
    private CallBackInterface mm;
    private AdminProductCBMethods adminProductScreenActivity;
    private Store store;
    int oppCode;
    String barcode;
    private static Model shared = new Model();

    public static Model getShared(){
        return shared;
    }
    private Adapter adapter = Adapter.getShared();
    public void findAllStores(@NonNull final Context context) throws ExceptionInInitializerError{
       adapter.findAllStores(context, this);
    }
    public void populateDeapartmentListForStore(Store store, WelcomeScreenModelMethods mm){
        this.mm = mm;
        this.store = store;
        //Adapter.retrieveAllDepartmentsForStore(store, this);
    }
    public void validateBarcode(String barcode, int oppCode, AdminProductCBMethods cbm){
        adminProductScreenActivity = cbm;
        this.oppCode = oppCode;
        this.barcode = barcode;
        adapter.validateIfBarcodeExist(store, departmentList, barcode, this);
    }
    public void getNameFromBarcode(String barcode, AdminProductCBMethods cbm){
        adminProductScreenActivity = cbm;
    }

    @Override
    public void validateIfBarcodeExistHandler(boolean searchWasSuccess, @NonNull BarcodeExistResult barcodeExistResult) {
        if(searchWasSuccess){
            switch (getCaseNumber(barcodeExistResult)){
                case 0: logCaseProblem(barcodeExistResult);return;
                case 1: adminProductScreenActivity.buttonPressedCB(oppCode, barcode, 1, barcodeExistResult.commodity); return;
                case 2: adminProductScreenActivity.buttonPressedCB(oppCode, barcode, 2, barcodeExistResult.commodity );return;
                case 3: adminProductScreenActivity.buttonPressedCB(oppCode, barcode, 3, barcodeExistResult.commodity);return;
                case 4: adminProductScreenActivity.buttonPressedCB(oppCode, barcode, 4, barcodeExistResult.commodity);return;
            }

        }

    }

    @Override
    public void validateNameForVendorIsUniqueHandler(boolean searchWasSuccess, boolean nameIsUnique) {

    }

    @Override
    public void createItemForStoreInDepartment(boolean isSuccessful) {

    }

    @Override
    public void updateItemHandler(boolean isSuccessful) {

    }

    @Override
    public void deleteItemHandler(boolean isSuccessful) {

    }

    @Override
    public void findItemsBySearchHandler(boolean searchWasSuccessful, @NonNull List<Commodity> commodityList) {

    }

    @Override
    public void isAdminUsernameUniqueHandler(boolean adminSearchWasSuccess, boolean adminFound) {

    }

    @Override
    public void loginAdminByUsernameAndPassword(boolean adminLoginWasSuccess, boolean adminFoundAndIsInStore, @Nullable Admin admin) {

    }

    @Override
    public void findAdminByEmpId(boolean adminSearchWasSuccess, boolean adminFoundAndIsInStore, boolean isForUpdate, boolean didAdminRetainPrivilegesToAcquire, @Nullable Admin admin) {

    }

    @Override
    public void deleteAdminHandler(boolean wasAdminRemoved, boolean didAdminRetainPrivilegesToRemove) {

    }

    @Override
    public void updateAdminHandler(boolean wasAdminUpdated, boolean didAdminRetainPrivilegesToUpdate) {

    }

    @Override
    public void getStoresHandler(boolean searchSuccess, @Nullable List<Store> storeList) {
        ((WelcomeScreenModelMethods)mm).storeCB(searchSuccess,storeList);

    }

    @Override
    public void initializeDepartments(boolean initSuccess, @Nullable List<Department> departmentList) {
        if(initSuccess){
            this.departmentList = departmentList;
            //Does a callback to Jared's code
        }


    }

    @Override
    public void addAdminHandler(boolean wasAdminAdded) {

    }

    private int getCaseNumber(BarcodeExistResult bxr){
        //returns case number per design document
        if(bxr.newBarcode && !bxr.newBarcodeToStore){
            return 1;
        }
        else if(bxr.newBarcodeToStore && bxr.newBarcode){
            return 2;
        }
        else if(!bxr.newBarcode && !bxr.newBarcodeToStore){
            return  3;
        }
        else if(bxr.newBarcodeToStore && bxr.newBarcode){
            return 4;
        }
        else return 0;
    }
    private void logCaseProblem(BarcodeExistResult bxe){
        Log.d("Product Case Error", "Error in product case bools. \nDatabse flag is: "
                + bxe.newBarcode + " \n Store barcode is:  " + bxe.newBarcodeToStore);
    }
    //This does the magic to get a department
    public Department getDepartmentFromDepartmentType(DepartmentType dt){
        for(Department d: departmentList){
            if(d.type.toString().equals(dt.toString())){
                return d;
            }
        }
        return null;
    }

}
