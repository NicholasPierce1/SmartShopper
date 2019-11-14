package com.example.smartshopper;


import android.content.Context;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.parse.Parse;

import java.util.HashMap;
import java.util.List;

// retains an external, singleton with a method to intake a custom Runnable to pass into a AsyncTask
// outputs are generalize and passed into callback interface
final class BackFourAppRepo{

    // enumerates local keys to procure Back4App database connection
    private static String appId = "";
    private static String clientId = "";
    private static String backFourAppURL = "";

    // private member to assert Parse connection has been initialized
    private boolean parseInitialize = false;

    // local member for singleton
    private final BackFourAppRepo shared = new BackFourAppRepo();

    // private constructor for universal, shared instance
    private BackFourAppRepo(){}

    // internal interface for repo execution
    static interface ExecuteRepoCallTask {

        // async method to execute
        @WorkerThread
        public abstract RepoCallbackResult executeRepo();
    }

    // internal interface for repo callback
    static interface RepoCallbackHandler{

        // merged, main-thread execution handler
        @MainThread
        public void repoCallback(@NonNull final HashMap<String, Boolean> operationResultHolder, @Nullable final DataAccess dataAccess, @Nullable final List<DataAccess> dataAccessList, @NonNull final AdapterMethodType adapterMethodType, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate);

    }

    // initializes repo with app, client, url keys
    @MainThread
    public void setParseInitialize(@NonNull Context context){

        // inits parse by passing application context
        Parse.initialize(new Parse.Configuration.Builder(context)
                .applicationId(BackFourAppRepo.appId)
                .clientKey(BackFourAppRepo.clientId)
                .server(BackFourAppRepo.backFourAppURL).build());

    }

    // instigates async process for repo result, passing custom Runnable into thread task
    public void instigateAsyncRepoTask(@NonNull final ExecuteRepoCallTask repoCallback, @NonNull final RepoCallbackHandler delegate){}

    // local, static to execute repo operations
}
