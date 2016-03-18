package com.ucredit.hermes.enums;

public enum BaiRongLocationTypes {
    HOME_ADDR("家庭住址"),
    BIZ_ADDR("公司地址"),
    PER_ADDR("户籍地址"),
    APPLY_ADDR("申请地址"),
    OTHER_ADDR("其他地址");
    private final String string;

    private BaiRongLocationTypes(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }

    public String getString() {
        return this.string;
    }
}
