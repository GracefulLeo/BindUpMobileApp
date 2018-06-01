package com.example.rrty6.vcardapp.ui.interfaces;

import android.content.Context;

import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Group;

public interface IMainActivity   {

    void inflateViewProfileFragment(Card card);

    void inflateViewContactProfileFragment (Card card);

    void inflateViewGroupProfileGroups (Group group);

    void inflateMyVCardFragment (Context context);

    void inflateGroupFragment (Context context);

    void inflateGroupCreateFragment (Context context);

    void inflateGroupNoGroupsFragment (Context context);

    void inflateCreateCardFragment (Context context);

    void inflateVCardFirstLoginFragment (Context context);

    void inflateContactFragment (Context context);

    void onBackPressed ();

}
