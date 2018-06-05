package com.bindup.vcard.vcardapp.data.storage.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@DatabaseTable(tableName = "CARDS")
//Entity for DB
public class Card implements Serializable {

    @DatabaseField(generatedId = true, columnName = "id")
    private Long id;

    @DatabaseField(columnName = "REMOTE_ID")/*(unique = true)*/
    private String remoteId;

    @DatabaseField//true is for MyCards, false for MyContacts for dividing in one db table
    private boolean isMy;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Logo logo = null;
    @DatabaseField
    private String title = null;
    @DatabaseField(canBeNull = false)
    private String name = null;
    @DatabaseField(canBeNull = false)
    private String surname = null;
    @DatabaseField
    private String midlename = null;
    @DatabaseField
    private String company = null;
    @DatabaseField
    private String address = null;
    @DatabaseField
    private String position = null;
    @ForeignCollectionField(eager = true)
    private Collection<Phone> phones = null;
    @ForeignCollectionField(eager = true)
    private Collection<Email> emails = null;
    @ForeignCollectionField(eager = true)
    private Collection<SocialLink> socialLinks = null;
    @DatabaseField
    private String site;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Base base;

    public Card() {
    }

    //Constructor for UI
    public Card(Logo logo, String title, String name, String surname, String midlename, String company, String address, String position,
                Collection<Phone> phones, Collection<Email> emails, String site, Collection<SocialLink> socialLinks, Base base) throws Exception {
        //Checkout field name shouldn't be null
        if (name != null && !name.isEmpty()) {
            //Checkout field surname shouldn't be null
            if (surname != null && !surname.isEmpty()) {
                if (logo != null && logo.getLogo() != null && !logo.getLogo().isEmpty()) {
                    this.logo = logo;
                }
                if (title != null && !title.isEmpty()) {
                    this.title = title;
                }
                this.name = name;
                this.surname = surname;
                if (midlename != null && !midlename.isEmpty()) {
                    this.midlename = midlename;
                }
                if (company != null && !company.isEmpty()) {
                    this.company = company;
                }
                if (address != null && !address.isEmpty()) {
                    this.address = address;
                }
                if (position != null && !position.isEmpty()) {
                    this.position = position;
                }
                if (phones != null && !phones.isEmpty()) {
                    this.phones = phones;
                    for (Phone phone : phones) {
                        if (phone != null) {
                            phone.setCard(this);
                        }
                    }
                }
                if (emails != null && !emails.isEmpty()) {
                    this.emails = emails;
                    for (Email email : emails) {
                        if (email != null) {
                            email.setCard(this);
                        }
                    }
                }
                if (socialLinks != null && !socialLinks.isEmpty()) {
                    this.socialLinks = socialLinks;
                    for (SocialLink socialLink : socialLinks) {
                        if (socialLink != null) {
                            socialLink.setCard(this);
                        }
                    }
                }
                if (site != null && !site.isEmpty()) {
                    this.site = site;
                }
                if (base != null && !base.getBase64().isEmpty()) {
                    this.base = base;
                }
            } else {
                throw new Exception("Invalid method parameter: Card's String Surname can't be null or empty");
            }
        } else {
            throw new Exception("Invalid method parameter: Card's String Name can't be null or empty");
        }
    }


    public void update(Card card) {
//        if (card.getLogo() != null && !card.getLogo().getLogo().isEmpty()) {
        logo = card.getLogo();
//        }
        if (card.getTitle() != null && !card.getTitle().isEmpty()) {
            title = card.getTitle();
        }
        if (card.getName() != null && !card.getName().isEmpty()) {
            name = card.getName();
        }
        if (card.getSurname() != null && !card.getSurname().isEmpty()) {
            surname = card.getSurname();
        }
//        if (card.getMidlename() != null && !card.getMidlename().isEmpty()) {
        midlename = card.getMidlename();
//        }
//        if (card.getCompany() != null && !card.getCompany().isEmpty()) {
        company = card.getCompany();
//        }
//        if (card.getAddress() != null && !card.getAddress().isEmpty()) {
        address = card.getAddress();
//        }
//        if (card.getPosition() != null && !card.getPosition().isEmpty()) {
        position = card.getPosition();
//        }
//        if (card.getPhones() != null && !card.getPhones().isEmpty()) {
        phones = card.getPhones();
        for (Phone phone : phones) {
            phone.setCard(this);
        }
//        }
        emails = card.getEmails();
        for (Email email : emails) {
            email.setCard(this);
        }
        site = card.getSite();
        base = card.getBase();
    }

    public void addPhone(Phone phone) {
        if (phone != null && phone.getPhone() != null && !phone.getPhone().isEmpty()) {
            if (phones == null) {
                phones = new ArrayList<>();
            }
            phones.add(phone);
        }
    }

    public void addEmail(Email email) {
        if (email != null && email.getEmail() != null && !email.getEmail().isEmpty()) {
            if (emails == null) {
                emails = new ArrayList<>();
            }
            emails.add(email);
        }
    }

    public String getSocialLinksString() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Map<String, String> newMap = new HashMap<>();
        for (SocialLink link : socialLinks) {
            newMap.put(link.getType().toString(), link.getValue());
        }
        return gson.toJson(newMap);
    }

    //region===========================Setters==================================


    public void setTitle(String title) {
        this.title = title;
    }

    public void setMy(boolean my) {
        isMy = my;
    }

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

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setMidlename(String midlename) {
        this.midlename = midlename;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setPhones(Collection<Phone> phones) {
        this.phones = phones;
    }

    public void setEmails(Collection<Email> emails) {
        this.emails = emails;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public void setSocialLinks(Collection<SocialLink> socialLinks) {
        this.socialLinks = socialLinks;
    }

    //endregion========================Setters==================================

    //region===========================Getters==================================


    public String getTitle() {
        return title;
    }

    public boolean isMy() {
        return isMy;
    }

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

    public String getSurname() {
        return surname;
    }

    public String getMidlename() {
        return midlename;
    }

    public String getCompany() {
        return company;
    }

    public String getAddress() {
        return address;
    }

    public String getPosition() {
        return position;
    }

    public Collection<Phone> getPhones() {
        return phones;
    }

    public Collection<Email> getEmails() {
        return emails;
    }

    public String getSite() {
        return site;
    }

    public Base getBase() {
        return base;
    }

    public Collection<SocialLink> getSocialLinks() {
        return socialLinks;
    }

    //endregion========================Getters======================================


    @Override
    public String toString() {
        return "Card{" + '\n' +
                "id=" + id + ",\n" +
                "remoteId='" + remoteId + ",\n" +
                "logo=" + logo + ",\n" +
                "name='" + name + ",\n" +
                "surname='" + surname + ",\n" +
                "midlename='" + midlename + ",\n" +
                "company='" + company + ",\n" +
                "address='" + address + ",\n" +
                "position='" + position + ",\n" +
                "phones=" + (phones != null && phones.size() > 0 ? Arrays.toString(phones.toArray()) : "null") + ",\n" +
                "emails=" + (emails != null && emails.size() > 0 ? Arrays.toString(emails.toArray()) : "null") + ",\n" +
                "site='" + site + ",\n" +
                "base=" + (base == null) + "  " + (base != null ? (base.getBase64().length()) : "null") + ",\n" +
                '}';
    }
}
