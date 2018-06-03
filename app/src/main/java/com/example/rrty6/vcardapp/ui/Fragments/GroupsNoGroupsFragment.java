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
public class GroupsNoGroupsFragment extends Fragment {

    //constants
    private static final String TAG = "MyVcardFragment";

    //Vars
    private IMainActivity mInterface;
    private Context mContext;


    @SuppressLint("ValidFragment")
    public GroupsNoGroupsFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.groups_no_groups_view, container, false);
        Log.d(TAG, "onCreateView: MyVCard fragment started ...");
        mInterface = (IMainActivity) mContext;
        FloatingActionButton mFabNoGroups = view.findViewById(R.id.fab_groups_no_groups);
        mFabNoGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.fab_groups_no_groups) {
                    // Inflating GroupCreateFragment via interface here...
                    mInterface.inflateGroupCreateFragment(mContext);
                }
            }
        });
        return view;
    }
}







