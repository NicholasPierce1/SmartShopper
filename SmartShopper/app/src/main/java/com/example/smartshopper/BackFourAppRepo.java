package com.example.smartshopper;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

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
    private static final BackFourAppRepo shared = new BackFourAppRepo();

    // private constructor for universal, shared instance
    @MainThread
    private BackFourAppRepo(){}

    // static method to return shared instance
    @NonNull
    @MainThread
    public static BackFourAppRepo getShared(){
        return BackFourAppRepo.shared;
    }

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
        public void repoCallback(@NonNull final HashMap<String, Boolean> operationResultHolder, @Nullable final DataAccess dataAccess, @Nullable final List<? extends DataAccess> dataAccessList, @NonNull final AdapterMethodType adapterMethodType, @NonNull final BrokerCallbackDelegate brokerCallbackDelegate);

    }

    // initializes repo with app, client, url keys
    @MainThread
    public void setParseInitialize(@NonNull Context context){

        // inits parse by passing application context
        Parse.initialize(new Parse.Configuration.Builder(context)
                .applicationId(BackFourAppRepo.appId)
                .clientKey(BackFourAppRepo.clientId)
                .server(BackFourAppRepo.backFourAppURL).build());

        // assures connection has been rendered
        this.parseInitialize = true;

    }

    // asserts connection has been rendered to parse server
    private void assertConnectionToBackFourApp(){
        if(!this.parseInitialize)
            throw new ExceptionInInitializerError("Back4App connection either not set or failed to set.");
    }

    // instigates async process for repo result, passing custom Runnable into thread task
    @MainThread
    public void instigateAsyncRepoTask(@NonNull final ExecuteRepoCallTask repoCallTask, @NonNull final RepoCallbackHandler delegate){

        // corroborates that connection to Back4App is rendered
        this.assertConnectionToBackFourApp();

        // creates and executes async task process for repo operation
        /* HOOT: possible solution -- AsyncTask does not perform checks on generic arguments held by any object-param so I can create a class that has two members
            one for each of the params and create + pass object into async task (of course I modify the first generic arg to AsyncTask)
         */
        //noinspection unchecked
        (new BackFourAppRepo.BackFourAppRepoAsyncTask()).execute(new Pair<ExecuteRepoCallTask, RepoCallbackHandler>(repoCallTask, delegate));
    }

    // local async task definition to execute repo operations
    private static class BackFourAppRepoAsyncTask extends AsyncTask<Pair<ExecuteRepoCallTask, RepoCallbackHandler>, Void, Pair<RepoCallbackResult, RepoCallbackHandler>>{

        @Override
        @WorkerThread
        @NonNull
        @SafeVarargs
        public final Pair<RepoCallbackResult, RepoCallbackHandler> doInBackground(@NonNull final Pair<ExecuteRepoCallTask, RepoCallbackHandler>... repoPair){

            // asserts that only one input, generic arg is passed in
            if(repoPair.length != 1)
                throw new IndexOutOfBoundsException("must pass an ExecutreRepoCallTask and a RepoCallbackHandler comprised in a Pair");

            // effectuates custom runnable for result
            return new Pair<RepoCallbackResult, RepoCallbackHandler>(repoPair[0].first.executeRepo(), repoPair[0].second);
        }

        // invokes callback handler, passing repo callback result
        @Override
        @MainThread
        protected void onPostExecute(@NonNull final Pair<RepoCallbackResult, RepoCallbackHandler> repoCallbackResultPair) {
            super.onPostExecute(repoCallbackResultPair);

            // creates local reference to repo callback result
            final RepoCallbackResult repoCallbackResult = repoCallbackResultPair.first;

            // instigates callback to handler, passing composite data info
            repoCallbackResultPair.second.repoCallback(repoCallbackResult.operationResultHolder,
                    repoCallbackResult.dataAccess,
                    repoCallbackResult.dataAccessList,
                    repoCallbackResult.adapterMethodType,
                    repoCallbackResult.brokerCallbackDelegate);
        }
    }
}
