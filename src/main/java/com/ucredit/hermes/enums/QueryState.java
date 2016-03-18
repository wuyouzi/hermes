/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * @author caoming
 */
public enum QueryState {
    NEW("创建"),
    END("结束"),
    QUERY("查询中");

    private final String name;

    private QueryState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
