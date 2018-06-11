package com.bindup.vcard.vcardapp.data.operations;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.bindup.vcard.vcardapp.utils.App;

import java.util.ArrayList;
import java.util.Arrays;

import static com.bindup.vcard.vcardapp.utils.Const.JobConsts.*;

public class MyJobService extends JobService {
    private static final String TAG = "MyJobService";
    public static final String IS_SUCCESS = "isSuccess";
    public static final String MY_BC_RCVR = "MyBroadcastReceiver";
    private JobParameters mParams;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isSuccess = false;
            if (intent.hasExtra(IS_SUCCESS)) {
                isSuccess = intent.getBooleanExtra(IS_SUCCESS, false);
            }
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
            jobFinished(mParams, !isSuccess);
        }
    };

    @Override
    public boolean onStartJob(JobParameters params) {
        mParams = params;
        PersistableBundle bundle = params.getExtras();
        String action = bundle.getString(ACTION, null);

        Intent intent = new Intent(App.getContext(), JobIntentService.class);
        switch (action) {
            case ACTION_CREATE_CARD:
                Log.i(TAG, "ACTION_CREATE_CARD");
                LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(MY_BC_RCVR + ACTION_EXECUTE_CREATE_CARD));
                intent.putExtra(CARD_ID, bundle.getLong(CARD_ID));
                intent.setAction(ACTION_EXECUTE_CREATE_CARD);
                App.getContext().startService(intent);
                return false;
            case ACTION_UPDATE_CARD:
                Log.i(TAG, "ACTION_UPDATE_CARD");
                LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(MY_BC_RCVR + ACTION_EXECUTE_UPDATE_CARD));
                intent.putExtra(CARD_ID, bundle.getLong(CARD_ID));
                intent.setAction(ACTION_EXECUTE_UPDATE_CARD);
                App.getContext().startService(intent);
                return false;
            case ACTION_DELETE_CARD:
                Log.i(TAG, "ACTION_DELETE_CARD");
                LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(MY_BC_RCVR + ACTION_EXECUTE_DELETE_CARD));
                intent.putExtra(CARD_REMOTE_ID, bundle.getString(CARD_REMOTE_ID));
                intent.setAction(ACTION_EXECUTE_DELETE_CARD);
                App.getContext().startService(intent);
                return false;
            case ACTION_ADD_CONTACT:
                Log.i(TAG, "ACTION_ADD_CONTACT");
                LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(MY_BC_RCVR + ACTION_EXECUTE_ADD_CONTACT));
                intent.putExtra(CONTACT_REMOTE_ID, bundle.getString(CONTACT_REMOTE_ID));
                intent.setAction(ACTION_EXECUTE_ADD_CONTACT);
                App.getContext().startService(intent);
                return false;
            case ACTION_DELETE_CONTACT:
                Log.i(TAG, "ACTION_DELETE_CONTACT");
                LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(MY_BC_RCVR + ACTION_EXECUTE_DELETE_CONTACT));
                intent.putExtra(CONTACT_REMOTE_ID, bundle.getString(CONTACT_REMOTE_ID));
                intent.setAction(ACTION_EXECUTE_DELETE_CONTACT);
                App.getContext().startService(intent);
                return false;
            case ACTION_CREATE_GROUP:
                Log.i(TAG, "ACTION_CREATE_GROUP");
                LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(MY_BC_RCVR + ACTION_EXECUTE_CREATE_GROUP));
                intent.putExtra(GROUP_ID, bundle.getLong(GROUP_ID));
                intent.setAction(ACTION_EXECUTE_CREATE_GROUP);
                App.getContext().startService(intent);
                return false;
            case ACTION_UPDATE_GROUP:
                Log.i(TAG, "ACTION_UPDATE_GROUP");
                LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(MY_BC_RCVR + ACTION_EXECUTE_UPDATE_GROUP));
                intent.putExtra(GROUP_ID, bundle.getLong(GROUP_ID));
                intent.setAction(ACTION_EXECUTE_UPDATE_GROUP);
                App.getContext().startService(intent);
                return false;
            case ACTION_UPDATE_GROUP_CONTACTS:
                Log.i(TAG, "ACTION_UPDATE_GROUP_CONTACTS");
                LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(MY_BC_RCVR + ACTION_EXECUTE_UPDATE_GROUP_CONTACTS));
                intent.putExtra(GROUP_REMOTE_ID, bundle.getLong(GROUP_ID));
                intent.setAction(ACTION_EXECUTE_UPDATE_GROUP_CONTACTS);
                App.getContext().startService(intent);
                return false;
            case ACTION_ADD_CONTACT_TO_GROUP:
                Log.i(TAG, "ACTION_ADD_CONTACT_TO_GROUP");
                LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(MY_BC_RCVR + ACTION_EXECUTE_ADD_CONTACT_TO_GROUP));
                intent.putExtra(GROUP_REMOTE_ID, bundle.getString(GROUP_REMOTE_ID));
                intent.putExtra(CONTACT_REMOTE_ID, bundle.getString(CONTACT_REMOTE_ID));
                intent.setAction(ACTION_EXECUTE_ADD_CONTACT_TO_GROUP);
                App.getContext().startService(intent);
                return false;
            case ACTION_DELETE_CONTACT_FROM_GROUP:
                Log.i(TAG, "ACTION_DELETE_CONTACT_FROM_GROUP");
                LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(MY_BC_RCVR + ACTION_EXECUTE_DELETE_CONTACT_FROM_GROUP));
                intent.putExtra(GROUP_REMOTE_ID, bundle.getString(GROUP_REMOTE_ID));
                intent.putExtra(CONTACT_REMOTE_ID, bundle.getString(CONTACT_REMOTE_ID));
                intent.setAction(ACTION_EXECUTE_DELETE_CONTACT_FROM_GROUP);
                App.getContext().startService(intent);
                return false;
            case ACTION_DELETE_GROUP:
                Log.i(TAG, "ACTION_DELETE_GROUP");
                LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(MY_BC_RCVR + ACTION_EXECUTE_DELETE_GROUP));
                intent.putExtra(GROUP_REMOTE_ID, bundle.getString(GROUP_REMOTE_ID));
                intent.setAction(ACTION_EXECUTE_DELETE_GROUP);
                App.getContext().startService(intent);
                return false;
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
