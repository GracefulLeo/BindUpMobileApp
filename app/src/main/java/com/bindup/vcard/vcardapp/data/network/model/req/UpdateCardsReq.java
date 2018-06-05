package com.bindup.vcard.vcardapp.data.network.model.req;

import com.bindup.vcard.vcardapp.data.managers.DataManager;
import com.bindup.vcard.vcardapp.data.network.model.SimpleField;
import com.bindup.vcard.vcardapp.data.network.model.SimpleUnd;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateCardsReq {


    @SerializedName("uid")
    @Expose
    public String uid;
    @SerializedName("status")
    @Expose
    public String status = "1";
    @SerializedName("roles")
    @Expose
    public Roles roles = new Roles();
    @SerializedName("field_my_vcards")
    @Expose
    public SimpleField fieldMyVcards;


    public UpdateCardsReq(List<SimpleUnd> und) {
        uid = DataManager.getInstance().getPreferenceManager().loadUserID();
        fieldMyVcards = new SimpleField(und);
    }

    public class Roles {
        @SerializedName("2")
        @Expose
        public String _2 = "authenticated user";

    }
}
