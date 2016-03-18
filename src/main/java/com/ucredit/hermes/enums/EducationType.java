/**
 * Copyright(c) 2011-2011 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * 性别
 *
 * @author liuqianqian
 */
public enum EducationType {
    /**
     * 男
     */
    EDUCATION("学历"),
    /**
     * 女
     */
    EDUCATION_INSCHOOL("学籍");

    private final String string;

    private EducationType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
