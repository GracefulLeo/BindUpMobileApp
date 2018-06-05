package com.bindup.vcard.vcardapp.data.network.model.req;

import android.util.Log;

import com.bindup.vcard.vcardapp.data.managers.DataManager;
import com.bindup.vcard.vcardapp.data.storage.model.Card;
import com.bindup.vcard.vcardapp.data.storage.model.Email;
import com.bindup.vcard.vcardapp.data.storage.model.Phone;
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
    @SerializedName("name")
    @Expose
    public String name = null;
    @SerializedName("surname")
    @Expose
    public String surname = null;
    @SerializedName("middle_name")
    @Expose
    public String middleName = null;
    @SerializedName("company")
    @Expose
    public String company = null;
    @SerializedName("position")
    @Expose
    public String position = null;
    @SerializedName("address")
    @Expose
    public String address = null;
    @SerializedName("phone")
    @Expose
    public String phone = null;
    @SerializedName("email")
    @Expose
    public String mail = null;
    @SerializedName("web_site")
    @Expose
    public String webSite = null;
    @SerializedName("social_links")
    @Expose
    public String socialLinks = null;
    @SerializedName("base64_vcard")
    @Expose
    public String base64Vcard = null;

    public CreateCardReq(Card card) {
        type = "vcard";
        setTitle(card.getTitle());
        name = card.getName();
        surname = card.getSurname();
        if (card.getLogo() != null && card.getLogo().getFid() != null) {
            fieldLogotype = new FieldLogotype(card.getLogo().getFid());
        }
        if (card.getMidlename() != null) {
            middleName = card.getMidlename();
        }
        if (card.getCompany() != null) {
            company = card.getCompany();
        }
        if (card.getPosition() != null) {
            position = card.getPosition();
        }
        if (card.getAddress() != null) {
            address = card.getAddress();
        }
        if (card.getPhones() != null && card.getPhones().size() != 0) {
            StringBuilder phones = new StringBuilder();
            for (Phone p : card.getPhones()) {
                if (phones.length() == 0) {
                    phones.append(p.getPhone());
                } else {
                    phones.append(",");
                    phones.append(p.getPhone());
                }
            }
            phone = phones.toString();
        }
        if (card.getEmails() != null && card.getEmails().size() != 0) {
            StringBuilder mails = new StringBuilder();
            for (Email p : card.getEmails()) {
                if (mails.length() == 0) {
                    mails.append(p.getEmail());
                } else {
                    mails.append(",");
                    mails.append(p.getEmail());
                }
            }
            mail = mails.toString();
        }
        if (card.getSite() != null) {
            webSite = card.getSite();
        }
        if (card.getSocialLinks() != null) {
            socialLinks = card.getSocialLinksString();
        }
        base64Vcard = "data:image/png;base64," + card.getBase().getBase64();
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
