package com.example.smartshopper;

import android.content.res.Resources;
import android.net.IpSecManager;

import androidx.annotation.NonNull;

import com.parse.ParseObject;

import java.io.Serializable;

// retains shared definition and functionality employed by data access types
public abstract class DataAccess implements Serializable {

    // defines shared, locally used objectId value (universal, surrogate key used in back4app)
    private String objectId;

    // package accessible string to acquire shared key of "objectId"
    static final String objectIdKey = "objectId";

    // default constructor for empty declaration
    public DataAccess(){}

    // allows for package private retrieval of object id
    @NonNull
    final String getObjectId(){
        assert(this.objectId != null);

        return this.objectId;
    }

    // assigns objectId from parse object
    public final void setObjectIdFromParseObject(@NonNull final ParseObject parseObject){
        this.objectId = parseObject.getObjectId();
    }

    // builder class to facilitate ORM's amalgamation
    // methods are defined internally from DA's concrete classes
    public static abstract class Builder{

    }

    // package accessible enum that holds mappings to DA class names
    static enum DA_ClassNameRelationMapping{
        Admin("Admin"),
        Commodity("Commodity"),
        Department("Department"),
        DepartmentStock("Department_Stock"),
        Store("Store"),
        StoreDept("Departments_In_Store");

        // local reference relation name
        String relationName;

        DA_ClassNameRelationMapping(@NonNull final String relationName){
            this.relationName = relationName;
        }

        // retrieves relation name
        @NonNull
        String getRelationName(){
            return this.relationName;
        }

    }


}
