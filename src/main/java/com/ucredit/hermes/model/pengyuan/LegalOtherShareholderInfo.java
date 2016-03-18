/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.pengyuan;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.enums.CompanyStatus;
import com.ucredit.hermes.enums.TreatResult;

/**
 * 法定代表人股权投资信息
 *
 * @author liuqianqian
 */
@Entity
@Table(name = "legal_other_shareholder_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LegalOtherShareholderInfo extends BaseModel<Integer> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8496164345111540770L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="companyInfos_id")
    private Integer companyInfos_id;

    /**
     * 被投资机构名称
     */
    @Column(length = 512)
    private String corpName;

    /**
     * 工商注册号
     */
    @Column(length = 64)
    private String registerNo;

    /**
     * 企业类型
     */
    @ManyToOne(optional = true)
    private CorpTypeInfo corpType;

    /**
     * 注册资金
     */
    @Column(precision = 16, scale = 2)
    private BigDecimal registFund = BigDecimal.ZERO;

    /**
     * 企业当前状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private CompanyStatus status;

    /**
     * 工商登记机关
     */
    @Column(length = 256)
    private String registerDepartment;

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
     * @return the corpName
     */
    public String getCorpName() {
        return this.corpName;
    }

    /**
     * @param corpName
     *        the corpName to set
     */
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    /**
     * @return the registerNo
     */
    public String getRegisterNo() {
        return this.registerNo;
    }

    /**
     * @param registerNo
     *        the registerNo to set
     */
    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    /**
     * @return the corpType
     */
    public CorpTypeInfo getCorpType() {
        return this.corpType;
    }

    /**
     * @param corpType
     *        the corpType to set
     */
    public void setCorpType(CorpTypeInfo corpType) {
        this.corpType = corpType;
    }

    /**
     * @return the registFund
     */
    public BigDecimal getRegistFund() {
        return this.registFund;
    }

    /**
     * @param registFund
     *        the registFund to set
     */
    public void setRegistFund(BigDecimal registFund) {
        this.registFund = registFund;
    }

    /**
     * @return the status
     */
    public CompanyStatus getStatus() {
        return this.status;
    }

    /**
     * @param status
     *        the status to set
     */
    public void setStatus(CompanyStatus status) {
        this.status = status;
    }

    /**
     * @return String
     */
    @Transient
    public String getStatusString() {
        return this.status == null ? "" : this.status.toString();
    }

    /**
     * @return the registerDepartment
     */
    public String getRegisterDepartment() {
        return this.registerDepartment;
    }

    /**
     * @param registerDepartment
     *        the registerDepartment to set
     */
    public void setRegisterDepartment(String registerDepartment) {
        this.registerDepartment = registerDepartment;
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
