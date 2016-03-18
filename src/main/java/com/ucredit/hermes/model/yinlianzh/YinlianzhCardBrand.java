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
 * 卡品牌
 * 11：9字头银联卡
 * 12：62银联标准卡
 * 13：其他银联标准卡（视同）
 * 01：VISA卡（VIS）银联标识卡
 * 02：万事达卡（MCC）银联标识卡
 * 03：万事顺卡（MAE）银联标识卡
 * 04：JCB卡（JCB）银联标识卡
 * 05：大莱卡（Dinner Club）银联标识卡
 * 06：运通卡（AMX）银联标识卡
 * 07：其他银联标识卡
 * 99：其他
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "yinlianzh_card_brand")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class YinlianzhCardBrand extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = -244902378602693287L;

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
