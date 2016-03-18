/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * 参保单位类型
 * 
 * @author caoming
 */
public enum UnitType {
    BUSINESS("10", "企业"),
    PUBLIC_INSTITUTION("20", "事业单位"),
    FULL_FUNDING_INSTITUTION("21", "全额拨款事业单位"),
    BALANCE_ALLOCATION_INSTITUTION("22", "差额拨款事业单位"),
    RAISE_PUBLIC_INSTITUTION("23", "自收自支事业单位"),
    OFFICE("30", "机关"),
    SOCIAL_ORGANIZATION("40", "社会团体"),
    PRIVATE_NON_ENTERPRISE_UNIT("50", "民办非企业单位"),
    INDIVIDUAL_BUSINESS_LICENCE("60", "城镇个体工商户"),
    SERVICE_CENTER("70", "再就业服务中心"),
    PERSON("99", "个人缴费");
    private final String code;
    private final String string;

    private UnitType(String code, String string) {
        this.code = code;
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }

    public String getCode() {
        return this.code;
    }
}
