package com.example.smartshopper;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public interface buttonInput{
        //NICK
        public void submitButtonPushed(int actionCode, Commodity commodity);
        public void cancelButtonPushed();
    }
    private buttonInput myActivity = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProductInput() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * param param1 Parameter 1.
     * param param2 Parameter 2.
     * @return A new instance of fragment ProductInput.
     */
    // TODO: Rename and change types and number of parameters
//    public static ProductInput newInstance(String param1, String param2) {
//        ProductInput fragment = new ProductInput();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

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
        vendorET = v.findViewById(R.id.DeptET);
        deptET = v.findViewById(R.id.DeptET);
        isleET = v.findViewById(R.id.IsleET);
        priceET = v.findViewById(R.id.priceET);
        tagsTV = v.findViewById(R.id.tagsTV);
        tagsET = v.findViewById(R.id.tagsET);
        submitBTN = v.findViewById(R.id.pSubmitBTN);
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NICK

                //Barcode -> callback gives commidity -> to controller //FOR UPDATE
//             myActivity.submitButtonPushed(AdminProductScreenActivity.submitCode, new Commodity(null, null, null
//             ,null, null, null, null));
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
}
