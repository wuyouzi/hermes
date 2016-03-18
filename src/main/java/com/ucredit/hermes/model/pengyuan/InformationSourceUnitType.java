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
import org.hibernate.validator.constraints.Length;

/**
 * 信息来源单位所属类型表
 *
 * @author caoming
 */
@Entity
@Table(name = "information_source_unit_types")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InformationSourceUnitType extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 8218057466179983194L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 代码
     */
    @Column(length = 16)
    @Length(max = 16)
    private String code;

    /**
     * 描述
     */
    @Column(length = 64)
    @Length(max = 64)
    private String description;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*
     * (non-Javadoc)
     * @see com.ucredit.hermes.model.IDOwner#getId()
     */
    @Override
    public Integer getId() {
        return this.id;
    }

    /*
     * (non-Javadoc)
     * @see com.ucredit.hermes.model.IDOwner#setId(java.lang.Object)
     */
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "InformationSourceUnitType [code=" + this.code
            + ", description=" + this.description + "]";
    }

}
