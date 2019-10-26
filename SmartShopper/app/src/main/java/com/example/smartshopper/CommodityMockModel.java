package com.example.smartshopper;

import android.util.Log;

import java.util.ArrayList;

public class CommodityMockModel {
    public static Store cStore = new Store("Maryville,MO","Walmart");
   private static Department d1 = new Department(DepartmentType.grocery, true, 1,5, cStore);
   private static Department d2 = new Department(DepartmentType.frozen, true, 6,9, cStore);
   public static Commodity c1 = new Commodity("001", "Fritos", "Lays", 2.25, true,
            Location.aisleFourLeft, d1);
   public static Commodity c2 = new Commodity("002", "Cardborad Pizza", "Tostionos", 4.25, true,
            Location.aisleSixLeft, d2);
    public static ArrayList<Commodity> commodityList= new ArrayList<Commodity>();
    public  static ArrayList<String> barCodes = new ArrayList<>();
    public static ArrayList<String> vendors = new ArrayList<>();
    public static ArrayList<String> names = new ArrayList<>();

    public static Commodity commodityFinder(String barCode){



        //Method predicated on the fact that the proper, valid, and relevant adminID exists
        int finder = barCodes.indexOf(barCode);
        Log.d("LoginDeBug", "finder: " + finder);

        return commodityList.get(finder);
    }

    public static void fakeCreator(Commodity c){
        commodityList.add(c);
        barCodes.add(c.barcode);
        names.add(c.name);
        vendors.add(c.vendorName);
    }
    public static void fakeUpdator(Commodity c){
        String id = c.barcode;
        fakeDestroyer(id);
        fakeCreator(c);
    }
    public static void fakeDestroyer(String id){
        int temp = barCodes.indexOf(id);
        commodityList.remove(temp);
        barCodes.remove(temp);
        names.remove(temp);
        vendors.remove(temp);
    }

}
