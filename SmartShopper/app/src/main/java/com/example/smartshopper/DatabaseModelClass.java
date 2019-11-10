package com.example.smartshopper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DatabaseModelClass implements BrokerCallbackDelegate {
    @Override
    public void validateIfBarcodeExistHandler(@NonNull BarcodeExistResult barcodeExistResult) {
        
    }

    @Override
    public void validateNameForVendorIsUniqueHandler(boolean nameIsUnique) {

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
    public void findItemsBySearchHandler(@NonNull List<Commodity> commodityList) {

    }

    @Override
    public void findAdminByLoginHandler(boolean adminLoginSuccess, @Nullable Admin admin) {

    }

    @Override
    public void deleteAdminHandler(boolean wasAdminRemoved) {

    }

    @Override
    public void updateAdminHandler(boolean wasAdminUpdated) {

    }

    @Override
    public void getStoresHandler(boolean searchSuccess, @Nullable List<Store> storeList) {

    }

    @Override
    public void initializeDepartments(boolean initSuccess, @Nullable List<Department> departmentList) {

    }
}
