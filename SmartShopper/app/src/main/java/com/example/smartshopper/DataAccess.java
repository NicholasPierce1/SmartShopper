package com.example.smartshopper;

import androidx.annotation.NonNull;

import java.io.Serializable;

// retains shared definition and functionality employed by data access types
abstract class DataAccess implements Serializable {

    // defines shared, locally used objectId value (universal, surrogate key used in back4app)
    private String objectId;

    // default constructor for empty declaration
    DataAccess(){}

    // allows for package private retrieval of object id
    @NonNull
    String getObjectId(){
        assert(this.objectId != null);

        return this.objectId;
    }

    
}