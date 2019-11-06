package com.example.smartshopper;

import androidx.annotation.NonNull;

import com.parse.ParseObject;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

// denotes the definition of Department in data access layer
public final class Department extends DataAccess {

    // enumerates shared properties
    String objectID;

    // enumerates local properties

    public DepartmentType type; // indicates the type the department is

    public boolean hasAisles; // indicates if the department contains numeric aisles-- stored in StoreDepartments

    public int minAisle; // acquire from ordinal in Location-- stored in StoreDepartments

    public int maxAisle; // acquire from ordinal in Location-- stored in StoreDepartments

    public Store store; // stored in StoreDepartments

    // enumerates local key values for col lookup/storing
    private static final String typeKey = "departmentType";

    @Deprecated
    public Department(final DepartmentType departmentType, final boolean hasAisles, final int minAisle, final int maxAisle, final Store store){
        this.type = departmentType;
        this.hasAisles = hasAisles;
        this.minAisle = minAisle;
        this.maxAisle = maxAisle;
        this.store = store;
    }

    private Department(){}

    // abstract class implementation to render DA object from using a parse object and other composite inputs
    public static class Builder extends DataAccess.Builder{

        // renders Department from parsed object and stored dept
        @NonNull
        public Department build(@NonNull final ParseObject parseObject, @NonNull final StoredDept storedDept, @NonNull final Store store){

            // creates local reference
            final Department department = new Department();

            // assigns local state
            department.type = DepartmentType.getDepartmentTypeFromID(parseObject.getInt(Department.typeKey));
            department.hasAisles = storedDept.hasAisle;
            department.minAisle = storedDept.minAisle;
            department.maxAisle = storedDept.maxAisle;
            department.store = store;

            // renders object id from its parse object
            department.setObjectIdFromParseObject(parseObject);

            return department;
        }

    }
}
