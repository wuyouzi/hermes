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
 * 查询类型
 *
 * @author liuqianqian
 */
@Entity
@Table(name = "queryType_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QueryTypeInfo extends BaseModel<Integer> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -282265962627794195L;

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
     * 查询类型名称
     */
    @Column(length = 32, nullable = false)
    private String name;

    /**
     * 可查询的收费子报告id
     */
    @Column(length = 128, nullable = false)
    private String chargesSubReportID;

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
     * @return the chargesSubReportID
     */
    public String getChargesSubReportID() {
        return this.chargesSubReportID;
    }

    /**
     * @param chargesSubReportID
     *        the chargesSubReportID to set
     */
    public void setChargesSubReportID(String chargesSubReportID) {
        this.chargesSubReportID = chargesSubReportID;
    }

}
