/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * 参保种类
 * 
 * @author caoming
 */
public enum InsuranceType {
    OLD_AGE_PENSION("1", "养老"),
    MEDICAL_INSURANCE("2", "医疗"),
    HOUSING_FUND("3", "住房公积金"),
    ACCIDENT_INSURANCE("4", "工伤"),
    UNEMPLOYMENT_INSURANCE("5", "失业保险"),
    BIRTH_INSURANCE("6", "生育险"),
    OTHER("-1", "不详");

    private final String code;
    private final String string;

    private InsuranceType(String code, String string) {
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
