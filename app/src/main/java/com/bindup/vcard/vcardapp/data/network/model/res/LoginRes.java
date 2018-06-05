package com.bindup.vcard.vcardapp.data.network.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//Used for loginUser
public class LoginRes {
    @SerializedName("sessid")
    @Expose
    private String sessid;
    @SerializedName("session_name")
    @Expose
    private String sessionName;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user")
    @Expose
    private UserRes user;

    public String getToken() {
        return token;
    }

    public UserRes getUser() {
        return user;
    }

    public class UserRes {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("mail")
        @Expose
        private String mail;
        @SerializedName("theme")
        @Expose
        private String theme;
        @SerializedName("signature")
        @Expose
        private String signature;
        @SerializedName("signature_format")
        @Expose
        private String signatureFormat;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("access")
        @Expose
        private String access;
        @SerializedName("login")
        @Expose
        private int login;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("timezone")
        @Expose
        private String timezone;
        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("picture")
        @Expose
        private Object picture;
        @SerializedName("data")
        @Expose
        private boolean data;
        @SerializedName("roles")
        @Expose
        private Object roles;
        @SerializedName("field_my_contacts")
        @Expose
        private Object fieldMyContacts;
        @SerializedName("field_groups")
        @Expose
        private Object fieldGroups;
        @SerializedName("field_my_vcards")
        @Expose
        private Object fieldMyVcards;

        public String getUid() {
            return uid;
        }

        public Object getFieldMyContacts() {
            return fieldMyContacts;
        }

        public Object getFieldGroups() {
            return fieldGroups;
        }

        public Object getFieldMyVcards() {
            return fieldMyVcards;
        }
    }
}
