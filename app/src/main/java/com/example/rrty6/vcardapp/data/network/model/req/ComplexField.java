package com.example.rrty6.vcardapp.data.network.model.req;

import com.example.rrty6.vcardapp.data.network.model.ComplexUnd;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ComplexField {

    @SerializedName("und")
    @Expose
    public List<ComplexUnd> und;

    public List<ComplexUnd> getUnd() {
        return und;
    }
}
