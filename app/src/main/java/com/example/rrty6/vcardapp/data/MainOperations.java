package com.example.rrty6.vcardapp.data;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.rrty6.vcardapp.data.managers.DataManager;
import com.example.rrty6.vcardapp.data.network.NetworkOperations;
import com.example.rrty6.vcardapp.data.network.model.req.UploadLogoReq.Filepath;
import com.example.rrty6.vcardapp.data.storage.CardCompare;
import com.example.rrty6.vcardapp.data.storage.GroupCompare;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Group;
import com.example.rrty6.vcardapp.data.storage.operation.DatabaseOperation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainOperations {
    private static final DataManager DATA_MANAGER = DataManager.getInstance();

    public static boolean isAuthorized() {
        return DATA_MANAGER.isAuthorized();
    }

    private static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void register(String email, String password) throws Exception {
        if (!DATA_MANAGER.isAuthorized()) {
            if (isValidEmail(email)) {
                NetworkOperations.register(email, password);
            } else {
                throw new Exception("Email is not valid");
            }
        }
    }

    public static void login(String email, String password) throws Exception {
        if (!DATA_MANAGER.isAuthorized()) {
            if (isValidEmail(email)) {
                NetworkOperations.signIn(email, password);
            } else {
                throw new Exception("Email is not valid");
            }
        }
    }

    public static void logout() throws Exception {
        if (DATA_MANAGER.isAuthorized()) {
            NetworkOperations.logOut();
        }
    }

    public static void downloadUserData() throws SQLException {
        if (DATA_MANAGER.isAuthorized()) {
            for (Card card : NetworkOperations.downloadMyCards()) {
                DatabaseOperation.saveCard(card);
            }
            for (Card card : NetworkOperations.downloadMyContacts()) {
                DatabaseOperation.saveCard(card);
            }
            for (Group group : NetworkOperations.getMyGroups()) {
                DatabaseOperation.createGroup(group, null);
                for (String cardRemoteId : NetworkOperations.getGroupContacts(group)) {
                    DataManager.getInstance().addContactToGroup(group, DataManager.getInstance().getCardFromDB(cardRemoteId));
                }
            }
        }
    }

    public static void updateUserData() throws SQLException {
        if (DATA_MANAGER.isAuthorized()) {
            List<Card> cards = DATA_MANAGER.getCardList();
            List<String> strings = new ArrayList<>();
            for (Card card : cards) {
                strings.add(card.getRemoteId());
            }
            for (Card card : NetworkOperations.downloadMyCards()) {
                if (!strings.contains(card.getRemoteId())) {
                    DatabaseOperation.saveCard(card);
                }
            }
            for (Card card : NetworkOperations.downloadMyContacts()) {
                if (!strings.contains(card.getRemoteId())) {
                    DatabaseOperation.saveCard(card);
                }
            }
            strings.clear();
            for (Group group : DATA_MANAGER.getGroupList()) {
                strings.add(group.getRemoteId());
            }
            for (Group group : NetworkOperations.getMyGroups()) {
                if (!strings.contains(group.getRemoteId())) {
                    DatabaseOperation.createGroup(group, null);
                    for (String cardRemoteId : NetworkOperations.getGroupContacts(group)) {
                        DataManager.getInstance().addContactToGroup(group, DataManager.getInstance().getCardFromDB(cardRemoteId));
                    }
                }
            }
        }
    }//TODO: test

    //For UI method
    public static void createCard(final Card card) throws Exception {
        if (DATA_MANAGER.isAuthorized()) {
            //Checkout field name shouldn't be null
            if (card.getName() != null && !card.getName().isEmpty()) {
                //Checkout field surname shouldn't be null
                if (card.getSurname() != null && !card.getSurname().isEmpty()) {
                    //Buffer for throwing exception out of method
                    final SQLException[] exception = {null};
                    //Call.execute in .createLogo and .createCard method needs to run in asynctask/parallel thread
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                //Setup marker that it is myCard
                                card.setMy(true);
                                //Saving to DB
                                DatabaseOperation.createCard(card);

                                //Uploading logo to server and getting foreign Id
                                card.setLogo(NetworkOperations.createLogo(card.getLogo(), Filepath.VCARDS));

                                NetworkOperations.createCard(card);
                            } catch (SQLException e) {
                                exception[0] = new SQLException(e);
                            }
                        }
                    });

                    thread.start();//Starts the thread
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (exception[0] != null) {
                        throw exception[0];
                    }


                } else {
                    throw new Exception("Invalid method parameter: Card's String Surname can't be null or empty");
                }
            } else {
                throw new Exception("Invalid method parameter: Card's String Name can't be null or empty");
            }
        }
    }

    //For UI method
    public static void updateCard(final Card oldCard, Card newCard) throws SQLException {
        if (DATA_MANAGER.isAuthorized()) {
            CardCompare compare = new CardCompare(oldCard, newCard);
            oldCard.update(newCard);
            if (compare.fieldLogotype) {
                //Call.execute in .createLogo and .createCard method needs to run in asynctask/parallel thread
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //Uploading logo to server and getting foreign Id
                        oldCard.setLogo(NetworkOperations.createLogo(oldCard.getLogo(), Filepath.VCARDS));
                    }
                });

                thread.start();//Starts the thread
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            DataManager.getInstance().updateCard(oldCard);
            NetworkOperations.updateCard(oldCard, compare);
        }
    }

    //For UI method
    public static void deleteCard(final Card card) throws SQLException {
        if (DATA_MANAGER.isAuthorized()) {
            if (card.getRemoteId() != null && !card.getRemoteId().isEmpty()) {
                NetworkOperations.deleteCard(card);

                DatabaseOperation.deleteCard(card);
            }
        }
    }

    //For UI method
    public static void createContact(final Card card) throws Exception {
        if (DATA_MANAGER.isAuthorized()) {
            if (card.getRemoteId() == null || card.getRemoteId().isEmpty()) {
                throw new IllegalArgumentException("Invalid method parameter: incoming Card's remote Id can't be null or empty");
            }
            card.setMy(false);

            //Checkout field name shouldn't be null
            if (card.getName() != null && !card.getName().isEmpty()) {
                //Checkout field surname shouldn't be null
                if (card.getSurname() != null && !card.getSurname().isEmpty()) {
                    //Buffer for throwing exception out of method
                    final SQLException[] exception = {null};
                    //Call.execute in .createLogo and .createCard method needs to run in asynctask/parallel thread
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                //Setup marker that it is Contact
                                card.setMy(false);
                                //Saving to DB
                                DatabaseOperation.createCard(card);

                                NetworkOperations.addContact(card);
                            } catch (SQLException e) {
                                exception[0] = new SQLException(e);
                            }
                        }
                    });

                    thread.start();//Starts the thread
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (exception[0] != null) {
                        throw exception[0];
                    }
                } else {
                    throw new Exception("Invalid method parameter: Card's String Surname can't be null or empty");
                }
            } else {
                throw new Exception("Invalid method parameter: Card's String Name can't be null or empty");
            }
        }
    }

    public static void deleteContact(Card card) throws SQLException {
        if (DATA_MANAGER.isAuthorized()) {
            if (card.getRemoteId() != null && !card.getRemoteId().isEmpty()) {
                NetworkOperations.deleteContact(card);
            }
            DatabaseOperation.deleteCard(card);
        }
    }

    //For UI method
    public static void createGroup(final Group group, @Nullable final List<Card> contacts) throws SQLException {
        if (DATA_MANAGER.isAuthorized()) {
            //Checkout field name shouldn't be null
            if (group.getName() != null && !group.getName().isEmpty()) {

                //Buffer for throwing exception out of method
                final SQLException[] exception = {null};
                //Call.execute in .createLogo and .createCard method needs to run in asynctask/parallel thread
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            DatabaseOperation.createGroup(group, contacts);

                            //Uploading logo to server and getting foreign Id
                            group.setLogo(NetworkOperations.createLogo(group.getLogo(), Filepath.GROUPS));

                            NetworkOperations.createGroup(group, contacts);
                        } catch (SQLException e) {
                            exception[0] = new SQLException(e);
                        }
                    }
                });

                thread.start();//Starts the thread
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (exception[0] != null) {
                    throw exception[0];
                }
            } else {
                throw new IllegalArgumentException("Invalid method parameter: Group's String Name can't be null or empty");
            }
        }
    }

    public static void updateGroup(final Group oldGroup, Group newGroup) throws SQLException {
        if (DATA_MANAGER.isAuthorized()) {
            GroupCompare compare = new GroupCompare(oldGroup, newGroup);
            oldGroup.update(newGroup);
            if (compare.fieldLogotype) {
                //Call.execute in .createLogo and .createCard method needs to run in asynctask/parallel thread
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //Uploading logo to server and getting foreign Id
                        oldGroup.setLogo(NetworkOperations.createLogo(oldGroup.getLogo(), Filepath.VCARDS));
                    }
                });

                thread.start();//Starts the thread
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            DataManager.getInstance().updateGroup(oldGroup);
            NetworkOperations.updateGroup(oldGroup, compare);
        }
    }

    public static void updateGroupContacts(Group group, List<Card> contacts) throws SQLException {
        if (DATA_MANAGER.isAuthorized()) {
            DatabaseOperation.updateGroupContacts(group, contacts);
            NetworkOperations.updateGroupContacts(group, contacts);
        }
    }

    public static void deleteGroup(Group group) throws SQLException {
        if (DATA_MANAGER.isAuthorized()) {
            NetworkOperations.deleteGroup(group);
            DatabaseOperation.deleteGroup(group);
        }
    }

    //From DB
    public static List<Card> getCardList() throws SQLException {
        if (DATA_MANAGER.isAuthorized()) {
            return DataManager.getInstance().getCardList();
        } else {
            return null;
        }
    }

    public static Card getCard(Long id) throws SQLException {
        return DatabaseOperation.getCard(id);
    }

    //From DB
    public static List<Group> getGroupList() throws SQLException {
        if (DATA_MANAGER.isAuthorized()) {
            return DataManager.getInstance().getGroupList();
        } else {
            return null;
        }
    }

    //From DB
    public static List<Card> getContacts() throws SQLException {
        if (DATA_MANAGER.isAuthorized()) {
            return DataManager.getInstance().getContactList();
        } else {
            return null;
        }
    }

    //From DB
    public static List<Card> getGroupContacts(Group group) throws SQLException {
        if (DATA_MANAGER.isAuthorized()) {
            return DataManager.getInstance().getGroupContacts(group);
        } else {
            return null;
        }
    }
}
