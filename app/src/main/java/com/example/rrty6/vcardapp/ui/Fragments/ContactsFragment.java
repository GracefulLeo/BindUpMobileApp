package com.example.rrty6.vcardapp.ui.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.rrty6.vcardapp.ui.Activities.MainActivity;
import com.example.rrty6.vcardapp.ui.adapter.ContactsRecyclerViewAdapter;

import java.sql.SQLException;
import java.util.List;

public class ContactsFragment extends Fragment {

    private static final int NUM_COLUMNS = 1;
    private static final String TAG = "ContactsFragment";

    //widgets
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
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideFloatingActionButton();
        }
        setHasOptionsMenu(true);
        mRecyclerView = view.findViewById(R.id.recycler_view_container_contacts);
        mRecyclerView.setHasFixedSize(true);

        getContacts();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_navigation_menu_contacts, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getContacts() {
        if (mCards != null) {
            mCards.clear();
        }
        try {
            mCards = MainOperations.getContacts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (mContactsRecyclerViewAdapter == null) {
            initRecyclerView();
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mContactsRecyclerViewAdapter = new ContactsRecyclerViewAdapter(getActivity(), mCards);
        mRecyclerView.setAdapter(mContactsRecyclerViewAdapter);
    }
}