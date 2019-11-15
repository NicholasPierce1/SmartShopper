package com.example.smartshopper;

import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.List;

public interface SearchResultHandler extends CallBackInterface{

    public void searchCB(boolean operationSuccess, boolean searchGood, boolean wasSearchTooShort, @Nullable List<Commodity> commodityList);
}
