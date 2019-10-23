package com.example.smartshopper;

// denotes the definition of Commodities and groceries in data access layer
public final class Commodity {

    // enumerates shared properties
    String objectId;

    // enumerates local properties

    public String barcode;  // unique id for item

    public String name; // unique in conjunction with vendor name

    public String vendorName;  // denotes vendor that creates item

    public double price; // acquired from DepartmentStock

    public boolean hasAisle; // indicates if item has an aisle type position acquired from DepartmentStock

    public Location location; // explicates where the item is positioned relative to the store

}
