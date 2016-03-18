/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.pengyuan;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

/**
 * @author caoming
 */
@Entity
@Table(name = "pengYuan_counts")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PengYuanCount extends BaseModel<Integer> {
    private static final long serialVersionUID = -7110726191036435749L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 收费子报告代码
     */
    @Column(length = 64)
    @Length(max = 64)
    private String subReportCode;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();
    private int mainId;
    /**
     * 收费报告计费次数
     */
    private int count;

    public PengYuanCount(String subReportCode, int count, int mainId) {
        super();
        this.subReportCode = subReportCode;
        this.count = count;
        this.mainId = mainId;
    }

    public PengYuanCount() {
        super();
    }

    public String getSubReportCode() {
        return this.subReportCode;
    }

    public void setSubReportCode(String subReportCode) {
        this.subReportCode = subReportCode;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return this.createTime;
    }

    /**
     * @param createTime
     *        the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the mainId
     */
    public int getMainId() {
        return this.mainId;
    }

    /**
     * @param mainId
     *        the mainId to set
     */
    public void setMainId(int mainId) {
        this.mainId = mainId;
    }
}
