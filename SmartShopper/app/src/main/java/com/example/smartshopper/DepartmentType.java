package com.example.smartshopper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.security.InvalidKeyException;

public enum DepartmentType implements Serializable {
    grocery(1), floral(2), deli(3), bakery(4), produce(5), meat(6), seafood(7), dairy(8), frozen(9);

    // holds ordinal value for ease-creation and search
    private int departmentTypeID;

    DepartmentType(final int departmentTypeID){
        this.departmentTypeID = departmentTypeID;
    }

    // from the DepartmentType, return its department type id
    public int getDepartmentTypeID(){
        return this.departmentTypeID;
    }

    // from an int return the enum type corresponding to it
    @Nullable
    public static DepartmentType getDepartmentTypeFromID(int id){

        // employs a switch to walk through cases
        switch(id){
            case 1:
                return DepartmentType.grocery;
            case 2:
                return DepartmentType.floral;
            case 3:
                return DepartmentType.deli;
            case 4:
                return DepartmentType.bakery;
            case 5:
                return DepartmentType.produce;
            case 6:
                return DepartmentType.meat;
            case 7:
                return DepartmentType.seafood;
            case 8:
                return DepartmentType.dairy;
            case 9:
                return DepartmentType.frozen;
            default: // handles all invalid cases
                return null;
        }
    }

    // helper to convert dept types from a string
    @Nullable
    public static DepartmentType getDepartmentTypeFromName(@NonNull final String deptName){

        switch(deptName){
            case "grocery": return DepartmentType.grocery;
            case "floral": return DepartmentType.floral;
            case "deli": return DepartmentType.deli;
            case "bakery": return DepartmentType.bakery;
            case "produce": return DepartmentType.produce;
            case "meat": return DepartmentType.meat;
            case "seafood": return DepartmentType.seafood;
            case "dairy": return DepartmentType.dairy;
            case "frozen": return DepartmentType.frozen;
            default: return null;
        }
    }

    // returns largest department id/ how many departments there are
    public static int getDepartmentCount(){
        return DepartmentType.values().length;
    }
}
