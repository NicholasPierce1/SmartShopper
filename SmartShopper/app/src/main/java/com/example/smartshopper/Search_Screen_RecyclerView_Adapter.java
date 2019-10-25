package com.example.smartshopper;

import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Search_Screen_RecyclerView_Adapter{


    public static class SearchScreenViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout linearView;

        public SearchScreenViewHolder(LinearLayout v){
            super(v);
            linearView = v;
        }
    }

    @NonNull
    public Search_Screen_RecyclerView_Adapter.SearchScreenViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.search_screen_rv, parent, false);
        SearchScreenViewHolder vh = new SearchScreenViewHolder(v);
        return vh;
    }

}
