package com.example.rrty6.vcardapp.data.exchange.WifiP2pExchange;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.rrty6.vcardapp.data.MainOperations;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Email;
import com.example.rrty6.vcardapp.data.storage.model.Phone;
import com.example.rrty6.vcardapp.data.storage.model.SocialLink;
import com.example.rrty6.vcardapp.utils.App;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.logging.Handler;

public class WifiOperations implements WifiP2pManager.ConnectionInfoListener {
    private static WifiOperations instance;
    private String deviceName;
    private ArrayAdapter<WifiP2pDevice> adapter;


    private static boolean wasWifiEnabled = true;
    private int port = 8448;

//    private final IntentFilter intentFilter = new IntentFilter();
//    private final IntentFilter blockIntentFilter = new IntentFilter();
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
//    private BroadcastReceiver receiver = null;
//    private Activity activity = null;
    private Card receivedData = null;
    private Card toSendData = null;
    private boolean success = false;
    private BroadcastReceiver stopReceiver = new StopNetworking();
    android.os.Handler handler;

    public WifiOperations(android.os.Handler handler, Card data) throws Exception {
        if (instance == null) {
//            this.activity = activity;
            this.handler = handler;
            toSendData = data;
            instance = this;

            try {
//                adapter = new PeerAdapter(App.getContext(), android.R.layout.simple_list_item_1);
            } catch (Exception e) {
                adapter = PeerAdapter.getInstance();
            }

            manager = (WifiP2pManager) App.getContext().getSystemService(Context.WIFI_P2P_SERVICE);
            channel = manager.initialize(App.getContext(), App.getContext().getMainLooper(), null);

//            intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//            intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//            intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//            intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        } else {
            throw new Exception("Instance of WifiOperations is already exist");
        }
    }

    public static WifiOperations getInstance() {
        if (instance != null) {
            return instance;
        } else {
            return null;
        }
    }

    public ArrayAdapter<WifiP2pDevice> getAdapter() {
        return adapter;
    }
//
//    public void onPause() {
//        activity.unregisterReceiver(receiver);
//    }
//
//    public void onResume() {
//        receiver = new WiFiDirectBroadcastReceiver(manager, channel);
//        activity.registerReceiver(receiver, intentFilter);
//    }

    public void onDestroy() {
        manager.stopPeerDiscovery(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {


            }

            @Override
            public void onFailure(int i) {

            }
        });
        manager.clearLocalServices(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i) {


            }
        });
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i) {

            }
        });
    }

    public void initDiscover() {
        startWifi();
        if (!wasWifiEnabled) {
            handler.sendEmptyMessage(2);
//            initBlock();
        }
        setName(toSendData.getName());
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {}
            @Override
            public void onFailure(int reasonCode) {
                initDiscover();
            }
        });
    }

//    public Card getReceivedData() {
//        return receivedData;
//    }

//    public void setToSendData(Card toSendData) {
//        this.toSendData = toSendData;
//    }

//    public void onCancelDiscover() {
//        manager.stopPeerDiscovery(channel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onFailure(int reason) {
//
//            }
//        });
//    }

    private void startWifi() {
        WifiManager wManager = (WifiManager) App.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wManager.isWifiEnabled()) {
            wManager.setWifiEnabled(true);
            while (!wManager.isWifiEnabled()) {
            }
            wasWifiEnabled = false;
        }
    }

    private void finishWifi() {
        returnName();
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {

            }
        });
        if (!wasWifiEnabled) {
            WifiManager wManager = (WifiManager) App.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wManager.setWifiEnabled(false);
            wasWifiEnabled = true;
        }
    }

    protected void connect(WifiP2pConfig config) {
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {}

            @Override
            public void onFailure(int reason) {}
        });
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        if (info.groupFormed) {
            if (info.isGroupOwner) {
                send();
            } else {
                InetAddress address = info.groupOwnerAddress;
                send(address);
            }
        }
    }

    private void send(InetAddress address) {
        if (!wasWifiEnabled) {
            handler.sendEmptyMessage(3);
//            activity.unregisterReceiver(stopReceiver);
        }
        ClientThread clientThread = new ClientThread(address, port, toSendData);
        Thread thread = new Thread(clientThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finishWifi();
        receivedData = clientThread.getReceiveObject();
        try {
            for (Phone phone : receivedData.getPhones()) {
                phone.setCard(receivedData);
            }
            for (Email email : receivedData.getEmails()) {
                email.setCard(receivedData);
            }
            for (SocialLink socialLink : receivedData.getSocialLinks()) {
                socialLink.setCard(receivedData);
            }
            MainOperations.createContact(receivedData);
            success=true;
            handler.sendEmptyMessage(1);
//            kill();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isSuccess() {
        return success;
    }

    private void send() {
        if (!wasWifiEnabled) {
            handler.sendEmptyMessage(3);
//            activity.unregisterReceiver(stopReceiver);
        }
        ServerThread serverThread = new ServerThread(port, toSendData);
        Thread thread = new Thread(serverThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finishWifi();
        receivedData = serverThread.getReceiveObject();
        try {
            for (Phone phone : receivedData.getPhones()) {
                phone.setCard(receivedData);
            }
            for (Email email : receivedData.getEmails()) {
                email.setCard(receivedData);
            }
            for (SocialLink socialLink : receivedData.getSocialLinks()) {
                socialLink.setCard(receivedData);
            }for (Phone phone : receivedData.getPhones()) {
                phone.setCard(receivedData);
            }
            for (Email email : receivedData.getEmails()) {
                email.setCard(receivedData);
            }
            for (SocialLink socialLink : receivedData.getSocialLinks()) {
                socialLink.setCard(receivedData);
            }
            MainOperations.createContact(receivedData);
            success=true;
            handler.sendEmptyMessage(1);
//            kill();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void kill() {
//        Toast toast = Toast.makeText(activity, "Card is successfully shared", Toast.LENGTH_LONG);
//        toast.show();
//    }

    public void setName(String name) {
        setNewDeviceName(name);
    }

    public void setHandler(android.os.Handler handler) {
        this.handler = handler;
    }

    private void setNewDeviceName(final String name) {
        try {
            WifiP2pManager manager1 = (WifiP2pManager) App.getContext().getApplicationContext().getSystemService(Context.WIFI_P2P_SERVICE);
            WifiP2pManager.Channel channel1 = manager1.initialize(App.getContext().getApplicationContext(), App.getContext().getApplicationContext().getMainLooper(), null);
            Method method = manager1.getClass().getMethod("setDeviceName",WifiP2pManager.Channel.class, String.class, WifiP2pManager.ActionListener.class);
            method.invoke(manager1, channel1, name, new WifiP2pManager.ActionListener() {
                public void onSuccess() {
                }
                public void onFailure(int reason) {

                }
            });
        } catch (Exception e)   {
            e.printStackTrace();
        }
    }

    private void returnName() {
        setNewDeviceName(deviceName);
    }

    public void setDeviceName(String deviceName) {
        if (this.deviceName == null || this.deviceName.isEmpty()) {
            this.deviceName = deviceName;
        }
    }

//    private void initBlock() {
//        blockIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        blockIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
//        blockIntentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
//
//        activity.registerReceiver(stopReceiver, blockIntentFilter);
//    }
}
