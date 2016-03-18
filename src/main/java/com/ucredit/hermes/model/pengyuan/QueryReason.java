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
 * 查询原因
 *
 * @author caoming
 */
@Entity
@Table(name = "query_reasons")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QueryReason extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -2754422496589069030L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 8)
    @Length(max = 8)
    private String code;

    @Column(length = 512)
    @Length(max = 512)
    private String reason;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
        return "QueryReason [code=" + this.code + ", reason=" + this.reason
            + "]";
    }

}
