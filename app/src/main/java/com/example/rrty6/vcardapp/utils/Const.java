package com.example.rrty6.vcardapp.utils;

//String constants used in java classes, not in resources
public interface Const {
    String USER_ID = "USER_ID";
    String ACCESS_TOKEN = "ACCESS_TOKEN";
    String COOKIE = "COOKIE";

    enum ListRole {
        card,
        contact,
        group
    }

    enum LinkType {
        VIBER("Viber"),
        WHATSAPP("WhatsApp"),
        SKYPE("Skype"),
        FACEBOOK_MESSENGER("Facebook messenger"),
        TELEGRAM("Telegram"),
        FACEBOOK("Facebook"),
        TWITTER("Twitter"),
        GOOGLE_PLUS("Google+"),
        LINKEDIN("LinkedIn"),
        YOUTUBE("YouTube"),
        INSTAGRAM("Instagram"),
        VK("VKontakte");

        private String text;

        LinkType(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }

        public static LinkType getType(String s) {
            for (LinkType type : LinkType.values()) {
                if (type.text.equalsIgnoreCase(s)) {
                    return type;
                }
            }
            return null;
        }
    }

    interface JobConsts{
        String ACTION = "ACTION";

        String ACTION_PERFORM_CREATE_CARD = "bind-up.ACTION_PERFORM_CREATE_CARD";
        String ACTION_PERFORM_UPDATE_CARD = "bind-up.ACTION_PERFORM_UPDATE_CARD";
        String ACTION_PERFORM_DELETE_CARD = "bind-up.ACTION_PERFORM_DELETE_CARD";
        String ACTION_PERFORM_ADD_CONTACT = "bind-up.ACTION_PERFORM_ADD_CONTACT";
        String ACTION_PERFORM_DELETE_CONTACT = "bind-up.ACTION_PERFORM_DELETE_CONTACT";
        String ACTION_PERFORM_CREATE_GROUP = "bind-up.ACTION_PERFORM_CREATE_GROUP";
        String ACTION_PERFORM_UPDATE_GROUP = "bind-up.ACTION_PERFORM_UPDATE_GROUP";
        String ACTION_PERFORM_UPDATE_GROUP_CONTACTS = "bind-up.ACTION_PERFORM_UPDATE_GROUP_CONTACTS";
        String ACTION_PERFORM_DELETE_GROUP = "bind-up.ACTION_PERFORM_DELETE_GROUP";

        String ACTION_CREATE_CARD = "bind-up.ACTION_CREATE_CARD";
        String ACTION_UPDATE_CARD = "bind-up.ACTION_UPDATE_CARD";
        String ACTION_DELETE_CARD = "bind-up.ACTION_DELETE_CARD";
        String ACTION_ADD_CONTACT = "bind-up.ACTION_ADD_CONTACT";
        String ACTION_DELETE_CONTACT = "bind-up.ACTION_DELETE_CONTACT";
        String ACTION_CREATE_GROUP = "bind-up.ACTION_CREATE_GROUP";
        String ACTION_UPDATE_GROUP = "bind-up.ACTION_UPDATE_GROUP";
        String ACTION_UPDATE_GROUP_CONTACTS = "bind-up.ACTION_UPDATE_GROUP_CONTACTS";
        String ACTION_DELETE_GROUP = "bind-up.ACTION_DELETE_GROUP";

        String ACTION_EXECUTE_CREATE_CARD = "bind-up.ACTION_EXECUTE_CREATE_CARD";
        String ACTION_EXECUTE_UPDATE_CARD = "bind-up.ACTION_EXECUTE_UPDATE_CARD";
        String ACTION_EXECUTE_DELETE_CARD = "bind-up.ACTION_EXECUTE_DELETE_CARD";
        String ACTION_EXECUTE_ADD_CONTACT = "bind-up.ACTION_EXECUTE_ADD_CONTACT";
        String ACTION_EXECUTE_DELETE_CONTACT = "bind-up.ACTION_EXECUTE_DELETE_CONTACT";
        String ACTION_EXECUTE_CREATE_GROUP = "bind-up.ACTION_EXECUTE_CREATE_GROUP";
        String ACTION_EXECUTE_UPDATE_GROUP = "bind-up.ACTION_EXECUTE_UPDATE_GROUP";
        String ACTION_EXECUTE_UPDATE_GROUP_CONTACTS = "bind-up.ACTION_EXECUTE_UPDATE_GROUP_CONTACTS";
        String ACTION_EXECUTE_DELETE_GROUP = "bind-up.ACTION_EXECUTE_DELETE_GROUP";

        String CARD_ID = "CARD_ID";
        String CARD_REMOTE_ID = "CARD_REMOTE_ID";
        String CONTACT_REMOTE_ID = "CONTACT_REMOTE_ID";
        String GROUP_ID = "GROUP_ID";
        String GROUP_REMOTE_ID = "GROUP_REMOTE_ID";
        String GROUP_CONTACTS_IDS = "GROUP_CONTACTS_IDS";
    }
}
