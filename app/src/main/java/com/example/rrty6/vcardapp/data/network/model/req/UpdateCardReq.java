package com.example.rrty6.vcardapp.data.network.model.req;

import com.example.rrty6.vcardapp.data.storage.CardCompare;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Email;
import com.example.rrty6.vcardapp.data.storage.model.Phone;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UpdateCardReq {
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("field_logotype")
    @Expose
    public FieldLogotype fieldLogotype = null;
    @SerializedName("field_name")
    @Expose
    public Field fieldName = null;
    @SerializedName("field_surname")
    @Expose
    public Field fieldSurname = null;
    @SerializedName("field_middle_name")
    @Expose
    public Field fieldMiddleName = null;
    @SerializedName("field_company_name")
    @Expose
    public Field fieldCompanyName = null;
    @SerializedName("field_position")
    @Expose
    public Field fieldPosition = null;
    @SerializedName("field_address")
    @Expose
    public Field fieldAddress = null;
    @SerializedName("field_phone")
    @Expose
    public Field fieldPhone = null;
    @SerializedName("field_mail")
    @Expose
    public Field fieldMail = null;
    @SerializedName("field_web_site")
    @Expose
    public Field fieldWebSite = null;
    @SerializedName("field_social_links")
    @Expose
    public Field fieldSocialLinks = null;
    @SerializedName("field_base64_vcard")
    @Expose
    public Field fieldBase64Vcard = null;

    public UpdateCardReq(Card card, CardCompare compare) {
        type = "vcard";
        if (compare.title && card.getTitle()!=null && !card.getTitle().isEmpty()) {
            title = card.getTitle();
        }
        if (compare.fieldLogotype) {
            fieldLogotype = new FieldLogotype(card.getLogo().getFid());
        }
        if (compare.fieldName && card.getName()!=null && !card.getName().isEmpty()) {
            fieldName = new Field(card.getName());
        }
        if (compare.fieldSurname && card.getMidlename()!=null && !card.getMidlename().isEmpty()) {
            fieldSurname = new Field(card.getSurname());
        }
        if (compare.fieldMiddleName) {
            fieldMiddleName = new Field(card.getMidlename());
        }
        if (compare.fieldCompanyName) {
            fieldCompanyName = new Field(card.getCompany());
        }
        if (compare.fieldPosition) {
            fieldPosition = new Field(card.getPosition());
        }
        if (compare.fieldAddress) {
            fieldAddress = new Field(card.getAddress());
        }
        if (compare.fieldPhone) {
            List<String> phones = new ArrayList<>();
            for (Phone p : card.getPhones()) {
                phones.add(p.getPhone());
            }
            fieldPhone = new Field(phones);
        }
        if (compare.fieldMail) {
            List<String> emails = new ArrayList<>();
            for (Email p : card.getEmails()) {
                emails.add(p.getEmail());
            }
            fieldMail = new Field(emails);
        }
        if (compare.fieldWebSite) {
            fieldWebSite = new Field(card.getSite());
        }
        if (compare.fieldSocialLinks) {
            fieldSocialLinks = new Field(card.getSocialLinksString());
        }
        if (compare.fieldBase64Vcard) {
            fieldBase64Vcard = new Field(card.getBase().getBase64());
        }
    }

    public class Field {

        @SerializedName("und")
        @Expose
        public List<Und> und = null;


        Field(String value) {
            this.und = new ArrayList<>();
            Und und = new Und(value);
            this.und.add(und);
        }

        //Setter for multiple fields(mail/phone)
        Field(List<String> values) {
            this.und = new ArrayList<>();
            for (String value : values) {
                Und und = new Und(value);
                this.und.add(und);
            }
        }
    }

    private class Und {

        @SerializedName("value")
        @Expose
        public String value;

        public Und(String value) {
            this.value = value;
        }
    }

    public class FieldLogotype {

        @SerializedName("und")
        @Expose
        public List<LogoUnd> logoUnd = null;

        FieldLogotype(String fid) {
            this.logoUnd = new ArrayList<>();
            LogoUnd logoUnd = new LogoUnd(fid);
            this.logoUnd.add(logoUnd);
        }
    }

    private class LogoUnd {

        @SerializedName("fid")
        @Expose
        public String fid;

        public LogoUnd(String fid) {
            this.fid = fid;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
