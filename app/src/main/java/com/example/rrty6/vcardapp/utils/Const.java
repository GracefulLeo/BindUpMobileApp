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
}
