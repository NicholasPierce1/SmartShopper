package com.example.smartshopper;

import java.io.Serializable;

// denotes the definition of Store in data access layer
public final class Store implements Serializable {

    // enumerates shared properties
    String objectID;

    // enumerates local properties
    public static String[] s1 = {"Maryville,MO", "Ez Shop"};

    public String location;  // "city,StateAbbreviation"

    public String name; // type of store like "Walmart"


    public Store(final String location, final String name){
        this.location = location;
        this.name = name;

    }




   
}
