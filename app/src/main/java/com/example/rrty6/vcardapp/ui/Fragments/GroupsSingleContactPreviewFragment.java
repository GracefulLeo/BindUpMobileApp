package com.example.rrty6.vcardapp.ui.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rrty6.vcardapp.GlideApp;
import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.data.MainOperations;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.ui.interfaces.IMainActivity;

public class GroupsSingleContactPreviewFragment extends Fragment {

    //constants
    private static final String TAG = "ViewProfileFragment";

    //widgets
    private TextView mSurnameTextView, mNameTextView, mMiddleNameTextView, mCompanyNameTextView,
            mAdressTextView, mPositionTextView, mWebSiteTextView, mSurnameText, mNameText,
            mMiddleNameText, mCompanyText, mAdressText, mPositionText, mWebSiteText;
    private ImageView mCompanyLogoImage;

    //vars
    private Card mMyVcard;
    private IMainActivity mInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mMyVcard = new MainOperations(new Handler()).getCard(bundle.getLong("card id"));
            setHasOptionsMenu(true);

        }
        Log.d(TAG, "onCreate: got incoming bundle: ");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.top_navigation_menu_groups_preview, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.vcard_view_for_contacts_preview_fragment, container, false);
        Log.d(TAG, "onCreateView: started.");

        //      View Init!!
        mCompanyLogoImage = view.findViewById(R.id.contacts_preview_logo);

        //        Text View INIT!!!!
        mSurnameTextView = view.findViewById(R.id.contacts_preview_surname_text_view);
        mNameTextView = view.findViewById(R.id.contacts_preview_name_text_view);
        mMiddleNameTextView = view.findViewById(R.id.contacts_preview_middle_name_text_view);
        mCompanyNameTextView = view.findViewById(R.id.contacts_preview_company_name_text_view);
        mAdressTextView = view.findViewById(R.id.contacts_preview_adress_text_view);
        mPositionTextView = view.findViewById(R.id.contacts_preview_position_text_view);
        mWebSiteTextView = view.findViewById(R.id.contacts_preview_website_text_view);

        //          Edit Text INIT!!
        mSurnameText = view.findViewById(R.id.contacts_preview_surname_text);
        mNameText = view.findViewById(R.id.contacts_preview_name_text);
        mMiddleNameText = view.findViewById(R.id.contacts_preview_middle_name_text);
        mCompanyText = view.findViewById(R.id.contacts_preview_company_name_text);
        mAdressText = view.findViewById(R.id.contacts_preview_adress_text);
        mPositionText = view.findViewById(R.id.contacts_preview_position_text);
        mWebSiteText = view.findViewById(R.id.contacts_preview_website_text);

        //          Buttons INIT!!

        init();
        return view;
    }


    private void init() {
        Log.d(TAG, "init: initializing " + getString(R.string.tag_fragment_preview_my_vcard));

        // Setting values to textview's
        mSurnameTextView.setText(mMyVcard.getSurname());
        mNameTextView.setText(mMyVcard.getName());
        mMiddleNameTextView.setText(mMyVcard.getMidlename());
        mCompanyNameTextView.setText(mMyVcard.getCompany());
        mAdressTextView.setText(mMyVcard.getAddress());
        mPositionTextView.setText(mMyVcard.getPosition());
        mWebSiteTextView.setText(mMyVcard.getSite());

        // Setting values to group edittext's, i think this is wrong
        mSurnameText.setText(mMyVcard.getSurname());
        mNameText.setText(mMyVcard.getName());
        mMiddleNameText.setText(mMyVcard.getMidlename());
        mCompanyText.setText(mMyVcard.getCompany());
        mAdressText.setText(mMyVcard.getAddress());
        mPositionText.setText(mMyVcard.getPosition());
        mWebSiteText.setText(mMyVcard.getSite());

        if (mMyVcard.getLogo() != null) {
            GlideApp
                    .with(getActivity())
                    .load(mMyVcard.getLogo().getLogoBitmap())
                    .into(mCompanyLogoImage);
        }
    }
}