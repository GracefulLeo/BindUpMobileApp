package com.example.rrty6.vcardapp.data.network.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//Used for getCard
//TODO: rebuild und classes
public class GetCardRes {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("changed")
    @Expose
    private String changed;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("middle_name")
    @Expose
    private String middleName;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("web_site")
    @Expose
    private String webSite;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("base64_vcard")
    @Expose
    private String base64Vcard;
    @SerializedName("social_links")
    @Expose
    private String socialLinks;
    @SerializedName("field_logotype")
    @Expose
    private Object fieldLogotype;


    public class FieldLogotype {

        @SerializedName("und")
        @Expose
        public List<Logo> und = null;

        public List<Logo> getUnd() {
            return und;
        }
    }

    public class Logo {

        @SerializedName("fid")
        @Expose
        public String fid;
        @SerializedName("uid")
        @Expose
        public String uid;
        @SerializedName("filename")
        @Expose
        public String filename;
        @SerializedName("uri")
        @Expose
        public String uri;
        @SerializedName("filemime")
        @Expose
        public String filemime;
        @SerializedName("filesize")
        @Expose
        public String filesize;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("timestamp")
        @Expose
        public String timestamp;
        @SerializedName("alt")
        @Expose
        public String alt;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("width")
        @Expose
        public String width;
        @SerializedName("height")
        @Expose
        public String height;

        public String getFid() {
            return fid;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getCompany() {
        return company;
    }

    public String getAddress() {
        return address;
    }

    public String getPosition() {
        return position;
    }

    public String getWebSite() {
        return webSite;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getBase64Vcard() {
        return base64Vcard;
    }

    public String getSocialLinks() {
        return socialLinks;
    }

    public Object getFieldLogotype() {
        return fieldLogotype;
    }
}
