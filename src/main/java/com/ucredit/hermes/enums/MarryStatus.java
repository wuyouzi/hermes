/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * 婚姻状况
 * 
 * @author caoming
 */
public enum MarryStatus {
    SPINSTERHOOD("1", "未婚"),
    MARRIED("2", "已婚"),
    UNKNOWN("3", "不详"),
    DIVORCE("4", "离婚"),
    WIDOWED("5", "丧偶"),
    OTHER("9", "其它");

    private final String string;
    private final String code;

    private MarryStatus(String code, String string) {
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
