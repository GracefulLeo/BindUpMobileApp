package com.bindup.vcard.vcardapp.data;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.bindup.vcard.vcardapp.data.managers.DataManager;
import com.bindup.vcard.vcardapp.data.network.NetworkOperations;
import com.bindup.vcard.vcardapp.data.operations.JobInitiation;
import com.bindup.vcard.vcardapp.data.storage.model.Card;
import com.bindup.vcard.vcardapp.data.storage.model.Comment;
import com.bindup.vcard.vcardapp.data.storage.model.Group;
import com.bindup.vcard.vcardapp.data.storage.model.History;
import com.bindup.vcard.vcardapp.data.storage.operation.DatabaseOperation;
import com.bindup.vcard.vcardapp.utils.App;

import java.util.ArrayList;
import java.util.List;

import static com.bindup.vcard.vcardapp.utils.UIHandler.WhatValue.*;

public class MainOperations {
    private static final String TAG = "MainOperations";
    private MainOperations INSTANCE;
    private static DataManager mDataManager = DataManager.getInstance();
    private final Handler handler;
    private NetworkOperations networkOperations = new NetworkOperations();

    public MainOperations(Handler handler) {
        this.handler = handler;
        INSTANCE = this;
    }

    public static boolean isAuthorized() {
        return mDataManager.isAuthorized();
    }

    private static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void register(final String email, final String password) {
        handler.sendEmptyMessage(registerStart);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "register start");
                if (App.hasConnection()) {
                    if (mDataManager.isAuthorized()) {
                        Log.d(TAG, "user is still authorized");
                        DatabaseOperation.clearDb();
                        mDataManager.getPreferenceManager().logoutUser();
                    }
                    networkOperations.register(handler, email, password, INSTANCE);
                } else {
                    Log.e(TAG, "There is no internet");
                    handler.sendEmptyMessage(thereIsNoInternet);
                }
            }
        }).start();
    }

    public void login(final String email, final String password) {
        handler.sendEmptyMessage(loginStart);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "login start");
                if (App.hasConnection()) {
                    if (mDataManager.isAuthorized()) {
                        DatabaseOperation.clearDb();
                        mDataManager.getPreferenceManager().logoutUser();
                    }
                    networkOperations.signIn(handler, email, password, INSTANCE);
                } else {
                    Log.e(TAG, "There is no internet");
                    handler.sendEmptyMessage(thereIsNoInternet);
                }
            }
        }).start();
    }

    public void logout() {
        handler.sendEmptyMessage(logoutStart);
        new Thread(new Runnable() {
            public void run() {
                Log.d(TAG, "logout start");
                if (App.hasConnection()) {
                    if (mDataManager.isAuthorized()) {
                        networkOperations.logOut();
                        handler.sendEmptyMessage(logoutFinished);
                    } else {
                        Log.e(TAG, "User has not been authorized");
                        handler.sendEmptyMessage(userHasNotBeenAuthorized);
                    }
                } else {
                    Log.e(TAG, "There is no internet");
                    handler.sendEmptyMessage(thereIsNoInternet);
                }
            }
        }).start();
    }

    public void downloadUserData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "downloadUserData");
                for (Card card : NetworkOperations.downloadMyCards()) {
                    DatabaseOperation.saveCard(card);
                }
                for (Card card : NetworkOperations.downloadMyContacts()) {
                    DatabaseOperation.saveCard(card);
                }
                for (Group group : NetworkOperations.dowloadMyGroups()) {
                    DatabaseOperation.createGroup(group, null);
                    for (String cardRemoteId : NetworkOperations.getGroupContacts(group.getRemoteId())) {
                        DatabaseOperation.addContactToGroupDownloadin(group, cardRemoteId);
                    }
                }
                handler.sendEmptyMessage(authorizationFinished);
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
//            for (Group group : NetworkOperations.dowloadMyGroups()) {
//                if (!strings.contains(group.getRemoteId())) {
//                    DatabaseOperation.createGroup(group, null);
//                    for (String cardRemoteId : NetworkOperations.getGroupContacts(group.getRemoteId())) {
//                        mDataManager.addContactToGroupDownloadin(group, mDataManager.getCardFromDB(cardRemoteId));
//                    }
//                }
//            }
//        }
//    }//TODO: test

    public void createCard(final Card card) {
        handler.sendEmptyMessage(createCardStart);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "createCard start");
                if (mDataManager.isAuthorized()) {
                    card.setMy(true);
                    DatabaseOperation.createCard(card);
                    JobInitiation.createCard(card.getId());
                    handler.sendEmptyMessage(createCardFinished);
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(userHasNotBeenAuthorized);
                }
            }
        }).start();
    }

    public void updateCard(final Card oldCard, final Card newCard) {
        handler.sendEmptyMessage(updateCardStart);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "updateCard start");
                if (mDataManager.isAuthorized()) {
                    if (!oldCard.equals(newCard)) {
                        oldCard.update(newCard);
                        DatabaseOperation.updateCard(oldCard);
                        History history = new History(oldCard, newCard);
                        DatabaseOperation.saveHistory(history);
                        JobInitiation.addHistory(history.getId());
                        JobInitiation.updateCard(oldCard.getId());
                    }
                    handler.sendEmptyMessage(updateCardFinished);
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(userHasNotBeenAuthorized);
                }
            }
        }).start();
    }

    public void deleteCard(final Card card) {
        handler.sendEmptyMessage(deleteCardStart);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "deleteCard start");
                if (mDataManager.isAuthorized()) {
                    JobInitiation.deleteCard(card.getId());
                    DatabaseOperation.deleteCard(card);
                    handler.sendEmptyMessage(deleteCardFinished);
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(userHasNotBeenAuthorized);
                }
            }
        }).start();
    }

    public void createContact(final Card card) {
        handler.sendEmptyMessage(createContactStart);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "createContact for contact: " + card.getRemoteId());
                if (mDataManager.isAuthorized()) {
                    card.setMy(false);
                    DatabaseOperation.createCard(card);
                    JobInitiation.addContact(card.getRemoteId());
                    handler.sendEmptyMessage(createContactFinished);
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(userHasNotBeenAuthorized);
                }
            }
        }).start();
    }

    public void updateContactComment(final Comment comment) {
        handler.sendEmptyMessage(updateContactCommentStart);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "updateContactComment start");
                if (mDataManager.isAuthorized()) {
                    DatabaseOperation.updateContactComment(comment);
                    JobInitiation.updateContactComment(comment.getId());
                    handler.sendEmptyMessage(updateContactCommentFinished);
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(userHasNotBeenAuthorized);
                }
            }
        }).start();
    }

    public void deleteContact(final Card card) {
        handler.sendEmptyMessage(deleteContactStart);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "deleteContact");
                if (mDataManager.isAuthorized()) {
                    List<Group> groups = DatabaseOperation.getGroupsWhereContact(card);
                    if (groups != null && !groups.isEmpty()) {
                        for (Group group : groups) {
                            JobInitiation.deleteContactFromGroup(group.getRemoteId(), card.getRemoteId());
                            DatabaseOperation.deleteContactFromGroup(group, card);
                        }
                    }
                    JobInitiation.deleteContact(card.getRemoteId());
                    DatabaseOperation.deleteCard(card);
                    handler.sendEmptyMessage(deleteContactFinished);
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(userHasNotBeenAuthorized);
                }
            }
        }).start();
    }

    public void createGroup(final Group group, @Nullable final List<Card> contacts) {
        handler.sendEmptyMessage(createGroupStart);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "createGroup start");
                if (mDataManager.isAuthorized()) {
                    DatabaseOperation.createGroup(group, contacts);
                    List<String> ids = new ArrayList<>();
                    if (contacts != null && contacts.size() > 0) {
                        for (Card card : contacts) {
                            ids.add(card.getRemoteId());
                        }
                    }
                    JobInitiation.createGroup(group.getId(), ids);
                    handler.sendEmptyMessage(createGroupFinished);
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(userHasNotBeenAuthorized);
                }
            }
        }).start();
    }

    public void updateGroup(final Group oldGroup, final Group newGroup) {
        handler.sendEmptyMessage(updateGroupStart);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "updateGroup start");
                if (mDataManager.isAuthorized()) {
                    oldGroup.update(newGroup);
                    DatabaseOperation.updateGroup(oldGroup);
                    JobInitiation.updateGroup(oldGroup.getId());
                    handler.sendEmptyMessage(updateGroupFinished);
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(userHasNotBeenAuthorized);
                }
            }
        }).start();
    }

    public void updateGroupContacts(final Group group, final List<Card> contacts) {
        handler.sendEmptyMessage(updateGroupContactsStart);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "updateGroupContacts start");
                if (mDataManager.isAuthorized()) {
                    DatabaseOperation.updateGroupContacts(group, contacts);
                    List<String> ids = new ArrayList<>();
                    if (contacts != null && contacts.size() > 0) {
                        for (Card card : contacts) {
                            ids.add(card.getRemoteId());
                        }
                    }
                    JobInitiation.updateGroupContacts(group.getRemoteId(), ids);
                    handler.sendEmptyMessage(updateGroupContactsFinished);
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(userHasNotBeenAuthorized);
                }
            }
        }).start();
    }

    public void addContactToGroup(final Group group, final Card contact) {
        handler.sendEmptyMessage(addContactToGroupStart);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "addContactToGroup start");
                if (mDataManager.isAuthorized()) {
                    DatabaseOperation.addContactToGroup(group, contact);
                    JobInitiation.addContactToGroup(group.getRemoteId(), contact.getRemoteId());
                    handler.sendEmptyMessage(addContactToGroupFinished);
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(userHasNotBeenAuthorized);
                }
            }
        }).start();
    }

    public void addContactsToGroup(final Group group, final List<Card> contacts) {
        handler.sendEmptyMessage(addContactToGroupStart);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "addContactsToGroup start");
                if (mDataManager.isAuthorized()) {
                    for (Card contact : contacts) {
                        DatabaseOperation.addContactToGroup(group, contact);
                        JobInitiation.addContactToGroup(group.getRemoteId(), contact.getRemoteId());
                    }
                    handler.sendEmptyMessage(addContactToGroupFinished);
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(userHasNotBeenAuthorized);
                }
            }
        }).start();
    }

    public void deleteContactFromGroup(final Group group, final Card contact) {
        handler.sendEmptyMessage(deleteContactFromGroupStart);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "deleteContactFromGroup start");
                if (mDataManager.isAuthorized()) {
                    DatabaseOperation.deleteContactFromGroup(group, contact);

                    JobInitiation.deleteContactFromGroup(group.getRemoteId(), contact.getRemoteId());
                    handler.sendEmptyMessage(deleteContactFromGroupFinished);
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(userHasNotBeenAuthorized);
                }
            }
        }).start();
    }

    public void deleteGroup(final Group group) {
        handler.sendEmptyMessage(deleteGroupStart);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "deleteGroup start");
                if (mDataManager.isAuthorized()) {
                    JobInitiation.deleteGroup(group.getRemoteId());
                    DatabaseOperation.deleteGroup(group);
                    handler.sendEmptyMessage(deleteGroupFinished);
                } else {
                    Log.e(TAG, "User has not been authorized");
                    handler.sendEmptyMessage(userHasNotBeenAuthorized);
                }
            }
        }).start();
    }

    //From DB
    public List<Card> getCardList() {
        Log.d(TAG, "getCardList");
        if (mDataManager.isAuthorized()) {
            return DatabaseOperation.getCardList();
        } else {
            Log.e(TAG, "User has not been authorized");
            handler.sendEmptyMessage(userHasNotBeenAuthorized);
            return null;
        }
    }

    //From DB
    public Card getCard(Long id) {
        Log.d(TAG, "getCard for card: " + id);
        if (mDataManager.isAuthorized()) {
            return DatabaseOperation.getCard(id);
        } else {
            Log.e(TAG, "User has not been authorized");
            handler.sendEmptyMessage(userHasNotBeenAuthorized);
            return null;
        }
    }

    //From DB
    public List<Group> getGroupList() {
        Log.d(TAG, "getGroupList");
        if (mDataManager.isAuthorized()) {
            return DatabaseOperation.getGroupList();
        } else {
            Log.e(TAG, "User has not been authorized");
            handler.sendEmptyMessage(userHasNotBeenAuthorized);
            return null;
        }
    }

    //From DB
    public List<Card> getContacts() {
        Log.d(TAG, "getContacts");
        if (mDataManager.isAuthorized()) {
            return DatabaseOperation.getContactList();
        } else {
            Log.e(TAG, "User has not been authorized");
            handler.sendEmptyMessage(userHasNotBeenAuthorized);
            return null;
        }
    }

    //From DB
    public Group getGroup(Long id) {
        Log.d(TAG, "getGroup for card: " + id);
        if (mDataManager.isAuthorized()) {
            return DatabaseOperation.getGroup(id);
        } else {
            Log.e(TAG, "User has not been authorized");
            handler.sendEmptyMessage(userHasNotBeenAuthorized);
            return null;
        }
    }

    //From DB
    public List<Card> getGroupContacts(Group group) {
        Log.d(TAG, "getGroupContacts for group: " + group.getId());
        if (mDataManager.isAuthorized()) {
            return DatabaseOperation.getGroupContacts(group);
        } else {
            Log.e(TAG, "User has not been authorized");
            handler.sendEmptyMessage(userHasNotBeenAuthorized);
            return null;
        }
    }

    public List<Card> getContactsForGroup(Group group) {
        Log.d(TAG, "getContactsForGroup for group: " + group.getId());
        if (mDataManager.isAuthorized()) {
            List<Card> allContacts = DatabaseOperation.getContactList();
            allContacts.removeAll(DatabaseOperation.getGroupContacts(group));
            return allContacts;
        } else {
            Log.e(TAG, "User has not been authorized");
            handler.sendEmptyMessage(userHasNotBeenAuthorized);
            return null;
        }
    }
}
