package com.example.rrty6.vcardapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.data.MainOperations;
import com.example.rrty6.vcardapp.ui.Activities.LoginActivity;
import com.example.rrty6.vcardapp.ui.Activities.MainActivity;
import com.example.rrty6.vcardapp.ui.Activities.RegisterActivity;
import com.example.rrty6.vcardapp.ui.interfaces.IMainActivity;

import static com.example.rrty6.vcardapp.utils.UIHandler.WhatValue.*;

public class UIHandler extends Handler {
    private LoginActivity loginActivity;
    private RegisterActivity registerActivity;
    private MainActivity mainActivity;
    private IMainActivity mInterface = null;

    public UIHandler(Activity activity, @Nullable IMainActivity mInterface) {
        try {
            loginActivity = (LoginActivity) activity;
        } catch (ClassCastException e) {
        }
        try {
            registerActivity = (RegisterActivity) activity;
        } catch (ClassCastException e) {
        }
        try {
            mainActivity = (MainActivity) activity;
        } catch (ClassCastException e) {
        }
        if (mInterface != null) {
            this.mInterface = mInterface;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        Toast toast;
        Intent intent;
        switch (msg.what) {
            case thereIsNoInternet:
                if (registerActivity != null) {
                    registerActivity.progressBarChange(false, null);
                    toast = Toast.makeText(registerActivity,R.string.there_is_no_internet,Toast.LENGTH_LONG);
                    toast.show();
                    break;
                }
                if (loginActivity != null) {
                    loginActivity.progressBarChange(false);
                    toast = Toast.makeText(loginActivity,R.string.there_is_no_internet,Toast.LENGTH_LONG);
                    toast.show();
                    break;
                }
                if (mainActivity != null) {
                    mainActivity.progressBarChange(false);
                    toast = Toast.makeText(mainActivity,R.string.there_is_no_internet,Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            case registerStart:
                // start progress bar
                registerActivity.progressBarChange(true,registerActivity.getResources().getString(R.string.registration_process_message));
                break;
            case alreadyUsedEmail:
                // stop progress bar
                registerActivity.progressBarChange(false, null);
                //TODO create method in register activity, warning about email
                toast = Toast.makeText(registerActivity, R.string.registration_already_used_email_message,Toast.LENGTH_LONG);
                toast.show();
                break;
            case registerFinished:
                registerActivity.progressBarChange(true,registerActivity.getResources().getString(R.string.registration_succesfully_finished_message));
                break;
            case loginStart:
                loginActivity.progressBarChange(true);
                break;
            case wrongUserNameOrPassword:
                loginActivity.progressBarChange(false);
                toast = Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.login_wrong_userdata_message),Toast.LENGTH_LONG);
                toast.show();
                break;
            case authorizationFinished:
                if (registerActivity != null) {
                    intent = new Intent(registerActivity, MainActivity.class);
                    registerActivity.progressBarChange(false , null);
                    registerActivity.startActivity(intent);
                    registerActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    registerActivity.finish();
                }
                if (loginActivity != null) {
                    intent = new Intent(loginActivity, MainActivity.class);
                    loginActivity.progressBarChange(false);
                    loginActivity.startActivity(intent);
                    loginActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    loginActivity.finish();
                }
                break;
            case logoutStart:
                mainActivity.progressBarChange(true);
                break;
            case logoutFinished:
                intent = new Intent( mainActivity, LoginActivity.class);
                intent.putExtra("logout", false);
                mainActivity.startActivity(intent);
                mainActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                mainActivity.finish();
                break;
        }
    }

    public interface WhatValue {
        int thereIsNoInternet = 26;//++
        int registerStart = 1;  //++
        int registerFinished = 27;//++
        int alreadyUsedEmail = 2;//++
        int wrongUserNameOrPassword = 3; //++
        int loginStart = 4;     //++
        int logoutStart = 5;    //++
        int logoutFinished = 6; //++
        int authorizationFinished = 7;//++
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
