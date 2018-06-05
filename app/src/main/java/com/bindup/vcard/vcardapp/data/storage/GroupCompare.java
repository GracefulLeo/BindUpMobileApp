package com.bindup.vcard.vcardapp.data.storage;

import com.bindup.vcard.vcardapp.data.storage.model.Group;

public class GroupCompare {

    public boolean fieldLogotype = true;

    public boolean fieldGroupName = true;

    public boolean fieldDescription = true;

    public GroupCompare(Group group1, Group group2) {
        if (group1.getLogo() == null && group2.getLogo() == null ||
                group1.getLogo() != null && group2.getLogo() != null && (group1.getLogo().getLogo() == null && group2.getLogo().getLogo() == null ||
                        group1.getLogo() != null && group2.getLogo() != null && group1.getLogo().getLogo().equals(group2.getLogo().getLogo()))) {
            fieldLogotype = false;
        }
        if (group1.getName() == null && group2.getName() == null ||
                group1.getName() != null && group2.getName() != null && group1.getName().equals(group2.getName())) {
            fieldGroupName = false;
        }
        if (group1.getDescription() == null && group2.getDescription() == null ||
                group1.getDescription() != null && group2.getDescription() != null && group1.getDescription().equals(group2.getDescription())) {
            fieldDescription = false;
        }
    }
}
