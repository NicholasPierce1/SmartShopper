package com.example.smartshopper;

import androidx.annotation.NonNull;

import com.parse.ParseObject;

import java.io.Serializable;

// relational object to store state between department and store relations-- proxy for creation of department objects
final class StoredDept extends DataAccess {

    // local string keys to create, and id cols within relation
    private static final String hasAisleKey = "hasAisle";
    private static final String minAisleKey = "minAisle";
    private static final String maxAisleKey = "maxAisle";
    private static final String departmentObjectIdKey = "departmentObjectId";
    private static final String storeObjectIdKey = "storeObjectId";

    // enumerates package private state for proxy-relation
    boolean hasAisle; // used to determine if department retains any aisle range

    // if retains aisle range, denotes threshold of range, else -1 is value
    int minAisle;
    int maxAisle;

    // holds non-composite keys to indicate store and department
    String departmentObjectId;
    String storeObjectId;

    // defines empty constructor for parse object creation
    private StoredDept(){}

    // abstract class implementation to render DA object from using a parse object and other composite inputs
    public static class Builder extends DataAccess.Builder{

        // renders StoredDept from a parsed object derived from store dept relation
        public StoredDept build(@NonNull final ParseObject parseObject){

            // creates local ref
            final StoredDept storedDept = new StoredDept();

            // assigns local state from parse object
            storedDept.hasAisle = parseObject.getBoolean(StoredDept.hasAisleKey);
            storedDept.maxAisle = parseObject.getInt(StoredDept.maxAisleKey);
            storedDept.minAisle = parseObject.getInt(StoredDept.minAisleKey);
            storedDept.storeObjectId = parseObject.getString(StoredDept.storeObjectIdKey);
            storedDept.departmentObjectId = parseObject.getString(StoredDept.departmentObjectIdKey);

            // sets object id from parse object
            storedDept.setObjectIdFromParseObject(parseObject);

            return storedDept;

        }
    }

}
