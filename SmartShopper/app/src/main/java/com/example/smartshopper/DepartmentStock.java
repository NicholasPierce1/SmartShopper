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
    private static final String storeObjectIdKey = "storeObjectId";
    private static final String departmentObjectIdKey = "departmentObjectId";
    private static final String itemObjectIdKey = "itemObjectId";
    private static final String locationKey = "location";
    private static final String priceKey = "price";

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


        return departmentStock;
    }

    // abstract class implementation to render DA object from using a parse object and other composite inputs
    public static class Builder extends DataAccess.Builder{

        // renders Department Stock from Department and Comp Item (NOTE: object id need not be set)
        @NonNull
        public DepartmentStock build(@NonNull final Department department, @NonNull final Commodity commodity, final double price, @NonNull final Location location ){

            // creates local reference
            DepartmentStock departmentStock = new DepartmentStock();

            // assigns local state
            departmentStock.storeObjectId = department.store.getObjectId();
            departmentStock.departmentObjectId = department.getObjectId();
            departmentStock.price = price;
            departmentStock.location = location;

            // returns ref
            return departmentStock;
        }
    }

}
