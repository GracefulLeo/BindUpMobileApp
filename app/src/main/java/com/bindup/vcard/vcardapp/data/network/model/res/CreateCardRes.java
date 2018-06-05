package com.bindup.vcard.vcardapp.data.network.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateCardRes {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("uri")
    @Expose
    private String uri;

    public String getId() {
        return id;
    }
}
