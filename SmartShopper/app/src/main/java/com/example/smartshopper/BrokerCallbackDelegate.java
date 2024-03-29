package com.example.smartshopper;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

// purpose: provides a callback delegate to handle all operations that transpire within repo
@MainThread
public interface BrokerCallbackDelegate {

    // handler to access results from barcode searching
    public abstract void validateIfBarcodeExistHandler(final boolean searchWasSuccess, @NonNull final BarcodeExistResult barcodeExistResult);

    // handler to access results from vendor validation (true- name is unique for vendor. false- otherwise)
    public abstract void validateNameForVendorIsUniqueHandler(final boolean searchWasSuccess, final boolean nameIsUnique);

    // handler to access action result of updating an item
    public abstract void createItemForStoreInDepartment(final boolean isSuccessful);

    // handler to access action result of updating an item
    public abstract void updateItemHandler(final boolean isSuccessful);

    // handler to access action result of deleting an item
    public abstract void deleteItemHandler(final boolean isSuccessful);

    // handler to access search results
    public abstract void findItemsBySearchHandler(final boolean searchWasSuccessful, @NonNull final List<Commodity> commodityList);

    // handler to access login results
    public abstract void isAdminUsernameUniqueHandler(final boolean adminSearchWasSuccess, final boolean adminIsUnique);

    // handler to login admin
    public abstract void loginAdminByUsernameAndPassword(final boolean adminLoginWasSuccess, final boolean adminFoundAndIsInStore, @Nullable final Admin admin);

    // handler to find an admin by an empId
    // second boolean indicates to broker whether this invocation was for a create action or an update
    public abstract void findAdminByEmpId(final boolean adminSearchWasSuccess, final boolean adminFoundAndIsInStore, final boolean didAdminRetainPrivilegesToAcquire, @Nullable final Admin admin);

    // handler to access the creation of an admin
    public abstract void addAdminHandler(final boolean wasAdminAdded);

    // handler to access deletion of admin
    public abstract void deleteAdminHandler(final boolean wasAdminRemoved, final boolean wasAdminFound, final boolean didAdminRetainPrivilegesToRemove);

    // handler to ensure if update of admin was successful
    public abstract void updateAdminHandler(final boolean wasAdminUpdated);

    // handler to give all stores
    public abstract void getStoresHandler(final boolean searchSuccess, @Nullable final List<Store> storeList);

    // handler to give all complete departments per store
    public abstract void initializeDepartmentsHandler(final boolean initSuccess, @Nullable final List<Department> departmentList);
}
