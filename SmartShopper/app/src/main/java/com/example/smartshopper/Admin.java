package com.example.smartshopper;

// denotes the definition of Store in data access layer
public final class Admin {

    // enumerates shared properties
    String objectID;

    // enumerates local properties

    public String name; // name of employee in relation

    public String userName; // name of employee in login system

    public String password; // password for verification of employee

    public AdminLevel adminLevel; // denotes level of privileges that an admin can perform

    public Store store; // denotes store that admin presides in
}
