package com.bindup.vcard.vcardapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bindup.vcard.vcardapp.GlideApp;
import com.bindup.vcard.vcardapp.R;
import com.bindup.vcard.vcardapp.data.storage.model.Card;
import com.bindup.vcard.vcardapp.ui.interfaces.IMainActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "ConnectionsAdapter";

    //vars
    private List<Card> mMyContacts;
    private Context mContext;
    private IMainActivity mInterface;


    public ContactsRecyclerViewAdapter(Context context, List<Card> cards) {
        mContext = context;
        mMyContacts = cards;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contacts_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactsRecyclerViewAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        if (mMyContacts.get(position).getLogo() != null) {
            GlideApp
                    .with(holder.itemView.getContext())
                    .load(mMyContacts.get(position).getLogo().getLogoBitmap())
                    .into(holder.mCompanyLogo);
        }
        else if(mMyContacts.get(position).getLogo() == null) {
            holder.mCompanyLogo.setImageResource(R.drawable.ic_person);
        }
        holder.mSurname.setText(mMyContacts.get(position).getSurname());
        holder.mName.setText(mMyContacts.get(position).getName());
        holder.mContactsPreviewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.inflateViewContactProfileFragment(mMyContacts.get(position));
            }
        });

//        holder.mCompany.setText(mMyVcards.get(position).getCompany());
//        holder.mPosition.setText(mMyVcards.get(position).getPosition());
//final Card card = mFilteredUsers.get(position);

    }

    @Override
    public int getItemCount() {
        if (mMyContacts != null) {
            return mMyContacts.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mInterface = (IMainActivity) mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mContactsPreviewContainer;
        CircleImageView mCompanyLogo;
        TextView mName;
        TextView mSurname;
        TextView mCompany;
        TextView mPosition;
        CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            mContactsPreviewContainer = itemView.findViewById(R.id.contacts_preview_container);
            mCompanyLogo = itemView.findViewById(R.id.contact_image);
            mName = itemView.findViewById(R.id.contact_name);
            mSurname = itemView.findViewById(R.id.contact_surname);
//            mCompany = itemView.findViewById(R.id.my_vcard_company);
//            mPosition = itemView.findViewById(R.id.my_vcard_position);
//            mCardView = itemView.findViewById(R.id.card_view);
        }
    }
}