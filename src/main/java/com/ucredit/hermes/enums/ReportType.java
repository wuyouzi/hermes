/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * 子报告类型
 * 
 * @author caoming
 */
public enum ReportType {
    NORMAL_REPORT("普通查询子报告"),
    SPECIAL_REPORT("专网查询子报告");

    private final String string;

    private ReportType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }

}
