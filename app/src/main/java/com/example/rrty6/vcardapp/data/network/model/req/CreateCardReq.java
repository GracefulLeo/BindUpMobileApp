package com.example.rrty6.vcardapp.data.network.model.req;

import com.example.rrty6.vcardapp.data.managers.DataManager;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Email;
import com.example.rrty6.vcardapp.data.storage.model.Phone;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateCardReq {

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

    public CreateCardReq(Card card) {
        type = "vcard";

        setTitle(card.getTitle());
        fieldName = new Field(card.getName());
        fieldSurname = new Field(card.getSurname());
        if (card.getLogo() != null && card.getLogo().getFid() != null) {
            fieldLogotype = new FieldLogotype(card.getLogo().getFid());
            try {
                DataManager.getInstance().addLogo(card.getLogo());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (card.getMidlename() != null) {
            fieldMiddleName = new Field(card.getMidlename());
        }
        if (card.getCompany() != null) {
            fieldCompanyName = new Field(card.getCompany());
        }
        if (card.getPosition() != null) {
            fieldPosition = new Field(card.getPosition());
        }
        if (card.getAddress() != null) {
            fieldAddress = new Field(card.getAddress());
        }
        if (card.getPhones() != null && card.getPhones().size() != 0) {
            List<String> phones = new ArrayList<>();
            for (Phone p : card.getPhones()) {
                phones.add(p.getPhone());
            }
            fieldPhone = new Field(phones);
        }
        if (card.getEmails() != null && card.getEmails().size() != 0) {
            List<String> mails = new ArrayList<>();
            for (Email p : card.getEmails()) {
                mails.add(p.getEmail());
            }
            fieldMail = new Field(mails);
        }
        if (card.getSite() != null) {
            fieldWebSite = new Field(card.getSite());
        }
        if (card.getSocialLinks() != null) {
            fieldSocialLinks = new Field(card.getSocialLinksString());
            System.out.println(new Gson().toJson(fieldSocialLinks));
        }
        if (card.getBase() != null) {
            fieldBase64Vcard = new Field("data:image/png;base64," + card.getBase().getBase64());
        }
    }

    public class Field {

        @SerializedName("und")
        @Expose
        public List<Und> und = new ArrayList<>();


        public Field(String value) {
            Und und = new Und(value);
            this.und.add(und);
        }

        //Setter for multiple fields(mail/phone)
        public Field(List<String> values) {
            for (String value: values) {
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
        public List<LogoUnd> logoUnd = new ArrayList<>();

        public FieldLogotype(String fid) {
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
