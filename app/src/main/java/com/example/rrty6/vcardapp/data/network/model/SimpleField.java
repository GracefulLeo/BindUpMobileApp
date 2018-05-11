package com.example.rrty6.vcardapp.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SimpleField {
    @SerializedName("und")
    @Expose
    private List<SimpleUnd> und;

    public SimpleField(List<SimpleUnd> und) {
        this.und = und;
    }

    public SimpleField() {
        und = new ArrayList<>();
    }

    public List<SimpleUnd> getUnd() {
        return und;
    }

    public void add(SimpleUnd und) {
        this.und.add(und);
    }
}
