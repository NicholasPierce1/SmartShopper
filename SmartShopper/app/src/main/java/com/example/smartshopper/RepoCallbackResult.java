package com.example.smartshopper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;

// holds outputs produced from all repo operations
final class RepoCallbackResult {

    // enumerates public static constants for hashmap keys
    public static final String operationSuccessKey = "operationSuccess";
    public static final String adapterOperationSuccessKey = "adapterOperationSuccess";
    public static final String contextOperationSuccessKey = "contextOperationSuccess";

    // enumerates local output variables conditionally produced, and passed into creation of instance

    // denotes whether a) operation was success, b) specific operation was success, and possible c) context operation was success
    HashMap<String, Boolean> operationResultHolder;

    // holds up-casted DA object (can be composite or full)
    DataAccess dataAccess;

    // holds up-castes DA objects (always be full)
    List<DataAccess> dataAccessList;

    // denotes what adapter method is transpiring
    AdapterMethodType adapterMethodType;

    // used to invoke final callback to broker
    BrokerCallbackDelegate brokerCallbackDelegate;

    // private constructor to bar simplified creation
    private RepoCallbackResult(){}

    // accessible constructor to repo & adapter
    RepoCallbackResult(@NonNull final HashMap<String, Boolean> operationResultHolder, @NonNull final AdapterMethodType adapterMethodType, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate, @Nullable final DataAccess dataAccess, @Nullable final List<DataAccess> dataAccessList){
        this.operationResultHolder = operationResultHolder;
        this.adapterMethodType = adapterMethodType;
        this.brokerCallbackDelegate = brokerCallbackDelegate;
        this.dataAccess = dataAccess;
        this.dataAccessList = dataAccessList;
    }
}
