package com.bindup.vcard.vcardapp.data.network.model.req;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryReq {
    @SerializedName("type")
    @Expose
    public String type;
}
