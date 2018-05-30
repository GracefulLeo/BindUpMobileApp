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
import com.example.rrty6.vcardapp.ui.Fragments.GroupCreateFragment;
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
                        inflateMyVCardEditFragment();
                        break;

                    case R.id.back_arrow_contacts:
                        onBackPressed();
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
                                dialog.dismiss();
                                inflateMyVCardFragment(getApplicationContext());
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
                            inflateVCardFirstLoginFragment(getApplicationContext());
                            break;
                        }
                        Log.d(TAG, "onNavigationItemSelected: MyVCard item selected...");
                        inflateMyVCardFragment(getApplicationContext());
                        break;
                    }
                    case R.id.contactsItem: {
                        inflateContactFragment(getApplicationContext());
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
                                inflateGroupNoGroupsFragment(getApplicationContext());
                                break;
                            }
                        }
                        if (groups.size() != 0) {
                            inflateGroupFragment();
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
                        //@TODO Play around case:"there is no vcards" . For now it's just a mock dialog
                        if (cards.isEmpty()) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                            alertDialogBuilder.setMessage(getString(R.string.no_vcards_share_fragment));
                            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG, "onClick: Closing dialog...");
                                    inflateMyVCardFragment(MainActivity.this);
                                    dialog.dismiss();
                                }
                            });
                            alertDialogBuilder.setIcon(R.drawable.ic_warning);
                            alertDialogBuilder.setTitle(" ");
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        } else {
                            inflateShareFragment();
                        }
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

    //Constants
    private static final String TAG = "MainActivity";
    public static FloatingActionButton mFab;

    //vars
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private List<Card> cards;
    private List<Group> groups;
    private Bundle args = new Bundle();
    private ArrayList<String> mFragmentTags = new ArrayList<>();
    private ArrayList<FragmentTag> mFragments = new ArrayList<>();
    private int mExitCount = 0;

    //Fragments
    private GroupsFragment mGroupsFragment;
    private GroupCreateFragment mGroupCreateFragment; //++++
    private GroupsNoGroupsFragment mGroupsNoGroupsFragment; //++++
    private GroupsPreviewFragment mGroupsPreviewFragment;
    private MyVcardCreateCardFragment mMyVcardCreateCardFragment; //++++
    private MyVCardEditFragment mMyVcardEditFragment;
    private MyVcardFragment mMyVcardFragment; //++++
    private MyVcardFirstLoginFragment mMyVcardFirstLoginFragment; //++++
    private MyVCardPreviewFragment mMyVcardPreviewFragment;
    private ContactsFragment mContactsFragment; //++++
    private ContactsPreviewFragment mContactsPreviewFragment;
    private ShareFragment mShareFragment;

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
            inflateContactFragment(this);
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
                inflateVCardFirstLoginFragment(this);
            }
        }
    }

    private void init() {
        // Initizialisation ....
        inflateMyVCardFragment(this);
    }

    public void isFirstLogin() {
        Log.d(TAG, "isFirstLogin: checking if this a first login...");
        final SharedPreferences preferences = App.getSharedPreferences();
        boolean isFirstLogin = preferences.getBoolean(PreferenceKeys.FIRST_TIME_LOGIN, true);
        if (isFirstLogin) {
            inflateVCardFirstLoginFragment(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                inflateCreateCardFragment(this);
                break;
        }
    }

    // Here is placed code for inflating all fragments in app putted in separate methods
    //----------------------------------------------------------------------------------
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
        if (mContactsPreviewFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(mContactsPreviewFragment).commitAllowingStateLoss();
        }
        mContactsPreviewFragment = new ContactsPreviewFragment();
        //@TODO same here as bellow. Check are we really need to initialize args like that...
        Bundle args = new Bundle();
        args.putLong("card id", card.getId());
        mContactsPreviewFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content_frame, mContactsPreviewFragment, getString(R.string.tag_fragment_preview_contacts));
        transaction.commit();
        mFragmentTags.add(getString(R.string.tag_fragment_preview_contacts));
        mFragments.add(new FragmentTag(mContactsPreviewFragment, getString(R.string.tag_fragment_preview_contacts)));

        setFragmentVisibility(getString(R.string.tag_fragment_preview_contacts));
    }

    @Override
    public void inflateViewGroupProfileGroups(Group group) {
        if (mGroupsPreviewFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(mGroupsPreviewFragment).commitAllowingStateLoss();
        }
        mGroupsPreviewFragment = new GroupsPreviewFragment();
        //@TODO Check is this bundle initialization is correct
        Bundle args = new Bundle();
        args.putLong("groups id", group.getId());
        mGroupsPreviewFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content_frame, mGroupsPreviewFragment, getString(R.string.tag_fragment_preview_groups));
        transaction.commit();
        mFragmentTags.add(getString(R.string.tag_fragment_preview_groups));
        mFragments.add(new FragmentTag(mGroupsPreviewFragment, getString(R.string.tag_fragment_preview_groups)));

        setFragmentVisibility(getString(R.string.tag_fragment_preview_groups));
    }

    @Override
    public void inflateMyVCardFragment(Context context) {
        if (mMyVcardFragment == null) {
            Log.d(TAG, "Inflating: My VCard...");
            mMyVcardFragment = new MyVcardFragment(this);
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

    @Override
    public void inflateGroupCreateFragment(Context context) {
        if (mGroupCreateFragment == null) {
            Log.d(TAG, "Inflating: GroupsCreateFragment...");
            mGroupCreateFragment = new GroupCreateFragment(this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content_frame, mGroupCreateFragment, getString(R.string.tag_fragment_groups_create));
            transaction.commit();
            mFragmentTags.add(getString(R.string.tag_fragment_groups_create));
            mFragments.add(new FragmentTag(mGroupCreateFragment, getString(R.string.tag_fragment_groups_create)));
        } else {
            mFragmentTags.remove(getString(R.string.tag_fragment_groups_create));
            mFragmentTags.add(getString(R.string.tag_fragment_groups_create));
        }
        setFragmentVisibility(getString(R.string.tag_fragment_groups_create));
    }

    @Override
    public void inflateGroupNoGroupsFragment(Context context) {
        if (mGroupsNoGroupsFragment == null) {
            Log.d(TAG, "Inflating: GroupsNoGroupFragment...");
            mGroupsNoGroupsFragment = new GroupsNoGroupsFragment(this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content_frame, mGroupsNoGroupsFragment, getString(R.string.tag_fragment_groups_no_groups));
            transaction.commit();
            mFragmentTags.add(getString(R.string.tag_fragment_groups_no_groups));
            mFragments.add(new FragmentTag(mGroupsNoGroupsFragment, getString(R.string.tag_fragment_groups_no_groups)));
        } else {
            mFragmentTags.remove(getString(R.string.tag_fragment_groups_no_groups));
            mFragmentTags.add(getString(R.string.tag_fragment_groups_no_groups));
        }
        setFragmentVisibility(getString(R.string.tag_fragment_groups_no_groups));
    }

    @Override
    public void inflateCreateCardFragment(Context context) {
        if (mMyVcardCreateCardFragment == null) {
            Log.d(TAG, "Inflating: CreateCardFragment...");
            mMyVcardCreateCardFragment = new MyVcardCreateCardFragment(this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content_frame, mMyVcardCreateCardFragment, getString(R.string.tag_fragment_my_vcard_create_card));
            transaction.commit();
            mFragmentTags.add(getString(R.string.tag_fragment_my_vcard_create_card));
            mFragments.add(new FragmentTag(mMyVcardCreateCardFragment, getString(R.string.tag_fragment_my_vcard_create_card)));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            mFragmentTags.remove(getString(R.string.tag_fragment_my_vcard_create_card));
            mFragmentTags.add(getString(R.string.tag_fragment_my_vcard_create_card));
        }
        setFragmentVisibility(getString(R.string.tag_fragment_my_vcard_create_card));
    }

    @Override
    public void inflateVCardFirstLoginFragment(Context context) {
        if (mMyVcardFirstLoginFragment == null) {
            Log.d(TAG, "Inflating: FirstLogin...");
            mMyVcardFirstLoginFragment = new MyVcardFirstLoginFragment(this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content_frame, mMyVcardFirstLoginFragment, getString(R.string.tag_fragment_my_vcard));
            transaction.commit();
            mFragmentTags.add(getString(R.string.tag_fragment_my_vcard_first_login));
            mFragments.add(new FragmentTag(mMyVcardFirstLoginFragment, getString(R.string.tag_fragment_my_vcard_first_login)));
        } else {
            mFragmentTags.remove(getString(R.string.tag_fragment_my_vcard_first_login));
            mFragmentTags.add(getString(R.string.tag_fragment_my_vcard_first_login));
        }
        setFragmentVisibility(getString(R.string.tag_fragment_my_vcard_first_login));
    }

    @Override
    public void inflateContactFragment(Context context) {
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
    }

    private void inflateGroupFragment() {
        if (mGroupsFragment == null) {
            Log.d(TAG, "onNavigationItemSelected: Groups...");
            mGroupsFragment = new GroupsFragment(this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content_frame, mGroupsFragment, getString(R.string.tag_fragment_groups));
            transaction.commit();
            mFragmentTags.add(getString(R.string.tag_fragment_groups));
            mFragments.add(new FragmentTag(mGroupsFragment, getString(R.string.tag_fragment_groups)));
        } else {
            mFragmentTags.remove(getString(R.string.tag_fragment_groups));
            mFragmentTags.add(getString(R.string.tag_fragment_groups));
        }
        setFragmentVisibility(getString(R.string.tag_fragment_groups));
    }

    private void inflateMyVCardEditFragment() {
        if (mMyVcardEditFragment == null) {
            Log.d(TAG, "onMenuItemClick: Edit My VCard...");
            mMyVcardEditFragment = new MyVCardEditFragment(this);
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
    }

    private void inflateShareFragment() {
        if (mShareFragment == null) {
            Log.d(TAG, "onNavigationItemSelected: Share...");
            mShareFragment = new ShareFragment(this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content_frame, mShareFragment, getString(R.string.tag_fragment_share));
            transaction.commit();
            mFragmentTags.add(getString(R.string.tag_fragment_share));
            mFragments.add(new FragmentTag(mShareFragment, getString(R.string.tag_fragment_share)));
        } else {
            mFragmentTags.remove(getString(R.string.tag_fragment_share));
            mFragmentTags.add(getString(R.string.tag_fragment_share));
        }
        setFragmentVisibility(getString(R.string.tag_fragment_share));
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

    @Override
    public void onBackPressed() {
        int backStackCount = mFragmentTags.size();
        if (backStackCount > 1){
            //navigate backwards...
            String topFragmentTag = mFragmentTags.get(backStackCount - 1);
            String newTopFragmentTag = mFragmentTags.get(backStackCount - 2);
            setFragmentVisibility(newTopFragmentTag);
            mFragmentTags.remove(topFragmentTag);
            mExitCount = 0;
        }
        else if(backStackCount == 1){
            String topFragmentTag = mFragmentTags.get(backStackCount - 1);
            if (topFragmentTag.equals(getString(R.string.tag_fragment_my_vcard))){
                mMyVcardFragment.scrollToTop();
                mExitCount ++;
                Toast.makeText(this, R.string.message_for_one_more_tap_to_exit, Toast.LENGTH_SHORT).show();
            }
            else {
                mExitCount ++;
                Toast.makeText(this, R.string.message_for_one_more_tap_to_exit, Toast.LENGTH_SHORT).show();
            }
        }
        if (mExitCount >= 2){
            super.onBackPressed();
        }
    }

    // Methods for FAB. (Hiding and showing)
    public void showFloatingActionButton() {
        mFab.show();
    }

    public void hideFloatingActionButton() {
        mFab.hide();
    }

}

