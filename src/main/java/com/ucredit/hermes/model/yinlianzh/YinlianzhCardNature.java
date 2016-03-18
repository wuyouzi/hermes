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
 * 卡性质
 * 00：未知
 * 01：借记卡
 * 02：贷记卡
 * 03：准贷记账户
 * 04：借贷合一卡
 * 05：预付费卡
 * 06：半开放预付费卡
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "yinlianzh_card_nature")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class YinlianzhCardNature extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -8639072127896410002L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 编号
     */
    @Column(length = 32, nullable = false)
    private String code;

    /**
     * 名称
     */
    @Column(length = 32, nullable = false)
    private String name;

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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
