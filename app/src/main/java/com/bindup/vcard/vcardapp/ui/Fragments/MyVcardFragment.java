package com.bindup.vcard.vcardapp.ui.Fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bindup.vcard.vcardapp.R;
import com.bindup.vcard.vcardapp.data.MainOperations;
import com.bindup.vcard.vcardapp.data.storage.model.Card;
import com.bindup.vcard.vcardapp.ui.Activities.MainActivity;
import com.bindup.vcard.vcardapp.ui.adapter.MyVCardsFragmentAdapter;
import com.bindup.vcard.vcardapp.ui.interfaces.IMainActivity;

import java.sql.SQLException;
import java.util.List;

@SuppressLint("ValidFragment")
public class MyVcardFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    //constants
    private static final String TAG = "MyVcardFragment";
    private static final int NUM_COLUMNS = 1;

    //widgets
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //vars
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private MyVCardsFragmentAdapter mRecyclerViewAdapter;
    private List<Card> mCards;
    private IMainActivity mInterface;
    private Context mContext;

    @SuppressLint("ValidFragment")
    public MyVcardFragment(Context context) {
        this.mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInterface = (IMainActivity) mContext;
        List<Card> cards = new MainOperations(new Handler()).getCardList();
        if (cards == null) {
//             Inflate MyVCardFirstLoginFragment via interface here...
            mInterface.inflateVCardFirstLoginFragment(mContext);
        }
        View view = inflater.inflate(R.layout.my_vcard_fragment, container, false);
        Log.d(TAG, "onCreateView: MyVCard fragment started ...");
        mRecyclerView = view.findViewById(R.id.my_vcard_recycler_view_container);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_myvcard);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);

        showCards();
        mRecyclerViewAdapter.notifyDataSetChanged();
        return view;
    }

    private void showCards() {
        if (mCards != null) {
            mCards.clear();
        }
        if (mRecyclerViewAdapter != null){
        mRecyclerViewAdapter.notifyDataSetChanged();
        }
        mCards = new MainOperations(new Handler()).getCardList();
//        if (mRecyclerViewAdapter == null){
        initRecyclerView();
//        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: initiated...");
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerViewAdapter = new MyVCardsFragmentAdapter(getActivity(), mCards);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    public void scrollToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        showCards();
        onItemsLoadComplete();
    }

    public void onItemsLoadComplete() {
        Log.d(TAG, "onItemsLoadComplete: complete...");
        mRecyclerViewAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
