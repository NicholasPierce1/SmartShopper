package com.example.smartshopper;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdminMockModelClass {
    public static Admin a1 = new Admin("Matthew Berry", "001", "admin", AdminLevel.owner, Store.storeBuilder("001"));
    public static Admin a2 = new Admin("Joe Average", "002", "admin", AdminLevel.storeAdmin, Store.storeBuilder("001"));
    public static Admin a3 = new Admin("Mason Middle", "002", "admin", AdminLevel.managingStoreAdmin, Store.storeBuilder("001"));
    public static ArrayList<String> adminUserNames = new ArrayList<String>();
    public static ArrayList<String> adminPw = new ArrayList<String>();
    public ArrayList<Admin> adminList = new ArrayList<Admin>();
    //IMPORTANT: THE INDEXING OF THE THREE ARRAYLISTS MUST MATCH! If they don't bad things happen
    //Bad things...
    //VERY BAD THINGS
    private boolean started = populateFakeData(); //This is just to make java do a thing
    //For milestone two
    public Admin adminFinder(String id){
        //Method predicated on the fact that the proper, valid, and relevant adminID exists
        int finder = adminList.indexOf(id);
        return adminList.get(finder);
    }
    private boolean populateFakeData(){
        adminList.add(a1);
        adminUserNames.add(a1.userName);
        adminPw.add(a1.password);
        adminList.add(a2);
        adminUserNames.add(a2.userName);
        adminPw.add(a2.password);
        adminList.add(a3);
        adminUserNames.add(a3.userName);
        adminPw.add(a3.password);
        return true;
    }
    public void fakeCreator(Admin a){
        adminList.add(a);
        adminUserNames.add(a.userName);
        adminPw.add(a.password);
    }
    public void fakeUpdator(Admin a){
        String id = a.userName;
        fakeDestroyer(id);
        fakeCreator(a);
    }
    public void fakeDestroyer(String id){
        int temp = adminUserNames.indexOf(id);
        adminList.remove(temp);
        adminUserNames.remove(temp);
        adminPw.remove(temp);
    }
}
