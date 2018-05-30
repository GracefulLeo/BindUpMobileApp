package com.example.rrty6.vcardapp.ui.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.ui.interfaces.IMainActivity;

@SuppressLint("ValidFragment")
public class MyVcardFirstLoginFragment extends Fragment {

    //constants
    private static final String TAG = "MyVcardFragment";
    private FloatingActionButton mFabMyVcardFirstFragment;

    //Vars
    private IMainActivity mInterface;
    private Context mContext;

    @SuppressLint("ValidFragment")
    public MyVcardFirstLoginFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.my_vcard_first_login, container, false);
        mInterface = (IMainActivity) mContext;
        mFabMyVcardFirstFragment = view.findViewById(R.id.fab_first_login_fragment);
        mFabMyVcardFirstFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fab_first_login_fragment:
                        Log.d(TAG, "onNavigationItemSelected: Fab pressed...");
                        // Inflating MyVCardCreateCardFragment via interface here...
                        mInterface.inflateCreateCardFragment(mContext);
                        break;
                }
            }
        });
        Log.d(TAG, "onCreateView: MyVCard fragment started ...");
        return view;
    }
}