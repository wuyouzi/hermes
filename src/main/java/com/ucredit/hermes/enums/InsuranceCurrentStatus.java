/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * 保险缴费状态
 * 
 * @author caoming
 */
public enum InsuranceCurrentStatus {
    UNINSURED("0", "未参保"),
    INSURED_PAY("1", "参保缴费"),
    STOP_PAY("2", "暂停缴费"),
    TEMINATION_PAY("3", "终止缴费");

    private final String code;
    private final String string;

    private InsuranceCurrentStatus(String code, String string) {
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
