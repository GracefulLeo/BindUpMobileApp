package com.example.rrty6.vcardapp.data.network.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//Used for getToken
public class TokenRes {
    @SerializedName("token")
    @Expose
    public String token;

    public String getToken() {
        return token;
    }
}
