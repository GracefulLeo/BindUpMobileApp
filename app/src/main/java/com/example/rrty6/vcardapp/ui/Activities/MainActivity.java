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
import com.example.rrty6.vcardapp.ui.adapter.MyVCardsFragmentAdapter;
import com.example.rrty6.vcardapp.ui.interfaces.IMainActivity;
import com.example.rrty6.vcardapp.utils.App;
import com.example.rrty6.vcardapp.utils.PreferenceKeys;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IMainActivity {

    private static final String TAG = "MainActivity";

    private MyVCardsFragmentAdapter mMyVcardFragmentAdapter;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;

    public static FloatingActionButton mFab;
//@TODO check, is there necessary to have android:hardwareAccelerated="false"android:largeHeap="true" , in androidmanifest xml file
    private List<Card> cards;
    private List<Group> groups;
    private MyVCardPreviewFragment mMyVcardPreviewFragment;
    private ContactsPreviewFragment contactsPreviewFragment;
    private Bundle args = new Bundle();
    private GroupsPreviewFragment groupsPreviewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(this);


        init();
        initToolbar();
        initNavigationView();
        isFirstLogin();
        if (getIntent().getStringExtra("key") != null && getIntent().getStringExtra("key").equals("To contacts")) {
            ContactsFragment mContactsFragment = new ContactsFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_content_frame, mContactsFragment, getString(R.string.tag_contacts));
            transaction.addToBackStack(getString(R.string.tag_contacts));
            transaction.commit();
        }
    }


    //  ----------------------------------!!!initialization!!!------------------------------------------


    private void firstCheck() {
        cards = new ArrayList<>();
        try {
            cards = MainOperations.getCardList();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (cards != null) {
            if (cards.size() == 0) {
                MyVcardFirstLoginFragment myVcardFirstLoginFragment = new MyVcardFirstLoginFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_content_frame, myVcardFirstLoginFragment, getString(R.string.tag_MyVcards));
                transaction.addToBackStack("MyVcard");
                transaction.commit();
            }
        }
    }

    private void init() {

        MyVcardFragment mMyVcardFragment = new MyVcardFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content_frame, mMyVcardFragment, getString(R.string.tag_MyVcards));
        transaction.addToBackStack(getString(R.string.tag_MyVcards));
        transaction.commit();
        Log.d(TAG, "init: fragment transaction...");
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_button_in_preview_fragment:
//@TODO Make that Bundle here is work properly, test more...
                        MyVCardEditFragment mMyVcardEditFragment = new MyVCardEditFragment();
                        mMyVcardEditFragment.setArguments(args);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_content_frame, mMyVcardEditFragment, getString(R.string.tag_MyVcards));
                        transaction.addToBackStack(getString(R.string.tag_MyVcards));
                        transaction.commit();
                        Log.d(TAG, "init: MyVCards fragment transaction...");
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
//                                MyVcardFirstLoginFragment mMyVcardFirstLoginFragment = new MyVcardFirstLoginFragment();
//                                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
//                                transaction1.replace(R.id.main_content_frame, mMyVcardFirstLoginFragment, getString(R.string.tag_MyVcards));
//                                transaction1.addToBackStack(getString(R.string.tag_MyVcards));
//                                transaction1.commit();
//                                Log.d(TAG, "init: MyVCards fragment transaction...");
                                MyVcardFragment mMyVcardFragment = new MyVcardFragment();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.main_content_frame, mMyVcardFragment, getString(R.string.tag_MyVcards));
                                transaction.addToBackStack(getString(R.string.tag_MyVcards));
                                transaction.commit();
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
                        cards = new ArrayList<>();
                        try {
                            cards = MainOperations.getCardList();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (cards != null && cards.size() == 0) {
                            MyVcardFirstLoginFragment myVcardFirstLoginFragment = new MyVcardFirstLoginFragment();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.main_content_frame, myVcardFirstLoginFragment, getString(R.string.tag_MyVcards));
                            transaction.addToBackStack("MyVcard");
                            transaction.commit();
                            break;

                        }
                        Log.d(TAG, "onNavigationItemSelected: MyVCard item selected...");
                        MyVcardFragment mMyVcardFragment = new MyVcardFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_content_frame, mMyVcardFragment, getString(R.string.tag_MyVcards));
                        transaction.addToBackStack(getString(R.string.tag_MyVcards));
                        transaction.commit();
                        Log.d(TAG, "init: MyVCards fragment transaction...");
                        break;
                    }
                    case R.id.contactsItem: {
                        Log.d(TAG, "onNavigationItemSelected: Contacts item selected...");
                        ContactsFragment mContactsFragment = new ContactsFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_content_frame, mContactsFragment, getString(R.string.tag_contacts));
                        transaction.addToBackStack(getString(R.string.tag_contacts));
                        transaction.commit();
                        Log.d(TAG, "init:Contact fragment transaction...");
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
                                GroupsNoGroupsFragment groupsNoGroupsFragment = new GroupsNoGroupsFragment();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.main_content_frame, groupsNoGroupsFragment, "Groups");
                                transaction.addToBackStack("Groups");
                                transaction.commit();
                                break;
                            }
                        }
                        if (groups.size() != 0) {
                            Log.d(TAG, "onNavigationItemSelected: Groups item selected...");
                            GroupsFragment groupsFragment = new GroupsFragment();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.main_content_frame, groupsFragment, "Groups");
                            transaction.addToBackStack("Groups");
                            transaction.commit();
                            Log.d(TAG, "onNavigationItemSelected: Group fragment transaction");
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
                        if (cards.isEmpty()){

                        }
                        Intent intent = new Intent(MainActivity.this, ShareActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                    case R.id.log_out_item:
                        Log.d(TAG, "onNavigationItemSelected: log out item clicked...");
                        intent = new Intent(MainActivity.this, LoginActivity.class);
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
            MyVcardFirstLoginFragment fragment = new MyVcardFirstLoginFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_content_frame, fragment, getString(R.string.tag_fragment_preview_my_vcard));
            transaction.addToBackStack(getString(R.string.tag_fragment_preview_my_vcard));
            transaction.commit();
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
        if (isFirstLogin == false) {
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
                Log.d(TAG, "onNavigationItemSelected: Fab pressed...");
                MyVcardCreateCardFragment mVCardViewFragment = new MyVcardCreateCardFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_content_frame, mVCardViewFragment, "Groups");
                transaction.addToBackStack("Groups");
                transaction.commit();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                Log.d(TAG, "onNavigationItemSelected:Groups fragment transaction...");
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
    }

    @Override
    public void inflateViewContactProfileFragment(Card card) {
        if (contactsPreviewFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(contactsPreviewFragment).commitAllowingStateLoss();
        }
        contactsPreviewFragment = new ContactsPreviewFragment();
//@TODO change all tags to the normal one
        Bundle args = new Bundle();
        args.putLong("card id", card.getId());
        contactsPreviewFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content_frame, contactsPreviewFragment, getString(R.string.tag_fragment_contacts_preview));
        transaction.commit();
    }

    @Override
    public void inflateViewGroupProfileGroups(Group group) {
        if (groupsPreviewFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(groupsPreviewFragment).commitAllowingStateLoss();
        }
        groupsPreviewFragment = new GroupsPreviewFragment();
        Bundle args = new Bundle();
        args.putLong("groups id", group.getId());
        groupsPreviewFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content_frame, groupsPreviewFragment, "Groups");
        transaction.commit();
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

