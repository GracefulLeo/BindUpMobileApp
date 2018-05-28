package com.example.rrty6.vcardapp.ui.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.data.MainOperations;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Group;
import com.example.rrty6.vcardapp.ui.Fragments.ContactsFragment;
import com.example.rrty6.vcardapp.ui.Fragments.ContactsPreviewFragment;
import com.example.rrty6.vcardapp.ui.Fragments.GroupsFragment;
import com.example.rrty6.vcardapp.ui.Fragments.GroupsNoGroupsFragment;
import com.example.rrty6.vcardapp.ui.Fragments.GroupsPreviewFragment;
import com.example.rrty6.vcardapp.ui.Fragments.MyVCardEditFragment;
import com.example.rrty6.vcardapp.ui.Fragments.MyVCardPreviewFragment;
import com.example.rrty6.vcardapp.ui.Fragments.MyVcardCreateCardFragment;
import com.example.rrty6.vcardapp.ui.Fragments.MyVcardFirstLoginFragment;
import com.example.rrty6.vcardapp.ui.Fragments.MyVcardFragment;
import com.example.rrty6.vcardapp.ui.Fragments.ShareFragment;
import com.example.rrty6.vcardapp.ui.interfaces.IMainActivity;
import com.example.rrty6.vcardapp.ui.model.FragmentTag;
import com.example.rrty6.vcardapp.utils.App;
import com.example.rrty6.vcardapp.utils.PreferenceKeys;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IMainActivity {

    //Constants
    private static final String TAG = "MainActivity";

    //@TODO check, is there necessary to have android:hardwareAccelerated="false"android:largeHeap="true" , in androidmanifest xml file
    //@TODO UPDATE: now we need to check, should we get the parameters above back for consistent application work
    //Graphical elements
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    public static FloatingActionButton mFab;

    //vars
    private List<Card> cards;
    private List<Group> groups;
    private Bundle args = new Bundle();
    private ArrayList<String> mFragmentTags = new ArrayList<>();
    private ArrayList<FragmentTag> mFragments = new ArrayList<>();
    private int mExitCount = 0;

    //Fragments

    private ShareFragment shareFragment;
    private MyVcardCreateCardFragment myVcardCreateCardFragment;
    private GroupsFragment groupsFragment;
    private GroupsNoGroupsFragment groupsNoGroupsFragment;
    private MyVCardEditFragment mMyVcardEditFragment;
    private MyVcardFragment mMyVcardFragment;
    private MyVcardFirstLoginFragment myVcardFirstLoginFragment;
    private ContactsFragment mContactsFragment;
    private GroupsPreviewFragment groupsPreviewFragment;
    private MyVCardPreviewFragment mMyVcardPreviewFragment;
    private ContactsPreviewFragment contactsPreviewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(this);
        Bundle bundle = new Bundle();


        init();
        initToolbar();
        initNavigationView();
        isFirstLogin();
        // After Share option we move transactioning on this point. So if statement with key from
        // bundle below (bundle.getString("key")) is needed to go to the contact fragment right after sharing
        if (bundle.getString("key") != null && getIntent().getStringExtra("key").equals("To contacts")) {
            //BackStack management right here
            if (mContactsFragment == null) {
                Log.d(TAG, "onCreate: Contacts...");
                mContactsFragment = new ContactsFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.main_content_frame, mContactsFragment, getString(R.string.tag_fragment_contacts));
                transaction.commit();
                mFragmentTags.add(getString(R.string.tag_fragment_contacts));
                mFragments.add(new FragmentTag(mContactsFragment, getString(R.string.tag_fragment_contacts)));
            } else {
                mFragmentTags.remove(getString(R.string.tag_fragment_contacts));
                mFragmentTags.add(getString(R.string.tag_fragment_contacts));
            }
            setFragmentVisibility(getString(R.string.tag_fragment_contacts));
        }
    }

    //  ----------------------------------!!!initialization!!!------------------------------------------

    //Check is this first appearance of user in the app or not
    private void firstCheck() {
        cards = new ArrayList<>();
        try {
            cards = MainOperations.getCardList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (cards != null) {
            if (cards.size() == 0) {
                if (myVcardFirstLoginFragment == null) {
                    Log.d(TAG, "firstCheck: First Login...");
                    myVcardFirstLoginFragment = new MyVcardFirstLoginFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, myVcardFirstLoginFragment, getString(R.string.tag_fragment_my_vcard));
                    transaction.commit();
                    mFragmentTags.add(getString(R.string.tag_fragment_my_vcard_first_login));
                    mFragments.add(new FragmentTag(myVcardFirstLoginFragment, getString(R.string.tag_fragment_my_vcard_first_login)));
                } else {
                    mFragmentTags.remove(getString(R.string.tag_fragment_my_vcard_first_login));
                    mFragmentTags.add(getString(R.string.tag_fragment_my_vcard_first_login));
                }
                setFragmentVisibility(getString(R.string.tag_fragment_my_vcard_first_login));
            }
        }
    }

    private void init() {
        // Initizialisation ....
        if (mMyVcardFragment == null) {
            Log.d(TAG, "init: MyVcard...");
            mMyVcardFragment = new MyVcardFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content_frame, mMyVcardFragment, getString(R.string.tag_fragment_my_vcard));
            transaction.commit();
            mFragmentTags.add(getString(R.string.tag_fragment_my_vcard));
            mFragments.add(new FragmentTag(mMyVcardFragment, getString(R.string.tag_fragment_my_vcard)));
        } else {
            mFragmentTags.remove(getString(R.string.tag_fragment_my_vcard));
            mFragmentTags.add(getString(R.string.tag_fragment_my_vcard));
        }
        setFragmentVisibility(getString(R.string.tag_fragment_my_vcard));
    }

    private void initToolbar() {
        // Toolbar initizialisation ...
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_button_in_preview_fragment:
//@TODO Make that Bundle here is work properly, test more...
                        if (mMyVcardEditFragment == null) {
                            Log.d(TAG, "onMenuItemClick: Edit My VCard...");
                            mMyVcardEditFragment = new MyVCardEditFragment();
                            mMyVcardEditFragment.setArguments(args);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.add(R.id.main_content_frame, mMyVcardEditFragment, getString(R.string.tag_fragment_my_vcard_edit));
                            transaction.commit();
                            mFragmentTags.add(getString(R.string.tag_fragment_my_vcard_edit));
                            mFragments.add(new FragmentTag(mMyVcardEditFragment, getString(R.string.tag_fragment_my_vcard_edit)));
                        } else {
                            mFragmentTags.remove(getString(R.string.tag_fragment_my_vcard_edit));
                            mFragmentTags.add(getString(R.string.tag_fragment_my_vcard_edit));
                        }
                        setFragmentVisibility(getString(R.string.tag_fragment_my_vcard_edit));
                        break;
                    case R.id.edit_delete_button:
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        alertDialogBuilder.setMessage(getString(R.string.delete_message));
                        alertDialogBuilder.setPositiveButton(R.string.delete_confirmation_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    View view = getCurrentFocus();
                                    if (view != null) {
                                        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                    }
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Your card " + MainOperations.getCard(args.getLong("card id")).getSurname() + " " + MainOperations.getCard(args.getLong("card id")).getName() + " " + MainOperations.getCard(args.getLong("card id")).getMidlename() + " has been succesfully deleted",
                                            Toast.LENGTH_LONG);
                                    toast.show();
                                    System.out.println(MainOperations.getCard(args.getLong("card id")));
                                    MainOperations.deleteCard(MainOperations.getCard(args.getLong("card id")));
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                if (mMyVcardFragment == null) {
                                    Log.d(TAG, "onClick: My VCard");
                                    mMyVcardFragment = new MyVcardFragment();
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.add(R.id.main_content_frame, mMyVcardFragment, getString(R.string.tag_fragment_my_vcard));
                                    transaction.commit();
                                    mFragmentTags.add(getString(R.string.tag_fragment_my_vcard));
                                    mFragments.add(new FragmentTag(mMyVcardFragment, getString(R.string.tag_fragment_my_vcard)));
                                } else {
                                    mFragmentTags.remove(getString(R.string.tag_fragment_my_vcard));
                                    mFragmentTags.add(getString(R.string.tag_fragment_my_vcard));
                                }
                                setFragmentVisibility(getString(R.string.tag_fragment_my_vcard));
                                dialog.dismiss();
                            }
                        });
                        alertDialogBuilder.setNegativeButton(R.string.delete_declining_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialogBuilder.setIcon(R.drawable.ic_warning);
                        alertDialogBuilder.setTitle(" ");
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        break;
                }
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.top_navigation_menu_my_vcards);
    }

    private void initNavigationView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Log.d(TAG, "initNavigationView: NavDrawer initialization...");
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.view_navigation_open, R.string.view_navigation_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        Log.d(TAG, "initNavigationView: toggle initialized...");

        NavigationView mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                //@TODO check if there are no communication via fragment to fragment,if it is
                //@TODO use interface (like IMainActivity) to change that no-no pattern.
                //@TODO if problems appears check Matt Tabian course.
                switch (item.getItemId()) {
                    case R.id.MyVCardItem: {
                        //@TODO check, is this correct to clear backstack here
                        mFragmentTags.clear();
                        mFragmentTags = new ArrayList<>();
                        cards = new ArrayList<>();
                        try {
                            cards = MainOperations.getCardList();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (cards != null && cards.size() == 0) {
                            if (myVcardFirstLoginFragment == null) {
                                Log.d(TAG, "onNavigationItemSelected: FirstLogin...");
                                myVcardFirstLoginFragment = new MyVcardFirstLoginFragment();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.add(R.id.main_content_frame, myVcardFirstLoginFragment, getString(R.string.tag_fragment_my_vcard));
                                transaction.commit();
                                mFragmentTags.add(getString(R.string.tag_fragment_my_vcard_first_login));
                                mFragments.add(new FragmentTag(myVcardFirstLoginFragment, getString(R.string.tag_fragment_my_vcard_first_login)));
                            } else {
                                mFragmentTags.remove(getString(R.string.tag_fragment_my_vcard_first_login));
                                mFragmentTags.add(getString(R.string.tag_fragment_my_vcard_first_login));
                            }
                            setFragmentVisibility(getString(R.string.tag_fragment_my_vcard_first_login));
                            break;
                        }
                        Log.d(TAG, "onNavigationItemSelected: MyVCard item selected...");
                        if (mMyVcardFragment == null) {
                            Log.d(TAG, "onNavigationItemSelected: My VCard...");
                            mMyVcardFragment = new MyVcardFragment();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.add(R.id.main_content_frame, mMyVcardFragment, getString(R.string.tag_fragment_my_vcard));
                            transaction.commit();
                            mFragmentTags.add(getString(R.string.tag_fragment_my_vcard));
                            mFragments.add(new FragmentTag(mMyVcardFragment, getString(R.string.tag_fragment_my_vcard)));
                        } else {
                            mFragmentTags.remove(getString(R.string.tag_fragment_my_vcard));
                            mFragmentTags.add(getString(R.string.tag_fragment_my_vcard));
                        }
                        setFragmentVisibility(getString(R.string.tag_fragment_my_vcard));
                        break;
                    }
                    case R.id.contactsItem: {
                        if (mContactsFragment == null) {
                            Log.d(TAG, "onNavigationItemSelected: Contacts...");
                            mContactsFragment = new ContactsFragment();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.add(R.id.main_content_frame, mContactsFragment, getString(R.string.tag_fragment_contacts));
                            transaction.commit();
                            mFragmentTags.add(getString(R.string.tag_fragment_contacts));
                            mFragments.add(new FragmentTag(mContactsFragment, getString(R.string.tag_fragment_contacts)));
                        } else {
                            mFragmentTags.remove(getString(R.string.tag_fragment_contacts));
                            mFragmentTags.add(getString(R.string.tag_fragment_contacts));
                        }
                        setFragmentVisibility(getString(R.string.tag_fragment_contacts));
                        break;
                    }
                    case R.id.groups_item: {
                        groups = new ArrayList<>();
                        try {
                            groups = MainOperations.getGroupList();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (groups != null) {
                            if (groups.size() == 0) {
                                if (groupsNoGroupsFragment == null) {
                                    Log.d(TAG, "onNavigationItemSelected: GroupsNoGroup...");
                                    groupsNoGroupsFragment = new GroupsNoGroupsFragment();
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.add(R.id.main_content_frame, groupsNoGroupsFragment, getString(R.string.tag_fragment_groups_no_groups));
                                    transaction.commit();
                                    mFragmentTags.add(getString(R.string.tag_fragment_groups_no_groups));
                                    mFragments.add(new FragmentTag(groupsNoGroupsFragment, getString(R.string.tag_fragment_groups_no_groups)));
                                } else {
                                    mFragmentTags.remove(getString(R.string.tag_fragment_groups_no_groups));
                                    mFragmentTags.add(getString(R.string.tag_fragment_groups_no_groups));
                                }
                                setFragmentVisibility(getString(R.string.tag_fragment_groups_no_groups));
                                break;
                            }
                        }
                        if (groups.size() != 0) {
                            if (groupsFragment == null) {
                                Log.d(TAG, "onNavigationItemSelected: Groups...");
                                groupsFragment = new GroupsFragment();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.add(R.id.main_content_frame, groupsFragment, getString(R.string.tag_fragment_groups));
                                transaction.commit();
                                mFragmentTags.add(getString(R.string.tag_fragment_groups));
                                mFragments.add(new FragmentTag(groupsFragment, getString(R.string.tag_fragment_groups)));
                            } else {
                                mFragmentTags.remove(getString(R.string.tag_fragment_groups));
                                mFragmentTags.add(getString(R.string.tag_fragment_groups));
                            }
                            setFragmentVisibility(getString(R.string.tag_fragment_groups));
                            break;
                        }
                    }
                    case R.id.share_item:
                        Log.d(TAG, "onNavigationItemSelected: share item clicked...");
                        cards = new ArrayList<>();
                        try {
                            cards = MainOperations.getCardList();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (cards.isEmpty()) {

                        }
                        if (shareFragment == null) {
                            Log.d(TAG, "onNavigationItemSelected: Share...");
                            shareFragment = new ShareFragment();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.add(R.id.main_content_frame, shareFragment, getString(R.string.tag_fragment_share));
                            transaction.commit();
                            mFragmentTags.add(getString(R.string.tag_fragment_share));
                            mFragments.add(new FragmentTag(shareFragment, getString(R.string.tag_fragment_share)));
                        } else {
                            mFragmentTags.remove(getString(R.string.tag_fragment_share));
                            mFragmentTags.add(getString(R.string.tag_fragment_share));
                        }
                        setFragmentVisibility(getString(R.string.tag_fragment_share));
                        break;
                    case R.id.log_out_item:
                        Log.d(TAG, "onNavigationItemSelected: log out item clicked...");
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.putExtra("logout", false);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        try {
                            MainOperations.logout();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        finish();
                        break;

                }

                return true;
            }
        });
    }

    public void isFirstLogin() {
        Log.d(TAG, "isFirstLogin: checking if this a first login...");
        final SharedPreferences preferences = App.getSharedPreferences();
        boolean isFirstLogin = preferences.getBoolean(PreferenceKeys.FIRST_TIME_LOGIN, true);
        if (isFirstLogin) {
            if (myVcardFirstLoginFragment == null) {
                Log.d(TAG, "isFirstLogin: MyVCardFirstLogin...");
                myVcardFirstLoginFragment = new MyVcardFirstLoginFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.main_content_frame, myVcardFirstLoginFragment, getString(R.string.tag_fragment_my_vcard_first_login));
                transaction.commit();
                mFragmentTags.add(getString(R.string.tag_fragment_my_vcard_first_login));
                mFragments.add(new FragmentTag(myVcardFirstLoginFragment, getString(R.string.tag_fragment_my_vcard_first_login)));
            } else {
                mFragmentTags.remove(getString(R.string.tag_fragment_my_vcard_first_login));
                mFragmentTags.add(getString(R.string.tag_fragment_my_vcard_first_login));
            }
            setFragmentVisibility(getString(R.string.tag_fragment_my_vcard_first_login));

            Log.d(TAG, "isFirstLogin: launching alert dialog...");
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(getString(R.string.first_time_user_message));
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "onClick: Closing dialog...");
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(PreferenceKeys.FIRST_TIME_LOGIN, false);
                    editor.commit();
                    dialog.dismiss();


                }
            });
            alertDialogBuilder.setIcon(R.drawable.ic_warning);
            alertDialogBuilder.setTitle(" ");
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        if (!isFirstLogin) {
            firstCheck();
        }
    }
//
//    public void isGroupsEmpty (){
//        Log.d(TAG, "isGroupsEmpty: ");
//        boolean isGroupEmpty = true;
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (myVcardCreateCardFragment == null) {
                    Log.d(TAG, "onNavigationItemSelected: Fab pressed.MyVCard create...");
                    myVcardCreateCardFragment = new MyVcardCreateCardFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, myVcardCreateCardFragment, getString(R.string.tag_fragment_my_vcard_create_card));
                    transaction.commit();
                    mFragmentTags.add(getString(R.string.tag_fragment_my_vcard_create_card));
                    mFragments.add(new FragmentTag(myVcardCreateCardFragment, getString(R.string.tag_fragment_my_vcard_create_card)));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    Log.d(TAG, "onNavigationItemSelected:Groups fragment transaction...");
                } else {
                    mFragmentTags.remove(getString(R.string.tag_fragment_my_vcard_create_card));
                    mFragmentTags.add(getString(R.string.tag_fragment_my_vcard_create_card));
                }
                setFragmentVisibility(getString(R.string.tag_fragment_my_vcard_create_card));
                break;
        }
    }

    @Override
    public void inflateViewProfileFragment(Card card) {
        if (mMyVcardPreviewFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(mMyVcardPreviewFragment).commitAllowingStateLoss();
        }
        mMyVcardPreviewFragment = new MyVCardPreviewFragment();

        args.clear();
        args.putLong("card id", card.getId());
        mMyVcardPreviewFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content_frame, mMyVcardPreviewFragment, getString(R.string.tag_fragment_preview_my_vcard));
        transaction.commit();
        mFragmentTags.add(getString(R.string.tag_fragment_preview_my_vcard));
        mFragments.add(new FragmentTag(mMyVcardPreviewFragment, getString(R.string.tag_fragment_preview_my_vcard)));

        setFragmentVisibility(getString(R.string.tag_fragment_preview_my_vcard));
    }

    @Override
    public void inflateViewContactProfileFragment(Card card) {
        if (contactsPreviewFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(contactsPreviewFragment).commitAllowingStateLoss();
        }
        contactsPreviewFragment = new ContactsPreviewFragment();
        //@TODO change all tags to the normal one
        //@TODO same here as bellow. Check are we really need to initialize args like that...
        Bundle args = new Bundle();
        args.putLong("card id", card.getId());
        contactsPreviewFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content_frame, contactsPreviewFragment, getString(R.string.tag_fragment_preview_contacts));
        transaction.commit();
        mFragmentTags.add(getString(R.string.tag_fragment_preview_contacts));
        mFragments.add(new FragmentTag(contactsPreviewFragment, getString(R.string.tag_fragment_preview_contacts)));

        setFragmentVisibility(getString(R.string.tag_fragment_preview_contacts));
    }

    @Override
    public void inflateViewGroupProfileGroups(Group group) {
        if (groupsPreviewFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(groupsPreviewFragment).commitAllowingStateLoss();
        }
        groupsPreviewFragment = new GroupsPreviewFragment();
        //@TODO Check is this bundle initialization is correct
        Bundle args = new Bundle();
        args.putLong("groups id", group.getId());
        groupsPreviewFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content_frame, groupsPreviewFragment, getString(R.string.tag_fragment_preview_groups));
        transaction.commit();
        mFragmentTags.add(getString(R.string.tag_fragment_preview_groups));
        mFragments.add(new FragmentTag(groupsPreviewFragment, getString(R.string.tag_fragment_preview_groups)));

        setFragmentVisibility(getString(R.string.tag_fragment_preview_groups));
    }

    private void setFragmentVisibility(String tagname) {
        for (int i = 0; i < mFragments.size(); i++) {
            if (tagname.equals(mFragments.get(i).getTag())) {
                //show
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.show(mFragments.get(i).getFragment());
                transaction.commit();
            } else {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.hide(mFragments.get(i).getFragment());
                transaction.commit();
            }
        }
    }

    // Methods for FAB. (Hiding and showing)
    public void showFloatingActionButton() {
        mFab.show();
    }

    public void hideFloatingActionButton() {
        mFab.hide();
    }
    //    }
//        }
//            super.onBackPressed();
//        if(mExitCount >= 2){
//
//        }
//            }
//                Toast.makeText(this, "1 more click to exit", Toast.LENGTH_SHORT).show();
//                mExitCount++;
//            else{
//            }
//                Toast.makeText(this, "1 more click to exit", Toast.LENGTH_SHORT).show();
//                mExitCount++;
//                mHomeFragment.scrollToTop();
//            if(topFragmentTag.equals(getString(R.string.tag_fragment_home))){
//            String topFragmentTag = mFragmentsTags.get(backStackCount - 1);
//        else if( backStackCount == 1){
//        }
//            mExitCount = 0;
//
//            mFragmentsTags.remove(topFragmentTag);
//
//            setFragmentVisibilities(newTopFragmentTag);
//            String newTopFragmentTag = mFragmentsTags.get(backStackCount - 2);
//
//            String topFragmentTag = mFragmentsTags.get(backStackCount - 1);
//        if(backStackCount > 1){
//        int backStackCount = mFragmentsTags.size();
//
//        super.onBackPressed();
//    public void onBackPressed() {
//    @Override

}

