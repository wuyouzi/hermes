/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.pengyuan;

import java.math.BigDecimal;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.enums.TreatResult;

/**
 * 企业股东信息
 *
 * @author liuqianqian
 */
@Entity
@Table(name = "company_shareholder_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CompanyShareholderInfo extends BaseModel<Integer> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2024175008107149644L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 主表 company_infos
     */
    @Column(name="companyInfos_id")
    private Integer companyInfos_id;

    /**
     * 股东名称
     */
    @Column(length = 512)
    private String name;

    /**
     * 认缴出资额(万元)
     */
    @Column(precision = 16, scale = 2)
    private BigDecimal contributiveFund = BigDecimal.ZERO;

    /**
     * 出资比例(%)
     */
    @Column(precision = 16, scale = 2)
    private BigDecimal contributivePercent = BigDecimal.ZERO;

    /**
     * 币种
     */
    @ManyToOne(optional = true)
    private CurrencyInfo fundCurrency;

    /**
     * 出资日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "contributiveDate")
    private Date contributiveDate;

    /**
     * 子报告ID
     */
    @ManyToOne(optional = true)
    private SubReportInfo subReportType;

    /**
     * 子报告查询状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private TreatResult treatResult;

    /**
     * 错误信息
     */
    @Column(length = 128)
    private String errorMessage;

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

    public Integer getCompanyInfos_id() {
        return this.companyInfos_id;
    }

    public void setCompanyInfos_id(Integer companyInfos_id) {
        this.companyInfos_id = companyInfos_id;
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
     * @return the contributiveFund
     */
    public BigDecimal getContributiveFund() {
        return this.contributiveFund;
    }

    /**
     * @param contributiveFund
     *        the contributiveFund to set
     */
    public void setContributiveFund(BigDecimal contributiveFund) {
        this.contributiveFund = contributiveFund;
    }

    /**
     * @return the contributivePercent
     */
    public BigDecimal getContributivePercent() {
        return this.contributivePercent;
    }

    /**
     * @param contributivePercent
     *        the contributivePercent to set
     */
    public void setContributivePercent(BigDecimal contributivePercent) {
        this.contributivePercent = contributivePercent;
    }

    /**
     * @return the fundCurrency
     */
    public CurrencyInfo getFundCurrency() {
        return this.fundCurrency;
    }

    /**
     * @param fundCurrency
     *        the fundCurrency to set
     */
    public void setFundCurrency(CurrencyInfo fundCurrency) {
        this.fundCurrency = fundCurrency;
    }

    /**
     * @return the contributiveDate
     */
    public Date getContributiveDate() {
        return this.contributiveDate;
    }

    /**
     * @param contributiveDate
     *        the contributiveDate to set
     */
    public void setContributiveDate(Date contributiveDate) {
        this.contributiveDate = contributiveDate;
    }

    /**
     * @return the subReportType
     */
    public SubReportInfo getSubReportType() {
        return this.subReportType;
    }

    /**
     * @param subReportType
     *        the subReportType to set
     */
    public void setSubReportType(SubReportInfo subReportType) {
        this.subReportType = subReportType;
    }

    /**
     * @return the treatResult
     */
    public TreatResult getTreatResult() {
        return this.treatResult;
    }

    /**
     * @param treatResult
     *        the treatResult to set
     */
    public void setTreatResult(TreatResult treatResult) {
        this.treatResult = treatResult;
    }

    /**
     * @return String
     */
    @Transient
    public String getTreatResultString() {
        return this.treatResult == null ? "" : this.treatResult.toString();
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * @param errorMessage
     *        the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
