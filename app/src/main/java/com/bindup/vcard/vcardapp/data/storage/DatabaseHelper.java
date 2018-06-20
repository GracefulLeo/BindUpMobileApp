package com.bindup.vcard.vcardapp.data.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
import com.bindup.vcard.vcardapp.utils.AppConfig;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

//Used !ONLY in Application
//DB actions via DataManager
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private Context context;
    private Dao<Card, Long> cardDao = null;
    private Dao<Email, Long> emailDao = null;
    private Dao<Phone, Long> phoneDao = null;
    private Dao<SocialLink, Long> socialLinkDao = null;
    private Dao<Logo, Long> logoDao = null;
    private Dao<Base, Long> baseDao = null;
    private Dao<Group, Long> groupDao = null;
    private Dao<GroupCard, Long> groupCardDao = null;
    private Dao<Comment, Long> commentDao = null;
    private Dao<History, Long> historyDao = null;

    public DatabaseHelper(Context context) {
        super(context, AppConfig.DB_NAME, null, AppConfig.DB_VERSION);
        this.context = context;
    }

    @Override
    //Creates tables
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        Log.i(DatabaseHelper.class.getName(), "onCreate");
        try {
            TableUtils.createTableIfNotExists(connectionSource, Card.class);
            TableUtils.createTableIfNotExists(connectionSource, Email.class);
            TableUtils.createTableIfNotExists(connectionSource, Phone.class);
            TableUtils.createTableIfNotExists(connectionSource, SocialLink.class);
            TableUtils.createTableIfNotExists(connectionSource, Logo.class);
            TableUtils.createTableIfNotExists(connectionSource, Base.class);
            TableUtils.createTableIfNotExists(connectionSource, Group.class);
            TableUtils.createTableIfNotExists(connectionSource, GroupCard.class);
            TableUtils.createTableIfNotExists(connectionSource, Comment.class);
            TableUtils.createTableIfNotExists(connectionSource, History.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            //TODO: Finish onUpdate DB
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Card.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop database", e);
            throw new RuntimeException(e);
        }
    }

    //region===================================GetDao=============================================
    public Dao<Card, Long> getCardDao() {
        if (cardDao == null) {
            try {
                cardDao = getDao(Card.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cardDao;
    }

    public Dao<Email, Long> getEmailDao() {
        if (emailDao == null) {
            try {
                emailDao = getDao(Email.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return emailDao;
    }

    public Dao<Phone, Long> getPhoneDao() {
        if (phoneDao == null) {
            try {
                phoneDao = getDao(Phone.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return phoneDao;
    }

    public Dao<SocialLink, Long> getSocialLinkDao() {
        if (socialLinkDao == null) {
            try {
                socialLinkDao = getDao(SocialLink.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return socialLinkDao;
    }

    public Dao<Logo, Long> getLogoDao() {
        if (logoDao == null) {
            try {
                logoDao = getDao(Logo.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return logoDao;
    }

    public Dao<Base, Long> getBaseDao() {
        if (baseDao == null) {
            try {
                baseDao = getDao(Base.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return baseDao;
    }

    public Dao<Group, Long> getGroupDao() {
        if (groupDao == null) {
            try {
                groupDao = getDao(Group.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return groupDao;
    }

    public Dao<GroupCard, Long> getGroupCardDao() {
        if (groupCardDao == null) {
            try {
                groupCardDao = getDao(GroupCard.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return groupCardDao;
    }

    public Dao<Comment, Long> getCommentDao() {
        if (commentDao == null) {
            try {
                commentDao = getDao(Comment.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return commentDao;
    }

    public Dao<History, Long> getHistoryDao() {
        if (historyDao == null) {
            try {
                historyDao = getDao(History.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return historyDao;
    }

    //endregion================================GetDao=============================================

    public Context getContext() {
        return context;
    }

    @Override
    //Clearing the memory
    public void close() {
        super.close();
        cardDao = null;
        emailDao = null;
        phoneDao = null;
        socialLinkDao = null;
        logoDao = null;
        baseDao = null;
        groupDao = null;
        groupCardDao = null;
        commentDao = null;
    }
}
