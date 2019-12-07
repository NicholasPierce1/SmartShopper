package com.example.smartshopper;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.MissingResourceException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductInput.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {link ProductInput#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductInput extends Fragment implements AdapterView.OnItemSelectedListener{
    // TODO: 11/27/2019 Get rid of isleET code and deptET code
    TextView nameTV, vendorTV, deptTV, isleTV, priceTV, resultTV, tagsTV;
    EditText barCodeET, nameET, vendorET,  isleET, priceET, tagsET;
    Button cancleBTN, submitBTN;
    Commodity c;
    int submitCode;
    Spinner deptSPNR, isleSPNR;
    Model m = Model.getShared();
    Department deptSelected;

    // private member to retains converted string list
    private List<String> deptNamesForSpinnerOneAdapter = null;

    // private member to retain mappings of department to location/s
    private HashMap<Department, List<Location>> deptToLocationMappings = new HashMap<Department, List<Location>>();

    // private member to retain a current location for spinner two
    private Location currentLocationSelection = null;

    // private member to hold the list of the current depatment's locations
    private List<Location> currentLocationsForSelectedDepartment = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public interface buttonInput{
        public void submitButtonPushed(int actionCode, boolean check, Bundle commodity);
        public void cancelButtonPushed();

    }
    private buttonInput myActivity = null;

    // TODO: Rename and change types of parameters
    private int createCase;

    //private OnFragmentInteractionListener mListener;

    public ProductInput() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_product_input, container, false);
        nameTV = v.findViewById(R.id.NameTV);
        vendorTV = v.findViewById(R.id.VendorTV);
        deptTV = v.findViewById(R.id.DeptTV);
        isleTV = v.findViewById(R.id.islieTV);
        priceTV = v.findViewById(R.id.priceTV);
        resultTV = v.findViewById(R.id.ResultTV);
        barCodeET = v.findViewById(R.id.BarcodeET);
        nameET = v.findViewById(R.id.NameET);
        vendorET = v.findViewById(R.id.VendorET);
        priceET = v.findViewById(R.id.priceET);
        tagsTV = v.findViewById(R.id.tagsTV);
        tagsET = v.findViewById(R.id.tagsET);
        deptSPNR = v.findViewById(R.id.deptSPNR);
        deptSPNR.setOnItemSelectedListener(this);
        isleSPNR = v.findViewById(R.id.isleSPNR);
        isleSPNR.setOnItemSelectedListener(this);
        submitBTN = v.findViewById(R.id.pSubmitBTN);

        // initializes department mapping's
        this.initializeDepartmentToLocationMappings(this.m.getDeptList());

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //We are either creating a comodity or changing. We will pass in the things we are doing
                //as a bundle to make life easy
               boolean check = createCase == 3; //In the event of  create case
                if(submitCode == 1){ //creation
                    myActivity.submitButtonPushed(1, check, retrive());
                }
                else if(submitCode == 2){
                    myActivity.submitButtonPushed( 2, false,setToComodity());
                }
                else if(submitCode == 3){
                    myActivity.submitButtonPushed(3, false,setToComodity());
                }
                else{ Log.d("Broke", "Stuffs' broke, submit onClckListener unexpected value of " +
                        submitCode);}
            }
        });
        cancleBTN = v.findViewById(R.id.pCancelBTN);
        cancleBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myActivity.cancelButtonPushed();
            }
        });
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId()==deptSPNR.getId()){

            // use selected index value (i) to get dept name and invoke helper method to get department
            String localDeptName = deptNamesForSpinnerOneAdapter.get(i);
            deptSelected = getDepartmentFromDepartmentNameSelection(m.getDeptList(),localDeptName);
            Log.d("Product", localDeptName);
            Log.d("Product", deptSelected.type.name());
            // then use department to get list of locations, set the instance member that holds the location list
            this.currentLocationsForSelectedDepartment = this.deptToLocationMappings.get(deptSelected);

            // and then set that list to array adapter two for spinner two
            List<String>locationNames = new ArrayList<>();
            for(Location loc: currentLocationsForSelectedDepartment){
                locationNames.add(loc.name());
            }
            final ArrayAdapter<String> locationNameArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, locationNames );
            locationNameArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            this.isleSPNR.setAdapter(locationNameArrayAdapter);
            this.isleSPNR.setOnItemSelectedListener(this);

            return;
        }
        else{
            //  LOCATION SPINNER
            // use selection index (i) to acquire current location from instance member that holds location list
            // then set current location
           this.currentLocationSelection = currentLocationsForSelectedDepartment.get(i);

        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // utilized to render/ initialize the mappings of the department to its locations
    private void initializeDepartmentToLocationMappings(@NonNull final List<Department> departmentList){
        // TODO: create hashmap on type key value to department instance itself then put all mappings in
        // declares departments for grocery and produce
        Department groceryDept = null;
        Department produceDept = null;

        // acquires grocery and produce instance and sets local instance
        for(Department department: departmentList){

            // acquires grocery department
            if(department.type.equals(DepartmentType.grocery))
                groceryDept = department;

            // acquires and sets produce
            else if(department.type.equals(DepartmentType.produce))
                produceDept = department;

        }

        // assert vars are not null
        if(groceryDept == null || produceDept == null)
            throw new RuntimeException("no department for grocery or produce");

        // renders mappings

        // produce dept mappings
        this.deptToLocationMappings.put(produceDept, Arrays.asList(Location.produceDepartmentLeftMostAisle, Location.produceDepartmentLeftMostDisplay, Location.produceDepartmentRightMostDisplay, Location.produceDepartmentTopMostAisle) );

        // grocery dept mappings
        this.deptToLocationMappings.put(groceryDept, this.getLocationsFromGroceryDepartment(groceryDept));

        // frozen dept mappings
        this.deptToLocationMappings.put(groceryDept, this.getLocationsFromGroceryDepartment(groceryDept));

        // grocery dept mappings
        this.deptToLocationMappings.put(groceryDept, this.getLocationsFromGroceryDepartment(groceryDept));

        // grocery dept mappings
        this.deptToLocationMappings.put(groceryDept, this.getLocationsFromGroceryDepartment(groceryDept));

    }

    // private method to return list of all mappings of grocery dept
    @NonNull
    private List<Location> getLocationsFromGroceryDepartment(@NonNull final Department groceryDept){

        // creates local list to populate for grocery location list
        final List<Location> groceryDeptList = new ArrayList<Location>();

        // holds current location id number
        int currentLocationIdForAisle = groceryDept.minAisle;

        // walks through all ailses within grocery dept, populating list with location
        while(currentLocationIdForAisle < groceryDept.maxAisle * 2)
            groceryDeptList.add(Location.getLocationFromAisleNumberLocationId(currentLocationIdForAisle++));

        return groceryDeptList;
    }

    // from department name selected, acquire department instance
    @NonNull
    private Department getDepartmentFromDepartmentNameSelection(@NonNull final List<Department> deptList, @NonNull final String deptNameSelectedByUser) throws ExceptionInInitializerError {

        // acquires department type from user selection
        final DepartmentType departmentTypeFromUserSelection = DepartmentType.getDepartmentTypeFromName(deptNameSelectedByUser);

        // asserts dept type not null
        if(departmentTypeFromUserSelection == null)
            throw new ExceptionInInitializerError("User selection does not map to department type selection: ".concat(deptNameSelectedByUser));

        // returns acquired dept from department type
        for(Department department: deptList) {
            if (department.type.equals(departmentTypeFromUserSelection))
                return department;
        }

        // outside of loop, throw exception
        throw new ExceptionInInitializerError("User selection does not map to department selection: ".concat(deptNameSelectedByUser));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
        myActivity = (buttonInput) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void setCreateCase(int createCase){
        this.createCase = createCase;
    }
    public void setSubmitCode(int sub){
        submitCode = sub;
    }
    public void prepareFragmentForPresentation(Commodity c, ArrayList<String> depts) {

        // sets dept name list
        this.deptNamesForSpinnerOneAdapter = depts;

        this.c = c;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,depts );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deptSPNR.setAdapter(arrayAdapter);

        if (submitCode == 1) {//creation
            if (createCase == 1) { //Exisiting product
                lockNameaAndVendorAndUnlockRest();
               nameET.setText(""+c.name);
               vendorET.setText(""+c.vendorName);
            }
            else unlockAll();
        }
        else if(submitCode == 2){
            lockNameaAndVendorAndUnlockRest();
            displayAll(c);
        }
        else if(submitCode == 3){
            lockAll();
            displayAll(c);
        }

    }

    public void unlockAll(){
        nameET.setClickable(true);
        nameET.setText("");
        vendorET.setClickable(true);
        vendorET.setText("");
       isleSPNR.setClickable(true);
        priceET.setClickable(true);
        priceET.setText("");
        tagsET.setClickable(true);
        tagsET.setText("");
        deptSPNR.setClickable(true);
    }
    public void lockAll(){
        deptSPNR.setClickable(false);
        isleSPNR.setClickable(false);
        nameET.setClickable(false);
        vendorET.setClickable(false);
        priceET.setClickable(false);
        tagsET.setClickable(false);
    }
    public void lockNameaAndVendorAndUnlockRest(){
        deptSPNR.setClickable(true);
        isleSPNR.setClickable(true);
        nameET.setClickable(false);
        vendorET.setClickable(false);
        isleET.setClickable(true);
        priceET.setClickable(true);
        priceET.setText("");
        tagsET.setClickable(true);
        tagsET.setText("");
    }
    public void displayAll(Commodity c){
        //Don't forget department
        nameET.setText(""+c.name);
        vendorET.setText(""+ c.vendorName);
        isleET.setText("" +Location.getLocationFromLocationId(c.location.getLocationID()));
        priceET.setText("" + c.price);
        tagsET.setText(""+ c.searchPhrase);
    }
    private Bundle retrive(){
        //This is for the controller later
        Bundle b = new Bundle();
        b.putString("name", nameET.getText().toString());
        b.putString("vendor", vendorET.getText().toString());
        //b.putInt("dept", Integer.parseInt(deptET.getText().toString()));
        b.putInt("isle",Integer.parseInt(isleET.getText().toString()));
        b.putDouble("price", Double.parseDouble(priceET.getText().toString()));
        b.putString("tags", tagsET.getText().toString());
        return b;
    }
    private Bundle setToComodity(){
        //For Updation (catchNotAWordException)
        c.department =deptSelected;

        c.location = currentLocationSelection;
        c.price = Double.parseDouble(priceET.getText().toString());
        c.searchPhrase = tagsET.getText().toString();
        Bundle b = new Bundle();
        b.putSerializable("C", c);
        return b;
    }

}