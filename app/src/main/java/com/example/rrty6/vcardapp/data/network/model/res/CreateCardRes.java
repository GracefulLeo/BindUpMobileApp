package com.example.rrty6.vcardapp.data.network.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateCardRes {

    @SerializedName("nid")
    @Expose
    private String nid;
    @SerializedName("uri")
    @Expose
    private String uri;

    public String getNid() {
        return nid;
    }
}
