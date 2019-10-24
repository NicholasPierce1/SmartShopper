package com.example.smartshopper;

public class AdminMockModelClass {
    public static String[] a1 = {"Matthew Berry", "001", "admin", "Owner", "001"};
    //For milestone two
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
