package com.example.smartshopper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* purpose: when validating if barcode exist, multiple variations can ensue--
    1) the barcode does not exist in any store
    2) the barcode exist to database, but not to current store. The composite item shall be returned
    3) the barcode exist to the store. The full item shall be returned to the store
 */
public final class BarcodeExistResult {

    // denotes if item exist to database
    public boolean newBarcode;

    // denotes if item exist to store
    public boolean newBarcodeToStore;

    // holds the composite, or complete item (complete retains relational data such as price, location, department, hasAisles)
    public Commodity commodity;

    // package private constructor for repo creation
    BarcodeExistResult(final boolean newBarcode, final boolean newBarcodeToStore, @Nullable Commodity commodity){
        this.newBarcode = newBarcode;
        this.newBarcodeToStore = newBarcodeToStore;
        this.commodity = commodity;
    }

}
