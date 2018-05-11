package com.example.rrty6.vcardapp.ui.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.data.MainOperations;
import com.example.rrty6.vcardapp.data.storage.model.Group;
import com.example.rrty6.vcardapp.ui.Activities.MainActivity;
import com.example.rrty6.vcardapp.ui.adapter.GroupsRecyclerViewAdapter;

import java.sql.SQLException;
import java.util.List;

public class GroupsFragment extends Fragment {

    private static final int NUM_COLUMNS = 1;
    private static final String TAG = "GroupsFragment";

    //widgets
    private RecyclerView mRecyclerView;
    //vars
    private GroupsRecyclerViewAdapter mGroupsRecyclerViewAdapter;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private List<Group> mGroups;
    private FloatingActionButton mFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_fragment, container, false);
        Log.d(TAG, "onCreateView: started.");

        mRecyclerView = view.findViewById(R.id.recycler_view_container_groups);
        mRecyclerView.setHasFixedSize(true);
        MainActivity.mFab.hide();
        mFab = view.findViewById(R.id.fab_groups);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.fab_groups){
                    GroupCreateFragment groupCreateFragmentFragment = new GroupCreateFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_content_frame,groupCreateFragmentFragment,getString(R.string.tag_groups_fragment));
                    transaction.addToBackStack(getString(R.string.tag_groups_fragment));
                    transaction.commit();
                    Log.d(TAG, "onClick: Groups preview called...");
                }
            }
        });

        setHasOptionsMenu(true);

        getGroups();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_navigation_menu_contacts, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getGroups() {
        if (mGroups != null) {
            mGroups.clear();
        }
        try {
            mGroups = MainOperations.getGroupList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (mGroupsRecyclerViewAdapter == null) {
            initRecyclerView();
        }
        if(mGroups == null){
            GroupsNoGroupsFragment groupsNoGroupsFragment = new GroupsNoGroupsFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_content_frame, groupsNoGroupsFragment,getString(R.string.tag_groups_fragment));
            transaction.addToBackStack(getString(R.string.tag_groups_fragment));
            transaction.commit();
            Log.d(TAG, "onClick: NoGroups fragment called...");
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mGroupsRecyclerViewAdapter = new GroupsRecyclerViewAdapter(getActivity(), mGroups);
        mRecyclerView.setAdapter(mGroupsRecyclerViewAdapter);
    }

}