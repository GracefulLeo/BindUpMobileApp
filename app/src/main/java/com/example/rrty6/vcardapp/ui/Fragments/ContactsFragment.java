package com.example.rrty6.vcardapp.ui.Fragments;

import android.os.Bundle;
import android.os.Handler;
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
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.ui.adapter.ContactsRecyclerViewAdapter;

import java.util.List;

public class ContactsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    //constants
    private static final int NUM_COLUMNS = 1;
    private static final String TAG = "ContactsFragment";

    //widgets
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    //vars
    private ContactsRecyclerViewAdapter mContactsRecyclerViewAdapter;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private List<Card> mCards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        Log.d(TAG, "onCreateView: started.");
        setHasOptionsMenu(true);
        mRecyclerView = view.findViewById(R.id.recycler_view_container_contacts);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_contacts);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);

        getContacts();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.top_navigation_menu_contacts_and_groups, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getContacts() {
        if (mCards != null) {
            mCards.clear();
        }
        mCards = new MainOperations(new Handler()).getContacts();
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mContactsRecyclerViewAdapter = new ContactsRecyclerViewAdapter(getActivity(), mCards);
        mRecyclerView.setAdapter(mContactsRecyclerViewAdapter);
    }

    @Override
    public void onRefresh() {
        getContacts();
        onItemsLoadComplete();
    }

    public void onItemsLoadComplete() {
        Log.d(TAG, "onItemsLoadComplete: complete...");
        mContactsRecyclerViewAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}