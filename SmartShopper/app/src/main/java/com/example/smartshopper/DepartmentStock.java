package com.example.smartshopper;

import androidx.annotation.NonNull;

import com.parse.ParseObject;

// package private class to hold proxy data for items in a certain department in a certain store
final class DepartmentStock extends DataAccess {

    // enumerates local state
    Location location; // holds location of item within proxy
    double price; // price of item

    String storeObjectId; // holds foreign key to store relation
    String departmentObjectId; // holds foreign key to department relation
    String itemObjectId; // holds foreign key to item relation

    // enumerates local keys for parse object lookups and sets
    private static String storeObjectIdKey = "storeObjectId";
    private static String departmentObjectIdKey = "departmentObjectId";
    private static String itemObjectIdKey = "itemObjectId";
    private static String locationKey = "location";
    private static String priceKey = "price";

    // private constructor for facilitated static builders
    private DepartmentStock(){}

    // creates DepartmentStock from parse objects and proxy info
    @NonNull
    static DepartmentStock setDepartmentStockState(@NonNull final ParseObject store, @NonNull final ParseObject dept, @NonNull ParseObject item,
                                                   @NonNull final Location location, final double price){
        // creates local DepartmentStock
        final DepartmentStock departmentStock = new DepartmentStock();

        // assigns parameterized state
        departmentStock.storeObjectId = store.getObjectId();
        departmentStock.departmentObjectId = dept.getObjectId();
        departmentStock.itemObjectId = item.getObjectId();

        departmentStock.location = location;
        departmentStock.price = price;

        // sets that creation is complete for conversion to parse object
        departmentStock.setHasBeenCreated();

        return departmentStock;
    }

    // overrides data access abstraction for parse object conversion
    @Override
    @NonNull
    public ParseObject convertToParseObject(@NonNull final ParseObject parseObject){
        // asserts that DA has been created
        super.assertDataAccessObjectHasBeenCreated();

        // supplements local state into parse object
        parseObject.put(DepartmentStock.storeObjectIdKey, this.storeObjectId);
        parseObject.put(DepartmentStock.departmentObjectIdKey, this.departmentObjectId);
        parseObject.put(DepartmentStock.itemObjectIdKey, this.itemObjectId);
        parseObject.put(DepartmentStock.locationKey, this.location.getLocationID());
        parseObject.put(DepartmentStock.priceKey, this.price);

        return parseObject;
    }


}
