package com.bindup.vcard.vcardapp.data.operations;

import android.content.Intent;
import android.util.Log;

import com.bindup.vcard.vcardapp.data.storage.model.Card;
import com.bindup.vcard.vcardapp.data.storage.model.Group;
import com.bindup.vcard.vcardapp.data.storage.model.History;
import com.bindup.vcard.vcardapp.utils.App;

import java.util.ArrayList;
import java.util.List;

import static com.bindup.vcard.vcardapp.utils.Const.JobConsts.*;

public class JobInitiation {
    private static final String TAG = "JobInitiation";

    public static void createCard(Long cardId) {
        Log.i(TAG, "Create card");
        Intent createCardIntentForBroadcast = new Intent(App.getContext(), JobBroadcastReceiver.class);
        createCardIntentForBroadcast.putExtra(CARD_ID, cardId);
//        createCardIntentForBroadcast.putExtra(LOGO_ID, card.getLogo().getId());
        createCardIntentForBroadcast.setAction(ACTION_PERFORM_CREATE_CARD);
        App.getContext().sendBroadcast(createCardIntentForBroadcast);
    }

    public static void updateCard(Long cardId) {
        Log.i(TAG, "Update Card");
        Intent updateCardIntentForBroadcast = new Intent(App.getContext(), JobBroadcastReceiver.class);
        updateCardIntentForBroadcast.putExtra(CARD_ID, cardId);
        updateCardIntentForBroadcast.setAction(ACTION_PERFORM_UPDATE_CARD);
        App.getContext().sendBroadcast(updateCardIntentForBroadcast);
    }

    public static void deleteCard(Long cardId) {
        Log.i(TAG, "Delete Card");
        Intent deleteCardIntentForBroadcast = new Intent(App.getContext(), JobBroadcastReceiver.class);
        deleteCardIntentForBroadcast.putExtra(CARD_ID, cardId);
        deleteCardIntentForBroadcast.setAction(ACTION_PERFORM_DELETE_CARD);
        App.getContext().sendBroadcast(deleteCardIntentForBroadcast);
    }

    public static void addContact(String remoteId) {
        Log.i(TAG, "Add contact");
        Intent addContactIntentForBroadcast = new Intent(App.getContext(), JobBroadcastReceiver.class);
        addContactIntentForBroadcast.putExtra(CONTACT_REMOTE_ID, remoteId);
        addContactIntentForBroadcast.setAction(ACTION_PERFORM_ADD_CONTACT);
        App.getContext().sendBroadcast(addContactIntentForBroadcast);
    }

    public static void deleteContact(String remoteId) {
        Log.i(TAG, "deleteContact");
        Intent deleteContactIntentForBroadcast = new Intent(App.getContext(), JobBroadcastReceiver.class);
        deleteContactIntentForBroadcast.putExtra(CONTACT_REMOTE_ID, remoteId);
        deleteContactIntentForBroadcast.setAction(ACTION_PERFORM_DELETE_CONTACT);
        App.getContext().sendBroadcast(deleteContactIntentForBroadcast);
    }

    public static void createGroup(Long groupId, List<String> cardList) {
        Log.i(TAG, "Create group");
        Intent createGroupIntentForBroadcast = new Intent(App.getContext(), JobBroadcastReceiver.class);
        createGroupIntentForBroadcast.putExtra(GROUP_ID, groupId);
        createGroupIntentForBroadcast.setAction(ACTION_PERFORM_CREATE_GROUP);
        App.getContext().sendBroadcast(createGroupIntentForBroadcast);
    }

    public static void updateGroup(Long groupId) {
        Log.i(TAG, "updateGroup");
        Intent updateCardIntentForBroadcast = new Intent(App.getContext(), JobBroadcastReceiver.class);
        updateCardIntentForBroadcast.putExtra(GROUP_ID, groupId);
        updateCardIntentForBroadcast.setAction(ACTION_PERFORM_UPDATE_GROUP);
        App.getContext().sendBroadcast(updateCardIntentForBroadcast);
    }

    public static void updateGroupContacts(String groupRemoteId, List<String> cardList) {
        Log.i(TAG, "updateGroupContacts");
        Intent createGroupIntentForBroadcast = new Intent(App.getContext(), JobBroadcastReceiver.class);
        createGroupIntentForBroadcast.putExtra(GROUP_ID, groupRemoteId);
        createGroupIntentForBroadcast.setAction(ACTION_PERFORM_UPDATE_GROUP_CONTACTS);
        App.getContext().sendBroadcast(createGroupIntentForBroadcast);
    }

    //TODO: Play around the case if group just
    public static void addContactToGroup(String groupRemoteId, String contactRemoteId) {
        Log.i(TAG, "addContactToGroup");
        Intent createGroupIntentForBroadcast = new Intent(App.getContext(), JobBroadcastReceiver.class);
        createGroupIntentForBroadcast.putExtra(GROUP_REMOTE_ID, groupRemoteId);
        createGroupIntentForBroadcast.putExtra(CONTACT_REMOTE_ID, contactRemoteId);
        createGroupIntentForBroadcast.setAction(ACTION_PERFORM_ADD_CONTACT_TO_GROUP);
        App.getContext().sendBroadcast(createGroupIntentForBroadcast);
    }

    public static void deleteContactFromGroup(String groupRemoteId, String contactRemoteId) {
        Log.i(TAG, "deleteContactFromGroup");
        Intent createGroupIntentForBroadcast = new Intent(App.getContext(), JobBroadcastReceiver.class);
        createGroupIntentForBroadcast.putExtra(GROUP_REMOTE_ID, groupRemoteId);
        createGroupIntentForBroadcast.putExtra(CONTACT_REMOTE_ID, contactRemoteId);
        createGroupIntentForBroadcast.setAction(ACTION_PERFORM_DELETE_CONTACT_FROM_GROUP);
        App.getContext().sendBroadcast(createGroupIntentForBroadcast);
    }

    public static void deleteGroup(String groupRemoteId) {
        Log.i(TAG, "deleteGroup");
        Intent deleteCardIntentForBroadcast = new Intent(App.getContext(), JobBroadcastReceiver.class);
        deleteCardIntentForBroadcast.putExtra(GROUP_REMOTE_ID, groupRemoteId);
        deleteCardIntentForBroadcast.setAction(ACTION_PERFORM_DELETE_GROUP);
        App.getContext().sendBroadcast(deleteCardIntentForBroadcast);
    }

    public static void updateContactComment(Long commentId) {
        Log.i(TAG, "updateContactComment");
        Intent deleteCardIntentForBroadcast = new Intent(App.getContext(), JobBroadcastReceiver.class);
        deleteCardIntentForBroadcast.putExtra(CONTACT_COMMENT_ID, commentId);
        deleteCardIntentForBroadcast.setAction(ACTION_PERFORM_UPDATE_CONTACT_COMMENT);
        App.getContext().sendBroadcast(deleteCardIntentForBroadcast);
    }

    public static void addHistory(Long historyId) {
        Log.i(TAG, "addHistory");
        Intent deleteCardIntentForBroadcast = new Intent(App.getContext(), JobBroadcastReceiver.class);
        deleteCardIntentForBroadcast.putExtra(HISTORY_ID, historyId);
        deleteCardIntentForBroadcast.setAction(ACTION_PERFORM_ADD_HISTORY);
        App.getContext().sendBroadcast(deleteCardIntentForBroadcast);
    }
}
