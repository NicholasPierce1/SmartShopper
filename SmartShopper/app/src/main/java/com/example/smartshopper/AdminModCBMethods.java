package com.example.smartshopper;

import android.os.Bundle;

import androidx.annotation.Nullable;

public interface AdminModCBMethods extends CallBackInterface {
    public void adminIdCheckCB(int oppCode, boolean exists, boolean hasPermission, @Nullable Admin a);
    public void adminNotFound();
    public void aCreateCB(boolean success);
    public void aModifyCB(boolean success);
    public void aDelCB(boolean success);
    public void adminUsernameCB(boolean valid);


}
