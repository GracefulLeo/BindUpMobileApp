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
    @SerializedName("group_name")
    @Expose
    public String groupName;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("field_logotype")
    @Expose
    public FieldLogotype fieldLogotype;
    @SerializedName("field_my_contacts")
    @Expose
    public MyContacts fieldMyContacts;

    public UpdateGroupReq(Group group) {
        if (group.getRemoteId() != null && !group.getRemoteId().isEmpty()) {
            type = "group";
            id = group.getRemoteId();
            if (group.getLogo() != null && group.getLogo().getFid() != null) {
                fieldLogotype = new FieldLogotype(group.getLogo().getFid());
            }
            if (group.getName() != null) {
                groupName = group.getName();
            }
            if (group.getDescription() != null) {
                description = group.getDescription();
            }
        }
    }

    public UpdateGroupReq(String remoteId, List<String> contacts) {
        if (remoteId != null && !remoteId.isEmpty()) {
            type = "group";
            id = remoteId;
            fieldMyContacts = new MyContacts(contacts);
        }
    }

    public String getId() {
        return id;
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
