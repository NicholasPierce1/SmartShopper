package com.example.smartshopper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Model implements BrokerCallbackDelegate {
    private List<Department> departmentList;
    private List<Store>  stores;

    public  Model(){}
    public void findAllStores(){
       //Adapter.findAllStores(this );
    }
    public void populateDeapartmentListForStore(Store store){
        //Adapter.retrieveAllDepartmentsForStore(store, this);
    }

    @Override
    public void validateIfBarcodeExistHandler(boolean searchWasSuccess, @NonNull BarcodeExistResult barcodeExistResult) {

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
        stores = storeList;
        //Makes a callback to Jared's code
    }

    @Override
    public void initializeDepartments(boolean initSuccess, @Nullable List<Department> departmentList) {
        if(initSuccess){
            this.departmentList = departmentList;
            //Does a callback to Jared's code
        }

    }
}
