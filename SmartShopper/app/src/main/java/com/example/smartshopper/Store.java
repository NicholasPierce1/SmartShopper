package com.example.smartshopper;

// denotes the definition of Store in data access layer
public final class Store {

    // enumerates shared properties
    String objectID;

    // enumerates local properties
    public static String[] s1 = {"Maryville,MO", "Ez Shop"};

    public String location;  // "city,StateAbbreviation"

    public String name; // type of store like "Walmart"


    public Store(){

    }
     // defines constructor for simplified creation
    public Store(String location, String name){
        //TODO: validated location input for base pattern

        this.location = location;
        this.name = name;
    }

    public static Store storeBuilder(String storeID){
        String[] storeParams = getStoreParams(storeID);
        String slocation, sname;
        slocation = storeParams[0];
        sname = storeParams[1];
        Store store = new Store(slocation, sname);
        return store;
    }

    private static String[] getStoreParams(String storeID){
        //stubbed method like the one in Admin
        return s1;
    }



   
}
