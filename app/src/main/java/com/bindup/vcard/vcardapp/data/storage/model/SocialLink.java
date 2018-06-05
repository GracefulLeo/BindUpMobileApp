package com.bindup.vcard.vcardapp.data.storage.model;

import com.bindup.vcard.vcardapp.utils.Const.LinkType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@DatabaseTable(tableName = "SOCIALLINK")
public class SocialLink implements Serializable{
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(foreign = true)
    private transient Card card;

    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private LinkType type;

    @DatabaseField
    private String value;

    public SocialLink() {
    }

    public SocialLink(LinkType type, String value) {
        if (type != null && value != null && !value.isEmpty()) {
            this.type = type;
            this.value = value;
        }
    }

    //region==================================Setters=============================

    public void setId(long id) {
        this.id = id;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setType(LinkType type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //endregion===============================Setters=============================

    //region==================================Getters=============================

    public long getId() {
        return id;
    }

    public Card getCard() {
        return card;
    }

    public LinkType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    //endregion===============================Getters=============================
}
