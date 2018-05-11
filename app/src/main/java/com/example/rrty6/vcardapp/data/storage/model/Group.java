package com.example.rrty6.vcardapp.data.storage.model;

import android.support.annotation.Nullable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "GROUPS")
public class Group {

    @DatabaseField(generatedId = true, columnName = "id")
    private Long id;

    @DatabaseField
    private String remoteId = null;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Logo logo = null;

    @DatabaseField(canBeNull = false)
    private String name = null;

    @DatabaseField
    private String description = null;

    public Group() {
    }

    //Constructor for UI
    public Group(@Nullable Logo logo, String name, @Nullable String description) throws Exception {
        if (name != null && !name.isEmpty()) {
            this.name = name;
            if (logo!=null && !logo.getLogo().isEmpty()) {
                this.logo = logo;
            }
            if (description!=null && !description.isEmpty()) {
                this.description = description;
            }
        } else {
            throw new Exception("Name can't be null or empty");
        }
    }

    //Constructor for DB
//    public Group(String remoteId, Logo logo, String name, String description) {
//        this.remoteId = remoteId;
//        this.logo = logo;
//        this.name = name;
//        this.description = description;
//    }

    public void update(Group group) {
        logo = group.getLogo();
        if (group.getName()!=null && !group.getName().isEmpty()) {
            name = group.getName();
        }
        description = group.getDescription();
    }

    //region===========================Setters==================================

    public void setId(Long id) {
        this.id = id;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    //endregion========================Setters==================================

    //region===========================Getters==================================

    public Long getId() {
        return id;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public Logo getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    //endregion========================Getters======================================
}
