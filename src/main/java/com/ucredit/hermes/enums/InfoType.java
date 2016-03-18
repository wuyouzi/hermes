/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * 声明类型
 * 
 * @author caoming
 */
public enum InfoType {
    NON_ONESELF_STATEMENT("1", "非本人声明"),
    ONESELF_STATEMENT("2", "本人声明");

    private final String code;
    private final String string;

    private InfoType(String code, String string) {
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
