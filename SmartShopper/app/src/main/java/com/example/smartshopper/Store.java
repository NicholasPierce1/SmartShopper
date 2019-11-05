package com.example.smartshopper;

import androidx.annotation.NonNull;

import com.parse.ParseObject;

import java.io.Serializable;

// denotes the definition of Store in data access layer
public class Store extends DataAccess {

    // enumerates local properties
    public String location;  // "city,StateAbbreviation"
    public String name; // type of store like "Walmart"

    // enumerates local string keys for col lookup/sets
    private static final String locationKey = "location";
    private static final String nameKey = "name";


    @Deprecated
    public Store(final String location, final String name){
        this.location = location;
        this.name = name;

    }

    // data access constructor for static creation
    private Store(){}

}
