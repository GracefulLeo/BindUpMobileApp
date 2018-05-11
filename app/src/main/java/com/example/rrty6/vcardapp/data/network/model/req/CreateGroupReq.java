package com.example.rrty6.vcardapp.data.network.model.req;

import com.example.rrty6.vcardapp.data.network.model.SimpleUnd;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupReq {
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("field_group_name")
    @Expose
    public Field fieldGroupName;
    @SerializedName("field_description")
    @Expose
    public Field fieldDescription;
    @SerializedName("field_logotype")
    @Expose
    public FieldLogo fieldLogotype;
    @SerializedName("field_my_contacts")
    @Expose
    public MyContacts fieldMyContacts;

    public CreateGroupReq() {
        this.type = "group";
        this.fieldGroupName = new Field();
        this.fieldDescription = new Field();
        this.fieldLogotype = new FieldLogo();
        this.fieldMyContacts = new MyContacts();
    }

    public class Field {

        @SerializedName("und")
        @Expose
        public List<Und> und = new ArrayList<>();

        public void setUnd(String value) {
            Und und = new Und(value);
            this.und.add(und);
        }
    }

    public class Und {

        @SerializedName("value")
        @Expose
        public String value;

        public Und(String value) {
            this.value = value;
        }
    }

    public class FieldLogo {
        @SerializedName("und")
        @Expose
        public List<LogoUnd> logoUnd = new ArrayList<>();

        public void setLogoUnd(String fid) {
            LogoUnd logoUnd = new LogoUnd(fid);
            this.logoUnd.add(logoUnd);
        }
    }

    private class LogoUnd {
        @SerializedName("fid")
        @Expose
        public String fid;

        public LogoUnd(String fid) {
            this.fid = fid;
        }
    }

    public class MyContacts {
        @SerializedName("und")
        @Expose
        public List<SimpleUnd> und = new ArrayList<>();

        public void addUnd(SimpleUnd und) {
            this.und.add(und);
        }
    }
}
