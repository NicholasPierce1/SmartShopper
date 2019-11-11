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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Search_screenActivity extends AppCompatActivity{
    Random rando = new Random();
    TextView errorTV;
    EditText itemEntered;
    String fake;

    Store store1 = new Store("Maryville", "Hyvee");
    Store store2 = new Store("Maryville", "Walmart");

    Department produce = new Department(DepartmentType.produce, true, 1, 7, store1);
    Department grocery = new Department(DepartmentType.grocery, true, 1, 20, store2);

    Commodity item1 = new Commodity("0001", "Apple", "Red Delicious", 0.99, true, Location.produceDepartmentLeftMostAisle, produce);
    Commodity item2 = new Commodity("0002", "Orange", "Sunkist", 0.89, true, Location.produceDepartmentLeftMostAisle, produce);
    Commodity item3 = new Commodity("0003", "Baked beans", "Bush's", 1.00, true, Location.aisleOneLeft, grocery);
    Commodity item4 = new Commodity("0004", "24pk water", "Ice Mountain", 2.89, true, Location.aisleFourRight, grocery);
    Commodity item5 = new Commodity("0005", "2lb sugar", "C & H", 2.49, true, Location.aisleFiveLeft, grocery);

    ArrayList<Commodity> itemListReturn0 = new ArrayList<>();
    ArrayList<Commodity> itemListReturn1 = new ArrayList<>();
    ArrayList<Commodity> itemListReturn2 = new ArrayList<>();

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

        itemListReturn0.add(item1);
        itemListReturn0.add(item2);
        itemListReturn0.add(item3);
        itemListReturn0.add(item4);
        itemListReturn0.add(item5);

        itemListReturn1.add(item2);
        itemListReturn1.add(item4);
        itemListReturn1.add(item5);

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
                    controller.returnedItemIntent(controller.itemListReturn1.get(position));

                    return true; // Use up the tap gesture
                }
            }
            // we didn't handle the gesture so pass it on
            return false;
        }
    }

    public void itemSearch(View v){
        if(itemEntered.getText().toString().isEmpty()){
            errorTV.setText("This field cannot be blank");
            return;
        }
        else if(itemEntered.getText().toString().length() < 4){
            errorTV.setText("Invalid input");
            return;
        }

        List<Commodity> commodityList = findItemByUserText(fake);
        Log.d("randomNumLength", String.valueOf(commodityList.size()));

        if(commodityList.size() == 0){
            errorTV.setText("Be more general with your search");
            return;
        }
        else if(commodityList.size() > 3){
            errorTV.setText("Be more specific with your search");
            return;
        }

        if(commodityList.size() == 1){
            this.returnedItemIntent(commodityList.get(0));


        }
        // else here- 2-3
        // pass list into recycler view for user selection
        else{
            this.adapter  = new Search_Screen_RecyclerView_Adapter(commodityList);
            this.recyclerView.setAdapter(this.adapter);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            this.recyclerView.setVisibility(View.VISIBLE);
        }

    }

    public ArrayList<Commodity> findItemByUserText(String item){
        int number = rando.nextInt(3);

        Log.d("randomNum", String.valueOf(number));

        if(number == 0){ // all
            return itemListReturn0;
        }
        else if(number == 1){ // three
            return itemListReturn1;
        }
        else // empty
            return itemListReturn2;

    }

    public void returnedItemIntent(Commodity commodity){
        Intent itemReturned = new Intent (this, StoreScreenActivity.class);
        itemReturned.putExtra(this.getString(R.string.itemLookupExtraKey), commodity);
        //itemReturned.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(itemReturned);

    }

    public void welcomeScreenIntent(View v){
        Intent welcomeIntent = new Intent(this, Welcome_screenActivity.class);
        startActivity(welcomeIntent);
    }

    public void storeScreenIntent(View v){
        Intent storeScreen = new Intent(this, StoreScreenActivity.class);
        startActivity(storeScreen);
    }

    public void findItemIntent(View v){
        return;
    }

}
