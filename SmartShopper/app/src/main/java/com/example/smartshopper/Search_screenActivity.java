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
import java.util.Random;

public class Search_screenActivity extends AppCompatActivity implements tabBarFragment.TabBarAction, SearchResultHandler{

    Random rando = new Random();
    TextView errorTV;
    EditText itemEntered;
    String fake;

    RecyclerView recyclerView;
    private GestureDetectorCompat detector = null;

    Search_Screen_RecyclerView_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);
        errorTV = findViewById(R.id.errorTV);
        itemEntered = findViewById(R.id.itemNameET);

        this.recyclerView = (RecyclerView)super.findViewById(R.id.itemRecyclerView);
        this.recyclerView.setVisibility(View.INVISIBLE);


        // Make a Listener for taps
        detector = new GestureDetectorCompat(this, new RecyclerViewOnGestureListenerForSearchMultiSelection(this));
        // add the listener to the recycler
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e){
                return detector.onTouchEvent(e);
            }
        });
    }

    @Override
    public void onTabBarItemClicked(@NonNull final Class<?> classToIntentTo){
        if(!this.getClass().equals(classToIntentTo))
        this.startActivity(new Intent(this, classToIntentTo));
    }

    // TODO: check if both bools are true ( check list size > 1 -- if so, create adapter and set to RV + notify data set change )
    // TODO: if 1st bool false (tell user to try again), 1st true and 2nd false ( tell user to refine search )
    @Override
    public void searchCB(boolean operationSuccess, boolean searchGood, List<Commodity> commodityList){

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
                    // TODO: extract item from list displayed
                    controller.returnedItemIntent(null);

                    return true; // Use up the tap gesture
                }
            }
            // we didn't handle the gesture so pass it on
            return false;
        }
    }

    // TODO: extract string value, assert length > 3, invoke model method giving string in
    public void itemSearch(View v){
        itemEntered = findViewById(R.id.itemNameET);
        final String searchPhrase = String.valueOf(itemEntered.getText());

        if(searchPhrase.length() > 3){

        }
    }



    public void returnedItemIntent(Commodity commodity){
        Intent itemReturned = new Intent (this, StoreScreenActivity.class);
        itemReturned.putExtra(this.getString(R.string.itemLookupExtraKey), commodity);
        //itemReturned.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(itemReturned);

    }


}
