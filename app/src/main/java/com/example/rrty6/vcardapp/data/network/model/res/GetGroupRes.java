package com.example.rrty6.vcardapp.data.network.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetGroupRes {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("changed")
    @Expose
    public String changed;
    @SerializedName("uid")
    @Expose
    public String uid;
    @SerializedName("field_description")
    @Expose
    public Object fieldDescription;
    @SerializedName("field_group_name")
    @Expose
    public Object fieldGroupName;
    @SerializedName("field_logotype")
    @Expose
    public Object fieldLogotype;
    @SerializedName("field_my_contacts")
    @Expose
    public Object fieldMyContacts;

    public String getId() {
        return id;
    }

    public Object getFieldDescription() {
        return fieldDescription;
    }

    public Object getFieldGroupName() {
        return fieldGroupName;
    }

    public Object getFieldLogotype() {
        return fieldLogotype;
    }

    public Object getFieldMyContacts() {
        return fieldMyContacts;
    }
}
