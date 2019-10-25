package com.example.smartshopper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);
        errorTV = findViewById(R.id.errorTV);
        itemEntered = findViewById(R.id.itemNameET);
    }

    public void itemSearch(View v){
        if(itemEntered.getText().toString().isEmpty()){
            errorTV.setText("This field cannot be blank");
        }
        else if(itemEntered.getText().toString().length() < 4){
            errorTV.setText("Invalid input");
        }

        List<Commodity> commodityList = findItemByUserText(fake);

        if(commodityList.size() == 0){
            errorTV.setText("Be more general with your search");
        }
        else if(commodityList.size() > 3){
            errorTV.setText("Be more specific with your search");
        }

        if(commodityList.size() == 1){
            this.returnedItemIntent(commodityList.get(0));
        }
        else if(commodityList.size() >= 2 && commodityList.size() <= 3){

        }
        // else here- 2-3
        // pass list into recycler view for user selection


    }

    public ArrayList<Commodity> findItemByUserText(String item){
        int number = rando.nextInt(3);

        itemListReturn0.add(item1);
        itemListReturn0.add(item2);
        itemListReturn0.add(item3);
        itemListReturn0.add(item4);
        itemListReturn0.add(item5);

        itemListReturn1.add(item2);
        itemListReturn1.add(item4);
        itemListReturn1.add(item5);

        if(number == 0){
            return itemListReturn0;
        }
        else if(number == 1){
            return itemListReturn1;
        }
        else
            return itemListReturn2;

    }

    public void returnedItemIntent(Commodity commodity){
        Intent itemReturned = new Intent (this, StoreScreenActivity.class);

        itemReturned.putExtra("commodity", commodity);

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
