package com.bindup.vcard.vcardapp.data.storage.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.Date;

@DatabaseTable(tableName = "HISTORY")
public class History {
    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField(foreign = true,columnName = "CONTACT")
    private Card contactId;
    @DatabaseField(dataType = DataType.DATE)
    private Date date;
    @ForeignCollectionField(eager = true)
    private Collection<Change> changes;

    public History() {
    }

    public History(Card contactId, Date date, Collection<Change> changes) {
        this.contactId = contactId;
        this.date = date;
        this.changes = changes;
        for (Change change : changes) {
            change.setHistory(this);
        }
    }

    //region==================================Setters=============================

    public void setId(Long id) {
        this.id = id;
    }

    public void setContactId(Card contactId) {
        this.contactId = contactId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setChanges(Collection<Change> changes) {
        this.changes = changes;
    }


    //endregion===============================Setters=============================


    //region==================================Getters=============================

    public Long getId() {
        return id;
    }

    public Card getContactId() {
        return contactId;
    }

    public Date getDate() {
        return date;
    }

    public Collection<Change> getChanges() {
        return changes;
    }


    //endregion===============================Getters=============================
}