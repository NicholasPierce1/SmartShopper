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
    public static final String contextAuxiliaryOperationSuccessKey = "contextAuxiliaryOperationSuccess";

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


    // (helper) takes three booleans in order of (operation, adapter, context) and returns hashmap retaining their values
    @NonNull
    public static HashMap<String, Boolean> setOperationResultBooleans(@NonNull final Boolean operationWasSuccess, @NonNull final Boolean adapterOperationWasSuccess, @NonNull final Boolean contextOperationWasSuccess, @NonNull final Boolean contextAuxOperationWasSuccess){

        // creates local ref to Hashmap
        final HashMap<String, Boolean> operationResults = new HashMap<String,Boolean>();

        // sets boolean values with public keys
        operationResults.put(RepoCallbackResult.operationSuccessKey, operationWasSuccess);
        operationResults.put(RepoCallbackResult.adapterOperationSuccessKey, adapterOperationWasSuccess);
        operationResults.put(RepoCallbackResult.contextOperationSuccessKey, contextOperationWasSuccess);
        operationResults.put(RepoCallbackResult.contextAuxiliaryOperationSuccessKey, contextAuxOperationWasSuccess);

        return operationResults;
    }

    // overloaded helper for operations that only requires three booleans
    @NonNull
    public static HashMap<String, Boolean> setOperationResultBooleans(@NonNull final Boolean operationWasSuccess, @NonNull final Boolean adapterOperationWasSuccess, @NonNull final Boolean contextOperationWasSuccess){
        return RepoCallbackResult.setOperationResultBooleans(operationWasSuccess, adapterOperationWasSuccess, contextOperationWasSuccess, false);
    }


    // overloaded helper for operations that only require one boolean
    @NonNull
    public static HashMap<String, Boolean> setOperationResultBooleans(@NonNull final Boolean operationWasSuccess){
        return RepoCallbackResult.setOperationResultBooleans(operationWasSuccess, false);
    }

    // overloaded helper for operations that only requires two booleans
    @NonNull
    public static HashMap<String, Boolean> setOperationResultBooleans(@NonNull final Boolean operationWasSuccess, @NonNull final Boolean adapterOperationWasSuccess){
        return RepoCallbackResult.setOperationResultBooleans(operationWasSuccess, adapterOperationWasSuccess, false);
    }
}
