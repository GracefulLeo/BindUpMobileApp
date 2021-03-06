package com.bindup.vcard.vcardapp.data.network.model.req;

import com.bindup.vcard.vcardapp.data.managers.DataManager;
import com.bindup.vcard.vcardapp.data.network.model.SimpleField;
import com.bindup.vcard.vcardapp.data.network.model.SimpleUnd;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateGroupsReq {

    @SerializedName("uid")
    @Expose
    public String uid;
    @SerializedName("status")
    @Expose
    public String status = "1";
    @SerializedName("roles")
    @Expose
    public Roles roles = new Roles();
    @SerializedName("field_groups")
    @Expose
    public SimpleField fieldMyGroups;


    public UpdateGroupsReq(List<SimpleUnd> und) {
        uid = DataManager.getInstance().getPreferenceManager().loadUserID();
        fieldMyGroups = new SimpleField(und);
    }

    public class Roles {
        @SerializedName("2")
        @Expose
        public String _2 = "authenticated user";

    }
}
