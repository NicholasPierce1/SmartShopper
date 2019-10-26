package com.example.smartshopper;

import android.util.Pair;

import java.util.ArrayList;

public final class CommodityMockModelTakeTwo {

    // defines local instance state

    // creates strore
    public Store cStore = new Store("Maryville,MO","Walmart");

    // creates departments
    private Department d1 = new Department(DepartmentType.grocery, true, 1,5, cStore);
    private Department d2 = new Department(DepartmentType.frozen, true, 6,9, cStore);

    // creates commodities
    public Commodity c1 = new Commodity("001", "Fritos", "Lays", 2.25, true,
            Location.aisleFourLeft, d1);
    public Commodity c2 = new Commodity("002", "Cardborad Pizza", "Tostionos", 4.25, true,
            Location.aisleSixLeft, d2);

    // holds all commodities
    public ArrayList<Commodity> commodityList= new ArrayList<Commodity>();

    // renders singleton
    public static CommodityMockModelTakeTwo shared = new CommodityMockModelTakeTwo();

    // private constructor
    private CommodityMockModelTakeTwo(){
        // inserts local state in commodity list
        this.commodityList.add(c1);
        this.commodityList.add(c2);
    }

    // validates if add input is correct, and conditionally creates new commodity
    public boolean addCommodity(String barcode, String name, String vendor, String department, int ailseNumber, double price){

        // gets department object from name
        Department connectedDepartment = this.getDepartmentFromDepartmentName(department);


        // checks that no fields are empty & department does not equal null
        if(barcode.isEmpty() || name.isEmpty() || connectedDepartment == null)
            return false;

        // asserts that name-vendor combination is unique and barcode is unique
        for(Commodity commodity: commodityList){
            if((commodity.name.equals(name) && commodity.vendorName.equals(vendor)) || commodity.barcode.equals(barcode))
                return false;
        }

        // asserts department range has aisles and aisle number is in range
        if(!connectedDepartment.hasAisles || connectedDepartment.maxAisle < ailseNumber || connectedDepartment.minAisle > ailseNumber){
            return false;
        }

        // asserts price is non-negative and not infinite
        if(price <= 0 || Double.isInfinite(price))
            return false;

        // asserts barcode is unique


        // item valid. append item
        this.commodityList.add(new Commodity(barcode,name,vendor,price,true, Location.getLocationFromAisleNumber(ailseNumber), connectedDepartment));

        return true;
    }

    // acquires department from string
    public Department getDepartmentFromDepartmentName(String departmentName){
        switch(departmentName){
            case "grocery": return d1;
            case "frozen": return d2;
            default: return null;
        }
    }

    // removes item
    public boolean removeItem(String barcode){
        if(!this.barcodeExist(barcode).first)
            return false;

        // removes item
        this.commodityList.remove(this.barcodeExist(barcode).second.intValue());
        return true;
    }

    // verifies that barcode exist
    private Pair<Boolean, Integer> barcodeExist(String barcode){
        // asserts barcode exist
        for(int i = 0; i < this.commodityList.size(); i++){
            if(this.commodityList.get(i).barcode.equals(barcode))
                return new Pair<Boolean, Integer>(true, i);
        }

        return new Pair<>(false, -1);
    }

    // get item from barcode
    public Commodity getCommodityFromBarcode(String barcode){
        if(!this.barcodeExist(barcode).first)
            return null;

        return this.commodityList.get(this.barcodeExist(barcode).second);
    }

    // modify item
    public boolean modifyCommodityFromBarcode(String barcode, String name, String vendor, String department, int ailseNumber, double price){

        if(barcode.isEmpty())
            return false;

        // gets commodity from barcode
        final Commodity commodity = this.getCommodityFromBarcode(barcode);

        // asserts commodity not null
        if(commodity == null)
            return false;

        // removes
        this.commodityList.remove(commodity);

        // adds
        this.addCommodity(barcode,name,vendor,department,ailseNumber,price);
        return true;
    }

}
