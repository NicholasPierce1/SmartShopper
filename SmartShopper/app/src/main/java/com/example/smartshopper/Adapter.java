package com.example.smartshopper;

import android.content.Context;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;


// retains external methods with internal class w/ helper methods for DA coalescing
// communicates with repo by passing custom runnables
public final class Adapter implements BackFourAppRepo.RepoCallbackHandler{

    // item's CRUD methods

    // denotes shared delegate for repo operations
    private static Adapter shared = new Adapter();

    // returns shared delegate for broker implementation
    public  static Adapter getShared(){
        return shared;
    }

    private Adapter(){}

    // searches for, and conditionally combines department, deptStock, and item to create app's full item by barcode
    public void validateIfBarcodeExist(@NonNull final Store store, @NonNull final List<Department> departmentList, @NonNull final String barcode, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){}

    // validates if the name for the pending item is unique to vendor
    public void validateItemNameToVendorIsUnique(@NonNull final String itemName, @NonNull final String vendorName, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){}

    // saves the pending item via composite inputs w/ the denoted store and dept
    public void createAndSaveItemForStoreInDept(@NonNull final Department department, @NonNull final String barcode, @NonNull final String name, @NonNull final String vendorName, final double price, @NonNull final Location location, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){}

    // updates the item via the newly refined item's state positing that non-categorical state (store and dept) hasn't been altered
    public void updateItem(@NonNull final Commodity commodity, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){}

    // deletes the item via its barcode
    public void deleteItemFromBarcode(@NonNull final Store store, @NonNull final String barcode, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){}

    // searches for an item by a search phrase on predicate the the item's formal name OR its categorical name contains the search
    public void searchForItemByPhrase(@NonNull final Store store, @NonNull final String searchPhrase, @NonNull final List<Department> departmentList, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){}

    // finds an admin by its username if exist
    public void isAdminUsernameUnique(@NonNull final Store store, @NonNull final String username, @NonNull BrokerCallbackDelegate brokerCallbackDelegate){}

    // login admin by uName and password
    public void loginAdminByUsernameAndPassword(@NonNull final Store store, @NonNull final String username, @NonNull final String password, @NonNull BrokerCallbackDelegate brokerCallbackDelegate){}

    // finds an admin by its empId and states if invocation is for login or update
    public void findAdminByEmpId(@NonNull final Store store, @NonNull final String empId, final boolean isLoggingIn, @NonNull final Admin adminThatRequestedSearch, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){}

    // creates and saves an admin
    public void saveAdminToStore(@NonNull final Store store, @NonNull final String empId, @NonNull final String name, @NonNull final String userName, @NonNull final String password, @NonNull final AdminLevel adminLevel, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){}

    // deletes an admin by its empId
    public void deleteAdmin(@NonNull final Store store, @NonNull final String empId, @NonNull final Admin adminThatRequestedDelete, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){}

    // updates an admin
    public void updateAdmin(@NonNull final Admin admin, @NonNull final Admin adminThatRequestedUpdate, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){}

    // initializing methods for model implementation

    // retrieves all stores
    public void findAllStores(@NonNull final Context applicationContext, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){}

    // retrieves, and coalesces, all departments for that store
    public void retrieveAllDepartmentsForStore(@NonNull final Store store, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){}

    // public method implementation to receive repo callback, downcast datatype params to appropriate broker callback
    @Override
    @MainThread
    public void repoCallback(@NonNull final HashMap<String, Boolean> operationResultHolder, @Nullable final DataAccess dataAccess, @Nullable final List<DataAccess> dataAccessList, @NonNull final AdapterMethodType adapterMethodType, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){

        // TODO: switch case to institute all callbacks to broker delegate
    }

    // local inner class to render helper methods for ORM facilitation
    private class ORM_Helper{


    }
}
