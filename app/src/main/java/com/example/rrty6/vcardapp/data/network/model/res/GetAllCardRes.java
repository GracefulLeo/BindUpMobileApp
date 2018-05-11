package com.example.rrty6.vcardapp.data.network.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//Used for getAllCards
public class GetAllCardRes {

    @SerializedName("nid")
    @Expose
    public String nid;

    @SerializedName("uri")
    @Expose
    public String uri;

    public String getNid() {
        return nid;
    }
}
