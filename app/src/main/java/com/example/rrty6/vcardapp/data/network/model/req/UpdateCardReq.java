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

    public UpdateCardReq(Card card) {
        type = "vcard";
        title = card.getTitle();
        fieldLogotype = new FieldLogotype(card.getLogo().getFid());
        name = card.getName();
        surname = card.getSurname();
        middleName = card.getMidlename();
        company = card.getCompany();
        position = card.getPosition();
        address = card.getAddress();
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
        webSite = card.getSite();
        socialLinks = card.getSocialLinksString();
        base64Vcard = card.getBase().getBase64();
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
