package com.example.smartshopper;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Search_screenActivity extends AppCompatActivity {
    TextView errorTV;
    EditText itemEntered;

    Store store1 = new Store("Maryville", "Hyvee");
    Store store2 = new Store("Maryville", "Walmart");

    Department produce = new Department(DepartmentType.produce, true, 1, 7, store1);
    Department grocery = new Department(DepartmentType.grocery, true, 1, 20, store2);

    Commodity item1 = new Commodity("0001", "Apple", "Red Delicious", 0.99, true, Location.produceDepartmentLeftMostAisle, produce);
    Commodity item2 = new Commodity("0002", "Orange", "Sunkist", 0.89, true, Location.produceDepartmentLeftMostAisle, produce);
    Commodity item3 = new Commodity("0003", "Baked beans", "Bush's", 1.00, true, Location.aisleOneLeft, grocery);
    Commodity item4 = new Commodity("0004", "24pk water", "Ice Mountain", 2.89, true, Location.aisleFourRight, grocery);
    Commodity item5 = new Commodity("0005", "2lb sugar", "C & H", 2.49, true, Location.aisleFiveLeft, grocery);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);
        errorTV = findViewById(R.id.departmentTV);
        itemEntered = findViewById(R.id.itemNameET);
    }

    public void itemSearch(View v){
        if(itemEntered.equals(null)){
            errorTV.setText("");
        }
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
