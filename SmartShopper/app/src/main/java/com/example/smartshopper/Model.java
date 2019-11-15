package com.example.smartshopper;

import android.content.Context;
import android.os.Bundle;
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
    private String no = "";
    int oppCode;
    private  Bundle rtrn = new Bundle();
    String barcode;
    private static Model shared = new Model();
    private Commodity co;
    private int createCase = -1;
    private String username;
    private Admin requestor;
    private boolean inHouse;

    public static Model getShared(){
        return shared;
    }
    private Adapter adapter = Adapter.getShared();

    // initial method for model/broker -- acquire all stores for user selection / initialization
    public void findAllStores(@NonNull final Context context, @NonNull final WelcomeScreenModelMethods welcomeScreenModelMethods) throws ExceptionInInitializerError{

        // upcast delegate reference
        this.mm = welcomeScreenModelMethods;

        // enjoins compiler to commence retrieval of all stores
        adapter.findAllStores(context, this);
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

    public void createItem(Bundle b){
        adapter.createAndSaveItemForStoreInDept((Department)b.getSerializable("dept"),
                b.getString("barcode"), b.getString("name"), b.getString("vendor")
        , b.getString("tags"), b.getDouble("price"), (Location)b.getSerializable("location"), this);
    }
    public void updateItem(Bundle c){
        adapter.updateItem((Commodity)c.getSerializable("c"), this);
    }
    public  void deleteItem(Commodity c){
        adapter.deleteItemFromBarcode(store, c.barcode, this);
    }
    public void findAdminByID(String id, Admin requestor){
        adapter.findAdminByEmpId(store, id, false, requestor, this );
    }
    public Store getStroe(){
        return store;
    }
    public void createAdmin(@NonNull final String name, @NonNull String id, @NonNull final String userName, @NonNull final String password, @NonNull final AdminLevel adminLevel){
       adapter.saveAdminToStore(store, id, name, userName, password, adminLevel, this);
    }
    public void updateAdmin(Admin admin){
        adapter.updateAdmin(admin, this);

    }
    public void deleteAdmin(Admin user, Admin subject){
        adapter.deleteAdmin(store, subject.empID, user,this);
    }



    public void validateComodityInput(boolean weNeedToCheckName, int oppCode,  Bundle c){
        this.oppCode = oppCode;

        String name = "", vendor = "", tags;
        int dept, isle;
        double price=-1.0;
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
        else validateNameForVendorIsUniqueHandler(true, true);


    }

    public void validateBarcode(String barcode, int oppCode, AdminProductCBMethods cbm){
        adminProductScreenActivity = cbm;
        this.oppCode = oppCode;
        this.barcode = barcode;

        adapter.validateIfBarcodeExist(store, departmentList, barcode, this);
    }

    public void checkForExistingUsername(int oppCode,  String username, Admin requestor){
        this.requestor = requestor;
        this.username = username;
        this.oppCode = oppCode;
        adapter.isAdminUsernameUnique(username, this);
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

    @Override
    public void createItemForStoreInDepartment(boolean isSuccessful) {
        ((AdminProductCBMethods)mm).createCB(isSuccessful);

    }

    @Override
    public void updateItemHandler(boolean isSuccessful) {
        ((AdminProductCBMethods)mm).updateCB(isSuccessful);

    }

    @Override
    public void deleteItemHandler(boolean isSuccessful) {
        ((AdminProductCBMethods)mm).delCB(isSuccessful);

    }


    // implements handler to process adapter response for searching items
    @Override
    public void findItemsBySearchHandler(boolean searchWasSuccessful, @NonNull List<Commodity> commodityList) {

        // if operation was error, invoke handler, passing error results + converse
        if(!searchWasSuccessful)
            ((SearchResultHandler)this.mm).searchCB(false, false, false, null);

        // search was success, assert size of list is in context of business rules
        if(commodityList.size() > 3 || commodityList.isEmpty())
            ((SearchResultHandler)this.mm).searchCB(searchWasSuccessful, false, false, null);

        // else, operation was success AND search size yielded within scopes
        ((SearchResultHandler)this.mm).searchCB(searchWasSuccessful, true, false, commodityList);

    }

    @Override
    public void isAdminUsernameUniqueHandler(boolean adminSearchWasSuccess, boolean adminFound) {
        if(adminSearchWasSuccess){
            if(oppCode == 1){
                ((AdminModCBMethods)mm).adminIdCheckCB(oppCode, adminFound, null);
                //WE don't care about an admin object so move on.
            }
            //For Update
            else if(oppCode == 2 || oppCode == 3){
                if(!adminFound){
                    ((AdminModCBMethods)mm).adminIdCheckCB(oppCode, adminFound, null);
                }
                else{
                    inHouse = true;
                    findAdminByID(username, requestor);
                }
            }


        }



    }

    @Override
    public void loginAdminByUsernameAndPassword(boolean adminLoginWasSuccess, boolean adminFoundAndIsInStore, @Nullable Admin admin) {

    }

    @Override
    public void findAdminByEmpId(boolean adminSearchWasSuccess, boolean adminFoundAndIsInStore, boolean didAdminRetainPrivilegesToAcquire, @Nullable Admin admin) {
            if(adminFoundAndIsInStore && adminFoundAndIsInStore){
                if(inHouse){
                    //WE know it's from a mehtod called by model and not an external class
                    finishisAdminUsernameUniqueHandler(admin);
                }
                else{
                    // TODO: 11/14/2019 Finish this mehtod for external calls if needed
                }
            }
            else{
                ((AdminModCBMethods)mm).adminNotFound();
            }

    }

    @Override
    public void deleteAdminHandler(boolean wasAdminRemoved, boolean wasAdminFound, boolean didAdminRetainPrivilegesToRemove) {
        ((AdminModCBMethods)mm).aDelCB(wasAdminRemoved);
    }

    @Override
    public void updateAdminHandler(boolean wasAdminUpdated) {
        ((AdminModCBMethods)mm).aModifyCB(wasAdminUpdated);
    }

    @Override
    public void getStoresHandler(boolean searchSuccess, @Nullable List<Store> storeList) {
        ((WelcomeScreenModelMethods)mm).storeCB(searchSuccess,storeList);

    }

    @Override
    public void initializeDepartmentsHandler(boolean initSuccess, @Nullable List<Department> departmentList) {
        if(initSuccess)
            this.departmentList = departmentList;

        // invokes downcast delegate for apt callback
        ((WelcomeScreenModelMethods)this.mm).departmentCB(initSuccess);
    }

    // external method to be invoked by search controller to enjoin adapter to commence search by phrase
    public void searchCommoditiesBySearchPhrase(@NonNull final String searchPhrase, @NonNull final SearchResultHandler searchResultHandler){

        // assigns upcasted delegate handler
        this.mm = searchResultHandler;

        // checks length is greater than three
        if(searchPhrase.length() <= 3)
            searchResultHandler.searchCB(false, false, true, null);

        // acquires shared adapter to invoke search
        this.adapter.searchForItemByPhrase(this.store, searchPhrase, this.departmentList, this);

    }

    @Override
    public void addAdminHandler(boolean wasAdminAdded) {
        ((AdminModCBMethods)mm).aCreateCB(wasAdminAdded);

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
    private boolean isEmpty(String s) {
        return (s == null || s.equals("") || s.equals(" "));
    }
    private void finishisAdminUsernameUniqueHandler(Admin a){

        ((AdminModCBMethods)mm).adminIdCheckCB(oppCode, true, a);

    }

}



































































































































