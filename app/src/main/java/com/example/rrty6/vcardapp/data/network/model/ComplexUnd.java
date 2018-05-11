package com.example.rrty6.vcardapp.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComplexUnd {

    @SerializedName("value")
    @Expose
    public String value;
    @SerializedName("format")
    @Expose
    public Object format;
    @SerializedName("safe_value")
    @Expose
    public String safeValue;

    public String getValue() {
        return value;
    }
}
