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
 * @author caoming
 */
@Entity
@Table(name = "area_codes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AreaCode extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 205520999002357429L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 6)
    @Length(max = 6)
    private String code;

    @Column(length = 512)
    @Length(max = 512)
    private String caption;

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

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String toString() {
        return "AreaCode [code=" + this.code + ", caption=" + this.caption
            + "]";
    }

}
