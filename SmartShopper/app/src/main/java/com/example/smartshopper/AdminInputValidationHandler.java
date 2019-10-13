package com.example.smartshopper;
//@Author Matthew Berry
public class AdminInputValidationHandler {
    //The goal of this class is to use one method that validates similar but slightly differnt
    //input between the product and admin screen.

    public boolean isExistingValue (boolean isProduct, String valueCode){
        //is Product is true if the call is made from the Admin Product Screen
        if(isProduct){
            //Makes a database call to see if the value exists or not.
            return productValidationCheck(valueCode);
        }
        else{
            return adminValidationCheck(valueCode);
        }
    }
     boolean productValidationCheck(String valCode){
        //This is  a stubbed method for now
        if(valCode.equals("123"))
         return true;
        else return false;
    }

    boolean adminValidationCheck(String valCode){
        //Also a stubbed method.
        if(valCode.equals("001"))
         return true;
        else return false;

    }

}
