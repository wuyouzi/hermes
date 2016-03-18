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
public enum Gender {
    /**
     * 男
     */
    MALE("男"),
    /**
     * 女
     */
    FEMALE("女"),
    /**
     * 不详
     */
    OTHER("不详");

    private final String string;

    private Gender(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
