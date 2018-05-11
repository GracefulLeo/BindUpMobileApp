package com.example.rrty6.vcardapp.data.storage.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "PHONE")
public class Phone implements Serializable{

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(foreign = true)
    private transient Card card;

    @DatabaseField
    private String phone;

    public Phone() {
    }

    public Phone(String phone) {
        if (phone!=null && !phone.isEmpty()) {
            this.phone = phone;
        }
    }

//    public Phone(Card card, String phone) {
//        this.card = card;
//        this.phone = phone;
//    }

    //region==================================Setters=============================

    public void setId(long id) {
        this.id = id;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //endregion===============================Setters=============================

    //region==================================Getters=============================

    public long getId() {
        return id;
    }

    public Card getCard() {
        return card;
    }

    public String getPhone() {
        return phone;
    }

    //endregion===============================Getters=============================


    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                '}';
    }
}
