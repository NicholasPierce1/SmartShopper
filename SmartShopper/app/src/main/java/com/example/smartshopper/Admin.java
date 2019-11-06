package com.example.smartshopper;

import androidx.annotation.NonNull;

import com.parse.ParseObject;

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

    public String empID; // unique id to employee -- NOT primary key to relation

    // enumerate local keys for col lookup/storing
    private static final String nameKey = "name";
    private static final String userNameKey = "username";
    private static final String passwordKey = "password";
    private static final String adminLevelKey = "adminLevel";
    private static final String storeKey = "storeId";
    private static final String empIdKey = "empId";

    private Admin(){

    }


    // temporarily public for milestone 2
    @Deprecated
    public Admin(String name, String userName, String pw, AdminLevel level, Store store){
        this.name = name;
        this.userName = userName;
        password = pw;
        adminLevel = level;
        this.store = store;
    }

    // abstract class implementation to render DA object from using a parse object and other composite inputs
    public static class Builder extends DataAccess.Builder{

        // render DA object from using composite inputs and a Store (NOTE: no need to set object id)
        @NonNull
        public Admin build(@NonNull final Store store, @NonNull final String name, @NonNull final String userName, @NonNull final String pw, @NonNull final AdminLevel level, @NonNull final String empId){
            // creates local ref
            final Admin admin = new Admin();

            // assigns local ref state
            admin.adminLevel = level;
            admin.empID = empId;
            admin.name = name;
            admin.userName = userName;
            admin.password = pw;
            admin.store = store;

            return admin;

        }
    }

}
