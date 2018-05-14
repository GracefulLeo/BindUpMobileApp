package com.example.rrty6.vcardapp.ui.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.example.rrty6.vcardapp.data.exchange.WifiP2pExchange.WifiOperations;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Logo;

import java.sql.SQLException;
import java.util.List;

public class ShareActivity extends AppCompatActivity {

    private static final String TAG = "Share activity";
    private static final int NUM_COLUMNS = 1;

    private List<Card> cards;
    private WifiOperations wifiOperations;
    private Card mMyVcard;
    private ListView listViewPeers;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        listViewPeers = findViewById(R.id.share_list_view);
        try {
            wifiOperations = new WifiOperations(this, mMyVcard);
        } catch (Exception e) {
            wifiOperations = WifiOperations.getInstance();
            wifiOperations.setActivity(this);
            wifiOperations.setToSendData(mMyVcard);
            e.printStackTrace();
        }
        try {
            cards = MainOperations.getCardList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cardSelector();
    }

    public void cardSelector() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select VCard you want to share");

        ListView modeList = new ListView(this);
        try {
            cards = MainOperations.getCardList();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        modeList.setAdapter(new MyAdapter());

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
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });
    }

    private void startShare() {
        System.out.println(wifiOperations == null);
        listViewPeers = findViewById(R.id.share_list_view);
        arrayAdapter = wifiOperations.getAdapter();
        System.out.println(arrayAdapter == null);
        listViewPeers.setAdapter(arrayAdapter);
        listViewPeers.setOnItemClickListener((AdapterView.OnItemClickListener) arrayAdapter);
        wifiOperations.setToSendData(mMyVcard);
        System.out.println(mMyVcard);
        wifiOperations.initDiscover();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wifiOperations.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wifiOperations.onResume();
    }

    @Override
    protected void onDestroy() {
        wifiOperations.onDestroy();
        super.onDestroy();
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
        public Logo getLogo (int position) {
            return cards.get(position).getLogo();
        }
        //custom method
        public String getSurname (int position){
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
            if (cards.get(position).getLogo() != null){
            ((ImageView) convertView.findViewById(R.id.share_contact_image))
                  .setImageBitmap(getLogo(position).getLogoBitmap());
            }
            return convertView;
        }
    }

}

