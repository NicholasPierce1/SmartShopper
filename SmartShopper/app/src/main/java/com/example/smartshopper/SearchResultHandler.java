package com.example.smartshopper;

import android.widget.LinearLayout;

import java.util.List;

public interface SearchResultHandler extends CallBackInterface{

    public void searchCB(boolean operationSuccess, boolean searchGood, List<Commodity> commodityList);
}
