/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.pengyuan;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import com.ucredit.hermes.enums.UnitType;

/**
 * 广东个人近36个月养老参保记录
 *
 * @author caoming
 */
@Entity
@Table(name = "history_si_person_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HistorySiPersonInfo extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -2221856064814398658L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 64)
    @Index(name = "idx_history_si_person_infos_documentNo")
    private String documentNo;

    /**
     * 单位名称
     */
    @Column(length = 64)
    private String unitName;

    /**
     * 参保单位机构代码
     */
    @Column(length = 32)
    private String orgCode;

    /**
     * 历史参保行政代码
     */
    @ManyToOne
    private AreaCode areaCode;

    /**
     * 历史参保类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private UnitType unitType;

    /**
     * 历史参保开始月份，离当前较近的月份，YYYYMM
     */
    @Temporal(TemporalType.DATE)
    private Date startDate;
    /**
     * 历史参保开始月份，离当前较近的月份，YYYYMM
     */
    @Temporal(TemporalType.DATE)
    private Date endDate;

    /**
     * 在historyStartDate和historyEndDate时间段内各月的参保情况
     * （各月参保情况用分号分割），0表示未参保，1表示参保并缴费，
     * 9 表示未知。
     */
    @Column(length = 128)
    private String insuranceState;

    /**
     * 个人社保信息主表
     */
    @ManyToOne(optional = true)
    private GdSiPersonInfo gdSiPersonInfo;

    public String getDocumentNo() {
        return this.documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getUnitName() {
        return this.unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public AreaCode getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(AreaCode areaCode) {
        this.areaCode = areaCode;
    }

    public UnitType getUnitType() {
        return this.unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getInsuranceState() {
        return this.insuranceState;
    }

    public void setInsuranceState(String insuranceState) {
        this.insuranceState = insuranceState;
    }

    public GdSiPersonInfo getGdSiPersonInfo() {
        return this.gdSiPersonInfo;
    }

    public void setGdSiPersonInfo(GdSiPersonInfo gdSiPersonInfo) {
        this.gdSiPersonInfo = gdSiPersonInfo;
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
        return "HistorySiPersonInfo [unitName=" + this.unitName + ", orgCode="
            + this.orgCode + ", areaCode="
            + (this.areaCode == null ? "" : this.areaCode.toString())
            + ", unitType=" + this.unitType + ", startDate=" + this.startDate
            + ", endDate=" + this.endDate + ", insuranceState="
            + this.insuranceState + "]";
    }

}
