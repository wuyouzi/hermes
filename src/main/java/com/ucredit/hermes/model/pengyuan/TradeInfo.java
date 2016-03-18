/**
 * Copyright(c) 2011-2013 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.pengyuan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * 行业类型表
 *
 * @author liuqianqian
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "trade_infos")
public class TradeInfo extends BaseModel<Integer> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7183328059593201155L;

    /**
     * 表 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 行业代码编码
     */
    @Column(name = "code", length = 64)
    @Index(name = "idx_trade_infos_code")
    private String code;

    /**
     * 门类
     */
    @Column(name = "tradeCode", length = 64)
    private String tradeCode;

    /**
     * 门类名称
     */
    @Column(name = "tradeName", length = 64)
    private String tradeName;

    /**
     * 大类编号
     */
    @Column(name = "tradeBigCode", length = 256)
    private String tradeBigCode;

    /**
     * 大类名称
     */
    @Column(name = "tradeBigName", length = 256)
    private String tradeBigName;

    /**
     * 中类编号
     */
    @Column(name = "tradeMiddleCode", length = 256)
    private String tradeMiddleCode;

    /**
     * 中类名称
     */
    @Column(name = "tradeMiddleName", length = 256)
    private String tradeMiddleName;

    /**
     * 小类编号
     */
    @Column(name = "tradeLittleCode", length = 256)
    private String tradeLittleCode;

    /**
     * 小类名称
     */
    @Column(name = "tradeLittleName", length = 256)
    private String tradeLittleName;

    /**
     * @return the id
     */
    @Override
    public Integer getId() {
        return this.id;
    }

    /**
     * @param id
     *        the id to set
     */
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * @param code
     *        the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the tradeCode
     */
    public String getTradeCode() {
        return this.tradeCode;
    }

    /**
     * @param tradeCode
     *        the tradeCode to set
     */
    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    /**
     * @return the tradeName
     */
    public String getTradeName() {
        return this.tradeName;
    }

    /**
     * @param tradeName
     *        the tradeName to set
     */
    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    /**
     * @return the tradeBigCode
     */
    public String getTradeBigCode() {
        return this.tradeBigCode;
    }

    /**
     * @param tradeBigCode
     *        the tradeBigCode to set
     */
    public void setTradeBigCode(String tradeBigCode) {
        this.tradeBigCode = tradeBigCode;
    }

    /**
     * @return the tradeBigName
     */
    public String getTradeBigName() {
        return this.tradeBigName;
    }

    /**
     * @param tradeBigName
     *        the tradeBigName to set
     */
    public void setTradeBigName(String tradeBigName) {
        this.tradeBigName = tradeBigName;
    }

    /**
     * @return the tradeMiddleCode
     */
    public String getTradeMiddleCode() {
        return this.tradeMiddleCode;
    }

    /**
     * @param tradeMiddleCode
     *        the tradeMiddleCode to set
     */
    public void setTradeMiddleCode(String tradeMiddleCode) {
        this.tradeMiddleCode = tradeMiddleCode;
    }

    /**
     * @return the tradeMiddleName
     */
    public String getTradeMiddleName() {
        return this.tradeMiddleName;
    }

    /**
     * @param tradeMiddleName
     *        the tradeMiddleName to set
     */
    public void setTradeMiddleName(String tradeMiddleName) {
        this.tradeMiddleName = tradeMiddleName;
    }

    /**
     * @return the tradeLittleCode
     */
    public String getTradeLittleCode() {
        return this.tradeLittleCode;
    }

    /**
     * @param tradeLittleCode
     *        the tradeLittleCode to set
     */
    public void setTradeLittleCode(String tradeLittleCode) {
        this.tradeLittleCode = tradeLittleCode;
    }

    /**
     * @return the tradeLittleName
     */
    public String getTradeLittleName() {
        return this.tradeLittleName;
    }

    /**
     * @param tradeLittleName
     *        the tradeLittleName to set
     */
    public void setTradeLittleName(String tradeLittleName) {
        this.tradeLittleName = tradeLittleName;
    }

    @Override
    public String toString() {
        return "TradeInfo [code=" + this.code + ", tradeCode=" + this.tradeCode
            + ", tradeName=" + this.tradeName + ", tradeBigCode="
            + this.tradeBigCode + ", tradeBigName=" + this.tradeBigName
            + ", tradeMiddleCode=" + this.tradeMiddleCode
            + ", tradeMiddleName=" + this.tradeMiddleName
            + ", tradeLittleCode=" + this.tradeLittleCode
            + ", tradeLittleName=" + this.tradeLittleName + "]";
    }

}
