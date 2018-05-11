package com.example.rrty6.vcardapp.data.network.model.req;

import com.example.rrty6.vcardapp.data.network.model.SimpleUnd;
import com.example.rrty6.vcardapp.data.storage.GroupCompare;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Group;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UpdateGroupReq {
    @SerializedName("id")
    @Expose
    private String id;
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
    public FieldLogotype fieldLogotype;
    @SerializedName("field_my_contacts")
    @Expose
    public MyContacts fieldMyContacts;

    public UpdateGroupReq(Group group, GroupCompare compare) {
        if (group.getRemoteId() != null && !group.getRemoteId().isEmpty()) {
            type = "group";
            id = group.getRemoteId();
            if (compare.fieldLogotype) {
                fieldLogotype = new FieldLogotype(group.getLogo().getFid());
            }
            if (compare.fieldGroupName) {
                fieldGroupName = new Field(group.getName());
            }
            if (compare.fieldDescription) {
                fieldDescription = new Field(group.getDescription());
            }
        }
    }

    public UpdateGroupReq(Group group, List<Card> contacts) {
        if (group.getRemoteId() != null && !group.getRemoteId().isEmpty()) {
            type = "group";
            id = group.getRemoteId();
            List<String> contact = new ArrayList<>();
            for (Card card: contacts) {
                contact.add(card.getRemoteId());
            }
            fieldMyContacts = new MyContacts(contact);
        }
    }

    public String getId() {
        return id;
    }

    public class Field {

        @SerializedName("und")
        @Expose
        public List<Und> und = null;


        Field(String value) {
            this.und = new ArrayList<>();
            Und und = new Und(value);
            this.und.add(und);
        }

        //Setter for multiple field contacts
        Field(List<String> values) {
            this.und = new ArrayList<>();
            for (String value : values) {
                Und und = new Und(value);
                this.und.add(und);
            }
        }
    }

    private class Und {

        @SerializedName("value")
        @Expose
        public String value;

        public Und(String value) {
            this.value = value;
        }
    }

    public class FieldLogotype {

        @SerializedName("und")
        @Expose
        public List<LogoUnd> logoUnd = null;

        FieldLogotype(String fid) {
            this.logoUnd = new ArrayList<>();
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
        public List<SimpleUnd> und;

        public MyContacts(List<String> colection) {
            und = new ArrayList<>();
            for (String s : colection) {
                und.add(new SimpleUnd(s));
            }
        }
    }
}
