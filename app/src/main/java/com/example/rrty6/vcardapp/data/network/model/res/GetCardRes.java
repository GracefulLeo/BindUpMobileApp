package com.example.rrty6.vcardapp.data.network.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//Used for getCard
//TODO: rebuild und classes
public class GetCardRes {

    @SerializedName("vid")
    @Expose
    public String vid;
    @SerializedName("uid")
    @Expose
    public String uid;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("log")
    @Expose
    public String log;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("comment")
    @Expose
    public String comment;
    @SerializedName("promote")
    @Expose
    public String promote;
    @SerializedName("sticky")
    @Expose
    public String sticky;
    @SerializedName("nid")
    @Expose
    public String nid;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("language")
    @Expose
    public String language;
    @SerializedName("created")
    @Expose
    public String created;
    @SerializedName("changed")
    @Expose
    public String changed;
    @SerializedName("tnid")
    @Expose
    public String tnid;
    @SerializedName("translate")
    @Expose
    public String translate;
    @SerializedName("revision_timestamp")
    @Expose
    public String revisionTimestamp;
    @SerializedName("revision_uid")
    @Expose
    public String revisionUid;
    @SerializedName("field_address")
    @Expose
    public Object fieldAddress;
    @SerializedName("field_company_name")
    @Expose
    public Object fieldCompanyName;
    @SerializedName("field_logotype")
    @Expose
    public Object fieldLogotype;
    @SerializedName("field_mail")
    @Expose
    public Object fieldMail;
    @SerializedName("field_middle_name")
    @Expose
    public Object fieldMiddleName;
    @SerializedName("field_name")
    @Expose
    public Object fieldName;
    @SerializedName("field_phone")
    @Expose
    public Object fieldPhone;
    @SerializedName("field_position")
    @Expose
    public Object fieldPosition;
    @SerializedName("field_surname")
    @Expose
    public Object fieldSurname;
    @SerializedName("field_web_site")
    @Expose
    public Object fieldWebSite;
    @SerializedName("field_social_links")
    @Expose
    public Object fieldSocialLinks;
    @SerializedName("field_base64_vcard")
    @Expose
    public Object fieldBase64Vcard;

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("picture")
    @Expose
    public String picture;
    @SerializedName("data")
    @Expose
    public String data;
    @SerializedName("path")
    @Expose
    public String path;

    public String getTitle() {
        return title;
    }

    public Object getFieldName() {
        return fieldName;
    }

    public Object getFieldSurname() {
        return fieldSurname;
    }

    public Object getFieldMiddleName() {
        return fieldMiddleName;
    }

    public Object getFieldCompanyName() {
        return fieldCompanyName;
    }

    public Object getFieldAddress() {
        return fieldAddress;
    }

    public Object getFieldPosition() {
        return fieldPosition;
    }

    public Object getFieldWebSite() {
        return fieldWebSite;
    }

    public Object getFieldBase64Vcard() {
        return fieldBase64Vcard;
    }

    public Object getFieldLogotype() {
        return fieldLogotype;
    }

    public String getNid() {
        return nid;
    }

    public Object getFieldMail() {
        return fieldMail;
    }

    public Object getFieldPhone() {
        return fieldPhone;
    }

    public Object getFieldSocialLinks() {
        return fieldSocialLinks;
    }

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

    public class FieldBase64Vcard {

        @SerializedName("und")
        @Expose
        public List<Base64> und = null;

        public List<Base64> getUnd() {
            return und;
        }
    }

    public class Base64 {

        @SerializedName("value")
        @Expose
        public String value;

        public String getValue() {
            return value;
        }
    }
}
