package com.bindup.vcard.vcardapp.ui.interfaces;

import android.content.Context;

import com.bindup.vcard.vcardapp.data.storage.model.Card;
import com.bindup.vcard.vcardapp.data.storage.model.Group;

public interface IMainActivity   {

    void inflateViewProfileFragment(Card card);

    void inflateViewContactProfileFragment (Card card);

    void inflateGroupPreviewFragment (Group group);

    void inflateViewGroupsSingleContactPreview(Card card);

    void inflateMyVCardFragment (Context context);

    void inflateGroupFragment (Context context);

    void inflateGroupCreateFragment (Context context);

    void inflateGroupNoGroupsFragment (Context context);

    void inflateCreateCardFragment (Context context);

    void inflateVCardFirstLoginFragment (Context context);

    void inflateContactFragment (Context context);

    void onBackPressed ();

}
