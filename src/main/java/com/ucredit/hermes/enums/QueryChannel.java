/**
 * Copyright(c) 2011-2011 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * 数据渠道
 * 
 * @author liuqianqian
 */
public enum QueryChannel {
    /**
     * 人人贷
     */
    RENRENDAI("人人贷"),
    /**
     * 友信
     */
    UCREATE("友信");

    private final String string;

    private QueryChannel(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
