package com.ucredit.hermes.enums;

public enum DataChannelSubType {
    /**
     * 国政通身份证
     */
    GUOZHENGTONG_IDENTIFY("GUOZHENGTONG_IDENTIFY"),
    /**
     * 鹏元企业信息
     */
    PENGYUAN_COMPANY("PENGYUAN_COMPANY"),
    /**
     * 鹏元企业信息模糊
     */
    PENGYUAN_COMPANY_FUZZY("PENGYUAN_COMPANY_FUZZY"),
    /**
     * 鹏元学历信息
     */
    PENGYUAN_EDU("PENGYUAN_EDU"),
    /**
     * 鹏元学籍信息
     */
    PENGYUAN_EDU_REG("PENGYUAN_EDU_REG"),
    /**
     * 鹏元广东社保信息
     */
    PENGYUAN_GDSOCIAL("PENGYUAN_GDSOCIAL"),
    /**
     * 鹏元户籍信息
     */
    PENGYUAN_POLICE_RES("PENGYUAN_POLICE_RES"),
    /**
     * 鹏元
     */
    PENGYUAN("鹏元"),
    /**
     * HERMES系统
     */
    HERMES("Hermes"),
    /**
     * 安盛黑名单
     */
    ANSHENG_BLACKLIST("安盛黑名单"),
    /**
     * 友信审核系统
     */
    UCREDIT("友信"),
    /**
     * 百融审核系统
     */
    BAIRONG("百融"),
    BAIRONG_SUBTYPE("BAIRONG"),

    JUXINLI("聚信立"),

    YINLIANZH("银联智惠商户流水"),

    QIANHAIBLACK("QIANHAIBLACK");

    private final String string;

    private DataChannelSubType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
