package com.example.smartshopper;

import android.graphics.ColorSpace;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

import java.util.List;

public class Search_Screen_RecyclerView_Adapter extends RecyclerView.Adapter<Search_Screen_RecyclerView_Adapter.SearchScreenViewHolder> {

    private List<Commodity> commodityList;

    public Search_Screen_RecyclerView_Adapter(@NonNull final List<Commodity> commodityList){
        this.commodityList = commodityList;
    }

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

    public void onBindViewHolder(SearchScreenViewHolder vh, int which){
        TextView itemName = vh.itemView.findViewById(R.id.itemRVTV);
        TextView departmentName = vh.itemView.findViewById(R.id.departmentRVTV);
        itemName.setText(commodityList.get(which).name);
        departmentName.setText(commodityList.get(which).department.type.name());
    }

    public int getItemCount(){
        Log.d("item count size", String.valueOf(this.commodityList.size()));
        return this.commodityList.size();
    }

    public List<Commodity> getAdapterDataRef(){
        return this.commodityList;
    }

}
