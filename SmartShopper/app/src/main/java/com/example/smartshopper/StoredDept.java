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

    // creates StoredDept from dept and store as parse object w/ other state. Sets hasBeenCreated
    static StoredDept setStoredDeptState(@NonNull final ParseObject store, @NonNull final ParseObject dept){
        return StoredDept.setStoreDeptStateWithAisleRanges(store,dept,false, -1,-1);
    }

    // overload that passes min and max aisle range
    static StoredDept setStoreDeptStateWithAisleRanges(@NonNull final ParseObject store, @NonNull final ParseObject dept, final boolean hasAisle, final int minAisle, final int maxAisle){

        // creates local instance for state config
        final StoredDept storedDept = new StoredDept();

        // assigns store and department id
        storedDept.departmentObjectId = dept.getObjectId();
        storedDept.storeObjectId = dept.getObjectId();

        // sets dept ranges
        storedDept.hasAisle = hasAisle;
        storedDept.maxAisle = hasAisle ? maxAisle: -1;
        storedDept.minAisle = hasAisle ? minAisle: -1;

        return storedDept;
    }

}
