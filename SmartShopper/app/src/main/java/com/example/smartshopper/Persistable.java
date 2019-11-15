package com.example.smartshopper;


import androidx.annotation.NonNull;

import com.parse.ParseObject;

// supplements the implementation/behavior of converting a read-- acquired from Back4App to a composite DA-- relational data not attached
// also appends functionality to convert a composite OR complete to a parsed object for persistence
public interface Persistable{

    @NonNull
    public abstract ParseObject toParseObject();

}
