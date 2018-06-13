package com.bindup.vcard.vcardapp.data.storage.model;

import com.bindup.vcard.vcardapp.utils.Const.CardFields;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "CHANGE")
public class Change {
    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField(foreign = true)
    private transient History history;
    @DatabaseField
    private CardFields field;
    @DatabaseField
    private String previousValue;
    @DatabaseField
    private String newValue;

    public Change() {
    }

    public Change(CardFields field, String previousValue, String newValue) {
        this.field = field;
        this.previousValue = previousValue;
        this.newValue = newValue;
    }

    //region==================================Setters=============================

    public void setId(Long id) {
        this.id = id;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public void setField(CardFields field) {
        this.field = field;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    //endregion===============================Setters=============================

    //region==================================Getters=============================

    public Long getId() {
        return id;
    }

    public History getHistory() {
        return history;
    }

    public CardFields getField() {
        return field;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public String getNewValue() {
        return newValue;
    }

    //endregion===============================Getters=============================
}