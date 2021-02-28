package com.study.oauth2.server.enums;

public enum Gender {

    MALE, FEMALE, UNKNOWN;


    public static Gender getGender(String value) {
        switch (value) {
            case "MALE":
                return Gender.MALE;
            case "FEMALE":
                return Gender.FEMALE;
            case "UNKNOWN":
                return Gender.UNKNOWN;
            default:
                return null;
        }


    }


    public static String getValue(Gender gender) {
        switch (gender) {
            case MALE:
                return "MALE";
            case FEMALE:
                return "FEMALE";
            case UNKNOWN:
                return "UNKNOWN";
            default:
                return null;
        }


    }
}
