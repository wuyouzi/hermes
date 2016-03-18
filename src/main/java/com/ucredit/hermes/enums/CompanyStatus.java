/**
 * Copyright(c) 2011-2011 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * 企业当前状态
 *
 * @author liuqianqian
 */
public enum CompanyStatus {
    /**
     * 在营(开业)
     */
    OPENED("在营(开业)"),
    /**
     * 吊销
     */
    REVOKE("吊销"),
    /**
     * 注销
     */
    CANCELLATION("注销"),
    /**
     * 迁出
     */
    MOVE("迁出"),
    /**
     * 停业
     */
    CLOSE("停业"),
    /**
     * 其他
     */
    OTHER("其他");

    private final String string;

    private CompanyStatus(String string) {
        this.string = string;
    }

    public static CompanyStatus getCompanyStatus(String str) {
        if ("OPENED".equals(str)) {
            return CompanyStatus.OPENED;
        } else if ("REVOKE".equals(str)) {
            return CompanyStatus.REVOKE;
        } else if ("CANCELLATION".equals(str)) {
            return CompanyStatus.CANCELLATION;
        } else if ("MOVE".equals(str)) {
            return CompanyStatus.MOVE;
        } else if ("CLOSE".equals(str)) {
            return CompanyStatus.CLOSE;
        } else {
            return CompanyStatus.OTHER;
        }
    }

    @Override
    public String toString() {
        return this.string;
    }
}
