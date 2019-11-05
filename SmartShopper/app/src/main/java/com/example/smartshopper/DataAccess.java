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

    // determines that object was fully created before configured into parse object
    private boolean hasBeenCreated = false;

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

    // asserts that object has been created before conversion
    public final void assertDataAccessObjectHasBeenCreated(){if(!this.hasBeenCreated) throw new Resources.NotFoundException("must set data access state before conversion to parse object");}

    // sets hasBeenCreated after parse object creation
    public final void setHasBeenCreated(){this.hasBeenCreated = true;}

    // converts itself to a ParseObject for searching
    @NonNull
    public abstract ParseObject convertToParseObject(@NonNull final ParseObject parseObject);

    // creates DataAccess object from returned parse object from back4app
    @NonNull
    public abstract DataAccess createFromParseObject(@NonNull final ParseObject parseObject);

}
