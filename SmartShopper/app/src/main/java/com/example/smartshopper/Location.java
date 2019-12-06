package com.example.smartshopper;

import androidx.annotation.Nullable;

import java.io.Serializable;

enum Location implements Serializable {
    aisleOneLeft(1), aisleOneRight(2), aisleTwoLeft(3), aisleTwoRight(4), aisleThreeLeft(5), aisleThreeRight(6),
    aisleFourLeft(7), aisleFourRight(8), aisleFiveLeft(9), aisleFiveRight(10), aisleSixLeft(11), aisleSixRight(12),
    aisleSevenLeft(13), aisleSevenRight(14), aisleEightLeft(15), aisleEightRight(16),
    aisleNineLeft(17),  aisleNineRight(18), aisleTenRight(19), frozenDepartment(20), dairyDepartment(21),
    meatDepartment(22), seafoodDepartment(23), bakeryDepartment(24), deliDepartment(25),  floralDepartment(26),
    produceDepartmentLeftMostAisle(27), produceDepartmentTopMostAisle(28), produceDepartmentLeftMostDisplay(29), produceDepartmentRightMostDisplay(30);


    // defines locationID
    private int locationID;

    Location(int locationID){
        this.locationID = locationID;
    }

    // returns location id
    public int getLocationID(){return this.locationID;}

    // returns location from aisle number
    @Nullable
    public static Location getLocationFromAisleNumberLocationId(final int aisleNumber) {

        // validates input to assert aisleNumber is within aisle range [1-19]
        if(aisleNumber > Location.aisleTenRight.locationID || aisleNumber < Location.aisleOneLeft.locationID)
            return null;

        // employs switch to acquire corresponding location
        switch(aisleNumber){
            case 1: return aisleOneLeft;
            case 2: return aisleOneRight;
            case 3: return aisleTwoLeft;
            case 4: return aisleTwoRight;
            case 5: return aisleThreeLeft;
            case 6: return aisleThreeRight;
            case 7: return aisleFourLeft;
            case 8: return aisleFourRight;
            case 9: return aisleFiveLeft;
            case 10: return aisleFiveRight;
            case 11: return aisleSixLeft;
            case 12: return aisleSixRight;
            case 13: return aisleSevenLeft;
            case 14: return aisleSevenRight;
            case 15: return aisleEightLeft;
            case 16: return aisleEightRight;
            case 17: return aisleNineLeft;
            case 18: return aisleNineRight;
            default: return aisleTenRight;
        }
    }

    // creates location from exhaustive list of all possibilities-- aisle and non-aisle
    @Nullable
    public static Location getLocationFromLocationId(int locationID){
        // asserts that threshold range is within location's range
        if(locationID < aisleOneLeft.locationID || locationID > produceDepartmentRightMostDisplay.locationID)
            return null;

        // employs switch to walk through all non-aisle cases granted that location is non-aisle
        if(Location.getLocationFromAisleNumberLocationId(locationID) != null)
            return Location.getLocationFromAisleNumberLocationId(locationID);

        switch(locationID){
            case 20: return frozenDepartment;
            case 21: return dairyDepartment;
            case 22: return meatDepartment;
            case 23: return seafoodDepartment;
            case 24: return bakeryDepartment;
            case 25: return deliDepartment;
            case 26: return floralDepartment;
            case 27: return produceDepartmentLeftMostAisle;
            case 28: return produceDepartmentTopMostAisle;
            case 29: return produceDepartmentLeftMostDisplay;
            default: return produceDepartmentRightMostDisplay;
        }
    }
}
