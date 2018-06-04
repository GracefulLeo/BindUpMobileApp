package com.example.rrty6.vcardapp.ui.Fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rrty6.vcardapp.GlideApp;
import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.data.MainOperations;
import com.example.rrty6.vcardapp.data.storage.model.Base;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Email;
import com.example.rrty6.vcardapp.data.storage.model.Logo;
import com.example.rrty6.vcardapp.data.storage.model.Phone;
import com.example.rrty6.vcardapp.ui.Activities.MainActivity;
import com.example.rrty6.vcardapp.ui.adapter.MyVCardsFragmentAdapter;
import com.example.rrty6.vcardapp.ui.interfaces.IMainActivity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

@SuppressLint("ValidFragment")
public class MyVCardEditFragment extends Fragment implements View.OnClickListener{

    //constants
    private static final String TAG = "ViewProfileFragment";
    private static final int PICK_PHOTO_REQUEST = 1;

    //widgets
    private TextView mSurnameTextView, mNameTextView, mMiddleNameTextView, mCompanyNameTextView,
            mAdressTextView, mPositionTextView, mWebSiteTextView, mPhoneTextView, mEmailTextView;
    private EditText mCardIdText, mSurnameText, mNameText, mMiddleNameText, mCompanyText,
            mAdressText, mPositionText, mWebSiteText, mPhoneText, mEmailText;
    private ImageView mCompanyLogoImage;
    private Button mUpdateBtn, mChangeLogoBtn;
    private Bitmap bitmap = null;
    private Bitmap mCardBitmapForView;
    private CardView mCardView;

    //vars
    private Card mMyVcard;
    private IMainActivity mInterface;
    private Context mContext;

    @SuppressLint("ValidFragment")
    public MyVCardEditFragment(Context context) {
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
        }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.vcard_view_for_editing_fragment, container, false);
        mInterface = (IMainActivity) mContext;
        Log.d(TAG, "onCreateViewMyVCardEdit: started.");

        //      View Init!!
        mCompanyLogoImage = view.findViewById(R.id.preview_logo);
        mCardView = view.findViewById(R.id.edit_cardview_vcard);

        //        Text View INIT!!!!
        mSurnameTextView = view.findViewById(R.id.edit_surname_text_view);
        mNameTextView = view.findViewById(R.id.edit_name_text_view);
        mMiddleNameTextView = view.findViewById(R.id.edit_middle_name_text_view);
        mCompanyNameTextView = view.findViewById(R.id.edit_company_name_text_view);
        mAdressTextView = view.findViewById(R.id.edit_adress_text_view);
        mPositionTextView = view.findViewById(R.id.edit_position_text_view);
        mWebSiteTextView = view.findViewById(R.id.edit_website_text_view);
        mPhoneTextView = view.findViewById(R.id.edit_phone_text_view);
        mEmailTextView = view.findViewById(R.id.edit_email_text_view);

        //          Edit Text INIT!!
        mCardIdText = view.findViewById(R.id.edit_vcard_id_text);
        mSurnameText = view.findViewById(R.id.edit_surname_text);
        mNameText = view.findViewById(R.id.edit_name_text);
        mMiddleNameText = view.findViewById(R.id.edit_middle_name_text);
        mCompanyText =  view.findViewById(R.id.edit_company_name_text);
        mAdressText = view.findViewById(R.id.edit_adress_text);
        mPositionText = view.findViewById(R.id.edit_position_text);
        mWebSiteText = view.findViewById(R.id.edit_website_text);
        mPhoneText = view.findViewById(R.id.edit_phone_text);
        mEmailText = view.findViewById(R.id.edit_email_text);

        //          Buttons INIT!!
        mUpdateBtn = view.findViewById(R.id.edit_btn_update);
        mChangeLogoBtn = view.findViewById(R.id.edit_btn_change_logo);
        mUpdateBtn.setOnClickListener(this);
        mChangeLogoBtn.setOnClickListener(this);

        init();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.top_navigation_menu_my_vcards_edit,menu);
        setMenuVisibility(true);
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
            if (phones.size()>0) {
                mPhoneTextView.setText(phones.get(0).getPhone());
            } else { mPhoneTextView.setVisibility(View.INVISIBLE);
            }
            if (emails.size()>0) {
                mEmailTextView.setText(emails.get(0).getEmail());
            } else { mEmailTextView.setVisibility(View.INVISIBLE);
            }

            mCardIdText.setText(mMyVcard.getId().toString());
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
        }catch (NullPointerException e) {e.getMessage();} catch (Exception e) {
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

    public void uploadPhoto () {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent,PICK_PHOTO_REQUEST);
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
                        GlideApp
                                .with(getActivity())
                                .load(bitmap)
                                .into(mCompanyLogoImage);
                    }
                }
        }
    }

    public void loadView(CardView cardView){
        cardView.setDrawingCacheEnabled(true);
        mCardBitmapForView =  loadBitmapFromView(cardView);
    }

    public Bitmap loadBitmapFromView(View v) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(600,339, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);

        return returnedBitmap;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.edit_btn_update:
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }


                Logo logo = (bitmap != null )? new Logo(null,bitmap):mMyVcard.getLogo();
                List<Phone> phones = new ArrayList<>();
                phones.add(new Phone(mPhoneText.getText().toString()));
                List<Email> emails = new ArrayList<>();
                emails.add(new Email(mEmailText.getText().toString()));
                //@TODO add all parameters properly
                loadView(mCardView);
                try {
                    Card cardForUpdate = new Card(logo,mCardIdText.getText().toString(),mNameText.getText().toString(),
                            mSurnameText.getText().toString(),mMiddleNameText.getText().toString(),mCompanyText.getText().toString(),
                            mAdressText.getText().toString(),mPositionText.getText().toString(),phones,emails,mWebSiteText.getText().toString(),
                            null,new Base(mCardBitmapForView));
                    new MainOperations(new Handler()).updateCard(mMyVcard,cardForUpdate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast toast = Toast.makeText(getActivity(), "Your card has been succesfully updated!", Toast.LENGTH_LONG);
                toast.show();
                // Inflating MyVCardFragment via interface here...
                mInterface.inflateMyVCardFragment(mContext);
                break;
            case R.id.edit_btn_change_logo:
                Log.d(TAG, "onClick: clicked upload button...");
                uploadPhoto();
        }
    }
}