package com.bindup.vcard.vcardapp.data.storage.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "GROUPCARD")
public class GroupCard {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "group_id")
    private Group group;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "card_id")
    private Card card;

    public GroupCard() {
    }

    public GroupCard(Group group, Card card) {
        this.group = group;
        this.card = card;
    }

    //region===========================Setters==================================

    public void setId(Long id) {
        this.id = id;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setCard(Card card) {
        this.card = card;
    }


    //endregion========================Setters==================================

    //region===========================Getters==================================

    public Long getId() {
        return id;
    }

    public Group getGroup() {
        return group;
    }

    public Card getCard() {
        return card;
    }


    //endregion========================Getters======================================
}
