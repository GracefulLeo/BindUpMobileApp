package com.example.rrty6.vcardapp.data.network.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserRes {
    @SerializedName("uid")
    @Expose
    public String uid;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("mail")
    @Expose
    public String mail;
    @SerializedName("theme")
    @Expose
    public String theme;
    @SerializedName("signature")
    @Expose
    public String signature;
    @SerializedName("signature_format")
    @Expose
    public String signatureFormat;
    @SerializedName("created")
    @Expose
    public String created;
    @SerializedName("access")
    @Expose
    public String access;
    @SerializedName("login")
    @Expose
    public String login;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("timezone")
    @Expose
    public String timezone;
    @SerializedName("language")
    @Expose
    public String language;
    @SerializedName("picture")
    @Expose
    public String picture;
    @SerializedName("data")
    @Expose
    public boolean data;
    @SerializedName("roles")
    @Expose
    public Object roles;
    @SerializedName("field_my_contacts")
    @Expose
    public Object fieldMyContacts;
    @SerializedName("field_groups")
    @Expose
    public Object fieldGroups;
    @SerializedName("field_history")
    @Expose
    public Object fieldHistory;
    @SerializedName("field_my_vcards")
    @Expose
    public Object fieldMyVcards;


    public Object getFieldMyContacts() {
        return fieldMyContacts;
    }

    public Object getFieldGroups() {
        return fieldGroups;
    }

    public Object getFieldHistory() {
        return fieldHistory;
    }

    public Object getFieldMyVcards() {
        return fieldMyVcards;
    }
}
