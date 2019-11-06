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


    // abstract class implementation to render DA object from using a parse object and other composite inputs
    public static class Builder extends DataAccess.Builder{

        @NonNull
        public Store build(@NonNull final ParseObject parseObject){

            // creates local store
            final Store store = new Store();

            // assigns local state
            store.name = parseObject.getString(Store.nameKey);
            store.location = parseObject.getString(Store.locationKey);

            // assigns Data access state
            store.setObjectIdFromParseObject(parseObject);

            return store;
        }

    }
}
