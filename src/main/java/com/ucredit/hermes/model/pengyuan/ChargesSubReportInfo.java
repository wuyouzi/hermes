/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
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

/**
 * 收费子报告
 *
 * @author liuqianqian
 */
@Entity
@Table(name = "charges_subReport_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChargesSubReportInfo extends BaseModel<Integer> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2007161346255597211L;

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
     * 收费报告名称
     */
    @Column(length = 256, nullable = false)
    private String name;

    /**
     * 收费子报告名称
     */
    @Column(length = 256, nullable = false)
    private String childID;

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
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     *        the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the childID
     */
    public String getChildID() {
        return this.childID;
    }

    /**
     * @param childID
     *        the childID to set
     */
    public void setChildID(String childID) {
        this.childID = childID;
    }

}
