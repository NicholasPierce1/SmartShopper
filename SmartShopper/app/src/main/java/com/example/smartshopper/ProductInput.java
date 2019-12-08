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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


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
    EditText barCodeET, nameET, vendorET, priceET, tagsET;
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

    // private member to enjoin deptSpinner to update selection w/ saved value
    private boolean shouldUpdateAisleSelection = false;
    private int updatedAisleSelectionToModifyDepartment = -1;

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

            // then use department to get list of locations, set the instance member that holds the location list
            this.currentLocationsForSelectedDepartment = this.deptToLocationMappings.get(deptSelected);

            // and then set that list to array adapter two for spinner two
            List<String>locationNames = new ArrayList<>();
            for(Location loc: currentLocationsForSelectedDepartment){
                locationNames.add(loc.name());
            }
            ArrayAdapter<String> locationNameArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, locationNames );
            locationNameArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            this.isleSPNR.setAdapter(locationNameArrayAdapter);
            this.isleSPNR.setOnItemSelectedListener(this);

            // checks if update is required in newly created adapter -- if so then set selection
            if(this.shouldUpdateAisleSelection)
                this.isleSPNR.setSelection(this.updatedAisleSelectionToModifyDepartment);

            // resets flag for update instruction
            this.shouldUpdateAisleSelection = false;
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

        // declares internal hashmap to hold dept type pairings to department
        final HashMap<DepartmentType, Department> departmentTypeDepartmentMappings = new HashMap<DepartmentType,Department>();

        // creates internal mappings by walking through all departments, putting the department type and department entries in
        for(Department department: departmentList)
            departmentTypeDepartmentMappings.put(department.type, department);

        // renders mappings

        // asserts grocery department is not null/ exist
        final Department groceryDept = departmentTypeDepartmentMappings.get(DepartmentType.grocery);
        assert(groceryDept != null);

        // produce dept mappings
        this.deptToLocationMappings.put(departmentTypeDepartmentMappings.get(DepartmentType.produce), Arrays.asList(Location.produceDepartmentLeftMostAisle, Location.produceDepartmentLeftMostDisplay, Location.produceDepartmentRightMostDisplay, Location.produceDepartmentTopMostAisle) );

        // grocery dept mappings
        this.deptToLocationMappings.put(departmentTypeDepartmentMappings.get(DepartmentType.grocery), this.getLocationsFromGroceryDepartment(groceryDept));

        // bakery dept mappings
        this.deptToLocationMappings.put(departmentTypeDepartmentMappings.get(DepartmentType.bakery), Collections.singletonList(Location.bakeryDepartment));

        // dairy dept mappings
        this.deptToLocationMappings.put(departmentTypeDepartmentMappings.get(DepartmentType.dairy),  Collections.singletonList(Location.dairyDepartment));

        // deli dept mappings
        this.deptToLocationMappings.put(departmentTypeDepartmentMappings.get(DepartmentType.deli), Collections.singletonList(Location.deliDepartment));

        // floral dept mappings
        this.deptToLocationMappings.put(departmentTypeDepartmentMappings.get(DepartmentType.floral), Collections.singletonList(Location.floralDepartment));

        // frozen dept mappings
        this.deptToLocationMappings.put(departmentTypeDepartmentMappings.get(DepartmentType.frozen),  Collections.singletonList(Location.frozenDepartment));

        // meat dept mappings
        this.deptToLocationMappings.put(departmentTypeDepartmentMappings.get(DepartmentType.meat),  Collections.singletonList(Location.meatDepartment));

        // seafood seafood mappings
        this.deptToLocationMappings.put(departmentTypeDepartmentMappings.get(DepartmentType.seafood),  Collections.singletonList(Location.seafoodDepartment));

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

        myActivity = (buttonInput) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
                deptSPNR.setEnabled(true);
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
        nameET.setEnabled(true);
        nameET.setText("");
        vendorET.setEnabled(true);
        vendorET.setText("");
        isleSPNR.setEnabled(true);
        priceET.setEnabled(true);
        priceET.setText("");
        tagsET.setEnabled(true);
        tagsET.setText("");
        deptSPNR.setEnabled(true);
    }
    public void lockAll(){
        deptSPNR.setEnabled(false);
        isleSPNR.setEnabled(false);
        nameET.setEnabled(false);
        vendorET.setEnabled(false);
        priceET.setEnabled(false);
        tagsET.setEnabled(false);
    }
    public void lockNameaAndVendorAndUnlockRest(){
        isleSPNR.setEnabled(true);
        deptSPNR.setEnabled(false);
        nameET.setEnabled(false);
        vendorET.setEnabled(false);
        priceET.setEnabled(true);
        priceET.setText("");
        tagsET.setEnabled(true);
        tagsET.setText("");
    }
    public void displayAll(Commodity c){
        //Don't forget department
        nameET.setText(""+c.name);
        vendorET.setText(""+ c.vendorName);

        // sets flag and current int value of location for modification of aisle spinner's adapter
        this.shouldUpdateAisleSelection = true;
        this.updatedAisleSelectionToModifyDepartment = this.deptToLocationMappings.get(c.department).indexOf(c.location);

        deptSPNR.setSelection(deptNamesForSpinnerOneAdapter.indexOf(c.department.type.name()));

        priceET.setText("" + c.price);
        tagsET.setText(""+ c.searchPhrase);
    }
    private Bundle retrive(){
        //This is for the controller later. Creation of item.
        Bundle b = new Bundle();
        b.putBoolean("isSafe", false);
        try {
            b.putString("name", nameET.getText().toString());
            b.putString("vendor", vendorET.getText().toString());
            b.putSerializable("isle", currentLocationSelection);
            b.putSerializable("dept", deptSelected);
            b.putDouble("price", Double.parseDouble(priceET.getText().toString()));
            b.putString("tags", tagsET.getText().toString());
            b.putBoolean("isSafe", true);

        }
        catch (NumberFormatException e){
            Toast.makeText(getContext(), "Price is invalid or blank", Toast.LENGTH_LONG);
        }
        catch (NullPointerException e){
            Toast.makeText(getContext(), "Fields are empty", Toast.LENGTH_LONG).show();
        }
        return b;
    }
    private Bundle setToComodity(){
        //For Updation (catchNotAWordException)
        Bundle b = new Bundle();
        b.putBoolean("isSafe", false);
        try {
            c.department = deptSelected;
            c.location = currentLocationSelection;
            c.price = Double.parseDouble(priceET.getText().toString());
            c.searchPhrase = tagsET.getText().toString();
            b.putSerializable("C", c);
            b.putBoolean("isSafe", true);
        }
        catch (NumberFormatException e){
            Toast.makeText(getContext(), "Price is invalid or blank", Toast.LENGTH_LONG).show();

        }
        catch (NullPointerException e){
            Toast.makeText(getContext(), "Fields are empty", Toast.LENGTH_LONG).show();
        }
        return b;
    }

}