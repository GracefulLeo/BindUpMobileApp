package com.example.rrty6.vcardapp.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimpleUnd {

    @SerializedName("target_id")
    @Expose
    public String targetId;

    public SimpleUnd(String targetId) {
        this.targetId = targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetId() {
        return targetId;
    }
}
