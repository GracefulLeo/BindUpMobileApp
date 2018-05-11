package com.example.rrty6.vcardapp.ui.interfaces;

import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Group;

public interface IMainActivity   {

    void  inflateViewProfileFragment(Card card);
    void inflateViewContactProfileFragment (Card card);
    void inflateViewGroupProfileGroups (Group group);

}
