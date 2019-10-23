package com.example.smartshopper;

// denotes the definition of Department in data access layer
public final class Department {

    // enumerates shared properties
    String objectID;

    // enumerates local properties

    public DepartmentType type; // indicates the type the department is

    public boolean hasAisles; // indicates if the department contains numeric aisles-- stored in StoreDepartments

    public int minAisle; // acquire from ordinal in Location-- stored in StoreDepartments

    public int maxAisle; // acquire from ordinal in Location-- stored in StoreDepartments

    public Store store; // stored in StoreDepartments
}
