package com.example.smartshopper;

import android.content.Context;
import android.content.res.Resources;
import android.provider.ContactsContract;
import android.util.Pair;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;


// retains external methods with internal class w/ helper methods for DA coalescing
// communicates with repo by passing custom runnables
public final class Adapter implements BackFourAppRepo.RepoCallbackHandler{

    // holds repo reference
    private BackFourAppRepo backFourAppRepo;

    // denotes shared delegate for repo operations
    private static Adapter shared = new Adapter();

    // returns shared delegate for broker implementation
    public  static Adapter getShared(){
        return shared;
    }

    private Adapter(){}

    // searches for, and conditionally combines department, deptStock, and item to create app's full item by barcode
    public void validateIfBarcodeExist(@NonNull final Store store, @NonNull final List<Department> departmentList, @NonNull final String barcode, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){

        // creates local ref to repo task
        final BackFourAppRepo.ExecuteRepoCallTask executeRepoCallTask = new BackFourAppRepo.ExecuteRepoCallTask() {
            @Override
            public RepoCallbackResult executeRepo() {

                // enumerates local state promised to delegate
                HashMap<String, Boolean> operationsResult = null;
                Commodity commodity = null;

                // try-catch-finally for all parse operation/s
                try{
                    // acquires Parse query targeting item
                    final ParseQuery<ParseObject> itemSearchQuery = ParseQuery.getQuery(DataAccess.DA_ClassNameRelationMapping.Commodity.getRelationName());

                    // acquires all items where barcode is equal
                    itemSearchQuery.whereEqualTo(Commodity.barcodeNameKey, barcode);
                    final List<ParseObject> itemListAsParse = itemSearchQuery.find();

                    // asserts that list size is 1 (0 is thrown as exception)
                    if (itemListAsParse.size() != 1)
                        throw new RuntimeException("error state in data integrity-- multiple items exist with such barcode. Count: ".concat(String.valueOf(itemListAsParse.size())));

                    // creates commodity
                    commodity = Commodity.Builder.toDataAccessFromParse(itemListAsParse.get(0));

                    // creates parse query targeting DeptStock
                    final ParseQuery<ParseObject> deptStockQuery = ParseQuery.getQuery(DataAccess.DA_ClassNameRelationMapping.DepartmentStock.getRelationName());

                    // sets where clause on predicate that store id match AND commodity id match
                    deptStockQuery.whereEqualTo(DepartmentStock.storeObjectIdKey, store.getObjectId());
                    deptStockQuery.whereEqualTo(DepartmentStock.itemObjectIdKey, commodity.getObjectId());

                    // acquires all dept stock that match constraints
                    final List<ParseObject> deptStockListAsParse = deptStockQuery.find();

                    // asserts size is 1 (0 throws exception)
                    if(deptStockListAsParse.size() != 1)
                        throw new RuntimeException("error state in data integrity-- multiple Department Stock tailored to store and commodity. Count: ".concat(String.valueOf(deptStockListAsParse.size())));

                    // renders dept stock builder (known size of 1)
                    final DepartmentStock departmentStock = DepartmentStock.Builder.toDataAccessFromParse(deptStockListAsParse.get(0));

                    // invokes helper to acquire dept where StoredDept's dept id == dept's
                    final Department department = Adapter.ORM_Helper.getDepartmentFromDeptStockParseId(departmentStock, departmentList);

                    // updates item with store and dept stock
                    commodity.updateCommodity(department, departmentStock);

                    // sets success result codes
                    operationsResult = RepoCallbackResult.setOperationResultBooleans(true, true, true);

                }
                catch(ParseException ex){

                    // differentiates cases from ( a. parse error in connection, b. commodity exist to database only, c. commodity does not exist at all )
                    // NOTE : parse exception code will equal "object not found"
                    if(ex.getCode() == ParseException.OBJECT_NOT_FOUND) { // all cases but 'a'

                        // checks between case 'b' and 'c' on predicate if commodity is null
                        if (commodity == null) {
                            // commodity not found in item relation from barcode -- case 'c'
                            operationsResult = RepoCallbackResult.setOperationResultBooleans(true);
                        } else {
                            // commodity existed to item relation from barcode, but not from store -- case 'b'
                            operationsResult = RepoCallbackResult.setOperationResultBooleans(true, true);
                        }
                    }
                    else{
                        // some other error-- case 'a'
                        operationsResult = RepoCallbackResult.setOperationResultBooleans(false);
                        }
                }
                finally{
                    assert(operationsResult != null);
                    assert(commodity != null);

                    // renders, and returns, repo callback
                    return new RepoCallbackResult(operationsResult, AdapterMethodType.validateIfBarcodeExist, brokerCallbackDelegate, commodity, null);
                }
            }
        };

        // enjoins repo to effectuate task
        this.backFourAppRepo.instigateAsyncRepoTask(executeRepoCallTask, this);
    }

    // validates if the name for the pending item is unique to vendor
    public void validateItemNameToVendorIsUnique(@NonNull final String itemName, @NonNull final String vendorName, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate) {

        // creates local reference to repo task
        final BackFourAppRepo.ExecuteRepoCallTask executeRepoCallTask = new BackFourAppRepo.ExecuteRepoCallTask() {
            @Override
            public RepoCallbackResult executeRepo() {

                // enumerates state promised to callback
                HashMap<String, Boolean> operationResults = null;

                // try-catch-finally block for finding item with same vendor name and barcode
                try {

                    // creates parse query targeting commodity
                    final ParseQuery<ParseObject> vendorNameDuplicateQuery = ParseQuery.getQuery(DataAccess.DA_ClassNameRelationMapping.Commodity.getRelationName());

                    // sets predicate where a prior commodity has the same vendor name and item name
                    vendorNameDuplicateQuery.whereEqualTo(Commodity.vendorNameKey, vendorName);
                    vendorNameDuplicateQuery.whereEqualTo(Commodity.nameKey, itemName);

                    // searches on predicates
                    final List<ParseObject> duplicates = vendorNameDuplicateQuery.find();

                    // sets error results
                    operationResults = RepoCallbackResult.setOperationResultBooleans(true, false);

                }
                catch (ParseException ex) {

                    // differentiates between cases ( a. no duplicates found, b. internal error ) on code value
                    if(ex.getCode() == ParseException.OBJECT_NOT_FOUND){
                        // sets success code
                        operationResults = RepoCallbackResult.setOperationResultBooleans(true, true);
                    }
                    else // error incurred
                        operationResults = RepoCallbackResult.setOperationResultBooleans(false);
                   }
                finally {

                    assert(operationResults != null);

                    // returns repo callback result
                    return new RepoCallbackResult(operationResults, AdapterMethodType.validateNameForVendorIsUnique, brokerCallbackDelegate, null, null);

                }

            }
        };

        // enjoins repo to execute task
        this.backFourAppRepo.instigateAsyncRepoTask(executeRepoCallTask, this);
    }

    // saves the pending item via composite inputs w/ the denoted store and dept
    public void createAndSaveItemForStoreInDept(@NonNull final Department department, @NonNull final String barcode, @NonNull final String name, @NonNull final String vendorName, @NonNull final String searchPhrase, final double price, @NonNull final Location location, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){

        // creates local ref to repo task
        BackFourAppRepo.ExecuteRepoCallTask executeRepoCallTask = new BackFourAppRepo.ExecuteRepoCallTask() {
            @Override
            public RepoCallbackResult executeRepo() {

                // enumerates state promised to callback
                HashMap<String, Boolean> operationsResults = null;

                // creates composite commodity
                Commodity commodity = Commodity.Builder.build(barcode,name, vendorName, searchPhrase);

                // try-catch-finally to save item, create dept stock, and save dept stock
                try{

                    // converts commodity to parse object
                    final ParseObject commodityToSave = commodity.toParseObject();

                    // saves commodity
                    commodityToSave.save();

                    // asserts that commodity's object id is not null
                    if(commodityToSave.getObjectId() == null)
                        throw new RuntimeException("saved Commodity does not retain an object id after save");

                    // updates commodity ref
                    commodity = Commodity.Builder.toDataAccessFromParse(commodityToSave);

                    // creates dept stock
                    final DepartmentStock departmentStock = DepartmentStock.Builder.build(department, commodity, price, location);

                    // converts and save dept stock
                    departmentStock.toParseObject().save();

                    // sets success codes
                    operationsResults = RepoCallbackResult.setOperationResultBooleans(true);

                }
                catch(ParseException ex){
                    // sets error codes
                    operationsResults = RepoCallbackResult.setOperationResultBooleans(false);
                }
                finally {
                    assert(operationsResults != null);

                    // returns repo callback results
                    return new RepoCallbackResult(operationsResults, AdapterMethodType.createItem, brokerCallbackDelegate, null, null);
                }
            }
        };

        // enjoins repo to effectuate task
        this.backFourAppRepo.instigateAsyncRepoTask(executeRepoCallTask, this);
    }

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
    public void findAllStores(@NonNull final Context applicationContext, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate)throws ExceptionInInitializerError{

        // sets local repo reference
        this.backFourAppRepo = BackFourAppRepo.getShared();

        // IMPORTANT: init parse data base connection
        this.backFourAppRepo.setParseInitialize(applicationContext);

        // creates local Repo runnable reference
        final BackFourAppRepo.ExecuteRepoCallTask executeRepoCallTask = new BackFourAppRepo.ExecuteRepoCallTask() {
            @Override
            public RepoCallbackResult executeRepo() {

                // creates parse query to find all stores
                final ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(DataAccess.DA_ClassNameRelationMapping.Store.getRelationName());

                // local refs to RepoCallbackResult params
                HashMap<String, Boolean> operationResults = null;
                List<Store> storeList = null;

                // try-catch to retrieve all stores
                try{
                    // retrieves and uses ORM to convert to list of stores
                    List<ParseObject> parseObjectList = parseQuery.find();
                    storeList = new ArrayList<Store>();

                    // walks through all parse objects holding store info and invokes orm for conversion
                    for(ParseObject parseObject: parseObjectList)
                        storeList.add(Store.Builder.build(parseObject));

                    // sets bool results
                    operationResults = RepoCallbackResult.setOperationResultBooleans(true);

                }
                catch(ParseException ex){

                    // sets error state results
                    operationResults = RepoCallbackResult.setOperationResultBooleans(false);

                }
                finally {
                    assert(operationResults != null);
                    // returns call back result
                    return new RepoCallbackResult(operationResults, AdapterMethodType.getAllStores, brokerCallbackDelegate, null, storeList);
                }
            }
        };

        // invokes repo to effectuate repo task
        this.backFourAppRepo.instigateAsyncRepoTask(executeRepoCallTask, this);
    }

    // retrieves, and coalesces, all departments for that store
    public void retrieveAllDepartmentsForStore(@NonNull final Store store, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){

        // creates local ref to repo task
        final BackFourAppRepo.ExecuteRepoCallTask executeRepoCallTask = new BackFourAppRepo.ExecuteRepoCallTask() {

            @Override
            public RepoCallbackResult executeRepo() {

                // local ref to composite return types
                List<Department> departmentList = null;
                HashMap<String, Boolean> operationResults = null;

                try {

                    // creates parse query targeting store dept
                    final ParseQuery<ParseObject> deptStockParseQuery = ParseQuery.getQuery(DataAccess.DA_ClassNameRelationMapping.StoreDept.getRelationName());

                    // sets constraint where store object's id matches store dept's store id
                    deptStockParseQuery.whereEqualTo(StoredDept.storeObjectIdKey, store.getObjectId());

                    // retrieves, and converts all dept stock
                    final List<ParseObject> parseObjectList = deptStockParseQuery.find();
                    final List<StoredDept> storedDeptList = Adapter.ORM_Helper.convertParseObjectsToStoredDept(parseObjectList);

                    // creates parse query targeting Department
                    final ParseQuery<ParseObject> deptParseQuery = ParseQuery.getQuery(DataAccess.DA_ClassNameRelationMapping.Department.getRelationName());

                    // retrieves all depts
                    final List<ParseObject> departmentParseObjectList = deptParseQuery.find();

                    // invokes helper to convert, department as parse object, a list of store dept, and store to create dept list
                    departmentList = Adapter.ORM_Helper.findAndCreateDepartmentFromRelationalState(departmentParseObjectList, storedDeptList, store);

                    // sets success bool results
                    operationResults = RepoCallbackResult.setOperationResultBooleans(true);

                }
                catch(ParseException ex){
                    // sets error bool results
                    operationResults = RepoCallbackResult.setOperationResultBooleans(false);
                }
                finally {
                    assert(operationResults != null);

                    // returns compiled results to callback
                    return new RepoCallbackResult(operationResults, AdapterMethodType.initializeDepartments, brokerCallbackDelegate, null, departmentList);
                }
            }
        };

        // enjoins repo to execute custom runnable
        this.backFourAppRepo.instigateAsyncRepoTask(executeRepoCallTask, this);
    }

    // private helper method to retrieve an item from a barcode (commodity is composite, boolean is true if item is found OR item doesn't exist to database)
    @NonNull
    private Pair<Commodity, Boolean> findCompositeCommodityFromBarcode(@NonNull final String barcode){
        try {
            // acquires Parse query targeting item
            final ParseQuery<ParseObject> itemSearchQuery = ParseQuery.getQuery(DataAccess.DA_ClassNameRelationMapping.Commodity.getRelationName());

            // acquires all items where barcode is equal
            itemSearchQuery.whereEqualTo(Commodity.barcodeNameKey, barcode);
            final List<ParseObject> itemListAsParse = itemSearchQuery.find();

            // asserts that list size is 1 (0 is thrown as exception)
            if (itemListAsParse.size() != 1)
                throw new RuntimeException("error state in data integrity-- multiple items exist with such barcode. Count: ".concat(String.valueOf(itemListAsParse.size())));

            // acquires composite item from list -- known size of 1
            return new Pair<Commodity, Boolean>(Commodity.Builder.toDataAccessFromParse(itemListAsParse.get(0)), true);
        }
        catch(ParseException ex){ // no item exist OR internal error

            // holds local ref to bool of ex's code != Object not found
            final boolean itemNotFound = ex.getCode() == ParseException.OBJECT_NOT_FOUND;

            return new Pair<Commodity,  Boolean>(null, itemNotFound);
        }
    }

    // public method implementation to receive repo callback, downcast datatype params to appropriate broker callback
    @Override
    @MainThread
    public void repoCallback(@NonNull final HashMap<String, Boolean> operationResultHolder, @Nullable final DataAccess dataAccess, @Nullable final List<? extends DataAccess> dataAccessList, @NonNull final AdapterMethodType adapterMethodType, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){

        // acquires local refs to boolean values (safe unboxing operations -- will always be set)
        final boolean operationWasSuccess = operationResultHolder.get(RepoCallbackResult.operationSuccessKey);
        final boolean adapterOperationWasSuccess = operationResultHolder.get(RepoCallbackResult.adapterOperationSuccessKey);
        final boolean contextOperationWasSuccess = operationResultHolder.get(RepoCallbackResult.contextOperationSuccessKey);
        final boolean contextAxillaryOperationWasSuccess = operationResultHolder.get(RepoCallbackResult.contextAuxiliaryOperationSuccessKey);

        // creates switch case on AdapterMethodType (all cast operations are safe)
        switch(adapterMethodType){
            case validateIfBarcodeExist:
                brokerCallbackDelegate.validateIfBarcodeExistHandler(operationWasSuccess, new BarcodeExistResult(adapterOperationWasSuccess, contextOperationWasSuccess, (Commodity)dataAccess));
                break;
            case validateNameForVendorIsUnique:
                brokerCallbackDelegate.validateNameForVendorIsUniqueHandler(operationWasSuccess, adapterOperationWasSuccess);
                break;
            case createItem:
                brokerCallbackDelegate.createItemForStoreInDepartment(operationWasSuccess);
            case updateItem:
                brokerCallbackDelegate.updateItemHandler(operationWasSuccess);
                break;
            case deleteItem:
                brokerCallbackDelegate.deleteItemHandler(operationWasSuccess);
                break;
            case findItemBySearch: // (SAFE cast, never not null and is commodity)
                assert(dataAccessList != null);
                brokerCallbackDelegate.findItemsBySearchHandler(operationWasSuccess,(List<Commodity>)dataAccessList);
            case findAdminByLogin:
                brokerCallbackDelegate.loginAdminByUsernameAndPassword(operationWasSuccess, adapterOperationWasSuccess, (Admin)dataAccess);
                break;
            case findAdminByEmpId:
                brokerCallbackDelegate.findAdminByEmpId(operationWasSuccess, adapterOperationWasSuccess, contextOperationWasSuccess, contextAxillaryOperationWasSuccess, (Admin)dataAccess);
                break;
            case validateIfAdminUsernameIsUnique:
                brokerCallbackDelegate.isAdminUsernameUniqueHandler(operationWasSuccess, adapterOperationWasSuccess);
                break;
            case addAdmin:
                brokerCallbackDelegate.addAdminHandler(operationWasSuccess);
                break;
            case deleteAdmin:
                brokerCallbackDelegate.deleteAdminHandler(operationWasSuccess, adapterOperationWasSuccess);
                break;
            case updateAdmin:
                brokerCallbackDelegate.updateAdminHandler(operationWasSuccess, adapterOperationWasSuccess);
                break;
            case initializeDepartments: // (SAFE cast, never not null and is department)
                assert(dataAccessList != null);
                brokerCallbackDelegate.initializeDepartments(operationWasSuccess, (List<Department>)dataAccessList);
                break;
            default:  // ** getAllStores ** (SAFE cast, never not null and is store)
                assert(dataAccessList != null);
                brokerCallbackDelegate.getStoresHandler(operationWasSuccess, (List<Store>)dataAccessList);
        }
    }

    // local inner class to render helper methods for ORM facilitation
    private static class ORM_Helper{

        // retrieves a list of parse objects and converts to StoredDept
        @NonNull
        static List<StoredDept> convertParseObjectsToStoredDept(@NonNull final List<ParseObject> parseObjectList){

            // creates local list ref
            final List<StoredDept> storedDeptList = new ArrayList<StoredDept>();

            // walks through all parse objects, using ORM to convert them into StoredDept
            for(ParseObject parseObject: parseObjectList)
                storedDeptList.add(StoredDept.Builder.build(parseObject));

            return storedDeptList;
        }

        // helper to convert, department as parse object, a list of store dept, and store to create dept list
        @NonNull
        static List<Department> findAndCreateDepartmentFromRelationalState(@NonNull final List<ParseObject> departmentsAsParse, @NonNull final List<StoredDept> storedDeptList, @NonNull final Store store){

            // local ref to dept list
            final List<Department> departmentList = new ArrayList<Department>();

            // walks through all stored departments and finds match where stored dept id == dept id
            for(StoredDept storedDept: storedDeptList){

                // finds matching dept
                final ParseObject parseObject = getDepartmentAsParseFromStoreDepartment(storedDept, departmentsAsParse);

                // creates dept, and appends to list
                departmentList.add(Department.Builder.build(parseObject, storedDept, store));
            }

            return departmentList;
        }

        // helper to retrieve department as parse where stored dept's id match
        @NonNull
        static ParseObject getDepartmentAsParseFromStoreDepartment(@NonNull final StoredDept storedDept, @NonNull final List<ParseObject> parseObjectList){

            // walks through all parse objects and returns the dept where id's match
            for(ParseObject parseObject: parseObjectList){
                if(parseObject.getObjectId().equals(storedDept.getObjectId()))
                    return parseObject;
            }

            // throws exception is no match uncovered
            throw new NoSuchElementException("no department pertains to stored dept");
        }

        // helper to acquire Department dept where StoredDept's dept id == Department's
        @NonNull
        static Department getDepartmentFromDeptStockParseId(@NonNull final DepartmentStock deptStock, @NonNull final List<Department> departmentList){

            // walks through all departments and finds department that has same id
            for(Department department: departmentList){
                if(department.getObjectId().equals(deptStock.getObjectId()))
                    return department;
            }

            // throws exception is no match uncovered
            throw new NoSuchElementException("no department pertains to dept stock");
        }

    }
}
