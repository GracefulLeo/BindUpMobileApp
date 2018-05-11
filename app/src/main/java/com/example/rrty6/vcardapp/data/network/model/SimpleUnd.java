package com.example.rrty6.vcardapp.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimpleUnd {

    @SerializedName("target_id")
    @Expose
    public String target_id;

    public SimpleUnd(String targetId) {
        this.target_id = targetId;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public String getTarget_id() {
        return target_id;
    }
}
