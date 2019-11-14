package com.example.smartshopper;

import android.os.Bundle;

import androidx.annotation.Nullable;

public interface AdminModCBMethods extends CallBackInterface {
    public void adminIdCheckCB(int oppCode, boolean exists, @Nullable Admin a);
    public void adminNotFound();
}
