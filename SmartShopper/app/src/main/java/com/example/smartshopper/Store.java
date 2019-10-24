package com.example.smartshopper;

// denotes the definition of Store in data access layer
public final class Store {

    // enumerates shared properties
    String objectID;

    // enumerates local properties

    public String location;  // "city,StateAbbreviation"

    public String name; // type of store like "Walmart"

    // defines constructor for simplified creation
    public Store(String location, String name){
        //TODO: validated location input for base pattern

        this.location = location;
        this.name = name;
    }
}
