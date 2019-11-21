package com.example.smartshopper;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseDecoder;
import com.parse.ParseObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductInput.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {link ProductInput#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductInput extends Fragment {
    TextView nameTV, vendorTV, deptTV, isleTV, priceTV, resultTV, tagsTV;
    EditText barCodeET, nameET, vendorET, deptET, isleET, priceET, tagsET;
    Button cancleBTN, submitBTN;
    Commodity c;
    int submitCode;
    Model m = Model.getShared();

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
        deptET = v.findViewById(R.id.DeptET);
        isleET = v.findViewById(R.id.IsleET);
        priceET = v.findViewById(R.id.priceET);
        tagsTV = v.findViewById(R.id.tagsTV);
        tagsET = v.findViewById(R.id.tagsET);
        submitBTN = v.findViewById(R.id.pSubmitBTN);
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
    public void prepareFragmentForPresentation(Commodity c) {
        this.c = c;


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
        deptET.setClickable(true);
        deptET.setText("");
        isleET.setClickable(true);
        isleET.setText("");
        priceET.setClickable(true);
        priceET.setText("");
        tagsET.setClickable(true);
        tagsET.setText("");
    }
    public void lockAll(){
        nameET.setClickable(false);
        vendorET.setClickable(false);
        deptET.setClickable(false);
        isleET.setClickable(false);
        priceET.setClickable(false);
        tagsET.setClickable(false);
    }
    public void lockNameaAndVendorAndUnlockRest(){
        nameET.setClickable(false);
        vendorET.setClickable(false);
        deptET.setClickable(true);
        deptET.setText("");
        isleET.setClickable(true);
        isleET.setText("");
        priceET.setClickable(true);
        priceET.setText("");
        tagsET.setClickable(true);
        tagsET.setText("");
    }
    public void displayAll(Commodity c){
        nameET.setText(""+c.name);
        vendorET.setText(""+ c.vendorName);
        deptET .setText(""+c.department.type.toString());
        isleET.setText("" +Location.getLocationFromLocationId(c.location.getLocationID()));
        priceET.setText("" + c.price);
        tagsET.setText(""+ c.searchPhrase);
    }
    private Bundle retrive(){
        //This is for the controller later
        Bundle b = new Bundle();
        b.putString("name", nameET.getText().toString());
        b.putString("vendor", vendorET.getText().toString());
        b.putInt("dept", Integer.parseInt(deptET.getText().toString()));
        b.putInt("isle",Integer.parseInt(isleET.getText().toString()));
        b.putDouble("price", Double.parseDouble(priceET.getText().toString()));
        b.putString("tags", tagsET.getText().toString());
        return b;
    }
    private Bundle setToComodity(){
        //For Updation (catch NotAWordException)
        c.department =(m.getDepartmentFromDepartmentType(DepartmentType.getDepartmentTypeFromID(
                Integer.parseInt(deptET.getText().toString())
        )));
        c.location = Location.getLocationFromAisleNumber(Integer.parseInt(isleET.getText().toString()));
        c.price = Double.parseDouble(priceET.getText().toString());
        c.searchPhrase = tagsET.getText().toString();
        Bundle b = new Bundle();
        b.putSerializable("C", c);
        return b;
    }

}