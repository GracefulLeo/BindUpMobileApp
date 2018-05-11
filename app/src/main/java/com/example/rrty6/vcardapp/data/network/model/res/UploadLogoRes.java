package com.example.rrty6.vcardapp.data.network.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadLogoRes {

    @SerializedName("fid")
    @Expose
    public String fid;

    @SerializedName("uri")
    @Expose
    public String uri;

    public String getFid() {
        return fid;
    }
}
