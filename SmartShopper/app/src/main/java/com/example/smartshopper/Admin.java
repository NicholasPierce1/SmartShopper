package com.example.smartshopper;

// denotes the definition of Store in data access layer
public final class Admin {

    // enumerates shared properties
    String objectID;

    // enumerates local properties
    public static String[] a1 = {"Matthew Berry", "001", "admin", "Owner", "001"};

    public String name; // name of employee in relation

    public String userName; // name of employee in login system

    public String password; // password for verification of employee

    public AdminLevel adminLevel; // denotes level of privileges that an admin can perform

    public Store store; // denotes store that admin presides in

    public Admin(){

    }


    private Admin(String name, String userName, String pw, AdminLevel level, Store store){
        this.name = name;
        this.userName = userName;
        password = pw;
        adminLevel = level;
        this.store = store;
    }
    public static Admin adminBuilder(String adminID){
        String[] adminParams = getAdminDetails(adminID);
         String nme, aid, passwrd, adlevel, sid;
         AdminLevel aLevel;
         Store aStore = new Store();
        nme = adminParams[0];
        aid = adminParams[1];
        passwrd = adminParams[2];
        adlevel = adminParams[3];
        if(adlevel.equals("Admin")){
            aLevel = AdminLevel.storeAdmin;
        }
        else if(adlevel.equals("Store Admin")){
            aLevel = AdminLevel.storeAdmin;
        }
        else aLevel = AdminLevel.owner;
        sid = adminParams[4];
        aStore = Store.storeBuilder(sid);

        Admin admin = new Admin(nme, aid, passwrd, aLevel, aStore);
        return admin;
    }

    private static String[] getAdminDetails(String adminID){
        //At some point this calls the class that class the db and retunrs an object based on adminin
        return a1;
    }

}
