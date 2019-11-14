package com.example.smartshopper;

public interface AdminProductCBMethods extends CallBackInterface {
    public void buttonPressedCB(int code, String barcode, int createCase, Commodity commodity);

}
