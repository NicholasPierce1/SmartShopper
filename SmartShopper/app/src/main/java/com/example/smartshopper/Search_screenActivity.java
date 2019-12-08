package com.example.smartshopper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Search_screenActivity extends AppCompatActivity implements tabBarFragment.TabBarAction, SearchResultHandler{


    TextView errorTV;
    EditText itemEntered;

    // instance member to hold commodity list
    private List<Commodity> commodityList = new ArrayList<Commodity>();

    RecyclerView recyclerView;
    private GestureDetectorCompat detector = null;

    Search_Screen_RecyclerView_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);
        errorTV = findViewById(R.id.errorTV);
        itemEntered = findViewById(R.id.itemNameET);


        // acquires rv and assigns to member ref
        this.recyclerView = (RecyclerView)super.findViewById(R.id.itemRecyclerView);

        // Make a Listener for taps
        detector = new GestureDetectorCompat(this, new RecyclerViewOnGestureListenerForSearchMultiSelection(this));
        // add the listener to the recycler
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e){
                return detector.onTouchEvent(e);
            }
        });

        // renders adapter with def/initial, empty commodity list
        this.adapter = new Search_Screen_RecyclerView_Adapter(commodityList);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.recyclerView.setVisibility(View.INVISIBLE);



    }

    @Override
    public void onTabBarItemClicked(@NonNull final Class<?> classToIntentTo){
        if(!this.getClass().equals(classToIntentTo))
        this.startActivity(new Intent(this, classToIntentTo));
    }


    @Override
    public void searchCB(boolean operationSuccess, boolean searchGood, boolean wasSearchTooShort, List<Commodity> commodityList){

        Log.d("test bool search", "search good: ".concat(String.valueOf(searchGood)) + " operation success: ".concat(String.valueOf(operationSuccess)));
        // if search was too short- or if operation succeeded, but no results were yielded- apprise user of faulty case
        if(wasSearchTooShort || (operationSuccess && !searchGood)) {
            Log.d("test bool search", "!!!");
            this.updateResultTextViewWithString("Please refine search.");
            return;
        }
        else if(!operationSuccess){
            this.updateResultTextViewWithString("Error in search. Please try again.");
            return;
        }

        // items within range of [1-3] acquired, create adapter, set to RV, propagate update to adapter
        assert(commodityList != null);

        // on success- update instance member and notify data set change
        for(Commodity commodity: commodityList)
            this.commodityList.add(commodity); // done this way to ensure success and not alter ref's address in adapter

        this.adapter.notifyDataSetChanged();

        // set visibility
        this.recyclerView.setVisibility(View.VISIBLE);
    }

    private class RecyclerViewOnGestureListenerForSearchMultiSelection extends GestureDetector.SimpleOnGestureListener {

        Search_screenActivity controller;

        RecyclerViewOnGestureListenerForSearchMultiSelection(@NonNull final Search_screenActivity search_screenActivity){
            this.controller = search_screenActivity;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (view != null) {
                RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
                if (holder instanceof Search_Screen_RecyclerView_Adapter.SearchScreenViewHolder) {
                    int position = holder.getAdapterPosition();

                    // acquire item from position and invoke intent w/ clear top

                    controller.returnedItemIntent(controller.adapter.getAdapterDataRef().get(position));

                    return true; // Use up the tap gesture
                }
            }
            // we didn't handle the gesture so pass it on
            return false;
        }
    }


    // acquires search, converts to lowercase, and instigates model search process
    public void itemSearch(View v){
        itemEntered = findViewById(R.id.itemNameET);
        final String searchPhrase = String.valueOf(itemEntered.getText()).toLowerCase();

        Model.getShared().searchCommoditiesBySearchPhrase(searchPhrase, this);

        // hides prior search result
        this.errorTV.setText("");
        this.recyclerView.setVisibility(View.INVISIBLE);
    }



    public void returnedItemIntent(Commodity commodity){
        Intent itemReturned = new Intent (this, StoreScreenActivity.class);
        itemReturned.putExtra(this.getString(R.string.itemLookupExtraKey), commodity);
        //itemReturned.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(itemReturned);

    }

    private void updateResultTextViewWithString(@NonNull final String textViewVale){
        this.errorTV.setText(textViewVale);
    }


}
