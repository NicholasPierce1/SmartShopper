package com.example.smartshopper;
//@Author Matthew Berry
public class AdminInputValidationHandler {
    //The goal of this class is to use one method that validates similar but slightly differnt
    //input between the product and admin screen.

    public static boolean isExistingValue (boolean isProduct, String valueCode){
        //is Product is true if the call is made from the Admin Product Screen
        if(isProduct){
            //Makes a database call to see if the value exists or not.
            return productValidationCheck(valueCode);
        }
        else{
            return adminValidationCheck(valueCode);
        }
    }
     private static boolean  productValidationCheck(String valCode){
        //This is  a stubbed method for now
        if(valCode.equals("123"))
         return true;
        else return false;
    }

   private static boolean adminValidationCheck(String valCode){
        //Also a stubbed method.
        if(AdminMockModelClass.adminUserNames.contains(valCode))
         return true;
        else return false;

    }

}
