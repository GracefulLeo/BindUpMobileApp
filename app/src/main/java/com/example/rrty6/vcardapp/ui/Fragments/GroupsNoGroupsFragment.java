package com.example.rrty6.vcardapp.ui.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.ui.Activities.MainActivity;

public class GroupsNoGroupsFragment extends Fragment {

    private static final String TAG = "MyVcardFragment";
    private FloatingActionButton mFabNoGroups;


    public GroupsNoGroupsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.groups_no_groups_view, container, false);
        Log.d(TAG, "onCreateView: MyVCard fragment started ...");

        MainActivity.mFab.hide();
        mFabNoGroups = view.findViewById(R.id.fab_groups_no_groups);
        mFabNoGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.fab_groups_no_groups){
                    GroupCreateFragment groupCreateFragmentFragment = new GroupCreateFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_content_frame,groupCreateFragmentFragment,getString(R.string.tag_fragment_groups_create));
                    transaction.addToBackStack(getString(R.string.tag_fragment_groups_create));
                    transaction.commit();
                    Log.d(TAG, "onClick: Groups preview called...");
                }
            }
        });
//@todo checkout, if FAB is would work in the way i expect it should ....
        return view;

    }


}







