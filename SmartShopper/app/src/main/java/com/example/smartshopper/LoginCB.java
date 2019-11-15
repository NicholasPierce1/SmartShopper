package com.example.smartshopper;

public interface LoginCB extends CallBackInterface {
    public void loginCB(boolean success, boolean foundInStore, Admin admin);
}
