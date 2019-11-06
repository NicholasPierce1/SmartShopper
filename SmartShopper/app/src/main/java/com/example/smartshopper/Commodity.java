package com.example.smartshopper;

import androidx.annotation.NonNull;

import com.parse.ParseObject;

import java.io.Serializable;

// denotes the definition of Commodities and groceries in data access layer
public final class Commodity extends DataAccess {

    // enumerates shared properties
    String objectId;

    // enumerates local properties

    public String barcode;  // unique id for item

    public String name; // unique in conjunction with vendor name

    public String vendorName;  // denotes vendor that creates item

    public double price; // acquired from DepartmentStock

    public boolean hasAisle; // indicates if item has an aisle type position acquired from DepartmentStock

    public Location location; // explicates where the item is positioned relative to the store

    public Department department; // denotes the corresponding department that the item pertains in conjunction with store

    // enumerates local keys for col lookup/set
    private static final String nameKey = "name";
    private static final String vendorNameKey = "vendorName";

    // temporary public constructor to expedite local-state creation
    @Deprecated
    public Commodity(final String barcode, final String name, final String vendorName, final double price, final boolean hasAisle,
                     final Location location, final Department department){
        this.barcode = barcode;
        this.name = name;
        this.vendorName = vendorName;
        this.price = price;
        this.hasAisle = hasAisle;
        this.location = location;
        this.department = department;
    }

    private Commodity(){}

    // updates Commodity ref state
    public void updateCommodity(@NonNull final Department department, @NonNull final DepartmentStock departmentStock, @NonNull final ParseObject parseObject){

        // updates department ref
        this.department = department;

        // updates ref state stored in relation proxy
        this.location = departmentStock.location;
        this.price = departmentStock.price;
        this.hasAisle = department.hasAisles;

        // updates local object id
        this.setObjectIdFromParseObject(parseObject);

    }

    // abstract class implementation to render DA object from using a parse object and other composite inputs
    public static class Builder extends DataAccess.Builder{

        // renders composite build from user inputs for saving/validation
        @NonNull
        public Commodity build(@NonNull final String barcode,@NonNull final String name,@NonNull final String vendorName){

            // creates local ref
            final Commodity commodity = new Commodity();

            commodity.barcode = barcode;
            commodity.name = name;
            commodity.vendorName = vendorName;

            return commodity;
        }

    }
}
