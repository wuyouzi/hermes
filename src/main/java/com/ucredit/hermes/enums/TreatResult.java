/**
 * Copyright(c) 2011-2011 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * 子报告查询状态
 * 
 * @author liuqianqian
 */
public enum TreatResult {
    /**
     * 查得
     */
    RICHARD("查得"),
    /**
     * 未查得
     */
    NORICHARD("未查得"),
    /**
     * 其他原因未查得
     */
    OTHER("其他原因未查得");

    private final String string;

    private TreatResult(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
