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

    public UpdateCardReq(Card card, CardCompare compare) {
        type = "vcard";
        if (compare.title && card.getTitle()!=null && !card.getTitle().isEmpty()) {
            title = card.getTitle();
        }
        if (compare.fieldLogotype) {

            fieldLogotype = new FieldLogotype(card.getLogo().getFid());
        }
        if (compare.fieldName && card.getName()!=null && !card.getName().isEmpty()) {
            name = card.getName();
        }
        if (compare.fieldSurname && card.getMidlename()!=null && !card.getMidlename().isEmpty()) {
            surname = card.getSurname();
        }
        if (compare.fieldMiddleName) {
            middleName = card.getMidlename();
        }
        if (compare.fieldCompanyName) {
            company = card.getCompany();
        }
        if (compare.fieldPosition) {
            position = card.getPosition();
        }
        if (compare.fieldAddress) {
            address = card.getAddress();
        }
        if (compare.fieldPhone) {
            StringBuilder phones = new StringBuilder();
            for (Phone p: card.getPhones()) {
                if (phones.length() == 0) {
                    phones.append(p.getPhone());
                } else {
                    phones.append(",");
                    phones.append(p.getPhone());
                }
            }
            phone = phones.toString();
        }
        if (compare.fieldMail) {
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
        if (compare.fieldWebSite) {
            webSite = card.getSite();
        }
        if (compare.fieldSocialLinks) {
            socialLinks = card.getSocialLinksString();
        }
        if (compare.fieldBase64Vcard) {
            base64Vcard = card.getBase().getBase64();
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
