package com.example.rrty6.vcardapp.ui.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rrty6.vcardapp.GlideApp;
import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.data.MainOperations;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Email;
import com.example.rrty6.vcardapp.data.storage.model.Phone;
import com.example.rrty6.vcardapp.ui.interfaces.IMainActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupsPreviewFragment extends Fragment implements View.OnClickListener{

    //constants
    private static final String TAG = "ViewProfileFragment";
    private static final int PICK_PHOTO_REQUEST = 1;

    //widgets
    private TextView mFragmentHeading, mSurnameTextView, mNameTextView, mMiddleNameTextView, mCompanyNameTextView,
            mAdressTextView, mPositionTextView, mWebSiteTextView, mPhoneTextView, mEmailTextView,
            mCardIdText, mSurnameText, mNameText, mMiddleNameText, mCompanyText,
            mAdressText, mPositionText, mWebSiteText, mPhoneText, mEmailText;
    private RelativeLayout mBackArrow;
    private ImageView mCompanyLogoImage;
    private Button mBackBtn;
    private FloatingActionButton mFabForCall, mFabForSms, mFabForEmail;

    //vars
    private Card mMyVcard;
    private IMainActivity mInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            try {
                mMyVcard = MainOperations.getCard(bundle.getLong("card id"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
           Log.d(TAG, "onCreate: got incoming bundle: ");
        }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.vcard_view_for_contacts_preview_fragment, container, false);
        Log.d(TAG, "onCreateView: started.");
        //      View Init!!
        mCompanyLogoImage = view.findViewById(R.id.contacts_preview_logo);

        //       Picture init!
        mBackArrow = view.findViewById(R.id.back_arrow);

        //        Text View INIT!!!!
        mFragmentHeading = view.findViewById(R.id.contacts_preview_fragment_heading);
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
        mCardIdText = view.findViewById(R.id.contacts_preview_vcard_id_text);
        mSurnameText = view.findViewById(R.id.contacts_preview_surname_text);
        mNameText = view.findViewById(R.id.contacts_preview_name_text);
        mMiddleNameText = view.findViewById(R.id.contacts_preview_middle_name_text);
        mCompanyText =  view.findViewById(R.id.contacts_preview_company_name_text);
        mAdressText = view.findViewById(R.id.contacts_preview_adress_text);
        mPositionText = view.findViewById(R.id.contacts_preview_position_text);
        mWebSiteText = view.findViewById(R.id.contacts_preview_website_text);
        mPhoneText = view.findViewById(R.id.contacts_preview_phone_text);
        mEmailText = view.findViewById(R.id.contacts_preview_email_text);

        //          Buttons INIT!!
        mBackBtn = view.findViewById(R.id.contacts_preview_btn_back);
        mFabForCall = view.findViewById(R.id.fab_call_contacts_preview);
        mFabForSms = view.findViewById(R.id.fab_sms_contacts_preview);
        mFabForEmail = view.findViewById(R.id.fab_email_contacts_preview);

        mFabForSms.setOnClickListener(this);
        mFabForCall.setOnClickListener(this);
        mFabForEmail.setOnClickListener(this);
//        mEditButton.setOnClickListener(this);

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
            mFragmentHeading.setText("Contacts");
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
            if (phones.size()>0) {
                mPhoneTextView.setText(phones.get(0).getPhone());
            } else { mPhoneTextView.setVisibility(View.INVISIBLE);
            }
            if (emails.size()>0) {
                mEmailTextView.setText(emails.get(0).getEmail());
            } else { mEmailTextView.setVisibility(View.INVISIBLE);
            }
            mSurnameText.setText(mMyVcard.getSurname());
            mNameText.setText(mMyVcard.getName());
            mMiddleNameText.setText(mMyVcard.getMidlename());
            mCompanyText.setText(mMyVcard.getCompany());
            mAdressText.setText(mMyVcard.getAddress());
            mPositionText.setText(mMyVcard.getPosition());
            mWebSiteText.setText(mMyVcard.getSite());
            if (phones.size()>0) {
                mPhoneText.setText(phones.get(0).getPhone());
            } else { mPhoneText.setVisibility(View.INVISIBLE);
            }
            if (emails.size()>0) {
                mEmailText.setText(emails.get(0).getEmail());
            } else { mEmailText.setVisibility(View.INVISIBLE);
            }
            if (mEmailText.getText().toString().isEmpty() ){
                mFabForEmail.setVisibility(View.GONE);
            }
            if (mPhoneText.getText().toString().isEmpty()){
                mFabForCall.setVisibility(View.GONE);
                mFabForSms.setVisibility(View.GONE);
            }
        }catch (NullPointerException e) {e.getMessage();} catch (Exception e) {
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
        switch(v.getId()){
            case R.id.preview_btn_back:
                break;
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
}