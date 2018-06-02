package com.example.rrty6.vcardapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rrty6.vcardapp.GlideApp;
import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.ui.interfaces.IMainActivity;

import java.util.ArrayList;
import java.util.List;

public class MyVCardsFragmentAdapter extends RecyclerView.Adapter<MyVCardsFragmentAdapter.ViewHolder> {

    //constants
    private static final String TAG = "MyVCardsFragmentAdapter";

    //vars
    private List<Card> mMyVcards;
    private Context mContext;
    private IMainActivity mInterface;


    public MyVCardsFragmentAdapter(Context context, List<Card> cards) {
        mMyVcards = cards;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main_feed, parent, false);
        Log.d(TAG, "onCreateViewHolder: created...");
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyVCardsFragmentAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called...");

        if (mMyVcards.get(position).getLogo() != null) {
            GlideApp
                    .with(holder.itemView.getContext())
                    .load(mMyVcards.get(position).getLogo().getLogoBitmap())
                    .into(holder.mCompanyLogo);
        }
        System.out.println(mMyVcards.get(position).getSurname()==null);
        holder.mSurname.setText(mMyVcards.get(position).getSurname());
        holder.mName.setText(mMyVcards.get(position).getName());
        holder.mCompany.setText(mMyVcards.get(position).getCompany());
        holder.mPosition.setText(mMyVcards.get(position).getPosition());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Card Clicked...");
                mInterface.inflateViewProfileFragment(mMyVcards.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mMyVcards != null) {
            return mMyVcards.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mInterface = (IMainActivity) mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mCompanyLogo;
        TextView mName;
        TextView mSurname;
        TextView mCompany;
        TextView mPosition;
        CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCompanyLogo = itemView.findViewById(R.id.my_vcard_preview_company_logo);
            mName = itemView.findViewById(R.id.my_vcard_name);
            mSurname = itemView.findViewById(R.id.my_vcard_surname);
            mCompany = itemView.findViewById(R.id.my_vcard_company);
            mPosition = itemView.findViewById(R.id.my_vcard_position);
            mCardView = itemView.findViewById(R.id.card_view);
        }
    }
}
