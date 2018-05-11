package com.example.rrty6.vcardapp.data.network.model.req;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//Request model used in call's body
public class UserRegisterReq {

    @SerializedName("mail")
    @Expose
    private String username;
    @SerializedName("pass")
    @Expose
    private String password;

    public UserRegisterReq(String email, String password) {
        this.username = email;
        this.password = password;
    }
}
