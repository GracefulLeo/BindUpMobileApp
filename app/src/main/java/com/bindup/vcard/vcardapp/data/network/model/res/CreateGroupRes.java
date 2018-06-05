package com.bindup.vcard.vcardapp.data.network.model.res;

import com.bindup.vcard.vcardapp.data.network.model.SimpleUnd;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateGroupRes {
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("field_group_name")
    @Expose
    public Field fieldGroupName;
    @SerializedName("field_description")
    @Expose
    public Field fieldDescription;
    @SerializedName("field_my_contacts")
    @Expose
    public FieldMyContacts fieldMyContacts;
    @SerializedName("field_logotype")
    @Expose
    public FieldLogotype fieldLogotype;
    @SerializedName("changed")
    @Expose
    public int changed;
    @SerializedName("uid")
    @Expose
    public String uid;
    @SerializedName("id")
    @Expose
    public String id;


    public String getId() {
        return id;
    }

    public class Field {

        @SerializedName("und")
        @Expose
        public List<Und> und;

    }

    public class Und {

        @SerializedName("value")
        @Expose
        public String value;

    }

    public class FieldLogotype {

        @SerializedName("und")
        @Expose
        public List<Und1> und;

    }

    public class Und1 {

        @SerializedName("fid")
        @Expose
        public String fid;
        @SerializedName("width")
        @Expose
        public int width;
        @SerializedName("height")
        @Expose
        public int height;

    }

    private class FieldMyContacts {
        @SerializedName("und")
        @Expose
        public List<SimpleUnd> und;
    }
}
