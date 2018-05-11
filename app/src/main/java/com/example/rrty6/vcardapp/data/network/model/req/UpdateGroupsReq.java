package com.example.rrty6.vcardapp.data.network.model.req;

import com.example.rrty6.vcardapp.data.managers.DataManager;
import com.example.rrty6.vcardapp.data.network.model.SimpleField;
import com.example.rrty6.vcardapp.data.network.model.SimpleUnd;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateGroupsReq {

    @SerializedName("uid")
    @Expose
    public String uid;
    @SerializedName("field_groups")
    @Expose
    public SimpleField fieldMyGroups;


    public UpdateGroupsReq(List<SimpleUnd> und) {
        uid = DataManager.getInstance().getPreferenceManager().loadUserID();
        fieldMyGroups = new SimpleField(und);
    }
}
