package com.example.smartshopper;

import androidx.annotation.NonNull;

import com.parse.ParseObject;

// denotes the definition of Store in data access layer
public final class Admin extends DataAccess implements Persistable {

    // enumerates shared properties
    String objectID;

    // enumerates local properties

    public String name; // name of employee in relation

    public String userName; // name of employee in login system

    public String password; // password for verification of employee

    public AdminLevel adminLevel; // denotes level of privileges that an admin can perform

    public Store store; // denotes store that admin presides in

    public long empID; // unique id to employee -- NOT primary key to relation

    // enumerate local keys for col lookup/storing
    static final String nameKey = "name";
    static final String userNameKey = "username";
    static final String passwordKey = "password";
    static final String adminLevelKey = "adminLevel";
    static final String storeKey = "storeId";
    static final String empIdKey = "empId";

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
        //Admin class
        // render DA object from using composite inputs and a Store (NOTE: no need to set object id)
        @NonNull
        public static Admin build(@NonNull final Store store, @NonNull final String name, @NonNull final String userName, @NonNull final String pw, @NonNull final AdminLevel level, final long empId){
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

        // appends functionality to convert a composite OR complete to a parsed object for persistence
        @NonNull
        public static Admin toDataAccessFromParse(@NonNull final ParseObject parseObject, @NonNull final Store store){

            // creates local admin object
            final Admin admin = new Admin();

            // assigns local state
            admin.store = store;
            admin.password = parseObject.getString(Admin.passwordKey);
            admin.userName = parseObject.getString(Admin.userNameKey);
            admin.name = parseObject.getString(Admin.nameKey);
            admin.empID = parseObject.getLong(Admin.empIdKey);
            admin.adminLevel = AdminLevel.getAdminLevelFromInt(parseObject.getInt(Admin.adminLevelKey));

            admin.setObjectIdFromParseObject(parseObject);

            return admin;
        }
    }

    @NonNull
    public ParseObject toParseObject(){

        // creates local reference to Admin relation
        final ParseObject parseObject = new ParseObject("Admin");

        // assigns composite state to parse object
        parseObject.put(Admin.nameKey, this.name);
        parseObject.put(Admin.passwordKey, this.password);
        parseObject.put(Admin.storeKey, this.store.getObjectId());
        parseObject.put(Admin.adminLevelKey, this.adminLevel.getIdType());
        parseObject.put(Admin.userNameKey, this.userName);
        parseObject.put(Admin.empIdKey, this.empID);

        return parseObject;
    }


}
