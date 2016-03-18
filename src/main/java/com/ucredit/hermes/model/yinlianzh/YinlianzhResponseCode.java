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
 * 应答码 交易状态
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "yinlianzh_response_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class YinlianzhResponseCode extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -6518184170494084835L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * code
     */
    @Column(length = 32, nullable = false)
    private String code;

    /**
     * 对外显示
     */
    @Column(length = 32, nullable = false)
    private String outDisplay;
    /**
     * 终端显示（V1.0）
     */
    @Column(length = 64)
    private String terminalDisplay;
    /**
     * 终端操作
     */
    @Column(length = 64)
    private String terminalOperate;
    /**
     * 含义
     */
    @Column(length = 512)
    private String meaning;
    /**
     * 适用条件
     */
    @Column(length = 512)
    private String applicableCondition;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOutDisplay() {
        return this.outDisplay;
    }

    public void setOutDisplay(String outDisplay) {
        this.outDisplay = outDisplay;
    }

    public String getTerminalDisplay() {
        return this.terminalDisplay;
    }

    public void setTerminalDisplay(String terminalDisplay) {
        this.terminalDisplay = terminalDisplay;
    }

    public String getTerminalOperate() {
        return this.terminalOperate;
    }

    public void setTerminalOperate(String terminalOperate) {
        this.terminalOperate = terminalOperate;
    }

    public String getMeaning() {
        return this.meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getApplicableCondition() {
        return this.applicableCondition;
    }

    public void setApplicableCondition(String applicableCondition) {
        this.applicableCondition = applicableCondition;
    }

}
