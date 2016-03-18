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
 * 卡种
 * 01：人民币卡
 * 02：人民币境外卡
 * 03：多币种卡
 * 04：国际卡
 * 05：境外卡
 * 06：外资卡
 * 07：外卡
 * 99：其他
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "yinlianzh_card_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class YinlianzhCardType extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 3775013283589377603L;

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
