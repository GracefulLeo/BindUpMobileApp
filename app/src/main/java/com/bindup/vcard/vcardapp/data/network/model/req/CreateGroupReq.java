package com.bindup.vcard.vcardapp.data.network.model.req;

import com.bindup.vcard.vcardapp.data.network.model.SimpleUnd;
import com.bindup.vcard.vcardapp.data.storage.model.Group;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupReq {
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("name")
    @Expose
    public String groupName;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("field_logotype")
    @Expose
    public FieldLogo fieldLogotype;
    @SerializedName("field_my_contacts")
    @Expose
    public MyContacts fieldMyContacts;

    public CreateGroupReq(String name) {
        this.type = "group";
        this.groupName = name;
        this.fieldLogotype = new FieldLogo();
        this.fieldMyContacts = new MyContacts();
    }

    public CreateGroupReq(Group group, List<String> contactsIds) {
        type = "group";
        groupName = group.getName();
        if (group.getDescription() != null && !group.getDescription().isEmpty()) {
            description = group.getDescription();
        }
        if (group.getLogo() != null) {
            fieldLogotype = new FieldLogo();
            fieldLogotype.setLogoUnd(group.getLogo().getFid());
        }
        if (contactsIds != null && contactsIds.size() != 0) {
            fieldMyContacts = new MyContacts();
            for (String id : contactsIds) {
                fieldMyContacts.addUnd(new SimpleUnd(id));
            }
        }
    }

    public void setDescription(String description) {
        this.description = description;
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
