package com.bindup.vcard.vcardapp.ui.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bindup.vcard.vcardapp.GlideApp;
import com.bindup.vcard.vcardapp.R;
import com.bindup.vcard.vcardapp.data.MainOperations;
import com.bindup.vcard.vcardapp.data.storage.model.Base;
import com.bindup.vcard.vcardapp.data.storage.model.Card;
import com.bindup.vcard.vcardapp.data.storage.model.Email;
import com.bindup.vcard.vcardapp.data.storage.model.Logo;
import com.bindup.vcard.vcardapp.data.storage.model.Phone;
import com.bindup.vcard.vcardapp.ui.Activities.MainActivity;
import com.bindup.vcard.vcardapp.ui.interfaces.IMainActivity;
import com.bindup.vcard.vcardapp.utils.BitmapHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

@SuppressLint("ValidFragment")
public class MyVcardCreateCardFragment extends Fragment implements View.OnClickListener {

    //constants
    private static final String TAG = "VcardViewFragment";
    private static final int PICK_PHOTO_REQUEST = 1;


    //widgets
    private Bitmap bitmap = null;
    private TextView mSurnameTextView, mNameTextView, mMiddleNameTextView, mCompanyNameTextView,
            mAdressTextView, mPositionTextView, mWebSiteTextView, mPhoneTextView, mEmailTextView;
    private EditText mCardIdEditText, mSurnameEditText, mNameEditText, mMiddleNameEditText,
            mCompanyEditText, mAdressEditText, mPositionEditText, mWebSiteEditText, mPhoneEditText, mEmailEditText;
    private ImageView mCompanyLogoImage;
    private Button mBtnSaveData, mBtnUploadPhoto;
    private CardView mCardView;
    private Bitmap mCardBitmapForView;

    //vars
    private List<Phone> phones = null;
    private List<Email> emails = null;
    private IMainActivity mInterface;
    private Context mContext;

    @SuppressLint("ValidFragment")
    public MyVcardCreateCardFragment(Context context) {
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.vcard_view_edit_and_create_fragment, container, false);
        Log.d(TAG, "onCreateView: started Creation of the Card..........");
        mInterface = (IMainActivity) mContext;
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideFloatingActionButton();
        }
//        setHasOptionsMenu(true);

        //      View Init!!
        mCompanyLogoImage = view.findViewById(R.id.create_logo);
        mCardView = view.findViewById(R.id.create_vcard);

        //        Text View INIT!!!!
        mSurnameTextView = view.findViewById(R.id.create_surname_text_view);
        mNameTextView = view.findViewById(R.id.create_name_text_view);
        mMiddleNameTextView = view.findViewById(R.id.create_middle_name_text_view);
        mCompanyNameTextView = view.findViewById(R.id.create_company_name_text_view);
        mAdressTextView = view.findViewById(R.id.create_adress_text_view);
        mPositionTextView = view.findViewById(R.id.create_position_text_view);
        mWebSiteTextView = view.findViewById(R.id.create_website_text_view);
        mPhoneTextView = view.findViewById(R.id.create_phone_text_view);
        mEmailTextView = view.findViewById(R.id.create_email_text_view);

        //          Edit Text INIT!!

        mCardIdEditText = view.findViewById(R.id.create_vcard_id__edit_text);
        mSurnameEditText = view.findViewById(R.id.create_surname_edit_text);
        mNameEditText = view.findViewById(R.id.create_name_text);
        mMiddleNameEditText = view.findViewById(R.id.create_middle_name_text);
        mCompanyEditText = view.findViewById(R.id.create_company_name_text);
        mAdressEditText = view.findViewById(R.id.create_adress_text);
        mPositionEditText = view.findViewById(R.id.create_position_text);
        mWebSiteEditText = view.findViewById(R.id.create_website_text);
        mPhoneEditText = view.findViewById(R.id.create_phone_text);
        mEmailEditText = view.findViewById(R.id.create_email_text);

        //          Buttons INIT!!
        mBtnUploadPhoto = view.findViewById(R.id.create_upload_btn_my_vcard);
        mBtnSaveData = view.findViewById(R.id.create_save_btn);
        mBtnUploadPhoto.setOnClickListener(this);
        mBtnSaveData.setOnClickListener(this);

        init();
        return view;
    }
//TODO think is here is needed a button for uploading photo in menu separately
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
//        inflater.inflate(R.menu.top_navigation_menu_registration,menu);
//        setMenuVisibility(true);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    private void init() {
        Log.d(TAG, "init: initializing " + getString(R.string.tag_fragment_preview_my_vcard));
    }

    public void uploadPhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_PHOTO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_PHOTO_REQUEST:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri selectedImage = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        bitmap = BitmapHelper.getScaledBitmap(bitmap, 125, 60);
                        GlideApp
                                .with(getActivity())
                                .load(bitmap)
                                .into(mCompanyLogoImage);
                    }
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_save_btn:
                Log.d(TAG, "onClick: save button clicked...");
                if (!mNameEditText.getText().toString().trim().isEmpty()) {
                    if (!mSurnameEditText.getText().toString().trim().isEmpty()) {
                        try {
                            View view = getActivity().getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            //@TODO Solve the problem with emails(if emails more then one add other field, same with phones
                            mSurnameTextView.setText(mSurnameEditText.getText().toString());
                            mNameTextView.setText(mNameEditText.getText().toString());
                            mMiddleNameTextView.setText(mMiddleNameEditText.getText().toString());
                            mCompanyNameTextView.setText(mCompanyEditText.getText().toString());
                            mAdressTextView.setText(mAdressEditText.getText().toString());
                            mPositionTextView.setText(mPositionEditText.getText().toString());
                            mWebSiteTextView.setText(mWebSiteEditText.getText().toString());
                            mPhoneTextView.setText(mPhoneEditText.getText().toString());
                            String s = mPhoneEditText.getText().toString();
                            if (s != null && s.trim().length() > 0) {
                                phones = new ArrayList<>();
                                phones.add(new Phone(s.trim()));
                            }
                            s = null;
                            s = mEmailEditText.getText().toString();
                            if (s != null && s.trim().length() > 0) {
                                emails = new ArrayList<>();
                                emails.add(new Email(s.trim()));
                            }
                            mEmailTextView.setText(mEmailEditText.getText().toString());
                            loadView(mCardView);
                            Card userCard = new Card(new Logo(null, bitmap), mCardIdEditText.getText().toString(),
                                    mNameEditText.getText().toString(), mSurnameEditText.getText().toString(),
                                    mMiddleNameEditText.getText().toString(),
                                    mCompanyEditText.getText().toString(), mAdressEditText.getText().toString(),
                                    mPositionEditText.getText().toString(), phones, emails, mWebSiteEditText.getText().toString(), null, new Base(mCardBitmapForView));
        //@TODO fix the field "SITE" above!!!!
                            new MainOperations(new Handler()).createCard(userCard);
                            Snackbar.make(v, "Saved", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        } catch (NullPointerException e) {
                            e.getMessage();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Field surname is required", Toast.LENGTH_SHORT).show();
                        break;
                    }
                } else {
                    Toast.makeText(getActivity(), "Field name is required", Toast.LENGTH_SHORT).show();
                    break;
                }

                // Inflating MyVCardFragment via interface here...
                mInterface.inflateMyVCardFragment(mContext);
                break;
            case R.id.create_upload_btn_my_vcard:
                Log.d(TAG, "onClick: clicked upload button...");
                uploadPhoto();
                break;
        }
    }

    public void loadView(CardView cardView) {
        cardView.setDrawingCacheEnabled(true);
        mCardBitmapForView = loadBitmapFromView(cardView);
    }

    public Bitmap loadBitmapFromView(View v) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(600, 339, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);
        return returnedBitmap;
    }
}






