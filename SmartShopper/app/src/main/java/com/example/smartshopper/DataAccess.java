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

}
