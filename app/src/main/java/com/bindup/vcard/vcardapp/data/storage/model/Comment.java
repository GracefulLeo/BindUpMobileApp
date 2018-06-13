package com.bindup.vcard.vcardapp.data.storage.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "COMMENTS")
public class Comment {

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField(foreign = true, columnName = "CONTACT")
    private Card contact;
    @DatabaseField
    private String comment;

    public Comment() {
    }

    //For UI method
    public Comment(Card contact, String comment) {
        this.contact = contact;
        this.comment = comment;
    }

    //region==================================Setters=============================

    public void setId(Long id) {
        this.id = id;
    }

    public void setContact(Card contact) {
        this.contact = contact;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    //endregion===============================Setters=============================


    //region==================================Getters=============================

    public Long getId() {
        return id;
    }

    public Card getContact() {
        return contact;
    }

    public String getComment() {
        return comment;
    }


    //endregion===============================Getters=============================
}
