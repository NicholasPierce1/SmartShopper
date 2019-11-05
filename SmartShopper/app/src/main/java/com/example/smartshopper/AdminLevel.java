package com.example.smartshopper;

import androidx.annotation.AnyThread;
import androidx.annotation.Nullable;

public enum AdminLevel {
    storeAdmin(0), managingStoreAdmin(1), owner(2);

    // holds local ordinal value in instance definition
    private int idType;

    private AdminLevel(int idType){
        this.idType =  idType;
    }

    // permits acquiring of idType of admin level
    public int getIdType(){return this.idType;}

    // facilitates creation of admin object via customer ordinal
    @Nullable
    @AnyThread
    public AdminLevel getAdminLevelFromInt(int i){

        // employs a switch to return acceptable variations of admin
        switch(i){
            case 0: return AdminLevel.storeAdmin;
            case 1: return AdminLevel.managingStoreAdmin;
            case 2: return AdminLevel.owner;
            default: return null;
        }
    }
}
