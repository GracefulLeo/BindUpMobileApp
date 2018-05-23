package com.example.rrty6.vcardapp.ui.Fragments;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.data.MainOperations;
import com.example.rrty6.vcardapp.data.exchange.WifiP2pExchange.ClientThread;
import com.example.rrty6.vcardapp.data.exchange.WifiP2pExchange.PeerAdapter;
import com.example.rrty6.vcardapp.data.exchange.WifiP2pExchange.ServerThread;
import com.example.rrty6.vcardapp.data.exchange.WifiP2pExchange.StopNetworking;
import com.example.rrty6.vcardapp.data.exchange.WifiP2pExchange.WiFiDirectBroadcastReceiver;
import com.example.rrty6.vcardapp.data.exchange.WifiP2pExchange.WifiOperations;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Email;
import com.example.rrty6.vcardapp.data.storage.model.Logo;
import com.example.rrty6.vcardapp.data.storage.model.Phone;
import com.example.rrty6.vcardapp.data.storage.model.SocialLink;
import com.example.rrty6.vcardapp.utils.App;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.List;

public class ShareFragment extends Fragment implements WifiP2pManager.ConnectionInfoListener {

    private static final String TAG = "Share activity";
    private static final int NUM_COLUMNS = 1;

    private List<Card> cards;
    private WifiOperations wifiOperations;
    private Card mMyVcard;
    private ListView listViewPeers;
    private ArrayAdapter arrayAdapter;


    private static WifiP2pManager manager;
    private static WifiP2pManager.Channel channel;
    private static String deviceName;
    private static boolean wasWifiEnabled = true;
    private final IntentFilter intentFilter = new IntentFilter();
    private final IntentFilter blockIntentFilter = new IntentFilter();
    private BroadcastReceiver stopReceiver = new StopNetworking();
    private BroadcastReceiver receiver = null;
    private Card receivedData = null;
    private int port = 8448;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        Log.d(TAG, "onCreateView: Share fragment....");
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        listViewPeers = view.findViewById(R.id.share_list_view);

//        try {
//            wifiOperations = new WifiOperations(handler, mMyVcard);
//        } catch (Exception e) {
//            wifiOperations = WifiOperations.getInstance();
//            wifiOperations.setHandler(handler);
//            e.printStackTrace();
//        }
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
        System.out.println(wifiOperations == null);
        listViewPeers = getActivity().findViewById(R.id.share_list_view);
//        arrayAdapter = wifiOperations.getAdapter();
        try {
            arrayAdapter = new PeerAdapter(App.getContext(), android.R.layout.simple_list_item_1);
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
//            handler.sendEmptyMessage(2);
            initBlock();
        }
        setName(mMyVcard.getName());
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {}
            @Override
            public void onFailure(int reasonCode) {
                initDiscover();
            }
        });
    }

    public static void connect(WifiP2pConfig config) {
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {}

            @Override
            public void onFailure(int reason) {}
        });
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
            Method method = manager1.getClass().getMethod("setDeviceName",WifiP2pManager.Channel.class,
                    String.class, WifiP2pManager.ActionListener.class);
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

    private void initBlock() {
        blockIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        blockIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        blockIntentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);

        getActivity().registerReceiver(stopReceiver, blockIntentFilter);
    }

    public static void setDeviceName(String newDeviceName) {
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
        receiver = new WiFiDirectBroadcastReceiver(manager, channel);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        wifiOperations.onDestroy();
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
//            handler.sendEmptyMessage(3);
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
            kill();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            kill();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void kill() {
        ContactsFragment contactsFragment = new ContactsFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity()
                .getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_content_frame, contactsFragment, "Contacts");
        fragmentTransaction.addToBackStack("Contacts");
        fragmentTransaction.commit();
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

    private void returnName() {
        setNewDeviceName(deviceName);
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

}

