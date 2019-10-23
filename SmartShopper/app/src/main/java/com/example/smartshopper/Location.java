package com.example.smartshopper;

enum Location {
    aisleOne(1), aisleTwo(2), aisleThree(3), aisleFour(4), aisleFive(5),
    aisleSix(6), aisleSeven(7), aisleEight(8), aisleNine(9), aisleTen(10);

    // defines locationID
    private int locationID;

    Location(int locationID){
        this.locationID = locationID;
    }

    // TODO: implement method to return location id and determine enum by location id
}
