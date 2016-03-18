/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.yinlianzh;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 币种
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "yinlianzh_currency_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class YinlianzhCurrencyType extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 228951456033097670L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 国家/地区代码
     */
    @Column(length = 32, nullable = false)
    private String countryCode;

    /**
     * 国家/地区名称
     */
    @Column(length = 32, nullable = false)
    private String countryName;

    /**
     * 交易货币代码
     */
    private String currencyCode;

    /**
     * 交易货币名称
     */
    private String currencyName;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return this.currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

}
