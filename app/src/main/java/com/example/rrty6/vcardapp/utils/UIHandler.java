package com.example.rrty6.vcardapp.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import static com.example.rrty6.vcardapp.utils.UIHandler.WhatValue.*;

public class UIHandler extends Handler {
    private Context context;

    public UIHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        Toast toast;
        switch (msg.what) {
            case registerStart:
                toast = Toast.makeText(context, "MEthod have been started", Toast.LENGTH_LONG);
                toast.show();
                break;
            case authorizationFinished:
                toast = Toast.makeText(context, "MEthod have been ended", Toast.LENGTH_LONG);
                toast.show();
                break;
        }
    }

    public interface WhatValue {
        int thereIsNoInternet = 26;
        int registerStart = 1;
        int alreadyUsedEmail = 2;
        int wrongUserNameOrPassword = 3;
        int loginStart = 4;
        int logoutStart = 5;
        int logoutFinished = 6;
        int authorizationFinished = 7;
        int userHasNotBeenAuthorized = 8;
        int createCardStart = 9;
        int createCardFinished = 10;
        int updateCardStart = 11;
        int updateCardFinished = 12;
        int deleteCardStart = 13;
        int deleteCardFinished = 13;
        int createContactStart = 14;
        int createContactFinished = 15;
        int deleteContactStart = 16;
        int deleteContactFinished = 17;
        int createGroupStart = 18;
        int createGroupFinished = 19;
        int updateGroupStart = 20;
        int updateGroupFinished = 21;
        int updateGroupContactsStart = 22;
        int updateGroupContactsFinished = 23;
        int deleteGroupStart = 24;
        int deleteGroupFinished = 25;
    }
}
