package com.example.rrty6.vcardapp.data.exchange.WifiP2pExchange;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.util.Log;

public class StopNetworking extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            Log.d("YAZAN", ">>>>SUPPLICANT_STATE_CHANGED_ACTION<<<<<<");
            SupplicantState supl_state = ((SupplicantState) intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE));
            switch (supl_state) {
//                case ASSOCIATED:
//                    break;
                case ASSOCIATING:
                    wifi.disconnect();
                    break;
                case AUTHENTICATING:
                    wifi.disconnect();
                    break;
//                case COMPLETED:
//                    break;
//                case DISCONNECTED:
//                    break;
                case DORMANT:
                    wifi.disconnect();
                    break;
                case FOUR_WAY_HANDSHAKE:
                    wifi.disconnect();
                    break;
                case GROUP_HANDSHAKE:
                    wifi.disconnect();
                    break;
//                case INACTIVE:
//                    break;
//                case INTERFACE_DISABLED:
//                    break;
//                case INVALID:
//                    break;
//                case SCANNING:
//                    break;
//                case UNINITIALIZED:
//                    break;
//                default:
//                    break;
            }
        }

    }
}
