package com.example.rrty6.vcardapp.data.storage.operation;


import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.rrty6.vcardapp.data.managers.DataManager;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Email;
import com.example.rrty6.vcardapp.data.storage.model.Base;
import com.example.rrty6.vcardapp.data.storage.model.Group;
import com.example.rrty6.vcardapp.data.storage.model.GroupCard;
import com.example.rrty6.vcardapp.data.storage.model.Logo;
import com.example.rrty6.vcardapp.data.storage.model.Phone;
import com.example.rrty6.vcardapp.data.storage.model.SocialLink;

import java.sql.SQLException;
import java.util.List;

//Stub to promote work with DB
public class DatabaseOperation {
    private static final DataManager DATA_MANAGER = DataManager.getInstance();
//    private Handler handler;
//
//    public DatabaseOperation(Handler handler) {
//        this.handler = handler;
//    }

    //Save one MyCard/Contact from server
    public static void saveCard(final Card card) {
        DATA_MANAGER.addLogo(card.getLogo());

        DATA_MANAGER.addBase64(card.getBase());

        DATA_MANAGER.addCard(card);

        if (card.getSocialLinks() != null && card.getSocialLinks().size() > 0) {
            for (SocialLink link : card.getSocialLinks()) {
                link.setCard(card);
                DATA_MANAGER.addSocialLink(link);
            }
        }

        if (card.getEmails() != null && card.getEmails().size() > 0) {
            for (Email email : card.getEmails()) {
                email.setCard(card);
                DATA_MANAGER.addEmail(email);
            }
        }

        if (card.getPhones() != null && card.getPhones().size() > 0) {
            for (Phone phone : card.getPhones()) {
                phone.setCard(card);
                DATA_MANAGER.addPhone(phone);
            }
        }
    }

    public static void createCard(Card card) {
        if (card.getLogo() != null) {
            DATA_MANAGER.addLogo(card.getLogo());
        }
        DATA_MANAGER.addBase64(card.getBase());
        DATA_MANAGER.addCard(card);
        if (card.getTitle() == null || card.getTitle().isEmpty()) {
            card.setTitle("Visit card #" + card.getId());
        }
        DATA_MANAGER.updateCard(card);
        DATA_MANAGER.addEmails(card.getEmails());
        DATA_MANAGER.addPhones(card.getPhones());
        DATA_MANAGER.addSocialLinks(card.getSocialLinks());
    }

    public static void updateCard(Card card) {
        if (card.getLogo() != null && card.getLogo().getId() != null) {
            DATA_MANAGER.addLogo(card.getLogo());
        }
        if (card.getBase() != null) {
            DATA_MANAGER.addBase64(card.getBase());
        }
        DATA_MANAGER.updateCard(card);
    }

    public static void createGroup(Group group, @Nullable List<Card> contacts) {
        if (group.getLogo() != null) {
            DATA_MANAGER.addLogo(group.getLogo());
        }
        DATA_MANAGER.addGroup(group);
        if (contacts != null && contacts.size() > 0) {
            for (Card card : contacts) {
                DATA_MANAGER.addContactToGroup(group, card);
            }
        }
    }

    public static void deleteCard(Card card) {
        if (card.getLogo() != null) {
            DATA_MANAGER.deleteLogo(card.getLogo());
        }
        DATA_MANAGER.deleteBase64(card.getBase());
        DATA_MANAGER.deleteCard(card);
        DATA_MANAGER.deleteEmails(card.getEmails());
        DATA_MANAGER.deletePhones(card.getPhones());
    }

    public static void updateGroupContacts(Group group, List<Card> contacts) {
        List<Card> oldList = DATA_MANAGER.getGroupContacts(group);
        for (Card card : oldList) {
            DATA_MANAGER.deleteContactFromGroup(group, card);
        }
        for (Card card : contacts) {
            DATA_MANAGER.addContactToGroup(group, card);
        }
    }

    public static void deleteGroup(Group group) {
        List<Card> oldList = DATA_MANAGER.getGroupContacts(group);
        for (Card card : oldList) {
            DATA_MANAGER.deleteContactFromGroup(group, card);
        }
        DATA_MANAGER.deleteGroup(group);
    }

    public static void clearDb() {
        for (Card card : DATA_MANAGER.getAllCardsFromDB()) {
            DATA_MANAGER.deleteCard(card);
        }
        for (Group group : DATA_MANAGER.getGroupList()) {
            DATA_MANAGER.deleteGroup(group);
        }
        DATA_MANAGER.deleteEmails(DATA_MANAGER.getAllEmails());
        DATA_MANAGER.deletePhones(DATA_MANAGER.getAllPhones());
        for (Base base : DATA_MANAGER.getAllBases()) {
            DATA_MANAGER.deleteBase64(base);
        }
        for (Logo logo : DATA_MANAGER.getAllLogos()) {
            DATA_MANAGER.deleteLogo(logo);
        }
        for (GroupCard groupCard : DATA_MANAGER.getAllGroupCards()) {
            DATA_MANAGER.deleteGroupCard(groupCard);
        }
    }

    public static Card getCard(Long id) {
        return DATA_MANAGER.getCardById(id);
    }

//    public static Logo getLogo(Long id) {
//        if (id != 0) {
//            return DATA_MANAGER.getLogo(id);
//        } else {
//            return null;
//        }
//    }

    public static Group getGroup(Long id) {
        return DATA_MANAGER.getGroup(id);
    }

    public static void addContactToGroup(Group group, String cardRemoteId) {
        DATA_MANAGER.addContactToGroup(group, DATA_MANAGER.getCardFromDB(cardRemoteId));
    }

    public static void updateGroup(Group group) {
        if (group.getLogo() != null && group.getLogo().getId() != null) {
            DATA_MANAGER.addLogo(group.getLogo());
        }
        DATA_MANAGER.updateGroup(group);
    }

    public static List<Group> getGroupList() {
        return DATA_MANAGER.getGroupList();
    }

    public static List<Card> getContactList() {
        return DATA_MANAGER.getContactList();
    }

    public static List<Card> getGroupContacts(Group group) {
        return DATA_MANAGER.getGroupContacts(group);
    }

    public static Group getGroupByRemoteId(String remoteId) {
        return DATA_MANAGER.getGroupByRemoteId(remoteId);
    }
}
