package com.bindup.vcard.vcardapp.data.exchange;

import com.bindup.vcard.vcardapp.data.managers.DataManager;
import com.bindup.vcard.vcardapp.data.storage.model.Card;

import java.io.Serializable;

public class ExchangeModel implements Serializable {
    private Card card;
    private String token;

    public ExchangeModel(Card card) {
        this.card = card;
        this.token = DataManager.getInstance().getPreferenceManager().loadToken();
    }
}