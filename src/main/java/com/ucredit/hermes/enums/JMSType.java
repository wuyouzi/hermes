/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * JMS类型
 *
 * @author caoming
 */
public enum JMSType {
    THIRD_JMS_QUEUE("鹏元企业信息"),
    GUO_ZHENG_TONG_JMS_QUEUE("国政通身份信息"),
    POLICE_JMS_QUEUE("鹏元专线个人户籍信息"),
    EDUCATION_JMS_QUEUE("鹏元学历信息"),
    LAST_EDUCATION_JMS_QUEUE("鹏元最高学历信息"),
    IN_SCHOOL_EDUCATION_JMS_QUEUE("鹏元学籍信息"),
    GD_SI_JMS_QUEUE("鹏元广东社保信息"),
    JU_XIN_LI("聚信立用户信息"),
    JU_XIN_LI2("聚信立报告2信息"),
    JU_XIN_LI_VERIFY("聚信立验证信息"),
    YINLIANZH_JMS_QUEUE("银联智惠商户流水信息下载"),
    YINLIANZH_JMS_SEARCH_QUEUE("银联智惠商户流水信息查询"),
    QIANHAI_JMS_BLACK("前海征信黑名单");

    private final String string;

    private JMSType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }

}
