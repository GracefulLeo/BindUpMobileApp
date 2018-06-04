package com.example.rrty6.vcardapp.ui.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rrty6.vcardapp.GlideApp;
import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.data.MainOperations;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Email;
import com.example.rrty6.vcardapp.data.storage.model.Phone;
import com.example.rrty6.vcardapp.ui.Activities.MainActivity;
import com.example.rrty6.vcardapp.ui.interfaces.IMainActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class MyVCardPreviewFragment extends Fragment implements View.OnClickListener {

    // constants
    private static final String TAG = "ViewProfileFragment";
    private static final int PICK_PHOTO_REQUEST = 1;

    // widgets
    private TextView mSurnameTextView, mNameTextView, mMiddleNameTextView, mCompanyNameTextView,
            mAdressTextView, mPositionTextView, mWebSiteTextView, mPhoneTextView, mEmailTextView,
            mSurnameText, mNameText, mMiddleNameText, mCompanyText,
            mAdressText, mPositionText, mWebSiteText, mPhoneText, mEmailText;
    private ImageView mCompanyLogoImage;
    private Button mBackBtn;
    private RelativeLayout mMiddleNameContainer, mCompanyContainer, mAdressContainer, mPositionContainer, mWebSiteContainer, mPhoneContainer, mEmailContainer;

    // vars
    private Card mMyVcard;
    private IMainActivity mInterface;
    private Context mContext;

    @SuppressLint("ValidFragment")
    public MyVCardPreviewFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mMyVcard = new MainOperations(new Handler()).getCard(bundle.getLong("card id"));
        }
        setHasOptionsMenu(true);
//           Log.d(TAG, "onCreate in MyVCardPreviewFragment: got incoming bundle: " + mMyVcard.getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.vcard_view_for_preview_fragment, container, false);
        Log.d(TAG, "onCreateView: started.");
        if (mInterface == null) {
            mInterface = (IMainActivity) mContext;
        }
        if (mMyVcard == null) {
//            mInterface = (IMainActivity) mContext;
            mInterface.inflateMyVCardFragment(mContext);
            Toast toast = Toast.makeText(getActivity(), R.string.myvcard_the_card_is_deleted_message, Toast.LENGTH_SHORT);
            toast.show();
            return null;
        }
//        ((MainActivity)getActivity()).hideFloatingActionButton();

        //      View Init!!
        mCompanyLogoImage = view.findViewById(R.id.preview_logo);

        //        Text View INIT!!!!
        mSurnameTextView = view.findViewById(R.id.preview_surname_text_view);
        mNameTextView = view.findViewById(R.id.preview_name_text_view);
        mMiddleNameTextView = view.findViewById(R.id.preview_middle_name_text_view);
        mCompanyNameTextView = view.findViewById(R.id.preview_company_name_text_view);
        mAdressTextView = view.findViewById(R.id.preview_adress_text_view);
        mPositionTextView = view.findViewById(R.id.preview_position_text_view);
        mWebSiteTextView = view.findViewById(R.id.preview_website_text_view);
        mPhoneTextView = view.findViewById(R.id.preview_phone_text_view);
        mEmailTextView = view.findViewById(R.id.preview_email_text_view);

        //          Edit Text INIT!!
        mSurnameText = view.findViewById(R.id.preview_surname_text);
        mNameText = view.findViewById(R.id.preview_name_text);
        mMiddleNameText = view.findViewById(R.id.preview_middle_name_text);
        mCompanyText = view.findViewById(R.id.preview_company_name_text);
        mAdressText = view.findViewById(R.id.preview_adress_text);
        mPositionText = view.findViewById(R.id.preview_position_text);
        mWebSiteText = view.findViewById(R.id.preview_website_text);
        mPhoneText = view.findViewById(R.id.preview_phone_text);
        mEmailText = view.findViewById(R.id.preview_email_text);

        //          RelativeLayout INIT!!
        mMiddleNameContainer = view.findViewById(R.id.preview_middle_name_container);
        mCompanyContainer = view.findViewById(R.id.preview_company_name_container);
        mAdressContainer = view.findViewById(R.id.preview_adress_container);
        mPositionContainer = view.findViewById(R.id.preview_position_container);
        mWebSiteContainer = view.findViewById(R.id.preview_website_container);
        mPhoneContainer = view.findViewById(R.id.preview_phone_container);
        mEmailContainer = view.findViewById(R.id.preview_email_container);


        init();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu:  IT WORK!!!");
        menu.clear();
        inflater.inflate(R.menu.top_navigation_menu_my_vcards_preview, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
        } else if (mMyVcard.getLogo() == null) {
            mCompanyLogoImage.setImageResource(R.drawable.ic_person);
        }
    }

    @Override
    public void onClick(View v) {
    }
}