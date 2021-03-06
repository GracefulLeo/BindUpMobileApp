package com.bindup.vcard.vcardapp.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.bindup.vcard.vcardapp.data.storage.DatabaseHelper;
import com.bindup.vcard.vcardapp.data.storage.model.Base;
import com.bindup.vcard.vcardapp.data.storage.model.Card;
import com.bindup.vcard.vcardapp.data.storage.model.Comment;
import com.bindup.vcard.vcardapp.data.storage.model.Email;
import com.bindup.vcard.vcardapp.data.storage.model.Group;
import com.bindup.vcard.vcardapp.data.storage.model.GroupCard;
import com.bindup.vcard.vcardapp.data.storage.model.History;
import com.bindup.vcard.vcardapp.data.storage.model.Logo;
import com.bindup.vcard.vcardapp.data.storage.model.Phone;
import com.bindup.vcard.vcardapp.data.storage.model.SocialLink;
import com.j256.ormlite.dao.Dao;


//Mainly used for DataManager
public class App extends Application {
    private static SharedPreferences sSharedPreferences;
    private static Context sContext;

    //region=======================BD declaration=======================
    private static DatabaseHelper helper;
    private static Dao<Card, Long> cardDao;
    private static Dao<Email, Long> emailDao;
    private static Dao<Phone, Long> phoneDao;
    private static Dao<SocialLink, Long> socialLinkDao;
    private static Dao<Logo, Long> logoDao;
    private static Dao<Base, Long> baseDao;
    private static Dao<Group, Long> groupDao;
    private static Dao<GroupCard, Long> groupCardDao;
    private static Dao<Comment, Long> commentDao;
    private static Dao<History, Long> historyDao;
    //endregion====================BD declaration=======================

    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)){
//            return;
//        }
//        LeakCanary.install(this);
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sContext = this;

        //region=======================BD init=======================
        helper = new DatabaseHelper(this);
        cardDao = helper.getCardDao();
        emailDao = helper.getEmailDao();
        phoneDao = helper.getPhoneDao();
        socialLinkDao = helper.getSocialLinkDao();
        logoDao = helper.getLogoDao();
        baseDao = helper.getBaseDao();
        groupDao = helper.getGroupDao();
        groupCardDao = helper.getGroupCardDao();
        commentDao = helper.getCommentDao();
        historyDao = helper.getHistoryDao();
        //endregion====================BD init=======================
    }

    public static Context getContext() {
        return sContext;
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public static boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager)sContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    //region=======================BD=======================
    public static DatabaseHelper getHelper() {
        return helper;
    }

    public static Dao<Card, Long> getCardDao() {
        return cardDao;
    }

    public static Dao<Email, Long> getEmailDao() {
        return emailDao;
    }

    public static Dao<Phone, Long> getPhoneDao() {
        return phoneDao;
    }

    public static Dao<SocialLink, Long> getSocialLinkDao() {
        return socialLinkDao;
    }

    public static Dao<Logo, Long> getLogoDao() {
        return logoDao;
    }

    public static Dao<Base, Long> getBaseDao() {
        return baseDao;
    }

    public static Dao<Group, Long> getGroupDao() {
        return groupDao;
    }

    public static Dao<GroupCard, Long> getGroupCardDao() {
        return groupCardDao;
    }

    public static Dao<Comment, Long> getCommentDao() {
        return commentDao;
    }

    public static Dao<History, Long> getHistoryDao() {
        return historyDao;
    }

    //endregion====================BD=======================
}
