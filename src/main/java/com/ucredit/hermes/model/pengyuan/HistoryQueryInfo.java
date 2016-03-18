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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * 近两年历史查询明细
 *
 * @author caoming
 */
@Entity
@Table(name = "history_query_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HistoryQueryInfo extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -2471107189674653892L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 64)
    @Index(name = "idx_history_query_infos_documentNo")
    private String documentNo;

    /**
     * 查询单位ID，如果不是本单位的查询记录，则为-1
     */
    @Index(name = "idx_history_query_infos_unit")
    private int unit;

    /**
     * 信息来源单位所属类型
     */
    @ManyToOne
    private InformationSourceUnitType information;

    /**
     * 查询原因
     */
    @ManyToOne
    private QueryReason queryReason;

    /**
     * 查询日期，格式YYYYMMDD
     */
    private Date queryDate;

    private BaseHeadReport baseHeadReport;

    /**
     * 广东社保信息
     */
    @ManyToOne(optional = true)
    private GdSiPersonInfo personInfo;

    public String getDocumentNo() {
        return this.documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public int getUnit() {
        return this.unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public InformationSourceUnitType getInformation() {
        return this.information;
    }

    public void setInformation(InformationSourceUnitType information) {
        this.information = information;
    }

    public QueryReason getQueryReason() {
        return this.queryReason;
    }

    public void setQueryReason(QueryReason queryReason) {
        this.queryReason = queryReason;
    }

    public Date getQueryDate() {
        return this.queryDate;
    }

    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }

    public BaseHeadReport getBaseHeadReport() {
        return this.baseHeadReport;
    }

    public void setBaseHeadReport(BaseHeadReport baseHeadReport) {
        this.baseHeadReport = baseHeadReport;
    }

    public GdSiPersonInfo getPersonInfo() {
        return this.personInfo;
    }

    public void setPersonInfo(GdSiPersonInfo personInfo) {
        this.personInfo = personInfo;
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
        return "HistoryQueryInfo [unit=" + this.unit + ", information="
            + (this.information == null ? "" : this.information.toString())
            + ", queryReason="
            + (this.queryReason == null ? "" : this.queryReason.toString())
            + ", queryDate=" + this.queryDate + "]";
    }

}
