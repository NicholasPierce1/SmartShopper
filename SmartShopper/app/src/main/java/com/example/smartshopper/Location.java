package com.example.smartshopper;

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

    // TODO: implement method to return location id and determine enum by location id
}
