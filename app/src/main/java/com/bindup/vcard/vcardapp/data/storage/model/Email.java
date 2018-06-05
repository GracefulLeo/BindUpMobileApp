package com.bindup.vcard.vcardapp.data.storage.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "EMAIL")
//Entity for DB
public class Email implements Serializable {

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(foreign = true)
    private transient Card card = null;
    @DatabaseField
    private String email = null;

    public Email() {
    }

    //For UI method
    public Email(String email) {
        if (email != null && !email.isEmpty()) {
            this.email = email;
        }
    }

//    public Email(Card card, String email) {
//        this.card = card;
//        this.email = email;
//    }

    //region==================================Setters=============================

    public void setId(long id) {
        this.id = id;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //endregion===============================Setters=============================

    //region==================================Getters=============================

    public long getId() {
        return id;
    }

    public Card getCard() {
        return card;
    }

    public String getEmail() {
        return email;
    }

    //endregion===============================Getters=============================


    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
