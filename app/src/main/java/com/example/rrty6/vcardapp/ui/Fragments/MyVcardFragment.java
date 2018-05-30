package com.example.rrty6.vcardapp.ui.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.data.MainOperations;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.ui.Activities.MainActivity;
import com.example.rrty6.vcardapp.ui.adapter.MyVCardsFragmentAdapter;
import com.example.rrty6.vcardapp.ui.interfaces.IMainActivity;

import java.sql.SQLException;
import java.util.List;

public class MyVcardFragment extends Fragment {

    //constants
    private static final String TAG = "MyVcardFragment";
    private static final  int NUM_COLUMNS = 1;
    
    //widgets
    private RecyclerView mRecyclerView;

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
        try {
            if (MainOperations.getCardList().isEmpty()){
                // Inflate MyVCardFirstLoginFragment via interface here...
                mInterface.inflateVCardFirstLoginFragment(mContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        View view = inflater.inflate(R.layout.my_vcard_fragment,container,false);
        mInterface = (IMainActivity) mContext;
        Log.d(TAG, "onCreateView: MyVCard fragment started ...");
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showFloatingActionButton();
        }
        mRecyclerView = view.findViewById(R.id.my_vcard_recycler_view_container);
        mRecyclerView.setHasFixedSize(true);

        showCards();
        return view;
    }

    private void showCards() {
        if (mCards != null){
            mCards.clear();
        }
        try {
            mCards = MainOperations.getCardList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (mRecyclerViewAdapter == null){
            initRecyclerView();
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: initiated...");
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerViewAdapter = new MyVCardsFragmentAdapter(getActivity(),mCards);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    public void scrollToTop(){
        mRecyclerView.smoothScrollToPosition(0);
    }
}
