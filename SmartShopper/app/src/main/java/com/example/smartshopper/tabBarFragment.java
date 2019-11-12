package com.example.smartshopper;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class tabBarFragment extends Fragment{
    private TabBarAction delegate;

    interface TabBarAction{
        @MainThread
        public void onTabBarItemClicked(@NonNull final Class<?> classToIntentTo);
    }


    // on attach
    @Override
    public void onAttach(@NonNull final Context context){
        super.onAttach(context);

        this.delegate = (TabBarAction)context; // SAFE Operation for usage
    }


    public tabBarFragment(){
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        // Inflate the layout for this fragment
        final LinearLayout linearLayout = (LinearLayout)inflater.inflate(R.layout.tabbarfragment, container, false);

        // acquires image button and sets onclick
        ((ImageButton)linearLayout.findViewById(R.id.tabBarWelcomeScreenTab)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(@NonNull final View view){
                onUserClick((ImageButton)view);
            }
        });

        ((ImageButton)linearLayout.findViewById(R.id.tabBarFindItemTab)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(@NonNull final View view){
                onUserClick((ImageButton)view);
            }
        });

        ((ImageButton)linearLayout.findViewById(R.id.tabBarStoreScreenTab)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(@NonNull final View view){
                onUserClick((ImageButton)view);
            }
        });


        return linearLayout;
    }

    public void onUserClick(@NonNull final ImageButton view){
        // local ref to view's id value (int) as well as holding the Class<?> type
        final int viewId = view.getId();
        Class<?> classTypeToIntentOverTo;


        if(viewId == R.id.tabBarWelcomeScreenTab) {
            classTypeToIntentOverTo = Welcome_screenActivity.class;
        }
        else if(viewId == R.id.tabBarFindItemTab){
            classTypeToIntentOverTo = Search_screenActivity.class;
        }
        else
            return;

        // invoke delegate method to commence intent, passes end-target to delegate
        this.delegate.onTabBarItemClicked(classTypeToIntentOverTo);
    }

}
