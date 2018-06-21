package com.bindup.vcard.vcardapp.data.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.bindup.vcard.vcardapp.utils.App;
import com.bindup.vcard.vcardapp.utils.Const;

//The Singleton of this class keeps in Application
//The instance obtains via DataManager
public class PreferencesManager {
    private final SharedPreferences mSharedPreferences;
//    private final Context mContext; //Not sure about necessary here

    public PreferencesManager() {
        this.mSharedPreferences = App.getSharedPreferences();
//        this.mContext = App.getContext();
    }

    //Saves session name and value as cookie in preferences
    //Needed for most calls
    public void saveCookie(String cookie) {
        if (cookie != null && !cookie.isEmpty()) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(Const.COOKIE, cookie);
            editor.apply();
        }
    }

    //Returns cookie
    public String loadCookie() {
        return mSharedPreferences.getString(Const.COOKIE,null);
    }

    //Saves UserRes Id in preferences after login
    public void saveUserID(String id) {
        if (id != null && !id.isEmpty()) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(Const.USER_ID, id);
            editor.apply();
        }
    }

    //Used in Call("get all users cards")
    public String loadUserID() {
        return mSharedPreferences.getString(Const.USER_ID, null);
    }

    //Saves XCSRFtoken in preferences
    public void saveXCSRFToken(String tokenXCSRF) {
        if (tokenXCSRF != null && !tokenXCSRF.isEmpty()) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(Const.ACCESS_TOKEN, tokenXCSRF);
            editor.apply();
        }
    }

    //Used in logout
    public String loadXCSRFToken() {
        return mSharedPreferences.getString(Const.ACCESS_TOKEN, null);
    }

    //Clear access data from preferences(session and XCSRFtoken)
    public void logoutUser() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(Const.COOKIE);
        editor.remove(Const.ACCESS_TOKEN);
        editor.remove(Const.USER_ID);
        editor.apply();
    }

    public void saveToken(String token) {
        if (token != null && !token.isEmpty()) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(Const.USER_TOKEN, token);
            editor.apply();
        }
    }

    public String loadToken() {
        return mSharedPreferences.getString(Const.USER_TOKEN, null);
    }
}
