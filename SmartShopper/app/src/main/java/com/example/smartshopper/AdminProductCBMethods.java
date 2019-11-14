package com.example.smartshopper;

import android.os.Bundle;

public interface AdminProductCBMethods extends CallBackInterface {
    public void buttonPressedCB(int code, String barcode, int createCase, Commodity commodity);
    public void validationCB(String wrong, int oppCode, Bundle b);
    public void createCB(boolean cb);
    public void updateCB(boolean cb);
    public void delCB(boolean cb);

}
