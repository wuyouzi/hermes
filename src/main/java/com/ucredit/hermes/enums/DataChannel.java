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
public enum DataChannel {
    /**
     * 国政通
     */
    GUOZHENGTONG("国政通"),
    /**
     * 鹏元
     */
    PENGYUAN("鹏元"),
    /**
     * HERMES系统
     */
    HERMES("Hermes"),
    /**
     * 安盛
     */
    ANSHENG("安盛"),
    /**
     * 友信审核系统
     */
    UCREDIT("友信"),
    /**
     * 百融审核系统
     */
    BAIRONG("百融"),
    /**
     * 聚信立
     */
    JUXINLI("聚信立"),
    /**
     * 银联智惠
     */
    YINLIANZH("银联智惠"),
    /**
     * 爬取平台
     */
    CRAWL("爬取平台"),

    /**
     * 前海征信
     */
    QIANHAI("前海征信");

    private final String string;

    private DataChannel(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
