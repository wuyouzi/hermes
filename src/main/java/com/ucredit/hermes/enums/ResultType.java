/**
 * Copyright(c) 2011-2011 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * 返回结果的状态
 * 
 * @author liuqianqian
 */
public enum ResultType {
    /**
     * 查询中
     */
    QUERY("查询中"),
    /**
     * 成功
     */
    SUCCESS("成功"),
    /**
     * 失败
     */
    FAILURE("失败");

    private final String string;

    private ResultType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
