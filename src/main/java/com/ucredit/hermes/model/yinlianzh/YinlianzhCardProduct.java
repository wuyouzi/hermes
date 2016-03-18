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
 * 卡产品
 * 00：通用卡
 * 01：公务卡
 * 02：公共缴费类卡
 * 03：航空卡
 * 04：学生卡
 * 05：社保卡
 * 06：交通卡
 * 07：积分卡
 * 08：军人卡
 * 09：市民卡
 * 10：商务卡
 * 11：旅游卡
 * 97：涉农卡
 * 98： 银联高端卡
 * 99 ：双标识高端卡 "
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "yinlianzh_card_product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class YinlianzhCardProduct extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 8544760418168456750L;

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
