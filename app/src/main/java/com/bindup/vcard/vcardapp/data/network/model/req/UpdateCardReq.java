package com.bindup.vcard.vcardapp.data.network.model.req;

import com.bindup.vcard.vcardapp.data.storage.CardCompare;
import com.bindup.vcard.vcardapp.data.storage.model.Card;
import com.bindup.vcard.vcardapp.data.storage.model.Email;
import com.bindup.vcard.vcardapp.data.storage.model.Phone;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UpdateCardReq {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("field_logotype")
    @Expose
    private FieldLogotype fieldLogotype = null;
    @SerializedName("name")
    @Expose
    private String name = null;
    @SerializedName("surname")
    @Expose
    private String surname = null;
    @SerializedName("middle_name")
    @Expose
    private String middleName = null;
    @SerializedName("company")
    @Expose
    private String company = null;
    @SerializedName("position")
    @Expose
    private String position = null;
    @SerializedName("address")
    @Expose
    private String address = null;
    @SerializedName("phone")
    @Expose
    private String phone = null;
    @SerializedName("email")
    @Expose
    private String mail = null;
    @SerializedName("web_site")
    @Expose
    private String webSite = null;
    @SerializedName("social_links")
    @Expose
    private String socialLinks = null;
    @SerializedName("base64_vcard")
    @Expose
    private String base64Vcard = null;

    public UpdateCardReq(Card card) {
        type = "vcard";
        id = card.getRemoteId();
        title = card.getTitle();
        if (card.getLogo() != null) {
            fieldLogotype = new FieldLogotype(card.getLogo().getFid());
        }
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
        base64Vcard = "data:image/png;base64," + card.getBase().getBase64();
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
