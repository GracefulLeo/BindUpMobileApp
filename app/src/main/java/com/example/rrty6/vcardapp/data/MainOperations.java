package com.example.rrty6.vcardapp.data;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.rrty6.vcardapp.data.managers.DataManager;
import com.example.rrty6.vcardapp.data.network.NetworkOperations;
import com.example.rrty6.vcardapp.data.operations.JobInitiation;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Group;
import com.example.rrty6.vcardapp.data.storage.operation.DatabaseOperation;

import java.util.ArrayList;
import java.util.List;

import static com.example.rrty6.vcardapp.utils.UIHandler.WhatValue.*;

public class MainOperations {
    private static final String TAG = "MainOperations";
    private MainOperations INSTANCE;
    private static DataManager mDataManager = DataManager.getInstance();
    private final Handler handler;
    private NetworkOperations networkOperations = new NetworkOperations();
//    private DatabaseOperation databaseOperation;

    public MainOperations(Handler handler) {
        this.handler = handler;
        INSTANCE = this;
//        networkOperations = new NetworkOperations(handler);
//        databaseOperation = new DatabaseOperation(handler);
    }

    public static boolean isAuthorized() {
        return mDataManager.isAuthorized();
    }

    private static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void register(final String email, final String password) {
        handler.sendEmptyMessage(0);//Finish: Start progress bar (end in downloadUserData)
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "register start");
                if (mDataManager.isAuthorized()) {
                    Log.i(TAG, "user is still authorized");
                    DatabaseOperation.clearDb();
                    mDataManager.getPreferenceManager().logoutUser();
                }
                if (isValidEmail(email)) {
                    networkOperations.register(handler, email, password, INSTANCE);
                } else {
                    handler.sendEmptyMessage(0);//Finish: Email is not valid
                }
            }
        }).start();
    }

    public void login(final String email, final String password) {
        handler.sendEmptyMessage(methodStart);//Finish: Start progress bar (end in downloadUserData)
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "login start");
                if (mDataManager.isAuthorized()) {
                    DatabaseOperation.clearDb();
                    mDataManager.getPreferenceManager().logoutUser();
                }
                if (isValidEmail(email)) {
                    networkOperations.signIn(email, password, INSTANCE);
                } else {
                    mDataManager.handleError(handler);
                }
            }
        }).start();
    }

    public void logout() {
        handler.sendEmptyMessage(0);//Finish: Start progress bar
        new Thread(new Runnable() {
            public void run() {
                Log.i(TAG, "logout start");
                if (mDataManager.isAuthorized()) {
                    networkOperations.logOut();
                    handler.sendEmptyMessage(0);//Finish: logout successfully finished
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(0);//Finish: creating card failed User has not been authorized
                }
            }
        }).start();
    }

    public void downloadUserData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "downloadUserData");
                for (Card card : networkOperations.downloadMyCards()) {
                    DatabaseOperation.saveCard(card);
                }
                for (Card card : networkOperations.downloadMyContacts()) {
                    DatabaseOperation.saveCard(card);
                }
                for (Group group : networkOperations.getMyGroups()) {
                    DatabaseOperation.createGroup(group, null);
                    for (String cardRemoteId : networkOperations.getGroupContacts(group.getRemoteId())) {
                        mDataManager.addContactToGroup(group, mDataManager.getCardFromDB(cardRemoteId));
                    }
                }
                handler.sendEmptyMessage(methodEnd);//Finish: login/registration successfully finished
            }
        }).start();
    }

//    public static void updateUserData() throws SQLException {
//        if (DATA_MANAGER.isAuthorized()) {
//            List<Card> cards = DATA_MANAGER.getCardList();
//            List<String> strings = new ArrayList<>();
//            for (Card card : cards) {
//                strings.add(card.getRemoteId());
//            }
//            for (Card card : NetworkOperations.downloadMyCards()) {
//                if (!strings.contains(card.getRemoteId())) {
//                    DatabaseOperation.saveCard(card);
//                }
//            }
//            for (Card card : NetworkOperations.downloadMyContacts()) {
//                if (!strings.contains(card.getRemoteId())) {
//                    DatabaseOperation.saveCard(card);
//                }
//            }
//            strings.clear();
//            for (Group group : DATA_MANAGER.getGroupList()) {
//                strings.add(group.getRemoteId());
//            }
//            for (Group group : NetworkOperations.getMyGroups()) {
//                if (!strings.contains(group.getRemoteId())) {
//                    DatabaseOperation.createGroup(group, null);
//                    for (String cardRemoteId : NetworkOperations.getGroupContacts(group.getRemoteId())) {
//                        mDataManager.addContactToGroup(group, mDataManager.getCardFromDB(cardRemoteId));
//                    }
//                }
//            }
//        }
//    }//TODO: test

    public void createCard(final Card card) {
        handler.sendEmptyMessage(0);//Finish: start creating card
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDataManager.isAuthorized()) {
                    Log.i(TAG, "createCard start");
                    card.setMy(true);
                    DatabaseOperation.createCard(card);
                    JobInitiation.createCard(card.getId());
                    handler.sendEmptyMessage(0);//Finish: creating card finished
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(0);//Finish: creating card failed User has not been authorized
                }
            }
        }).start();
    }

    public void updateCard(final Card oldCard, final Card newCard) {
        handler.sendEmptyMessage(0);//Finish: start updating card
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDataManager.isAuthorized()) {
                    Log.i(TAG, "updateCard start for card: " + oldCard.getRemoteId());
                    oldCard.update(newCard);
                    mDataManager.updateCard(oldCard);
                    JobInitiation.updateCard(oldCard.getId());
                    handler.sendEmptyMessage(0);//Finish: updating card finished
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(0);//Finish: updating card failed User has not been authorized
                }
            }
        }).start();
    }

    public void deleteCard(final Card card) {
        handler.sendEmptyMessage(0);//Finish: start deleting card
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDataManager.isAuthorized()) {
                    Log.i(TAG, "deleteCard start for card: " + card.getRemoteId());
                    JobInitiation.deleteCard(card.getRemoteId());
                    DatabaseOperation.deleteCard(card);
                    handler.sendEmptyMessage(0);//Finish: deleting card finished
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(0);//Finish: deleting card failed User has not been authorized
                }
            }
        }).start();
    }

    public void createContact(final Card card) {
        handler.sendEmptyMessage(0);//Finish: start creating contact
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "createContact for contact: " + card.getRemoteId());
                if (mDataManager.isAuthorized()) {
                    card.setMy(false);
                    DatabaseOperation.createCard(card);
                    JobInitiation.addContact(card.getRemoteId());
                    handler.sendEmptyMessage(0);//Finish: creating contact finished
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(0);//Finish: creating contact failed User has not been authorized
                }
            }
        }).start();
    }

    public void deleteContact(final Card card) {
        handler.sendEmptyMessage(0);//Finish: start deleting contact
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDataManager.isAuthorized()) {
                    Log.i(TAG, "deleteContact");
                    JobInitiation.deleteContact(card.getRemoteId());
                    DatabaseOperation.deleteCard(card);
                    handler.sendEmptyMessage(0);//Finish: deleting contact finished
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(0);//Finish: deleting contact failed User has not been authorized
                }
            }
        }).start();
    }

    public void createGroup(final Group group, @Nullable final List<Card> contacts) {
        handler.sendEmptyMessage(0);//Finish: start creating group
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDataManager.isAuthorized()) {
                    Log.i(TAG, "createGroup start");
                    DatabaseOperation.createGroup(group, contacts);
                    List<String> ids = new ArrayList<>();
                    if (contacts != null && contacts.size() > 0) {
                        for (Card card : contacts) {
                            ids.add(card.getRemoteId());
                        }
                    }
                    JobInitiation.createGroup(group.getId(), ids);
                    handler.sendEmptyMessage(0);//Finish: creating group finished
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(0);//Finish: creating group failed User has not been authorized
                }
            }
        }).start();
    }

    public void updateGroup(final Group oldGroup, final Group newGroup) {
        handler.sendEmptyMessage(0);//Finish: start updating group
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDataManager.isAuthorized()) {
                    Log.i(TAG, "updateGroup start");
                    oldGroup.update(newGroup);
                    mDataManager.updateGroup(oldGroup);
                    JobInitiation.updateGroup(oldGroup.getId());
                    handler.sendEmptyMessage(0);//Finish: updating group finished
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(0);//Finish: updating group failed User has not been authorized
                }
            }
        }).start();
    }

    public void updateGroupContacts(final Group group, final List<Card> contacts) {
        handler.sendEmptyMessage(0);//Finish: start updating group contacts
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDataManager.isAuthorized()) {
                    Log.i(TAG, "updateGroupContacts start");
                    DatabaseOperation.updateGroupContacts(group, contacts);
                    List<String> ids = new ArrayList<>();
                    if (contacts != null && contacts.size() > 0) {
                        for (Card card : contacts) {
                            ids.add(card.getRemoteId());
                        }
                    }
                    JobInitiation.updateGroupContacts(group.getRemoteId(), ids);
                    handler.sendEmptyMessage(0);//Finish: updating group contacts finished
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(0);//Finish: updating group contacts failed User has not been authorized
                }
            }
        }).start();
    }

    public void deleteGroup(final Group group) {
        handler.sendEmptyMessage(0);//Finish: start deleting group
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDataManager.isAuthorized()) {
                    Log.i(TAG, "deleteGroup start");
                    JobInitiation.deleteGroup(group.getRemoteId());
                    DatabaseOperation.deleteGroup(group);
                    handler.sendEmptyMessage(0);//Finish: deleting group finished
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(0);//Finish: deleting group failed User has not been authorized
                }
            }
        }).start();
    }

    //From DB
    public List<Card> getCardList() {
        Log.i(TAG, "getCardList");
        if (mDataManager.isAuthorized()) {
            return mDataManager.getCardList();
        } else {
            Log.e(TAG, "User has not been authorized");
            handler.sendEmptyMessage(0);//Finish: getting card list failed User has not been authorized
            return null;
        }
    }

    //From DB
    public Card getCard(Long id) {
        Log.i(TAG, "getCard for card: " + id);
        if (mDataManager.isAuthorized()) {
            return DatabaseOperation.getCard(id);
        } else {
            Log.e(TAG, "User has not been authorized");
            handler.sendEmptyMessage(0);//Finish: getting card failed User has not been authorized
            return null;
        }
    }

    //From DB
    public List<Group> getGroupList() {
        Log.i(TAG, "getGroupList");
        if (mDataManager.isAuthorized()) {
            return mDataManager.getGroupList();
        } else {
            Log.e(TAG, "User has not been authorized");
            handler.sendEmptyMessage(0);//Finish: getting group list failed User has not been authorized
            return null;
        }
    }

    //From DB
    public List<Card> getContacts() {
        Log.i(TAG, "getContacts");
        if (mDataManager.isAuthorized()) {
            return mDataManager.getContactList();
        } else {
            Log.e(TAG, "User has not been authorized");
            handler.sendEmptyMessage(0);//Finish: deleting contacts list failed User has not been authorized
            return null;
        }
    }

    //From DB //TODO:Change group to groupId
    public List<Card> getGroupContacts(Group group) {
        Log.i(TAG, "getGroupContacts for group: " + group.getId());
        if (mDataManager.isAuthorized()) {
            return mDataManager.getGroupContacts(group);
        } else {
            Log.e(TAG, "User has not been authorized");
            handler.sendEmptyMessage(0);//Finish: deleting group contacts list failed User has not been authorized
            return null;
        }
    }
}
