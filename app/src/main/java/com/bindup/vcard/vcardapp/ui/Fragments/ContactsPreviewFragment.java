package com.bindup.vcard.vcardapp.ui.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bindup.vcard.vcardapp.GlideApp;
import com.bindup.vcard.vcardapp.R;
import com.bindup.vcard.vcardapp.data.MainOperations;
import com.bindup.vcard.vcardapp.data.storage.model.Card;
import com.bindup.vcard.vcardapp.data.storage.model.Email;
import com.bindup.vcard.vcardapp.data.storage.model.Group;
import com.bindup.vcard.vcardapp.data.storage.model.Logo;
import com.bindup.vcard.vcardapp.data.storage.model.Phone;
import com.bindup.vcard.vcardapp.ui.interfaces.IMainActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactsPreviewFragment extends Fragment implements View.OnClickListener {
    //constants
    private static final String TAG = "ViewProfileFragment";

    //widgets
    private TextView mSurnameTextView, mNameTextView, mMiddleNameTextView, mCompanyNameTextView,
            mAdressTextView, mPositionTextView, mWebSiteTextView, mPhoneTextView, mEmailTextView,
            mSurnameText, mNameText, mMiddleNameText, mCompanyText,
            mAdressText, mPositionText, mWebSiteText, mPhoneText, mEmailText;
    private ImageView mCompanyLogoImage;
    private FloatingActionButton mFabForCall, mFabForSms, mFabForEmail;
    private RelativeLayout mMiddleNameContainer, mCompanyContainer, mAdressContainer, mPositionContainer, mWebSiteContainer, mPhoneContainer, mEmailContainer;

    //vars
    private Group mGroup;
    private MainOperations mainOperations;
    private List<Group> selectedGroups;
    private List<Group> mGroups;
    private Card mMyVcard;
    private IMainActivity mInterface;

    //TODO add delete button here

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mMyVcard = new MainOperations(new Handler()).getCard(bundle.getLong("card id"));
        }
        Log.d(TAG, "onCreate: got incoming bundle: " + mMyVcard.getName());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.top_navigation_menu_contacts_and_groups_preview, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.vcard_view_for_contacts_preview_fragment, container, false);
        Log.d(TAG, "onCreateView: started.");
        setHasOptionsMenu(true);

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
        mPhoneTextView = view.findViewById(R.id.contacts_preview_phone_text_view);
        mEmailTextView = view.findViewById(R.id.contacts_preview_email_text_view);

        //          Edit Text INIT!!
        mSurnameText = view.findViewById(R.id.contacts_preview_surname_text);
        mNameText = view.findViewById(R.id.contacts_preview_name_text);
        mMiddleNameText = view.findViewById(R.id.contacts_preview_middle_name_text);
        mCompanyText = view.findViewById(R.id.contacts_preview_company_name_text);
        mAdressText = view.findViewById(R.id.contacts_preview_adress_text);
        mPositionText = view.findViewById(R.id.contacts_preview_position_text);
        mWebSiteText = view.findViewById(R.id.contacts_preview_website_text);
        mPhoneText = view.findViewById(R.id.contacts_preview_phone_text);
        mEmailText = view.findViewById(R.id.contacts_preview_email_text);

        //          Buttons INIT!!
        mFabForCall = view.findViewById(R.id.fab_call_contacts_preview);
        mFabForSms = view.findViewById(R.id.fab_sms_contacts_preview);
        mFabForEmail = view.findViewById(R.id.fab_email_contacts_preview);

        // RelativeLayout INIT!
        mMiddleNameContainer = view.findViewById(R.id.contacts_preview_middle_name_container);
        mCompanyContainer = view.findViewById(R.id.contacts_preview_company_name_container);
        mAdressContainer = view.findViewById(R.id.contacts_preview_adress_container);
        mPositionContainer = view.findViewById(R.id.contacts_preview_position_container);
        mWebSiteContainer = view.findViewById(R.id.contacts_preview_website_container);
        mPhoneContainer = view.findViewById(R.id.contacts_preview_phone_container);
        mEmailContainer = view.findViewById(R.id.contacts_preview_email_container);


        mFabForSms.setOnClickListener(this);
        mFabForCall.setOnClickListener(this);
        mFabForEmail.setOnClickListener(this);

        init();
        return view;
    }

    void phone(String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
    }

    void sms(String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNumber, null)));
    }

    public void sendEmail(String receiverEmail) {
        Intent intent = new Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto:" + receiverEmail));
        if (intent.resolveActivity(getActivity().getApplicationContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void init() {
        Log.d(TAG, "init: initializing " + getString(R.string.tag_fragment_preview_my_vcard));
        try {
            List<Phone> phones = new ArrayList<>(mMyVcard.getPhones());
            List<Email> emails = new ArrayList<>(mMyVcard.getEmails());
            //@TODO Solve the problem with emails(if emails more then one add other field, same with phones
            mSurnameTextView.setText(mMyVcard.getSurname());
            mNameTextView.setText(mMyVcard.getName());
            mMiddleNameTextView.setText(mMyVcard.getMidlename());
            mCompanyNameTextView.setText(mMyVcard.getCompany());
            mAdressTextView.setText(mMyVcard.getAddress());
            mPositionTextView.setText(mMyVcard.getPosition());
            mWebSiteTextView.setText(mMyVcard.getSite());
            if (phones.size() > 0) {
                mPhoneTextView.setText(phones.get(0).getPhone());
            } else {
                mPhoneTextView.setVisibility(View.INVISIBLE);
            }
            if (emails.size() > 0) {
                mEmailTextView.setText(emails.get(0).getEmail());
            } else {
                mEmailTextView.setVisibility(View.INVISIBLE);
            }

            mSurnameText.setText(mMyVcard.getSurname());
            mNameText.setText(mMyVcard.getName());

            if (mMyVcard.getMidlename() != null && !mMyVcard.getMidlename().trim().isEmpty()) {
                mMiddleNameText.setText(mMyVcard.getMidlename());
            } else {
                mMiddleNameContainer.setVisibility(View.GONE);
            }

            if (mMyVcard.getCompany() != null && !mMyVcard.getCompany().trim().isEmpty()) {
                mCompanyText.setText(mMyVcard.getCompany());
            } else {
                mCompanyContainer.setVisibility(View.GONE);
            }

            if (mMyVcard.getAddress() != null && !mMyVcard.getAddress().trim().isEmpty()) {
                mAdressText.setText(mMyVcard.getAddress());
            } else {
                mAdressContainer.setVisibility(View.GONE);
            }

            if (mMyVcard.getPosition() != null && !mMyVcard.getPosition().trim().isEmpty()) {
                mPositionText.setText(mMyVcard.getPosition());
            } else {
                mPositionContainer.setVisibility(View.GONE);
            }

            if (mMyVcard.getSite() != null && !mMyVcard.getSite().trim().isEmpty()) {
                mWebSiteText.setText(mMyVcard.getSite());
            } else {
                mWebSiteContainer.setVisibility(View.GONE);
            }
            if (phones.size() > 0) {
                mPhoneText.setText(phones.get(0).getPhone());
            } else {
                mPhoneContainer.setVisibility(View.GONE);
            }
            if (emails.size() > 0) {
                mEmailText.setText(emails.get(0).getEmail());
            } else {
                mEmailContainer.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mMyVcard.getLogo() != null) {
            GlideApp
                    .with(getActivity())
                    .load(mMyVcard.getLogo().getLogoBitmap())
                    .into(mCompanyLogoImage);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_call_contacts_preview:
                phone(mPhoneText.getText().toString());
                break;
            case R.id.fab_sms_contacts_preview:
                sms(mPhoneText.getText().toString());
                break;
            case R.id.fab_email_contacts_preview:
                sendEmail(mEmailText.getText().toString());
        }
    }

    public void usersSelector() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //@TODO Change harcoded phrase to string value ...
        builder.setTitle("Select group to which you want add this contact");
        ListView modeList = new ListView(getActivity());
        selectedGroups = new MainOperations(new Handler()).getGroupList();
        modeList.setAdapter(new ContactsPreviewFragment.MyAdapter());
        builder.setView(modeList);
        // dialogue creation...
        builder.setPositiveButton(R.string.create_groups_add_user_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    mainOperations.addContactToGroup(mGroup, mMyVcard);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        final Dialog dialog = builder.create();
        dialog.show();
        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onClick: clicked...");
                selectedGroups.add(mGroups.get(position));
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });
        // add users button....

    }

    // Adapter for alert dialog, to work properly with collection representing
    private class MyAdapter extends BaseAdapter implements Checkable {
        @Override
        public int getCount() {
            return mGroups.size();
        }

        @Override
        public String getItem(int position) {
            return mGroups.get(position).getName();
        }

        //custom method
        public Logo getLogo(int position) {
            return mGroups.get(position).getLogo();
        }

        //custom method
        public String getGroupName(int position) {
            return mGroups.get(position).getName();
        }

        @Override
        public long getItemId(int position) {
            return mGroups.get(position).hashCode();
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_share_list_item, container, false);
            }
            // Can add anything we want...
            ((TextView) convertView.findViewById(R.id.share_contact_name))
                    .setText(getItem(position));
            ((TextView) convertView.findViewById(R.id.share_contact_surname))
                    .setText(getGroupName(position));
            if (mGroups.get(position).getLogo() != null) {
                ((ImageView) convertView.findViewById(R.id.share_contact_image))
                        .setImageBitmap(getLogo(position).getLogoBitmap());
            }
            return convertView;
        }

        @Override
        public void setChecked(boolean checked) {

        }

        @Override
        public boolean isChecked() {
            return false;
        }

        @Override
        public void toggle() {

        }
    }

}