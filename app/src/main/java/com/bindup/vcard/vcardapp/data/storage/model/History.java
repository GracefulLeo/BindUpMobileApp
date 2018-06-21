package com.bindup.vcard.vcardapp.data.storage.model;

import android.support.annotation.NonNull;

import com.bindup.vcard.vcardapp.utils.Const;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import static com.bindup.vcard.vcardapp.utils.Const.CardFields.ADDRESS;
import static com.bindup.vcard.vcardapp.utils.Const.CardFields.COMPANY;
import static com.bindup.vcard.vcardapp.utils.Const.CardFields.EMAILS;
import static com.bindup.vcard.vcardapp.utils.Const.CardFields.LOGO;
import static com.bindup.vcard.vcardapp.utils.Const.CardFields.MIDDLENAME;
import static com.bindup.vcard.vcardapp.utils.Const.CardFields.NAME;
import static com.bindup.vcard.vcardapp.utils.Const.CardFields.PHONES;
import static com.bindup.vcard.vcardapp.utils.Const.CardFields.POSITION;
import static com.bindup.vcard.vcardapp.utils.Const.CardFields.SITE;
import static com.bindup.vcard.vcardapp.utils.Const.CardFields.SURNAME;

@DatabaseTable(tableName = "HISTORY")
public class History {
    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField
    private String contactId;
    @DatabaseField(dataType = DataType.DATE)
    private Date date;
    @ForeignCollectionField(eager = true)
    private Collection<Change> changes;

    public History() {
    }

    public History(@NonNull String contactId/*, Collection<Change> changes*/) {
        this.contactId = contactId;
        this.date = new Date();
        this.changes = new ArrayList<>();
//        this.changes = changes;
//        for (Change change : changes) {
//            change.setHistory(this);
//        }
    }

    public History(Card previousCard, Card newCard) {
        this.contactId = previousCard.getRemoteId();
        this.date = new Date();
        this.changes = new ArrayList<>();
        if (previousCard.getLogo() == null && newCard.getLogo() != null ||
                previousCard.getLogo() != null && !previousCard.getLogo().equals(newCard.getLogo())) {
            addChange(new Change(LOGO, null, null));//TODO: think about adding small logos just for history
        }
        if (!previousCard.getName().equals(newCard.getName())) {
            addChange(new Change(NAME, previousCard.getName(), newCard.getName()));
        }
        if (!previousCard.getSurname().equals(newCard.getSurname())) {
            addChange(new Change(SURNAME, previousCard.getSurname(), newCard.getSurname()));
        }
        if (previousCard.getMidlename() == null && newCard.getMidlename() != null ||
                previousCard.getMidlename() != null && !previousCard.getMidlename().equals(newCard.getMidlename())) {
            addChange(new Change(MIDDLENAME, previousCard.getMidlename(), newCard.getMidlename()));
        }
        if (previousCard.getCompany() == null && newCard.getCompany() != null ||
                previousCard.getCompany() != null && !previousCard.getCompany().equals(newCard.getCompany())) {
            addChange(new Change(COMPANY, previousCard.getCompany(), newCard.getCompany()));
        }
        if (previousCard.getPosition() == null && newCard.getPosition() != null ||
                previousCard.getPosition() != null && !previousCard.getPosition().equals(newCard.getPosition())) {
            addChange(new Change(POSITION, previousCard.getPosition(), newCard.getPosition()));
        }
        if (previousCard.getAddress() == null && newCard.getAddress() != null ||
                previousCard.getAddress() != null && !previousCard.getAddress().equals(newCard.getAddress())) {
            addChange(new Change(ADDRESS, previousCard.getAddress(), newCard.getAddress()));
        }
        if (previousCard.getSite() == null && newCard.getSite() != null ||
                previousCard.getSite() != null && !previousCard.getSite().equals(newCard.getSite())) {
            addChange(new Change(SITE, previousCard.getSite(), newCard.getSite()));
        }
        if (previousCard.getPhones() == null && newCard.getPhones() != null ||
                previousCard.getPhones() != null && !previousCard.getPhones().equals(newCard.getPhones())) {
            Iterator iterator1 = previousCard.getPhones().iterator();
            Iterator iterator2 = newCard.getPhones().iterator();
            while (iterator1.hasNext() || iterator2.hasNext()) {
                Phone phone1;
                if (iterator1.hasNext()) {
                    phone1 = (Phone) iterator1.next();
                } else {
                    phone1 = null;
                }
                Phone phone2;
                if (iterator2.hasNext()) {
                    phone2 = (Phone) iterator2.next();
                } else {
                    phone2 = null;
                }
                if (phone1 != null) {
                    if (phone2 == null) {
                        addChange(new Change(PHONES, phone1.getPhone(), null));
                    } else if (!phone1.equals(phone2)) {
                        addChange(new Change(PHONES, phone1.getPhone(), phone2.getPhone()));
                    }
                } else if (phone2 != null) {
                    addChange(new Change(PHONES, null, phone2.getPhone()));
                }
            }
        }
        if (previousCard.getEmails() == null && newCard.getEmails() != null ||
                previousCard.getEmails() != null && !previousCard.getEmails().equals(newCard.getEmails())) {
            Iterator iterator1 = previousCard.getEmails().iterator();
            Iterator iterator2 = newCard.getEmails().iterator();
            while (iterator1.hasNext() && iterator2.hasNext()) {
                Email email1;
                if (iterator1.hasNext()) {
                    email1 = (Email) iterator1.next();
                } else {
                    email1 = null;
                }
                Email email2;
                if (iterator2.hasNext()) {
                    email2 = (Email) iterator2.next();
                } else {
                    email2 = null;
                }
                if (email1 != null) {
                    if (email2 == null) {
                        addChange(new Change(EMAILS, email1.getEmail(), null));
                    } else if (!email1.equals(email2)) {
                        addChange(new Change(EMAILS, email1.getEmail(), email2.getEmail()));
                    }
                } else if (email2 != null) {
                    addChange(new Change(EMAILS, null, email2.getEmail()));
                }
            }
        }
        if (previousCard.getSocialLinks() == null && newCard.getSocialLinks() != null ||
                previousCard.getSocialLinks() != null && !previousCard.getSocialLinks().equals(newCard.getSocialLinks())) {
            if (newCard.getSocialLinks() == null) {
                for (SocialLink socialLink : previousCard.getSocialLinks()) {
                    addChange(new Change(Const.CardFields.getField(socialLink.getType()), socialLink.getValue(), null));
                }
            }
            boolean isExist = false;
            for (SocialLink socialLink1 : previousCard.getSocialLinks()) {
                for (SocialLink socialLink2 : newCard.getSocialLinks()) {
                    if (socialLink1.getType() == socialLink2.getType() && !socialLink1.getValue().equals(socialLink2.getValue())) {
                        addChange(new Change(Const.CardFields.getField(socialLink1.getType()), socialLink1.getValue(), socialLink2.getValue()));
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    addChange(new Change(Const.CardFields.getField(socialLink1.getType()), socialLink1.getValue(), null));
                    isExist = false;
                }
            }
            for (SocialLink socialLink2 : newCard.getSocialLinks()) {
                for (SocialLink socialLink1 : previousCard.getSocialLinks()) {
                    if (socialLink2.getType() == socialLink1.getType()) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    addChange(new Change(Const.CardFields.getField(socialLink2.getType()), null, socialLink2.getValue()));
                }
            }
        }
    }

    public void addChange(Change change) {
        change.setHistory(this);
        this.changes.add(change);
    }

    public boolean isEmpty() {
        return changes.isEmpty();
    }

    //region==================================Setters=============================

    public void setId(Long id) {
        this.id = id;
    }

    public void setContactId(String contactId) {
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

    public String getContactId() {
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