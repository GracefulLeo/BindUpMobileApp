package com.example.rrty6.vcardapp.data.operations;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.util.Log;

import static com.example.rrty6.vcardapp.utils.Const.JobConsts.*;

public class JobBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "JobBroadcastReceiver";
    private static int sJobId = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName jobService = new ComponentName(context, MyJobService.class);
        JobInfo.Builder exerciseJobBuilder = new JobInfo.Builder(sJobId++, jobService);
        exerciseJobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        exerciseJobBuilder.setRequiresDeviceIdle(false);
        exerciseJobBuilder.setRequiresCharging(false);

        PersistableBundle bundle = new PersistableBundle();
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        switch (intent.getAction()) {
            case ACTION_PERFORM_CREATE_CARD:
                Log.i(TAG, "ACTION_PERFORM_CREATE_CARD");
                bundle.putString(ACTION, ACTION_CREATE_CARD);
                bundle.putLong(CARD_ID, intent.getLongExtra(CARD_ID, 0));
                exerciseJobBuilder.setExtras(bundle);
                jobScheduler.schedule(exerciseJobBuilder.build());
                break;
            case ACTION_PERFORM_UPDATE_CARD:
                Log.i(TAG, "ACTION_PERFORM_UPDATE_CARD");
                bundle.putString(ACTION, ACTION_UPDATE_CARD);
                bundle.putLong(CARD_ID, intent.getLongExtra(CARD_ID, 0));
                exerciseJobBuilder.setExtras(bundle);
                jobScheduler.schedule(exerciseJobBuilder.build());
                break;
            case ACTION_PERFORM_DELETE_CARD:
                Log.i(TAG, "ACTION_PERFORM_DELETE_CARD");
                bundle.putString(ACTION, ACTION_DELETE_CARD);
                bundle.putString(CARD_REMOTE_ID, intent.getStringExtra(CARD_REMOTE_ID));
                exerciseJobBuilder.setExtras(bundle);
                jobScheduler.schedule(exerciseJobBuilder.build());
                break;
            case ACTION_PERFORM_ADD_CONTACT:
                Log.i(TAG, "ACTION_PERFORM_ADD_CONTACT");
                bundle.putString(ACTION, ACTION_ADD_CONTACT);
                bundle.putString(CONTACT_REMOTE_ID, intent.getStringExtra(CONTACT_REMOTE_ID));
                exerciseJobBuilder.setExtras(bundle);
                jobScheduler.schedule(exerciseJobBuilder.build());
                break;
            case ACTION_PERFORM_DELETE_CONTACT:
                Log.i(TAG, "ACTION_PERFORM_DELETE_CONTACT");
                bundle.putString(ACTION, ACTION_DELETE_CONTACT);
                bundle.putString(CONTACT_REMOTE_ID, intent.getStringExtra(CONTACT_REMOTE_ID));
                exerciseJobBuilder.setExtras(bundle);
                jobScheduler.schedule(exerciseJobBuilder.build());
                break;
            case ACTION_PERFORM_CREATE_GROUP:
                Log.i(TAG, "ACTION_PERFORM_CREATE_GROUP");
                bundle.putString(ACTION, ACTION_CREATE_GROUP);
                bundle.putLong(GROUP_ID, intent.getLongExtra(GROUP_ID, 0));
                bundle.putStringArray(GROUP_CONTACTS_IDS, (String[]) intent.getStringArrayListExtra(GROUP_CONTACTS_IDS).toArray());
                exerciseJobBuilder.setExtras(bundle);
                jobScheduler.schedule(exerciseJobBuilder.build());
                break;
            case ACTION_PERFORM_UPDATE_GROUP:
                Log.i(TAG, "ACTION_PERFORM_UPDATE_GROUP");
                bundle.putString(ACTION, ACTION_UPDATE_GROUP);
                bundle.putLong(GROUP_ID, intent.getLongExtra(GROUP_ID, 0));
                exerciseJobBuilder.setExtras(bundle);
                jobScheduler.schedule(exerciseJobBuilder.build());
                break;
            case ACTION_PERFORM_UPDATE_GROUP_CONTACTS:
                Log.i(TAG, "ACTION_PERFORM_UPDATE_GROUP_CONTACTS");
                bundle.putString(ACTION, ACTION_UPDATE_GROUP_CONTACTS);
                bundle.putString(GROUP_REMOTE_ID, intent.getStringExtra(GROUP_REMOTE_ID));
                bundle.putStringArray(GROUP_CONTACTS_IDS, (String[]) intent.getStringArrayListExtra(GROUP_CONTACTS_IDS).toArray());
                exerciseJobBuilder.setExtras(bundle);
                jobScheduler.schedule(exerciseJobBuilder.build());
                break;
            case ACTION_PERFORM_DELETE_GROUP:
                Log.i(TAG, "ACTION_PERFORM_DELETE_GROUP");
                bundle.putString(ACTION, ACTION_DELETE_GROUP);
                bundle.putString(GROUP_REMOTE_ID, intent.getStringExtra(GROUP_REMOTE_ID));
                exerciseJobBuilder.setExtras(bundle);
                jobScheduler.schedule(exerciseJobBuilder.build());
                break;
            default:
                Log.e(TAG, "Unknown action: " + intent.getAction());
        }
    }
}
