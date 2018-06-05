package com.bindup.vcard.vcardapp.data.storage;

import com.bindup.vcard.vcardapp.data.storage.model.Card;
import com.bindup.vcard.vcardapp.data.storage.model.Email;
import com.bindup.vcard.vcardapp.data.storage.model.Phone;
import com.bindup.vcard.vcardapp.data.storage.model.SocialLink;

import java.util.Iterator;

public class CardCompare {

    public boolean title = true;
    public boolean fieldLogotype = true;
    public boolean fieldName = true;
    public boolean fieldSurname = true;
    public boolean fieldMiddleName = true;
    public boolean fieldCompanyName = true;
    public boolean fieldPosition = true;
    public boolean fieldAddress = true;
    public boolean fieldPhone = true;
    public boolean fieldMail = true;
    public boolean fieldWebSite = true;
    public boolean fieldSocialLinks = true;
    public boolean fieldBase64Vcard = true;

    public CardCompare(Card card1, Card card2) {
        if (card1.getTitle() == null && card2.getTitle() == null ||
                card1.getTitle() != null && card2.getTitle() != null && card1.getTitle().equals(card2.getTitle())) {
            title = false;
        }
        if (card1.getLogo() == null && card2.getLogo() == null ||
                card1.getLogo() != null && card2.getLogo() != null && (card1.getLogo().getLogo() == null && card2.getLogo().getLogo() == null ||
                card1.getLogo() != null && card2.getLogo() != null && card1.getLogo().getLogo().equals(card2.getLogo().getLogo()))) {
            fieldLogotype = false;
        }
        if (card1.getName() == null && card2.getName() == null ||
                card1.getName() != null && card2.getName() != null && card1.getName().equals(card2.getName())) {
            fieldName = false;
        }
        if (card1.getSurname() == null && card2.getSurname() == null ||
                card1.getSurname() != null && card2.getSurname() != null && card1.getSurname().equals(card2.getSurname())) {
            fieldSurname = false;
        }
        if (card1.getMidlename() == null && card2.getMidlename() == null ||
                card1.getMidlename() != null && card2.getMidlename() != null && card1.getMidlename().equals(card2.getMidlename())) {
            fieldMiddleName = false;
        }
        if (card1.getCompany() == null && card2.getCompany() == null ||
                card1.getCompany() != null && card2.getCompany() != null && card1.getCompany().equals(card2.getCompany())) {
            fieldCompanyName = false;
        }
        if (card1.getPosition() == null && card2.getPosition() == null ||
                card1.getPosition() != null && card2.getPosition() != null && card1.getPosition().equals(card2.getPosition())) {
            fieldPosition = false;
        }
        if (card1.getAddress() == null && card2.getAddress() == null ||
                card1.getAddress() != null && card2.getAddress() != null && card1.getAddress().equals(card2.getAddress())) {
            fieldAddress = false;
        }
        if (card1.getPhones() == null && card2.getPhones() == null ||
                card1.getPhones() != null && card2.getPhones() != null && card1.getPhones().size() == card2.getPhones().size()) {
            fieldPhone = false;
            Iterator iterator1 = card1.getPhones().iterator();
            Iterator iterator2 = card2.getPhones().iterator();
            while (iterator1.hasNext() && iterator2.hasNext() && !fieldPhone) {
                Phone phone1 = (Phone) iterator1.next();
                Phone phone2 = (Phone) iterator2.next();
                if (!phone1.getPhone().equals(phone2.getPhone())) {
                    fieldPhone = true;
                }
            }
        }
        if (card1.getEmails() == null && card2.getEmails() == null ||
                card1.getEmails() != null && card2.getEmails() != null && card1.getEmails().size() == card2.getEmails().size()) {
            fieldMail = false;
            Iterator iterator1 = card1.getEmails().iterator();
            Iterator iterator2 = card2.getEmails().iterator();
            while (iterator1.hasNext() && iterator2.hasNext() && !fieldMail) {
                Email email1 = (Email) iterator1.next();
                Email email2 = (Email) iterator2.next();
                if (!email1.getEmail().equals(email2.getEmail())) {
                    fieldMail = true;
                }
            }
        }
        if (card1.getSocialLinks() == null && card2.getSocialLinks() == null ||
                card1.getSocialLinks() != null && card2.getSocialLinks() != null && card1.getSocialLinks().size() == card2.getSocialLinks().size()) {
            fieldSocialLinks = false;
            for (SocialLink socialLink1 : card1.getSocialLinks()) {
                for (SocialLink socialLink2 : card2.getSocialLinks()) {
                    if (socialLink1.getType() == socialLink2.getType() && !socialLink1.getValue().equals(socialLink2.getValue())) {
                        fieldSocialLinks = true;
                        break;
                    }
                }
                if (fieldSocialLinks) {
                    break;
                }
            }
        }
        if (card1.getSite() == null && card2.getSite() == null ||
                card1.getSite() != null && card2.getSite() != null && card1.getSite().equals(card2.getSite())) {
            fieldWebSite = false;
        }
        if (card1.getBase() == null && card2.getBase() == null ||
                card1.getBase() != null && card2.getBase() != null && (card1.getBase().getBase64() == null && card2.getBase().getBase64() == null ||
                card1.getBase().getBase64() != null && card2.getBase().getBase64() != null && card1.getBase().getBase64().equals(card2.getBase().getBase64()))) {
            fieldBase64Vcard = false;
        }
    }
}
