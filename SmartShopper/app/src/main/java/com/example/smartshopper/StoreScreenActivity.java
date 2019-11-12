package com.example.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.AnyThread;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreScreenActivity extends AppCompatActivity {

    // enumerates local member state

    // holds all locations pertaining to the store's schematic: textviews are found via departments of searched commodity
    // from the search commodity, the location is referenced to acquire the text view
    private HashMap<DepartmentType, HashMap<Location, TextView>> departmentToTextViewInfoReference;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.store_screen);

        // initializes view references by location and department
        this.initializeDepartmentToTextViewInfoReference();

        // refreshes/ initializes store schematic view from intent
        this.initializeSchematicView();

    }

    // initializes mappings between location and text view
    @NonNull
    @AnyThread
    private List<Pair<DepartmentType,HashMap<Location, TextView>>> initializeInternalHashmapMappings(){

        // creates local list to hold the department type hashmap pairs
        final List<Pair<DepartmentType, HashMap<Location, TextView>>> innerListPairings = new ArrayList<Pair<DepartmentType, HashMap<Location, TextView>>>();

        // local counter for item walk through all departments
        int deptCounter = 1;

        // constructs while loop to walk through all department
        while(deptCounter < DepartmentType.getDepartmentCount() + 1) {

            // creates local hash map reference
            final HashMap<Location, TextView> innerHashMap = new HashMap<Location, TextView>();

            // creates department type from id
            DepartmentType departmentType = DepartmentType.getDepartmentTypeFromID(deptCounter);

            // asserts not null
            assert(departmentType != null);

            // employs switch to walk through all cases
            switch(departmentType) {

                case grocery:
                    // grocery aisle 10
                    innerHashMap.put(Location.aisleTenRight, (TextView) super.findViewById(R.id.groceryDepartmentA10RightTV));

                    // grocery aisle 9
                    innerHashMap.put(Location.aisleNineLeft, (TextView) super.findViewById(R.id.groceryDepartmentA9LeftTV));
                    innerHashMap.put(Location.aisleNineRight, (TextView) super.findViewById(R.id.groceryDepartmentA9RightTV));

                    // grocery aisle 8
                    innerHashMap.put(Location.aisleEightLeft, (TextView) super.findViewById(R.id.groceryDepartmentA8LeftTV));
                    innerHashMap.put(Location.aisleEightRight, (TextView) super.findViewById(R.id.groceryDepartmentA8RightTV));

                    // grocery aisle 7
                    innerHashMap.put(Location.aisleSevenLeft, (TextView) super.findViewById(R.id.groceryDepartmentA7LeftTV));
                    innerHashMap.put(Location.aisleSevenRight, (TextView) super.findViewById(R.id.groceryDepartmentA7RightTV));

                    // grocery aisle 6
                    innerHashMap.put(Location.aisleSixLeft, (TextView) super.findViewById(R.id.groceryDepartmentA6LeftTV));
                    innerHashMap.put(Location.aisleSixRight, (TextView) super.findViewById(R.id.groceryDepartmentA6RightTV));

                    // grocery aisle 5
                    innerHashMap.put(Location.aisleFiveLeft, (TextView) super.findViewById(R.id.groceryDepartmentA5LeftTV));
                    innerHashMap.put(Location.aisleFiveRight, (TextView) super.findViewById(R.id.groceryDepartmentA5RightTV));

                    // grocery aisle 4
                    innerHashMap.put(Location.aisleFourLeft, (TextView) super.findViewById(R.id.groceryDepartmentA4LeftTV));
                    innerHashMap.put(Location.aisleFourRight, (TextView) super.findViewById(R.id.groceryDepartmentA4RightTV));

                    // grocery aisle 3
                    innerHashMap.put(Location.aisleThreeLeft, (TextView) super.findViewById(R.id.groceryDepartmentA3LeftTV));
                    innerHashMap.put(Location.aisleThreeRight, (TextView) super.findViewById(R.id.groceryDepartmentA3RightTV));

                    // grocery aisle 2
                    innerHashMap.put(Location.aisleTwoLeft, (TextView) super.findViewById(R.id.groceryDepartmentA2LeftTV));
                    innerHashMap.put(Location.aisleTwoRight, (TextView) super.findViewById(R.id.groceryDepartmentA2RightTV));

                    // grocery aisle 1
                    innerHashMap.put(Location.aisleOneLeft, (TextView) super.findViewById(R.id.groceryDepartmentA1LeftTV));
                    innerHashMap.put(Location.aisleOneRight, (TextView) super.findViewById(R.id.groceryDepartmentA1RightTV));

                    // creates pair object and adds to list
                    innerListPairings.add(new Pair<DepartmentType, HashMap<Location, TextView>>(departmentType, innerHashMap));
                    break;

                case produce:

                    // appends all sub locations in hashmap
                    innerHashMap.put(Location.produceDepartmentLeftMostAisle, (TextView)super.findViewById(R.id.produceDepartmentLeftMostAisleTV));
                    innerHashMap.put(Location.produceDepartmentLeftMostDisplay, (TextView)super.findViewById(R.id.produceDepartmentLeftMostDisplayTV));
                    innerHashMap.put(Location.produceDepartmentTopMostAisle, (TextView)super.findViewById(R.id.produceDepartmentTopMostAisleTV));
                    innerHashMap.put(Location.produceDepartmentRightMostDisplay, (TextView)super.findViewById(R.id.produceDepartmentRightMostDisplayTV));

                    // creates pair object and adds to list
                    innerListPairings.add(new Pair<DepartmentType, HashMap<Location, TextView>>(departmentType, innerHashMap));
                    break;

                case floral:
                    innerHashMap.put(Location.floralDepartment, (TextView)super.findViewById(R.id.floralDepartmentTV));
                    // creates pair object and adds to list
                    innerListPairings.add(new Pair<DepartmentType, HashMap<Location, TextView>>(departmentType, innerHashMap));
                    break;

                case deli:
                    innerHashMap.put(Location.deliDepartment, (TextView)super.findViewById(R.id.deliDepartmentTV));
                    // creates pair object and adds to list
                    innerListPairings.add(new Pair<DepartmentType, HashMap<Location, TextView>>(departmentType, innerHashMap));
                    break;

                case bakery:
                    innerHashMap.put(Location.bakeryDepartment, (TextView)super.findViewById(R.id.bakeryDepartmentTV));
                    // creates pair object and adds to list
                    innerListPairings.add(new Pair<DepartmentType, HashMap<Location, TextView>>(departmentType, innerHashMap));
                    break;

                case meat:
                    innerHashMap.put(Location.meatDepartment, (TextView)super.findViewById(R.id.meatDepartmentTV));
                    // creates pair object and adds to list
                    innerListPairings.add(new Pair<DepartmentType, HashMap<Location, TextView>>(departmentType, innerHashMap));
                    break;

                case seafood:
                    innerHashMap.put(Location.seafoodDepartment, (TextView)super.findViewById(R.id.seafoodDepartmentTV));
                    // creates pair object and adds to list
                    innerListPairings.add(new Pair<DepartmentType, HashMap<Location, TextView>>(departmentType, innerHashMap));
                    break;

                case dairy:
                    innerHashMap.put(Location.dairyDepartment, (TextView)super.findViewById(R.id.dairyDepartmentTV));
                    // creates pair object and adds to list
                    innerListPairings.add(new Pair<DepartmentType, HashMap<Location, TextView>>(departmentType, innerHashMap));
                    break;

                case frozen:
                    innerHashMap.put(Location.frozenDepartment, (TextView)super.findViewById(R.id.frozenDepartmentTV));
                    // creates pair object and adds to list
                    innerListPairings.add(new Pair<DepartmentType, HashMap<Location, TextView>>(departmentType, innerHashMap));
                    break;
            }

            // increments counter
            deptCounter++;
        }

        return innerListPairings;
    }

    @MainThread
    // uses inner department type - hashmap pairing to initialize controller's departmentToTextViewInfoReference
    private void initializeDepartmentToTextViewInfoReference(){

        // acquires local inner mappings
        final List<Pair<DepartmentType, HashMap<Location, TextView>>> innerMappings = this.initializeInternalHashmapMappings();

        // initializes hashmap for controller
        this.departmentToTextViewInfoReference = new HashMap<DepartmentType, HashMap<Location, TextView>>();

        // walks through list and appends members to hashmap
        for(Pair<DepartmentType, HashMap<Location, TextView>> pairMapping: innerMappings){
            this.departmentToTextViewInfoReference.put(pairMapping.first, pairMapping.second);
        }
    }

    //takes intent and checks bundle, then conditionally displays location of item per dept and location
    @MainThread
    private void initializeSchematicView(){

        // acquires pair from intent
        final Pair<Boolean, Commodity> intentPair = this.needsToRefreshTextViewFromIntent();

        // if true then refresh schematic view - else turn on all views
        if(intentPair.first){

            // turn offs all text views
            this.turnOnOrOffAllSchematicViews(false);

            // turns on text view w/ corresponding commodity state endpoint
            this.turnStoreSchematicTextViewOnFromCommodity(intentPair.second);

        }
        else{
            // initial creation OR tab bar creation
            this.turnOnOrOffAllSchematicViews(true);
        }

    }

    // turns on or off all views within schematic
    @MainThread
    private void turnOnOrOffAllSchematicViews(boolean turnOn){

        // walks through all inner hashmaps and textviews and sets visibility varying on turnOn param
        for(HashMap<Location, TextView> innerHashMap: this.departmentToTextViewInfoReference.values()) {
            for (TextView schematicTextView : innerHashMap.values())
                schematicTextView.setVisibility(turnOn ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @MainThread
    private void turnStoreSchematicTextViewOnFromCommodity(@NonNull final Commodity commodity){
        // turns textview on per commodity's department type and location
        this.departmentToTextViewInfoReference.get(commodity.department.type).get(commodity.location).setVisibility(View.VISIBLE);

    }

    // checks bundle and returns if intent that instigated activity is from search AND has data
    @NonNull
    @MainThread
    private Pair<Boolean, Commodity> needsToRefreshTextViewFromIntent(){

        // acquires commodity (null default)
        Commodity commodity = (Commodity)this.getIntent().getSerializableExtra(this.getString(R.string.itemLookupExtraKey));

        return new Pair<Boolean, Commodity>(commodity != null, commodity);

    }

    // configures the "emulated tab bar intents" to insert functionality into tabs

    // invokes by third image button on tab application, intents to search activity and preserves stack (w/o re-ordering)
    public void searchIntent(View v){
        Intent searchIntent = new Intent(this, Search_screenActivity.class);
        startActivity(searchIntent);
    }

    // invokes by first image button on tab application, intents to welcome and preserves stack (w/o re-ordering)
    public void welcomeScreenIntent(View v){
        Intent searchIntent = new Intent(this, Welcome_screenActivity.class);
        startActivity(searchIntent);
    }

    // current context- no action performed
    public void storeScreenIntent(View v){
        return;
    }

    public void onTabBarItemClicked(@NonNull final Class<?> classToIntentTo){
        if(!this.getClass().equals(classToIntentTo))
            this.startActivity(new Intent(this, classToIntentTo));
    }

    @Override
    public void onRestart(){
        super.onRestart();

        //this.initializeSchematicView();
    }
}
