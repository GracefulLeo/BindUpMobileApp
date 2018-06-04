package com.example.rrty6.vcardapp.ui.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.rrty6.vcardapp.ui.adapter.GroupsRecyclerViewAdapter;
import com.example.rrty6.vcardapp.ui.interfaces.IMainActivity;

import java.util.List;

@SuppressLint("ValidFragment")
public class GroupsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    //constants
    private static final int NUM_COLUMNS = 1;
    private static final String TAG = "GroupsFragment";

    //widgets
    private RecyclerView mRecyclerView;

    //vars
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GroupsRecyclerViewAdapter mGroupsRecyclerViewAdapter;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private List<Group> mGroups;
    private FloatingActionButton mFab;
    private IMainActivity mInterface;
    private Context mContext;

    @SuppressLint("ValidFragment")
    public GroupsFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_fragment, container, false);
        mInterface = (IMainActivity) mContext;
        Log.d(TAG, "onCreateView: started.");

        mRecyclerView = view.findViewById(R.id.recycler_view_container_groups);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_groups);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        mFab = view.findViewById(R.id.fab_groups);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.fab_groups){
                    // Inflating GroupCreateFragment via interface here...
                    mFab.hide();
                    mInterface.inflateGroupCreateFragment(mContext);
                }
            }
        });
        setHasOptionsMenu(true);
        getGroups();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.top_navigation_menu_contacts_and_groups, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getGroups() {
        if (mGroups != null) {
            mGroups.clear();
        }
        mGroups = new MainOperations(new Handler()).getGroupList();
//        if (mGroupsRecyclerViewAdapter == null) {
            initRecyclerView();
//        }
        if(mGroups == null){
            // Inflating GroupsNoGroupFragment via interface here...
            mInterface.inflateGroupNoGroupsFragment(mContext);
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mGroupsRecyclerViewAdapter = new GroupsRecyclerViewAdapter(getActivity(), mGroups);
        mRecyclerView.setAdapter(mGroupsRecyclerViewAdapter);
    }

    @Override
    public void onRefresh() {
        getGroups();
        onItemsLoadComplete();
    }

    public void onItemsLoadComplete() {
        Log.d(TAG, "onItemsLoadComplete: complete...");
        mGroupsRecyclerViewAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}