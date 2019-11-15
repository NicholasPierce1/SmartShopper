package com.example.smartshopper;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;

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
                    final Department department = Adapter.ORM_Helper.getDepartmentFromDeptStock(departmentStock, departmentList);

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
    public void updateItem(@NonNull final Commodity commodity, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){

        // holds local ref to repo task
        final BackFourAppRepo.ExecuteRepoCallTask executeRepoCallTask = new BackFourAppRepo.ExecuteRepoCallTask() {
            @Override
            public RepoCallbackResult executeRepo() {

                // enumerates state promised to callback
                HashMap<String, Boolean> operationResult = null;

                // try-catch-finally block to re-save commodity
                try{

                    // converts commodity to parse object and saves
                    commodity.toParseObject().save();

                    // sets success code
                    operationResult = RepoCallbackResult.setOperationResultBooleans(true);

                }
                catch(ParseException ex){
                    // sets error code
                    operationResult = RepoCallbackResult.setOperationResultBooleans(false);
                }
                finally{

                    assert(operationResult != null);

                    // returns repo callback result
                    return new RepoCallbackResult(operationResult, AdapterMethodType.updateItem, brokerCallbackDelegate, null, null);
                }
            }
        };

        // enjoins repo to execute task
        this.backFourAppRepo.instigateAsyncRepoTask(executeRepoCallTask, this);
    }

    // deletes the item via its barcode
    public void deleteItemFromBarcode(@NonNull final Store store, @NonNull final String barcode, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){

        // creates local reference to repo task
        final BackFourAppRepo.ExecuteRepoCallTask executeRepoCallTask = new BackFourAppRepo.ExecuteRepoCallTask() {
            @Override
            public RepoCallbackResult executeRepo() {

                // enumerates promised state to callback
                HashMap<String, Boolean> operationResults = null;

                // try-catch-finally block to find item, find dept stock, delete dept stock, and delete item
                try{

                    // invokes helper method to acquire item
                    final Commodity commodityToDelete = findCompositeCommodityFromBarcode(barcode);

                    // creates parse query targeting DeptStock
                    final ParseQuery<ParseObject> findDeptStock = ParseQuery.getQuery(DataAccess.DA_ClassNameRelationMapping.DepartmentStock.getRelationName());

                    // sets predicate where commodity id and store id equal
                    findDeptStock.whereEqualTo(DepartmentStock.itemObjectIdKey, commodityToDelete.getObjectId());
                    findDeptStock.whereEqualTo(DepartmentStock.storeObjectIdKey, store.getObjectId());

                    // acquires list of department stocks where predicate is assuaged
                    List<ParseObject> deptStockListAsParse = findDeptStock.find();

                    // asserts list's size is 1 (0 throws exception)
                    if(deptStockListAsParse.size() != 1)
                        throw new RuntimeException("error state in data integrity-- multiple Department Stock tailored to store and commodity. Count: ".concat(String.valueOf(deptStockListAsParse.size())));

                    // acquires, converts, and deletes dept stock w/ known size of 1
                    deptStockListAsParse.get(0).delete();

                    // converts and deletes commodity
                    commodityToDelete.toParseObject().delete();

                    // sets success results code
                    operationResults = RepoCallbackResult.setOperationResultBooleans(true);

                }
                catch(ParseException ex){
                    // sets error results code
                    operationResults = RepoCallbackResult.setOperationResultBooleans(false);
                }
                finally{

                    assert(operationResults != null);

                    // returns repo callback results
                    return new RepoCallbackResult(operationResults, AdapterMethodType.deleteItem, brokerCallbackDelegate, null, null);
                }
            }
        };

        // enjoins repo to effectuate task
        this.backFourAppRepo.instigateAsyncRepoTask(executeRepoCallTask, this);
    }

    // searches for an item by a search phrase on predicate the the item's formal name OR its categorical name contains the search
    public void searchForItemByPhrase(@NonNull final Store store, @NonNull final String searchPhrase, @NonNull final List<Department> departmentList, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){

        // creates local repo task
        final BackFourAppRepo.ExecuteRepoCallTask executeRepoCallTask = new BackFourAppRepo.ExecuteRepoCallTask() {
            @Override
            public RepoCallbackResult executeRepo() {

                // enumerates promised state to callback
                HashMap<String, Boolean> operationResults = null;
                List<Commodity> commodityList = null;

                // try-catch-finally block to acquire all items where searchPhrase is contained in name or search phrase,
                // acquire of DeptStock where store id and commodity id match, convert dept stock,
                // and update item w/ dept stock and department
                try{

                    // array list to hold inner queries to be 'or' together
                    final ArrayList<ParseQuery<ParseObject>> orQueryList = new ArrayList<ParseQuery<ParseObject>>();

                    // creates two inner queries to be 'or' together

                    // query one on Commodity (search phrase contained in searchPhrase)
                    final ParseQuery<ParseObject> searchPhraseContains = ParseQuery.getQuery(DataAccess.DA_ClassNameRelationMapping.Commodity.getRelationName());
                    searchPhraseContains.whereContains(Commodity.searchPhraseKey, searchPhrase);

                    // query two on Commodity (search phrase contained in name)
                    final ParseQuery<ParseObject> nameContains = ParseQuery.getQuery(DataAccess.DA_ClassNameRelationMapping.Commodity.getRelationName());
                    nameContains.whereContains(Commodity.nameKey, searchPhrase);

                    // 'or' the queries
                    final ParseQuery<ParseObject> comprisedCommodityQuery = ParseQuery.or(orQueryList);

                    // invokes search to acquire list of commodities that assuage 'or' predicate
                    final List<ParseObject> commodityListAsParse = comprisedCommodityQuery.find();

                    // invokes helper to convert list to a commodity list
                    commodityList = Adapter.ORM_Helper.convertCommodityListAsParseToCommodityList(commodityListAsParse);

                    // invokes helper to convert commodityList to a list of object ids
                    final List<String> objectIdList = Adapter.ORM_Helper.convertDataAccessToObjectIdList(commodityList);

                    // creates parse query targeting dept stock
                    final ParseQuery<ParseObject> deptStockQuery = ParseQuery.getQuery(DataAccess.DA_ClassNameRelationMapping.DepartmentStock.getRelationName());

                    // sets predicate that dept stock's item id is contained in  object id list
                    deptStockQuery.whereContainedIn(DepartmentStock.itemObjectIdKey, objectIdList);

                    // acquires all dept stocks that assuage predicate
                    final List<ParseObject> deptStockListAsParse = deptStockQuery.find();

                    // invokes helper to convert dept stock as parse to a list of DeptStock
                    final List<DepartmentStock> departmentStockList = Adapter.ORM_Helper.convertParseObjectsToDeptStock(deptStockListAsParse);

                    // invokes helper to amalgamate department stock to department list
                    List<Pair<DepartmentStock, Department>> departmentStockToDepartmentList = Adapter.ORM_Helper.combineDepartmentStockWithDepartment(departmentStockList, departmentList);

                    // invokes helper to update item from deptmentStockToDepartmentList pairs where object id's match
                    Adapter.ORM_Helper.updateItemFromDeptStockToDepartmentPairList(commodityList, departmentStockToDepartmentList);

                    // sets success code
                    operationResults = RepoCallbackResult.setOperationResultBooleans(true);

                }
                catch(ParseException ex){

                    // sets error code
                    operationResults = RepoCallbackResult.setOperationResultBooleans(false);

                    // upon empty results the list shall be non-null, empty
                    if(ex.getCode() == ParseException.OBJECT_NOT_FOUND)
                        commodityList = new ArrayList<Commodity>();

                }
                finally{

                    assert(operationResults != null);
                    assert(commodityList != null);

                    // returns repo callback results
                    return new RepoCallbackResult(operationResults, AdapterMethodType.findItemBySearch, brokerCallbackDelegate, null, commodityList);

                }
            }
        };

        // enjoins repo to execute task
        this.backFourAppRepo.instigateAsyncRepoTask(executeRepoCallTask, this);
    }

    // finds an admin by its username if exist
    public void isAdminUsernameUnique(@NonNull final String username, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){

        // creates local ref to repo task
        final BackFourAppRepo.ExecuteRepoCallTask executeRepoCallTask = new BackFourAppRepo.ExecuteRepoCallTask() {
            @Override
            public RepoCallbackResult executeRepo() {

                // enumerates state promised to callback
                HashMap<String, Boolean> operationResults = null;

                // try-catch-finally block to search for an admin on predicate of username's match
                try{

                    // creates parse query targeting Admin
                    final ParseQuery<ParseObject> adminUsernameQuery = ParseQuery.getQuery(DataAccess.DA_ClassNameRelationMapping.Admin.getRelationName());

                    // sets predicate where username match
                    adminUsernameQuery.whereEqualTo(Admin.userNameKey, username);

                    // finds all admin that assuage predicate
                    final List<ParseObject> adminListAsParse = adminUsernameQuery.find();

                    // if here then list is (1-m), regardless of such case issues error results
                    operationResults = RepoCallbackResult.setOperationResultBooleans(true, false);

                }
                catch(ParseException ex){

                    // differentiates cases of parse exceptions (cases: a. no object found on such predicate, b. internal error incurred)
                    if(ex.getCode() == ParseException.OBJECT_NOT_FOUND){
                        // sets success codes-- no admin uncovered
                        operationResults = RepoCallbackResult.setOperationResultBooleans(true, true);
                    }
                    else{
                        // sets error code -- internal error
                        operationResults = RepoCallbackResult.setOperationResultBooleans(false);
                    }

                }
                finally{

                    assert(operationResults != null);

                    // returns repo callback result
                    return new RepoCallbackResult(operationResults, AdapterMethodType.validateIfAdminUsernameIsUnique, brokerCallbackDelegate, null, null);

                }
            }
        };

        // enjoins repo to instigate repo task
        this.backFourAppRepo.instigateAsyncRepoTask(executeRepoCallTask, this);
    }

    // login admin by uName and password
    public void loginAdminByUsernameAndPassword(@NonNull final Store store, @NonNull final String username, @NonNull final String password, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate) {

        // creates local ref to repo task
        final BackFourAppRepo.ExecuteRepoCallTask executeRepoCallTask = new BackFourAppRepo.ExecuteRepoCallTask() {
            @Override
            public RepoCallbackResult executeRepo() {

                // enumerates local state promised to the callback
                HashMap<String, Boolean> operationResults = null;
                Admin admin = null;

                // try-catch-finally block to find an admin in denoted store with login credentials
                try {

                    // creates parse query targeting admin
                    final ParseQuery<ParseObject> adminLoginQuery = ParseQuery.getQuery(DataAccess.DA_ClassNameRelationMapping.Admin.getRelationName());

                    // sets predicates on where store matches, username matches, and password matches
                    adminLoginQuery.whereEqualTo(Admin.storeKey, store.getObjectId());
                    adminLoginQuery.whereEqualTo(Admin.userNameKey, username);
                    adminLoginQuery.whereEqualTo(Admin.passwordKey, password);

                    // finds admin list where predicate matches
                    final List<ParseObject> adminListAsParse = adminLoginQuery.find();

                    // asserts that list's size is only one (0 throws parse exception)
                    if (adminListAsParse.size() != 1)
                        throw new RuntimeException("error state in data integrity-- multiple Admin tailored to store and login credentials. Count: ".concat(String.valueOf(adminListAsParse.size())));

                    // extracts known admin as parse, covert to admin, and sets to admin
                    admin = Admin.Builder.toDataAccessFromParse(adminListAsParse.get(0), store);

                    // sets success code
                    operationResults = RepoCallbackResult.setOperationResultBooleans(true, true);

                } catch (ParseException ex) {
                    // differentiates cases of parse exceptions (cases: a. no object found on such predicate, b. internal error incurred)
                    if(ex.getCode() == ParseException.OBJECT_NOT_FOUND){
                        // sets error code - no admin found
                        operationResults = RepoCallbackResult.setOperationResultBooleans(true, false);
                    }
                    else{
                        // sets error code -- internal error
                        operationResults = RepoCallbackResult.setOperationResultBooleans(false);
                    }

                }
                finally {

                    assert(operationResults != null);

                    // returns composite repo callback result
                   return new RepoCallbackResult(operationResults, AdapterMethodType.findAdminByLogin, brokerCallbackDelegate, admin, null);
                }
            }
        };

        // enjoins repo to instigate repo task
        this.backFourAppRepo.instigateAsyncRepoTask(executeRepoCallTask, this);
    }

    // finds an admin by its empId and states if invocation is for login or update
    public void findAdminByEmpId(@NonNull final Store store, @NonNull final String empId, final boolean isLoggingIn, @NonNull final Admin adminThatRequestedSearch, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){

        // creates local ref to repo task
        final BackFourAppRepo.ExecuteRepoCallTask executeRepoCallTask = new BackFourAppRepo.ExecuteRepoCallTask() {
            @Override
            public RepoCallbackResult executeRepo() {

                // enumerates state promised to callback
                HashMap<String, Boolean> operationResults = RepoCallbackResult.setOperationResultBooleans(false);
                Admin admin = null;

                // try-catch-finally block to find admin in store w/ empId,
                // access if admin that requested has higher admin level permissions than the converted admin, and return accordingly
                try{

                    // creates parse query targeting admin
                    final ParseQuery<ParseObject> findAdminByEmpIdQuery = ParseQuery.getQuery(DataAccess.DA_ClassNameRelationMapping.Admin.getRelationName());

                    // sets predicate where store id match and emp id match
                    findAdminByEmpIdQuery.whereEqualTo(Admin.storeKey, store.getObjectId());
                    findAdminByEmpIdQuery.whereEqualTo(Admin.empIdKey, empId);

                    // finds admins that match predicate
                    final List<ParseObject> adminListAsParse = findAdminByEmpIdQuery.find();

                    // asserts size is one (0 throws parse exception)
                    if (adminListAsParse.size() != 1)
                        throw new RuntimeException("error state in data integrity-- multiple Admin tailored to store and login credentials. Count: ".concat(String.valueOf(adminListAsParse.size())));

                    // extracts and converts known admin as parse to DA
                    admin = Admin.Builder.toDataAccessFromParse(adminListAsParse.get(0), store);

                    // asserts that admin that requested search has higher priviledges than admin found
                    if(admin.adminLevel.getIdType() > adminThatRequestedSearch.adminLevel.getIdType()){
                        // good case, set success codes
                        operationResults = RepoCallbackResult.setOperationResultBooleans(true, true, true);
                    }
                    else{
                        // admin level lock codes
                        operationResults = RepoCallbackResult.setOperationResultBooleans(true,true, false);
                    }
                }
                catch(ParseException ex){

                    // differentiates cases of parse exceptions (cases: a. no object found on such predicate, b. internal error incurred)
                    if(ex.getCode() == ParseException.OBJECT_NOT_FOUND){
                        // sets error code - no admin found
                        operationResults = RepoCallbackResult.setOperationResultBooleans(true, false);
                    }
                    else{
                        // sets error code -- internal error
                        operationResults = RepoCallbackResult.setOperationResultBooleans(false);
                    }
                }
                finally{

                    // returns repo callback result composite
                    return new RepoCallbackResult(operationResults, AdapterMethodType.findAdminByEmpId, brokerCallbackDelegate, admin,null);
                }
            }
        };

        // enjoins repo to execute task
        this.backFourAppRepo.instigateAsyncRepoTask(executeRepoCallTask, this);
    }

    // creates and saves an admin
    public void saveAdminToStore(@NonNull final Store store, @NonNull final String empId, @NonNull final String name, @NonNull final String userName, @NonNull final String password, @NonNull final AdminLevel adminLevel, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){

        // creates local ref to repo task
        final BackFourAppRepo.ExecuteRepoCallTask executeRepoCallTask = new BackFourAppRepo.ExecuteRepoCallTask() {
            @Override
            public RepoCallbackResult executeRepo() {

                // enumerates shared state promised to callback
                HashMap<String, Boolean> operationResults = RepoCallbackResult.setOperationResultBooleans(false);

                // try-catch-finally block to create full admin and save
                try {

                    // creates full admin
                    final Admin adminToSave = Admin.Builder.build(store, name, userName, password, adminLevel, empId);

                    // converts admin to parse and saves admin
                    adminToSave.toParseObject().save();

                    // sets success codes
                    operationResults = RepoCallbackResult.setOperationResultBooleans(true);

                } catch (ParseException ex) {
                    // sets bad codes
                    operationResults = RepoCallbackResult.setOperationResultBooleans(false);
                }
                finally {

                    // returns composites RepoCallBackResult
                    return new RepoCallbackResult(operationResults, AdapterMethodType.addAdmin, brokerCallbackDelegate, null, null);
                }

            }
        };

        // invokes repo to instigate task
        this.backFourAppRepo.instigateAsyncRepoTask(executeRepoCallTask, this);
    }

    // deletes an admin by its empId
    public void deleteAdmin(@NonNull final Store store, @NonNull final String empId, @NonNull final Admin adminThatRequestedDelete, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){

        // creates local ref to repo task
        final BackFourAppRepo.ExecuteRepoCallTask executeRepoCallTask = new BackFourAppRepo.ExecuteRepoCallTask() {
            @Override
            public RepoCallbackResult executeRepo() {

                // enumerates shared state promised to callback
                HashMap<String, Boolean> operationResults = RepoCallbackResult.setOperationResultBooleans(false);

                // enumerates local state needed outside of try's scopes
                Admin adminToDelete = null;

                // try-catch-finally block to find admin where id's match, check admin rank is higher from request, and delete
                try {

                    // creates parse query targeting admin
                    final ParseQuery<ParseObject> adminToDeleteQuery = ParseQuery.getQuery(DataAccess.DA_ClassNameRelationMapping.Admin.getRelationName());

                    // sets predicate where admin id matches and store id matches
                    adminToDeleteQuery.whereEqualTo(Admin.storeKey, store.getObjectId());
                    adminToDeleteQuery.whereEqualTo(Admin.empIdKey, empId);

                    // finds admins that match predicate
                    final List<ParseObject> adminListAsParse = adminToDeleteQuery.find();

                    // asserts size is one (0 throws parse exception)
                    if (adminListAsParse.size() != 1)
                        throw new RuntimeException("error state in data integrity-- multiple Admin tailored to store and login credentials. Count: ".concat(String.valueOf(adminListAsParse.size())));

                    // extracts and converts known admin as parse to DA
                    adminToDelete = Admin.Builder.toDataAccessFromParse(adminListAsParse.get(0), store);

                    // checks if admin rank of request is higher than to delete
                    if(adminThatRequestedDelete.adminLevel.getIdType() > adminToDelete.adminLevel.getIdType()){

                        //  converts admin to parse and delete
                        adminToDelete.toParseObject().delete();

                        // sets success codes
                        operationResults = RepoCallbackResult.setOperationResultBooleans(true, true, true);
                    }
                    else {

                        // sets error codes -- admin did not retain level
                        operationResults = RepoCallbackResult.setOperationResultBooleans(true, true, false);
                    }

                } catch (ParseException ex) {
                    // differentiates cases (a. search for admin was unsuccessful, b. internal error)
                    if(adminToDelete == null){

                        // sets error codes admin not found
                        operationResults = RepoCallbackResult.setOperationResultBooleans(true, false);
                    }
                    else{
                        // internal error
                        operationResults = RepoCallbackResult.setOperationResultBooleans(false);
                    }
                }
                finally {

                    // returns composites RepoCallBackResult
                    return new RepoCallbackResult(operationResults, AdapterMethodType.addAdmin, brokerCallbackDelegate, null, null);
                }

            }
        };

        // invokes repo to instigate task
        this.backFourAppRepo.instigateAsyncRepoTask(executeRepoCallTask, this);
    }

    // updates an admin
    public void updateAdmin(@NonNull final Admin admin, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate){

        // local ref to repo task
        final BackFourAppRepo.ExecuteRepoCallTask executeRepoCallTask = new BackFourAppRepo.ExecuteRepoCallTask() {
            @Override
            public RepoCallbackResult executeRepo() {

                // enumerates local state promised to callback
                HashMap<String, Boolean> operationResults = RepoCallbackResult.setOperationResultBooleans(false);

                // try-catch-final block to convert to parse and resave
                try {

                    // converts to parse object and saves
                    admin.toParseObject().save();

                    // sets success code
                    operationResults = RepoCallbackResult.setOperationResultBooleans(true);

                } catch (ParseException ex) {

                    // sets errors code
                    operationResults = RepoCallbackResult.setOperationResultBooleans(false);

                } finally {

                    // renders return repo callback result
                    return new RepoCallbackResult(operationResults, AdapterMethodType.updateAdmin, brokerCallbackDelegate, null, null);

                }
            }
        };

        // enjoins repo to effectuate task
        this.backFourAppRepo.instigateAsyncRepoTask(executeRepoCallTask, this);
    }

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
                    Log.d("is null", String.valueOf(operationResults == null));
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
    private Commodity findCompositeCommodityFromBarcode(@NonNull final String barcode) throws ParseException{

        // acquires Parse query targeting item
        final ParseQuery<ParseObject> itemSearchQuery = ParseQuery.getQuery(DataAccess.DA_ClassNameRelationMapping.Commodity.getRelationName());

        // acquires all items where barcode is equal
        itemSearchQuery.whereEqualTo(Commodity.barcodeNameKey, barcode);
        final List<ParseObject> itemListAsParse = itemSearchQuery.find();

        // asserts that list size is 1 (0 is thrown as exception)
        if (itemListAsParse.size() != 1)
            throw new RuntimeException("error state in data integrity-- multiple items exist with such barcode. Count: ".concat(String.valueOf(itemListAsParse.size())));

        // acquires composite item from list -- known size of 1
        return Commodity.Builder.toDataAccessFromParse(itemListAsParse.get(0));
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
                brokerCallbackDelegate.findAdminByEmpId(operationWasSuccess, adapterOperationWasSuccess, contextOperationWasSuccess, (Admin)dataAccess);
                break;
            case validateIfAdminUsernameIsUnique:
                brokerCallbackDelegate.isAdminUsernameUniqueHandler(operationWasSuccess, adapterOperationWasSuccess);
                break;
            case addAdmin:
                brokerCallbackDelegate.addAdminHandler(operationWasSuccess);
                break;
            case deleteAdmin:
                brokerCallbackDelegate.deleteAdminHandler(operationWasSuccess, adapterOperationWasSuccess, contextOperationWasSuccess);
                break;
            case updateAdmin:
                brokerCallbackDelegate.updateAdminHandler(operationWasSuccess);
                break;
            case initializeDepartments: // (SAFE cast, never not null and is department)
                assert(dataAccessList != null);
                brokerCallbackDelegate.initializeDepartmentsHandler(operationWasSuccess, (List<Department>)dataAccessList);
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

        // retrieves a list of parse objects and converts to DeptStock
        @NonNull
        static List<DepartmentStock> convertParseObjectsToDeptStock(@NonNull final List<ParseObject> parseObjectList){

            // creates local list ref
            final List<DepartmentStock> departmentStockList = new ArrayList<DepartmentStock>();

            // walks through all parse objects, using ORM to convert them into StoredDept
            for(ParseObject parseObject: parseObjectList)
                departmentStockList.add(DepartmentStock.Builder.toDataAccessFromParse(parseObject));

            return departmentStockList;
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

                if(parseObject.getObjectId().equals(storedDept.departmentObjectId))
                    return parseObject;
            }

            // throws exception is no match uncovered
            throw new NoSuchElementException("no department pertains to stored dept");
        }

        // helper to acquire Department dept where StoredDept's dept id == Department's
        @NonNull
        static Department getDepartmentFromDeptStock(@NonNull final DepartmentStock deptStock, @NonNull final List<Department> departmentList){

            // walks through all departments and finds department that has same id
            for(Department department: departmentList){
                if(department.getObjectId().equals(deptStock.getObjectId()))
                    return department;
            }

            // throws exception is no match uncovered
            throw new NoSuchElementException("no department pertains to dept stock");
        }

        // helper to convert a list of parse objects of type commodity to commodity list
        @NonNull
        static List<Commodity> convertCommodityListAsParseToCommodityList(@NonNull final List<ParseObject> parseObjectList){

            // creates local ref to commodity list
            final ArrayList<Commodity> commodityArrayList = new ArrayList<Commodity>();

            // walks through all parse objects, invoking ORM to convert over
            for(ParseObject parseObject: parseObjectList)
                commodityArrayList.add(Commodity.Builder.toDataAccessFromParse(parseObject));

            return commodityArrayList;
        }

        // helper to convert a list of composite of full DAs to list of objectIds
        @NonNull
        static List<String> convertDataAccessToObjectIdList(@NonNull final List<? extends  DataAccess> dataAccessList){

            // creates local ref to array list of object ids
            final ArrayList<String> objectIdList = new ArrayList<String>();

            // walks through all DAs, acquiring their object ids
            for(DataAccess dataAccess: dataAccessList)
                objectIdList.add(dataAccess.getObjectId());

            return objectIdList;
        }

        // helper to amalgamate pairs of dept stock list with department list
        @NonNull
        static List<Pair<DepartmentStock, Department>> combineDepartmentStockWithDepartment(@NonNull final List<DepartmentStock> departmentStockList, @NonNull final List<Department> departmentList){

            // creates local list pair ref
            final List<Pair<DepartmentStock, Department>> deptStockToDeptPairList = new ArrayList<Pair<DepartmentStock, Department>>();

            // walks through all department stock and matches it to department where object id's match
            for(DepartmentStock departmentStock: departmentStockList){

                // invokes helper to acquire ref of Department where object id's match
                final Department departmentOfMatch = getDepartmentFromDeptStock(departmentStock, departmentList);

                // appends pair to list
                deptStockToDeptPairList.add(new Pair<DepartmentStock, Department>(departmentStock, departmentOfMatch));
            }

            return deptStockToDeptPairList;
        }

        // helper to update item from dept stock w/ dept pair list
        static void updateItemFromDeptStockToDepartmentPairList(@NonNull final List<Commodity> commodityList, @NonNull final List<Pair<DepartmentStock, Department>> deptStockToDepartmentPairList){

            // walks through all items, finding and updating where item object id == department stock's commodity id
            for(Commodity commodity: commodityList){

                // invokes helper to acquire pair where object id's match
                final Pair<DepartmentStock, Department> pair = getPairOfDeptStockWithDepartmentFromCommodity(commodity, deptStockToDepartmentPairList);

                // invokes update on commodity
                commodity.updateCommodity(pair.second, pair.first);
            }
        }

        // helper method to return dept stock w/ dept pair where it matched commodity object id
        @NonNull
        static Pair<DepartmentStock, Department> getPairOfDeptStockWithDepartmentFromCommodity(@NonNull final Commodity commodity, @NonNull final List<Pair<DepartmentStock, Department>> deptStockToDeptPairList){

            // walks through all pairs and returns match on object id equality
            for(Pair<DepartmentStock, Department> pair: deptStockToDeptPairList){
                if(pair.first.itemObjectId.equals(commodity.getObjectId()))
                    return pair;
            }

            // throws exception is no match uncovered
            throw new NoSuchElementException("no pair pertains to commodity's object id");
        }

    }
}
