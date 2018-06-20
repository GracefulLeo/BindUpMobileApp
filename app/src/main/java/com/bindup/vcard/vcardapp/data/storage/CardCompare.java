package com.bindup.vcard.vcardapp.data.storage;

import com.bindup.vcard.vcardapp.data.storage.model.Card;
import com.bindup.vcard.vcardapp.data.storage.model.Change;
import com.bindup.vcard.vcardapp.data.storage.model.Email;
import com.bindup.vcard.vcardapp.data.storage.model.History;
import com.bindup.vcard.vcardapp.data.storage.model.Phone;
import com.bindup.vcard.vcardapp.data.storage.model.SocialLink;
import com.bindup.vcard.vcardapp.utils.Const;
import com.bindup.vcard.vcardapp.utils.Const.CardFields;

import java.util.Date;
import java.util.Iterator;

import static com.bindup.vcard.vcardapp.utils.Const.CardFields.*;

public class CardCompare {//true - for changed field, false - for unchanged fields

    private History history;
    private boolean title = false;
    private boolean fieldLogotype = false;
    private boolean fieldName = false;
    private boolean fieldSurname = false;
    private boolean fieldMiddleName = false;
    private boolean fieldCompanyName = false;
    private boolean fieldPosition = false;
    private boolean fieldAddress = false;
    private boolean fieldPhone = false;
    private boolean fieldMail = false;
    private boolean fieldWebSite = false;
    private boolean fieldSocialLinks = false;
    private boolean fieldBase64Vcard = false;

    public CardCompare(Card previousCard, Card newCard) {
        history = new History(previousCard.getRemoteId());
        if (newCard.getTitle() != null && !previousCard.getTitle().equals(newCard.getTitle())) {
            title = true;
        }
        if (previousCard.getLogo() == null && newCard.getLogo() != null ||
                previousCard.getLogo() != null && !previousCard.getLogo().equals(newCard.getLogo())) {
            fieldLogotype = true;
            history.addChange(new Change(LOGO, null, null));//TODO: think about adding small logos just for history
        }
        if (!previousCard.getName().equals(newCard.getName())) {
            fieldName = true;
            history.addChange(new Change(NAME, previousCard.getName(), newCard.getName()));
        }
        if (!previousCard.getSurname().equals(newCard.getSurname())) {
            fieldSurname = true;
            history.addChange(new Change(SURNAME, previousCard.getSurname(), newCard.getSurname()));
        }
        if (previousCard.getMidlename() == null && newCard.getMidlename() != null ||
                previousCard.getMidlename() != null && !previousCard.getMidlename().equals(newCard.getMidlename())) {
            fieldMiddleName = true;
            history.addChange(new Change(MIDDLENAME, previousCard.getMidlename(), newCard.getMidlename()));
        }
        if (previousCard.getCompany() == null && newCard.getCompany() != null ||
                previousCard.getCompany() != null && !previousCard.getCompany().equals(newCard.getCompany())) {
            fieldCompanyName = true;
            history.addChange(new Change(COMPANY, previousCard.getCompany(), newCard.getCompany()));
        }
        if (previousCard.getPosition() == null && newCard.getPosition() != null ||
                previousCard.getPosition() != null && !previousCard.getPosition().equals(newCard.getPosition())) {
            fieldPosition = true;
            history.addChange(new Change(POSITION, previousCard.getPosition(), newCard.getPosition()));
        }
        if (previousCard.getAddress() == null && newCard.getAddress() != null ||
                previousCard.getAddress() != null && !previousCard.getAddress().equals(newCard.getAddress())) {
            fieldAddress = true;
            history.addChange(new Change(ADDRESS, previousCard.getAddress(), newCard.getAddress()));
        }
        if (previousCard.getSite() == null && newCard.getSite() != null ||
                previousCard.getSite() != null && !previousCard.getSite().equals(newCard.getSite())) {
            fieldWebSite = true;
            history.addChange(new Change(SITE, previousCard.getSite(), newCard.getSite()));
        }
        if (previousCard.getPhones() == null && newCard.getPhones() != null ||
                previousCard.getPhones() != null && !previousCard.getPhones().equals(newCard.getPhones())) {
            fieldPhone = true;
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
                        history.addChange(new Change(PHONES, phone1.getPhone(), null));
                    } else if (!phone1.equals(phone2)) {
                        history.addChange(new Change(PHONES, phone1.getPhone(), phone2.getPhone()));
                    }
                } else if (phone2 != null) {
                    history.addChange(new Change(PHONES, null, phone2.getPhone()));
                }
            }
        }
        if (previousCard.getEmails() == null && newCard.getEmails() != null ||
                previousCard.getEmails() != null && !previousCard.getEmails().equals(newCard.getEmails())) {
            fieldMail = true;
            Iterator iterator1 = previousCard.getEmails().iterator();
            Iterator iterator2 = newCard.getEmails().iterator();
            while (iterator1.hasNext() && iterator2.hasNext() && !fieldMail) {
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
                        history.addChange(new Change(EMAILS, email1.getEmail(), null));
                    } else if (!email1.equals(email2)) {
                        history.addChange(new Change(EMAILS, email1.getEmail(), email2.getEmail()));
                    }
                } else if (email2 != null) {
                    history.addChange(new Change(EMAILS, null, email2.getEmail()));
                }
            }
        }
        if (previousCard.getSocialLinks() == null && newCard.getSocialLinks() != null ||
                previousCard.getSocialLinks() != null && !previousCard.getSocialLinks().equals(newCard.getSocialLinks())) {
            fieldSocialLinks = true;
            if (newCard.getSocialLinks() == null) {
                for (SocialLink socialLink : previousCard.getSocialLinks()) {
                    history.addChange(new Change(CardFields.getField(socialLink.getType()), socialLink.getValue(), null));
                }
            }
            boolean isExist = false;
            for (SocialLink socialLink1 : previousCard.getSocialLinks()) {
                for (SocialLink socialLink2 : newCard.getSocialLinks()) {
                    if (socialLink1.getType() == socialLink2.getType() && !socialLink1.getValue().equals(socialLink2.getValue())) {
                        history.addChange(new Change(CardFields.getField(socialLink1.getType()), socialLink1.getValue(), socialLink2.getValue()));
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    history.addChange(new Change(CardFields.getField(socialLink1.getType()), socialLink1.getValue(), null));
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
                    history.addChange(new Change(CardFields.getField(socialLink2.getType()), null, socialLink2.getValue()));
                }
            }
        }
        if (previousCard.getBase().getBase64().equals(newCard.getBase().getBase64())) {
            fieldBase64Vcard = true;
        }
    }

    //region==================================Getters=============================

    public History getHistory() {
        return history;
    }

    public boolean isTitle() {
        return title;
    }

    public boolean isFieldLogotype() {
        return fieldLogotype;
    }

    public boolean isFieldName() {
        return fieldName;
    }

    public boolean isFieldSurname() {
        return fieldSurname;
    }

    public boolean isFieldMiddleName() {
        return fieldMiddleName;
    }

    public boolean isFieldCompanyName() {
        return fieldCompanyName;
    }

    public boolean isFieldPosition() {
        return fieldPosition;
    }

    public boolean isFieldAddress() {
        return fieldAddress;
    }

    public boolean isFieldPhone() {
        return fieldPhone;
    }

    public boolean isFieldMail() {
        return fieldMail;
    }

    public boolean isFieldWebSite() {
        return fieldWebSite;
    }

    public boolean isFieldSocialLinks() {
        return fieldSocialLinks;
    }

    public boolean isFieldBase64Vcard() {
        return fieldBase64Vcard;
    }


    //endregion===============================Getters=============================
}
