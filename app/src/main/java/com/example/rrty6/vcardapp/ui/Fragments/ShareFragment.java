package com.example.rrty6.vcardapp.ui.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.data.MainOperations;
import com.example.rrty6.vcardapp.data.exchange.WifiP2pExchange.ClientThread;
import com.example.rrty6.vcardapp.data.exchange.WifiP2pExchange.PeerAdapter;
import com.example.rrty6.vcardapp.data.exchange.WifiP2pExchange.ServerThread;
import com.example.rrty6.vcardapp.data.exchange.WifiP2pExchange.StopNetworking;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Email;
import com.example.rrty6.vcardapp.data.storage.model.Logo;
import com.example.rrty6.vcardapp.data.storage.model.Phone;
import com.example.rrty6.vcardapp.data.storage.model.SocialLink;
import com.example.rrty6.vcardapp.ui.interfaces.IMainActivity;
import com.example.rrty6.vcardapp.utils.App;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.List;

@SuppressLint("ValidFragment")
public class ShareFragment extends Fragment implements WifiP2pManager.ConnectionInfoListener {

    //constants
    private static final String TAG = "Share fragment";

    //widgets
    private ListView listViewPeers;
    private ArrayAdapter arrayAdapter;

    //vars
    private Card mMyVcard;
    private List<Card> cards;
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private String deviceName;
    private static boolean wasWifiEnabled = true;
    private final IntentFilter intentFilter = new IntentFilter();
    private final IntentFilter blockIntentFilter = new IntentFilter();
    private BroadcastReceiver stopReceiver = new StopNetworking();
    private BroadcastReceiver receiver = null;
    private Card receivedData = null;
    private int port = 8448;
    private IMainActivity mInterface;
    private Context mContext;

    @SuppressLint("ValidFragment")
    public ShareFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        mInterface = (IMainActivity) mContext;
        Log.d(TAG, "onCreateView: Share fragment....");
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        listViewPeers = view.findViewById(R.id.share_list_view);
        try {
            cards = MainOperations.getCardList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        manager = (WifiP2pManager) getContext().getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(getContext(), App.getContext().getMainLooper(), null);

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        cardSelector();
        return view;
    }

    public void cardSelector() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select VCard you want to share");
        ListView modeList = new ListView(getActivity());
        try {
            cards = MainOperations.getCardList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        modeList.setAdapter(new ShareFragment.MyAdapter());
        builder.setView(modeList);
        //@TODO Check, is there anything else we can add to this button...
        builder.setNegativeButton("Cancel", null);
        final Dialog dialog = builder.create();
        dialog.show();
        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(cards.get(position));
                Log.d(TAG, "onClick: clicked...");
                mMyVcard = cards.get(position);
                dialog.dismiss();
                startShare();
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });
    }

    private void startShare() {
        listViewPeers = getActivity().findViewById(R.id.share_list_view);
        try {
            arrayAdapter = new PeerAdapter(this, android.R.layout.simple_list_item_1);
        } catch (Exception e) {
            arrayAdapter = PeerAdapter.getInstance();
        }
        System.out.println(arrayAdapter == null);
        listViewPeers.setAdapter(arrayAdapter);
        listViewPeers.setOnItemClickListener((AdapterView.OnItemClickListener) arrayAdapter);
        System.out.println(mMyVcard);
        initDiscover();
    }

    public void initDiscover() {
        startWifi();
        if (!wasWifiEnabled) {
            initBlock();
        }
        setName(mMyVcard.getName());
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onFailure(int reasonCode) {
                initDiscover();
            }
        });
    }

    public void connect(WifiP2pConfig config) {
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int reason) {
            }
        });
        Log.i(TAG, "connect");
    }

    public void setName(String name) {
        setNewDeviceName(name);
    }

    private void setNewDeviceName(final String name) {
        try {
            WifiP2pManager manager1 = (WifiP2pManager) App.getContext().getApplicationContext()
                    .getSystemService(Context.WIFI_P2P_SERVICE);
            WifiP2pManager.Channel channel1 = manager1.initialize(App.getContext().getApplicationContext(),
                    App.getContext().getApplicationContext().getMainLooper(), null);
            Method method = manager1.getClass().getMethod("setDeviceName", WifiP2pManager.Channel.class,
                    String.class, WifiP2pManager.ActionListener.class);
            method.invoke(manager1, channel1, name, new WifiP2pManager.ActionListener() {
                public void onSuccess() {
                }

                public void onFailure(int reason) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBlock() {
        blockIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        blockIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        blockIntentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);

        getActivity().registerReceiver(stopReceiver, blockIntentFilter);
    }

    public void setDeviceName(String newDeviceName) {
        if (deviceName == null || deviceName.isEmpty()) {
            deviceName = newDeviceName;
        }
    }

    private void startWifi() {
        WifiManager wManager = (WifiManager) App.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wManager.isWifiEnabled()) {
            wManager.setWifiEnabled(true);
            while (!wManager.isWifiEnabled()) {
            }
            wasWifiEnabled = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(/*manager, channel,this*/);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        manager.stopPeerDiscovery(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() { }

            @Override
            public void onFailure(int i) { }
        });
        manager.clearLocalServices(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() { }

            @Override
            public void onFailure(int i) { }
        });
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() { }

            @Override
            public void onFailure(int i) { }
        });
        super.onDestroy();
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
            getActivity().unregisterReceiver(stopReceiver);
        }
        ClientThread clientThread = new ClientThread(address, port, mMyVcard);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        kill();
    }

    private void send() {
        if (!wasWifiEnabled) {
//            handler.sendEmptyMessage(3);
            getActivity().unregisterReceiver(stopReceiver);
        }
        ServerThread serverThread = new ServerThread(port, mMyVcard);
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
            }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        kill();
    }

    private void kill() {
        Toast toast = Toast.makeText(getActivity(), "Card is successfully shared", Toast.LENGTH_LONG);
        toast.show();
        // Inflating ContactFragment via interface here...
        mInterface.inflateContactFragment(mContext);
    }

    private void finishWifi() {
        returnName();
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() { }

            @Override
            public void onFailure(int reason) { }
        });
        if (!wasWifiEnabled) {
            WifiManager wManager = (WifiManager) App.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wManager.setWifiEnabled(false);
            wasWifiEnabled = true;
        }
    }

    private void returnName() {
        setNewDeviceName(deviceName);
    }

    public void requestConnectionInfo() {
        // we are connected with the other device, request connection
        // info to find group owner
        if (manager != null) {
            manager.requestConnectionInfo(channel, this);
        }
    }

    // Adapter for alert dialog, to work properly with collection representing
    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return cards.size();
        }

        @Override
        public String getItem(int position) {
            return cards.get(position).getName();
        }

        //custom method
        public Logo getLogo(int position) {
            return cards.get(position).getLogo();
        }

        //custom method
        public String getSurname(int position) {
            return cards.get(position).getSurname();
        }

        @Override
        public long getItemId(int position) {
            return cards.get(position).hashCode();
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_share_list_item, container, false);
            }
            // Can add anything we want...
            ((TextView) convertView.findViewById(R.id.share_contact_name))
                    .setText(getItem(position));
            ((TextView) convertView.findViewById(R.id.share_contact_surname))
                    .setText(getSurname(position));
            if (cards.get(position).getLogo() != null) {
                ((ImageView) convertView.findViewById(R.id.share_contact_image))
                        .setImageBitmap(getLogo(position).getLogoBitmap());
            }
            return convertView;
        }
    }

    private class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

//        private WifiP2pManager manager;
//        private WifiP2pManager.Channel channel;
//        private ShareFragment fragment;

        public WiFiDirectBroadcastReceiver(/*WifiP2pManager manager, WifiP2pManager.Channel channel, ShareFragment shareFragment*/) {
            super();
//            this.manager = manager;
//            this.channel = channel;
//            this.fragment = shareFragment;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                    int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                    if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                        //Wifi P2P is enabled
                    } else {
                        //Wifi P2P is disabled
                    }
                    break;
                case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                    // request available peers from the wifi p2p manager. This is an
                    // asynchronous call and the calling activity is notified with a
                    // callback on PeerListListener.onPeersAvailable()
                    if (manager != null) {
                        manager.requestPeers(channel, PeerAdapter.getInstance());
                    }
                    break;
                case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                    if (manager == null) {
                        return;
                    }
                    NetworkInfo networkInfo = (NetworkInfo) intent
                            .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                    if (networkInfo.isConnected()) {
                        requestConnectionInfo();
                    } else {
                        // It's a disconnect
                    }
                    break;
                case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                    WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
                    setDeviceName(device.deviceName);
                    break;
            }
        }
    }
}

