package com.bindup.vcard.vcardapp.data.exchange.wifip2pexchange;

import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bindup.vcard.vcardapp.ui.Fragments.ShareFragment;

import java.util.Collection;

public class PeerAdapter extends ArrayAdapter<WifiP2pDevice> implements WifiP2pManager.PeerListListener, AdapterView.OnItemClickListener {
    private static PeerAdapter instance;
    private static Object lock = new Object();
    private int res;
    private ShareFragment fragment;

    public PeerAdapter(ShareFragment shareFragment, int res) throws Exception {
        super(shareFragment.getActivity(), res);
        if (instance == null) {
            this.fragment = shareFragment;
            this.res = res;
            instance = this;
        } else {
            throw new Exception("Instance of PeerAdapter is already exist");
        }
    }

    public static PeerAdapter getInstance() {
        if (instance != null) {
            synchronized (PeerAdapter.class) {
                return instance;
            }
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(res, null);
        }
//        if (!super.isEmpty()) {
            if (super.isEnabled(position)) {
                ((TextView) convertView.findViewById(android.R.id.text1)).setText(super.getItem(position).deviceName);
            }
//        }
        return convertView;
    }

    public void destroy() {
        super.clear();
        instance = null;
    }

    public void updateAdapter(Collection<WifiP2pDevice> devices) {
        if (!super.isEmpty()) {
            super.clear();
        }
        if (devices != null && devices.size() > 0) {
            super.addAll(devices);
        }
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        updateAdapter(peers.getDeviceList());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = this.getItem(position).deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        fragment.connect(config);
    }
}
