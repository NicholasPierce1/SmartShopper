package com.example.smartshopper;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class Model implements BrokerCallbackDelegate {
    //This model is Beetlejuice approved
    private List<Department> departmentList;
    private CallBackInterface mm;
    private AdminProductCBMethods adminProductScreenActivity;
    private Store store;
    private String no = "";
    int oppCode;
    private  Bundle rtrn = new Bundle();
    String barcode;
    private static Model shared = new Model();
    private Commodity co;
    private int createCase = -1;
    private String adminID, username;
    private Admin requestor;
    private boolean inHouse;

    public static Model getShared(){
        return shared;
    }
    private Adapter adapter = Adapter.getShared();

    // returns local store ref stored by model after user selection in initialization
    public Store getStore(){
        return store;
    }


    // INIT METHODS (Controller--Model)


    // initial method for model/broker -- acquire all stores for user selection / initialization
    public void findAllStores(@NonNull final Context context, @NonNull final WelcomeScreenModelMethods welcomeScreenModelMethods) throws ExceptionInInitializerError{

        // upcast delegate reference
        this.mm = welcomeScreenModelMethods;

        // enjoins compiler to commence retrieval of all stores
        adapter.findAllStores(context, this);
    }

    @Override
    public void getStoresHandler(boolean searchSuccess, @Nullable List<Store> storeList) {
        ((WelcomeScreenModelMethods)mm).storeCB(searchSuccess,storeList);
    }



    // from user selection, set store ref and acquire all depts per store
    public void initializeDeapartmentListForStore(@NonNull final Store store, @NonNull final WelcomeScreenModelMethods mm){

        // upcast delegate reference
        this.mm = mm;

        // assigns user selected store
        this.store = store;

        // invokes adapter to retrieve all departments per store
        adapter.retrieveAllDepartmentsForStore(store, this);
    }

    @Override
    public void initializeDepartmentsHandler(boolean initSuccess, @Nullable List<Department> departmentList) {
        if(initSuccess)
            this.departmentList = departmentList;

        // invokes downcast delegate for apt callback
        ((WelcomeScreenModelMethods)this.mm).departmentCB(initSuccess);
    }




    // ITEM METHODS (CONTROLLER-MODEL)

    // validate uniqueness of item via barcode
    // TODO, Mat why aren't you using callbacks? The delegate is not used in this transaction
    public void validateBarcode(String barcode, int oppCode, AdminProductCBMethods cbm){
        adminProductScreenActivity = cbm;
        this.oppCode = oppCode;
        this.barcode = barcode;
        Log.d("ProductProbs", "IN Validatebarcode" + barcode);
        adapter.validateIfBarcodeExist(store, departmentList, barcode, this);
    }

    @Override
    public void validateIfBarcodeExistHandler(boolean searchWasSuccess, @NonNull BarcodeExistResult barcodeExistResult) {
        Log.d("ProductProbs", "New bar: " + barcodeExistResult.newBarcode + "Is new to store: " + barcodeExistResult.newBarcodeToStore);
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



    // creates/adds item
    public void createItem(Bundle b, AdminProductCBMethods cmb){
        this.mm = cmb;

        adapter.createAndSaveItemForStoreInDept((Department)b.getSerializable("dept"),
                b.getString("barcode"), b.getString("name"), b.getString("vendor")
        , b.getString("tags"), b.getDouble("price"), (Location)b.getSerializable("location"), this);
    }

    @Override
    public void createItemForStoreInDepartment(boolean isSuccessful) {
        Log.d("ProductProbs", "Success of creation: " + isSuccessful);
        ((AdminProductCBMethods)mm).createCB(isSuccessful);

    }


    // updates item
    public void updateItem(Bundle c, AdminProductCBMethods cbm){
        this.mm = cbm;


        adapter.updateItem((Commodity)c.getSerializable("c"), this);
    }

    @Override
    public void updateItemHandler(boolean isSuccessful) {
        ((AdminProductCBMethods)mm).updateCB(isSuccessful);

    }



    // deletes item
    public  void deleteItem(Commodity c, AdminProductCBMethods cbm){
        this.mm = cbm;

        // TODO: Mat, we only need barcode, you can change the commodity to a string of barcode
        // TODO: I find the item internally for you, that why the callback let's you know if it worked
        adapter.deleteItemFromBarcode(store, c.barcode, this);
    }

    @Override
    public void deleteItemHandler(boolean isSuccessful) {
        ((AdminProductCBMethods)mm).delCB(isSuccessful);

    }




    // searches for item via search phrase
    public void searchCommoditiesBySearchPhrase(@NonNull final String searchPhrase, @NonNull final SearchResultHandler searchResultHandler){

        // assigns upcasted delegate handler
        this.mm = searchResultHandler;

        // checks length is greater than three
        if(searchPhrase.length() <= 3)
            searchResultHandler.searchCB(false, false, true, null);

        // acquires shared adapter to invoke search
        this.adapter.searchForItemByPhrase(this.store, searchPhrase, this.departmentList, this);
    }

    // implements handler to process adapter response for searching items
    @Override
    public void findItemsBySearchHandler(boolean searchWasSuccessful, @NonNull List<Commodity> commodityList) {

        // if operation was error, invoke handler, passing error results + converse
        if(!searchWasSuccessful) {
            ((SearchResultHandler) this.mm).searchCB(false, false, false, null);
            return;
        }

        // search was success, assert size of list is in context of business rules
        if(commodityList.size() > 3 || commodityList.isEmpty()) {
            ((SearchResultHandler) this.mm).searchCB(searchWasSuccessful, false, false, null);
            return;
        }

        // else, operation was success AND search size yielded within scopes
        ((SearchResultHandler)this.mm).searchCB(searchWasSuccessful, true, false, commodityList);

    }



    // validates commodity is sound to add
    public void validateComodityInput(boolean weNeedToCheckName, int oppCode,  Bundle c, AdminProductCBMethods cbm){

        this.mm = cbm;
        this.oppCode = oppCode;

        String name = "", vendor = "", tags;
        int dept, isle;
        double price;
        Department department;
        Location location;
        //Now begins all of our validation checks for Comodity.
        //No will be used as A string of problems that occured.
        if(oppCode ==1) {
            name = c.getString("name", "");
            vendor = c.getString("vendor", "");
            if(isEmpty(name)){
                no += "Name is empty";
                weNeedToCheckName = false;
            }

            if(isEmpty(vendor)){
                no += " Venodr is empty";
                weNeedToCheckName = false;
            }
            dept = (c.getInt("dept"));
            isle = (c.getInt("isle"));
            DepartmentType dt = DepartmentType.getDepartmentTypeFromID(dept);
            department =  getDepartmentFromDepartmentType(dt);
            location = Location.getLocationFromLocationId(isle);
            tags = c.getString("tags");
            price = (c.getDouble("price"));

        }
        else { //WE assume the later of the two options it could be
            co = (Commodity) c.getSerializable("C");
            name = co.name;
            vendor = co.vendorName;
            price = co.price;
            department = co.department;
            location = co.location;
            tags = co.searchPhrase;
        }
        //For the return home if they get one
        rtrn.putString("name", name);
        rtrn.putString("vendor", vendor);
        rtrn.putDouble("price", price);
        rtrn.putSerializable("dept",department);
        rtrn.putSerializable("location", location);
        rtrn.putString("tags", tags);

        if(price < 0){
            no +=" Price cannot be negative.";
        }
        if(tags.isEmpty()){
            no += " Tags cannot be empty.";
        }
        if (weNeedToCheckName){
            //Callback time.
            adapter.validateItemNameToVendorIsUnique(name, vendor, this);
        }

        // TODO, Mat be VERY sure to call a handler directly from the model
        else validateNameForVendorIsUniqueHandler(true, true);
    }

    @Override
    public void validateNameForVendorIsUniqueHandler(boolean searchWasSuccess, boolean nameIsUnique) {
        if(searchWasSuccess){
            if(!nameIsUnique){
                no += "That product name already exists for the vendor";
            }
        }
        else no = "Cannot Update or create at this time at this time. Please try again later";
        if(oppCode ==1){
            ((AdminProductCBMethods)mm).validationCB(no, oppCode, rtrn);
        }

        else {
            Bundle r = new Bundle();
            r.putSerializable("c", co);
            ((AdminProductCBMethods)mm).validationCB(no, oppCode, r);
        }

    }
    // ADMIN METHODS (CONTROLLER- MODEL)


    public void findAdminByID(int oppCode,String id, Admin adminThatIssuedRequest, AdminModCBMethods cbm){
        Log.d("AdminValid", "In admin **ID** called method");
        this.mm= cbm;
        this.oppCode = oppCode;

        adapter.findAdminByEmpId(store, id, adminThatIssuedRequest, this );
    }

    @Override
    public void findAdminByEmpId(boolean adminSearchWasSuccess, boolean adminFoundAndIsInStore, boolean didAdminRetainPrivilegesToAcquire, @Nullable Admin admin) {
        //Method called to make sure the empid exits or does not exist. empid != username
        Log.d("AdminValid", "In FindadminByID handler privalge: " + didAdminRetainPrivilegesToAcquire);
        if(adminSearchWasSuccess){
            ((AdminModCBMethods)mm).adminIdCheckCB(oppCode,adminFoundAndIsInStore, didAdminRetainPrivilegesToAcquire,admin);
        }
        else{
            ((AdminModCBMethods)mm).adminNotFound();
        }
    }

    // creates/adds admin
    public void createAdmin(@NonNull final String name, @NonNull String id, @NonNull final String userName, @NonNull final String password, @NonNull final AdminLevel adminLevel, AdminModCBMethods cbm){
        this.mm = cbm;

        adapter.saveAdminToStore(store, id, name, userName, password, adminLevel, this);
    }

    @Override
    public void addAdminHandler(boolean wasAdminAdded) {
        ((AdminModCBMethods)mm).aCreateCB(wasAdminAdded);
    }



    // updates admin
    public void updateAdmin(Admin admin, AdminModCBMethods cbm){
        this.mm = cbm;

        adapter.updateAdmin(admin, this);
    }

    @Override
    public void updateAdminHandler(boolean wasAdminUpdated) {
        ((AdminModCBMethods)mm).aModifyCB(wasAdminUpdated);
    }



    // deletes admin
    public void deleteAdmin(Admin user, Admin subject, AdminModCBMethods cbm){
        this.mm = cbm;

        // TODO, Mat the purpose of the adapter method is to help your model implementation, I find the admin for you by the empId
        // TODO, lemme help and just pass the unverified empId value to me
        adapter.deleteAdmin(store, subject.empID, user,this);
    }

    @Override
    public void deleteAdminHandler(boolean wasAdminRemoved, boolean wasAdminFound, boolean didAdminRetainPrivilegesToRemove) {
        ((AdminModCBMethods)mm).aDelCB(wasAdminRemoved);
    }



    // validates uniqueness of admin's adminID
    public void checkForExistingUsername(int oppCode,  String username, Admin requestor, @NonNull final AdminModCBMethods adminModCBMethods){

        // upcast delegate ref to singleton member
        this.mm = adminModCBMethods;

        // TODO, Mat why are you setting 'requestor'? Does controller not have local ref of the loggedInAdmin?
        // TODO, Mat, are you validating a adminID is unqiue, then retrieving and admin by empId -- which is different -- with that adminID?
        this.requestor = requestor;
        this.username = username;
        this.oppCode = oppCode;

        // enjoins adapter to corroborate if admin adminID is unique
        Log.d("AdminValid", "**Username** in model call: " + username);
        Log.d("AdminValid", "USERNAME OPPCODE: " + oppCode);
        inHouse = false;
        adapter.isAdminUsernameUnique(username,this);
    }

    @Override
    public void isAdminUsernameUniqueHandler(boolean adminSearchWasSuccess, boolean adminFound) {
        Log.d("AdminValid", "Admin valid from model:" + adminFound);
        if(adminSearchWasSuccess){
            ((AdminModCBMethods)mm).adminUsernameCB(adminFound);
        }

    }



    // ADMIN METHOD (LOGIN CONTROLLER- MODEL)
    // logins an admin
    public void login(String username, String password, LoginCB cbm){
        this.mm = cbm;

        adapter.loginAdminByUsernameAndPassword(store, username, password, this);
    }

    @Override
    public void loginAdminByUsernameAndPassword(boolean adminLoginWasSuccess, boolean adminFoundAndIsInStore, @Nullable Admin admin) {
        ((LoginCB)mm).loginCB(adminLoginWasSuccess, adminFoundAndIsInStore,admin);
    }


    // internal model calls to corroborate business logic


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
    Department getDepartmentFromDepartmentType(DepartmentType dt){
        for(Department d: departmentList){
            if(d.type.toString().equals(dt.toString())){
                return d;
            }
        }
        return null;
    }
    private boolean isEmpty(String s) {
        return (s == null || s.equals("") || s.equals(" "));
    }

}
