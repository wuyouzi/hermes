package com.ucredit.hermes.enums;

public enum TongdunType {
    IDENTITYNO("identityno"),
    MOBILE("mobile"),
    CONTACT1_MBILE("contact1_mbile"),
    CONTACT2_MBILE("contact2_mbile"),
    CONTACT3_MBILE("contact3_mbile"),
    CONTACT4_MBILE("contact4_mbile"),
    OTHER("other");
    private final String string;

    private TongdunType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }

}
