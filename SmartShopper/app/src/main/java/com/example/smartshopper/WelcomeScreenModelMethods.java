package com.example.smartshopper;

import java.util.List;

public interface WelcomeScreenModelMethods  extends  CallBackInterface{
    //This method will be used for creation
    public void storeCB(boolean success, List<Store> store);
    public void departmentCB(boolean success);
}
