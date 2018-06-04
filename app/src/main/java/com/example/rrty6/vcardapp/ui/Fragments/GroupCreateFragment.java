package com.example.rrty6.vcardapp.ui.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rrty6.vcardapp.GlideApp;
import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.data.MainOperations;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Group;
import com.example.rrty6.vcardapp.data.storage.model.Logo;
import com.example.rrty6.vcardapp.ui.Activities.MainActivity;
import com.example.rrty6.vcardapp.ui.interfaces.IMainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

@SuppressLint("ValidFragment")
public class GroupCreateFragment extends Fragment implements View.OnClickListener {

    //constants

    private static final String TAG = "GroupCreateFragment";
    private static final int PICK_PHOTO_REQUEST = 1;

    //widgets

    private Bitmap bitmap = null;
    private TextView mFragmentHeading;
    private EditText mGroupNameEditText, mGroupDescriptionEditText;
    private ImageView mGroupCreateLogoImage;
    private Button mBtnSaveData, mBtnUploadLogoGroup, mBtnAddUsersGroup;

    //vars

    private List<Card> selectedContacts = new ArrayList<>();
    private Group group;
    private List<Card> contacts = new ArrayList<>();
    private IMainActivity mInterface;
    private Context mContext;

    @SuppressLint("ValidFragment")
    public GroupCreateFragment(Context context) {
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.group_view_edit_and_create_fragment, container, false);
        Log.d(TAG, "onCreateView: started.");
        mInterface = (IMainActivity) mContext;

        //       Picture init!
        mGroupCreateLogoImage = view.findViewById(R.id.create_group_logo);

        //        Text View INIT!!!!
        mFragmentHeading = view.findViewById(R.id.preview_fragment_heading);

        //          Edit Text INIT!!
        mGroupNameEditText = view.findViewById(R.id.create_group_name_edit_text);
        mGroupDescriptionEditText = view.findViewById(R.id.create_group_description_edit_text);

        //          Buttons INIT!!
        mBtnUploadLogoGroup = view.findViewById(R.id.create_upload_logo_groups_btn);
        mBtnSaveData = view.findViewById(R.id.create_save_groups_btn);
        mBtnAddUsersGroup = view.findViewById(R.id.create_add_users_groups_btn);
        mBtnUploadLogoGroup.setOnClickListener(this);
        mBtnSaveData.setOnClickListener(this);
        mBtnAddUsersGroup.setOnClickListener(this);


        init();
        return view;
    }

    private void init() {
        Log.d(TAG, "init: initializing " + getString(R.string.tag_fragment_preview_my_vcard));
        mFragmentHeading.setText(R.string.tag_fragment_groups);
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
                        GlideApp
                                .with(getActivity())
                                .load(bitmap)
                                .into(mGroupCreateLogoImage);
                    }
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_save_btn:
                Log.d(TAG, "onClick: save button clicked...");
                try {
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    group = new Group(new Logo(null, bitmap), mGroupNameEditText.getText().toString(), mGroupDescriptionEditText.getText().toString());
                    new MainOperations(new Handler()).createGroup(group, null);
                    Snackbar.make(v, R.string.created_group_message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } catch (NullPointerException e) {
                    e.getMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //inflating MyVCardFragment via interface here...
                mInterface.inflateGroupFragment(mContext);
                break;
            case R.id.create_upload_btn_my_vcard:
                Log.d(TAG, "onClick: clicked upload button...");
                uploadPhoto();
                break;
            case R.id.create_add_users_groups_btn:
                Log.d(TAG, "onClick: add users button clicked...");
                try {
                    group = new Group(new Logo(null, bitmap), mGroupNameEditText.getText().toString(), mGroupDescriptionEditText.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                usersSelector();
        }
    }

    public void usersSelector() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select VCard you want to share");
        ListView modeList = new ListView(getActivity());
        contacts = new MainOperations(new Handler()).getContacts();
        modeList.setAdapter(new GroupCreateFragment.MyAdapter());
        builder.setView(modeList);
        // dialogue creation...
        builder.setPositiveButton(R.string.create_groups_add_user_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    group = new Group(new Logo(null, bitmap), mGroupNameEditText.getText().toString(), mGroupDescriptionEditText.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new MainOperations(new Handler()).createGroup(group, selectedContacts);
            }
        });
        final Dialog dialog = builder.create();
        dialog.show();
        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onClick: clicked...");
                selectedContacts.add(contacts.get(position));
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });
        // add users button....

    }

    // Adapter for alert dialog, to work properly with collection representing
    private class MyAdapter extends BaseAdapter implements Checkable {
        @Override
        public int getCount() {
            return contacts.size();
        }

        @Override
        public String getItem(int position) {
            return contacts.get(position).getName();
        }

        //custom method
        public Logo getLogo(int position) {
            return contacts.get(position).getLogo();
        }

        //custom method
        public String getSurname(int position) {
            return contacts.get(position).getSurname();
        }

        @Override
        public long getItemId(int position) {
            return contacts.get(position).hashCode();
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
                    .setText(getSurname(position));
            if (contacts.get(position).getLogo() != null) {
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






