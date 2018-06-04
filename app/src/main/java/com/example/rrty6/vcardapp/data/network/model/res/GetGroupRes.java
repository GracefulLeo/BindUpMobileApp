package com.example.rrty6.vcardapp.data.network.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetGroupRes {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("changed")
    @Expose
    private String changed;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("name")
    @Expose
    private String groupName;
    @SerializedName("field_logotype")
    @Expose
    private Object fieldLogotype;
    @SerializedName("field_my_contacts")
    @Expose
    private Object fieldMyContacts;

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getGroupName() {
        return groupName;
    }

    public Object getFieldLogotype() {
        return fieldLogotype;
    }

    public Object getFieldMyContacts() {
        return fieldMyContacts;
    }
}
