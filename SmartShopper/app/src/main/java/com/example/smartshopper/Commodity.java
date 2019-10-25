package com.example.smartshopper;

import java.io.Serializable;

// denotes the definition of Commodities and groceries in data access layer
public final class Commodity implements Serializable {

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

    // temporary public constructor to expidite local-state creation
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

}
