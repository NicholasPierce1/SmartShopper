package com.example.smartshopper;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdminMockModelClass {
    public static boolean firstTime = true;
    public static Admin a1 = new Admin("Matthew Berry", "001", "admin", AdminLevel.owner, Store.storeBuilder("001"));
    public static Admin a2 = new Admin("Joe Average", "002", "admin", AdminLevel.storeAdmin, Store.storeBuilder("001"));
    public static Admin a3 = new Admin("Mason Middle", "003", "admin", AdminLevel.managingStoreAdmin, Store.storeBuilder("001"));
    public static ArrayList<String> adminUserNames = new ArrayList<String>();
    public static ArrayList<String> adminPw = new ArrayList<String>();
    public static ArrayList<Admin> adminList = new ArrayList<Admin>();
    //IMPORTANT: THE INDEXING OF THE THREE ARRAYLISTS MUST MATCH! If they don't bad things happen
    //Bad things...
    //VERY BAD THINGS
    //For milestone two
    public static Admin adminFinder(String id){
        Log.d("LoginDeBug", "{A}Size of admin list is: "+
                AdminMockModelClass.adminList.size());


        //Method predicated on the fact that the proper, valid, and relevant adminID exists
        int finder = adminUserNames.indexOf(id);
        Log.d("LoginDeBug", "finder: " + finder);

        return adminList.get(finder);
    }

    public static void fakeCreator(Admin a){
        adminList.add(a);
        adminUserNames.add(a.userName);
        adminPw.add(a.password);
    }
    public static void fakeUpdator(Admin a){
        String id = a.userName;
        fakeDestroyer(id);
        fakeCreator(a);
    }
    public static void fakeDestroyer(String id){
        int temp = adminUserNames.indexOf(id);
        adminList.remove(temp);
        adminUserNames.remove(temp);
        adminPw.remove(temp);
    }
}
