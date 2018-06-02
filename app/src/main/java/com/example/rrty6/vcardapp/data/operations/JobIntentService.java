package com.example.rrty6.vcardapp.data.operations;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.rrty6.vcardapp.data.network.NetworkOperations;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Group;
import com.example.rrty6.vcardapp.data.storage.operation.DatabaseOperation;

import java.sql.SQLException;

import static com.example.rrty6.vcardapp.data.operations.MyJobService.IS_SUCCESS;
import static com.example.rrty6.vcardapp.data.operations.MyJobService.MY_BC_RCVR;
import static com.example.rrty6.vcardapp.utils.Const.JobConsts.*;

public class JobIntentService extends IntentService {
    private static final String TAG = "JobIntentService";

    public JobIntentService() {
        super("BindUpIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Intent successIntent = null;
            boolean isSuccess = false;
            Card card = null;
            Group group = null;
            switch (action) {
                case ACTION_EXECUTE_CREATE_CARD:
                    Log.i(TAG, "ACTION_EXECUTE_CREATE_CARD begin");
                    card = DatabaseOperation.getCard(intent.getLongExtra(CARD_ID, 0));
                    isSuccess = NetworkOperations.createCard(card);
                    successIntent = new Intent(MY_BC_RCVR + ACTION_EXECUTE_CREATE_CARD);
                    successIntent.putExtra(IS_SUCCESS, isSuccess);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(successIntent);
                    Log.i(TAG, "ACTION_EXECUTE_CREATE_CARD end");
                    break;
                case ACTION_EXECUTE_UPDATE_CARD:
                    Log.i(TAG, "ACTION_EXECUTE_UPDATE_CARD begin");
                    card = DatabaseOperation.getCard(intent.getLongExtra(CARD_ID, 0));
                    isSuccess = NetworkOperations.updateCard(card);
                    successIntent = new Intent(MY_BC_RCVR + ACTION_EXECUTE_UPDATE_CARD);
                    successIntent.putExtra(IS_SUCCESS, isSuccess);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(successIntent);
                    Log.i(TAG, "ACTION_EXECUTE_UPDATE_CARD end");
                    break;
                case ACTION_EXECUTE_DELETE_CARD:
                    Log.i(TAG, "ACTION_EXECUTE_DELETE_CARD begin");
                    isSuccess = NetworkOperations.deleteCard(intent.getStringExtra(CARD_REMOTE_ID));
                    successIntent = new Intent(MY_BC_RCVR + ACTION_EXECUTE_DELETE_CARD);
                    successIntent.putExtra(IS_SUCCESS, isSuccess);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(successIntent);
                    Log.i(TAG, "ACTION_EXECUTE_DELETE_CARD end");
                    break;
                case ACTION_EXECUTE_ADD_CONTACT:
                    Log.i(TAG, "ACTION_EXECUTE_ADD_CONTACT begin");
                    isSuccess = NetworkOperations.addContact(intent.getStringExtra(CONTACT_REMOTE_ID));
                    successIntent = new Intent(MY_BC_RCVR + ACTION_EXECUTE_ADD_CONTACT);
                    successIntent.putExtra(IS_SUCCESS, isSuccess);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(successIntent);
                    Log.i(TAG, "ACTION_EXECUTE_ADD_CONTACT end");
                    break;
                case ACTION_EXECUTE_DELETE_CONTACT:
                    Log.i(TAG, "ACTION_EXECUTE_DELETE_CONTACT begin");
                    isSuccess = NetworkOperations.deleteContact(intent.getStringExtra(CONTACT_REMOTE_ID));
                    successIntent = new Intent(MY_BC_RCVR + ACTION_EXECUTE_DELETE_CONTACT);
                    successIntent.putExtra(IS_SUCCESS, isSuccess);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(successIntent);
                    Log.i(TAG, "ACTION_EXECUTE_DELETE_CONTACT end");
                    break;
                case ACTION_EXECUTE_CREATE_GROUP:
                    Log.i(TAG, "ACTION_EXECUTE_CREATE_GROUP begin");
                    group = DatabaseOperation.getGroup(intent.getLongExtra(GROUP_ID, 0));
                    isSuccess = NetworkOperations.createGroup(group, intent.getStringArrayListExtra(GROUP_CONTACTS_IDS));
                    successIntent = new Intent(MY_BC_RCVR + ACTION_EXECUTE_CREATE_GROUP);
                    successIntent.putExtra(IS_SUCCESS, isSuccess);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(successIntent);
                    Log.i(TAG, "ACTION_EXECUTE_CREATE_GROUP end");
                    break;
                case ACTION_EXECUTE_UPDATE_GROUP:
                    Log.i(TAG, "ACTION_EXECUTE_UPDATE_GROUP begin");
                    group = DatabaseOperation.getGroup(intent.getLongExtra(GROUP_ID, 0));
                    isSuccess = NetworkOperations.updateGroup(group);
                    successIntent = new Intent(MY_BC_RCVR + ACTION_EXECUTE_UPDATE_GROUP);
                    successIntent.putExtra(IS_SUCCESS, isSuccess);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(successIntent);
                    Log.i(TAG, "ACTION_EXECUTE_UPDATE_GROUP end");
                    break;
                case ACTION_EXECUTE_UPDATE_GROUP_CONTACTS:
                    Log.i(TAG, "ACTION_EXECUTE_UPDATE_GROUP_CONTACTS begin");
                    isSuccess = NetworkOperations.updateGroupContacts(DatabaseOperation.getGroup(intent.getLongExtra(GROUP_ID,0)));
                    successIntent = new Intent(MY_BC_RCVR + ACTION_EXECUTE_UPDATE_GROUP_CONTACTS);
                    successIntent.putExtra(IS_SUCCESS, isSuccess);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(successIntent);
                    Log.i(TAG, "ACTION_EXECUTE_UPDATE_GROUP_CONTACTS end");
                    break;
                case ACTION_EXECUTE_DELETE_GROUP:
                    Log.i(TAG, "ACTION_EXECUTE_DELETE_GROUP begin");
                    isSuccess = NetworkOperations.deleteGroup(intent.getStringExtra(GROUP_REMOTE_ID));
                    successIntent = new Intent(MY_BC_RCVR + ACTION_EXECUTE_DELETE_GROUP);
                    successIntent.putExtra(IS_SUCCESS, isSuccess);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(successIntent);
                    Log.i(TAG, "ACTION_EXECUTE_DELETE_GROUP end");
                    break;
            }
        }
    }
}
