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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.enums.TreatResult;

/**
 * 企业法院被执行信息
 *
 * @author liuqianqian
 */
@Entity
@Table(name = "company_court_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CompanyCourtInfo extends BaseModel<Integer> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1654848586398629377L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 主表 company_infos
     */
    /*@ManyToOne(optional = false)
    private CompanyInfo companyInfos;*/

    /**
     * 被执行人名称
     */
    @Column(length = 512)
    private String corpName;

    /**
     * 币种
     */
    @ManyToOne(optional = true)
    private CurrencyInfo fundCurrency;

    /**
     * 执行标的
     */
    @Column(length = 64)
    private String belongings;

    /**
     * 执行法院
     */
    @Column(length = 256)
    private String execCourt;

    /**
     * 案件状态
     */
    @Column(length = 64)
    private String status;

    /**
     * 案号
     */
    @Column(length = 256)
    private String recordNo;

    /**
     * 立案时间
     */
    @Temporal(TemporalType.DATE)
    private Date recordTime;

    /**
     * 信息获取日期
     */
    @Temporal(TemporalType.DATE)
    private Date infoDate;

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
    
    @Column(name="companyInfos_id")
    private Integer companyInfos_id;
    

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
     * @return the belongings
     */
    public String getBelongings() {
        return this.belongings;
    }

    /**
     * @param belongings
     *        the belongings to set
     */
    public void setBelongings(String belongings) {
        this.belongings = belongings;
    }

    /**
     * @return the execCourt
     */
    public String getExecCourt() {
        return this.execCourt;
    }

    /**
     * @param execCourt
     *        the execCourt to set
     */
    public void setExecCourt(String execCourt) {
        this.execCourt = execCourt;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * @param status
     *        the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the recordNo
     */
    public String getRecordNo() {
        return this.recordNo;
    }

    /**
     * @param recordNo
     *        the recordNo to set
     */
    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    /**
     * @return the recordTime
     */
    public Date getRecordTime() {
        return this.recordTime;
    }

    /**
     * @param recordTime
     *        the recordTime to set
     */
    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    /**
     * @return the infoDate
     */
    public Date getInfoDate() {
        return this.infoDate;
    }

    /**
     * @param infoDate
     *        the infoDate to set
     */
    public void setInfoDate(Date infoDate) {
        this.infoDate = infoDate;
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
